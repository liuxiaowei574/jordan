package com.nuctech.ls.model.vo.system;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志VO类
 * 
 * @author liushaowei
 *
 */
public class SystemOperateLogVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2132605739297490418L;

    /**
     * 缺省的构造函数
     */
    public SystemOperateLogVO() {
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
     * 操作类型 0. 推送 1. 处理
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

    /* 操作人姓名 */
    private String operateUserName;

    /* 操作人姓名 */
    private String userName;

    public String getOperateId() {
        return this.operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }

    public Date getOperateTime() {
        return this.operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateUser() {
        return this.operateUser;
    }

    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    public String getOperateClass() {
        return this.operateClass;
    }

    public void setOperateClass(String operateClass) {
        this.operateClass = operateClass;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getOperateType() {
        return this.operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getReceiveUser() {
        return this.receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public Date getReceiveTime() {
        return this.receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getTransferData() {
        return this.transferData;
    }

    public void setTransferData(String transferData) {
        this.transferData = transferData;
    }

    public String getOperateDesc() {
        return this.operateDesc;
    }

    public void setOperateDesc(String operateDesc) {
        this.operateDesc = operateDesc;
    }

    public String getOperateUserName() {
        return operateUserName;
    }

    public void setOperateUserName(String operateUserName) {
        this.operateUserName = operateUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
