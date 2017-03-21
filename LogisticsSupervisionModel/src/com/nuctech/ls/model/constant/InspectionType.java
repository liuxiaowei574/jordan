package com.nuctech.ls.model.constant;

import com.nuctech.ls.model.util.ModelUtils;

public enum InspectionType {
    // 图检
    IAS("IAS", "Image Analysis"),
    // 手检
    RAS("RAS", "Recheck Analysis"),
    // 最终放行
    FNLINSPCT("FNLINSPCT", "Final Inspection");

    InspectionType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static InspectionType getConclusionTypeByName(String name) {
        return ModelUtils.getEnumFromString(InspectionType.class, name);
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
