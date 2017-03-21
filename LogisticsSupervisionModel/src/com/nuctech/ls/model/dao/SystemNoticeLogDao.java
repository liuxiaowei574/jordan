package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemNoticeLogBO;
import com.nuctech.ls.model.bo.system.LsSystemNoticesBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 通知接收日志 DAO
 * </p>
 * 创建时间：2016年5月27日
 */
@Repository
public class SystemNoticeLogDao extends LSBaseDao<LsSystemNoticeLogBO, Serializable> {

    /**
     * 根据
     * 
     * @param noticeId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsSystemNoticeLogBO> findNoticeLogListByNoticeId(String noticeId) {
        Criteria criteria = getSession().createCriteria(LsSystemNoticeLogBO.class);
        criteria.add(Restrictions.eq("noticeId", noticeId));
        return criteria.list();
    }

    @SuppressWarnings({ "rawtypes" })
    public PageList findNoticeLogList(String noticeId, PageQuery<Map> pageQuery) {
        String queryString = "select t, u " + " from LsSystemNoticeLogBO t, LsSystemUserBO u where 1=1 "
                + " and t.receiveUser = u.userId " + "/~ and t.noticeId = '[noticeId]' ~/"
                + "/~ and t.dealType = '[dealType]' ~/" + "/~ and t.noticeType = '[noticeType]' ~/"
                + "/~ order by [sortColumns] ~/";
        return pageQuery(queryString, pageQuery);
    }

    public void deleteNoticeLogByNoticeId(String noticeId) {
        Session session = this.getSession();
        String hql = "delete LsSystemNoticeLogBO where noticeId = :noticeId";
        Query query = session.createQuery(hql);
        query.setParameter("noticeId", noticeId);
        query.executeUpdate();
    }

    /**
     * 通过通知ID和接收人查询通知日志
     * 
     * @param noticeId
     * @param receiveUser
     * @return
     */
    public LsSystemNoticeLogBO findNoticeLogByNoticeIdAndReceiveUser(String noticeId, String receiveUser) {
        Criteria criteria = getSession().createCriteria(LsSystemNoticeLogBO.class);
        criteria.add(Restrictions.eq("noticeId", noticeId));
        criteria.add(Restrictions.eq("receiveUser", receiveUser));
        return (LsSystemNoticeLogBO) criteria.uniqueResult();
    }

    /**
     * 通过通知ID和类型查询通知日志
     * 
     * @param noticeId
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsSystemNoticeLogBO> findNoticeLogByNoticeIdAndType(String noticeId, String type) {
        Criteria criteria = getSession().createCriteria(LsSystemNoticeLogBO.class);
        criteria.add(Restrictions.eq("noticeId", noticeId));
        criteria.add(Restrictions.eq("dealType", type));
        return criteria.list();
    }

    public int findNumber(String id) {
        /*
         * Criteria criteria =
         * getSession().createCriteria(LsSystemNoticeLogBO.class);
         * criteria.add(Restrictions.eq("receiveUser", id));
         * criteria.add(Restrictions.eq("dealType", "0")); return (int)
         * criteria.list().size();
         */
        String queryString = "select t.*,l.*,u.* "
                + "from LS_SYSTEM_NOTICES t,LS_SYSTEM_NOTICE_LOG l,LS_SYSTEM_USER u where 1=1 "
                + " and t.NOTICE_ID=l.NOTICE_ID " + " and t.PUBLISHER=u.USER_ID " + " and l.RECEIVE_USER='" + id + "'"
                + " and l.DEAL_TYPE='" + 0 + "'";
        Session session = this.getSession();
        List<?> list = session.createSQLQuery(queryString).addEntity("t", LsSystemNoticesBO.class)
                .addEntity("l", LsSystemNoticeLogBO.class).addEntity("u", LsSystemUserBO.class).list();
        return list.size();
    }

    /**
     * 根据接收人和通知的id查询通知日志
     * 
     * @param noticeId
     * @param userId
     * @return
     */
    public LsSystemNoticeLogBO findLog(String noticeId, String userId) {
        Criteria criteria = getSession().createCriteria(LsSystemNoticeLogBO.class);
        criteria.add(Restrictions.eq("noticeId", noticeId));
        criteria.add(Restrictions.eq("receiveUser", userId));
        return (LsSystemNoticeLogBO) criteria.uniqueResult();
    }
}
