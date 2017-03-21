package com.nuctech.ls.model.bo.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[角色功能资源表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_SYSTEM_ROLE_FUNCTIONS")
public class LsSystemRoleFunctionsBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4361731718938634116L;

    /**
     * 缺省的构造函数
     */
    public LsSystemRoleFunctionsBO() {
        super();
    }

    /* 角色功能资源主键 */
    private String roleFunctionsId;

    /* 角色编码 */
    private String roleId;

    /* 功能资源编码 */
    private String functionsId;

    @Id
    @Column(name = "ROLE_FUNCTIONS_ID", nullable = false, length = 50)
    public String getRoleFunctionsId() {
        return this.roleFunctionsId;
    }

    public void setRoleFunctionsId(String roleFunctionsId) {
        this.roleFunctionsId = roleFunctionsId;
    }

    @Column(name = "ROLE_ID", nullable = true, length = 50)
    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Column(name = "FUNCTIONS_ID", nullable = true, length = 50)
    public String getFunctionsId() {
        return this.functionsId;
    }

    public void setFunctionsId(String functionsId) {
        this.functionsId = functionsId;
    }
}
