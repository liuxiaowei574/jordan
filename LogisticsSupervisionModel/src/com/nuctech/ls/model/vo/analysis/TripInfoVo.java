package com.nuctech.ls.model.vo.analysis;

import org.springframework.beans.BeanUtils;

import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;

public class TripInfoVo extends LsMonitorTripBO {

    /* 车牌号 */
    private String vehiclePlateNumber;

    /* 司机姓名 */
    private String driverName;

    public TripInfoVo() {
        super();
    }

    public TripInfoVo(LsMonitorTripBO ls, String vehiclePlateNumber, String driverName) {
        super();
        BeanUtils.copyProperties(ls, this);
        this.driverName = driverName;
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

}
