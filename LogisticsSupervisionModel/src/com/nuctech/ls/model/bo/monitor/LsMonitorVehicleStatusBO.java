package com.nuctech.ls.model.bo.monitor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[车辆状态表
 * 
 * 记录车辆最新的实时状态 与缓存在内存中的数据保持同步 ]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_MONITOR_VEHICLE_STATUS")
public class LsMonitorVehicleStatusBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 728072217848498207L;

    /**
     * 缺省的构造函数
     */
    public LsMonitorVehicleStatusBO() {
        super();
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
     * 行程状态 0.待激活,1.进行中,2.待结束,3.已结束
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

    /** 实际GPS上数的设备号 */
    private String gpsTrackingDeviceNumber;

    @Id
    @Column(name = "VEHICLE_STATUS_ID", nullable = false, length = 50)
    public String getVehicleStatusId() {
        return this.vehicleStatusId;
    }

    public void setVehicleStatusId(String vehicleStatusId) {
        this.vehicleStatusId = vehicleStatusId;
    }

    @Column(name = "TRIP_ID", nullable = true, length = 50)
    public String getTripId() {
        return this.tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    @Column(name = "VEHICLE_ID", nullable = true, length = 50)
    public String getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Column(name = "LOCATION_TYPE", nullable = true, length = 2)
    public String getLocationType() {
        return this.locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    @Column(name = "GPS_SEQ", nullable = true, length = 20)
    public Long getGpsSeq() {
        return this.gpsSeq;
    }

    public void setGpsSeq(Long gpsSeq) {
        this.gpsSeq = gpsSeq;
    }

    @Column(name = "TRACKING_DEVICE_NUMBER", nullable = true, length = 50)
    public String getTrackingDeviceNumber() {
        return this.trackingDeviceNumber;
    }

    public void setTrackingDeviceNumber(String trackingDeviceNumber) {
        this.trackingDeviceNumber = trackingDeviceNumber;
    }

    @Column(name = "ESEAL_NUMBER", nullable = true, length = 100)
    public String getEsealNumber() {
        return this.esealNumber;
    }

    public void setEsealNumber(String esealNumber) {
        this.esealNumber = esealNumber;
    }

    @Column(name = "SENSOR_NUMBER", nullable = true, length = 100)
    public String getSensorNumber() {
        return this.sensorNumber;
    }

    public void setSensorNumber(String sensorNumber) {
        this.sensorNumber = sensorNumber;
    }

    @Column(name = "ESEAL_ORDER", nullable = true, length = 100)
    public String getEsealOrder() {
        return this.esealOrder;
    }

    public void setEsealOrder(String esealOrder) {
        this.esealOrder = esealOrder;
    }

    @Column(name = "SENSOR_ORDER", nullable = true, length = 100)
    public String getSensorOrder() {
        return this.sensorOrder;
    }

    public void setSensorOrder(String sensorOrder) {
        this.sensorOrder = sensorOrder;
    }

    @Column(name = "LOCATION_TIME", nullable = true)
    public Date getLocationTime() {
        return this.locationTime;
    }

    public void setLocationTime(Date locationTime) {
        this.locationTime = locationTime;
    }

    @Column(name = "LOCATION_STATUS", nullable = true, length = 20)
    public String getLocationStatus() {
        return this.locationStatus;
    }

    public void setLocationStatus(String locationStatus) {
        this.locationStatus = locationStatus;
    }

    @Column(name = "ELOCK_STATUS", nullable = true, length = 2)
    public String getElockStatus() {
        return this.elockStatus;
    }

    public void setElockStatus(String elockStatus) {
        this.elockStatus = elockStatus;
    }

    @Column(name = "POLE_STATUS", nullable = true, length = 2)
    public String getPoleStatus() {
        return this.poleStatus;
    }

    public void setPoleStatus(String poleStatus) {
        this.poleStatus = poleStatus;
    }

    @Column(name = "BROKEN_STATUS", nullable = true, length = 2)
    public String getBrokenStatus() {
        return this.brokenStatus;
    }

    public void setBrokenStatus(String brokenStatus) {
        this.brokenStatus = brokenStatus;
    }

    @Column(name = "EVENT_UPLOAD", nullable = true, length = 2)
    public String getEventUpload() {
        return this.eventUpload;
    }

    public void setEventUpload(String eventUpload) {
        this.eventUpload = eventUpload;
    }

    @Column(name = "LONGITUDE", nullable = true, length = 20)
    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Column(name = "LATITUDE", nullable = true, length = 20)
    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Column(name = "ALTITUDE", nullable = true, length = 20)
    public String getAltitude() {
        return this.altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    @Column(name = "ELOCK_SPEED", nullable = true, length = 20)
    public String getElockSpeed() {
        return this.elockSpeed;
    }

    public void setElockSpeed(String elockSpeed) {
        this.elockSpeed = elockSpeed;
    }

    @Column(name = "DIRECTION", nullable = true, length = 20)
    public String getDirection() {
        return this.direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Column(name = "TRAVEL_STATUS", nullable = true, length = 2)
    public String getTravelStatus() {
        return this.travelStatus;
    }

    public void setTravelStatus(String travelStatus) {
        this.travelStatus = travelStatus;
    }

    @Column(name = "RELATED_DEVICE", nullable = true, length = 200)
    public String getRelatedDevice() {
        return this.relatedDevice;
    }

    public void setRelatedDevice(String relatedDevice) {
        this.relatedDevice = relatedDevice;
    }

    @Column(name = "ELECTRICITY_VALUE", nullable = true, length = 20)
    public String getElectricityValue() {
        return this.electricityValue;
    }

    public void setElectricityValue(String electricityValue) {
        this.electricityValue = electricityValue;
    }

    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "CHECKIN_USER", nullable = true, length = 50)
    public String getCheckinUser() {
        return this.checkinUser;
    }

    public void setCheckinUser(String checkinUser) {
        this.checkinUser = checkinUser;
    }

    @Column(name = "CHECKIN_TIME", nullable = true)
    public Date getCheckinTime() {
        return this.checkinTime;
    }

    public void setCheckinTime(Date checkinTime) {
        this.checkinTime = checkinTime;
    }

    @Column(name = "CHECKIN_PORT", nullable = true, length = 50)
    public String getCheckinPort() {
        return this.checkinPort;
    }

    public void setCheckinPort(String checkinPort) {
        this.checkinPort = checkinPort;
    }

    @Column(name = "CHECKIN_PICTURE", nullable = true, length = 200)
    public String getCheckinPicture() {
        return this.checkinPicture;
    }

    public void setCheckinPicture(String checkinPicture) {
        this.checkinPicture = checkinPicture;
    }

    @Column(name = "CHECKOUT_USER", nullable = true, length = 50)
    public String getCheckoutUser() {
        return this.checkoutUser;
    }

    public void setCheckoutUser(String checkoutUser) {
        this.checkoutUser = checkoutUser;
    }

    @Column(name = "CHECKOUT_TIME", nullable = true)
    public Date getCheckoutTime() {
        return this.checkoutTime;
    }

    public void setCheckoutTime(Date checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    @Column(name = "CHECKOUT_PORT", nullable = true, length = 50)
    public String getCheckoutPort() {
        return this.checkoutPort;
    }

    public void setCheckoutPort(String checkoutPort) {
        this.checkoutPort = checkoutPort;
    }

    @Column(name = "CHECKOUT_PICTURE", nullable = true, length = 200)
    public String getCheckoutPicture() {
        return this.checkoutPicture;
    }

    public void setCheckoutPicture(String checkoutPicture) {
        this.checkoutPicture = checkoutPicture;
    }

    @Column(name = "TRIP_STATUS", nullable = true, length = 2)
    public String getTripStatus() {
        return this.tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    @Column(name = "GROUP_ID", nullable = true, length = 50)
    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Column(name = "IS_ALARM", nullable = true, length = 2)
    public String getIsAlarm() {
        return this.isAlarm;
    }

    public void setIsAlarm(String isAlarm) {
        this.isAlarm = isAlarm;
    }

    @Column(name = "RISK_STATUS", nullable = true, length = 2)
    public String getRiskStatus() {
        return this.riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    @Column(name = "ROUTE_ID", nullable = true, length = 50)
    public String getRouteId() {
        return this.routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    @Column(name = "GPS_TRACKING_DEVICE_NUMBER", nullable = true, length = 50)
    public String getGpsTrackingDeviceNumber() {
        return gpsTrackingDeviceNumber;
    }

    public void setGpsTrackingDeviceNumber(String gpsTrackingDeviceNumber) {
        this.gpsTrackingDeviceNumber = gpsTrackingDeviceNumber;
    }

//    public LsMonitorVehicleStatusBO getClone(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {
//        if (lsMonitorVehicleStatusBO != null) {
//            try {
//                return (LsMonitorVehicleStatusBO) lsMonitorVehicleStatusBO.clone();
//            } catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

}
