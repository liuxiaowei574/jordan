package com.nuctech.ls.model.util;

public enum ParamsType {
    nomalParamsType("0"), // 常规类型参数
    alarmParamsType("1"); // 告警类型参数

    private String type;

    private ParamsType(String type) {
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

}
