package com.nuctech.ls.model.bo.warehouse;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[设备调配记录表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_WAREHOUSE_DEVICE_DISPATCH")
public class LsWarehouseDeviceDispatchBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2214414513325602268L;

    /**
     * 缺省的构造函数
     */
    public LsWarehouseDeviceDispatchBO() {
        super();
    }

    /* 调配主键 */
    private String dispatchId;

    /* 申请主键 */
    private String applicationId;

    /* 申请节点编号 */
    private String toPort;

    /*
     * 迁出节点 迁出口岸编号
     */
    private String fromPort;

    /* 终端数量 */
    private String deviceNumber;

    /* 子锁数量 */
    private String esealNumber;

    /* 传感器数量 */
    private String sensorNumber;

    /* 其他数量 */
    private String otherNumber;

    /*
     * 调配人
     */
    private String dispatchUser;

    /*
     * 调配人角色
     */
    private String dispatchRole;

    /* 调配时间 */
    private Date dispatchTime;

    /*
     * 调度状态 0、未调度 1、调度完成 2、调度驳回
     */
    private String dispatchStatus;

    @Id
    @Column(name = "DISPATCH_ID", nullable = false, length = 50)
    public String getDispatchId() {
        return this.dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
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

    @Column(name = "FROM_PORT", nullable = true, length = 50)
    public String getFromPort() {
        return this.fromPort;
    }

    public void setFromPort(String fromPort) {
        this.fromPort = fromPort;
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

    @Column(name = "DISPATCH_USER", nullable = true, length = 50)
    public String getDispatchUser() {
        return this.dispatchUser;
    }

    public void setDispatchUser(String dispatchUser) {
        this.dispatchUser = dispatchUser;
    }

    @Column(name = "DISPATCH_TIME", nullable = true)
    public Date getDispatchTime() {
        return this.dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    @Column(name = "DISPATCH_STATUS", nullable = true, length = 2)
    public String getDispatchStatus() {
        return this.dispatchStatus;
    }

    public void setDispatchStatus(String dispatchStatus) {
        this.dispatchStatus = dispatchStatus;
    }

    @Column(name = "DISPATCH_ROLE", nullable = true, length = 50)
    public String getDispatchRole() {
        return dispatchRole;
    }

    public void setDispatchRole(String dispatchRole) {
        this.dispatchRole = dispatchRole;
    }

    @Override
    public String toString() {
        return "LsWarehouseDeviceDispatchBO [dispatchId=" + dispatchId + ", applicationId=" + applicationId
                + ", toPort=" + toPort + ", fromPort=" + fromPort + ", deviceNumber=" + deviceNumber + ", esealNumber="
                + esealNumber + ", sensorNumber=" + sensorNumber + ", otherNumber=" + otherNumber + ", dispatchUser="
                + dispatchUser + ", dispatchRole=" + dispatchRole + ", dispatchTime=" + dispatchTime
                + ", dispatchStatus=" + dispatchStatus + "]";
    }

}
