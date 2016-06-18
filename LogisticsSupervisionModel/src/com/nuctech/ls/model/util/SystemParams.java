/**
 * 
 */
package com.nuctech.ls.model.util;

/**
 * @author sunming
 *
 */
public enum SystemParams {
	
	STAY_TIME("STAY_TIME"),
	STATIC_SPEED("STATIONARY_SPEED"),
	LOW_BATTERY("LOW_BATTERY"),
	//报警行程比例
	ALARM_TRIP_PERCENT("ALARM_TRIP_PERCENT"),
	//严重报警行程比例
	SERIOUS_ALARM_TRIP_PERCENT("SERIOUS_ALARM_TRIP_PERCENT"),
	//常规报警行程比例
	NORMAL_ALARM_TRIP_PERCENT("NORMAL_ALARM_TRIP_PERCENT"),
	//严重报警数量比例
	SERIOUS_ALARM_NUMBER_PERCENT("SERIOUS_ALARM_NUMBER_PERCENT"),
	//轻微报警数量比例
	NORMAL_ALARM_NUMBER_PERCENT("NORMAL_ALARM_NUMBER_PERCENT");
	
	private String name;
	
	private SystemParams(String name){
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	

}
