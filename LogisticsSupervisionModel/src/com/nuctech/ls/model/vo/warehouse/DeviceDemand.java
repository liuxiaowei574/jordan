package com.nuctech.ls.model.vo.warehouse;

import java.io.Serializable;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 设备需求
 * </p>
 * 创建时间：2016年6月21日
 */
public class DeviceDemand implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 9040129065221095842L;

    private String portId;

    private Integer trackDeviceNumber;

    private Integer esealNumber;

    private Integer sensorNumber;

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public Integer getTrackDeviceNumber() {
        return trackDeviceNumber;
    }

    public void setTrackDeviceNumber(Integer trackDeviceNumber) {
        this.trackDeviceNumber = trackDeviceNumber;
    }

    public Integer getEsealNumber() {
        return esealNumber;
    }

    public void setEsealNumber(Integer esealNumber) {
        this.esealNumber = esealNumber;
    }

    public Integer getSensorNumber() {
        return sensorNumber;
    }

    public void setSensorNumber(Integer sensorNumber) {
        this.sensorNumber = sensorNumber;
    }

}
