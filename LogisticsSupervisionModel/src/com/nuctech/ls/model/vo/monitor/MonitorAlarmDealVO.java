package com.nuctech.ls.model.vo.monitor;

import java.util.Date;

import javax.persistence.Id;

/**
 * 业务对象处理的实体-报警处理VO类
 *
 * @author： nuctech
 */
public class MonitorAlarmDealVO {

    /**
     * 缺省的构造函数
     */
    public MonitorAlarmDealVO() {
        super();
    }

    public MonitorAlarmDealVO(String dealId, String alarmId, String recipientsUser, String recipientsUserName,
            Date receiveTime, String dealUser, String dealUserName, String dealMethod, Date dealTime, String dealResult,
            String dealDesc) {
        super();
        this.dealId = dealId;
        this.alarmId = alarmId;
        this.recipientsUser = recipientsUser;
        this.recipientsUserName = recipientsUserName;
        this.receiveTime = receiveTime;
        this.dealUser = dealUser;
        this.dealUserName = dealUserName;
        this.dealMethod = dealMethod;
        this.dealTime = dealTime;
        this.dealResult = dealResult;
        this.dealDesc = dealDesc;
    }

    /* 报警处理主键 */
    private String dealId;

    /* 报警主键 */
    private String alarmId;

    /* 接收人 */
    private String recipientsUser;

    /* 接收人姓名 */
    private String recipientsUserName;

    /*
     * 接收时间 只记录最新人员的接收时间
     */
    private Date receiveTime;

    /* 处理人 */
    private String dealUser;

    /* 处理人姓名 */
    private String dealUserName;

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
    public String getDealId() {
        return this.dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getAlarmId() {
        return this.alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getRecipientsUser() {
        return this.recipientsUser;
    }

    public void setRecipientsUser(String recipientsUser) {
        this.recipientsUser = recipientsUser;
    }

    public Date getReceiveTime() {
        return this.receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getDealUser() {
        return this.dealUser;
    }

    public void setDealUser(String dealUser) {
        this.dealUser = dealUser;
    }

    public String getDealMethod() {
        return this.dealMethod;
    }

    public void setDealMethod(String dealMethod) {
        this.dealMethod = dealMethod;
    }

    public Date getDealTime() {
        return this.dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public String getDealResult() {
        return this.dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    public String getDealDesc() {
        return this.dealDesc;
    }

    public void setDealDesc(String dealDesc) {
        this.dealDesc = dealDesc;
    }

    public String getRecipientsUserName() {
        return recipientsUserName;
    }

    public void setRecipientsUserName(String recipientsUserName) {
        this.recipientsUserName = recipientsUserName;
    }

    public String getDealUserName() {
        return dealUserName;
    }

    public void setDealUserName(String dealUserName) {
        this.dealUserName = dealUserName;
    }
}
