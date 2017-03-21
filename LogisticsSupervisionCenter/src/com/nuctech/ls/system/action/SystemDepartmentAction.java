package com.nuctech.ls.system.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.util.OperateContentType;
import com.nuctech.ls.model.util.OperateEntityType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.ztree.TreeNode;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 组织结构管理Action
 * 
 * @author liushaowei
 *
 */
@Namespace("/deptMgmt")
public class SystemDepartmentAction extends LSBaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1389238299592275611L;

    protected static final String DEFAULT_SORT_COLUMNS = "d.organizationType ASC";

    private Logger logger = Logger.getLogger(UserAction.class);

    @Resource
    private SystemDepartmentService systemDepartmentService;

    private List<TreeNode> departmentList;
    private List<TreeNode> countryTreeList;
    public List<LsSystemDepartmentBO> depList = null;
    private String[] checkedId;
    private String[] organizationIds;
    private LsSystemDepartmentBO systemDepartmentBO;
    private LsSystemDepartmentBO org ;
    private LsSystemDepartmentBO org1 ;
    private String orgId;
    private String organizationId;
    @Resource
    private SystemOperateLogService logService;
    @Resource
    private SystemModules systemModules;
    public JSONArray departmentArr = new JSONArray();

    /**
     * 根据用户Id查询所属口岸
     * 
     * @return
     */
    @Action(value = "findPortByUserId")
    public String findPortByUserId() {
        LsSystemDepartmentBO systemDepartmentBO = systemDepartmentService
                .findPortByUserId(((SessionUser) session.get(Constant.SESSION_USER)).getUserId());
        if (systemDepartmentBO != null) {
            JSONObject json = new JSONObject();
            json.put("portId", systemDepartmentBO.getOrganizationId());
            json.put("portName", systemDepartmentBO.getOrganizationName());
            response.setCharacterEncoding("utf-8");
            try {
                response.getWriter().println(json.toString());
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return null;
    }

    /**
     * 根据用户Id查询所在国家所有口岸，用户所属口岸除外
     * 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Action(value = "findAllPortByUserId")
    public String findAllPortByUserId() throws Exception {
        String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
        List<LsSystemDepartmentBO> deptList = systemDepartmentService.findAllPortByUserId(userId);
        LsSystemDepartmentBO systemDepartmentBO = systemDepartmentService.findPortByUserId(userId);
        String departId = "";
        if (systemDepartmentBO == null) {
            logger.error("用户所属口岸为空！");
        } else {
            departId = systemDepartmentBO.getOrganizationId();
        }

        if (deptList != null && deptList.size() > 0) {
            for (Iterator<LsSystemDepartmentBO> it = deptList.iterator(); it.hasNext();) {
                LsSystemDepartmentBO department = it.next();
                if (department.getOrganizationId().equals(departId)) {
                    it.remove();
                }
            }
        }
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = systemDepartmentService.fromObjectList(deptList, new PageList<LsSystemDepartmentBO>(),
                pageQuery);

        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(retJson.toString());
        return null;
    }

    /**
     * 组织机构管理 赵苏阳
     */
    @SuppressWarnings("unchecked")
    @Action(value = "list")
    public void list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = systemDepartmentService.fromObjectList(pageQuery, null, false, checkedId);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();

    }

    // 添加Modal调用方法
    @Action(value = "addModal",
            results = { @Result(name = "success", location = "/system/systemDepartment/departmentAdd.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String addModal() {
        if (NuctechUtil.isNotNull(organizationId)) {
            organizationId = organizationId.substring(0, organizationId.length() - 1);
            // 根据id获取机构的名称
            systemDepartmentBO = systemDepartmentService.findById(organizationId);
            if(NuctechUtil.isNotNull(systemDepartmentBO)){
                request.setAttribute("organizationName", systemDepartmentBO.getOrganizationName());
            }
        }
        return SUCCESS;
    }

    /**
     * 组织机构删除
     * 
     * @return
     */
    @Action(value = "delDepartmentById",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }) })
    public String delDepartmentById() {

        try {
			if (organizationIds != null) {
			    String s[] = organizationIds[0].split(",");
			    for (int i = 0; i < s.length; i++) {
			    	
			    	if(NuctechUtil.isNotNull(systemDepartmentService.findById(s[i]))){
			    		systemDepartmentService.deleteById(s[i]);
			    	}
			    	
			        List<LsSystemDepartmentBO> list = systemDepartmentService.findByParentId(s[i]);
			        if (NuctechUtil.isNotNull(list)) {
			            for (LsSystemDepartmentBO systemDepartmentBO : list) {
			                systemDepartmentService.deleteById(systemDepartmentBO.getOrganizationId());
			            }
			        }
			       
			        addLog(OperateContentType.DELETE.toString(), OperateEntityType.DEPARTMENT.toString(),
			                "organizationId:" + s[i]);
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

        return SUCCESS;
    }

    // 构建组织机构树
    @Action(value = "findDepartmentTree", results = { @Result(name = "success", type = "json") })
    public String findDepartmentTree() throws Exception {
        departmentList = systemDepartmentService.findDepartmentTree();
        return SUCCESS;
    }

    /**
     * 组织机构新增
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "addDepartment", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String addDepartment() throws Exception {
        try {
            if (systemDepartmentBO != null && !systemDepartmentBO.getOrganizationType().equals("1")) {
                String organizationId = generatePrimaryKey();
                String s_parentId = request.getParameter("s_parentId");
                systemDepartmentBO.setParentId(request.getParameter("s_parentId"));
                systemDepartmentBO.setOrganizationId(organizationId);
               // systemDepartmentBO.setLevelCode(systemDepartmentBO.getParentId() + "." + organizationId);
               //设置机构levelCode
                String levelCode = "";
                org = systemDepartmentService.findById(s_parentId);
                org1 = systemDepartmentService.findById(s_parentId);
                //在二级机构如口岸下面添加机构
                while(NuctechUtil.isNotNull(org)){
                	if(NuctechUtil.isNotNull(org.getParentId())){
                		levelCode += org.getParentId() + "." + org.getOrganizationId()+".";
                	}
                	if(NuctechUtil.isNotNull(org.getParentId())){
                		org = systemDepartmentService.findById( org.getParentId());
                	}else {
                		org = null;
                	}
                }
                //在一级机构国家下面添加机构
                if(NuctechUtil.isNull(org1.getParentId())){
            		levelCode = org1.getOrganizationId() + ".";
            	}
                
                levelCode += organizationId;
                systemDepartmentBO.setLevelCode(levelCode);
                systemDepartmentBO.setIsEnable("1");
                systemDepartmentService.add(systemDepartmentBO);
                addLog(OperateContentType.ADD.toString(), OperateEntityType.DEPARTMENT.toString(),
                        systemDepartmentBO.toString());
                return SUCCESS;
                //新增国家时，自动添加机构"管理员中心"，"质量中心","控制中心"
            } else if(systemDepartmentBO != null && systemDepartmentBO.getOrganizationType().equals("1")){
            	//添加国家
            	String countryOrgId = generatePrimaryKey();
            	systemDepartmentBO.setOrganizationId(countryOrgId);
            	systemDepartmentBO.setLevelCode(countryOrgId);
            	systemDepartmentService.add(systemDepartmentBO);
            	//添加管理员中心
            	LsSystemDepartmentBO adminOrg = new LsSystemDepartmentBO();
            	String adOrgId = generatePrimaryKey();
            	adminOrg.setOrganizationId(adOrgId);
            	adminOrg.setOrganizationName("Admin Center");
            	adminOrg.setOrganizationShort("Admin Center");
            	adminOrg.setOrganizationType("3");
            	adminOrg.setLevelCode(systemDepartmentBO.getOrganizationId() + "." + adOrgId);
            	adminOrg.setParentId(countryOrgId);
            	adminOrg.setIsEnable("1");
            	systemDepartmentService.add(adminOrg);
            	//添加控制中心
            	LsSystemDepartmentBO controlOrg = new LsSystemDepartmentBO();
            	String controlOrgId = generatePrimaryKey();
            	controlOrg.setOrganizationId(controlOrgId);
            	controlOrg.setOrganizationName("Control Room");
            	controlOrg.setOrganizationShort("Control Room");
            	controlOrg.setOrganizationType("3");
            	controlOrg.setLevelCode(systemDepartmentBO.getOrganizationId() + "." + controlOrgId);
            	controlOrg.setParentId(countryOrgId);
            	controlOrg.setIsEnable("1");
            	systemDepartmentService.add(controlOrg);
            	/*//添加质量中心
            	LsSystemDepartmentBO qualityOrg = new LsSystemDepartmentBO();
            	String qualityOrgId = generatePrimaryKey();
            	qualityOrg.setOrganizationId(qualityOrgId);
            	qualityOrg.setOrganizationName("Quality Center");
            	qualityOrg.setOrganizationShort("Quality Center");
            	qualityOrg.setOrganizationType("3");
            	qualityOrg.setLevelCode(systemDepartmentBO.getOrganizationId() + "." + qualityOrgId);
            	qualityOrg.setParentId(countryOrgId);
            	qualityOrg.setIsEnable("1");
            	systemDepartmentService.add(qualityOrg);*/
            	return SUCCESS;
            }else{
            	 return ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    /**
     * 修改模态框弹出
     * 
     * @return
     */
    @Action(value = "editDepartment",
            results = { @Result(name = "success", location = "/system/systemDepartment/departmentEdit.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editDepartment() {
        if (!NuctechUtil.isNull(orgId)) {
            systemDepartmentBO = this.systemDepartmentService.findById(orgId);
            String orgType = systemDepartmentBO.getOrganizationType();
            request.setAttribute("orgType", orgType);
            return SUCCESS;
        } else {
            message = "Find Object Mis!";
            return ERROR;
        }
    }

    /**
     * 修改组织机构
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "modifyDepartment",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String modifyDepartment() throws Exception {
        if (systemDepartmentBO != null) {
            systemDepartmentService.modi(systemDepartmentBO);
            addLog(OperateContentType.EDIT.toString(), OperateEntityType.DEPARTMENT.toString(),
                    systemDepartmentBO.toString());
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    @Action(value = "toList",
            results = { @Result(name = "success", location = "/system/systemDepartment/departmentMgmt.jsp") })
    public String toList() {
        /*
         * pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS); depList =
         * systemDepartmentService.findDep(pageQuery); departmentArr.clear(); if
         * (depList != null) { departmentArr.addAll(depList); }
         */
        return SUCCESS;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "dlist")
    public void dlist() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = systemDepartmentService.fromObjectList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();

    }

    /**
     * 日志记录方法
     * 
     * @param content
     * @param params
     */
    private void addLog(String operate, String entity, String params) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        logService.addLog(operate, entity, sessionUser.getUserId(), this.getClass().toString(), params);
    }

    @SuppressWarnings("unchecked")
    @Action(value = "findAllPorts")
    public String findAllPorts() throws Exception {
        String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
        List<LsSystemDepartmentBO> deptList = systemDepartmentService.findAllPortByUserId(userId);
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = systemDepartmentService.fromObjectList(deptList, new PageList<LsSystemDepartmentBO>(),
                pageQuery);

        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(retJson.toString());
        return null;
    }

    public List<TreeNode> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<TreeNode> departmentList) {
        this.departmentList = departmentList;
    }

    public String[] getCheckedId() {
        return checkedId;
    }

    public void setCheckedId(String[] checkedId) {
        this.checkedId = checkedId;
    }

    public String[] getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(String[] organizationIds) {
        this.organizationIds = organizationIds;
    }

    public LsSystemDepartmentBO getSystemDepartmentBO() {
        return systemDepartmentBO;
    }

    public void setSystemDepartmentBO(LsSystemDepartmentBO systemDepartmentBO) {
        this.systemDepartmentBO = systemDepartmentBO;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public List<TreeNode> getCountryTreeList() {
        return countryTreeList;
    }

    public void setCountryTreeList(List<TreeNode> countryTreeList) {
        this.countryTreeList = countryTreeList;
    }

    public JSONArray getDepartmentArr() {
        return departmentArr;
    }

    public void setDepartmentArr(JSONArray departmentArr) {
        this.departmentArr = departmentArr;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public SystemModules getSystemModules() {
        return systemModules;
    }

    public void setSystemModules(SystemModules systemModules) {
        this.systemModules = systemModules;
    }
}
