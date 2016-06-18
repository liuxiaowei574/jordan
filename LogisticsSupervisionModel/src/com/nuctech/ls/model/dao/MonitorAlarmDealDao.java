package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmDealBO;

@Repository
public class MonitorAlarmDealDao extends LSBaseDao<LsMonitorAlarmDealBO, Serializable> {
	/**
	 * 新增报警处理信息
	 * 
	 * @param lsMonitorAlarmDealBO
	 */
	public void addMonitorAlarm(LsMonitorAlarmDealBO lsMonitorAlarmDealBO) {
		persist(lsMonitorAlarmDealBO);
	}

	/**
	 * 查找所有的报警处理信息
	 * 
	 * @return
	 */
	public List<LsMonitorAlarmDealBO> findAllAlarmDeal() {
		return findAll();
	}

	/**
	 * 根据报警Id获取该报警所有的处理信息
	 * 
	 * @param alarmId
	 * @return
	 */
	public List<LsMonitorAlarmDealBO> findAllAlarmDealByAlarmId(String alarmId) {
		return findAllBy("alarmId", alarmId);
	}
}
