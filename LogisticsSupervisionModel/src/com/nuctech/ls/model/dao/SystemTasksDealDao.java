package com.nuctech.ls.model.dao;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemTasksDealBO;

@Repository
public class SystemTasksDealDao extends LSBaseDao<LsSystemTasksDealBO, Serializable> {

    /**
     * 根据接收人和任务的id查询任务处理日志
     * 
     * @param taskId
     * @param userId
     * @return
     */
    public LsSystemTasksDealBO findLog(String taskId, String userId) {
        Criteria criteria = getSession().createCriteria(LsSystemTasksDealBO.class);
        criteria.add(Restrictions.eq("taskId", taskId));
        criteria.add(Restrictions.eq("receiveUser", userId));
        return (LsSystemTasksDealBO) criteria.uniqueResult();
    }
}
