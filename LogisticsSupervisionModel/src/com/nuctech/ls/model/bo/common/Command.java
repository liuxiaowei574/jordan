package com.nuctech.ls.model.bo.common;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;

public class Command implements Serializable {

    private static final long serialVersionUID = -3262831481821328800L;

    private static final Logger logger = Logger.getLogger(Command.class.getName());
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
     * 命令
     */
    private String command;

    private Properties dataPropts;

    public Command() {
    }

    public Command(String site, Integer laneNumber, String device, Integer deviceNumber, String command) {
        this.site = site;
        this.laneNumber = laneNumber;
        this.device = device;
        this.deviceNumber = deviceNumber;
        this.command = command;
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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Command putData(String dataName, String dataValue) {
        if (dataName == null || "".equals(dataName)) {
            logger.info("site=" + this.site + ";laneNumber=" + this.laneNumber + ";Command=" + this.command);
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
                + deviceNumber + ", command=" + command + "]";
    }
}
