package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.util.DeviceStatus;
import com.nuctech.ls.model.vo.warehouse.ElockDepartmentVO;

/**
 * 作者赵苏阳
 * 
 * 描述
 * <p>
 * 关锁查询Dao
 * <p>
 * 
 * @author zsy 创建时间2016年5月18
 *
 */
@Repository
public class WarehouseElockDao extends LSBaseDao<LsWarehouseElockBO, Serializable> {

    private LSBaseDao<LsWarehouseElockBO, Serializable> basedao = null;

    /**
     * 根据关锁Id查询
     * 
     * @param elockId
     *        关锁Id
     * @return
     */

    public LsWarehouseElockBO getWarehouseElockById(String elockId) {
        Criteria criteria = basedao.getSession().createCriteria(LsWarehouseElockBO.class);
        criteria.add(Restrictions.eq("elockId", elockId));
        return (LsWarehouseElockBO) criteria.uniqueResult();
    }

    /**
     * 根据关锁编号查询
     * 
     * @param elockNumber
     *        关锁号
     * @return
     */
    public LsWarehouseElockBO getElockByNum(String elockNumber) {
        Criteria criteria = getSession().createCriteria(LsWarehouseElockBO.class);
        criteria.add(Restrictions.eq("elockNumber", elockNumber));
        return (LsWarehouseElockBO) criteria.uniqueResult();
    }

    /**
     * 查询所有关锁(非在途关锁)
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsWarehouseElockBO> getAllElock() {
        Criteria criteria = getSession().createCriteria(LsWarehouseElockBO.class);
        //criteria.add(Restrictions.ne("elockStatus", "2"));//统计包括"在途"的关锁
        return (List<LsWarehouseElockBO>) criteria.list();
    }

    /**
     * 统计口岸可用的关锁数
     * 
     * @param portId
     *        关锁ID
     * @return
     */
    public int statisticsAvailableElockByPortId(String portId) {
        Criteria criteria = getSession().createCriteria(LsWarehouseElockBO.class);
        criteria.setProjection(Projections.count("elockId"));
        criteria.add(Restrictions.eq("belongTo", portId));
        criteria.add(Restrictions.eq("elockStatus", DeviceStatus.Normal.getText()));
        return ((Long) criteria.uniqueResult()).intValue();
    }

    /**
     * 统计口岸不可用的关锁数
     * 
     * @param portId
     * @return
     */
    public int statisticsNotAvailableElockByPortId(String portId) {
        Criteria criteria = getSession().createCriteria(LsWarehouseElockBO.class);
        criteria.setProjection(Projections.count("elockId"));
        criteria.add(Restrictions.eq("belongTo", portId));
        criteria.add(Restrictions.in(
                "elockStatus",
                new Object[] { DeviceStatus.Destory.getText(), DeviceStatus.Destory.getText(),
                        DeviceStatus.Maintain.getText() }));
        return ((Long) criteria.uniqueResult()).intValue();
    }

    // 随机查询
    @SuppressWarnings({ "rawtypes" })
    public List getList(int m, String organizationId) {
        Session session = this.getSession();
        String sql = "SELECT top " + m
                + " e.ELOCK_ID, e.ELOCK_NUMBER,e.SIM_CARD,e.INTERVAL,e.GATEWAY_ADDRESS,e.ELOCK_STATUS,"
                + "s.ORGANIZATION_NAME " + "from LS_WAREHOUSE_ELOCK  e left join LS_SYSTEM_DEPARTMENT s "
                + "on (s.ORGANIZATION_ID=e.BELONG_TO) where e.ELOCK_STATUS='1'  and e.BELONG_TO= '" + organizationId
                + "' ORDER BY NEWID()";
        List list = null;
        try {
            list = session.createSQLQuery(sql).addScalar("ELOCK_ID", StandardBasicTypes.STRING)
                    .addScalar("ELOCK_NUMBER", StandardBasicTypes.STRING)
                    .addScalar("SIM_CARD", StandardBasicTypes.STRING).addScalar("INTERVAL", StandardBasicTypes.STRING)
                    .addScalar("GATEWAY_ADDRESS", StandardBasicTypes.STRING)
                    .addScalar("ELOCK_STATUS", StandardBasicTypes.STRING)
                    .addScalar("ORGANIZATION_NAME", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<ElockDepartmentVO> getElockSystem(String organizationId) {
        String elockStatus = DeviceStatus.Normal.getText();
        String queryString = "select {e.*},{d.*} from Ls_Warehouse_Elock e,Ls_System_Department d where 1=1"
                + " and e.belong_To=d.organization_Id " + " and d.organization_Id = '" + organizationId + "'"+" and e.ELOCK_STATUS='"+elockStatus+"'";
        Query query = this.getSession().createSQLQuery(queryString).addEntity("e", LsWarehouseElockBO.class)
                .addEntity("d", LsSystemDepartmentBO.class);
        List<Object[]> list = query.list();
        List<ElockDepartmentVO> elockDepartmentVOs = new ArrayList<ElockDepartmentVO>();
        for (int i = 0; i < list.size(); i++) {
            ElockDepartmentVO elockDepartmentVO = new ElockDepartmentVO();
            elockDepartmentVO.setElockId(((LsWarehouseElockBO) list.get(i)[0]).getElockId());
            elockDepartmentVO.setElockNumber(((LsWarehouseElockBO) list.get(i)[0]).getElockNumber());
            elockDepartmentVO.setOrganizationName(((LsSystemDepartmentBO) list.get(i)[1]).getOrganizationName());
            elockDepartmentVO.setSimCard(((LsWarehouseElockBO) list.get(i)[0]).getSimCard());
            elockDepartmentVO.setInterval(((LsWarehouseElockBO) list.get(i)[0]).getInterval());
            elockDepartmentVO.setGatewayAddress(((LsWarehouseElockBO) list.get(i)[0]).getGatewayAddress());
            elockDepartmentVO.setElockStatus(((LsWarehouseElockBO) list.get(i)[0]).getElockStatus());

            elockDepartmentVOs.add(elockDepartmentVO);
        }
        return elockDepartmentVOs;
    }

    /**
     * 统计正在使用的（在途）的关锁 的数量
     * 
     * @return
     */
    public int findElockCountInUse() {
        Session session = this.getSession();
        @SuppressWarnings("rawtypes")
        List list = session.createCriteria(LsWarehouseElockBO.class)
                .add(Restrictions.eq("elockStatus", DeviceStatus.Inway.getText())).list();
        int elockCount = list.size();
        return elockCount;
    }

    /**
     * 统计正在使用的（在途）的关锁
     * 
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List findElockInWay() {
        String queryString = "select e.*,d.* from Ls_Warehouse_Elock e,Ls_System_Department d where 1=1"
                + " and e.belong_To=d.organization_Id " + " and e.ELOCK_STATUS = '" + 2 + "'";
        Query query = this.getSession().createSQLQuery(queryString).addEntity("e", LsWarehouseElockBO.class)
                .addEntity("d", LsSystemDepartmentBO.class);
        List<Object[]> list = query.list();
        List<ElockDepartmentVO> elockDepartmentVOs = new ArrayList<ElockDepartmentVO>();
        for (int i = 0; i < list.size(); i++) {
            ElockDepartmentVO elockDepartmentVO = new ElockDepartmentVO();
            elockDepartmentVO.setElockId(((LsWarehouseElockBO) list.get(i)[0]).getElockId());
            elockDepartmentVO.setElockNumber(((LsWarehouseElockBO) list.get(i)[0]).getElockNumber());
            elockDepartmentVO.setOrganizationName(((LsSystemDepartmentBO) list.get(i)[1]).getOrganizationName());

            elockDepartmentVOs.add(elockDepartmentVO);
        }
        return elockDepartmentVOs;
    }

    /**
     * 统计某个口岸下关锁的数量(非在途关锁)
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsWarehouseElockBO> findElockByOrgId(String portId) {
        Criteria criteria = getSession().createCriteria(LsWarehouseElockBO.class);
        criteria.add(Restrictions.eq("belongTo", portId));
        //criteria.add(Restrictions.ne("elockStatus", "2"));
        return (List<LsWarehouseElockBO>) criteria.list();
    }

    /**
     * 查询所有关锁升级信息
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> elockUpgradeInfo() {
        String queryString = "select {e.*},{s.*} from LS_WAREHOUSE_ELOCK e left join LS_MONITOR_VEHICLE_STATUS s"
                + " on e.ELOCK_NUMBER = s.TRACKING_DEVICE_NUMBER";
        SQLQuery sqlQuery = this.getSession().createSQLQuery(queryString).addEntity("e", LsWarehouseElockBO.class)
                .addEntity("s", LsMonitorVehicleStatusBO.class);
        return sqlQuery.list();
    }
}
