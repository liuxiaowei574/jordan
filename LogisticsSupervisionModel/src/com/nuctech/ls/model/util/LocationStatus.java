/**
 * 
 */
package com.nuctech.ls.model.util;

/**
 * @author sunming
 *         定位状态
 *         “state”:”01110”, //此字段代表状态位
 *         BYTE 0 0:ACC关； 1:ACC开
 *         BYTE 1 0:未定位 1:定位
 *         BYTE 2 0:北纬 1:南纬
 *         BYTE 3 0:东经 1:西经
 *         BYTE 4 0:运营状态 1:停运状态
 */
public enum LocationStatus {

    ACC_OFF(0, "0"),
    ACC_ON(0, "1"),
    LOCATION_OFF(1, "0"),
    LOCATION_ON(1, "1"),
    NORTH_LATITUDE(2, "0"),
    SOUTH_LATITUDE(2, "1"),
    EAST_LONGITUDE(3, "0"),
    WEST_LONGITUDE(3, "1"),
    ON_WORK(4, "0"),
    OFF_WORK(4, "1");

    private int index;
    private String value;

    /**
     * @param index
     * @param value
     */
    private LocationStatus(int index, String value) {
        this.index = index;
        this.value = value;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index
     *        the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *        the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
