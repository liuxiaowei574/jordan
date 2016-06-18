package com.nuctech.ls.model.vo.system;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>通知接收日志 页面VO</p>
 * 创建时间：2016年5月28日
 */
public class SystemNoticeLogVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 202976598921289642L;
	
	/* 通知接收主键 */
	private String noticeRevId;

	/* 通知编号 */
	private String noticeId;

	/* 接收人 ID */
	private String receiveUser;
	
	/* 接收人 姓名 */
	private String receiveUserName;

	/* 接收时间 */
	private Date receiveTime;
	
	/* 处理方式  0:未处理；1：已处理*/
	private String dealType;

	public String getNoticeRevId() {
		return noticeRevId;
	}

	public void setNoticeRevId(String noticeRevId) {
		this.noticeRevId = noticeRevId;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}


	public String getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(String receiveUser) {
		this.receiveUser = receiveUser;
	}

	public String getReceiveUserName() {
		return receiveUserName;
	}

	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}
	
	

}
