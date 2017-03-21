package com.nuctech.ls.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.dao.MonitorVehicleStatusDao;
import com.nuctech.ls.model.vo.monitor.VehicleInfoVO;

import net.sf.json.JSONObject;

/**
 * 
 * @author liqingxian
 *
 */
@Service
@Transactional
public class MonitorVehicleStatusService extends LSBaseService {

    @Resource
    private MonitorVehicleStatusDao monitorVehicleStatusDao;

    /**
     * 界面初始化查询所有车辆最新状态
     * 
     * @param locationType
     * @param organizationId
     * @param tripStatus
     * @return
     */
    public List<VehicleInfoVO> findAllVehicleStatus(String locationType, String organizationId, String tripStatus,
            String qdPorts, String zdPorts, String vehicleplatename) {
        return monitorVehicleStatusDao.findAllByProperty(locationType, organizationId, tripStatus, qdPorts, zdPorts,
                vehicleplatename);
    }

    /**
     * 界面初始化查询所有巡逻队最新状态
     * 
     * @param locationType
     * @return
     */
    public List<VehicleInfoVO> findAllPatrolStatus(String locationType, String organizationId, String roleName,
            String tripStatus) {
        return monitorVehicleStatusDao.findAllPatrolStatus(locationType, organizationId, roleName, tripStatus);
    }

    /**
     * @param entity
     */
    public void saveOrUpdate(LsMonitorVehicleStatusBO entity) {
        monitorVehicleStatusDao.saveOrUpdate(entity);
    }

    /**
     * 界面初始化查询所有在途车辆最新状态
     * 
     * @return
     */
    public List<LsMonitorVehicleStatusBO> findAllOnWayVehicleStatus(String locationType) {

        // HashMap<String, String> keyMap = new LinkedHashMap<String,String>();
        // keyMap.put("checkinTime", "desc");
        return monitorVehicleStatusDao.findAllOnWayVehicleStatus(locationType);// ("tripStatus",
                                                                               // TRIP_STATUS_ONWAY,
                                                                               // keyMap);
    }

    /**
     * toJson
     * 
     * @param srcList
     * @param pageList
     * @param pageQuery
     * @return
     */
    @SuppressWarnings("rawtypes")
    public JSONObject vehilciStatusObjectList(List<LsMonitorVehicleStatusBO> srcList,
            PageList<LsMonitorVehicleStatusBO> pageList, PageQuery<Map> pageQuery) {
        if (srcList == null || srcList.size() < 1) {
            return null;
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.addAll(srcList);
        pageList.setTotalItems(srcList.size());
        return fromObjectList(pageList, null, false);
    }

    @SuppressWarnings("rawtypes")
    public JSONObject vehicleInfoObjectList(List<VehicleInfoVO> srcList, PageList<VehicleInfoVO> pageList,
            PageQuery<Map> pageQuery) {
        if (srcList == null || srcList.size() < 1) {
            return null;
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.addAll(srcList);
        pageList.setTotalItems(srcList.size());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 根据车载台号码，查找最新一条巡逻队信息
     * 
     * @param trackingDeviceNumber
     * @return
     */
    public LsMonitorVehicleStatusBO findLatestPatrolByNumber(String trackingDeviceNumber) {
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("trackingDeviceNumber", trackingDeviceNumber);
        propertiesMap.put("locationType", "1");
        HashMap<String, String> orderby = new HashMap<String, String>();
        orderby.put("locationTime", "desc");
        return monitorVehicleStatusDao.findByProperties(propertiesMap, orderby);
    }

    /**
     * 根据设备号和tripId，查找一条状态信息
     * 
     * @param trackingDeviceNumber
     * @param tripId
     * @return
     */
    public LsMonitorVehicleStatusBO findPatrolByNumber(String trackingDeviceNumber, String tripId) {
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("trackingDeviceNumber", trackingDeviceNumber);
        propertiesMap.put("tripId", tripId);
        HashMap<String, String> orderby = new HashMap<String, String>();
        return monitorVehicleStatusDao.findByProperties(propertiesMap, orderby);
    }

    /**
     * 根据车牌号或关锁号检索车辆
     * 
     * @param locationType
     * @param organizationId
     * @param tripStatus
     * @param qdPorts
     * @param zdPorts
     * @return
     */
    public List<VehicleInfoVO> findVehicleInfoBySearchNum(String searchNumber) {
        return monitorVehicleStatusDao.findVehicleInfoBySearchNum(searchNumber);
    }

    public VehicleInfoVO findVehicleStatusByAlarm(String tripId) {
        return monitorVehicleStatusDao.findVehicleStatusByAlarm(tripId);
    }

    public List<LsMonitorVehicleStatusBO> findAllByTripId(String tripId) {
        return monitorVehicleStatusDao.findAllBy("tripId", tripId);
    }

    public List<LsMonitorVehicleStatusBO> findAllByTrackingDeviceNum(String trackingDeviceNumber) {
        return monitorVehicleStatusDao.findAllBy("trackingDeviceNumber", trackingDeviceNumber);
    }

    public List<VehicleInfoVO> findOnWayVehicleByTravelStatus(String travelStatus) {
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("travelStatus", travelStatus);
        return monitorVehicleStatusDao.findOnWayVehicleByTravelStatus(travelStatus);
    }

    public LsMonitorVehicleStatusBO findVehicleStatusByNumAndTripId(String tripId, String trackingDeviceNumber) {
        return monitorVehicleStatusDao.findLatestCommonVehicleStatusBo(tripId, trackingDeviceNumber);
    }

    public LsMonitorVehicleStatusBO findOneByTripId(String tripId, String vehicleId) {
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("tripId", tripId);
        propertiesMap.put("vehicleId", vehicleId);
        HashMap<String, String> orderby = new HashMap<String, String>();
        return monitorVehicleStatusDao.findByProperties(propertiesMap, orderby);
    }

    public void delete(String[] ids) {
        String hql = "delete LsMonitorVehicleStatusBO where vehicleStatusId in :ids";
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("ids", ids);
        monitorVehicleStatusDao.batchUpdateOrDeleteByHql(hql, propertiesMap);
    }

    /**
     * 批量删除
     * 
     * @param tripId
     */
    public void deleteByTripId(String tripId) {
        String hql = "delete LsMonitorVehicleStatusBO where tripId = :tripId ";
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("tripId", tripId);
        monitorVehicleStatusDao.batchUpdateOrDeleteByHql(hql, propertiesMap);
    }

}
