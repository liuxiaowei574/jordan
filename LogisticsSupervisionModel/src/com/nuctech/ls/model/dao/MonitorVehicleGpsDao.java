/**
 * 
 */
package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
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
	 *            表名称
	 * 
	 *            create table LS_MONITOR_VEHICLE_GPS ( GPS_ID varchar(50) not null, TRIP_ID varchar(50) null, LOCATION_TYPE varchar(2) null, GPS_SEQ
	 *            varchar(50) null, TRACKING_DEVICE_NUMBER varchar(50) null, LOCATION_TIME datetime null, LOCATION_STATUS varchar(20) null,
	 *            ELOCK_STATUS varchar(2) null, POLE_STATUS varchar(2) null, BROKEN_STATUS varchar(2) null, EVENT_UPLOAD varchar(2) null, LONGITUDE
	 *            varchar(20) null, LATITUDE varchar(20) null, ALTITUDE varchar(20) null, ELOCK_SPEED varchar(20) null, DIRECTION varchar(20) null,
	 *            ELECTRICITY_VALUE varchar(20) null ) go
	 * 
	 *            alter table LS_MONITOR_VEHICLE_GPS add constraint PK_LS_MONITOR_VEHICLE_GPS primary key nonclustered (GPS_ID) go
	 * 
	 * @return
	 */
	public boolean createVehicleGpsTable(String tableName) {

		String sql = "CREATE TABLE [GPS]." + tableName + " (" + "GPS_ID               varchar(50)          not null,"
				+ "TRIP_ID              varchar(50)          null," 
				+ "LOCATION_TYPE        varchar(2)           null,"
				+ "GPS_SEQ              varchar(50)          null," 
				+ "TRACKING_DEVICE_NUMBER varchar(50)          null,"
				+ "LOCATION_TIME        datetime             null," 
				+ "LOCATION_STATUS      varchar(20)          null,"
				+ "ELOCK_STATUS         varchar(2)           null," 
				+ "POLE_STATUS          varchar(2)           null,"
				+ "BROKEN_STATUS        varchar(2)           null," 
				+ "EVENT_UPLOAD         varchar(2)           null,"
				+ "LONGITUDE            varchar(20)          null," 
				+ "LATITUDE             varchar(20)          null,"
				+ "ALTITUDE             varchar(20)          null," 
				+ "ELOCK_SPEED          varchar(20)          null,"
				+ "DIRECTION            varchar(20)          null," 
				+ "ELECTRICITY_VALUE    varchar(20)          null,"
				+ "RELATED_DEVICE       varchar(200)          null,"
				+ "CREATE_TIME          datetime             null " + "); "
				+ "ALTER TABLE "+ tableName + "   ADD CONSTRAINT PK_"+ tableName +" PRIMARY KEY NONCLUSTERED (GPS_ID)";

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
		sql.append("[LONGITUDE], [LATITUDE], [ALTITUDE], [ELOCK_SPEED], [DIRECTION], [ELECTRICITY_VALUE],[RELATED_DEVICE],[CREATE_TIME])");
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
	public List<LsMonitorVehicleGpsBO> findAllByCondition(String trackingDeviceNumber, LsMonitorTripBO lsMonitorTripBO) {
		
		/**
         * 判断数据库表是否存在
         */
        try {
            String sql = "select * from LS_MONITOR_VEHICLE_GPS_" + trackingDeviceNumber 
            		+ " where TRIP_ID = '" + lsMonitorTripBO.getTripId() + "' ";
            
            if(NuctechUtil.isNotNull(lsMonitorTripBO.getCheckinTime())){
            	String lowerDateTime = DateUtils.date2String(lsMonitorTripBO.getCheckinTime(), DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
            	sql = sql + " and LOCATION_TIME>'" + lowerDateTime + "' ";
    		}
            
            if(NuctechUtil.isNotNull(lsMonitorTripBO.getCheckoutTime())){
            	String upperDateTime = DateUtils.date2String(lsMonitorTripBO.getCheckoutTime(), DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
            	sql = sql + " and LOCATION_TIME<'" + upperDateTime + "' ";
    		}else{
    			String upperDateTime = DateUtils.date2String(new Date(), DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
    			sql = sql + " and LOCATION_TIME<'" + upperDateTime + "' ";
    		}
            sql = sql + " and LONGITUDE is not null and LATITUDE is not null order by GPS_SEQ asc";
            // 查询结果
            System.out.println(sql);
            List<LsMonitorVehicleGpsBO> result = this.getSession().createSQLQuery(sql).addEntity(LsMonitorVehicleGpsBO.class).list();
            return result;
        } catch (Exception e) {
        	logger.warn("路线轨迹表LS_MONITOR_VEHICLE_GPS_" + trackingDeviceNumber+"不存在！");
            return null;
        }
		
		/*Criteria crit = this.getSession().createCriteria(LsMonitorVehicleGpsBO.class);
		//date2String
		crit.add(Restrictions.eq("trackingDeviceNumber", trackingDeviceNumber));
		crit.add(Restrictions.eq("tripId", lsMonitorTripBO.getTripId()));
		if(NuctechUtil.isNotNull(lsMonitorTripBO.getCheckinTime())){
			crit.add(Restrictions.gt("locationTime", lsMonitorTripBO.getCheckinTime()));
		}
		if(NuctechUtil.isNotNull(lsMonitorTripBO.getCheckoutTime())){
			crit.add(Restrictions.le("locationTime", lsMonitorTripBO.getCheckoutTime()));
		}else{
			crit.add(Restrictions.le("locationTime", new Date()));
		}
					
		crit.addOrder(Order.asc("gpsSeq"));
		return crit.list();*/
	}

}
