/**
 * 
 */
package com.nuctech.ls.model.util;

/**
 * @author sunming
 *
 */
public enum SystemParams {

    STAY_TIME("STAY_TIME", ""),
    STATIC_SPEED("STATIONARY_SPEED", ""),
    LOW_BATTERY("LOW_BATTERY", ""),
    // 报警行程比例
    RISK_ALARM_TRIP_PERCENT_RED("RISK_ALARM_TRIP_PERCENT_RED", "66"),
    RISK_ALARM_TRIP_PERCENT_YELLOW("RISK_ALARM_TRIP_PERCENT_YELLOW", "33"),
    // 严重报警行程比例
    RISK_SERIOUS_ALARM_TRIP_PERCENT_RED("RISK_SERIOUS_ALARM_TRIP_PERCENT_RED", "66"),
    RISK_SERIOUS_ALARM_TRIP_PERCENT_YELLOW("RISK_SERIOUS_ALARM_TRIP_PERCENT_YELLOW", "33"),
    // 常规报警行程比例
    RISK_NORMAL_ALARM_TRIP_PERCENT_RED("RISK_NORMAL_ALARM_TRIP_PERCENT_RED", "66"),
    RISK_NORMAL_ALARM_TRIP_PERCENT_YELLOW("RISK_NORMAL_ALARM_TRIP_PERCENT_YELLOW", "33"),
    // 严重报警数量比例
    RISK_SERIOUS_ALARM_NUMBER_PERCENT_RED("RISK_SERIOUS_ALARM_NUMBER_PERCENT_RED", "66"),
    RISK_SERIOUS_ALARM_NUMBER_PERCENT_YELLOW("RISK_SERIOUS_ALARM_NUMBER_PERCENT_YELLOW", "33"),
    // 轻微报警数量比例
    RISK_NORMAL_ALARM_NUMBER_PERCENT_RED("RISK_NORMAL_ALARM_NUMBER_PERCENT_RED", "66"),
    RISK_NORMAL_ALARM_NUMBER_PERCENT_YELLOW("RISK_NORMAL_ALARM_NUMBER_PERCENT_YELLOW", "33"),
    // 各纬度总和占比
    RISK_FINAL_RED("RISK_FINAL_RED", "66"),
    RISK_FINAL_YELLOW("RISK_FINAL_YELLOW", "33");

    private String name;
    private String defaultValue;

    private SystemParams(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *        the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue
     *        the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

}
