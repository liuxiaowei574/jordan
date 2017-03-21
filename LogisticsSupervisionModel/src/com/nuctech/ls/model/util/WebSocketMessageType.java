/**
 * 
 */
package com.nuctech.ls.model.util;

/**
 * @author sunming
 *
 */
public enum WebSocketMessageType {

    VEHICLE_GPS("VEHICLE_GPS"), VEHICLE_ALARM("VEHICLE_ALARM"), PORTAL_GPS("PORTAL_GPS");

    private String type;

    private WebSocketMessageType(String type) {
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
