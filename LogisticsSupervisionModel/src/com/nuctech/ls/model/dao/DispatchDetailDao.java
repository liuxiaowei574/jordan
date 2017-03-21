package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceDispatchBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDispatchDetailBO;

/**
 * 作者赵苏阳
 * 
 * 描述
 * <p>
 * 设备调度明细Dao
 * <p>
 * 
 * @author zsy 创建时间2016年6月13
 *
 */
@Repository
public class DispatchDetailDao extends LSBaseDao<LsWarehouseDispatchDetailBO, Serializable> {

    /**
     * 根据设备类型和记录ID查询设备详细信息列表
     * 
     * @param deviceDispatchID
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsWarehouseDispatchDetailBO>
            findWarehouseDispatchDetailByDeviceDispatchIDAndType(String deviceDispatchID, String type) {
        Criteria criteria = getSession().createCriteria(LsWarehouseDispatchDetailBO.class);
        criteria.add(Restrictions.eq("dispatchId", deviceDispatchID));
        criteria.add(Restrictions.eq("deviceType", type));
        List<LsWarehouseDispatchDetailBO> list = criteria.list();
        return list;
    }

    @SuppressWarnings("unused")
    private static final String TRACKING_DEVICE = "TRACKING_DEVICE";
    @SuppressWarnings("unused")
    private LSBaseDao<LsWarehouseDispatchDetailBO, Serializable> basedao = null;

    /**
     * 调度详细表和关锁表以及组织机构表的关联查询；查出关锁的信息显示到调度页面中
     */
    @SuppressWarnings("rawtypes")
    public List getElockDispatchDetailBODetail(String viewDispatchIds) {
        Session session = this.getSession();
        // 注意参数的引号
        String elockSql = "select e.*,d.*,p.* "
                + "from LS_WAREHOUSE_ELOCK e,LS_WAREHOUSE_DISPATCH_DETAIL d ,LS_SYSTEM_DEPARTMENT p "
                + "where 1=1 and e.ELOCK_ID = d.DEVICE_ID and e.BELONG_TO = p.ORGANIZATION_ID  and d.DISPATCH_ID ='"
                + viewDispatchIds + "'";
        List warehouseElockList = null;
        try {
            warehouseElockList = session.createSQLQuery(elockSql).addScalar("ELOCK_NUMBER", StandardBasicTypes.STRING)
                    .addScalar("SIM_CARD", StandardBasicTypes.STRING).addScalar("INTERVAL", StandardBasicTypes.STRING)
                    .addScalar("GATEWAY_ADDRESS", StandardBasicTypes.STRING)
                    .addScalar("ELOCK_STATUS", StandardBasicTypes.STRING)
                    .addScalar("ORGANIZATION_NAME", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return warehouseElockList;
    }

    /**
     * 调度详细表和子锁表以及组织机构表的关联查询；查出子锁的信息显示到调度页面中
     */
    @SuppressWarnings("rawtypes")
    public List getEsealDispatchDetailBODetail(String viewDispatchIds) {
        Session session = this.getSession();
        // 注意参数的引号

        String esealSql = "select e.*,d.*,p.* "
                + "from LS_WAREHOUSE_ESEAL e,LS_WAREHOUSE_DISPATCH_DETAIL d ,LS_SYSTEM_DEPARTMENT p where 1=1"
                + " and e.ESEAL_ID = d.DEVICE_ID" + " and e.BELONG_TO = p.ORGANIZATION_ID" + " and d.DISPATCH_ID = '"
                + viewDispatchIds + "'";
        List warehouseEsealList = null;
        try {
            warehouseEsealList = session.createSQLQuery(esealSql).addScalar("ESEAL_NUMBER", StandardBasicTypes.STRING)
                    .addScalar("ESEAL_STATUS", StandardBasicTypes.STRING)
                    .addScalar("ORGANIZATION_NAME", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return warehouseEsealList;
    }

    /**
     * 调度详细表和传感器表以及组织机构表的关联查询；查出子锁的信息显示到调度页面中
     * 
     * @param viewDispatchIds
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List getSensorDispatchDetailBODetail(String viewDispatchIds) {
        Session session = this.getSession();
        // 注意参数的引号

        String sensorSql = "select s.*,d.*,p.* "
                + "from LS_WAREHOUSE_SENSOR s,LS_WAREHOUSE_DISPATCH_DETAIL d ,LS_SYSTEM_DEPARTMENT p where 1=1"
                + " and s.SENSOR_ID = d.DEVICE_ID" + " and s.BELONG_TO = p.ORGANIZATION_ID" + " and d.DISPATCH_ID = '"
                + viewDispatchIds + "'";
        List warehouseSensorList = null;
        try {
            warehouseSensorList = session.createSQLQuery(sensorSql)
                    .addScalar("SENSOR_NUMBER", StandardBasicTypes.STRING)
                    .addScalar("SENSOR_STATUS", StandardBasicTypes.STRING)
                    .addScalar("SENSOR_TYPE", StandardBasicTypes.STRING)
                    .addScalar("ORGANIZATION_NAME", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return warehouseSensorList;
    }

    /**
     * 根据dispatchId查出调度详细表中的内容
     * 
     * @param dispatchId
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List getDeviceList(String dispatchId) {
        Session session = this.getSession();
        String querySql = "select d.* from LS_WAREHOUSE_DISPATCH_DETAIL d where d.DISPATCH_ID= '" + dispatchId + "'"
                + "ORDER BY DEVICE_TYPE";
        List deviceDetailList = null;
        try {
            deviceDetailList = session.createSQLQuery(querySql).addEntity(LsWarehouseDispatchDetailBO.class).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return deviceDetailList;
        /*
         * @SuppressWarnings("unused") Session session = this.getSession();
         * String Sql =
         * "select d.*,e.* from LS_WAREHOUSE_DISPATCH_DETAIL d,LS_WAREHOUSE_ELOCK e where 1=1"
         * +" and e.ELOCK_ID = d.DEVICE_ID" +" and d.DISPATCH_ID = '"
         * +dispatchId+"'"; List deviceDetailList = null;
         * deviceDetailList=session.createSQLQuery(Sql).addEntity(
         * LsWarehouseDispatchDetailBO.class).list();
         * return deviceDetailList ;
         */

    }

    /**
     * 通过调度的申请id查询调度类
     * @param applicationId
     * @return
     */
    public LsWarehouseDeviceDispatchBO findDispatchBo(String applicationId){
        Criteria criteria = getSession().createCriteria(LsWarehouseDeviceDispatchBO.class); 
        criteria.add(Restrictions.eq("applicationId", applicationId));
        LsWarehouseDeviceDispatchBO warehouseDeviceDispatchBO = (LsWarehouseDeviceDispatchBO) criteria.uniqueResult();
        if (warehouseDeviceDispatchBO == null) {
            return null;
        }
        return warehouseDeviceDispatchBO;
    }
    
}