package com.nuctech.ls.model.bo.sla;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[罚单表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_SLA_PUNISH")
public class LsSlaPunishBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4150319190206665322L;

    /**
     * 缺省的构造函数
     */
    public LsSlaPunishBO() {
        super();
    }

    /* 罚单主键 */
    private String punishId;

    /* 罚单名称 */
    private String punishName;

    /* 事故类型 */
    private String slaType;

    /* 罚款金额 */
    private String punishValue;

    /* 事故内容 */
    private String slaContent;

    /* 登记时间 */
    private Date createTime;

    /* 解决时间 */
    private Date solveTime;

    /* 解决人姓名 */
    private String solveName;

    /* 登记人 */
    private String createUser;

    @Id
    @Column(name = "PUNISH_ID", nullable = false, length = 50)
    public String getPunishId() {
        return this.punishId;
    }

    public void setPunishId(String punishId) {
        this.punishId = punishId;
    }

    @Column(name = "PUNISH_NAME", nullable = true, length = 100)
    public String getPunishName() {
        return this.punishName;
    }

    public void setPunishName(String punishName) {
        this.punishName = punishName;
    }

    @Column(name = "SLA_TYPE", nullable = true, length = 100)
    public String getSlaType() {
        return this.slaType;
    }

    public void setSlaType(String slaType) {
        this.slaType = slaType;
    }

    @Column(name = "PUNISH_VALUE", nullable = true, length = 20)
    public String getPunishValue() {
        return this.punishValue;
    }

    public void setPunishValue(String punishValue) {
        this.punishValue = punishValue;
    }

    @Column(name = "SLA_CONTENT", nullable = true, length = 2000)
    public String getSlaContent() {
        return this.slaContent;
    }

    public void setSlaContent(String slaContent) {
        this.slaContent = slaContent;
    }

    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "SOLVE_TIME", nullable = true)
    public Date getSolveTime() {
        return this.solveTime;
    }

    public void setSolveTime(Date solveTime) {
        this.solveTime = solveTime;
    }

    @Column(name = "SOLVE_NAME", nullable = true, length = 100)
    public String getSolveName() {
        return this.solveName;
    }

    public void setSolveName(String solveName) {
        this.solveName = solveName;
    }

    @Column(name = "CREATE_USER", nullable = true, length = 50)
    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
