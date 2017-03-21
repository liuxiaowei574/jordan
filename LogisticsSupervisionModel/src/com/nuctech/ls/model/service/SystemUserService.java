package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemOrganizationUserBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.system.LsSystemUserRoleBO;
import com.nuctech.ls.model.dao.SystemOrganizationUserDao;
import com.nuctech.ls.model.dao.SystemUserDao;
import com.nuctech.ls.model.dao.SystemUserRoleDao;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.Encrypter;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 系统用户相关 Service
 * 
 * @author xunan
 * @2016年5月17日
 *
 */
@Service
@Transactional
public class SystemUserService extends LSBaseService {

    @Resource
    private SystemUserDao systemUserDao;

    @Resource
    private SystemUserRoleDao systemUserRoleDao;
    @Resource
    private SystemOrganizationUserDao systemOrganizationUserDao;
    @Resource
    private SystemModules systemModules;

    /**
     * 系统登录
     * 
     * @param loginName
     *        登录账号
     * @param password
     *        密码
     * @return
     * @throws Exception
     */
    public LsSystemUserBO login(String loginName, String password) throws Exception {
        return systemUserDao.login(loginName, password);
    }

    /**
     * 修改用户
     * 
     * @param systemUser
     */
    public void modify(LsSystemUserBO systemUser) {
        systemUserDao.merge(systemUser);
    }

    /**
     * 通过用户账号查询用户信息
     * 
     * @param userAccount
     *        用户账号
     * @return
     */
    public LsSystemUserBO findSystemUserByUserAccount(String userAccount) {
        return systemUserDao.findSystemUserByUserAccount(userAccount);
    }

    /**
     * 根据ID查询用户
     * 
     * @param id
     *        用户主键ID
     * @return
     */
    public LsSystemUserBO findById(String id) {
        return systemUserDao.findById(id);
    }

    /**
     * 用户账号是否存在
     * 
     * @param userAccount
     *        用户账号
     * @return
     */
    public boolean isUserAccountExist(String userAccount) {
        LsSystemUserBO systemUser = systemUserDao.findSystemUserByUserAccount(userAccount);
        if (systemUser == null) {
            return false;
        } else {
            return true;
        }
    }

    public void save(LsSystemUserBO systemUser) {
        Encrypter encrypter = Encrypter.getInstance(Encrypter.Algorithms.MD5);
        systemUser.setUserPassword(encrypter.encrypt(LsSystemUserBO.DEFAULT_PASSWORD));
        systemUserDao.save(systemUser);
    }

    @SuppressWarnings("rawtypes")
    public PageList findUsers(PageQuery<Map> pageQuery) {
        String queryString = "select t from LsSystemUserBO t where 1=1 " + "/~ and t.userId = '[userId]' ~/"
                + "/~ and t.userAccount like '%[userAccount]%' ~/" + "/~ and t.userName like '%[userName]%' ~/"
                + "/~ and t.userPhone like '%[userPhone]%' ~/" + "/~ and t.userEmail like '%[userEmail]%' ~/"
                + "/~ and t.level like '%[level]%' ~/" + "/~ and t.position like '%[position]%' ~/"
                + "/~ order by [sortColumns] ~/";

        return systemUserDao.pageQuery(queryString, pageQuery);
    }

    @SuppressWarnings("rawtypes")
    public PageList findPatrolUsers(PageQuery<Map> pageQuery) {
        String queryString = "select user from LsSystemUserBO user,LsCommonPatrolBO patrol"
                + "  where user.userId=patrol.potralUser " + "/~ and user.userId = '[userId]' ~/"
                + "/~ and user.userAccount like '%[userAccount]%' ~/" + "/~ and user.userName like '%[userName]%' ~/"
                + "/~ and user.userPhone like '%[userPhone]%' ~/" + "/~ and user.userEmail like '%[userEmail]%' ~/"
                + "/~ and user.level like '%[level]%' ~/" + "/~ and user.position like '%[position]%' ~/"
                + "/~ order by [sortColumns] ~/";

        return systemUserDao.pageQuery(queryString, pageQuery);
    }

    @SuppressWarnings("rawtypes")
    public PageList findSessionUser(PageQuery<Map> pageQuery) {
        String queryString = "select t,r from LsSystemUserBO t, LsSystemRoleBO r, LsSystemUserRoleBO ur where 1=1 "
                + " and t.userId = ur.userId and r.roleId = ur.roleId " + "/~ and t.userId = '[userId]' ~/"
                + "/~ and t.userAccount like '%[userAccount]%' ~/" + "/~ and t.userName like '%[userName]%' ~/"
                + "/~ and t.userPhone like '%[userPhone]%' ~/" + "/~ and t.userEmail like '%[userEmail]%' ~/"
                + "/~ and t.level like '%[level]%' ~/" + "/~ and t.position like '%[position]%' ~/"
                + "/~ order by [sortColumns] ~/";

        return systemUserDao.pageQuery(queryString, pageQuery);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        PageList<LsSystemUserBO> pageList = findUsers(pageQuery);
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject fromSessionUserObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) {
        PageList<Object> queryList = findSessionUser(pageQuery);
        PageList<SessionUser> pageList = new PageList<SessionUser>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                SessionUser sessionuser = new SessionUser();
                BeanUtils.copyProperties(objs[0], sessionuser);
                BeanUtils.copyProperties(objs[1], sessionuser);
                pageList.add(sessionuser);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 
     * @param b
     * @param ids
     */
    public int batchLockOrUnlockUser(String enable, String[] ids) {
        String hql = "update LsSystemUserBO set isEnable=:enable where userId in :ids";
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("enable", enable);
        propertiesMap.put("ids", ids);
        int count = systemUserDao.batchUpdateOrDeleteByHql(hql, propertiesMap);
        return count;
    }

    /**
     * 重置用户账号密码
     * 
     * @param ids
     *        账号ID数组
     */
    public void resetUserPasswordByIds(String[] ids) {
        String[] userIdArr = ids[0].split(",");
        Encrypter encrypter = Encrypter.getInstance(Encrypter.Algorithms.MD5);
        if (ids.length > 0) {
            for (String id : userIdArr) {
                LsSystemUserBO user = systemUserDao.findById(id);
                user.setUserPassword(encrypter.encrypt(LsSystemUserBO.DEFAULT_PASSWORD));
                systemUserDao.update(user);
            }
        }
        // HashMap<String, Object> propertiesMap = new HashMap<String,
        // Object>();
        // propertiesMap.put("userPassword",
        // encrypter.encrypt(LsSystemUserBO.DEFAULT_PASSWORD));
        // propertiesMap.put("ids", ids);
        // int count = systemUserDao.batchUpdateOrDeleteByHql(hql,
        // propertiesMap);
        // return count;
    }

    /**
     * 查找quelityCenter用户
     * 
     * @return
     */
    public List<String> getQualityCenterUsers() {
        List<String> users = new ArrayList<>();
        List<LsSystemUserRoleBO> userRoles = systemUserRoleDao
                .getSystemUsersByRoleId(RoleType.qualityCenterUser.getType());
        if (userRoles != null && userRoles.size() > 0) {
            for (LsSystemUserRoleBO userRole : userRoles) {
                users.add(userRole.getUserId());
            }
        }
        return users;
    }

    // 返回JSON单表查询
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject fromObjectList1(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        String queryString = "select t,d,ou "
                + "from LsSystemUserBO t,LsSystemDepartmentBO d,LsSystemOrganizationUserBO ou where 1=1 "
                + " and t.userId=ou.userId " + " and ou.organizationId=d.organizationId "
                + "/~ and t.userId = '[userId]' ~/" + "/~ and t.userAccount like '%[userAccount]%' ~/"
                + "/~ and t.userName like '%[userName]%' ~/" + "/~ and t.userPhone like '%[userPhone]%' ~/"
                + "/~ and t.userEmail like '%[userEmail]%' ~/" + "/~ and t.level like '%[level]%' ~/"
                + "/~ and t.position like '%[position]%' ~/" + "/~ and d.organizationId like '%[organizationId]%' ~/"
                + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = systemUserDao.pageQuery(queryString, pageQuery);
        PageList<SessionUser> pageList = new PageList<SessionUser>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                SessionUser sessionuser = new SessionUser();
                BeanUtils.copyProperties(objs[0], sessionuser);
                BeanUtils.copyProperties(objs[1], sessionuser);
                BeanUtils.copyProperties(objs[2], sessionuser);
                pageList.add(sessionuser);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 根据查询条件，获取用户信息、组织机构信息、角色信息
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject fromUserRoleDepartList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) {

        String queryString = "select t,d.organizationName,r.roleName "
                + "from LsSystemUserBO t,LsSystemDepartmentBO d,LsSystemOrganizationUserBO ou,LsSystemRoleBO r,"
                + "LsSystemUserRoleBO ur " + " where 1=1 " + " and t.userId=ou.userId "
                + " and ou.organizationId=d.organizationId " + " and t.userId=ur.userId " + " and ur.roleId=r.roleId ";

        // 如果报警推送模块未开启，只查询最基本角色
        if (!systemModules.isAlarmPushOn()) {
            queryString = queryString + " and r.roleId in ('" + RoleType.admin.getType() + "','"
                    + RoleType.contromRoomUser.getType() + "','" + RoleType.portUser.getType() + "') ";
        }
        // 只有admin可以看到自己
        HttpSession session = ServletActionContext.getRequest().getSession();
        String userAccount = ((SessionUser) session.getAttribute(Constant.SESSION_USER)).getUserAccount();
        if (!"admin".equalsIgnoreCase(userAccount)) {
            queryString += " and t.userAccount != 'admin' ";
        }
        queryString = queryString + "/~ and t.userId = '[userId]' ~/" + "/~ and t.userAccount like '%[userAccount]%' ~/"
                + "/~ and t.userName like '%[userName]%' ~/" + "/~ and t.userPhone like '%[userPhone]%' ~/"
                + "/~ and t.userEmail like '%[userEmail]%' ~/" + "/~ and t.level like '%[level]%' ~/"
                + "/~ and t.position like '%[position]%' ~/" + "/~ and d.organizationId like '%[organizationId]%' ~/"
                + "/~ and r.roleId = '[roleId]' ~/" + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = systemUserDao.pageQuery(queryString, pageQuery);
        PageList<SessionUser> pageList = new PageList<SessionUser>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                SessionUser sessionuser = new SessionUser();
                BeanUtils.copyProperties(objs[0], sessionuser);
                sessionuser.setOrganizationName((String) objs[1]);// 设置所属机构
                sessionuser.setRoleName((String) objs[2]);// 设置角色
                pageList.add(sessionuser);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 查询controlRoon的用户
     * 
     * @param systemOrganizationUser
     */
    public List<SessionUser> getControlRoomUserslist(String userId) {
        return systemUserDao.findContromRoomList(userId);
    }

    public void saveUserOrganization(LsSystemOrganizationUserBO systemOrganizationUser) {
        systemOrganizationUserDao.save(systemOrganizationUser);
    }

    public LsSystemOrganizationUserBO findOrganizationIdByUserId(String userId) {
        LsSystemOrganizationUserBO systemOrganizationUser = systemOrganizationUserDao.findDepartIdByUserId(userId);
        return systemOrganizationUser;
    }

    public void modifyUserOrganization(LsSystemOrganizationUserBO systemOrganizationUser) {
        systemOrganizationUserDao.update(systemOrganizationUser);
    }

    public LsSystemUserRoleBO findRoleIdByUserId(String userId) {
        LsSystemUserRoleBO systemUserRole = systemUserRoleDao.getSystemUserRoleByUserId(userId);
        return systemUserRole;
    }

    public void saveUserRole(LsSystemUserRoleBO systemUserRole) {
        systemUserRoleDao.save(systemUserRole);
    }

    public void modifyUserRole(LsSystemUserRoleBO systemUserRole) {
        systemUserRoleDao.update(systemUserRole);
    }

    public List<LsSystemUserBO> findUserByRoleId(String type) {
        return systemUserDao.findUserByRoleId(type);
    }

    public List<LsSystemUserBO> findUserByRoleIds(String... types) {
        return systemUserDao.findUserByRoleIds(types);
    }

    /**
     * 查询所有用户
     */
    public List<LsSystemUserBO> findAllUsers(String... types) {
        return systemUserDao.findAll();
    }

    /**
     * 查询所有角色为巡逻队的用户
     */
    public List<SessionUser> findAllPatrolUsers() {
        return systemUserDao.findAllPatrolUser();
    }

    /**
     * 随机的选择一位控制中心的主管用户，获取其id
     * 
     * @return
     */
    public String randomFindOneManager() {
        return systemUserDao.findOneManagerUser();
    }
    /**
     * 根据口岸id查询，所属该口岸的所有用户
     * @param orgId
     * @return
     */
    public List<LsSystemOrganizationUserBO>  findByPortId(String orgId){
        return systemUserDao.findByPortId(orgId);
    }
    
    /**
     * 控制中心用户登陆，查询系统中所有用户；口岸用户登陆查询该口岸的所有用户
     * @param orgId
     * @return
     */
    public List<?>  findUserByOrganizationId(String orgId){
        return systemUserDao.findUserByOrganizationId(orgId);
    }
}
