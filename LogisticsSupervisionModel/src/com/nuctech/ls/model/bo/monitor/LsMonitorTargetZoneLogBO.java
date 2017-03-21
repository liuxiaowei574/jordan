package com.nuctech.ls.model.bo.monitor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[Target zoon 记录表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_MONITOR_TARGET_ZOON_LOG")
public class LsMonitorTargetZoneLogBO implements Serializable {

    private static final long serialVersionUID = -8475269010278016159L;

    /**
     * 缺省的构造函数
     */
    public LsMonitorTargetZoneLogBO() {
        super();
    }

    /** 主键 */
    private String logId;

    /** 路线区域主键 */
    private String routeAreaId;

    /** 行程Id */
    private String tripId;

    /** 进入位置GPS序列号 */
    private Long inGpsSeq;

    /** 离开位置GPS序列号 */
    private Long outGpsSeq;

    /** 追踪终端号 */
    private String trackingDeviceNumber;

    /** 进入时间 */
    private Date inAreaTime;

    /** 离开时间 */
    private Date outAreaTime;

    @Id
    @Column(name = "LOG_ID", nullable = false, length = 50)
    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    @Column(name = "ROUTE_AREA_ID", nullable = true, length = 50)
    public String getRouteAreaId() {
        return routeAreaId;
    }

    public void setRouteAreaId(String routeAreaId) {
        this.routeAreaId = routeAreaId;
    }

    @Column(name = "TRIP_ID", nullable = true, length = 50)
    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    @Column(name = "IN_GPS_SEQ", nullable = true, length = 20)
    public Long getInGpsSeq() {
        return inGpsSeq;
    }

    public void setInGpsSeq(Long inGpsSeq) {
        this.inGpsSeq = inGpsSeq;
    }

    @Column(name = "OUT_GPS_SEQ", nullable = true, length = 20)
    public Long getOutGpsSeq() {
        return outGpsSeq;
    }

    public void setOutGpsSeq(Long outGpsSeq) {
        this.outGpsSeq = outGpsSeq;
    }

    @Column(name = "TRACKING_DEVICE_NUMBER", nullable = true, length = 50)
    public String getTrackingDeviceNumber() {
        return trackingDeviceNumber;
    }

    public void setTrackingDeviceNumber(String trackingDeviceNumber) {
        this.trackingDeviceNumber = trackingDeviceNumber;
    }

    @Column(name = "IN_AREA_TIME", nullable = true)
    public Date getInAreaTime() {
        return inAreaTime;
    }

    public void setInAreaTime(Date inAreaTime) {
        this.inAreaTime = inAreaTime;
    }

    @Column(name = "OUT_AREA_TIME", nullable = true)
    public Date getOutAreaTime() {
        return outAreaTime;
    }

    public void setOutAreaTime(Date outAreaTime) {
        this.outAreaTime = outAreaTime;
    }

}
