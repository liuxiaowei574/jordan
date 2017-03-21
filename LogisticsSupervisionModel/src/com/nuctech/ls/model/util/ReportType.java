package com.nuctech.ls.model.util;

import com.nuctech.util.MessageResourceUtil;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 关锁状态 枚举类
 * </p>
 * 创建时间：2016年6月3日
 */
public enum ReportType {

    TrackUnit("0"), // 车载台
    Elock("1"), // 关锁
    Eseal("2"), // 子锁
    Sensor("3"); // 传感器

    private String text;

    private ReportType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
