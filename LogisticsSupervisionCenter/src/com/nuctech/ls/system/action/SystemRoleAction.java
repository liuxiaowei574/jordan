package com.nuctech.ls.system.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemFunctionsBO;
import com.nuctech.ls.model.bo.system.LsSystemRoleBO;
import com.nuctech.ls.model.bo.system.LsSystemRoleFunctionsBO;
import com.nuctech.ls.model.service.SystemFunctionService;
import com.nuctech.ls.model.service.SystemRoleService;
import com.nuctech.ls.model.vo.ztree.TreeNode;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 作者： 赵苏阳
 *
 * 描述：
 * <p>
 * 通知 Action
 * </p>
 * 创建时间：2016年8月8日
 */
@Namespace("/roleDistribute")
public class SystemRoleAction extends LSBaseAction {

    private static final long serialVersionUID = 1L;
    protected static final String DEFAULT_SORT_COLUMNS = "s.roleId ASC";
    @Resource
    private SystemRoleService systemRoleService;
    @Resource
    private SystemFunctionService systemFunctionService;
    String roleId;
    private LsSystemRoleBO systemRoleBO;
    private String[] roleIds;
    private String[] functionIds;
    private LsSystemRoleFunctionsBO systemRoleFunctionsBO;
    private List<TreeNode> functionList;
    private LsSystemFunctionsBO systemFunctionsBO;

    /**
     * 初始化角色表
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Action(value = "list")
    public void list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = systemRoleService.fromObjectList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /**
     * 弹出编辑模态框
     * 
     * @return
     */
    @Action(value = "editModal",
            results = { @Result(name = "success", location = "/system/roleDistribute/roleDistributeEdit.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editModal() {

        if (!NuctechUtil.isNull(roleId)) {
            systemRoleBO = systemRoleService.findById(roleId);
            return SUCCESS;
        } else {
            message = "Find Object Mis!";
            return ERROR;
        }
    }

    @Action(value = "delRoleById", results = { @Result(name = "success", params = { "root", "true" },
            location = "/system/roleDistribute/roleDistributeList.jsp") })
    public String delRoleById() {
        if (roleIds != null) {
            String s[] = roleIds[0].split(",");
            for (int i = 0; i < s.length; i++) {
                systemRoleService.deleteById(s[i]);
            }
        }

        return SUCCESS;
    }

    /**
     * 跳转到功能分配模态框
     * 
     * @return
     */
    @Action(value = "roleFunctions",
            results = { @Result(name = "success", location = "/system/roleDistribute/systemFunctions.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String roleFunctions() {
        request.setAttribute("roleId", roleId);
        systemRoleFunctionsBO = systemRoleService.findByRoleId(roleId);
        String sFunctionsId = systemRoleFunctionsBO.getFunctionsId();// 获取角色功能表中的功能ID即字段functionsId
        request.setAttribute("sFunctionsId", sFunctionsId);
        return SUCCESS;
    }

    @Action(value = "functionList", results = { @Result(name = "success", type = "json") })
    public void functionList() throws IOException {
        List<LsSystemFunctionsBO> functionList = systemFunctionService.getSystemFunctionList();
        List<LsSystemFunctionsBO> funList = new ArrayList<>();
        JSONArray retJson = JSONArray.fromObject(functionList);
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    @Action(value = "modifyRoleFunction",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String modifyRoleFunction() throws Exception {
        /**
         * 一共5种角色，可以先把5种角色的具有的功能初始化。。。即在数据库表中先写好。。。
         * 然后再根据roleId查出来表stemRoleFunctionsBO的功能Id
         * 
         */
        /*
         * StringBuffer sBuffer = new StringBuffer(); for (int i = 0; i <
         * functionIds.length; i++) { sBuffer.append(functionIds[i]); } String
         * functionsId = sBuffer.toString(); systemRoleFunctionsBO =
         * systemRoleService.findByRoleId(roleId);
         * systemRoleFunctionsBO.setFunctionsId(functionsId);
         * systemRoleService.modify(systemRoleFunctionsBO); return SUCCESS;
         */
        StringBuffer stringBuffer = new StringBuffer();
        String string[] = functionIds[0].split(",");
        for (int i = 0; i < string.length; i++) {
            systemFunctionsBO = systemFunctionService.findByFunctionId(string[i]);
            String parentId = systemFunctionsBO.getParentId();
            stringBuffer.append(parentId + ",");
            // stringBuffer.append(parentId);
        }
        for (int i = 0; i < functionIds.length; i++) {
            stringBuffer.append(functionIds[i]);
        }
        String functionsId = stringBuffer.toString();
        // 把其中重复的"菜单"id去掉
        String array[] = functionsId.split(",");
        ArrayList list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            if (!list.contains(array[i])) {
                list.add(array[i]);
            }
        }
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            sBuffer.append(list.get(i) + ",");
        }
        String id = sBuffer.toString();
        String fid = id.substring(0, id.lastIndexOf(","));
        systemRoleFunctionsBO = systemRoleService.findByRoleId(roleId);
        systemRoleFunctionsBO.setFunctionsId(fid);
        systemRoleService.modify(systemRoleFunctionsBO);
        return SUCCESS;
    }

    @Action(value = "editSystemRole", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editSystemRole() throws Exception {
        if (systemRoleBO != null) {
            systemRoleService.modifyRole(systemRoleBO);
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    /**
     * 跳转到权限管理页面
     * 
     * @return
     */
    @Action(value = "roleMgmtTolist",
            results = { @Result(name = "success", location = "/system/roleDistribute/roleDistributeList.jsp") })
    public String roleMgmtTolist() {
        return SUCCESS;
    }

    /**
     * 构建功能树
     * 
     * @return
     */
    @Action(value = "findFunctionTree", results = { @Result(name = "success", type = "json") })
    public String findFunctionTree() throws Exception {
        functionList = systemFunctionService.findFunctionTree();
        return SUCCESS;
    }

    /**
     * 跳转到功能分配模态框(树状展示)
     * 
     * @return
     */
    @Action(value = "roleFunctionsTree",
            results = { @Result(name = "success", location = "/system/roleDistribute/functionTree.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String roleFunctionsTree() {
        systemRoleFunctionsBO = systemRoleService.findByRoleId(roleId);
        request.setAttribute("functionIds", systemRoleFunctionsBO.getFunctionsId());
        return SUCCESS;
    }

    /**
     * 在树状展示下修改角色功能表
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "updateRoleFunction",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String updateRoleFunction() throws Exception {

        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < functionIds.length; i++) {
            sBuffer.append(functionIds[i]);
        }
        String functionsId = sBuffer.toString();
        systemRoleFunctionsBO = systemRoleService.findByRoleId(roleId);
        systemRoleFunctionsBO.setFunctionsId(functionsId);
        systemRoleService.modify(systemRoleFunctionsBO);
        return SUCCESS;

    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public LsSystemRoleBO getSystemRoleBO() {
        return systemRoleBO;
    }

    public void setSystemRoleBO(LsSystemRoleBO systemRoleBO) {
        this.systemRoleBO = systemRoleBO;
    }

    public String[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String[] roleIds) {
        this.roleIds = roleIds;
    }

    public String[] getFunctionIds() {
        return functionIds;
    }

    public void setFunctionIds(String[] functionIds) {
        this.functionIds = functionIds;
    }

    public LsSystemRoleFunctionsBO getSystemRoleFunctionsBO() {
        return systemRoleFunctionsBO;
    }

    public void setSystemRoleFunctionsBO(LsSystemRoleFunctionsBO systemRoleFunctionsBO) {
        this.systemRoleFunctionsBO = systemRoleFunctionsBO;
    }

    public List<TreeNode> getFunctionList() {
        return functionList;
    }

    public void setFunctionList(List<TreeNode> functionList) {
        this.functionList = functionList;
    }

    public LsSystemFunctionsBO getSystemFunctionsBO() {
        return systemFunctionsBO;
    }

    public void setSystemFunctionsBO(LsSystemFunctionsBO systemFunctionsBO) {
        this.systemFunctionsBO = systemFunctionsBO;
    }
}
