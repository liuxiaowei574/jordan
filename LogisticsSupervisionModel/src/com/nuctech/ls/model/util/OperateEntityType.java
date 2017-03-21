package com.nuctech.ls.model.util;

import com.nuctech.util.MessageResourceUtil;

/**
 * 作者： liutonglei
 *
 * 描述：
 * <p>
 * 操作体类型 枚举操作对应的对象
 * </p>
 * 创建时间：2016年11月28日
 */
public enum OperateEntityType {
    /**
     * 用户
     */
    USER("USER"),
    /**
     * 密码
     */
    PASSWORD("PASSWORD"),
    /**
     * 行程
     */
    TRIP("TRIP"),
    /**
     * 关锁
     */
    ELOCK("ELOCK"),
    /**
     * 子锁
     */
    ESEAL("ESEAL"),
    /**
     * 传感器
     */
    SENSOR("SENSOR"),
    /**
     * 车载台
     */
    TRACKUNIT("TRACKUNIT"),
    /**
     * 巡逻队
     */
    PATROL("PATROL"),
    /**
     * 关锁报警
     */
    ELOCK_ALARM("ELOCK_ALARM"),
    /**
     * 报警类型
     */
    ALARMTYPR("ALARMTYPR"),
    /**
     * 设备调度
     */
    DEVICE_DISPATCH("DEVICE_DISPATCH"),
    /**
     * 机构
     */
    DEPARTMENT("DEPARTMENT"),
    /**
     * 通知
     */
    NOTICE("NOTICE");

    private String desc; // 描述

    private OperateEntityType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
