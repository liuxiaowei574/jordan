/**
 * 
 */
package com.nuctech.ls.model.util;

import com.nuctech.util.MessageResourceUtil;

/**
 * 报警类型
 * 
 * @author sunming
 *
 */
public enum AlarmType {
    // 长时间滞留
    LONG_STAY("LONG_STAY"),
    // 路线偏移
    ROUTE_DEVIATION("ROUTE_DEVIATION"),
    // 卫星信号丢失
    SATELLITE_LOSS("SATELLITE_LOSS"),
    // GSM信号丢失
    GSM_LOSS("GSM_LOSS"),
    // 开锁
    OPEN_LOCK("OPEN_LOCK"),
    // 开锁又重新关闭
    CLOSED_LOCK("CLOSED_LOCK"),
    // 反向行驶
    OPPOSITE_ROUTE("OPPOSITE_ROUTE"),
    // 强拆报警
    DEVICE_BROKEN("DEVICE_BROKEN"),
    // 危险区域报警
    ENTER_DANGEROUS_AREA("ENTER_DANGEROUS_AREA"),
    // 误报
    FALSE_ALARM("FALSE_ALARM"),
    // 低电量
    LOW_BATTERY("LOW_BATTERY"),
    // 行程超时报警
    EXCEEDING_TRIP_ALLOWED_TIME("EXCEEDING_TRIP_ALLOWED_TIME"),
    // 子锁失联
    ESEAL_LOSS("ESEAL_LOSS"),
    // 子锁破坏
    TAMPERING_WITH_ESEAL("TAMPERING_WITH_ESEAL"),
    // 传感器失联
    SENSOR_LOSS("SENSOR_LOSS"),
    // 传感器破坏
    TAMPERING_WITH_SENSOR("TAMPERING_WITH_SENSOR"),
    // 车载台报警
    TRACK_UNIT_ALARM("TRACK_UNIT_ALARM"),
    // target zoon报警
    TARGET_ZOON("TARGET_ZOON");

    private String alarmType;

    private AlarmType(String alarmType) {
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

    /**
     * 从资源文件读取国际化值
     * 
     * @return
     */
    public String getKey() {
        return MessageResourceUtil.getMessageInfo(this.getClass().getSimpleName() + "." + this.name());
    }

}
