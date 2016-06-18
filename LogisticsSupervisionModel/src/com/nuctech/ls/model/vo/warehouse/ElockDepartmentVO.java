package com.nuctech.ls.model.vo.warehouse;


/**
 * 关锁和组织机构VO类
 * @author Administrator
 *
 */

public class ElockDepartmentVO {
	/* 关锁主键 */
	private String elockId;

	
	/* 关锁号 */
	private String elockNumber;

	/* 所属节点 */
	private String belongTo;

	/* SIM卡号,多个用逗号分开 */
	private String simCard;

	/* 信息上传频率 */
	private String interval;

	/* 网关地址 */
	private String gatewayAddress;

	/*
	 * 关锁状态 维修、损坏、报废等
	 */
	private String elockStatus;
	
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
    
    public ElockDepartmentVO (){
    	super();
    }
    
    public ElockDepartmentVO(String elockId, String elockNumber,String belongTo,
    		String simCard, String interval,String gatewayAddress,String elockStatus,
    		String organizationId,String organizationName,String parentId,String organizationType,
    		String levelCode,String longitude,String latitude,String isEnable,String organizationDesc){
    	super();
    	
    	this.elockId=elockId;
    	this.elockNumber=elockNumber;
    	this.belongTo=belongTo;
    	this.simCard=simCard;
    	this.interval=interval;
    	this.gatewayAddress=gatewayAddress;
    	this.elockStatus=elockStatus;
    	this.organizationId=organizationId;
    	this.organizationName=organizationName;
    	this.parentId=parentId;
    	this.organizationType=organizationType;
    	this.levelCode=levelCode;
    	this.longitude=longitude;
    	this.latitude=latitude;
    	this.isEnable=isEnable;
    	this.organizationDesc=organizationDesc;
    }
    
    
    public String getElockId() {
		return elockId;
	}

	public void setElockId(String elockId) {
		this.elockId = elockId;
	}

	public String getElockNumber() {
		return elockNumber;
	}

	public void setElockNumber(String elockNumber) {
		this.elockNumber = elockNumber;
	}

	public String getBelongTo() {
		return belongTo;
	}

	public void setBelongTo(String belongTo) {
		this.belongTo = belongTo;
	}

	public String getSimCard() {
		return simCard;
	}

	public void setSimCard(String simCard) {
		this.simCard = simCard;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getGatewayAddress() {
		return gatewayAddress;
	}

	public void setGatewayAddress(String gatewayAddress) {
		this.gatewayAddress = gatewayAddress;
	}

	public String getElockStatus() {
		return elockStatus;
	}

	public void setElockStatus(String elockStatus) {
		this.elockStatus = elockStatus;
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

}
