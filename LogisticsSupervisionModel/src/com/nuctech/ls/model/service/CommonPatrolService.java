package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.gis.GisPoint;
import com.nuctech.gis.GisUtil;
import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRaPointBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.system.LsSystemRoleBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.system.LsSystemUserRoleBO;
import com.nuctech.ls.model.dao.CommonPatrolDao;
import com.nuctech.ls.model.dao.MonitorRaPointDao;
import com.nuctech.ls.model.dao.MonitorRouteAreaDao;
import com.nuctech.ls.model.dao.SystemDepartmentDao;
import com.nuctech.ls.model.dao.SystemRoleDao;
import com.nuctech.ls.model.dao.SystemUserDao;
import com.nuctech.ls.model.dao.SystemUserRoleDao;
import com.nuctech.ls.model.dao.WarehouseElockDao;
import com.nuctech.ls.model.util.OrderType;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.monitor.PatrolUserVO;
import com.nuctech.ls.model.vo.warehouse.PatrolDepartmentVO;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service
@Transactional
public class CommonPatrolService extends LSBaseService {

    @Resource
    private WarehouseElockDao warehouseDao;
    @Resource
    private CommonPatrolDao commonPatrolDao;

    @Resource
    private SystemUserDao systemUserDao;
    @Resource
    private SystemRoleDao systemRoleDao;
    @Resource
    private SystemUserRoleDao systemUserRoleDao;
    @Resource
    private MonitorRouteAreaDao monitorRouteAreaDao;
    @Resource
    private MonitorRaPointDao monitorRaPointDao;
    @Resource
    private SystemDepartmentDao systemDepartmentDao;

    public List<LsCommonPatrolBO> findAllCommonPatrol(String organizationId) {
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("deleteMark", Constant.MARK_UN_DELETED);
        /*
         * if (NuctechUtil.isNotNull(organizationId)) {
         * propertiesMap.put("belongToPort",organizationId); }
         */
        HashMap<String, String> orderbyMap = new HashMap<String, String>();
        orderbyMap.put("createTime", "desc");
        return commonPatrolDao.findAllBy(propertiesMap, orderbyMap);
    }

    /**
     * 查询所有的巡逻队
     */
    public List<LsCommonPatrolBO> findAllCommonPatrol() {
        return commonPatrolDao.findAll();
    }

    /**
     * 查询所有的巡逻队列表
     * 
     * @return
     */
    public List<PatrolUserVO> getPatrolUserList() {
        return commonPatrolDao.getPatrolUserList();
    }

    /**
     * 查询所有的护送巡逻队列表
     * 
     * @return
     */
    public List<PatrolUserVO> getEscortPatrolUserList() {
        return commonPatrolDao.getEscortPatrolUserList();
    }

    public LsCommonPatrolBO findCommonPatrolByTrackUnitNumber(String trackUnitNumber) {
        return commonPatrolDao.findCommonPatrolByTrackUnitNumber(trackUnitNumber);
    }

    public LsCommonPatrolBO findCommonPatrolByPatrolUser(String patrolUser) {
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("potralUser", patrolUser);
        HashMap<String, String> orderby = new LinkedHashMap<String, String>();
        orderby.put("potralUser", OrderType.ASC.getText());
        return commonPatrolDao.findByProperties(propertiesMap, orderby);
    }

    public LsCommonPatrolBO findCommonPatrolById(String patrolId) {
        return commonPatrolDao.findById(patrolId);
    }

    public int delPatrolsByIds(String ids) {
        return commonPatrolDao.delPatrolsByIds(ids);
    }

    public void addPatrol(LsCommonPatrolBO lsCommonPatrolBO) {
        commonPatrolDao.persist(lsCommonPatrolBO);
    }

    public void updatePatrol(LsCommonPatrolBO lsCommonPatrolBO) {
        commonPatrolDao.merge(lsCommonPatrolBO);
    }

    /**
     * 赵苏阳
     */
    // 查询巡逻队列表
    public List<PatrolDepartmentVO> getPatrolDapatmentlist() {
        return commonPatrolDao.getPatrolList();
    }

    /**
     * 根据当前口岸坐标，查找与该坐标位于同一行政区域内的护送巡逻队列表
     * 
     * @param portId
     * @return
     */
    public List<PatrolUserVO> findCommonPatrolByPort(String portId) {
        List<PatrolUserVO> patrolUserList = new ArrayList<>();
        LsSystemRoleBO systemRoleBO = systemRoleDao.findByProperty("roleName", RoleType.escortPatrol.toString());
        List<LsSystemUserRoleBO> systemUserRoleBOs = systemUserRoleDao.getSystemUsersByRoleId(systemRoleBO.getRoleId());
        if (NuctechUtil.isNotNull(systemUserRoleBOs) && systemUserRoleBOs.size() > 0) {
            int length = systemUserRoleBOs.size();
            String[] userIds = new String[length];
            for (int i = 0; i < length; i++) {
                userIds[i] = systemUserRoleBOs.get(i).getUserId();
            }
            LsMonitorRouteAreaBO areaBO = this.findXZQYRouteAreaByPortId(portId);
            if (NuctechUtil.isNotNull(areaBO)) {
                List<LsCommonPatrolBO> patrolList = commonPatrolDao
                        .findEscortPatrolByBelongArea(areaBO.getRouteAreaId(), userIds);
                if (NuctechUtil.isNotNull(patrolList) && patrolList.size() > 0) {
                    for (LsCommonPatrolBO patrolBO : patrolList) {
                        PatrolUserVO patrolUserVO = new PatrolUserVO();
                        BeanUtils.copyProperties(patrolBO, patrolUserVO);
                        patrolUserVO.setPotralUserName(systemUserDao.findUserNameById(patrolBO.getPotralUser()));
                        patrolUserList.add(patrolUserVO);
                    }
                }
            }
        }
        return patrolUserList;
    }

    /**
     * 根据口岸Id，查找该口岸坐标所属的行政区域
     * 
     * @param portId
     * @return
     */
    public LsMonitorRouteAreaBO findXZQYRouteAreaByPortId(String portId) {
        LsSystemDepartmentBO department = systemDepartmentDao.findById(portId);
        if (NuctechUtil.isNull(department)) {
            return null;
        }
        // 一个口岸只能属于一个行政区域
        // 口岸和行政区域不能保存id映射关系，所以需要先获取所有行政区域，再循环通过判断口岸坐标是否位于某个行政区域范围内来查找。
        List<LsMonitorRouteAreaBO> monitorRouteAreaBOs = monitorRouteAreaDao
                .findAllPatrolArea(Constant.ROUTEAREA_TYPE_QYHF);
        if (NuctechUtil.isNull(monitorRouteAreaBOs) || monitorRouteAreaBOs.isEmpty()) {
            return null;
        }
        GisPoint p = new GisPoint(Double.parseDouble(department.getLongitude()),
                Double.parseDouble(department.getLatitude()));
        for (LsMonitorRouteAreaBO monitorRouteArea : monitorRouteAreaBOs) {
            List<LsMonitorRaPointBO> monitorRaPointBOs = findAllMonitorRaPointByRouteAreaId(
                    monitorRouteArea.getRouteAreaId());
            if (NuctechUtil.isNull(monitorRaPointBOs) || monitorRaPointBOs.isEmpty()) {
                return null;
            }
            List<GisPoint> points = new ArrayList<>(monitorRaPointBOs.size());
            for (LsMonitorRaPointBO point : monitorRaPointBOs) {
                points.add(new GisPoint(Double.parseDouble(point.getLongitude()),
                        Double.parseDouble(point.getLatitude())));
            }
            if (GisUtil.isPointInPolygon(p, points)) {
                return monitorRouteArea;
            }
        }
        return null;
    }

    /**
     * 根据编号获取区域或线路信息
     * 
     * @param id
     */
    public List<LsMonitorRaPointBO> findAllMonitorRaPointByRouteAreaId(String areaId) {
        HashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("gpsSeq", "asc");
        return monitorRaPointDao.findAllBy("routeAreaId", areaId, map);
    }

    /**
     * 在巡逻队管理中显示下方的列表
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {

        String queryString = "select p,r,u,t "
                + "from LsCommonPatrolBO p,LsMonitorRouteAreaBO r,LsSystemUserBO u,LsWarehouseTrackUnitBO t "
                + "where 1=1" + " and p.trackUnitNumber=t.trackUnitNumber " + " and p.potralUser=u.userId "
                + " and p.belongToArea=r.routeAreaId " + "/~ and p.trackUnitNumber like '%[trackUnitNumber]%' ~/"
                + "/~ and p.belongToArea like '%[belongToArea]%' ~/" + "/~ and p.potralUser like '%[potralUser]%' ~/"
                + "/~ order by [sortColumns] ~/";

        PageList<Object> queryList = commonPatrolDao.pageQuery(queryString, pageQuery);

        PageList<PatrolDepartmentVO> pageList = new PageList<PatrolDepartmentVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                PatrolDepartmentVO patrolDepartmentVO = new PatrolDepartmentVO();
                BeanUtils.copyProperties(objs[0], patrolDepartmentVO);
                BeanUtils.copyProperties(objs[1], patrolDepartmentVO);
                BeanUtils.copyProperties(objs[2], patrolDepartmentVO);
                BeanUtils.copyProperties(objs[3], patrolDepartmentVO);
                if (NuctechUtil.isNotNull(patrolDepartmentVO.getCreateUser())) {
                    LsSystemUserBO systemUser = systemUserDao.findById(patrolDepartmentVO.getCreateUser());
                    if (NuctechUtil.isNotNull(systemUser)) {
                        patrolDepartmentVO.setCreateUserName(systemUser.getUserName());
                    }
                }
                pageList.add(patrolDepartmentVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 保存巡逻队
     */
    public void add(LsCommonPatrolBO lsCommonPatrolBO) {
        commonPatrolDao.save(lsCommonPatrolBO);
    }

    /**
     * 根据id删除巡逻队
     */
    public void deleteById(String id) {
        commonPatrolDao.deleteById(id);
    }

    /**
     * 根据行程Id查找关联的巡逻队
     * 
     * @param tripId
     * @return
     */
    public List<LsCommonPatrolBO> findAllByTripId(String tripId) {
        return commonPatrolDao.findAllByTripId(tripId);
    }

    public LsCommonPatrolBO findCommonPatrolByPatrolNumber(String patrolNumber) {
        return commonPatrolDao.findByProperty("patrolNumber", patrolNumber);
    }

    /**
     * 批量更新
     * 
     * @param tripId
     */
    public void updateSetNullByTripId(String tripId) {
        String hql = "update LsCommonPatrolBO set tripId = null where tripId = :tripId ";
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("tripId", tripId);
        commonPatrolDao.batchUpdateOrDeleteByHql(hql, propertiesMap);
    }
}
