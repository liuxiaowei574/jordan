package com.nuctech.ls.model.bo.monitor;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[报警处理表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_MONITOR_ALARM_DEAL")
public class LsMonitorAlarmDealBO {
	/**
	 * 缺省的构造函数
	 */
	public LsMonitorAlarmDealBO() {
		super();
	}

	/* 报警处理主键 */
	private String dealId;

	/* 报警主键 */
	private String alarmId;

	/* 接收人 */
	private String recipientsUser;

	/*
	 * 接收时间 只记录最新人员的接收时间
	 */
	private Date receiveTime;

	/* 处理人 */
	private String dealUser;

	/*
	 * 处理方式 0-转发 1-处理
	 */
	private String dealMethod;

	/* 处理时间 */
	private Date dealTime;

	/* 处理结果 */
	private String dealResult;

	/* 备注 */
	private String dealDesc;

	@Id
	@Column(name = "DEAL_ID", nullable = false, length = 50)
	public String getDealId() {
		return this.dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	@Column(name = "ALARM_ID", nullable = true, length = 50)
	public String getAlarmId() {
		return this.alarmId;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}

	@Column(name = "RECIPIENTS_USER", nullable = true, length = 50)
	public String getRecipientsUser() {
		return this.recipientsUser;
	}

	public void setRecipientsUser(String recipientsUser) {
		this.recipientsUser = recipientsUser;
	}

	@Column(name = "RECEIVE_TIME", nullable = true)
	public Date getReceiveTime() {
		return this.receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	@Column(name = "DEAL_USER", nullable = true, length = 50)
	public String getDealUser() {
		return this.dealUser;
	}

	public void setDealUser(String dealUser) {
		this.dealUser = dealUser;
	}

	@Column(name = "DEAL_METHOD", nullable = true, length = 2)
	public String getDealMethod() {
		return this.dealMethod;
	}

	public void setDealMethod(String dealMethod) {
		this.dealMethod = dealMethod;
	}

	@Column(name = "DEAL_TIME", nullable = true)
	public Date getDealTime() {
		return this.dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	@Column(name = "DEAL_RESULT", nullable = true, length = 2)
	public String getDealResult() {
		return this.dealResult;
	}

	public void setDealResult(String dealResult) {
		this.dealResult = dealResult;
	}

	@Column(name = "DEAL_DESC", nullable = true, length = 200)
	public String getDealDesc() {
		return this.dealDesc;
	}

	public void setDealDesc(String dealDesc) {
		this.dealDesc = dealDesc;
	}
}
