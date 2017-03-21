package com.nuctech.ls.center.map.action;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.service.CommonVehicleService;
import com.nuctech.util.Constant;

@ParentPackage("json-default")
@Namespace("/commonvehicle")
public class CommonVehicleAction extends LSBaseAction{
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 1L;
  
	
	private LsCommonVehicleBO lsCommonVehicleBO;
	@Resource
	private CommonVehicleService commonVehicleService;
	@Action(value = "updateVehicleFreezeAlarm", results = {
    		@Result(name="success",type = "json")
    		})
    public void updateVehicleFreezeAlarm() throws Exception {
		
        try {
        	if(!"".equalsIgnoreCase(freezeAlarm)&&!"".equalsIgnoreCase(vehicleId)){
        		lsCommonVehicleBO = commonVehicleService.findById(vehicleId);
        		if(Constant.FREEZE_ALARM_YES.equalsIgnoreCase(freezeAlarm)){
        			lsCommonVehicleBO.setFreezeAlarm(Constant.FREEZE_ALARM_YES);
        		}else if(Constant.FREEZE_ALARM_NO.equalsIgnoreCase(freezeAlarm)){
        			lsCommonVehicleBO.setFreezeAlarm(Constant.FREEZE_ALARM_NO);
        		}else{
        			lsCommonVehicleBO.setFreezeAlarm(Constant.FREEZE_ALARM_YES);
        		}
        		commonVehicleService.updateCommonVehicle(lsCommonVehicleBO);
        	}
            this.response.getWriter().println(result);
        } catch (IOException e) {
        	message = e.getMessage();
        	logger.error(message);
        }
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
