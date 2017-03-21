package com.nuctech.ls.center.map.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.service.CommonVehicleService;

@Namespace("/vehiclesetting")
public class MonitorVehicleSettingAction extends LSBaseAction{

	private static final long serialVersionUID = 1L;

	@Resource 
	private CommonVehicleService commonVehicleService;
	@Action(value = "vehicleSetModalShow", results = {
			@Result(name = "success", location = "/monitor/propertySetter.jsp"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String vehicleSetModalShow() throws Exception {
	    if(!"".equals(vehicleId)){
	        LsCommonVehicleBO  commonVehicleBO = new LsCommonVehicleBO();
	        commonVehicleBO = commonVehicleService.findById(vehicleId);
	        this.freezeAlarm = commonVehicleBO.getFreezeAlarm();
	    }
		return SUCCESS;
	}
	
	
	private String vehicleId;

    
    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
	
    private String freezeAlarm;
    
    public String getFreezeAlarm() {
        return freezeAlarm;
    }

    
    public void setFreezeAlarm(String freezeAlarm) {
        this.freezeAlarm = freezeAlarm;
    }
    
}
