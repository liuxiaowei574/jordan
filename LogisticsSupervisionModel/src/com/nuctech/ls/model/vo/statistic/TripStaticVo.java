package com.nuctech.ls.model.vo.statistic;

import java.io.Serializable;

/**
 * @author liangpengfei
 * 
 *         行程监管统计报告
 *
 */
public class TripStaticVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1638822686819752423L;
    // 口岸名称
    private String portid;
    // 口岸名称
    private String portname;
    // 开始时间
    private String starttime;
    // 结束时间
    private String endtime;
    // 统计数量-检入数
    private String checkins;
    // 统计数量-检出数
    private String checkouts;
    // 检出/入时间
    private String ctime;
    // 检出/入标志（ 1 检出 0 检入）
    private String flag;
    // 检出/入状态
    private String status;
    // 检出入人员
    private String user;

    public String getPortid() {
        return portid;
    }

    public void setPortid(String portid) {
        this.portid = portid;
    }

    public String getPortname() {
        return portname;
    }

    public void setPortname(String portname) {
        this.portname = portname;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getCheckins() {
        return checkins;
    }

    public void setCheckins(String checkins) {
        this.checkins = checkins;
    }

    public String getCheckouts() {
        return checkouts;
    }

    public void setCheckouts(String checkouts) {
        this.checkouts = checkouts;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
