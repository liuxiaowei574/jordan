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
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceApplicationBO;
import com.nuctech.ls.model.util.DeviceType;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 调度申请 DAO
 * </p>
 * 创建时间：2016年6月4日
 */
@Repository
public class WarehouseDeviceApplicationDao extends LSBaseDao<LsWarehouseDeviceApplicationBO, Serializable> {

    @SuppressWarnings("unchecked")
    public Map<String, Integer> findTurnInDevices(String portId, Date startDate, Date endDate) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        Integer trackingDeviceNumber = 0;
        Integer esealNumber = 0;
        Integer sensorNumber = 0;
        Criteria criteria = getSession().createCriteria(LsWarehouseDeviceApplicationBO.class);
        if (NuctechUtil.isNotNull(portId)) {
            criteria.add(Restrictions.eq("applcationPort", portId));
        }
        if (!NuctechUtil.isNull(startDate) && !NuctechUtil.isNull(endDate)) {
            criteria.add(Restrictions.between("finishTime", startDate, endDate));
        }
        List<LsWarehouseDeviceApplicationBO> warehouseDeviceApplicationList = criteria.list();
        if (warehouseDeviceApplicationList != null && !warehouseDeviceApplicationList.isEmpty()) {
            for (LsWarehouseDeviceApplicationBO warehouseDeviceApplication : warehouseDeviceApplicationList) {
                //调度申请"处理"后，设备才能算作"转入"
                if(warehouseDeviceApplication.getApplyStatus().equals(Constant.DEVICE_ALREADY_DEAL_WITH)||warehouseDeviceApplication.getApplyStatus().equals(Constant.DEVICE_ALREADY_FINISH)){
                    trackingDeviceNumber += Integer.parseInt(warehouseDeviceApplication.getDeviceNumber());
                    esealNumber += Integer.parseInt(warehouseDeviceApplication.getEsealNumber());
                    sensorNumber += Integer.parseInt(warehouseDeviceApplication.getSensorNumber());
                }
            }
        }
        map.put(DeviceType.TRACKING_DEVICE.getType(), trackingDeviceNumber);
        map.put(DeviceType.ESEAL.getType(), esealNumber);
        map.put(DeviceType.SENSOR.getType(), sensorNumber);
        return map;
    }
}
