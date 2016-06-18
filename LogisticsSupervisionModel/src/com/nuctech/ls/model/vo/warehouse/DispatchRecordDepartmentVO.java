package com.nuctech.ls.model.vo.warehouse;

import java.util.Date;

/**
 * 调度记录表和组织机构VO类
 * @author Administrator
 *
 */
public class DispatchRecordDepartmentVO {
	 public String getDispatchId() {
		return dispatchId;
	}

	public void setDispatchId(String dispatchId) {
		this.dispatchId = dispatchId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getToPort() {
		return toPort;
	}

	public void setToPort(String toPort) {
		this.toPort = toPort;
	}

	public String getFromPort() {
		return fromPort;
	}

	public void setFromPort(String fromPort) {
		this.fromPort = fromPort;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getEsealNumber() {
		return esealNumber;
	}

	public void setEsealNumber(String esealNumber) {
		this.esealNumber = esealNumber;
	}

	public String getSensorNumber() {
		return sensorNumber;
	}

	public void setSensorNumber(String sensorNumber) {
		this.sensorNumber = sensorNumber;
	}

	public String getOtherNumber() {
		return otherNumber;
	}

	public void setOtherNumber(String otherNumber) {
		this.otherNumber = otherNumber;
	}

	public String getDispatchUser() {
		return dispatchUser;
	}

	public void setDispatchUser(String dispatchUser) {
		this.dispatchUser = dispatchUser;
	}

	public Date getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public String getDispatchStatus() {
		return dispatchStatus;
	}

	public void setDispatchStatus(String dispatchStatus) {
		this.dispatchStatus = dispatchStatus;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public String getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	public String getOrganizationDesc() {
		return organizationDesc;
	}

	public void setOrganizationDesc(String organizationDesc) {
		this.organizationDesc = organizationDesc;
	}

	/* 调配主键 */
    private String dispatchId;

    /* 申请主键 */
    private String applicationId;

    /* 申请节点编号 */
    private String toPort;

    /* 迁出节点
    迁出口岸编号 */
    private String fromPort;

    /* 终端数量 */
    private String deviceNumber;

    /* 子锁数量 */
    private String esealNumber;

    /* 传感器数量 */
    private String sensorNumber;

    /* 其他数量 */
    private String otherNumber;

    /* 调配人
    */
    private String dispatchUser;

    /* 调配时间 */
    private Date dispatchTime;

    /* 调度状态
    0、未调度
    1、调度完成
    2、调度驳回 */
    private String dispatchStatus;
    
    /* 机构主键 */
    private String organizationId;

    /* 机构名称 */
    private String organizationName;

    /* 上级机构 */
    private String parentId;

    /* 1、国家
    2、口岸
    3、监管场所
    4、建管中心 */
    private String organizationType;

    /* 层次码 */
    private String levelCode;

    /* 经度 */
    private String longitude;

    /* 纬度 */
    private String latitude;

    /* 有效标记 */
    private String isEnable;

    /* 机构描述 */
    private String organizationDesc;
    
    
    public String getFromPortName() {
		return fromPortName;
	}

	public void setFromPortName(String fromPortName) {
		this.fromPortName = fromPortName;
	}

	public String getToPortName() {
		return toPortName;
	}

	public void setToPortName(String toPortName) {
		this.toPortName = toPortName;
	}

	private String fromPortName;
    private String toPortName;
}
