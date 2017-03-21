package com.nuctech.ls.model.vo.analysis;

import java.io.Serializable;

/**
 * @author liangpengfei
 * 
 *         风险参数模型
 *
 */
public class RiskSettingVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // id
    private String id;
    // 参数项名称
    private String name;
    // 参数项第二名称
    private String secnm;
    // 参数项低风险阀值
    private String lowv;
    // 参数项中风险阀值
    private String midv;
    // 参数项高风险阀值
    private String hightv;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecnm() {
        return secnm;
    }

    public void setSecnm(String secnm) {
        this.secnm = secnm;
    }

    public String getLowv() {
        return lowv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLowv(String lowv) {
        this.lowv = lowv;
    }

    public String getMidv() {
        return midv;
    }

    public void setMidv(String midv) {
        this.midv = midv;
    }

    public String getHightv() {
        return hightv;
    }

    public void setHightv(String hightv) {
        this.hightv = hightv;
    }

}
