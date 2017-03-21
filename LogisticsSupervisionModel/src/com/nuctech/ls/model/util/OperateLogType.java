package com.nuctech.ls.model.util;

/**
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 操作日志 类型 枚举对象
 * </p>
 * 创建时间：2016年5月18日
 */
public enum OperateLogType {

    PUSH("推送", "1"), HANDLE("处理", "0");

    private String desc; // 描述
    private String value; // 值

    private OperateLogType(String desc, String value) {
        this.desc = desc;
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
