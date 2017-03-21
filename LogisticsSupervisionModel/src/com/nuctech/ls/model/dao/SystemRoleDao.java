package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemRoleBO;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.util.Constant;
import com.nuctech.util.ConstantConfig;

/**
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 系统角色 DAO
 * </p>
 * 创建时间：2016年5月19日
 */
@Repository
public class SystemRoleDao extends LSBaseDao<LsSystemRoleBO, Serializable> {

    /**
     * 巡逻队相关角色
     */
    private static List<String> patrolRoles = new ArrayList<>();
    /**
     * 风险分析相关角色
     */
    private static List<String> riskRoles = new ArrayList<>();

    static {
        patrolRoles.add(RoleType.patrolManager.getType());
        patrolRoles.add(RoleType.enforcementPatrol.getType());
        patrolRoles.add(RoleType.escortPatrol.getType());

        riskRoles.add(RoleType.riskAnalysisUser.getType());
    }

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
        return criteria.list();
    }

    /**
     * 根据roleId数组查找角色
     * 
     * @param roleIds
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsSystemRoleBO> findByIds(String... roleIds) {
        String validFlag = ConstantConfig.readValue(Constant.VALID_FLAG);
        Criteria criteria = getSession().createCriteria(LsSystemRoleBO.class);
        criteria.add(Restrictions.in("roleId", roleIds));
        criteria.add(Restrictions.eq("isEnable", validFlag));
        return criteria.list();
    }

    /**
     * 判断当前角色Id是否巡逻队相关角色: patrolManager,enforcementPatrol,escortPatrol
     * 
     * @param roleId
     * @return
     */
    public boolean isPatrolRole(String roleId) {
        return patrolRoles.indexOf(roleId) > -1;
    }

    /**
     * 判断当前角色Id是否风险分析相关角色: riskAnalysisUser
     * 
     * @param roleId
     * @return
     */
    public boolean isRiskRole(String roleId) {
        return riskRoles.indexOf(roleId) > -1;
    }
}
