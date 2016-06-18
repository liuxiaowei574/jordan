/**
 * 
 */
package com.nuctech.ls.model.util;

/**
 * 设备工作状态
 * 
 * @author sunming
 *
 */
public enum DeviceWorkStatus {
	/* 关锁状态、1-施封、0-解封 */
	DEVICE_ELOCKSTATUS_SEAL("1"), DEVICE_ELOCKSTATUS_UNSEAL("0"),
	/* 锁杆状态 1-关，2-开 */
	DEVICE_POLESTATUS_CLOSED("1"), DEVICE_POLESTATUS_OPENED("2"),
	/* 防拆状态 antidismantle 0-正常 1-被拆 */
	DEVICE_BROKENSTATUS_NORMAL("0"), DEVICE_BROKENSTATUS_BROKEN("1"),
	
	// 子锁正常
	SEAL_NORMAL("2"),
	// 子锁被卸下
	SEAL_OFF("3"),
	// 子锁被拆
	SEAL_BROKEN("4"),
	// 子锁失联
	SEAL_LOST("5"),

	// 传感器正常
	SENSOR_NORMAL("2"),
	// 传感器被卸下
	SENSOR_OFF("3"),
	// 传感器被拆
	SENSOR_BROKEN("4"),
	// 传感器失联
	SENSOR_LOST("5");

	private String text;

	private DeviceWorkStatus(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
