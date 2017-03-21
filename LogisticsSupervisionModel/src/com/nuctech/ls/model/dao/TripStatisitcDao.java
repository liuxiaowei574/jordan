package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.vo.statistic.TripStaticVo;
import com.nuctech.util.DateUtils;
import com.nuctech.util.NuctechUtil;

/**
 * @author liangpengfei
 * 
 *         行程监管统计报告
 *
 */
@Repository
public class TripStatisitcDao extends LSBaseDao<LsSystemDepartmentBO, Serializable> {

    /**
     * @param deptid
     * @return
     *         汇总口岸行程信息 检入统计
     * 
     */
    @SuppressWarnings("rawtypes")
    public PageList<TripStaticVo> checkCount(PageQuery<Map> pageQuery) {
        List<TripStaticVo> ins = checkinCount(pageQuery);
        List<TripStaticVo> outs = checkoutCount(pageQuery);
        List<TripStaticVo> alls = new ArrayList<TripStaticVo>();
        PageList<TripStaticVo> result = new PageList<TripStaticVo>();
        for (TripStaticVo out : outs) {
            boolean joinleft = true;
            for (TripStaticVo in : ins) {
                if (ObjectUtils.nullSafeEquals(out.getPortid(), in.getPortid())) {
                    joinleft = false;
                    in.setCheckouts(out.getCheckouts());
                    alls.add(in);

                }

            }
            if (joinleft) {
                out.setCheckins("0");
                alls.add(out);
            }

        }
        result.addAll(alls);
        result.setPage(pageQuery.getPage());
        result.setPageSize(pageQuery.getPageSize());
        result.setTotalItems(alls.size());
        return result;

    }

    /**
     * @param pageQuery
     * @return
     * 
     *         行程检入统计
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private List<TripStaticVo> checkinCount(PageQuery<Map> pageQuery) {
        List<TripStaticVo> result = new ArrayList<TripStaticVo>();
        StringBuffer sql = new StringBuffer(
                "select  d.ORGANIZATION_ID portid,d.ORGANIZATION_NAME portname,count(1) checkins");
        sql.append(" from LS_SYSTEM_DEPARTMENT d,LS_MONITOR_TRIP t");
        sql.append(" where d.ORGANIZATION_ID=t.CHECKIN_PORT ");
        if (StringUtils.isNotBlank(pageQuery.getFilters().get("starttime") + "")) {
            sql.append(" and t.CHECKIN_TIME>'");
            sql.append(pageQuery.getFilters().get("starttime").toString());
            sql.append("'");
        }
        if (StringUtils.isNotBlank(pageQuery.getFilters().get("endtime") + "")) {
            sql.append(" and t.CHECKIN_TIME<'");
            sql.append(pageQuery.getFilters().get("endtime").toString());
            sql.append("'");
        }
        if (StringUtils.isNotBlank(pageQuery.getFilters().get("portname") + "")) {
            sql.append(" and d.ORGANIZATION_ID like '%");
            sql.append(pageQuery.getFilters().get("portname").toString());
            sql.append("%'");
        }
        sql.append(" GROUP BY  d.ORGANIZATION_ID,d.ORGANIZATION_NAME ");
        if (StringUtils.isNotBlank(pageQuery.getSortColumns())) {
            sql.append(" order by ");
            sql.append(pageQuery.getSortColumns());
        }
        try {
            result = this.getSession().createSQLQuery(sql.toString()).addScalar("portid").addScalar("portname")
                    .addScalar("checkins", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.aliasToBean(TripStaticVo.class)).list();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @param pageQuery
     * @return
     * 
     *         行程检入统计
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<TripStaticVo> checkoutCount(PageQuery<Map> pageQuery) {
        List<TripStaticVo> result = new ArrayList<TripStaticVo>();
        StringBuffer sql = new StringBuffer(
                "select  d.ORGANIZATION_ID portid,d.ORGANIZATION_NAME portname,count(1) checkouts");
        sql.append(" from LS_SYSTEM_DEPARTMENT d,LS_MONITOR_TRIP t");
        sql.append(" where d.ORGANIZATION_ID=t.CHECKOUT_PORT ");
        if (StringUtils.isNotBlank(pageQuery.getFilters().get("starttime") + "")) {
            sql.append(" and t.CHECKOUT_TIME>'");
            sql.append(pageQuery.getFilters().get("starttime").toString());
            sql.append("'");
        }
        if (StringUtils.isNotBlank(pageQuery.getFilters().get("endtime") + "")) {
            sql.append(" and t.CHECKOUT_TIME<'");
            sql.append(pageQuery.getFilters().get("endtime").toString());
            sql.append("'");
        }
        if (StringUtils.isNotBlank(pageQuery.getFilters().get("portname") + "")) {
            sql.append(" and d.ORGANIZATION_ID like '%");
            sql.append(pageQuery.getFilters().get("portname").toString());
            sql.append("%'");
        }
        sql.append(" GROUP BY  d.ORGANIZATION_ID,d.ORGANIZATION_NAME ");
        if (StringUtils.isNotBlank(pageQuery.getSortColumns())) {
            sql.append(" order by ");
            sql.append(pageQuery.getSortColumns());
        }
        try {
            result = this.getSession().createSQLQuery(sql.toString()).addScalar("portid").addScalar("portname")
                    .addScalar("checkouts", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.aliasToBean(TripStaticVo.class)).list();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @return
     *         根据部门查询所有的行程
     * 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public PageList<TripStaticVo> tripDetail(PageQuery<Map> pageQuery) {
        String queryString = "select d,t ,'1'" + " from LsSystemDepartmentBO d,LsMonitorTripBO t"
                + " where d.organizationId=t.checkoutPort " + "/~ and d.organizationId like '%[portname]%' ~/ "
                + "/~ and t.checkoutTime > '[starttime]' ~/ " + "/~ and t.checkoutTime < '[endtime]' ~/ "
                + " union select d,t ,'0'" + " from LsSystemDepartmentBO d,LsMonitorTripBO t"
                + " where d.organizationId=t.checkinPort " + "/~ and d.organizationId like '%[portname]%' ~/ "
                + "/~ and t.checkinTime > '[starttime]' ~/ " + "/~ and t.checkinTime < '[endtime]' ~/ "
                + "/~ order by [sortColumns] ~/";

        PageList<Object> queryList = pageQuery(queryString, pageQuery);

        PageList<TripStaticVo> pageList = new PageList<TripStaticVo>();
        try {
            if (queryList != null && queryList.size() > 0) {
                LsSystemDepartmentBO dept = null;
                LsMonitorTripBO trip = null;
                for (Object obj : queryList) {
                    Object[] objs = (Object[]) obj;
                    dept = (LsSystemDepartmentBO) objs[0];
                    trip = (LsMonitorTripBO) objs[1];
                    TripStaticVo ts = new TripStaticVo();

                    ts.setPortid(dept.getOrganizationId());
                    ts.setPortname(dept.getOrganizationName());
                    if (NuctechUtil.isNotNull(trip.getCheckoutTime())) {
                        String dateStr = DateUtils.date2String(trip.getCheckoutTime(),
                                DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
                        ts.setCtime(dateStr);
                    }
                    //行程未结束时，检出时间为空，将检入时间赋给ctime
                    if(NuctechUtil.isNull(trip.getCheckoutTime())){
                        String dateStr = DateUtils.date2String(trip.getCheckinTime(),
                                DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
                        ts.setCtime(dateStr);
                    }
                    
                    if (ObjectUtils.nullSafeEquals(dept.getOrganizationId(), trip.getCheckinPort())) {
                        ts.setFlag("0");
                    } else if (ObjectUtils.nullSafeEquals(dept.getOrganizationId(), trip.getCheckoutPort())) {
                        ts.setFlag("1");
                    } else {
                        ts.setFlag("-1");
                    }

                    ts.setStatus(trip.getTripStatus());

                    pageList.add(ts);
                }
            }

            pageList.setPage(pageQuery.getPage());
            pageList.setPageSize(pageQuery.getPageSize());
            pageList.setTotalItems(queryList.getTotalItems());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageList;

    }
}
