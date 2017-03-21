package com.nuctech.ls.model.vo.warehouse;

/**
 * 巡逻队和组织机构和路线区域规划表和用户和车载台VO类
 * 
 * @author zhaosuyang
 *
 */
public class PatrolDepartmentVO {

    /**
     * 巡逻队
     */
    /* 巡逻队主键 */
    private String patrolId;
    /* 创建人 */
    private String createUser;
    /* 创建人Name */
    private String createUserName;

    /* 巡逻队类型 */
    private String patrolType;

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getPatrolId() {
        return patrolId;
    }

    public void setPatrolId(String patrolId) {
        this.patrolId = patrolId;
    }

    public String getTrackUnitNumber() {
        return trackUnitNumber;
    }

    public void setTrackUnitNumber(String trackUnitNumber) {
        this.trackUnitNumber = trackUnitNumber;
    }

    public String getBelongToArea() {
        return belongToArea;
    }

    public void setBelongToArea(String belongToArea) {
        this.belongToArea = belongToArea;
    }

    public String getBelongToPort() {
        return belongToPort;
    }

    public void setBelongToPort(String belongToPort) {
        this.belongToPort = belongToPort;
    }

    public String getPotralUser() {
        return potralUser;
    }

    public void setPotralUser(String potralUser) {
        this.potralUser = potralUser;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getRouteAreaId() {
        return routeAreaId;
    }

    public void setRouteAreaId(String routeAreaId) {
        this.routeAreaId = routeAreaId;
    }

    public String getRouteAreaName() {
        return routeAreaName;
    }

    public void setRouteAreaName(String routeAreaName) {
        this.routeAreaName = routeAreaName;
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

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public String getPatrolNumber() {
        return patrolNumber;
    }

    public void setPatrolNumber(String patrolNumber) {
        this.patrolNumber = patrolNumber;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getPatrolType() {
        return patrolType;
    }

    public void setPatrolType(String patrolType) {
        this.patrolType = patrolType;
    }

    /* 车载台编号 */
    private String trackUnitNumber;

    /* 所属区域 */
    private String belongToArea;

    /* 所属节点 */
    private String belongToPort;

    /* 负责人 */
    private String potralUser;
    /**
     * 机构表
     */
    /* 机构主键 */
    private String organizationId;

    /* 机构名称 */
    private String organizationName;
    /**
     * 区域表
     */
    /* 路线区域主键 */
    private String routeAreaId;

    /* 路线区域名称 */
    private String routeAreaName;
    /**
     * 用户表
     */
    /* 用户主键 */
    private String userId;

    /* 用户名 */
    private String userAccount;
    /** 车牌号 */
    private String vehiclePlateNumber;

    /* 巡逻队编号 */
    private String patrolNumber;
}
