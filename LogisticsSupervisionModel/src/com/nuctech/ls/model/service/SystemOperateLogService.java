package com.nuctech.ls.model.service;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.system.LsSystemOperateLogBO;
import com.nuctech.ls.model.dao.SystemOperateLogDao;
import com.nuctech.ls.model.util.OperateLogType;
import com.nuctech.util.SystemUtil;

/**
 * 作者： 徐楠
 *
 * 描述：<p>系统操作日志 Service</p>
 * 创建时间：2016年5月18日
 */
@Service
@Transactional
public class SystemOperateLogService extends LSBaseService {
	
	@Resource
	private SystemOperateLogDao systemOperateLogDao;

	/**
	 * 添加操作日志
	 * 
	 * @param log
	 * 		操作日志对象
	 */
	public void addLog(LsSystemOperateLogBO log) {
		systemOperateLogDao.save(log);
	}
	
	/**
	 * 添加操作类型 为1：处理类型日志
	 * 
	 * @param functionId
	 * 			功能编号
	 * @param operateUser
	 * 			操作人 
	 * @param operateClass
	 * 			操作类 
	 * @param transferData
	 * 			传输数据
	 */
	public void addLog(String content, String operateUser, String operateClass, String transferData) {
		LsSystemOperateLogBO systemOperateLog = new LsSystemOperateLogBO();
		systemOperateLog.setOperateId(UUID.randomUUID().toString());
		systemOperateLog.setIpAddress(SystemUtil.getIpAddr(ServletActionContext.getRequest()));
		systemOperateLog.setOperateUser(operateUser);
		systemOperateLog.setOperateDesc(content);
		systemOperateLog.setOperateClass(operateClass);
		systemOperateLog.setTransferData(transferData);
		systemOperateLog.setOperateTime(new Date());
		systemOperateLog.setOperateType(OperateLogType.HANDLE.getValue());
		systemOperateLogDao.save(systemOperateLog);
	}
}
