package com.nuctech.ls.model.bo.common;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[司机的相关信息 ]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_COMMON_DRIVER")
public class LsCommonDriverBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1960437051049008732L;

    /**
     * 缺省的构造函数
     */
    public LsCommonDriverBO() {
        super();
    }

    /** 司机主键 */
    private String driverId;

    /** 司机姓名 */
    private String driverName;

    /** 司机国家 */
    private String driverCountry;

    /** idCard（唯一） */
    private String driverIdCard;

    @Id
    @Column(name = "DRIVER_ID", nullable = false, length = 50)
    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    @Column(name = "DRIVER_NAME", nullable = true, length = 100)
    public String getDriverName() {
        return this.driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @Column(name = "DRIVER_COUNTRY", nullable = true, length = 100)
    public String getDriverCountry() {
        return this.driverCountry;
    }

    public void setDriverCountry(String driverCountry) {
        this.driverCountry = driverCountry;
    }

    @Column(name = "DRIVER_ID_CARD", nullable = true, length = 100)
    public String getDriverIdCard() {
        return driverIdCard;
    }

    public void setDriverIdCard(String driverIdCard) {
        this.driverIdCard = driverIdCard;
    }

}
