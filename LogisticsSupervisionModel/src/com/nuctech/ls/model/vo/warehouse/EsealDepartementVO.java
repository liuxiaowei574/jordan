package com.nuctech.ls.model.vo.warehouse;

import java.util.Date;

public class EsealDepartementVO {
	/* 子锁主键 */
	private String esealId;

	public String getEsealId() {
		return esealId;
	}

	public void setEsealId(String esealId) {
		this.esealId = esealId;
	}

	public String getEsealNumber() {
		return esealNumber;
	}

	public void setEsealNumber(String esealNumber) {
		this.esealNumber = esealNumber;
	}

	public String getBelongTo() {
		return belongTo;
	}

	public void setBelongTo(String belongTo) {
		this.belongTo = belongTo;
	}

	public String getEsealStatus() {
		return esealStatus;
	}

	public void setEsealStatus(String esealStatus) {
		this.esealStatus = esealStatus;
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

	/* 子锁号 */
	private String esealNumber;

	/* 所属节点 */
	private String belongTo;

	/*
	 * 关锁状态 维修、损坏、报废等
	 */
	private String esealStatus;

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
