package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.dm.LsDmAlarmLevelBO;
import com.nuctech.ls.model.bo.dm.LsDmAlarmTypeBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.vo.alarm.AlarmLevelTypeVO;

@Repository
public class AlarmLevelModifyDao extends LSBaseDao<LsDmAlarmTypeBO, Serializable> {

    @Resource
    private SystemModules systemModules;

    @SuppressWarnings("rawtypes")
    public PageList<AlarmLevelTypeVO> findAllAlarmTypeLevel(PageQuery<Map> pageQuery) {
        PageList<AlarmLevelTypeVO> pageList = new PageList<>();
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        int firstRow = pageList.getFirstRecordIndex();
        int maxResults = pageQuery.getPageSize();

        String sql = "select {l.*}, {t.*}, {u.*} " + " from LS_DM_ALARM_LEVEL l, LS_DM_ALARM_TYPE t "
                + " left join LS_SYSTEM_USER u on t.UPDATE_USER=u.USER_ID "
                + " left join LS_SYSTEM_USER_ROLE r on u.USER_ID=r.USER_ID ";

        String countSql = "select count(t.ALARM_TYPE_ID) " + " from LS_DM_ALARM_LEVEL l, LS_DM_ALARM_TYPE t "
                + " left join LS_SYSTEM_USER u on t.UPDATE_USER=u.USER_ID "
                + " left join LS_SYSTEM_USER_ROLE r on u.USER_ID=r.USER_ID ";
        // left join 条件
        if (!systemModules.isPatrolOn()) {// 车载台和巡逻队模块不存在
            sql += " and r.ROLE_ID not in(7,9,10) ";
            countSql += " and r.ROLE_ID not in(7,9,10) ";
        }
        if (!systemModules.isRiskOn()) {// 风险分析模块不存在
            sql += " and r.ROLE_ID not in(6) ";
            countSql += " and r.ROLE_ID not in(6) ";
        }
        sql += " where l.ALARM_LEVEL_ID=t.ALARM_LEVEL_ID ";
        countSql += " where l.ALARM_LEVEL_ID=t.ALARM_LEVEL_ID ";
        // where 条件
        if (!systemModules.isAreaOn()) {// 区域场地模块不存在
            sql += " and t.ALARM_TYPE_CODE not in('ENTER_DANGEROUS_AREA','TARGET_ZOON') ";
            countSql += " and t.ALARM_TYPE_CODE not in('ENTER_DANGEROUS_AREA','TARGET_ZOON') ";
        }
        if (!systemModules.isPatrolOn()) {// 车载台和巡逻队模块不存在
            sql += " and t.ALARM_TYPE_CODE not in('TRACK_UNIT_ALARM') ";
            countSql += " and t.ALARM_TYPE_CODE not in('TRACK_UNIT_ALARM') ";
        }

        HttpServletRequest request = ServletActionContext.getRequest();
        String sortname = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        if ("userName".equals(sortname)) {
            sql += " order by u.USER_NAME ";
        } else if ("createTime".equals(sortname)) {
            sql += " order by t.CREATE_TIME ";
        } else if ("updateTime".equals(sortname)) {
            sql += " order by t.UPDATE_TIME ";
        } else if ("alarmLevelCode".equals(sortname)) {
            sql += " order by l.ALARM_LEVEL_CODE ";
        } else {
            sql += " order by t.UPDATE_TIME ";
        }
        if ("asc".equalsIgnoreCase(sortOrder)) {
            sql += " asc ";
        } else if ("desc".equalsIgnoreCase(sortOrder)) {
            sql += " desc ";
        } else {
            sql += " asc ";
        }

        Query countQuery = this.getSession().createSQLQuery(countSql);

        Query query = this.getSession().createSQLQuery(sql).addEntity("l", LsDmAlarmLevelBO.class)
                .addEntity("t", LsDmAlarmTypeBO.class).addEntity("u", LsSystemUserBO.class).setFirstResult(firstRow)
                .setMaxResults(maxResults);
        List<AlarmLevelTypeVO> alarmList = new ArrayList<AlarmLevelTypeVO>();
        @SuppressWarnings("unchecked")
        List<Object[]> list = query.list();
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                Object[] objs = (Object[]) obj;
                AlarmLevelTypeVO alarmLevelTypeVO = new AlarmLevelTypeVO();
                if (objs[0] != null) {
                    BeanUtils.copyProperties(objs[0], alarmLevelTypeVO);
                }
                if (objs[1] != null) {
                    BeanUtils.copyProperties(objs[1], alarmLevelTypeVO);
                }
                if (objs[2] != null) {
                    BeanUtils.copyProperties(objs[2], alarmLevelTypeVO);
                    alarmLevelTypeVO.setUserName(((LsSystemUserBO) objs[2]).getUserName());
                } else {
                    alarmLevelTypeVO.setUpdateTime(null);
                }
                alarmList.add(alarmLevelTypeVO);
            }
        }

        pageList.addAll(alarmList);
        pageList.setTotalItems(((Number) countQuery.uniqueResult()).intValue());
        return pageList;
    }

    /*
     * public LsDmAlarmTypeBO findByAlarmName(String alarmName){
     * String sql = "select t.* from LS_DM_ALARM_TYPE t WHERE t.ALARM_TYPE_NAME = '"+alarmName+"'";
     * Session session = this.getSession();
     * return (LsDmAlarmTypeBO) session.createQuery(sql);
     * }
     */
}
