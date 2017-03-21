package com.nuctech.ls.model.util;

import com.nuctech.util.MessageResourceUtil;

public enum AlarmDealMethod {

    /**
     * 误报警
     */
    FailAlarm("0"),
    /**
     * 正常处理
     */
    NormalDeal("1"),
    /**
     * 转发
     */
    Forward("2"),
    /**
     * 撤销
     */
    Revoke("3");

    private String text;

    private AlarmDealMethod(String text) {
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
