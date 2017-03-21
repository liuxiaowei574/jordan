package com.nuctech.ls.model.vo.monitor;

import java.util.List;

import com.nuctech.ls.model.bo.monitor.LsMonitorRaPointBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;

public class MonitorRouteAreaRaPointVO {

    private LsMonitorRouteAreaBO lsMonitorRouteAreaBO;

    private List<LsMonitorRaPointBO> lsMonitorRaPointBOs;

    public LsMonitorRouteAreaBO getLsMonitorRouteAreaBO() {
        return lsMonitorRouteAreaBO;
    }

    public void setLsMonitorRouteAreaBO(LsMonitorRouteAreaBO lsMonitorRouteAreaBO) {
        this.lsMonitorRouteAreaBO = lsMonitorRouteAreaBO;
    }

    public List<LsMonitorRaPointBO> getLsMonitorRaPointBOs() {
        return lsMonitorRaPointBOs;
    }

    public void setLsMonitorRaPointBOs(List<LsMonitorRaPointBO> lsMonitorRaPointBOs) {
        this.lsMonitorRaPointBOs = lsMonitorRaPointBOs;
    }

}
