package com.nuctech.ls.model.bo.monitor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[记录行程信息，每个行程信息可以有多辆车，每辆车1把关锁、1-6把子锁，1-6把传感器，每辆车都要拍照，每辆车有各自的报警，
 * 每辆车可有多种货物]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_MONITOR_TRIP")
public class LsMonitorTripBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7132923470535659020L;

    /**
     * 缺省的构造函数
     */
    public LsMonitorTripBO() {
        super();
    }

    /* 行程主键 */
    private String tripId;

    /** 报关单号 */
    private String declarationNumber;

    /* 规划路线 */
    private String routeId;

    /*
     * 0. 绿色 1. 黄色 2. 红色
     */
    private String riskStatus;

    /* 检入人员 */
    private String checkinUser;

    /* 检入时间 */
    private Date checkinTime;

    /* 检入地点 */
    private String checkinPort;

    /* 检出人员 */
    private String checkoutUser;

    /* 检出时间 */
    private Date checkoutTime;

    /* 检出地点 */
    private String checkoutPort;

    /* 行程耗时 */
    private String timeCost;

    /**
     * 行程状态 0. 待激活 1. 进行中 2. 待结束 3. 已结束
     */
    private String tripStatus;

    /* 监控组主键 */
    private String groupId;

    /**
     * 行程激活时选择的target zoon，对应LsMonitorRouteAreaBO表主键，多个用英文逗号隔开
     */
    private String targetZoonId;

    /**
     * 是否特殊申请。0：否，1：是
     */
    private String specialFlag;

    /**
     * 特殊申请的理由
     */
    private String reason;

    @Id
    @Column(name = "TRIP_ID", nullable = false, length = 50)
    public String getTripId() {
        return this.tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    @Column(name = "ROUTE_ID", nullable = true, length = 50)
    public String getRouteId() {
        return this.routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    @Column(name = "RISK_STATUS", nullable = true, length = 2)
    public String getRiskStatus() {
        return this.riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    @Column(name = "CHECKIN_USER", nullable = true, length = 50)
    public String getCheckinUser() {
        return this.checkinUser;
    }

    public void setCheckinUser(String checkinUser) {
        this.checkinUser = checkinUser;
    }

    @Column(name = "CHECKIN_TIME", nullable = true)
    public Date getCheckinTime() {
        return this.checkinTime;
    }

    public void setCheckinTime(Date checkinTime) {
        this.checkinTime = checkinTime;
    }

    @Column(name = "CHECKIN_PORT", nullable = true, length = 50)
    public String getCheckinPort() {
        return this.checkinPort;
    }

    public void setCheckinPort(String checkinPort) {
        this.checkinPort = checkinPort;
    }

    @Column(name = "CHECKOUT_USER", nullable = true, length = 50)
    public String getCheckoutUser() {
        return this.checkoutUser;
    }

    public void setCheckoutUser(String checkoutUser) {
        this.checkoutUser = checkoutUser;
    }

    @Column(name = "CHECKOUT_TIME", nullable = true)
    public Date getCheckoutTime() {
        return this.checkoutTime;
    }

    public void setCheckoutTime(Date checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    @Column(name = "CHECKOUT_PORT", nullable = true, length = 50)
    public String getCheckoutPort() {
        return this.checkoutPort;
    }

    public void setCheckoutPort(String checkoutPort) {
        this.checkoutPort = checkoutPort;
    }

    @Column(name = "TIME_COST", nullable = true, length = 20)
    public String getTimeCost() {
        return this.timeCost;
    }

    public void setTimeCost(String timeCost) {
        this.timeCost = timeCost;
    }

    @Column(name = "TRIP_STATUS", nullable = true, length = 2)
    public String getTripStatus() {
        return this.tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    @Column(name = "GROUP_ID", nullable = true, length = 50)
    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Column(name = "DECLARATION_NUMBER", nullable = true, length = 50)
    public String getDeclarationNumber() {
        return this.declarationNumber;
    }

    public void setDeclarationNumber(String declarationNumber) {
        this.declarationNumber = declarationNumber;
    }

    @Column(name = "TARGET_ZOON_ID", nullable = true, length = 3000)
    public String getTargetZoonId() {
        return targetZoonId;
    }

    public void setTargetZoonId(String targetZoonId) {
        this.targetZoonId = targetZoonId;
    }

    @Column(name = "SPECIAL_FLAG", nullable = true, length = 10)
    public String getSpecialFlag() {
        return specialFlag;
    }

    public void setSpecialFlag(String specialFlag) {
        this.specialFlag = specialFlag;
    }

    @Column(name = "REASON", nullable = true, length = 2000)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LsMonitorTripBO(String tripId, String declarationNumber, String routeId, String riskStatus,
            String checkinUser, Date checkinTime, String checkinPort, String checkoutUser, Date checkoutTime,
            String checkoutPort, String timeCost, String tripStatus, String groupId, String targetZoonId,
            String specialFlag, String reason) {
        super();
        this.tripId = tripId;
        this.declarationNumber = declarationNumber;
        this.routeId = routeId;
        this.riskStatus = riskStatus;
        this.checkinUser = checkinUser;
        this.checkinTime = checkinTime;
        this.checkinPort = checkinPort;
        this.checkoutUser = checkoutUser;
        this.checkoutTime = checkoutTime;
        this.checkoutPort = checkoutPort;
        this.timeCost = timeCost;
        this.tripStatus = tripStatus;
        this.groupId = groupId;
        this.targetZoonId = targetZoonId;
        this.specialFlag = specialFlag;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "LsMonitorTripBO [tripId=" + tripId + ", declarationNumber=" + declarationNumber + ", routeId=" + routeId
                + ", riskStatus=" + riskStatus + ", checkinUser=" + checkinUser + ", checkinTime=" + checkinTime
                + ", checkinPort=" + checkinPort + ", checkoutUser=" + checkoutUser + ", checkoutTime=" + checkoutTime
                + ", checkoutPort=" + checkoutPort + ", timeCost=" + timeCost + ", tripStatus=" + tripStatus
                + ", groupId=" + groupId + ", targetZoonId=" + targetZoonId + ", specialFlag=" + specialFlag
                + ", reason=" + reason + "]";
    }

}
