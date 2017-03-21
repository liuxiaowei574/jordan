package com.nuctech.ls.model.vo.warehouse;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liangpengfei
 * 
 *         关锁调度报告
 *
 */
public class ElockDispatchReportVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // 关锁号
    private String elockNumber;
    // sim卡号
    private String simCard;
    // 接收部门
    private String applcationPortName;
    // 申请日期
    private Date applyTime;
    // 接受日期
    private Date recviceTime;
    // 发送部门
    private String organizationShort;

    public String getElockNumber() {
        return elockNumber;
    }

    public void setElockNumber(String elockNumber) {
        this.elockNumber = elockNumber;
    }

    public String getSimCard() {
        return simCard;
    }

    public void setSimCard(String simCard) {
        this.simCard = simCard;
    }

    public String getApplcationPortName() {
        return applcationPortName;
    }

    public void setApplcationPortName(String applcationPortName) {
        this.applcationPortName = applcationPortName;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getRecviceTime() {
        return recviceTime;
    }

    public void setRecviceTime(Date recviceTime) {
        this.recviceTime = recviceTime;
    }

    public String getOrganizationShort() {
        return organizationShort;
    }

    public void setOrganizationShort(String organizationShort) {
        this.organizationShort = organizationShort;
    }

}
