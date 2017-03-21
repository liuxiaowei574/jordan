package com.nuctech.ls.model.bo.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[监管车辆的相关信息 ]<br>
 * 每辆车1把关锁、1-6把子锁，1-6把传感器，每辆车都要拍照，每辆车有各自的报警，每辆车可有多种货物
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_COMMON_VEHICLE")
public class LsCommonVehicleBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1960437051049008732L;

    /**
     * 缺省的构造函数
     */
    public LsCommonVehicleBO() {
        super();
    }

    /* 主键 */
    private String vehicleId;

    /** 行程Id */
    private String tripId;

    /** 车牌号 */
    private String vehiclePlateNumber;

    /** 集装箱号，多个用英文逗号隔开 */
    private String containerNumber;

    /** 车辆国家 */
    private String vehicleCountry;

    /** 拖车号 */
    private String trailerNumber;

    /** 车辆类型：0-普通车辆; */
    private String vehicleType;

    /** 司机主键 */
    private String driverId;

    /** 追踪终端号 */
    private String trackingDeviceNumber;

    /** 子锁号，多个用英文逗号隔开 */
    private String esealNumber;

    /** 传感器编号，多个用英文逗号隔开 */
    private String sensorNumber;

    /** 子锁顺序 */
    private String esealOrder;

    /** 传感器顺序 */
    private String sensorOrder;

    /** 检入图片路径 */
    private String checkinPicture;

    /** 检出图片路径，多个用英文逗号隔开 */
    private String checkoutPicture;

    /** 货物分类Id，多个用英文逗号隔开 */
    private String goodsType;

    /** 车辆是否接收报警 */
    private String freezeAlarm;

    /**
     * 0. 绿色 1. 黄色 2. 红色
     */
    private String riskStatus;

    @Id
    @Column(name = "VEHICLE_ID", nullable = false, length = 50)
    public String getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Column(name = "VEHICLE_PLATE_NUMBER", nullable = true, length = 50)
    public String getVehiclePlateNumber() {
        return this.vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    @Column(name = "CONTAINER_NUMBER", nullable = true, length = 50)
    public String getContainerNumber() {
        return this.containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    @Column(name = "VEHICLE_COUNTRY", nullable = true, length = 100)
    public String getVehicleCountry() {
        return this.vehicleCountry;
    }

    public void setVehicleCountry(String vehicleCountry) {
        this.vehicleCountry = vehicleCountry;
    }

    @Column(name = "TRAILER_NUMBER", nullable = true, length = 50)
    public String getTrailerNumber() {
        return this.trailerNumber;
    }

    public void setTrailerNumber(String trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    @Column(name = "VEHICLE_TYPE", nullable = true, length = 2)
    public String getVehicleType() {
        return this.vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Column(name = "DRIVER_ID", nullable = true, length = 50)
    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    @Column(name = "TRIP_ID", nullable = true, length = 50)
    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
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

    @Column(name = "CHECKIN_PICTURE", nullable = true, length = 2000)
    public String getCheckinPicture() {
        return this.checkinPicture;
    }

    public void setCheckinPicture(String checkinPicture) {
        this.checkinPicture = checkinPicture;
    }

    @Column(name = "CHECKOUT_PICTURE", nullable = true, length = 2000)
    public String getCheckoutPicture() {
        return this.checkoutPicture;
    }

    public void setCheckoutPicture(String checkoutPicture) {
        this.checkoutPicture = checkoutPicture;
    }

    @Column(name = "GOODS_TYPE", nullable = true, length = 200)
    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    @Column(name = "FREEZE_ALARM", nullable = true, length = 2)
    public String getFreezeAlarm() {
        return freezeAlarm;
    }

    public void setFreezeAlarm(String freezeAlarm) {
        this.freezeAlarm = freezeAlarm;
    }

    @Column(name = "RISK_STATUS", nullable = true, length = 2)
    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

}
