/**
 * 
 */
package com.nuctech.ls.services.gps.models;

/**
 * @author sunming
 *
 */
public class AbstractMessageContent {

 // 事件ID
    private String id;
    // 关锁号
    private String phone_num;
    
    private String seq_num;
    
    private String deviceType;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the phone_num
     */
    public String getPhone_num() {
//      if (null != phone_num && phone_num.length() == 12) {
//          return MessageConstants.PRE_ELOCK + phone_num.substring(2, phone_num.length());
//      }
        return phone_num;
    }

    /**
     * @param phone_num
     *            the phone_num to set
     */
    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getSeq_num() {
        return seq_num;
    }

    public void setSeq_num(String seq_num) {
        this.seq_num = seq_num;
    }

    /**
     * @return the deviceType
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType the deviceType to set
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    

}
