package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemNoticeLogBO;
import com.nuctech.ls.model.bo.system.LsSystemNoticesBO;
import com.nuctech.ls.model.vo.system.SystemNoticeLogVO;

/**
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 通知 DAO
 * </p>
 * 创建时间：2016年5月27日
 */
@Repository
public class SystemNoticeDao extends LSBaseDao<LsSystemNoticesBO, Serializable> {

    @SuppressWarnings("unchecked")
    public List<LsSystemNoticesBO> findSystemNoticesListByUserId(String userId) {
        Criteria criteria = getSession().createCriteria(LsSystemNoticesBO.class);
        criteria.add(Restrictions.like("noticeUsers", '%' + userId + '%'));
        return criteria.list();
    }

    /**
     * 查询未处理的通知
     */
    @SuppressWarnings("unchecked")
    public List<SystemNoticeLogVO> findUnDealNotice(String userId) {
        String sql = "select t.*,l.* " + "from LS_SYSTEM_NOTICES t,LS_SYSTEM_NOTICE_LOG l "
                + "where 1=1  and t.NOTICE_ID=l.NOTICE_ID  and l.DEAL_TYPE= 0 and l.RECEIVE_USER='" + userId + "'";
        List<Object[]> list = this.getSession().createSQLQuery(sql).addEntity("t", LsSystemNoticesBO.class)
                .addEntity("l", LsSystemNoticeLogBO.class).list();
        List<SystemNoticeLogVO> systemNoticeLogVOs = new ArrayList<SystemNoticeLogVO>();
        for (int i = 0; i < list.size(); i++) {
            SystemNoticeLogVO systemNoticeLogVO = new SystemNoticeLogVO();

            systemNoticeLogVO.setNoticeId(((LsSystemNoticesBO) list.get(i)[0]).getNoticeId());
            systemNoticeLogVO.setNoticeContent(((LsSystemNoticesBO) list.get(i)[0]).getNoticeContent());
            systemNoticeLogVO.setNoticeTitle(((LsSystemNoticesBO) list.get(i)[0]).getNoticeTitle());
            systemNoticeLogVO.setDeployTime(((LsSystemNoticesBO) list.get(i)[0]).getDeployTime());
            systemNoticeLogVO.setPublisher(((LsSystemNoticesBO) list.get(i)[0]).getPublisher());

            systemNoticeLogVOs.add(systemNoticeLogVO);

        }
        return systemNoticeLogVOs;
    }

}
