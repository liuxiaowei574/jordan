package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.system.LsSystemOrganizationUserBO;
import com.nuctech.ls.model.dao.SystemDepartmentDao;
import com.nuctech.ls.model.dao.SystemOrganizationUserDao;
import com.nuctech.ls.model.vo.ztree.TreeNode;
import com.nuctech.util.Constant;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 组织机构 Service
 * 
 * @author liushaowei
 *
 */
@Service
@Transactional
public class SystemDepartmentService extends LSBaseService {

    @Resource
    private SystemDepartmentDao systemDepartmentDao;

    @Resource
    private SystemOrganizationUserDao systemOrganizationUserDao;

    /**
     * 根据用户Id查询组织机构Id
     * 
     * @param userId
     *        用户Id
     * @return
     */
    public String findDepartmentIdByUserId(String userId) {
        LsSystemOrganizationUserBO orgUser = systemOrganizationUserDao.findDepartIdByUserId(userId);
        if (orgUser != null) {
            return orgUser.getOrganizationId();
        }
        return null;
    }

    /**
     * 根据用户Id查询所属口岸
     * 
     * @param userId
     *        用户Id
     * @return
     */
    public LsSystemDepartmentBO findPortByUserId(String userId) {
        String deptId = findDepartmentIdByUserId(userId);
        if (deptId != null) {
            HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
            propertiesMap.put("organizationId", deptId);
            propertiesMap.put("organizationType", Constant.ORGANIZATION_TYPE_PORT);
            return systemDepartmentDao.findByProperties(propertiesMap, null);
        }
        return null;
    }

    /**
     * 根据用户Id查找所在国家所有口岸
     * 
     * @param deptId
     * @return
     */

    /*
     * public List<LsSystemDepartmentBO> findAllPortByUserId(String userId) {
     * String deptId = findDepartmentIdByUserId(userId); if (deptId != null) {
     * LsSystemDepartmentBO departmentBO = systemDepartmentDao.findById(deptId);
     * String countryId = findCountryIdByDeptId(departmentBO); if (countryId !=
     * null) { HashMap<String, Object> propertiesMap = new HashMap<String,
     * Object>(); propertiesMap.put("parentId", countryId);
     * propertiesMap.put("organizationType", Constant.ORGANIZATION_TYPE_PORT);
     * HashMap<String, String> orderby = new HashMap<String, String>();
     * orderby.put("organizationName", "asc"); return
     * systemDepartmentDao.findAllBy(propertiesMap, orderby); } } return null; }
     */

    public List<LsSystemDepartmentBO> findAllPortByUserId(String userId) {
        String deptId = findDepartmentIdByUserId(userId);
        if (deptId != null) {
            LsSystemDepartmentBO departmentBO = systemDepartmentDao.findById(deptId);
            String countryId = findCountryIdByDeptId(departmentBO);
            if (countryId != null) {
                List<LsSystemDepartmentBO> systemDepartmentBOsList = new ArrayList<LsSystemDepartmentBO>();
                //查询所有口岸
                systemDepartmentBOsList = systemDepartmentDao.findAllPortByCountryId(countryId);
                //查询所有机构
//                systemDepartmentBOsList = systemDepartmentDao.findAllDepartmentByCountryId(countryId);
                return systemDepartmentBOsList;
            }
        }
        return null;
    }

    /**
     * toJson
     * 
     * @param srcList
     * @param pageList
     * @param pageQuery
     * @return
     */
    @SuppressWarnings("rawtypes")
    public JSONObject fromObjectList(List<LsSystemDepartmentBO> srcList, PageList<LsSystemDepartmentBO> pageList,
            PageQuery<Map> pageQuery) {
        if (srcList == null || srcList.size() < 1) {
            return null;
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.addAll(srcList);
        pageList.setTotalItems(srcList.size());
        return fromObjectList(pageList, null, false);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes,
            String[] checkedId) {
        PageList<LsSystemDepartmentBO> pageList = findDepartement(pageQuery, checkedId);
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        PageList<LsSystemDepartmentBO> pageList = findDep(pageQuery);
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 根据组织Id查找国家节点Id
     * 
     * @param deptId
     * @return
     */
    public String findCountryIdByDeptId(LsSystemDepartmentBO departmentBO) {
        return systemDepartmentDao.findCountryIdByDeptId(departmentBO);
    }

    /**
     * 根据ID查询组织机构
     * 
     * @param id
     *        组织机构主键ID
     * @return
     */
    public LsSystemDepartmentBO findById(String id) {
        return systemDepartmentDao.findById(id);
    }

    /**
     * 组织机构管理 赵苏阳
     */
    @SuppressWarnings("rawtypes")
    public PageList findDepartement(PageQuery<Map> pageQuery, String[] checkedId) {
        // 加个判断，写两个sql语句
        String queryString = null;
        String s = null;
        if (checkedId[0].isEmpty()) {
            queryString = "select d from LsSystemDepartmentBO d" + " where 1=1 "
                    + "/~ and d.organizationId = '[organizationId]' ~/" + "/~ and d.parentId = '[parentId]' ~/"
                    + "/~ and d.organizationName = '[organizationName]' ~/"
                    + "/~ and d.organizationShort = '[organizationShort]' ~/"
                    + "/~ and d.organizationType = '[organizationType]' ~/" + "/~ and d.longitude = '[longitude]' ~/"
                    + "/~ and d.latitude = '[latitude]' ~/" + "/~ order by [sortColumns] ~/";
        } else {
            // 判断是否为"国家"
            if (checkedId[0].endsWith(",")) {
                s = checkedId[0].substring(0, checkedId[0].length() - 1);
            }

            queryString = "select d from LsSystemDepartmentBO d  where parentId in ('" + s + "') or organizationId='"
                    + s + "' ORDER BY d.organizationType ";
        }
        return systemDepartmentDao.pageQuery(queryString, pageQuery);
    }

    // 组织机构树构建
    public List<TreeNode> findDepartmentTree() {
        return systemDepartmentDao.findSDTree();
    }

    public void deleteById(String organizationId) {
        systemDepartmentDao.deleteById(organizationId);
    }

    public void add(LsSystemDepartmentBO systemDepartmentBO) {
        systemDepartmentDao.save(systemDepartmentBO);
    }

    public void modi(LsSystemDepartmentBO systemDepartmentBO) {
        systemDepartmentDao.merge(systemDepartmentBO);
    }

    @SuppressWarnings("rawtypes")
    public PageList findDep(PageQuery<Map> pageQuery) {
        String queryString = "select d from LsSystemDepartmentBO d" + " where 1=1 "
                + "/~ and d.organizationId like '%[organizationId]%' ~/" + "/~ and d.parentId like '%[parentId]%' ~/"
                + "/~ and d.organizationName like '%[organizationName]%' ~/"
                + "/~ and d.organizationShort like '%[organizationShort]%' ~/"
                + "/~ and d.organizationType like '%[organizationType]%' ~/"
                + "/~ and d.longitude like '%[longitude]%' ~/" + "/~ and d.latitude like '%[latitude]%' ~/"
                + "/~ order by [sortColumns] ~/";

        return systemDepartmentDao.pageQuery(queryString, pageQuery);
    }

    public List<LsSystemDepartmentBO> findByParentId(String parentId) {
        return (systemDepartmentDao.findAllBy("parentId", parentId));
    }
    
    /**
     * 查找所有口岸机构
     * @return
     */
    public List<LsSystemDepartmentBO> findAllPort(){
        return systemDepartmentDao.findAllPort();
    }
}
