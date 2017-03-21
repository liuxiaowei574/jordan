package com.nuctech.ls.model.bo.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[待办任务]
 *
 * @author： zsy
 */
@Entity
@Table(name = "LS_SYSTEM_TASKS")
public class LsSystemTasksBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4217135781014054752L;

    /**
     * 缺省的构造函数
     */
    public LsSystemTasksBO() {
        super();
    }

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

    /* 紧急程度 */
    private String priority;

    @Id
    @Column(name = "TASK_ID", nullable = false, length = 50)
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Column(name = "TASK_TITLE", nullable = true, length = 100)
    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    @Column(name = "TASK_CONTENT", nullable = true, length = 100)
    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    @Column(name = "DEPLOY_TIME", nullable = true)
    public Date getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(Date deployTime) {
        this.deployTime = deployTime;
    }

    @Column(name = "PUBLISHER", nullable = true, length = 50)
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Column(name = "TASK_TYPE", nullable = true, length = 2)
    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Column(name = "TASK_RECEIVE_USER", nullable = true, length = 50)
    public String getTaskReceiveUsers() {
        return taskReceiveUsers;
    }

    public void setTaskReceiveUsers(String taskReceiveUsers) {
        this.taskReceiveUsers = taskReceiveUsers;
    }

    @Column(name = "TASK_PRIORITY", nullable = true, length = 50)
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
