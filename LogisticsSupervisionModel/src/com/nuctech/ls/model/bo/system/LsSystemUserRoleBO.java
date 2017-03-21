package com.nuctech.ls.model.bo.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[用户角色表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_SYSTEM_USER_ROLE")
public class LsSystemUserRoleBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1421521127000462623L;

    /**
     * 缺省的构造函数
     */
    public LsSystemUserRoleBO() {
        super();
    }

    /* 用户角色主键 */
    private String userRoleId;

    /* 角色编码 */
    private String roleId;

    /* 用户编码 */
    private String userId;

    @Id
    @Column(name = "USER_ROLE_ID", nullable = false, length = 50)
    public String getUserRoleId() {
        return this.userRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }

    @Column(name = "ROLE_ID", nullable = true, length = 50)
    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Column(name = "USER_ID", nullable = true, length = 50)
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
