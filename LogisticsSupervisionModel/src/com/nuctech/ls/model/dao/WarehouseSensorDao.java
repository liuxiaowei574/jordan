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
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseSensorBO;
import com.nuctech.ls.model.util.DeviceStatus;
import com.nuctech.ls.model.vo.warehouse.ElockDepartmentVO;
import com.nuctech.ls.model.vo.warehouse.SensorDepartmentVO;

/**
 * 作者赵苏阳
 * 
 * 描述<p>子锁Dao<p>
 * @author zsy
 * 创建时间2016年5月28
 *
 */

@Repository
public class WarehouseSensorDao extends LSBaseDao<LsWarehouseSensorBO, Serializable>{
	private LSBaseDao<LsWarehouseSensorBO, Serializable> basedao;
	/**
	 * 根据传感器Id查询
	 * @param sensorId
	 * @return
	 */
	public  LsWarehouseSensorBO getWarehouseSensor(String sensorId){
		Criteria c = basedao.getSession().createCriteria(LsWarehouseSensorBO.class);
		c.add(Restrictions.eq("sensorId", sensorId));
		return (LsWarehouseSensorBO)c.uniqueResult();
		
		//return (LsWarehouseSensorBO)basedao.findById(sensorId);
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
		return ((Long)criteria.uniqueResult()).intValue();
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
		criteria.add(Restrictions.in("sensorNumber", new Object[] {DeviceStatus.Destory.getText(),
				DeviceStatus.Destory.getText(), DeviceStatus.Maintain.getText()}));
		return ((Long)criteria.uniqueResult()).intValue();
	}
	
	
	//随机查询
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public List getSensorList(int m){
			Session session = this.getSession();
			String sql ="SELECT top "+ m +" e.SENSOR_ID, e.SENSOR_NUMBER,e.SENSOR_STATUS,e.SENSOR_TYPE,s.ORGANIZATION_NAME "
					+ "from LS_WAREHOUSE_SENSOR  e left join LS_SYSTEM_DEPARTMENT s on s.ORGANIZATION_ID=e.BELONG_TO ORDER BY NEWID()";
			List list = null;
			try {
				list = session.createSQLQuery(sql).addScalar("SENSOR_ID", StandardBasicTypes.STRING)
						.addScalar("SENSOR_NUMBER", StandardBasicTypes.STRING)
						.addScalar("SENSOR_STATUS", StandardBasicTypes.STRING)
						.addScalar("SENSOR_TYPE", StandardBasicTypes.STRING)
						.addScalar("ORGANIZATION_NAME", StandardBasicTypes.STRING).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
						.list();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
			return list;
		}
		
		//传感器表和机构表关联查询
		public List<SensorDepartmentVO> getSensorSystemList(String organizationId){
			String queryString = 
					"select {e.*},{d.*} from Ls_Warehouse_sensor e,Ls_System_Department d where 1=1"
									+ " and e.belong_To=d.organization_Id "
									+ " and d.organization_Id = "+organizationId;
				Query query = this.getSession().createSQLQuery(queryString).addEntity("e",LsWarehouseSensorBO.class).addEntity("d",LsSystemDepartmentBO.class);
				List<Object[]> list = query.list();
				List<SensorDepartmentVO> sensorDepartmentVOs = new ArrayList<SensorDepartmentVO>();
				for (int i = 0; i < list.size(); i++) {
					SensorDepartmentVO sensorDepartmentVO = new SensorDepartmentVO();
					sensorDepartmentVO.setSensorId(((LsWarehouseSensorBO)list.get(i)[0]).getSensorId());
					sensorDepartmentVO.setSensorNumber(((LsWarehouseSensorBO)list.get(i)[0]).getSensorNumber());
					sensorDepartmentVO.setOrganizationName(((LsSystemDepartmentBO)list.get(i)[1]).getOrganizationName());
					sensorDepartmentVO.setSensorStatus(((LsWarehouseSensorBO)list.get(i)[0]).getSensorStatus());
					sensorDepartmentVO.setSensorType(((LsWarehouseSensorBO)list.get(i)[0]).getSensorType());
					
					sensorDepartmentVOs.add(sensorDepartmentVO);
				}
				return sensorDepartmentVOs;
		}
		
}
