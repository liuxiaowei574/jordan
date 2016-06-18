package com.nuctech.ls.model.dao;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemFunctionsBO;
import com.nuctech.util.Constant;
import com.nuctech.util.ConstantConfig;

/**
 * 作者： 徐楠
 *
 * 描述：<p>系统菜单 DAO</p>
 * 创建时间：2016年5月18日
 */
@Repository
public class SystemFunctionsDao extends LSBaseDao<LsSystemFunctionsBO, Serializable> {

	/**
	 * 查询有效菜单ID
	 * 
	 * @param id
	 * @return
	 */
	public LsSystemFunctionsBO findValidFunctionById(String id) {
		String inValidFlag = ConstantConfig.readValue(Constant.VALID_FLAG); 
		Criteria criteria = getSession().createCriteria(LsSystemFunctionsBO.class);
		criteria.add(Restrictions.eq("functionId", id));
		criteria.add(Restrictions.eq("isEnable", inValidFlag));
		return (LsSystemFunctionsBO)criteria.uniqueResult();
	}
}
