package com.nuctech.ls.center.map.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.center.utils.BeansToJson;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRaPointBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.ls.model.redis.RedisClientTemplate;
import com.nuctech.ls.model.service.CommonVehicleService;
import com.nuctech.ls.model.service.MonitorAlarmService;
import com.nuctech.ls.model.service.MonitorRaPointService;
import com.nuctech.ls.model.service.MonitorTripGpsService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.MonitorVehicleGpsService;
import com.nuctech.ls.model.vo.monitor.ViewAlarmReportVO;
import com.nuctech.util.Constant;
import com.nuctech.util.JSONUtil;
import com.nuctech.util.NuctechUtil;
import com.nuctech.util.RedisKey;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@ParentPackage("json-default")
@Namespace("/monitorvehicle")
public class MonitorVehicleGpsAction extends LSBaseAction {
	protected static final String DEFAULT_SORT_COLUMNS = "t.locationTime DESC";
	private static final String PORTAL_TABLE_PREFIX = "LS_MONITOR_PORTAL_GPS_";
	private static final String VEHICLE_TABLE_PREFIX = "LS_MONITOR_VEHICLE_GPS_";

	private static final long serialVersionUID = 1L;
	@Resource
	private MonitorTripService monitorTripService;
	@Resource
	private MonitorTripGpsService monitorVehicleTripGpsService;
	@Resource
	private MonitorAlarmService monitorAlarmService;
	@Resource
	private MonitorRaPointService monitorRaPointService;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	private List<LsMonitorTripBO> lsMonitorTripBOs;// 车辆行程列表
	private LsMonitorTripBO lsMonitorTripBO;// 车辆行程
	private List<LsMonitorVehicleGpsBO> lsMonitorVehicleGpsBOs;// 某辆车轨迹表
	private List<LsMonitorAlarmBO> lsMonitorAlarmBOs;// 某辆车轨所有报警
	private List<ViewAlarmReportVO> lsMonitorAlarmVOs;// 某辆车轨所有报警
	private List<LsMonitorRaPointBO> lsMonitorRaPointBOs;// 某辆车轨迹表
	private String message;
	private boolean success;// 结果
	@Resource
	private CommonVehicleService commonVehicleService;
	@Resource
	private MonitorVehicleGpsService monitorVehicleGpsService;
	@Resource
	private RedisClientTemplate redisClientTemplate;

	/**
	 * 获取车辆信息并分级显示
	 */
	@Action(value = "findAllMonitorTrips", results = { @Result(name = "success", type = "json") })
	public void findAllMonitorTrips() throws Exception {
		this.lsMonitorTripBOs = this.monitorTripService.findAllTrips();
		String result = new BeansToJson<LsMonitorTripBO, Serializable>().beanToJson(this.lsMonitorTripBOs);
		logger.info(String.format("查询所有车辆并分级显示"));
		try {
			this.response.getWriter().println(result);
		} catch (IOException e) {
			message = e.getMessage();
			logger.error(message);
		}
	}

	@Action(value = "addModal", results = { @Result(name = "success", location = "/monitor/monitorRaAdd.jsp"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String addModal() {
		lsMonitorTripBO = new LsMonitorTripBO();
		return SUCCESS;
	}

	/**
     * 获取车辆信息并分级显示
     */
    @Action(value = "findAllMonitorVehicleGpsByEclockNum_bak")
    public String findAllMonitorVehicleGpsByEclockNum_bak() throws Exception {
        JSONObject json = new JSONObject();
        try {
            LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService.findById(vehicleId);
            if (NuctechUtil.isNotNull(lsCommonVehicleBO)) {
                this.lsMonitorTripBO = this.monitorTripService.findById(lsCommonVehicleBO.getTripId());
                this.lsMonitorVehicleGpsBOs = this.monitorVehicleTripGpsService
                        .findLsMonitorVehicleGpsByEclockNum(trackingDeviceNumber, lsMonitorTripBO);
                json.put("lsMonitorTripBO", lsMonitorTripBO);
                json.put("lsMonitorVehicleGpsBOs", lsMonitorVehicleGpsBOs);
                if (lsMonitorVehicleGpsBOs != null) {
//                  this.lsMonitorRaPointBOs = this.monitorRaPointService.findAllMonitorRaPointByRouteAreaId(this.lsMonitorTripBO.getRouteId());
//                  this.lsMonitorAlarmVOs = this.monitorAlarmService.findAlarmVOsByTripIdAndStatus(this.lsMonitorTripBO.getTripId());
//                  json.put("lsMonitorRaPointBOs", lsMonitorRaPointBOs);
//                  json.put("lsMonitorAlarmVOs", lsMonitorAlarmVOs);
                    json.put("success", true);
                } else {
                    json.put("success", false);
                }
            } else {
                logger.error("Vehicle does not exist! vehicleId=" + vehicleId);
            }

            logger.info(String.format(
                    "查询某个车辆运行轨迹:参数为：" + vehicleId + "," + trackingDeviceNumber + "," + lsMonitorTripBO.getTripId() + ","
                            + lsMonitorTripBO.getCheckinTime() + "," + lsMonitorTripBO.getCheckoutTime()));
        } catch (Exception e) {
            message = e.getMessage();
            logger.error(message);
            json.put("success", false);
            json.put("message", message);
        } finally {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print(json.toString());
            out.flush();
            out.close();
        }
        return null;
    }
    
    /**
     * 获取车辆信息并分级显示
     * @return
     * @throws Exception
     */
    @Action(value = "findAllMonitorVehicleGpsByEclockNum")
    public String findAllMonitorVehicleGpsByEclockNum() throws Exception {
        JSONObject json = new JSONObject();
        try {
            LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService.findById(vehicleId);
            if (NuctechUtil.isNotNull(lsCommonVehicleBO)) {
                this.lsMonitorTripBO = this.monitorTripService.findById(lsCommonVehicleBO.getTripId());
                
                json.put("success", false);
                //先从缓存取，缓存没有，从数据库查，并存入缓存
                //key: CNNTxxxxx:xxxxxxxxx
                String key = trackingDeviceNumber + ":" + lsMonitorTripBO.getTripId();
                List<String> resultList = redisClientTemplate.lrange(key, 0, Constant.MAX_PAGE_SIZE);
                JSONArray jsonArray = new JSONArray();
            	if(NuctechUtil.isNull(resultList) || resultList.isEmpty()) {
            		jsonArray = this.monitorVehicleTripGpsService
                    		.findGpsColumnsByEclockNum(trackingDeviceNumber, lsMonitorTripBO);
            		redisClientTemplate.lpush(key, JSONUtil.toArray(jsonArray, true));
//            		resultList = JSONUtil.toStringList(jsonArray);
            		json.put("lsMonitorVehicleGpsBOs", jsonArray);
            		json.put("success", true);
            	} else {
            		json.put("success", true);
            		json.put("lsMonitorVehicleGpsBOs", resultList);
            	}
            	//resultList若不为空，直接传递string数组让前台解析为对象
                
                json.put("lsMonitorTripBO", lsMonitorTripBO);
                /*
                json.put("lsMonitorVehicleGpsBOs", resultList);
               	if (jsonArray != null) {
                    json.put("success", true);
                } else {
                }
                */
            } else {
                logger.error("Vehicle does not exist! vehicleId=" + vehicleId);
            }

            logger.info(String.format(
                    "查询某个车辆运行轨迹:参数为：" + vehicleId + "," + trackingDeviceNumber + "," + lsMonitorTripBO.getTripId() + ","
                            + lsMonitorTripBO.getCheckinTime() + "," + lsMonitorTripBO.getCheckoutTime()));
        } catch (Exception e) {
            message = e.getMessage();
            logger.error(message);
            json.put("success", false);
            json.put("message", message);
        } finally {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print(json.toString());
            out.flush();
            out.close();
        }
        return null;
    }

	/**
     * 获取车辆信息并分级显示
     */
    @Action(value = "findOneVehicleGpsPlanRoute")
    public String findOneVehicleGpsPlanRoute() throws Exception {
        JSONObject json = new JSONObject();
        try {
            LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService.findById(vehicleId);
            if (NuctechUtil.isNotNull(lsCommonVehicleBO)) {
                this.lsMonitorTripBO = this.monitorTripService.findById(lsCommonVehicleBO.getTripId());
                if (lsMonitorTripBO != null) {
                    this.lsMonitorRaPointBOs = this.monitorRaPointService.findAllMonitorRaPointByRouteAreaId(this.lsMonitorTripBO.getRouteId());
                    json.put("lsMonitorRaPointBOs", lsMonitorRaPointBOs);
                    json.put("success", true);
                } else {
                    json.put("success", false);
                }
            } else {
                logger.error("Vehicle does not exist! vehicleId=" + vehicleId);
            }

            logger.info(String.format(
                    "查询某个车辆运行轨迹:参数为：" + vehicleId + "," + trackingDeviceNumber + "," + lsMonitorTripBO.getTripId() + ","
                            + lsMonitorTripBO.getCheckinTime() + "," + lsMonitorTripBO.getCheckoutTime()));
        } catch (Exception e) {
            message = e.getMessage();
            logger.error(message);
            json.put("success", false);
            json.put("message", message);
        } finally {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print(json.toString());
            out.flush();
            out.close();
        }
        return null;
    }
    
    
    
    /**
     * 获取车辆信息并分级显示
     */
    @Action(value = "findOneVehicleAlarm")
    public String findOneVehicleAlarm() throws Exception {
        JSONObject json = new JSONObject();
        try {
            LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService.findById(vehicleId);
            if (NuctechUtil.isNotNull(lsCommonVehicleBO)) {
            	String tripId = lsCommonVehicleBO.getTripId();
                this.lsMonitorTripBO = this.monitorTripService.findById(tripId);
                if (lsMonitorTripBO != null) {
//                     this.lsMonitorAlarmVOs = this.monitorAlarmService.findAlarmVOsByTripIdAndStatus(this.lsMonitorTripBO.getTripId());
                	
                	//先从缓存取，缓存没有，从数据库查，并存入缓存
                	JSONArray simpleAlarms = new JSONArray();
                	String key = RedisKey.ALARMLIST_TRIPID_PREFIX + tripId;
                	List<String> alarmIdList = redisClientTemplate.lrange(key, 0, Constant.MAX_PAGE_SIZE);
                	if(NuctechUtil.isNull(alarmIdList) || alarmIdList.isEmpty()) {
                		simpleAlarms = this.monitorAlarmService.findSimpleAlarmsByTripId(tripId);
                		alarmIdList = getAlarmIds(simpleAlarms);
                		redisClientTemplate.lpush(key, alarmIdList.toArray(new String[]{}));
                	} else {
                		simpleAlarms = monitorAlarmService.findAllInRange(alarmIdList.toArray(new String[]{}));
                	}
                	json.put("lsMonitorAlarmVOs", simpleAlarms);
//                	json.put("lsMonitorAlarmVOs", lsMonitorAlarmVOs);
                    json.put("success", true);
                } else {
                    json.put("success", false);
                }
            } else {
                logger.error("Vehicle does not exist! vehicleId=" + vehicleId);
            }

            logger.info(String.format(
                    "查询某个车辆运行轨迹:参数为：" + vehicleId + "," + trackingDeviceNumber + "," + lsMonitorTripBO.getTripId() + ","
                            + lsMonitorTripBO.getCheckinTime() + "," + lsMonitorTripBO.getCheckoutTime()));
        } catch (Exception e) {
            message = e.getMessage();
            logger.error(message);
            json.put("success", false);
            json.put("message", message);
        } finally {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print(json.toString());
            out.flush();
            out.close();
        }
        return null;
    }

    /**
     * 获取所有报警记录的alarmId
     * @param lsMonitorAlarmVOs
     * @return
     */
	private List<String> getAlarmIds(JSONArray lsMonitorAlarmVOs) {
		List<String> list = new ArrayList<>(lsMonitorAlarmVOs.size());
		for (int i = 0, len = lsMonitorAlarmVOs.size(); i < len; i++) {
			JSONObject obj = lsMonitorAlarmVOs.getJSONObject(i);
			list.add((String) obj.get("alarmId"));
		}
		return list;
	}

	private String trackingDeviceNumber;

	public String getTrackingDeviceNumber() {
		return trackingDeviceNumber;
	}

	public void setTrackingDeviceNumber(String trackingDeviceNumber) {
		this.trackingDeviceNumber = trackingDeviceNumber;
	}

	private String vehicleId;

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public List<LsMonitorVehicleGpsBO> getLsMonitorVehicleGpsBOs() {
		return lsMonitorVehicleGpsBOs;
	}

	public void setLsMonitorVehicleGpsBOs(List<LsMonitorVehicleGpsBO> lsMonitorVehicleGpsBOs) {
		this.lsMonitorVehicleGpsBOs = lsMonitorVehicleGpsBOs;
	}

	public List<LsMonitorAlarmBO> getLsMonitorAlarmBOs() {
		return lsMonitorAlarmBOs;
	}

	public void setLsMonitorAlarmBOs(List<LsMonitorAlarmBO> lsMonitorAlarmBOs) {
		this.lsMonitorAlarmBOs = lsMonitorAlarmBOs;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<LsMonitorTripBO> getLsMonitorTripBOs() {
		return lsMonitorTripBOs;
	}

	public void setLsMonitorTripBOs(List<LsMonitorTripBO> lsMonitorTripBOs) {
		this.lsMonitorTripBOs = lsMonitorTripBOs;
	}

	public LsMonitorTripBO getLsMonitorTripBO() {
		return lsMonitorTripBO;
	}

	public void setLsMonitorTripBO(LsMonitorTripBO lsMonitorTripBO) {
		this.lsMonitorTripBO = lsMonitorTripBO;
	}

	public List<LsMonitorRaPointBO> getLsMonitorRaPointBOs() {
		return lsMonitorRaPointBOs;
	}

	public void setLsMonitorRaPointBOs(List<LsMonitorRaPointBO> lsMonitorRaPointBOs) {
		this.lsMonitorRaPointBOs = lsMonitorRaPointBOs;
	}

	/**
	 * 获取纬度
	 * 
	 * @return
	 */
	private double getRandomLat() {
		Random r = new Random();
		double d1 = r.nextDouble();
		double d = 40 + d1;
		return d;
	}

	/**
	 * 获取经度
	 * 
	 * @return
	 */
	private double getRandomLng() {
		Random r = new Random();
		double d1 = r.nextDouble();
		double d = 116 + d1;
		return d;
	}

	@Action(value = "toList", results = { @Result(name = "success", location = "/monitor/vehicleGps/list.jsp") })
	public String toList() {
		return SUCCESS;
	}

	/**
	 * 查找设备轨迹表名,模糊匹配
	 * 
	 * @return
	 */
	@Action(value = "findTables")
	public String findTables() {
		String deviceNum = request.getParameter("deviceNum");
		if (NuctechUtil.isNotNull(deviceNum)) {
			List<String> tableNames = monitorVehicleGpsService.findTableNamesByDeviceNum(deviceNum);
			if (tableNames != null && tableNames.size() > 0) {
				JSONArray array = new JSONArray();
				for (String name : tableNames) {
					JSONObject obj = new JSONObject();
					obj.put("deviceNum", getDeviceNum(name));
					array.add(obj);
				}
				try {
					JSONObject json = new JSONObject();
					json.put("value", array);
					this.response.getWriter().println(json.toString());
				} catch (IOException e) {
					logger.error(message);
				}
			}
		}
		return null;
	}

	/**
	 * 从表名获取设备号
	 * 
	 * @param tableName
	 * @return
	 */
	private String getDeviceNum(String tableName) {
		if (tableName.startsWith(PORTAL_TABLE_PREFIX)) {
			return getPortalNum(tableName);
		} else if (tableName.startsWith(VEHICLE_TABLE_PREFIX)) {
			return getElockNum(tableName);
		} else {
			return "";
		}
	}

	/**
	 * 从表名获取车载台号
	 * 
	 * @param tableName
	 * @return
	 */
	private String getPortalNum(String tableName) {
		return tableName.substring(PORTAL_TABLE_PREFIX.length());
	}

	/**
	 * 从表名获取关锁号
	 * 
	 * @param tableName
	 * @return
	 */
	private String getElockNum(String tableName) {
		return tableName.substring(VEHICLE_TABLE_PREFIX.length());
	}

	/**
	 * 查询
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "list")
	public void list() throws IOException {
		String trackingDeviceNumber = request.getParameter("s_trackingDeviceNumber");
		if (NuctechUtil.isNotNull(trackingDeviceNumber)) {
			JSONObject retJson = new JSONObject();
			// 查找对应的车载台表是否存在
			boolean isExist = monitorVehicleGpsService
					.findTableNameExsitorNot(PORTAL_TABLE_PREFIX + trackingDeviceNumber);
			if (isExist) {
				pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
				retJson = monitorVehicleGpsService.findAll(PORTAL_TABLE_PREFIX, pageQuery, null, false);
			} else {
				// 如果车载台表不存在，查找是否是设备表
				isExist = monitorVehicleGpsService.findTableNameExsitorNot(VEHICLE_TABLE_PREFIX + trackingDeviceNumber);
				if (isExist) {
					pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
					retJson = monitorVehicleGpsService.findAll(VEHICLE_TABLE_PREFIX, pageQuery, null, false);
				}
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.print(retJson.toString());
			out.flush();
			out.close();
		}
	}

	public List<ViewAlarmReportVO> getLsMonitorAlarmVOs() {
		return lsMonitorAlarmVOs;
	}

	public void setLsMonitorAlarmVOs(List<ViewAlarmReportVO> lsMonitorAlarmVOs) {
		this.lsMonitorAlarmVOs = lsMonitorAlarmVOs;
	}

}
