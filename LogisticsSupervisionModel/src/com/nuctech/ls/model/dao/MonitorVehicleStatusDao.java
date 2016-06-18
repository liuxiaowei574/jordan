package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.util.NuctechUtil;

/**
 * 用于记录最新车辆状态信息
 * 
 * @author liqingxian
 *
 */
@Repository
public class MonitorVehicleStatusDao extends LSBaseDao<LsMonitorVehicleStatusBO, Serializable> {

	private final static String TRIP_STATUS_ONWAY = "0";

	@SuppressWarnings("unchecked")
	public List<LsMonitorVehicleStatusBO> findAllOnWayVehicleStatus(String locationType) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(LsMonitorVehicleStatusBO.class);
		crit.add(Restrictions.eq("tripStatus", TRIP_STATUS_ONWAY));
		if(NuctechUtil.isNotNull(locationType)){
			crit.add(Restrictions.eq("locationType", locationType));
		}
		crit.addOrder(Order.asc("checkinTime"));
		return crit.list();
	}
}
