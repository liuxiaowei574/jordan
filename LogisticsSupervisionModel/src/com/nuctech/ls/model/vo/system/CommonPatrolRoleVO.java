package com.nuctech.ls.model.vo.system;

import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.ls.model.bo.system.LsSystemRoleBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.system.LsSystemUserRoleBO;

public class CommonPatrolRoleVO {

    private LsMonitorVehicleGpsBO monitorVehicleGpsBO;

    public LsMonitorVehicleGpsBO getMonitorVehicleGpsBO() {
        return monitorVehicleGpsBO;
    }

    public void setMonitorVehicleGpsBO(LsMonitorVehicleGpsBO monitorVehicleGpsBO) {
        this.monitorVehicleGpsBO = monitorVehicleGpsBO;
    }

    private LsCommonPatrolBO commonPatrolBO;

    private LsSystemUserBO lsSystemUserBO;

    private LsSystemUserRoleBO lsSystemUserRoleBO;

    private LsSystemRoleBO lsSystemRoleBO;

    public LsSystemRoleBO getLsSystemRoleBO() {
        return lsSystemRoleBO;
    }

    public void setLsSystemRoleBO(LsSystemRoleBO lsSystemRoleBO) {
        this.lsSystemRoleBO = lsSystemRoleBO;
    }

    public LsCommonPatrolBO getCommonPatrolBO() {
        return commonPatrolBO;
    }

    public void setCommonPatrolBO(LsCommonPatrolBO commonPatrolBO) {
        this.commonPatrolBO = commonPatrolBO;
    }

    public LsSystemUserBO getLsSystemUserBO() {
        return lsSystemUserBO;
    }

    public void setLsSystemUserBO(LsSystemUserBO lsSystemUserBO) {
        this.lsSystemUserBO = lsSystemUserBO;
    }

    public LsSystemUserRoleBO getLsSystemUserRoleBO() {
        return lsSystemUserRoleBO;
    }

    public void setLsSystemUserRoleBO(LsSystemUserRoleBO lsSystemUserRoleBO) {
        this.lsSystemUserRoleBO = lsSystemUserRoleBO;
    }

}
