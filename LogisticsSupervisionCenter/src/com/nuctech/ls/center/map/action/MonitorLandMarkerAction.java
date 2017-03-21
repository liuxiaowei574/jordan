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
import com.nuctech.ls.model.bo.monitor.LsMonitorLandMarkerBO;
import com.nuctech.ls.model.service.MonitorLandMarkerService;
import com.nuctech.util.NuctechUtil;

@ParentPackage("json-default")
@Namespace("/landmarker")
public class MonitorLandMarkerAction extends LSBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	private MonitorLandMarkerService landMarkerService;

	private List<LsMonitorLandMarkerBO> monitorLandMarkerBOs;
	private String landId;
	private String landName;
	private String landImage;
	private String latitude;
	private String longitude;
	private String description;
	private LsMonitorLandMarkerBO lsMonitorLandMarkerBO;
	private String result;

	/**
	 * 获取所有landmarker
	 * 
	 * @return
	 */
	@Action(value = "findAllLandMarkers", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public void findAllLandMarkers() {
		this.monitorLandMarkerBOs = landMarkerService.findAllLandMarkers(landName);
		String result = new BeansToJson<LsMonitorLandMarkerBO, Serializable>().beanToJson(this.monitorLandMarkerBOs);
		logger.info(String.format("查询所有landmarker"));
		try {
			this.response.getWriter().println(result);
		} catch (IOException e) {
			message = e.getMessage();
			logger.error(message);
		}
	}

	@Action(value = "findLandMarkerById", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String findLandMarkerById() {
		this.lsMonitorLandMarkerBO = landMarkerService.findLandMarkerById(landId);
		logger.info(String.format("find landmarker by landId"));
		result = SUCCESS;
		return result;
	}

	@Action(value = "deleteLandMarkerById", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String deleteLandMarkerById() {
		logger.info("删除landmarker获取到的landermarkerIds是：" + landId);

		int count = landMarkerService.delLanderMarkerByIds(getStrResult(landId));
		if (count > 0) {
			this.result = SUCCESS;
		} else {
			this.result = ERROR;
		}
		return this.result;
	}

	@Action(value = "addLanderMarker", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String addLanderMarker() {
		lsMonitorLandMarkerBO = new LsMonitorLandMarkerBO();
		lsMonitorLandMarkerBO.setLandId(generatePrimaryKey());
		lsMonitorLandMarkerBO.setLandName(landName);
		lsMonitorLandMarkerBO.setLandImage(landImage);
		lsMonitorLandMarkerBO.setLatitude(latitude);
		lsMonitorLandMarkerBO.setLongitude(longitude);
		lsMonitorLandMarkerBO.setDescription(description);
		logger.info(String.format("add lsMonitorLandMarkerBO, MonitorlandemarkerId is：%s",
				lsMonitorLandMarkerBO.getLandId()));
		landMarkerService.addLanderMarker(lsMonitorLandMarkerBO);
		result = SUCCESS;
		return result;
	}

	@Action(value = "saveOrUpdateLanderMarker", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String saveOrUpdateLanderMarker() {
		lsMonitorLandMarkerBO = landMarkerService.findLandMarkerById(landId);
		if (NuctechUtil.isNotNull(lsMonitorLandMarkerBO)) {
			lsMonitorLandMarkerBO.setLandName(landName);
			lsMonitorLandMarkerBO.setLandImage(landImage);
			lsMonitorLandMarkerBO.setLatitude(latitude);
			lsMonitorLandMarkerBO.setLongitude(longitude);
			lsMonitorLandMarkerBO.setDescription(description);
			logger.info(String.format("edit lsMonitorLandMarkerBO, MonitorlandemarkerId is：%s",
					lsMonitorLandMarkerBO.getLandId()));
			landMarkerService.addLanderMarker(lsMonitorLandMarkerBO);
		}
		result = SUCCESS;
		return result;
	}

	/**
	 * 对接收到的字符串进行处理
	 * 
	 * @return
	 */
	public String getStrResult(String str) {
		StringBuffer sb = new StringBuffer();
		if (NuctechUtil.isNotNull(str)) {
			if (str.indexOf(",") > 0) {
				String[] strs = str.split(",");
				for (int i = 0; i < strs.length - 1; i++) {
					sb.append("'" + strs[i] + "',");
				}
				sb.append("'" + strs[strs.length - 1] + "'");
			} else {
				sb.append("'" + str + "'");
			}
		}
		return sb.toString();
	}

	public String getLandId() {
		return landId;
	}

	public void setLandId(String landId) {
		this.landId = landId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getLandName() {
		return landName;
	}

	public void setLandName(String landName) {
		this.landName = landName;
	}

	public String getLandImage() {
		return landImage;
	}

	public void setLandImage(String landImage) {
		this.landImage = landImage;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LsMonitorLandMarkerBO getLsMonitorLandMarkerBO() {
		return lsMonitorLandMarkerBO;
	}

	public void setLsMonitorLandMarkerBO(LsMonitorLandMarkerBO lsMonitorLandMarkerBO) {
		this.lsMonitorLandMarkerBO = lsMonitorLandMarkerBO;
	}

}
