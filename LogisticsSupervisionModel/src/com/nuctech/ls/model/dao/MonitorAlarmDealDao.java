package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmDealBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.vo.monitor.PatrolVehicleStatusVO;

@Repository
public class MonitorAlarmDealDao extends LSBaseDao<LsMonitorAlarmDealBO, Serializable> {

    /**
     * 新增报警处理信息
     * 
     * @param lsMonitorAlarmDealBO
     */
    public void addMonitorAlarm(LsMonitorAlarmDealBO lsMonitorAlarmDealBO) {
        persist(lsMonitorAlarmDealBO);
    }

    /**
     * 查找所有的报警处理信息
     * 
     * @return
     */
    public List<LsMonitorAlarmDealBO> findAllAlarmDeal() {
        return findAll();
    }

    /**
     * 根据报警Id获取该报警所有的处理信息
     * 
     * @param alarmId
     * @return
     */
    public List<LsMonitorAlarmDealBO> findAllAlarmDealByAlarmId(String alarmId) {
        return findAllBy("alarmId", alarmId);
    }

    /**
     * 根据报警Id获取该报警所有的处理信息
     * 
     * @param alarmId
     * @param orderby
     * @return
     */
    public List<LsMonitorAlarmDealBO> findAllAlarmDealByAlarmId(String alarmId, HashMap<String, String> orderby) {
        return findAllBy("alarmId", alarmId, orderby);
    }

    /**
     * 获取巡逻队与车辆信息
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<PatrolVehicleStatusVO> getPatrolVehicles() {
        String queryString = "select {patrol.*},{vehiclstatus.*} "
                + "from LsCommonPatrolBO patrol,LsMonitorVehicleStatusBO vehiclstatus where 1=1"
                + " and patrol.trackUnitNumber=vehiclstatus.trackingDeviceNumber ";
        Query query = this.getSession().createSQLQuery(queryString).addEntity("patrol", LsCommonPatrolBO.class)
                .addEntity("vehiclstatus", LsMonitorVehicleStatusBO.class);
        List<Object[]> list = query.list();
        List<PatrolVehicleStatusVO> patrolVehicleStatusVOs = new ArrayList<PatrolVehicleStatusVO>();
        for (int i = 0; i < list.size(); i++) {
            PatrolVehicleStatusVO patrolVehicleStatusVO = new PatrolVehicleStatusVO();
            patrolVehicleStatusVO.setPotralUser(((LsCommonPatrolBO) list.get(i)[0]).getPotralUser());
            patrolVehicleStatusVO.setLatitude(((LsMonitorVehicleStatusBO) list.get(i)[1]).getLatitude());
            patrolVehicleStatusVO.setLongitude(((LsMonitorVehicleStatusBO) list.get(i)[1]).getLongitude());

            patrolVehicleStatusVOs.add(patrolVehicleStatusVO);
        }
        return patrolVehicleStatusVOs;
    }
}
