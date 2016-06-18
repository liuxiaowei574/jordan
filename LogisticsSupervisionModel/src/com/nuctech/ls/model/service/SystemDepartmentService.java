package com.nuctech.ls.model.service;

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
import com.nuctech.util.Constant;

import net.sf.json.JSONObject;

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
	 *            用户Id
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
	 *            用户Id
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
	public List<LsSystemDepartmentBO> findAllPortByUserId(String userId) {
		String deptId = findDepartmentIdByUserId(userId);
		if (deptId != null) {
			LsSystemDepartmentBO departmentBO = systemDepartmentDao.findById(deptId);
			String countryId = findCountryIdByDeptId(departmentBO);
			if (countryId != null) {
				HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
				propertiesMap.put("parentId", countryId);
				propertiesMap.put("organizationType", Constant.ORGANIZATION_TYPE_PORT);
				return systemDepartmentDao.findAllBy(propertiesMap, null);
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

	/**
	 * 根据组织Id查找国家节点Id
	 * 
	 * @param deptId
	 * @return
	 */
	public String findCountryIdByDeptId(LsSystemDepartmentBO departmentBO) {
		if (departmentBO == null || departmentBO.getLevelCode() == null) {
			return null;
		}
		String levelCode = departmentBO.getLevelCode(); // 001.002.003.004
		return levelCode.split("\\.")[0];
	}

	/**
	 * 根据ID查询组织机构
	 * 
	 * @param id
	 *            组织机构主键ID
	 * @return
	 */
	public LsSystemDepartmentBO findById(String id) {
		return systemDepartmentDao.findById(id);
	}
}
