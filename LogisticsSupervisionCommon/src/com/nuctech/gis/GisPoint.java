package com.nuctech.gis;

/**
 * 坐标类
 * 
 * @author liqingxian
 *
 */
public class GisPoint {

    /**
     * 构造函数
     * 
     * @param lng
     * @param lat
     */
    public GisPoint(double lng, double lat) {
        super();
        this.lng = lng;
        this.lat = lat;
    }

    /**
     * 经度
     */
    private double lng;
    /**
     * 纬度
     */
    private double lat;

    public double getLng() {
        return this.lng;
    }

    public double getLat() {
        return this.lat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GisPoint) {
            GisPoint gisPoint = (GisPoint) obj;
            if (gisPoint.getLat() == this.lat && gisPoint.getLng() == this.lng) {
                return true;
            }
        }
        return false;
    }
}
