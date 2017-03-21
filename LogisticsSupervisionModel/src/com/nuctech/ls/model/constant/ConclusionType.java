package com.nuctech.ls.model.constant;

import com.nuctech.ls.model.util.ModelUtils;

public enum ConclusionType {
    
    // 一检结论: Pass, Check
    PASS("PASS", "Pass"), REFER_TO_SECONDARY_CHECK("REFER_TO_SECONDARY_CHECK", "Check"),
    
    // 二检结论: Pass, Refer for external agencies, Detection
    REFER_FOR_EXTERNAL_AGENCIES("REFER_FOR_EXTERNAL_AGENCIES", "Refer for external agencies"),
    ABNORMITY_DETECTED("DETECTION", "Detection"),
    RECHECK("RECHECK","Recheck");//重新检查
    
    ConclusionType(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public static ConclusionType getConclusionTypeByName(String name) {
        if (name != null && name.toUpperCase().equals("PASS")) {
            return PASS;
        }
        if (name != null && name.toUpperCase().equals("DETECTION")) {
            return ABNORMITY_DETECTED;
        } else {
            ConclusionType type = ModelUtils.getEnumFromString(ConclusionType.class, name);
            return type;
        }
    }
    
    String name;
    String description;
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
}
