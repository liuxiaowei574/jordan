/**
 * 
 */
package com.nuctech.ls.services.gps.models;

/**
 * 数据下发消息返回消息
 * 
 * @author sunming
 *
 */
public class ConsoleMessage {

    // 命令
    private String cmd;
    // 关键字
    private String entry;
    // 值
    private Object value;

    /**
     * @return the cmd
     */
    public String getCmd() {
        return cmd;
    }

    /**
     * @param cmd
     *        the cmd to set
     */
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    /**
     * @return the entry
     */
    public String getEntry() {
        return entry;
    }

    /**
     * @param entry
     *        the entry to set
     */
    public void setEntry(String entry) {
        this.entry = entry;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value
     *        the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

}
