package com.nuctech.ls.system.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.ConstantConfig;
import com.nuctech.util.NuctechUtil;

@Namespace("/userMgmt")
public class UserAction extends LSBaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1389238299592275611L;
	private Logger logger = Logger.getLogger(UserAction.class);

	protected static final String DEFAULT_SORT_COLUMNS = "userId ASC";
	
	@Resource
	private SystemUserService systemUserService;
	@Resource
	private SystemOperateLogService logService;
	
	private LsSystemUserBO systemUser;
	private String[] ids; //userID字符串，多个之间逗号分隔
 
	
	@Action(value = "toList", results = {@Result(name = "success", location = "/system/userMgmt/userList.jsp")})
	public String toList() {
		return SUCCESS;
	}
	
	@Action(value = "addModal", results = {
			@Result(name = "success", location = "/system/userMgmt/add.jsp"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String addModal() {
		systemUser = new LsSystemUserBO();
		return SUCCESS;
	}
	
	@Action(value = "editModal", results = {
			@Result(name = "success", location = "/system/userMgmt/edit.jsp"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String editModal() {
		if(systemUser != null) {
			String userId = systemUser.getUserId();
			if(!NuctechUtil.isNull(userId)) {
				systemUser = systemUserService.findById(userId);
				return SUCCESS;
			} else {
				message = "Find Object Mis!";
				return ERROR;
			}
		} else {
			return ERROR;
		}
	}
	
	@Action(value="list")
	public void list() throws IOException{
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);		
		JSONObject retJson = systemUserService.fromObjectList(pageQuery,null,false);		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");		
		PrintWriter out = response.getWriter(); 
		out.print(retJson.toString());
		out.flush();
		out.close();
	}
	
	@Action(value = "addUser", results = {
			@Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "error" }) })
	public String addUser() throws Exception {
		if(systemUser != null) {
			String status = ConstantConfig.readValue(Constant.VALID_FLAG);
			systemUser.setIsEnable(status);
			systemUserService.save(systemUser);
			userLog("add user", systemUser.toString());
            return SUCCESS;
		} else {
            return ERROR;
		}
	}
	
	@Action(value = "editUser", results = {
			@Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "error" }) })
	public String editUser() throws Exception {
		if(systemUser != null) {
			systemUserService.modify(systemUser);
			userLog("edit user", systemUser.toString());
            return SUCCESS;
		} else {
            return ERROR;
		}
	}

	@Action(value = "enableUserByIds", results = {
			@Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String enableUserByIds() {
		if (NuctechUtil.isNull(ids)) {
			message = "启用失败,参数[ids]为空";
			logger.error("启用失败,参数[ids]为空");
			return ERROR;
		}
		try {
			String validFlag = ConstantConfig.readValue(Constant.VALID_FLAG);
			systemUserService.batchLockOrUnlockUser(validFlag, ids);
			userLog("enable user", ids.toString());
		} catch (Exception e) {
			message = e.getMessage();
			logger.error("启用用户异常", e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	@Action(value = "disableUserByIds", results = {
			@Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String disableUserByIds() {
		if (NuctechUtil.isNull(ids)) {
			message = "禁用失败,参数[ids]为空";
			logger.error("禁用失败,参数[ids]为空");
			return ERROR;
		}
		try {
			String invalidFlag = ConstantConfig.readValue(Constant.INVALID_FLAG);
			systemUserService.batchLockOrUnlockUser(invalidFlag, ids);
			userLog("disable user", ids.toString());
		} catch (Exception e) {
			message = e.getMessage();
			logger.error("禁用用户异常", e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	@Action(value = "resetUserPasswordByIds", results = {
			@Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String resetUserPasswordByIds() {
		if (NuctechUtil.isNull(ids)) {
			message = "重置账号密码失败,参数[ids]为空";
			logger.error("重置账号密码失败,参数[ids]为空");
			return ERROR;
		}
		try {
			systemUserService.resetUserPasswordByIds(ids);
			userLog("reset user password", ids.toString());
		} catch (Exception e) {
			message = e.getMessage();
			logger.error("重置账号密码异常", e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 日志记录方法
	 * 
	 * @param content
	 */
	private void userLog(String content, String params) {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
		logService.addLog(content, sessionUser.getUserId(), UserAction.class.toString(), params);
	}
	public LsSystemUserBO getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(LsSystemUserBO systemUser) {
		this.systemUser = systemUser;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	
}
