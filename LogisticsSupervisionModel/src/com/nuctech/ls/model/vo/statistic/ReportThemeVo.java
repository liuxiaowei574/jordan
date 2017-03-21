package com.nuctech.ls.model.vo.statistic;

/**
 * @author liangpengfei
 * 
 * 
 *         动态报表主题
 *
 */
public class ReportThemeVo {

    // 主题的ID，，
    private int id;
    // 主题的名称
    private String name;
    // 主题的父ID
    private int pid;
    // 主题的请求地址
    private String requestUrl;
    // 主题的列
    private String[] columns;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

}
