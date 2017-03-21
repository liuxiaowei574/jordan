package com.nuctech.ls.model.bo.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[机构用户表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_SYSTEM_ORGANIZATION_USER")
public class LsSystemOrganizationUserBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5081560139315611148L;

    /**
     * 缺省的构造函数
     */
    public LsSystemOrganizationUserBO() {
        super();
    }

    /* 机构用户主键 */
    private String orgUserId;

    /* 机构编码 */
    private String organizationId;

    /* 用户编码 */
    private String userId;

    @Id
    @Column(name = "ORG_USER_ID", nullable = false, length = 50)
    public String getOrgUserId() {
        return this.orgUserId;
    }

    public void setOrgUserId(String orgUserId) {
        this.orgUserId = orgUserId;
    }

    @Column(name = "ORGANIZATION_ID", nullable = true, length = 50)
    public String getOrganizationId() {
        return this.organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Column(name = "USER_ID", nullable = true, length = 50)
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
