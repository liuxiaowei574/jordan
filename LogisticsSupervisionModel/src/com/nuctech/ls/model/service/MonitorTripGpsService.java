package com.nuctech.ls.model.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.dao.MonitorVehicleGpsDao;
import com.nuctech.ls.model.dao.MonitorVehicleStatusDao;
import com.nuctech.util.DateJsonValueProcessor;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

@Service
@Transactional
public class MonitorTripGpsService extends LSBaseService {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Resource
    private MonitorVehicleGpsDao monitorVehicleGpsDao;

    private List<LsMonitorVehicleGpsBO> lsMonitorVehicleGpsBOs;
    @Resource
    public MonitorVehicleStatusDao monitorVehicleStatusDao;

    /**
     * 根据行程id获取车辆运动轨迹
     * 
     * @param tripId
     */
    public List<LsMonitorVehicleGpsBO> findLsMonitorVehicleGpsByEclockNum(String trackingDeviceNumber,
            LsMonitorTripBO lsMonitorTripBO) {
        this.lsMonitorVehicleGpsBOs = monitorVehicleGpsDao.findAllByCondition(trackingDeviceNumber, lsMonitorTripBO);
        if (NuctechUtil.isNull(lsMonitorVehicleGpsBOs)) {
            // 如果为空，表明读取的关锁号和网关上来的设备号不一致，需读取状态表获取实际号码
            LsMonitorVehicleStatusBO monitorVehicleStatusBO = monitorVehicleStatusDao
                    .findLatestCommonVehicleStatusBo(lsMonitorTripBO.getTripId(), trackingDeviceNumber);
            if (NuctechUtil.isNotNull(monitorVehicleStatusBO)) {
                String gpsTrackingDeviceNumber = monitorVehicleStatusBO.getGpsTrackingDeviceNumber();
                logger.info(String.format("读取的关锁号和网关上数设备号不一致。读取号码：%s，网关上数号码：%s", trackingDeviceNumber,
                        gpsTrackingDeviceNumber));
                lsMonitorVehicleGpsBOs = monitorVehicleGpsDao.findAllByCondition(gpsTrackingDeviceNumber,
                        lsMonitorTripBO);
            }
        }
        return this.lsMonitorVehicleGpsBOs;

    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONArray findGpsColumnsByEclockNum(String trackingDeviceNumber,
            LsMonitorTripBO lsMonitorTripBO) {
        List<Object> list = monitorVehicleGpsDao.findColumnsByCondition(trackingDeviceNumber, lsMonitorTripBO);
		if(list == null) {
            // 如果为空，表明读取的关锁号和网关上来的设备号不一致，需读取状态表获取实际号码
            LsMonitorVehicleStatusBO monitorVehicleStatusBO = monitorVehicleStatusDao
                    .findLatestCommonVehicleStatusBo(lsMonitorTripBO.getTripId(), trackingDeviceNumber);
            if (NuctechUtil.isNotNull(monitorVehicleStatusBO)) {
                String gpsTrackingDeviceNumber = monitorVehicleStatusBO.getGpsTrackingDeviceNumber();
                logger.info(String.format("读取的关锁号和网关上数设备号不一致。读取号码：%s，网关上数号码：%s", trackingDeviceNumber,
                        gpsTrackingDeviceNumber));
                list = monitorVehicleGpsDao.findColumnsByCondition(gpsTrackingDeviceNumber, lsMonitorTripBO);
            }
        }
		if(list != null && !list.isEmpty()) {
			// list -> jsonArray
			JSONArray array = new JSONArray();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); // 排除关联死循环
			jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));// 格式化日期
			for(Object object : list){
				// map -> json
				Map map = (Map) object;
				JSONObject obj = new JSONObject();
				obj.putAll(map, jsonConfig);
				array.add(obj);
			}
			return array;
		}
		
		return null;
    }

}
