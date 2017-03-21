package com.nuctech.ls.center.alarm.action;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Id;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;

import com.nuctech.gis.GisPoint;
import com.nuctech.gis.GisUtil;
import com.nuctech.ls.center.websocket.WebsocketService;
import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.dm.LsDmAlarmTypeBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmDealBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.bo.sla.LsVehiclePunishBo;
import com.nuctech.ls.model.bo.system.LsSystemNoticesBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseTrackUnitBO;
import com.nuctech.ls.model.memcached.MemcachedUtil;
import com.nuctech.ls.model.redis.RedisClientTemplate;
import com.nuctech.ls.model.service.AlarmLevelModifyService;
import com.nuctech.ls.model.service.CommonPatrolService;
import com.nuctech.ls.model.service.CommonPortService;
import com.nuctech.ls.model.service.CommonVehicleService;
import com.nuctech.ls.model.service.MonitorAlarmDealService;
import com.nuctech.ls.model.service.MonitorAlarmService;
import com.nuctech.ls.model.service.MonitorRouteAreaService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.MonitorVehicleStatusService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemNoticeService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.service.VehiclePunishService;
import com.nuctech.ls.model.service.WarehouseElockService;
import com.nuctech.ls.model.service.WarehouseTrackUnitService;
import com.nuctech.ls.model.util.AlarmDealMethod;
import com.nuctech.ls.model.util.AlarmDealType;
import com.nuctech.ls.model.util.DeviceStatus;
import com.nuctech.ls.model.util.NoticeType;
import com.nuctech.ls.model.vo.monitor.AlarmVehicleStatusVO;
import com.nuctech.ls.model.vo.monitor.MonitorTripVehicleVO;
import com.nuctech.ls.model.vo.monitor.PatrolVehicleStatusVO;
import com.nuctech.ls.model.vo.monitor.ViewAlarmReportVO;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.CommonStringUtil;
import com.nuctech.util.Constant;
import com.nuctech.util.MessageResourceUtil;
import com.nuctech.util.NuctechUtil;
import com.nuctech.util.RedisKey;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 报警处理弹出界面
 * 
 * @author liqingxian
 *
 */

@ParentPackage("json-default")
@Namespace("/alarmdeal")
public class MonitorAlarmDealAction extends LSBaseAction {

    private String userId;
    @Resource
    public MemcachedUtil memcachedUtil;
    private static final long serialVersionUID = 1L;
    @Resource
    private MonitorAlarmService monitoAlarmServcice;
    @Resource
    private MonitorAlarmDealService monitorAlarmDealService;
    @Resource
    private SystemUserService systemUserService;
    @Resource
    private CommonPatrolService commonPatrolService;
    @Resource
    private MonitorVehicleStatusService monitorVehicleStatusService;
    @Resource
    private SystemNoticeService systemNoticeService;
    @Resource
    private CommonVehicleService commonVehicleService;
    @Resource
    private AlarmLevelModifyService alarmTypeService;
    @Resource
    private CommonPortService commonPortService;
    @Resource
    private MonitorTripService monitorTripService;
    @Resource
    private MonitorRouteAreaService monitorRouteAreaService;
    private LsSystemNoticesBO sysNoticesBO;
    private List<LsCommonPatrolBO> comomPatrals;
    @Resource
    private VehiclePunishService vehiclePunishService;
    private LsVehiclePunishBo lsVehiclePunishBo;
    private List<LsVehiclePunishBo> vehiclePunishList = new ArrayList<LsVehiclePunishBo>();
    private String type;
    private MonitorTripVehicleVO tripVehicleVO;
    @Resource
    private SystemDepartmentService systemDepartmentService;
    @Resource
    private WarehouseTrackUnitService trackService;
    private List<LsWarehouseTrackUnitBO> unitList;
    @Resource
    private WarehouseElockService warehouseElockService;

    @Resource
    private SystemModules systemModules;
    /**
     * 报警信息Service
     */
    @Resource
    private MonitorAlarmService monitorAlarmService;
    @Resource
	private RedisClientTemplate redisClientTemplate;

    @Action(value = "alarmDealModalShow",
            results = { @Result(name = "success", location = "/monitor/alarm/alarmHandler.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String alarmDealModalShow() throws Exception {
        // 查询车辆罚款表中的所有记录
        vehiclePunishList = vehiclePunishService.findAll();
        this.lsMonitorAlarmDealBO = new LsMonitorAlarmDealBO();
        String alarmId = request.getParameter("alarmId");

        LsMonitorAlarmBO lsMonitorAlarmBO = monitoAlarmServcice.findMonitorAlarmByAlarmId(alarmId);
        if (NuctechUtil.isNotNull(lsMonitorAlarmBO)) {
            LsMonitorTripBO lsMonitorTripBO = monitorTripService.findById(lsMonitorAlarmBO.getTripId());
            if (NuctechUtil.isNotNull(lsMonitorTripBO)) {
                tripVehicleVO = new MonitorTripVehicleVO();
                BeanUtils.copyProperties(lsMonitorTripBO, tripVehicleVO);
                LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService.findById(lsMonitorAlarmBO.getVehicleId());
                if (NuctechUtil.isNotNull(lsCommonVehicleBO)) {
                    BeanUtils.copyProperties(lsCommonVehicleBO, tripVehicleVO);
                }
                if (NuctechUtil.isNotNull(tripVehicleVO.getCheckinPort())) {
                    tripVehicleVO.setCheckinPortName(
                            systemDepartmentService.findById(tripVehicleVO.getCheckinPort()).getOrganizationName());
                }
                if (NuctechUtil.isNotNull(tripVehicleVO.getCheckoutPort())) {
                    tripVehicleVO.setCheckoutPortName(
                            systemDepartmentService.findById(tripVehicleVO.getCheckoutPort()).getOrganizationName());
                }
                if (NuctechUtil.isNotNull(tripVehicleVO.getCheckinUser())) {
                    tripVehicleVO.setCheckinUserName(
                            systemUserService.findById(tripVehicleVO.getCheckinUser()).getUserName());
                }
                if (NuctechUtil.isNotNull(tripVehicleVO.getCheckoutUser())) {
                    tripVehicleVO.setCheckoutUserName(
                            systemUserService.findById(tripVehicleVO.getCheckoutUser()).getUserName());
                }
            }
        }

        if (NuctechUtil.isNotNull(tripVehicleVO.getCheckinPort())) {
            unitList = trackService.findTrackUnitsByPortId(tripVehicleVO.getCheckinPort());
        }
        return SUCCESS;
    }

    @Action(value = "addAlarmDeal", results = { @Result(name = "success", type = "json"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public void addAlarmDeal() throws Exception {
        if (NuctechUtil.isNotNull(alarmId)) {
            lsMonitorAlarmDealBO = new LsMonitorAlarmDealBO();
            lsMonitorAlarmDealBO.setAlarmId(alarmId);
            lsMonitorAlarmDealBO.setDealId(generatePrimaryKey());
            if (!"".equalsIgnoreCase(lsMonitorAlarmDealBO.getRecipientsUser())
                    || null != lsMonitorAlarmDealBO.getRecipientsUser()) {
                SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                lsMonitorAlarmDealBO.setRecipientsUser(sessionUser.getUserId());
                lsMonitorAlarmDealBO.setDealUser(sessionUser.getUserId());
            }
            // 根据罚款类型查处车辆罚款记录从而得出相应的记录id
            if (NuctechUtil.isNotNull(type)) {
                lsVehiclePunishBo = vehiclePunishService.findByPro(type);
                if (NuctechUtil.isNotNull(lsVehiclePunishBo)) {
                    lsMonitorAlarmDealBO.setVpunishId(lsVehiclePunishBo.getVpunishId());
                }
            }

            Date now = new Date();
            lsMonitorAlarmDealBO.setDealTime(now);
            lsMonitorAlarmDealBO.setReceiveTime(now);
            lsMonitorAlarmDealBO.setDealDesc(dealDesc);
            lsMonitorAlarmDealBO.setDealMethod(dealMethod);
            lsMonitorAlarmDealBO.setDealResult(AlarmDealType.Dealt.getText());
            lsMonitorAlarmDealBO.setIsPunish(isPunish);
            lsMonitorAlarmDealBO.setPunishContent(punishContent);
            monitorAlarmDealService.addAlarmDeal(lsMonitorAlarmDealBO);
            JSONObject alarmDealjson = JSONObject.fromObject(lsMonitorAlarmDealBO);
            this.alarmBO = monitoAlarmServcice.findMonitorAlarmByAlarmId(alarmId);
            if (this.alarmBO == null) {
                message = "";
            } else {
            	//将对应的报警信息从缓存删除。先执行。防止alarmBO修改属性导致redis找不到值
                ViewAlarmReportVO alarmReportVO = new ViewAlarmReportVO();
                BeanUtils.copyProperties(alarmBO, alarmReportVO);
                LsDmAlarmTypeBO alarmTypeBO = alarmTypeService.find(alarmBO.getAlarmTypeId());
                if (alarmTypeBO != null) {
                    alarmReportVO.setAlarmLevelId(alarmTypeBO.getAlarmLevelId());
                }
                LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService.findById(alarmBO.getVehicleId());
                if(NuctechUtil.isNotNull(lsCommonVehicleBO)) {
                	removeAlarm(alarmReportVO, lsCommonVehicleBO);
                }
                
                JSONObject alarmjson = JSONObject.fromObject(this.alarmBO);
                String alarmjsonStr = "";
                String dealuserId = alarmDealjson.getString("dealUser");
                LsSystemUserBO lsSystemUserBO = systemUserService.findById(dealuserId);
                String dealUser = lsSystemUserBO.getUserName();
                alarmDealjson.put("dealName", dealUser);
                alarmjsonStr = alarmjson.toString();
                this.alarmBO.setIsPunish(isPunish);
                this.alarmBO.setPunishContent(punishContent);
                this.alarmBO.setAlarmStatus(AlarmDealType.Dealt.getText());
                this.alarmBO.setReceiveTime(now);
                // 设置报警的负责人为当前处理报警的人员
                SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                this.alarmBO.setUserId(sessionUser.getUserId());
                message = "{\"alarmDealResult\": {\"lsMonitorAlarmDealBO\": " + alarmDealjson.toString()
                        + ",\"alarmBO\": " + alarmjsonStr + " }}";
                monitoAlarmServcice.updateMonitorAlarm(this.alarmBO);
                LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO = monitorVehicleStatusService
                        .findOneByTripId(alarmBO.getTripId(), alarmBO.getVehicleId());
                if (NuctechUtil.isNotNull(lsMonitorVehicleStatusBO)) {
                    decreaseRiskStatus(lsMonitorVehicleStatusBO);
                }

                // 处理报警，降低LsCommonVehicleBO表中的riskStatus的等级
                if (NuctechUtil.isNotNull(alarmBO.getTripId())) {
                    decreaseRiskStatus(lsCommonVehicleBO, alarmBO.getAlarmTypeId());
                }
            }
            // 设置关锁状态
            String[] s_deviceDestroy = request.getParameterValues("s_deviceDestroy");
            if (null != s_deviceDestroy && s_deviceDestroy.length > 0) {
                String status = s_deviceDestroy[0];
                if ("1".equals(status)) {
                    String trackingDeviceNumber = request.getParameter("trackingDeviceNumber");
                    LsWarehouseElockBO warehouseElock = warehouseElockService.findByElockNumber(trackingDeviceNumber);
                    if (NuctechUtil.isNotNull(warehouseElock)) {
                        warehouseElock.setElockStatus(DeviceStatus.Destory.getText());
                        warehouseElockService.modify(warehouseElock);
                    }
                }
            }
            
            try {
                this.response.getWriter().println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 从缓存删除对应报警
     * @param vehicleId
     * @param tripId
     */
    private void removeAlarm(ViewAlarmReportVO alarmReportVO, LsCommonVehicleBO lsCommonVehicleBO) {
    	JSONObject obj = new JSONObject();
		obj.put("alarmId", CommonStringUtil.ifNull(alarmReportVO.getAlarmId(), ""));
		obj.put("alarmContent", CommonStringUtil.ifNull(alarmReportVO.getAlarmContent(), ""));
		obj.put("alarmTime", CommonStringUtil.ifNull(alarmReportVO.getAlarmTime(), ""));
		obj.put("alarmLongitude", CommonStringUtil.ifNull(alarmReportVO.getAlarmLongitude(), ""));
		obj.put("alarmLatitude", CommonStringUtil.ifNull(alarmReportVO.getAlarmLatitude(), ""));
		obj.put("alarmTypeCode", CommonStringUtil.ifNull(alarmReportVO.getAlarmTypeId(), ""));
		obj.put("alarmLevelCode", CommonStringUtil.ifNull(alarmReportVO.getAlarmLevelId(), ""));
		obj.put("vehiclePlateNumber", CommonStringUtil.ifNull(lsCommonVehicleBO.getVehiclePlateNumber(), ""));
		obj.put("userId", CommonStringUtil.ifNull(alarmReportVO.getUserId(), ""));
		obj.put("tripId", CommonStringUtil.ifNull(alarmReportVO.getTripId(), ""));
		String jsonStr = obj.toString();
		
		String key = RedisKey.ALARMLIST_VEHICLEID_PREFIX + lsCommonVehicleBO.getVehicleId();
		redisClientTemplate.lrem(key, 0, jsonStr);
		
		String keyTrip = RedisKey.ALARMLIST_TRIPID_PREFIX + alarmReportVO.getTripId();
		redisClientTemplate.lrem(keyTrip, 0, jsonStr);
	}

    @Action(value = "getPatrolsInCircle", results = { @Result(name = "success", type = "json"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public void getPatrolsInCircle() throws Exception {
        String result = "";
        // List<PatrolVehicleStatusVO> bos = new
        // ArrayList<PatrolVehicleStatusVO>();
        // this.comomPatrals = commonPatrolService.findAllCommonPatrol();
        // List<LsMonitorVehicleStatusBO> monitorVehicleStatusBOs = new
        // ArrayList<LsMonitorVehicleStatusBO>();
        // for (Iterator<LsCommonPatrolBO> iterator =
        // comomPatrals.iterator();iterator.hasNext();) {
        // LsCommonPatrolBO lsCommonPatrolBO = (LsCommonPatrolBO)
        // iterator.next();
        // LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO
        // =(LsMonitorVehicleStatusBO)
        // memcachedUtil.get(lsCommonPatrolBO.getTrackUnitNumber());
        // GisPoint point = new
        // GisPoint(Double.valueOf(lsMonitorVehicleStatusBO.getLongitude()),Double.parseDouble(lsMonitorVehicleStatusBO.getLatitude()));
        // GisPoint center = new
        // GisPoint(Double.valueOf(alarmLongitude),Double.valueOf(alarmLatitude));
        // if(GisUtil.isPointInCircle(point, center,
        // 10000*Double.valueOf(radius)))
        // {
        // PatrolVehicleStatusVO patrolVehicleStatusVO = new
        // PatrolVehicleStatusVO();
        // monitorVehicleStatusBOs.add(lsMonitorVehicleStatusBO);
        // patrolVehicleStatusVO.setPatrolId(lsCommonPatrolBO.getPatrolId());
        // patrolVehicleStatusVO.setPotralUser(lsCommonPatrolBO.getPotralUser());
        // patrolVehicleStatusVO.setLatitude(lsMonitorVehicleStatusBO.getLatitude());
        // patrolVehicleStatusVO.setLongitude(lsMonitorVehicleStatusBO.getLongitude());
        // bos.add(patrolVehicleStatusVO);
        // }
        // result = JSONObject.fromObject(monitorVehicleStatusBOs).toString();
        // }

        /**
         * 由于memcached暂时未连上，暂时以查数据库方式查询
         */
        List<LsMonitorVehicleStatusBO> monitorVehicleStatusBOs = monitorVehicleStatusService
                .findAllOnWayVehicleStatus("1");
        List<PatrolVehicleStatusVO> bos = new ArrayList<PatrolVehicleStatusVO>();
        for (LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO : monitorVehicleStatusBOs) {
            GisPoint point = new GisPoint(Double.valueOf(lsMonitorVehicleStatusBO.getLongitude()),
                    Double.parseDouble(lsMonitorVehicleStatusBO.getLatitude()));
            GisPoint center = new GisPoint(Double.valueOf(alarmLongitude), Double.valueOf(alarmLatitude));
            if (GisUtil.isPointInCircle(point, center, Double.valueOf(radius))) {
                PatrolVehicleStatusVO patrolVehicleStatusVO = new PatrolVehicleStatusVO();
                LsCommonPatrolBO commonPatrolBO = commonPatrolService
                        .findCommonPatrolByTrackUnitNumber(lsMonitorVehicleStatusBO.getTrackingDeviceNumber());
                if (commonPatrolBO == null) {
                } else {
                    patrolVehicleStatusVO.setPatrolId(commonPatrolBO.getPatrolId());
                    patrolVehicleStatusVO
                            .setPotralUser(systemUserService.findById(commonPatrolBO.getPotralUser()).getUserName());
                    patrolVehicleStatusVO.setLatitude(lsMonitorVehicleStatusBO.getLatitude());
                    patrolVehicleStatusVO.setLongitude(lsMonitorVehicleStatusBO.getLongitude());
                    patrolVehicleStatusVO.setTrackUnitNumber(lsMonitorVehicleStatusBO.getTrackingDeviceNumber());
                    // LsSystemDepartmentBO lsCommonPortBO = commonPortService
                    // .findPortById(commonPatrolBO.getBelongToPort());
                    LsMonitorRouteAreaBO monitorRouteAreaBO = monitorRouteAreaService
                            .findMonitorRouteAreaById(commonPatrolBO.getBelongToArea());
                    if (null == monitorRouteAreaBO) {

                    } else {
                        patrolVehicleStatusVO.setBelongToArea(monitorRouteAreaBO.getRouteAreaName());
                    }
                    // patrolVehicleStatusVO.setBelongToPort(lsCommonPortBO.getOrganizationName());

                    patrolVehicleStatusVO.setUserId(userId);

                    bos.add(patrolVehicleStatusVO);
                }

            }
        }
        result = JSONArray.fromObject(bos).toString();
        this.response.getWriter().println(result);
    }

    private static final String DEFAULT_SORT_COLUMNS_WITH_ALIAS = "t.checkinTime DESC";

    @Action(value = "patrolAlarmDeal", results = { @Result(name = "success", type = "json"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public void patrolAlarmDeal() throws Exception {
        if (!"".equalsIgnoreCase(alarmId)) {
            lsMonitorAlarmDealBO = new LsMonitorAlarmDealBO();
            lsMonitorAlarmDealBO.setAlarmId(alarmId);
            lsMonitorAlarmDealBO.setDealId(generatePrimaryKey());
            LsCommonPatrolBO commonPatrolBO = commonPatrolService.findCommonPatrolById(patrolId);
            if (!"".equalsIgnoreCase(lsMonitorAlarmDealBO.getRecipientsUser())
                    || null != lsMonitorAlarmDealBO.getRecipientsUser()) {
                SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);

                lsMonitorAlarmDealBO.setRecipientsUser(commonPatrolBO.getPotralUser());
                lsMonitorAlarmDealBO.setDealUser(sessionUser.getUserId());
            }
            lsMonitorAlarmDealBO.setDealTime(new Date());
            lsMonitorAlarmDealBO.setDealDesc(dealDesc);
            lsMonitorAlarmDealBO.setReceiveTime(new Date());
            // 推送属于转发。
            lsMonitorAlarmDealBO.setDealMethod(AlarmDealMethod.Forward.getText());
            lsMonitorAlarmDealBO.setDealResult(dealResult);
            lsMonitorAlarmDealBO.setIsPunish(isPunish);
            lsMonitorAlarmDealBO.setPunishContent(punishContent);
            // String alarmJson =
            // JSONObject.fromObject(lsMonitorAlarmDealBO).toString();

            /**
             * 由于memcached暂时未连上，暂时以查数据库方式查询
             */
            // LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO =
            // (LsMonitorVehicleStatusBO)
            // memcachedUtil.get(commonPatrolBO.getTrackUnitNumber());
            LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO = monitorVehicleStatusService
                    .findPatrolByNumber(commonPatrolBO.getTrackUnitNumber(), commonPatrolBO.getTripId());
            LsMonitorAlarmBO alarmBO = monitoAlarmServcice.findMonitorAlarmByAlarmId(alarmId);
            pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
            pageQuery.setPageSize(1);
            pageQuery.setPage(1);

            MonitorTripVehicleVO tripVehicleVO = monitorTripService.findOneTripVehicleAlarm(pageQuery);
            String alarmDealJson = JSONObject.fromObject(lsMonitorAlarmDealBO).toString();
            /**
             * 添加报警处理信息
             */
            monitorAlarmDealService.addAlarmDeal(lsMonitorAlarmDealBO);
            /**
             * 构建前端返回信息
             */
            AlarmVehicleStatusVO alarmVehicleStatusVO = new AlarmVehicleStatusVO();
            alarmVehicleStatusVO.setLsMonitorAlarmBO(alarmBO);
            alarmBO.setAlarmStatus(AlarmDealType.Dealing.getText());
            // 转发后，负责人改为转发给的用户
            alarmBO.setUserId(commonPatrolBO.getPotralUser());
            monitoAlarmServcice.updateMonitorAlarm(alarmBO);

            alarmVehicleStatusVO.setLsMonitorVehicleStatusBO(lsMonitorVehicleStatusBO);

            ViewAlarmReportVO alarmReportVO = new ViewAlarmReportVO();
            BeanUtils.copyProperties(alarmBO, alarmReportVO);
            LsDmAlarmTypeBO alarmTypeBO = alarmTypeService.find(alarmBO.getAlarmTypeId());
            if (alarmTypeBO != null) {
                alarmReportVO.setAlarmLevelId(alarmTypeBO.getAlarmLevelId());
            }
            // String alarmJson = JSONObject.fromObject(alarmBO).toString();
            String alarmJson = JSONObject.fromObject(alarmReportVO).toString();

            // String alarmVehicleJson = JSONObject.fromObject(alarmVehicleStatusVO).toString();
            // LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService
            // .findById(alarmVehicleStatusVO.getLsMonitorVehicleStatusBO().getVehicleId());

            /**
             * 报警信息推送处理
             */
            sysNoticesBO = createNotice(tripVehicleVO, alarmBO, commonPatrolBO.getPotralUser());
            systemNoticeService.addNotice(sysNoticesBO);

            JSONObject json = new JSONObject();
            json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_ALARMHANDLE);
            json.put("noticeId", sysNoticesBO.getNoticeId());
            json.put("title", sysNoticesBO.getNoticeTitle());
            json.put("content", sysNoticesBO.getNoticeContent());
            json.put("receiveUser", sysNoticesBO.getNoticeUsers());
            WebsocketService.sendMessage(json.toString());
            /// WebsocketService.sendMessage(alarmVehicleJson);
            String result = "{\"alarmDealResult\": {\"lsMonitorAlarmDealBO\": " + alarmDealJson + ",\"alarmReportVO\": "
                    + alarmJson + " }}";
            this.response.getWriter().println(result);
        } else {
        }

    }

    private LsSystemNoticesBO createNotice(MonitorTripVehicleVO tripVehicleVO, LsMonitorAlarmBO lsMonitorAlarmBO,
            String reciveUser) {
        sysNoticesBO = new LsSystemNoticesBO();
        sysNoticesBO.setNoticeId(generatePrimaryKey());
        String title = MessageResourceUtil.getMessageInfo("alarm.label.alarmDeal.msgSendTitle");
        String noticeContent = MessageResourceUtil.getMessageInfo("alarm.label.alarmDeal.noticeContent");
        sysNoticesBO.setNoticeTitle(title);
        String alarmType = MessageResourceUtil.getMessageInfo("AlarmType." + lsMonitorAlarmBO.getAlarmTypeId());
        LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService.findById(lsMonitorAlarmBO.getVehicleId());
        noticeContent = MessageFormat.format(noticeContent,
                lsCommonVehicleBO != null ? lsCommonVehicleBO.getVehiclePlateNumber() : "",
                lsMonitorAlarmBO.getAlarmTime(), alarmType);
        sysNoticesBO.setDeployTime(new Date());
        sysNoticesBO.setNoticeContent(noticeContent);
        sysNoticesBO.setNoticeState(Constant.NOTICE_STATE_FINISH);
        sysNoticesBO.setNoticeType(NoticeType.AlarmNotice.getType());
        sysNoticesBO.setNoticeUsers(reciveUser);
        return sysNoticesBO;
    }

    @Action(value = "getPatrolLocation", results = { @Result(name = "success", type = "json"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String getPatrolLocation() throws Exception {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        if (null != sessionUser) {
            commonPatrolBO = commonPatrolService.findCommonPatrolByPatrolUser(sessionUser.getUserId());

            if (null != commonPatrolBO) {
                String trackingDeviceNumber = commonPatrolBO.getTrackUnitNumber();
                lsMonitorVehicleStatusBO = monitorVehicleStatusService.findLatestPatrolByNumber(trackingDeviceNumber);
                if (lsMonitorVehicleStatusBO != null) {
                    patrolLatitude = lsMonitorVehicleStatusBO.getLatitude();
                    patrolLongitude = lsMonitorVehicleStatusBO.getLongitude();
                    return SUCCESS;
                }
            }

        }
        return ERROR;
    }

    /**
     * 降低车辆状态表的风险级别，但不能低于该行程初始风险值
     * 
     * @param lsMonitorVehicleStatusBO
     */
    private void decreaseRiskStatus(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {
        if (NuctechUtil.isNotNull(lsMonitorVehicleStatusBO)) {
            LsCommonVehicleBO commonVehicleBO = commonVehicleService.findById(lsMonitorVehicleStatusBO.getVehicleId());
            List<LsMonitorAlarmBO> alarmboList = monitorAlarmService
                    .findAlarmsByTripIdAndStatus(commonVehicleBO.getTripId());
            // 剩余未处理+处理中的报警数大于3时，级别肯定是红色，3条以内，处理一条，报警级别降低一级
            String initStatus = commonVehicleBO.getRiskStatus();
            if (alarmboList != null && alarmboList.size() < 3) {
                String statusTrip = NuctechUtil.isNull(initStatus) ? "0" : initStatus;
                int statusTripInt = Integer.parseInt(statusTrip);

                String status = lsMonitorVehicleStatusBO.getRiskStatus();
                status = NuctechUtil.isNull(status) ? "0" : status;
                int statusInt = Integer.parseInt(status);
                statusInt = Math.max(statusInt - 1, statusTripInt);
                lsMonitorVehicleStatusBO.setRiskStatus(String.valueOf(statusInt));
                monitorVehicleStatusService.saveOrUpdate(lsMonitorVehicleStatusBO);
            }
        }
    }

    private void decreaseRiskStatus(LsCommonVehicleBO lsCommonVehicleBO, String alarmTypeId) {
        // 根据alarmType判断处理的报警为"轻微"还是"严重"
        LsDmAlarmTypeBO alarmTypeBO = alarmTypeService.find(alarmTypeId);
        if (NuctechUtil.isNotNull(lsCommonVehicleBO)) {
            List<LsMonitorAlarmBO> alarmboList = monitorAlarmService
                    .findAlarmsByTripIdAndStatus(lsCommonVehicleBO.getTripId());
            // 车辆初始等级
            String initStatus = lsCommonVehicleBO.getRiskStatus();
            // 剩余未处理+处理中的报警数大于3时，级别肯定是红色，3条以内，处理一条，报警级别降低一级
            if (alarmboList != null && alarmboList.size() < 3) {
                String statusTrip = NuctechUtil.isNull(initStatus) ? "0" : initStatus;
                int statusTripInt = Integer.parseInt(statusTrip);
                // 处理轻微报警风险等级降1，处理严重报警风险等级降2
                if (alarmTypeBO.getAlarmLevelId().equals("0")) {
                    statusTripInt = Math.max(statusTripInt - 1, 0);
                } else {
                    statusTripInt = Math.max(statusTripInt - 2, 0);
                }
                lsCommonVehicleBO.setRiskStatus(String.valueOf(statusTripInt));
                commonVehicleService.updateCommonVehicle(lsCommonVehicleBO);
            }
        }
    }

    private String patrolLatitude;
    private String patrolLongitude;

    public String getPatrolLatitude() {
        return patrolLatitude;
    }

    public void setPatrolLatitude(String patrolLatitude) {
        this.patrolLatitude = patrolLatitude;
    }

    public String getPatrolLongitude() {
        return patrolLongitude;
    }

    public void setPatrolLongitude(String patrolLongitude) {
        this.patrolLongitude = patrolLongitude;
    }

    private LsCommonPatrolBO commonPatrolBO;

    public LsCommonPatrolBO getCommonPatrolBO() {
        return commonPatrolBO;
    }

    public void setCommonPatrolBO(LsCommonPatrolBO commonPatrolBO) {
        this.commonPatrolBO = commonPatrolBO;
    }

    private String locationType;

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    /**
     * 圆的半径
     */
    private String radius;

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    private String alarmLatitude;// 报警点纬度
    private String alarmLongitude;// 报警点经度

    public String getAlarmLatitude() {
        return alarmLatitude;
    }

    public void setAlarmLatitude(String alarmLatitude) {
        this.alarmLatitude = alarmLatitude;
    }

    public String getAlarmLongitude() {
        return alarmLongitude;
    }

    public void setAlarmLongitude(String alarmLongitude) {
        this.alarmLongitude = alarmLongitude;
    }

    private LsMonitorAlarmBO alarmBO;

    public LsMonitorAlarmBO getAlarmBO() {
        return alarmBO;
    }

    public void setAlarmBO(LsMonitorAlarmBO alarmBO) {
        this.alarmBO = alarmBO;
    }

    private LsMonitorAlarmDealBO lsMonitorAlarmDealBO;

    public LsMonitorAlarmDealBO getLsMonitorAlarmDealBO() {
        return lsMonitorAlarmDealBO;
    }

    public void setLsMonitorAlarmDealBO(LsMonitorAlarmDealBO lsMonitorAlarmDealBO) {
        this.lsMonitorAlarmDealBO = lsMonitorAlarmDealBO;
    }

    private LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO;

    public LsMonitorVehicleStatusBO getLsMonitorVehicleStatusBO() {
        return lsMonitorVehicleStatusBO;
    }

    public void setLsMonitorVehicleStatusBO(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {
        this.lsMonitorVehicleStatusBO = lsMonitorVehicleStatusBO;
    }

    /**
     * 巡逻队Id
     */
    private String patrolId;

    /* 报警处理主键 */
    private String dealId;

    /* 报警主键 */
    private String alarmId;

    /* 接收人 */
    private String recipientsUser;

    /*
     * 接收时间 只记录最新人员的接收时间
     */
    private Date receiveTime;

    /* 处理人 */
    private String dealUser;

    /*
     * 处理方式 0-转发 1-处理
     */
    private String dealMethod;

    /* 处理时间 */
    private Date dealTime;

    /* 处理结果 */
    private String dealResult;

    /* 备注 */
    private String dealDesc;

    public String getPatrolId() {
        return patrolId;
    }

    public void setPatrolId(String patrolId) {
        this.patrolId = patrolId;
    }

    @Id
    @Column(name = "DEAL_ID", nullable = false, length = 50)
    public String getDealId() {
        return this.dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    @Column(name = "ALARM_ID", nullable = true, length = 50)
    public String getAlarmId() {
        return this.alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    @Column(name = "RECIPIENTS_USER", nullable = true, length = 50)
    public String getRecipientsUser() {
        return this.recipientsUser;
    }

    public void setRecipientsUser(String recipientsUser) {
        this.recipientsUser = recipientsUser;
    }

    @Column(name = "RECEIVE_TIME", nullable = true)
    public Date getReceiveTime() {
        return this.receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Column(name = "DEAL_USER", nullable = true, length = 50)
    public String getDealUser() {
        return this.dealUser;
    }

    public void setDealUser(String dealUser) {
        this.dealUser = dealUser;
    }

    @Column(name = "DEAL_METHOD", nullable = true, length = 2)
    public String getDealMethod() {
        return this.dealMethod;
    }

    public void setDealMethod(String dealMethod) {
        this.dealMethod = dealMethod;
    }

    @Column(name = "DEAL_TIME", nullable = true)
    public Date getDealTime() {
        return this.dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    @Column(name = "DEAL_RESULT", nullable = true, length = 2)
    public String getDealResult() {
        return this.dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    @Column(name = "DEAL_DESC", nullable = true, length = 200)
    public String getDealDesc() {
        return this.dealDesc;
    }

    public void setDealDesc(String dealDesc) {
        this.dealDesc = dealDesc;
    }

    /* 是否罚款 */
    private String isPunish;

    /* 罚款内容 */
    private String punishContent;

    public String getIsPunish() {
        return isPunish;
    }

    public void setIsPunish(String isPunish) {
        this.isPunish = isPunish;
    }

    public String getPunishContent() {
        return punishContent;
    }

    public void setPunishContent(String punishContent) {
        this.punishContent = punishContent;
    }

    public LsVehiclePunishBo getLsVehiclePunishBo() {
        return lsVehiclePunishBo;
    }

    public void setLsVehiclePunishBo(LsVehiclePunishBo lsVehiclePunishBo) {
        this.lsVehiclePunishBo = lsVehiclePunishBo;
    }

    public List<LsVehiclePunishBo> getVehiclePunishList() {
        return vehiclePunishList;
    }

    public void setVehiclePunishList(List<LsVehiclePunishBo> vehiclePunishList) {
        this.vehiclePunishList = vehiclePunishList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MonitorTripVehicleVO getTripVehicleVO() {
        return tripVehicleVO;
    }

    public void setTripVehicleVO(MonitorTripVehicleVO tripVehicleVO) {
        this.tripVehicleVO = tripVehicleVO;
    }

    public List<LsWarehouseTrackUnitBO> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<LsWarehouseTrackUnitBO> unitList) {
        this.unitList = unitList;
    }

    public SystemModules getSystemModules() {
        return systemModules;
    }

    public void setSystemModules(SystemModules systemModules) {
        this.systemModules = systemModules;
    }

    /**
     * 撤销向巡逻队推送的报警信息
     * 
     * @throws Exception
     */
    @Action(value = "patrolAlarmRevoke", results = { @Result(name = "success", type = "json"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String patrolAlarmRevoke() throws Exception {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);// 获取当前用户id
        // 根据alarmId查出报警信息表，更改"当前负责人"改为撤销人即当前用户；(只能撤销未处理以及处理中的报警)
        LsMonitorAlarmBO lsMonitorAlarmBO = monitoAlarmServcice.findMonitorAlarmByAlarmId(alarmId);
        // 判断报警是否已经处理(未处理的报警可以撤销)
        if (lsMonitorAlarmBO.getAlarmStatus() != Constant.alarmStatus_dealed) {
            lsMonitorAlarmBO.setUserId(sessionUser.getUserId());
            monitoAlarmServcice.updateMonitorAlarm(lsMonitorAlarmBO);
        } else {
            return ERROR;
        }

        // 新增报警处理记录
        lsMonitorAlarmDealBO = new LsMonitorAlarmDealBO();
        lsMonitorAlarmDealBO.setAlarmId(alarmId);
        lsMonitorAlarmDealBO.setDealId(generatePrimaryKey());
        lsMonitorAlarmDealBO.setDealDesc(MessageResourceUtil.getMessageInfo("alarm.label.alarmDeal.msgRevokeTitle"));
        lsMonitorAlarmDealBO.setDealMethod(AlarmDealMethod.Revoke.getText());
        lsMonitorAlarmDealBO.setDealTime(new Date());
        lsMonitorAlarmDealBO.setIsPunish(isPunish);
        lsMonitorAlarmDealBO.setPunishContent(punishContent);
        lsMonitorAlarmDealBO.setReceiveTime(new Date());
        lsMonitorAlarmDealBO.setDealTime(new Date());
        lsMonitorAlarmDealBO.setDealResult(dealResult);
        lsMonitorAlarmDealBO.setRecipientsUser(sessionUser.getUserId());// 当前用户撤销报警，接收人就变为该当前用户
        lsMonitorAlarmDealBO.setDealUser(sessionUser.getUserId());// 处理人也为当前用户

        monitorAlarmDealService.addAlarmDeal(lsMonitorAlarmDealBO);
        return SUCCESS;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
