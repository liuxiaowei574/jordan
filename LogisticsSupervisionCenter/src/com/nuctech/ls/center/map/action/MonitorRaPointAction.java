package com.nuctech.ls.center.map.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.center.utils.DateJsonValueProcessor;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.monitor.LsMonitorRaPointBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.service.MonitorRaPointService;
import com.nuctech.ls.model.service.MonitorRouteAreaService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;



@ParentPackage("json-default")
@Namespace("/monitorRaPoint")
public class MonitorRaPointAction extends LSBaseAction {

	private static final long serialVersionUID = 1L;
	private String message;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private List<LsMonitorRaPointBO> lsMonitorRaPointBOs;

	@Resource
	private MonitorRaPointService monitorRaPointService;
	@Resource
	private MonitorRouteAreaService monitorRouteAreaService;

	private LsMonitorRouteAreaBO lsMonitorRouteAreaBO;
	/**
	 * 根据绘制图形添加点坐标
	 * 
	 * @throws Exception
	 */
	@Action(value = "addMonitorRaPoint", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String addMonitorRaPoint() throws Exception {
		List<LsMonitorRaPointBO> lsMonitorRaPointBOs = new ArrayList<LsMonitorRaPointBO>();
		JSONArray json = JSONArray.fromObject(pointJson ); // 首先把字符串转成 JSONArray  对象
		if(json.size()>0){
		  for(int i=0;i<json.size();i++){
		    JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
		    LsMonitorRaPointBO bo = new LsMonitorRaPointBO();
		    bo.setLatitude(job.get("lat").toString());
		    bo.setLongitude(job.get("lng").toString());
		    bo.setGpsSeq(Long.valueOf(i));
		    bo.setPointId(generatePrimaryKey());
		    bo.setRouteAreaId(generatePrimaryKey());
		    monitorRaPointService.addMonitorRaPoint(bo);
			logger.info(String.format("add MonitorRaPoint, MonitorRaPointId is：%s", bo.getRouteAreaId()));
		    lsMonitorRaPointBOs.add(bo);
		  }
		  return SUCCESS;
		}
		 else {
			message = "add addMonitorRaPoints failed";
			logger.error(message);
			return ERROR;
		}
		
	}

	/**
	 * 根据线路或区域编号获取坐标集合
	 * 
	 * @param routeAreaId
	 * @return
	 */
	@Action(value = "getPointsByRouteAreaId", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String getPointsByRouteAreaId() {
		try {
			if (routeAreaId == null || routeAreaId == "") return ERROR;
			this.lsMonitorRouteAreaBO = this.monitorRouteAreaService
					.findMonitorRouteAreaById(routeAreaId);
			this.lsMonitorRaPointBOs = this.monitorRaPointService.findAllMonitorRaPointByRouteAreaId(routeAreaId);
//			JsonConfig jsonConfig = new JsonConfig();
//			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); // 排除关联死循环
//			jsonConfig.registerJsonValueProcessor(java.util.Date.class,
//					new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));// 鏃堕棿瑙ｆ瀽
//			JSONArray lineitemArray = JSONArray.fromObject(this.lsMonitorRaPointBOs, jsonConfig);
//			String result = JSONArray.fromObject(lineitemArray).toString();
//			String strResult = "{\"routeType\":\"" + (null == this.lsMonitorRouteAreaBO ? "" : this.lsMonitorRouteAreaBO.getRouteAreaType())
//					+ "\",\"routeAreaStatus\":\"" + (null == this.lsMonitorRouteAreaBO ? "" : this.lsMonitorRouteAreaBO.getRouteAreaStatus()) + "\",\"jsonData\":"
//					+ result + "}";
			return SUCCESS;
		} catch (Exception e1) {
			message = e1.getMessage();
			logger.error(message);
		}
        return ERROR;
	}

	/**
	 * 线路或区域Id用于接收参数
	 */
	private String routeAreaId;

	public String getRouteAreaId() {
		return routeAreaId;
	}

	public void setRouteAreaId(String routeAreaId) {
		this.routeAreaId = routeAreaId;
	}
    /**
     * 用于记录规划路线或区域坐标集合
     */
	private LsMonitorRaPointBO lsMonitorRaPointBO;

	public LsMonitorRaPointBO getLsMonitorRaPointBO() {
		return lsMonitorRaPointBO;
	}

	public void setLsMonitorRaPointBO(LsMonitorRaPointBO lsMonitorRaPointBO) {
		this.lsMonitorRaPointBO = lsMonitorRaPointBO;
	}
	
	
	
    public List<LsMonitorRaPointBO> getLsMonitorRaPointBOs() {
        return lsMonitorRaPointBOs;
    }

    
    public void setLsMonitorRaPointBOs(List<LsMonitorRaPointBO> lsMonitorRaPointBOs) {
        this.lsMonitorRaPointBOs = lsMonitorRaPointBOs;
    }

    /**
	 * 传递点json值
	 */
	private String pointJson;

	public String getPointJson() {
		return pointJson;
	}

	public void setPointJson(String pointJson) {
		this.pointJson = pointJson;
	}

    
    public LsMonitorRouteAreaBO getLsMonitorRouteAreaBO() {
        return lsMonitorRouteAreaBO;
    }

    
    public void setLsMonitorRouteAreaBO(LsMonitorRouteAreaBO lsMonitorRouteAreaBO) {
        this.lsMonitorRouteAreaBO = lsMonitorRouteAreaBO;
    }
	
	
}
