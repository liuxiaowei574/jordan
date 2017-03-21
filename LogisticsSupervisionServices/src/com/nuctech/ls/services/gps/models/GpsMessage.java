
package com.nuctech.ls.services.gps.models;

/**
 * GPS坐标属性
 * 
 * @author sunming
 *
 */
public class GpsMessage {

    /*
     * //此字段代表状态位
     * BYTE 0 0:ACC关； 1:ACC开
     * BYTE 1 0:未定位 1:定位
     * BYTE 2 0:北纬 1:南纬
     * BYTE 3 0:东经 1:西经
     * BYTE 4 0:运营状态 1:停运状态
     */
    private String state;
    // 维度 以度数为单位的经度乘以10的6次方,精确到百万分子一度
    private String latitude;
    // 经度以度数为单位的经度乘以10的6次方,精确到百万分子一度
    private String longitude;
    // 海拔高度,单位为米(m)
    private String altitude;
    // 速度 km/h
    private String speed;
    // 方位角，正北为0度，顺时针方向
    private String direction;
    // 关锁信息收到时间
    private String timestamp;

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     *        the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     *        the latitude to set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     *        the longitude to set
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the altitude
     */
    public String getAltitude() {
        return altitude;
    }

    /**
     * @param altitude
     *        the altitude to set
     */
    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    /**
     * @return the speed
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * @param speed
     *        the speed to set
     */
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    /**
     * @return the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction
     *        the direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *        the timestamp to set
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
