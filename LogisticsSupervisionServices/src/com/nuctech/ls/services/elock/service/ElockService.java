package com.nuctech.ls.services.elock.service;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.common.Command;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.MonitorVehicleStatusService;
import com.nuctech.ls.services.jms.CommandMessageSender;
import com.nuctech.ls.services.jms.ElockReaderSender;
import com.nuctech.util.NuctechUtil;

/**
 * elock Service
 * 
 * @author 姜永权
 *
 */
@Service
public class ElockService extends LSBaseService {

    @Resource
    private MonitorTripService monitorTripService;
    @Resource
    private MonitorVehicleStatusService monitorVehicleStatusService;
    @Resource
    private ElockReaderSender elockReaderSender;
    @Resource
    private CommandMessageSender commandMessageSender;

    private String commandPWD = "0000000000";

    /**
     * 处理125k消息
     * 
     * @param jsonMsg
     */
    public void dealAwake(JSONObject jsonMsg) {
        // 锁号
        String sealNo = jsonMsg.getString("sealNo");
        StringBuffer tripId = new StringBuffer();
        // 检查行程是否激活
        boolean activateFlag = checkIsActivate(sealNo, tripId);
        if (activateFlag) {
            // 检查关锁是否已经施封
            boolean elockLockedFlag = checkIsElockLocked(sealNo, tripId);
            if (!elockLockedFlag) {
                // 如果是解封状态，发送施封mq消息
                elockReaderSender.sendMessage(getSetLockedInfo(sealNo), "0");
            }
            // 发送抬杆儿命名
            Command command = new Command("", 1, "Barrier", 1, "allowUp");
            commandMessageSender.send(command);
        }
    }

    /**
     * 组装施封消息
     * 
     * @param sealNo
     * @return
     */
    private String getSetLockedInfo(String sealNo) {
        JSONObject json = new JSONObject();
        json.put("Id", "setLocked");
        json.put("commandPWD", commandPWD);
        json.put("sealNo", sealNo);
        return json.toString();
    }

    /**
     * 检查行程是否激活
     * 
     * @param sealNo
     * @return
     */
    private boolean checkIsActivate(String sealNo, StringBuffer tripId) {
        if (NuctechUtil.isNotNull(sealNo)) {
            // 检查行程是否激活
            LsMonitorTripBO monitorTripBO = monitorTripService.findLastestPatroltripByUnitNumber(sealNo);
            if (NuctechUtil.isNotNull(monitorTripBO) && NuctechUtil.isNotNull(monitorTripBO.getTripStatus())) {
                // tripId
                if (NuctechUtil.isNotNull(monitorTripBO.getTripId())) {
                    tripId.append(monitorTripBO.getTripId());
                }
                String tripStatus = monitorTripBO.getTripStatus();
                // 行程已激活,行程状态 0. 待激活 1. 进行中 2. 待结束 3. 已结束
                if ("1".equals(tripStatus) || "2".equals(tripStatus)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查关锁是否已经施封
     * 
     * @param sealNo
     * @param tripId
     * @return 施封返回true
     */
    private boolean checkIsElockLocked(String sealNo, StringBuffer tripId) {
        if (NuctechUtil.isNotNull(tripId.toString())) {
            LsMonitorVehicleStatusBO monitorVehicleStatusBO = monitorVehicleStatusService
                    .findVehicleStatusByNumAndTripId(tripId.toString(), sealNo);
            if (NuctechUtil.isNotNull(monitorVehicleStatusBO)
                    && NuctechUtil.isNotNull(monitorVehicleStatusBO.getElockStatus())) {
                // 关锁状态、1-施封、0-解封
                if ("1".equals(monitorVehicleStatusBO.getElockStatus())) {
                    return true;
                }
            }
        }
        return false;
    }
}
