package com.nuctech.ls.model.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.monitor.LsMonitorRaPointBO;
import com.nuctech.ls.model.dao.MonitorRaPointDao;

@Service
@Transactional
public class MonitorRaPointService extends LSBaseService {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	@Resource
	private MonitorRaPointDao monitorRaPointDao;

	

	/**
	 * 获取所有线路或区域
	 * 
	 */
	public List<LsMonitorRaPointBO> findAllMonitorRaPoints() throws Exception {
		return monitorRaPointDao.findAll();
	}
	/**
	 * 根据所画图形添加坐标点
	 * 
	 * @param lsMonitorRaPointBO
	 */
	public void addMonitorRaPoint(LsMonitorRaPointBO lsMonitorRaPointBO) throws Exception {
		monitorRaPointDao.addMonitorRaPoint(lsMonitorRaPointBO);
	}
	
	/**
	 * 删除坐标点
	 * 
	 * @param lsMonitorRaPointBO
	 */
	public void deleteMonitorRaPoint(String routeAreaId) throws Exception {
		HashMap<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put("routeAreaId", routeAreaId);
		monitorRaPointDao.deleteByPropertys(keyMap);
	}

	/**
	 * 根据编号获取区域或线路信息
	 * 
	 * @param id
	 */
	public List<LsMonitorRaPointBO> findAllMonitorRaPointByRouteAreaId(String areaId) throws Exception {
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("gpsSeq", "asc");
		logger.info(String.format("get MonitorRaPoint by RouteAreaId, RouteAreaId is：%s", areaId));
		return monitorRaPointDao.findAllBy("routeAreaId", areaId, map);
	}
	public void deleteMonitorRaPointByRAIds(String ids) {
		monitorRaPointDao.deleteMonitorRaPointByRAIds(ids);
	}
	
	
	
}
