package com.nuctech.ls.model.bo.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[监管节点表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_COMMON_CONTURY")
public class LsCommonConturyBO {
	/**
	 * 缺省的构造函数
	 */
	public LsCommonConturyBO() {
		super();
	}

	/* 主键 */
	private String conturyId;

	/* 国家编号 */
	private String conturyCode;

	/* 国家名称 */
	private String conturyName;

	/* 国家描述 */
	private String conturyDesc;

	@Id
	@Column(name = "CONTURY_ID", nullable = false, length = 50)
	public String getConturyId() {
		return this.conturyId;
	}

	public void setConturyId(String conturyId) {
		this.conturyId = conturyId;
	}

	@Column(name = "CONTURY_CODE", nullable = true, length = 50)
	public String getConturyCode() {
		return this.conturyCode;
	}

	public void setConturyCode(String conturyCode) {
		this.conturyCode = conturyCode;
	}

	@Column(name = "CONTURY_NAME", nullable = true, length = 100)
	public String getConturyName() {
		return this.conturyName;
	}

	public void setConturyName(String conturyName) {
		this.conturyName = conturyName;
	}

	@Column(name = "CONTURY_DESC", nullable = true, length = 200)
	public String getConturyDesc() {
		return this.conturyDesc;
	}

	public void setConturyDesc(String conturyDesc) {
		this.conturyDesc = conturyDesc;
	}
}
