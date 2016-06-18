package com.nuctech.ls.model.dao;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemRoleFunctionsBO;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>角色功能</p>
 * 创建时间：2016年6月1日
 */
@Repository
public class SystemRoleFunctionsDao extends LSBaseDao<LsSystemRoleFunctionsBO, Serializable> {

	/**
	 * 角色ID
	 * 
	 * @param roleId
	 * @return
	 */
	public LsSystemRoleFunctionsBO findSystemRoleFunctionsByRoleId(String roleId) {
		Criteria criteria = getSession().createCriteria(LsSystemRoleFunctionsBO.class);
		criteria.add(Restrictions.eq("roleId", roleId));
		return (LsSystemRoleFunctionsBO)criteria.uniqueResult();
	}
}
