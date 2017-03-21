package com.nuctech.ls.model.constant;

public enum AlarmType {
    VEHICLE_BLACKLIST("VEHICLE_BLACKLIST", "黑名单车辆", 2),
    VEHICLE_DEVIATE_LINE("VEHICLE_DEVIATE_LINE", "偏离指定线路", 1),
    VEHICLE_DEVIATE_REGION("VEHICLE_DEVIATE_REGION", "偏离指定区域", 2),
    VEHICLE_STAY_TOOLONG("VEHICLE_STAY_TOOLONG", "滞留时间过长", 2),
    VEHICLE_ARRIVE_LATE("VEHICLE_ARRIVE_LATE", "未在指定时间内到达", 2),
    VEHICLE_MISSING("VEHICLE_MISSING", "车辆失踪", 2),
    ELOCK_BATTERY_LOW("ELOCK_BATTERY_LOW", "电子关锁电量过低", 2),
    ELOCK_UNSEAL("ELOCK_UNSEAL", "电子关锁解封", 1),
    ELOCK_SEAL("ELOCK_SEAL", "电子关锁施封", 2),
    ELOCK_OPEN_ILLEGAL("ELOCK_OPEN_ILLEGAL", "电子关锁非法开启", 1);

    AlarmType(String name, String description, int level) {
        this.name = name;
        this.description = description;
        this.level = level;
    }

    String name;
    String description;
    int level;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }

}
