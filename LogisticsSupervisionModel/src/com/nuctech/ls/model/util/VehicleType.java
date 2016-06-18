/**
 * 
 */
package com.nuctech.ls.model.util;

/**
 * 车辆类型
 * 
 * @author sunming
 *
 */
public enum VehicleType {
	
	Patrol("1"),
	Vehicle("0");
	
	
	private String type;
	
	private VehicleType(String type){
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
