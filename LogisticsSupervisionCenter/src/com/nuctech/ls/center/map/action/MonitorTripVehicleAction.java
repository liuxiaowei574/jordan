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
import com.nuctech.ls.common.memcached.MemcachedUtil;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.service.MonitorTripVehicleService;
import com.nuctech.ls.model.service.MonitorVehicleStatusService;
import com.nuctech.ls.model.vo.monitor.LsMonitorTripVehicleVo;
import com.nuctech.util.NuctechUtil;
@ParentPackage("json-default")
@Namespace("/monitorTripVehicle")
/**
 * 查找行程与车辆关联信息显示车辆列表
 */
public class MonitorTripVehicleAction extends LSBaseAction {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private static final long serialVersionUID = 1L;
	@Resource
	private MonitorTripVehicleService monitorTripVehicleService;
	@Resource
	MonitorVehicleStatusService monitorVehicleStatusService;
	@Resource
	public MemcachedUtil memcachedUtil;
	
	private List<LsMonitorTripVehicleVo> lsMonitorTripVehicleVos;
	private List<LsMonitorVehicleStatusBO> lsMonitorVehicleStatusBOs;
	
	private String tripStatus;//车辆流程是否完成
	private String qdPorts;//所选择检入口岸的集合
	private String zdPorts;//所选择检出口岸的集合
	private String vehicleTypes;//巡逻车、普通车辆，目前弃用
	/**
	 * 查找行程与车辆关联信息显示车辆列表
	 */
	@Action(value = "findAllTripVelhicle", results = {@Result(name="success",type = "json")})
	public void findAllTripVelhicle() throws Exception{
		logger.info(String.format("根据行程查询对应车辆!"));
		logger.info("根据车辆完成状态查询车辆列表，目前状态为："+tripStatus+"，出发口岸是："+qdPorts+",到达口岸是："+zdPorts);
		lsMonitorTripVehicleVos = monitorTripVehicleService.findAllTripVehicleBySql(tripStatus,getStrResult(qdPorts),getStrResult(zdPorts));
		String result = new BeansToJson<LsMonitorTripVehicleVo,Serializable>().beanToJson(this.lsMonitorTripVehicleVos);
		 try {
	            this.response.getWriter().println(result);
	        } catch (IOException e) {
	        	message = e.getMessage();
	        	logger.error(message);
	        }
	}
	
	
	/**
	 * 缓存当中获取车辆信息，用于实时刷新
	 */
	@Action(value = "findAllTripVelhicleByMemCached", results = {@Result(name="success",type = "json")})
	public void findAllTripVelhicleByMemCached() throws Exception{
	
		logger.info(String.format("缓存中查找车辆列表!"));
		lsMonitorVehicleStatusBOs = this.monitorVehicleStatusService.findAllOnWayVehicleStatus(null);
		for (LsMonitorVehicleStatusBO lsMonitorVehicleBo : lsMonitorVehicleStatusBOs) {
			String trackingDeviceNum = lsMonitorVehicleBo.getTrackingDeviceNumber();
			Object obj = memcachedUtil.get(trackingDeviceNum);
			//待完善
		}
		lsMonitorTripVehicleVos = monitorTripVehicleService.findAllTripVehicleBySql(tripStatus,getStrResult(qdPorts),getStrResult(zdPorts));
		String result = new BeansToJson<LsMonitorTripVehicleVo,Serializable>().beanToJson(this.lsMonitorTripVehicleVos);
		 try {
	            this.response.getWriter().println(result);
	        } catch (IOException e) {
	        	message = e.getMessage();
	        	logger.error(message);
	        }
	}
	
	/**
	 * 过滤类型
	 */
	private String filterType;
	public String getFilterType() {
		return filterType;
	}
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}
	
	 /**
     * 对接收到的字符串进行处理
     * @return
     */
    public String getStrResult(String str){
    	StringBuffer sb = new StringBuffer();
    	if(NuctechUtil.isNotNull(str)){
        	if(str.indexOf(",")>0){
        		String[] strs = str.split(",");
        		for (int i = 0; i < strs.length-1; i++) {
        			sb.append("'"+strs[i]+"',");
    			}
        		sb.append("'"+strs[strs.length-1]+"'");
        	}else{
        		sb.append("'"+str+"'");
        	}
    	}
    	return sb.toString();
    }

	/**
	 * ids 选择的港口Id集合
	 * @return
	 */
    private String ids;
     
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getTripStatus() {
		return tripStatus;
	}
	public void setTripStatus(String tripStatus) {
		this.tripStatus = tripStatus;
	}

	public String getQdPorts() {
		return qdPorts;
	}
	public void setQdPorts(String qdPorts) {
		this.qdPorts = qdPorts;
	}

	public String getZdPorts() {
		return zdPorts;
	}
	public void setZdPorts(String zdPorts) {
		this.zdPorts = zdPorts;
	}

	public String getVehicleTypes() {
		return vehicleTypes;
	}
	public void setVehicleTypes(String vehicleTypes) {
		this.vehicleTypes = vehicleTypes;
	}
	
}
