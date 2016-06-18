package com.nuctech.ls.report.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.service.InventoryReportService;
import com.nuctech.ls.model.vo.report.DeviceInventoryVO;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.DateUtils;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>库存管理 报告</p>
 * 创建时间：2016年6月13日
 */
@Namespace("/inventoryReport")
public class InventoryReportAction extends LSBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6225416451500664470L;
	
	//关锁流入
	private final static String TRACK_DEVICE_FLOW_IN = "trackDeviceFlowIn";
	//关锁流出
	private final static String TRACK_DEVICE_FLOW_OUT = "trackDeviceFlowOut";
	//子锁流入
	private final static String ESEAL_FLOW_IN = "esealFlowIn";
	//子锁流出
	private final static String ESEAL_FLOW_OUT = "esealFlowOut";
	//传感器流入
	private final static String SENSOR_FLOW_IN = "sensorFlowIn";
	//传感器流出
	private final static String SENSOR_FLOW_OUT = "sensorFlowOut";
	//关锁转入
	private final static String TRACK_DEVICE_TURN_IN = "trackDeviceTurnIn"; 
	//关锁转出
	private final static String TRACK_DEVICE_TURN_OUT = "trackDeviceTurnOut"; 
	//子锁转入
	private final static String ESEAL_TURN_IN = "esealTurnIn"; 
	//子锁转出
	private final static String ESEAL_TURN_OUT = "esealTurnOut"; 
	//传感器转入
	private final static String SENSOR_TURN_IN = "sensorTurnIn"; 
	//传感器转出
	private final static String SENSOR_TURN_OUT = "sensorTurnOut"; 

	@Resource
	private InventoryReportService inventoryReportService;
	
	private DeviceInventoryVO deviceInventory;
	
	private String type;
	
	/**
	 * 通知首页链接 返回到通知的首页
	 * 
	 * @return
	 */
	@Action(value="index", results = {
			@Result(name = "success", location = "/report/inventory.jsp")
	})
	public String index() {
		try {
			SessionUser sessionUser = (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
			if(sessionUser != null) {
				Date startDate = DateUtils.stringToDate("1900-01-01");
				Date endDate = new Date();
				deviceInventory = inventoryReportService.statisticsDeviceInventory(sessionUser.getOrganizationId(), startDate, endDate);
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	@Action(value="list")
	public void list() throws IOException{
		pageQuery = this.newPageQuery("");
		Map<String, Object> filters = new HashMap<String, Object>();
		JSONArray retJson = null;
		if(type.equals(TRACK_DEVICE_FLOW_IN)) {
			filters.put("checkoutPort", "5304");
			filters.put("checkoutStartTime", "1900-01-01 00:00:00");
			filters.put("checkoutEndTime", DateUtils.date2String(new Date()));
			pageQuery.setFilters(filters);
			retJson = inventoryReportService.findTrackDeviceFlowList(pageQuery, null,false);
		} else if(type.equals(TRACK_DEVICE_FLOW_OUT)) {
			filters.put("checkinPort", "5304");
			filters.put("checkinStartTime", "1900-01-01 00:00:00");
			filters.put("checkinEndTime", DateUtils.date2String(new Date()));
			pageQuery.setFilters(filters);
			retJson = inventoryReportService.findTrackDeviceFlowList(pageQuery, null,false);
		} else if(type.equals(ESEAL_FLOW_IN)) {
			filters.put("checkoutPort", "5304");
			filters.put("checkoutStartTime", "1900-01-01 00:00:00");
			filters.put("checkoutEndTime", DateUtils.date2String(new Date()));
			pageQuery.setFilters(filters);
			retJson = inventoryReportService.findEsealFlowList(pageQuery, null,false);
		} else if(type.equals(ESEAL_FLOW_OUT)) {
			filters.put("checkinPort", "5304");
			filters.put("checkinStartTime", "1900-01-01 00:00:00");
			filters.put("checkinEndTime", DateUtils.date2String(new Date()));
			pageQuery.setFilters(filters);
			retJson = inventoryReportService.findEsealFlowList(pageQuery, null,false);
		} else if(type.equals(SENSOR_FLOW_IN)){
			filters.put("checkoutPort", "5304");
			filters.put("checkoutStartTime", "1900-01-01 00:00:00");
			filters.put("checkoutEndTime", DateUtils.date2String(new Date()));
			pageQuery.setFilters(filters);
			retJson = inventoryReportService.findSensorFlowList(pageQuery, null,false);
		} else if(type.equals(SENSOR_FLOW_OUT)) {
			filters.put("checkinPort", "5304");
			filters.put("checkinStartTime", "1900-01-01 00:00:00");
			filters.put("checkinEndTime", DateUtils.date2String(new Date()));
			pageQuery.setFilters(filters);
			retJson = inventoryReportService.findSensorFlowList(pageQuery, null,false);
		} else if(type.equals(TRACK_DEVICE_TURN_IN)) {
			filters.put("applcationPort", "5304");
			filters.put("finishStartTime", "1900-01-01 00:00:00");
			filters.put("finishEndTime", DateUtils.date2String(new Date()));
			pageQuery.setFilters(filters);
			retJson = inventoryReportService.findTrackDeviceTrunInList(pageQuery, null,false);
		} else if(type.equals(TRACK_DEVICE_TURN_OUT)) {
			filters.put("fromPort", "5304");
			filters.put("dispatchStartTime", "1900-01-01 00:00:00");
			filters.put("dispatchEndTime", DateUtils.date2String(new Date()));
			pageQuery.setFilters(filters);
			retJson = inventoryReportService.findTrackDeviceTrunOutList(pageQuery, null,false);
		} else if(type.equals(ESEAL_TURN_IN)) {
			filters.put("applcationPort", "5304");
			filters.put("finishStartTime", "1900-01-01 00:00:00");
			filters.put("finishEndTime", DateUtils.date2String(new Date()));
			pageQuery.setFilters(filters);
			retJson = inventoryReportService.findEsealTrunInList(pageQuery, null,false);
		} else if(type.equals(ESEAL_TURN_OUT)) {
			filters.put("fromPort", "5304");
			filters.put("dispatchStartTime", "1900-01-01 00:00:00");
			filters.put("dispatchEndTime", DateUtils.date2String(new Date()));
			pageQuery.setFilters(filters);
			retJson = inventoryReportService.findEsealTrunOutList(pageQuery, null,false);
		} else if(type.equals(SENSOR_TURN_IN)) {
			filters.put("applcationPort", "5304");
			filters.put("finishStartTime", "1900-01-01 00:00:00");
			filters.put("finishEndTime", DateUtils.date2String(new Date()));
			pageQuery.setFilters(filters);
			retJson = inventoryReportService.findSensorTrunInList(pageQuery, null,false);
		} else if(type.equals(SENSOR_TURN_OUT)) {
			filters.put("fromPort", "5304");
			filters.put("dispatchStartTime", "1900-01-01 00:00:00");
			filters.put("dispatchEndTime", DateUtils.date2String(new Date()));
			pageQuery.setFilters(filters);
			retJson = inventoryReportService.findSensorTrunOutList(pageQuery, null,false);
		} 
		//retJson = inventoryReportService.findTrackDeviceFlowInList(pageQuery, null,false);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");		
		PrintWriter out = response.getWriter(); 
		out.print(retJson.toString());
		out.flush();
		out.close();
	}
	
	public String findDeviceInventory() {
		return SUCCESS;
	}

	public DeviceInventoryVO getDeviceInventory() {
		return deviceInventory;
	}

	public void setDeviceInventory(DeviceInventoryVO deviceInventory) {
		this.deviceInventory = deviceInventory;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
