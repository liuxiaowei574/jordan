package com.nuctech.ls.model.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.hibernate.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jcraft.jsch.Session;
import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.dao.WarehouseElockDao;
import com.nuctech.ls.model.vo.warehouse.ElockDepartmentVO;

import groovy.time.BaseDuration.From;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 作者： 赵苏阳
 *
 * 描述：<p>关锁增删改查 Service</p>
 * 创建时间：2016年5月18日
 */

@Service
@Transactional
public class WarehouseElockService extends LSBaseService {
	
	@Resource
	private WarehouseElockDao warehouseDao;
	
	/**
	 * 增加关锁
	 * @param warehouseElock
	 */
	public void add(LsWarehouseElockBO warehouseElock){
		 warehouseDao.save(warehouseElock);
	}
	
	
	/**
	 * 删除关锁
	 * @param warehouseElock
	 */
	public void delete(LsWarehouseElockBO warehouseElock){
		 warehouseDao.delete(warehouseElock);
	}
	
	public void deleteById(String elockId){
		warehouseDao.deleteById(elockId);
	}
	/**
	 * 修改关锁
	 * @param warehouseElock
	 */
	public void modify(LsWarehouseElockBO warehouseElock) {
		 warehouseDao.merge(warehouseElock);
	}
	
	
	/**
	 * 根据关锁Id查询
	 * @param elockId
	 * @return
	 */
	public LsWarehouseElockBO findById(String elockId){
		return warehouseDao.findById(elockId);
	}

	//查询数据库中所有的关锁记录
	/*public List<LsWarehouseElockBO> findAllelock(){
		return (List<LsWarehouseElockBO>) warehouseDao.findAll();
	}*/
	//查询关锁表和机构表
	public List<ElockDepartmentVO> getElockDapatmentlist(String organizationId){
		return warehouseDao.getElockSystem(organizationId);
	}
	
	
	
	
	//返回JSON单表查询
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject fromObjectList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		String queryString = 
				"select e,d from LsWarehouseElockBO e,LsSystemDepartmentBO d where 1=1"
								+ " and e.belongTo=d.organizationId "
								+"/~ and e.elockNumber like '%[elockNumber]%' ~/"
								+"/~ and e.simCard like '%[simCard]%' ~/"
								+"/~ and e.belongTo like '%[belongTo]%' ~/"
								+"/~ and e.interval like  '%[interval]%' ~/"
								+"/~ and e.elockStatus like  '%[elockStatus]%' ~/"
								+"/~ and e.simCard like '%[simCard]%' ~/"
								+ "/~ order by [sortColumns] ~/";
		PageList<Object> queryList = warehouseDao.pageQuery(queryString, pageQuery);
		PageList<ElockDepartmentVO> pageList = new PageList<ElockDepartmentVO>();
		if (queryList != null && queryList.size() > 0){
			for (Object obj : queryList) {
				Object[] objs = (Object[]) obj;
				ElockDepartmentVO elockDepartmentVO = new ElockDepartmentVO();
				BeanUtils.copyProperties((LsWarehouseElockBO) objs[0], elockDepartmentVO);
				BeanUtils.copyProperties((LsSystemDepartmentBO) objs[1], elockDepartmentVO);
				pageList.add(elockDepartmentVO);
				}
			}
				pageList.setPage(pageQuery.getPage());
				pageList.setPageSize(pageQuery.getPageSize());
				pageList.setTotalItems(queryList.getTotalItems());
				return fromObjectList(pageList, null, false);
		}
	
		
	//调度的随机查询
	@SuppressWarnings("rawtypes")
	public List getELock(int m){
		return warehouseDao.getList(m);
	}
	/**
	 * 根据关锁号查询记录
	 * @param elockNumber
	 * @return
	 */
	public LsWarehouseElockBO findByElockNumber(String elockNumber){
		return warehouseDao.findByProperty("elockNumber", elockNumber);
	}
}
