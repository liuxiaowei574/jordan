package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseEsealBO;
import com.nuctech.ls.model.util.DeviceStatus;
import com.nuctech.ls.model.vo.warehouse.EsealDepartementVO;
import com.nuctech.util.MessageResourceUtil;

/**
 * 作者赵苏阳
 * 
 * 描述
 * <p>
 * 子锁查询Dao
 * <p>
 * 
 * @author zsy 创建时间2016年5月27
 *
 */
@Repository
public class WarehouseEsealDao extends LSBaseDao<LsWarehouseEsealBO, Serializable> {

    private LSBaseDao<LsWarehouseEsealBO, Serializable> basedao = null;

    /**
     * 根据Id子锁查询查询
     * 
     * @param esealId
     * @return
     */
    public LsWarehouseEsealBO getWarehouseEseal(String esealId) {
        Criteria criteria = basedao.getSession().createCriteria(LsWarehouseEsealBO.class);
        criteria.add(Restrictions.eq("esealId", esealId));
        return (LsWarehouseEsealBO) criteria.uniqueResult();

    }

    /**
     * 根据子锁号查询
     * 
     * @param esealNumber
     * @return
     */
    public LsWarehouseEsealBO getEsealByesealNum(String esealNumber) {
        Criteria criteria = getSession().createCriteria(LsWarehouseEsealBO.class);
        criteria.add(Restrictions.eq("esealNumber", esealNumber));
        return (LsWarehouseEsealBO) criteria.uniqueResult();

    }

    /**
     * 查询所有子锁
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsWarehouseEsealBO> getAllEseal() {
        Criteria criteria = getSession().createCriteria(LsWarehouseEsealBO.class);
        //criteria.add(Restrictions.ne("esealStatus", "2"));
        return (List<LsWarehouseEsealBO>) criteria.list();
    }

    /**
     * 统计口岸可用的子锁数
     * 
     * @param portId
     * @return
     */
    public Integer statisticsAvailableEsealByPortId(String portId) {
        try {
            Criteria criteria = getSession().createCriteria(LsWarehouseEsealBO.class);
            criteria.setProjection(Projections.count("esealId"));
            criteria.add(Restrictions.eq("belongTo", portId));
            criteria.add(Restrictions.eq("esealStatus", DeviceStatus.Normal.getText()));
            return ((Long) criteria.uniqueResult()).intValue();
        } catch (HibernateException e) {
            e.printStackTrace();
            return null;

        }
    }

    /**
     * 统计口岸不可用的子锁数
     * 
     * @param portId
     * @return
     */
    public Integer statisticsNotAvailableEsealByPortId(String portId) {
        Criteria criteria = getSession().createCriteria(LsWarehouseEsealBO.class);
        criteria.setProjection(Projections.count("esealId"));
        criteria.add(Restrictions.eq("belongTo", portId));
        criteria.add(Restrictions.in("esealStatus", new Object[] { DeviceStatus.Destory.getText(),
                DeviceStatus.Destory.getText(), DeviceStatus.Maintain.getText() }));
        return ((Long) criteria.uniqueResult()).intValue();
    }

    // 随机查询
    @SuppressWarnings({ "rawtypes" })
    public List getEsealList(int m, String organizationId) {
        Session session = this.getSession();
        String sql = "SELECT top " + m + " e.ESEAL_ID,e.ESEAL_NUMBER,e.ESEAL_STATUS,s.ORGANIZATION_NAME "
                + "from LS_WAREHOUSE_ESEAL e left join LS_SYSTEM_DEPARTMENT s "
                + "on (s.ORGANIZATION_ID=e.BELONG_TO) where e.ESEAL_STATUS='1' and e.BELONG_TO= '" + organizationId
                + "' ORDER BY NEWID()";
        List list = null;
        try {
            list = session.createSQLQuery(sql).addScalar("ESEAL_ID", StandardBasicTypes.STRING)
                    .addScalar("ESEAL_NUMBER", StandardBasicTypes.STRING)
                    .addScalar("ESEAL_STATUS", StandardBasicTypes.STRING)
                    .addScalar("ORGANIZATION_NAME", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<EsealDepartementVO> getEsealSystem(String organizationId) {
        String esealStatus = DeviceStatus.Normal.getText();
        String queryString = "select {e.*},{d.*} from Ls_Warehouse_Eseal e,Ls_System_Department d where 1=1"
                + " and e.belong_To=d.organization_Id " + " and d.organization_Id='" + organizationId + "'"+" and e.ESEAL_STATUS='"+esealStatus+"'";
        Query query = this.getSession().createSQLQuery(queryString).addEntity("e", LsWarehouseEsealBO.class)
                .addEntity("d", LsSystemDepartmentBO.class);
        List<Object[]> list = query.list();
        List<EsealDepartementVO> esealDepartementVOs = new ArrayList<EsealDepartementVO>();
        for (int i = 0; i < list.size(); i++) {
            EsealDepartementVO esealDepartementVO = new EsealDepartementVO();

            esealDepartementVO.setEsealId(((LsWarehouseEsealBO) list.get(i)[0]).getEsealId());
            esealDepartementVO.setEsealNumber(((LsWarehouseEsealBO) list.get(i)[0]).getEsealNumber());
            esealDepartementVO.setOrganizationName(((LsSystemDepartmentBO) list.get(i)[1]).getOrganizationName());
            esealDepartementVO.setEsealStatus(((LsWarehouseEsealBO) list.get(i)[0]).getEsealStatus());

            esealDepartementVOs.add(esealDepartementVO);
        }
        return esealDepartementVOs;
    }

    /**
     * 统计正在使用的（在途）的子锁
     * 
     * @return
     */
    public int findEsealCountInUse() {
        Session session = this.getSession();
        @SuppressWarnings("rawtypes")
        List list = session.createCriteria(LsWarehouseEsealBO.class)
                .add(Restrictions.eq("esealStatus", DeviceStatus.Inway.getText())).list();
        int esealCount = list.size();
        return esealCount;
    }

    /**
     * 查询指定口岸设备的库存信息
     * 
     * @param portName
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsWarehouseEsealBO> findPortEseal(String portName,String seriesName) {
    	String esealStatus = "";
    	String keyongEseal = MessageResourceUtil.getMessageInfo("statistic.eseal.keyong");//可用子锁
    	String zaituEseal = MessageResourceUtil.getMessageInfo("statistic.eseal.zaitu");//在途子锁
    	String sunhuaiEseal = MessageResourceUtil.getMessageInfo("statistic.eseal.sunhuai");//损坏子锁
    	String weixiuEseal = MessageResourceUtil.getMessageInfo("statistic.eseal.weixiu");//维修子锁
    	String baofeiEseal = MessageResourceUtil.getMessageInfo("statistic.eseal.baofei");//报废子锁
        Session session = this.getSession();
        String sql = "SELECT s.ESEAL_NUMBER,s.ESEAL_STATUS  " + "FROM LS_WAREHOUSE_ESEAL s,LS_SYSTEM_DEPARTMENT d "
                + "where s.BELONG_TO =d.ORGANIZATION_ID and d.ORGANIZATION_NAME='" + portName + "'";
        if(seriesName.equals(keyongEseal)){
        	esealStatus = DeviceStatus.Normal.getText();
        	sql +=" and s.ESEAL_STATUS='" +esealStatus+"'";
    	}
        if(seriesName.equals(zaituEseal)){
        	esealStatus = DeviceStatus.Inway.getText();
        	sql +=" and s.ESEAL_STATUS='" +esealStatus+"'";
    	}
        if(seriesName.equals(sunhuaiEseal)){
        	esealStatus = DeviceStatus.Destory.getText();
        	sql +=" and s.ESEAL_STATUS='" +esealStatus+"'";
    	}
        if(seriesName.equals(weixiuEseal)){
        	esealStatus = DeviceStatus.Maintain.getText();
        	sql +=" and s.ESEAL_STATUS='" +esealStatus+"'";
    	}
        if(seriesName.equals(baofeiEseal)){
        	esealStatus = DeviceStatus.Scrap.getText();
        	sql +=" and s.ESEAL_STATUS='" +esealStatus+"'";
    	}
        
        List<LsWarehouseEsealBO> list = null;
        try {
            list = session.createSQLQuery(sql).addScalar("ESEAL_NUMBER", StandardBasicTypes.STRING)
                    .addScalar("ESEAL_STATUS", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 统计某个口岸下子锁的数量(非在途状态)
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsWarehouseEsealBO> findEsealByOrgId(String portId) {
        Criteria criteria = getSession().createCriteria(LsWarehouseEsealBO.class);
        criteria.add(Restrictions.eq("belongTo", portId));
       // criteria.add(Restrictions.ne("esealStatus", "2"));
        return (List<LsWarehouseEsealBO>) criteria.list();
    }

}
