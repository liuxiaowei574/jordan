/**
 * 
 */
package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.util.DateUtils;
import com.nuctech.util.NuctechUtil;

/**
 * @author sunming
 *
 */
@Repository
public class MonitorVehicleGpsDao extends LSBaseDao<LsMonitorVehicleGpsBO, Serializable> {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * 查询表在数据库中是否存在
     * 
     * @param tableName
     * @return
     */
    public boolean findTableNameExsitorNot(String tableName) {

        String sql = "select name from sysobjects where xtype='U' and name=?";

        try {
            SQLQuery sqlQuery = getSession().createSQLQuery(sql);
            sqlQuery.setString(0, tableName);
            @SuppressWarnings("unchecked")
            List<String> result = sqlQuery.list();

            if (result != null && result.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    /**
     * 创建设备终端相关的数据库表
     * 
     * @param tableName
     *        表名称
     * 
     *        create table LS_MONITOR_VEHICLE_GPS ( GPS_ID varchar(50) not
     *        null, TRIP_ID varchar(50) null, LOCATION_TYPE varchar(2) null,
     *        GPS_SEQ varchar(50) null, TRACKING_DEVICE_NUMBER varchar(50)
     *        null, LOCATION_TIME datetime null, LOCATION_STATUS varchar(20)
     *        null, ELOCK_STATUS varchar(2) null, POLE_STATUS varchar(2)
     *        null, BROKEN_STATUS varchar(2) null, EVENT_UPLOAD varchar(2)
     *        null, LONGITUDE varchar(20) null, LATITUDE varchar(20) null,
     *        ALTITUDE varchar(20) null, ELOCK_SPEED varchar(20) null,
     *        DIRECTION varchar(20) null, ELECTRICITY_VALUE varchar(20) null
     *        ) go
     * 
     *        alter table LS_MONITOR_VEHICLE_GPS add constraint
     *        PK_LS_MONITOR_VEHICLE_GPS primary key nonclustered (GPS_ID) go
     * 
     * @return
     */
    public boolean createVehicleGpsTable(String tableName) {

        String sql = "CREATE TABLE [GPS]." + tableName + " (" + "GPS_ID               varchar(50)          not null,"
                + "TRIP_ID              varchar(50)          null," + "LOCATION_TYPE        varchar(2)           null,"
                + "GPS_SEQ              int                  null,"
                + "TRACKING_DEVICE_NUMBER varchar(50)          null,"
                + "LOCATION_TIME        datetime             null," + "LOCATION_STATUS      varchar(20)          null,"
                + "ELOCK_STATUS         varchar(2)           null," + "POLE_STATUS          varchar(2)           null,"
                + "BROKEN_STATUS        varchar(2)           null," + "EVENT_UPLOAD         varchar(2)           null,"
                + "LONGITUDE            varchar(20)          null," + "LATITUDE             varchar(20)          null,"
                + "ALTITUDE             varchar(20)          null," + "ELOCK_SPEED          varchar(20)          null,"
                + "DIRECTION            varchar(20)          null," + "ELECTRICITY_VALUE    varchar(20)          null,"
                + "RELATED_DEVICE       varchar(200)          null," + "CREATE_TIME          datetime             null "
                + "); " + "ALTER TABLE [GPS]." + tableName + "   ADD CONSTRAINT PK_" + tableName
                + " PRIMARY KEY NONCLUSTERED (GPS_ID)";

        try {
            logger.info("执行创建语句：" + sql);
            SQLQuery sqlQuery = getSession().createSQLQuery(sql);
            sqlQuery.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 插入GPS数据
     * 
     * @param tableName
     * @param monitorVehicleGpsBO
     * @return
     */
    public boolean insertVehicleGpsData(String tableName, LsMonitorVehicleGpsBO monitorVehicleGpsBO) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuffer sql = new StringBuffer("INSERT INTO ");
        sql.append("[GPS]." + tableName);
        sql.append("([GPS_ID], [TRIP_ID], [LOCATION_TYPE], [GPS_SEQ], [TRACKING_DEVICE_NUMBER], [LOCATION_TIME], ");
        sql.append("[LOCATION_STATUS], [ELOCK_STATUS], [POLE_STATUS], [BROKEN_STATUS], [EVENT_UPLOAD], ");
        sql.append("[LONGITUDE], [LATITUDE], [ALTITUDE], [ELOCK_SPEED], "
                + "[DIRECTION], [ELECTRICITY_VALUE],[RELATED_DEVICE],[CREATE_TIME])");
        sql.append("  VALUES (");
        sql.append("'" + monitorVehicleGpsBO.getGpsId() + "',");
        sql.append("'" + monitorVehicleGpsBO.getTripId() + "',");
        sql.append(monitorVehicleGpsBO.getLocationType() + ",");
        sql.append(monitorVehicleGpsBO.getGpsSeq() + ",");
        sql.append("'" + monitorVehicleGpsBO.getTrackingDeviceNumber() + "',");
        sql.append("'" + format.format(monitorVehicleGpsBO.getLocationTime()) + "',");
        sql.append("'" + monitorVehicleGpsBO.getLocationStatus() + "',");
        sql.append(monitorVehicleGpsBO.getElockStatus() + ",");
        sql.append(monitorVehicleGpsBO.getPoleStatus() + ",");
        sql.append(monitorVehicleGpsBO.getBrokenStatus() + ",");
        sql.append(monitorVehicleGpsBO.getEventUpload() + ",");
        sql.append(monitorVehicleGpsBO.getLongitude() + ",");
        sql.append(monitorVehicleGpsBO.getLatitude() + ",");
        sql.append(monitorVehicleGpsBO.getAltitude() + ",");
        sql.append(monitorVehicleGpsBO.getElockSpeed() + ",");
        sql.append(monitorVehicleGpsBO.getDirection() + ",");
        sql.append(monitorVehicleGpsBO.getElectricityValue() + ",");
        sql.append("'" + monitorVehicleGpsBO.getRelatedDevice() + "',");
        sql.append("'" + format.format(new Date()) + "'");
        sql.append(")");
        try {
            logger.info("执行插入语句：" + sql);
            SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
            sqlQuery.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("插入失败，插入语句为：" + sql);
            return false;
        }
        logger.info("插入成功，插入语句为：" + sql);
        return true;
    }

    @SuppressWarnings("unchecked")
    public List<LsMonitorVehicleGpsBO> findAllByCondition(String trackingDeviceNumber,
            LsMonitorTripBO lsMonitorTripBO) {

        /**
         * 判断数据库表是否存在
         */
        try {
            String sql = "select * from GPS.LS_MONITOR_VEHICLE_GPS_" + trackingDeviceNumber + " where TRIP_ID = '"
                    + lsMonitorTripBO.getTripId() + "' ";

            if (NuctechUtil.isNotNull(lsMonitorTripBO.getCheckinTime())) {
                String lowerDateTime = DateUtils.date2String(lsMonitorTripBO.getCheckinTime(),
                        DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
                sql = sql + " and LOCATION_TIME>'" + lowerDateTime + "' ";
            }

            if (NuctechUtil.isNotNull(lsMonitorTripBO.getCheckoutTime())) {
                String upperDateTime = DateUtils.date2String(lsMonitorTripBO.getCheckoutTime(),
                        DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
                sql = sql + " and LOCATION_TIME<'" + upperDateTime + "' ";
            } else {
                String upperDateTime = DateUtils.date2String(new Date(),
                        DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
                sql = sql + " and LOCATION_TIME<'" + upperDateTime + "' ";
            }
            sql = sql + " and LONGITUDE is not null and LATITUDE is not null order by LOCATION_TIME asc";
            // 查询结果
            List<LsMonitorVehicleGpsBO> result = this.getSession().createSQLQuery(sql)
                    .addEntity(LsMonitorVehicleGpsBO.class).list();
            return result;
        } catch (Exception e) {
            logger.warn("查询路线轨迹异常！trackingDeviceNumber=" + trackingDeviceNumber + ",error:" + e.getMessage());
            return null;
        }

        /*
         * Criteria crit =
         * this.getSession().createCriteria(LsMonitorVehicleGpsBO.class);
         * //date2String crit.add(Restrictions.eq("trackingDeviceNumber",
         * trackingDeviceNumber)); crit.add(Restrictions.eq("tripId",
         * lsMonitorTripBO.getTripId()));
         * if(NuctechUtil.isNotNull(lsMonitorTripBO.getCheckinTime())){
         * crit.add(Restrictions.gt("locationTime",
         * lsMonitorTripBO.getCheckinTime())); }
         * if(NuctechUtil.isNotNull(lsMonitorTripBO.getCheckoutTime())){
         * crit.add(Restrictions.le("locationTime",
         * lsMonitorTripBO.getCheckoutTime())); }else{
         * crit.add(Restrictions.le("locationTime", new Date())); }
         * crit.addOrder(Order.asc("gpsSeq")); return crit.list();
         */
    }
    
    /**
     * 查询指定字段
     * @param trackingDeviceNumber
     * @param lsMonitorTripBO
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List findColumnsByCondition(String trackingDeviceNumber, LsMonitorTripBO lsMonitorTripBO) {
        /**
         * 判断数据库表是否存在
         */
        try {
			String sql = "select LATITUDE latitude, LONGITUDE longitude, DIRECTION direction, LOCATION_TIME locationTime, ELECTRICITY_VALUE electricityValue, ALTITUDE altitude, ELOCK_SPEED elockSpeed from GPS.LS_MONITOR_VEHICLE_GPS_"
					+ trackingDeviceNumber + " where TRIP_ID = '" + lsMonitorTripBO.getTripId() + "' ";

            if (NuctechUtil.isNotNull(lsMonitorTripBO.getCheckinTime())) {
                String lowerDateTime = DateUtils.date2String(lsMonitorTripBO.getCheckinTime(),
                        DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
                sql = sql + " and LOCATION_TIME>'" + lowerDateTime + "' ";
            }

            if (NuctechUtil.isNotNull(lsMonitorTripBO.getCheckoutTime())) {
                String upperDateTime = DateUtils.date2String(lsMonitorTripBO.getCheckoutTime(),
                        DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
                sql = sql + " and LOCATION_TIME<'" + upperDateTime + "' ";
            } else {
                String upperDateTime = DateUtils.date2String(new Date(),
                        DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
                sql = sql + " and LOCATION_TIME<'" + upperDateTime + "' ";
            }
            sql = sql + " and LONGITUDE is not null and LATITUDE is not null order by LOCATION_TIME asc";
            // 查询结果
            List<LsMonitorVehicleGpsBO> result = this.getSession().createSQLQuery(sql)
            		.addScalar("latitude", StandardBasicTypes.STRING)
                    .addScalar("longitude", StandardBasicTypes.STRING)
                    .addScalar("direction", StandardBasicTypes.STRING)
                    .addScalar("locationTime", StandardBasicTypes.TIMESTAMP)
                    .addScalar("electricityValue", StandardBasicTypes.STRING)
                    .addScalar("altitude", StandardBasicTypes.STRING)
                    .addScalar("elockSpeed", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            return result;
        } catch (Exception e) {
            logger.warn("查询路线轨迹异常！trackingDeviceNumber=" + trackingDeviceNumber + ",error:" + e.getMessage());
            return null;
        }
    }

    @SuppressWarnings({ "rawtypes", "unused" })
    public List findTrackStatusList(int time) {
        Date date = new Date();
        String string = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
        Session session = this.getSession();
        String sql1 = "SELECT top 1 t.GPS_ID,t.TRACKING_DEVICE_NUMBER,t.ALTITUDE,t.LATITUDE,t.LOCATION_TIME "
                + "FROM [GPS].[LS_MONITOR_PORTAL_GPS_869152021253561] t " + "where t.LOCATION_TIME <'" + string
                + "'ORDER BY t.LOCATION_TIME desc";
        // 查询每天指定时间段的数据
        int timeout = time + 1;
        String sql = "SELECT * FROM [GPS].[LS_MONITOR_PORTAL_GPS_869152021253561] "
                + "where DATEPART(hh, LOCATION_TIME) between'" + time + "'and'" + timeout + "'";

        List list = null;
        try {
            list = session.createSQLQuery(sql).addScalar("GPS_ID", StandardBasicTypes.STRING)
                    .addScalar("TRACKING_DEVICE_NUMBER", StandardBasicTypes.STRING)
                    .addScalar("ALTITUDE", StandardBasicTypes.STRING).addScalar("LATITUDE", StandardBasicTypes.STRING)
                    .addScalar("LOCATION_TIME", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }

    public LsMonitorVehicleGpsBO findByGpsId(String id) {
        Session session = this.getSession();
        String sql = "SELECT * FROM [GPS].[LS_MONITOR_PORTAL_GPS_869152021253561] where GPS_ID='" + id + "'";
        return (LsMonitorVehicleGpsBO) session.createSQLQuery(sql).addEntity(LsMonitorVehicleGpsBO.class)
                .uniqueResult();

    }

    /**
     * 查询设备号开头的表
     * 
     * @param deviceNum
     * @return
     */
    public List<String> findTableNamesByDeviceNum(String deviceNum) {
        String sql = "select name from sysobjects where xtype='U' and name like ? order by name ASC ";
        try {
            SQLQuery sqlQuery = getSession().createSQLQuery(sql);
            sqlQuery.setString(0, "%" + deviceNum + "%");
            @SuppressWarnings("unchecked")
            List<String> result = sqlQuery.list();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 查找所有日志
     * 
     * @param tablePrefix
     *        表名前缀，是车载台表还是设备表
     * @param pageQuery
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public PageList<LsMonitorVehicleGpsBO> findAll(String tablePrefix, PageQuery<Map> pageQuery) {
        Map<String, Object> filters = pageQuery.getFilters();
        if (filters == null) {
            logger.error("No parameters!");
            return new PageList();
        }
        String trackingDeviceNumber = (String) filters.get("trackingDeviceNumber");
        String tableName = tablePrefix + trackingDeviceNumber;

        String conditions = "";
        String tripId = (String) filters.get("tripId");
        if (NuctechUtil.isNotNull(tripId)) {
            conditions += " and t.TRIP_ID = " + tripId;
        }
        String locationType = (String) filters.get("locationType");
        if (NuctechUtil.isNotNull(locationType)) {
            conditions += " and t.LOCATION_TYPE = " + locationType;
        }
        String locationStartTime = (String) filters.get("locationStartTime");
        if (NuctechUtil.isNotNull(locationStartTime)) {
            String time = DateUtils.date2String(
                    DateUtils.strToDate(locationStartTime, DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI),
                    DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI);
            conditions += " and t.LOCATION_TIME >= '" + time + "' ";
        }
        String locationEndTime = (String) filters.get("locationEndTime");
        if (NuctechUtil.isNotNull(locationEndTime)) {
            String time = DateUtils.date2String(
                    DateUtils.strToDate(locationEndTime, DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI),
                    DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI);
            conditions += " and t.LOCATION_TIME <= '" + time + "' ";
        }
        String locationStatus = (String) filters.get("locationStatus");
        if (NuctechUtil.isNotNull(locationStatus)) {
            conditions += " and t.LOCATION_STATUS = " + locationStatus;
        }
        String elockStatus = (String) filters.get("elockStatus");
        if (NuctechUtil.isNotNull(elockStatus)) {
            conditions += " and t.ELOCK_STATUS = " + elockStatus;
        }
        String poleStatus = (String) filters.get("poleStatus");
        if (NuctechUtil.isNotNull(poleStatus)) {
            conditions += " and t.POLE_STATUS = " + poleStatus;
        }
        String brokenStatus = (String) filters.get("brokenStatus");
        if (NuctechUtil.isNotNull(brokenStatus)) {
            conditions += " and t.BROKEN_STATUS = " + brokenStatus;
        }
        String eventUpload = (String) filters.get("eventUpload");
        if (NuctechUtil.isNotNull(eventUpload)) {
            conditions += " and t.EVENT_UPLOAD = " + eventUpload;
        }

        String sql = "select t.* from GPS." + tableName + " t where 1=1 ";
        String countSql = "select count(1) from GPS." + tableName + " t where 1=1 ";
        // count语句不加order by
        countSql += conditions;

        HttpServletRequest request = ServletActionContext.getRequest();
        String sortname = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        if ("tripId".equals(sortname)) {
            conditions += " order by t.TRIP_ID ";
        } else if ("locationType".equals(sortname)) {
            conditions += " order by t.CREATE_TIME ";
        } else if ("locationStartTime".equals(sortname)) {
            conditions += " order by t.LOCATION_TIME ";
        } else if ("locationStatus".equals(sortname)) {
            conditions += " order by t.LOCATION_STATUS ";
        } else if ("elockStatus".equals(sortname)) {
            conditions += " order by t.ELOCK_STATUS ";
        } else if ("poleStatus".equals(sortname)) {
            conditions += " order by t.POLE_STATUS ";
        } else if ("brokenStatus".equals(sortname)) {
            conditions += " order by t.BROKEN_STATUS ";
        } else if ("eventUpload".equals(sortname)) {
            conditions += " order by t.EVENT_UPLOAD ";
        } else {
            conditions += " order by t.LOCATION_TIME ";
        }
        if ("asc".equalsIgnoreCase(sortOrder)) {
            conditions += " asc ";
        } else {
            conditions += " desc ";
        }
        sql += conditions;

        PageList<LsMonitorVehicleGpsBO> pageList = new PageList<>();
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        int firstRow = pageList.getFirstRecordIndex();
        int maxResults = pageQuery.getPageSize();

        Query countQuery = this.getSession().createSQLQuery(countSql);
        Query query = this.getSession().createSQLQuery(sql).addEntity("t", LsMonitorVehicleGpsBO.class)
                .setFirstResult(firstRow).setMaxResults(maxResults);
        List<LsMonitorVehicleGpsBO> list = query.list();

        pageList.addAll(list);
        pageList.setTotalItems(((Number) countQuery.uniqueResult()).intValue());
        return pageList;
    }

}
