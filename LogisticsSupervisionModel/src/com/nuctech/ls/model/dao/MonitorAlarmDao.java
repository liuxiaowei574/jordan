/**
 * 
 */
package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmBO;
import com.nuctech.ls.model.util.AlarmDealType;
import com.nuctech.ls.model.util.AlarmType;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.monitor.ViewAlarmReportVO;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.CommonStringUtil;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author sunming
 *
 */
@Repository
public class MonitorAlarmDao extends LSBaseDao<LsMonitorAlarmBO, Serializable> {

    /**
     * 新增报警信息
     * 
     * @param lsMonitorAlarmBO
     */
    public void addMonitorAlarm(LsMonitorAlarmBO lsMonitorAlarmBO) {
        persist(lsMonitorAlarmBO);
    }

    /**
     * 查找所有的报警信息
     * 
     * @return
     */
    public List<LsMonitorAlarmBO> findAllAlarm() {
        return findAll();
    }

    public List<LsMonitorAlarmBO> findLsMonitorAlarmByTripId(String tripId) {
        return findAllBy("tripId", tripId);
    }

    @SuppressWarnings("unchecked")
    public List<LsMonitorAlarmBO> findAlarmsByTripIdAndStatus(String tripId) {
        Criteria criteria = getSession().createCriteria(LsMonitorAlarmBO.class);
        criteria.add(Restrictions.eq("tripId", tripId));
        criteria.add(Restrictions.or(Restrictions.eq("alarmStatus", AlarmDealType.Undeal.getText()),
                Restrictions.eq("alarmStatus", AlarmDealType.Dealing.getText())));
        return criteria.list();
        // return findAllBy("tripId", tripId);
    }

    @SuppressWarnings("unchecked")
    public List<ViewAlarmReportVO> findAlarmVOsByTripIdAndStatus(String tripId) {
        Criteria criteria = getSession().createCriteria(ViewAlarmReportVO.class);
        criteria.add(Restrictions.eq("tripId", tripId));
        criteria.add(Restrictions.or(Restrictions.eq("alarmStatus", AlarmDealType.Undeal.getText()),
                Restrictions.eq("alarmStatus", AlarmDealType.Dealing.getText())));
        return criteria.list();
    }
    
	/**
	 * 查询某个行程的所有报警的简单对象
	 * 
	 * @param tripId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONArray findSimpleAlarmsByTripId(String tripId) {
		Query query = getSession().createQuery(
				" select alarmId, alarmContent, alarmTime, alarmLongitude, alarmLatitude, alarmTypeCode, alarmLevelCode, vehiclePlateNumber from ViewAlarmReportVO "
						+ " where tripId = :tripId and (alarmStatus = :alarmStatus1 or alarmStatus = :alarmStatus2) order by alarmLevelCode desc, alarmTime desc ");
		query.setParameter("tripId", tripId).setParameter("alarmStatus1", AlarmDealType.Undeal.getText())
				.setParameter("alarmStatus2", AlarmDealType.Dealing.getText());
		List<Object[]> list = query.list();
		JSONArray array = new JSONArray();
		if(list != null && list.size() > 0) {
			JSONObject obj = new JSONObject();
			for(Object[] objs : list){
				obj.put("alarmId", CommonStringUtil.ifNull(objs[0], ""));
				obj.put("alarmContent", CommonStringUtil.ifNull(objs[1], ""));
				obj.put("alarmTime", CommonStringUtil.ifNull(objs[2], ""));
				obj.put("alarmLongitude", CommonStringUtil.ifNull(objs[3], ""));
				obj.put("alarmLatitude", CommonStringUtil.ifNull(objs[4], ""));
				obj.put("alarmTypeCode", CommonStringUtil.ifNull(objs[5], ""));
				obj.put("alarmLevelCode", CommonStringUtil.ifNull(objs[6], ""));
				obj.put("vehiclePlateNumber", CommonStringUtil.ifNull(objs[7], ""));
				array.add(obj);
			}
		}
		return array;
	}

    @SuppressWarnings("unchecked")
    public List<ViewAlarmReportVO> findAlarmVOByTripIdAndStatus(String tripId) {
        Criteria criteria = getSession().createCriteria(ViewAlarmReportVO.class);
        criteria.add(Restrictions.eq("tripId", tripId));
        criteria.add(Restrictions.or(Restrictions.eq("alarmStatus", AlarmDealType.Undeal.getText()),
                Restrictions.eq("alarmStatus", AlarmDealType.Dealing.getText())));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<ViewAlarmReportVO> findAlarmList() {
        Criteria crit = sessionFactory.getCurrentSession().createCriteria(ViewAlarmReportVO.class);
        HttpSession session = ServletActionContext.getRequest().getSession();
        crit.add(Restrictions.eq("tripStatus", Constant.TRIP_STATUS_STARTED));
        // 列表不显示已处理的
        crit.add(Restrictions.ne("alarmStatus", AlarmDealType.Dealt.getText()));
        // 巡逻队只能看到自己的报警
        SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
        if (RoleType.escortPatrol.toString().equalsIgnoreCase(sessionUser.getRoleName())
                || RoleType.enforcementPatrol.toString().equalsIgnoreCase(sessionUser.getRoleName())) {
            crit.add(Restrictions.eq("userId", sessionUser.getUserId()));
        }else if(RoleType.portUser.toString().equalsIgnoreCase(sessionUser.getRoleName())){
            crit.add(Restrictions.or(Restrictions.eq("checkinPort", sessionUser.getOrganizationId()),
                    Restrictions.eq("checkoutPort", sessionUser.getOrganizationId())));
        }
        crit.addOrder(Order.desc("alarmLevelId"));
        crit.addOrder(Order.desc("alarmTime"));
        return crit.list();
    }

    /**
     * 查找报警中最新推送的巡逻队
     * 
     * @param tripId
     * @param vehicleId
     * @param userIds
     * @return
     */
    @SuppressWarnings("unchecked")
    public LsMonitorAlarmBO findLatestPatrol(String tripId, String vehicleId, String[] userIds) {
        Criteria criteria = getSession().createCriteria(LsMonitorAlarmBO.class);
        criteria.add(Restrictions.eq("tripId", tripId));
        criteria.add(Restrictions.eq("vehicleId", vehicleId));
        criteria.add(Restrictions.in("userId", userIds));
        criteria.addOrder(Order.desc("alarmTime"));
        List<LsMonitorAlarmBO> alarmBOs = criteria.list();
        if (NuctechUtil.isNotNull(alarmBOs) && alarmBOs.size() > 0) {
            return alarmBOs.get(0);
        }
        return null;
    }

    /**
     * 查找给定负责人中当前负责报警数最少的
     * 
     * @param tripId
     * @param userIds
     * @return
     */
    @SuppressWarnings("unchecked")
    public String findUserIdByAlarmCount(String tripId, String[] userIds) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T.USER_ID FROM LS_MONITOR_ALARM A, ").append(" ( ")
                .append(" SELECT USER_ID,MAX(RECEIVE_TIME) RECEIVE_TIME,COUNT(1) DEAL_ALARM_COUNT "
                        + "FROM LS_MONITOR_ALARM ")
                .append(" WHERE TRIP_ID = :tripId ").append(" AND USER_ID IN (:userIds) ").append(" GROUP BY USER_ID")
                .append(" ) T ").append(" WHERE 1 = 1 ").append(" AND T.USER_ID=A.USER_ID")
                .append(" AND T.RECEIVE_TIME = A.RECEIVE_TIME ")
                .append(" ORDER BY T.DEAL_ALARM_COUNT ASC,T.RECEIVE_TIME DESC,A.ALARM_TIME DESC ");

        try {
            SQLQuery sqlQuery = getSession().createSQLQuery(sql.toString());
            sqlQuery.setParameter("tripId", tripId).setParameterList("userIds", userIds);
            List<String> list = sqlQuery.list();
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查询失败:" + sql);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public List<ViewAlarmReportVO> findAlarmListV(String vehicleId) {
        // Session session = this.getSession();
        // String sql =

        Criteria crit = sessionFactory.getCurrentSession().createCriteria(ViewAlarmReportVO.class);
        HttpSession session = ServletActionContext.getRequest().getSession();
        crit.add(Restrictions.eq("tripStatus", Constant.TRIP_STATUS_STARTED));
        crit.add(Restrictions.eq("vehicleId", vehicleId));
        // 列表不显示已处理的
        crit.add(Restrictions.ne("alarmStatus", AlarmDealType.Dealt.getText()));
        // 巡逻队只能看到自己的报警
        SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
        if (RoleType.escortPatrol.toString().equalsIgnoreCase(sessionUser.getRoleName())
                || RoleType.enforcementPatrol.toString().equalsIgnoreCase(sessionUser.getRoleName())) {
            crit.add(Restrictions.eq("userId", sessionUser.getUserId()));
        }
        crit.addOrder(Order.desc("alarmLevelId"));
        crit.addOrder(Order.desc("alarmTime"));
        return crit.list();
    }
    

    @SuppressWarnings("unchecked")
    public JSONArray findSimpleAlarmList(String vehicleId) {
        // 忽略巡逻队
        Query query = getSession().createQuery(
				" select alarmId, alarmContent, alarmTime, alarmLongitude, alarmLatitude, alarmTypeCode, alarmLevelCode, vehiclePlateNumber, userId, tripId from ViewAlarmReportVO "
						+ " where vehicleId = :vehicleId and alarmStatus <> :alarmStatus and tripStatus = :tripStatus order by alarmLevelCode desc, alarmTime desc ");
		query.setParameter("vehicleId", vehicleId).setParameter("alarmStatus", AlarmDealType.Dealt.getText())
				.setParameter("tripStatus", Constant.TRIP_STATUS_STARTED);
		List<Object[]> list = query.list();
		JSONArray array = new JSONArray();
		if(list != null && list.size() > 0) {
			JSONObject obj = new JSONObject();
			for(Object[] objs : list){
				obj.put("alarmId", CommonStringUtil.ifNull(objs[0], ""));
				obj.put("alarmContent", CommonStringUtil.ifNull(objs[1], ""));
				obj.put("alarmTime", CommonStringUtil.ifNull(objs[2], ""));
				obj.put("alarmLongitude", CommonStringUtil.ifNull(objs[3], ""));
				obj.put("alarmLatitude", CommonStringUtil.ifNull(objs[4], ""));
				obj.put("alarmTypeCode", CommonStringUtil.ifNull(objs[5], ""));
				obj.put("alarmLevelCode", CommonStringUtil.ifNull(objs[6], ""));
				obj.put("vehiclePlateNumber", CommonStringUtil.ifNull(objs[7], ""));
				obj.put("userId", CommonStringUtil.ifNull(objs[8], ""));
				obj.put("tripId", CommonStringUtil.ifNull(objs[9], ""));
				array.add(obj);
			}
		}
		return array;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<ViewAlarmReportVO> findAlarmListA(String alarmId) {
        org.hibernate.Session session = this.getSession();
        String sql = "SELECT a.ALARM_TIME,v.VEHICLE_PLATE_NUMBER, t.ALARM_TYPE_NAME,a.ALARM_TYPE_ID,t.ALARM_LEVEL_ID "
                + "FROM LS_MONITOR_ALARM a ,LS_COMMON_VEHICLE v,LS_DM_ALARM_TYPE t "
                + "where a.VEHICLE_ID = v.VEHICLE_ID and t.ALARM_TYPE_ID=a.ALARM_TYPE_ID and a.ALARM_ID='" + alarmId
                + "'";
        List list = null;
        list = session.createSQLQuery(sql).addScalar("ALARM_TIME", StandardBasicTypes.STRING)
                .addScalar("VEHICLE_PLATE_NUMBER", StandardBasicTypes.STRING)
                .addScalar("ALARM_TYPE_NAME", StandardBasicTypes.STRING)
                .addScalar("ALARM_TYPE_ID", StandardBasicTypes.STRING)
                .addScalar("ALARM_LEVEL_ID", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }

    /**
     * 根据tripId和指定车辆查询指定类型的报警
     * 
     * @param tripId
     * @param vehicleId
     * @param alarmTypeId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsMonitorAlarmBO> findAlarmByType(String tripId, String vehicleId, String alarmTypeId) {
        Criteria criteria = getSession().createCriteria(LsMonitorAlarmBO.class);
        criteria.add(Restrictions.eq("tripId", tripId));
        criteria.add(Restrictions.eq("vehicleId", vehicleId));
        criteria.add(Restrictions.eq("alarmTypeId", alarmTypeId));
        return criteria.list();
    }

    public List<LsMonitorAlarmBO> findExceedTimeAlarm(String tripId, String vehicleId) {
        return findAlarmByType(tripId, vehicleId, AlarmType.EXCEEDING_TRIP_ALLOWED_TIME.getAlarmType());
    }

    public List<LsMonitorAlarmBO> findTargetZoonAlarm(String tripId, String vehicleId) {
        return findAlarmByType(tripId, vehicleId, AlarmType.TARGET_ZOON.getAlarmType());
    }

    @SuppressWarnings("rawtypes")
    public List findStatic() {
        org.hibernate.Session session = this.getSession();
        String sql = "SELECT t.* FROM VIEW_ALARM_REPORT t where 1=1";
        List list = null;
        list = session.createSQLQuery(sql).addScalar("DECLARATION_NUMBER", StandardBasicTypes.STRING)
                .addScalar("TRACKING_DEVICE_NUMBER", StandardBasicTypes.STRING)
                .addScalar("ESEAL_NUMBER", StandardBasicTypes.STRING)
                .addScalar("SENSOR_NUMBER", StandardBasicTypes.STRING)
                .addScalar("VEHICLE_PLATE_NUMBER", StandardBasicTypes.STRING)
                .addScalar("ALARM_TIME", StandardBasicTypes.STRING).addScalar("USER_NAME", StandardBasicTypes.STRING)
                .addScalar("ALARM_STATUS", StandardBasicTypes.STRING)
                .addScalar("ALARM_LEVEL_CODE", StandardBasicTypes.STRING)
                .addScalar("ALARM_TYPE_NAME", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

        return list;
    }
    
	/**
	 * 查询指定alarm id的报警
	 * 
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONArray findAllInRange(String... ids) {
		Query query = getSession().createQuery(
				" select alarmId, alarmContent, alarmTime, alarmLongitude, alarmLatitude, alarmTypeCode, alarmLevelCode, vehiclePlateNumber from ViewAlarmReportVO "
						+ " where alarmId in (:ids) order by alarmLevelCode desc, alarmTime desc ");
		query.setParameterList("ids", ids);
		List<Object[]> list = query.list();
		JSONArray array = new JSONArray();
		if(list != null && list.size() > 0) {
			JSONObject obj = new JSONObject();
			for(Object[] objs : list){
				obj.put("alarmId", CommonStringUtil.ifNull(objs[0], ""));
				obj.put("alarmContent", CommonStringUtil.ifNull(objs[1], ""));
				obj.put("alarmTime", CommonStringUtil.ifNull(objs[2], ""));
				obj.put("alarmLongitude", CommonStringUtil.ifNull(objs[3], ""));
				obj.put("alarmLatitude", CommonStringUtil.ifNull(objs[4], ""));
				obj.put("alarmTypeCode", CommonStringUtil.ifNull(objs[5], ""));
				obj.put("alarmLevelCode", CommonStringUtil.ifNull(objs[6], ""));
				obj.put("vehiclePlateNumber", CommonStringUtil.ifNull(objs[7], ""));
				array.add(obj);
			}
		}
		return array;
	}
}
