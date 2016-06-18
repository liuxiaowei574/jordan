/**
 * 
 */
package com.nuctech.ls.model.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.dao.MonitorTripReportDao;
import com.nuctech.ls.model.vo.monitor.MonitorTripVehicleVO;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;

/**
 * 监管报告Service
 * @author liushaowei
 *
 */
@Service
@Transactional
public class MonitorTripReportService extends LSBaseService {
	private final static String ORDER_BY = " /~ order by [sortColumns] ~/ ";
	
	@Resource
	public MonitorTripReportDao monitorTripReportDao;

	/**
	 * 查询监管行程信息及从属信息列表
	 * 
	 * @param pageQuery
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject findTripReportList(PageQuery<Map> pageQuery) {
		// 查询条件：车牌号、关锁号、司机姓名、检入地点、检出地点、检入时间、检出时间、检入人员、检出人员，点击“详情”进入详情页，展示该行程所有的报警信息
		String queryString = "select t,v, "
				+ "(select userName from LsSystemUserBO where userId = t.checkinUser), "
				+ "(select userName from LsSystemUserBO where userId = t.checkoutUser) "
				+ " from LsMonitorTripBO t,LsCommonVehicleBO v where 1=1 "
				+ " and t.vehicleId=v.vehicleId "
				+ "/~ and t.trackingDeviceNumber like '%[trackingDeviceNumber]%' ~/"
				+ "/~ and t.checkinUser like '%[checkinUser]%' ~/" 
				+ "/~ and t.checkinTime >= '[checkinStartTime]' ~/"
				+ "/~ and t.checkinTime <= '[checkinEndTime]' ~/"
				+ "/~ and t.checkoutUser like '%[checkoutUser]%' ~/" 
				+ "/~ and t.checkoutTime >= '[checkoutStartTime]' ~/"
				+ "/~ and t.checkoutTime <= '[checkoutEndTime]' ~/"
				+ "/~ and t.checkinPort = '[checkinPort]' ~/"
				+ "/~ and t.checkoutPort = '[checkoutPort]' ~/"
				+ "/~ and v.vehiclePlateNumber like '%[vehiclePlateNumber]%' ~/"
				+ "/~ and v.driverName like '%[driverName]%' ~/";
		
		//获取checkinUserId、checkoutUserId条件
		Map<String, Object> filtersMap = pageQuery.getFilters();
		if (filtersMap != null) {
			if (filtersMap.get("checkinUserId") != null) {
				String checkinUserId = (String) filtersMap.get("checkinUserId");
				if (NuctechUtil.isNotNull(checkinUserId)) {
					String inCondition = joinInCondition("t.checkinUser", checkinUserId);
					queryString += inCondition;
				}
			}
			if (filtersMap.get("checkoutUserId") != null) {
				String checkoutUserId = (String) filtersMap.get("checkoutUserId");
				if (NuctechUtil.isNotNull(checkoutUserId)) {
					String inCondition = joinInCondition("t.checkoutUser", checkoutUserId);
					queryString += inCondition;
				}
			}
		}
		queryString += ORDER_BY;
		
		PageList<Object> queryList = monitorTripReportDao.pageQuery(queryString, pageQuery);
		PageList<MonitorTripVehicleVO> pageList = new PageList<MonitorTripVehicleVO>();
		if (queryList != null && queryList.size() > 0) {
			for (Object obj : queryList) {
				Object[] objs = (Object[]) obj;
				MonitorTripVehicleVO tripVehicleVO = new MonitorTripVehicleVO();
				BeanUtils.copyProperties((LsMonitorTripBO) objs[0], tripVehicleVO);
				BeanUtils.copyProperties((LsCommonVehicleBO) objs[1], tripVehicleVO);
				tripVehicleVO.setCheckinUserName((objs[2] == null) ? "" : (String)objs[2]);
				tripVehicleVO.setCheckoutUserName((objs[3] == null) ? "" : (String)objs[3]);
				pageList.add(tripVehicleVO);
			}
		}
		pageList.setPage(pageQuery.getPage());
		pageList.setPageSize(pageQuery.getPageSize());
		pageList.setTotalItems(queryList.getTotalItems());
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

}
