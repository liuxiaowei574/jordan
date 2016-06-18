package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemRoleBO;
import com.nuctech.util.Constant;
import com.nuctech.util.ConstantConfig;

/**
 * 作者： 徐楠
 *
 * 描述：<p>系统角色 DAO</p>
 * 创建时间：2016年5月19日
 */
@Repository
public class SystemRoleDao extends LSBaseDao<LsSystemRoleBO, Serializable> {

	/**
	 * 查询所有有效的角色
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LsSystemRoleBO> findAllRoles() {
		String validFlag = ConstantConfig.readValue(Constant.VALID_FLAG);
		Criteria criteria = getSession().createCriteria(LsSystemRoleBO.class);
		criteria.add(Restrictions.eq("isEnable", validFlag));
		return (List<LsSystemRoleBO>)criteria.list();
	}
}
