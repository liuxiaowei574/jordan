package com.nuctech.ls.model.util;

import com.nuctech.util.MessageResourceUtil;

/**
 * 作者： 刘少伟
 * 修改人：刘同磊
 * 描述：
 * <p>
 * 操作内容类型 枚举操作对象
 * </p>
 * 创建时间：2016年8月25日
 */
public enum OperateContentType {
    /**
     * 添加
     */
    ADD("ADD"),
    /**
     * 修改
     */
    EDIT("EDIT"),
    /**
     * 删除
     */
    DELETE("DELETE"),
    /**
     * 启用
     */
    ENABLE("ENABLE"),
    /**
     * 禁用
     */
    DISABLE("DISABLE"),
    /**
     * 重置
     */
    RESET("RESET"),
    /**
     * 施封
     */
    SETLOCKED("SETLOCKED"),
    /**
     * 解封
     */
    SETUNLOCKED("SETUNLOCKED"),
    /**
     * 解除
     */
    CLEAR("CLEAR"),
    /**
     * 激活
     */
    ACTIVATE("ACTIVATE"),
    /**
     * 结束
     */
    FINISH("FINISH"),
    /**
     * 撤销
     */
    REVOKE("REVOKE"),
    /**
     * 申请
     */
    REQUEST("REQUEST"),
    /**
     * 执行
     */
    EXECUTE("EXECUTE"),
    /**
     * 接收
     */
    RECEIVE("RECEIVE"),
    /**
     * 发布
     */
    PUBLISH("PUBLISH");

    private String desc; // 描述

    private OperateContentType(String desc) {
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
