package com.nuctech.ls.model.bo.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 设备状态变化报告参数设置的实体-[生成报告的参数设置表]
 *
 * @author： zsy
 */

@Entity
@Table(name = "LS_REGULAR_REPORT_PARAMETER_SET")
public class LsRegularReportParaSetBO implements Serializable {

    private static final long serialVersionUID = 1L;

    public LsRegularReportParaSetBO() {
        super();
    }

    /* 主键 */
    private String reportId;

    /* 名称 */
    private String reportName;

    /*
     * 类型 -车载台状态报告类型，关锁状态报告类型等 ;0-车载台状态变化报告,1-关锁状态变化报告,2-子锁状态变化报告,3-传感器状态变化报告等
     */
    private String reportType;

    /* 周期 */
    private String cycle;

    /* 定制时间 生成报告产生的时间间隔 */
    private int customTime;

    /* 状态-0：启用；1：禁用 */
    private String isEnable;

    @Id
    @Column(name = "REPORT_ID", nullable = false, length = 50)
    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    @Column(name = "REPORT_NAME", nullable = true, length = 100)
    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    @Column(name = "REPORT_TYPE", nullable = true, length = 100)
    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    @Column(name = "REPORT_CYCLE", nullable = true, length = 100)
    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    @Column(name = "CUSTOM_TIME", nullable = true, length = 100)
    public int getCustomTime() {
        return customTime;
    }

    public void setCustomTime(int customTime) {
        this.customTime = customTime;
    }

    @Column(name = "IS_ENABLE", nullable = true, length = 100)
    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
}
