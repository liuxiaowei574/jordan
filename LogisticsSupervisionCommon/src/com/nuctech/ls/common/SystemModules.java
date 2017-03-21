package com.nuctech.ls.common;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

/**
 * 系统功能模块配置
 *
 */
@Component
public class SystemModules {

    /**
     * 功能开启标识
     */
    private final String MODULE_ON = "1";

    /**
     * 车载台和巡逻队模块
     */
    @Resource
    private String patrolModule;
    /**
     * 审批模块
     */
    @Resource
    private String approvalModule;
    /**
     * 风险分析模块
     */
    @Resource
    private String riskModule;
    /**
     * 调度模块
     */
    @Resource
    private String dispatchModule;
    /**
     * 待办任务模块
     */
    @Resource
    private String taskModule;
    /**
     * 报警推送模块
     */
    @Resource
    private String alarmPushModule;
    /**
     * 区域场地模块
     */
    @Resource
    private String areaModule;

    /**
     * 车载台和巡逻队模块是否开启
     * 
     * @return
     */
    public boolean isPatrolOn() {
        return MODULE_ON.equals(patrolModule);
    }

    /**
     * 审批模块是否开启
     * 
     * @return
     */
    public boolean isApprovalOn() {
        return MODULE_ON.equals(approvalModule);
    }

    /**
     * 风险分析模块是否开启
     * 
     * @return
     */
    public boolean isRiskOn() {
        return MODULE_ON.equals(riskModule);
    }

    /**
     * 调度模块是否开启
     * 
     * @return
     */
    public boolean isDispatchOn() {
        return MODULE_ON.equals(dispatchModule);
    }

    /**
     * 待办任务模块是否开启
     * 
     * @return
     */
    public boolean isTaskOn() {
        return MODULE_ON.equals(taskModule);
    }

    /**
     * 报警推送模块是否开启
     * 
     * @return
     */
    public boolean isAlarmPushOn() {
        return MODULE_ON.equals(alarmPushModule);
    }

    /**
     * 区域场地模块是否开启
     * 
     * @return
     */
    public boolean isAreaOn() {
        return MODULE_ON.equals(areaModule);
    }

    /**
     * 转为Json字符串
     * 
     * @return
     */
    public String toJSON() {
        JSONObject json = new JSONObject();
        json.put("isPatrolOn", this.isPatrolOn());
        json.put("isApprovalOn", this.isApprovalOn());
        json.put("isRiskOn", this.isRiskOn());
        json.put("isDispatchOn", this.isDispatchOn());
        json.put("isTaskOn", this.isTaskOn());
        json.put("isAlarmPushOn", this.isAlarmPushOn());
        json.put("isAreaOn", this.isAreaOn());
        return json.toString();
    }
}
