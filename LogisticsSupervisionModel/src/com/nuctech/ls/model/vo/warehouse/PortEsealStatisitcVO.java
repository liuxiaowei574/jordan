package com.nuctech.ls.model.vo.warehouse;

import java.io.Serializable;

/**
 * 
 * 作者： zsy
 *
 * 描述：
 * <p>
 * 统计分析-口岸子锁统计
 * </p>
 * 创建时间：2016年12月12日
 */
public class PortEsealStatisitcVO implements Serializable {

    /** 
     * 
     */
    private static final long serialVersionUID = 7487656973278736843L;

    // 口岸ID
    private String portId;
    // 子锁状态
    private String esealstatus;

    /**
     * 口岸名称
     */
    private String portName;
    /**
     * 父id
     */
    private String pid;

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

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getEsealstatus() {
        return esealstatus;
    }

    public void setEsealstatus(String esealstatus) {
        this.esealstatus = esealstatus;
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
}
