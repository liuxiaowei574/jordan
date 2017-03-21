package com.nuctech.ls.model.constant;

/**
 * 车辆检测业务类型 转关/出口/进口
 * @author daijundi
 * 
 */
public enum InspectionBusinessType {
    IMPORT("IMPORT", "Import"),
    EXPORT("EXPORT", "Export"), TRANSIT("TRANSIT", "Transit");
    InspectionBusinessType(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    String name;
    String description;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
