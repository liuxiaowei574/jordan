package com.nuctech.ls.model.bo.dm;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 业务对象处理的实体-[报警类型代码表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_DM_ALARM_TYPE")
public class LsDmAlarmTypeBO
{
    /**
     * 缺省的构造函数
     */
    public LsDmAlarmTypeBO()
    {
        super();
    }

    /* 类型主键 */
    private String alarmTypeId;

    /* 类型名称 */
    private String alarmTypeName;

    /* 类型代码 */
    private String alarmTypeCode;

    /* 级别主键 */
    private String alarmLevelId;

    /* 创建人 */
    private String createUser;

    /* 创建时间 */
    private Date createTime;

    /* 更新人员 */
    private String updateUser;

    /* 更新时间 */
    private Date updateTime;

    @Id
    @Column(name = "ALARM_TYPE_ID", nullable = false, length = 50)
    public String getAlarmTypeId()
    {
        return this.alarmTypeId;
    }

    public void setAlarmTypeId(String alarmTypeId)
    {
        this.alarmTypeId = alarmTypeId;
    }

    @Column(name = "ALARM_TYPE_NAME", nullable = true, length = 100)
    public String getAlarmTypeName()
    {
        return this.alarmTypeName;
    }

    public void setAlarmTypeName(String alarmTypeName)
    {
        this.alarmTypeName = alarmTypeName;
    }

    @Column(name = "ALARM_TYPE_CODE", nullable = true, length = 20)
    public String getAlarmTypeCode()
    {
        return this.alarmTypeCode;
    }

    public void setAlarmTypeCode(String alarmTypeCode)
    {
        this.alarmTypeCode = alarmTypeCode;
    }

    @Column(name = "ALARM_LEVEL_ID", nullable = true, length = 50)
    public String getAlarmLevelId()
    {
        return this.alarmLevelId;
    }

    public void setAlarmLevelId(String alarmLevelId)
    {
        this.alarmLevelId = alarmLevelId;
    }

    @Column(name = "CREATE_USER", nullable = true, length = 50)
    public String getCreateUser()
    {
        return this.createUser;
    }

    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }

    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_USER", nullable = true, length = 50)
    public String getUpdateUser()
    {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser)
    {
        this.updateUser = updateUser;
    }

    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime()
    {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
}
