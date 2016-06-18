package com.nuctech.ls.model.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseSensorBO;
import com.nuctech.ls.model.dao.WarehouseSensorDao;
import com.nuctech.ls.model.vo.warehouse.ElockDepartmentVO;
import com.nuctech.ls.model.vo.warehouse.SensorDepartmentVO;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 作者： 赵苏阳
 *
 * 描述：<p>传感器增删改查 Service</p>
 * 创建时间：2016年5月28日
 */

@Service
@Transactional
public class WarehouseSensorService extends LSBaseService{
	@Resource
	private WarehouseSensorDao sensorDao;
	
	/**
	 * 添加传感器
	 * @param warehouseSensor
	 */
	public void add(LsWarehouseSensorBO warehouseSensor){
		sensorDao.save(warehouseSensor);
	}
	
	/**
	 * 根据id删除传感器
	 * @param sensorId
	 */
	public void deleteById(String sensorId){
		sensorDao.deleteById(sensorId);
	}
	
	
	/**
	 * 修改传感器
	 * @param warehouseSensor
	 */
	public void modify(LsWarehouseSensorBO warehouseSensor){
		sensorDao.merge(warehouseSensor);
	}
	
	/**
	 * 根据Id查询
	 * @param sensorId
	 * @return
	 */
	public LsWarehouseSensorBO findById(String sensorId){
		return sensorDao.findById(sensorId);
	}
	
	/**
	 *单表查询
	 * 返回JSON
	 * @param pageQuery
	 * @return
	 */
	/*public PageList findWarehouseSensor(PageQuery<Map> pageQuery) {
		String queryString = "select t from LsWarehouseSensorBO t where 1=1 "
				+"/~ and t.sensorNumber = '[sensorNumber]' ~/"
				+"/~ and t.belongTo = '[belongTo]' ~/"
				+"/~ and t.sensorStatus = '[sensorStatus]' ~/"
				+"/~ and t.sensorType = '[sensorType]' ~/"
				+ "/~ order by [sortColumns] ~/";
		
		return sensorDao.pageQuery(queryString, pageQuery);
	}

	public JSONObject fromObjectList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		PageList<LsWarehouseSensorBO> pageList = findWarehouseSensor(pageQuery);
		return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
	}	*/
	//传感器表与组织机构表关联查询
		public JSONObject fromObjectList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes){
			String queryString = 
					"select e,d from LsWarehouseSensorBO e,LsSystemDepartmentBO d where 1=1"
									+ " and e.belongTo=d.organizationId "
									+"/~ and e.sensorNumber like '%[sensorNumber]%' ~/"
									+"/~ and e.sensorStatus like '%[sensorStatus]%' ~/"
									+"/~ and e.belongTo like '%[belongTo]%' ~/"
									+"/~ and e.sensorType like '%[sensorType]%' ~/"
									+ "/~ order by [sortColumns] ~/";
			PageList<Object> queryList = sensorDao.pageQuery(queryString, pageQuery);
			PageList<SensorDepartmentVO> pageList = new PageList<SensorDepartmentVO>();
			if (queryList != null && queryList.size() > 0){
				for (Object obj : queryList) {
					Object[] objs = (Object[]) obj;
					SensorDepartmentVO sensorDepartmentVO = new SensorDepartmentVO();
					BeanUtils.copyProperties((LsWarehouseSensorBO) objs[0], sensorDepartmentVO);
					BeanUtils.copyProperties((LsSystemDepartmentBO) objs[1], sensorDepartmentVO);
					pageList.add(sensorDepartmentVO);
					}
				}
					pageList.setPage(pageQuery.getPage());
					pageList.setPageSize(pageQuery.getPageSize());
					pageList.setTotalItems(queryList.getTotalItems());
					return fromObjectList(pageList, null, false);
		}
	
	/**
	 * 根据传感器号查询记录
	 * @param sensorNumber
	 * @return
	 */
	public LsWarehouseSensorBO findBySensorNumber(String sensorNumber){
		return sensorDao.findByProperty("sensorNumber", sensorNumber);
	}
	
	//查询数据库中所有的关锁记录
		public List<LsWarehouseSensorBO> findAllelock(){
			return (List<LsWarehouseSensorBO>) sensorDao.findAll();
		}
		//查询传感器表和机构表
		public List<SensorDepartmentVO> getSensorDepartmentlist(String organizationId){
			return sensorDao.getSensorSystemList(organizationId);
		}
	
	//调度的随机查询
		@SuppressWarnings("rawtypes")
		public List getSensor(int m){
			return sensorDao.getSensorList(m);
		}
}
