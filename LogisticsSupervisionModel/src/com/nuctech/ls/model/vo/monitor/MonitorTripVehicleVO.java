package com.nuctech.ls.model.vo.monitor;

import java.util.Date;
import java.util.List;

/**
 * 行程信息VO类
 * 
 * @author liushaowei
 *
 */
public class MonitorTripVehicleVO {

    /* 行程主键 */
    private String tripId;

    /* 追踪终端号 */
    private String trackingDeviceNumber;

    /** 子锁号，多个用英文逗号隔开 */
    private String esealNumber;

    /** 传感器编号，多个用英文逗号隔开 */
    private String sensorNumber;

    /* 规划路线Id */
    private String routeId;

    /* 规划路线 */
    private String routeName;

    /** 规划路线用时（分钟） */
    private String routeCost;

    /*
     * 0. 绿色 1. 黄色 2. 红色
     */
    private String riskStatus;

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

    /** 行程耗时（分钟） */
    private String timeCost;

    /**
     * 0. 待激活 1. 进行中 2. 待结束 3. 已结束
     */
    private String tripStatus;

    /* 报关单号 */
    private String declarationNumber;

    /** 巡逻队主键，多个用英文逗号隔开 */
    private String patrolId;
    private String patrolName;

    /** 车牌号，多个用英文逗号隔开 */
    private String vehiclePlateNumber;

    /**
     * 行程激活时选择的target zoon，对应LsMonitorRouteAreaBO表主键
     */
    private String targetZoonId;
    /**
     * Target zoon name
     */
    private String targetZoonName;

    /**
     * 是否特殊申请。0：否，1：是
     */
    private String specialFlag;

    /**
     * 特殊申请的理由
     */
    private String reason;

    private List<CommonVehicleDriverVO> commonVehicleDriverList;

    public MonitorTripVehicleVO() {
        super();
    }

    public MonitorTripVehicleVO(String tripId, String trackingDeviceNumber, String esealNumber, String sensorNumber,
            String routeId, String routeName, String routeCost, String riskStatus, String checkinUser,
            String checkinUserName, Date checkinTime, String checkinPort, String checkinPortName, String checkinPicture,
            String checkoutUser, String checkoutUserName, Date checkoutTime, String checkoutPort,
            String checkoutPortName, String timeCost, String tripStatus, String declarationNumber, String patrolId,
            String patrolName, String vehiclePlateNumber, String targetZoonId, String targetZoonName,
            String specialFlag, String reason, List<CommonVehicleDriverVO> commonVehicleDriverList) {
        super();
        this.tripId = tripId;
        this.trackingDeviceNumber = trackingDeviceNumber;
        this.esealNumber = esealNumber;
        this.sensorNumber = sensorNumber;
        this.routeId = routeId;
        this.routeName = routeName;
        this.routeCost = routeCost;
        this.riskStatus = riskStatus;
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
        this.timeCost = timeCost;
        this.tripStatus = tripStatus;
        this.declarationNumber = declarationNumber;
        this.patrolId = patrolId;
        this.patrolName = patrolName;
        this.vehiclePlateNumber = vehiclePlateNumber;
        this.targetZoonId = targetZoonId;
        this.targetZoonName = targetZoonName;
        this.specialFlag = specialFlag;
        this.reason = reason;
        this.commonVehicleDriverList = commonVehicleDriverList;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
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
        return this.timeCost;
    }

    /**
     * 秒数转为HH:mm:ss
     * 
     * @param timeCost
     * @return
     */
    @SuppressWarnings("unused")
    private String convert2Time(String timeCost) {
        if (timeCost == null || timeCost.length() < 1) {
            return "";
        }
        int totalSeconds = Integer.valueOf(timeCost);
        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60 % 60;
        int hours = totalSeconds / 3600;
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

    public String getDeclarationNumber() {
        return declarationNumber;
    }

    public void setDeclarationNumber(String declarationNumber) {
        this.declarationNumber = declarationNumber;
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

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getPatrolId() {
        return patrolId;
    }

    public void setPatrolId(String patrolId) {
        this.patrolId = patrolId;
    }

    public List<CommonVehicleDriverVO> getCommonVehicleDriverList() {
        return commonVehicleDriverList;
    }

    public void setCommonVehicleDriverList(List<CommonVehicleDriverVO> commonVehicleDriverList) {
        this.commonVehicleDriverList = commonVehicleDriverList;
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

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public String getTargetZoonId() {
        return targetZoonId;
    }

    public void setTargetZoonId(String targetZoonId) {
        this.targetZoonId = targetZoonId;
    }

    public String getTargetZoonName() {
        return targetZoonName;
    }

    public void setTargetZoonName(String targetZoonName) {
        this.targetZoonName = targetZoonName;
    }

    public String getPatrolName() {
        return patrolName;
    }

    public void setPatrolName(String patrolName) {
        this.patrolName = patrolName;
    }

    public String getSpecialFlag() {
        return specialFlag;
    }

    public void setSpecialFlag(String specialFlag) {
        this.specialFlag = specialFlag;
    }

    public String getRouteCost() {
        return routeCost;
    }

    public void setRouteCost(String routeCost) {
        this.routeCost = routeCost;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
