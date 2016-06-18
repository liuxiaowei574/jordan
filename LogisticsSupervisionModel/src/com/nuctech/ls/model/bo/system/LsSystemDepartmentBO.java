package com.nuctech.ls.model.bo.system;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 业务对象处理的实体-[机构表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_SYSTEM_DEPARTMENT")
public class LsSystemDepartmentBO
{
    /**
     * 缺省的构造函数
     */
    public LsSystemDepartmentBO()
    {
        super();
    }

    /* 机构主键 */
    private String organizationId;

    /* 机构名称 */
    private String organizationName;

    /* 机构简称 */
    private String organizationShort;

    /* 上级机构 */
    private String parentId;

    /* 1、国家
    2、口岸
    3、监管场所
    4、建管中心 */
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

    @Id
    @Column(name = "ORGANIZATION_ID", nullable = false, length = 50)
    public String getOrganizationId()
    {
        return this.organizationId;
    }

    public void setOrganizationId(String organizationId)
    {
        this.organizationId = organizationId;
    }

    @Column(name = "ORGANIZATION_NAME", nullable = true, length = 100)
    public String getOrganizationName()
    {
        return this.organizationName;
    }

    public void setOrganizationName(String organizationName)
    {
        this.organizationName = organizationName;
    }

    @Column(name = "ORGANIZATION_SHORT", nullable = true, length = 100)
    public String getOrganizationShort()
    {
        return this.organizationShort;
    }

    public void setOrganizationShort(String organizationShort)
    {
        this.organizationShort = organizationShort;
    }

    @Column(name = "PARENT_ID", nullable = true, length = 50)
    public String getParentId()
    {
        return this.parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    @Column(name = "ORGANIZATION_TYPE", nullable = true, length = 2)
    public String getOrganizationType()
    {
        return this.organizationType;
    }

    public void setOrganizationType(String organizationType)
    {
        this.organizationType = organizationType;
    }

    @Column(name = "LEVEL_CODE", nullable = true, length = 200)
    public String getLevelCode()
    {
        return this.levelCode;
    }

    public void setLevelCode(String levelCode)
    {
        this.levelCode = levelCode;
    }

    @Column(name = "LONGITUDE", nullable = true, length = 20)
    public String getLongitude()
    {
        return this.longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    @Column(name = "LATITUDE", nullable = true, length = 20)
    public String getLatitude()
    {
        return this.latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    @Column(name = "IS_ENABLE", nullable = true, length = 2)
    public String getIsEnable()
    {
        return this.isEnable;
    }

    public void setIsEnable(String isEnable)
    {
        this.isEnable = isEnable;
    }

    @Column(name = "ORGANIZATION_DESC", nullable = true, length = 200)
    public String getOrganizationDesc()
    {
        return this.organizationDesc;
    }

    public void setOrganizationDesc(String organizationDesc)
    {
        this.organizationDesc = organizationDesc;
    }
}
