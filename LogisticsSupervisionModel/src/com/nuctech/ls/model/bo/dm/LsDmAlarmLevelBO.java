package com.nuctech.ls.model.bo.dm;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[报警级别代码表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_DM_ALARM_LEVEL")
public class LsDmAlarmLevelBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5511835402048069105L;

    /**
     * 缺省的构造函数
     */
    public LsDmAlarmLevelBO() {
        super();
    }

    /* 级别主键 */
    private String alarmLevelId;

    /* 级别名称 */
    private String alarmLevelName;

    /* 级别代码 0-轻微 1-严重 */
    private String alarmLevelCode;

    /* 创建人 */
    private String createUser;

    /* 创建时间 */
    private Date createTime;

    /* 更新人员 */
    private String updateUser;

    /* 更新时间 */
    private Date updateTime;

    @Id
    @Column(name = "ALARM_LEVEL_ID", nullable = false, length = 50)
    public String getAlarmLevelId() {
        return this.alarmLevelId;
    }

    public void setAlarmLevelId(String alarmLevelId) {
        this.alarmLevelId = alarmLevelId;
    }

    @Column(name = "ALARM_LEVEL_NAME", nullable = true, length = 100)
    public String getAlarmLevelName() {
        return this.alarmLevelName;
    }

    public void setAlarmLevelName(String alarmLevelName) {
        this.alarmLevelName = alarmLevelName;
    }

    @Column(name = "ALARM_LEVEL_CODE", nullable = true, length = 20)
    public String getAlarmLevelCode() {
        return this.alarmLevelCode;
    }

    public void setAlarmLevelCode(String alarmLevelCode) {
        this.alarmLevelCode = alarmLevelCode;
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
}
