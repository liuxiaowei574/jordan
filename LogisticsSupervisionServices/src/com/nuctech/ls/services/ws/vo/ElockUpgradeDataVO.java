package com.nuctech.ls.services.ws.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 关锁升级VO
 * 
 * @author 姜永权
 *
 */
@XStreamAlias("ElockUpgradeDataVO")
public class ElockUpgradeDataVO implements Serializable {

    private static final long serialVersionUID = 3653726492896926240L;

    public ElockUpgradeDataVO() {
    }

    public ElockUpgradeDataVO(String elockNumber, String electricityValue, String belongToPortName,
            String isTripActivate) {
        this.elockNumber = elockNumber;
        this.electricityValue = electricityValue;
        this.belongToPortName = belongToPortName;
        this.isTripActivate = isTripActivate;
    }

    /* 关锁号 */
    private String elockNumber;

    /* 电量 */
    private String electricityValue;

    /* 所属口岸名称 */
    private String belongToPortName;
    /*
     * 是否在途
     */
    private String isTripActivate;

    public String getElockNumber() {
        return this.elockNumber;
    }

    public void setElockNumber(String elockNumber) {
        this.elockNumber = elockNumber;
    }

    public String getElectricityValue() {
        return electricityValue;
    }

    public void setElectricityValue(String electricityValue) {
        this.electricityValue = electricityValue;
    }

    public String getBelongToPortName() {
        return belongToPortName;
    }

    public void setBelongToPortName(String belongToPortName) {
        this.belongToPortName = belongToPortName;
    }

    public String getIsTripActivate() {
        return isTripActivate;
    }

    public void setIsTripActivate(String isTripActivate) {
        this.isTripActivate = isTripActivate;
    }

}
