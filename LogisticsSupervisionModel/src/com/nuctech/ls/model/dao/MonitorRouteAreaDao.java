package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
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
	 * @return
	 * @throws Exception
	 */
    public List<LsMonitorRouteAreaBO> findAllRouteAreas(String menuType) throws Exception{
    	
    	Criteria crit= this.getSession().createCriteria(LsMonitorRouteAreaBO.class);
    	if(NuctechUtil.isNotNull(menuType)){
    		if(Constant.BUTTON_TYPE_LINE.equals(menuType)){
    			crit.add(Restrictions.or(Restrictions.eq("routeAreaType", Constant.ROUTEAREA_TYPE_LINE)));
    		}else if(Constant.BUTTON_TYPE_CDGL.equals(menuType)){
    			crit.add(Restrictions.or(Restrictions.eq("routeAreaType", Constant.ROUTEAREA_TYPE_JGQY)));
    		}else if(Constant.BUTTON_TYPE_QYGL.equals(menuType)){
    			crit.add(Restrictions.or(Restrictions.eq("routeAreaType", Constant.ROUTEAREA_TYPE_WXQY), Restrictions.eq("routeAreaType", Constant.ROUTEAREA_TYPE_AQQY),Restrictions.eq("routeAreaType", Constant.ROUTEAREA_TYPE_QYHF)));
    			//crit.add(Restrictions.sqlRestriction("routeAreaType in (?)", "2,1,4", StringType.INSTANCE));
    		}
    	}
        crit.addOrder(Order.desc("createTime"));
        // 查询结果
        @SuppressWarnings("unchecked")
        List<LsMonitorRouteAreaBO> list = crit.list();
    	//List<LsMonitorRouteAreaBO> list = findAll();
        return list;
    }
    
    /**
     * 插入或更新线路区域
     * @param lsMonitorRouteAreaBO
     */
    public void addMonitorRouteArea(LsMonitorRouteAreaBO lsMonitorRouteAreaBO) {
    	persist(lsMonitorRouteAreaBO);
    }

	public int delRouteAreaByRAIds(String ids) {
		String sql = " delete from LS_MONITOR_ROUTE_AREA where ROUTE_AREA_ID in (" + ids + ")";
        return this.getSession().createSQLQuery(sql).executeUpdate();
	}
    
   
}
