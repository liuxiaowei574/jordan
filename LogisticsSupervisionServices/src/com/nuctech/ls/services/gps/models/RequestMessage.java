/**
 * 
 */
package com.nuctech.ls.services.gps.models;

/**
 * @author sunming
 *
 */
public class RequestMessage extends AbstractMessageContent {

    // 鉴权码
    private String code;
    // 掉线状态
    private String reason;
    // 掉线时间
    private String timestamp;
    // GPS信息
    private GpsMessage gps;
    // 关锁信息
    private LockMessage lock;

    private ConsoleMessage console;

    private RegisterMessage register;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *        the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason
     *        the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the gps
     */
    public GpsMessage getGps() {
        return gps;
    }

    /**
     * @param gps
     *        the gps to set
     */
    public void setGps(GpsMessage gps) {
        this.gps = gps;
    }

    /**
     * @return the lock
     */
    public LockMessage getLock() {
        return lock;
    }

    /**
     * @param lock
     *        the lock to set
     */
    public void setLock(LockMessage lock) {
        this.lock = lock;
    }

    /**
     * @return the console
     */
    public ConsoleMessage getConsole() {
        return console;
    }

    /**
     * @param console
     *        the console to set
     */
    public void setConsole(ConsoleMessage console) {
        this.console = console;
    }

    /**
     * @return the register
     */
    public RegisterMessage getRegister() {
        return register;
    }

    /**
     * @param register
     *        the register to set
     */
    public void setRegister(RegisterMessage register) {
        this.register = register;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
