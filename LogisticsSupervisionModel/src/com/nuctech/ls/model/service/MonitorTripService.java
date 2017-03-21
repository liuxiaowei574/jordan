package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.common.LsCommonDriverBO;
import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.dao.CommonDriverDao;
import com.nuctech.ls.model.dao.CommonPatrolDao;
import com.nuctech.ls.model.dao.CommonVehicleDao;
import com.nuctech.ls.model.dao.MonitorTripDao;
import com.nuctech.ls.model.dao.SystemDepartmentDao;
import com.nuctech.ls.model.dao.SystemUserDao;
import com.nuctech.ls.model.vo.monitor.CommonVehicleDriverVO;
import com.nuctech.ls.model.vo.monitor.MonitorTripVehicleVO;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.MessageResourceUtil;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 车辆行程信息Service
 * 
 * @author liushaowei
 *
 */
@Service
@Transactional
public class MonitorTripService extends LSBaseService {

    private final static String ORDER_BY = " /~ order by [sortColumns] ~/ ";
    @Resource
    private  MonitorTripDao monitorTripDao;

    @Resource
    private SystemDepartmentDao systemDepartmentDao;
    @Resource
    private CommonVehicleDao commonVehicleDao;
    @Resource
    private CommonDriverDao commonDriverDao;
    @Resource
    private CommonPatrolDao commonPatrolDao;
    @Resource
    private SystemUserDao systemUserDao;

    /**
     * 根据tripId获取行程信息
     */
    public LsMonitorTripBO findById(String id) {
        return monitorTripDao.findById(id);
    }

    /**
     * 根据设备编号获取行程
     * 
     * @param id
     * @return
     */
    public LsMonitorTripBO findLastestMonitortripByDeviceNumber(String trackingDeviceNumber) {
        return monitorTripDao.findLastestMonitortripByDeviceNumber(trackingDeviceNumber);
    }

    /**
     * 根据设备编号获取巡逻队行程
     * 
     * @param id
     * @return
     */
    public LsMonitorTripBO findLastestPatroltripByUnitNumber(String trackingDeviceNumber) {
        return monitorTripDao.findLastestPatroltripByDeviceNumber(trackingDeviceNumber);
    }

    /**
     * 根据条件获取指定的车辆行程信息
     * 
     * @param propertiesMap
     *        查询条件
     * @return
     * @throws Exception
     */
    public LsMonitorTripBO findMonitorTrip(HashMap<String, Object> propertiesMap) throws Exception {
        return monitorTripDao.findByProperties(propertiesMap, null);
    }

    /**
     * 新增车辆行程信息
     * 
     * @param lsMonitorTripBO
     * @throws Exception
     */
    public void addMonitorTrip(LsMonitorTripBO lsMonitorTripBO) throws Exception {
        monitorTripDao.addMonitorTrip(lsMonitorTripBO);
    }

    /**
     * 持久化车辆行程信息
     * 
     * @param lsMonitorTripBO
     * @throws Exception
     */
    public void merge(LsMonitorTripBO lsMonitorTripBO) throws Exception {
        monitorTripDao.merge(lsMonitorTripBO);
    }

    /**
     * 更新行程信息
     * 
     * @param lsMonitorTripBO
     */
    public void updateMonitorTrip(LsMonitorTripBO lsMonitorTripBO) {
        monitorTripDao.update(lsMonitorTripBO);
    }

    /**
     * 查找所有的行程信息
     * 
     * @return
     * @throws Exception
     */
    public List<LsMonitorTripBO> findAllTrips() throws Exception {
        return monitorTripDao.findAllTrips();
    }

    /**
     * 查询行程和车辆信息列表。每个行程有一个车辆List
     * 
     * @param pageQuery
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findTripVehicleList(PageQuery<Map> pageQuery) throws Exception {
        String queryString = "select t from LsMonitorTripBO t, LsCommonVehicleBO v where 1=1 "
                + " and t.tripId = v.tripId " + "/~ and v.trackingDeviceNumber like '%[trackingDeviceNumber]%' ~/"
                + "/~ and v.esealNumber like '%[esealNumber]%' ~/" + "/~ and v.sensorNumber like '%[sensorNumber]%' ~/"
                + "/~ and t.checkinUser = '[checkinUser]' ~/" + "/~ and t.checkoutPort = '[checkoutPort]' ~/"
                + "/~ and t.tripStatus = '[tripStatus]' ~/" + "/~ order by [sortColumns] ~/";
        PageList<LsMonitorTripBO> queryList = monitorTripDao.pageQuery(queryString, pageQuery);
        PageList<MonitorTripVehicleVO> pageList = new PageList<MonitorTripVehicleVO>();
        if (queryList != null && queryList.size() > 0) {
            for (LsMonitorTripBO tripBo : queryList) {
                MonitorTripVehicleVO tripVO = new MonitorTripVehicleVO();
                BeanUtils.copyProperties(tripBo, tripVO);
                if (NuctechUtil.isNotNull(tripVO.getCheckinPort())) {
                    tripVO.setCheckinPortName(
                            systemDepartmentDao.findById(tripVO.getCheckinPort()).getOrganizationName());
                }
                if (NuctechUtil.isNotNull(tripVO.getCheckoutPort())) {
                    tripVO.setCheckoutPortName(
                            systemDepartmentDao.findById(tripVO.getCheckoutPort()).getOrganizationName());
                }
                List<CommonVehicleDriverVO> commonVehicleDriverBOList = findAllVehicleDriverByTripId(
                        tripBo.getTripId());
                tripVO.setCommonVehicleDriverList(commonVehicleDriverBOList);
                copyProperties(tripVO, commonVehicleDriverBOList);
                pageList.add(tripVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 根据tripId获取指定的车辆信息
     * 
     * @param tripId
     * @return
     * @throws Exception
     */
    public List<CommonVehicleDriverVO> findAllVehicleDriverByTripId(String tripId) throws Exception {
        List<LsCommonVehicleBO> commonVehicleBOList = commonVehicleDao.findAllByTripId(tripId);
        List<CommonVehicleDriverVO> commonVehicleDriverVOList = new ArrayList<>();
        if (null != commonVehicleBOList && commonVehicleBOList.size() > 0) {
            for (LsCommonVehicleBO commonVehicleBO : commonVehicleBOList) {
                CommonVehicleDriverVO commonVehicleDriverBO = new CommonVehicleDriverVO();
                BeanUtils.copyProperties(commonVehicleBO, commonVehicleDriverBO);
                LsCommonDriverBO commonDriverBO = commonDriverDao.findById(commonVehicleBO.getDriverId());
                BeanUtils.copyProperties(commonDriverBO, commonVehicleDriverBO);
                commonVehicleDriverVOList.add(commonVehicleDriverBO);
            }
        }
        return commonVehicleDriverVOList;
    }

    /**
     * 查询一条行程和车辆信息。行程有一个车辆List
     * 
     * @param pageQuery
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findOneTripVehicle(PageQuery<Map> pageQuery) throws Exception {
        String queryString = "select t "
                + "(select organizationName from LsSystemDepartmentBO where organizationId = t.checkinPort), "
                + "(select organizationName from LsSystemDepartmentBO where organizationId = t.checkoutPort), "
                + "(select routeAreaName from LsMonitorRouteAreaBO where routeAreaId = t.routeId), "
                + "(select userName from LsSystemUserBO where userId = t.checkinUser) "
                + " from LsMonitorTripBO t, LsCommonVehicleBO v where 1=1 " + " and t.tripId = v.tripId "
                // + " and t.tripStatus = '0' " //行程状态：0,进行中
                + "/~ and t.declarationNumber = '[declarationNumber]' ~/"
                + "/~ and v.trackingDeviceNumber = '[trackingDeviceNumber]' ~/" + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = monitorTripDao.pageQuery(queryString, pageQuery);
        PageList<MonitorTripVehicleVO> pageList = new PageList<MonitorTripVehicleVO>();
        if (queryList != null && queryList.size() > 0) {
            // 只取第一条
            Object[] objs = (Object[]) queryList.get(0);
            MonitorTripVehicleVO tripVehicleVO = new MonitorTripVehicleVO();
            BeanUtils.copyProperties(objs[0], tripVehicleVO);
            tripVehicleVO.setCheckinPortName((objs[1] == null) ? "" : (String) objs[1]);
            tripVehicleVO.setCheckoutPortName((objs[2] == null) ? "" : (String) objs[2]);
            tripVehicleVO.setRouteName((objs[3] == null) ? "" : (String) objs[3]);
            tripVehicleVO.setCheckinUserName((objs[4] == null) ? "" : (String) objs[4]);
            HttpSession session = ServletActionContext.getRequest().getSession();
            tripVehicleVO
                    .setCheckoutUserName(((SessionUser) session.getAttribute(Constant.SESSION_USER)).getUserName());

            List<CommonVehicleDriverVO> commonVehicleDriverBOList = findAllVehicleDriverByPageQuery(pageQuery,
                    tripVehicleVO.getTripId());
            tripVehicleVO.setCommonVehicleDriverList(commonVehicleDriverBOList);
            copyProperties(tripVehicleVO, commonVehicleDriverBOList);

            pageList.add(tripVehicleVO);
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<CommonVehicleDriverVO> findAllVehicleDriverByPageQuery(PageQuery<Map> pageQuery, String tripId) {
        Map<String, Object> filters = pageQuery.getFilters();
        HashMap<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("tripId", tripId);
        HashMap<String, Object> propertiesLikeMap = new HashMap<>();
        if (NuctechUtil.isNotNull(filters.get("vehiclePlateNumber"))) {
            propertiesLikeMap.put("vehiclePlateNumber", filters.get("vehiclePlateNumber"));
        }

        List<LsCommonVehicleBO> commonVehicleBOList = commonVehicleDao.findAllBy(propertiesMap, propertiesLikeMap,
                null);
        List<CommonVehicleDriverVO> vehicleDriverVOList = new ArrayList<>();
        if (commonVehicleBOList != null && commonVehicleBOList.size() > 0) {
            for (LsCommonVehicleBO commonVehicleBO : commonVehicleBOList) {
                CommonVehicleDriverVO vehicleDriverVO = new CommonVehicleDriverVO();
                BeanUtils.copyProperties(commonVehicleBO, vehicleDriverVO);
                LsCommonDriverBO commonDriverBO = commonDriverDao.findById(commonVehicleBO.getDriverId());
                if (null != commonDriverBO) {
                    BeanUtils.copyProperties(commonDriverBO, vehicleDriverVO);
                }
                vehicleDriverVO.setGoodsTypeName(getGoodsTypeName(vehicleDriverVO.getGoodsType()));
                vehicleDriverVOList.add(vehicleDriverVO);
            }
        }

        return vehicleDriverVOList;
    }

    /**
     * 根据编码从资源文件获取货物类型名称
     * 
     * @param goodsTypes
     * @return
     */
    private String getGoodsTypeName(String goodsTypes) {
        if (NuctechUtil.isNull(goodsTypes)) {
            return goodsTypes;
        }
        String[] types = goodsTypes.split(",");
        if (types == null) {
            return null;
        }
        StringBuffer name = new StringBuffer();
        for (String goodsType : types) {
            name.append(MessageResourceUtil.getMessageInfo("GoodsType.GOODS_TYPE" + goodsType)).append(",");
        }
        return name.substring(0, name.length() - 1);
    }

    /**
     * 查询一条行程和车辆信息。行程有一个车辆List，返回对应的VO类
     * 
     * @param pageQuery
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public MonitorTripVehicleVO findOneTripVehicleAlarm(PageQuery<Map> pageQuery) {
        String queryString = "select t,"
                + "(select organizationName from LsSystemDepartmentBO where organizationId = t.checkinPort), "
                + "(select organizationName from LsSystemDepartmentBO where organizationId = t.checkoutPort), "
                + "(select routeAreaName from LsMonitorRouteAreaBO where routeAreaId = t.routeId), "
                + "(select userName from LsSystemUserBO where userId = t.checkinUser), "
                + "(select userName from LsSystemUserBO where userId = t.checkoutUser), "
                + "(select routeAreaName from LsMonitorRouteAreaBO where routeAreaId = t.targetZoonId), "
                + "(select routeCost from LsMonitorRouteAreaBO where routeAreaId = t.routeId) "
                + " from LsMonitorTripBO t, LsCommonVehicleBO v where 1=1 " + " and t.tripId = v.tripId "
                + "/~ and v.trackingDeviceNumber = '[trackingDeviceNumber]' ~/"
                + "/~ and t.declarationNumber like '%[declarationNumber]%' ~/" + "/~ and t.tripId = '[tripId]' ~/";
        Map<String, Object> filtersMap = pageQuery.getFilters();
        if (filtersMap != null && filtersMap.get("tripStatus") != null) {
            String tripStatus = (String) filtersMap.get("tripStatus");
            if (NuctechUtil.isNotNull(tripStatus)) {
                queryString += " and t.tripStatus in ('" + tripStatus.replaceAll("\\s*,\\s*", "','") + "') ";
            }
        }
        queryString += ORDER_BY;

        PageList<Object> queryList = monitorTripDao.pageQuery(queryString, pageQuery);
        if (queryList != null && queryList.size() > 0) {
            // 只取第一条
            Object[] objs = (Object[]) queryList.get(0);
            MonitorTripVehicleVO tripVehicleVO = new MonitorTripVehicleVO();
            BeanUtils.copyProperties(objs[0], tripVehicleVO);
            tripVehicleVO.setCheckinPortName((objs[1] == null) ? "" : (String) objs[1]);
            tripVehicleVO.setCheckoutPortName((objs[2] == null) ? "" : (String) objs[2]);
            tripVehicleVO.setRouteName((objs[3] == null) ? "" : (String) objs[3]);
            tripVehicleVO.setCheckinUserName((objs[4] == null) ? "" : (String) objs[4]);
            tripVehicleVO.setCheckoutUserName((objs[5] == null) ? "" : (String) objs[5]);
            tripVehicleVO.setTargetZoonName((objs[6] == null) ? "" : (String) objs[6]);
            tripVehicleVO.setRouteCost((objs[7] == null) ? "" : (String) objs[7]);
            // HttpSession session = ServletActionContext.getRequest().getSession();

            String patrolNames = findPatrolNames(tripVehicleVO);
            if (NuctechUtil.isNotNull(patrolNames)) {
                tripVehicleVO.setPatrolName(patrolNames);
            }

            List<CommonVehicleDriverVO> commonVehicleDriverBOList = findAllVehicleDriverByPageQuery(pageQuery,
                    tripVehicleVO.getTripId());
            tripVehicleVO.setCommonVehicleDriverList(commonVehicleDriverBOList);
            copyProperties(tripVehicleVO, commonVehicleDriverBOList);

            return tripVehicleVO;
        }
        return null;
    }

    /**
     * 查找行程的巡逻队用户名和车牌号
     * 
     * @param tripVehicleVO
     * @return
     */
    private String findPatrolNames(MonitorTripVehicleVO tripVehicleVO) {
        List<LsCommonPatrolBO> commonPatrolBOs = commonPatrolDao.findAllByTripId(tripVehicleVO.getTripId());
        if (NuctechUtil.isNotNull(commonPatrolBOs) && commonPatrolBOs.size() > 0) {
            StringBuffer patrolNames = new StringBuffer();
            for (LsCommonPatrolBO commonPatrolBO : commonPatrolBOs) {
                LsSystemUserBO systemUser = systemUserDao.findById(commonPatrolBO.getPotralUser());
                if (NuctechUtil.isNotNull(systemUser)) {
                    String s = systemUser.getUserName() + "/" + commonPatrolBO.getVehiclePlateNumber(); // 巡逻队用户名/车牌号
                    patrolNames.append(s + ",");
                }
            }
            if (patrolNames.length() > 0) {
                patrolNames.deleteCharAt(patrolNames.length() - 1);
                return patrolNames.toString();
            }
        }
        return null;
    }

    /**
     * 从所有车辆中拷贝属性到VO类
     * 
     * @param tripVehicleVO
     * @param commonVehicleDriverBOList
     */
    private void copyProperties(MonitorTripVehicleVO tripVehicleVO,
            List<CommonVehicleDriverVO> commonVehicleDriverBOList) {
        if (null != commonVehicleDriverBOList && commonVehicleDriverBOList.size() > 0) {
            List<String> vehiclePlateNumber = new ArrayList<>();
            List<String> trackingDeviceNumber = new ArrayList<>();
            List<String> esealNumber = new ArrayList<>();
            List<String> sensorNumber = new ArrayList<>();
            for (CommonVehicleDriverVO vehicleDriverVO : commonVehicleDriverBOList) {
                if (NuctechUtil.isNotNull(vehicleDriverVO.getVehiclePlateNumber())) {
                    vehiclePlateNumber.add(vehicleDriverVO.getVehiclePlateNumber());
                }
                if (NuctechUtil.isNotNull(vehicleDriverVO.getTrackingDeviceNumber())) {
                    trackingDeviceNumber.add(vehicleDriverVO.getTrackingDeviceNumber());
                }
                if (NuctechUtil.isNotNull(vehicleDriverVO.getEsealNumber())) {
                    esealNumber.add(vehicleDriverVO.getEsealNumber());
                }
                if (NuctechUtil.isNotNull(vehicleDriverVO.getSensorNumber())) {
                    sensorNumber.add(vehicleDriverVO.getSensorNumber());
                }
            }
            tripVehicleVO.setVehiclePlateNumber(StringUtils.join(vehiclePlateNumber, ","));
            tripVehicleVO.setTrackingDeviceNumber(StringUtils.join(trackingDeviceNumber, ","));
            tripVehicleVO.setEsealNumber(StringUtils.join(esealNumber, ","));
            tripVehicleVO.setSensorNumber(StringUtils.join(sensorNumber, ","));
        }
    }

    @SuppressWarnings("rawtypes")
    public JSONObject fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        PageList pageList = findTripList(pageQuery);
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    @SuppressWarnings("rawtypes")
    private PageList findTripList(PageQuery<Map> pageQuery) {
        String queryString = "select new com.nuctech.ls.model.vo.analysis.TripInfoVo(t,v.vehiclePlateNumber,"
                + "d.driverName) " + "from LsMonitorTripBO t,LsCommonVehicleBO v, LsCommonDriverBO d where 1=1 "
                + " and t.tripId=v.tripId " + " and v.driverId = d.driverId "
                + "/~ and v.vehiclePlateNumber = '[vehiclePlateNumber]' ~/" + "/~ and d.driverName = '[driverName]' ~/"
                + " and t.tripStatus='3'";
        return monitorTripDao.pageQuery(queryString, pageQuery);
    }

    public LsMonitorTripBO findLatestTripByVehicleId(String vehicleId) {
        HashMap<String, String> orderby = new HashMap<>();
        orderby.put("checkinTime", "desc");
        return monitorTripDao.findByProperty("vehicleId", vehicleId, orderby);
    }

    /**
     * 根据报关单号获取行程信息
     */
    public LsMonitorTripBO findByDeclarationNumber(String declarationNumber) {
        return monitorTripDao.findByProperty("declarationNumber", declarationNumber);
    }

    /**
     * 根据车牌号，查找该车辆最新参与的一次行程
     * 
     * @param vehiclePlateNumber
     * @return
     */
    public LsMonitorTripBO findLatestByVehiclePlateNumber(String vehiclePlateNumber) {
        List<LsCommonVehicleBO> commonVehicleBOs = commonVehicleDao.findAllByVehiclePlateNumber(vehiclePlateNumber);
        if (NuctechUtil.isNotNull(commonVehicleBOs) && commonVehicleBOs.size() > 0) {
            int length = commonVehicleBOs.size();
            String[] tripIds = new String[length];
            for (int i = 0; i < length; i++) {
                tripIds[i] = commonVehicleBOs.get(i).getTripId();
            }
            HashMap<String, Object> propertiesInMap = new HashMap<>();
            propertiesInMap.put("tripId", tripIds);
            HashMap<String, String> orderby = new HashMap<>();
            orderby.put("checkinTime", "desc");
            return monitorTripDao.findByProperties(null, null, propertiesInMap, orderby);
        }
        return null;
    }

    /**
     * 根据tripId删除
     * 
     * @param tripId
     */
    public void deleteById(String tripId) {
        monitorTripDao.deleteById(tripId);
    }

    /**
     * 返回检出口岸为指定口岸的、待结束和进行中的设备号
     * 
     * @param trackingDeviceNumber
     * @param checkoutPort
     * @return
     */
    public List<String> findUnFinishedDeviceNum(String trackingDeviceNumber, String checkoutPort) {
        return monitorTripDao.findUnFinishedDeviceNum(trackingDeviceNumber, checkoutPort);
    }

    /**
     * 根据上报设备编号查询真实设备编号
     */
    public String findRealTrackingDeviceNumberByUpload(String uploadTrackingDeviceNUmber) {
        return monitorTripDao.findRealTrackingDeviceNumberByUpload(uploadTrackingDeviceNUmber);
    }
    
    
    /**
     * 关锁使用统计列表展示
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    public List<?> findelockUse(String elockNum,String belongTo) {
        return monitorTripDao.findelockUse(elockNum,belongTo);
    }
    /**
     * 统计被使用过的关锁数量
     * @return
     */
    public  int findElockCount(){
        return monitorTripDao.findElockCount();
    }
    
    /**
     * 子锁使用统计列表展示
     * @return
     */
    public List<?> findesealUse(String deviceNum,String belongTo) {
        return monitorTripDao.findesealUse(deviceNum,belongTo);
    }
    /**
     * 统计被使用过的子锁数量
     * @return
     */
    public  int findEsealCount(){
        return monitorTripDao.findEsealCount();
    }
    
    /**
     * 传感器使用统计列表展示
     * @return
     */
    public List<?> findsensorUse(String deviceNum,String belongTo) {
        return monitorTripDao.findsensorUse(deviceNum,belongTo);
    }
    
    /**
     * 统计被使用过的传感器数量
     * @return
     */
    public  int findSensorCount(){
        return monitorTripDao.findSensorCount();
    }
}
