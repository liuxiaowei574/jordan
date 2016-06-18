/**
 * 
 */
package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmBO;

/**
 * @author sunming
 *
 */
@Repository
public class MonitorAlarmDao extends LSBaseDao<LsMonitorAlarmBO, Serializable> {
	/**
	 * 新增报警信息
	 * 
	 * @param lsMonitorAlarmBO
	 */
	public void addMonitorAlarm(LsMonitorAlarmBO lsMonitorAlarmBO) {
		persist(lsMonitorAlarmBO);
	}

	/**
	 * 查找所有的报警信息
	 * 
	 * @return
	 */
	public List<LsMonitorAlarmBO> findAllAlarm() {
		return findAll();
	}

	public List<LsMonitorAlarmBO> findLsMonitorAlarmByTripId(String tripId) {
		return findAllBy("tripId", tripId);
	}
}
