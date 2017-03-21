package com.nuctech.ls.system.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.common.page.JsonDateValueProcessor;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.system.LsSystemFunctionsBO;
import com.nuctech.ls.model.bo.system.LsSystemNoticesBO;
import com.nuctech.ls.model.bo.system.LsSystemRoleBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.system.LsSystemUserLogBO;
import com.nuctech.ls.model.bo.system.LsSystemUserRoleBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemNoticeLogService;
import com.nuctech.ls.model.service.SystemNoticeService;
import com.nuctech.ls.model.service.SystemRoleService;
import com.nuctech.ls.model.service.SystemTasksService;
import com.nuctech.ls.model.service.SystemUserLogService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.ConstantConfig;
import com.nuctech.util.Encrypter;
import com.nuctech.util.LoginSystem;
import com.nuctech.util.NuctechUtil;
import com.nuctech.util.SystemUtil;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 系统登录 登出 action
 * </p>
 * 创建时间：2016年5月17日
 */
@Namespace("/security")
public class SecurityAction extends LSBaseAction {

    /**
    * 
    */
    private static final long serialVersionUID = -4116126983793334468L;

    private static final Logger logger = Logger.getLogger(SecurityAction.class.getName());

    /**
     * 用户对象
     */
    private LsSystemUserBO user;

    private List<LsSystemNoticesBO> noticeList;

    @Resource
    private SystemUserService systemUserService;
    @Resource
    private SystemUserLogService systemUserLogService;
    @Resource
    private SystemRoleService systemRoleService;
    @Resource
    private SystemDepartmentService systemDepartmentService;
    @Resource
    private SystemNoticeService systemNoticeService;
    // 查询用户未读取通知的数量
    private int needDealNoticeCount;
    // 查询用户的待办任务的数量
    private int needDealMissionCount;
    @Resource
    private SystemNoticeLogService systemNoticeLogService;
    @Resource
    private SystemTasksService systemTasksService;

    /**
     * 系统登录
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "login",
            results = { @Result(name = "success", location = "/index.jsp", type = "redirect"),
                    @Result(name = "input", location = "/login.jsp"),
                    @Result(name = "patrolIndex", location = "/patrol_index.jsp", type = "redirect"),
                    @Result(name = "adminIndex", location = "/system/online/list.jsp", type = "redirect") })
    public String login() throws Exception {
        try {
            if (NuctechUtil.isNull(user) || NuctechUtil.isNull(user.getUserAccount())
                    || NuctechUtil.isNull(user.getUserPassword())) {
                message = getLocaleString("login.inputNull");
                return INPUT;
            }
            HttpSession session = request.getSession();
            SessionUser sessionUser = new SessionUser();
            LsSystemUserBO loginUser = null;
//            String userAccount = user.getUserAccount().toLowerCase();
//            String userPassword = user.getUserPassword().toLowerCase();
            String userAccount = user.getUserAccount();
            String userPassword = user.getUserPassword();
            loginUser = systemUserService.findSystemUserByUserAccount(userAccount);
            if (NuctechUtil.isNull(loginUser) || !loginUser.getUserAccount().equals(userAccount)) {
                message = getLocaleString("login.inputError");
                logger.info(String.format("登录账号不存在，账号：%s", userAccount));
                return INPUT;
            }
            Encrypter en = Encrypter.getInstance(Encrypter.Algorithms.MD5);
            if (!en.encrypt(userPassword).equals(loginUser.getUserPassword())) {
                message = getLocaleString("login.inputError");
                logger.info(String.format("登录密码不正确，账号：%s", userAccount));
                return INPUT;
            }
            String inValidUserFlag = ConstantConfig.readValue(Constant.INVALID_FLAG);
            if (loginUser.getIsEnable().equals(inValidUserFlag)) {
                message = getLocaleString("login.user.forbidden");
                logger.info(String.format("登录账户禁用，账号：%s", userAccount));
                return INPUT;
            }

            // 更新登录用户相关信息
            loginUser.setLogonTime(new Date());
            loginUser.setLogoutTime(null);
            loginUser.setLogonSystem(LoginSystem.TRACKING);
            loginUser.setIpAddress(SystemUtil.getIpAddr(ServletActionContext.getRequest()));
            loginUser.setToken(UUID.randomUUID().toString());
            loginUser.setLogLocation(user.getLogLocation());
            systemUserService.modify(loginUser);
            BeanUtils.copyProperties(loginUser, sessionUser);
            // 保存 登录日志详细信息
            LsSystemUserLogBO systemUserLog = new LsSystemUserLogBO();
            systemUserLog.setLogUser(loginUser.getUserId());
            systemUserLog.setLogonSystem(LoginSystem.TRACKING);
            systemUserLog.setLogonTime(new Date());
            systemUserLog.setIpAddress(SystemUtil.getIpAddr(ServletActionContext.getRequest()));
            systemUserLog.setUserLogId(generatePrimaryKey());
            systemUserLogService.save(systemUserLog);
            session.setAttribute(Constant.USER_LOG_ID, systemUserLog.getUserLogId());

            // 查询用户对应的角色信息
            LsSystemUserRoleBO systemUserRole = systemRoleService.findSystemUserRole(loginUser.getUserId());
            if (NuctechUtil.isNotNull(systemUserRole)) {
                String roleId = systemUserRole.getRoleId();
                // 巡逻队模块不存在时，不允许相关角色登录
                if (!systemModules.isPatrolOn() && systemRoleService.isPatrolRole(roleId)) {
                    message = getLocaleString("login.user.patrolModule.off");
                    logger.info(String.format("巡逻队模块未开启，禁止登录。账号：%s", userAccount));
                    return INPUT;
                }
                // 风险分析模块不存在时，不允许相关角色登录
                if (!systemModules.isRiskOn() && systemRoleService.isRiskRole(roleId)) {
                    message = getLocaleString("login.user.riskModule.off");
                    logger.info(String.format("风险分析模块未开启，禁止登录。账号：%s", userAccount));
                    return INPUT;
                }
                // 审批模块不存在时，不允许多余中心角色登录
                List<String> controlRoles = Arrays.asList(new String[] { "2", "3", "5" });
                if (!systemModules.isApprovalOn() && controlRoles.indexOf(roleId) > -1) {
                    message = getLocaleString("login.user.approvalModule.off");
                    logger.info(String.format("审批模块未开启，不允许多余中心用户登录。账号：%s", userAccount));
                    return INPUT;
                }
                // 根据角色ID查询菜单
                List<LsSystemFunctionsBO> systemFunctionList = systemRoleService.findRoleSystemFunctionsList(roleId);
                sessionUser.setRoleId(roleId);
                LsSystemRoleBO systemRole = systemRoleService.findById(sessionUser.getRoleId());
                sessionUser.setRoleName(systemRole.getRoleName());
                sessionUser.setSystemFunctionList(systemFunctionList);
            }
            String organizationId = systemDepartmentService.findDepartmentIdByUserId(loginUser.getUserId());
            LsSystemDepartmentBO depart = systemDepartmentService.findById(organizationId);
            sessionUser.setOrganizationId(organizationId);
            sessionUser.setOrganizationName(depart.getOrganizationName());
            UserOnlineListener.isAlreadyEnter(request.getSession(), loginUser.getUserAccount());
            session.setAttribute(Constant.SESSION_USER, sessionUser);
            session.setAttribute(Constant.WSGPSURL, wsGpsUrl);
            session.setAttribute(Constant.WSNOTICEURL, wsNoticeUrl);
            session.setAttribute(Constant.TRIP_PHOTO_PATH_HTTP, rootPathHttp + tripPhotoPath);
            session.setAttribute("systemModulesLqx", systemModules);
            // 系统功能模块
            session.setAttribute("systemModules", systemModules.toJSON());
            // 查询登录用户未读的通知数量,在LsSystemNoticeLogBO表中根据接收人id查出登陆用户未读取通知的数量；dealType=0代表未读取
            String userId = loginUser.getUserId();
            needDealNoticeCount = systemNoticeLogService.findCount(userId);
            session.setAttribute(Constant.needDealNoticeCount, needDealNoticeCount);
            // 查询未处理的任务数量
            needDealMissionCount = systemTasksService.findCount(userId);
            session.setAttribute(Constant.needDealMissionCount, needDealMissionCount);
            // 查询当前用户角色
            if (systemRoleService.isPatrol(sessionUser.getRoleName())) {
                // 巡逻队用户：查询通知列表
                noticeList = systemNoticeService.findSystemNoticesListByUserId(sessionUser.getUserId());
                return "patrolIndex";
            } else if (systemRoleService.isAdminRole(sessionUser.getRoleName())) {
                // 管理员用户
                return "adminIndex";
            } else {
                return SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    /**
     * sessionUser对象转成json串
     * 
     * @param sessionUser
     * @return
     */
    private String toJson(SessionUser sessionUser) {
        JsonConfig config = new JsonConfig();
        config.setIgnoreDefaultExcludes(false);
        config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor(Constant.JordanTimeFormat));

        JSONObject jsonObj = JSONObject.fromObject(sessionUser, config);
        return jsonObj.toString();
    }

    /**
     * 退出系统
     */
    @Action(value = "/exitSystem", results = { @Result(location = "/login.jsp", type = "redirect") })
    public String exitSystem() {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        if (sessionUser != null) {
            LsSystemUserBO onlineUser = systemUserService.findById(sessionUser.getUserId());
            if (onlineUser != null) {
                onlineUser.setLogoutTime(new Date());
                onlineUser.setToken(null);
                systemUserService.modify(onlineUser);
            }
            // 修改登录日志
            String onlineUserLogId = (String) request.getSession().getAttribute(Constant.USER_LOG_ID);
            LsSystemUserLogBO systemUserLog = systemUserLogService.findById(onlineUserLogId);
            if (systemUserLog != null) {
                systemUserLog.setLogoutTime(new Date());
                systemUserLog.setLogoutType(LogoutType.NORMAL.getValue());
                systemUserLogService.modify(systemUserLog);
                logger.info(String.format("退出系统%s", sessionUser.getUserAccount()));
            }
            request.getSession().removeAttribute(Constant.USER_LOG_ID);
            // request.getSession().invalidate();
            sessionUser = null;
        }
        return SUCCESS;
    }

    @Action(value = "userPasswordEditModal",
            results = { @Result(name = "success", location = "/system/userMgmt/user_password_edit.jsp") })
    public String userPasswordEditModal() {
        return SUCCESS;
    }

    @Action(value = "validUserPassword")
    public void validUserPassword() throws IOException {
        Encrypter encrypter = Encrypter.getInstance(Encrypter.Algorithms.MD5);
        pageQuery = this.newPageQuery("");
        Map<String, Object> filters = pageQuery.getFilters();
        String oldPassword = (String) filters.get("oldPassword");
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        LsSystemUserBO systemUser = systemUserService.findById(sessionUser.getUserId());
        String password = encrypter.encrypt(oldPassword);
        JSONObject jsonObject = new JSONObject();
        if (systemUser.getUserPassword().equals(password)) {
            jsonObject.put("valid", true);
        } else {
            jsonObject.put("valid", false);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
        out.flush();
        out.close();
    }

    @Action(value = "editUserPassword",
            results = { @Result(name = "success", type = "json"), @Result(name = "error", type = "json") })
    public String editUserPassword() {
        Encrypter encrypter = Encrypter.getInstance(Encrypter.Algorithms.MD5);
        pageQuery = this.newPageQuery("");
        Map<String, Object> filters = pageQuery.getFilters();
        String confirmPassword = (String) filters.get("confirmPassword");
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        LsSystemUserBO systemUser = systemUserService.findById(sessionUser.getUserId());
        systemUser.setUserPassword(encrypter.encrypt(confirmPassword));
        try {
            systemUserService.modify(systemUser);
            return SUCCESS;
        } catch (Exception e) {
            return ERROR;
        }
    }

    public LsSystemUserBO getUser() {
        return user;
    }

    public void setUser(LsSystemUserBO user) {
        this.user = user;
    }

    @Resource
    private String patrolModule;

    @Resource
    private String wsGpsUrl;

    @Resource
    private String wsNoticeUrl;

    @Resource
    private String rootPathHttp;

    @Resource
    private String tripPhotoPath;

    @Resource
    private SystemModules systemModules;

    public String getWsGpsUrl() {
        return wsGpsUrl;
    }

    public String getWsNoticeUrl() {
        return wsNoticeUrl;
    }

    public List<LsSystemNoticesBO> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<LsSystemNoticesBO> noticeList) {
        this.noticeList = noticeList;
    }

    public int getNeedDealNoticeCount() {
        return needDealNoticeCount;
    }

    public void setNeedDealNoticeCount(int needDealNoticeCount) {
        this.needDealNoticeCount = needDealNoticeCount;
    }

    public String getPatrolModule() {
        return patrolModule;
    }

    public void setPatrolModule(String patrolModule) {
        this.patrolModule = patrolModule;
    }

}
