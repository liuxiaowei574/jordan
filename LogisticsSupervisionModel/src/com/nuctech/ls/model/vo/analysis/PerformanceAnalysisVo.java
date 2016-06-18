package com.nuctech.ls.model.vo.analysis;

public class PerformanceAnalysisVo {
	/**
	 * 用户Id
	 */
	private String userId;
	/**
	 * 用户帐户
	 */
	private String userAccount;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 在线时长，精确到分钟
	 */
	private long onlineTime;
	/**
	 * 处理方式 0-转发 1-处理
	 */
	private String dealMethod;
	/**
	 * 转发总数
	 */
	private long forwardingAlarmTotalAmount;
	/**
	 * 转发报警总时长，精确到分钟
	 */
	private long forwardingDealAlarmTime;
	/**
	 * 处理报警总数
	 */
	private long dealAlarmTotalAmount;
	/**
	 * 处理报警总时长，精确到分钟
	 */
	private long dealAlarmTime;
	
	
	public PerformanceAnalysisVo(String userId, String userAccount, String userName, long onlineTime) {
		super();
		this.userId = userId;
		this.userAccount = userAccount;
		this.userName = userName;
		this.onlineTime = onlineTime;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public long getOnlineTime() {
		return onlineTime;
	}
	public void setOnlineTime(long onlineTime) {
		this.onlineTime = onlineTime;
	}
	public long getDealAlarmTotalAmount() {
		return dealAlarmTotalAmount;
	}
	public void setDealAlarmTotalAmount(long dealAlarmTotalAmount) {
		this.dealAlarmTotalAmount = dealAlarmTotalAmount;
	}
	public long getDealAlarmTime() {
		return dealAlarmTime;
	}
	public void setDealAlarmTime(long dealAlarmTime) {
		this.dealAlarmTime = dealAlarmTime;
	}
	public String getDealMethod() {
		return dealMethod;
	}
	public void setDealMethod(String dealMethod) {
		this.dealMethod = dealMethod;
	}
	public long getForwardingAlarmTotalAmount() {
		return forwardingAlarmTotalAmount;
	}
	public void setForwardingAlarmTotalAmount(long forwardingAlarmTotalAmount) {
		this.forwardingAlarmTotalAmount = forwardingAlarmTotalAmount;
	}
	public long getForwardingDealAlarmTime() {
		return forwardingDealAlarmTime;
	}
	public void setForwardingDealAlarmTime(long forwardingDealAlarmTime) {
		this.forwardingDealAlarmTime = forwardingDealAlarmTime;
	}	
}