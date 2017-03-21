package com.nuctech.ls.model.constant;

public enum ELockStatusType {
    OFFLINE("OFFLINE", "心跳包超时"),
    REMOVE("REMOVE", "被拆报警"), 
    SEALED("SEALED", "施封状态"),
    UNSEALED("UNSEALED", "解封状态"),
    UNKNOWN("UNKNOWN", "未知");
    ELockStatusType(String name, String description) {
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
