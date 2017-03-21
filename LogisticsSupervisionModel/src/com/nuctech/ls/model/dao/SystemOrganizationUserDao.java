package com.nuctech.ls.model.dao;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemOrganizationUserBO;

/**
 * 组织机构和用户关联 DAO
 * 
 * @author liushaowei
 *
 */
@Repository
public class SystemOrganizationUserDao extends LSBaseDao<LsSystemOrganizationUserBO, Serializable> {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * 根据用户Id查询组织机构Id
     * 
     * @param userId
     * @return
     */
    public LsSystemOrganizationUserBO findDepartIdByUserId(String userId) {
        Criteria criteria = getSession().createCriteria(LsSystemOrganizationUserBO.class);
        criteria.add(Restrictions.eq("userId", userId));
        LsSystemOrganizationUserBO orgUser = (LsSystemOrganizationUserBO) criteria.uniqueResult();
        if (orgUser == null) {
            return null;
        }
        logger.info(String.format("根据用户Id查询组织机构信息， userId: %s", userId));
        return orgUser;
    }

}
