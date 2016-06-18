package com.nuctech.ls.model.vo.system;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.nuctech.ls.model.bo.system.LsSystemFunctionsBO;
import com.nuctech.util.LoginSystem;

/**
 * 作者： 徐楠
 *
 * 描述：<p>系统用户登录相关Session信息</p>
 * 创建时间：2016年5月18日
 */
public class SessionUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7654166294200237250L;

	/* 用户主键 */
	private String userId;

	/* 用户名 */
	private String userAccount;

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
	
	/* 角色ID */
	private String roleId;
	
	/* 菜单List */
	private List<LsSystemFunctionsBO> systemFunctionList;
	
	/* 机构编码 */
	private String organizationId;

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

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public List<LsSystemFunctionsBO> getSystemFunctionList() {
		return systemFunctionList;
	}

	public void setSystemFunctionList(List<LsSystemFunctionsBO> systemFunctionList) {
		this.systemFunctionList = systemFunctionList;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	
}
