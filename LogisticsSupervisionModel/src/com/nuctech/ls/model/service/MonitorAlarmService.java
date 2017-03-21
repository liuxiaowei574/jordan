/**
 * 
 */
package com.nuctech.ls.model.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.dao.CommonPatrolDao;
import com.nuctech.ls.model.dao.MonitorAlarmDao;
import com.nuctech.ls.model.dao.MonitorTripDao;
import com.nuctech.ls.model.dao.SystemDepartmentDao;
import com.nuctech.ls.model.dao.SystemUserDao;
import com.nuctech.ls.model.util.AlarmDealType;
import com.nuctech.ls.model.util.AlarmManualType;
import com.nuctech.ls.model.util.AlarmType;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.monitor.ViewAlarmReportVO;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author sunming
 *
 */
@Service
@Transactional
public class MonitorAlarmService extends LSBaseService {

    private final static String ORDER_BY = " /~ order by [sortColumns] ~/ ";

    @Resource
    public MonitorAlarmDao monitorAlarmDao;

    @Resource
    public MonitorTripDao monitorTripDao;

    @Resource
    public CommonPatrolDao commonPatrolDao;

    @Resource
    public SystemUserDao systemUserDao;
    @Resource
    private SystemModules systemModules;
    @Resource
    public SystemDepartmentDao systemDepartmentDao;

    /**
     * 添加
     * 
     * @param entity
     */
    public LsMonitorAlarmBO saveAlarm(AlarmType alarmType, LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO,
            String content) {

        LsMonitorAlarmBO lsMonitorAlarmBO = new LsMonitorAlarmBO();
        lsMonitorAlarmBO.setAlarmId(generatePrimaryKey());
        lsMonitorAlarmBO.setTripId(lsMonitorVehicleStatusBO.getTripId());
        lsMonitorAlarmBO.setAlarmLatitude(lsMonitorVehicleStatusBO.getLatitude());
        lsMonitorAlarmBO.setAlarmLongitude(lsMonitorVehicleStatusBO.getLongitude());
        lsMonitorAlarmBO.setAlarmTypeId(alarmType.getAlarmType());
        lsMonitorAlarmBO.setAlarmTime(new Date());
        lsMonitorAlarmBO.setAlarmContent(content);
        lsMonitorAlarmBO.setIsManual(AlarmManualType.Unmanual.getAlarmType());
        lsMonitorAlarmBO.setAlarmStatus(AlarmDealType.Undeal.getText());
        lsMonitorAlarmBO.setVehicleId(lsMonitorVehicleStatusBO.getVehicleId());

        monitorAlarmDao.persist(lsMonitorAlarmBO);

        return lsMonitorAlarmBO;
    }

    /**
     * 添加报警信息
     */

    public void addAlarm(LsMonitorAlarmBO lsMonitorAlarmBO) {
        monitorAlarmDao.addMonitorAlarm(lsMonitorAlarmBO);
    }

    /**
     * 查询报警信息及从属信息列表
     * 
     * @param pageQuery
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findAlarmList(PageQuery<Map> pageQuery) {
    	HttpSession session = ServletActionContext.getRequest().getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
        String organizationId = sessionUser.getOrganizationId();
    	String queryString = "";
    	queryString = "select t from ViewAlarmReportVO t" + " where 1=1 ";
    	//口岸用户只能看到从该口岸检入/检出的行程报告
		 if(sessionUser.getRoleId().equals(RoleType.portUser.getType())){
	         queryString += "and '"+organizationId+"' in (t.checkinPort ,t.checkoutPort)"; 
	     }
		 queryString += "/~ and t.vehiclePlateNumber like '%[vehiclePlateNumber]%' ~/"
	                + "/~ and t.trackingDeviceNumber like '%[trackingDeviceNumber]%' ~/"
	                + "/~ and t.declarationNumber like '%[declarationNumber]%' ~/"
	                + "/~ and t.alarmStatus = '[alarmStatus]' ~/" + "/~ and t.alarmLevelId = '[alarmLevel]' ~/"
	                + "/~ and t.alarmTypeId = '[alarmType]' ~/" + "/~ and t.alarmTime >= '[alarmStartTime]' ~/"
	                + "/~ and t.alarmTime <= '[alarmEndTime]' ~/";
        Map<String, Object> filtersMap = pageQuery.getFilters();
        if (filtersMap != null && filtersMap.get("userId") != null) {
            String userId = (String) filtersMap.get("userId");
            if (NuctechUtil.isNotNull(userId)) {
                String inCondition = joinInCondition("t.userId", userId);
                queryString += inCondition;
            }
        }
        queryString += ORDER_BY;
        PageList<ViewAlarmReportVO> pageList = monitorAlarmDao.pageQuery(queryString, pageQuery);
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(pageList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    public List<ViewAlarmReportVO> findAlarmList() {
        return monitorAlarmDao.findAlarmList();
    }

    /**
     * 查询违规报告列表
     * 
     * @return
     */
    public List<?> findStatic() {
        return monitorAlarmDao.findStatic();
    }

    /**
     * 拼接带in的条件语句：value1,value2,value3 --> and xxx in ('xxx','xxx')
     * 
     * @param columnName
     *        字段名
     * @param value
     *        值（value1,value2,value3...）
     * @return
     */
    private String joinInCondition(String columnName, String value) {
        StringBuffer inCondition = new StringBuffer();
        String[] values = filterStr(value).split("\\s*,\\s*");
        if (values != null && values.length > 0) {
            inCondition.append(" and ").append(columnName).append(" in ('").append(value.replaceAll("\\s*,\\s*", "','"))
                    .append("')");
        }
        return inCondition.toString();
    }

    /**
     * 过滤查询条件的特殊字符
     * 
     * @param str
     */
    protected String filterStr(String str) {
        return str.replaceAll("\\[", "[[]").replaceAll("\\^", "[^]").replaceAll("_", "[_]").replaceAll("%", "[%]");
    }

    /**
     * 根据行程Id获取该行程的所有报警
     * 
     * @param pageQuery
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<ViewAlarmReportVO> findListByTripId(PageQuery pageQuery) {
        // return monitorAlarmDao.findAllBy("tripId", tripId);
        String queryString = "select t from ViewAlarmReportVO t" + " where 1=1 " + "/~ and t.tripId = '[tripId]' ~/"
                + " order by alarmLevelCode desc, t.alarmTime desc ";
        PageList<ViewAlarmReportVO> pageList = monitorAlarmDao.pageQuery(queryString, pageQuery);
        return pageList;
    }

    public List<LsMonitorAlarmBO> findLsMonitorAlarmByTripId(String tripId) {
        return monitorAlarmDao.findLsMonitorAlarmByTripId(tripId);
    }

    public List<LsMonitorAlarmBO> findAlarmsByTripIdAndStatus(String tripId) {
        return monitorAlarmDao.findAlarmsByTripIdAndStatus(tripId);
    }

    public List<ViewAlarmReportVO> findAlarmVOsByTripIdAndStatus(String tripId) {
        return monitorAlarmDao.findAlarmVOsByTripIdAndStatus(tripId);
    }
    
    /**
     * 查询某个行程的所有报警的简单对象
     * @param tripId
     * @return
     */
    public JSONArray findSimpleAlarmsByTripId(String tripId) {
    	return monitorAlarmDao.findSimpleAlarmsByTripId(tripId);
    }

    public List<ViewAlarmReportVO> findAlarmVOByTripIdAndStatus(String tripId) {
        return monitorAlarmDao.findAlarmVOByTripIdAndStatus(tripId);
    }

    public LsMonitorAlarmBO findMonitorAlarmByAlarmId(String alarmId) {
        return monitorAlarmDao.findById(alarmId);
    }

    public void updateMonitorAlarm(LsMonitorAlarmBO lsMonitorAlarmBO) {
        monitorAlarmDao.update(lsMonitorAlarmBO);
    }

    /**
     * 为报警查找负责人<br>
     * 1) 如果当前车辆在当前行程中，已推送过报警给某巡逻队（可能多个），则选择最近一次推送的巡逻队，以后报警都推送给它<br>
     * 2) 如果当前行程有护送巡逻队（可能多个），则选择护送巡逻队中当前负责的报警数最少的进行推送<br>
     * 3) 如果没有护送巡逻队，则转到中心用户，查找有处理报警权限的且当前负责的报警数量最少的进行推送<br>
     * 
     * @param lsMonitorAlarmBO
     */
    public String selectChargeUserId(LsMonitorAlarmBO lsMonitorAlarmBO) {
        String tripId = lsMonitorAlarmBO.getTripId();
        // 如果车载台和巡逻队模块开启
        if (systemModules.isPatrolOn()) {
            // 查找所有巡逻队用户
            List<SessionUser> users = systemUserDao.findAllPatrolUser();
            if (NuctechUtil.isNotNull(users) && users.size() > 0) {
                int size = users.size();
                String[] userIds = new String[size];
                for (int i = 0; i < size; i++) {
                    userIds[i] = users.get(i).getUserId();
                }
                // 查找在此次行程中已经推送过的巡逻队
                LsMonitorAlarmBO monitorAlarmBO = monitorAlarmDao.findLatestPatrol(tripId,
                        lsMonitorAlarmBO.getVehicleId(), userIds);
                if (NuctechUtil.isNotNull(monitorAlarmBO)) {
                    return monitorAlarmBO.getUserId();
                }
                // 查找该行程中负责所有车辆报警数最少的处理人（护送巡逻队）
                String userId = monitorAlarmDao.findUserIdByAlarmCount(tripId, userIds);
                if (NuctechUtil.isNotNull(userId)) {
                    return userId;
                }
                // 查找该行程的护送巡逻队，随机选择一支
                List<LsCommonPatrolBO> patrolBOs = commonPatrolDao.findAllByTripId(tripId);
                if (NuctechUtil.isNotNull(patrolBOs) && patrolBOs.size() > 0) {
                    return getRandomPatrol(patrolBOs).getPotralUser();
                }
            }
        }

        // 如果报警推送模块开启，则查询contromRoomUser、followupUser角色；否则只查询contromRoomUser
        String[] roleTypes;
        if (systemModules.isAlarmPushOn()) {
            roleTypes = new String[] { RoleType.contromRoomUser.getType(), RoleType.followupUser.getType() };
        } else {
            roleTypes = new String[] { RoleType.contromRoomUser.getType() };
        }
        LsMonitorTripBO monitorTripBO = monitorTripDao.findById(tripId);
        String countryId = systemDepartmentDao.findCountryIdByDeptId(monitorTripBO.getCheckinPort());
        List<LsSystemUserBO> centerUsers = systemUserDao.findUserByRoleIds(countryId, roleTypes);
        if (NuctechUtil.isNotNull(centerUsers) && centerUsers.size() > 0) {
            int size = centerUsers.size();
            String[] userIds = new String[size];
            for (int i = 0; i < size; i++) {
                userIds[i] = centerUsers.get(i).getUserId();
            }
            // 查找负责报警数最少的处理人（中心人员）
            String userId = monitorAlarmDao.findUserIdByAlarmCount(lsMonitorAlarmBO.getTripId(), userIds);
            if (NuctechUtil.isNotNull(userId)) {
                return userId;
            }
        }
        return null;
    }

    /**
     * 从指定的巡逻队集合中随机选择一支
     * 
     * @param patrolBOs
     * @return
     */
    private LsCommonPatrolBO getRandomPatrol(List<LsCommonPatrolBO> patrolBOs) {
        return patrolBOs.get(new Random().nextInt(patrolBOs.size()));
    }

    /**
     * 查询指定车辆的报警
     * 
     * @param vehicleId
     * @return
     */
    public List<ViewAlarmReportVO> findAlarmListV(String vehicleId) {
        return monitorAlarmDao.findAlarmListV(vehicleId);
    }
    
    /**
     * 查询指定车辆的简单报警信息
     * 
     * @param vehicleId
     * @return
     */
    public JSONArray findSimpleAlarmList(String vehicleId) {
    	return monitorAlarmDao.findSimpleAlarmList(vehicleId);
    }

    /**
     * 查询指定报警点的报警的详细信息
     * 
     * @param vehicleId
     * @return
     */
    public List<ViewAlarmReportVO> findAlarmListA(String alarmId) {
        return monitorAlarmDao.findAlarmListA(alarmId);
    }

    /**
     * 寻找指定车辆的行程超时报警
     * 
     * @param tripId
     * @param vehicleId
     * @return
     */
    public List<LsMonitorAlarmBO> findExceedTimeAlarm(String tripId, String vehicleId) {
        return monitorAlarmDao.findExceedTimeAlarm(tripId, vehicleId);
    }

    /**
     * 寻找指定车辆的Target zoon报警
     * 
     * @param tripId
     * @param vehicleId
     * @return
     */
    public List<LsMonitorAlarmBO> findTargetZoonAlarm(String tripId, String vehicleId) {
        return monitorAlarmDao.findTargetZoonAlarm(tripId, vehicleId);
    }
    
    /**
     * 查询指定alarm id的报警
     * @param ids
     * @return
     */
    public JSONArray findAllInRange(String... ids) {
        return monitorAlarmDao.findAllInRange(ids);
    }
}
