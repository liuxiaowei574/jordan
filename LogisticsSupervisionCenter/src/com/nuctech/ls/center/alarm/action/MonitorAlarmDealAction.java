package com.nuctech.ls.center.alarm.action;

import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Id;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmDealBO;
import com.nuctech.ls.model.service.MonitorAlarmDealService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;

/**
 * 报警处理弹出界面
 * @author liqingxian
 *
 */

@ParentPackage("json-default")
@Namespace("/alarmdeal")
public class MonitorAlarmDealAction extends LSBaseAction{

	
	private static final long serialVersionUID = 1L;

	@Resource
	private MonitorAlarmDealService monitorAlarmDealService;
	
	@Action(value = "alarmDealModalShow", results = {
			@Result(name = "success", location = "/monitor/alarm/alarmHandler.jsp"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String alarmDealModalShow() {
		lsMonitorAlarmDealBO = new LsMonitorAlarmDealBO();
		return SUCCESS;
	}
	
	@Action(value = "addAlarmDeal", results = {
			@Result(name = "success", type = "json"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String addAlarmDeal() {
		//if(lsMonitorAlarmDealBO!=null){
				lsMonitorAlarmDealBO.setAlarmId(alarmId);
				System.out.println(alarmId);
				lsMonitorAlarmDealBO.setDealId(generatePrimaryKey());
				if(!"".equalsIgnoreCase(lsMonitorAlarmDealBO.getRecipientsUser())||null!=lsMonitorAlarmDealBO.getRecipientsUser()){
					SessionUser sessionUser = (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
					lsMonitorAlarmDealBO.setRecipientsUser(sessionUser.getUserId());
					lsMonitorAlarmDealBO.setDealUser(sessionUser.getUserId());
				}
				lsMonitorAlarmDealBO.setDealTime(new Date());
				lsMonitorAlarmDealBO.setDealDesc(dealDesc);
				System.out.println(dealDesc);
				lsMonitorAlarmDealBO.setReceiveTime(new Date());
				lsMonitorAlarmDealBO.setDealMethod(dealMethod);
				lsMonitorAlarmDealBO.setDealResult(dealResult);
				monitorAlarmDealService.addAlarmDeal(lsMonitorAlarmDealBO);
		//	}
		return SUCCESS;
	}
	
	private LsMonitorAlarmDealBO lsMonitorAlarmDealBO;

	public LsMonitorAlarmDealBO getLsMonitorAlarmDealBO() {
		return lsMonitorAlarmDealBO;
	}

	public void setLsMonitorAlarmDealBO(LsMonitorAlarmDealBO lsMonitorAlarmDealBO) {
		this.lsMonitorAlarmDealBO = lsMonitorAlarmDealBO;
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
