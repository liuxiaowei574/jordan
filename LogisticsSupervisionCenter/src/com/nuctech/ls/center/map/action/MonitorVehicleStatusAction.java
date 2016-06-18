package com.nuctech.ls.center.map.action;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.service.MonitorVehicleStatusService;

import net.sf.json.JSONObject;

/**
 * 
 * @author liqingxian
 *
 */
@ParentPackage("json-default")
@Namespace("/vehiclestatus")
public class MonitorVehicleStatusAction extends LSBaseAction {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 1L;
	@Resource
	MonitorVehicleStatusService monitorVehicleStatusService;
	
	private String locationType;
	private String trackingDeviceNumber;
	private LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO;
	private boolean success;//结果
	/**
	 * 列表排序字段
	 */
	private static final String DEFAULT_SORT_COLUMNS = "checkinTime DESC";
	private List<LsMonitorVehicleStatusBO> lsMonitorVehicleStatusBOs;

	@SuppressWarnings("unchecked")
	@Action(value = "findAllVehicleStatus", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public void findAllVehicleStatus() throws Exception {
		this.lsMonitorVehicleStatusBOs = this.monitorVehicleStatusService.findAllVehicleStatus(this.locationType);
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
		JSONObject resJson = this.monitorVehicleStatusService.vehilciStatusObjectList(this.lsMonitorVehicleStatusBOs,
				new PageList<LsMonitorVehicleStatusBO>(), pageQuery);
		logger.info(String.format("查询地图所有车辆及巡逻队初始化状态"));
		try {
			this.response.getWriter().println(resJson);
		} catch (IOException e) {
			message = e.getMessage();
			logger.error(message);
		}
	}

	/**
	 * 查询初始化所有在途车辆
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "findAllOnWayVehicleStatus", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public void findAllOnWayVehicleStatus() throws Exception {
		this.lsMonitorVehicleStatusBOs = this.monitorVehicleStatusService.findAllOnWayVehicleStatus(this.locationType);
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
		Map filtersMap = pageQuery.getFilters();
		
		JSONObject resJson = this.monitorVehicleStatusService.vehilciStatusObjectList(this.lsMonitorVehicleStatusBOs,
				new PageList<LsMonitorVehicleStatusBO>(), pageQuery);
		logger.info(String.format("查询地图所有车辆初始化状态"));
		try {
			this.response.getWriter().println(resJson);
		} catch (IOException e) {
			message = e.getMessage();
			logger.error(message);
		}
	}
	
	@Action(value = "findPatrolByNumber", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String findPatrolByNumber() throws Exception {
		logger.info(String.format("查询巡逻队位置信息"));
		try {
			this.lsMonitorVehicleStatusBO = this.monitorVehicleStatusService.findPatrolByNumber(this.trackingDeviceNumber);
			if(lsMonitorVehicleStatusBO!=null){
				this.success = true;
			}else{
				this.success = false;
			}
		} catch (Exception e) {
			message = e.getMessage();
			logger.error(message);
			this.success = false;
		}
		return SUCCESS;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public LsMonitorVehicleStatusBO getLsMonitorVehicleStatusBO() {
		return lsMonitorVehicleStatusBO;
	}

	public void setLsMonitorVehicleStatusBO(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {
		this.lsMonitorVehicleStatusBO = lsMonitorVehicleStatusBO;
	}

	public String getTrackingDeviceNumber() {
		return trackingDeviceNumber;
	}

	public void setTrackingDeviceNumber(String trackingDeviceNumber) {
		this.trackingDeviceNumber = trackingDeviceNumber;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
