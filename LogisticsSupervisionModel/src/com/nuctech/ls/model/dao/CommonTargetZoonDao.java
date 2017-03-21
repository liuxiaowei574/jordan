package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.monitor.LsMonitorTargetZoneLogBO;

/**
 * Target Zoon Log Dao
 * 
 * @author liushaowei
 *
 */
@Repository
public class CommonTargetZoonDao extends LSBaseDao<LsMonitorTargetZoneLogBO, Serializable> {

    /**
     * 新增记录
     * 
     * @param lsMonitorTargetZoneLogBO
     */
    @Override
    public void persist(LsMonitorTargetZoneLogBO lsMonitorTargetZoneLogBO) {
        persist(lsMonitorTargetZoneLogBO);
    }

    /**
     * 更新记录
     * 
     * @param lsMonitorTargetZoneLogBO
     */
    @Override
    public void update(LsMonitorTargetZoneLogBO lsMonitorTargetZoneLogBO) {
        update(lsMonitorTargetZoneLogBO);
    }

    /**
     * 根据tripId获取全部记录信息
     * 
     * @param tripId
     * @return
     * @throws Exception
     */
    public List<LsMonitorTargetZoneLogBO> findAllByTripId(String tripId) {
        return findAllBy("tripId", tripId);
    }

    /**
     * 根据tripId和设备号，以及区域Id查找一条记录
     * 
     * @param tripId
     * @param trackingDeviceNumber
     * @param routeAreaId
     * @return
     */
    public LsMonitorTargetZoneLogBO findByProperties(String tripId, String trackingDeviceNumber, String routeAreaId) {
        HashMap<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("tripId", tripId);
        propertiesMap.put("trackingDeviceNumber", trackingDeviceNumber);
        propertiesMap.put("routeAreaId", routeAreaId);
        HashMap<String, String> orderby = new HashMap<>();
        orderby.put("inAreaTime", "desc");
        return findByProperties(propertiesMap, orderby);
    }

}