package com.nuctech.ls.model.bo.monitor;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * 业务对象处理的实体-[在行程中，如遇到中途检查过程，作为检查记录存在]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_MONITOR_CHECK")
public class LsMonitorCheckBO {
	/**
	 * 缺省的构造函数
	 */
	public LsMonitorCheckBO() {
		super();
	}

	/* 检查主键 */
	private String checkId;

	/* 行程主键 */
	private String tripId;

	/* 检查地点 */
	private String checkPort;

	/* 检查时间 */
	private Date checkTime;

	/* 检查人员 */
	private String checkUser;

	/* 检查结果 */
	private String checkResult;

	/* 检查备注 */
	private String checkDesc;

	@Id
	@Column(name = "CHECK_ID", nullable = false, length = 50)
	public String getCheckId() {
		return this.checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	@Column(name = "TRIP_ID", nullable = true, length = 50)
	public String getTripId() {
		return this.tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	@Column(name = "CHECK_PORT", nullable = true, length = 50)
	public String getCheckPort() {
		return this.checkPort;
	}

	public void setCheckPort(String checkPort) {
		this.checkPort = checkPort;
	}

	
	@Column(name = "CHECK_TIME", nullable = true)
	public Date getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	@Column(name = "CHECK_USER", nullable = true, length = 50)
	public String getCheckUser() {
		return this.checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	@Column(name = "CHECK_RESULT", nullable = true, length = 2)
	public String getCheckResult() {
		return this.checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	@Column(name = "CHECK_DESC", nullable = true, length = 200)
	public String getCheckDesc() {
		return this.checkDesc;
	}

	public void setCheckDesc(String checkDesc) {
		this.checkDesc = checkDesc;
	}
}
