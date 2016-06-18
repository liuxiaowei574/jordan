package com.nuctech.ls.model.bo.warehouse;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 业务对象处理的实体-[设备调配明细表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_WAREHOUSE_DISPATCH_DETAIL")
public class LsWarehouseDispatchDetailBO
{
    /**
     * 缺省的构造函数
     */
    public LsWarehouseDispatchDetailBO()
    {
        super();
    }

    /* 调配明细主键 */
    private String detailId;

    /* 调配主键 */
    private String dispatchId;

    /* 设备主键
    主锁、子锁、传感器对应信息表中的主键 */
    private String deviceId;

    /* 设备编号
    主锁、子锁、传感器的编号 */
    private String deviceNumber;

    /* 设备类型 */
    private String deviceType;

    /* 接收人 */
    private String recviceUser;

    /* 接收时间 */
    private Date recviceTime;

    /* 接收状态
    0. 未接收
    1. 已接收 */
    private String recviceStatus;

    @Id
    @Column(name = "DETAIL_ID", nullable = false, length = 50)
    public String getDetailId()
    {
        return this.detailId;
    }

    public void setDetailId(String detailId)
    {
        this.detailId = detailId;
    }

    @Column(name = "DISPATCH_ID", nullable = false, length = 50)
    public String getDispatchId()
    {
        return this.dispatchId;
    }

    public void setDispatchId(String dispatchId)
    {
        this.dispatchId = dispatchId;
    }

    @Column(name = "DEVICE_ID", nullable = false, length = 50)
    public String getDeviceId()
    {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    @Column(name = "DEVICE_NUMBER", nullable = true, length = 50)
    public String getDeviceNumber()
    {
        return this.deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber)
    {
        this.deviceNumber = deviceNumber;
    }

    @Column(name = "DEVICE_TYPE", nullable = true, length = 20)
    public String getDeviceType()
    {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    @Column(name = "RECVICE_USER", nullable = true, length = 50)
    public String getRecviceUser()
    {
        return this.recviceUser;
    }

    public void setRecviceUser(String recviceUser)
    {
        this.recviceUser = recviceUser;
    }

    @Column(name = "RECVICE_TIME", nullable = true)
    public Date getRecviceTime()
    {
        return this.recviceTime;
    }

    public void setRecviceTime(Date recviceTime)
    {
        this.recviceTime = recviceTime;
    }

    @Column(name = "RECVICE_STATUS", nullable = true, length = 2)
    public String getRecviceStatus()
    {
        return this.recviceStatus;
    }

    public void setRecviceStatus(String recviceStatus)
    {
        this.recviceStatus = recviceStatus;
    }
}
