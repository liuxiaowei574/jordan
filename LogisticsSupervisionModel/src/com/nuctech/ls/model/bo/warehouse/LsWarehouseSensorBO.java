package com.nuctech.ls.model.bo.warehouse;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[传感器表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_WAREHOUSE_SENSOR")
public class LsWarehouseSensorBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2310278359797150488L;

    /**
     * 缺省的构造函数
     */
    public LsWarehouseSensorBO() {
        super();
    }

    /* 关锁主键 */
    private String sensorId;

    /* 传感器编号 */
    private String sensorNumber;

    /* 所属节点 */
    private String belongTo;

    /*
     * 传感器状态 维修、损坏、报废等
     */
    private String sensorStatus;

    /* 传感器类型 */
    private String sensorType;

    /* 创建人 */
    private String createUser;

    /* 创建时间 */
    private Date createTime;

    @Id
    @Column(name = "SENSOR_ID", nullable = false, length = 50)
    public String getSensorId() {
        return this.sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    @Column(name = "SENSOR_NUMBER", nullable = true, length = 50)
    public String getSensorNumber() {
        return this.sensorNumber;
    }

    public void setSensorNumber(String sensorNumber) {
        this.sensorNumber = sensorNumber;
    }

    @Column(name = "BELONG_TO", nullable = true, length = 50)
    public String getBelongTo() {
        return this.belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    @Column(name = "SENSOR_STATUS", nullable = true, length = 2)
    public String getSensorStatus() {
        return this.sensorStatus;
    }

    public void setSensorStatus(String sensorStatus) {
        this.sensorStatus = sensorStatus;
    }

    @Column(name = "SENSOR_TYPE", nullable = true, length = 2)
    public String getSensorType() {
        return this.sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    @Column(name = "CREATE_USER", nullable = true, length = 50)
    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "LsWarehouseSensorBO [sensorId=" + sensorId + ", sensorNumber=" + sensorNumber + ", belongTo=" + belongTo
                + ", sensorStatus=" + sensorStatus + ", sensorType=" + sensorType + ", createUser=" + createUser
                + ", createTime=" + createTime + "]";
    }

}
