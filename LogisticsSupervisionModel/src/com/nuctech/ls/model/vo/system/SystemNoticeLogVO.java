package com.nuctech.ls.model.vo.system;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 通知接收日志 页面VO
 * </p>
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

    /* 处理方式 0:未处理；1：已处理 */
    private String dealType;

    /* 通知主题 */
    private String noticeTitle;

    /* 通知内容 */
    private String noticeContent;

    /* 发布时间 */
    private Date deployTime;

    /* 发布人 */
    private String publisher;

    /* 通知人员 */
    private String noticeUsers;

    /*
     * 通知完成状态 0.起草 1.发布 2.结束
     */
    private String noticeState;
    /*
     * 通知类型;0.普通通知，1.报警通知，2.行程通知，3.调度通知(0代表普通通知由用户手动添加，1，2，3为系统通知)
     */
    private String noticeType;

    /* 姓名 */
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Date getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(Date deployTime) {
        this.deployTime = deployTime;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getNoticeUsers() {
        return noticeUsers;
    }

    public void setNoticeUsers(String noticeUsers) {
        this.noticeUsers = noticeUsers;
    }

    public String getNoticeState() {
        return noticeState;
    }

    public void setNoticeState(String noticeState) {
        this.noticeState = noticeState;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
