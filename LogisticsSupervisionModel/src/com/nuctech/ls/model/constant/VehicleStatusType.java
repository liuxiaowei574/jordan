package com.nuctech.ls.model.constant;

public enum VehicleStatusType {
    EntryWaiting("EntryWaiting"," 入口等待"),
    Measuring("Measuring","测量"),
    XRayScanning("XRayScanning","XRay扫描"),
    ImageAnalyzing("ImageAnalyzing","图像分析"),
    DocScanning("DocScanning","单据扫描"),
    Checkin("Checkin","比对站出发"),
    Checkout("Checkout","到达验出站"),
    DataComparing("DataComparing","数据比对"),
    FinishedCheck("FinishedCheck","检验完成");
    
    VehicleStatusType(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    String name;
    String description;

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
}
