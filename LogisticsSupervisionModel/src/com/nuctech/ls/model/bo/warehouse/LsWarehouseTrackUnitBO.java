package com.nuctech.ls.model.bo.warehouse;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[车载台表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_WAREHOUSE_TRACK_UNIT")
public class LsWarehouseTrackUnitBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7938806782134623770L;

    /**
     * 缺省的构造函数
     */
    public LsWarehouseTrackUnitBO() {
        super();
    }

    /* 车载台主键 */
    private String trackUnitId;

    /* 车载台号 */
    private String trackUnitNumber;

    /* 所属节点 */
    private String belongTo;

    /* SIM卡号,多个用逗号分开 */
    private String simCard;

    /* 信息上传频率 */
    private String interval;

    /* 网关地址 */
    private String gatewayAddress;

    /*
     * 车载台状态 维修、损坏、报废等
     */
    private String trackUnitStatus;

    /* 创建人 */
    private String createUser;

    /* 创建时间 */
    private Date createTime;

    /* 更新人员 */
    private String updateUser;

    /* 更新时间 */
    private Date updateTime;

    @Id
    @Column(name = "TRACK_UNIT_ID", nullable = false, length = 50)
    public String getTrackUnitId() {
        return this.trackUnitId;
    }

    public void setTrackUnitId(String trackUnitId) {
        this.trackUnitId = trackUnitId;
    }

    @Column(name = "TRACK_UNIT_NUMBER", nullable = true, length = 50)
    public String getTrackUnitNumber() {
        return this.trackUnitNumber;
    }

    public void setTrackUnitNumber(String trackUnitNumber) {
        this.trackUnitNumber = trackUnitNumber;
    }

    @Column(name = "BELONG_TO", nullable = true, length = 50)
    public String getBelongTo() {
        return this.belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    @Column(name = "SIM_CARD", nullable = true, length = 100)
    public String getSimCard() {
        return this.simCard;
    }

    public void setSimCard(String simCard) {
        this.simCard = simCard;
    }

    @Column(name = "INTERVAL", nullable = true, length = 20)
    public String getInterval() {
        return this.interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    @Column(name = "GATEWAY_ADDRESS", nullable = true, length = 20)
    public String getGatewayAddress() {
        return this.gatewayAddress;
    }

    public void setGatewayAddress(String gatewayAddress) {
        this.gatewayAddress = gatewayAddress;
    }

    @Column(name = "TRACK_UNIT_STATUS", nullable = true, length = 2)
    public String getTrackUnitStatus() {
        return this.trackUnitStatus;
    }

    public void setTrackUnitStatus(String trackUnitStatus) {
        this.trackUnitStatus = trackUnitStatus;
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

    @Override
    public String toString() {
        return "LsWarehouseTrackUnitBO [trackUnitId=" + trackUnitId + ", trackUnitNumber=" + trackUnitNumber
                + ", belongTo=" + belongTo + ", simCard=" + simCard + ", interval=" + interval + ", gatewayAddress="
                + gatewayAddress + ", trackUnitStatus=" + trackUnitStatus + ", createUser=" + createUser
                + ", createTime=" + createTime + ", updateUser=" + updateUser + ", updateTime=" + updateTime + "]";
    }
}
