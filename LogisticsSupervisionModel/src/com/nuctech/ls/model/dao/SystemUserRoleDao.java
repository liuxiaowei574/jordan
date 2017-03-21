package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemUserRoleBO;

/**
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 系统用户角色关联 DAO
 * </p>
 * 创建时间：2016年5月17日
 */
@Repository
public class SystemUserRoleDao extends LSBaseDao<LsSystemUserRoleBO, Serializable> {

    /**
     * 通过用户ID查询用户角色关联对象
     * 
     * @param userId
     *        用户ID
     * @return
     */
    public LsSystemUserRoleBO getSystemUserRoleByUserId(String userId) {
        Criteria criteria = getSession().createCriteria(LsSystemUserRoleBO.class);
        criteria.add(Restrictions.eq("userId", userId));
        return (LsSystemUserRoleBO) criteria.uniqueResult();
    }

    /**
     * 查找指定角色Id的所有用户
     * 
     * @param roleId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsSystemUserRoleBO> getSystemUsersByRoleId(String roleId) {
        Criteria criteria = getSession().createCriteria(LsSystemUserRoleBO.class);
        criteria.add(Restrictions.eq("roleId", roleId));
        return criteria.list();
    }
}
