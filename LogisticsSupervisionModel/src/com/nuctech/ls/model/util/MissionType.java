/**
 * 
 */
package com.nuctech.ls.model.util;

/**
 * 任务类型
 * 
 * @author zsy
 *
 */
public enum MissionType {

    TripEscortMission("1"), // 行程护送任务
    AlarmDealMission("2"), // 报警处理任务
    DispatchMission("3"); // 设备调度任务

    private String type;

    private MissionType(String type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *        the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

}
