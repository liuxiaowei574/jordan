package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseTrackUnitBO;
import com.nuctech.ls.model.vo.monitor.PatrolUserVO;
import com.nuctech.ls.model.vo.warehouse.PatrolDepartmentVO;
import com.nuctech.util.Constant;

@Repository
public class CommonPatrolDao extends LSBaseDao<LsCommonPatrolBO, Serializable> {

    /**
     * 
     * @param number
     * @return
     */
    public LsCommonPatrolBO findCommonPatrolByTrackUnitNumber(String trackUnitNumber) {
        Criteria criteria = getSession().createCriteria(LsCommonPatrolBO.class);
        criteria.add(Restrictions.eq("trackUnitNumber", trackUnitNumber));
        return (LsCommonPatrolBO) criteria.uniqueResult();
    }

    public int delPatrolsByIds(String ids) {
        String sql = " update LS_COMMON_PATROL set DELETE_MARK='" + Constant.MARK_DELETED + "' where PATROL_ID in ("
                + ids + ")";
        return this.getSession().createSQLQuery(sql).executeUpdate();
    }

    /**
     * 
     * @param patrolUser
     * @return
     */
    public LsCommonPatrolBO findCommonPatrolByPatrolUser(String potralUser) {
        Criteria criteria = getSession().createCriteria(LsCommonPatrolBO.class);
        criteria.add(Restrictions.eq("potralUser", potralUser));
        return (LsCommonPatrolBO) criteria.uniqueResult();
    }

    /**
     * 赵苏阳
     */
    // 查询巡逻队列表
    public List<PatrolDepartmentVO> getPatrolList() {
        String queryString = "select {p.*},{a.*},{u.*},{t.*},{s.*} "
                + "from LS_COMMON_PATROL p,LS_MONITOR_ROUTE_AREA a ,"
                + "LS_SYSTEM_USER u,LS_SYSTEM_USER s,LS_WAREHOUSE_TRACK_UNIT t where 1=1"
                + " and p.BELONG_TO_AREA=a.ROUTE_AREA_ID " + " and p.POTRAL_USER = u.USER_ID "
                + " and p.CREATE_USER = s.USER_ID " + " and p.TRACK_UNIT_NUMBER=t.TRACK_UNIT_NUMBER ";
        Query query = this.getSession().createSQLQuery(queryString).addEntity("p", LsCommonPatrolBO.class)
                .addEntity("a", LsMonitorRouteAreaBO.class).addEntity("u", LsSystemUserBO.class)
                .addEntity("t", LsWarehouseTrackUnitBO.class).addEntity("s", LsSystemUserBO.class);
        @SuppressWarnings("unchecked")
        List<Object[]> list = query.list();
        List<PatrolDepartmentVO> patrolList = new ArrayList<PatrolDepartmentVO>();
        for (int i = 0; i < list.size(); i++) {
            PatrolDepartmentVO patrolDepartmentVO = new PatrolDepartmentVO();
            patrolDepartmentVO.setPatrolId(((LsCommonPatrolBO) list.get(i)[0]).getPatrolId());
            patrolDepartmentVO.setCreateUser(((LsSystemUserBO) list.get(i)[4]).getUserName());
            patrolDepartmentVO.setPotralUser(((LsSystemUserBO) list.get(i)[2]).getUserName());
            patrolDepartmentVO.setRouteAreaName(((LsMonitorRouteAreaBO) list.get(i)[1]).getRouteAreaName());
            patrolDepartmentVO.setUserAccount(((LsSystemUserBO) list.get(i)[2]).getUserAccount());
            patrolDepartmentVO.setTrackUnitNumber(((LsWarehouseTrackUnitBO) list.get(i)[3]).getTrackUnitNumber());

            patrolList.add(patrolDepartmentVO);
        }
        return patrolList;
    }

    /**
     * 查询所有的巡逻队列表
     * 
     * @return
     */
    public List<PatrolUserVO> getPatrolUserList() {
        String queryString = "select {p.*},{a.*},{u.*},{t.*} "
                + "from LS_COMMON_PATROL p,LS_MONITOR_ROUTE_AREA a ,LS_SYSTEM_USER u,"
                + "LS_WAREHOUSE_TRACK_UNIT t where 1=1"
                + " and p.DELETE_MARK = '0' and p.BELONG_TO_AREA=a.ROUTE_AREA_ID " + " and p.POTRAL_USER = u.USER_ID "
                + " and p.TRACK_UNIT_NUMBER=t.TRACK_UNIT_NUMBER ";
        Query query = this.getSession().createSQLQuery(queryString).addEntity("p", LsCommonPatrolBO.class)
                .addEntity("a", LsMonitorRouteAreaBO.class).addEntity("u", LsSystemUserBO.class)
                .addEntity("t", LsWarehouseTrackUnitBO.class);
        @SuppressWarnings("unchecked")
        List<Object[]> list = query.list();
        List<PatrolUserVO> patrolList = new ArrayList<PatrolUserVO>();
        for (int i = 0; i < list.size(); i++) {
            PatrolUserVO patrolUserVO = new PatrolUserVO();
            LsCommonPatrolBO commonPatrolBO = (LsCommonPatrolBO) list.get(i)[0];
            BeanUtils.copyProperties(commonPatrolBO, patrolUserVO);
            patrolUserVO.setPatrolId(commonPatrolBO.getPatrolId());
            patrolUserVO.setCreateUser(commonPatrolBO.getCreateUser());
            patrolUserVO.setPotralUser(commonPatrolBO.getPotralUser());
            patrolUserVO.setPotralUserName(((LsSystemUserBO) list.get(i)[2]).getUserName());
            patrolUserVO.setTrackUnitNumber(((LsWarehouseTrackUnitBO) list.get(i)[3]).getTrackUnitNumber());

            patrolList.add(patrolUserVO);
        }
        return patrolList;
    }

    /**
     * 查询所有的护送巡逻队列表
     * 
     * @return
     */
    public List<PatrolUserVO> getEscortPatrolUserList() {
        String queryString = "select {p.*},{a.*},{u.*},{t.*} "
                + "from LS_COMMON_PATROL p,LS_MONITOR_ROUTE_AREA a ,LS_SYSTEM_USER u,"
                + "LS_WAREHOUSE_TRACK_UNIT t where 1=1"
                + " and p.DELETE_MARK = '0' and p.BELONG_TO_AREA=a.ROUTE_AREA_ID " + " and p.POTRAL_USER = u.USER_ID "
                + " and p.TRACK_UNIT_NUMBER=t.TRACK_UNIT_NUMBER AND p.patrolType='1' ";
        Query query = this.getSession().createSQLQuery(queryString).addEntity("p", LsCommonPatrolBO.class)
                .addEntity("a", LsMonitorRouteAreaBO.class).addEntity("u", LsSystemUserBO.class)
                .addEntity("t", LsWarehouseTrackUnitBO.class);
        @SuppressWarnings("unchecked")
        List<Object[]> list = query.list();
        List<PatrolUserVO> patrolList = new ArrayList<PatrolUserVO>();
        for (int i = 0; i < list.size(); i++) {
            PatrolUserVO patrolUserVO = new PatrolUserVO();
            LsCommonPatrolBO commonPatrolBO = (LsCommonPatrolBO) list.get(i)[0];
            BeanUtils.copyProperties(commonPatrolBO, patrolUserVO);
            patrolUserVO.setPatrolId(commonPatrolBO.getPatrolId());
            patrolUserVO.setCreateUser(commonPatrolBO.getCreateUser());
            patrolUserVO.setPotralUser(commonPatrolBO.getPotralUser());
            patrolUserVO.setPotralUserName(((LsSystemUserBO) list.get(i)[2]).getUserName());
            patrolUserVO.setTrackUnitNumber(((LsWarehouseTrackUnitBO) list.get(i)[3]).getTrackUnitNumber());

            patrolList.add(patrolUserVO);
        }
        return patrolList;
    }

    /**
     * 根据所属区域，和巡逻队用户的Id，查找指定的巡逻队集合
     * 
     * @param belongToArea
     * @param potralUserIds
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsCommonPatrolBO> findEscortPatrolByBelongArea(String belongToArea, String[] potralUserIds) {
        Criteria criteria = getSession().createCriteria(LsCommonPatrolBO.class);
        criteria.add(Restrictions.eq("belongToArea", belongToArea));
        criteria.add(Restrictions.in("potralUser", potralUserIds));
        return criteria.list();
    }

    /**
     * 根据行程Id查找关联的巡逻队
     * 
     * @param tripId
     * @return
     */
    public List<LsCommonPatrolBO> findAllByTripId(String tripId) {
        return findAllBy("tripId", tripId);
    }

}
