package com.nuctech.ls.model.vo.warehouse;

import java.io.Serializable;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * TODO
 * </p>
 * 创建时间：2016年6月3日
 */
public class DispatchPortChartVO implements Serializable {

    /** 
     * 
     */
    private static final long serialVersionUID = 1306672752010950431L;

    /**
     * 可用关锁
     */
    private Integer availableTrackDevice;

    /**
     * 损坏关锁
     */
    private Integer destroyTrackDevice;

    /**
     * 预留关锁
     */
    private Integer reservationTrackDevice;

    /**
     * 可用子锁
     */
    private Integer availableEseal;

    /**
     * 损坏子锁
     */
    private Integer destroyEseal;

    /**
     * 预留子锁
     */
    private Integer reservationEseal;

    /**
     * 可用传感器
     */
    private Integer availableSensor;

    /**
     * 损坏传感器
     */
    private Integer destroySensor;

    /**
     * 预留传感器
     */
    private Integer reservationSensor;

    public Integer getAvailableTrackDevice() {
        return availableTrackDevice;
    }

    public void setAvailableTrackDevice(Integer availableTrackDevice) {
        this.availableTrackDevice = availableTrackDevice;
    }

    public Integer getDestroyTrackDevice() {
        return destroyTrackDevice;
    }

    public void setDestroyTrackDevice(Integer destroyTrackDevice) {
        this.destroyTrackDevice = destroyTrackDevice;
    }

    public Integer getReservationTrackDevice() {
        return reservationTrackDevice;
    }

    public void setReservationTrackDevice(Integer reservationTrackDevice) {
        this.reservationTrackDevice = reservationTrackDevice;
    }

    public Integer getAvailableEseal() {
        return availableEseal;
    }

    public void setAvailableEseal(Integer availableEseal) {
        this.availableEseal = availableEseal;
    }

    public Integer getDestroyEseal() {
        return destroyEseal;
    }

    public void setDestroyEseal(Integer destroyEseal) {
        this.destroyEseal = destroyEseal;
    }

    public Integer getReservationEseal() {
        return reservationEseal;
    }

    public void setReservationEseal(Integer reservationEseal) {
        this.reservationEseal = reservationEseal;
    }

    public Integer getAvailableSensor() {
        return availableSensor;
    }

    public void setAvailableSensor(Integer availableSensor) {
        this.availableSensor = availableSensor;
    }

    public Integer getDestroySensor() {
        return destroySensor;
    }

    public void setDestroySensor(Integer destroySensor) {
        this.destroySensor = destroySensor;
    }

    public Integer getReservationSensor() {
        return reservationSensor;
    }

    public void setReservationSensor(Integer reservationSensor) {
        this.reservationSensor = reservationSensor;
    }

}
