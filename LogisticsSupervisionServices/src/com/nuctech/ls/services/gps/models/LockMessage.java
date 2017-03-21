/**
 * 
 */
package com.nuctech.ls.services.gps.models;

/**
 * @author sunming
 *
 */
public class LockMessage {

    // 锁开关状态：1----关 2----开
    private String status;
    // 施解封状态：1----施封 0----解封
    private String sealStatus;
    // 电量值:124(毫伏，相当于1.24V)
    private String vvv;
    // 防拆开关状态: 0----正常 1----被拆触发
    private String antidismantle;
    /*
     * //上报原因
     * 0----定时
     * 1----锁开/关状态变更
     * 2----被拆状态变更
     * 3----按键施封
     * 4----RFID施封
     * 5----RFID解封
     * 6----SMS施封
     * 7----SMS解封
     * 8----锁因低电量休眠下线
     * 9----非注册卡施封
     * A----非注册卡解封
     * B----注册卡施封
     * C----注册卡解封
     * D----NFC施封
     * E----NFC解封
     * F----平台施封
     * G----平台解封
     * H----非注册上锁卡
     * I----非注册开锁卡
     * J----注册上锁卡
     * K----注册开锁卡
     * L----施封下有封条被解开(异常变化)
     * M----有封条被拆开
     * N----有封条通讯超时
     * O----封条正常状态变更(正常变化)
     */
    private String events;
    // 锁ID号
    private String lockId;
    // 操作ID,如：按键施封，RFID读写器施/解封，平台远程施解封
    private String operationID;
    /*
     * 0----无绑定封条
     * 1----已绑定封条
     * 2----封条为关
     * 3----封条为开
     * 4----封条被破坏
     * 5----封条通讯超时
     */
    private String[] seals;

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     *        the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the sealStatus
     */
    public String getSealStatus() {
        return sealStatus;
    }

    /**
     * @param sealStatus
     *        the sealStatus to set
     */
    public void setSealStatus(String sealStatus) {
        this.sealStatus = sealStatus;
    }

    /**
     * @return the vvv
     */
    public String getVvv() {
        return vvv;
    }

    /**
     * @param vvv
     *        the vvv to set
     */
    public void setVvv(String vvv) {
        this.vvv = vvv;
    }

    /**
     * @return the antidismantle
     */
    public String getAntidismantle() {
        return antidismantle;
    }

    /**
     * @param antidismantle
     *        the antidismantle to set
     */
    public void setAntidismantle(String antidismantle) {
        this.antidismantle = antidismantle;
    }

    /**
     * @return the event
     */
    public String getEvents() {
        return events;
    }

    /**
     * @param event
     *        the event to set
     */
    public void setEvents(String events) {
        this.events = events;
    }

    /**
     * @return the lockId
     */
    public String getLockId() {
        return lockId;
    }

    /**
     * @param lockId
     *        the lockId to set
     */
    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    /**
     * @return the operationID
     */
    public String getOperationID() {
        return operationID;
    }

    /**
     * @param operationID
     *        the operationID to set
     */
    public void setOperationID(String operationID) {
        this.operationID = operationID;
    }

    /**
     * @return the seals
     */
    public String[] getSeals() {
        return seals;
    }

    /**
     * @param seals
     *        the seals to set
     */
    public void setSeals(String[] seals) {
        this.seals = seals;
    }

}
