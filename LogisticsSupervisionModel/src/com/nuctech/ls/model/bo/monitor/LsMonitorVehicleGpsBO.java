package com.nuctech.ls.model.bo.monitor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[车辆轨迹表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_MONITOR_VEHICLE_GPS")
public class LsMonitorVehicleGpsBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6604880762385206474L;

    /**
     * 缺省的构造函数
     */
    public LsMonitorVehicleGpsBO() {
        super();
    }

    /* 位置主键 */
    private String gpsId;

    /* 行程主键 */
    private String tripId;

    /*
     * 位置类型 0, 关锁 1 车载台
     */
    private String locationType;

    /* 位置序列号 */
    private Long gpsSeq;

    /* 追踪终端号 */
    private String trackingDeviceNumber;

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

    /* 电量 */
    private String electricityValue;

    /* 关联设备 */
    private String relatedDevice;

    /* 创建时间 */
    private Date createTime;

    @Id
    @Column(name = "GPS_ID", nullable = false, length = 50)
    public String getGpsId() {
        return this.gpsId;
    }

    public void setGpsId(String gpsId) {
        this.gpsId = gpsId;
    }

    @Column(name = "TRIP_ID", nullable = true, length = 50)
    public String getTripId() {
        return this.tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
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

    @Column(name = "ELECTRICITY_VALUE", nullable = true, length = 20)
    public String getElectricityValue() {
        return this.electricityValue;
    }

    public void setElectricityValue(String electricityValue) {
        this.electricityValue = electricityValue;
    }

    @Column(name = "RELATED_DEVICE", nullable = true, length = 200)
    public String getRelatedDevice() {
        return this.relatedDevice;
    }

    public void setRelatedDevice(String relatedDevice) {
        this.relatedDevice = relatedDevice;
    }

    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
