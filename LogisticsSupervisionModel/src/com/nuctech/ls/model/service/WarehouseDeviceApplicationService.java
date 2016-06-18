package com.nuctech.ls.model.service;

import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemNoticesBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceApplicationBO;
import com.nuctech.ls.model.dao.WarehouseDeviceApplicationDao;
import com.nuctech.util.Constant;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>调度申请 Service</p>
 * 创建时间：2016年6月4日
 */
@Service
@Transactional
public class WarehouseDeviceApplicationService extends LSBaseService {
	
	@Resource
	private WarehouseDeviceApplicationDao warehouseDeviceApplicationDao;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject findDeviceApplicationList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		String queryString = "select t from LsWarehouseDeviceApplicationBO t where 1=1 and t.applyStatus = "+ Constant.DEVICE_ALREADY_APPLIED
				+ "/~ and t.applicationId = '[applicationId]' ~/"
				+ "/~ and t.applcationPortName like '%[applcationPortName]%' ~/"
				+ "/~ order by [sortColumns] ~/";
		PageList<LsWarehouseDeviceApplicationBO> pageList = warehouseDeviceApplicationDao.pageQuery(queryString, pageQuery);
		return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
	}
	
	public void save(LsWarehouseDeviceApplicationBO warehouseDeviceApplication) {
		warehouseDeviceApplicationDao.save(warehouseDeviceApplication);
	}
	
	/**
	 * 根据ID查询申请记录
	 * 
	 * @param id
	 * @return
	 */
	public LsWarehouseDeviceApplicationBO findByID(String id) {
		return warehouseDeviceApplicationDao.findById(id);
	}
	
	/**
	 * 修改实体
	 * 
	 * @param warehouseDeviceApplication
	 */
	public void modify(LsWarehouseDeviceApplicationBO warehouseDeviceApplication) {
		warehouseDeviceApplicationDao.update(warehouseDeviceApplication);
	}
}
