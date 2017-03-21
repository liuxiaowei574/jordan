package com.nuctech.ls.model.vo.analysis;

/**
 * LsDmAlarmTypeBO.alarmLevelId与孙明约定0为轻微报警，1为严重报警
 * 
 * @author huangxinfang
 *
 */
public class RiskAnalysisVo {

    /**
     * 主键 内容为：车牌号 或 司机
     */
    private String riskAnalysisId;
    /**
     * 行程数量
     */
    private long tripTotalNum;
    /**
     * 行程报警数
     */
    private long tripAlarmTotalNum;
    /**
     * 行程严重报警总数
     */
    private long tripSeriousAlarmTotalNum;
    /**
     * 行程轻微报警总数
     */
    private long tripMinorAlarmTotalNum;
    /**
     * 
     * 报警总数
     */
    private long alarmTotalNum;
    /**
     * 严重报警总数
     */
    private long seriousAlarmTotalNum;
    /**
     * 轻微报警总数
     */
    private long minorAlarmTotalNum;

    public RiskAnalysisVo() {
        super();
    }

    public RiskAnalysisVo(String riskAnalysisId, long tripTotalNum, long tripAlarmTotalNum,
            long tripSeriousAlarmTotalNum, long tripMinorAlarmTotalNum, long alarmTotalNum, long seriousAlarmTotalNum,
            long minorAlarmTotalNum) {
        super();
        this.riskAnalysisId = riskAnalysisId;
        this.tripTotalNum = tripTotalNum;
        this.tripAlarmTotalNum = tripAlarmTotalNum;
        this.tripSeriousAlarmTotalNum = tripSeriousAlarmTotalNum;
        this.tripMinorAlarmTotalNum = tripMinorAlarmTotalNum;
        this.alarmTotalNum = alarmTotalNum;
        this.seriousAlarmTotalNum = seriousAlarmTotalNum;
        this.minorAlarmTotalNum = minorAlarmTotalNum;
    }

    public String getRiskAnalysisId() {
        return riskAnalysisId;
    }

    public void setRiskAnalysisId(String riskAnalysisId) {
        this.riskAnalysisId = riskAnalysisId;
    }

    public long getTripTotalNum() {
        return tripTotalNum;
    }

    public void setTripTotalNum(long tripTotalNum) {
        this.tripTotalNum = tripTotalNum;
    }

    public long getTripAlarmTotalNum() {
        return tripAlarmTotalNum;
    }

    public void setTripAlarmTotalNum(long tripAlarmTotalNum) {
        this.tripAlarmTotalNum = tripAlarmTotalNum;
    }

    public long getTripSeriousAlarmTotalNum() {
        return tripSeriousAlarmTotalNum;
    }

    public void setTripSeriousAlarmTotalNum(long tripSeriousAlarmTotalNum) {
        this.tripSeriousAlarmTotalNum = tripSeriousAlarmTotalNum;
    }

    public long getTripMinorAlarmTotalNum() {
        return tripMinorAlarmTotalNum;
    }

    public void setTripMinorAlarmTotalNum(long tripMinorAlarmTotalNum) {
        this.tripMinorAlarmTotalNum = tripMinorAlarmTotalNum;
    }

    public long getAlarmTotalNum() {
        return alarmTotalNum;
    }

    public void setAlarmTotalNum(long alarmTotalNum) {
        this.alarmTotalNum = alarmTotalNum;
    }

    public long getSeriousAlarmTotalNum() {
        return seriousAlarmTotalNum;
    }

    public void setSeriousAlarmTotalNum(long seriousAlarmTotalNum) {
        this.seriousAlarmTotalNum = seriousAlarmTotalNum;
    }

    public long getMinorAlarmTotalNum() {
        return minorAlarmTotalNum;
    }

    public void setMinorAlarmTotalNum(long minorAlarmTotalNum) {
        this.minorAlarmTotalNum = minorAlarmTotalNum;
    }

}
