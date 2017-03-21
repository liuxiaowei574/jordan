package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemFunctionsBO;
import com.nuctech.ls.model.bo.system.LsSystemRoleBO;
import com.nuctech.ls.model.bo.system.LsSystemRoleFunctionsBO;
import com.nuctech.ls.model.bo.system.LsSystemUserRoleBO;
import com.nuctech.ls.model.dao.SystemFunctionsDao;
import com.nuctech.ls.model.dao.SystemRoleDao;
import com.nuctech.ls.model.dao.SystemRoleFunctionsDao;
import com.nuctech.ls.model.dao.SystemUserRoleDao;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.util.Constant;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 系统角色 Service
 * </p>
 * 创建时间：2016年5月17日
 */
@Service
@Transactional
public class SystemRoleService extends LSBaseService {

    @Resource
    private SystemModules systemModules;
    @Resource
    private SystemUserRoleDao systemUserRoleDao;
    @Resource
    private SystemRoleDao systemRoleDao;
    @Resource
    private SystemFunctionsDao systemFunctionDao;
    @Resource
    private SystemRoleFunctionsDao systemRoleFunctionDao;

    public LsSystemUserRoleBO findSystemUserRole(String userId) {
        return systemUserRoleDao.getSystemUserRoleByUserId(userId);
    }

    /**
     * 查询所有有效的角色
     * 
     * @return
     */
    public List<LsSystemRoleBO> findAllRoles() {
        return systemRoleDao.findAllRoles();
    }

    @SuppressWarnings("rawtypes")
    public PageList findRoles(PageQuery<Map> pageQuery) {
        String queryString = "select t from LsSystemRoleBO t where 1=1 " + "/~ and t.roleName = '[roleName]' ~/"
                + "/~ order by [sortColumns] ~/";
        return systemRoleDao.pageQuery(queryString, pageQuery);
    }

    /**
     * 通过角色的ID查询角色所有的菜单项
     * 
     * @param roleId
     *        角色ID
     * @return
     */
    public List<LsSystemFunctionsBO> findRoleSystemFunctionsList(String roleId) {
        List<LsSystemFunctionsBO> list = new ArrayList<LsSystemFunctionsBO>(); // 结果集
        Map<String, LsSystemFunctionsBO> funcMap = new HashMap<>(); // 菜单Id与菜单项映射

        LsSystemRoleFunctionsBO systemRoleFunction = systemRoleFunctionDao.findSystemRoleFunctionsByRoleId(roleId);
        if (systemRoleFunction != null) {
            String functionIds = systemRoleFunction.getFunctionsId();
            if (functionIds != null) {
                List<String> menuIds = new ArrayList<String>(); // 一级菜单Id
                List<String> subMenuIds = new ArrayList<String>(); // 二级菜单Id
                List<LsSystemFunctionsBO> funclist = new ArrayList<LsSystemFunctionsBO>(); // 查询出的菜单、功能Id

                // 查询
                String[] functionIdArray = functionIds.split("\\s*,\\s*");
                if (functionIdArray != null) {
                    for (String functionId : functionIdArray) {
                        LsSystemFunctionsBO function = systemFunctionDao.findValidFunctionById(functionId);
                        if (function != null) {
                            funclist.add(function);
                            if (Constant.FUNCTION_TYPE_MENU.equals(function.getFunctionType())) {
                                menuIds.add(function.getFunctionId());
                            } else if (Constant.FUNCTION_TYPE_FUNCTION.equals(function.getFunctionType())) {
                                subMenuIds.add(function.getFunctionId());
                            }
                        }
                    }
                }
                // 菜单项过滤，当有某二级菜单权限时，必须同时有一级菜单权限，才可以，否则忽略该项
                if (funclist.size() > 0) {
                    for (LsSystemFunctionsBO functionsBO : funclist) {
                        if (Constant.FUNCTION_TYPE_MENU.equals(functionsBO.getFunctionType())) {
                            funcMap.put(functionsBO.getFunctionId(), functionsBO);
                        } else if (Constant.FUNCTION_TYPE_FUNCTION.equals(functionsBO.getFunctionType())
                                && menuIds.indexOf(functionsBO.getParentId()) > -1) {
                            funcMap.put(functionsBO.getFunctionId(), functionsBO);
                        }
                    }
                }
                // 去除无子菜单的一级菜单
                if (menuIds.size() > 0) {
                    for (String menuId : menuIds) {
                        boolean empty = true;
                        List<LsSystemFunctionsBO> subFuncs = systemFunctionDao.findSubFunctions(menuId);
                        if (subFuncs != null && subFuncs.size() > 0 && subMenuIds.size() > 0) {
                            for (LsSystemFunctionsBO functionsBO : subFuncs) {
                                if (subMenuIds.indexOf(functionsBO.getFunctionId()) > -1) {
                                    empty = false;
                                    break;
                                }
                            }
                        }
                        if (empty) {
                            funcMap.remove(menuId);
                        }
                    }
                }
                list.addAll(funcMap.values());

                // 排序，便于前台展示
                Collections.sort(list, new Comparator<LsSystemFunctionsBO>() {

                    @Override
                    public int compare(LsSystemFunctionsBO o1, LsSystemFunctionsBO o2) {
                        return o1.getLevelCode().compareTo(o2.getLevelCode());
                    }
                });
            }
        }
        return list;
    }

    public LsSystemRoleBO findById(String roleId) {
        return systemRoleDao.findById(roleId);
    }

    /**
     * 根据roleId数组查找角色
     * 
     * @param roleIds
     * @return
     */
    public List<LsSystemRoleBO> findByRoleIds(String... roleIds) {
        return systemRoleDao.findByIds(roleIds);
    }

    /**
     * 查询所有的基本角色
     * 
     * @return
     */
    public List<LsSystemRoleBO> findBasicRoles() {
        return this.findByRoleIds(RoleType.admin.getType(), RoleType.contromRoomUser.getType(),
                RoleType.portUser.getType());
    }

    /**
     * 查询基本角色里的Control Room角色
     * 
     * @return
     */
    public List<LsSystemRoleBO> findBasicControlRoles() {
        return this.findByRoleIds(RoleType.contromRoomUser.getType());
    }

    /**
     * 查询所有角色里的Control Room角色
     * 
     * @return
     */
    public List<LsSystemRoleBO> findAllControlRoles() {
        return this.findByRoleIds(RoleType.contromRoomUser.getType(), RoleType.contromRoomManager.getType(),
                RoleType.followupUser.getType(), RoleType.enforcementPatrol.getType(), RoleType.escortPatrol.getType(),
                RoleType.patrolManager.getType(), RoleType.riskAnalysisUser.getType());
    }

    @SuppressWarnings("rawtypes")
    public JSONObject fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        PageList<LsSystemRoleBO> pageList = findSystemRole(pageQuery);
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public PageList<LsSystemRoleBO> findSystemRole(PageQuery<Map> pageQuery) {
        String queryString = "select s from LsSystemRoleBO s" + " where 1=1 ";
        if (!systemModules.isPatrolOn()) {
            queryString += " and s.roleId not in (7,9,10)";
        }
        if (!systemModules.isRiskOn()) {
            queryString += " and s.roleId not in (6)";
        }
        if (!systemModules.isAlarmPushOn()) {
            queryString += " and s.roleId not in (2,3,5)";
        }
        queryString += "/~ and s.roleId like '%[roleId]%' ~/" + "/~ and s.roleName like '%[roleName]%' ~/"
                + "/~ order by [sortColumns] ~/";
        return systemRoleDao.pageQuery(queryString, pageQuery);
    }

    public void deleteById(String roleId) {
        systemRoleDao.deleteById(roleId);
    }

    public LsSystemRoleFunctionsBO findByRoleId(String roleId) {
        return systemRoleFunctionDao.findById(roleId);
    }

    /**
     * 修改角色功能表
     */
    public void modify(LsSystemRoleFunctionsBO systemRoleFunctionsBO) {
        systemRoleFunctionDao.update(systemRoleFunctionsBO);
    }

    /**
     * 修改功能表
     */
    public void modify(LsSystemFunctionsBO lsSystemFunctionsBO) {
        systemFunctionDao.update(lsSystemFunctionsBO);
    }

    /**
     * 修改角色表
     * 
     * @param warehouseElock
     */
    public void modifyRole(LsSystemRoleBO systemRoleBO) {
        systemRoleDao.update(systemRoleBO);
    }

    /**
     * 判断角色是否执法/护送巡逻队角色
     */
    public boolean isPatrol(String roleName) {
        return RoleType.enforcementPatrol.toString().equals(roleName)
                || RoleType.escortPatrol.toString().equals(roleName);
    }

    /**
     * 判断当前角色Id是否巡逻队相关角色: patrolManager,enforcementPatrol,escortPatrol
     * 
     * @param roleId
     * @return
     */
    public boolean isPatrolRole(String roleId) {
        return systemRoleDao.isPatrolRole(roleId);
    }

    /**
     * 判断当前角色Id是否风险分析相关角色: riskAnalysisUser
     * 
     * @param roleId
     * @return
     */
    public boolean isRiskRole(String roleId) {
        return systemRoleDao.isRiskRole(roleId);
    }

    /**
     * 判断角色是否管理员角色
     */
    public boolean isAdminRole(String roleName) {
        return RoleType.admin.toString().equals(roleName);
    }

    public SystemModules getSystemModules() {
        return systemModules;
    }

    public void setSystemModules(SystemModules systemModules) {
        this.systemModules = systemModules;
    }
}
