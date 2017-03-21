package com.nuctech.ls.services.gps.utils;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.model.bo.ValidateResult;
import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.bo.system.LsSystemRoleBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.system.LsSystemUserRoleBO;
import com.nuctech.ls.model.memcached.MemcachedUtil;
import com.nuctech.ls.model.redis.RedisClientTemplate;
import com.nuctech.ls.model.service.CommonPatrolService;
import com.nuctech.ls.model.service.CommonVehicleService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.MonitorVehicleGpsService;
import com.nuctech.ls.model.service.MonitorVehicleStatusService;
import com.nuctech.ls.model.service.SystemRoleService;
import com.nuctech.ls.model.service.SystemUserRoleService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.util.VehicleType;
import com.nuctech.ls.model.util.WebSocketMessageType;
import com.nuctech.ls.model.vo.system.CommonPatrolRoleVO;
import com.nuctech.ls.services.gps.alarm.VehicleAlarmHandler;
import com.nuctech.ls.services.gps.models.GpsMessage;
import com.nuctech.ls.services.gps.models.LockMessage;
import com.nuctech.ls.services.gps.models.RequestMessage;
import com.nuctech.ls.services.websokcet.WebsocketService;
import com.nuctech.util.Constant;
import com.nuctech.util.DateUtils;
import com.nuctech.util.GpsToGoogleChinaMap;
import com.nuctech.util.NuctechUtil;

import edu.emory.mathcs.backport.java.util.Arrays;
import net.sf.json.JSONObject;

/**
 * @author sunming
 *
 */
@Component
public class MonitorVehicleGPSHandle {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource
    public MonitorVehicleGpsService monitorVehicleGpsService;
    @Resource
    public MonitorTripService monitorTripService;
    @Resource
    public MonitorVehicleStatusService monitorVehicleStatusService;
    @Resource
    public CommonPatrolService commonPatrolService;
    @Resource
    public VehicleAlarmHandler vehicleAlarmHandler;
    @Resource
    public WebsocketService websocketService;
    @Resource
    public MemcachedUtil memcachedUtil;
    @Resource
	private RedisClientTemplate redisClientTemplate;
    @Resource
    public CommonVehicleService commonVehicleService;
    @Resource
    public SystemUserService systemUserService;
    @Resource
    public SystemRoleService systemRoleService;
    @Resource
    public SystemUserRoleService systemUserRoleService;
    @Resource
    private SystemModules systemModules;

    private static final String PRE_VHEICLE_GPS_TABLE_NAME = "LS_MONITOR_VEHICLE_GPS_";
    private static final String PRE_PORTAL_GPS_TABLE_NAME = "LS_MONITOR_PORTAL_GPS_";
    private static final String PORTAL_MESSAGE_SIGNAL = "AT";
    private static final String LOCK = "A";
    private static DecimalFormat df = new DecimalFormat("#######.000000");

    /**
     * 转换GPS信息并保存入库
     * 
     * @param requestMessage
     */
    public void dealRequestMessage(RequestMessage requestMessage) {

        try {
            logger.info("handle message:" + requestMessage.getSeq_num() + "-----" + requestMessage.getPhone_num());
            if (NuctechUtil.isNotNull(requestMessage.getGps())) {
                GpsMessage gpsMessage = requestMessage.getGps();
                logger.info("handle message:" + requestMessage.getPhone_num() + "-----"
                        + JSONObject.fromObject(gpsMessage).toString());
                // 转换json消息字符串成LsMonitorVehicleGpsBO实体对象
                LsMonitorVehicleGpsBO monitorVehicleGpsBO = convertMonitorVehicleGps(requestMessage, gpsMessage);
                // 如果是巡逻队的车载台
                if (NuctechUtil.isNotNull(monitorVehicleGpsBO.getTrackingDeviceNumber())) {
                    if (NuctechUtil.isNotNull(requestMessage.getDeviceType())
                            && PORTAL_MESSAGE_SIGNAL.endsWith(requestMessage.getDeviceType())) {
                        // 如果车载台和巡逻队模块开启，才往下处理
                        // if (!systemModules.isPatrolOn()) {
                        // continue;
                        // }
                        LsCommonPatrolBO lsCommonPatrolBO = commonPatrolService
                                .findCommonPatrolByTrackUnitNumber(monitorVehicleGpsBO.getTrackingDeviceNumber());
                        // 查找到巡逻队再插入
                        if (lsCommonPatrolBO != null) {
                            if (NuctechUtil.isNotNull(lsCommonPatrolBO.getTripId())) {
                                monitorVehicleGpsBO.setTripId(lsCommonPatrolBO.getTripId());
                            }
                            monitorVehicleGpsBO.setLocationType(VehicleType.Patrol.getType());
                            saveLsMonitorVehicleGpsInfo(monitorVehicleGpsBO, WebSocketMessageType.PORTAL_GPS.getType());
                        }
                    } else {
                        // 验证数据是否需要保存
                    	ValidateResult result = validateMonitorVehicleGps(monitorVehicleGpsBO);
                        // 单独将数据保存到memcached里面，激活行程查询用
                        saveCachedStatusBO(monitorVehicleGpsBO);
                        // 验证通过后保存数据
                        if (result.isSaveFlag()) {
                            saveLsMonitorVehicleGpsInfo(monitorVehicleGpsBO,
                                    WebSocketMessageType.VEHICLE_GPS.getType(), result);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("dealRequestMessage exception!!!!", e);
        }

    }

    /**
     * 处理掉线信息
     * 
     * @param requestMessage
     */
    public void dealOffLineMessage(RequestMessage requestMessage) {
        try {
            String timestamp = requestMessage.getTimestamp();
            Date offLineDate = null;
            if (NuctechUtil.isNotNull(timestamp)) {
                offLineDate = DateUtils.strToDate(timestamp, "yyyy-MM-dd HH:mm:ss").getTime();
            } else {
                logger.warn("Elock offLine timestamp is null!");
                return;
            }
            String trackingDeviceNumber = requestMessage.getPhone_num();
            logger.info("dealOffLineMessage:" + trackingDeviceNumber);

            // 首先根据设备号查询行程
            if (NuctechUtil.isNotNull(trackingDeviceNumber)) {
                // 查找行程数据中是否存在当前设备
                LsMonitorTripBO lsMonitorTripBO = monitorTripService
                        .findLastestMonitortripByDeviceNumber(trackingDeviceNumber);
                if (NuctechUtil.isNotNull(lsMonitorTripBO)) {
                    if (NuctechUtil.isNotNull(lsMonitorTripBO.getCheckinTime())
                            && offLineDate.getTime() > lsMonitorTripBO.getCheckinTime().getTime()) {
                        if (Constant.TRIP_STATUS_STARTED.equals(lsMonitorTripBO.getTripStatus())
                                || Constant.TRIP_STATUS_TO_START.equals(lsMonitorTripBO.getTripStatus())) {
                            LsMonitorVehicleStatusBO monitorVehicleStatusBO = monitorVehicleStatusService
                                    .findVehicleStatusByNumAndTripId(lsMonitorTripBO.getTripId(), trackingDeviceNumber);
                            LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService
                                    .findLatestCommonVehicleBo(lsMonitorTripBO.getTripId(), trackingDeviceNumber);
                            if (NuctechUtil.isNotNull(lsCommonVehicleBO)) {
                                if (Constant.FREEZE_ALARM_YES.equals(lsCommonVehicleBO.getFreezeAlarm())) {
                                    vehicleAlarmHandler.alarmOffLine(monitorVehicleStatusBO);
                                }
                            }
                        } else {
                            logger.info("TRIP_STATUS_STARTED is abnormal!");
                        }
                    }
                } else {
                    logger.info("lsMonitorTripBO is null!");
                }
            }

        } catch (Exception e) {
            logger.error("dealOffLineMessage exception!", e);
        }
    }
    
    /**
     * 保存车辆或巡逻队的GPS信息
     * 
     * @param monitorVehicleGpsBO
     * @throws Exception
     */
    private void saveLsMonitorVehicleGpsInfo(LsMonitorVehicleGpsBO monitorVehicleGpsBO, String messageType)
            throws Exception {
    	saveLsMonitorVehicleGpsInfo(monitorVehicleGpsBO, messageType, new ValidateResult(true, true));
    }

    /**
     * 保存车辆或巡逻队的GPS信息
     * 
     * @param monitorVehicleGpsBO
     * @throws Exception
     */
    private void saveLsMonitorVehicleGpsInfo(LsMonitorVehicleGpsBO monitorVehicleGpsBO, String messageType, ValidateResult result)
            throws Exception {
        String trackingDeviceNumber = monitorVehicleGpsBO.getTrackingDeviceNumber();
        String tripId = monitorVehicleGpsBO.getTripId();
        // 数据库对应表名称
        String tableName = PRE_VHEICLE_GPS_TABLE_NAME + trackingDeviceNumber;
        // 将保存的信息放入缓存中
        LsMonitorVehicleStatusBO monitorVehicleStatusBO = monitorVehicleStatusService
                .findVehicleStatusByNumAndTripId(tripId, trackingDeviceNumber);
        LsMonitorTripBO lsMonitorTripBO = monitorTripService
                .findLastestMonitortripByDeviceNumber(monitorVehicleGpsBO.getTrackingDeviceNumber());
        if (WebSocketMessageType.PORTAL_GPS.getType().equals(messageType)) {
            tableName = PRE_PORTAL_GPS_TABLE_NAME + trackingDeviceNumber;
            monitorVehicleStatusBO = convertCachedStatusBO(monitorVehicleStatusBO, monitorVehicleGpsBO);

            // 巡逻队也需要保存行程状态
            if (null == lsMonitorTripBO) {
                lsMonitorTripBO = monitorTripService.findById(tripId);
                if (null != lsMonitorTripBO) {
                    monitorVehicleStatusBO.setTripStatus(lsMonitorTripBO.getTripStatus());
                }
            }
        } else {
            monitorVehicleStatusBO = convertCachedStatusBO(monitorVehicleStatusBO, monitorVehicleGpsBO,
                    lsMonitorTripBO);
        }

        if (NuctechUtil.isNotNull(monitorVehicleStatusBO)) {
            // 状态表保存实际GPS上数的设备号，以便地图上查找路线
            monitorVehicleStatusBO.setGpsTrackingDeviceNumber(trackingDeviceNumber);
            // 保存最新坐标信息
            // TODO 有可能查到的定位时间比当前线程的记录要新，此时是否应该覆盖？
            synchronized (LOCK) {
                LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO = monitorVehicleStatusService
                        .findVehicleStatusByNumAndTripId(tripId, trackingDeviceNumber);
                if (NuctechUtil.isNotNull(lsMonitorVehicleStatusBO)) {
                    monitorVehicleStatusBO.setVehicleStatusId(lsMonitorVehicleStatusBO.getVehicleStatusId());
                } else {
                    monitorVehicleStatusBO.setVehicleStatusId(generatePrimaryKey());
                }
                monitorVehicleStatusService.saveOrUpdate(monitorVehicleStatusBO);
            }
        }

        // 保存GPS数据
        monitorVehicleGpsService.saveMonitorVehicleGps(monitorVehicleGpsBO, tableName);

//        Object object = memcachedUtil.get(monitorVehicleGpsBO.getTrackingDeviceNumber(),
//                LsMonitorVehicleStatusBO.class);
//        if (object == null) {
//            memcachedUtil.add(monitorVehicleGpsBO.getTrackingDeviceNumber(), monitorVehicleStatusBO);
//        } else {
//            memcachedUtil.replace(monitorVehicleGpsBO.getTrackingDeviceNumber(), monitorVehicleStatusBO);
//        }
        JSONObject jsonObject = JSONObject.fromObject(monitorVehicleStatusBO);
        redisClientTemplate.set(monitorVehicleGpsBO.getTrackingDeviceNumber(), jsonObject.toString());
        
        if(result.isSendFlag()) {
        	if (WebSocketMessageType.PORTAL_GPS.getType().equals(messageType)) {
        		LsCommonPatrolBO bo = commonPatrolService
        				.findCommonPatrolByTrackUnitNumber(monitorVehicleGpsBO.getTrackingDeviceNumber());
        		LsSystemUserBO lsSystemUserBO = systemUserService.findById(bo.getPotralUser());
        		LsSystemUserRoleBO lsSystemUserRoleBO = systemUserRoleService.findRoleByUserId(lsSystemUserBO.getUserId());
        		LsSystemRoleBO lsSystemRoleBO = systemRoleService.findById(lsSystemUserRoleBO.getRoleId());
        		CommonPatrolRoleVO commonPatrolRoleVO = new CommonPatrolRoleVO();
        		commonPatrolRoleVO.setCommonPatrolBO(bo);
        		commonPatrolRoleVO.setMonitorVehicleGpsBO(monitorVehicleGpsBO);
        		commonPatrolRoleVO.setLsSystemUserBO(lsSystemUserBO);
        		commonPatrolRoleVO.setLsSystemUserRoleBO(lsSystemUserRoleBO);
        		commonPatrolRoleVO.setLsSystemRoleBO(lsSystemRoleBO);
        		sendWebsocketMessage(commonPatrolRoleVO, WebSocketMessageType.PORTAL_GPS.getType());
        	} else {
        		sendWebsocketMessage(monitorVehicleGpsBO, WebSocketMessageType.VEHICLE_GPS.getType());
        	}
        }
    }

    /**
     * @param monitorVehicleGpsBO
     * @param messageType
     */
    private void sendWebsocketMessage(Object object, String messageType) {
        JSONObject message = new JSONObject();
        message.put("messageType", messageType);
        message.put("messageContent", JSONObject.fromObject(object));
        WebsocketService.sendMessage(message.toString());
    }

    /**
     * @param monitorVehicleGpsBO
     * @return
     * @throws Exception
     */
    private ValidateResult validateMonitorVehicleGps(LsMonitorVehicleGpsBO monitorVehicleGpsBO) throws Exception {
    	ValidateResult result = new ValidateResult();
        Date locationTime = monitorVehicleGpsBO.getLocationTime();
        if (NuctechUtil.isNull(locationTime)) {
            logger.info("GPS location time is null,will ignore this record");
            return result;
        }
        // 首先判断measureTime是否大于当前时间十分钟以上，若大于则为非法GIS数据，直接废弃
        Date nowDate = new Date();
        if (locationTime.getTime() > nowDate.getTime() + 1 * 10 * 60 * 1000) {
            logger.info("GIS provide the actual test time is more than ten minutes,coordinate invalid,"
                    + "gpsMeasure.getMeasureTime().getTime()=" + locationTime);
            return result;
        }
        // 判断坐标是否为0
        if (0 == Double.valueOf(monitorVehicleGpsBO.getLatitude())
                || 0 == Double.valueOf(monitorVehicleGpsBO.getLongitude())) {
            logger.info("the coordinate of longitude(latitude) from GPS is 0");
            return result;
        }
        String trackingDeviceNumber = monitorVehicleGpsBO.getTrackingDeviceNumber();
        // 设备号不为空
        if (trackingDeviceNumber != null && !"".equals(trackingDeviceNumber)) {
            // logger.info(" 追踪设备号=" + trackingDeviceNumber);
            // 查找行程数据中是否存在当前设备
            LsMonitorTripBO lsMonitorTripBO = monitorTripService
                    .findLastestMonitortripByDeviceNumber(trackingDeviceNumber);

            if (null != lsMonitorTripBO) {
                // 设置行程关联
                monitorVehicleGpsBO.setTripId(lsMonitorTripBO.getTripId());
                logger.info("the trip information : the trip number=" + lsMonitorTripBO.getTripId() + ",device number="
                        + trackingDeviceNumber + ",departure port=" + lsMonitorTripBO.getCheckinPort()
                        + ",departure time=" + lsMonitorTripBO.getCheckinTime() + ",arrive port="
                        + lsMonitorTripBO.getCheckoutPort());
                // 判断locationTime是否大于车辆检入时间，若小于则为历史GIS数据,正常存储等待后台定时任务处理
                Date checkInDateTime = lsMonitorTripBO.getCheckinTime();
                if (locationTime.getTime() < checkInDateTime.getTime()) {
                    // logger.info(
                    // "历史车辆补发数据，GIS提供真实测试测量时间小于车辆检入系统时间，等待其他任务处理，定位时间=" + locationTime);
                    // 若此车辆,不接收实时坐标信息
                    return result;
                } else if (locationTime.getTime() > checkInDateTime.getTime()
                        && locationTime.getTime() < new Date().getTime() - 1 * 10 * 60 * 1000) {
                    // 若locationTime大于车辆检入时间，但小于当前系统时间10分钟以上，则视为当前车辆的补传数据,正常存入数据库，但不推送
                    // logger.info("当前车辆补发数据，GIS提供真实测试测量时间大于车辆检入时间，但小于当前系统时间10分钟以上，定位时间=" +
                    // locationTime);
                    // 若此车辆已经完成,不接收实时坐标信息
                    if (Constant.TRIP_STATUS_FINISHED.equals(lsMonitorTripBO.getTripStatus())) {
                        return result;
                    } else if (Constant.TRIP_STATUS_STARTED.equals(lsMonitorTripBO.getTripStatus())
                            || Constant.TRIP_STATUS_TO_START.equals(lsMonitorTripBO.getTripStatus())) {
                    	result.setSaveFlag(true);
                        return result;
                    }
                } else {
                    // 若mesaureTime大于车辆检入时间，但小于当前系统时间10分钟以内,正常保存，且推送
                    // this.logger.info(
                    // "当前车辆实时数据，gpsMeasure.getMeasureTime().getTime()=" + locationTime);
                    // 若此车辆已经完成,不接收实时坐标信息
                    if (Constant.TRIP_STATUS_FINISHED.equals(lsMonitorTripBO.getTripStatus())) {
                        return result;
                    } else if (Constant.TRIP_STATUS_STARTED.equals(lsMonitorTripBO.getTripStatus())
                            || Constant.TRIP_STATUS_TO_START.equals(lsMonitorTripBO.getTripStatus())) {
                        // 封装最新状态对象，放置缓存中
                        LsMonitorVehicleStatusBO monitorVehicleStatusBO = monitorVehicleStatusService
                                .findVehicleStatusByNumAndTripId(lsMonitorTripBO.getTripId(), trackingDeviceNumber);
                        monitorVehicleStatusBO = convertCachedStatusBO(monitorVehicleStatusBO, monitorVehicleGpsBO,
                                lsMonitorTripBO);

                        // 判断报警状态
                        // vehicleAlarmHandler.setLsMonitorVehicleStatusBO(monitorVehicleStatusBO);
                        // 报警分析和处理
                        LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService
                                .findLatestCommonVehicleBo(lsMonitorTripBO.getTripId(), trackingDeviceNumber);
                        if (NuctechUtil.isNotNull(lsCommonVehicleBO)) {
                            if (Constant.FREEZE_ALARM_YES.equals(lsCommonVehicleBO.getFreezeAlarm())) {
                                vehicleAlarmHandler.alarmDistinguish(monitorVehicleStatusBO);
                            }
                        }
                        result.setSaveFlag(true);
                        result.setSendFlag(true);
                        return result;
                    }
                }
            }
        } else {
            logger.info(
                    "did not found" + monitorVehicleGpsBO.getTrackingDeviceNumber() + "correspond trip information");
        }

        return result;
    }

    /**
     * 将RequestMessage转化为List<LsMonitorVehicleGpsBO>
     * 
     * @param requestMessage
     * @return
     */
    private LsMonitorVehicleGpsBO convertMonitorVehicleGps(RequestMessage requestMessage, GpsMessage gps) {
        LsMonitorVehicleGpsBO monitorVehicleGpsBO = new LsMonitorVehicleGpsBO();

        /**
         * base info
         */

        String trackingDeviceNumber = requestMessage.getPhone_num();

        // tracking device设备
        monitorVehicleGpsBO.setLocationType(VehicleType.Vehicle.getType());

        // 设置序列号
        if (null != requestMessage.getSeq_num() && !"".equals(requestMessage.getSeq_num())) {
            String sequence = requestMessage.getSeq_num().replace(",", "");
            monitorVehicleGpsBO.setGpsSeq(Long.valueOf(sequence));
        }

        // 设置关锁号
        if (trackingDeviceNumber != null && !"".equals(trackingDeviceNumber)) {
            monitorVehicleGpsBO.setTrackingDeviceNumber(trackingDeviceNumber);
            String realTrackingDeviceNumber = monitorTripService
                    .findRealTrackingDeviceNumberByUpload(trackingDeviceNumber);
            if (null != realTrackingDeviceNumber && !"".equals(realTrackingDeviceNumber)) {
                monitorVehicleGpsBO.setTrackingDeviceNumber(realTrackingDeviceNumber);
            }

        }

        /**
         * GPS info
         */
        if (NuctechUtil.isNotNull(gps)) {
            Long latitude = Long.valueOf(gps.getLatitude());
            Long longitude = Long.valueOf(gps.getLongitude());
            if (latitude != null && !"".equals(latitude) && longitude != null && !"".equals(longitude)) {
                double latOrigin = (double) latitude / 1000000;
                double lngOrigin = (double) longitude / 1000000;
                double latTranformDouble = GpsToGoogleChinaMap.transformToLat(latOrigin, lngOrigin);
                String latTranform = df.format(latTranformDouble);
                double lngTranformDouble = GpsToGoogleChinaMap.transformToLon(latOrigin, lngOrigin);
                String lngTranform = df.format(lngTranformDouble);
                monitorVehicleGpsBO.setLatitude(String.valueOf(latTranform));
                monitorVehicleGpsBO.setLongitude(String.valueOf(lngTranform));
            }

            // if (longitude != null && !"".equals(longitude)) {
            // //monitorVehicleGpsBO.setLongitude(String.valueOf((double) longitude / 1000000));
            // logger.info("经度" + longitude);
            // }

            if (latitude != null && !"".equals(latitude)) {
                monitorVehicleGpsBO.setAltitude(gps.getAltitude());
            }
            String speed = gps.getSpeed();
            if (speed != null && !"".equals(speed)) {
                monitorVehicleGpsBO.setElockSpeed(String.valueOf(Double.parseDouble(speed)));
            }
            String state = gps.getState();
            if (state != null && !"".equals(state)) {
                monitorVehicleGpsBO.setLocationStatus(state);
            }
            String direction = gps.getDirection();
            if (direction != null && !"".equals(direction)) {
                monitorVehicleGpsBO.setDirection(direction);
            }

            String timestamp = gps.getTimestamp();
            if (timestamp != null && !"".equals(timestamp)) {
                monitorVehicleGpsBO.setLocationTime(DateUtils.strToDate(timestamp, "yyyy-MM-dd HH:mm:ss").getTime());
            }
        }

        /**
         * lock info
         */
        LockMessage lock = requestMessage.getLock();

        if (lock != null && !"".equals(lock)) {
            // 设置电量
            String vvv = lock.getVvv();
            if (vvv != null && !"".equals(vvv)) {
                monitorVehicleGpsBO.setElectricityValue(vvv);
            }

            String sealStatus = lock.getSealStatus();

            String lockstatus = lock.getStatus();

            String antidismantle = lock.getAntidismantle();

            String[] seals = lock.getSeals();

            if (antidismantle != null && !"".equals(antidismantle)) {
                monitorVehicleGpsBO.setBrokenStatus(antidismantle);
            }

            // 施解封状态：1----施封 0----解封
            if (sealStatus != null && !"".equals(sealStatus)) {
                monitorVehicleGpsBO.setElockStatus(sealStatus);
            }

            // 锁开关状态：1----关 2----开
            if (lockstatus != null && !"".equals(lockstatus)) {
                monitorVehicleGpsBO.setPoleStatus(lockstatus);
            }

            if (null != lock.getEvents() && !"".equals(lock.getEvents())) {
                monitorVehicleGpsBO.setEventUpload(lock.getEvents());
            }
            // 关联设备情况
            if (seals != null && seals.length > 0) {
                monitorVehicleGpsBO.setRelatedDevice(Arrays.toString(seals));
            }
        }

        return monitorVehicleGpsBO;
    }

    /**
     * 将当前最新车辆状态组装成对象 用于放置缓存中
     * 
     * @param monitorVehicleGpsBO
     * @param lsMonitorTripBO
     * @return
     */
    private LsMonitorVehicleStatusBO convertCachedStatusBO(LsMonitorVehicleStatusBO monitorVehicleStatusBO,
            LsMonitorVehicleGpsBO monitorVehicleGpsBO) {
        if (NuctechUtil.isNull(monitorVehicleStatusBO)) {
            monitorVehicleStatusBO = new LsMonitorVehicleStatusBO();
        }
        monitorVehicleStatusBO.setTripId(monitorVehicleGpsBO.getTripId());
        monitorVehicleStatusBO.setTrackingDeviceNumber(monitorVehicleGpsBO.getTrackingDeviceNumber());
        monitorVehicleStatusBO.setGpsSeq(monitorVehicleGpsBO.getGpsSeq());
        monitorVehicleStatusBO.setLocationTime(monitorVehicleGpsBO.getLocationTime());
        monitorVehicleStatusBO.setLocationStatus(monitorVehicleGpsBO.getLocationStatus());
        monitorVehicleStatusBO.setElockStatus(monitorVehicleGpsBO.getElockStatus());
        monitorVehicleStatusBO.setLocationType(monitorVehicleGpsBO.getLocationType());
        monitorVehicleStatusBO.setPoleStatus(monitorVehicleGpsBO.getPoleStatus());
        monitorVehicleStatusBO.setBrokenStatus(monitorVehicleGpsBO.getBrokenStatus());
        monitorVehicleStatusBO.setEventUpload(monitorVehicleGpsBO.getEventUpload());
        monitorVehicleStatusBO.setLongitude(monitorVehicleGpsBO.getLongitude());
        monitorVehicleStatusBO.setLatitude(monitorVehicleGpsBO.getLatitude());
        monitorVehicleStatusBO.setAltitude(monitorVehicleGpsBO.getAltitude());
        monitorVehicleStatusBO.setElockSpeed(monitorVehicleGpsBO.getElockSpeed());
        monitorVehicleStatusBO.setDirection(monitorVehicleGpsBO.getDirection());
        monitorVehicleStatusBO.setElectricityValue(monitorVehicleGpsBO.getElectricityValue());
        monitorVehicleStatusBO.setCreateTime(monitorVehicleGpsBO.getCreateTime());

        return monitorVehicleStatusBO;
    }

    /**
     * 将当前最新车辆状态组装成对象 用于放置缓存中
     * 
     * @param monitorVehicleGpsBO
     * @param lsMonitorTripBO
     * @return
     * @throws Exception
     */
    private LsMonitorVehicleStatusBO convertCachedStatusBO(LsMonitorVehicleStatusBO monitorVehicleStatusBO,
            LsMonitorVehicleGpsBO monitorVehicleGpsBO, LsMonitorTripBO lsMonitorTripBO) throws Exception {

        if (lsMonitorTripBO == null) {
            return null;
        }

        if (NuctechUtil.isNull(monitorVehicleStatusBO)) {
            monitorVehicleStatusBO = new LsMonitorVehicleStatusBO();
        }

        monitorVehicleStatusBO.setTripId(lsMonitorTripBO.getTripId());
        monitorVehicleStatusBO.setCheckinUser(lsMonitorTripBO.getCheckinUser());
        monitorVehicleStatusBO.setCheckinTime(lsMonitorTripBO.getCheckinTime());
        monitorVehicleStatusBO.setCheckinPort(lsMonitorTripBO.getCheckinPort());
        // monitorVehicleStatusBO.setCheckoutUser(lsMonitorTripBO.getCheckoutUser());
        // monitorVehicleStatusBO.setCheckoutTime(lsMonitorTripBO.getCheckoutTime());
        monitorVehicleStatusBO.setCheckoutPort(lsMonitorTripBO.getCheckoutPort());
        // monitorVehicleStatusBO.setCheckoutPicture(lsMonitorTripBO.getCheckoutPicture());
        monitorVehicleStatusBO.setTripStatus(lsMonitorTripBO.getTripStatus());
        monitorVehicleStatusBO.setGroupId(lsMonitorTripBO.getGroupId());
        monitorVehicleStatusBO.setRouteId(lsMonitorTripBO.getRouteId());

        // monitorVehicleStatusBO.setIsAlarm();
        // monitorVehicleStatusBO.setTravelStatus();

        if (monitorVehicleGpsBO != null) {
            monitorVehicleStatusBO.setGpsSeq(monitorVehicleGpsBO.getGpsSeq());
            monitorVehicleStatusBO.setLocationTime(monitorVehicleGpsBO.getLocationTime());
            monitorVehicleStatusBO.setLocationStatus(monitorVehicleGpsBO.getLocationStatus());
            monitorVehicleStatusBO.setElockStatus(monitorVehicleGpsBO.getElockStatus());
            monitorVehicleStatusBO.setLocationType(monitorVehicleGpsBO.getLocationType());
            monitorVehicleStatusBO.setPoleStatus(monitorVehicleGpsBO.getPoleStatus());
            monitorVehicleStatusBO.setBrokenStatus(monitorVehicleGpsBO.getBrokenStatus());
            monitorVehicleStatusBO.setEventUpload(monitorVehicleGpsBO.getEventUpload());
            // double googleLat =
            // GpsToGoogleChinaMap.transformToLat(Double.parseDouble(monitorVehicleGpsBO.getLatitude()),
            // Double.parseDouble(monitorVehicleGpsBO.getLongitude()));
            // double googlelon =
            // GpsToGoogleChinaMap.transformToLon(Double.parseDouble(monitorVehicleGpsBO.getLatitude()),
            // Double.parseDouble(monitorVehicleGpsBO.getLongitude()));
            monitorVehicleStatusBO.setLongitude(monitorVehicleGpsBO.getLongitude());
            monitorVehicleStatusBO.setLatitude(monitorVehicleGpsBO.getLatitude());
            monitorVehicleStatusBO.setAltitude(monitorVehicleGpsBO.getAltitude());
            monitorVehicleStatusBO.setElockSpeed(monitorVehicleGpsBO.getElockSpeed());
            monitorVehicleStatusBO.setDirection(monitorVehicleGpsBO.getDirection());
            monitorVehicleStatusBO.setElectricityValue(monitorVehicleGpsBO.getElectricityValue());
            monitorVehicleStatusBO.setCreateTime(monitorVehicleGpsBO.getCreateTime());
            LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService.findLatestCommonVehicleBo(
                    lsMonitorTripBO.getTripId(), monitorVehicleGpsBO.getTrackingDeviceNumber());
            if (null != lsCommonVehicleBO) {
                monitorVehicleStatusBO.setVehicleId(lsCommonVehicleBO.getVehicleId());
                monitorVehicleStatusBO.setTrackingDeviceNumber(lsCommonVehicleBO.getTrackingDeviceNumber());
                monitorVehicleStatusBO.setEsealNumber(lsCommonVehicleBO.getEsealNumber());
                monitorVehicleStatusBO.setSensorNumber(lsCommonVehicleBO.getSensorNumber());
                monitorVehicleStatusBO.setEsealOrder(lsCommonVehicleBO.getEsealOrder());
                monitorVehicleStatusBO.setSensorOrder(lsCommonVehicleBO.getSensorOrder());
                monitorVehicleStatusBO.setCheckinPicture(lsCommonVehicleBO.getCheckinPicture());
            }
        }

        return monitorVehicleStatusBO;
    }

    /**
     * 将当前最新车辆状态组装成对象 用于放置缓存中
     * 
     * @param monitorVehicleGpsBO
     * @param lsMonitorTripBO
     * @return
     */
    private LsMonitorVehicleStatusBO saveCachedStatusBO(LsMonitorVehicleGpsBO monitorVehicleGpsBO) {

        LsMonitorVehicleStatusBO monitorVehicleStatusBO = new LsMonitorVehicleStatusBO();

        monitorVehicleStatusBO.setTrackingDeviceNumber(monitorVehicleGpsBO.getTrackingDeviceNumber());
        monitorVehicleStatusBO.setGpsSeq(monitorVehicleGpsBO.getGpsSeq());
        monitorVehicleStatusBO.setLocationTime(monitorVehicleGpsBO.getLocationTime());
        monitorVehicleStatusBO.setLocationStatus(monitorVehicleGpsBO.getLocationStatus());
        monitorVehicleStatusBO.setElockStatus(monitorVehicleGpsBO.getElockStatus());
        monitorVehicleStatusBO.setLocationType(monitorVehicleGpsBO.getLocationType());
        monitorVehicleStatusBO.setPoleStatus(monitorVehicleGpsBO.getPoleStatus());
        monitorVehicleStatusBO.setBrokenStatus(monitorVehicleGpsBO.getBrokenStatus());
        monitorVehicleStatusBO.setEventUpload(monitorVehicleGpsBO.getEventUpload());
        monitorVehicleStatusBO.setLongitude(monitorVehicleGpsBO.getLongitude());
        monitorVehicleStatusBO.setLatitude(monitorVehicleGpsBO.getLatitude());
        monitorVehicleStatusBO.setAltitude(monitorVehicleGpsBO.getAltitude());
        monitorVehicleStatusBO.setElockSpeed(monitorVehicleGpsBO.getElockSpeed());
        monitorVehicleStatusBO.setDirection(monitorVehicleGpsBO.getDirection());
        monitorVehicleStatusBO.setElectricityValue(monitorVehicleGpsBO.getElectricityValue());
        monitorVehicleStatusBO.setCreateTime(monitorVehicleGpsBO.getCreateTime());
        if (NuctechUtil.isNotNull(monitorVehicleGpsBO.getTripId())) {
        	//将最新GPS数据，存入该行程的key中
            String key = monitorVehicleGpsBO.getTrackingDeviceNumber() + ":" + monitorVehicleGpsBO.getTripId();
            JSONObject obj = new JSONObject();
            obj.put("latitude", monitorVehicleGpsBO.getLatitude());
            obj.put("longitude", monitorVehicleGpsBO.getLongitude());
            obj.put("direction", monitorVehicleGpsBO.getDirection());
            obj.put("locationTime", monitorVehicleGpsBO.getLocationTime());
            obj.put("electricityValue", monitorVehicleGpsBO.getElectricityValue());
            obj.put("altitude", monitorVehicleGpsBO.getAltitude());
            obj.put("elockSpeed", monitorVehicleGpsBO.getElockSpeed());
            redisClientTemplate.lpush(key, obj.toString());
            redisClientTemplate.ltrim(key, 0, Constant.MAX_PAGE_SIZE);
            
            LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService.findLatestCommonVehicleBo(
                    monitorVehicleGpsBO.getTripId(), monitorVehicleGpsBO.getTrackingDeviceNumber());
            if (null != lsCommonVehicleBO) {
                monitorVehicleStatusBO.setVehicleId(lsCommonVehicleBO.getVehicleId());
            }
        }

//        Object object = memcachedUtil.get("trip_" + monitorVehicleGpsBO.getTrackingDeviceNumber(),
//                LsMonitorVehicleStatusBO.class);
//        if (object == null) {
//            memcachedUtil.add("trip_" + monitorVehicleGpsBO.getTrackingDeviceNumber(), monitorVehicleStatusBO);
//        } else {
//            memcachedUtil.replace("trip_" + monitorVehicleGpsBO.getTrackingDeviceNumber(), monitorVehicleStatusBO);
//        }
        JSONObject jsonObject = JSONObject.fromObject(monitorVehicleStatusBO);
        redisClientTemplate.set("trip_" + monitorVehicleGpsBO.getTrackingDeviceNumber(), jsonObject.toString());

        return monitorVehicleStatusBO;
    }

    /**
     * 获取主键
     * 
     * @return
     */
    public String generatePrimaryKey() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static void main(String[] args) {
        String[] seals = { "1", "2", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1" };
        System.out.println(Arrays.toString(seals));
        System.out.println(Arrays.toString(seals).substring(1, Arrays.toString(seals).length() - 1));
        System.out.println(Arrays.binarySearch(seals, "2"));
    }

}
