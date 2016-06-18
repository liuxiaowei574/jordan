package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

/**
 * 作者： 徐楠
 *
 * 描述：<p>系统角色 Service</p>
 * 创建时间：2016年5月17日
 */
@Service
@Transactional
public class SystemRoleService extends LSBaseService {
	
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
	
	public List<LsSystemRoleBO> findAllRoles() {
		return systemRoleDao.findAllRoles();
	}
	
	@SuppressWarnings("rawtypes")
	public PageList findRoles(PageQuery<Map> pageQuery) {
		String queryString = "select t from LsSystemRoleBO t where 1=1 "
				+ "/~ and t.roleName = '[roleName]' ~/"
				+ "/~ order by [sortColumns] ~/";
		return systemRoleDao.pageQuery(queryString, pageQuery);
	}
	
	/**
	 * 通过角色的ID查询角色所有的菜单项
	 * 
	 * @param roleId
	 * 			角色ID
	 * @return
	 */
	public List<LsSystemFunctionsBO> findRoleSystemFunctionsList(String roleId) {
		//String hql = "select roleFunctionsId from LsSystemRoleFunctionsBO where roleId = :roleId";
		List<LsSystemFunctionsBO> list = new ArrayList<LsSystemFunctionsBO>();
		
		LsSystemRoleFunctionsBO systemRoleFunction = systemRoleFunctionDao.findSystemRoleFunctionsByRoleId(roleId);
		if(systemRoleFunction != null) {
			String functionIds = systemRoleFunction.getRoleFunctionsId();
			if(functionIds != null) {
				String[] functionIdArray = functionIds.split(",");
				for(String functionId : functionIdArray) {
					LsSystemFunctionsBO function = systemFunctionDao.findValidFunctionById(functionId);
					list.add(function);
				}
			}
		}
		return list;
	}
}
