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
import com.nuctech.ls.model.bo.warehouse.LsWarehouseSensorBO;
import com.nuctech.ls.model.util.DeviceStatus;
import com.nuctech.ls.model.vo.warehouse.SensorDepartmentVO;
import com.nuctech.util.MessageResourceUtil;

/**
 * 作者赵苏阳
 * 
 * 描述
 * <p>
 * 子锁Dao
 * <p>
 * 
 * @author zsy 创建时间2016年5月28
 *
 */

@Repository
public class WarehouseSensorDao extends LSBaseDao<LsWarehouseSensorBO, Serializable> {

    private LSBaseDao<LsWarehouseSensorBO, Serializable> basedao;

    /**
     * 根据传感器Id查询
     * 
     * @param sensorId
     * @return
     */
    public LsWarehouseSensorBO getWarehouseSensor(String sensorId) {
        Criteria c = basedao.getSession().createCriteria(LsWarehouseSensorBO.class);
        c.add(Restrictions.eq("sensorId", sensorId));
        return (LsWarehouseSensorBO) c.uniqueResult();
    }

    /**
     * 根据传感器号查询
     * 
     * @param sensorNumber
     * @return
     */
    public LsWarehouseSensorBO getSensorBysensorNum(String sensorNumber) {
        Criteria c = getSession().createCriteria(LsWarehouseSensorBO.class);
        c.add(Restrictions.eq("sensorNumber", sensorNumber));
        return (LsWarehouseSensorBO) c.uniqueResult();
    }

    /**
     * 查询所有传感器(非在途状态传感器)
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsWarehouseSensorBO> getAllSensor() {
        Criteria criteria = getSession().createCriteria(LsWarehouseSensorBO.class);
        //criteria.add(Restrictions.ne("sensorStatus", "2"));
        return (List<LsWarehouseSensorBO>) criteria.list();
    }

    /**
     * 统计口岸可用的子锁数
     * 
     * @param portId
     * @return
     */
    public Integer statisticsAvailableSensorByPortId(String portId) {
        Criteria criteria = getSession().createCriteria(LsWarehouseSensorBO.class);
        criteria.setProjection(Projections.count("sensorId"));
        criteria.add(Restrictions.eq("belongTo", portId));
        criteria.add(Restrictions.eq("sensorStatus", DeviceStatus.Normal.getText()));
        return ((Long) criteria.uniqueResult()).intValue();
    }

    /**
     * 统计口岸不可用的子锁数
     * 
     * @param portId
     * @return
     */
    public Integer statisticsNotAvailableSensorByPortId(String portId) {
        Criteria criteria = getSession().createCriteria(LsWarehouseSensorBO.class);
        criteria.setProjection(Projections.count("sensorId"));
        criteria.add(Restrictions.eq("belongTo", portId));
        criteria.add(Restrictions.in("sensorNumber", new Object[] { DeviceStatus.Destory.getText(),
                DeviceStatus.Destory.getText(), DeviceStatus.Maintain.getText() }));
        return ((Long) criteria.uniqueResult()).intValue();
    }

    // 随机查询
    @SuppressWarnings({ "rawtypes" })
    public List getSensorList(int m, String organizationId) {
        Session session = this.getSession();
        String sql = "SELECT top " + m
                + " e.SENSOR_ID, e.SENSOR_NUMBER,e.SENSOR_STATUS,e.SENSOR_TYPE,s.ORGANIZATION_NAME "
                + "from LS_WAREHOUSE_SENSOR  e left join LS_SYSTEM_DEPARTMENT s "
                + "on (s.ORGANIZATION_ID=e.BELONG_TO) where e.SENSOR_STATUS='1' and e.BELONG_TO= '" + organizationId
                + "' ORDER BY NEWID()";
        List list = null;
        try {
            list = session.createSQLQuery(sql).addScalar("SENSOR_ID", StandardBasicTypes.STRING)
                    .addScalar("SENSOR_NUMBER", StandardBasicTypes.STRING)
                    .addScalar("SENSOR_STATUS", StandardBasicTypes.STRING)
                    .addScalar("SENSOR_TYPE", StandardBasicTypes.STRING)
                    .addScalar("ORGANIZATION_NAME", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 传感器表和机构表关联查询
    @SuppressWarnings("unchecked")
    public List<SensorDepartmentVO> getSensorSystemList(String organizationId) {
        String sensorStatus = DeviceStatus.Normal.getText();
        String queryString = "select {e.*},{d.*} from Ls_Warehouse_sensor e,Ls_System_Department d where 1=1"
                + " and e.belong_To=d.organization_Id " + " and d.organization_Id = '" + organizationId + "'"+" and e.SENSOR_STATUS='"+sensorStatus+"'";
        Query query = this.getSession().createSQLQuery(queryString).addEntity("e", LsWarehouseSensorBO.class)
                .addEntity("d", LsSystemDepartmentBO.class);
        List<Object[]> list = query.list();
        List<SensorDepartmentVO> sensorDepartmentVOs = new ArrayList<SensorDepartmentVO>();
        for (int i = 0; i < list.size(); i++) {
            SensorDepartmentVO sensorDepartmentVO = new SensorDepartmentVO();
            sensorDepartmentVO.setSensorId(((LsWarehouseSensorBO) list.get(i)[0]).getSensorId());
            sensorDepartmentVO.setSensorNumber(((LsWarehouseSensorBO) list.get(i)[0]).getSensorNumber());
            sensorDepartmentVO.setOrganizationName(((LsSystemDepartmentBO) list.get(i)[1]).getOrganizationName());
            sensorDepartmentVO.setSensorStatus(((LsWarehouseSensorBO) list.get(i)[0]).getSensorStatus());
            sensorDepartmentVO.setSensorType(((LsWarehouseSensorBO) list.get(i)[0]).getSensorType());

            sensorDepartmentVOs.add(sensorDepartmentVO);
        }
        return sensorDepartmentVOs;
    }

    /**
     * 统计正在使用的（在途）的传感器
     * 
     * @return
     */
    public int findElockCountInUse() {
        Session session = this.getSession();
        @SuppressWarnings("rawtypes")
        List list = session.createCriteria(LsWarehouseSensorBO.class)
                .add(Restrictions.eq("sensorStatus", DeviceStatus.Inway.getText())).list();
        int sensorCount = list.size();
        return sensorCount;
    }

    /**
     * 统计某个口岸下传感器的数量(非在途状态传感器)
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsWarehouseSensorBO> findSensorByOrgId(String portId) {
        Criteria criteria = getSession().createCriteria(LsWarehouseSensorBO.class);
        criteria.add(Restrictions.eq("belongTo", portId));
        //criteria.add(Restrictions.ne("sensorStatus", "2"));
        return (List<LsWarehouseSensorBO>) criteria.list();
    }

    /**
     * 统计指定口岸传感器的库存情况
     * 
     * @param portName
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsWarehouseSensorBO> findPortSensor(String portName,String seriesName) {
    	String sensorStatus = "";
    	String keyongSensor = MessageResourceUtil.getMessageInfo("statistic.sensor.keyong");//可用传感器
    	String zaituSensor = MessageResourceUtil.getMessageInfo("statistic.sensor.zaitu");//在途传感器
    	String sunhuaiSensor = MessageResourceUtil.getMessageInfo("statistic.sensor.sunhuai");//损坏传感器
    	String weixiuSensor = MessageResourceUtil.getMessageInfo("statistic.sensor.weixiu");//维修传感器
    	String baofeiSensor = MessageResourceUtil.getMessageInfo("statistic.sensor.baofei");//报废传感器
    	
        Session session = this.getSession();
        String sql = "SELECT s.SENSOR_NUMBER,s.SENSOR_STATUS  " + "FROM LS_WAREHOUSE_SENSOR s,LS_SYSTEM_DEPARTMENT d "
                + "where s.BELONG_TO =d.ORGANIZATION_ID and d.ORGANIZATION_NAME='" + portName + "'";
        
        if(seriesName.equals(keyongSensor)){
        	sensorStatus = DeviceStatus.Normal.getText();
        	sql +=" and s.SENSOR_STATUS='" +sensorStatus+"'";
    	}
        if(seriesName.equals(zaituSensor)){
        	sensorStatus = DeviceStatus.Inway.getText();
        	sql +=" and s.SENSOR_STATUS='" +sensorStatus+"'";
    	}
        if(seriesName.equals(sunhuaiSensor)){
        	sensorStatus = DeviceStatus.Destory.getText();
        	sql +=" and s.SENSOR_STATUS='" +sensorStatus+"'";
    	}
        if(seriesName.equals(weixiuSensor)){
        	sensorStatus = DeviceStatus.Maintain.getText();
        	sql +=" and s.SENSOR_STATUS='" +sensorStatus+"'";
    	}
        if(seriesName.equals(baofeiSensor)){
        	sensorStatus = DeviceStatus.Scrap.getText();
        	sql +=" and s.SENSOR_STATUS='" +sensorStatus+"'";
    	}
        
        List<LsWarehouseSensorBO> list = null;
        try {
            list = session.createSQLQuery(sql).addScalar("SENSOR_NUMBER", StandardBasicTypes.STRING)
                    .addScalar("SENSOR_STATUS", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }
}
