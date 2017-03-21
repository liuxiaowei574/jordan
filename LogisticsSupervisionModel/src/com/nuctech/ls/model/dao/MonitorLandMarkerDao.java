package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.monitor.LsMonitorLandMarkerBO;

@Repository
public class MonitorLandMarkerDao extends LSBaseDao<LsMonitorLandMarkerBO, Serializable> {

    public int delLandMarkerByRAIds(String ids) {
        String sql = " delete from LS_MONITOR_LANDMARKER where LAND_ID in (" + ids + ")";
        return this.getSession().createSQLQuery(sql).executeUpdate();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List findByName(String landName) {
        Criteria crit = this.getSession().createCriteria(LsMonitorLandMarkerBO.class);
        crit.add(Restrictions.like("landName", landName, MatchMode.ANYWHERE));
        List<LsMonitorLandMarkerBO> list = crit.list();
        return list;
    }

}
