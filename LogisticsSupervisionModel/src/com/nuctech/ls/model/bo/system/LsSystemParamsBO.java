package com.nuctech.ls.model.bo.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[系统参数表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_SYSTEM_PARAMS")
public class LsSystemParamsBO {
	/**
	 * 缺省的构造函数
	 */
	public LsSystemParamsBO() {
		super();
	}

	/* 参数主键 */
	private String paramId;

	/* 参数名称 */
	private String paramName;

	/* 参数代码 */
	private String paramCode;

	/* 参数值 */
	private String paramValue;

	/*
	 * 参数状态 0-有效 1-无效
	 */
	private String paramStatus;

	@Id
	@Column(name = "PARAM_ID", nullable = false, length = 50)
	public String getParamId() {
		return this.paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	@Column(name = "PARAM_NAME", nullable = true, length = 100)
	public String getParamName() {
		return this.paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	@Column(name = "PARAM_CODE", nullable = true, length = 50)
	public String getParamCode() {
		return this.paramCode;
	}

	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}

	@Column(name = "PARAM_VALUE", nullable = true, length = 20)
	public String getParamValue() {
		return this.paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	@Column(name = "PARAM_STATUS", nullable = true, length = 2)
	public String getParamStatus() {
		return this.paramStatus;
	}

	public void setParamStatus(String paramStatus) {
		this.paramStatus = paramStatus;
	}
}
