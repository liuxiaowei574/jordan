package com.nuctech.ls.model.bo.warehouse;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * 业务对象处理的实体-[子锁表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_WAREHOUSE_ESEAL")
public class LsWarehouseEsealBO {
	/**
	 * 缺省的构造函数
	 */
	public LsWarehouseEsealBO() {
		super();
	}

	/* 子锁主键 */
	private String esealId;

	/* 子锁号 */
	private String esealNumber;

	/* 所属节点 */
	private String belongTo;

	/*
	 * 关锁状态 维修、损坏、报废等
	 */
	private String esealStatus;

	/* 创建人 */
	private String createUser;

	/* 创建时间 */
	private Date createTime;

	@Id
	@Column(name = "ESEAL_ID", nullable = false, length = 50)
	public String getEsealId() {
		return this.esealId;
	}

	public void setEsealId(String esealId) {
		this.esealId = esealId;
	}

	@Column(name = "ESEAL_NUMBER", nullable = true, length = 50)
	public String getEsealNumber() {
		return this.esealNumber;
	}

	public void setEsealNumber(String esealNumber) {
		this.esealNumber = esealNumber;
	}

	@Column(name = "BELONG_TO", nullable = true, length = 50)
	public String getBelongTo() {
		return this.belongTo;
	}

	public void setBelongTo(String belongTo) {
		this.belongTo = belongTo;
	}

	@Column(name = "ESEAL_STATUS", nullable = true, length = 2)
	public String getEsealStatus() {
		return this.esealStatus;
	}

	public void setEsealStatus(String esealStatus) {
		this.esealStatus = esealStatus;
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
}
