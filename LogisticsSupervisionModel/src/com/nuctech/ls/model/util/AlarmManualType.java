/**
 * 
 */
package com.nuctech.ls.model.util;

/**
 * @author sunming
 *
 */
public enum AlarmManualType {
    /**
     * 非手动，0
     */
    Unmanual("0"),
    /**
     * 手动，1
     */
    Manual("1");

    private String alarmType;

    private AlarmManualType(String alarmType) {
        this.alarmType = alarmType;
    }

    /**
     * @return the alarmType
     */
    public String getAlarmType() {
        return alarmType;
    }

    /**
     * @param alarmType
     *        the alarmType to set
     */
    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }
}
