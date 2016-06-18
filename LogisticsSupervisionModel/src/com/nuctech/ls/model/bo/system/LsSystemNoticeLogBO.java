package com.nuctech.ls.model.bo.system;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * 业务对象处理的实体-[通知接收记录]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_SYSTEM_NOTICE_LOG")
public class LsSystemNoticeLogBO {
	/**
	 * 缺省的构造函数
	 */
	public LsSystemNoticeLogBO() {
		super();
	}

	/* 通知接收主键 */
	private String noticeRevId;

	/* 通知编号 */
	private String noticeId;

	/* 接收人 */
	private String receiveUser;

	/* 接收时间 */
	private Date receiveTime;

	/* 处理方式  0:未处理；1：已处理*/
	private String dealType;

	@Id
	@Column(name = "NOTICE_REV_ID", nullable = false, length = 50)
	public String getNoticeRevId() {
		return this.noticeRevId;
	}

	public void setNoticeRevId(String noticeRevId) {
		this.noticeRevId = noticeRevId;
	}

	@Column(name = "NOTICE_ID", nullable = true, length = 50)
	public String getNoticeId() {
		return this.noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
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

	@Column(name = "DEAL_TYPE", nullable = true, length = 2)
	public String getDealType() {
		return this.dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}
}
