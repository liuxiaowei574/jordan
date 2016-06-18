package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDispatchDetailBO;
import com.nuctech.ls.model.dao.DispatchDetailDao;

/**
 * 作者： 赵苏阳
 *
 * 描述：<p>设备调度的明细即“保存”相关信息 Service</p>
 * 创建时间：2016年6月13日
 */

@Service
@Transactional
public class DispatchDetailService extends LSBaseService {
	
	@Resource
	private DispatchDetailDao dispatchDetailDao;
	
	/**
	 * 保存调度明细的信息
	 * @param warehouseElock
	 */
	
	public void saveDetail(LsWarehouseDispatchDetailBO dispatchDetail){
		dispatchDetailDao.save(dispatchDetail);
	}
	
	/**
	 *根据调度详细表的调度Id，查询关锁的信息
	 * @param viewDispatchIds
	 * @return
	 */
	public List  getElockDetailList(String viewDispatchIds){
		return dispatchDetailDao.getElockDispatchDetailBODetail(viewDispatchIds);
	}
	
	/**
	 * 根据调度详细表的调度Id,查询子锁的信息
	 * @param viewDispatchIds
	 * @return
	 */
	public List getEsealDetaillist(String viewDispatchIds){
		return dispatchDetailDao.getEsealDispatchDetailBODetail(viewDispatchIds);
	}
	/**
	 * 根据调度详细表的调度Id,查询传感器的信息
	 * @param viewDispatchIds
	 * @return
	 */
	public List getSensorDetaillist(String viewDispatchIds){
		return dispatchDetailDao.getSensorDispatchDetailBODetail(viewDispatchIds);
	}
	
	
}
