package com.nuctech.ls.model.vo.statistic;

import java.io.Serializable;

/**
 * @author liangpengfei
 * 
 *         车辆监管统计报告
 *
 */
public class VehicleStaticVo implements Serializable {

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
    // 统计数量
    private String number;
    // 明细内容
    // 车牌
    private String vehiclePlateNumber;
    // 车辆类型
    private String vehicleType;
    // 所属国家
    private String vehicleCountry;
    // 检出时间
    private String checkoutTime;
    // 检出口岸
    private String checkoutPort;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPortid() {
        return portid;
    }

    public void setPortid(String portid) {
        this.portid = portid;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleCountry() {
        return vehicleCountry;
    }

    public void setVehicleCountry(String vehicleCountry) {
        this.vehicleCountry = vehicleCountry;
    }

    public String getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public String getCheckoutPort() {
        return checkoutPort;
    }

    public void setCheckoutPort(String checkoutPort) {
        this.checkoutPort = checkoutPort;
    }

}
