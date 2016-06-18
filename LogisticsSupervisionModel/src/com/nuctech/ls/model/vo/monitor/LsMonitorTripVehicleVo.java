package com.nuctech.ls.model.vo.monitor;

import com.nuctech.ls.common.base.LSBaseBO;
/**
 * 坐标集合,记录该行程中某辆车的
 */

public class LsMonitorTripVehicleVo extends LSBaseBO {

	/**
	 * 缺省的构造函数
	 */
	public LsMonitorTripVehicleVo() {
		super();
	}

	public LsMonitorTripVehicleVo(String vehicleId, String vehiclePlateNumber, String trackingDeviceNumber,
			String tripStatus) {
		this.vehicleId = vehicleId;
		this.vehiclePlateNumber = vehiclePlateNumber;
		this.trackingDeviceNumber = trackingDeviceNumber;
		this.tripStatus = tripStatus;
	}

	/**
	 * 车辆Id
	 */
	private String vehicleId;
	/* 车牌号 */
	private String vehiclePlateNumber;

	/* 关锁号 */
	private String trackingDeviceNumber;
     
	/**
	 * 行程状态
	 */
	private String tripStatus;
	/**
	 * 车辆等级 
	 */
	private String riskStatus;
	
	private String checkinTime;
	
	public String getTripStatus() {
		return tripStatus;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
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

	public String getTrackingDeviceNumber() {
		return trackingDeviceNumber;
	}

	public void setTrackingDeviceNumber(String trackingDeviceNumber) {
		this.trackingDeviceNumber = trackingDeviceNumber;
	}

	public String getRiskStatus() {
		return riskStatus;
	}

	public void setRiskStatus(String riskStatus) {
		this.riskStatus = riskStatus;
	}

	public String getCheckinTime() {
		return checkinTime;
	}

	public void setCheckinTime(String checkinTime) {
		this.checkinTime = checkinTime;
	}

	



}
