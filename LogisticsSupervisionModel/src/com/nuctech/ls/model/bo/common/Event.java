package com.nuctech.ls.model.bo.common;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

public class Event implements Serializable {

    private static final long serialVersionUID = -510427274346113271L;

    private static final Logger logger = Logger.getLogger(Event.class.getName());
    /**
     * 区域
     */
    private String site;
    /**
     * 道号
     */
    private Integer laneNumber;
    /**
     * 设备名称
     */
    private String device;
    /**
     * 设备序号
     */
    private Integer deviceNumber;
    /**
     * 事件
     */
    private String event;
    /**
     * 事件发生时间
     */
    private Date eventDateTime;

    private Properties dataPropts;

    public Event() {
    }

    public Event(String site, Integer laneNumber, String device, Integer deviceNumber, String event) {
        this.site = site;
        this.laneNumber = laneNumber;
        this.device = device;
        this.deviceNumber = deviceNumber;
        this.event = event;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getLaneNumber() {
        return laneNumber;
    }

    public void setLaneNumber(Integer laneNumber) {
        this.laneNumber = laneNumber;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Integer getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(Integer deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(Date eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public Event putData(String dataName, String dataValue) {
        if (dataName == null || "".equals(dataName)) {
            logger.info("site=" + this.site + ";laneNumber=" + this.laneNumber + ";event=" + this.event);
            return this;
        }
        if (dataPropts == null) {
            dataPropts = new Properties();
        }
        if (dataValue != null)
            dataPropts.put(dataName, dataValue);
        return this;
    }

    /**
     * @return the data
     */
    public Properties getDataPropts() {
        if (dataPropts == null) {
            dataPropts = new Properties();
        }
        return dataPropts;
    }

    @Override
    public String toString() {
        return "Event [site=" + site + ", laneNumber=" + laneNumber + ", device=" + device + ", deviceNumber="
                + deviceNumber + ", eventDateTime=" + eventDateTime + ", event=" + event + "]";
    }
}
