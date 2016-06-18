package com.nuctech.ls.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.vo.analysis.PerformanceAnalysisVo;

import javacommon.xsqlbuilder.XsqlBuilder;
import javacommon.xsqlbuilder.XsqlBuilder.XsqlFilterResult;
import net.sf.json.JSONObject;

@Repository
public class PerformanceAnalysisDao extends LSBaseDao{
	
	public List<PerformanceAnalysisVo> findUserOnline(PageQuery<Map> pageQuery) {
		String queryString = "SELECT u.USER_ID,	u.USER_ACCOUNT,	u.USER_NAME, SUM(DATEDIFF(MINUTE, l.LOGON_TIME,l.LOGOUT_TIME)) FROM	LS_SYSTEM_USER u,LS_SYSTEM_USER_LOG l WHERE	u.USER_ID = l.LOG_USER AND l.LOGOUT_TIME IS NOT NULL AND l.LOGON_TIME IS NOT NULL "
				+ "/~ AND l.LOGON_TIME >= '[dateStart]' ~/"
				+ "/~ AND l.LOGON_TIME <= '[dateEnd]' ~/"
				+ " GROUP BY u.USER_ID, u.USER_ACCOUNT, u.USER_NAME";
		XsqlBuilder builder = this.getXsqlBuilder();
	    Map filtersMap = pageQuery.getFilters();
	    XsqlFilterResult queryXsqlResult = builder.generateHql(queryString, filtersMap);
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(queryXsqlResult.getXsql());
		List list = query.list();
		if (list != null && list.size() > 0) {
			List<PerformanceAnalysisVo> result = new ArrayList<PerformanceAnalysisVo>();
			for(int i=0;i<list.size();i++){
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

	public JSONObject findDealAlarm() {
		String queryString = "SELECT u.USER_ID, u.USER_ACCOUNT, u.USER_NAME, ad.DEAL_METHOD, SUM(DATEDIFF(MINUTE, ad.RECEIVE_TIME, ad.DEAL_TIME)) as dealtime, count(*) as total FROM LS_SYSTEM_USER u, LS_MONITOR_ALARM_DEAL ad WHERE u.USER_ID = ad.DEAL_USER AND ad.RECEIVE_TIME IS NOT NULL AND ad.DEAL_TIME IS NOT NULL GROUP BY u.USER_ID, u.USER_ACCOUNT, u.USER_NAME, ad.DEAL_METHOD";
		Query query = this.sessionFactory.getCurrentSession().createSQLQuery(queryString);
		List list = query.list();
		JSONObject retJson = new JSONObject();
		if (list != null && list.size() > 0) {
			for(int i=0;i<list.size();i++){
				Object[] arr = (Object[]) list.get(i);
				String key=String.format("%s%s%s", arr[0].toString(),arr[1].toString(),arr[2].toString());
				String subKey = arr[3].toString();
				JSONObject json = new JSONObject();
				json.put("time", arr[4].toString());
				json.put("sum", arr[5].toString());
				JSONObject subJson = null;
				if(!retJson.containsKey(key)){
					subJson = new JSONObject();
				}else{
					subJson = retJson.getJSONObject(key);
				}
				subJson.put(subKey, json);
				retJson.put(key, subJson);
			}
		}
		return retJson;
	}
}
