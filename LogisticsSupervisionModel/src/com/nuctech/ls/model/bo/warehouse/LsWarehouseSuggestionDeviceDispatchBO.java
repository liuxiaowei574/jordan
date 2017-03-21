package com.nuctech.ls.model.bo.warehouse;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[调度分析方案表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_WAREHOUSE_SUGGESTION_DEVICE_DISPATCH")
public class LsWarehouseSuggestionDeviceDispatchBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5462219079761362697L;

    /**
     * 缺省的构造函数
     */
    public LsWarehouseSuggestionDeviceDispatchBO() {
        super();
    }

    /* 方案主键 */
    private String suggestionId;

    /* 申请主键 */
    private String applicationId;

    /* 申请节点编号 */
    private String toPort;

    /* 终端数量 */
    private String deviceNumber;

    /* 子锁数量 */
    private String esealNumber;

    /* 传感器数量 */
    private String sensorNumber;

    /* 其他数量 */
    private String otherNumber;

    /*
     * 迁出节点
     * 迁出口岸编号
     */
    private String fromPort;

    /* 运输时间 */
    private Date transferTime;

    /* 节点距离 */
    private String portDistance;

    /* 终端预留数量 */
    private String keepDeviceNumber;

    /* 子锁预留数量 */
    private String keepEsealNumber;

    /* 传感器预留数量 */
    private String keepSensorNumber;

    /* 迁出节点终端空闲数量 */
    private String freeDeviceNumber;

    /* 迁出节点子锁空闲数 */
    private String freeEsealNumber;

    /* 迁出节点传感器空闲数 */
    private String freeSensorNumber;

    /* 迁出节点终端总数 */
    private String fromPortDeviceTotal;

    /* 迁出节点子锁总数 */
    private String fromPortEsealTotal;

    /* 迁出节点传感器总数 */
    private String fromPortSensorTotal;

    /* 迁出节点终端需求数 */
    private String fromPortDeviceRequirement;

    /* 迁出节点子锁需求数 */
    private String fromPortEsealRequirement;

    /* 迁出节点传感器需求数 */
    private String fromPortSensorRequirement;

    @Id
    @Column(name = "SUGGESTION_ID", nullable = false, length = 50)
    public String getSuggestionId() {
        return this.suggestionId;
    }

    public void setSuggestionId(String suggestionId) {
        this.suggestionId = suggestionId;
    }

    @Column(name = "APPLICATION_ID", nullable = false, length = 50)
    public String getApplicationId() {
        return this.applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    @Column(name = "TO_PORT", nullable = true, length = 50)
    public String getToPort() {
        return this.toPort;
    }

    public void setToPort(String toPort) {
        this.toPort = toPort;
    }

    @Column(name = "DEVICE_NUMBER", nullable = true, length = 20)
    public String getDeviceNumber() {
        return this.deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    @Column(name = "ESEAL_NUMBER", nullable = true, length = 20)
    public String getEsealNumber() {
        return this.esealNumber;
    }

    public void setEsealNumber(String esealNumber) {
        this.esealNumber = esealNumber;
    }

    @Column(name = "SENSOR_NUMBER", nullable = true, length = 20)
    public String getSensorNumber() {
        return this.sensorNumber;
    }

    public void setSensorNumber(String sensorNumber) {
        this.sensorNumber = sensorNumber;
    }

    @Column(name = "OTHER_NUMBER", nullable = true, length = 20)
    public String getOtherNumber() {
        return this.otherNumber;
    }

    public void setOtherNumber(String otherNumber) {
        this.otherNumber = otherNumber;
    }

    @Column(name = "FROM_PORT", nullable = true, length = 50)
    public String getFromPort() {
        return this.fromPort;
    }

    public void setFromPort(String fromPort) {
        this.fromPort = fromPort;
    }

    @Column(name = "TRANSFER_TIME", nullable = true)
    public Date getTransferTime() {
        return this.transferTime;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    @Column(name = "PORT_DISTANCE", nullable = true, length = 20)
    public String getPortDistance() {
        return this.portDistance;
    }

    public void setPortDistance(String portDistance) {
        this.portDistance = portDistance;
    }

    @Column(name = "KEEP_DEVICE_NUMBER", nullable = true, length = 20)
    public String getKeepDeviceNumber() {
        return this.keepDeviceNumber;
    }

    public void setKeepDeviceNumber(String keepDeviceNumber) {
        this.keepDeviceNumber = keepDeviceNumber;
    }

    @Column(name = "KEEP_ESEAL_NUMBER", nullable = true, length = 20)
    public String getKeepEsealNumber() {
        return this.keepEsealNumber;
    }

    public void setKeepEsealNumber(String keepEsealNumber) {
        this.keepEsealNumber = keepEsealNumber;
    }

    @Column(name = "KEEP_SENSOR_NUMBER", nullable = true, length = 20)
    public String getKeepSensorNumber() {
        return this.keepSensorNumber;
    }

    public void setKeepSensorNumber(String keepSensorNumber) {
        this.keepSensorNumber = keepSensorNumber;
    }

    @Column(name = "FREE_DEVICE_NUMBER", nullable = true, length = 20)
    public String getFreeDeviceNumber() {
        return this.freeDeviceNumber;
    }

    public void setFreeDeviceNumber(String freeDeviceNumber) {
        this.freeDeviceNumber = freeDeviceNumber;
    }

    @Column(name = "FREE_ESEAL_NUMBER", nullable = true, length = 20)
    public String getFreeEsealNumber() {
        return this.freeEsealNumber;
    }

    public void setFreeEsealNumber(String freeEsealNumber) {
        this.freeEsealNumber = freeEsealNumber;
    }

    @Column(name = "FREE_SENSOR_NUMBER", nullable = true, length = 20)
    public String getFreeSensorNumber() {
        return this.freeSensorNumber;
    }

    public void setFreeSensorNumber(String freeSensorNumber) {
        this.freeSensorNumber = freeSensorNumber;
    }

    @Column(name = "FROM_PORT_DEVICE_TOTAL", nullable = true, length = 20)
    public String getFromPortDeviceTotal() {
        return this.fromPortDeviceTotal;
    }

    public void setFromPortDeviceTotal(String fromPortDeviceTotal) {
        this.fromPortDeviceTotal = fromPortDeviceTotal;
    }

    @Column(name = "FROM_PORT_ESEAL_TOTAL", nullable = true, length = 20)
    public String getFromPortEsealTotal() {
        return this.fromPortEsealTotal;
    }

    public void setFromPortEsealTotal(String fromPortEsealTotal) {
        this.fromPortEsealTotal = fromPortEsealTotal;
    }

    @Column(name = "FROM_PORT_SENSOR_TOTAL", nullable = true, length = 20)
    public String getFromPortSensorTotal() {
        return this.fromPortSensorTotal;
    }

    public void setFromPortSensorTotal(String fromPortSensorTotal) {
        this.fromPortSensorTotal = fromPortSensorTotal;
    }

    @Column(name = "FROM_PORT_DEVICE_REQUIREMENT", nullable = true, length = 20)
    public String getFromPortDeviceRequirement() {
        return this.fromPortDeviceRequirement;
    }

    public void setFromPortDeviceRequirement(String fromPortDeviceRequirement) {
        this.fromPortDeviceRequirement = fromPortDeviceRequirement;
    }

    @Column(name = "FROM_PORT_ESEAL_REQUIREMENT", nullable = true, length = 20)
    public String getFromPortEsealRequirement() {
        return this.fromPortEsealRequirement;
    }

    public void setFromPortEsealRequirement(String fromPortEsealRequirement) {
        this.fromPortEsealRequirement = fromPortEsealRequirement;
    }

    @Column(name = "FROM_PORT_SENSOR_REQUIREMENT", nullable = true, length = 20)
    public String getFromPortSensorRequirement() {
        return this.fromPortSensorRequirement;
    }

    public void setFromPortSensorRequirement(String fromPortSensorRequirement) {
        this.fromPortSensorRequirement = fromPortSensorRequirement;
    }
}
