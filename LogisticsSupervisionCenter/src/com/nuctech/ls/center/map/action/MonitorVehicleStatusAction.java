package com.nuctech.ls.center.map.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.center.utils.DateJsonValueProcessor;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.memcached.MemcachedUtil;
import com.nuctech.ls.model.redis.RedisClientTemplate;
import com.nuctech.ls.model.service.CommonVehicleService;
import com.nuctech.ls.model.service.MonitorAlarmService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.MonitorVehicleStatusService;
import com.nuctech.ls.model.vo.monitor.VehicleInfoVO;
import com.nuctech.ls.model.vo.monitor.ViewAlarmReportVO;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.JSONUtil;
import com.nuctech.util.NuctechUtil;
import com.nuctech.util.RedisKey;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 
 * @author liqingxian
 *
 */
@ParentPackage("json-default")
@Namespace("/vehiclestatus")
public class MonitorVehicleStatusAction extends LSBaseAction {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 1L;
	private static final String CNNT = "CNNT";
	@Resource
	MonitorVehicleStatusService monitorVehicleStatusService;
	@Resource
	private CommonVehicleService commonVehicleService;
	@Resource
	private MonitorTripService monitorTripService;
	@Resource
	private MonitorAlarmService monitorAlarmService;
	@Resource
	public MemcachedUtil memcachedUtil;
	@Resource
	private RedisClientTemplate redisClientTemplate;

	private String travleStatus;
	private String locationType;
	private String trackingDeviceNumber;
	private LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO;
	private boolean success;// 结果
	private LsCommonVehicleBO lsCommonVehicleBO;
	private LsMonitorTripBO lsMonitorTripBO;
	private String tripId;
	private String vehicleId;
	private String tripStatus;// 车辆流程是否完成
	private String qdPorts;// 所选择检入口岸的集合
	private String zdPorts;// 所选择检出口岸的集合
	private String roleName;
	private String vehicleplatename;
	private String alarmId;// 地图上报警的id
	/**
	 * 列表排序字段
	 */
	private static final String DEFAULT_SORT_COLUMNS = "checkinTime DESC";
	private List<LsMonitorVehicleStatusBO> lsMonitorVehicleStatusBOs;
	private List<VehicleInfoVO> vehicleInfoVOs;
	private VehicleInfoVO vehicleInfoVO;
	private List<ViewAlarmReportVO> alarmList;

	@SuppressWarnings("unchecked")
	@Action(value = "findAllVehicleStatus", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public void findAllVehicleStatus() throws Exception {
		logger.info(String.format("根据行程查询对应车辆!"));
		logger.info("根据车辆完成状态查询车辆列表，目前状态为：" + tripStatus + "，出发口岸是：" + qdPorts + ",到达口岸是：" + zdPorts);
		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
		String organizationId = "";
		if (sessionUser != null) {
			organizationId = sessionUser.getOrganizationId();
		}
		this.vehicleInfoVOs = this.monitorVehicleStatusService.findAllVehicleStatus(this.locationType, organizationId,
				getStrResult(tripStatus), getStrResult(qdPorts), getStrResult(zdPorts), vehicleplatename);
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
		JSONObject resJson = this.monitorVehicleStatusService.vehicleInfoObjectList(this.vehicleInfoVOs,
				new PageList<VehicleInfoVO>(), pageQuery);
		if(NuctechUtil.isNotNull(resJson)){
		    resJson.put("BelongTo", organizationId);
		}
		try {
			this.response.getWriter().println(resJson);
		} catch (IOException e) {
			message = e.getMessage();
			logger.error(message);
		}
	}

	@SuppressWarnings("unchecked")
	@Action(value = "findAllPatrolStatus", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public void findAllPatrolStatus() throws Exception {
		logger.info(String.format("查询巡逻队列表及在地图上显示的车辆!"));
		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
		String organizationId = "";
		if (sessionUser != null) {
			organizationId = sessionUser.getOrganizationId();
		}
		this.vehicleInfoVOs = this.monitorVehicleStatusService.findAllPatrolStatus(this.locationType, organizationId,
				this.roleName, Constant.TRIP_STATUS_STARTED);
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
		JSONObject resJson = this.monitorVehicleStatusService.vehicleInfoObjectList(this.vehicleInfoVOs,
				new PageList<VehicleInfoVO>(), pageQuery);
		try {
			this.response.getWriter().println(resJson);
		} catch (IOException e) {
			message = e.getMessage();
			logger.error(message);
		}
	}

	/**
	 * 查询初始化所有在途车辆
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "findAllOnWayVehicleStatus", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public void findAllOnWayVehicleStatus() throws Exception {
		this.lsMonitorVehicleStatusBOs = this.monitorVehicleStatusService.findAllOnWayVehicleStatus(this.locationType);
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
		Map filtersMap = pageQuery.getFilters();

		JSONObject resJson = this.monitorVehicleStatusService.vehilciStatusObjectList(this.lsMonitorVehicleStatusBOs,
				new PageList<LsMonitorVehicleStatusBO>(), pageQuery);
		logger.info(String.format("查询地图所有车辆初始化状态"));
		try {
			this.response.getWriter().println(resJson);
		} catch (IOException e) {
			message = e.getMessage();
			logger.error(message);
		}
	}

	@Action(value = "findVehicleInfoByAlarm", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String findVehicleInfoByAlarm() throws Exception {
		logger.info(String.format("根据报警查询对应车辆!"));
		try {
			if (this.tripId != null) {
				this.vehicleInfoVO = this.monitorVehicleStatusService.findVehicleStatusByAlarm(this.tripId);
				if (vehicleInfoVO != null) {
					this.success = true;
				} else {
					this.success = false;
				}
			} else {
				this.success = false;
			}
		} catch (Exception e) {
			message = e.getMessage();
			logger.error(message);
			this.success = false;
		}
		return SUCCESS;

	}

	@Action(value = "vehicleOverViewMap", results = { @Result(name = "success", location = "/monitor/overViewMap.jsp"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String vehicleOverViewMap() throws Exception {
		// this.trackingDeviceNumber = trackingDeviceNumber;
		return SUCCESS;
	}

	@Action(value = "queryMonitorVehicleStatus", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String queryMonitorVehicleStatus() {
		this.lsMonitorVehicleStatusBO = monitorVehicleStatusService.findVehicleStatusByNumAndTripId(tripId,
				trackingDeviceNumber);
		if (!this.lsMonitorVehicleStatusBO.equals(null)) {
			this.lsCommonVehicleBO = commonVehicleService.findById(this.lsMonitorVehicleStatusBO.getVehicleId());
		}
		return SUCCESS;
	}

	@Action(value = "findPatrolByNumber", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String findPatrolByNumber() throws Exception {
		logger.info(String.format("查询巡逻队位置信息"));
		try {
			this.lsMonitorVehicleStatusBO = this.monitorVehicleStatusService
					.findLatestPatrolByNumber(this.trackingDeviceNumber);
			if (lsMonitorVehicleStatusBO != null) {
				this.success = true;
			} else {
				this.success = false;
			}
		} catch (Exception e) {
			message = e.getMessage();
			logger.error(message);
			this.success = false;
		}
		return SUCCESS;
	}

	@Action(value = "findTripInfoByStatusId", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String findTripInfoByStatusId() throws Exception {
		logger.info(String.format("查询车辆相关信息"));
		try {
			this.lsCommonVehicleBO = this.commonVehicleService.findById(this.vehicleId);
			this.lsMonitorTripBO = this.monitorTripService.findById(this.tripId);
			this.success = true;
		} catch (Exception e) {
			message = e.getMessage();
			logger.error(message);
			this.success = false;
		}
		return SUCCESS;
	}

	@Action(value = "findAllEndTripPatrol", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public void findAllEndTripPatrol() throws Exception {
		logger.info(String.format("查询行程已结束的巡逻队"));
		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
		String organizationId = "";
		if (sessionUser != null) {
			organizationId = sessionUser.getOrganizationId();
		}
		this.vehicleInfoVOs = this.monitorVehicleStatusService.findAllPatrolStatus(this.locationType, organizationId,
				this.roleName, Constant.TRIP_STATUS_FINISHED);
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
		JSONObject resJson = this.monitorVehicleStatusService.vehicleInfoObjectList(this.vehicleInfoVOs,
				new PageList<VehicleInfoVO>(), pageQuery);
		try {
			this.response.getWriter().println(resJson);
		} catch (IOException e) {
			message = e.getMessage();
			logger.error(message);
		}
	}

	/**
	 * 根设备号或车牌号 关锁号查询车辆
	 */
	@Action(value = "findVehicleBytrackOrPlateNum", results = { @Result(name = "success", type = "json") })
	public String findVehicleBytrackOrPlateNum() {
		if (!"".equalsIgnoreCase(searchNumber)) {
			this.vehicleInfoVOs = this.monitorVehicleStatusService.findVehicleInfoBySearchNum(searchNumber);
			return SUCCESS;
		}
		return ERROR;
	}

	/**
	 * 在途车辆根据车辆状态查询显示车辆列表
	 */
	@Action(value = "findOnWayVehicleByTravelStatus", results = { @Result(name = "success", type = "json") })
	public String findOnWayVehicleByTravelStatus() {
		if (!"".equalsIgnoreCase(travleStatus)) {
			this.vehicleInfoVOs = this.monitorVehicleStatusService.findOnWayVehicleByTravelStatus(travleStatus);
			return SUCCESS;
		}
		return ERROR;
	}

	@Action(value = "findDeviceNumber", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String findDeviceNumber() throws Exception {
		logger.info(String.format("查询巡逻队位置信息"));
		try {
//			this.lsMonitorVehicleStatusBO = (LsMonitorVehicleStatusBO) memcachedUtil
//					.get("trip_" + this.trackingDeviceNumber, LsMonitorVehicleStatusBO.class);
			String value = redisClientTemplate.get("trip_" + this.trackingDeviceNumber);
			if(NuctechUtil.isNotNull(value)) {
				this.lsMonitorVehicleStatusBO = (LsMonitorVehicleStatusBO) JSONObject.toBean(JSONObject.fromObject(value),
						LsMonitorVehicleStatusBO.class);
			}
			// this.lsMonitorVehicleStatusBO =
			// this.monitorVehicleStatusService.findDeviceNumber(this.trackingDeviceNumber);
			if (lsMonitorVehicleStatusBO != null) {
				this.success = true;
			} else {
				//如找不到，可能是航宏达的锁，转换格式后再找一次,CNNT->00
				if(trackingDeviceNumber.startsWith(CNNT)) {
					String deviceNumber = trackingDeviceNumber.replace(CNNT, "00");
//					lsMonitorVehicleStatusBO = (LsMonitorVehicleStatusBO) memcachedUtil
//							.get("trip_" + deviceNumber, LsMonitorVehicleStatusBO.class);
					String value1 = redisClientTemplate.get("trip_" + deviceNumber);
					if(NuctechUtil.isNotNull(value1)) {
						this.lsMonitorVehicleStatusBO = (LsMonitorVehicleStatusBO) JSONObject.toBean(JSONObject.fromObject(value1),
								LsMonitorVehicleStatusBO.class);
					}
					if (lsMonitorVehicleStatusBO != null) {
						this.success = true;
					} else {
						this.success = false;
					}
				} else {
					this.success = false;
				}
			}
		} catch (Exception e) {
			message = e.getMessage();
			logger.error(message);
			this.success = false;
		}
		return SUCCESS;
	}

	/**
	 * 列表查询
	 * 
	 * @throws IOException
	 */
	@Action(value = "alarmlist")
	public String alarmlist() throws IOException {
		logger.info(String.format("查询在途车辆报警列表!"));
		JSONObject json = new JSONObject();
		try {
			this.alarmList = monitorAlarmService.findAlarmList();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); // 排除关联死循环
			jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONArray lineitemArray = JSONArray.fromObject(this.alarmList, jsonConfig);
			String result = JSONArray.fromObject(lineitemArray).toString();
			if (alarmList != null) {
				json.put("success", true);
				json.put("alarmList", result);
			} else {
				json.put("success", false);
			}
		} catch (Exception e) {
			message = e.getMessage();
			logger.error(message);
			json.put("success", false);
		}
		response.setCharacterEncoding("utf-8");
		// 禁止浏览器开辟缓存
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma", "no-cache");
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}

	// 用于输入检索的信息
	private String searchNumber;

	public String getSearchNumber() {
		return searchNumber;
	}

	public void setSearchNumber(String searchNumber) {
		this.searchNumber = searchNumber;
	}

	/**
	 * 对接收到的字符串进行处理
	 * 
	 * @return
	 */
	public String getStrResult(String str) {
		StringBuffer sb = new StringBuffer();
		if (NuctechUtil.isNotNull(str)) {
			if (str.indexOf(",") > 0) {
				String[] strs = str.split(",");
				for (int i = 0; i < strs.length - 1; i++) {
					sb.append("'" + strs[i] + "',");
				}
				sb.append("'" + strs[strs.length - 1] + "'");
			} else {
				sb.append("'" + str + "'");
			}
		}
		return sb.toString();
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getTravleStatus() {
		return travleStatus;
	}

	public void setTravleStatus(String travleStatus) {
		this.travleStatus = travleStatus;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public LsMonitorVehicleStatusBO getLsMonitorVehicleStatusBO() {
		return lsMonitorVehicleStatusBO;
	}

	public void setLsMonitorVehicleStatusBO(LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO) {
		this.lsMonitorVehicleStatusBO = lsMonitorVehicleStatusBO;
	}

	public String getTrackingDeviceNumber() {
		return trackingDeviceNumber;
	}

	public void setTrackingDeviceNumber(String trackingDeviceNumber) {
		this.trackingDeviceNumber = trackingDeviceNumber;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public LsCommonVehicleBO getLsCommonVehicleBO() {
		return lsCommonVehicleBO;
	}

	public void setLsCommonVehicleBO(LsCommonVehicleBO lsCommonVehicleBO) {
		this.lsCommonVehicleBO = lsCommonVehicleBO;
	}

	public LsMonitorTripBO getLsMonitorTripBO() {
		return lsMonitorTripBO;
	}

	public void setLsMonitorTripBO(LsMonitorTripBO lsMonitorTripBO) {
		this.lsMonitorTripBO = lsMonitorTripBO;
	}

	public List<LsMonitorVehicleStatusBO> getLsMonitorVehicleStatusBOs() {
		return lsMonitorVehicleStatusBOs;
	}

	public void setLsMonitorVehicleStatusBOs(List<LsMonitorVehicleStatusBO> lsMonitorVehicleStatusBOs) {
		this.lsMonitorVehicleStatusBOs = lsMonitorVehicleStatusBOs;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public List<VehicleInfoVO> getVehicleInfoVOs() {
		return vehicleInfoVOs;
	}

	public void setVehicleInfoVOs(List<VehicleInfoVO> vehicleInfoVOs) {
		this.vehicleInfoVOs = vehicleInfoVOs;
	}

	public String getTripStatus() {
		return tripStatus;
	}

	public void setTripStatus(String tripStatus) {
		this.tripStatus = tripStatus;
	}

	public String getQdPorts() {
		return qdPorts;
	}

	public void setQdPorts(String qdPorts) {
		this.qdPorts = qdPorts;
	}

	public String getZdPorts() {
		return zdPorts;
	}

	public void setZdPorts(String zdPorts) {
		this.zdPorts = zdPorts;
	}

	public VehicleInfoVO getVehicleInfoVO() {
		return vehicleInfoVO;
	}

	public void setVehicleInfoVO(VehicleInfoVO vehicleInfoVO) {
		this.vehicleInfoVO = vehicleInfoVO;
	}

	public List<ViewAlarmReportVO> getAlarmList() {
		return alarmList;
	}

	public void setAlarmList(List<ViewAlarmReportVO> alarmList) {
		this.alarmList = alarmList;
	}

	/**
	 * 查询指定车辆的报警信息
	 * 
	 * @throws IOException
	 */
//	@Action(value = "vehicleAlarmlist_bak")
	public String vehicleAlarmlist_bak() throws IOException {
		logger.info(String.format("查询指定的在途车辆报警列表!"));
		JSONObject json = new JSONObject();
		try {
			this.alarmList = monitorAlarmService.findAlarmListV(vehicleId);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); // 排除关联死循环
			jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
			JSONArray lineitemArray = JSONArray.fromObject(this.alarmList, jsonConfig);
			String result = JSONArray.fromObject(lineitemArray).toString();
			if (alarmList != null) {
				json.put("success", true);
				json.put("alarmList", result);
			} else {
				json.put("success", false);
			}
		} catch (Exception e) {
			message = e.getMessage();
			logger.error(message);
			json.put("success", false);
		}
		response.setCharacterEncoding("utf-8");
		// 禁止浏览器开辟缓存
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma", "no-cache");
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}
	
	/**
	 * 查询指定车辆的报警信息
	 * @return
	 * @throws IOException
	 */
	@Action(value = "vehicleAlarmlist")
	public String vehicleAlarmlist() throws IOException {
		logger.info(String.format("查询指定的在途车辆报警列表!"));
		JSONObject json = new JSONObject();
		try {
			json.put("success", false);
			
			//先从缓存取，缓存没有，从数据库查，并存入缓存
            String key = RedisKey.ALARMLIST_VEHICLEID_PREFIX + vehicleId;
            List<String> resultList = redisClientTemplate.lrange(key, 0, Constant.MAX_PAGE_SIZE);
            JSONArray jsonArray;
        	if(NuctechUtil.isNull(resultList) || resultList.isEmpty()) {
        		jsonArray = monitorAlarmService.findSimpleAlarmList(vehicleId);
        		redisClientTemplate.lpush(key, JSONUtil.toArray(jsonArray, true));
        		json.put("success", true);
        		json.put("alarmList", jsonArray);
        	} else {
//        		jsonArray = JSONUtil.toJsonArray(resultList);
        		json.put("success", true);
        		json.put("alarmList", resultList);
        	}
			
		} catch (Exception e) {
			message = e.getMessage();
			logger.error(message);
			json.put("success", false);
		}
		response.setCharacterEncoding("utf-8");
		// 禁止浏览器开辟缓存
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma", "no-cache");
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 查询指定报警的报警信息
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action(value = "appointedAlarm")
	public void appointedAlarm() throws IOException {
		JSONObject json = new JSONObject();
		try {
			this.alarmList = monitorAlarmService.findAlarmListA(alarmId);
			if (alarmList != null) {
				json.put("success", true);
				json.put("alarmList", alarmList);
			} else {
				json.put("success", false);
			}
		} catch (Exception e) {
			message = e.getMessage();
			logger.error(message);
			json.put("success", false);
		}
		response.setCharacterEncoding("utf-8");
		// 禁止浏览器开辟缓存
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma", "no-cache");
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			logger.error(e);
		}
	}

	/**
	 * 模拟5000辆车
	 */
	@Action(value = "addMonitorVehicleStatus", results = { @Result(name = "success", type = "json") })
	public String addMonitorVehicleStatus() {
		for (int i = 0; i < 5000; i++) {
			convertCachedStatusBO(i);
		}
		return SUCCESS;
	}

	private void convertCachedStatusBO(long i) {
		LsMonitorVehicleStatusBO monitorVehicleStatusBO = new LsMonitorVehicleStatusBO();

		monitorVehicleStatusBO.setVehicleStatusId(UUID.randomUUID().toString());
		monitorVehicleStatusBO.setTripId(UUID.randomUUID().toString());
		monitorVehicleStatusBO.setTrackingDeviceNumber("6");
		monitorVehicleStatusBO.setGpsSeq(i);
		monitorVehicleStatusBO.setLocationTime(new Date());
		monitorVehicleStatusBO.setLocationStatus("1");
		monitorVehicleStatusBO.setElockStatus("1");
		monitorVehicleStatusBO.setLocationType("0");
		monitorVehicleStatusBO.setPoleStatus("1");
		monitorVehicleStatusBO.setBrokenStatus("0");
		monitorVehicleStatusBO.setEventUpload("0");
		monitorVehicleStatusBO.setLongitude(String.valueOf(getRandomLng()));
		monitorVehicleStatusBO.setLatitude(String.valueOf(getRandomLat()));
		monitorVehicleStatusBO.setAltitude(String.valueOf(Math.floor(Math.random() * 66)));
		monitorVehicleStatusBO.setElockSpeed(String.valueOf(Math.floor(Math.random() * 66)));
		monitorVehicleStatusBO.setDirection(String.valueOf(Math.floor(Math.random() * 180)));
		monitorVehicleStatusBO.setElectricityValue(String.valueOf(Math.floor(Math.random() * 180)));
		monitorVehicleStatusBO.setCreateTime(new Date());
		monitorVehicleStatusBO.setTripStatus("1");
		monitorVehicleStatusService.saveOrUpdate(monitorVehicleStatusBO);
		// LsCommonVehicleBO lsCommonVehicleBO =
		// commonVehicleService.findCommonVehicleBo(monitorVehicleGpsBO.getTripId(),
		// monitorVehicleGpsBO.getTrackingDeviceNumber(), null);
		// if(null != lsCommonVehicleBO) {
		// monitorVehicleStatusBO.setVehicleId(lsCommonVehicleBO.getVehicleId());
		// }

	}

	/**
	 * 获取纬度
	 * 
	 * @return
	 */
	private double getRandomLat() {
		Random r = new Random();
		double d1 = r.nextDouble();
		double d = 32 + d1 * 10;
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
		double d = 35 + d1 * 10;
		return d;
	}

	public String getVehicleplatename() {
		return vehicleplatename;
	}

	public void setVehicleplatename(String vehicleplatename) {
		this.vehicleplatename = vehicleplatename;
	}

	public String getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}
}
