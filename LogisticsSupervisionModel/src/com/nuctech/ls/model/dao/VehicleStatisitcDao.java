package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.vo.statistic.VehicleStaticVo;

/**
 * @author liangpengfei
 * 
 *         车辆监管统计报告
 *
 */
@Repository
public class VehicleStatisitcDao extends LSBaseDao<LsSystemDepartmentBO, Serializable> {

    /**
     * @param deptid
     * @return
     *         汇总口岸车辆信息
     * 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<VehicleStaticVo> countVehicle(PageQuery<Map> pageQuery) {
        List<VehicleStaticVo> result = new ArrayList<VehicleStaticVo>();
        StringBuffer sql = new StringBuffer(
                "select  d.ORGANIZATION_ID portid,d.ORGANIZATION_NAME portname,count(1) number");
        sql.append(" from LS_COMMON_VEHICLE v,LS_SYSTEM_DEPARTMENT d,LS_MONITOR_TRIP t");
        sql.append(" where v.trip_id=t.trip_id");
        sql.append(" and d.ORGANIZATION_ID=t.CHECKIN_PORT and v.vehicle_plate_number is not null");
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
            sql.append(" and d.ORGANIZATION_NAME like '%");
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
                    .addScalar("number", StandardBasicTypes.STRING)
                    .setResultTransformer(Transformers.aliasToBean(VehicleStaticVo.class)).list();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return result;

    }

    /**
     * @param deptid
     * @return
     *         根据部门汇总车辆信息
     * 
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public PageList<VehicleStaticVo> countVehicleDetail(PageQuery<Map> pageQuery) {
        String queryString = "select v,d,t " + " from LsCommonVehicleBO v,LsSystemDepartmentBO d,LsMonitorTripBO t"
                + " where v.tripId=t.tripId "
                + " and d.organizationId=t.checkinPort and v.vehiclePlateNumber is not null "
                + "/~ and d.organizationName like '%[portname]%' ~/ " + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = pageQuery(queryString, pageQuery);

        PageList<VehicleStaticVo> pageList = new PageList<VehicleStaticVo>();
        try {
            if (queryList != null && queryList.size() > 0) {
                for (Object obj : queryList) {

                    Object[] objs = (Object[]) obj;
                    VehicleStaticVo vvo = new VehicleStaticVo();
                    BeanUtils.copyProperties(objs[0], vvo);
                    vvo.setPortname(((LsSystemDepartmentBO) objs[1]).getOrganizationName());
                    vvo.setCheckoutTime(ObjectUtils.nullSafeToString(((LsMonitorTripBO) objs[2]).getCheckinTime()));
                    pageList.add(vvo);
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
