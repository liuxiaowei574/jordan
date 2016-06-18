package com.nuctech.ls.model.bo.system;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;



import com.nuctech.util.LoginSystem;

/**
 * 业务对象处理的实体-[用户表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_SYSTEM_USER")
public class LsSystemUserBO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8680511095014414878L;

	/**
	 * 默认密码
	 */
	public static final String DEFAULT_PASSWORD = "123456";
	/**
	 * 缺省的构造函数
	 */
	public LsSystemUserBO() {
		super();
		userId = UUID.randomUUID().toString();
	}

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

    /* 有效标记  1:有效; 0:无效*/
    private String isEnable;


	@Id
	@Column(name = "USER_ID", nullable = false, length = 50)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "USER_ACCOUNT", nullable = true, length = 100 , unique = true)
	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	@Column(name = "USER_PASSWORD", nullable = true, length = 50)
	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	@Column(name = "USER_NAME", nullable = true, length = 100)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "USER_PHONE", nullable = true, length = 20)
	public String getUserPhone() {
		return this.userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	@Column(name = "USER_EMAIL", nullable = true, length = 100)
	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Column(name = "USER_ADDRESS", nullable = true, length = 200)
	public String getUserAddress() {
		return this.userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
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

	
	@Column(name = "LOGOUT_TIME", nullable = true)
	public Date getLogoutTime() {
		return this.logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	@Column(name = "IP_ADDRESS", nullable = true, length = 20)
	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Column(name = "TOKEN", nullable = true, length = 255)
	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Column(name = "IS_ENABLE", nullable = true, length = 2)
	public String getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	@Override
	public String toString() {
		return "LsSystemUserBO [userId=" + userId + ", userAccount="
				+ userAccount + ", userPassword=" + userPassword
				+ ", userName=" + userName + ", userPhone=" + userPhone
				+ ", userEmail=" + userEmail + ", userAddress=" + userAddress
				+ ", logonSystem=" + logonSystem + ", logonTime=" + logonTime
				+ ", logoutTime=" + logoutTime + ", ipAddress=" + ipAddress
				+ ", token=" + token + ", isEnable=" + isEnable + "]";
	}
	
	
}
