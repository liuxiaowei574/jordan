package com.nuctech.ls.model.vo.statistic;

/**
 * @author liangpengfei
 * 
 * 
 *         动态报表维度
 *
 */
public class ReportDimensionVo {

    // 维度包含的列
    private String[] columns;
    // 维度的名称
    private String name;

    public ReportDimensionVo addName(String name) {
        this.name = name;
        return this;
    }

    public ReportDimensionVo addColumns(String[] columns) {
        this.columns = columns;
        return this;
    }

    public String[] getColumns() {
        return columns;
    }

    public String getName() {
        return name;
    }

}
