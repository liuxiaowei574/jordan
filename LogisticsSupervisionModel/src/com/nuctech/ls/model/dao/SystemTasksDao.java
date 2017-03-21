package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemTasksBO;

@Repository
public class SystemTasksDao extends LSBaseDao<LsSystemTasksBO, Serializable> {

    @SuppressWarnings("rawtypes")
    public int findNumber(String id) {
        Session session = this.getSession();
        String sql = "SELECT t.*,d.* " + "FROM LS_SYSTEM_TASKS t ,LS_SYSTEM_TASKS_DEAL d "
                + "where t.TASK_ID=d.TASK_ID and d.DEAL_TYPE='0' and t.TASK_RECEIVE_USER='" + id + "'";
        List missionList = null;
        missionList = session.createSQLQuery(sql).addScalar("DEPLOY_TIME", StandardBasicTypes.STRING)
                .addScalar("TASK_CONTENT", StandardBasicTypes.STRING).addScalar("TASK_TYPE", StandardBasicTypes.STRING)
                .addScalar("TASK_PRIORITY", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return missionList.size();
    }

    @SuppressWarnings("rawtypes")
    public List<?> findAllUndealMission() {
        Session session = this.getSession();
        String sql = "SELECT t.*,d.* " + "FROM LS_SYSTEM_TASKS t ,LS_SYSTEM_TASKS_DEAL d "
                + "where t.TASK_ID=d.TASK_ID and d.DEAL_TYPE='0'";
        List missionList = null;
        missionList = session.createSQLQuery(sql).addScalar("DEPLOY_TIME", StandardBasicTypes.STRING)
                .addScalar("TASK_CONTENT", StandardBasicTypes.STRING).addScalar("TASK_TYPE", StandardBasicTypes.STRING)
                .addScalar("TASK_PRIORITY", StandardBasicTypes.STRING).addScalar("TASK_ID", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return missionList;
    }

    @SuppressWarnings("rawtypes")
    public List<?> findAllUndealMission(String id) {
        Session session = this.getSession();
        String sql = "SELECT t.*,d.* " + "FROM LS_SYSTEM_TASKS t ,LS_SYSTEM_TASKS_DEAL d "
                + "where t.TASK_ID=d.TASK_ID and d.DEAL_TYPE='0' and t.TASK_RECEIVE_USER='" + id + "'";
        List missionList = null;
        missionList = session.createSQLQuery(sql).addScalar("DEPLOY_TIME", StandardBasicTypes.STRING)
                .addScalar("TASK_CONTENT", StandardBasicTypes.STRING).addScalar("TASK_TYPE", StandardBasicTypes.STRING)
                .addScalar("TASK_PRIORITY", StandardBasicTypes.STRING).addScalar("TASK_ID", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return missionList;
    }

    @SuppressWarnings("rawtypes")
    public List<?> findAllUndealAlarmMission(String id) {
        Session session = this.getSession();
        String sql = "SELECT t.*,d.* " + "FROM LS_SYSTEM_TASKS t ,LS_SYSTEM_TASKS_DEAL d "
                + "where t.TASK_ID=d.TASK_ID and d.DEAL_TYPE='0' and t.TASK_TYPE='2' and t.TASK_RECEIVE_USER='" + id
                + "'";
        List alarmMissionList = null;
        alarmMissionList = session.createSQLQuery(sql).addScalar("DEPLOY_TIME", StandardBasicTypes.STRING)
                .addScalar("TASK_TITLE", StandardBasicTypes.STRING).addScalar("TASK_CONTENT", StandardBasicTypes.STRING)
                .addScalar("TASK_TYPE", StandardBasicTypes.STRING).addScalar("TASK_PRIORITY", StandardBasicTypes.STRING)
                .addScalar("TASK_ID", StandardBasicTypes.STRING).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
        return alarmMissionList;
    }

    @SuppressWarnings("rawtypes")
    public List<?> findAllUndealAlarmMission() {
        Session session = this.getSession();
        String sql = "SELECT t.*,d.* " + "FROM LS_SYSTEM_TASKS t ,LS_SYSTEM_TASKS_DEAL d "
                + "where t.TASK_ID=d.TASK_ID and d.DEAL_TYPE='0' and t.TASK_TYPE='2'";
        List alarmMissionList = null;
        alarmMissionList = session.createSQLQuery(sql).addScalar("DEPLOY_TIME", StandardBasicTypes.STRING)
                .addScalar("TASK_TITLE", StandardBasicTypes.STRING).addScalar("TASK_CONTENT", StandardBasicTypes.STRING)
                .addScalar("TASK_TYPE", StandardBasicTypes.STRING).addScalar("TASK_PRIORITY", StandardBasicTypes.STRING)
                .addScalar("TASK_ID", StandardBasicTypes.STRING).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
        return alarmMissionList;
    }

    public List<?> findAllUndealEscortMission(String id) {
        Session session = this.getSession();
        String sql = "SELECT t.*,d.* " + "FROM LS_SYSTEM_TASKS t ,LS_SYSTEM_TASKS_DEAL d "
                + "where t.TASK_ID=d.TASK_ID and d.DEAL_TYPE='0' and t.TASK_TYPE='1' and t.TASK_RECEIVE_USER='" + id
                + "'";
        @SuppressWarnings("rawtypes")
        List escortMissionList = null;
        escortMissionList = session.createSQLQuery(sql).addScalar("DEPLOY_TIME", StandardBasicTypes.STRING)
                .addScalar("TASK_TITLE", StandardBasicTypes.STRING).addScalar("TASK_CONTENT", StandardBasicTypes.STRING)
                .addScalar("TASK_TYPE", StandardBasicTypes.STRING).addScalar("TASK_PRIORITY", StandardBasicTypes.STRING)
                .addScalar("TASK_ID", StandardBasicTypes.STRING).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
        return escortMissionList;
    }

    public List<?> findAllUndealEscortMission() {
        Session session = this.getSession();
        String sql = "SELECT t.*,d.* " + "FROM LS_SYSTEM_TASKS t ,LS_SYSTEM_TASKS_DEAL d "
                + "where t.TASK_ID=d.TASK_ID and d.DEAL_TYPE='0' and t.TASK_TYPE='1'";
        @SuppressWarnings("rawtypes")
        List escortMissionList = null;
        escortMissionList = session.createSQLQuery(sql).addScalar("DEPLOY_TIME", StandardBasicTypes.STRING)
                .addScalar("TASK_TITLE", StandardBasicTypes.STRING).addScalar("TASK_CONTENT", StandardBasicTypes.STRING)
                .addScalar("TASK_TYPE", StandardBasicTypes.STRING).addScalar("TASK_PRIORITY", StandardBasicTypes.STRING)
                .addScalar("TASK_ID", StandardBasicTypes.STRING).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
        return escortMissionList;
    }

    public List<?> findAllUndealDispatchMission(String id) {
        Session session = this.getSession();
        String sql = "SELECT t.*,d.* " + "FROM LS_SYSTEM_TASKS t ,LS_SYSTEM_TASKS_DEAL d "
                + "where t.TASK_ID=d.TASK_ID and d.DEAL_TYPE='0' and t.TASK_TYPE='3' and t.TASK_RECEIVE_USER='" + id
                + "'";
        @SuppressWarnings("rawtypes")
        List dispatchMissionList = null;
        dispatchMissionList = session.createSQLQuery(sql).addScalar("DEPLOY_TIME", StandardBasicTypes.STRING)
                .addScalar("TASK_TITLE", StandardBasicTypes.STRING).addScalar("TASK_CONTENT", StandardBasicTypes.STRING)
                .addScalar("TASK_TYPE", StandardBasicTypes.STRING).addScalar("TASK_PRIORITY", StandardBasicTypes.STRING)
                .addScalar("TASK_ID", StandardBasicTypes.STRING).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
        return dispatchMissionList;
    }

    public List<?> findAllUndealDispatchMission() {
        Session session = this.getSession();
        String sql = "SELECT t.*,d.* " + "FROM LS_SYSTEM_TASKS t ,LS_SYSTEM_TASKS_DEAL d "
                + "where t.TASK_ID=d.TASK_ID and d.DEAL_TYPE='0' and t.TASK_TYPE='3'";
        @SuppressWarnings("rawtypes")
        List dispatchMissionList = null;
        dispatchMissionList = session.createSQLQuery(sql).addScalar("DEPLOY_TIME", StandardBasicTypes.STRING)
                .addScalar("TASK_TITLE", StandardBasicTypes.STRING).addScalar("TASK_CONTENT", StandardBasicTypes.STRING)
                .addScalar("TASK_TYPE", StandardBasicTypes.STRING).addScalar("TASK_PRIORITY", StandardBasicTypes.STRING)
                .addScalar("TASK_ID", StandardBasicTypes.STRING).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
        return dispatchMissionList;
    }

}
