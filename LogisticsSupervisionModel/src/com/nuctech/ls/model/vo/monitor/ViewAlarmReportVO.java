package com.nuctech.ls.model.vo.monitor;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 报警信息视图
 * 
 * @author liushaowei
 *
 */
@Entity
@Table(name = "VIEW_ALARM_REPORT")
public class ViewAlarmReportVO {
	/* 报警主键 */
	private String alarmId;

	/* 行程主键 */
	private String tripId;

	/* 类型主键 */
	private String alarmTypeId;

	/* 级别主键 */
	private String alarmLevelId;

	/* 报警时间 */
	private Date alarmTime;

	/* 报警经度 */
	private String alarmLongitude;

	/* 报警纬度 */
	private String alarmLatitude;

	/*
	 * 是否手动创建 0-非手动，1-手动
	 */
	private String isManual;

	/*
	 * 当前负责人 当前负责处理报警的人员 可能是中心的人员，也可能是巡逻队的人员，当有人接收报警时，则填写负责人
	 */
	private String userId;

	/*
	 * 接收时间 只记录最新人员的接收时间
	 */
	private Date receiveTime;

	/*
	 * 报警状态 0-未处理，1-处理中，2-已处理
	 */
	private String alarmStatus;

	/* 级别名称 */
	private String alarmLevelName;

	/* 类型名称 */
	private String alarmTypeName;

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

	/* 追踪终端号 */
	private String trackingDeviceNumber;

	/* 子锁号 */
	private String esealNumber;

	/* 传感器编号 */
	private String sensorNumber;

	/*
	 * 0. 绿色 1. 黄色 2. 红色
	 */
	private String riskStatus;

	/* 检入人员 */
	private String checkinUser;

	/* 检入时间 */
	private Date checkinTime;

	/* 检入地点 */
	private String checkinPort;

	/* 检出人员 */
	private String checkoutUser;

	/* 检出时间 */
	private Date checkoutTime;

	/* 检出地点 */
	private String checkoutPort;

	/* 行程耗时，单位：秒 */
	private String timeCost;

	/*
	 * 行程状态 0. 进行中 1. 已结束
	 */
	private String tripStatus;

	/* 监控组主键 */
	private String groupId;

	/* 用户名 */
	private String userAccount;

	/* 姓名 */
	private String userName;

	/* 登录时间 */
	private Date logonTime;

	/* 登出时间 */
	private Date logoutTime;

	/* IP_ADDRESS */
	private String ipAddress;

	/* TOKEN */
	private String token;

	/* 有效标记 1:有效; 0:无效 */
	private String isEnable;

	public ViewAlarmReportVO() {
		super();
	}

	public ViewAlarmReportVO(String alarmId, String tripId, String alarmTypeId, String alarmLevelId, Date alarmTime,
			String alarmLongitude, String alarmLatitude, String isManual, String userId, Date receiveTime,
			String alarmStatus, String alarmLevelName, String alarmTypeName, String vehiclePlateNumber,
			String declarationNumber, String containerNumber, String vehicleCountry, String trailerNumber,
			String driverName, String driverCountry, String vehicleType, String trackingDeviceNumber,
			String esealNumber, String sensorNumber, String riskStatus, String checkinUser, Date checkinTime,
			String checkinPort, String checkoutUser, Date checkoutTime, String checkoutPort, String timeCost,
			String tripStatus, String groupId, String userAccount, String userName, Date logonTime, Date logoutTime,
			String ipAddress, String token, String isEnable) {
		super();
		this.alarmId = alarmId;
		this.tripId = tripId;
		this.alarmTypeId = alarmTypeId;
		this.alarmLevelId = alarmLevelId;
		this.alarmTime = alarmTime;
		this.alarmLongitude = alarmLongitude;
		this.alarmLatitude = alarmLatitude;
		this.isManual = isManual;
		this.userId = userId;
		this.receiveTime = receiveTime;
		this.alarmStatus = alarmStatus;
		this.alarmLevelName = alarmLevelName;
		this.alarmTypeName = alarmTypeName;
		this.vehiclePlateNumber = vehiclePlateNumber;
		this.declarationNumber = declarationNumber;
		this.containerNumber = containerNumber;
		this.vehicleCountry = vehicleCountry;
		this.trailerNumber = trailerNumber;
		this.driverName = driverName;
		this.driverCountry = driverCountry;
		this.vehicleType = vehicleType;
		this.trackingDeviceNumber = trackingDeviceNumber;
		this.esealNumber = esealNumber;
		this.sensorNumber = sensorNumber;
		this.riskStatus = riskStatus;
		this.checkinUser = checkinUser;
		this.checkinTime = checkinTime;
		this.checkinPort = checkinPort;
		this.checkoutUser = checkoutUser;
		this.checkoutTime = checkoutTime;
		this.checkoutPort = checkoutPort;
		this.timeCost = timeCost;
		this.tripStatus = tripStatus;
		this.groupId = groupId;
		this.userAccount = userAccount;
		this.userName = userName;
		this.logonTime = logonTime;
		this.logoutTime = logoutTime;
		this.ipAddress = ipAddress;
		this.token = token;
		this.isEnable = isEnable;
	}

	@Id
	@GeneratedValue
	@Column(name = "ALARM_ID", unique = true, nullable = false)
	public String getAlarmId() {
		return this.alarmId;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}

	@Column(name = "ALARM_TYPE_ID")
	public String getAlarmTypeId() {
		return this.alarmTypeId;
	}

	public void setAlarmTypeId(String alarmTypeId) {
		this.alarmTypeId = alarmTypeId;
	}

	@Column(name = "ALARM_TIME")
	public Date getAlarmTime() {
		return this.alarmTime;
	}

	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}

	@Column(name = "ALARM_LONGITUDE")
	public String getAlarmLongitude() {
		return this.alarmLongitude;
	}

	public void setAlarmLongitude(String alarmLongitude) {
		this.alarmLongitude = alarmLongitude;
	}

	@Column(name = "ALARM_LATITUDE")
	public String getAlarmLatitude() {
		return this.alarmLatitude;
	}

	public void setAlarmLatitude(String alarmLatitude) {
		this.alarmLatitude = alarmLatitude;
	}

	@Column(name = "IS_MANUAL")
	public String getIsManual() {
		return this.isManual;
	}

	public void setIsManual(String isManual) {
		this.isManual = isManual;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "RECEIVE_TIME")
	public Date getReceiveTime() {
		return this.receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	@Column(name = "ALARM_STATUS")
	public String getAlarmStatus() {
		return this.alarmStatus;
	}

	public void setAlarmStatus(String alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	@Column(name = "ALARM_LEVEL_NAME")
	public String getAlarmLevelName() {
		return this.alarmLevelName;
	}

	public void setAlarmLevelName(String alarmLevelName) {
		this.alarmLevelName = alarmLevelName;
	}

	@Column(name = "ALARM_TYPE_NAME")
	public String getAlarmTypeName() {
		return this.alarmTypeName;
	}

	public void setAlarmTypeName(String alarmTypeName) {
		this.alarmTypeName = alarmTypeName;
	}

	@Column(name = "TRACKING_DEVICE_NUMBER")
	public String getTrackingDeviceNumber() {
		return this.trackingDeviceNumber;
	}

	public void setTrackingDeviceNumber(String trackingDeviceNumber) {
		this.trackingDeviceNumber = trackingDeviceNumber;
	}

	@Column(name = "ESEAL_NUMBER")
	public String getEsealNumber() {
		return this.esealNumber;
	}

	public void setEsealNumber(String esealNumber) {
		this.esealNumber = esealNumber;
	}

	@Column(name = "SENSOR_NUMBER")
	public String getSensorNumber() {
		return this.sensorNumber;
	}

	public void setSensorNumber(String sensorNumber) {
		this.sensorNumber = sensorNumber;
	}

	@Column(name = "RISK_STATUS")
	public String getRiskStatus() {
		return this.riskStatus;
	}

	public void setRiskStatus(String riskStatus) {
		this.riskStatus = riskStatus;
	}

	@Column(name = "CHECKIN_USER")
	public String getCheckinUser() {
		return this.checkinUser;
	}

	public void setCheckinUser(String checkinUser) {
		this.checkinUser = checkinUser;
	}

	@Column(name = "CHECKIN_TIME")
	public Date getCheckinTime() {
		return this.checkinTime;
	}

	public void setCheckinTime(Date checkinTime) {
		this.checkinTime = checkinTime;
	}

	@Column(name = "CHECKIN_PORT")
	public String getCheckinPort() {
		return this.checkinPort;
	}

	public void setCheckinPort(String checkinPort) {
		this.checkinPort = checkinPort;
	}

	@Column(name = "CHECKOUT_USER")
	public String getCheckoutUser() {
		return this.checkoutUser;
	}

	public void setCheckoutUser(String checkoutUser) {
		this.checkoutUser = checkoutUser;
	}

	@Column(name = "CHECKOUT_TIME")
	public Date getCheckoutTime() {
		return this.checkoutTime;
	}

	public void setCheckoutTime(Date checkoutTime) {
		this.checkoutTime = checkoutTime;
	}

	@Column(name = "CHECKOUT_PORT")
	public String getCheckoutPort() {
		return this.checkoutPort;
	}

	public void setCheckoutPort(String checkoutPort) {
		this.checkoutPort = checkoutPort;
	}

	@Column(name = "TIME_COST")
	public String getTimeCost() {
		return this.timeCost;
	}

	public void setTimeCost(String timeCost) {
		this.timeCost = timeCost;
	}

	@Column(name = "TRIP_STATUS")
	public String getTripStatus() {
		return this.tripStatus;
	}

	public void setTripStatus(String tripStatus) {
		this.tripStatus = tripStatus;
	}

	@Column(name = "GROUP_ID")
	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Column(name = "VEHICLE_PLATE_NUMBER")
	public String getVehiclePlateNumber() {
		return this.vehiclePlateNumber;
	}

	public void setVehiclePlateNumber(String vehiclePlateNumber) {
		this.vehiclePlateNumber = vehiclePlateNumber;
	}

	@Column(name = "DECLARATION_NUMBER")
	public String getDeclarationNumber() {
		return this.declarationNumber;
	}

	public void setDeclarationNumber(String declarationNumber) {
		this.declarationNumber = declarationNumber;
	}

	@Column(name = "CONTAINER_NUMBER")
	public String getContainerNumber() {
		return this.containerNumber;
	}

	public void setContainerNumber(String containerNumber) {
		this.containerNumber = containerNumber;
	}

	@Column(name = "VEHICLE_COUNTRY")
	public String getVehicleCountry() {
		return this.vehicleCountry;
	}

	public void setVehicleCountry(String vehicleCountry) {
		this.vehicleCountry = vehicleCountry;
	}

	@Column(name = "TRAILER_NUMBER")
	public String getTrailerNumber() {
		return this.trailerNumber;
	}

	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}

	@Column(name = "DRIVER_NAME")
	public String getDriverName() {
		return this.driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	@Column(name = "DRIVER_COUNTRY")
	public String getDriverCountry() {
		return this.driverCountry;
	}

	public void setDriverCountry(String driverCountry) {
		this.driverCountry = driverCountry;
	}

	@Column(name = "VEHICLE_TYPE")
	public String getVehicleType() {
		return this.vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	@Column(name = "USER_ACCOUNT")
	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	@Column(name = "USER_NAME")
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "LOGON_TIME")
	public Date getLogonTime() {
		return this.logonTime;
	}

	public void setLogonTime(Date logonTime) {
		this.logonTime = logonTime;
	}

	@Column(name = "LOGOUT_TIME")
	public Date getLogoutTime() {
		return this.logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	@Column(name = "IP_ADDRESS")
	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Column(name = "TOKEN")
	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Column(name = "IS_ENABLE")
	public String getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	@Column(name = "ALARM_LEVEL_ID")
	public String getAlarmLevelId() {
		return alarmLevelId;
	}

	public void setAlarmLevelId(String alarmLevelId) {
		this.alarmLevelId = alarmLevelId;
	}

	@Column(name = "TRIP_ID")
	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
}
