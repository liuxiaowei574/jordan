package com.nuctech.ls.system.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.catalina.tribes.util.Arrays;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.system.LsSystemOrganizationUserBO;
import com.nuctech.ls.model.bo.system.LsSystemRoleBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.system.LsSystemUserRoleBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.service.SystemRoleService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.util.OperateContentType;
import com.nuctech.ls.model.util.OperateEntityType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.ConstantConfig;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Namespace("/userMgmt")
public class UserAction extends LSBaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1389238299592275611L;
    private Logger logger = Logger.getLogger(UserAction.class);

    protected static final String DEFAULT_SORT_COLUMNS = "userAccount ASC";
    protected static final String DEFAULT_SORT_COLUMNS_ALIAS = "t.userAccount ASC";

    @Resource
    private SystemUserService systemUserService;
    @Resource
    private SystemOperateLogService logService;
    @Resource
    private SystemDepartmentService systemDepartmentService;
    @Resource
    private SystemRoleService systemRoleService;
    @Resource
    private SystemModules systemModules;

    private LsSystemUserBO systemUser;
    private List<LsSystemDepartmentBO> portList = new ArrayList<LsSystemDepartmentBO>();
    private List<LsSystemRoleBO> roleList = new ArrayList<LsSystemRoleBO>();
    private List<LsSystemRoleBO> centerRoleList = new ArrayList<LsSystemRoleBO>();
    private String[] ids; // userID字符串，多个之间逗号分隔
    private List<LsSystemDepartmentBO> deptList = new ArrayList<LsSystemDepartmentBO>();

    @Action(value = "toList", results = { @Result(name = "success", location = "/system/userMgmt/userList.jsp") })
    public String toList() {
        String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
        deptList = systemDepartmentService.findAllPortByUserId(userId);
        findRoleList();
        return SUCCESS;
    }

    @Action(value = "addModal", results = { @Result(name = "success", location = "/system/userMgmt/add.jsp"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String addModal() {
        systemUser = new LsSystemUserBO();
        // 查询所有角色信息
        findRoleList();
        // 查询所有口岸信息列表
        String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
        portList = systemDepartmentService.findAllPortByUserId(userId);
        return SUCCESS;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "editModal", results = { @Result(name = "success", location = "/system/userMgmt/edit.jsp"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String editModal() {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        if (systemUser != null) {
            String userId = systemUser.getUserId();
            if (!NuctechUtil.isNull(userId)) {
                Map<String, Object> filter = pageQuery.getFilters();
                systemUser = systemUserService.findById(userId);
                // 查询所有角色信息
                findRoleList();
                // 查询所有口岸信息列表
                String curUserId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
                portList = systemDepartmentService.findAllPortByUserId(curUserId);
                // 查询用户口岸
                LsSystemOrganizationUserBO systemOrganizationUser = systemUserService
                        .findOrganizationIdByUserId(systemUser.getUserId());
                if (systemOrganizationUser != null) {
                    String userPort = systemOrganizationUser.getOrganizationId();
                    filter.put("userPort", userPort);
                }
                LsSystemUserRoleBO systemUserRole = systemUserService.findRoleIdByUserId(systemUser.getUserId());
                if (systemUserRole != null) {
                    String userRole = systemUserRole.getRoleId();
                    filter.put("userRole", userRole);
                }
                return SUCCESS;
            } else {
                message = "Find Object Mis!";
                return ERROR;
            }
        } else {
            return ERROR;
        }
    }

    /**
     * 查找角色列表。如果报警推送模块开启，查找所有角色；否则查找最基本角色
     */
    private void findRoleList() {
        if (systemModules.isAlarmPushOn()) {
            roleList = systemRoleService.findAllRoles();
            centerRoleList = systemRoleService.findAllControlRoles();
        } else {
            roleList = systemRoleService.findBasicRoles();
            centerRoleList = systemRoleService.findBasicControlRoles();
        }
    }

    @SuppressWarnings("unchecked")
    @Action(value = "list")
    public void list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_ALIAS);
        String sortname = request.getParameter("sort");
        if ("organizationName".equals(sortname)) {
            pageQuery = this.newPageQuery("d.organizationName asc");
        } else if ("roleName".equals(sortname)) {
            pageQuery = this.newPageQuery("r.roleName asc");
        }
        JSONObject retJson = systemUserService.fromUserRoleDepartList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    @Action(value = "addUser", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String addUser() throws Exception {
        if (systemUser != null) {
            String status = ConstantConfig.readValue(Constant.VALID_FLAG);
            systemUser.setIsEnable(status);
            systemUserService.save(systemUser);
            //
            pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
            Map<String, Object> filter = pageQuery.getFilters();
            String userPort = (String) filter.get("userPort");
            LsSystemOrganizationUserBO systemOrganizationUser = new LsSystemOrganizationUserBO();
            systemOrganizationUser.setOrgUserId(generatePrimaryKey());
            systemOrganizationUser.setOrganizationId(userPort);
            systemOrganizationUser.setUserId(systemUser.getUserId());
            systemUserService.saveUserOrganization(systemOrganizationUser);
            String userRole = (String) filter.get("userRole");
            LsSystemUserRoleBO systemUserRole = new LsSystemUserRoleBO();
            systemUserRole.setRoleId(userRole);
            systemUserRole.setUserId(systemUser.getUserId());
            systemUserRole.setUserRoleId(generatePrimaryKey());
            systemUserService.saveUserRole(systemUserRole);
            userLog(OperateContentType.ADD.toString(), OperateEntityType.USER.toString(), systemUser.toString());
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    @Action(value = "editUser", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editUser() throws Exception {
        if (systemUser != null) {
            LsSystemUserBO systemUserBO = updateUserInfo();
            if (NuctechUtil.isNotNull(systemUserBO)) {
                systemUserService.modify(systemUserBO);
            }
            pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
            Map<String, Object> filter = pageQuery.getFilters();
            String userPort = (String) filter.get("userPort");
            LsSystemOrganizationUserBO systemOrganizationUser = systemUserService
                    .findOrganizationIdByUserId(systemUser.getUserId());
            systemOrganizationUser.setOrganizationId(userPort);
            systemUserService.modifyUserOrganization(systemOrganizationUser);

            LsSystemUserRoleBO systemUserRole = systemUserService.findRoleIdByUserId(systemUser.getUserId());
            String userRole = (String) filter.get("userRole");
            systemUserRole.setRoleId(userRole);
            systemUserService.modifyUserRole(systemUserRole);
            userLog(OperateContentType.EDIT.toString(), OperateEntityType.USER.toString(), systemUser.toString());
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    private LsSystemUserBO updateUserInfo() {
        LsSystemUserBO systemUserBO = systemUserService.findById(systemUser.getUserId());
        if (NuctechUtil.isNotNull(systemUserBO)) {
            systemUserBO.setUserAccount(systemUser.getUserAccount());
            systemUserBO.setUserName(systemUser.getUserName());
            systemUserBO.setUserPhone(systemUser.getUserPhone());
            systemUserBO.setUserEmail(systemUser.getUserEmail());
            systemUserBO.setUserAddress(systemUser.getUserAddress());
            systemUserBO.setLevel(systemUser.getLevel());
            systemUserBO.setPosition(systemUser.getPosition());
            systemUserBO.setCanDealAlarm(systemUser.getCanDealAlarm());
        }
        return systemUserBO;
    }

    @Action(value = "enableUserByIds",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String enableUserByIds() {
        if (NuctechUtil.isNull(ids)) {
            message = "启用失败,参数[ids]为空";
            logger.error("启用失败,参数[ids]为空");
            return ERROR;
        }
        try {
            String[] idArr = ids[0].split(",");
            String validFlag = ConstantConfig.readValue(Constant.VALID_FLAG);
            systemUserService.batchLockOrUnlockUser(validFlag, idArr);
            userLog(OperateContentType.ENABLE.toString(), OperateEntityType.USER.toString(), Arrays.toString(ids));
        } catch (Exception e) {
            message = e.getMessage();
            logger.error("启用用户异常", e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Action(value = "disableUserByIds",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String disableUserByIds() {
        if (NuctechUtil.isNull(ids)) {
            message = "禁用失败,参数[ids]为空";
            logger.error("禁用失败,参数[ids]为空");
            return ERROR;
        }
        try {
            String[] idArr = ids[0].split(",");
            String invalidFlag = ConstantConfig.readValue(Constant.INVALID_FLAG);
            systemUserService.batchLockOrUnlockUser(invalidFlag, idArr);
            userLog(OperateContentType.DISABLE.toString(), OperateEntityType.USER.toString(), ids.toString());
        } catch (Exception e) {
            message = e.getMessage();
            logger.error("禁用用户异常", e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Action(value = "resetUserPasswordByIds",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String resetUserPasswordByIds() {
        if (NuctechUtil.isNull(ids)) {
            message = "重置账号密码失败,参数[ids]为空";
            logger.error("重置账号密码失败,参数[ids]为空");
            return ERROR;
        }
        try {
            systemUserService.resetUserPasswordByIds(ids);
            userLog(OperateContentType.RESET.toString(), OperateEntityType.PASSWORD.toString(), ids.toString());
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
     * @param
     */
    private void userLog(String operate, String entity, String params) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        logService.addLog(operate, entity, sessionUser.getUserId(), UserAction.class.toString(), params);
    }

    /**
     * 根据Id查询用户信息
     * 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Action(value = "getUserById")
    public String getUserById() throws Exception {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_ALIAS);
        JSONObject retJson = systemUserService.fromSessionUserObjectList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
        return null;
    }

    /**
     * 显示控制中心用户的人员
     */
    @Action(value = "dlist", results = { @Result(name = "success", type = "json") })
    public void dlist() throws IOException {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String userID = sessionUser.getUserId();
        List<SessionUser> controlRoomUserList = systemUserService.getControlRoomUserslist(userID);

        JSONArray retJson = JSONArray.fromObject(controlRoomUserList);
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
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

    public List<LsSystemDepartmentBO> getPortList() {
        return portList;
    }

    public void setPortList(List<LsSystemDepartmentBO> portList) {
        this.portList = portList;
    }

    public List<LsSystemRoleBO> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<LsSystemRoleBO> roleList) {
        this.roleList = roleList;
    }

    public List<LsSystemDepartmentBO> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<LsSystemDepartmentBO> deptList) {
        this.deptList = deptList;
    }

    public List<LsSystemRoleBO> getCenterRoleList() {
        return centerRoleList;
    }

    public void setCenterRoleList(List<LsSystemRoleBO> centerRoleList) {
        this.centerRoleList = centerRoleList;
    }

}
