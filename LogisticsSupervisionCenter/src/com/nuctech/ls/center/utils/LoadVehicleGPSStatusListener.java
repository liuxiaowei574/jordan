/**
 * 
 */
package com.nuctech.ls.center.utils;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;

import com.nuctech.ls.common.memcached.MemcachedUtil;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.service.MonitorVehicleStatusService;

/**
 * @author sunming
 *
 */
public class LoadVehicleGPSStatusListener implements ServletContextListener {

	
	private Logger logger = Logger.getLogger(LoadVehicleGPSStatusListener.class);
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		MemcachedUtil memcachedUtil = ContextLoader.getCurrentWebApplicationContext().getBean(MemcachedUtil.class);
		MonitorVehicleStatusService vehicleStatusService = ContextLoader.getCurrentWebApplicationContext().getBean(MonitorVehicleStatusService.class);
		List<LsMonitorVehicleStatusBO> lsMonitorVehicleStatusBOs = vehicleStatusService.findAllOnWayVehicleStatus(null);
		
		if(lsMonitorVehicleStatusBOs != null && lsMonitorVehicleStatusBOs.size() > 0){
			for(LsMonitorVehicleStatusBO statusBO : lsMonitorVehicleStatusBOs){
				memcachedUtil.put(statusBO.getTrackingDeviceNumber(), statusBO);
				logger.debug("找到行程设备" + statusBO.getTrackingDeviceNumber()+",放入缓存中");
			}
		}
		
		
	}

}
