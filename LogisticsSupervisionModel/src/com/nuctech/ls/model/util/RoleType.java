package com.nuctech.ls.model.util;

import com.nuctech.util.MessageResourceUtil;

public enum RoleType {
    /**
     * // admin ADMIN("1"), // alarmCenter ADMINCENTER("2"), // portWorker
     * PORTWORKER("3"), // patrol PATROL("4"), // qualityCenter
     * QUALITYCENTER("5");
     */

    /**
     * 系统管理员
     */
    admin("1"),
    /**
     * 质量中心工作人员
     */
    qualityCenterUser("2"),
    /**
     * 控制中心监管人员
     */
    followupUser("3"),
    /**
     * 控制中心普通工作人员
     */
    contromRoomUser("4"),
    /**
     * 控制中心主管
     */
    contromRoomManager("5"),
    /**
     * 控制中心风险分析人员
     */
    riskAnalysisUser("6"),
    /**
     * 控制中心巡逻队主管
     */
    patrolManager("7"),
    /**
     * 口岸工作人员
     */
    portUser("8"),
    /**
     * 护送巡逻队
     */
    escortPatrol("9"),
    /**
     * 执法巡逻队
     */
    enforcementPatrol("10");

    private String type;

    private RoleType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 从资源文件读取国际化值
     * 
     * @return
     */
    public String getKey() {
        return MessageResourceUtil.getMessageInfo("system.role." + this.name());
    }
}
