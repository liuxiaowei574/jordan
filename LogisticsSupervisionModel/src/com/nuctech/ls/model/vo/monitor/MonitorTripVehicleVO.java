package com.nuctech.ls.model.vo.monitor;

import java.util.Date;

/**
 * 行程和车辆信息VO类
 * 
 * @author liushaowei
 *
 */
public class MonitorTripVehicleVO {
	/* 行程主键 */
	private String tripId;

	/* 车辆主键 */
	private String vehicleId;

	/* 追踪终端号 */
	private String trackingDeviceNumber;

	/* 子锁号 */
	private String esealNumber;

	/* 传感器编号 */
	private String sensorNumber;

	/* 规划路线Id */
	private String routeId;

	/* 规划路线 */
	private String routeName;

	/* 检入人员 */
	private String checkinUser;

	/* 检入人员姓名 */
	private String checkinUserName;

	/* 检入时间 */
	private Date checkinTime;

	/* 检入地点Id */
	private String checkinPort;

	/* 检入地点Name */
	private String checkinPortName;

	/* 检入图片路径 */
	private String checkinPicture;

	/* 检出人员 */
	private String checkoutUser;

	/* 检出人员姓名 */
	private String checkoutUserName;

	/* 检出时间 */
	private Date checkoutTime;

	/* 检出地点Id */
	private String checkoutPort;

	/* 检出地点Name */
	private String checkoutPortName;

	/* 检出图片路径 */
	private String checkoutPicture;

	/* 行程耗时，单位：秒 */
	private String timeCost;

	/*
	 * 行程状态 0. 进行中 1. 已结束
	 */
	private String tripStatus;

	/* 车牌号 */
	private String vehiclePlateNumber;

	/* 报关单号 */
	private String declarationNumber;

	/* 集装箱号 */
	private String containerNumber;

	/* 车辆国家 */
	private String vehicleCountry;

	/* 拖车号 */
	private String trailerNumber;

	/* 司机姓名 */
	private String driverName;

	/* 司机国家 */
	private String driverCountry;

	/* 车辆类型：0-普通车辆; */
	private String vehicleType;

	/* 子锁顺序 */
	private String esealOrder;

	/* 传感器顺序 */
	private String sensorOrder;

	public MonitorTripVehicleVO() {
		super();
	}

	public MonitorTripVehicleVO(String tripId, String vehicleId, String trackingDeviceNumber, String esealNumber,
			String sensorNumber, String routeId, String routeName, String checkinUser, String checkinUserName,
			Date checkinTime, String checkinPort, String checkinPortName, String checkinPicture, String checkoutUser,
			String checkoutUserName, Date checkoutTime, String checkoutPort, String checkoutPortName,
			String checkoutPicture, String timeCost, String tripStatus, String vehiclePlateNumber,
			String declarationNumber, String containerNumber, String vehicleCountry, String trailerNumber,
			String driverName, String driverCountry, String vehicleType, String esealOrder, String sensorOrder) {
		super();
		this.tripId = tripId;
		this.vehicleId = vehicleId;
		this.trackingDeviceNumber = trackingDeviceNumber;
		this.esealNumber = esealNumber;
		this.sensorNumber = sensorNumber;
		this.routeId = routeId;
		this.routeName = routeName;
		this.checkinUser = checkinUser;
		this.checkinUserName = checkinUserName;
		this.checkinTime = checkinTime;
		this.checkinPort = checkinPort;
		this.checkinPortName = checkinPortName;
		this.checkinPicture = checkinPicture;
		this.checkoutUser = checkoutUser;
		this.checkoutUserName = checkoutUserName;
		this.checkoutTime = checkoutTime;
		this.checkoutPort = checkoutPort;
		this.checkoutPortName = checkoutPortName;
		this.checkoutPicture = checkoutPicture;
		this.timeCost = timeCost;
		this.tripStatus = tripStatus;
		this.vehiclePlateNumber = vehiclePlateNumber;
		this.declarationNumber = declarationNumber;
		this.containerNumber = containerNumber;
		this.vehicleCountry = vehicleCountry;
		this.trailerNumber = trailerNumber;
		this.driverName = driverName;
		this.driverCountry = driverCountry;
		this.vehicleType = vehicleType;
		this.esealOrder = esealOrder;
		this.sensorOrder = sensorOrder;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getTrackingDeviceNumber() {
		return trackingDeviceNumber;
	}

	public void setTrackingDeviceNumber(String trackingDeviceNumber) {
		this.trackingDeviceNumber = trackingDeviceNumber;
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

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getCheckinUser() {
		return checkinUser;
	}

	public void setCheckinUser(String checkinUser) {
		this.checkinUser = checkinUser;
	}

	public Date getCheckinTime() {
		return checkinTime;
	}

	public void setCheckinTime(Date checkinTime) {
		this.checkinTime = checkinTime;
	}

	public String getCheckinPort() {
		return checkinPort;
	}

	public void setCheckinPort(String checkinPort) {
		this.checkinPort = checkinPort;
	}

	public String getCheckoutUser() {
		return checkoutUser;
	}

	public void setCheckoutUser(String checkoutUser) {
		this.checkoutUser = checkoutUser;
	}

	public Date getCheckoutTime() {
		return checkoutTime;
	}

	public void setCheckoutTime(Date checkoutTime) {
		this.checkoutTime = checkoutTime;
	}

	public String getCheckoutPort() {
		return checkoutPort;
	}

	public void setCheckoutPort(String checkoutPort) {
		this.checkoutPort = checkoutPort;
	}

	public String getTimeCost() {
		// return this.timeCost;
		return convert2Time(this.timeCost);
	}

	/**
	 * 秒数转为时间格式：HH:mm:ss
	 * 
	 * @param timeCost
	 * @return
	 */
	private String convert2Time(String timeCost) {
		if (timeCost == null || timeCost.length() < 1) {
			return "";
		}
		int time = Integer.valueOf(timeCost);
		int seconds = time % 60;
		int minutes = time / 60 % 60;
		int hours = time / 3600;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public void setTimeCost(String timeCost) {
		this.timeCost = timeCost;
	}

	public String getTripStatus() {
		return tripStatus;
	}

	public void setTripStatus(String tripStatus) {
		this.tripStatus = tripStatus;
	}

	public String getVehiclePlateNumber() {
		return vehiclePlateNumber;
	}

	public void setVehiclePlateNumber(String vehiclePlateNumber) {
		this.vehiclePlateNumber = vehiclePlateNumber;
	}

	public String getDeclarationNumber() {
		return declarationNumber;
	}

	public void setDeclarationNumber(String declarationNumber) {
		this.declarationNumber = declarationNumber;
	}

	public String getContainerNumber() {
		return containerNumber;
	}

	public void setContainerNumber(String containerNumber) {
		this.containerNumber = containerNumber;
	}

	public String getVehicleCountry() {
		return vehicleCountry;
	}

	public void setVehicleCountry(String vehicleCountry) {
		this.vehicleCountry = vehicleCountry;
	}

	public String getTrailerNumber() {
		return trailerNumber;
	}

	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverCountry() {
		return driverCountry;
	}

	public void setDriverCountry(String driverCountry) {
		this.driverCountry = driverCountry;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getCheckinPortName() {
		return checkinPortName;
	}

	public void setCheckinPortName(String checkinPortName) {
		this.checkinPortName = checkinPortName;
	}

	public String getCheckoutPortName() {
		return checkoutPortName;
	}

	public void setCheckoutPortName(String checkoutPortName) {
		this.checkoutPortName = checkoutPortName;
	}

	public String getCheckinPicture() {
		return checkinPicture;
	}

	public void setCheckinPicture(String checkinPicture) {
		this.checkinPicture = checkinPicture;
	}

	public String getCheckoutPicture() {
		return checkoutPicture;
	}

	public void setCheckoutPicture(String checkoutPicture) {
		this.checkoutPicture = checkoutPicture;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getCheckinUserName() {
		return checkinUserName;
	}

	public void setCheckinUserName(String checkinUserName) {
		this.checkinUserName = checkinUserName;
	}

	public String getCheckoutUserName() {
		return checkoutUserName;
	}

	public void setCheckoutUserName(String checkoutUserName) {
		this.checkoutUserName = checkoutUserName;
	}

	public String getEsealOrder() {
		return esealOrder;
	}

	public void setEsealOrder(String esealOrder) {
		this.esealOrder = esealOrder;
	}

	public String getSensorOrder() {
		return sensorOrder;
	}

	public void setSensorOrder(String sensorOrder) {
		this.sensorOrder = sensorOrder;
	}

}
