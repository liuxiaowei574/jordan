package com.nuctech.ls.model.vo.warehouse;

import java.io.Serializable;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 调度分析 口岸 VO
 * </p>
 * 创建时间：2016年6月3日
 */
public class DispatchPortVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7487656973278736843L;

    // 口岸ID
    private String portId;

    /**
     * 口岸名称
     */
    private String portName;

    /**
     * 关锁平均值
     */
    private Integer averageTrackDevice;
    /**
     * 关锁可用值(正常状态)
     */
    private Integer trackDeviceNumber;

    /**
     * 子锁平均值
     */
    private Integer averageEseal;
    /**
     * 子锁 可用值(正常状态)
     */
    private Integer eseal;
    /**
     * 传感器平均值
     */
    private Integer averageSensor;
    /**
     * 传感器 可用值(正常状态)
     */
    private Integer sensor;

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

    public Integer getAverageTrackDevice() {
        return averageTrackDevice;
    }

    public void setAverageTrackDevice(Integer averageTrackDevice) {
        this.averageTrackDevice = averageTrackDevice;
    }

    public Integer getAverageEseal() {
        return averageEseal;
    }

    public void setAverageEseal(Integer averageEseal) {
        this.averageEseal = averageEseal;
    }

    public Integer getAverageSensor() {
        return averageSensor;
    }

    public void setAverageSensor(Integer averageSensor) {
        this.averageSensor = averageSensor;
    }

    public Integer getTrackDeviceNumber() {
        return trackDeviceNumber;
    }

    public void setTrackDeviceNumber(Integer trackDeviceNumber) {
        this.trackDeviceNumber = trackDeviceNumber;
    }

    public Integer getEseal() {
        return eseal;
    }

    public void setEseal(Integer eseal) {
        this.eseal = eseal;
    }

    public Integer getSensor() {
        return sensor;
    }

    public void setSensor(Integer sensor) {
        this.sensor = sensor;
    }
}
