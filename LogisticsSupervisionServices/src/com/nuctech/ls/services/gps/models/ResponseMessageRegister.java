/**
 * 
 */
package com.nuctech.ls.services.gps.models;

/**
 * @author sunming
 *
 */
public class ResponseMessageRegister extends AbstractMessageContent {

    // 返回请求ID
    private String requestId;

    private String ret;

    private String code;

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

}
