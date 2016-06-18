package com.nuctech.ls.model.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.monitor.LsMonitorRaPointBO;
/**
 * @author liqingxian
 */
@Repository
public class MonitorRaPointDao extends LSBaseDao<LsMonitorRaPointBO, Serializable> {

	/**
	 * 根据User所画图形添加坐标点
	 * @param lsMonitorRaPointBO
	 */
	public void addMonitorRaPoint(LsMonitorRaPointBO lsMonitorRaPointBO){
		persist(lsMonitorRaPointBO);
	}

	public void deleteMonitorRaPointByRAIds(String ids) {
		String sql = " delete from LS_MONITOR_RA_POINT where ROUTE_AREA_ID in (" + ids + ")";
		System.err.println(sql);
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	
	

}
