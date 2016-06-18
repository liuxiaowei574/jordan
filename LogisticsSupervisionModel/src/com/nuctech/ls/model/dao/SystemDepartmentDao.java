package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

/**
 * 组织机构 DAO
 * 
 * @author liushaowei
 *
 */
@Repository
public class SystemDepartmentDao extends LSBaseDao<LsSystemDepartmentBO, Serializable> {

	/**
	 * 查询国家口岸列表
	 * 
	 * @param countryId
	 * 				国家ID
	 * @param portName
	 * 				口岸名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LsSystemDepartmentBO> findCountryPortList(String countryId, String portName) {
		Criteria criteria = getSession().createCriteria(LsSystemDepartmentBO.class);
		criteria.add(Restrictions.eq("organizationType", Constant.ORGANIZATION_TYPE_PORT));
		if(!NuctechUtil.isNull(countryId)) {
			criteria.add(Restrictions.eq("parentId", countryId));
		}
		if(!NuctechUtil.isNull(portName)) {
			criteria.add(Restrictions.like("organizationName", '%' + portName + '%'));
		}
		return (List<LsSystemDepartmentBO>)criteria.list();
	}
	
}
