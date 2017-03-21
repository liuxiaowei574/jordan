/**
 * 
 */
package com.nuctech.ls.services.gps.alarm;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.dm.LsDmAlarmTypeBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.memcached.MemcachedUtil;
import com.nuctech.ls.model.redis.RedisClientTemplate;
import com.nuctech.ls.model.service.AlarmLevelModifyService;
import com.nuctech.ls.model.service.CommonVehicleService;
import com.nuctech.ls.model.service.MonitorAlarmService;
import com.nuctech.ls.model.service.MonitorVehicleStatusService;
import com.nuctech.ls.model.util.AlarmType;
import com.nuctech.ls.model.util.WebSocketMessageType;
import com.nuctech.ls.model.vo.monitor.ViewAlarmReportVO;
import com.nuctech.ls.services.websokcet.WebsocketService;
import com.nuctech.util.CommonStringUtil;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;
import com.nuctech.util.RedisKey;

import edu.emory.mathcs.backport.java.util.Arrays;
import net.sf.json.JSONObject;

/**
 * 报警识别和区分
 * 
 * @author sunming
 *
 */
@Component
public class VehicleAlarmHandler {

    Logger logger = Logger.getLogger(this.getClass());
    // private LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO;
    // private static ThreadLocal<LsMonitorVehicleStatusBO> lsMonitorVehicleStatusBO = new
    // ThreadLocal<>();
    @Resource
    public AlarmValidation alarmValidation;
    @Resource
    public MonitorAlarmService monitorAlarmService;
    @Resource
    public MemcachedUtil memcachedUtil;
    @Resource
	private RedisClientTemplate redisClientTemplate;
    @Resource
    private SystemModules systemModules;
    @Resource
    private AlarmLevelModifyService alarmLevelModifyService;
    @Resource
    private MonitorVehicleStatusService monitorVehicleStatusService;
    @Resource
    private CommonVehicleService commonVehicleService;
    private static final String LOCK = "B";

    /**
     * 报警区分
     * 
     * @throws Exception
     */
    public void alarmDistinguish(LsMonitorVehicleStatusBO monitorVehicleStatusBO) throws Exception {
        // setLsMonitorVehicleStatusBO(value);
        // LsMonitorVehicleStatusBO monitorVehicleStatusBO = lsMonitorVehicleStatusBO.get();
        String gpsTrackingDeviceNumber = monitorVehicleStatusBO.getTrackingDeviceNumber();
        //Object object = memcachedUtil.get(gpsTrackingDeviceNumber, LsMonitorVehicleStatusBO.class);
        String value = redisClientTemplate.get(gpsTrackingDeviceNumber);
        LsMonitorVehicleStatusBO beforeLsMonitorVehicleStatusBO = null;
        if (value != null) {
			LsMonitorVehicleStatusBO statusBO = (LsMonitorVehicleStatusBO) JSONObject
					.toBean(JSONObject.fromObject(value), LsMonitorVehicleStatusBO.class);
            String beforeTripId = statusBO.getTripId();
            if (NuctechUtil.isNotNull(beforeTripId) && beforeTripId.equals(monitorVehicleStatusBO.getTripId())) {
                beforeLsMonitorVehicleStatusBO = statusBO;
            }
        }

        if (beforeLsMonitorVehicleStatusBO == null) {
            beforeLsMonitorVehicleStatusBO = new LsMonitorVehicleStatusBO();
        }

        // 开锁报警
        if (alarmValidation.validateOpenLockAlarm(monitorVehicleStatusBO, beforeLsMonitorVehicleStatusBO)) {
            logger.info("OPEN_LOCK alarm:" + gpsTrackingDeviceNumber);
            dealAlarm(AlarmType.OPEN_LOCK, gpsTrackingDeviceNumber, monitorVehicleStatusBO);
        }

        // 开锁又重新关闭报警
        if (alarmValidation.validateReclosedAlarm(monitorVehicleStatusBO, beforeLsMonitorVehicleStatusBO)) {
            logger.info("CLOSED_LOCK alarm:" + gpsTrackingDeviceNumber);
            dealAlarm(AlarmType.CLOSED_LOCK, gpsTrackingDeviceNumber, monitorVehicleStatusBO);
        }

        // 强拆报警 DEVICE_BROKEN("DEVICE_BROKEN"),
        if (alarmValidation.validateDeviceBrokenAlarm(monitorVehicleStatusBO, beforeLsMonitorVehicleStatusBO)) {
            logger.info("DEVICE_BROKEN alarm:" + gpsTrackingDeviceNumber);
            dealAlarm(AlarmType.DEVICE_BROKEN, gpsTrackingDeviceNumber, monitorVehicleStatusBO);
        }

        // 子锁失联 ESEAL_LOSS("ESEAL_LOSS"),
        List<String> sealLostList = alarmValidation.validateEsealLostAlarm(monitorVehicleStatusBO);
        if (sealLostList.size() > 0) {
            logger.info("ESEAL_LOSS alarm:" + gpsTrackingDeviceNumber);
            dealAlarm(AlarmType.ESEAL_LOSS, Arrays.toString(sealLostList.toArray()), monitorVehicleStatusBO);
        }

        // 子锁破坏 TAMPERING_WITH_ESEAL("TAMPERING_WITH_ESEAL"),
        List<String> sealBrokenList = alarmValidation.validateEsealBrokenAlarm(monitorVehicleStatusBO);
        if (sealBrokenList.size() > 0) {
            logger.info("TAMPERING_WITH_ESEAL alarm:" + gpsTrackingDeviceNumber);
            dealAlarm(AlarmType.TAMPERING_WITH_ESEAL, Arrays.toString(sealBrokenList.toArray()),
                    monitorVehicleStatusBO);
        }

        // 传感器失联 SENSOR_LOSS("SENSOR_LOSS"),
        List<String> sensorLostList = alarmValidation.validateSensorLostAlarm(monitorVehicleStatusBO);
        if (sensorLostList.size() > 0) {
            logger.info(" SENSOR_LOSS alarm:" + gpsTrackingDeviceNumber);
            dealAlarm(AlarmType.SENSOR_LOSS, Arrays.toString(sensorLostList.toArray()), monitorVehicleStatusBO);
        }

        // 传感器破坏 TAMPERING_WITH_SENSOR("TAMPERING_WITH_SENSOR");
        List<String> sensorBrokenList = alarmValidation.validateSensorBrokenAlarm(monitorVehicleStatusBO);
        if (sensorBrokenList.size() > 0) {
            logger.info("TAMPERING_WITH_SENSOR alarm:" + gpsTrackingDeviceNumber);
            dealAlarm(AlarmType.TAMPERING_WITH_SENSOR, Arrays.toString(sensorBrokenList.toArray()),
                    monitorVehicleStatusBO);
        }

        // 低电量 LOW_BATTERY("LOW_BATTERY"),
        // if (alarmValidation.validateLowBatteryAlarm(monitorVehicleStatusBO)) {
        // logger.info("LOW_BATTERY alarm:" + gpsTrackingDeviceNumber);
        // dealAlarm(AlarmType.LOW_BATTERY, gpsTrackingDeviceNumber,monitorVehicleStatusBO);
        // }

        // 行程超时报警 EXCEEDING_TRIP_ALLOWED_TIME("EXCEEDING_TRIP_ALLOWED_TIME"),
        if (alarmValidation.validateExceedingTripAllowedTimeAlarm(monitorVehicleStatusBO)) {
            // synchronized (LOCK) {
            // if (alarmValidation.validateExceedingTripAllowedTimeAlarm(monitorVehicleStatusBO)) {
            logger.info("EXCEEDING_TRIP_ALLOWED_TIME alarm:" + gpsTrackingDeviceNumber);
            dealAlarm(AlarmType.EXCEEDING_TRIP_ALLOWED_TIME, gpsTrackingDeviceNumber, monitorVehicleStatusBO);
            // }
            // }
        }

        // 如果车载台和巡逻队模块开启
        if (systemModules.isPatrolOn()) {
            // 车载台报警 TRACK_UNIT_ALARM("TRACK_UNIT_ALARM");
            List<String> trackUnitAlarmList = alarmValidation.validateTrackUnitAlarm();
            if (trackUnitAlarmList.size() > 0) {
                logger.info("TRACK_UNIT_ALARM alarm:" + gpsTrackingDeviceNumber);
                dealAlarm(AlarmType.TRACK_UNIT_ALARM, Arrays.toString(trackUnitAlarmList.toArray()),
                        monitorVehicleStatusBO);
            }
        }

        // 卫星信号丢失报警
        if (alarmValidation.validateSatelliteLostAlarm(monitorVehicleStatusBO, beforeLsMonitorVehicleStatusBO)) {
            logger.info("SATELLITE_LOSS alarm:" + gpsTrackingDeviceNumber);
            dealAlarm(AlarmType.SATELLITE_LOSS, gpsTrackingDeviceNumber, monitorVehicleStatusBO);
        }

        // 长时间滞留报警
        if (alarmValidation.validateLongStayAlarm(monitorVehicleStatusBO)) {
            logger.info("The ThreadId" + Thread.currentThread().getId()
                    + "-----Occured STAY_TIME alarm,alarm terminal is:" + gpsTrackingDeviceNumber);
            dealAlarm(AlarmType.LONG_STAY, gpsTrackingDeviceNumber, monitorVehicleStatusBO);
        }

        // 路线偏移报警
        if (alarmValidation.validatePlannedRouteOffsetAlarm(monitorVehicleStatusBO)) {
            if (null == beforeLsMonitorVehicleStatusBO
                    || !alarmValidation.validatePlannedRouteOffsetAlarm(beforeLsMonitorVehicleStatusBO)) {
                logger.info("The ThreadId" + Thread.currentThread().getId()
                        + "-----Occured ROUTE_DEVIATION alarm,alarm terminal is:" + gpsTrackingDeviceNumber);
                dealAlarm(AlarmType.ROUTE_DEVIATION, gpsTrackingDeviceNumber, monitorVehicleStatusBO);
            }
        }

        // 反向行驶 OPPOSITE_ROUTE("OPPOSITE_ROUTE"),
        if (alarmValidation.validateOppositeRouteAlarm(monitorVehicleStatusBO)) {
            logger.info("反向行驶报警:" + gpsTrackingDeviceNumber);
            dealAlarm(AlarmType.OPPOSITE_ROUTE, gpsTrackingDeviceNumber, monitorVehicleStatusBO);
        }
        // 如果不是有效定位，则忽略与定位有关报警。locationStatus第二位为1，代表有效定位。
        String location = monitorVehicleStatusBO.getLocationStatus().substring(1, 2);
        if (!"1".equals(location)) {
            logger.info(String.format(
                    "location fail," + "will ignore some alarm judgement.locationStatus:%s, lat:%s, lng:%s",
                    monitorVehicleStatusBO.getLocationStatus(), monitorVehicleStatusBO.getLatitude(),
                    monitorVehicleStatusBO.getLongitude()));
            return;
        }

        // 如果区域场地模块开启
        if (systemModules.isAreaOn()) {
            // 危险区域报警 ENTER_DANGEROUS_AREA("ENTER_DANGEROUS_AREA"),
            if (alarmValidation.validateEnterDangerousAreaAlarm(monitorVehicleStatusBO)) {
                logger.info("ENTER_DANGEROUS_AREA alarm:" + gpsTrackingDeviceNumber);
                dealAlarm(AlarmType.ENTER_DANGEROUS_AREA, gpsTrackingDeviceNumber, monitorVehicleStatusBO);
            }

            // 误报 FALSE_ALARM("FALSE_ALARM"),

            // target zoon报警
            if (alarmValidation.validateTargetZoonAlarm(monitorVehicleStatusBO)) {
                logger.info("Target zoon alarm:" + gpsTrackingDeviceNumber);
                dealAlarm(AlarmType.TARGET_ZOON, gpsTrackingDeviceNumber, monitorVehicleStatusBO);
            }
        }
    }

    /**
     * 处理掉线报警
     * 
     * @param monitorVehicleStatusBO
     * @throws Exception
     */
    public void alarmOffLine(LsMonitorVehicleStatusBO monitorVehicleStatusBO) throws Exception {
        String gpsTrackingDeviceNumber = monitorVehicleStatusBO.getTrackingDeviceNumber();
        // GSM信号丢失
        logger.info("GSM_LOSS alarm:" + gpsTrackingDeviceNumber);
        dealAlarm(AlarmType.GSM_LOSS, gpsTrackingDeviceNumber, monitorVehicleStatusBO);
    }

    /**
     * 报警处理
     */
    private void dealAlarm(AlarmType alarmType, String content, LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {
        // 保存报警信息
        logger.info("The ThreadId" + Thread.currentThread().getId() + "----trackingDeviceNumber=" + content);
        LsMonitorAlarmBO lsMonitorAlarmBO = monitorAlarmService.saveAlarm(alarmType, lsMonitorVehicleStatusBO, content);

        // 为报警选择处理人
        // 报警未处理前没有负责人也没有处理人
        // String userId = monitorAlarmService.selectChargeUserId(lsMonitorAlarmBO);
        // if (NuctechUtil.isNotNull(userId)) {
        // lsMonitorAlarmBO.setUserId(userId);
        // lsMonitorAlarmBO.setReceiveTime(new Date());
        // }
        // monitorAlarmService.updateMonitorAlarm(lsMonitorAlarmBO);

        ViewAlarmReportVO alarmReportVO = new ViewAlarmReportVO();
        BeanUtils.copyProperties(lsMonitorAlarmBO, alarmReportVO);
        LsDmAlarmTypeBO alarmTypeBO = alarmLevelModifyService.find(lsMonitorAlarmBO.getAlarmTypeId());
        if (alarmTypeBO != null) {
            alarmReportVO.setAlarmLevelId(alarmTypeBO.getAlarmLevelId());
        }

        // 更改自动报警车辆最新信息风险等级
        if (NuctechUtil.isNotNull(lsMonitorAlarmBO.getTripId())) {
            increaseRiskStatus1(lsMonitorVehicleStatusBO, lsMonitorAlarmBO.getAlarmTypeId());
        }
        // 更改自动报警车辆信息风险等级
        LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService.findById(lsMonitorAlarmBO.getVehicleId());
        if (NuctechUtil.isNotNull(lsMonitorAlarmBO.getTripId())) {
            increaseRiskStatus(lsCommonVehicleBO, lsMonitorAlarmBO.getAlarmTypeId());
        }
        
        //将报警信息添加进缓存
        pushAlarm(alarmReportVO, lsCommonVehicleBO);
        
        // 推送报警信息
        JSONObject message = new JSONObject();
        message.put("messageType", WebSocketMessageType.VEHICLE_ALARM.getType());
        message.put("messageContent", JSONObject.fromObject(alarmReportVO).toString());
        WebsocketService.sendMessage(message.toString());
    }

    /**
     * 将报警信息添加进缓存
     * @param alarmReportVO
     * @param lsCommonVehicleBO
     */
    private void pushAlarm(ViewAlarmReportVO alarmReportVO, LsCommonVehicleBO lsCommonVehicleBO) {
    	JSONObject obj = new JSONObject();
		obj.put("alarmId", CommonStringUtil.ifNull(alarmReportVO.getAlarmId(), ""));
		obj.put("alarmContent", CommonStringUtil.ifNull(alarmReportVO.getAlarmContent(), ""));
		obj.put("alarmTime", CommonStringUtil.ifNull(alarmReportVO.getAlarmTime(), ""));
		obj.put("alarmLongitude", CommonStringUtil.ifNull(alarmReportVO.getAlarmLongitude(), ""));
		obj.put("alarmLatitude", CommonStringUtil.ifNull(alarmReportVO.getAlarmLatitude(), ""));
		obj.put("alarmTypeCode", CommonStringUtil.ifNull(alarmReportVO.getAlarmTypeId(), ""));
		obj.put("alarmLevelCode", CommonStringUtil.ifNull(alarmReportVO.getAlarmLevelId(), ""));
		obj.put("vehiclePlateNumber", CommonStringUtil.ifNull(lsCommonVehicleBO.getVehiclePlateNumber(), ""));
		obj.put("userId", CommonStringUtil.ifNull(alarmReportVO.getUserId(), ""));
		obj.put("tripId", CommonStringUtil.ifNull(alarmReportVO.getTripId(), ""));
		String jsonStr = obj.toString();
		
		String key = RedisKey.ALARMLIST_VEHICLEID_PREFIX + lsCommonVehicleBO.getVehicleId();
		redisClientTemplate.lpush(key, jsonStr);
		redisClientTemplate.ltrim(key, 0, Constant.MAX_PAGE_SIZE);
		
		String keyTrip = RedisKey.ALARMLIST_TRIPID_PREFIX + alarmReportVO.getTripId();
		redisClientTemplate.lpush(keyTrip, jsonStr);
		redisClientTemplate.ltrim(keyTrip, 0, Constant.MAX_PAGE_SIZE);
	}

	/**
     * 增加车辆状态表的风险级别
     * 
     * @param lsMonitorVehicleStatusBO
     */
    private void increaseRiskStatus1(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO, String alarmType) {
        // 根据alarmType判断报警为"轻微"还是"严重"
        LsDmAlarmTypeBO alarmTypeBO = alarmLevelModifyService.find(alarmType);
        if (NuctechUtil.isNotNull(lsMonitorVehicleStatusBO)) {
            String riskStatus = lsMonitorVehicleStatusBO.getRiskStatus();
            riskStatus = NuctechUtil.isNull(riskStatus) ? "0" : riskStatus;
            int statusInt = Integer.parseInt(riskStatus);
            if (alarmTypeBO.getAlarmLevelId().equals("0")) {
                statusInt = Math.min(statusInt + 1, 2);
            } else {
                statusInt = Math.min(statusInt + 2, 2);
            }
            statusInt = Math.min(statusInt + 1, 2);
            lsMonitorVehicleStatusBO.setRiskStatus(String.valueOf(statusInt));
            // monitorVehicleStatusService.saveOrUpdate(lsMonitorVehicleStatusBO);
        }
    }

    /**
     * 
     * 增加车辆的风险级别(严重报警等级加2，轻微报警等级加1)
     * 
     * @param lsCommonVehicleBO
     */
    private void increaseRiskStatus(LsCommonVehicleBO lsCommonVehicleBO, String alarmType) {
        // 根据alarmType判断报警为"轻微"还是"严重"
        LsDmAlarmTypeBO alarmTypeBO = alarmLevelModifyService.find(alarmType);
        if (NuctechUtil.isNotNull(lsCommonVehicleBO)) {
            String riskStatus = lsCommonVehicleBO.getRiskStatus();
            riskStatus = NuctechUtil.isNull(riskStatus) ? "0" : riskStatus;
            int statusInt = Integer.parseInt(riskStatus);
            if (alarmTypeBO.getAlarmLevelId().equals("0")) {
                statusInt = Math.min(statusInt + 1, 2);
            } else {
                statusInt = Math.min(statusInt + 2, 2);
            }
            lsCommonVehicleBO.setRiskStatus(String.valueOf(statusInt));
            commonVehicleService.updateCommonVehicle(lsCommonVehicleBO);
        }
    }

    // private void setLsMonitorVehicleStatusBO(LsMonitorVehicleStatusBO value) {
    // LsMonitorVehicleStatusBO statusBO = lsMonitorVehicleStatusBO.get();
    // //if (null == statusBO) {
    // statusBO = value.getClone(value);
    // lsMonitorVehicleStatusBO.set(statusBO);
    // // }
    // }

}
