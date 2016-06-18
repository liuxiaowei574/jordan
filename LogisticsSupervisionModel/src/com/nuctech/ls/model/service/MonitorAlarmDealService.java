package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmDealBO;
import com.nuctech.ls.model.dao.MonitorAlarmDealDao;

@Service
@Transactional
public class MonitorAlarmDealService extends LSBaseService {

	@Resource
	private MonitorAlarmDealDao monitorAlarmDealDao;

	public void addAlarmDeal(LsMonitorAlarmDealBO lsMonitorAlarmDealBO) {
		monitorAlarmDealDao.save(lsMonitorAlarmDealBO);
	}

	public List<LsMonitorAlarmDealBO> findAllAlarmDealByAlarmId(String alarmId) {
		return monitorAlarmDealDao.findAllAlarmDealByAlarmId(alarmId);
	}
}
