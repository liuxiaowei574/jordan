package com.nuctech.ls.model.vo.warehouse;

import java.util.Date;

public class SensorDepartmentVO {
	/* 关锁主键 */
	private String sensorId;

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public String getSensorNumber() {
		return sensorNumber;
	}

	public void setSensorNumber(String sensorNumber) {
		this.sensorNumber = sensorNumber;
	}

	public String getBelongTo() {
		return belongTo;
	}

	public void setBelongTo(String belongTo) {
		this.belongTo = belongTo;
	}

	public String getSensorStatus() {
		return sensorStatus;
	}

	public void setSensorStatus(String sensorStatus) {
		this.sensorStatus = sensorStatus;
	}

	public String getSensorType() {
		return sensorType;
	}

	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	/* 传感器编号 */
	private String sensorNumber;

	/* 所属节点 */
	private String belongTo;

	/*
	 * 传感器状态 维修、损坏、报废等
	 */
	private String sensorStatus;

	/* 传感器类型 */
	private String sensorType;

	/* 创建人 */
	private String createUser;

	/* 创建时间 */
	private Date createTime;
	
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
}
