package com.nuctech.ls.center.device.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceApplicationBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.WarehouseDeviceApplicationService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;

@Namespace("/warehouseDeviceApplication")
public class WarehouseDeviceApplicationAction extends LSBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9216869044821261642L;
	
	private Logger logger = Logger.getLogger(WarehouseDeviceApplicationAction.class);
	
	@Resource
	private WarehouseDeviceApplicationService warehouseDeviceApplicationService;
	@Resource
	private SystemDepartmentService systemDepartmentService;
	private LsWarehouseDeviceApplicationBO warehouseDeviceApplication;
	
	private String result;
	
	/**
	 * 列表排序字段
	 */
	private static final String DEFAULT_SORT_COLUMNS = "applicationId ASC";

	@Action(value="index", results = {
			@Result(name = "success", location = "/device/application/list.jsp")
	})
	public String index() {
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value="list")
	public void list() throws IOException{
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);		
		JSONObject retJson = warehouseDeviceApplicationService.findDeviceApplicationList(pageQuery,null,false);		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");		
		PrintWriter out = response.getWriter(); 
		out.print(retJson.toString());
		out.flush();
		out.close();
	}
	
	@Action(value = "addModal", results = {
			@Result(name = "success", location = "/device/application/add.jsp")})
	public String addModal() {
		warehouseDeviceApplication = new LsWarehouseDeviceApplicationBO();
		return SUCCESS;
	}
	
	@Action(value = "addWarehouseDeviceApplication", results = {
			@Result(name = "success", type = "json"),
			@Result(name = "error", type = "json")})
	public String addWarehouseDeviceApplication() {
		if(warehouseDeviceApplication != null) {
			SessionUser sessionUser = (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
			if(sessionUser == null) {
				logger.error("请登录系统");
				result = "false";
				return ERROR;
			}
			LsSystemDepartmentBO systemDepartment = systemDepartmentService.findById(sessionUser.getOrganizationId());
			warehouseDeviceApplication.setApplicationId(generatePrimaryKey());
			warehouseDeviceApplication.setApplcationPort(sessionUser.getOrganizationId());
			warehouseDeviceApplication.setApplcationPortName(systemDepartment.getOrganizationName());
			warehouseDeviceApplication.setApplyUser(sessionUser.getUserId());
			warehouseDeviceApplication.setApplyTime(new Date());
			warehouseDeviceApplication.setApplyStatus(Constant.DEVICE_ALREADY_APPLIED);
			warehouseDeviceApplicationService.save(warehouseDeviceApplication);
			result = "true";
            return SUCCESS;
		} else {
			logger.debug("调度申请设备为空");
			result = "false";
            return ERROR;
		}
	}

	public LsWarehouseDeviceApplicationBO getWarehouseDeviceApplication() {
		return warehouseDeviceApplication;
	}

	public void setWarehouseDeviceApplication(
			LsWarehouseDeviceApplicationBO warehouseDeviceApplication) {
		this.warehouseDeviceApplication = warehouseDeviceApplication;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
}
