package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.ls.model.dao.VehicleTrackingDao;

/**
 * 
 * @author liqingxian
 *
 */
@Service
@Transactional
public class VehicleTrackingService extends LSBaseService{
   
	@Resource
	VehicleTrackingDao vehicleTrackingDao;
	public List<LsMonitorVehicleGpsBO> findAllMonitorVehicleGpsByTripId(LsMonitorTripBO lsMonitorTripBO){
		return vehicleTrackingDao.findMonitorVehicleGpsByTripId(lsMonitorTripBO);
	}
}
