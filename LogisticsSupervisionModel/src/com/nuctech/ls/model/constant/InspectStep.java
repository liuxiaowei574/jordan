package com.nuctech.ls.model.constant;
/**
 * 检查环节
 * @author daijundi
 *
 */
public enum InspectStep {
    AllStep("AllStep","所有检验环节"),
    MeasureStep("MeasureStep","测量环节"),
    WeighStep("WeighStep","称重环节"),
    XRayScanStep("XRayScanStep","XRay扫描环节"),
    ImageAnalysis("ImageAnalysis","图像分析环节"),
    DocScanStep("DocScanStep","单据扫描环节"),
    DataCompareStep("DataCompareStep","数据比对环节");
    InspectStep(String name, String description) {
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
