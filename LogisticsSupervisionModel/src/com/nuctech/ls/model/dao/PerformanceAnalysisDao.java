package com.nuctech.ls.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.vo.analysis.PerformanceAnalysisVo;

import javacommon.xsqlbuilder.XsqlBuilder;
import javacommon.xsqlbuilder.XsqlBuilder.XsqlFilterResult;
import net.sf.json.JSONObject;

@SuppressWarnings("rawtypes")
@Repository
public class PerformanceAnalysisDao extends LSBaseDao {

    public List<PerformanceAnalysisVo> findUserOnline(PageQuery<Map> pageQuery) {
        String queryString = "SELECT u.USER_ID, u.USER_ACCOUNT, u.USER_NAME, "
                + "SUM(DATEDIFF(MINUTE, l.LOGON_TIME,l.LOGOUT_TIME)) FROM "
                + "LS_SYSTEM_USER u,LS_SYSTEM_USER_LOG l WHERE "
                + "u.USER_ID = l.LOG_USER AND l.LOGOUT_TIME IS NOT NULL AND l.LOGON_TIME IS NOT NULL "
                + "/~ AND l.LOGON_TIME >= '[dateStart]' ~/" + "/~ AND l.LOGON_TIME <= '[dateEnd]' ~/"
                + " GROUP BY u.USER_ID, u.USER_ACCOUNT, u.USER_NAME";
        XsqlBuilder builder = this.getXsqlBuilder();
        Map filtersMap = pageQuery.getFilters();
        XsqlFilterResult queryXsqlResult = builder.generateHql(queryString, filtersMap);
        Query query = this.sessionFactory.getCurrentSession().createSQLQuery(queryXsqlResult.getXsql());
        List list = query.list();
        if (list != null && list.size() > 0) {
            List<PerformanceAnalysisVo> result = new ArrayList<PerformanceAnalysisVo>();
            for (int i = 0; i < list.size(); i++) {
                Object[] arr = (Object[]) list.get(i);
                String userId = arr[0].toString();
                String userAccount = arr[1].toString();
                String userName = arr[2].toString();
                String timeLong = arr[3].toString();
                long onlineTime = Long.parseLong(timeLong);
                result.add(new PerformanceAnalysisVo(userId, userAccount, userName, onlineTime));
            }
            return result;
        }
        return null;
    }

    /**
     * @param pageQuery
     * @return
     * 
     *         巡逻队在线时长统计
     */
    public List<PerformanceAnalysisVo> findPatrolUserOnline(PageQuery<Map> pageQuery) {
        String queryString = "select users.USER_ID,users.USER_ACCOUNT,users.USER_NAME,"
                + "SUM(DATEDIFF(MINUTE, ulog.LOGON_TIME, ulog.LOGOUT_TIME))"
                + " from LS_SYSTEM_USER users ,LS_COMMON_PATROL patrol,LS_SYSTEM_USER_LOG ulog"
                + " where users.user_id = patrol.potral_user" + " and users.USER_ID=ulog.LOG_USER"
                + "/~ AND ulog.LOGON_TIME >= '[timeStart]' ~/" + "/~ AND ulog.LOGON_TIME <= '[timeEnd]' ~/"
                + " GROUP BY users.USER_ID,users.USER_ACCOUNT,users.USER_NAME";

        XsqlBuilder builder = this.getXsqlBuilder();
        Map filtersMap = pageQuery.getFilters();
        XsqlFilterResult queryXsqlResult = builder.generateHql(queryString, filtersMap);
        Query query = this.sessionFactory.getCurrentSession().createSQLQuery(queryXsqlResult.getXsql());
        List list = query.list();
        if (list != null && list.size() > 0) {
            List<PerformanceAnalysisVo> result = new ArrayList<PerformanceAnalysisVo>();
            for (int i = 0; i < list.size(); i++) {
                Object[] arr = (Object[]) list.get(i);
                String userId = ObjectUtils.nullSafeToString(arr[0]);
                String userAccount = ObjectUtils.nullSafeToString(arr[1]);
                String userName = ObjectUtils.nullSafeToString(arr[2]);
                String timeLong = ObjectUtils.nullSafeToString(arr[3]);
                long onlineTime = "null".equals(timeLong) ? 0 : Long.parseLong(timeLong);
                result.add(new PerformanceAnalysisVo(userId, userAccount, userName, onlineTime));
            }
            return result;
        }
        return null;
    }

    public JSONObject findDealAlarm() {
        String queryString = "SELECT u.USER_ID, u.USER_ACCOUNT, u.USER_NAME, ad.DEAL_METHOD, "
                + "SUM(DATEDIFF(MINUTE, ad.RECEIVE_TIME, ad.DEAL_TIME)) as dealtime, count(*) as total "
                + "FROM LS_SYSTEM_USER u, LS_MONITOR_ALARM_DEAL ad "
                + "WHERE u.USER_ID = ad.DEAL_USER AND ad.RECEIVE_TIME IS NOT NULL AND ad.DEAL_TIME IS NOT NULL "
                + "GROUP BY u.USER_ID, u.USER_ACCOUNT, u.USER_NAME, ad.DEAL_METHOD";
        Query query = this.sessionFactory.getCurrentSession().createSQLQuery(queryString);
        List<?> list = query.list();
        JSONObject retJson = new JSONObject();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] arr = (Object[]) list.get(i);
                String key = String.format("%s%s%s", arr[0].toString(), arr[1].toString(), arr[2].toString());
                String subKey = arr[3].toString();
                JSONObject json = new JSONObject();
                json.put("time", arr[4].toString());
                json.put("sum", arr[5].toString());
                JSONObject subJson = null;
                if (!retJson.containsKey(key)) {
                    subJson = new JSONObject();
                } else {
                    subJson = retJson.getJSONObject(key);
                }
                subJson.put(subKey, json);
                retJson.put(key, subJson);
            }
        }
        return retJson;
    }

    /**
     * @return
     * 
     *         统计巡逻对人员的处理次数
     */
    public JSONObject findPatrolDealAlarm() {
        String queryString = "select users.USER_ID,users.USER_ACCOUNT,users.USER_NAME,deal.DEAL_METHOD,COUNT(*) times"
                + " from LS_SYSTEM_USER users ,LS_COMMON_PATROL patrol,LS_MONITOR_ALARM_DEAL deal"
                + " where users.user_id = patrol.potral_user" + " and users.USER_ID=deal.DEAL_USER"
                + " GROUP BY users.USER_ID,users.USER_ACCOUNT,users.USER_NAME, deal.DEAL_METHOD";
        Query query = this.sessionFactory.getCurrentSession().createSQLQuery(queryString);
        List list = query.list();
        JSONObject retJson = new JSONObject();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] arr = (Object[]) list.get(i);
                String key = String.format("%s%s%s", arr[0].toString(), arr[1].toString(), arr[2].toString());
                String subKey = arr[3].toString();
                JSONObject json = new JSONObject();
                json.put("sum", arr[4].toString());
                // json.put("sum", arr[5].toString());
                JSONObject subJson = null;
                if (!retJson.containsKey(key)) {
                    subJson = new JSONObject();
                } else {
                    subJson = retJson.getJSONObject(key);
                }
                subJson.put(subKey, json);
                retJson.put(key, subJson);
            }
        }
        return retJson;
    }

    /**
     * @param pageQuery
     * @return
     * 
     *         口岸人员的绩效指标统计
     * 
     *         1，在线时长
     *         2，平均在线时长
     * 
     */
    public List<PerformanceAnalysisVo> findPortUserOnline(PageQuery<Map> pageQuery) {
        String queryString = "select a.ORGANIZATION_ID ,a.ORGANIZATION_SHORT ,a.ORGANIZATION_NAME ,"
                + " SUM(a.sum1) sum1," + " SUM(a.avg) avg from ("
                + " select dept.ORGANIZATION_ID ,dept.ORGANIZATION_SHORT ,dept.ORGANIZATION_NAME ,"
                + " SUM(DATEDIFF(minute, ulog.LOGON_TIME, ulog.LOGOUT_TIME)) sum1,"
                + " SUM(DATEDIFF(minute,ulog.LOGON_TIME,ulog.LOGOUT_TIME))/count(*) avg"
                + " from LS_SYSTEM_DEPARTMENT as dept," + " LS_SYSTEM_USER as users," + " LS_SYSTEM_USER_LOG as ulog,"
                + " LS_SYSTEM_ORGANIZATION_USER as ou" + " where dept.ORGANIZATION_ID=ou.ORGANIZATION_ID"
                + " and users.USER_ID=ou.USER_ID" + " and users.USER_ID=ulog.LOG_USER"
                + " /~ AND dept.organization_type = '[organizationType]' ~/"
                + " /~ AND ulog.LOGON_TIME >= '[timeStart]' ~/" + " /~ AND ulog.LOGON_TIME <= '[timeEnd]' ~/"
                + " GROUP BY dept.ORGANIZATION_ID,dept.ORGANIZATION_SHORT,dept.ORGANIZATION_NAME " + " union"
                + " select dept.ORGANIZATION_ID ,dept.ORGANIZATION_SHORT ,dept.ORGANIZATION_NAME ,'0' sum1,'0' avg "
                + " from LS_SYSTEM_DEPARTMENT as dept," + " LS_SYSTEM_USER as users," + " LS_SYSTEM_USER_LOG as ulog,"
                + " LS_SYSTEM_ORGANIZATION_USER as ou" + " where dept.ORGANIZATION_ID=ou.ORGANIZATION_ID"
                + " and users.USER_ID=ou.USER_ID" + " and users.USER_ID=ulog.LOG_USER"
                + " /~ and dept.organization_type = '[organizationType]' ~/"
                + " /~ AND ulog.LOGON_TIME >= '[timeStart]' ~/" + " /~ AND ulog.LOGON_TIME <= '[timeEnd]' ~/"
                + " ) a group by a.ORGANIZATION_ID,a.ORGANIZATION_SHORT,a.ORGANIZATION_NAME";

        XsqlBuilder builder = this.getXsqlBuilder();
        Map filtersMap = pageQuery.getFilters();
        XsqlFilterResult queryXsqlResult = builder.generateHql(queryString, filtersMap);
        Query query = this.sessionFactory.getCurrentSession().createSQLQuery(queryXsqlResult.getXsql());
        List list = null;
        try {
            list = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (list != null && list.size() > 0) {
            List<PerformanceAnalysisVo> result = new ArrayList<PerformanceAnalysisVo>();
            for (int i = 0; i < list.size(); i++) {
                Object[] arr = (Object[]) list.get(i);
                String userId = ObjectUtils.nullSafeToString(arr[0]);
                String userAccount = ObjectUtils.nullSafeToString(arr[1]);
                String userName = ObjectUtils.nullSafeToString(arr[2]);
                String timeLong = ObjectUtils.nullSafeToString(arr[3]);
                String avgTimeLong = ObjectUtils.nullSafeToString(arr[4]);
                long onlineTime = "null".equals(timeLong) ? 0 : Long.parseLong(timeLong);
                long avgTime = "null".equals(timeLong) ? 0 : Long.parseLong(avgTimeLong);
                result.add(new PerformanceAnalysisVo(userId, userAccount, userName, onlineTime, avgTime));
            }
            return result;
        }
        return null;
    }

    /**
     * 
     * 1，行程开始处理数
     * 2，行程结束处理数
     * 3，报警处理数
     * 
     * @param pageQuery
     * @return
     */
    public JSONObject findPortJixiao() {
        String queryString = "select a.ORGANIZATION_ID,a.ORGANIZATION_SHORT,a.ORGANIZATION_NAME, "
                + "sum(a.c1) c1,sum(a.c2) c2,sum(a.c3) c3 from ("
                + " select dept.ORGANIZATION_ID,dept.ORGANIZATION_SHORT,dept.ORGANIZATION_NAME, "
                + "count(trip.CHECKIN_USER) c1,count(trip.CHECKOUT_USER) c2,count(alarm.ALARM_ID) c3"
                + " from LS_SYSTEM_USER users,LS_MONITOR_ALARM_DEAL alarm,LS_MONITOR_TRIP trip,"
                + "LS_SYSTEM_DEPARTMENT dept,LS_SYSTEM_ORGANIZATION_USER ou"
                + " where dept.ORGANIZATION_ID=ou.ORGANIZATION_ID" + " and users.USER_ID=ou.USER_ID"
                + " and alarm.DEAL_USER=users.USER_ID" + " and trip.CHECKIN_USER=users.USER_ID"
                + " GROUP BY dept.ORGANIZATION_ID,dept.ORGANIZATION_SHORT,dept.ORGANIZATION_NAME" + " union"
                + " select dept.ORGANIZATION_ID,dept.ORGANIZATION_SHORT,dept.ORGANIZATION_NAME, 0 c1,0 c2,0 c3"
                + " from LS_SYSTEM_DEPARTMENT dept "
                + " ) a  group by a.ORGANIZATION_ID,a.ORGANIZATION_SHORT,a.ORGANIZATION_NAME";
        Query query = this.sessionFactory.getCurrentSession().createSQLQuery(queryString);
        List list = query.list();
        JSONObject retJson = new JSONObject();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Object[] arr = (Object[]) list.get(i);
                String key = String.format("%s%s%s", arr[0].toString(), arr[1].toString(), arr[2].toString());
                String subKey = "0";
                JSONObject json = new JSONObject();
                json.put("tripCheckIn", arr[3].toString());// 行程结束次数
                json.put("tripCheckOut", arr[4].toString());// 行程开始次数
                json.put("alarm", arr[5].toString());// 报警处理次数
                // json.put("sum", arr[5].toString());
                JSONObject subJson = null;
                if (!retJson.containsKey(key)) {
                    subJson = new JSONObject();
                } else {
                    subJson = retJson.getJSONObject(key);
                }
                subJson.put(subKey, json);
                retJson.put(key, subJson);
            }
        }
        return retJson;
    }
}
