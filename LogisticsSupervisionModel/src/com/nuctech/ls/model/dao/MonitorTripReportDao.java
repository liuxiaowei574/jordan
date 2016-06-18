/**
 * 
 */
package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;

/**
 * 监管报告Dao
 * 
 * @author liushaowei
 *
 */
@Repository
public class MonitorTripReportDao extends LSBaseDao<LsMonitorTripBO, Serializable> {
	/**
	 * 新增行程监管信息
	 * 
	 * @param lsMonitorTripBO
	 */
	public void addMonitorTrip(LsMonitorTripBO lsMonitorTripBO) {
		persist(lsMonitorTripBO);
	}

	/**
	 * 查找所有的行程监管信息
	 * 
	 * @return
	 */
	public List<LsMonitorTripBO> findAllTrip() {
		return findAll();
	}
}
