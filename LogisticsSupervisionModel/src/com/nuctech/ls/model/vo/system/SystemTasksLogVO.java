package com.nuctech.ls.model.vo.system;

import java.util.Date;

/**
 * 
 * @author zhaosuyang
 *
 */
public class SystemTasksLogVO {

    /* 任务主键 */
    private String taskId;

    /* 任务主题 */
    private String taskTitle;

    /* 任务内容 */
    private String taskContent;

    /* 发布时间 */
    private Date deployTime;

    /* 发布人 */
    private String publisher;

    /* 任务指定完成人员 */
    private String taskReceiveUsers;

    /* 任务类型 1:护送任务；2：报警处理任务；3：设备调度任务； */
    private String taskType;

    /* 任务处理主键 */
    private String taskDealId;

    /* 任务接收人 */
    private String receiveUser;

    /* 任务处理时间 */
    private Date dealTime;

    /* 处理方式 0:未处理；1：已处理 */
    private String dealType;

    /* 用户主键 */
    private String userId;

    /* 用户名 */
    private String userAccount;

    /* 密码 */
    private String userPassword;

    /* 姓名 */
    private String userName;

    /* 紧急程度 */
    private String priority;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
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

    public String getTaskReceiveUsers() {
        return taskReceiveUsers;
    }

    public void setTaskReceiveUsers(String taskReceiveUsers) {
        this.taskReceiveUsers = taskReceiveUsers;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskDealId() {
        return taskDealId;
    }

    public void setTaskDealId(String taskDealId) {
        this.taskDealId = taskDealId;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
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

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
