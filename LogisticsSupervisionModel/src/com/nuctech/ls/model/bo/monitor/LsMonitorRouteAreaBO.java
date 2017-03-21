package com.nuctech.ls.model.bo.monitor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[路线区域规划表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_MONITOR_ROUTE_AREA")
public class LsMonitorRouteAreaBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8475269010278016159L;

    /**
     * 缺省的构造函数
     */
    public LsMonitorRouteAreaBO() {
        super();
    }

    /* 路线区域主键 */
    private String routeAreaId;

    /* 路线区域名称 */
    private String routeAreaName;

    /*
     * 路线区域类型 0-路线，1-安全区域，2-危险区域，3-监管区域，4-区域划分
     */
    private String routeAreaType;

    /*
     * 所属节点 每条路线或区域所属于的口岸 区划除外
     */
    private String belongToPort;

    /* 创建人 */
    private String createUser;

    /* 创建时间 */
    private Date createTime;

    /* 更新人员 */
    private String updateUser;

    /* 更新时间 */
    private Date updateTime;

    /*
     * 路线区域状态 0-有效，1-无效
     */
    private String routeAreaStatus;

    /* 缓冲区 */
    private String routeAreaBuffer;

    /** 路线用时（分钟） */
    private String routeCost;

    /** 距离（km） */
    private BigDecimal routeDistance;

    /* 起点 */
    private String startId;

    /* 起点名称 */
    private String startName;

    /* 起点经度 */
    private String startLongtitude;

    /* 起点纬度 */
    private String startLatitude;

    /* 终点 */
    private String endId;

    /* 终点名称 */
    private String endName;

    /* 终点经度 */
    private String endLongtitude;

    /* 终点纬度 */
    private String endLatitude;

    /* 路线颜色 */
    private String routeAreaColor;

    @Id
    @Column(name = "ROUTE_AREA_ID", nullable = false, length = 50)
    public String getRouteAreaId() {
        return this.routeAreaId;
    }

    public void setRouteAreaId(String routeAreaId) {
        this.routeAreaId = routeAreaId;
    }

    @Column(name = "ROUTE_AREA_NAME", nullable = true, length = 100)
    public String getRouteAreaName() {
        return this.routeAreaName;
    }

    public void setRouteAreaName(String routeAreaName) {
        this.routeAreaName = routeAreaName;
    }

    @Column(name = "ROUTE_AREA_TYPE", nullable = true, length = 2)
    public String getRouteAreaType() {
        return this.routeAreaType;
    }

    public void setRouteAreaType(String routeAreaType) {
        this.routeAreaType = routeAreaType;
    }

    @Column(name = "BELONG_TO_PORT", nullable = true, length = 50)
    public String getBelongToPort() {
        return this.belongToPort;
    }

    public void setBelongToPort(String belongToPort) {
        this.belongToPort = belongToPort;
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

    @Column(name = "ROUTE_AREA_STATUS", nullable = true, length = 2)
    public String getRouteAreaStatus() {
        return this.routeAreaStatus;
    }

    public void setRouteAreaStatus(String routeAreaStatus) {
        this.routeAreaStatus = routeAreaStatus;
    }

    @Column(name = "ROUTE_AREA_BUFFER", nullable = true, length = 20)
    public String getRouteAreaBuffer() {
        return this.routeAreaBuffer;
    }

    public void setRouteAreaBuffer(String routeAreaBuffer) {
        this.routeAreaBuffer = routeAreaBuffer;
    }

    @Column(name = "ROUTE_COST", nullable = true, length = 20)
    public String getRouteCost() {
        return this.routeCost;
    }

    public void setRouteCost(String routeCost) {
        this.routeCost = routeCost;
    }

    @Column(name = "ROUTE_DISTANCE", precision = 20, scale = 2, nullable = true)
    public BigDecimal getRouteDistance() {
        return this.routeDistance;
    }

    public void setRouteDistance(BigDecimal routeDistance) {
        this.routeDistance = routeDistance;
    }

    @Column(name = "START_ID", nullable = true, length = 50)
    public String getStartId() {
        return this.startId;
    }

    public void setStartId(String startId) {
        this.startId = startId;
    }

    @Column(name = "START_NAME", nullable = true, length = 100)
    public String getStartName() {
        return this.startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    @Column(name = "START_LONGTITUDE", nullable = true, length = 20)
    public String getStartLongtitude() {
        return this.startLongtitude;
    }

    public void setStartLongtitude(String startLongtitude) {
        this.startLongtitude = startLongtitude;
    }

    @Column(name = "START_LATITUDE", nullable = true, length = 20)
    public String getStartLatitude() {
        return this.startLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    @Column(name = "END_ID", nullable = true, length = 50)
    public String getEndId() {
        return this.endId;
    }

    public void setEndId(String endId) {
        this.endId = endId;
    }

    @Column(name = "END_NAME", nullable = true, length = 100)
    public String getEndName() {
        return this.endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    @Column(name = "END_LONGTITUDE", nullable = true, length = 20)
    public String getEndLongtitude() {
        return this.endLongtitude;
    }

    public void setEndLongtitude(String endLongtitude) {
        this.endLongtitude = endLongtitude;
    }

    @Column(name = "END_LATITUDE", nullable = true, length = 20)
    public String getEndLatitude() {
        return this.endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }

    @Column(name = "ROUTE_AREA_COLOR", nullable = true, length = 20)
    public String getRouteAreaColor() {
        return routeAreaColor;
    }

    public void setRouteAreaColor(String routeAreaColor) {
        this.routeAreaColor = routeAreaColor;
    }

}
