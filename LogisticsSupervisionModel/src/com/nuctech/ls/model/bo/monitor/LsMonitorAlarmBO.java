package com.nuctech.ls.model.bo.monitor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[记录车辆在行程中的报警信息]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_MONITOR_ALARM")
public class LsMonitorAlarmBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6764123916159062339L;

    /**
     * 缺省的构造函数
     */
    public LsMonitorAlarmBO() {
        super();
    }

    /* 报警主键 */
    private String alarmId;

    /* 行程主键 */
    private String tripId;

    /* 类型主键 */
    private String alarmTypeId;

    /* 报警内容 */
    private String alarmContent;

    /* 报警时间 */
    private Date alarmTime;

    /* 报警经度 */
    private String alarmLongitude;

    /* 报警纬度 */
    private String alarmLatitude;

    /* 是否罚款 */
    private String isPunish;

    /* 罚款内容 */
    private String punishContent;

    /*
     * 是否手动创建 0-非手动，1-手动
     */
    private String isManual;

    /*
     * 当前负责人 当前负责处理报警的人员 可能是中心的人员，也可能是巡逻队的人员，当有人接收报警时，则填写负责人
     */
    private String userId;

    /*
     * 接收时间 只记录最新人员的接收时间
     */
    private Date receiveTime;

    /* 创建人 */
    private String createUser;

    /*
     * 报警状态 0-未处理，1-处理中，2-已处理
     */
    private String alarmStatus;

    /** 车辆主键 */
    private String vehicleId;

    @Id
    @Column(name = "ALARM_ID", nullable = false, length = 50)
    public String getAlarmId() {
        return this.alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    @Column(name = "TRIP_ID", nullable = true, length = 50)
    public String getTripId() {
        return this.tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    @Column(name = "ALARM_TYPE_ID", nullable = true, length = 50)
    public String getAlarmTypeId() {
        return this.alarmTypeId;
    }

    public void setAlarmTypeId(String alarmTypeId) {
        this.alarmTypeId = alarmTypeId;
    }

    @Column(name = "ALARM_CONTENT", nullable = true, length = 200)
    public String getAlarmContent() {
        return this.alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    @Column(name = "ALARM_TIME", nullable = true)
    public Date getAlarmTime() {
        return this.alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    @Column(name = "ALARM_LONGITUDE", nullable = true, length = 20)
    public String getAlarmLongitude() {
        return this.alarmLongitude;
    }

    public void setAlarmLongitude(String alarmLongitude) {
        this.alarmLongitude = alarmLongitude;
    }

    @Column(name = "ALARM_LATITUDE", nullable = true, length = 20)
    public String getAlarmLatitude() {
        return this.alarmLatitude;
    }

    public void setAlarmLatitude(String alarmLatitude) {
        this.alarmLatitude = alarmLatitude;
    }

    @Column(name = "IS_PUNISH", nullable = true, length = 2)
    public String getIsPunish() {
        return this.isPunish;
    }

    public void setIsPunish(String isPunish) {
        this.isPunish = isPunish;
    }

    @Column(name = "PUNISH_CONTENT", nullable = true, length = 2000)
    public String getPunishContent() {
        return this.punishContent;
    }

    public void setPunishContent(String punishContent) {
        this.punishContent = punishContent;
    }

    @Column(name = "IS_MANUAL", nullable = true, length = 2)
    public String getIsManual() {
        return this.isManual;
    }

    public void setIsManual(String isManual) {
        this.isManual = isManual;
    }

    @Column(name = "USER_ID", nullable = true, length = 50)
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "RECEIVE_TIME", nullable = true)
    public Date getReceiveTime() {
        return this.receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Column(name = "CREATE_USER", nullable = true, length = 50)
    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Column(name = "ALARM_STATUS", nullable = true, length = 2)
    public String getAlarmStatus() {
        return this.alarmStatus;
    }

    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    @Column(name = "VEHICLE_ID", nullable = true, length = 50)
    public String getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}
