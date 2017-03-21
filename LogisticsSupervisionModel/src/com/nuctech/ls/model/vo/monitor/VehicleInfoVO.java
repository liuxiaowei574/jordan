package com.nuctech.ls.model.vo.monitor;

import java.util.Date;

public class VehicleInfoVO {

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
    private Long gpsSeq;
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

    private String checkinPortName;

    private String checkoutPortName;

    /* 巡逻队主键 */
    private String patrolId;

    /* 巡逻队编号 */
    private String patrolNumber;

    /* 车载台编号 */
    private String trackUnitNumber;

    /* 所属区域 */
    private String belongToArea;

    /* 所属节点 */
    private String belongToPort;

    /* 负责人 */
    private String potralUser;

    /* 负责人Name */
    private String potralUserName;

    /* 创建人 */
    private String createUser;

    /* 更新人员 */
    private String updateUser;

    /* 更新时间 */
    private Date updateTime;

    /* 删除标记 */
    private String deleteMark;

    /* 所属口岸名称 */
    private String belongToPortName;

    /* 路线区域名称 */
    private String routeAreaName;

    private String checkinUserName;

    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getVehicleStatusId() {
        return vehicleStatusId;
    }

    public void setVehicleStatusId(String vehicleStatusId) {
        this.vehicleStatusId = vehicleStatusId;
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

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public Long getGpsSeq() {
        return gpsSeq;
    }

    public void setGpsSeq(Long gpsSeq) {
        this.gpsSeq = gpsSeq;
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

    public Date getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(Date locationTime) {
        this.locationTime = locationTime;
    }

    public String getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(String locationStatus) {
        this.locationStatus = locationStatus;
    }

    public String getElockStatus() {
        return elockStatus;
    }

    public void setElockStatus(String elockStatus) {
        this.elockStatus = elockStatus;
    }

    public String getPoleStatus() {
        return poleStatus;
    }

    public void setPoleStatus(String poleStatus) {
        this.poleStatus = poleStatus;
    }

    public String getBrokenStatus() {
        return brokenStatus;
    }

    public void setBrokenStatus(String brokenStatus) {
        this.brokenStatus = brokenStatus;
    }

    public String getEventUpload() {
        return eventUpload;
    }

    public void setEventUpload(String eventUpload) {
        this.eventUpload = eventUpload;
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

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getElockSpeed() {
        return elockSpeed;
    }

    public void setElockSpeed(String elockSpeed) {
        this.elockSpeed = elockSpeed;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTravelStatus() {
        return travelStatus;
    }

    public void setTravelStatus(String travelStatus) {
        this.travelStatus = travelStatus;
    }

    public String getRelatedDevice() {
        return relatedDevice;
    }

    public void setRelatedDevice(String relatedDevice) {
        this.relatedDevice = relatedDevice;
    }

    public String getElectricityValue() {
        return electricityValue;
    }

    public void setElectricityValue(String electricityValue) {
        this.electricityValue = electricityValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getCheckinPicture() {
        return checkinPicture;
    }

    public void setCheckinPicture(String checkinPicture) {
        this.checkinPicture = checkinPicture;
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

    public String getCheckoutPicture() {
        return checkoutPicture;
    }

    public void setCheckoutPicture(String checkoutPicture) {
        this.checkoutPicture = checkoutPicture;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getIsAlarm() {
        return isAlarm;
    }

    public void setIsAlarm(String isAlarm) {
        this.isAlarm = isAlarm;
    }

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
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

    public String getPatrolId() {
        return patrolId;
    }

    public void setPatrolId(String patrolId) {
        this.patrolId = patrolId;
    }

    public String getTrackUnitNumber() {
        return trackUnitNumber;
    }

    public void setTrackUnitNumber(String trackUnitNumber) {
        this.trackUnitNumber = trackUnitNumber;
    }

    public String getBelongToArea() {
        return belongToArea;
    }

    public void setBelongToArea(String belongToArea) {
        this.belongToArea = belongToArea;
    }

    public String getBelongToPort() {
        return belongToPort;
    }

    public void setBelongToPort(String belongToPort) {
        this.belongToPort = belongToPort;
    }

    public String getPotralUser() {
        return potralUser;
    }

    public void setPotralUser(String potralUser) {
        this.potralUser = potralUser;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDeleteMark() {
        return deleteMark;
    }

    public void setDeleteMark(String deleteMark) {
        this.deleteMark = deleteMark;
    }

    public String getBelongToPortName() {
        return belongToPortName;
    }

    public void setBelongToPortName(String belongToPortName) {
        this.belongToPortName = belongToPortName;
    }

    public String getRouteAreaName() {
        return routeAreaName;
    }

    public void setRouteAreaName(String routeAreaName) {
        this.routeAreaName = routeAreaName;
    }

    public String getCheckinUserName() {
        return checkinUserName;
    }

    public void setCheckinUserName(String checkinUserName) {
        this.checkinUserName = checkinUserName;
    }

    public String getPotralUserName() {
        return potralUserName;
    }

    public void setPotralUserName(String potralUserName) {
        this.potralUserName = potralUserName;
    }

    public String getPatrolNumber() {
        return patrolNumber;
    }

    public void setPatrolNumber(String patrolNumber) {
        this.patrolNumber = patrolNumber;
    }

}
