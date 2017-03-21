package com.nuctech.ls.model.bo.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[巡逻队信息表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_COMMON_PATROL")
public class LsCommonPatrolBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8320033888745656903L;

    /**
     * 缺省的构造函数
     */
    public LsCommonPatrolBO() {
        super();
    }

    /* 巡逻队主键 */
    private String patrolId;

    /* 巡逻队编号 */
    private String patrolNumber;
    /* 巡逻队类型 */
    private String patrolType;

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
    /* 添加车载台安装照片路径 */
    private String patrolInstalPicture;

    /** 行程主键 */
    private String tripId;

    /** 车牌号 */
    private String vehiclePlateNumber;

    @Id
    @Column(name = "PATROL_ID", nullable = false, length = 50)
    public String getPatrolId() {
        return this.patrolId;
    }

    public void setPatrolId(String patrolId) {
        this.patrolId = patrolId;
    }

    @Column(name = "TRACK_UNIT_NUMBER", nullable = true, length = 50)
    public String getTrackUnitNumber() {
        return this.trackUnitNumber;
    }

    public void setTrackUnitNumber(String trackUnitNumber) {
        this.trackUnitNumber = trackUnitNumber;
    }

    @Column(name = "BELONG_TO_AREA", nullable = true, length = 50)
    public String getBelongToArea() {
        return this.belongToArea;
    }

    public void setBelongToArea(String belongToArea) {
        this.belongToArea = belongToArea;
    }

    @Column(name = "BELONG_TO_PORT", nullable = true, length = 50)
    public String getBelongToPort() {
        return this.belongToPort;
    }

    public void setBelongToPort(String belongToPort) {
        this.belongToPort = belongToPort;
    }

    @Column(name = "POTRAL_USER", nullable = true, length = 50)
    public String getPotralUser() {
        return this.potralUser;
    }

    public void setPotralUser(String potralUser) {
        this.potralUser = potralUser;
    }

    @Column(name = "CREATE_USER", nullable = true, length = 50)
    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_USER", nullable = true, length = 50)
    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "DELETE_MARK", nullable = true, length = 2)
    public String getDeleteMark() {
        return this.deleteMark;
    }

    public void setDeleteMark(String deleteMark) {
        this.deleteMark = deleteMark;
    }

    @Column(name = "TRIP_ID", nullable = true, length = 50)
    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    @Column(name = "PATROL_INSTALL_PICTURE", nullable = true, length = 2000)
    public String getPatrolInstalPicture() {
        return patrolInstalPicture;
    }

    public void setPatrolInstalPicture(String patrolInstalPicture) {
        this.patrolInstalPicture = patrolInstalPicture;
    }

    @Column(name = "VEHICLE_PLATE_NUMBER", nullable = true, length = 50)
    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    @Column(name = "PATROL_NUMBER", nullable = true, length = 50)
    public String getPatrolNumber() {
        return patrolNumber;
    }

    public void setPatrolNumber(String patrolNumber) {
        this.patrolNumber = patrolNumber;
    }

    public String getPatrolType() {
        return patrolType;
    }

    public void setPatrolType(String patrolType) {
        this.patrolType = patrolType;
    }

    @Override
    public String toString() {
        return "LsCommonPatrolBO [patrolId=" + patrolId + ", trackUnitNumber=" + trackUnitNumber + ", belongToArea="
                + belongToArea + ", belongToPort=" + belongToPort + ", potralUser=" + potralUser + ", createUser="
                + createUser + ", createTime=" + createTime + ", updateUser=" + updateUser + ", updateTime="
                + updateTime + ", deleteMark=" + deleteMark + ", patrolInstalPicture=" + patrolInstalPicture
                + ", tripId=" + tripId + "]";
    }
}
