package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Priority;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.util.DateUtils;
import com.nuctech.util.NuctechUtil;

@Repository
public class VehicleTrackingDao extends LSBaseDao<LsMonitorVehicleGpsBO, Serializable> {

    @Resource
    private CommonVehicleDao commonVehicleDao;
    @Resource
    private MonitorVehicleGpsDao monitorVehicleGpsDao;
    @Resource
    private MonitorVehicleStatusDao monitorVehicleStatusDao;

    /*
     * @SuppressWarnings({ "rawtypes" })
     * public List<LsMonitorVehicleGpsBO> findMonitorVehicleGpsByTripId(LsMonitorTripBO
     * lsMonitorTripBO){
     *//**
       * 判断数据库表是否存在
       *//*
         * try {
         * String sql = "select * from GPS.LS_MONITOR_VEHICLE_GPS_" +
         * lsMonitorTripBO.getTrackingDeviceNumber()
         * + " where TRIP_ID = '" + lsMonitorTripBO.getTripId() + "' ";
         * if(NuctechUtil.isNotNull(lsMonitorTripBO.getCheckinTime())){
         * String lowerDateTime = DateUtils.date2String(lsMonitorTripBO.getCheckinTime(),
         * DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
         * sql = sql + " and LOCATION_TIME>'" + lowerDateTime + "' ";
         * }
         * if(NuctechUtil.isNotNull(lsMonitorTripBO.getCheckoutTime())){
         * String upperDateTime = DateUtils.date2String(lsMonitorTripBO.getCheckoutTime(),
         * DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
         * sql = sql + " and LOCATION_TIME<'" + upperDateTime + "' ";
         * }else{
         * String upperDateTime = DateUtils.date2String(new Date(),
         * DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
         * sql = sql + " and LOCATION_TIME<'" + upperDateTime + "' ";
         * }
         * sql = sql + " and LONGITUDE is not null and LATITUDE is not null order by GPS_SEQ asc";
         * // 查询结果
         * List<LsMonitorVehicleGpsBO> result =
         * this.getSession().createSQLQuery(sql).addEntity(LsMonitorVehicleGpsBO.class).list();
         * return result;
         * } catch (Exception e) {
         * logger.warning("路线轨迹表LS_MONITOR_VEHICLE_GPS_" +
         * lsMonitorTripBO.getTrackingDeviceNumber()+"不存在！");
         * return null;
         * }
         * }
         */
    @SuppressWarnings("unchecked")
    public List<LsMonitorVehicleGpsBO> findPatrolGpsByTripId(String trackUnitNumber, String startTime, String endTime) {
        // String tripId = lsMonitorTripBO.getTripId();
        // Date checkinTime = lsMonitorTripBO.getCheckinTime();
        // Date checkoutTime = lsMonitorTripBO.getCheckoutTime();
        Date checkinTime = DateUtils.stringToDate(startTime);
        Date checkoutTime = DateUtils.stringToDate(endTime);
        try {
            StringBuffer sql = new StringBuffer();
            // sql.append("select * from GPS.LS_MONITOR_PORTAL_GPS_" + trackUnitNumber + " where
            // TRIP_ID = '" + tripId + "' ");
            sql.append("select * from GPS.LS_MONITOR_PORTAL_GPS_" + trackUnitNumber + " where  ");
            if (NuctechUtil.isNotNull(checkinTime)) {
                String lowerDateTime = DateUtils.date2String(checkinTime,
                        DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
                sql.append("  LOCATION_TIME>'" + lowerDateTime + "' ");
            }

            if (NuctechUtil.isNotNull(checkoutTime)) {
                String upperDateTime = DateUtils.date2String(checkoutTime,
                        DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
                sql.append(" and LOCATION_TIME<'" + upperDateTime + "' ");
            } else {
                String upperDateTime = DateUtils.date2String(new Date(),
                        DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
                sql.append(" and LOCATION_TIME<'" + upperDateTime + "' ");
            }
            sql.append(" and LONGITUDE is not null and LATITUDE is not null order by LOCATION_TIME asc");
            // 查询结果
            List<LsMonitorVehicleGpsBO> result = this.getSession().createSQLQuery(sql.toString())
                    .addEntity(LsMonitorVehicleGpsBO.class).list();
            return result;
        } catch (Exception e1) {
            logger.log(Priority.FATAL, e1.getMessage());;
        }
        return null;
    }

    @SuppressWarnings({ "unchecked" })
    public Map<String, List<LsMonitorVehicleGpsBO>> findMonitorVehicleGpsByTripId(LsMonitorTripBO lsMonitorTripBO) {
        /**
         * 判断数据库表是否存在
         */
        Map<String, List<LsMonitorVehicleGpsBO>> map = new HashMap<String, List<LsMonitorVehicleGpsBO>>();
        String tripId = lsMonitorTripBO.getTripId();
        Date checkinTime = lsMonitorTripBO.getCheckinTime();
        Date checkoutTime = lsMonitorTripBO.getCheckoutTime();
        List<LsCommonVehicleBO> commonVehicleBOs = new ArrayList<>();
        try {
            commonVehicleBOs = commonVehicleDao.findAllByTripId(tripId);
            if (commonVehicleBOs != null && commonVehicleBOs.size() > 0) {
                // 每辆车一把关锁。
                for (LsCommonVehicleBO commonVehicleBO : commonVehicleBOs) {
                    String trackingDeviceNumber = "";
                    try {
                        trackingDeviceNumber = commonVehicleBO.getTrackingDeviceNumber();
                        String tableName = "LS_MONITOR_VEHICLE_GPS_" + trackingDeviceNumber;
                        boolean exists = monitorVehicleGpsDao.findTableNameExsitorNot(tableName);
                        if (!exists) {
                            logger.warn("路线轨迹表LS_MONITOR_VEHICLE_GPS_" + trackingDeviceNumber
                                    + "不存在！将从LS_MONITOR_VEHICLE_STATUS表获取GPS原设备号");
                            LsMonitorVehicleStatusBO vehicleStatusBo = monitorVehicleStatusDao
                                    .findLatestCommonVehicleStatusBo(tripId, trackingDeviceNumber);
                            if (vehicleStatusBo != null) {
                                trackingDeviceNumber = vehicleStatusBo.getGpsTrackingDeviceNumber();
                                tableName = "LS_MONITOR_VEHICLE_GPS_" + trackingDeviceNumber;
                                exists = monitorVehicleGpsDao.findTableNameExsitorNot(tableName);
                            }
                        }
                        if (!exists) {
                            logger.warn("路线轨迹表LS_MONITOR_VEHICLE_GPS_" + trackingDeviceNumber + "不存在！将忽略此条数据");
                            continue;
                        }
                        StringBuffer sql = new StringBuffer();
                        sql.append("select * from GPS." + tableName + " where TRIP_ID = '" + tripId + "' ");

                        if (NuctechUtil.isNotNull(checkinTime)) {
                            String lowerDateTime = DateUtils.date2String(checkinTime,
                                    DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
                            sql.append(" and LOCATION_TIME>'" + lowerDateTime + "' ");
                        }

                        if (NuctechUtil.isNotNull(checkoutTime)) {
                            String upperDateTime = DateUtils.date2String(checkoutTime,
                                    DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
                            sql.append(" and LOCATION_TIME<'" + upperDateTime + "' ");
                        } else {
                            String upperDateTime = DateUtils.date2String(new Date(),
                                    DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
                            sql.append(" and LOCATION_TIME<'" + upperDateTime + "' ");
                        }
                        sql.append(" and LONGITUDE is not null and LATITUDE is not null order by LOCATION_TIME asc");
                        // 查询结果
                        List<LsMonitorVehicleGpsBO> result = this.getSession().createSQLQuery(sql.toString())
                                .addEntity(LsMonitorVehicleGpsBO.class).list();
                        map.put(trackingDeviceNumber, result);
                    } catch (Exception e) {
                        logger.warn("路线轨迹表LS_MONITOR_VEHICLE_GPS_" + trackingDeviceNumber + "不存在！");
                        return null;
                    }
                }
            }
        } catch (Exception e1) {
            logger.log(Priority.FATAL, e1.getMessage());
        }
        return map;
    }
}
