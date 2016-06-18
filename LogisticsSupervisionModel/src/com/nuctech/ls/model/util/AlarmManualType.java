/**
 * 
 */
package com.nuctech.ls.model.util;

/**
 * @author sunming
 *
 */
public enum AlarmManualType {
	Manual("0"),Unmanual("1");
	
	
	private String alarmType;
	
	private AlarmManualType(String alarmType){
		this.alarmType = alarmType;
	}

	/**
	 * @return the alarmType
	 */
	public String getAlarmType() {
		return alarmType;
	}

	/**
	 * @param alarmType the alarmType to set
	 */
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
}
