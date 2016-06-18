package com.nuctech.ls.model.util;

public enum DeviceType {

	//关锁
	TRACKING_DEVICE("TRACKING_DEVICE"),
	//子锁
	ESEAL("ESEAL"),
	//传感器
	SENSOR("SENSOR");
		
	private String type;
		
	private DeviceType(String type){
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
