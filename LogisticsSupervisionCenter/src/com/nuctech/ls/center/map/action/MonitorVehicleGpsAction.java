package com.nuctech.ls.center.map.action;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.center.utils.BeansToJson;
import com.nuctech.ls.center.websocket.WebsocketService;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.ls.model.service.MonitorAlarmService;
import com.nuctech.ls.model.service.MonitorTripGpsService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.util.Constant;

@ParentPackage("json-default")
@Namespace("/monitorvehicle")
public class MonitorVehicleGpsAction extends LSBaseAction{

	
	private static final long serialVersionUID = 1L;
	@Resource
	private MonitorTripService monitorTripService;
	@Resource
	private MonitorTripGpsService monitorVehicleTripGpsService;
	@Resource
	private MonitorAlarmService monitorAlarmService;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private List<LsMonitorTripBO> lsMonitorTripBOs;//车辆行程列表
	private LsMonitorTripBO lsMonitorTripBO;//车辆行程
	private List<LsMonitorVehicleGpsBO> lsMonitorVehicleGpsBOs;//某辆车轨迹表
	private List<LsMonitorAlarmBO> lsMonitorAlarmBOs;//某辆车轨迹表
	private String message;
	private boolean success;//结果
	/**
	 * 获取车辆信息并分级显示
	 */
	@Action(value = "findAllMonitorTrips", results = {
    		@Result(name="success",type = "json")
    		})
    public void findAllMonitorTrips() throws Exception {
        this.lsMonitorTripBOs = this.monitorTripService.findAllTrips();
        String result = new BeansToJson<LsMonitorTripBO,Serializable>().beanToJson(this.lsMonitorTripBOs);
        logger.info(String.format("查询所有车辆并分级显示"));
        try {
            this.response.getWriter().println(result);
        } catch (IOException e) {
        	message = e.getMessage();
        	logger.error(message);
        }
    }
	
	@Action(value = "addModal", results = {
			@Result(name = "success", location = "/monitor/monitorRaAdd.jsp"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String addModal() {
		lsMonitorTripBO = new LsMonitorTripBO();
		return SUCCESS;
	}
	

	
	
	/**
	 * 获取车辆信息并分级显示
	 */
	@Action(value = "findAllMonitorVehicleGpsByEclockNum", results = {@Result(name="success",type = "json")})
    public String findAllMonitorVehicleGpsByEclockNum() throws Exception {
		HashMap<String,Object> propertiesMap = new HashMap<String,Object>();
		propertiesMap.put("vehicleId", vehicleId);
		propertiesMap.put("trackingDeviceNumber", trackingDeviceNumber);
		
        try {
        	this.lsMonitorTripBO = this.monitorTripService.findMonitorTrip(propertiesMap);
            this.lsMonitorVehicleGpsBOs = this.monitorVehicleTripGpsService.findLsMonitorVehicleGpsByEclockNum(trackingDeviceNumber,lsMonitorTripBO);
            if(lsMonitorVehicleGpsBOs != null){
            	 this.lsMonitorAlarmBOs = this.monitorAlarmService.findLsMonitorAlarmByTripId(this.lsMonitorTripBO.getTripId());
                 //String result = new BeansToJson<LsMonitorVehicleGpsBO,Serializable>().beanToJson(this.lsMonitorVehicleGpsBOs);
                 this.success = true;
            }else{
            	 this.success = false;
            }
            
            logger.info(String.format("查询某个车辆运行轨迹:参数为："+vehicleId+","+trackingDeviceNumber+","+lsMonitorTripBO.getTripId()
            	+","+lsMonitorTripBO.getCheckinTime()+","+lsMonitorTripBO.getCheckoutTime()));
        } catch (Exception e) {
        	message = e.getMessage();
        	logger.error(message);
        	this.success = false;
        	e.printStackTrace();
        }
        return SUCCESS;
    }
	
	private String trackingDeviceNumber;
	public String getTrackingDeviceNumber() {
		return trackingDeviceNumber;
	}
	public void setTrackingDeviceNumber(String trackingDeviceNumber) {
		this.trackingDeviceNumber = trackingDeviceNumber;
	}
	
	private String vehicleId;
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	
	public List<LsMonitorVehicleGpsBO> getLsMonitorVehicleGpsBOs() {
		return lsMonitorVehicleGpsBOs;
	}

	public void setLsMonitorVehicleGpsBOs(List<LsMonitorVehicleGpsBO> lsMonitorVehicleGpsBOs) {
		this.lsMonitorVehicleGpsBOs = lsMonitorVehicleGpsBOs;
	}

	public List<LsMonitorAlarmBO> getLsMonitorAlarmBOs() {
		return lsMonitorAlarmBOs;
	}

	public void setLsMonitorAlarmBOs(List<LsMonitorAlarmBO> lsMonitorAlarmBOs) {
		this.lsMonitorAlarmBOs = lsMonitorAlarmBOs;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	
	public List<LsMonitorTripBO> getLsMonitorTripBOs() {
		return lsMonitorTripBOs;
	}

	public void setLsMonitorTripBOs(List<LsMonitorTripBO> lsMonitorTripBOs) {
		this.lsMonitorTripBOs = lsMonitorTripBOs;
	}

	public LsMonitorTripBO getLsMonitorTripBO() {
		return lsMonitorTripBO;
	}

	public void setLsMonitorTripBO(LsMonitorTripBO lsMonitorTripBO) {
		this.lsMonitorTripBO = lsMonitorTripBO;
	}

	/**
	 * 获取纬度
	 * @return
	 */
	 private double getRandomLat(){
		 Random r=new Random();
		 double d1 = r.nextDouble();
		 double d= 40 + d1;
		 return d;
	 }
	 /**
		 * 获取经度
		 * @return
		 */
	 private double getRandomLng(){
		 Random r=new Random();
		 double d1 = r.nextDouble();
		 double d= 116 + d1;
		 return d;
	 }
	
}
