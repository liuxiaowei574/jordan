/**
 * 
 */
package com.nuctech.ls.model.util;

import com.nuctech.util.MessageResourceUtil;

/**
 * 通知类型
 * 
 * @author sunming
 *
 */
public enum NoticeType {

    NomalNotice("0"), // 普通通知
    AlarmNotice("1"), // 报警通知
    TripNotice("2"), // 行程通知
    DispatchNotice("3"), // 调度通知
    TripEscortNotice("4");// 行程护送通知

    private String type;

    private NoticeType(String type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *        the type to set
     */
    public void setType(String type) {
        this.type = type;
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
