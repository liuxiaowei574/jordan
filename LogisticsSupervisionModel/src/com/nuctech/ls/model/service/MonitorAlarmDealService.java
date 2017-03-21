package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmDealBO;
import com.nuctech.ls.model.dao.MonitorAlarmDealDao;
import com.nuctech.ls.model.dao.SystemUserDao;
import com.nuctech.ls.model.vo.monitor.MonitorAlarmDealVO;
import com.nuctech.ls.model.vo.monitor.PatrolVehicleStatusVO;

@Service
@Transactional
public class MonitorAlarmDealService extends LSBaseService {

    @Resource
    private MonitorAlarmDealDao monitorAlarmDealDao;

    @Resource
    private SystemUserDao userDao;

    public void addAlarmDeal(LsMonitorAlarmDealBO lsMonitorAlarmDealBO) {
        monitorAlarmDealDao.save(lsMonitorAlarmDealBO);
    }

    public List<LsMonitorAlarmDealBO> findAllAlarmDealByAlarmId(String alarmId) {
        return monitorAlarmDealDao.findAllAlarmDealByAlarmId(alarmId);
    }

    public List<MonitorAlarmDealVO> findAllAlarmDealByAlarmId(String alarmId, HashMap<String, String> orderby) {
        List<MonitorAlarmDealVO> alarmDealList = new ArrayList<>();
        List<LsMonitorAlarmDealBO> list = monitorAlarmDealDao.findAllAlarmDealByAlarmId(alarmId, orderby);
        if (list != null && !list.isEmpty()) {
            for (LsMonitorAlarmDealBO bo : list) {
                MonitorAlarmDealVO vo = new MonitorAlarmDealVO();
                BeanUtils.copyProperties(bo, vo);
                vo.setRecipientsUserName(userDao.findUserNameById(bo.getRecipientsUser()));
                vo.setDealUserName(userDao.findUserNameById(bo.getDealUser()));
                alarmDealList.add(vo);
            }
        }
        return alarmDealList;
    }

    public List<PatrolVehicleStatusVO> findAllPatrolVehicleStatus() {
        return monitorAlarmDealDao.getPatrolVehicles();
    }
}
