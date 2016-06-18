package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceDispatchBO;
import com.nuctech.ls.model.util.DeviceType;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>调配记录 Dao</p>
 * 创建时间：2016年6月11日
 */
@Repository
public class WarehouseDeviceDispatchDao extends LSBaseDao<LsWarehouseDeviceDispatchBO, Serializable> {
	
	/**
	 * 通过申请ID查询记录List
	 * 
	 * @param applicationId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LsWarehouseDeviceDispatchBO> findWarehouseDeviceDispatchListByApplicationId(String applicationId) {
		Criteria criteria = getSession().createCriteria(LsWarehouseDeviceDispatchBO.class);
		criteria.add(Restrictions.eq("applicationId", applicationId));
		return criteria.list();
	}

	/**
	 * 统计转入设备总数
	 * 
	 * @param portId
	 * 			口岸ID
	 * @param startDate
	 * 			开始时间
	 * @param endDate
	 * 			结束时间
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Integer> findTurnOutDevices(String portId, Date startDate, Date endDate) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Integer trackingDeviceNumber = 0;
		Integer esealNumber = 0;
		Integer sensorNumber = 0;
		Criteria criteria = getSession().createCriteria(LsWarehouseDeviceDispatchBO.class);
		criteria.add(Restrictions.eq("fromPort", portId));
		criteria.add(Restrictions.between("dispatchTime", startDate, endDate));
		List<LsWarehouseDeviceDispatchBO> warehouseDeviceDispatchList = criteria.list();
		if(warehouseDeviceDispatchList != null && !warehouseDeviceDispatchList.isEmpty()) {
			for(LsWarehouseDeviceDispatchBO WarehouseDeviceDispatch : warehouseDeviceDispatchList) {
				trackingDeviceNumber += Integer.parseInt(WarehouseDeviceDispatch.getDeviceNumber());
				esealNumber += Integer.parseInt(WarehouseDeviceDispatch.getEsealNumber());
				sensorNumber += Integer.parseInt(WarehouseDeviceDispatch.getSensorNumber());
			}
		}
		map.put(DeviceType.TRACKING_DEVICE.getType(), trackingDeviceNumber);
		map.put(DeviceType.ESEAL.getType(), esealNumber);
		map.put(DeviceType.SENSOR.getType(), sensorNumber);
		return map;
	}
}
