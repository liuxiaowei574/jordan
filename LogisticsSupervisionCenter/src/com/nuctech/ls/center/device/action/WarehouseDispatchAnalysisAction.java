package com.nuctech.ls.center.device.action;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceApplicationBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceDispatchBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.WarehouseDeviceApplicationService;
import com.nuctech.ls.model.service.WarehouseDispatchAnalysisService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.DeviceInventoryChartsVO;
import com.nuctech.ls.model.vo.warehouse.DispatchActualProgram;
import com.nuctech.ls.model.vo.warehouse.DispatchPortVO;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>调度分析  Action</p>
 * 创建时间：2016年6月2日
 */
@Namespace("/warehouseDispatchAnalysis")
public class WarehouseDispatchAnalysisAction extends LSBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7737991034005394499L;
	
	@Resource
	private WarehouseDispatchAnalysisService warehouseDispatchAnalysisService;
	@Resource
	private WarehouseDeviceApplicationService warehouseDeviceApplicationService;
	@Resource
	private SystemDepartmentService systemDepartmentService;
	
	private List<DispatchPortVO> dispatchPortList;
	private String portId;
	private String portName; //查询口岸名称
	private List<DeviceInventoryChartsVO> deviceInventoryList; //设备库存列表
	private DispatchActualProgram dispatchActualProgram;
	private String applicationId; //申请ID
	private LsWarehouseDeviceApplicationBO deviceApplication;
	private LsSystemDepartmentBO applicationDepartment;
	//List<Userinfo> list = JSON.parseArray(jsonString, Userinfo.class);
	//提交返回结果
	private String result;

	@Action(value="index", results = {
			@Result(name = "success", location = "/device/dispatch/analysis.jsp")
	})
	public String index() {
		//根据申请的ID查询申请对象
		if(!NuctechUtil.isNull(applicationId)) {
			deviceApplication = warehouseDispatchAnalysisService.findWarehouseDeviceApplicationById(applicationId);
			applicationDepartment = systemDepartmentService.findById(deviceApplication.getApplcationPort());
		}
		return SUCCESS;
	}
	
	@Action(value = "listPort", results={
			@Result(name="success", location = "/device/dispatch/port_table.jsp")
	})
	public String listPort() {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
		dispatchPortList = warehouseDispatchAnalysisService.findDispatchPortList(portName, sessionUser.getUserId());
		return SUCCESS;
	}
	
	/**
	 * 查询设备库存报表
	 * @return
	 */
	@Action(value="findDeviceInventoryList", results={
			@Result(name="success", type="json")
	})
	public String findDeviceInventoryList() {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
		deviceInventoryList = warehouseDispatchAnalysisService.findDispatchPortChartList(sessionUser.getUserId());
		return SUCCESS;
	}
	
	@Action(value="findDispatchActualProgramByPortId", results={
			@Result(name="success", type="json")
	})
	public String findDispatchActualProgramByPortId() {
		dispatchActualProgram = warehouseDispatchAnalysisService.findDispatchActualProgramByPortId(portId);
		return SUCCESS;
	}
	
	@Action(value="submitDeviceDispatchInfo", results={
			@Result(name="success", type="json")
	})
	public String submitDeviceDispatchInfo() {
		String data = request.getParameter("deviceData");
		String aid = request.getParameter("applicationId");
		String applicationPort = request.getParameter("applicationPort");
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
		JSONArray jsonArray =JSONArray.fromObject(data);
		for(int i=0; i<jsonArray.size(); i++) {
			JSONArray subArray = (JSONArray)jsonArray.get(i);
			//保存实际提交方案
			LsWarehouseDeviceDispatchBO warehouseDeviceDispatch = new LsWarehouseDeviceDispatchBO();
			warehouseDeviceDispatch.setDispatchId(generatePrimaryKey());
			warehouseDeviceDispatch.setApplicationId(aid);
			warehouseDeviceDispatch.setFromPort((String)subArray.get(0));
			warehouseDeviceDispatch.setDeviceNumber((String)subArray.get(1));
			warehouseDeviceDispatch.setEsealNumber((String)subArray.get(2));
			warehouseDeviceDispatch.setSensorNumber((String)subArray.get(3));
			warehouseDeviceDispatch.setToPort(applicationPort);
			warehouseDeviceDispatch.setDispatchStatus(Constant.DEVICE_UN_DISPATCH);
			warehouseDispatchAnalysisService.saveWarehouseDeviceDispatch(warehouseDeviceDispatch);
			//修改申请状态
			LsWarehouseDeviceApplicationBO warehouseDeviceApplication = warehouseDeviceApplicationService.findByID(aid);
			warehouseDeviceApplication.setApplyStatus(Constant.DEVICE_ALREADY_DEAL_WITH);
			warehouseDeviceApplication.setDealTime(new Date());
			warehouseDeviceApplication.setDealUser(sessionUser.getUserId());
			warehouseDeviceApplicationService.modify(warehouseDeviceApplication);
		}
		result = "true";
		return SUCCESS;
	}

	public List<DispatchPortVO> getDispatchPortList() {
		return dispatchPortList;
	}

	public void setDispatchPortList(List<DispatchPortVO> dispatchPortList) {
		this.dispatchPortList = dispatchPortList;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public List<DeviceInventoryChartsVO> getDeviceInventoryList() {
		return deviceInventoryList;
	}

	public void setDeviceInventoryList(
			List<DeviceInventoryChartsVO> deviceInventoryList) {
		this.deviceInventoryList = deviceInventoryList;
	}

	public String getPortId() {
		return portId;
	}

	public void setPortId(String portId) {
		this.portId = portId;
	}

	public DispatchActualProgram getDispatchActualProgram() {
		return dispatchActualProgram;
	}

	public void setDispatchActualProgram(DispatchActualProgram dispatchActualProgram) {
		this.dispatchActualProgram = dispatchActualProgram;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public LsWarehouseDeviceApplicationBO getDeviceApplication() {
		return deviceApplication;
	}

	public void setDeviceApplication(
			LsWarehouseDeviceApplicationBO deviceApplication) {
		this.deviceApplication = deviceApplication;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public LsSystemDepartmentBO getApplicationDepartment() {
		return applicationDepartment;
	}

	public void setApplicationDepartment(LsSystemDepartmentBO applicationDepartment) {
		this.applicationDepartment = applicationDepartment;
	}


}
