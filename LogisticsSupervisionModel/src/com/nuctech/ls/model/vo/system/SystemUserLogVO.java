package com.nuctech.ls.model.vo.system;

import java.io.Serializable;
import java.util.Date;

import com.nuctech.util.LoginSystem;

/**
 * 业务对象处理的实体-[用户访问记录表]
 *
 * @author： nuctech
 */
public class SystemUserLogVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8168282791715918848L;

    /**
     * 缺省的构造函数
     */
    public SystemUserLogVO() {
        super();
    }

    /* 访问记录主键 */
    private String userLogId;

    /* 登录系统 */
    private LoginSystem logonSystem;

    /* 登录时间 */
    private Date logonTime;

    /* 登录IP地址 */
    private String ipAddress;

    /* 登录用户 */
    private String logUser;

    private String logUserName;
    private String userName;

    /* 登出时间 */
    private Date logoutTime;

    /**
     * 登出类型，参照LogoutType。 0, 自己登出，1.T出，2.会话超时。
     */
    private String logoutType;

    public SystemUserLogVO(String userLogId, LoginSystem logonSystem, Date logonTime, String ipAddress, String logUser,
            String logUserName, String userName, Date logoutTime, String logoutType) {
        super();
        this.userLogId = userLogId;
        this.logonSystem = logonSystem;
        this.logonTime = logonTime;
        this.ipAddress = ipAddress;
        this.logUser = logUser;
        this.logUserName = logUserName;
        this.userName = userName;
        this.logoutTime = logoutTime;
        this.logoutType = logoutType;
    }

    public String getUserLogId() {
        return this.userLogId;
    }

    public void setUserLogId(String userLogId) {
        this.userLogId = userLogId;
    }

    public LoginSystem getLogonSystem() {
        return this.logonSystem;
    }

    public void setLogonSystem(LoginSystem logonSystem) {
        this.logonSystem = logonSystem;
    }

    public Date getLogonTime() {
        return this.logonTime;
    }

    public void setLogonTime(Date logonTime) {
        this.logonTime = logonTime;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLogUser() {
        return this.logUser;
    }

    public void setLogUser(String logUser) {
        this.logUser = logUser;
    }

    public Date getLogoutTime() {
        return this.logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    public String getLogoutType() {
        return this.logoutType;
    }

    public void setLogoutType(String logoutType) {
        this.logoutType = logoutType;
    }

    public String getLogUserName() {
        return logUserName;
    }

    public void setLogUserName(String logUserName) {
        this.logUserName = logUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
