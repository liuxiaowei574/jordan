/**
 * 
 */
package com.nuctech.ls.center.utils;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;

import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.redis.RedisClientTemplate;
import com.nuctech.ls.model.service.MonitorVehicleStatusService;

import net.sf.json.JSONObject;

/**
 * @author sunming
 *
 */
public class LoadVehicleGPSStatusListener implements ServletContextListener {

	private Logger logger = Logger.getLogger(LoadVehicleGPSStatusListener.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		// MemcachedUtil memcachedUtil =
		// ContextLoader.getCurrentWebApplicationContext().getBean(MemcachedUtil.class);
		RedisClientTemplate redisClientTemplate = ContextLoader.getCurrentWebApplicationContext()
				.getBean(RedisClientTemplate.class);
		MonitorVehicleStatusService vehicleStatusService = ContextLoader.getCurrentWebApplicationContext()
				.getBean(MonitorVehicleStatusService.class);
		List<LsMonitorVehicleStatusBO> lsMonitorVehicleStatusBOs = vehicleStatusService.findAllOnWayVehicleStatus(null);

		if (lsMonitorVehicleStatusBOs != null && lsMonitorVehicleStatusBOs.size() > 0) {
			for (LsMonitorVehicleStatusBO statusBO : lsMonitorVehicleStatusBOs) {
				// memcachedUtil.add(statusBO.getTrackingDeviceNumber(),
				// statusBO);
				JSONObject json = JSONObject.fromObject(statusBO);
				redisClientTemplate.set(statusBO.getTrackingDeviceNumber(), json.toString());
				logger.debug("找到行程设备" + statusBO.getTrackingDeviceNumber() + ",放入缓存中");
			}
		}

	}

}
