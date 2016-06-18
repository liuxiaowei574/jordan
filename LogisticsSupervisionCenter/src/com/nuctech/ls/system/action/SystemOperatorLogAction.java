package com.nuctech.ls.system.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemUserLogBO;
import com.nuctech.ls.model.service.SystemUserLogService;

/**
 * 
 * @author 徐楠
 * 
 * 描述：<p>系统操作日志信息</p>
 * 创建时间：2016年5月26日
 */
public class SystemOperatorLogAction extends LSBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7406768552424614197L;
	private Logger logger = Logger.getLogger(SystemOperatorLogAction.class);
	
	/**
	 * 日志对象
	 */
	private LsSystemUserLogBO userLog;
	@Resource
	private SystemUserLogService userLogService;
	
	@Action(value = "toList", results = {@Result(name = "success", location = "/system/log/list.jsp")})
	public String toList() {
		return SUCCESS;
	}
	
	public LsSystemUserLogBO getUserLog() {
		return userLog;
	}
	public void setUserLog(LsSystemUserLogBO userLog) {
		this.userLog = userLog;
	}
	

}
