package com.nuctech.ls.model.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.system.LsSystemUserLogBO;
import com.nuctech.ls.model.dao.SystemUserLogDao;

/**
 * 作者： 徐楠
 *
 * 描述：<p>用户登录详细日志信息 Service</p>
 * 创建时间：2016年5月17日
 */
@Service
@Transactional
public class SystemUserLogService extends LSBaseService {
	
	@Resource
	private SystemUserLogDao systemUserLogDao;

	/**
	 * 保存用户详细日志信息
	 * 
	 * @param systemUserLog
	 */
	public void save(LsSystemUserLogBO systemUserLog) {
		systemUserLogDao.save(systemUserLog);
	}
	
	/**
	 * 修改用户日志信息
	 * 
	 * @param systemUserLog
	 */
	public void modify(LsSystemUserLogBO systemUserLog) {
		systemUserLogDao.update(systemUserLog);
	}
	
	public LsSystemUserLogBO findById(String id) {
		return systemUserLogDao.findById(id);
	}
}
