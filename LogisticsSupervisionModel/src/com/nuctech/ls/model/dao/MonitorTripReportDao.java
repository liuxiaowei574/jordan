/**
 * 
 */
package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
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

    /**
     * 查找行程列表(导出excel)
     * 
     * @return
     */
    public List<?> findTripData() {
        org.hibernate.Session session = this.getSession();
        String sql = "select distinct t.*,v.* " + "from LS_MONITOR_TRIP t, LS_COMMON_VEHICLE v "
                + "where 1=1  and t.TRIP_ID = v.TRIP_ID";
        List<?> list = null;
        list = session.createSQLQuery(sql).addScalar("DECLARATION_NUMBER", StandardBasicTypes.STRING)
                .addScalar("TRACKING_DEVICE_NUMBER", StandardBasicTypes.STRING)
                .addScalar("ESEAL_NUMBER", StandardBasicTypes.STRING)
                .addScalar("SENSOR_NUMBER", StandardBasicTypes.STRING)
                .addScalar("VEHICLE_PLATE_NUMBER", StandardBasicTypes.STRING)
                .addScalar("CHECKIN_USER", StandardBasicTypes.STRING)
                .addScalar("CHECKIN_TIME", StandardBasicTypes.STRING)
                .addScalar("CHECKOUT_USER", StandardBasicTypes.STRING)
                .addScalar("CHECKOUT_TIME", StandardBasicTypes.STRING)
                .addScalar("TRIP_STATUS", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

        return list;
    }

}
