package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.system.LsSystemParamsBO;
import com.nuctech.ls.model.dao.SystemParamsDao;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>系统参数 Service</p>
 * 创建时间：2016年6月2日
 */
@Service
@Transactional
public class SystemParamsService extends LSBaseService {
	
	/**
	 * 
	 */
	@Resource
	private SystemParamsDao systemParamsDao;

	/**
	 * @return
	 */
	public List<LsSystemParamsBO> findAllList() {
		return systemParamsDao.findAll();
	}
	
	
	/**
	 * 通过参数的Key值获取Value
	 * 
	 * @param paramCode
	 * @return
	 */
	public String findSystemParamsValueByKey(String paramCode) {
		return systemParamsDao.findSystemParamsValueByKey(paramCode);
	}
}
