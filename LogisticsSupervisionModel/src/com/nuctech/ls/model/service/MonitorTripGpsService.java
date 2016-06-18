package com.nuctech.ls.model.service;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.ls.model.dao.MonitorVehicleGpsDao;

@Service
@Transactional
public class MonitorTripGpsService extends LSBaseService {

	@Resource
	private MonitorVehicleGpsDao monitorVehicleGpsDao;

	private List<LsMonitorVehicleGpsBO> lsMonitorVehicleGpsBOs;

	/**
	 * 根据行程id获取车辆运动轨迹
	 * @param tripId 
	 */

	public List<LsMonitorVehicleGpsBO> findLsMonitorVehicleGpsByEclockNum(String trackingDeviceNumber, LsMonitorTripBO lsMonitorTripBO) {
		this.lsMonitorVehicleGpsBOs = monitorVehicleGpsDao.findAllByCondition(trackingDeviceNumber, lsMonitorTripBO);
		return this.lsMonitorVehicleGpsBOs;
		
	}

}
