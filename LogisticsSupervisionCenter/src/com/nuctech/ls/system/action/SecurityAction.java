package com.nuctech.ls.system.action;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;

import com.nuctech.ls.center.utils.LogoutType;
import com.nuctech.ls.center.utils.UserOnlineListener;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemFunctionsBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.system.LsSystemUserLogBO;
import com.nuctech.ls.model.bo.system.LsSystemUserRoleBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemRoleService;
import com.nuctech.ls.model.service.SystemUserLogService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.ConstantConfig;
import com.nuctech.util.Encrypter;
import com.nuctech.util.LoginSystem;
import com.nuctech.util.NuctechUtil;
import com.nuctech.util.SystemUtil;

/**
 * 作者： 徐楠
 *
 * 描述：<p>系统登录 登出 action</p>
 * 创建时间：2016年5月17日
 */
@Namespace("/security")
public class SecurityAction extends LSBaseAction {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = -4116126983793334468L;
	
	private static final Logger logger = Logger.getLogger(SecurityAction.class
            .getName());
	
	/**
     * 用户对象
     */
    private LsSystemUserBO user;
   
    @Resource
    private SystemUserService systemUserService;
    @Resource
    private SystemUserLogService systemUserLogService;
    @Resource
    private SystemRoleService systemRoleService;
    @Resource
    private SystemDepartmentService systemDepartmentService;
    
	
    /**
     * 系统登录
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "login", results = {
			@Result(name = "success", location = "/index.jsp", type = "redirect"),
			@Result(name = "input", location = "/login.jsp") })
	public String login() throws Exception {
    	try {
			HttpSession session = request.getSession();
			SessionUser sessionUser = new SessionUser();
			LsSystemUserBO loginUser = null;
			String userAccount = user.getUserAccount().toLowerCase();
			String userPassword = user.getUserPassword().toLowerCase();
			if (NuctechUtil.isNull(userAccount) || NuctechUtil.isNull(userPassword)) {
			    message = getText("login.inputNull");
			    return INPUT;
			}
			loginUser = systemUserService.findSystemUserByUserAccount(userAccount);
			if (NuctechUtil.isNull(loginUser)) {
				message = getText("login.inputError");
				logger.info(String.format("登录账号不存在，账号：%s", userAccount));
				return INPUT;
			}
			Encrypter en = Encrypter.getInstance(Encrypter.Algorithms.MD5);
			if (!en.encrypt(userPassword).equals(loginUser.getUserPassword())) {
			    message = getText("login.inputError");
			    logger.info(String.format("登录密码不正确，账号：%s", userAccount));
			    return INPUT;
			}
			String inValidUserFlag = ConstantConfig.readValue(Constant.INVALID_FLAG);
			if(loginUser.getIsEnable().equals(inValidUserFlag)) {
				message = getText("login.user.forbidden");
			    logger.info(String.format("登录账户禁用，账号：%s", userAccount));
				return INPUT;
			}
			//更新登录用户相关信息
			loginUser.setLogonTime(new Date());
			loginUser.setLogonSystem(LoginSystem.TRACKING);
			loginUser.setIpAddress(SystemUtil.getIpAddr(ServletActionContext.getRequest()));
			loginUser.setToken(UUID.randomUUID().toString());
			systemUserService.modify(loginUser);
			BeanUtils.copyProperties(loginUser, sessionUser);
			//保存 登录日志详细信息
			LsSystemUserLogBO systemUserLog = new LsSystemUserLogBO();
			systemUserLog.setLogUser(loginUser.getUserId());
			systemUserLog.setLogonSystem(LoginSystem.TRACKING);
			systemUserLog.setLogonTime(new Date());
			systemUserLog.setIpAddress(SystemUtil.getIpAddr(ServletActionContext.getRequest()));
			systemUserLog.setUserLogId(generatePrimaryKey());
			systemUserLogService.save(systemUserLog);
			session.setAttribute(Constant.USER_LOG_ID, systemUserLog.getUserLogId());
			
			//查询用户对应的角色信息
			LsSystemUserRoleBO systemUserRole = systemRoleService.findSystemUserRole(loginUser.getUserId());
			if(NuctechUtil.isNotNull(systemUserRole)) {
				String roleId = systemUserRole.getRoleId();
				//根据角色ID查询菜单
				List<LsSystemFunctionsBO> systemFunctionList = systemRoleService.findRoleSystemFunctionsList(roleId);
				sessionUser.setRoleId(roleId);
				sessionUser.setSystemFunctionList(systemFunctionList);
			}
			String organizationId = systemDepartmentService.findDepartmentIdByUserId(loginUser.getUserId());
			sessionUser.setOrganizationId(organizationId);
			UserOnlineListener.isAlreadyEnter(request.getSession(), loginUser.getUserAccount());
			session.setAttribute(Constant.SESSION_USER, sessionUser);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
    
    /**
     * 退出系统
     * */
    @Action(value = "/exitSystem", results = { @Result(location = "/login.jsp") })
    public String exitSystem() {
    	SessionUser sessionUser = (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
        if (sessionUser != null) {
        	//修改登录日志
        	String onlineUserLogId = (String)request.getSession().getAttribute(Constant.USER_LOG_ID);
        	LsSystemUserLogBO systemUserLog = systemUserLogService.findById(onlineUserLogId);
        	systemUserLog.setLogoutTime(new Date());
        	systemUserLog.setLogoutType(LogoutType.NORMAL.getValue());
        	systemUserLogService.modify(systemUserLog);
        	logger.info(String.format("退出系统%s", sessionUser.getUserAccount()));
        	sessionUser = null;
        }
        return SUCCESS;
    }


	public LsSystemUserBO getUser() {
		return user;
	}


	public void setUser(LsSystemUserBO user) {
		this.user = user;
	}

}
