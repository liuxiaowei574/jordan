package com.nuctech.ls.model.vo.warehouse;

import java.io.Serializable;
import java.util.Date;

import com.nuctech.util.LoginSystem;

/**
 * 调度申请和用户表（调度申请页面）
 * @author zhaosuyang
 *
 */
public class ApplicationUserVO implements Serializable{
    private static final long serialVersionUID = 1L;
    
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

    /*
     * 申请状态 1. 已申请 2. 已处理 3. 已完成
     */
    private String applyStatus;

    /* 处理人 */
    private String dealUser;

    /* 处理时间 */
    private Date dealTime;

    /* 完成时间 */
    private Date finishTime;
    
    
    /* 用户主键 */
    private String userId;

    /* 用户名 */
    private String userAccount;

    /* 密码 */
    private String userPassword;

    /* 姓名 */
    private String userName;

    /* 电话 */
    private String userPhone;

    /* 邮箱 */
    private String userEmail;

    /* 地址 */
    private String userAddress;

    /* 登录系统 */
    private LoginSystem logonSystem;

    /* 登录时间 */
    private Date logonTime;

    /* 登出时间 */
    private Date logoutTime;

    /* IP_ADDRESS */
    private String ipAddress;

    /* TOKEN */
    private String token;

    /* 有效标记 1:有效; 0:无效 */
    private String isEnable;

    /* 级别 */
    private String level;

    /* 职位 */
    private String position;

    /** 登录地点 */
    private String logLocation;

    
    public String getApplicationId() {
        return applicationId;
    }

    
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    
    public String getApplcationPort() {
        return applcationPort;
    }

    
    public void setApplcationPort(String applcationPort) {
        this.applcationPort = applcationPort;
    }

    
    public String getApplcationPortName() {
        return applcationPortName;
    }

    
    public void setApplcationPortName(String applcationPortName) {
        this.applcationPortName = applcationPortName;
    }

    
    public String getDeviceNumber() {
        return deviceNumber;
    }

    
    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    
    public String getEsealNumber() {
        return esealNumber;
    }

    
    public void setEsealNumber(String esealNumber) {
        this.esealNumber = esealNumber;
    }

    
    public String getSensorNumber() {
        return sensorNumber;
    }

    
    public void setSensorNumber(String sensorNumber) {
        this.sensorNumber = sensorNumber;
    }

    
    public String getOtherNumber() {
        return otherNumber;
    }

    
    public void setOtherNumber(String otherNumber) {
        this.otherNumber = otherNumber;
    }

    
    public String getApplyUser() {
        return applyUser;
    }

    
    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    
    public Date getApplyTime() {
        return applyTime;
    }

    
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    
    public String getApplyStatus() {
        return applyStatus;
    }

    
    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    
    public String getDealUser() {
        return dealUser;
    }

    
    public void setDealUser(String dealUser) {
        this.dealUser = dealUser;
    }

    
    public Date getDealTime() {
        return dealTime;
    }

    
    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    
    public Date getFinishTime() {
        return finishTime;
    }

    
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    
    public String getUserId() {
        return userId;
    }

    
    public void setUserId(String userId) {
        this.userId = userId;
    }

    
    public String getUserAccount() {
        return userAccount;
    }

    
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    
    public String getUserPassword() {
        return userPassword;
    }

    
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    
    public String getUserName() {
        return userName;
    }

    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    
    public String getUserPhone() {
        return userPhone;
    }

    
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    
    public String getUserEmail() {
        return userEmail;
    }

    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    
    public String getUserAddress() {
        return userAddress;
    }

    
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    
    public LoginSystem getLogonSystem() {
        return logonSystem;
    }

    
    public void setLogonSystem(LoginSystem logonSystem) {
        this.logonSystem = logonSystem;
    }

    
    public Date getLogonTime() {
        return logonTime;
    }

    
    public void setLogonTime(Date logonTime) {
        this.logonTime = logonTime;
    }

    
    public Date getLogoutTime() {
        return logoutTime;
    }

    
    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    
    public String getIpAddress() {
        return ipAddress;
    }

    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    
    public String getToken() {
        return token;
    }

    
    public void setToken(String token) {
        this.token = token;
    }

    
    public String getIsEnable() {
        return isEnable;
    }

    
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    
    public String getLevel() {
        return level;
    }

    
    public void setLevel(String level) {
        this.level = level;
    }

    
    public String getPosition() {
        return position;
    }

    
    public void setPosition(String position) {
        this.position = position;
    }

    
    public String getLogLocation() {
        return logLocation;
    }

    
    public void setLogLocation(String logLocation) {
        this.logLocation = logLocation;
    }
    
}
