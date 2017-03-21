package com.nuctech.ls.model.util;

import com.nuctech.util.MessageResourceUtil;

/**
 * 组织机构类型 1-国家，2-口岸，3-监管场所，4-监管中心,5-trackLink公司
 */
public enum OrganizationType {
    Country("1"), Port("2"), Monitor_Place("3"), Manage_Center("4"); // ,Device_room("5");

    private String text;

    private OrganizationType(String text) {
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
