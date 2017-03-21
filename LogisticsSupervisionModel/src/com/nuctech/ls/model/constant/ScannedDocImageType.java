package com.nuctech.ls.model.constant;

public enum ScannedDocImageType {
    Passport("Passport","护照扫描件"),
    Manifest("Manifest","报关单"),
    Invoice("invoice","发票"),
    LPR("LPR","车号识别图片"),
    CCR("CCR","箱号识别图片"),
    Camera("Camera","摄像头抓拍照片"),
    Bottom("Bottom","底盘照片"),
    SC("SC","车辆扫面图片");


    ScannedDocImageType(String name, String description) {
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
