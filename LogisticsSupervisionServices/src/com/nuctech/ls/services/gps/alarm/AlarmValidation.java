/**
 * 
 */
package com.nuctech.ls.services.gps.alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.nuctech.gis.GisPoint;
import com.nuctech.gis.GisUtil;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRaPointBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTargetZoneLogBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.memcached.MemcachedUtil;
import com.nuctech.ls.model.redis.RedisClientTemplate;
import com.nuctech.ls.model.service.CommonTargetZoonService;
import com.nuctech.ls.model.service.MonitorAlarmService;
import com.nuctech.ls.model.service.MonitorRaPointService;
import com.nuctech.ls.model.service.MonitorRouteAreaService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.SystemParamsService;
import com.nuctech.ls.model.util.DeviceWorkStatus;
import com.nuctech.ls.model.util.LocationStatus;
import com.nuctech.ls.model.util.RouteAreaType;
import com.nuctech.ls.model.util.SystemParams;
import com.nuctech.util.DateUtils;
import com.nuctech.util.DeviceUtils;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 报警分析
 * 
 * @author sunming
 *
 */
@Component
public class AlarmValidation {

    Logger logger = Logger.getLogger(this.getClass());
    
    
    @Resource
    public SystemParamsService systemParamsService;
    @Resource
    public MonitorRouteAreaService monitorRouteAreaService;
    @Resource
    public MonitorRaPointService monitorRaPointService;
    @Resource
    public MonitorAlarmService monitorAlarmService;
    @Resource
    public CommonTargetZoonService commonTargetZoonService;
    @Resource
    public MonitorTripService monitorTripService;
    @Resource
    public MemcachedUtil memcachedUtil;
    @Resource
	private RedisClientTemplate redisClientTemplate;

    // 用于存储静止状态的对象
    private static ConcurrentHashMap<String, LsMonitorVehicleStatusBO> longStayMap = 
            new ConcurrentHashMap<String, LsMonitorVehicleStatusBO>();
 // 用于存储静止状态的对象
    private static ConcurrentHashMap<String, String> longStayAlarmMap = 
            new ConcurrentHashMap<String, String>();

    /**
     * 长时间停留报警判断
     * 
     * @param lsMonitorVehicleGpsBO
     * @return
     */
    public boolean validateLongStayAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {

        // String routeId = lsMonitorVehicleStatusBO.getRouteId();
        // if (routeId != null && !"".equals(routeId)) {
        // if (routeId != null && !"".equals(routeId)) {
        // LsMonitorRouteAreaBO areaBO = monitorRouteAreaService.findMonitorRouteAreaById(routeId);
        // if(areaBO.getRouteAreaType().equalsIgnoreCase(Constant.ROUTEAREA_TYPE_AQQY)){
        // return false;
        // }
        // }
        // }
        
        String stationarySpeed = systemParamsService.findSystemParamsValueByKey(SystemParams.STATIC_SPEED.getName());
        if (stationarySpeed == null || "".equals(stationarySpeed)) {
            logger.error("Have not set STATIONARY_SPEED");
            return false;
        }

        String stayTime = systemParamsService.findSystemParamsValueByKey(SystemParams.STAY_TIME.getName());
        if (stayTime == null || "".equals(stayTime)) {
            logger.error("Have not set STAY_TIME alarm determines time");
            return false;
        }

        String speed = lsMonitorVehicleStatusBO.getElockSpeed();
        String key = lsMonitorVehicleStatusBO.getTrackingDeviceNumber()+lsMonitorVehicleStatusBO.getTripId();
        if (speed != null && !"".equals(speed)) {
            // 如果当前车辆速度小于静止速度参数，则判定车辆为静止状态。将当前车辆的数据放到内存里
            if (Double.valueOf(speed) < Double.valueOf(stationarySpeed)) {
                // 先查询是否已经有静止数据
                LsMonitorVehicleStatusBO vehicleGPSBOBefore = longStayMap
                        .get(key);
                String alarmSign = longStayAlarmMap.get(key);
                // 如果没有静止数据，作为第一个静止点插入
                if (vehicleGPSBOBefore == null) {
                    longStayMap.put(key, lsMonitorVehicleStatusBO);
                } else {
                    // 如果有静止点数据，则比较当前点和历史静止点数据
                    long differTime = lsMonitorVehicleStatusBO.getLocationTime().getTime()
                            - vehicleGPSBOBefore.getLocationTime().getTime();
                    // 如果停留时间间隔大于停留超时配置
                    if (differTime > Integer.parseInt(stayTime) * 1000 * 60 && NuctechUtil.isNull(alarmSign)) {
                        // 将缓存更新为最新坐标点,从最新坐标点开始计时
                        //longStayMap.remove(lsMonitorVehicleStatusBO.getTrackingDeviceNumber());
                        longStayAlarmMap.put(key, String.valueOf(differTime));
                        return true;
                    }
                }
            }else{
                longStayMap.remove(key);
                longStayAlarmMap.remove(key);
            }
        }

        return false;
    }

    /**
     * 路线偏移报警
     * 
     * @param monitorVehicleGpsBO
     * @param lsMonitorVehicleStatusBO
     * @return
     * @throws Exception
     */
    public boolean validatePlannedRouteOffsetAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) throws Exception {

        // String routeId = lsMonitorVehicleStatusBO.getRouteId();
        // if (routeId != null && !"".equals(routeId)) {
        // if (routeId != null && !"".equals(routeId)) {
        // LsMonitorRouteAreaBO areaBO = monitorRouteAreaService.findMonitorRouteAreaById(routeId);
        // if(areaBO.getRouteAreaType().equalsIgnoreCase(Constant.ROUTEAREA_TYPE_AQQY)){
        // return false;
        // }
        // }
        // }
        if (lsMonitorVehicleStatusBO.getLongitude() != null && lsMonitorVehicleStatusBO.getLatitude() != null
                && lsMonitorVehicleStatusBO.getRouteId() != null) {
            // 查找当前规划路线
            LsMonitorRouteAreaBO lsMonitorRouteAreaBO = monitorRouteAreaService
                    .findMonitorRouteAreaById(lsMonitorVehicleStatusBO.getRouteId());
            // 查找当前规划路线的点集合
            List<LsMonitorRaPointBO> monitorRaPointBOs = monitorRaPointService
                    .findAllMonitorRaPointByRouteAreaId(lsMonitorVehicleStatusBO.getRouteId());
            GisPoint point = new GisPoint(Double.valueOf(lsMonitorVehicleStatusBO.getLongitude()),
                    Double.valueOf(lsMonitorVehicleStatusBO.getLatitude()));
            boolean isInArea = false;// 是否在预定区域或路线上，默认为不在预定路线上
            boolean isInit = true; // 上面的值是否为初始值，默认为是初始值
            List<GisPoint> pointList = new ArrayList<GisPoint>();
            for (LsMonitorRaPointBO rapt : monitorRaPointBOs) {
                if (rapt.getLongitude() != null && rapt.getLatitude() != null) {
                    pointList
                            .add(new GisPoint(Double.valueOf(rapt.getLongitude()), Double.valueOf(rapt.getLatitude())));
                }
            }
            if (pointList.size() <= 0) {
                return false;
            }

            if (RouteAreaType.MONITOR_AREA.getText().equals(lsMonitorRouteAreaBO.getRouteAreaType())) {// 区域
                isInit = false;
                if (GisUtil.isPointInPolygon(point, pointList)) {
                    isInArea = true;
                }
            } else if (RouteAreaType.LINE.getText().equals(lsMonitorRouteAreaBO.getRouteAreaType())) { // 线
                isInit = false;
                if (GisUtil.isPointOnPolyline(point, pointList,
                        Double.valueOf(lsMonitorRouteAreaBO.getRouteAreaBuffer()))) {
                    isInArea = true;
                }
            } else if (RouteAreaType.POINT.getText().equals(lsMonitorRouteAreaBO.getRouteAreaType())) {// 点
                isInit = false;
                if (GisUtil.isPointInCircle(point, pointList.get(0),
                        Double.valueOf(lsMonitorRouteAreaBO.getRouteAreaBuffer()))) {
                    isInArea = true;
                }
            }

            if (isInArea == false && isInit == false) {
                return true;
            } else if (isInArea == true && isInit == false) {
                return false;
            }
        }

        return false;
    }

    //
    /**
     * 卫星信号丢失报警
     * 
     * 
     * @param monitorVehicleGpsBO
     * @return
     */
    public boolean validateSatelliteLostAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO,LsMonitorVehicleStatusBO beforeLsMonitorVehicleStatusBO) {

        String locationStatus = lsMonitorVehicleStatusBO.getLocationStatus();
        /*
         * 定位状态 “state”:”01110”, //此字段代表状态位 BYTE 0 0:ACC关； 1:ACC开 BYTE 1 0:未定位 1:定位 BYTE 2 0:北纬 1:南纬
         * BYTE 3 0:东经 1:西经 BYTE 4 0:运营状态 1:停运状态
         */
        //上一個點定位状态
        String beforeLocationStatus = beforeLsMonitorVehicleStatusBO.getLocationStatus();
        if (NuctechUtil.isNotNull(locationStatus)&&NuctechUtil.isNotNull(beforeLocationStatus)) {
            
            String location = locationStatus.substring(1, 2);
            String beforeLocation = beforeLocationStatus.substring(1, 2);
            if (LocationStatus.LOCATION_OFF.getValue().equals(location)&&LocationStatus.LOCATION_ON.getValue().equals(beforeLocation)) {
                return true;
            }
        }

        return false;
    }

    /**
     * GSM信号丢失
     * 
     * @param monitorVehicleGpsBO
     * @return
     */
    public boolean validateGSMLostAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {
        return false;
    }

    /**
     * 开锁报警
     * 
     * @param monitorVehicleGpsBO
     * @return
     */
    public boolean validateOpenLockAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO,LsMonitorVehicleStatusBO beforeLsMonitorVehicleStatusBO) {
        
        // 施解封状态
        String elockStatus = lsMonitorVehicleStatusBO.getElockStatus();
        // 锁杆开关状态
        String poleStatus = lsMonitorVehicleStatusBO.getPoleStatus();
     // 上一个点施解封状态
        String beforeElockStatus = beforeLsMonitorVehicleStatusBO.getElockStatus();
        //上一个点锁杆开关状态
        String beforePoleStatus = beforeLsMonitorVehicleStatusBO.getPoleStatus();
        // 在施封状态下被打开
        if (DeviceWorkStatus.DEVICE_ELOCKSTATUS_SEAL.getText().equals(elockStatus)
                && DeviceWorkStatus.DEVICE_POLESTATUS_OPENED.getText().equals(poleStatus)
                && DeviceWorkStatus.DEVICE_ELOCKSTATUS_SEAL.getText().equals(beforeElockStatus)
                && DeviceWorkStatus.DEVICE_POLESTATUS_CLOSED.getText().equals(beforePoleStatus)) {
                return true;
        }
        return false;
    }

    /**
     * 开锁又关闭报警
     * 
     * @param monitorVehicleGpsBO
     * @param lsMonitorVehicleStatusBO
     * @return
     */
    public boolean validateReclosedAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO,
            LsMonitorVehicleStatusBO beforeLsMonitorVehicleStatusBO) {

        // 施解封状态
        String elockStatus = lsMonitorVehicleStatusBO.getElockStatus();
        // 锁杆开关状态
        String poleStatus = lsMonitorVehicleStatusBO.getPoleStatus();

        if (lsMonitorVehicleStatusBO != null) {
            String beforeElockStatus = beforeLsMonitorVehicleStatusBO.getElockStatus();
            String befoerPoleStatus = beforeLsMonitorVehicleStatusBO.getPoleStatus();
            // 在施封状态下被关闭，且前一个状态锁杆是打开的状态
            if (DeviceWorkStatus.DEVICE_ELOCKSTATUS_SEAL.getText().equals(elockStatus)) {
                if (DeviceWorkStatus.DEVICE_POLESTATUS_CLOSED.getText().equals(poleStatus)
                        && DeviceWorkStatus.DEVICE_ELOCKSTATUS_SEAL.getText().equals(beforeElockStatus)
                        && DeviceWorkStatus.DEVICE_POLESTATUS_OPENED.getText().equals(befoerPoleStatus)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 反向行驶报警
     * 
     * @param monitorVehicleGpsBO
     * @return
     * @throws Exception
     */
    public boolean validateOppositeRouteAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) throws Exception {

        if (lsMonitorVehicleStatusBO.getLongitude() != null && lsMonitorVehicleStatusBO.getLatitude() != null
                && lsMonitorVehicleStatusBO.getRouteId() != null) {
            // 查找当前规划路线
            LsMonitorRouteAreaBO lsMonitorRouteAreaBO = monitorRouteAreaService
                    .findMonitorRouteAreaById(lsMonitorVehicleStatusBO.getRouteId());
            // 查找当前规划路线的点集合
            List<LsMonitorRaPointBO> monitorRaPointBOs = monitorRaPointService
                    .findAllMonitorRaPointByRouteAreaId(lsMonitorVehicleStatusBO.getRouteId());
            GisPoint point = new GisPoint(Double.valueOf(lsMonitorVehicleStatusBO.getLongitude()),
                    Double.valueOf(lsMonitorVehicleStatusBO.getLatitude()));

        }

        return false;
    }

    /**
     * 进入危险区域报警
     * 
     * @param monitorVehicleGpsBO
     * @return
     * @throws Exception
     */
    public boolean validateEnterDangerousAreaAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) throws Exception {

        if (lsMonitorVehicleStatusBO != null) {
            // 获取规划路线
            String routeId = lsMonitorVehicleStatusBO.getRouteId();
            if (routeId != null && !"".equals(routeId)) {
                List<LsMonitorRaPointBO> points = monitorRaPointService.findAllMonitorRaPointByRouteAreaId(routeId);
                if (points != null && points.size() > 0) {
                    List<GisPoint> gisPoints = new ArrayList<GisPoint>();
                    for (LsMonitorRaPointBO pointBO : points) {
                        gisPoints.add(new GisPoint(Double.valueOf(pointBO.getLatitude()),
                                Double.valueOf(pointBO.getLongitude())));
                    }
                    boolean validate = GisUtil
                            .isPointInPolygon(new GisPoint(Double.valueOf(lsMonitorVehicleStatusBO.getLatitude()),
                                    Double.valueOf(lsMonitorVehicleStatusBO.getLongitude())), gisPoints);

                    return validate;
                }
            }
        }
        return false;
    }

    /**
     * 强拆
     * 
     * @param monitorVehicleGpsBO
     *        当前状态
     * @param beforeLsMonitorVehicleStatusBO
     *        上一次状态
     * @return
     */
    public boolean validateDeviceBrokenAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO,
            LsMonitorVehicleStatusBO beforeLsMonitorVehicleStatusBO) {

        if (beforeLsMonitorVehicleStatusBO != null && lsMonitorVehicleStatusBO != null) {
            if (DeviceWorkStatus.DEVICE_BROKENSTATUS_NORMAL.getText()
                    .equals(beforeLsMonitorVehicleStatusBO.getBrokenStatus())
                    && DeviceWorkStatus.DEVICE_BROKENSTATUS_BROKEN.getText()
                            .equals(lsMonitorVehicleStatusBO.getBrokenStatus())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 低电量报警
     * 
     * @param monitorVehicleGpsBO
     * @return
     */
    public boolean validateLowBatteryAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {
        String lowBattery = systemParamsService.findSystemParamsValueByKey(SystemParams.LOW_BATTERY.getName());

        if (lowBattery != null && !"".equals(lowBattery)) {
            int percent = DeviceUtils.getElockBatteryPercentageOfHHD(lsMonitorVehicleStatusBO.getElectricityValue());
            if (percent < Integer.valueOf(lowBattery)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 行程超时报警
     * 
     * @param monitorVehicleGpsBO
     * @return
     */
    public boolean validateExceedingTripAllowedTimeAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {
        // 行程超时不用重复报警
        List<LsMonitorAlarmBO> alarmBOs = monitorAlarmService.findExceedTimeAlarm(lsMonitorVehicleStatusBO.getTripId(),
                lsMonitorVehicleStatusBO.getVehicleId());
        if (alarmBOs != null && alarmBOs.size() > 0) {
            return false;
        }
        String routeid = lsMonitorVehicleStatusBO.getRouteId();
        if (routeid != null && !"".equals(routeid)) {
            LsMonitorRouteAreaBO routeAreaBO = monitorRouteAreaService.findMonitorRouteAreaById(routeid);
            if (routeAreaBO != null) {
                String routeCost = routeAreaBO.getRouteCost();
                if (routeCost != null && !"".equals(routeCost)) {
                    Date checkinTime = lsMonitorVehicleStatusBO.getCheckinTime();
                    Date current = Calendar.getInstance().getTime();
                    long defference = DateUtils.differenceBetweenDate(current, checkinTime);
                    if (defference > (Long.valueOf(routeCost) * 60 * 1000)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * @param arrayString
     * @return
     */
    private String[] convertArrayString2Array(String arrayString) {
        if (arrayString == null || !"".equals(arrayString)) {
            return new String[] {};
        }
        arrayString = arrayString.substring(1, arrayString.length() - 1);
        String[] arrays = arrayString.split(",");

        return arrays;
    }

    /**
     * 子锁失联
     * 
     * @param monitorVehicleGpsBO
     * @return
     */
    public List<String> validateEsealLostAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {
        String relatedDevice = lsMonitorVehicleStatusBO.getRelatedDevice();
        String esealOrder = lsMonitorVehicleStatusBO.getEsealOrder();
        String[] eseals = convertArrayString2Array(esealOrder);

        List<String> alarmSeals = new ArrayList<String>();

        if (relatedDevice != null && !"".equals(relatedDevice)) {
            String[] devices = convertArrayString2Array(relatedDevice);
            if (devices != null && devices.length == 12) {
                for (int i = 6; i < devices.length; i++) {
                    String deviceStatus = devices[i];
                    if (DeviceWorkStatus.SEAL_LOST.equals(deviceStatus)) {
                        alarmSeals.add(eseals[i - 6]);
                    }
                }
            }
        }
        return alarmSeals;
    }

    /**
     * 子锁破坏
     * 
     * @param monitorVehicleGpsBO
     * @return
     */
    public List<String> validateEsealBrokenAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {

        String relatedDevice = lsMonitorVehicleStatusBO.getRelatedDevice();
        String esealOrder = lsMonitorVehicleStatusBO.getEsealOrder();
        String[] eseals = convertArrayString2Array(esealOrder);

        List<String> alarmSeals = new ArrayList<String>();

        if (relatedDevice != null && !"".equals(relatedDevice)) {
            String[] devices = convertArrayString2Array(relatedDevice);
            if (devices != null && devices.length == 12) {
                for (int i = 6; i < devices.length; i++) {
                    String deviceStatus = devices[i];
                    if (DeviceWorkStatus.SEAL_BROKEN.equals(deviceStatus)) {
                        alarmSeals.add(eseals[i - 6]);
                    }
                }
            }
        }
        return alarmSeals;
    }

    /**
     * 传感器失联
     * 
     * @param monitorVehicleGpsBO
     * @return
     */
    public List<String> validateSensorLostAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {
        String relatedDevice = lsMonitorVehicleStatusBO.getRelatedDevice();
        String sensorOrder = lsMonitorVehicleStatusBO.getSensorOrder();
        String[] sensors = convertArrayString2Array(sensorOrder);

        List<String> alarmSensors = new ArrayList<String>();

        if (relatedDevice != null && !"".equals(relatedDevice)) {
            String[] devices = convertArrayString2Array(relatedDevice);
            if (devices != null && devices.length == 12) {
                for (int i = 0; i < devices.length - 6; i++) {
                    String deviceStatus = devices[i];
                    if (DeviceWorkStatus.SENSOR_LOST.equals(deviceStatus)) {
                        alarmSensors.add(sensors[i]);
                    }
                }
            }
        }
        return alarmSensors;
    }

    /**
     * 传感器破坏
     * 
     * @param monitorVehicleGpsBO
     * @return
     */
    public List<String> validateSensorBrokenAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {
        String relatedDevice = lsMonitorVehicleStatusBO.getRelatedDevice();
        String sensorOrder = lsMonitorVehicleStatusBO.getSensorOrder();
        String[] sensors = convertArrayString2Array(sensorOrder);

        List<String> alarmSensors = new ArrayList<String>();

        if (relatedDevice != null && !"".equals(relatedDevice)) {
            String[] devices = convertArrayString2Array(relatedDevice);
            if (devices != null && devices.length == 12) {
                for (int i = 0; i < devices.length - 6; i++) {
                    String deviceStatus = devices[i];
                    if (DeviceWorkStatus.SENSOR_BROKEN.equals(deviceStatus)) {
                        alarmSensors.add(sensors[i]);
                    }
                }
            }
        }
        return alarmSensors;
    }

    /**
     * 车载台报警
     * 
     * @return
     */
    public List<String> validateTrackUnitAlarm() {
        return new ArrayList<String>();
    }

    /**
     * Target zoon报警
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean validateTargetZoonAlarm(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {
        String tripId = lsMonitorVehicleStatusBO.getTripId();
        String gpsTrackingDeviceNumber = lsMonitorVehicleStatusBO.getGpsTrackingDeviceNumber();
        // Target zoon报警不用重复报警
        List<LsMonitorAlarmBO> alarmBOs = monitorAlarmService.findTargetZoonAlarm(lsMonitorVehicleStatusBO.getTripId(),
                lsMonitorVehicleStatusBO.getVehicleId());
        if (alarmBOs != null && alarmBOs.size() > 0) {
            return false;
        }

        LsMonitorTripBO lsMonitorTripBO = monitorTripService.findById(tripId);
        // 寻找检入口岸的所属区域：如果坐标在区域内，再判断其是否处于target zoon内；如果不在，直接返回
        List<LsMonitorRouteAreaBO> monitorRouteAreaBOs = monitorRouteAreaService
                .findJGQYByPort(lsMonitorTripBO.getCheckinPort());
        if (monitorRouteAreaBOs == null || monitorRouteAreaBOs.isEmpty()) {
            return false;
        }
        String targetZoonId = lsMonitorTripBO.getTargetZoonId();
        if (NuctechUtil.isNull(targetZoonId)) {
            return false;
        }

        LsMonitorRouteAreaBO lsMonitorRouteAreaBO = monitorRouteAreaBOs.get(0);
        List<GisPoint> areaPoints = findAreaPoints(lsMonitorRouteAreaBO.getRouteAreaId());
        GisPoint p = new GisPoint(Double.parseDouble(lsMonitorVehicleStatusBO.getLongitude()),
                Double.parseDouble(lsMonitorVehicleStatusBO.getLatitude()));
        // 如果在口岸所属区域内，判断是否经过（处于）target zoon，
        if (GisUtil.isPointInPolygon(p, areaPoints)) {
            // 一个行程可能设定多个target zoon
            String[] routeAreaIds = targetZoonId.split(",");
            for (String routeAreaId : routeAreaIds) {
                String key = gpsTrackingDeviceNumber + "_" + routeAreaId;
                // 若缓存中有此记录，表明车辆已进入过区域内，直接判断下一个
//                Object obj = memcachedUtil.get(key, LsMonitorTargetZoneLogBO.class);
//                if (NuctechUtil.isNotNull(obj)) {
//                    continue;
//                }
                String value = redisClientTemplate.get(key);
                if (NuctechUtil.isNotNull(value)) {
                    continue;
                }
                // 若缓存中没有此记录，则判断当前坐标是否处于当前区域，如果处于区域内，把(key, lsMonitorTargetZoneLogBO)存入缓存
//                Object o = getAreaPoints(routeAreaId);
//                if (NuctechUtil.isNull(o)) {
//                    continue;
//                }
//                List<GisPoint> points = (List<GisPoint>) o;
                List<GisPoint> points = getAreaPoints(routeAreaId);
                if (NuctechUtil.isNull(points)) {
                    continue;
                }
                if (GisUtil.isPointInPolygon(p, points)) {
                    // 新增日志记录，并存入缓存
                    LsMonitorTargetZoneLogBO lsMonitorTargetZoneLogBO = new LsMonitorTargetZoneLogBO();
                    lsMonitorTargetZoneLogBO.setLogId(generatePrimaryKey());
                    lsMonitorTargetZoneLogBO.setInGpsSeq(lsMonitorVehicleStatusBO.getGpsSeq());
                    lsMonitorTargetZoneLogBO.setInAreaTime(lsMonitorVehicleStatusBO.getLocationTime());
                    lsMonitorTargetZoneLogBO.setRouteAreaId(routeAreaId);
                    lsMonitorTargetZoneLogBO.setTripId(tripId);
                    lsMonitorTargetZoneLogBO.setTrackingDeviceNumber(gpsTrackingDeviceNumber);
                    commonTargetZoonService.persist(lsMonitorTargetZoneLogBO);
                    //memcachedUtil.add(key, lsMonitorTargetZoneLogBO);
                    JSONObject json = JSONObject.fromObject(lsMonitorTargetZoneLogBO);
                    redisClientTemplate.set(key, json.toString());
                } else {
                    // 更新日志记录，并从缓存中删除
                    LsMonitorTargetZoneLogBO lsMonitorTargetZoneLogBO = commonTargetZoonService.findByProperties(tripId,
                            gpsTrackingDeviceNumber, routeAreaId);
                    updateMonitorTargetZoneLog(lsMonitorVehicleStatusBO, lsMonitorTargetZoneLogBO);
                    //memcachedUtil.delete(key);
                    redisClientTemplate.del(key);
                }
            }
        } else {
            // 如果不在口岸所属区域，判断如果之前经过过target zoon，则更新离开时间
            String[] routeAreaIds = targetZoonId.split(",");
            for (String routeAreaId : routeAreaIds) {
                // 获取每个target zoon的日志记录，更新离开时间
                String key = gpsTrackingDeviceNumber + "_" + routeAreaId;
                // 若缓存中无此记录，表明车辆已进入过区域内并已离开，或者未进入过
                //Object obj = memcachedUtil.get(key, LsMonitorTargetZoneLogBO.class);
                //if (NuctechUtil.isNull(obj)) {
            	String value = redisClientTemplate.get(key);
                if (NuctechUtil.isNull(value)) {
                    // 能从数据库查到，说明进入过并已离开
                    LsMonitorTargetZoneLogBO lsMonitorTargetZoneLogBO = commonTargetZoonService.findByProperties(tripId,
                            gpsTrackingDeviceNumber, routeAreaId);
                    if (NuctechUtil.isNotNull(lsMonitorTargetZoneLogBO)) {
                        continue;
                    } else {
                        return true;
                    }
                } else {
                    // 若缓存中有此记录，则判断当前坐标是否处于当前区域，如果不处于区域内，更新此时间为离开时间，并从缓存删除记录
//                    Object o = getAreaPoints(routeAreaId);
//                    if (NuctechUtil.isNull(o)) {
//                        continue;
//                    }
//                    List<GisPoint> points = (List<GisPoint>) o;
                	List<GisPoint> points = getAreaPoints(routeAreaId);
                    if (NuctechUtil.isNull(points)) {
                        continue;
                    }
                    if (!GisUtil.isPointInPolygon(p, points)) {
                        // 更新日志记录，并从缓存中删除
                        LsMonitorTargetZoneLogBO lsMonitorTargetZoneLogBO = commonTargetZoonService
                                .findByProperties(tripId, gpsTrackingDeviceNumber, routeAreaId);
                        updateMonitorTargetZoneLog(lsMonitorVehicleStatusBO, lsMonitorTargetZoneLogBO);
                        //memcachedUtil.delete(key);
                        redisClientTemplate.del(key);
                    }
                }
            }
        }
        return false;
    }

    /**
     * 更新target zoon日志信息
     * 
     * @param lsMonitorVehicleStatusBO
     * @param lsMonitorTargetZoneLogBO
     */
    private void updateMonitorTargetZoneLog(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO,
            LsMonitorTargetZoneLogBO lsMonitorTargetZoneLogBO) {
        lsMonitorTargetZoneLogBO.setOutAreaTime(lsMonitorVehicleStatusBO.getLocationTime());
        lsMonitorTargetZoneLogBO.setOutGpsSeq(lsMonitorVehicleStatusBO.getGpsSeq());
        commonTargetZoonService.update(lsMonitorTargetZoneLogBO);
    }

    /**
     * 根据区域Id，获取对应的坐标集合。若缓存没有，从数据库取，并存入缓存
     * 
     * @param routeAreaId
     * @return
     */
//    private Object getAreaPoints(String routeAreaId) {
//        Object o = memcachedUtil.get(routeAreaId, List.class);
//        if (NuctechUtil.isNotNull(o)) {
//            return o;
//        }
//        List<GisPoint> points = findAreaPoints(routeAreaId);
//        memcachedUtil.add(routeAreaId, points);
//        return points;
//    }
    private List<GisPoint> getAreaPoints(String routeAreaId) {
        String value = redisClientTemplate.get(routeAreaId);
        if (NuctechUtil.isNotNull(value)) {
            return toPointList(value);
        }
        List<GisPoint> points = findAreaPoints(routeAreaId);
        //memcachedUtil.add(routeAreaId, points);
        saveToCache(routeAreaId, points);
        return points;
    }

	private void saveToCache(String routeAreaId, List<GisPoint> points) {
		JSONArray json = new JSONArray();
        json.addAll(points);
        redisClientTemplate.set(routeAreaId, json.toString());
	}

    /**
     * GisPoint数组json串转为对应数组
     * @param value
     * @return
     */
	private List<GisPoint> toPointList(String value) {
		JSONArray json = JSONArray.fromObject(value);
		List<GisPoint> list = new ArrayList<>();
		for (int i = 0; i < json.size(); i++) {
			JSONObject jsonObject = json.getJSONObject(i);
			list.add(new GisPoint(jsonObject.getDouble("lng"), jsonObject.getDouble("lat")));
		}
		return list;
	}

	/**
     * 根据区域Id，从数据库读取对应的坐标集合
     * 
     * @param routeAreaId
     * @return
     */
    private List<GisPoint> findAreaPoints(String routeAreaId) {
        List<GisPoint> points = null;
        try {
            List<LsMonitorRaPointBO> monitorRaPointBOs = monitorRaPointService
                    .findAllMonitorRaPointByRouteAreaId(routeAreaId);
            points = monitorRaPointService.getAreaPoints(monitorRaPointBOs);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return points;
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
    
}
