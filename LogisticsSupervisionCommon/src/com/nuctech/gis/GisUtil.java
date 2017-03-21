package com.nuctech.gis;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.distance.DistanceOp;

/**
 * gis工具类
 * 
 * @author liqingxian
 *
 */
public class GisUtil {

    private static GeometryFactory geometryFactory = new GeometryFactory();
    /**
     * 地球半径
     */
    private static final double EARTHRADIUS = 6370996.81;

    /**
     * 判断点是否多边形内(待改进)
     * 
     * @param gisPoint
     * @param points
     * @return
     * @throws ParseException
     */
    public static boolean isPointwithinGeo(GisPoint gisPoint, List<GisPoint> points) throws ParseException {
        String wktPoly = "POLYGON ((";
        for (int i = 0; i < points.size(); i++) {
            wktPoly = wktPoly + points.get(i).getLng() + " " + points.get(i).getLat() + ", ";
            if (i == points.size() - 1) {
                wktPoly = wktPoly + points.get(i).getLng() + " " + points.get(i).getLat() + "))";
            }
        }
        Coordinate coord = new Coordinate(gisPoint.getLng(), gisPoint.getLat());
        Point point = geometryFactory.createPoint(coord);
        WKTReader reader = new WKTReader(geometryFactory);
        Polygon polygon = (Polygon) reader.read(wktPoly);
        boolean flag = point.within(polygon);
        return flag;
    }

    /**
     * 判断点是否多边形内（较准确）
     * 
     * @param point
     * @param polygon
     * @return
     */
    public static boolean isPointInPolygon(GisPoint point, List<GisPoint> points) {
        int N = points.size();
        boolean boundOrVertex = true;
        int intersectCount = 0;
        double precision = 2e-10;
        GisPoint p1;//
        GisPoint p2;//
        GisPoint p = point; // 传入点

        p1 = points.get(0);
        for (int i = 1; i <= N; ++i) {
            if (p.equals(p1)) {
                return boundOrVertex;
            }

            p2 = points.get(i % N);
            if (p.getLat() < Math.min(p1.getLat(), p2.getLat()) || p.getLat() > Math.max(p1.getLat(), p2.getLat())) {// ray
                                                                                                                     // is
                                                                                                                     // outside
                                                                                                                     // of
                                                                                                                     // our
                                                                                                                     // interests
                p1 = p2;
                continue;
            }

            if (p.getLat() > Math.min(p1.getLat(), p2.getLat()) && p.getLat() < Math.max(p1.getLat(), p2.getLat())) {
                if (p.getLng() <= Math.max(p1.getLng(), p2.getLng())) {// x is before of ray
                    if (p1.getLat() == p2.getLat() && p.getLng() >= Math.min(p1.getLng(), p2.getLng())) {
                        return boundOrVertex;
                    }

                    if (p1.getLng() == p2.getLng()) {
                        if (p1.getLng() == p.getLng()) {
                            return boundOrVertex;
                        } else {
                            ++intersectCount;
                        }
                    } else {
                        double xinters = (p.getLat() - p1.getLat()) * (p2.getLng() - p1.getLng())
                                / (p2.getLat() - p1.getLat()) + p1.getLng();
                        if (Math.abs(p.getLng() - xinters) < precision) {
                            return boundOrVertex;
                        }

                        if (p.getLng() < xinters) {
                            ++intersectCount;
                        }
                    }
                }
            } else {
                if (p.getLat() == p2.getLat() && p.getLng() <= p2.getLng()) {
                    GisPoint p3 = points.get((i + 1) % N);
                    if (p.getLat() >= Math.min(p1.getLat(), p3.getLat())
                            && p.getLat() <= Math.max(p1.getLat(), p3.getLat())) {
                        ++intersectCount;
                    } else {
                        intersectCount += 2;
                    }
                }
            }
            p1 = p2;
        }
        if (intersectCount % 2 == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断点是否在折线的缓冲区内
     * 
     * @param point
     * @param polyline
     * @param buffer
     * @returns {Boolean} 点在折线上返回true,否则返回false
     */
    public static boolean isPointOnPolyline(GisPoint point, List<GisPoint> polyline, double buffer) {
        // 判断点到折线的最短距离，若小于buffer返回true
        int size = polyline.size();

        Coordinate[] coordinates = new Coordinate[size];
        for (int i = 0; i < polyline.size(); i++) {
            Coordinate coordinate = new Coordinate(polyline.get(i).getLng(), polyline.get(i).getLat());
            coordinates[i] = coordinate;
        }

        Geometry polylineGeo = new GeometryFactory().createLineString(coordinates);
        Geometry pointGeo = new GeometryFactory().createPoint(new Coordinate(point.getLng(), point.getLat()));
        DistanceOp distOp = new DistanceOp(pointGeo, polylineGeo);
        double distance = distOp.distance();
        if (distance == 0) {
            return true;
        }
        Coordinate[] closestPt = distOp.closestPoints();
        double realDistance = GisUtil.getDistance(closestPt[0].x, closestPt[0].y, closestPt[1].x, closestPt[1].y);
        if (realDistance <= buffer) {
            return true;
        }
        return false;
    }

    /**
     * 判断点是否在折线上
     * 
     * @param {Point}
     *        point 点对象
     * @param {Polyline}
     *        polyline 折线对象
     * @returns {Boolean} 点在折线上返回true,否则返回false
     */
    public static boolean isPointOnPolyline(GisPoint point, List<GisPoint> polyline) {

        // 判断点是否在线段上，设点为Q，线段为P1P2 ，
        // 判断点Q在该线段上的依据是：( Q - P1 ) × ( P2 - P1 ) = 0，且 Q 在以 P1，P2为对角顶点的矩形内
        for (int i = 0; i < polyline.size() - 1; i++) {
            GisPoint curPt = polyline.get(i);
            GisPoint nextPt = polyline.get(i + 1);
            // 首先判断point是否在curPt和nextPt之间，即：此判断该点是否在该线段的外包矩形内
            if (point.getLng() >= Math.min(curPt.getLng(), nextPt.getLng())
                    && point.getLng() <= Math.max(curPt.getLng(), nextPt.getLng())
                    && point.getLat() >= Math.min(curPt.getLat(), nextPt.getLat())
                    && point.getLat() <= Math.max(curPt.getLat(), nextPt.getLat())) {
                // 判断点是否在直线上公式
                double precision = (curPt.getLng() - point.getLng()) * (nextPt.getLat() - point.getLat())
                        - (nextPt.getLng() - point.getLng()) * (curPt.getLat() - point.getLat());
                if (precision < 2e-10 && precision > -2e-10) {// 实质判断是否接近0
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断点是否在圆形内
     * 
     * @param {Point}
     *        point 点对象
     * @param {center}
     *        point 圆心
     * @param {radius}
     *        double 半径，单位是米
     * @returns {Boolean} 点在圆形内返回true,否则返回false
     */
    public static boolean isPointInCircle(GisPoint point, GisPoint center, double radius) {
        double dis = getDistance(point, center);
        if (dis <= radius) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 计算两点之间的距离,两点坐标必须为经纬度
     * 
     * @param lng1
     *        点1经度
     * @param lat1
     *        点1纬度
     * @param lng2
     *        点2经度
     * @param lat2
     *        点2纬度
     * @return {Number} 两点之间距离，单位为米
     */
    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        return getDistance(new GisPoint(lng1, lat1), new GisPoint(lng2, lat2));
    }

    /**
     * 计算两点之间的距离,两点坐标必须为经纬度
     * 
     * @param {point1}
     *        Point 点对象
     * @param {point2}
     *        Point 点对象
     * @returns {Number} 两点之间距离，单位为米
     */
    public static double getDistance(GisPoint point1, GisPoint point2) {

        GisPoint p1 = new GisPoint(getLoop(point1.getLng(), -180d, 180d), getRange(point1.getLat(), -74d, 74d));
        GisPoint p2 = new GisPoint(getLoop(point2.getLng(), -180d, 180d), getRange(point2.getLat(), -74d, 74d));

        double x1 = degreeToRad(p1.getLng());
        double y1 = degreeToRad(p1.getLat());
        double x2 = degreeToRad(p2.getLng());
        double y2 = degreeToRad(p2.getLat());

        return EARTHRADIUS * Math.acos((Math.sin(y1) * Math.sin(y2) + Math.cos(y1) * Math.cos(y2) * Math.cos(x2 - x1)));
    }

    /**
     * 点到线段的最短距离
     * 
     * @param point
     *        点
     * @param point1
     *        线段的端点
     * @param point2
     *        线段的端点
     * @return
     */
    public static double pointToSegDist(GisPoint point, GisPoint point1, GisPoint point2) {
        return pointToSegDist(point.getLng(), point.getLat(), point1.getLng(), point1.getLat(), point2.getLng(),
                point2.getLng());
    }

    /**
     * 点到线段的最短距离
     * 
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private static double pointToSegDist(double x, double y, double x1, double y1, double x2, double y2) {
        double distance = 0;
        double cross = (x2 - x1) * (x - x1) + (y2 - y1) * (y - y1);
        if (cross <= 0) {// 返回x到x1的距离
            // return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
            distance = getDistance(x, y, x1, y1);
        }

        double d2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);

        if (cross >= d2) {
            // return Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
            distance = getDistance(x, y, x2, y2);
        }

        double r = cross / d2;
        double px = x1 + (x2 - x1) * r;
        double py = y1 + (y2 - y1) * r;
        // return Math.sqrt((x - px) * (x - px) + (py - y1) * (py - y1));
        distance = getDistance(x, py, px, y1);
        return distance;
    }

    /**
     * 将度转化为弧度
     * 
     * @param {degree}
     *        Number 度
     * @returns {Number} 弧度
     */
    private static double degreeToRad(double degree) {
        return Math.PI * degree / 180;
    }

    /**
     * 将弧度转化为度
     * 
     * @param {radian}
     *        Number 弧度
     * @returns {Number} 度
     */
    @SuppressWarnings("unused")
    private static double radToDegree(double rad) {
        return (180 * rad) / Math.PI;
    }

    /**
     * 将v值限定在a,b之间，纬度使用
     */
    private static double getRange(Double v, Double a, Double b) {
        if (a != null) {
            v = Math.max(v, a);
        }
        if (b != null) {
            v = Math.min(v, b);
        }
        return v;
    }

    /**
     * 将v值限定在a,b之间，经度使用
     */
    private static double getLoop(Double v, Double a, Double b) {
        while (v > b) {
            v -= b - a;
        }
        while (v < a) {
            v += b - a;
        }
        return v;
    }

}
