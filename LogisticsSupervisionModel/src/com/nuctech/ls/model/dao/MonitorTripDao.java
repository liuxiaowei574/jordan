package com.nuctech.ls.model.dao;

import static com.nuctech.util.SqlRemoveUtils.removeFetchKeyword;
import static com.nuctech.util.SqlRemoveUtils.removeOrders;
import static com.nuctech.util.SqlRemoveUtils.removeSelect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.util.DeviceType;
import com.nuctech.util.Constant;
import com.nuctech.util.DateUtils;
import com.nuctech.util.NuctechUtil;

import javacommon.xsqlbuilder.XsqlBuilder;
import javacommon.xsqlbuilder.XsqlBuilder.XsqlFilterResult;


/**
 * 车辆行程信息Dao
 * 
 * @author liushaowei
 *
 */
@Repository
public class MonitorTripDao extends LSBaseDao<LsMonitorTripBO, Serializable> {

	/**
	 * 新增车辆行程信息
	 * 
	 * @param lsMonitorTripBO
	 */
	public void addMonitorTrip(LsMonitorTripBO lsMonitorTripBO) {
		persist(lsMonitorTripBO);
	}

	/**
	 * 查找所有的行程信息
	 * 
	 * @return
	 */
	public List<LsMonitorTripBO> findAllTrips() {
		List<LsMonitorTripBO> list = findAll();
		return list;
	}
	
	/**
	 * 查找设备终端关联的最新的车辆行程
	 * 
	 * @param trackingDeviceNumber
	 * @return
	 * 
	 * @author sunming
	 */
	public LsMonitorTripBO findLastestMonitortripByDeviceNumber(String trackingDeviceNumber){
		
		
		String sql = "SELECT * FROM LS_MONITOR_TRIP WHERE TRACKING_DEVICE_NUMBER='" + trackingDeviceNumber + "' ORDER BY CHECKIN_TIME DESC";
		
		try {
			logger.info("执行查询语句：" + sql);
			SQLQuery sqlQuery = getSession().createSQLQuery(sql).addEntity(LsMonitorTripBO.class);
			@SuppressWarnings("unchecked")
			List<LsMonitorTripBO> lsMonitorTripBOList = sqlQuery.list();
			if(lsMonitorTripBOList != null && lsMonitorTripBOList.size() > 0)
				return lsMonitorTripBOList.get(0);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询失败" + sql);
		}
		
		return null;
	}

	/**
	 * SQL语句分页查询
	 * @param sqlQueryString
	 * @param pageQuery
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageList pageQuerySql(final String sqlQueryString, final PageQuery<Map> pageQuery) {
		XsqlBuilder builder = this.getXsqlBuilder();
		Map filtersMap = pageQuery.getFilters();
		filtersMap.put("sortColumns", pageQuery.getSortColumns());

		XsqlFilterResult queryXsqlResult = builder.generateSql(sqlQueryString, filtersMap);
		final String countQueryString = "select count(*) " + removeSelect(removeFetchKeyword((sqlQueryString)));
		XsqlFilterResult countQueryXsqlResult = builder.generateSql(countQueryString, pageQuery.getFilters());
		Query query = setQueryParameters(this.sessionFactory.getCurrentSession().createSQLQuery(queryXsqlResult.getXsql()),
				queryXsqlResult.getAcceptedFilters());
		Query countQuery = setQueryParameters(
				this.sessionFactory.getCurrentSession().createSQLQuery(removeOrders(countQueryXsqlResult.getXsql())),
				countQueryXsqlResult.getAcceptedFilters());
		PageList pageList = new PageList();
		pageList.setPage(pageQuery.getPage());
		pageList.setPageSize(pageQuery.getPageSize());
		int firstRow = pageList.getFirstRecordIndex();
		int maxResults = pageQuery.getPageSize();
		pageList.addAll(query.setFirstResult(firstRow).setMaxResults(maxResults).list());
		pageList.setTotalItems(((Number) countQuery.uniqueResult()).intValue());
		return pageList;
	}
	
	//-------------------调度分析 相关统计-------------------

	/**
	 * 统计时间区间内日用设备均值
	 * 
	 * @param checkinPort
	 * 				检入口岸
	 * @param interval
	 * 				时间区间
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> findDeviceAverageCountByTimeInterval(String checkinPort, int interval) {
		List<Integer> deviceAverageCountList = new ArrayList<Integer>();
		int totalTrackingDeviceNumber = 0; //xx区间内使用关锁总数
		int totalEsealNumber = 0; //XX时间区间内使用子锁总数
		int totalSensorNumber = 0; //xx时间区间内使用的传感器
		String strEsealNumber = "";
		String strSensorNumber = "";
		Date beginDate = calBeginDate(interval);
		Criteria criteria = getSession().createCriteria(LsMonitorTripBO.class);
		criteria.add(Restrictions.eq("checkinPort", checkinPort));
		criteria.add(Restrictions.between("checkinTime", beginDate, new Date()));
		List<LsMonitorTripBO> monitorTripList = (List<LsMonitorTripBO>)criteria.list();
		if(monitorTripList != null) {
			for(LsMonitorTripBO monitorTrip : monitorTripList) {
				strEsealNumber += monitorTrip.getEsealNumber() + ",";
				strSensorNumber += monitorTrip.getSensorNumber() + ",";
			}
		}
		totalTrackingDeviceNumber = monitorTripList.size();
		String[]  esealNumberArray = strEsealNumber.split(",");
		totalEsealNumber = esealNumberArray.length;
		String[] sensorNumberArray = strSensorNumber.split(",");
		totalSensorNumber = sensorNumberArray.length;
		//关锁在interval时间区间内的平均使用次数(进一法)
		deviceAverageCountList.add((int)Math.ceil(totalTrackingDeviceNumber / (float)interval));
		//子锁在interval时间区间内的平均使用次数(进一法)
		deviceAverageCountList.add((int)Math.ceil(totalEsealNumber / (float)interval));
		//传感器在interval时间区间内的平均使用次数(进一法)
		deviceAverageCountList.add((int)Math.ceil(totalSensorNumber / (float)interval));
		return deviceAverageCountList;
	}
	
	/**
	 * 计算区间开始时间
	 * 
	 * @param interval
	 * 			区间(天)
	 * @return
	 */
	private Date calBeginDate(int interval) {
		String strBeginDate = DateUtils.getTimeBefore(interval, new Date());
		Date beginDate = DateUtils.stringToDate(strBeginDate);
		return beginDate;
	}
	
	/**
	 * 根据设备号和其他条件查询设备
	 * @param tripId
	 * @param filtersMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public LsMonitorTripBO findByIdAndFilters(String tripId, Map filtersMap) {
		HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
		propertiesMap.put("tripId", tripId);

		HashMap<String, Object> propertiesLikeMap = new HashMap<String, Object>();
		String trackingDeviceNumber = (String) filtersMap.get("trackingDeviceNumber");
		if (NuctechUtil.isNotNull(trackingDeviceNumber)) {
			propertiesLikeMap.put("trackingDeviceNumber", trackingDeviceNumber);
		}
		return findByProperties(propertiesMap, propertiesLikeMap, null, null);
	}
	
	//-----------------库存报告相关统计start-----------
	/**
	 * 查询某个口岸的流入/流出设备数
	 * 
	 * @param checkoutPort
	 * 				口岸ID
	 * @param startDate
	 * 				开始时间
	 * @param endDate
	 * 				结束时间
	 * @param type
	 * 				统计类型(流入/流出)
	 * @return
	 * 		设备对应的总数
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Integer> findPortFlowDevices(String portId, Date startDate, Date endDate, String type) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		int totalTrackingDeviceNumber = 0; //xx区间内使用关锁总数
		int totalEsealNumber = 0; //XX时间区间内使用子锁总数
		int totalSensorNumber = 0; //xx时间区间内使用的传感器
		String strEsealNumber = "";
		String strSensorNumber = "";
		Criteria criteria = getSession().createCriteria(LsMonitorTripBO.class);
		if(type.equals(Constant.FLOW_IN)) {
			criteria.add(Restrictions.eq("checkoutPort", portId));
			criteria.add(Restrictions.between("checkoutTime", startDate, endDate));
		} 
		if(type.equals(Constant.FLOW_OUT)) {
			criteria.add(Restrictions.eq("checkinPort", portId));
			criteria.add(Restrictions.between("checkinTime", startDate, endDate));
		}
		List<LsMonitorTripBO> monitorTripList = (List<LsMonitorTripBO>)criteria.list();
		if(monitorTripList != null && !monitorTripList.isEmpty()) {
			for(LsMonitorTripBO monitorTrip : monitorTripList) {
				strEsealNumber += monitorTrip.getEsealNumber() + ",";
				strSensorNumber += monitorTrip.getSensorNumber() + ",";
			}
		}
		totalTrackingDeviceNumber = monitorTripList.size();
		if(!NuctechUtil.isNull(strEsealNumber)) {
			String[]  esealNumberArray = strEsealNumber.split(",");
			totalEsealNumber = esealNumberArray.length;
		}
		if(!NuctechUtil.isNull(strSensorNumber)) {
			String[] sensorNumberArray = strSensorNumber.split(",");
			totalSensorNumber = sensorNumberArray.length;
		}
		map.put(DeviceType.TRACKING_DEVICE.getType(), totalTrackingDeviceNumber);
		map.put(DeviceType.ESEAL.getType(), totalEsealNumber);
		map.put(DeviceType.SENSOR.getType(), totalSensorNumber);
		return map;
	}
	
}
