package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

/**
 * 
 * @author liqingxian
 *
 */
@Repository
public class MonitorRouteAreaDao extends LSBaseDao<LsMonitorRouteAreaBO, Serializable> {

    /**
     * 查找所有的路线或区域
     * 
     * @return
     * @throws Exception
     */
    public List<LsMonitorRouteAreaBO> findAllRouteAreas(String menuType, String routeName) throws Exception {

        Criteria crit = this.getSession().createCriteria(LsMonitorRouteAreaBO.class);
        if (NuctechUtil.isNotNull(menuType)) {
            if (Constant.BUTTON_TYPE_LINE.equals(menuType)) {
                crit.add(Restrictions.or(Restrictions.eq("routeAreaType", Constant.ROUTEAREA_TYPE_LINE)));
            } else if (Constant.BUTTON_TYPE_CDGL.equals(menuType)) {
                crit.add(Restrictions.or(Restrictions.eq("routeAreaType", Constant.ROUTEAREA_TYPE_WXQY),
                        Restrictions.eq("routeAreaType", Constant.ROUTEAREA_TYPE_JGQY),
                        Restrictions.eq("routeAreaType", Constant.ROUTEAREA_TYPE_AQQY),
                        Restrictions.eq("routeAreaType", Constant.ROUTEAREA_TYPE_QYHF),
                        Restrictions.eq("routeAreaType", Constant.ROUTEAREA_TYPE_TARGET)));
                // crit.add(Restrictions.sqlRestriction("routeAreaType in (?)",
                // "2,1,4", StringType.INSTANCE));
            } else {

            }
        }
        if (NuctechUtil.isNotNull(routeName)) {
            crit.add(Restrictions.like("routeAreaName", routeName, MatchMode.ANYWHERE));
        }

        // crit.addOrder(Order.desc("createTime"));
        crit.addOrder(Order.asc("routeAreaName"));
        // 查询结果
        @SuppressWarnings("unchecked")
        List<LsMonitorRouteAreaBO> list = crit.list();
        // List<LsMonitorRouteAreaBO> list = findAll();
        return list;
    }

    /**
     * 插入或更新线路区域
     * 
     * @param lsMonitorRouteAreaBO
     */
    public void addMonitorRouteArea(LsMonitorRouteAreaBO lsMonitorRouteAreaBO) {
        persist(lsMonitorRouteAreaBO);
    }

    public int delRouteAreaByRAIds(String ids) {
        String sql = " delete from LS_MONITOR_ROUTE_AREA where ROUTE_AREA_ID in (" + ids + ")";
        return this.getSession().createSQLQuery(sql).executeUpdate();
    }

    /**
     * 按照距离倒叙
     * 
     * @param distancePortId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsMonitorRouteAreaBO> findMonitorRouteAreaOrderByDistance(String distancePortId) {
        Criteria criteria = this.getSession().createCriteria(LsMonitorRouteAreaBO.class);
        criteria.add(Restrictions.eq("endId", distancePortId));
        criteria.addOrder(Order.asc("routeDistance"));
        return criteria.list();
    }

    /**
     * 查找所有的路线或区域
     * 
     * @return
     * @throws Exception
     */
    public List<LsMonitorRouteAreaBO> findAllPatrolArea(String routeAreaType) {
        return findAllPatrolArea(routeAreaType, null);
    }

    /**
     * 查找所有的路线或区域
     * 
     * @param routeAreaType
     *        区域类型
     * @param belongToPort
     *        所属口岸。只有规划路线、监管区域、target zoon有所属区域
     * @return
     */
    public List<LsMonitorRouteAreaBO> findAllPatrolArea(String routeAreaType, String belongToPort) {
        Criteria crit = this.getSession().createCriteria(LsMonitorRouteAreaBO.class);
        crit.add(Restrictions.eq("routeAreaType", routeAreaType));
        if (NuctechUtil.isNotNull(belongToPort)) {
            crit.add(Restrictions.eq("belongToPort", belongToPort));
        }
        crit.addOrder(Order.desc("createTime"));
        // 查询结果
        @SuppressWarnings("unchecked")
        List<LsMonitorRouteAreaBO> list = crit.list();
        return list;
    }

    /**
     * 查找属于某个口岸下的所有监管区域
     * 
     * @param belongToPort
     * @return
     */
    public List<LsMonitorRouteAreaBO> findJGQYByPort(String belongToPort) {
        return findAllPatrolArea(Constant.ROUTEAREA_TYPE_JGQY, belongToPort);
    }

    /**
     * 查找属于某个口岸下的所有Target zoon
     * 
     * @param belongToPort
     * @return
     */
    public List<LsMonitorRouteAreaBO> findTargetZoonByPort(String belongToPort) {
        return findAllPatrolArea(Constant.ROUTEAREA_TYPE_TARGET, belongToPort);
    }

    /**
     * 根据线路区域名称查询线路区域
     * 
     * @param routeAreaName
     * @return
     */
    public List<LsMonitorRouteAreaBO> findRouteAreaByName(String routeAreaName) {
        Criteria crit = this.getSession().createCriteria(LsMonitorRouteAreaBO.class);
        crit.add(Restrictions.like("routeAreaName", "%" + routeAreaName + "%"));
        crit.addOrder(Order.desc("createTime"));
        @SuppressWarnings("unchecked")
        List<LsMonitorRouteAreaBO> list = crit.list();
        return list;
    }
}
