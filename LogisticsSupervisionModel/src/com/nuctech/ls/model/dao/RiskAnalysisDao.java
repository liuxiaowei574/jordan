package com.nuctech.ls.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.vo.analysis.RiskAnalysisVo;
import com.nuctech.util.NuctechUtil;

import javacommon.xsqlbuilder.XsqlBuilder;
import javacommon.xsqlbuilder.XsqlBuilder.XsqlFilterResult;

@SuppressWarnings("rawtypes")
@Repository
public class RiskAnalysisDao extends LSBaseDao {

    public PageList<RiskAnalysisVo> findLpnRiskAnalysisList(PageQuery<Map> pageQuery) {
        String queryString = "SELECT VEHICLE_PLATE_NUMBER, " + "COUNT (1) AS tripTotalNum, "
                + "SUM (tripAlarmNum) AS tripAlarmTotalNum, "
                + "SUM (tripSeriousAlarmNum) AS tripSeriousAlarmTotalNum, "
                + "SUM (tripMinorAlarmNum) AS tripMinorAlarmTotalNum, " + "SUM (alarmNum) AS alarmTotalNum, "
                + "SUM (seriousAlarmNum) AS seriousAlarmTotalNum, " + "SUM (minorAlarmNum) AS minorAlarmTotalNum "
                + "FROM ( SELECT t.TRIP_ID, v.VEHICLE_PLATE_NUMBER, "
                + "( SELECT COUNT (1) FROM LS_MONITOR_ALARM a WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' ) "
                + "AS alarmNum, "
                + "( SELECT COUNT (1) FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, LS_DM_ALARM_LEVEL al "
                + "WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' "
                + "AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID "
                + "AND al.ALARM_LEVEL_CODE = '0' ) AS minorAlarmNum, "
                + "( SELECT COUNT (1) FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, LS_DM_ALARM_LEVEL al "
                + "WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID "
                + "AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID AND al.ALARM_LEVEL_CODE = '1' ) AS seriousAlarmNum, "
                + "tripAlarmNum = ( CASE WHEN EXISTS ( SELECT * FROM LS_MONITOR_ALARM a WHERE a.TRIP_ID = t.TRIP_ID "
                + "AND t.TRIP_STATUS = '3' ) THEN 1 ELSE 0 END ), "
                + "tripMinorAlarmNum = ( CASE WHEN EXISTS ( SELECT * FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, "
                + "LS_DM_ALARM_LEVEL al WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' "
                + "AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID "
                + "AND al.ALARM_LEVEL_CODE = '0' ) THEN 1 ELSE 0 END ), "
                + "tripSeriousAlarmNum = ( CASE WHEN EXISTS ( SELECT * FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, "
                + "LS_DM_ALARM_LEVEL al WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' "
                + "AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID "
                + "AND al.ALARM_LEVEL_CODE = '1' ) " + "THEN 1 ELSE 0 END ) "
                + "FROM LS_COMMON_VEHICLE v, LS_MONITOR_TRIP t WHERE v.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' ) "
                + "tmp GROUP BY VEHICLE_PLATE_NUMBER";
        // + "/~ and t.userId = '[userId]' ~/"
        // + "/~ and t.userAccount like '%[userAccount]%' ~/"
        // + "/~ and t.userName like '%[userName]%' ~/"
        // + "/~ and t.userPhone like '%[userPhone]%' ~/"
        // + "/~ and t.userEmail like '%[userEmail]%' ~/"
        // + "/~ order by [sortColumns] ~/";

        XsqlBuilder builder = this.getXsqlBuilder();
        Map filtersMap = pageQuery.getFilters();
        XsqlFilterResult queryXsqlResult = builder.generateHql(queryString, filtersMap);
        Query query = this.sessionFactory.getCurrentSession().createSQLQuery(queryXsqlResult.getXsql());
        List list = query.list();
        if (list != null && list.size() > 0) {
            PageList<RiskAnalysisVo> result = new PageList<RiskAnalysisVo>();
            for (int i = 0; i < list.size(); i++) {
                Object[] arr = (Object[]) list.get(i);
                String riskId = arr[0].toString();
                long tripTotalNum = Long.parseLong(arr[1].toString());
                long tripAlarmTotalNum = Long.parseLong(arr[2].toString());
                long tripSeriousAlarmTotalNum = Long.parseLong(arr[3].toString());
                long tripMinorAlarmTotalNum = Long.parseLong(arr[4].toString());
                long alarmTotalNum = Long.parseLong(arr[5].toString());
                long seriousAlarmTotalNum = Long.parseLong(arr[6].toString());
                long minorAlarmTotalNum = Long.parseLong(arr[7].toString());
                result.add(new RiskAnalysisVo(riskId, tripTotalNum, tripAlarmTotalNum, tripSeriousAlarmTotalNum,
                        tripMinorAlarmTotalNum, alarmTotalNum, seriousAlarmTotalNum, minorAlarmTotalNum));
            }
            return result;
        }
        return null;
    }

    public PageList<RiskAnalysisVo> findDriverRiskAnalysisList(PageQuery<Map> pageQuery) {
        String queryString = "SELECT DRIVER_NAME, " + "COUNT (1) AS tripTotalNum, "
                + "SUM (tripAlarmNum) AS tripAlarmTotalNum, "
                + "SUM (tripSeriousAlarmNum) AS tripSeriousAlarmTotalNum, "
                + "SUM (tripMinorAlarmNum) AS tripMinorAlarmTotalNum, "
                + "SUM (alarmNum) AS alarmTotalNum, SUM (seriousAlarmNum) AS seriousAlarmTotalNum, "
                + "SUM (minorAlarmNum) AS minorAlarmTotalNum " + "FROM ( SELECT t.TRIP_ID, dr.DRIVER_NAME, "
                + "( SELECT COUNT (1) FROM LS_MONITOR_ALARM a WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' ) "
                + "AS alarmNum, "
                + "( SELECT COUNT (1) FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, LS_DM_ALARM_LEVEL al "
                + "WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' "
                + "AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID "
                + "AND al.ALARM_LEVEL_CODE = '0' ) AS minorAlarmNum, "
                + "( SELECT COUNT (1) FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, LS_DM_ALARM_LEVEL al "
                + "WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID "
                + "AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID AND al.ALARM_LEVEL_CODE = '1' ) AS seriousAlarmNum, "
                + "tripAlarmNum = ( CASE WHEN EXISTS ( SELECT * FROM LS_MONITOR_ALARM a "
                + "WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' ) THEN 1 ELSE 0 END ), "
                + "tripMinorAlarmNum = ( CASE WHEN EXISTS ( SELECT * FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, "
                + "LS_DM_ALARM_LEVEL al WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' "
                + "AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID "
                + "AND al.ALARM_LEVEL_CODE = '0' ) THEN 1 ELSE 0 END ), "
                + "tripSeriousAlarmNum = ( CASE WHEN EXISTS ( SELECT * FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, "
                + "LS_DM_ALARM_LEVEL al WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' "
                + "AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID "
                + "AND al.ALARM_LEVEL_CODE = '1' ) THEN 1 ELSE 0 END ) "
                + "FROM LS_COMMON_VEHICLE v, LS_MONITOR_TRIP t, LS_COMMON_DRIVER dr "
                + "WHERE v.TRIP_ID = t.TRIP_ID AND v.DRIVER_ID = dr.DRIVER_ID AND t.TRIP_STATUS = '3' ) "
                + "tmp GROUP BY DRIVER_NAME";
        // + "/~ and t.userId = '[userId]' ~/"
        // + "/~ and t.userAccount like '%[userAccount]%' ~/"
        // + "/~ and t.userName like '%[userName]%' ~/"
        // + "/~ and t.userPhone like '%[userPhone]%' ~/"
        // + "/~ and t.userEmail like '%[userEmail]%' ~/"
        // + "/~ order by [sortColumns] ~/";

        XsqlBuilder builder = this.getXsqlBuilder();
        Map filtersMap = pageQuery.getFilters();
        XsqlFilterResult queryXsqlResult = builder.generateHql(queryString, filtersMap);
        Query query = this.sessionFactory.getCurrentSession().createSQLQuery(queryXsqlResult.getXsql());
        List list = query.list();
        if (list != null && list.size() > 0) {
            PageList<RiskAnalysisVo> result = new PageList<RiskAnalysisVo>();
            for (int i = 0; i < list.size(); i++) {
                Object[] arr = (Object[]) list.get(i);
                String riskId = arr[0].toString();
                long tripTotalNum = Long.parseLong(arr[1].toString());
                long tripAlarmTotalNum = Long.parseLong(arr[2].toString());
                long tripSeriousAlarmTotalNum = Long.parseLong(arr[3].toString());
                long tripMinorAlarmTotalNum = Long.parseLong(arr[4].toString());
                long alarmTotalNum = Long.parseLong(arr[5].toString());
                long seriousAlarmTotalNum = Long.parseLong(arr[6].toString());
                long minorAlarmTotalNum = Long.parseLong(arr[7].toString());
                result.add(new RiskAnalysisVo(riskId, tripTotalNum, tripAlarmTotalNum, tripSeriousAlarmTotalNum,
                        tripMinorAlarmTotalNum, alarmTotalNum, seriousAlarmTotalNum, minorAlarmTotalNum));
            }
            return result;
        }
        return null;
    }

    public RiskAnalysisVo findLpnRiskAnalysisByVehiclePlateNum(PageQuery<Map> pageQuery) {
        Map filtersMap = pageQuery.getFilters();
        if (NuctechUtil.isNull(filtersMap.get("vehiclePlateNumber"))) {
            logger.info("findLpnRiskAnalysisByVehiclePlateNum: vehiclePlateNumber is null!");
            return null;
        }
        String queryString = "SELECT VEHICLE_PLATE_NUMBER, "
                + "COUNT (1) AS tripTotalNum, SUM (tripAlarmNum) AS tripAlarmTotalNum, "
                + "SUM (tripSeriousAlarmNum) AS tripSeriousAlarmTotalNum, "
                + "SUM (tripMinorAlarmNum) AS tripMinorAlarmTotalNum, " + "SUM (alarmNum) AS alarmTotalNum, "
                + "SUM (seriousAlarmNum) AS seriousAlarmTotalNum, " + "SUM (minorAlarmNum) AS minorAlarmTotalNum "
                + "FROM ( SELECT t.TRIP_ID, v.VEHICLE_PLATE_NUMBER, "
                + "( SELECT COUNT (1) FROM LS_MONITOR_ALARM a WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' ) "
                + "AS alarmNum, "
                + "( SELECT COUNT (1) FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, LS_DM_ALARM_LEVEL al "
                + "WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' "
                + "AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID "
                + "AND al.ALARM_LEVEL_CODE = '0' ) AS minorAlarmNum, "
                + "( SELECT COUNT (1) FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, LS_DM_ALARM_LEVEL al "
                + "WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID "
                + "AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID AND al.ALARM_LEVEL_CODE = '1' ) AS seriousAlarmNum, "
                + "tripAlarmNum = ( CASE WHEN EXISTS ( SELECT * FROM LS_MONITOR_ALARM a "
                + "WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' ) THEN 1 ELSE 0 END ), "
                + "tripMinorAlarmNum = ( CASE WHEN EXISTS ( SELECT * FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, "
                + "LS_DM_ALARM_LEVEL al WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' "
                + "AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID "
                + "AND al.ALARM_LEVEL_CODE = '0' ) THEN 1 ELSE 0 END ), "
                + "tripSeriousAlarmNum = ( CASE WHEN EXISTS ( SELECT * FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, "
                + "LS_DM_ALARM_LEVEL al WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' "
                + "AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID "
                + "AND al.ALARM_LEVEL_CODE = '1' ) THEN 1 ELSE 0 END ) "
                + "FROM LS_COMMON_VEHICLE v, LS_MONITOR_TRIP t WHERE v.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' "
                + " AND v.VEHICLE_PLATE_NUMBER = '" + filtersMap.get("vehiclePlateNumber") + "' "
                + " ) tmp GROUP BY  VEHICLE_PLATE_NUMBER";

        XsqlBuilder builder = this.getXsqlBuilder();
        XsqlFilterResult queryXsqlResult = builder.generateHql(queryString, filtersMap);
        Query query = this.sessionFactory.getCurrentSession().createSQLQuery(queryXsqlResult.getXsql());
        List list = query.list();
        if (list != null && list.size() > 0) {
            Object[] arr = (Object[]) list.get(0);
            String riskId = arr[0].toString();
            long tripTotalNum = Long.parseLong(arr[1].toString());
            long tripAlarmTotalNum = Long.parseLong(arr[2].toString());
            long tripSeriousAlarmTotalNum = Long.parseLong(arr[3].toString());
            long tripMinorAlarmTotalNum = Long.parseLong(arr[4].toString());
            long alarmTotalNum = Long.parseLong(arr[5].toString());
            long seriousAlarmTotalNum = Long.parseLong(arr[6].toString());
            long minorAlarmTotalNum = Long.parseLong(arr[7].toString());

            RiskAnalysisVo result = new RiskAnalysisVo(riskId, tripTotalNum, tripAlarmTotalNum,
                    tripSeriousAlarmTotalNum, tripMinorAlarmTotalNum, alarmTotalNum, seriousAlarmTotalNum,
                    minorAlarmTotalNum);
            return result;
        }
        return null;
    }

    public RiskAnalysisVo findDriverRiskAnalysisByDriverName(PageQuery<Map> pageQuery) {
        Map filtersMap = pageQuery.getFilters();
        if (NuctechUtil.isNull(filtersMap.get("driverName"))) {
            logger.info("findDriverRiskAnalysisByDriverName: driverName is null!");
            return null;
        }
        String queryString = "SELECT DRIVER_NAME, "
                + "COUNT (1) AS tripTotalNum, SUM (tripAlarmNum) AS tripAlarmTotalNum, "
                + "SUM (tripSeriousAlarmNum) AS tripSeriousAlarmTotalNum, "
                + "SUM (tripMinorAlarmNum) AS tripMinorAlarmTotalNum, " + "SUM (alarmNum) AS alarmTotalNum, "
                + "SUM (seriousAlarmNum) AS seriousAlarmTotalNum, " + "SUM (minorAlarmNum) AS minorAlarmTotalNum "
                + "FROM ( SELECT t.TRIP_ID, dr.DRIVER_NAME, " + "( SELECT COUNT (1) FROM LS_MONITOR_ALARM a "
                + "WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' ) AS alarmNum, "
                + "( SELECT COUNT (1) FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, "
                + "LS_DM_ALARM_LEVEL al WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' "
                + "AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID "
                + "AND al.ALARM_LEVEL_CODE = '0' ) AS minorAlarmNum, "
                + "( SELECT COUNT (1) FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, LS_DM_ALARM_LEVEL al "
                + "WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID "
                + "AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID AND al.ALARM_LEVEL_CODE = '1' ) AS seriousAlarmNum, "
                + "tripAlarmNum = ( CASE WHEN EXISTS ( SELECT * FROM LS_MONITOR_ALARM a "
                + "WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' ) THEN 1 ELSE 0 END ), "
                + "tripMinorAlarmNum = ( CASE WHEN EXISTS ( SELECT * FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, "
                + "LS_DM_ALARM_LEVEL al WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' "
                + "AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID "
                + "AND al.ALARM_LEVEL_CODE = '0' ) " + "THEN 1 ELSE 0 END ), "
                + "tripSeriousAlarmNum = ( CASE WHEN EXISTS ( SELECT * FROM LS_MONITOR_ALARM a, LS_DM_ALARM_TYPE aty, "
                + "LS_DM_ALARM_LEVEL al WHERE a.TRIP_ID = t.TRIP_ID AND t.TRIP_STATUS = '3' "
                + "AND a.ALARM_TYPE_ID = aty.ALARM_TYPE_ID "
                + "AND aty.ALARM_LEVEL_ID = al.ALARM_LEVEL_ID AND al.ALARM_LEVEL_CODE = '1' ) "
                + "THEN 1 ELSE 0 END ) FROM LS_COMMON_VEHICLE v, "
                + "LS_MONITOR_TRIP t, LS_COMMON_DRIVER dr WHERE v.TRIP_ID = t.TRIP_ID "
                + "AND v.DRIVER_ID = dr.DRIVER_ID AND t.TRIP_STATUS = '3' " + " AND dr.DRIVER_NAME = '"
                + filtersMap.get("driverName") + "' " + "  ) tmp GROUP BY DRIVER_NAME";

        XsqlBuilder builder = this.getXsqlBuilder();
        XsqlFilterResult queryXsqlResult = builder.generateHql(queryString, filtersMap);
        Query query = this.sessionFactory.getCurrentSession().createSQLQuery(queryXsqlResult.getXsql());
        List list = query.list();
        if (list != null && list.size() > 0) {
            Object[] arr = (Object[]) list.get(0);
            String riskId = arr[0].toString();
            long tripTotalNum = Long.parseLong(arr[1].toString());
            long tripAlarmTotalNum = Long.parseLong(arr[2].toString());
            long tripSeriousAlarmTotalNum = Long.parseLong(arr[3].toString());
            long tripMinorAlarmTotalNum = Long.parseLong(arr[4].toString());
            long alarmTotalNum = Long.parseLong(arr[5].toString());
            long seriousAlarmTotalNum = Long.parseLong(arr[6].toString());
            long minorAlarmTotalNum = Long.parseLong(arr[7].toString());

            RiskAnalysisVo result = new RiskAnalysisVo(riskId, tripTotalNum, tripAlarmTotalNum,
                    tripSeriousAlarmTotalNum, tripMinorAlarmTotalNum, alarmTotalNum, seriousAlarmTotalNum,
                    minorAlarmTotalNum);
            return result;
        }
        return null;
    }

}
