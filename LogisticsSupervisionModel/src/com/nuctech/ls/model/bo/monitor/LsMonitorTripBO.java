package com.nuctech.ls.model.bo.monitor;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 业务对象处理的实体-[记录车辆行程信息，每个车辆包含一条行程信息。]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_MONITOR_TRIP")
public class LsMonitorTripBO
{
    /**
     * 缺省的构造函数
     */
    public LsMonitorTripBO()
    {
        super();
    }

    /* 行程主键 */
    private String tripId;

    /* 主键 */
    private String vehicleId;

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

    /* 规划路线 */
    private String routeId;

    /* 0. 绿色
    1. 黄色
    2. 红色 */
    private String riskStatus;

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

    /* 行程耗时 */
    private String timeCost;

    /* 行程状态
    0. 进行中
    1. 已结束 */
    private String tripStatus;

    /* 监控组主键 */
    private String groupId;

    @Id
    @Column(name = "TRIP_ID", nullable = false, length = 50)
    public String getTripId()
    {
        return this.tripId;
    }

    public void setTripId(String tripId)
    {
        this.tripId = tripId;
    }

    @Column(name = "VEHICLE_ID", nullable = true, length = 50)
    public String getVehicleId()
    {
        return this.vehicleId;
    }

    public void setVehicleId(String vehicleId)
    {
        this.vehicleId = vehicleId;
    }

    @Column(name = "TRACKING_DEVICE_NUMBER", nullable = true, length = 50)
    public String getTrackingDeviceNumber()
    {
        return this.trackingDeviceNumber;
    }

    public void setTrackingDeviceNumber(String trackingDeviceNumber)
    {
        this.trackingDeviceNumber = trackingDeviceNumber;
    }

    @Column(name = "ESEAL_NUMBER", nullable = true, length = 100)
    public String getEsealNumber()
    {
        return this.esealNumber;
    }

    public void setEsealNumber(String esealNumber)
    {
        this.esealNumber = esealNumber;
    }

    @Column(name = "SENSOR_NUMBER", nullable = true, length = 100)
    public String getSensorNumber()
    {
        return this.sensorNumber;
    }

    public void setSensorNumber(String sensorNumber)
    {
        this.sensorNumber = sensorNumber;
    }

    @Column(name = "ESEAL_ORDER", nullable = true, length = 100)
    public String getEsealOrder()
    {
        return this.esealOrder;
    }

    public void setEsealOrder(String esealOrder)
    {
        this.esealOrder = esealOrder;
    }

    @Column(name = "SENSOR_ORDER", nullable = true, length = 100)
    public String getSensorOrder()
    {
        return this.sensorOrder;
    }

    public void setSensorOrder(String sensorOrder)
    {
        this.sensorOrder = sensorOrder;
    }

    @Column(name = "ROUTE_ID", nullable = true, length = 50)
    public String getRouteId()
    {
        return this.routeId;
    }

    public void setRouteId(String routeId)
    {
        this.routeId = routeId;
    }

    @Column(name = "RISK_STATUS", nullable = true, length = 2)
    public String getRiskStatus()
    {
        return this.riskStatus;
    }

    public void setRiskStatus(String riskStatus)
    {
        this.riskStatus = riskStatus;
    }

    @Column(name = "CHECKIN_USER", nullable = true, length = 50)
    public String getCheckinUser()
    {
        return this.checkinUser;
    }

    public void setCheckinUser(String checkinUser)
    {
        this.checkinUser = checkinUser;
    }

    @Column(name = "CHECKIN_TIME", nullable = true)
    public Date getCheckinTime()
    {
        return this.checkinTime;
    }

    public void setCheckinTime(Date checkinTime)
    {
        this.checkinTime = checkinTime;
    }

    @Column(name = "CHECKIN_PORT", nullable = true, length = 50)
    public String getCheckinPort()
    {
        return this.checkinPort;
    }

    public void setCheckinPort(String checkinPort)
    {
        this.checkinPort = checkinPort;
    }

    @Column(name = "CHECKIN_PICTURE", nullable = true, length = 2000)
    public String getCheckinPicture()
    {
        return this.checkinPicture;
    }

    public void setCheckinPicture(String checkinPicture)
    {
        this.checkinPicture = checkinPicture;
    }

    @Column(name = "CHECKOUT_USER", nullable = true, length = 50)
    public String getCheckoutUser()
    {
        return this.checkoutUser;
    }

    public void setCheckoutUser(String checkoutUser)
    {
        this.checkoutUser = checkoutUser;
    }

    @Column(name = "CHECKOUT_TIME", nullable = true)
    public Date getCheckoutTime()
    {
        return this.checkoutTime;
    }

    public void setCheckoutTime(Date checkoutTime)
    {
        this.checkoutTime = checkoutTime;
    }

    @Column(name = "CHECKOUT_PORT", nullable = true, length = 50)
    public String getCheckoutPort()
    {
        return this.checkoutPort;
    }

    public void setCheckoutPort(String checkoutPort)
    {
        this.checkoutPort = checkoutPort;
    }

    @Column(name = "CHECKOUT_PICTURE", nullable = true, length = 2000)
    public String getCheckoutPicture()
    {
        return this.checkoutPicture;
    }

    public void setCheckoutPicture(String checkoutPicture)
    {
        this.checkoutPicture = checkoutPicture;
    }

    @Column(name = "TIME_COST", nullable = true, length = 20)
    public String getTimeCost()
    {
        return this.timeCost;
    }

    public void setTimeCost(String timeCost)
    {
        this.timeCost = timeCost;
    }

    @Column(name = "TRIP_STATUS", nullable = true, length = 2)
    public String getTripStatus()
    {
        return this.tripStatus;
    }

    public void setTripStatus(String tripStatus)
    {
        this.tripStatus = tripStatus;
    }

    @Column(name = "GROUP_ID", nullable = true, length = 50)
    public String getGroupId()
    {
        return this.groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }
}
