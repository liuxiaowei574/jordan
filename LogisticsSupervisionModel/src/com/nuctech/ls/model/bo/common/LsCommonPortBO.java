package com.nuctech.ls.model.bo.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[监管口岸、监管场所或其他监管点]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_COMMON_PORT")
public class LsCommonPortBO {
	/**
	 * 缺省的构造函数
	 */
	public LsCommonPortBO() {
		super();
	}

	/* PORT_ID */
	private String portId;

	/* 主键 */
	private String conturyId;

	/* 节点编号 */
	private String portCode;

	/* 节点名称 */
	private String portName;

	/* 经度 */
	private String longitude;

	/* 纬度 */
	private String latitude;

	/* 节点类型(口岸、监管场所) */
	private String portType;

	/* 节点描述 */
	private String portDesc;

	@Id
	@Column(name = "PORT_ID", nullable = false, length = 50)
	public String getPortId() {
		return this.portId;
	}

	public void setPortId(String portId) {
		this.portId = portId;
	}

	@Column(name = "CONTURY_ID", nullable = true, length = 50)
	public String getConturyId() {
		return this.conturyId;
	}

	public void setConturyId(String conturyId) {
		this.conturyId = conturyId;
	}

	@Column(name = "PORT_CODE", nullable = true, length = 50)
	public String getPortCode() {
		return this.portCode;
	}

	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}

	@Column(name = "PORT_NAME", nullable = true, length = 100)
	public String getPortName() {
		return this.portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	@Column(name = "LONGITUDE", nullable = true, length = 20)
	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Column(name = "LATITUDE", nullable = true, length = 20)
	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@Column(name = "PORT_TYPE", nullable = true, length = 2)
	public String getPortType() {
		return this.portType;
	}

	public void setPortType(String portType) {
		this.portType = portType;
	}

	@Column(name = "PORT_DESC", nullable = true, length = 200)
	public String getPortDesc() {
		return this.portDesc;
	}

	public void setPortDesc(String portDesc) {
		this.portDesc = portDesc;
	}
}
