package com.nuctech.ls.model.vo.warehouse;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 调度计划 VO
 * </p>
 * 创建时间：2016年6月21日
 */
public class DispatchPlanVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2693263602651417041L;

    /**
     * 调度口岸ID
     */
    private String portId;

    /**
     * 调度口岸名称
     */
    private String portName;

    /**
     * 关锁数
     */
    private String trackDeviceNumber;

    /**
     * 子锁数
     */
    private String esealNumber;

    /**
     * 传感器数
     */
    private String sensor;

    /**
     * 距离需求口岸的距离
     */
    private BigDecimal distance;

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

    public String getTrackDeviceNumber() {
        return trackDeviceNumber;
    }

    public void setTrackDeviceNumber(String trackDeviceNumber) {
        this.trackDeviceNumber = trackDeviceNumber;
    }

    public String getEsealNumber() {
        return esealNumber;
    }

    public void setEsealNumber(String esealNumber) {
        this.esealNumber = esealNumber;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

}
