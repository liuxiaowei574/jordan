package com.nuctech.ls.model.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.dao.MonitorVehicleStatusDao;
/**
 * 
 * @author liqingxian
 *
 */
@Service
@Transactional
public class MonitorVehicleStatusService extends LSBaseService{

	 @Resource
	 private MonitorVehicleStatusDao monitorVehicleStatusDao;
	 
	 private final static String TRIP_STATUS_ONWAY = "0";
	 /**
	  * 界面初始化查询所有车辆最新状态
	  * @return
	  */
	 public List<LsMonitorVehicleStatusBO> findAllVehicleStatus(String monitorType){
		 return monitorVehicleStatusDao.findAllBy("findAllBy",monitorType);
	 }
	 
	 
	 /**
	 * @param entity
	 */
	public void saveOrUpdate(LsMonitorVehicleStatusBO entity){
		 monitorVehicleStatusDao.saveOrUpdate(entity);
	 }
	 
	 /**
	  * 界面初始化查询所有在途车辆最新状态
	  * @return
	  */
	 public List<LsMonitorVehicleStatusBO> findAllOnWayVehicleStatus(String locationType){
		 
//		 HashMap<String, String> keyMap = new LinkedHashMap<String,String>();
//		 keyMap.put("checkinTime", "desc");
		 return monitorVehicleStatusDao.findAllOnWayVehicleStatus(locationType);//("tripStatus", TRIP_STATUS_ONWAY, keyMap);
	 }
     /**
	 * toJson
	 * 
	 * @param srcList
	 * @param pageList
	 * @param pageQuery
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject vehilciStatusObjectList(List<LsMonitorVehicleStatusBO> srcList, PageList<LsMonitorVehicleStatusBO> pageList,
			PageQuery<Map> pageQuery) {
		if (srcList == null || srcList.size() < 1) {
			return null;
		}
		pageList.setPage(pageQuery.getPage());
		pageList.setPageSize(pageQuery.getPageSize());
		pageList.addAll(srcList);
		pageList.setTotalItems(srcList.size());
		return fromObjectList(pageList, null, false);
	}


	public LsMonitorVehicleStatusBO findPatrolByNumber(String trackingDeviceNumber) {
		return monitorVehicleStatusDao.findByProperty("trackingDeviceNumber", trackingDeviceNumber);
	} 
 
	 
}
