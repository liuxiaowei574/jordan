package com.nuctech.ls.model.bo.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[功能资源]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_SYSTEM_FUNCTIONS")
public class LsSystemFunctionsBO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5430171631033885266L;

	/**
	 * 缺省的构造函数
	 */
	public LsSystemFunctionsBO() {
		super();
	}

	public LsSystemFunctionsBO(String functionId, String functionName, String functionType, String functionPath, String levelCode, String parentId,
			String isEnable) {
		super();
		this.functionId = functionId;
		this.functionName = functionName;
		this.functionType = functionType;
		this.functionPath = functionPath;
		this.levelCode = levelCode;
		this.parentId = parentId;
		this.isEnable = isEnable;
	}

	/* 功能主键 */
	private String functionId;

	/* 功能名称 */
	private String functionName;

	/*
	 * 功能类型 0-菜单，1-功能，2-按钮
	 */
	private String functionType;

	/* 功能路径 */
	private String functionPath;

	/* 层次码 */
	private String levelCode;

	/* 上级目录 */
	private String parentId;

	/* 有效标记 */
	private String isEnable;

	@Id
	@Column(name = "FUNCTION_ID", nullable = false, length = 50)
	public String getFunctionId() {
		return this.functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	@Column(name = "FUNCTION_NAME", nullable = true, length = 100)
	public String getFunctionName() {
		return this.functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	@Column(name = "FUNCTION_TYPE", nullable = true, length = 2)
	public String getFunctionType() {
		return this.functionType;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	@Column(name = "FUNCTION_PATH", nullable = true, length = 200)
	public String getFunctionPath() {
		return this.functionPath;
	}

	public void setFunctionPath(String functionPath) {
		this.functionPath = functionPath;
	}

	@Column(name = "LEVEL_CODE", nullable = true, length = 200)
	public String getLevelCode() {
		return this.levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	@Column(name = "PARENT_ID", nullable = true, length = 50)
	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column(name = "IS_ENABLE", nullable = true, length = 2)
	public String getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
}
