package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.dao.MonitorTripVehicleDao;
import com.nuctech.ls.model.vo.monitor.LsMonitorTripVehicleVo;
/**
 * 用于查询车辆与行程关联信息
 * @author liqingxian
 *
 */
@Service
@Transactional
public class MonitorTripVehicleService extends LSBaseService {

	private List<LsMonitorTripVehicleVo> infoBos;
	
	@Resource
	private MonitorTripVehicleDao monitorTripVehicleDao;
	public List<LsMonitorTripVehicleVo> findAllTripVehicleBySql(String tripStatus,String qdPorts,String zdPorts){
		this.infoBos = monitorTripVehicleDao.findAllTripVehicleBySql(tripStatus,qdPorts,zdPorts);
		return infoBos;
		
	}
}
