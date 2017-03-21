package com.nuctech.ls.center.map.action;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.center.utils.BeansToJson;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.ls.model.service.MonitorAlarmService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.VehicleTrackingService;
import com.nuctech.ls.model.vo.monitor.ViewAlarmReportVO;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @author liqingxian
 *
 */
@ParentPackage("json-default")
@Namespace("/vehicletrack")
public class VehicleTrackingAction extends LSBaseAction {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	private MonitorTripService monitorTripService;
	@Resource
	private VehicleTrackingService vehicleTrackingService;
	@Resource
	private MonitorAlarmService monitorAlarmService;
	private LsMonitorTripBO lsMonitorTripBO;// 车辆行程
	private Map<String, List<LsMonitorVehicleGpsBO>> lsMonitorVehicleGpsBOs;
	private List<LsMonitorAlarmBO> lsMonitorAlarmBOs;
	private List<ViewAlarmReportVO> lsMonitorAlarmVOs;
    private List<LsMonitorVehicleGpsBO> lsMonitorPatrolGpsBOs;

	@Action(value = "getParamToVehicleTrack", results = {
			@Result(name = "success", location = "/include/vehicletrack.jsp"),
			@Result(name = "error", type = "json", params = { "root",
					"errorMessage" }) })
	public String getParamToVehicleTrack() {
		return SUCCESS;
	}

	@Action(value = "findVehicleTrackingGpsByTripId", results = {
			@Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public void findVehicleTrackingGpsByTripId() {
		this.lsMonitorTripBO = this.monitorTripService
				.findById(tripId);
		if (null != this.lsMonitorTripBO) {
			StringBuffer resultArray = new StringBuffer();
			this.lsMonitorVehicleGpsBOs = vehicleTrackingService.findAllMonitorVehicleGpsByTripId(lsMonitorTripBO);
			Set<Entry<String, List<LsMonitorVehicleGpsBO>>> keySet = lsMonitorVehicleGpsBOs.entrySet();
			for(Iterator<Entry<String, List<LsMonitorVehicleGpsBO>>> it = keySet.iterator();it.hasNext();) {
				Entry<String, List<LsMonitorVehicleGpsBO>> entry = it.next();
				String key = entry.getKey();
				List<LsMonitorVehicleGpsBO> value = entry.getValue();
				String result = new BeansToJson<LsMonitorVehicleGpsBO, Serializable>().beanToJson(value);
				if(NuctechUtil.isNotNull(result)) {
					resultArray.append("{\"" + key + "\":" + result).append("},");
				}
			}
			if(resultArray.length() > 0) {
				resultArray.deleteCharAt(resultArray.length() - 1);
			}
			
			String strResult = "{\"riskStatus\":\""
					+ lsMonitorTripBO.getRiskStatus() + "\",\"jsonData\":["
					+ resultArray.toString() + "]}";

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
	 * 根据关锁号获取巡逻队轨迹
	 */
	@Action(value = "findPatrolTrackByUnitNumber", results = {@Result(name="success",type = "json"),@Result(name = "error", type = "json")})
    public String findPatrolTrackByUnitNumber() throws Exception {
        try {
        	
        	//if(NuctechUtil.isNotNull(tripId)) {
        		//this.lsMonitorTripBO = monitorTripService.findById(tripId);
        		
        		this.lsMonitorPatrolGpsBOs = vehicleTrackingService.findPatrolGpsByTripId(trackUnitNumber,startTime,endTime);
        	//}else{
        		logger.error("trackUnitNumber does not exist! trackUnitNumber=" + trackUnitNumber);
        	//}
            
            logger.info(String.format("查询某个车辆运行轨迹:参数为："+trackUnitNumber+","+trackUnitNumber+","+lsMonitorTripBO.getTripId()
            	+","+lsMonitorTripBO.getCheckinTime()+","+lsMonitorTripBO.getCheckoutTime()));
        } catch (Exception e) {
        	message = e.getMessage();
        	logger.error(message);
        }
        return SUCCESS;
    }
	
	@Action(value = "findAlarmsByTripId")
	public String findAlarmsByTripId() throws IOException {
		JSONObject json = new JSONObject();
//		this.lsMonitorAlarmBOs = this.monitorAlarmService.findAlarmsByTripIdAndStatus(tripId);
		this.lsMonitorAlarmVOs = this.monitorAlarmService.findAlarmVOsByTripIdAndStatus(tripId);
		if(this.lsMonitorAlarmBOs!=null){
			logger.info("根据tripId查询历史轨迹成功");
			json.put("success", true);
			json.put("lsMonitorAlarmVOs", lsMonitorAlarmVOs);
		}else{
			json.put("success", false);
		}
		
		response.setCharacterEncoding("utf-8");
		// 禁止浏览器开辟缓存
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma", "no-cache");
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}
	
	@Action(value = "findAlarmVOByTripId")
	public String findAlarmVOByTripId() throws IOException {
		JSONObject json = new JSONObject();
		this.lsMonitorAlarmVOs = this.monitorAlarmService
				.findAlarmVOByTripIdAndStatus(tripId);
		if(this.lsMonitorAlarmVOs!=null){
			logger.info("根据tripId查询历史轨迹成功");
			json.put("success", true);
			json.put("lsMonitorAlarmVOs", lsMonitorAlarmVOs);
		}else{
			json.put("success", false);
		}
		
		response.setCharacterEncoding("utf-8");
		// 禁止浏览器开辟缓存
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma", "no-cache");
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}
    
	public List<LsMonitorVehicleGpsBO> getLsMonitorPatrolGpsBOs() {
		return lsMonitorPatrolGpsBOs;
	}

	public void setLsMonitorPatrolGpsBOs(List<LsMonitorVehicleGpsBO> lsMonitorPatrolGpsBOs) {
		this.lsMonitorPatrolGpsBOs = lsMonitorPatrolGpsBOs;
	}

	/**
	 * 巡逻队车载台编号,用于查询巡逻队轨迹
	 */
	private String trackUnitNumber;
	
	public String getTrackUnitNumber() {
		return trackUnitNumber;
	}

	public void setTrackUnitNumber(String trackUnitNumber) {
		this.trackUnitNumber = trackUnitNumber;
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

	public List<LsMonitorAlarmBO> getLsMonitorAlarmBOs() {
		return lsMonitorAlarmBOs;
	}

	public void setLsMonitorAlarmBOs(List<LsMonitorAlarmBO> lsMonitorAlarmBOs) {
		this.lsMonitorAlarmBOs = lsMonitorAlarmBOs;
	} 
	
	private String startTime;
	private String endTime;

    
    public String getStartTime() {
        return startTime;
    }

    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    
    public String getEndTime() {
        return endTime;
    }

    
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

	public List<ViewAlarmReportVO> getLsMonitorAlarmVOs() {
		return lsMonitorAlarmVOs;
	}

	public void setLsMonitorAlarmVOs(List<ViewAlarmReportVO> lsMonitorAlarmVOs) {
		this.lsMonitorAlarmVOs = lsMonitorAlarmVOs;
	}
	
}
