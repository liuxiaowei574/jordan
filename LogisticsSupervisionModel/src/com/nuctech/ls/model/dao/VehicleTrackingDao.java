package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.util.DateUtils;

@Repository
public class VehicleTrackingDao extends LSBaseDao<LsMonitorVehicleGpsBO, Serializable>{

	 
	@SuppressWarnings({ "rawtypes" })
	public List<LsMonitorVehicleGpsBO> findMonitorVehicleGpsByTripId(LsMonitorTripBO monitorTripBO){
		StringBuffer sql = new StringBuffer("SELECT GPS.ALTITUDE, GPS.ELECTRICITY_VALUE ,GPS.ELOCK_SPEED,GPS.GPS_SEQ,GPS.LATITUDE,GPS.LONGITUDE,GPS.LOCATION_TIME,GPS.DIRECTION  FROM ");
		sql.append("LS_MONITOR_VEHICLE_GPS_"+monitorTripBO.getTrackingDeviceNumber());
		sql.append(" gps ");
		sql.append(" WHERE gps.LOCATION_TIME>=");
		if(null!=monitorTripBO.getCheckinTime()){
			sql.append("'" + monitorTripBO.getCheckinTime() + "' ");
		}
		sql.append("AND");
		sql.append(" gps.LOCATION_TIME<=");
		sql.append("'" + monitorTripBO.getCheckoutTime() + "' ");
		try {
			logger.info("执行根据tripid查询历史轨迹语句：" + sql);
			 Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql.toString());
			 List list = query.list();
			 if (list != null && list.size() > 0) {
		            List<LsMonitorVehicleGpsBO> result = new ArrayList<LsMonitorVehicleGpsBO>();
		            for (int i = 0; i < list.size(); i++) {
		                Object[] obj = (Object[]) list.get(i);
		                LsMonitorVehicleGpsBO vo = new LsMonitorVehicleGpsBO();
		                if (obj[0] != null) {
		                    vo.setGpsId(obj[0].toString());
		                } else {
		                    vo.setGpsId("");
		                }
		                if (obj[6] != null) {
		                    vo.setLocationTime(DateUtils.stringToDate(obj[6].toString()));
		                } else {
		                    vo.setLocationTime(null);
		                }
		                if (obj[5] != null) {
		                    vo.setLongitude(obj[5].toString());
		                } else {
		                    vo.setLongitude("");
		                }
		                if (obj[5] != null) {
		                    vo.setLatitude(obj[5].toString());
		                } else {
		                    vo.setLatitude("");
		                }
		                if (obj[0] != null) {
		                    vo.setAltitude(obj[0].toString());
		                } else {
		                    vo.setAltitude("");
		                } if (obj[2] != null) {
		                    vo.setElockSpeed(obj[2].toString());
		                } else {
		                    vo.setElockSpeed("");
		                }if (obj[1] != null) {
		                    vo.setElectricityValue(obj[1].toString());
		                } else {
		                    vo.setElectricityValue("");
		                }
		                if (obj[7] != null) {
		                    vo.setDirection(obj[7].toString());
		                } else {
		                    vo.setDirection("");
		                }
		                result.add(vo);
		            }
		            logger.info("查询成功，查询语句为：" + sql);
		            return result;
		        }
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询失败，查询语句为：" + sql);
			return null;
		}
		return null;
		
		//return list;
	}
}
