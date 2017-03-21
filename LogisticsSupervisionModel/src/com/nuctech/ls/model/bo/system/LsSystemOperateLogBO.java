package com.nuctech.ls.model.bo.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[用户操作记录表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_SYSTEM_OPERATE_LOG")
public class LsSystemOperateLogBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2132605739297490418L;

    /**
     * 缺省的构造函数
     */
    public LsSystemOperateLogBO() {
        super();
    }

    /* 操作主键 */
    private String operateId;

    /* 操作时间 */
    private Date operateTime;

    /* 操作人 */
    private String operateUser;

    /* 操作类 */
    private String operateClass;

    /* IP地址 */
    private String ipAddress;

    /*
     * 操作类型
     * 0. 推送
     * 1. 处理
     */
    private String operateType;

    /* 接收人 */
    private String receiveUser;

    /* 接收时间 */
    private Date receiveTime;

    /* 传输数据 */
    private String transferData;

    /* 描述 */
    private String operateDesc;

    @Id
    @Column(name = "OPERATE_ID", nullable = false, length = 50)
    public String getOperateId() {
        return this.operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }

    @Column(name = "OPERATE_TIME", nullable = true)
    public Date getOperateTime() {
        return this.operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @Column(name = "OPERATE_USER", nullable = true, length = 50)
    public String getOperateUser() {
        return this.operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    @Column(name = "OPERATE_CLASS", nullable = true, length = 100)
    public String getOperateClass() {
        return this.operateClass;
    }

    public void setOperateClass(String operateClass) {
        this.operateClass = operateClass;
    }

    @Column(name = "IP_ADDRESS", nullable = true, length = 20)
    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Column(name = "OPERATE_TYPE", nullable = true, length = 50)
    public String getOperateType() {
        return this.operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    @Column(name = "RECEIVE_USER", nullable = true, length = 50)
    public String getReceiveUser() {
        return this.receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    @Column(name = "RECEIVE_TIME", nullable = true)
    public Date getReceiveTime() {
        return this.receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Column(name = "TRANSFER_DATA", nullable = true, length = 2000)
    public String getTransferData() {
        return this.transferData;
    }

    public void setTransferData(String transferData) {
        this.transferData = transferData;
    }

    @Column(name = "OPERATE_DESC", nullable = true, length = 2000)
    public String getOperateDesc() {
        return this.operateDesc;
    }

    public void setOperateDesc(String operateDesc) {
        this.operateDesc = operateDesc;
    }
}
