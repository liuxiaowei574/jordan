package com.nuctech.ls.model.bo.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 业务对象处理的实体-[系统参数表]
 *
 * @author： nuctech
 */
@Entity
@Table(name = "LS_SYSTEM_PARAMS")
public class LsSystemParamsBO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7693848843469093295L;

    /**
     * 缺省的构造函数
     */
    public LsSystemParamsBO() {
        super();
    }

    /* 参数主键 */
    private String paramId;

    /* 参数名称 */
    private String paramName;

    /* 参数代码 */
    private String paramCode;

    /* 参数值 */
    private String paramValue;

    /*
     * 参数状态 0-有效 1-无效
     */
    private String paramStatus;

    /* 参数类型 0-常规类型，1-告警类型 */
    private String paramType;

    /* 报警类型主键 */
    private String alarmTypeId;

    @Id
    @Column(name = "PARAM_ID", nullable = false, length = 50)
    public String getParamId() {
        return this.paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    @Column(name = "PARAM_NAME", nullable = true, length = 100)
    public String getParamName() {
        return this.paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    @Column(name = "PARAM_CODE", nullable = true, length = 50)
    public String getParamCode() {
        return this.paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    @Column(name = "PARAM_VALUE", nullable = true, length = 20)
    public String getParamValue() {
        return this.paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    @Column(name = "PARAM_STATUS", nullable = true, length = 2)
    public String getParamStatus() {
        return this.paramStatus;
    }

    public void setParamStatus(String paramStatus) {
        this.paramStatus = paramStatus;
    }

    @Column(name = "PARAM_TYPE", nullable = true, length = 50)
    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    @Column(name = "ALARM_TYPE_ID", nullable = true, length = 50)
    public String getAlarmTypeId() {
        return alarmTypeId;
    }

    public void setAlarmTypeId(String alarmTypeId) {
        this.alarmTypeId = alarmTypeId;
    }
}
