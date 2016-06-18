package com.nuctech.ls.model.bo.monitor;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * 业务对象处理的实体-[监控组信息]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_MONITOR_GROUP")
public class LsMonitorGroupBO {
	/**
	 * 缺省的构造函数
	 */
	public LsMonitorGroupBO() {
		super();
	}

	/* 监控组主键 */
	private String groupId;

	/* 监控组名称 */
	private String groupName;

	/* 创建人员 */
	private String createUser;

	/* 创建时间 */
	private Date createTime;

	@Id
	@Column(name = "GROUP_ID", nullable = false, length = 50)
	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Column(name = "GROUP_NAME", nullable = true, length = 100)
	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
