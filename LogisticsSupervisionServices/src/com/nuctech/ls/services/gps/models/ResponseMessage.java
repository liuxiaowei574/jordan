/**
 * 
 */
package com.nuctech.ls.services.gps.models;

/**
 * @author sunming
 *
 */
public class ResponseMessage extends AbstractMessageContent {

    // 返回请求ID
    private String requestId;

    private String ret;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * @return the ret
     */
    public String getRet() {
        return ret;
    }

    /**
     * @param ret
     *        the ret to set
     */
    public void setRet(String ret) {
        this.ret = ret;
    }

}
