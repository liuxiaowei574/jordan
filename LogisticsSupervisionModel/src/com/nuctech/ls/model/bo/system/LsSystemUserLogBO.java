package com.nuctech.ls.model.bo.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.nuctech.util.LoginSystem;

/**
 * 业务对象处理的实体-[用户访问记录表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_SYSTEM_USER_LOG")
public class LsSystemUserLogBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8168282791715918848L;

    /**
     * 缺省的构造函数
     */
    public LsSystemUserLogBO() {
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

    @Transient
    private String logUserName;

    /* 登出时间 */
    private Date logoutTime;

    /**
     * 登出类型，参照LogoutType。 0, 自己登出，1.T出，2.会话超时。
     */
    private String logoutType;

    public LsSystemUserLogBO(String userLogId, Date logonTime, String ipAddress, String logUser, String logUserName) {
        super();
        this.userLogId = userLogId;
        this.logonTime = logonTime;
        this.ipAddress = ipAddress;
        this.logUser = logUser;
        this.logUserName = logUserName;
    }

    public LsSystemUserLogBO(String userLogId, LoginSystem logonSystem, Date logonTime, String ipAddress,
            String logUser, String logUserName, Date logoutTime, String logoutType) {
        super();
        this.userLogId = userLogId;
        this.logonSystem = logonSystem;
        this.logonTime = logonTime;
        this.ipAddress = ipAddress;
        this.logUser = logUser;
        this.logUserName = logUserName;
        this.logoutTime = logoutTime;
        this.logoutType = logoutType;
    }

    @Id
    @Column(name = "USER_LOG_ID", nullable = false, length = 50)
    public String getUserLogId() {
        return this.userLogId;
    }

    public void setUserLogId(String userLogId) {
        this.userLogId = userLogId;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "LOGON_SYSTEM", nullable = true, length = 100)
    public LoginSystem getLogonSystem() {
        return this.logonSystem;
    }

    public void setLogonSystem(LoginSystem logonSystem) {
        this.logonSystem = logonSystem;
    }

    @Column(name = "LOGON_TIME", nullable = true)
    public Date getLogonTime() {
        return this.logonTime;
    }

    public void setLogonTime(Date logonTime) {
        this.logonTime = logonTime;
    }

    @Column(name = "IP_ADDRESS", nullable = true, length = 20)
    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Column(name = "LOG_USER", nullable = true, length = 50)
    public String getLogUser() {
        return this.logUser;
    }

    public void setLogUser(String logUser) {
        this.logUser = logUser;
    }

    @Column(name = "LOGOUT_TIME", nullable = true)
    public Date getLogoutTime() {
        return this.logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    @Column(name = "LOGOUT_TYPE", nullable = true, length = 2)
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

}
