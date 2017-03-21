package com.nuctech.ls.model.vo.monitor;

import java.util.Date;

public class PatrolUserVO {

    /* 巡逻队主键 */
    private String patrolId;

    /* 车载台编号 */
    private String trackUnitNumber;

    /* 所属区域 */
    private String belongToArea;

    /* 所属节点 */
    private String belongToPort;

    /* 负责人 */
    private String potralUser;

    /* 创建人 */
    private String createUser;

    /* 创建时间 */
    private Date createTime;

    /* 更新人员 */
    private String updateUser;

    /* 更新时间 */
    private Date updateTime;

    /* 删除标记 */
    private String deleteMark;

    /* 负责人 */
    private String potralUserName;

    /** 行程主键 */
    private String tripId;

    /** 车牌号 */
    private String vehiclePlateNumber;

    public String getPatrolId() {
        return this.patrolId;
    }

    public void setPatrolId(String patrolId) {
        this.patrolId = patrolId;
    }

    public String getTrackUnitNumber() {
        return this.trackUnitNumber;
    }

    public void setTrackUnitNumber(String trackUnitNumber) {
        this.trackUnitNumber = trackUnitNumber;
    }

    public String getBelongToArea() {
        return this.belongToArea;
    }

    public void setBelongToArea(String belongToArea) {
        this.belongToArea = belongToArea;
    }

    public String getBelongToPort() {
        return this.belongToPort;
    }

    public void setBelongToPort(String belongToPort) {
        this.belongToPort = belongToPort;
    }

    public String getPotralUser() {
        return this.potralUser;
    }

    public void setPotralUser(String potralUser) {
        this.potralUser = potralUser;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDeleteMark() {
        return this.deleteMark;
    }

    public void setDeleteMark(String deleteMark) {
        this.deleteMark = deleteMark;
    }

    public String getPotralUserName() {
        return potralUserName;
    }

    public void setPotralUserName(String potralUserName) {
        this.potralUserName = potralUserName;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

}
