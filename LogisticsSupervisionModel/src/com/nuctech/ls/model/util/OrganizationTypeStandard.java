package com.nuctech.ls.model.util;

import com.nuctech.util.MessageResourceUtil;

/**
 * 组织机构类型 1-国家，2-口岸，3-监管场所，4-监管中心,5-trackLink公司
 */
public enum OrganizationTypeStandard {
    Country("1"), Port("2"), Monitor_Place("3"), Manage_Center("4");

    private String text1;

    private OrganizationTypeStandard(String text1) {
        this.text1 = text1;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
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
