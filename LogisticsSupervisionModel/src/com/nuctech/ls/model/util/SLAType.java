/**
 * 
 */
package com.nuctech.ls.model.util;

import com.nuctech.util.MessageResourceUtil;

/**
 * @author sunming
 *
 */
public enum SLAType {
    /*
     * NORMAL_SLA("NORMAL_SLA","100"), SERIOUS_SLA("SERIOUS_SLA","1000");
     * private String key; private String punish;
     * private SLAType(String key,String punish){ this.punish = punish; this.key
     * = key; }
     *//**
       * @return the key
       */
    /*
     * public String getKey() { return key; }
     *//**
       * @param key
       *        the key to set
       */
    /*
     * public void setKey(String key) { this.key = key; }
     *//**
       * @return the punish
       */
    /*
     * public String getPunish() { return punish; }
     *//**
       * @param punish
       *        the punish to set
       *//*
         * public void setPunish(String punish) { this.punish = punish; }
         */
    NORMAL_SLA("100"), // 普通事故
    SERIOUS_SLA("1000");// 严重事故

    private String text;

    private SLAType(String text) {
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
