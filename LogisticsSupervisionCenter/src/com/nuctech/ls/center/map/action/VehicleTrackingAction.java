package com.nuctech.ls.center.map.action;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.center.utils.BeansToJson;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.VehicleTrackingService;


/**
 * 
 * @author liqingxian
 *
 */
@ParentPackage("json-default")
@Namespace("/vehicletrack")
public class VehicleTrackingAction extends LSBaseAction{

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	private MonitorTripService monitorTripService;
	@Resource
	private VehicleTrackingService vehicleTrackingService;
	private LsMonitorTripBO lsMonitorTripBO;//车辆行程
	private List<LsMonitorVehicleGpsBO> lsMonitorVehicleGpsBOs;
	
	
	@Action(value = "getParamToVehicleTrack", results = {
			@Result(name = "success", location = "/include/vehicletrack.jsp"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String getParamToVehicleTrack() {
		System.out.println(routeAreaId);
		return SUCCESS;
	}
	
	@Action(value = "findVehicleTrackingGpsByTripId", results = {
			@Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public void findVehicleTrackingGpsByTripId() {
		
		this.lsMonitorTripBO = this.monitorTripService.findMonitortripById(tripId);
		if(null!=this.lsMonitorTripBO){
			this.lsMonitorVehicleGpsBOs = vehicleTrackingService.findAllMonitorVehicleGpsByTripId(lsMonitorTripBO);
			String result = new BeansToJson<LsMonitorVehicleGpsBO,Serializable>().beanToJson(this.lsMonitorVehicleGpsBOs);
			String strResult = "{\"riskStatus\":\"" + lsMonitorTripBO.getRiskStatus()
			+ "\",\"jsonData\":"+ result + "}";
			
			try {
		           this.response.getWriter().println(strResult);
		           logger.info("根据tripId查询历史轨迹成功");
		        } catch (IOException e) {
		        	message = e.getMessage();
		        	logger.error(message);
		        }
		}
		
	}
	/**
	 * 规划区域或线路ID
	 */
	private String routeAreaId;
    /**
     * 行程ID
     */
	private String tripId;
	
	public String getRouteAreaId() {
		return routeAreaId;
	}

	public void setRouteAreaId(String routeAreaId) {
		this.routeAreaId = routeAreaId;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
	
}
