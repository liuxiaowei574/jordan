package com.nuctech.ls.model.bo.sla;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[车辆罚款表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_VEHICLE_PUNISH")
public class LsVehiclePunishBo implements Serializable {

    /**
     * 
     */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 缺省的构造函数
     */
    public LsVehiclePunishBo() {
        super();
    }

    /* 车辆罚款主键 */
    private String vpunishId;
    /* 罚款类型 */
    private String vpunishType;
    /* 罚款金额 */
    private String vpunishValue;

    @Id
    @Column(name = "VPUNISH_ID", nullable = false, length = 50)
    public String getVpunishId() {
        return vpunishId;
    }

    public void setVpunishId(String vpunishId) {
        this.vpunishId = vpunishId;
    }

    @Column(name = "VPUNISH_TYPE", nullable = true, length = 100)
    public String getVpunishType() {
        return vpunishType;
    }

    public void setVpunishType(String vpunishType) {
        this.vpunishType = vpunishType;
    }

    @Column(name = "VPUNISH_VALUE", nullable = true, length = 100)
    public String getVpunishValue() {
        return vpunishValue;
    }

    public void setVpunishValue(String vpunishValue) {
        this.vpunishValue = vpunishValue;
    }

}
