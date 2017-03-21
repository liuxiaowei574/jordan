package com.nuctech.ls.model.vo.warehouse;

import java.io.Serializable;

/**
 * 
 * 作者： zsy
 *
 * 描述：
 * <p>
 * 统计分析-口岸传感器统计
 * </p>
 * 创建时间：2016年12月12日
 */
public class PortSensorStatisitcVO implements Serializable {

    /** 
     * 
     */
    private static final long serialVersionUID = 7487656973278736843L;

    // 口岸ID
    private String portId;
    // 子锁状态
    private String sensorstatus;

    /**
     * 口岸名称
     */
    private String portName;
    /**
     * 父id
     */
    private String pid;

    /**
     * 可用传感器
     */
    private Integer skeyongs;

    /**
     * 损坏传感器
     */
    private Integer ssunhuais;

    /**
     * 在途传感器
     */
    private Integer szaitus;
    /**
     * 维修传感器
     */
    private Integer sweixius;

    /**
     * 报废传感器
     */
    private Integer sbaofeis;

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getSensorstatus() {
        return sensorstatus;
    }

    public void setSensorstatus(String sensorstatus) {
        this.sensorstatus = sensorstatus;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getSkeyongs() {
        return skeyongs;
    }

    public void setSkeyongs(Integer skeyongs) {
        this.skeyongs = skeyongs;
    }

    public Integer getSsunhuais() {
        return ssunhuais;
    }

    public void setSsunhuais(Integer ssunhuais) {
        this.ssunhuais = ssunhuais;
    }

    public Integer getSzaitus() {
        return szaitus;
    }

    public void setSzaitus(Integer szaitus) {
        this.szaitus = szaitus;
    }

    public Integer getSweixius() {
        return sweixius;
    }

    public void setSweixius(Integer sweixius) {
        this.sweixius = sweixius;
    }

    public Integer getSbaofeis() {
        return sbaofeis;
    }

    public void setSbaofeis(Integer sbaofeis) {
        this.sbaofeis = sbaofeis;
    }
}
