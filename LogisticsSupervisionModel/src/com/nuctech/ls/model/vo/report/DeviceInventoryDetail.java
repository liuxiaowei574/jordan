package com.nuctech.ls.model.vo.report;

import java.io.Serializable;
import java.util.Date;

public class DeviceInventoryDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -459508865085215937L;

	/**
	 * 关锁号
	 */
	private String trackDeviceNumber;
	
	/**
	 * 子锁号
	 */
	private String esealNumber;
	
	/**
	 * 传感器号
	 */
	private String sensorNumber;
	
	/**
	 * 流入时间
	 */
	private Date checkInDate;
	
	/**
	 * 流出时间
	 */
	private Date checkOutDate;
	
	/**
	 * 来源口岸ID
	 */
	private String form;
	
	/**
	 * 目的口岸ID
	 */
	private String to;
	
	/**
	 * 来源口岸名字
	 */
	private String formName;
	
	/**
	 * 目的口岸名字
	 */
	private String toName;

	public String getTrackDeviceNumber() {
		return trackDeviceNumber;
	}

	public void setTrackDeviceNumber(String trackDeviceNumber) {
		this.trackDeviceNumber = trackDeviceNumber;
	}

	public String getEsealNumber() {
		return esealNumber;
	}

	public void setEsealNumber(String esealNumber) {
		this.esealNumber = esealNumber;
	}

	public String getSensorNumber() {
		return sensorNumber;
	}

	public void setSensorNumber(String sensorNumber) {
		this.sensorNumber = sensorNumber;
	}

	public Date getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Date getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	
}
