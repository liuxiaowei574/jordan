package com.nuctech.ls.model.bo.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[监管车辆的集装箱信息(非必填)]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_COMMON_CONTAINER")
public class LsCommonContainerBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7564183539671492367L;

    /**
     * 缺省的构造函数
     */
    public LsCommonContainerBO() {
        super();
    }

    /* 集装箱主键 */
    private String containerId;

    /* 主键 */
    private String vehicleId;

    /* 集装箱号 */
    private String containerNumber;

    @Id
    @Column(name = "CONTAINER_ID", nullable = false, length = 50)
    public String getContainerId() {
        return this.containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    @Column(name = "VEHICLE_ID", nullable = true, length = 50)
    public String getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Column(name = "CONTAINER_NUMBER", nullable = true, length = 50)
    public String getContainerNumber() {
        return this.containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }
}
