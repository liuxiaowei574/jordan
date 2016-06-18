package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemNoticeLogBO;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>通知接收日志 DAO</p>
 * 创建时间：2016年5月27日
 */
@Repository
public class SystemNoticeLogDao extends LSBaseDao<LsSystemNoticeLogBO, Serializable> {

	/**
	 * 根据
	 * @param noticeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LsSystemNoticeLogBO> findNoticeLogListByNoticeId(String noticeId) {
		Criteria criteria = getSession().createCriteria(LsSystemNoticeLogBO.class);
		criteria.add(Restrictions.eq("noticeId", noticeId));
		return (List<LsSystemNoticeLogBO>)criteria.list();
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
		return (LsSystemNoticeLogBO)criteria.uniqueResult();
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
		return (List<LsSystemNoticeLogBO>)criteria.list();
	}
}
