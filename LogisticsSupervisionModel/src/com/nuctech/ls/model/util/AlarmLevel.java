/**
 * 
 */
package com.nuctech.ls.model.util;

import com.nuctech.util.MessageResourceUtil;

/**
 * 报警等级枚举类
 */
public enum AlarmLevel {

    // 轻微
    Light("0"),
    // 严重
    Serious("1");

    private String text;

    private AlarmLevel(String text) {
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
