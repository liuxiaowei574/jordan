package com.nuctech.ls.model.bo.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[监管车辆的相关信息 ]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_COMMON_VEHICLE")
public class LsCommonVehicleBO {
	/**
	 * 缺省的构造函数
	 */
	public LsCommonVehicleBO() {
		super();
	}

	/* 主键 */
	private String vehicleId;

	/* 车牌号 */
	private String vehiclePlateNumber;

	/* 报关单号 */
	private String declarationNumber;

	/* 集装箱号 */
	private String containerNumber;

	/* 车辆国家 */
	private String vehicleCountry;

	/* 拖车号 */
	private String trailerNumber;

	/* 司机姓名 */
	private String driverName;

	/* 司机国家 */
	private String driverCountry;

	/* 车辆类型：0-普通车辆; */
	private String vehicleType;

	@Id
	@Column(name = "VEHICLE_ID", nullable = false, length = 50)
	public String getVehicleId() {
		return this.vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	@Column(name = "VEHICLE_PLATE_NUMBER", nullable = true, length = 50)
	public String getVehiclePlateNumber() {
		return this.vehiclePlateNumber;
	}

	public void setVehiclePlateNumber(String vehiclePlateNumber) {
		this.vehiclePlateNumber = vehiclePlateNumber;
	}

	@Column(name = "DECLARATION_NUMBER", nullable = true, length = 50)
	public String getDeclarationNumber() {
		return this.declarationNumber;
	}

	public void setDeclarationNumber(String declarationNumber) {
		this.declarationNumber = declarationNumber;
	}

	@Column(name = "CONTAINER_NUMBER", nullable = true, length = 50)
	public String getContainerNumber() {
		return this.containerNumber;
	}

	public void setContainerNumber(String containerNumber) {
		this.containerNumber = containerNumber;
	}

	@Column(name = "VEHICLE_COUNTRY", nullable = true, length = 100)
	public String getVehicleCountry() {
		return this.vehicleCountry;
	}

	public void setVehicleCountry(String vehicleCountry) {
		this.vehicleCountry = vehicleCountry;
	}

	@Column(name = "TRAILER_NUMBER", nullable = true, length = 50)
	public String getTrailerNumber() {
		return this.trailerNumber;
	}

	public void setTrailerNumber(String trailerNumber) {
		this.trailerNumber = trailerNumber;
	}

	@Column(name = "DRIVER_NAME", nullable = true, length = 100)
	public String getDriverName() {
		return this.driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	@Column(name = "DRIVER_COUNTRY", nullable = true, length = 100)
	public String getDriverCountry() {
		return this.driverCountry;
	}

	public void setDriverCountry(String driverCountry) {
		this.driverCountry = driverCountry;
	}

	@Column(name = "VEHICLE_TYPE", nullable = true, length = 2)
	public String getVehicleType() {
		return this.vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
}
