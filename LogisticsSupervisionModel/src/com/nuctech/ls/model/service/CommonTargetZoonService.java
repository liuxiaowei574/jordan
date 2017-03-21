package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.monitor.LsMonitorTargetZoneLogBO;
import com.nuctech.ls.model.dao.CommonTargetZoonDao;

/**
 * Target Zoon Log Service
 * 
 * @author liushaowei
 *
 */
@Service
@Transactional
public class CommonTargetZoonService extends LSBaseService {

    @Resource
    private CommonTargetZoonDao commonTargetZoonDao;

    /**
     * 持久化
     * 
     * @param lsMonitorTargetZoneLogBO
     */
    public void persist(LsMonitorTargetZoneLogBO lsMonitorTargetZoneLogBO) {
        commonTargetZoonDao.persist(lsMonitorTargetZoneLogBO);
    }

    /**
     * 更新
     * 
     * @param lsMonitorTargetZoneLogBO
     */
    public void update(LsMonitorTargetZoneLogBO lsMonitorTargetZoneLogBO) {
        commonTargetZoonDao.update(lsMonitorTargetZoneLogBO);
    }

    /**
     * 根据tripId获取全部记录信息
     * 
     * @param tripId
     * @return
     * @throws Exception
     */
    public List<LsMonitorTargetZoneLogBO> findAllByTripId(String tripId) {
        return commonTargetZoonDao.findAllByTripId(tripId);
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
        return commonTargetZoonDao.findByProperties(tripId, trackingDeviceNumber, routeAreaId);
    }

}
