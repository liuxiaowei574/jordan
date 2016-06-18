package com.nuctech.ls.model.bo.warehouse;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 业务对象处理的实体-[调度申请表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_WAREHOUSE_DEVICE_APPLICATION")
public class LsWarehouseDeviceApplicationBO
{
    /**
     * 缺省的构造函数
     */
    public LsWarehouseDeviceApplicationBO()
    {
        super();
    }

    /* 申请主键 */
    private String applicationId;

    /* 申请节点 */
    private String applcationPort;
    
    /* 申请节点名称 */
    private String applcationPortName;

    /* 终端数量 */
    private String deviceNumber;

    /* 子锁数量 */
    private String esealNumber;

    /* 传感器数量 */
    private String sensorNumber;

    /* 其他数量 */
    private String otherNumber;

    /* 申请人 */
    private String applyUser;

    /* 申请时间 */
    private Date applyTime;

    /* 申请状态
    1. 已申请
    2. 已处理
    3. 已完成 */
    private String applyStatus;

    /* 处理人 */
    private String dealUser;

    /* 处理时间 */
    private Date dealTime;

    /* 完成时间 */
    private Date finishTime;

    @Id
    @Column(name = "APPLICATION_ID", nullable = false, length = 50)
    public String getApplicationId()
    {
        return this.applicationId;
    }

    public void setApplicationId(String applicationId)
    {
        this.applicationId = applicationId;
    }

    @Column(name = "APPLCATION_PORT", nullable = true, length = 50)
    public String getApplcationPort()
    {
        return this.applcationPort;
    }

    public void setApplcationPort(String applcationPort)
    {
        this.applcationPort = applcationPort;
    }
    
    @Column(name = "APPLCATION_PORT_NAME", nullable = true, length = 50)
    public String getApplcationPortName()
    {
        return this.applcationPortName;
    }

    public void setApplcationPortName(String applcationPortName)
    {
        this.applcationPortName = applcationPortName;
    }

    @Column(name = "DEVICE_NUMBER", nullable = true, length = 20)
    public String getDeviceNumber()
    {
        return this.deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber)
    {
        this.deviceNumber = deviceNumber;
    }

    @Column(name = "ESEAL_NUMBER", nullable = true, length = 20)
    public String getEsealNumber()
    {
        return this.esealNumber;
    }

    public void setEsealNumber(String esealNumber)
    {
        this.esealNumber = esealNumber;
    }

    @Column(name = "SENSOR_NUMBER", nullable = true, length = 20)
    public String getSensorNumber()
    {
        return this.sensorNumber;
    }

    public void setSensorNumber(String sensorNumber)
    {
        this.sensorNumber = sensorNumber;
    }

    @Column(name = "OTHER_NUMBER", nullable = true, length = 20)
    public String getOtherNumber()
    {
        return this.otherNumber;
    }

    public void setOtherNumber(String otherNumber)
    {
        this.otherNumber = otherNumber;
    }

    @Column(name = "APPLY_USER", nullable = true, length = 50)
    public String getApplyUser()
    {
        return this.applyUser;
    }

    public void setApplyUser(String applyUser)
    {
        this.applyUser = applyUser;
    }

    @Column(name = "APPLY_TIME", nullable = true)
    public Date getApplyTime()
    {
        return this.applyTime;
    }

    public void setApplyTime(Date applyTime)
    {
        this.applyTime = applyTime;
    }

    @Column(name = "APPLY_STATUS", nullable = true, length = 2)
    public String getApplyStatus()
    {
        return this.applyStatus;
    }

    public void setApplyStatus(String applyStatus)
    {
        this.applyStatus = applyStatus;
    }

    @Column(name = "DEAL_USER", nullable = true, length = 50)
    public String getDealUser()
    {
        return this.dealUser;
    }

    public void setDealUser(String dealUser)
    {
        this.dealUser = dealUser;
    }

    @Column(name = "DEAL_TIME", nullable = true)
    public Date getDealTime()
    {
        return this.dealTime;
    }

    public void setDealTime(Date dealTime)
    {
        this.dealTime = dealTime;
    }

    @Column(name = "FINISH_TIME", nullable = true)
    public Date getFinishTime()
    {
        return this.finishTime;
    }

    public void setFinishTime(Date finishTime)
    {
        this.finishTime = finishTime;
    }
}
