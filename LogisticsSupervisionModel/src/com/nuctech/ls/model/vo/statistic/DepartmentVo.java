package com.nuctech.ls.model.vo.statistic;

/**
 * 统计模块-已对接国家/公司 机构查询对象
 * 
 * @author liutonglei
 *
 */
public class DepartmentVo {

    /* 该类型机构数量 */
    private int number;
    /* 机构主键 */
    private String organizationId;

    /* 机构名称 */
    private String organizationName;

    /* 机构简称 */
    private String organizationShort;

    /* 上级机构 */
    private String parentId;

    /*
     * 1、国家 2、口岸 3、监管场所 4、建管中心
     */
    private String organizationType;

    /* 层次码 */
    private String levelCode;

    /* 经度 */
    private String longitude;

    /* 纬度 */
    private String latitude;

    /* 有效标记 */
    private String isEnable;

    /* 机构描述 */
    private String organizationDesc;
    /* 是否库房 */
    private String isRoom;
    /* 为口岸关锁的预留量做设置 */
    /* 预留比例 */
    private String reservationRatio;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public String getOrganizationShort() {
        return organizationShort;
    }

    public void setOrganizationShort(String organizationShort) {
        this.organizationShort = organizationShort;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getOrganizationDesc() {
        return organizationDesc;
    }

    public void setOrganizationDesc(String organizationDesc) {
        this.organizationDesc = organizationDesc;
    }

    public String getIsRoom() {
        return isRoom;
    }

    public void setIsRoom(String isRoom) {
        this.isRoom = isRoom;
    }

    public String getReservationRatio() {
        return reservationRatio;
    }

    public void setReservationRatio(String reservationRatio) {
        this.reservationRatio = reservationRatio;
    }

}
