package com.nuctech.ls.model.vo.warehouse;

import java.io.Serializable;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 调度分析 实际方案
 * </p>
 * 创建时间：2016年6月7日
 */
public class DispatchActualProgram implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4044231939440312546L;

    /**
     * 口岸ID
     */
    private String portId;

    /**
     * 口岸名称
     */
    private String portName;

    /**
     * 可用关锁
     */
    private Integer availableTrackDevice;

    /**
     * 可用子锁
     */
    private Integer availableEseal;

    /**
     * 可用传感器
     */
    private Integer availableSensor;

    /* 经度 */
    private String longitude;

    /* 纬度 */
    private String latitude;

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

    public Integer getAvailableTrackDevice() {
        return availableTrackDevice;
    }

    public void setAvailableTrackDevice(Integer availableTrackDevice) {
        this.availableTrackDevice = availableTrackDevice;
    }

    public Integer getAvailableEseal() {
        return availableEseal;
    }

    public void setAvailableEseal(Integer availableEseal) {
        this.availableEseal = availableEseal;
    }

    public Integer getAvailableSensor() {
        return availableSensor;
    }

    public void setAvailableSensor(Integer availableSensor) {
        this.availableSensor = availableSensor;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

}
