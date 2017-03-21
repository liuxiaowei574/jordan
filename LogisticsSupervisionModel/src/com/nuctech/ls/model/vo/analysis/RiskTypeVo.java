package com.nuctech.ls.model.vo.analysis;

import java.io.Serializable;

/**
 * @author liangpengfei
 * 
 *         风险参数模型
 *
 */
public class RiskTypeVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // id
    private String id;
    // pid
    private String pid;
    // 类别名称
    private String name;
    // 请求地址
    private String requestUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

}
