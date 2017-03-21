package com.nuctech.ls.model.vo.monitor;

import java.util.Date;

public class PatrolVehicleStatusVO {

    /* 巡逻队主键 */
    private String patrolId;

    /* 车载台编号 */
    private String trackUnitNumber;

    /* 所属区域 */
    private String belongToArea;

    /* 所属节点 */
    private String belongToPort;

    /* 负责人 */
    private String potralUser;

    /* 创建人 */
    private String createUser;

    /* 更新人员 */
    private String updateUser;

    /* 更新时间 */
    private Date updateTime;

    /* 删除标记 */
    private String deleteMark;

    /* 报警当前负责人 */
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPatrolId() {
        return this.patrolId;
    }

    public void setPatrolId(String patrolId) {
        this.patrolId = patrolId;
    }

    public String getTrackUnitNumber() {
        return this.trackUnitNumber;
    }

    public void setTrackUnitNumber(String trackUnitNumber) {
        this.trackUnitNumber = trackUnitNumber;
    }

    public String getBelongToArea() {
        return this.belongToArea;
    }

    public void setBelongToArea(String belongToArea) {
        this.belongToArea = belongToArea;
    }

    public String getBelongToPort() {
        return this.belongToPort;
    }

    public void setBelongToPort(String belongToPort) {
        this.belongToPort = belongToPort;
    }

    public String getPotralUser() {
        return this.potralUser;
    }

    public void setPotralUser(String potralUser) {
        this.potralUser = potralUser;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDeleteMark() {
        return this.deleteMark;
    }

    public void setDeleteMark(String deleteMark) {
        this.deleteMark = deleteMark;
    }

    /* 车辆状态主键 */
    private String vehicleStatusId;

    /* 行程主键 */
    private String tripId;

    /* 车辆信息 */
    private String vehicleId;

    /*
     * 位置类型 0, 关锁 1 车载台
     */
    private String locationType;

    /* 位置序列号 */
    private String gpsSeq;

    /* 追踪终端号 */
    private String trackingDeviceNumber;

    /* 子锁号 */
    private String esealNumber;

    /* 传感器编号 */
    private String sensorNumber;

    /* 子锁顺序 */
    private String esealOrder;

    /* 传感器顺序 */
    private String sensorOrder;

    /* 定位时间 */
    private Date locationTime;

    /*
     * 定位状态 “state”:”01110”, //此字段代表状态位 BYTE 0 0:ACC关； 1:ACC开 BYTE 1 0:未定位 1:定位
     * BYTE 2 0:北纬 1:南纬 BYTE 3 0:东经 1:西经 BYTE 4 0:运营状态 1:停运状态
     */
    private String locationStatus;

    /* 关锁状态、1-施封、0-解封 */
    private String elockStatus;

    /* 锁杆状态 1-关，2-开 */
    private String poleStatus;

    /*
     * 防拆状态 antidismantle 0-正常 1-被拆
     */
    private String brokenStatus;

    /*
     * 上报事件 // 0----定时 // 1----锁开/关状态变更 // 2----被拆状态变更 // 3----按键施封 //
     * 4----RFID施封 // 5----RFID解封 // 6----SMS施封 //
     */
    private String eventUpload;

    /* 经度 */
    private String longitude;

    /* 纬度 */
    private String latitude;

    /* 海拔 */
    private String altitude;

    /* 速度 */
    private String elockSpeed;

    /* 行驶方向 */
    private String direction;

    /*
     * 行驶状态 0-行驶中，1-停止
     */
    private String travelStatus;

    /* 关联设备 */
    private String relatedDevice;

    /* 电量 */
    private String electricityValue;

    /* 创建时间 */
    private Date createTime;

    /* 检入人员 */
    private String checkinUser;

    /* 检入时间 */
    private Date checkinTime;

    /* 检入地点 */
    private String checkinPort;

    /* 检入图片路径 */
    private String checkinPicture;

    /* 检出人员 */
    private String checkoutUser;

    /* 检出时间 */
    private Date checkoutTime;

    /* 检出地点 */
    private String checkoutPort;

    /* 检出图片路径 */
    private String checkoutPicture;

    /*
     * 行程状态 0. 进行中 1. 已结束
     */
    private String tripStatus;

    /* 监控组主键 */
    private String groupId;

    /*
     * 报警状态 0-无报警，1-有报警
     */
    private String isAlarm;

    /*
     * 0. 绿色 1. 黄色 2. 红色
     */
    private String riskStatus;

    /* 规划路线 */
    private String routeId;

    public String getVehicleStatusId() {
        return this.vehicleStatusId;
    }

    public void setVehicleStatusId(String vehicleStatusId) {
        this.vehicleStatusId = vehicleStatusId;
    }

    public String getTripId() {
        return this.tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getLocationType() {
        return this.locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getGpsSeq() {
        return this.gpsSeq;
    }

    public void setGpsSeq(String gpsSeq) {
        this.gpsSeq = gpsSeq;
    }

    public String getTrackingDeviceNumber() {
        return this.trackingDeviceNumber;
    }

    public void setTrackingDeviceNumber(String trackingDeviceNumber) {
        this.trackingDeviceNumber = trackingDeviceNumber;
    }

    public String getEsealNumber() {
        return this.esealNumber;
    }

    public void setEsealNumber(String esealNumber) {
        this.esealNumber = esealNumber;
    }

    public String getSensorNumber() {
        return this.sensorNumber;
    }

    public void setSensorNumber(String sensorNumber) {
        this.sensorNumber = sensorNumber;
    }

    public String getEsealOrder() {
        return this.esealOrder;
    }

    public void setEsealOrder(String esealOrder) {
        this.esealOrder = esealOrder;
    }

    public String getSensorOrder() {
        return this.sensorOrder;
    }

    public void setSensorOrder(String sensorOrder) {
        this.sensorOrder = sensorOrder;
    }

    public Date getLocationTime() {
        return this.locationTime;
    }

    public void setLocationTime(Date locationTime) {
        this.locationTime = locationTime;
    }

    public String getLocationStatus() {
        return this.locationStatus;
    }

    public void setLocationStatus(String locationStatus) {
        this.locationStatus = locationStatus;
    }

    public String getElockStatus() {
        return this.elockStatus;
    }

    public void setElockStatus(String elockStatus) {
        this.elockStatus = elockStatus;
    }

    public String getPoleStatus() {
        return this.poleStatus;
    }

    public void setPoleStatus(String poleStatus) {
        this.poleStatus = poleStatus;
    }

    public String getBrokenStatus() {
        return this.brokenStatus;
    }

    public void setBrokenStatus(String brokenStatus) {
        this.brokenStatus = brokenStatus;
    }

    public String getEventUpload() {
        return this.eventUpload;
    }

    public void setEventUpload(String eventUpload) {
        this.eventUpload = eventUpload;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAltitude() {
        return this.altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getElockSpeed() {
        return this.elockSpeed;
    }

    public void setElockSpeed(String elockSpeed) {
        this.elockSpeed = elockSpeed;
    }

    public String getDirection() {
        return this.direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTravelStatus() {
        return this.travelStatus;
    }

    public void setTravelStatus(String travelStatus) {
        this.travelStatus = travelStatus;
    }

    public String getRelatedDevice() {
        return this.relatedDevice;
    }

    public void setRelatedDevice(String relatedDevice) {
        this.relatedDevice = relatedDevice;
    }

    public String getElectricityValue() {
        return this.electricityValue;
    }

    public void setElectricityValue(String electricityValue) {
        this.electricityValue = electricityValue;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCheckinUser() {
        return this.checkinUser;
    }

    public void setCheckinUser(String checkinUser) {
        this.checkinUser = checkinUser;
    }

    public Date getCheckinTime() {
        return this.checkinTime;
    }

    public void setCheckinTime(Date checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getCheckinPort() {
        return this.checkinPort;
    }

    public void setCheckinPort(String checkinPort) {
        this.checkinPort = checkinPort;
    }

    public String getCheckinPicture() {
        return this.checkinPicture;
    }

    public void setCheckinPicture(String checkinPicture) {
        this.checkinPicture = checkinPicture;
    }

    public String getCheckoutUser() {
        return this.checkoutUser;
    }

    public void setCheckoutUser(String checkoutUser) {
        this.checkoutUser = checkoutUser;
    }

    public Date getCheckoutTime() {
        return this.checkoutTime;
    }

    public void setCheckoutTime(Date checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public String getCheckoutPort() {
        return this.checkoutPort;
    }

    public void setCheckoutPort(String checkoutPort) {
        this.checkoutPort = checkoutPort;
    }

    public String getCheckoutPicture() {
        return this.checkoutPicture;
    }

    public void setCheckoutPicture(String checkoutPicture) {
        this.checkoutPicture = checkoutPicture;
    }

    public String getTripStatus() {
        return this.tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getIsAlarm() {
        return this.isAlarm;
    }

    public void setIsAlarm(String isAlarm) {
        this.isAlarm = isAlarm;
    }

    public String getRiskStatus() {
        return this.riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getRouteId() {
        return this.routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }
}
