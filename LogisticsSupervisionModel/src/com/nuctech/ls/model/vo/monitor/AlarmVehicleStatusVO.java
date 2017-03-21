package com.nuctech.ls.model.vo.monitor;

import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;

public class AlarmVehicleStatusVO {

    private LsMonitorAlarmBO lsMonitorAlarmBO;

    private LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO;

    public LsMonitorAlarmBO getLsMonitorAlarmBO() {
        return lsMonitorAlarmBO;
    }

    public void setLsMonitorAlarmBO(LsMonitorAlarmBO lsMonitorAlarmBO) {
        this.lsMonitorAlarmBO = lsMonitorAlarmBO;
    }

    public LsMonitorVehicleStatusBO getLsMonitorVehicleStatusBO() {
        return lsMonitorVehicleStatusBO;
    }

    public void setLsMonitorVehicleStatusBO(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {
        this.lsMonitorVehicleStatusBO = lsMonitorVehicleStatusBO;
    }

}
