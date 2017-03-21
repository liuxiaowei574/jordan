package com.nuctech.ls.model.constant;

import com.nuctech.ls.model.util.ModelUtils;

public enum EnumSite {
    /**
     * 检入
     */
    SCA("SCA", "Scanning Area"), // 新加坡ICA
    /**
     * 一检等待区
     */
    PCB("PCB", "Primary Clearance Booths"), // 新加坡ICA
    /**
     * 二检区
     */
    SIA("SIA", "Second inspect area"), // 新加坡ICA
    /**
     * 出口区
     */
    EVS("EVS", "Exit Verification Site"),
    /**
     * 通勤通道
     */
    CCA("CCA", "CCA"),

    /**
     * 离开出口后场外
     */
    OFFSITE("OFFSITE", "Out of the inspection area"),
    /**
     * 比对站
     */
    CDA("CDA", "Compare Data Area");

    /**
     * 构造函数
     * 
     * @param siteName
     * @param siteDescription
     */
    EnumSite(String siteName, String siteDescription) {
        this.siteName = siteName;
        this.siteDescription = siteDescription;
    }

    String siteName;
    String siteDescription;

    public String getDescription() {
        return this.siteDescription;
    }

    public static EnumSite getEnumSiteByName(String siteName) {
        if (siteName == null || "".equals(siteName)) {
            return ModelUtils.getEnumFromString(EnumSite.class, siteName);
        } else {
            return null;
        }
    }

    public String getSiteName() {
        return this.siteName;
    }
}
