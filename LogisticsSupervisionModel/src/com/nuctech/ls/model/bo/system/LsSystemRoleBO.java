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
@Table(name = "LS_SYSTEM_ROLE")
public class LsSystemRoleBO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6600655793910680099L;

	/**
	 * 缺省的构造函数
	 */
	public LsSystemRoleBO() {
		super();
	}

	/* 角色主键 */
	private String roleId;

	/* 角色名称 */
	private String roleName;

	/* 有效标记 */
	private String isEnable;

	@Id
	@Column(name = "ROLE_ID", nullable = false, length = 50)
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "ROLE_NAME", nullable = true, length = 100)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "IS_ENABLE", nullable = true, length = 2)
	public String getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
}
