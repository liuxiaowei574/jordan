package com.nuctech.util;

/**
 * 登录系统类型
 * 
 * @author liushaowei
 *
 */
public enum LoginSystem {
    /**
     * TRACKING
     */
    TRACKING("TRACKING");

    private String desc; // 描述-国际化key

    private LoginSystem(String desc) {
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
