package com.nuctech.ls.model.service;

import java.util.HashMap;
import java.util.List;
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
import com.nuctech.ls.model.dao.MonitorTripDao;
import com.nuctech.ls.model.vo.monitor.MonitorTripVehicleVO;

import net.sf.json.JSONObject;

/**
 * 车辆行程信息Service
 * 
 * @author liushaowei
 *
 */
@Service
@Transactional
public class MonitorTripService extends LSBaseService {
	@Resource
	private MonitorTripDao monitorTripDao;

	/**
	 * 根据编号获取行程信息
	 */
	public LsMonitorTripBO findMonitortripById(String id) {
		return monitorTripDao.findById(id);
	}

	/**
	 * 根据设备编号获取行程
	 * 
	 * @param id
	 * @return
	 */
	public LsMonitorTripBO findLastestMonitortripByDeviceNumber(String trackingDeviceNumber) {
		return monitorTripDao.findLastestMonitortripByDeviceNumber(trackingDeviceNumber);
	}

	/**
	 * 根据条件获取指定的车辆行程信息
	 * 
	 * @param propertiesMap
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	public LsMonitorTripBO findMonitorTrip(HashMap<String, Object> propertiesMap) throws Exception {
		return monitorTripDao.findByProperties(propertiesMap, null);
	}

	/**
	 * 新增车辆行程信息
	 * 
	 * @param lsMonitorTripBO
	 * @throws Exception
	 */
	public void addMonitorTrip(LsMonitorTripBO lsMonitorTripBO) throws Exception {
		monitorTripDao.addMonitorTrip(lsMonitorTripBO);
	}

	/**
	 * 更新行程信息
	 * 
	 * @param lsMonitorTripBO
	 */
	public void updateMonitorTrip(LsMonitorTripBO lsMonitorTripBO) {
		monitorTripDao.update(lsMonitorTripBO);
	}

	/**
	 * 查找所有的行程信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<LsMonitorTripBO> findAllTrips() throws Exception {
		return monitorTripDao.findAllTrips();
	}

	/**
	 * 查询行程和车辆信息列表
	 * @param pageQuery
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject findTripVehicleList(PageQuery<Map> pageQuery) {
		String queryString = "select t,v from LsMonitorTripBO t,LsCommonVehicleBO v where 1=1 "
				+ " and t.vehicleId=v.vehicleId "
				+ "/~ and t.trackingDeviceNumber like '%[trackingDeviceNumber]%' ~/"
				+ "/~ and t.esealNumber like '%[esealNumber]%' ~/" 
				+ "/~ and t.sensorNumber like '%[sensorNumber]%' ~/"
				+ "/~ and t.checkinUser like '%[checkinUser]%' ~/" 
				+ "/~ and t.checkinTime = '[checkinTime]' ~/"
				+ "/~ and t.checkinPort like '%[checkinPort]%' ~/" 
				+ "/~ and t.tripStatus like '%[tripStatus]%' ~/"
				+ "/~ order by [sortColumns] ~/";
		PageList<Object> queryList = monitorTripDao.pageQuery(queryString, pageQuery);
		PageList<MonitorTripVehicleVO> pageList = new PageList<MonitorTripVehicleVO>();
		if (queryList != null && queryList.size() > 0) {
			for (Object obj : queryList) {
				Object[] objs = (Object[]) obj;
				MonitorTripVehicleVO tripVehicleVO = new MonitorTripVehicleVO();
				BeanUtils.copyProperties((LsMonitorTripBO) objs[0], tripVehicleVO);
				BeanUtils.copyProperties((LsCommonVehicleBO) objs[1], tripVehicleVO);
				pageList.add(tripVehicleVO);
			}
		}
		pageList.setPage(pageQuery.getPage());
		pageList.setPageSize(pageQuery.getPageSize());
		pageList.setTotalItems(queryList.getTotalItems());
		return fromObjectList(pageList, null, false);
	}
	
	/**
	 * 查询一条行程和车辆信息
	 * @param pageQuery
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject findOneTripVehicle(PageQuery<Map> pageQuery) {
		String queryString = "select t,v,"
				+ "(select organizationName from LsSystemDepartmentBO where organizationId = t.checkinPort), "
				+ "(select organizationName from LsSystemDepartmentBO where organizationId = t.checkoutPort), "
				+ "(select routeAreaName from LsMonitorRouteAreaBO where routeAreaId = t.routeId) "
				+ " from LsMonitorTripBO t,LsCommonVehicleBO v where 1=1 "
				+ " and t.vehicleId = v.vehicleId and t.tripStatus = '0' " //行程状态：0,进行中
				+ "/~ and t.trackingDeviceNumber = '[trackingDeviceNumber]' ~/"
				+ "/~ and v.declarationNumber = '[declarationNumber]' ~/"
				+ "/~ and v.vehiclePlateNumber = '[vehiclePlateNumber]' ~/"
				+ "/~ order by [sortColumns] ~/";
		PageList<Object> queryList = monitorTripDao.pageQuery(queryString, pageQuery);
		PageList<MonitorTripVehicleVO> pageList = new PageList<MonitorTripVehicleVO>();
		if (queryList != null && queryList.size() > 0) {
			Object[] objs = (Object[]) queryList.get(0);
			MonitorTripVehicleVO tripVehicleVO = new MonitorTripVehicleVO();
			BeanUtils.copyProperties((LsMonitorTripBO) objs[0], tripVehicleVO);
			BeanUtils.copyProperties((LsCommonVehicleBO) objs[1], tripVehicleVO);
			tripVehicleVO.setCheckinPortName((objs[2] == null) ? "" : (String)objs[2]);
			tripVehicleVO.setCheckoutPortName((objs[3] == null) ? "" : (String)objs[3]);
			tripVehicleVO.setRouteName((objs[4] == null) ? "" : (String)objs[4]);
			pageList.add(tripVehicleVO);
		}
		pageList.setPage(pageQuery.getPage());
		pageList.setPageSize(pageQuery.getPageSize());
		pageList.setTotalItems(queryList.getTotalItems());
		return fromObjectList(pageList, null, false);
	}

}
