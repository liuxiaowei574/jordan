package com.nuctech.ls.model.bo.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[任务处理记录]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_SYSTEM_TASKS_DEAL")
public class LsSystemTasksDealBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3550009399347373572L;

    /**
     * 缺省的构造函数
     */
    public LsSystemTasksDealBO() {
        super();
    }

    /* 任务处理主键 */
    private String taskDealId;

    /* 任务编号 */
    private String taskId;

    /* 任务接收人 */
    private String receiveUser;

    /* 任务处理时间 */
    private Date dealTime;

    /* 处理方式 0:未处理；1：已处理 */
    private String dealType;

    @Id
    @Column(name = "TASKS_DEAL_ID", nullable = false, length = 50)
    public String getTaskDealId() {
        return taskDealId;
    }

    public void setTaskDealId(String taskDealId) {
        this.taskDealId = taskDealId;
    }

    @Column(name = "TASK_ID", nullable = true, length = 50)
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Column(name = "RECEIVE_USER", nullable = true, length = 50)
    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    @Column(name = "DEAL_TIME", nullable = true)
    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    @Column(name = "DEAL_TYPE", nullable = true, length = 2)
    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }
}
