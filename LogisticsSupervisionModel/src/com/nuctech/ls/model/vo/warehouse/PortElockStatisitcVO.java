package com.nuctech.ls.model.vo.warehouse;

import java.io.Serializable;

/**
 * 
 * 作者： liang pengfei
 *
 * 描述：
 * <p>
 * 统计分析-口岸关锁统计
 * </p>
 * 创建时间：2016年6月3日
 */
public class PortElockStatisitcVO implements Serializable {

    /** 
     * 
     */
    private static final long serialVersionUID = 7487656973278736843L;

    // 口岸ID
    private String portId;
    // 关锁状态
    private String elockstatus;

    /**
     * 口岸名称
     */
    private String portName;
    /**
     * 父id
     */
    private String pid;

    /**
     * 可用关锁
     */
    private Integer keyongs;

    /**
     * 损坏关锁
     */
    private Integer sunhuais;

    /**
     * 在途关锁
     */
    private Integer zaitus;
    /**
     * 维修关锁
     */
    private Integer weixius;

    /**
     * 报废关锁
     */
    private Integer baofeis;

    /**
     * 可用子锁
     */
    private Integer ekeyongs;

    /**
     * 损坏子锁
     */
    private Integer esunhuais;

    /**
     * 在途子锁
     */
    private Integer ezaitus;
    /**
     * 维修子锁
     */
    private Integer eweixius;

    /**
     * 报废子锁
     */
    private Integer ebaofeis;

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
     * 报废子锁
     */
    private Integer sbaofeis;

    public Integer getZaitus() {
        return zaitus;
    }

    public void setZaitus(Integer zaitus) {
        this.zaitus = zaitus;
    }

    public Integer getBaofeis() {
        return baofeis;
    }

    public void setBaofeis(Integer baofeis) {
        this.baofeis = baofeis;
    }

    public Integer getWeixius() {
        return weixius;
    }

    public void setWeixius(Integer weixius) {
        this.weixius = weixius;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
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

    public Integer getKeyongs() {
        return keyongs;
    }

    public void setKeyongs(Integer keyongs) {
        this.keyongs = keyongs;
    }

    public String getElockstatus() {
        return elockstatus;
    }

    public void setElockstatus(String elockstatus) {
        this.elockstatus = elockstatus;
    }

    public Integer getSunhuais() {
        return sunhuais;
    }

    public void setSunhuais(Integer sunhuais) {
        this.sunhuais = sunhuais;
    }

    public Integer getEkeyongs() {
        return ekeyongs;
    }

    public void setEkeyongs(Integer ekeyongs) {
        this.ekeyongs = ekeyongs;
    }

    public Integer getEsunhuais() {
        return esunhuais;
    }

    public void setEsunhuais(Integer esunhuais) {
        this.esunhuais = esunhuais;
    }

    public Integer getEzaitus() {
        return ezaitus;
    }

    public void setEzaitus(Integer ezaitus) {
        this.ezaitus = ezaitus;
    }

    public Integer getEweixius() {
        return eweixius;
    }

    public void setEweixius(Integer eweixius) {
        this.eweixius = eweixius;
    }

    public Integer getEbaofeis() {
        return ebaofeis;
    }

    public void setEbaofeis(Integer ebaofeis) {
        this.ebaofeis = ebaofeis;
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
