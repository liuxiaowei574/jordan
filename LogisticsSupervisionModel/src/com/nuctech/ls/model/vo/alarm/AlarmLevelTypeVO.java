package com.nuctech.ls.model.vo.alarm;

import java.util.Date;

public class AlarmLevelTypeVO {

    /* 级别主键 */
    private String alarmLevelId;

    public String getAlarmLevelId() {
        return alarmLevelId;
    }

    public void setAlarmLevelId(String alarmLevelId) {
        this.alarmLevelId = alarmLevelId;
    }

    public String getAlarmLevelName() {
        return alarmLevelName;
    }

    public void setAlarmLevelName(String alarmLevelName) {
        this.alarmLevelName = alarmLevelName;
    }

    public String getAlarmLevelCode() {
        return alarmLevelCode;
    }

    public void setAlarmLevelCode(String alarmLevelCode) {
        this.alarmLevelCode = alarmLevelCode;
    }

    public String getAlarmTypeId() {
        return alarmTypeId;
    }

    public void setAlarmTypeId(String alarmTypeId) {
        this.alarmTypeId = alarmTypeId;
    }

    public String getAlarmTypeName() {
        return alarmTypeName;
    }

    public void setAlarmTypeName(String alarmTypeName) {
        this.alarmTypeName = alarmTypeName;
    }

    public String getAlarmTypeCode() {
        return alarmTypeCode;
    }

    public void setAlarmTypeCode(String alarmTypeCode) {
        this.alarmTypeCode = alarmTypeCode;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /* 用户主键 */
    private String userId;

    /* 用户名 */
    private String userAccount;
    /* 级别名称 */
    private String alarmLevelName;

    /* 级别代码 0-轻微 1-严重 */
    private String alarmLevelCode;

    /* 类型主键 */
    private String alarmTypeId;

    /* 类型名称 */
    private String alarmTypeName;

    /* 类型代码 */
    private String alarmTypeCode;

    /* 级别主键 */

    /* 创建人 */
    private String createUser;

    /* 创建时间 */
    private Date createTime;

    /* 更新人员 */
    private String updateUser;
    /* 更新人员 */
    private String userName;

    /* 更新时间 */
    private Date updateTime;

    /* 参数名称 */
    private String paramName;

    /* 参数值 */
    private String paramValue;
    /* 参数主键 */
    private String paramId;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
