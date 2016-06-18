/**
 * 
 */
package com.nuctech.ls.model.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.dao.MonitorAlarmDao;
import com.nuctech.ls.model.util.AlarmDealType;
import com.nuctech.ls.model.util.AlarmManualType;
import com.nuctech.ls.model.util.AlarmType;
import com.nuctech.ls.model.vo.monitor.ViewAlarmReportVO;
import com.nuctech.util.NuctechUtil;

/**
 * @author sunming
 *
 */
@Service
@Transactional
public class MonitorAlarmService extends LSBaseService {
	private final static String ORDER_BY = " /~ order by [sortColumns] ~/ ";
	
	@Resource
	public MonitorAlarmDao monitorAlarmDao;
	
	/**
	 * 添加
	 * 
	 * @param entity
	 */
	public LsMonitorAlarmBO saveAlarm(AlarmType alarmType,LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO, String content) {
		
		LsMonitorAlarmBO lsMonitorAlarmBO = new LsMonitorAlarmBO();
		lsMonitorAlarmBO.setAlarmId(generatePrimaryKey());
		lsMonitorAlarmBO.setTripId(lsMonitorVehicleStatusBO.getTripId());
		lsMonitorAlarmBO.setAlarmLatitude(lsMonitorVehicleStatusBO.getLatitude());
		lsMonitorAlarmBO.setAlarmLongitude(lsMonitorVehicleStatusBO.getLongitude());
		lsMonitorAlarmBO.setAlarmTime(new Date());
		lsMonitorAlarmBO.setAlarmContent(content);
		lsMonitorAlarmBO.setIsManual(AlarmManualType.Manual.getAlarmType());
		lsMonitorAlarmBO.setAlarmStatus(AlarmDealType.Undeal.getText());
		
		monitorAlarmDao.persist(lsMonitorAlarmBO);
		
		return lsMonitorAlarmBO;
	}

	/**
	 * 查询报警信息及从属信息列表
	 * 
	 * @param pageQuery
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject findAlarmList(PageQuery<Map> pageQuery) {
		// 查询条件：车牌号、关锁号、报警时间、负责人
		String queryString = "select t from ViewAlarmReportVO t" 
				+ " where 1=1 "
				+ "/~ and t.vehiclePlateNumber like '%[vehiclePlateNumber]%' ~/"
				+ "/~ and t.trackingDeviceNumber like '%[trackingDeviceNumber]%' ~/"
				+ "/~ and t.alarmStatus = '[alarmStatus]' ~/"
				+ "/~ and t.alarmLevelId = '[alarmLevel]' ~/"
				+ "/~ and t.alarmTypeId = '[alarmType]' ~/"
				+ "/~ and t.alarmTime >= '[alarmStartTime]' ~/" 
				+ "/~ and t.alarmTime <= '[alarmEndTime]' ~/";
		Map<String, Object> filtersMap = pageQuery.getFilters();
		if (filtersMap != null && filtersMap.get("userId") != null) {
			String userId = (String) filtersMap.get("userId");
			if (NuctechUtil.isNotNull(userId)) {
				String inCondition = joinInCondition("t.userId", userId);
				queryString += inCondition;
			}
		}
		queryString += ORDER_BY;
		PageList<ViewAlarmReportVO> pageList = monitorAlarmDao.pageQuery(queryString, pageQuery);
		pageList.setPage(pageQuery.getPage());
		pageList.setPageSize(pageQuery.getPageSize());
		pageList.setTotalItems(pageList.getTotalItems());
		return fromObjectList(pageList, null, false);
	}

	/**
	 * 拼接带in的条件语句：value1,value2,value3 --> and xxx in ('xxx','xxx')
	 * 
	 * @param columnName
	 *            字段名
	 * @param value
	 *            值（value1,value2,value3...）
	 * @return
	 */
	private String joinInCondition(String columnName, String value) {
		StringBuffer inCondition = new StringBuffer();
		String[] values = filterStr(value).split("\\s*,\\s*");
		if (values != null && values.length > 0) {
			inCondition.append(" and ").append(columnName).append(" in ('").append(value.replaceAll("\\s*,\\s*", "','"))
					.append("')");
		}
		return inCondition.toString();
	}

	/**
	 * 过滤查询条件的特殊字符
	 * 
	 * @param str
	 */
	protected String filterStr(String str) {
		return str.replaceAll("\\[", "[[]").replaceAll("\\^", "[^]").replaceAll("_", "[_]").replaceAll("%", "[%]");
	}
	
	/**
	 * 根据行程Id获取该行程的所有报警
	 * @param pageQuery
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ViewAlarmReportVO> findListByTripId(PageQuery pageQuery) {
//		return monitorAlarmDao.findAllBy("tripId", tripId);
		String queryString = "select t from ViewAlarmReportVO t" 
				+ " where 1=1 "
				+ "/~ and t.tripId = '[tripId]' ~/"
				+ " order by t.alarmTime desc ";
		PageList<ViewAlarmReportVO> pageList = monitorAlarmDao.pageQuery(queryString, pageQuery);
		return pageList;
	}

	public List<LsMonitorAlarmBO> findLsMonitorAlarmByTripId(String tripId) {
		return monitorAlarmDao.findLsMonitorAlarmByTripId(tripId);
	}

}
