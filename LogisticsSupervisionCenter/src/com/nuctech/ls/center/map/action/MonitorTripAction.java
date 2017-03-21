package com.nuctech.ls.center.map.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;

import com.nuctech.ls.center.utils.IpUtil;
import com.nuctech.ls.center.websocket.WebsocketService;
import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.common.page.JsonDateValueProcessor;
import com.nuctech.ls.model.bo.common.LsCommonDriverBO;
import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.system.LsSystemNoticesBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseEsealBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseSensorBO;
import com.nuctech.ls.model.memcached.MemcachedUtil;
import com.nuctech.ls.model.redis.RedisClientTemplate;
import com.nuctech.ls.model.service.CommonDriverService;
import com.nuctech.ls.model.service.CommonPatrolService;
import com.nuctech.ls.model.service.CommonVehicleService;
import com.nuctech.ls.model.service.MonitorAlarmService;
import com.nuctech.ls.model.service.MonitorRouteAreaService;
import com.nuctech.ls.model.service.MonitorTripReportService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.MonitorVehicleStatusService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemNoticeService;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.service.WarehouseElockService;
import com.nuctech.ls.model.service.WarehouseEsealService;
import com.nuctech.ls.model.service.WarehouseSensorService;
import com.nuctech.ls.model.util.DeviceStatus;
import com.nuctech.ls.model.util.NoticeType;
import com.nuctech.ls.model.util.OperateContentType;
import com.nuctech.ls.model.util.OperateEntityType;
import com.nuctech.ls.model.vo.monitor.CommonVehicleDriverVO;
import com.nuctech.ls.model.vo.monitor.MonitorTripVehicleVO;
import com.nuctech.ls.model.vo.monitor.PatrolUserVO;
import com.nuctech.ls.model.vo.monitor.ViewAlarmReportVO;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.DateUtils;
import com.nuctech.util.NuctechUtil;
import com.nuctech.util.RedisKey;
import com.nuctech.util.UploadDownloadUtil;

import jcifs.util.Base64;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * 车辆行程信息Action
 * 
 * @author liushaowei
 *
 */
@Namespace("/monitortrip")
public class MonitorTripAction extends LSBaseAction {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 1L;
	/**
	 * 列表排序字段
	 */
	@SuppressWarnings("unused")
	private static final String DEFAULT_SORT_COLUMNS = "checkinTime DESC";
	private static final String DEFAULT_SORT_COLUMNS_WITH_ALIAS = "t.checkinTime DESC";
	/**
	 * 行程信息Service
	 */
	@Resource
	private MonitorTripService monitorTripService;
	/**
	 * 车辆信息Service
	 */
	@Resource
	private CommonVehicleService commonVehicleService;
	/**
	 * 司机信息Service
	 */
	@Resource
	private CommonDriverService commonDriverService;
	/**
	 * 追踪终端号Service
	 */
	@Resource
	private WarehouseElockService warehouseElockService;
	/**
	 * 子锁增删改查 Service
	 */
	@Resource
	private WarehouseEsealService warehouseEsealService;
	/**
	 * 传感器增删改查 Service
	 */
	@Resource
	private WarehouseSensorService warehouseSensorService;
	@Resource
	private MonitorTripReportService monitorTripReportService;
	/**
	 * 系统存储根目录，绝对路径
	 */
	@Resource
	private String rootPath;
	/**
	 * 系统存储根目录访问路径
	 */
	@Resource
	private String rootPathHttp;
	/**
	 * 行程照片存储路径
	 */
	@Resource
	private String tripPhotoPath;

	/**
	 * 行程信息及车辆信息
	 */
	private MonitorTripVehicleVO tripVehicleVO;

	/**
	 * 本地上传的行程照片列表
	 */
	private File[] tripPhotoLocal;

	/**
	 * 上传的行程照片名列表，tripPhotoLocal加"FileName"表示对应的文件名
	 */
	private String[] tripPhotoLocalFileName;

	/**
	 * 拍摄的行程照片Base64值列表
	 */
	private String[] tripCameraBase64;

	/**
	 * 组织机构 Service
	 */
	@Resource
	private SystemDepartmentService systemDepartmentService;

	private List<ViewAlarmReportVO> alarmList;

	/**
	 * 报警信息Service
	 */
	@Resource
	private MonitorAlarmService monitorAlarmService;

	@Resource
	private SystemNoticeService noticeService;

	@Resource
	private SystemUserService systemUserService;

	private String clientIPAddress;

	@Resource
	private MonitorVehicleStatusService monitorVehicleStatusService;
	/**
	 * 文件传输方式。0：本地/网络共享路径，1：FTP协议。
	 */
	@Resource
	private String fileTransferType;
	/**
	 * FTP主机名
	 */
	@Resource
	private String ftpHostName;
	/**
	 * FTP端口
	 */
	@Resource
	private String ftpPort;
	/**
	 * FTP登录用户名
	 */
	@Resource
	private String ftpUserName;
	/**
	 * FTP登录密码
	 */
	@Resource
	private String ftpPassword;
	@Resource
	private SystemModules systemModules;

	@Resource
	private SystemOperateLogService logService;

	@Resource
	private CommonPatrolService commonPatrolService;
	@Resource
	private MonitorRouteAreaService monitorRouteAreaService;
	@Resource
	public MemcachedUtil memcachedUtil;
	@Resource
	private RedisClientTemplate redisClientTemplate;

	private String checkinPort;
	private String checkinPortName;
	private List<LsSystemDepartmentBO> checkoutPortList;
	private List<PatrolUserVO> patrolList;
	private List<LsMonitorRouteAreaBO> targetZoonList;
	private List<LsMonitorRouteAreaBO> routeAreaList;
	private String[] vehiclePlateNumber;
	private String[] trackingDeviceNumber;
	private String[] esealNumber;
	private String[] esealOrder;
	private String[] sensorNumber;
	private String[] sensorOrder;
	private String[] trailerNumber;
	private String[] vehicleCountry;
	private String[] driverName;
	private String[] driverCountry;
	private String[] driverIdCard;
	private String[] containerNumber;
	private String[] goodsType;
	private String[] patrolId;
	private String[] riskStatus;
	private String fileIndexVehicleNumMap;
	private String photoIndexVehicleNumMap;

	/**
	 * 跳转到编辑页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "toEdit", results = { @Result(name = "success", location = "/trip/edit.jsp") })
	public String toEdit() throws Exception {
		clientIPAddress = IpUtil.getIpAddr(request);
		if (tripVehicleVO != null) {
			String tripId = tripVehicleVO.getTripId();
			if (!NuctechUtil.isNull(tripId)) {
				LsMonitorTripBO lsMonitorTripBO = monitorTripService.findById(tripId);
				BeanUtils.copyProperties(lsMonitorTripBO, tripVehicleVO);
				if (lsMonitorTripBO != null) {
					List<CommonVehicleDriverVO> vehicleDriverVOList = commonVehicleService
							.findAllVehicleDriverByTripId(tripId);
					tripVehicleVO.setCommonVehicleDriverList(vehicleDriverVOList);
				}
				return SUCCESS;
			} else {
				return ERROR;
			}
		} else {
			return ERROR;
		}
	}

	/**
	 * 跳转到编辑页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "toDetail", results = { @Result(name = "success", location = "/trip/detail.jsp") })
	public String toDetail() throws Exception {
		String tripId = request.getParameter("tripId");
		tripVehicleVO = new MonitorTripVehicleVO();
		if (!NuctechUtil.isNull(tripId)) {
			LsMonitorTripBO lsMonitorTripBO = monitorTripService.findById(tripId);
			if (lsMonitorTripBO != null) {
				BeanUtils.copyProperties(lsMonitorTripBO, tripVehicleVO);
				if (NuctechUtil.isNotNull(tripVehicleVO.getCheckinPort())) {
					tripVehicleVO.setCheckinPortName(
							systemDepartmentService.findById(tripVehicleVO.getCheckinPort()).getOrganizationName());
				}
				if (NuctechUtil.isNotNull(tripVehicleVO.getCheckoutPort())) {
					tripVehicleVO.setCheckoutPortName(
							systemDepartmentService.findById(tripVehicleVO.getCheckoutPort()).getOrganizationName());
				}

				List<CommonVehicleDriverVO> vehicleDriverVOList = commonVehicleService
						.findAllVehicleDriverByTripId(tripId);
				tripVehicleVO.setCommonVehicleDriverList(vehicleDriverVOList);
			}
			return SUCCESS;
		} else {
			return ERROR;
		}
	}

	/**
	 * 跳转到列表页面
	 * 
	 * @return
	 */
	@Action(value = "toList", results = { @Result(name = "success", location = "/trip/list.jsp") })
	public String toList() {
		return SUCCESS;
	}

	/**
	 * 列表查询
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	@Action(value = "list")
	public String list() throws Exception {
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
//		Map filtersMap = pageQuery.getFilters();
//		if (filtersMap == null) {
//			filtersMap = new HashMap();
//		}
//		if (filtersMap.get("checkinUser") == null) {
//			Map<String, Object> session = ActionContext.getContext().getSession();
//			filtersMap.put("checkinUser", ((SessionUser) session.get(Constant.SESSION_USER)).getUserId());// 存放用户Id，不是登录名
//			pageQuery.setFilters(filtersMap);
//		}
//		JSONObject retJson = monitorTripService.findTripVehicleList(pageQuery);
		JSONObject retJson = monitorTripReportService.findTripReportList(pageQuery);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(retJson.toString());
		out.flush();
		out.close();
		return null;
	}

	/**
	 * 跳转到行程激活页面
	 * 
	 * @return
	 */
	@Action(value = "toActivate", results = { @Result(name = "success", location = "/trip/activate.jsp") })
	public String toActivate() {
		clientIPAddress = IpUtil.getIpAddr(request);
		
		String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
		LsSystemDepartmentBO systemDepartmentBO = systemDepartmentService.findPortByUserId(userId);
		if (NuctechUtil.isNotNull(systemDepartmentBO)) {
			String departId = systemDepartmentBO.getOrganizationId();
			checkinPort = systemDepartmentBO.getOrganizationId();
			checkinPortName = systemDepartmentBO.getOrganizationName();
			// 检出口岸列表
			checkoutPortList = systemDepartmentService.findAllPortByUserId(userId);
			if (checkoutPortList != null && checkoutPortList.size() > 0) {
				for (Iterator<LsSystemDepartmentBO> it = checkoutPortList.iterator(); it.hasNext();) {
					LsSystemDepartmentBO department = it.next();
					if (department.getOrganizationId().equals(departId)) {
						it.remove();
					}
				}
			}

			// 护送巡逻队列表
			if(systemModules.isPatrolOn()) {
				patrolList = commonPatrolService.getEscortPatrolUserList();
				for (Iterator<PatrolUserVO> it = patrolList.iterator(); it.hasNext();) {
					PatrolUserVO patrolUserVO = it.next();
					// 排除已经关联其他行程的巡逻队
					if (NuctechUtil.isNotNull(patrolUserVO.getTripId())) {
						it.remove();
					}
				}
			}
			if(systemModules.isAreaOn()) {
				// target zoon
				targetZoonList = monitorRouteAreaService.findTargetZoonByPort(checkinPort);
			}
		}
		return SUCCESS;
	}

	/**
	 * 行程激活
	 * 
	 * @return
	 */
	@Action(value = "activate")
	public String activate() {
		JSONObject json = new JSONObject();
		try {
			// 新增行程信息、车辆信息
			LsMonitorTripBO lsMonitorTripBO = saveOrUpdateMonitorTripInfo(tripVehicleVO);
			String tripId = tripVehicleVO.getTripId();

			// 保存上传的图片和拍摄的照片
			saveTripPhotoLocal(lsMonitorTripBO);
			saveTripPhotoCamera(lsMonitorTripBO);
			
			if(systemModules.isPatrolOn()) {
				//关联巡逻队
				if(patrolId != null && patrolId.length > 0) {
					for(String pid: patrolId) {
						LsCommonPatrolBO commonPatrolBo =  commonPatrolService.findCommonPatrolById(pid);
						if(NuctechUtil.isNotNull(commonPatrolBo)) {
							commonPatrolBo.setTripId(tripId);
							commonPatrolService.updatePatrol(commonPatrolBo);
						}
					}
				}
			}

			if(systemModules.isApprovalOn()) {
				// 推送请求
				String noticeId = sendTripRequest(lsMonitorTripBO.getTripId(), Constant.TRIP_ACTIVATE, null, null);
				logger.info(String.format("行程激活请求已发出! tripId=%s, noticeId=%s", tripId, noticeId));
			} else {
				//执行激活操作
				doActivate(lsMonitorTripBO);
			}

			// 记录日志
			addLog(OperateContentType.ACTIVATE.toString(), OperateEntityType.TRIP.toString(), tripVehicleVO.toString());

			json.put("result", true);
			json.put("tripId", tripId);
		} catch (Exception e) {
			json.put("message", (e.getMessage() == null) ? e.toString(): e.getMessage());
			logger.error(e);
			// 发生异常，所有已保存数据删除
			String tripId = tripVehicleVO.getTripId();
			commonVehicleService.deleteByTripId(tripId);
			monitorVehicleStatusService.deleteByTripId(tripId);
			commonPatrolService.updateSetNullByTripId(tripId);
			monitorTripService.deleteById(tripId);
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
	 * 行程请求审批通过
	 * 
	 * @return
	 */
	@Action(value = "passTrip")
	public String passTrip() {
		JSONObject json = new JSONObject();
		try {
			String tripId = request.getParameter("tripId");
			if (NuctechUtil.isNotNull(tripId)) {
				String userId = request.getParameter("userId");

				LsMonitorTripBO lsMonitorTripBO = monitorTripService.findById(tripId);
				if (Constant.TRIP_STATUS_TO_START.equals(lsMonitorTripBO.getTripStatus())) {
					//执行激活操作
					doActivate(lsMonitorTripBO);
					
					logger.info(String.format("中心审批通过，更新行程状态为已激活! tripId: %s, 审批人Id: %s", tripId, userId));
					// 推送行程审批结果给口岸人员
					sendTripResult(tripId, lsMonitorTripBO.getCheckinUser(), Constant.NOTICE_TYPE_TRIP_REQUEST,
							Constant.TRIP_RESULT_TYPE_PASS, null);

					if(systemModules.isPatrolOn()) {
						// 推送给巡逻队有行程需要护送
						sendToPatrol(lsMonitorTripBO.getTripId());
					}
				} else if (Constant.TRIP_STATUS_TO_FINISH.equals(lsMonitorTripBO.getTripStatus())) {
					//执行结束操作
					doFinish(lsMonitorTripBO);
					
					logger.info(String.format("中心审批通过，更新行程状态为已结束! tripId: %s, 审批人Id: %s", tripId, userId));
					
					if(systemModules.isPatrolOn()) {
						// 取消关联巡逻队
						List<LsCommonPatrolBO> commonPatrolBOs = commonPatrolService.findAllByTripId(tripId);
						if (NuctechUtil.isNotNull(commonPatrolBOs)) {
							for (LsCommonPatrolBO commonPatrolBo : commonPatrolBOs) {
								commonPatrolBo.setTripId(null);
								commonPatrolService.updatePatrol(commonPatrolBo);
								logger.info("取消关联巡逻队：patrolId: " + commonPatrolBo.getPatrolId());
							}
						}
					}

					// 推送行程审批结果给口岸人员
					sendTripResult(tripId, lsMonitorTripBO.getCheckoutUser(), Constant.NOTICE_TYPE_TRIP_RESULT,
							Constant.TRIP_RESULT_TYPE_PASS, null);
				}
				json.put("result", true);
			}
		} catch (Exception e) {
			json.put("message", e.getMessage());
			logger.error(e);
		}

		response.setCharacterEncoding("utf-8");
		// 禁止浏览器开辟缓存
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 执行结束操作
	 * @param lsMonitorTripBO
	 */
	private void doFinish(LsMonitorTripBO lsMonitorTripBO) {
		lsMonitorTripBO.setTripStatus(Constant.TRIP_STATUS_FINISHED); // 状态改为已结束
		monitorTripService.updateMonitorTrip(lsMonitorTripBO);
		
		String tripId = lsMonitorTripBO.getTripId();
		List<LsMonitorVehicleStatusBO> monitorVehicleStatusBOs = monitorVehicleStatusService.findAllByTripId(tripId);
		if (NuctechUtil.isNotNull(monitorVehicleStatusBOs) && monitorVehicleStatusBOs.size() > 0) {
			for(LsMonitorVehicleStatusBO monitorVehicleStatusBO : monitorVehicleStatusBOs) {
				monitorVehicleStatusBO.setTripStatus(Constant.TRIP_STATUS_FINISHED);
				monitorVehicleStatusService.saveOrUpdate(monitorVehicleStatusBO);
			}
		}
		
		List<LsCommonVehicleBO> commonVehicleBOs = commonVehicleService.findAllByTripId(tripId);
		if (commonVehicleBOs != null && commonVehicleBOs.size() > 0) {
			// 每辆车一把关锁。
			for (LsCommonVehicleBO commonVehicleBO : commonVehicleBOs) {
				// 更新追踪终端号状态为正常，并修改所属口岸为检出口岸，并更新最近使用人员、最近使用时间
				String trackingDeviceNumber = commonVehicleBO.getTrackingDeviceNumber();
				if (NuctechUtil.isNotNull(trackingDeviceNumber)) {
					logger.info(String.format("更新追踪终端号状态为正常! trackingDeviceNumber: %s", trackingDeviceNumber));
					updateTrackingDeviceNumberStatus(trackingDeviceNumber, DeviceStatus.Normal.getText(),
							lsMonitorTripBO.getCheckoutPort(), lsMonitorTripBO.getCheckoutUser(), lsMonitorTripBO.getCheckoutTime());
					
					//从缓存删除相关的key
					String keyDevice = trackingDeviceNumber + ":" + tripId;
					redisClientTemplate.del(keyDevice);
				}
				// 更新所有子锁号和传感器号状态为正常，并修改所属口岸为检出口岸
				String esealNumber = commonVehicleBO.getEsealNumber();
				if (NuctechUtil.isNotNull(esealNumber)) {
					logger.info(String.format("更新所有子锁号状态为正常! esealNumber：%s esealOrder: %s", esealNumber,
							commonVehicleBO.getEsealOrder()));
					updateEsealNumberStatus(esealNumber, DeviceStatus.Normal.getText(),
							lsMonitorTripBO.getCheckoutPort());
				}
				String sensorNumber = commonVehicleBO.getSensorNumber();
				if (NuctechUtil.isNotNull(sensorNumber)) {
					logger.info(String.format("更新所有传感器号状态为正常! sensorNumber: %s sensorOrder: %s", sensorNumber,
							commonVehicleBO.getSensorOrder()));
					updateSensorNumberStatus(sensorNumber, DeviceStatus.Normal.getText(),
							lsMonitorTripBO.getCheckoutPort());
				}
				
				if(systemModules.isAreaOn()) {
					//待删除的缓存的target zoon日志相关信息
					String targetZoonId = lsMonitorTripBO.getTargetZoonId();
					if(NuctechUtil.isNotNull(targetZoonId)) {
						String[] routeAreaIds = targetZoonId.split(",");
						for(String routeAreaId : routeAreaIds) {
							String key = trackingDeviceNumber + "_" + routeAreaId;
							logger.info("Deleting target zoon info from cache: key=" + key);
//							memcachedUtil.delete(key);
							redisClientTemplate.del(key);
						}
					}
				}
				
				//从缓存删除相关的key
				String key = RedisKey.ALARMLIST_VEHICLEID_PREFIX + commonVehicleBO.getVehicleId();
				redisClientTemplate.del(key);
			}
		}
		
		//从缓存删除相关的key
		String keyTrip = RedisKey.ALARMLIST_TRIPID_PREFIX + tripId;
		redisClientTemplate.del(keyTrip);
	}

	/**
	 * 执行激活操作
	 * @param tripId
	 */
	private void doActivate(LsMonitorTripBO lsMonitorTripBO) {
		lsMonitorTripBO.setTripStatus(Constant.TRIP_STATUS_STARTED); // 状态改为已激活
		monitorTripService.updateMonitorTrip(lsMonitorTripBO);
		
		String tripId = lsMonitorTripBO.getTripId();
		List<LsCommonVehicleBO> commonVehicleBOs = new ArrayList<>();
		commonVehicleBOs = commonVehicleService.findAllByTripId(tripId);
		if (commonVehicleBOs != null && commonVehicleBOs.size() > 0) {
			// 每辆车一把关锁。
			for (LsCommonVehicleBO commonVehicleBO : commonVehicleBOs) {
				// 更新追踪终端号状态为在途
				String trackingDeviceNumber = commonVehicleBO.getTrackingDeviceNumber();
				if (NuctechUtil.isNotNull(trackingDeviceNumber)) {
					logger.info(String.format("更新追踪终端号状态为在途! trackingDeviceNumber: %s", trackingDeviceNumber));
					updateTrackingDeviceNumberStatus(trackingDeviceNumber, DeviceStatus.Inway.getText(), null, lsMonitorTripBO.getCheckinUser(), lsMonitorTripBO.getCheckinTime());
				}
				// 更新所有子锁号和传感器号状态为在途
				String esealNumber = commonVehicleBO.getEsealNumber();
				if (NuctechUtil.isNotNull(esealNumber)) {
					logger.info(String.format("更新所有子锁号状态为在途! esealNumber：%s esealOrder: %s", esealNumber,
							commonVehicleBO.getEsealOrder()));
					updateEsealNumberStatus(esealNumber, DeviceStatus.Inway.getText(), null);
				}
				String sensorNumber = commonVehicleBO.getSensorNumber();
				if (NuctechUtil.isNotNull(sensorNumber)) {
					logger.info(String.format("更新所有传感器号状态为在途! sensorNumber: %s sensorOrder: %s", sensorNumber,
							commonVehicleBO.getSensorOrder()));
					updateSensorNumberStatus(sensorNumber, DeviceStatus.Inway.getText(), null);
				}
				//删除缓存中该设备号以前的状态信息
//				memcachedUtil.delete(trackingDeviceNumber);
				redisClientTemplate.del(trackingDeviceNumber);
			}
		}
		
		// 将车辆状态表的行程状态改为已激活
		List<LsMonitorVehicleStatusBO> monitorVehicleStatusBOs = new ArrayList<>();
		monitorVehicleStatusBOs = monitorVehicleStatusService.findAllByTripId(tripId);
		if (monitorVehicleStatusBOs != null && monitorVehicleStatusBOs.size() > 0) {
			for (LsMonitorVehicleStatusBO monitorVehicleStatusBO : monitorVehicleStatusBOs) {
				monitorVehicleStatusBO.setTripStatus(Constant.TRIP_STATUS_STARTED);
				monitorVehicleStatusService.saveOrUpdate(monitorVehicleStatusBO);
			}
		}
	}

	/**
	 * 行程请求审批不通过
	 * 
	 * @return
	 */
	@Action(value = "noPassTrip")
	public String noPassTrip() {
		JSONObject json = new JSONObject();
		try {
			String tripId = request.getParameter("tripId");
			if (NuctechUtil.isNotNull(tripId)) {
				String userId = request.getParameter("userId");
				String reason = request.getParameter("reason");

				LsMonitorTripBO lsMonitorTripBO = monitorTripService.findById(tripId);
				// lsMonitorTripBO.setApprovalReason(reason); //不通过的理由
				// monitorTripService.updateMonitorTrip(lsMonitorTripBO);
				logger.info(String.format("中心审批未通过！tripId: %s, 审批人Id: %s， 理由: %s", tripId, userId, reason));

				if (Constant.TRIP_STATUS_TO_START.equals(lsMonitorTripBO.getTripStatus())) {
					sendTripResult(tripId, lsMonitorTripBO.getCheckinUser(), Constant.NOTICE_TYPE_TRIP_REQUEST,
							Constant.TRIP_RESULT_TYPE_NOPASS, reason);
				} else if (Constant.TRIP_STATUS_TO_FINISH.equals(lsMonitorTripBO.getTripStatus())) {
					sendTripResult(tripId, lsMonitorTripBO.getCheckoutUser(), Constant.NOTICE_TYPE_TRIP_RESULT,
							Constant.TRIP_RESULT_TYPE_NOPASS, reason);
				}
				json.put("result", true);
			}
		} catch (Exception e) {
			json.put("message", e.getMessage());
			logger.error(e);
		}

		response.setCharacterEncoding("utf-8");
		// 禁止浏览器开辟缓存
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 发送行程结果通知
	 * 
	 * @param tripId
	 *            行程Id
	 * @param userId
	 *            用户Id
	 * @param noticeType
	 *            通知类型。1：行程激活审批结果。2：行程结束审批结果
	 * @param resultType
	 *            结果类型。1：审批通过。2：审批未通过
	 * @param reason
	 *            理由（不通过时）
	 * @return
	 */
	private String sendTripResult(String tripId, String userId, String noticeType, String resultType, String reason) {
		LsSystemNoticesBO notice = createTripResultNotice(tripId, userId, noticeType, resultType, reason);
		JSONObject json = new JSONObject();
		json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_TRIP_RESULT);
		json.put("noticeId", notice.getNoticeId());
		json.put("title", notice.getNoticeTitle());
		json.put("content", notice.getNoticeContent());
		json.put("receiveUser", notice.getNoticeUsers());
		WebsocketService.sendMessage(json.toString());
		return notice.getNoticeId();
	}

	/**
	 * 发送行程请求
	 * 
	 * @param tripId
	 *            行程Id
	 * @param noticeType
	 *            请求类型。1：行程激活请求。2：行程结束请求
	 * @param specialFlag
	 *            是否特殊申请（行程结束）
	 * @param reason
	 *            特殊申请理由
	 * @return 通知Id
	 */
	private String sendTripRequest(String tripId, String noticeType, String specialFlag, String reason) {
		LsSystemNoticesBO notice = createTripNotice(tripId, noticeType, specialFlag, reason);
		JSONObject json = new JSONObject();
		json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_TRIP);
		json.put("noticeId", notice.getNoticeId());
		json.put("title", notice.getNoticeTitle());
		json.put("content", notice.getNoticeContent());
		json.put("receiveUser", notice.getNoticeUsers());
		WebsocketService.sendMessage(json.toString());
		return notice.getNoticeId();
	}

	/**
	 * 向巡逻队推送通知
	 * 
	 * @param tripId
	 *            行程Id
	 * @return 通知Id
	 */
	private String sendToPatrol(String tripId) {
		List<LsCommonPatrolBO> commonPatrolBOs = commonPatrolService.findAllByTripId(tripId);
		List<LsCommonVehicleBO> commonVehicleBOs = commonVehicleService.findAllByTripId(tripId);
		if (NuctechUtil.isNotNull(commonPatrolBOs) && commonPatrolBOs.size() > 0
				&& NuctechUtil.isNotNull(commonVehicleBOs) && commonVehicleBOs.size() > 0) {
			String[] userIds = new String[commonPatrolBOs.size()];
			for (int i = 0, len = commonPatrolBOs.size(); i < len; i++) {
				userIds[i] = commonPatrolBOs.get(i).getPotralUser();
			}
			String[] vehiclePlateNumbers = new String[commonVehicleBOs.size()];
			for (int i = 0, len = commonVehicleBOs.size(); i < len; i++) {
				vehiclePlateNumbers[i] = commonVehicleBOs.get(i).getVehiclePlateNumber();
			}
			LsSystemNoticesBO notice = createPatrolNotice(tripId, vehiclePlateNumbers, userIds);
			JSONObject json = new JSONObject();
			json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_NOTICE);
			json.put("noticeId", notice.getNoticeId());
			json.put("title", notice.getNoticeTitle());
			json.put("content", notice.getNoticeContent());
			json.put("receiveUser", notice.getNoticeUsers());
			WebsocketService.sendMessage(json.toString());
			return notice.getNoticeId();
		}
		return null;
	}

	/**
	 * 创建行程请求通知
	 * 
	 * @param tripId
	 *            行程Id
	 * @param noticeType
	 *            请求类型。1：行程激活请求。2：行程结束请求
	 * @param specialFlag
	 *            是否特殊申请（行程结束）
	 * @param reason
	 *            特殊申请理由
	 * @return
	 */
	private LsSystemNoticesBO createTripNotice(String tripId, String noticeType, String specialFlag, String reason) {
		LsSystemNoticesBO notice = new LsSystemNoticesBO();
		notice.setDeployTime(new Date());
		if ("1".equals(noticeType)) {
			notice.setNoticeContent(getLocaleString("trip.info.notice.activate",
					((SessionUser) session.get(Constant.SESSION_USER)).getUserName(), tripId));
			notice.setNoticeTitle(getLocaleString("trip.activate.title"));
		} else if ("2".equals(noticeType)) {
			if(Constant.TRIP_SPECIAL_APPLY.equals(specialFlag)) {
				notice.setNoticeContent(getLocaleString("trip.info.notice.finish.special",
						((SessionUser) session.get(Constant.SESSION_USER)).getUserName(), reason, tripId));
				notice.setNoticeTitle(getLocaleString("trip.finish.title.special"));
			} else {
				notice.setNoticeContent(getLocaleString("trip.info.notice.finish",
						((SessionUser) session.get(Constant.SESSION_USER)).getUserName(), tripId));
				notice.setNoticeTitle(getLocaleString("trip.finish.title"));
			}
		}
		notice.setNoticeId(generatePrimaryKey());
		notice.setPublisher(((SessionUser) session.get(Constant.SESSION_USER)).getUserId());
		List<String> noticeUsers = getQualityUsers();
		notice.setNoticeUsers(StringUtils.join(noticeUsers, ","));
		notice.setNoticeType(NoticeType.TripNotice.getType());
		noticeService.addNotice(notice);
		noticeService.publish(new String[] { notice.getNoticeId() });
		return notice;
	}

	/**
	 * 创建行程结果通知
	 * 
	 * @param tripId
	 *            行程Id
	 * @param noticeType
	 *            请求类型。1：行程激活请求。2：行程结束请求
	 * @param resultType
	 *            结果类型。1：审批通过。2：审批未通过
	 * @param reason
	 *            理由（不通过时）
	 * @return
	 */
	private LsSystemNoticesBO createTripResultNotice(String tripId, String userId, String noticeType, String resultType,
			String reason) {
		LsSystemNoticesBO notice = new LsSystemNoticesBO();
		notice.setDeployTime(new Date());
		if (NuctechUtil.isNull(reason)) {
			notice.setNoticeContent(
					tripId + "," + resultType + "," + ((SessionUser) session.get(Constant.SESSION_USER)).getUserId());
		} else {
			notice.setNoticeContent(tripId + "," + resultType + ","
					+ ((SessionUser) session.get(Constant.SESSION_USER)).getUserId() + "," + reason);
		}
		if ("1".equals(noticeType)) {
			notice.setNoticeTitle(getLocaleString("trip.activate.title"));
		} else if ("2".equals(noticeType)) {
			notice.setNoticeTitle(getLocaleString("trip.finish.title"));
		}
		notice.setNoticeId(generatePrimaryKey());
		notice.setPublisher(((SessionUser) session.get(Constant.SESSION_USER)).getUserId());
		notice.setNoticeUsers(userId);
		notice.setNoticeType(NoticeType.TripNotice.getType());

		noticeService.addNotice(notice);
		noticeService.publish(new String[] { notice.getNoticeId() });
		return notice;
	}

	/**
	 * 创建推送给巡逻队的通知
	 * 
	 * @param tripId
	 * @param vehiclePlateNumbers
	 * @param userIds
	 * @return
	 */
	private LsSystemNoticesBO createPatrolNotice(String tripId, String[] vehiclePlateNumbers, String[] userIds) {
		LsSystemNoticesBO notice = new LsSystemNoticesBO();
		notice.setDeployTime(new Date());
		notice.setNoticeContent(
				getLocaleString("trip.message.escort.notice") + StringUtils.join(vehiclePlateNumbers, ","));
		notice.setNoticeTitle(getLocaleString("NoticeType.TripEscortNotice"));
		notice.setNoticeId(generatePrimaryKey());
		notice.setPublisher(((SessionUser) session.get(Constant.SESSION_USER)).getUserId());
		notice.setNoticeUsers(StringUtils.join(userIds, ","));
		notice.setNoticeType(NoticeType.TripEscortNotice.getType());

		noticeService.addNotice(notice);
		noticeService.publish(new String[] { notice.getNoticeId() });
		return notice;
	}

	private List<String> getQualityUsers() {
		return systemUserService.getQualityCenterUsers();
	}

	/**
	 * 行程请求审批通过，包括行程激活和行程结束请求
	 * 
	 * @return
	 */
	@Action(value = "doPass")
	public String doPass() {
		JSONObject json = new JSONObject();

		String tripId = request.getParameter("s_tripId");
		if (NuctechUtil.isNotNull(tripId)) {
			LsMonitorTripBO lsMonitorTripBO = monitorTripService.findById(tripId);
			if (lsMonitorTripBO != null) {
				if (Constant.TRIP_STATUS_TO_START.equals(lsMonitorTripBO.getTripStatus())) {
					lsMonitorTripBO.setTripStatus(Constant.TRIP_STATUS_STARTED);
					monitorTripService.updateMonitorTrip(lsMonitorTripBO);
					sendTripResult(tripId, lsMonitorTripBO.getCheckinUser(), Constant.NOTICE_TYPE_TRIP_REQUEST,
							Constant.TRIP_RESULT_TYPE_PASS, null);
					logger.info(String.format("行程激活请求审批通过! tripId：%s, 审批人: %s", tripId,
							((SessionUser) session.get(Constant.SESSION_USER)).getUserId()));
				} else if (Constant.TRIP_STATUS_TO_FINISH.equals(lsMonitorTripBO.getTripStatus())) {
					lsMonitorTripBO.setTripStatus(Constant.TRIP_STATUS_FINISHED);
					monitorTripService.updateMonitorTrip(lsMonitorTripBO);
					sendTripResult(tripId, lsMonitorTripBO.getCheckoutUser(), Constant.NOTICE_TYPE_TRIP_RESULT,
							Constant.TRIP_RESULT_TYPE_PASS, null);
					logger.info(String.format("行程结束请求审批通过! tripId：%s, 审批人: %s", tripId,
							((SessionUser) session.get(Constant.SESSION_USER)).getUserId()));
				}
			}
			json.put("result", true);
		}

		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 行程请求审批未通过，包括行程激活和行程结束请求
	 * 
	 * @return
	 */
	@Action(value = "doNoPass")
	public String doNoPass() {
		JSONObject json = new JSONObject();

		String tripId = request.getParameter("s_tripId");
		if (!NuctechUtil.isNull(tripId)) {
			String reason = request.getParameter("reason");
			LsMonitorTripBO lsMonitorTripBO = monitorTripService.findById(tripId);
			if (lsMonitorTripBO != null) {
				// 未通过，只推送通知
				if (Constant.TRIP_STATUS_TO_START.equals(lsMonitorTripBO.getTripStatus())) {
					sendTripResult(tripId, lsMonitorTripBO.getCheckinUser(), "1", "2", reason);
					logger.info(String.format("行程激活请求审批未通过! tripId：%s, 审批人: %s, reason: %s", tripId,
							((SessionUser) session.get(Constant.SESSION_USER)).getUserId(), reason));
				} else if (Constant.TRIP_STATUS_TO_FINISH.equals(lsMonitorTripBO.getTripStatus())) {
					sendTripResult(tripId, lsMonitorTripBO.getCheckoutUser(), "2", "2", reason);
					logger.info(String.format("行程结束请求审批未通过! tripId：%s, 审批人: %s, reason: %s", tripId,
							((SessionUser) session.get(Constant.SESSION_USER)).getUserId(), reason));
				}
			}
			json.put("result", true);
		}

		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 更新传感器状态为在途
	 * 
	 * @param sensorNumber
	 *            传感器号，多个用逗号分隔
	 * @param status
	 *            状态
	 * @param belongTo
	 *            所属口岸
	 */
	private void updateSensorNumberStatus(String sensorNumber, String status, String belongTo) {
		String[] numbers = sensorNumber.split("\\s*,\\s*");
		for (String number : numbers) {
			LsWarehouseSensorBO warehouseSensorBO = warehouseSensorService.findBySensorNumber(number);
			if (warehouseSensorBO != null) {
				warehouseSensorBO.setSensorStatus(status);
				if (NuctechUtil.isNotNull(belongTo)) {
					warehouseSensorBO.setBelongTo(belongTo);
				}
				warehouseSensorService.modify(warehouseSensorBO);
			} else {
				logger.info(String.format("找不到传感器号记录! sensorNumber：%s", number));
			}
		}
	}

	/**
	 * 更新追踪终端号状态
	 * 
	 * @param trackingDeviceNumber
	 *            追踪终端号
	 * @param status
	 *            状态
	 * @param belongTo
	 *            所属口岸
	 */
	private void updateTrackingDeviceNumberStatus(String trackingDeviceNumber, String status, String belongTo) {
		updateTrackingDeviceNumberStatus(trackingDeviceNumber, status, belongTo, null, null);
	}

	/**
	 * 更新追踪终端号状态
	 * 
	 * @param trackingDeviceNumber
	 *            追踪终端号
	 * @param status
	 *            状态
	 * @param belongTo
	 *            所属口岸
	 * @param lastUser
	 *            最后使用人员
	 * @param lastUseTime
	 *            最后使用时间
	 */
	private void updateTrackingDeviceNumberStatus(String trackingDeviceNumber, String status, String belongTo,
			String lastUser, Date lastUseTime) {
		LsWarehouseElockBO warehouseElockBO = warehouseElockService.findByElockNumber(trackingDeviceNumber);
		if (warehouseElockBO != null) {
			warehouseElockBO.setElockStatus(status);
			if (NuctechUtil.isNotNull(belongTo)) {
				warehouseElockBO.setBelongTo(belongTo);
			}
			if (NuctechUtil.isNotNull(lastUser)) {
				warehouseElockBO.setLastUser(lastUser);
			}
			if (NuctechUtil.isNotNull(lastUseTime)) {
				warehouseElockBO.setLastUseTime(lastUseTime);
			}
			warehouseElockService.modify(warehouseElockBO);
		} else {
			logger.info(String.format("找不到追踪终端号记录! trackingDeviceNumber：%s", trackingDeviceNumber));
		}
	}

	/**
	 * 更新子锁状态
	 * 
	 * @param esealNumber
	 *            子锁号，多个用逗号分隔
	 * @param status
	 *            状态
	 * @param belongTo
	 *            所属口岸
	 */
	private void updateEsealNumberStatus(String esealNumber, String status, String belongTo) {
		String[] numbers = esealNumber.split("\\s*,\\s*");
		for (String number : numbers) {
			LsWarehouseEsealBO warehouseEsealBO = warehouseEsealService.findByEsealNumber(number);
			if (warehouseEsealBO != null) {
				warehouseEsealBO.setEsealStatus(status);
				if (NuctechUtil.isNotNull(belongTo)) {
					warehouseEsealBO.setBelongTo(belongTo);
				}
				warehouseEsealService.modify(warehouseEsealBO);
			} else {
				logger.info(String.format("找不到子锁号记录! esealNumber：%s", number));
			}
		}
	}

	/**
	 * 保存拍照的照片
	 * 
	 * @param tripId
	 * @return
	 */
	private void saveTripPhotoCamera(LsMonitorTripBO lsMonitorTripBO) {
		if (tripCameraBase64 != null && NuctechUtil.isNotNull(photoIndexVehicleNumMap)) {
			JSONObject json = JSONObject.fromObject(photoIndexVehicleNumMap);
			for (int i = 0; i < tripCameraBase64.length; i++) {
				String fileName = new Date().getTime() + ".jpg";
				String vehiclePlateNumber = json.getString("" + i);
				LsCommonVehicleBO commonVehicleBO = commonVehicleService.findByVehiclePlateNumber(vehiclePlateNumber, lsMonitorTripBO.getTripId());
				String vehicleId = commonVehicleBO.getVehicleId();
				if ("0".equals(fileTransferType)) {
					String targetPath = rootPath + "/" + tripPhotoPath + "/" + vehicleId;
					uploadBase64ByLocal(targetPath, fileName, tripCameraBase64[i]);
				} else if ("1".equals(fileTransferType)) {
					String targetPath = "/" + tripPhotoPath + "/" + vehicleId;
					uploadBase64ByFTP(targetPath, fileName, tripCameraBase64[i]);
				}
				String path = "/" + vehicleId + "/" + fileName;
				if (NuctechUtil.isNull(commonVehicleBO.getCheckinPicture())) {
					commonVehicleBO.setCheckinPicture("");
				}
				if (Constant.TRIP_STATUS_TO_START.equals(lsMonitorTripBO.getTripStatus())) {
					if (NuctechUtil.isNull(commonVehicleBO.getCheckinPicture())) {
						commonVehicleBO.setCheckinPicture(path);
					} else {
						commonVehicleBO.setCheckinPicture(commonVehicleBO.getCheckinPicture() + "," + path);
					}
				} else if (Constant.TRIP_STATUS_TO_FINISH.equals(lsMonitorTripBO.getTripStatus())) {
					if (NuctechUtil.isNull(commonVehicleBO.getCheckoutPicture())) {
						commonVehicleBO.setCheckoutPicture(path);
					} else {
						commonVehicleBO.setCheckoutPicture(commonVehicleBO.getCheckoutPicture() + "," + path);
					}
				}
				commonVehicleService.updateCommonVehicle(commonVehicleBO);
			}
		}
	}

	/**
	 * 保存上传的本地图片
	 * 
	 * @param tripId
	 * @return
	 * @throws Exception
	 */
	private void saveTripPhotoLocal(LsMonitorTripBO lsMonitorTripBO) throws Exception {
		if (NuctechUtil.isNotNull(tripPhotoLocal) && NuctechUtil.isNotNull(fileIndexVehicleNumMap)) {
			JSONObject json = JSONObject.fromObject(fileIndexVehicleNumMap);
			for (int i = 0; i < tripPhotoLocal.length; i++) {
				if (tripPhotoLocal[i] != null) {
					String fileName = new Date().getTime() + ".jpg";
					String vehiclePlateNumber = json.getString("" + i);
					LsCommonVehicleBO commonVehicleBO = commonVehicleService
							.findByVehiclePlateNumber(vehiclePlateNumber, lsMonitorTripBO.getTripId());
					String vehicleId = commonVehicleBO.getVehicleId();
					if ("0".equals(fileTransferType)) {
						String targetPath = rootPath + "/" + tripPhotoPath + "/" + vehicleId;
						uploadFileByLocal(targetPath, fileName, tripPhotoLocal[i]);
					} else if ("1".equals(fileTransferType)) {
						String targetPath = "/" + tripPhotoPath + "/" + vehicleId;
						uploadFileByFTP(targetPath, fileName, tripPhotoLocal[i]);
					}
					String path = "/" + vehicleId + "/" + fileName;
					if (Constant.TRIP_STATUS_TO_START.equals(lsMonitorTripBO.getTripStatus())
							|| Constant.TRIP_STATUS_STARTED.equals(lsMonitorTripBO.getTripStatus())) {
						if (NuctechUtil.isNull(commonVehicleBO.getCheckinPicture())) {
							commonVehicleBO.setCheckinPicture(path);
						} else {
							commonVehicleBO.setCheckinPicture(commonVehicleBO.getCheckinPicture() + "," + path);
						}
					} else if (Constant.TRIP_STATUS_TO_FINISH.equals(lsMonitorTripBO.getTripStatus())
							|| Constant.TRIP_STATUS_FINISHED.equals(lsMonitorTripBO.getTripStatus())) {
						if (NuctechUtil.isNull(commonVehicleBO.getCheckoutPicture())) {
							commonVehicleBO.setCheckoutPicture(path);
						} else {
							commonVehicleBO.setCheckoutPicture(commonVehicleBO.getCheckoutPicture() + "," + path);
						}
					}
					commonVehicleService.updateCommonVehicle(commonVehicleBO);
				}
			}
		}
	}

	/**
	 * 本地/网络共享保存base64编码到图片
	 * 
	 * @param targetPath
	 *            目标路径
	 * @param fileName
	 *            文件名
	 * @param base64
	 *            编码
	 * @return
	 */
	private String uploadBase64ByLocal(String targetPath, String fileName, String base64) {
		base64ToFile(base64, targetPath + "/" + fileName);
		return fileName;
	}

	/**
	 * 本地/网络共享上传文件
	 * 
	 * @param targetPath
	 * @param fileName
	 * @param tripPhoto
	 * @return
	 * @throws Exception
	 */
	private String uploadFileByLocal(String targetPath, String fileName, File tripPhoto) throws Exception {
		NuctechUtil.uploadFile(targetPath, fileName, tripPhoto);
		return fileName;
	}

	/**
	 * 保存base64编码到图片，并通过FTP上传
	 * 
	 * @param targetPath
	 * @param fileName
	 * @param base64
	 * @return
	 */
	private String uploadBase64ByFTP(String targetPath, String fileName, String base64) {
		String tempDir = System.getProperty("java.io.tmpdir");
		String localPath = tempDir + targetPath + "/" + fileName;
		String remotePath = targetPath + "/" + fileName;

		base64ToFile(base64, localPath);
		boolean flag = UploadDownloadUtil.ftpUpload(ftpHostName, Integer.parseInt(ftpPort), ftpUserName, ftpPassword,
				remotePath, localPath);
		logger.info(String.format("FTP上传文件 %s -> %s 上传结果：%s", localPath, remotePath, flag));
		return remotePath;
	}

	/**
	 * 通过FTP上传文件
	 * 
	 * @param targetPath
	 * @param fileName
	 * @param tripPhoto
	 */
	private void uploadFileByFTP(String targetPath, String fileName, File tripPhoto) {
		String remotePath = targetPath + "/" + fileName;
		boolean flag = UploadDownloadUtil.ftpUpload(ftpHostName, Integer.parseInt(ftpPort), ftpUserName, ftpPassword,
				remotePath, tripPhoto.getAbsolutePath());
		logger.info(String.format("FTP上传文件 %s -> %s 上传结果：%s", tripPhoto.getAbsolutePath(), remotePath, flag));
	}

	/**
	 * 重命名同名文件：abc.jpg --> abc(1).jpg
	 * 
	 * @param path
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unused")
	private String renameFileIfExists(String path, String name) {
		File file = new File(path, name);
		if (file != null && file.exists()) {
			int index = name.lastIndexOf(".");
			if (index > -1) {
				name = name.substring(0, index) + "(1)" + name.substring(index);
			} else {
				name = name + "(1)";
			}
		}
		return name;
	}

	/**
	 * 保存Base64内容为图片
	 * 
	 * @param base64
	 * @param filePath
	 *            文件绝对路径
	 */
	private void base64ToFile(String base64, String filePath) {
		try {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			out.write(Base64.decode(base64));
			out.close();
			logger.info("保存Base64到文件：" + file.getAbsolutePath());
		} catch (FileNotFoundException e) {
			logger.error("保存图像出错！", e);
		} catch (IOException e) {
			logger.error("保存图像出错！", e);
		}
	}

	/**
	 * 新增行程及车辆信息，返回创建后的行程
	 * 
	 * @param tripVehicleVO
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private LsMonitorTripBO addMonitorTripInfo(MonitorTripVehicleVO tripVehicleVO) throws Exception {
		// 保存车辆信息
		List<CommonVehicleDriverVO> commonVehicleList = tripVehicleVO.getCommonVehicleDriverList();
		if (null != commonVehicleList && commonVehicleList.size() > 0) {
			for (CommonVehicleDriverVO commonVehicleDriver : commonVehicleList) {
				LsCommonDriverBO commonDriverBO = new LsCommonDriverBO();
				BeanUtils.copyProperties(commonVehicleDriver, commonDriverBO);
				commonDriverBO.setDriverId(generatePrimaryKey());
				commonDriverService.addCommonDriver(commonDriverBO);

				LsCommonVehicleBO commonVehicleBO = new LsCommonVehicleBO();
				BeanUtils.copyProperties(commonVehicleDriver, commonVehicleBO);
				commonVehicleBO.setVehicleId(generatePrimaryKey());
				commonVehicleBO.setDriverId(commonDriverBO.getDriverId());
				commonVehicleService.addCommonVehicle(commonVehicleBO);
			}
		}

		// 保存行程信息
		LsMonitorTripBO lsMonitorTripBO = new LsMonitorTripBO();
		BeanUtils.copyProperties(tripVehicleVO, lsMonitorTripBO);
		lsMonitorTripBO.setTripId(generatePrimaryKey());
		lsMonitorTripBO.setTripStatus(Constant.TRIP_STATUS_TO_START); // 状态为待激活。中心审批通过后改为已激活
		lsMonitorTripBO.setCheckinTime(new Date());
		lsMonitorTripBO.setCheckinUser(((SessionUser) session.get(Constant.SESSION_USER)).getUserId());
		monitorTripService.addMonitorTrip(lsMonitorTripBO);

		logger.info(String.format("保存车辆及行程信息，tripId：%s", lsMonitorTripBO.getTripId()));
		return lsMonitorTripBO;
	}

	/**
	 * 新增或修改行程及车辆信息，返回创建后的行程
	 * 
	 * @param tripVehicleVO
	 * @return
	 * @throws Exception
	 */
	private LsMonitorTripBO saveOrUpdateMonitorTripInfo(MonitorTripVehicleVO tripVehicleVO) throws Exception {
		// 保存更新行程信息
		LsMonitorTripBO lsMonitorTripBO = new LsMonitorTripBO();
		BeanUtils.copyProperties(tripVehicleVO, lsMonitorTripBO);
		if (NuctechUtil.isNull(lsMonitorTripBO.getTripId())) {
			lsMonitorTripBO.setTripId(generatePrimaryKey());
		}
		if(systemModules.isApprovalOn()) {
			lsMonitorTripBO.setTripStatus(Constant.TRIP_STATUS_TO_START); // 状态为待激活。中心审批通过后改为已激活
		} else {
			lsMonitorTripBO.setTripStatus(Constant.TRIP_STATUS_STARTED); // 状态为进行中
		}
		lsMonitorTripBO.setCheckinTime(new Date());
		lsMonitorTripBO.setCheckinUser(((SessionUser) session.get(Constant.SESSION_USER)).getUserId());
		monitorTripService.merge(lsMonitorTripBO);
		tripVehicleVO.setTripId(lsMonitorTripBO.getTripId());

		// 删除已关联的车辆信息
		List<LsCommonVehicleBO> vehicleList = commonVehicleService.findAllByTripId(lsMonitorTripBO.getTripId());
		if (NuctechUtil.isNotNull(vehicleList) && vehicleList.size() > 0) {
			String[] ids = new String[vehicleList.size()];
			for (int i = 0; i < vehicleList.size(); i++) {
				ids[i] = vehicleList.get(i).getVehicleId();
				// 删除每个车辆上传的图片
				String path = tripPhotoPath + "/" + vehicleList.get(i).getVehicleId();
				if (new File(path).exists()) {
					logger.info("删除目录" + path);
					NuctechUtil.deleteFloder(path);
				}
			}
			commonVehicleService.delete(ids);
		}
		// 保存更新车辆信息,保存车辆状态信息
		if (null != trackingDeviceNumber && trackingDeviceNumber.length > 0) {
			int length = trackingDeviceNumber.length;
			for (int i = 0; i < length; i++) {
				LsCommonDriverBO commonDriverBO = commonDriverService.findByIdCard(driverIdCard[i]);
				if (NuctechUtil.isNull(commonDriverBO)) {
					commonDriverBO = new LsCommonDriverBO();
					commonDriverBO.setDriverId(generatePrimaryKey());
					commonDriverBO.setDriverCountry(driverCountry[i]);
					commonDriverBO.setDriverIdCard(driverIdCard[i]);
					commonDriverBO.setDriverName(driverName[i]);
					commonDriverService.merge(commonDriverBO);
				}
				// 保存车辆信息
				LsCommonVehicleBO commonVehicleBO = new LsCommonVehicleBO();
				commonVehicleBO.setVehicleId(generatePrimaryKey());
				commonVehicleBO.setDriverId(commonDriverBO.getDriverId());
				commonVehicleBO.setTripId(lsMonitorTripBO.getTripId());
				commonVehicleBO.setTrackingDeviceNumber(trackingDeviceNumber[i]);
				commonVehicleBO.setContainerNumber(containerNumber[i]);
				commonVehicleBO.setEsealNumber(esealNumber[i]);
				commonVehicleBO.setEsealOrder(esealOrder[i]);
				commonVehicleBO.setGoodsType(goodsType[i]);
				commonVehicleBO.setSensorNumber(sensorNumber[i]);
				commonVehicleBO.setSensorOrder(sensorOrder[i]);
				commonVehicleBO.setTrailerNumber(trailerNumber[i]);
				commonVehicleBO.setVehicleCountry(vehicleCountry[i]);
				commonVehicleBO.setVehiclePlateNumber(vehiclePlateNumber[i]);
				commonVehicleBO.setFreezeAlarm(Constant.FREEZE_ALARM_YES);
				if(systemModules.isRiskOn() && riskStatus != null) {
					commonVehicleBO.setRiskStatus(riskStatus[i]);
				}
				commonVehicleService.merge(commonVehicleBO);

				// 保存车辆状态表信息
				LsMonitorVehicleStatusBO monitorVehicleStatusBO = new LsMonitorVehicleStatusBO();
				monitorVehicleStatusBO.setVehicleStatusId(generatePrimaryKey());
				monitorVehicleStatusBO.setTripId(lsMonitorTripBO.getTripId());
				monitorVehicleStatusBO.setEsealNumber(esealNumber[i]);
				if(systemModules.isRiskOn() && riskStatus != null) {
					monitorVehicleStatusBO.setRiskStatus(riskStatus[i]);
				}
				monitorVehicleStatusBO.setVehicleId(commonVehicleBO.getVehicleId());
				monitorVehicleStatusBO.setTripStatus("0");
				monitorVehicleStatusBO.setLocationType("0");
				monitorVehicleStatusBO.setTrackingDeviceNumber(trackingDeviceNumber[i]);
				monitorVehicleStatusBO.setCreateTime(new Date());
				monitorVehicleStatusBO.setBrokenStatus("0");
				monitorVehicleStatusBO.setSensorNumber(sensorNumber[i]);
				monitorVehicleStatusBO.setLocationTime(new Date());
				monitorVehicleStatusBO.setCheckinPort(tripVehicleVO.getCheckinPort());
				monitorVehicleStatusBO.setCheckoutPort(tripVehicleVO.getCheckoutPortName());
				monitorVehicleStatusBO.setRouteId(tripVehicleVO.getRouteId());
				monitorVehicleStatusService.saveOrUpdate(monitorVehicleStatusBO);
			}
		}
		BeanUtils.copyProperties(lsMonitorTripBO, tripVehicleVO);

		logger.info(String.format("保存或更新车辆及行程信息，tripId：%s", lsMonitorTripBO.getTripId()));
		return lsMonitorTripBO;
	}

	/**
	 * 更新行程及车辆信息
	 * 
	 * @param tripVehicleVO
	 * @return
	 * @throws Exception
	 */
	private LsMonitorTripBO updateTripVehicleVOToFinish(MonitorTripVehicleVO tripVehicleVO) throws Exception {
		// 更新行程信息
		LsMonitorTripBO lsMonitorTripBO = monitorTripService.findById(tripVehicleVO.getTripId());
		if (lsMonitorTripBO != null) {
			if(systemModules.isApprovalOn()) {
				lsMonitorTripBO.setTripStatus(Constant.TRIP_STATUS_TO_FINISH);// 改为待结束，中心用户审批通过后改为结束。
			} else {
				lsMonitorTripBO.setTripStatus(Constant.TRIP_STATUS_FINISHED);// 改为已结束
			}
			lsMonitorTripBO.setCheckoutUser(((SessionUser) session.get(Constant.SESSION_USER)).getUserId()); // 存放用户Id，不是登录名
			lsMonitorTripBO.setCheckoutTime(new Date());
			lsMonitorTripBO.setSpecialFlag(tripVehicleVO.getSpecialFlag());
			lsMonitorTripBO.setReason(tripVehicleVO.getReason());
			// 计算行程耗时，单位：分钟
			long timeCost = DateUtils.differenceBetweenDate(lsMonitorTripBO.getCheckoutTime(),
					lsMonitorTripBO.getCheckinTime()) / 1000 / 60;
			lsMonitorTripBO.setTimeCost(String.valueOf(timeCost));
			monitorTripService.updateMonitorTrip(lsMonitorTripBO);
			logger.info(String.format("更新行程信息，tripId：%s", lsMonitorTripBO.getTripId()));
		} else {
			logger.error(String.format("行程记录不存在！tripId: %s", tripVehicleVO.getTripId()));
			throw new Exception(String.format("行程记录不存在！tripId: %s", tripVehicleVO.getTripId()));
		}

		return lsMonitorTripBO;
	}

	/**
	 * 跳转到行程结束页面
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "toFinish", results = { @Result(name = "success", location = "/trip/finish.jsp") })
	public String toFinish() {
		clientIPAddress = IpUtil.getIpAddr(request);
		String s_declarationNumber = request.getParameter("s_declarationNumber");
		String s_trackingDeviceNumber = request.getParameter("s_trackingDeviceNumber");
		if (NuctechUtil.isNotNull(s_declarationNumber) || NuctechUtil.isNotNull(s_trackingDeviceNumber)) {
			pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
			pageQuery.setPageSize(1);
			pageQuery.setPage(1);

			tripVehicleVO = monitorTripService.findOneTripVehicleAlarm(pageQuery);
			if (tripVehicleVO != null) {
				pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
				Map<String, String> filters = pageQuery.getFilters();
				filters.put("tripId", tripVehicleVO.getTripId());
				pageQuery.setFilters(filters);

				List<CommonVehicleDriverVO> commonVehicleDriverList = monitorTripService
						.findAllVehicleDriverByPageQuery(pageQuery, tripVehicleVO.getTripId());
				tripVehicleVO.setCommonVehicleDriverList(commonVehicleDriverList);
				copyProperties(tripVehicleVO, commonVehicleDriverList);

				pageQuery.setPageSize(Constant.MAX_PAGE_SIZE);
				alarmList = monitorAlarmService.findListByTripId(pageQuery);
			}
		}
		return SUCCESS;
	}

	/**
	 * 从所有车辆中拷贝属性到VO类
	 * 
	 * @param tripVehicleVO
	 * @param commonVehicleDriverBOList
	 */
	private void copyProperties(MonitorTripVehicleVO tripVehicleVO,
			List<CommonVehicleDriverVO> commonVehicleDriverBOList) {
		if (null != commonVehicleDriverBOList && commonVehicleDriverBOList.size() > 0) {
			List<String> vehiclePlateNumber = new ArrayList<>();
			List<String> trackingDeviceNumber = new ArrayList<>();
			List<String> esealNumber = new ArrayList<>();
			List<String> sensorNumber = new ArrayList<>();
			for (CommonVehicleDriverVO vehicleDriverVO : commonVehicleDriverBOList) {
				if (NuctechUtil.isNotNull(vehicleDriverVO.getVehiclePlateNumber())) {
					vehiclePlateNumber.add(vehicleDriverVO.getVehiclePlateNumber());
				}
				if (NuctechUtil.isNotNull(vehicleDriverVO.getTrackingDeviceNumber())) {
					trackingDeviceNumber.add(vehicleDriverVO.getTrackingDeviceNumber());
				}
				if (NuctechUtil.isNotNull(vehicleDriverVO.getEsealNumber())) {
					esealNumber.add(vehicleDriverVO.getEsealNumber());
				}
				if (NuctechUtil.isNotNull(vehicleDriverVO.getSensorNumber())) {
					sensorNumber.add(vehicleDriverVO.getSensorNumber());
				}
			}
			tripVehicleVO.setVehiclePlateNumber(StringUtils.join(vehiclePlateNumber, ","));
			tripVehicleVO.setTrackingDeviceNumber(StringUtils.join(trackingDeviceNumber, ","));
			tripVehicleVO.setEsealNumber(StringUtils.join(esealNumber, ","));
			tripVehicleVO.setSensorNumber(StringUtils.join(sensorNumber, ","));
		}
	}

	/**
	 * 行程结束
	 * 
	 * @return
	 */
	@Action(value = "finish", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String finish() {
		JSONObject json = new JSONObject();
		try {
			// 更新状态
			String tripId = tripVehicleVO.getTripId();
			updateTripVehicleVOToFinish(tripVehicleVO);

			LsMonitorTripBO lsMonitorTripBO = monitorTripService.findById(tripId);

			// 保存照片
			saveTripPhotoLocal(lsMonitorTripBO);
			saveTripPhotoCamera(lsMonitorTripBO);

			if(systemModules.isApprovalOn()) {
				// 推送请求
				logger.info("推送请求通知...");
				String noticeId = sendTripRequest(lsMonitorTripBO.getTripId(), Constant.TRIP_FINISH, lsMonitorTripBO.getSpecialFlag(), lsMonitorTripBO.getReason());
				logger.info(String.format("行程结束成功! tripId：%s, noticeId: %s", tripId, noticeId));
			} else {
				//执行结束操作
				doFinish(lsMonitorTripBO);
			}

			// 记录日志
			addLog(OperateContentType.FINISH.toString(), OperateEntityType.TRIP.toString(), tripVehicleVO.toString());
			json.put("result", true);
		} catch (Exception e) {
			json.put("message", e.getMessage());
			logger.error(e);
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
	 * 查询行程信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "findOneTripVehicle", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String findOneTripVehicle() throws Exception {
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
		pageQuery.setPageSize(1);
		pageQuery.setPage(1);
		JSONObject retJson = monitorTripService.findOneTripVehicle(pageQuery);

		response.setCharacterEncoding("utf-8");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().println(retJson.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * 查询行程信息包含报警信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "findOneTripVehicleAlarm", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String findOneTripVehicleAlarm() {
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
		pageQuery.setPageSize(1);
		pageQuery.setPage(1);

		JsonConfig config = new JsonConfig();
		config.setIgnoreDefaultExcludes(false);
		config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor(Constant.JordanTimeFormat));

		JSONObject json = new JSONObject();
		MonitorTripVehicleVO tripVehicleVO = monitorTripService.findOneTripVehicleAlarm(pageQuery);
		if (tripVehicleVO != null) {
			JSONObject jsonObj = JSONObject.fromObject(tripVehicleVO, config);
			json.put("tripVehicleVO", jsonObj);

			pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
			Map<String, String> filters = pageQuery.getFilters();
			filters.put("tripId", tripVehicleVO.getTripId());
			pageQuery.setFilters(filters);
			alarmList = monitorAlarmService.findListByTripId(pageQuery);
			JSONArray jsonObj1 = JSONArray.fromObject(alarmList, config);
			json.put("alarmList", jsonObj1);
			/*
			 * for(ViewAlarmReportVO vo: alarmList) { JSONObject jsonObj1 =
			 * JSONObject.fromObject(vo, config); json.put("tripVehicleVO",
			 * jsonObj); }
			 */
		}

		response.setCharacterEncoding("utf-8");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}

	protected String getLocaleString(String messageKey, Object... params) {
		String message = getLocaleString(messageKey);
		MessageFormat mf = new MessageFormat(message, request.getLocale());
		return mf.format(params);
	}

	/**
	 * 更新行程
	 * 
	 * @return
	 */
	@Action(value = "update")
	public String update() {
		JSONObject json = new JSONObject();
		try {
			// 更新行程信息、车辆信息
			LsMonitorTripBO lsMonitorTripBO = updateMonitorTripInfo(tripVehicleVO);
			String tripId = lsMonitorTripBO.getTripId();

			// 重新保存上传的图片和拍摄的照片
			saveTripPhotoLocal(lsMonitorTripBO);
			saveTripPhotoCamera(lsMonitorTripBO);

			logger.info(String.format("行程激活请求已更新! tripId=%s", tripId));
			json.put("result", true);
		} catch (Exception e) {
			json.put("message", e.getMessage());
			logger.error(e);
		}

		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}

	private LsMonitorTripBO updateMonitorTripInfo(MonitorTripVehicleVO tripVehicleVO) throws Exception {
		// 更新车辆信息
		List<CommonVehicleDriverVO> commonVehicleDriverList = tripVehicleVO.getCommonVehicleDriverList();
		if (null != commonVehicleDriverList && commonVehicleDriverList.size() > 0) {
			for (CommonVehicleDriverVO commonVehicleDriver : commonVehicleDriverList) {
				LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService.findById(commonVehicleDriver.getVehicleId());
				BeanUtils.copyProperties(commonVehicleDriver, lsCommonVehicleBO);
				commonVehicleService.merge(lsCommonVehicleBO);

				LsCommonDriverBO lsCommonDriverBO = commonDriverService.findById(commonVehicleDriver.getDriverId());
				BeanUtils.copyProperties(commonVehicleDriver, lsCommonDriverBO);
				commonDriverService.merge(lsCommonDriverBO);
			}
		}

		// 更新行程信息
		LsMonitorTripBO lsMonitorTripBO = monitorTripService.findById(tripVehicleVO.getTripId());
		BeanUtils.copyProperties(tripVehicleVO, lsMonitorTripBO,
				new String[] { "vehicleId", "checkinUser", "checkinTime", "checkinPort", "tripStatus" });
		// 删除上传的图片
		// String path = tripPhotoPath + "/" + lsMonitorTripBO.getTripId();
		// if(new File(path).exists()) {
		// logger.info("删除目录" + path);
		// NuctechUtil.deleteFloder(path);
		// }
		monitorTripService.merge(lsMonitorTripBO);

		logger.info(String.format("更新车辆及行程信息，tripId：%s", lsMonitorTripBO.getTripId()));
		return lsMonitorTripBO;
	}

	@Action(value = "loadTripInfo")
	public String loadTripInfo() {
		JSONObject json = new JSONObject();
		String declarationNumber = request.getParameter("declarationNumber");
		try {
			LsMonitorTripBO lsMonitorTripBO = monitorTripService.findByDeclarationNumber(declarationNumber);
			if (NuctechUtil.isNotNull(lsMonitorTripBO)) {
				tripVehicleVO = new MonitorTripVehicleVO();
				BeanUtils.copyProperties(lsMonitorTripBO, tripVehicleVO);
				List<CommonVehicleDriverVO> commonVehicleDriverList = commonVehicleService
						.findAllVehicleDriverByTripId(lsMonitorTripBO.getTripId());
				tripVehicleVO.setCommonVehicleDriverList(commonVehicleDriverList);
				json.put("tripVehicleVO", tripVehicleVO);
			}
		} catch (Exception e) {
			json.put("message", e.getMessage());
			logger.error(e);
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
	 * 关锁操作日志记录
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "elockLog", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "error" }) })
	public String elockLog() throws Exception {
		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
		String type = request.getParameter("type");
		String trackingDeviceNumber = request.getParameter("trackingDeviceNumber");
		String data = "trackingDeviceNumber:" + trackingDeviceNumber;
		String operateContentType = "";
		String operateEntityType = "";
		if ("setLocked".equals(type)) {
			operateContentType = OperateContentType.SETLOCKED.toString();
			operateEntityType = OperateEntityType.ELOCK.toString();
		} else if ("setUnlocked".equals(type)) {
			operateContentType = OperateContentType.SETUNLOCKED.toString();
			operateEntityType = OperateEntityType.ELOCK.toString();
		} else if ("clearAlarm".equals(type)) {
			operateContentType = OperateContentType.CLEAR.toString();
			operateEntityType = OperateEntityType.ELOCK_ALARM.toString();
		}
		logService.addLog(operateContentType, operateEntityType, sessionUser.getUserId(), MonitorTripAction.class.toString(), data);
		return SUCCESS;
	}

	/**
	 * 记录行程操作日志
	 * 
	 * @param content
	 */
	private void addLog(String operate, String entity, String params) {
		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
		logService.addLog(operate, entity, sessionUser.getUserId(), MonitorTripAction.class.toString(), params);
	}

	/**
	 * 撤销行程
	 * 
	 * @return
	 */
	@Action(value = "revokeTrip", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String revokeTrip() {
		JSONObject json = new JSONObject();
		try {
			String tripId = request.getParameter("tripId");
			if (NuctechUtil.isNotNull(tripId)) {
				LsMonitorTripBO lsMonitorTripBO = monitorTripService.findById(tripId);
				lsMonitorTripBO.setTripStatus(Constant.TRIP_STATUS_TO_START);
				monitorTripService.updateMonitorTrip(lsMonitorTripBO);

				// 设备状态更改？

				// 记录日志
				addLog(OperateContentType.REVOKE.toString(), OperateEntityType.TRIP.toString(), lsMonitorTripBO.toString());
				// 更新关锁最后一次操作记录
				List<LsCommonVehicleBO> commonVehicleBOs = new ArrayList<>();
				commonVehicleBOs = commonVehicleService.findAllByTripId(tripId);
				if (commonVehicleBOs != null && commonVehicleBOs.size() > 0) {
					// 每辆车一把关锁。
					for (LsCommonVehicleBO commonVehicleBO : commonVehicleBOs) {
						updateTrackingDevice(commonVehicleBO.getTrackingDeviceNumber());
					}
				}

				json.put("result", true);
				logger.info(String.format("行程撤销! tripId：%s, userId: %s", tripId,
						((SessionUser) request.getSession().getAttribute(Constant.SESSION_USER)).getUserId()));
			}
		} catch (Exception e) {
			json.put("message", e.getMessage());
			logger.error(e);
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
	 * 更新关锁最近使用信息
	 * 
	 * @param trackingDeviceNumber
	 */
	private void updateTrackingDevice(String trackingDeviceNumber) {
		LsWarehouseElockBO warehouseElockBO = warehouseElockService.findByElockNumber(trackingDeviceNumber);
		if (warehouseElockBO != null) {
			warehouseElockBO.setLastUseTime(new Date());
			warehouseElockBO
					.setLastUser(((SessionUser) request.getSession().getAttribute(Constant.SESSION_USER)).getUserId());
			warehouseElockService.modify(warehouseElockBO);
		}
	}

	@Action(value = "findLatestByVehiclePlateNumber")
	public void findLatestByVehiclePlateNumber() throws IOException {
		String vehiclePlateNumber = request.getParameter("vehiclePlateNumber");
		LsMonitorTripBO lsMonitorTripBO = monitorTripService.findLatestByVehiclePlateNumber(vehiclePlateNumber);

		JSONObject json = new JSONObject();
		if (NuctechUtil.isNotNull(lsMonitorTripBO)) {
			json.put("isOnWay", !Constant.TRIP_STATUS_FINISHED.equals(lsMonitorTripBO.getTripStatus()));
		}
		response.setCharacterEncoding("UTF-8");
		// 禁止浏览器开辟缓存
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
		out.print(json.toString());
		out.flush();
		out.close();
	}

	/**
	 * 跳转到扫描条码页面
	 * 
	 * @return
	 */
	@Action(value = "toScan", results = { @Result(name = "success", location = "/trip/scan.jsp") })
	public String toScan() {
		return SUCCESS;
	}

	/**
	 * 跳转到拍照页面
	 * 
	 * @return
	 */
	@Action(value = "toCamera", results = { @Result(name = "success", location = "/trip/camera.jsp") })
	public String toCamera() {
		return SUCCESS;
	}
	
	/**
	 * 根据设备号提供匹配
	 * @return
	 * @throws Exception
	 */
	@Action(value = "queryByDeviceNum", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String queryByDeviceNum() throws Exception {
		String trackingDeviceNumber = request.getParameter("s_trackingDeviceNumber");
		String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
		LsSystemDepartmentBO systemDepartmentBO = systemDepartmentService.findPortByUserId(userId);
		if (NuctechUtil.isNotNull(systemDepartmentBO)) {
			List<String> numbers = monitorTripService.findUnFinishedDeviceNum(trackingDeviceNumber, systemDepartmentBO.getOrganizationId());
			if (numbers != null && numbers.size() > 0) {
				JSONArray array = new JSONArray();
				for (String number : numbers) {
					JSONObject obj = new JSONObject();
					obj.put("number", number);
					array.add(obj);
				}
				try {
					JSONObject json = new JSONObject();
					json.put("value", array);
					response.setCharacterEncoding("utf-8");
					response.setDateHeader("Expires", 0);
					response.getWriter().println(json.toString());
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}

		return null;
	}

	public MonitorTripVehicleVO getTripVehicleVO() {
		return tripVehicleVO;
	}

	public void setTripVehicleVO(MonitorTripVehicleVO tripVehicleVO) {
		this.tripVehicleVO = tripVehicleVO;
	}

	public File[] getTripPhotoLocal() {
		return tripPhotoLocal;
	}

	public void setTripPhotoLocal(File[] tripPhotoLocal) {
		this.tripPhotoLocal = tripPhotoLocal;
	}

	public String[] getTripPhotoLocalFileName() {
		return tripPhotoLocalFileName;
	}

	public void setTripPhotoLocalFileName(String[] tripPhotoLocalFileName) {
		this.tripPhotoLocalFileName = tripPhotoLocalFileName;
	}

	public String[] getTripCameraBase64() {
		return tripCameraBase64;
	}

	public void setTripCameraBase64(String[] tripCameraBase64) {
		this.tripCameraBase64 = tripCameraBase64;
	}

	public List<ViewAlarmReportVO> getAlarmList() {
		return alarmList;
	}

	public void setAlarmList(List<ViewAlarmReportVO> alarmList) {
		this.alarmList = alarmList;
	}

	public String getClientIPAddress() {
		return clientIPAddress;
	}

	public void setClientIPAddress(String clientIPAddress) {
		this.clientIPAddress = clientIPAddress;
	}

	public String getCheckinPort() {
		return checkinPort;
	}

	public void setCheckinPort(String checkinPort) {
		this.checkinPort = checkinPort;
	}

	public String getCheckinPortName() {
		return checkinPortName;
	}

	public void setCheckinPortName(String checkinPortName) {
		this.checkinPortName = checkinPortName;
	}

	public List<LsSystemDepartmentBO> getCheckoutPortList() {
		return checkoutPortList;
	}

	public void setCheckoutPortList(List<LsSystemDepartmentBO> checkoutPortList) {
		this.checkoutPortList = checkoutPortList;
	}

	public List<PatrolUserVO> getPatrolList() {
		return patrolList;
	}

	public void setPatrolList(List<PatrolUserVO> patrolList) {
		this.patrolList = patrolList;
	}

	public List<LsMonitorRouteAreaBO> getRouteAreaList() {
		return routeAreaList;
	}

	public void setRouteAreaList(List<LsMonitorRouteAreaBO> routeAreaList) {
		this.routeAreaList = routeAreaList;
	}

	public String[] getVehiclePlateNumber() {
		return vehiclePlateNumber;
	}

	public void setVehiclePlateNumber(String[] vehiclePlateNumber) {
		this.vehiclePlateNumber = vehiclePlateNumber;
	}

	public String[] getTrackingDeviceNumber() {
		return trackingDeviceNumber;
	}

	public void setTrackingDeviceNumber(String[] trackingDeviceNumber) {
		this.trackingDeviceNumber = trackingDeviceNumber;
	}

	public String[] getEsealNumber() {
		return esealNumber;
	}

	public void setEsealNumber(String[] esealNumber) {
		this.esealNumber = esealNumber;
	}

	public String[] getSensorNumber() {
		return sensorNumber;
	}

	public void setSensorNumber(String[] sensorNumber) {
		this.sensorNumber = sensorNumber;
	}

	public String[] getTrailerNumber() {
		return trailerNumber;
	}

	public void setTrailerNumber(String[] trailerNumber) {
		this.trailerNumber = trailerNumber;
	}

	public String[] getVehicleCountry() {
		return vehicleCountry;
	}

	public void setVehicleCountry(String[] vehicleCountry) {
		this.vehicleCountry = vehicleCountry;
	}

	public String[] getDriverName() {
		return driverName;
	}

	public void setDriverName(String[] driverName) {
		this.driverName = driverName;
	}

	public String[] getDriverCountry() {
		return driverCountry;
	}

	public void setDriverCountry(String[] driverCountry) {
		this.driverCountry = driverCountry;
	}

	public String[] getContainerNumber() {
		return containerNumber;
	}

	public void setContainerNumber(String[] containerNumber) {
		this.containerNumber = containerNumber;
	}

	public String[] getEsealOrder() {
		return esealOrder;
	}

	public void setEsealOrder(String[] esealOrder) {
		this.esealOrder = esealOrder;
	}

	public String[] getSensorOrder() {
		return sensorOrder;
	}

	public void setSensorOrder(String[] sensorOrder) {
		this.sensorOrder = sensorOrder;
	}

	public String[] getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String[] goodsType) {
		this.goodsType = goodsType;
	}

	public String[] getDriverIdCard() {
		return driverIdCard;
	}

	public void setDriverIdCard(String[] driverIdCard) {
		this.driverIdCard = driverIdCard;
	}

	public String[] getPatrolId() {
		return patrolId;
	}

	public void setPatrolId(String[] patrolId) {
		this.patrolId = patrolId;
	}

	public String getFileIndexVehicleNumMap() {
		return fileIndexVehicleNumMap;
	}

	public void setFileIndexVehicleNumMap(String fileIndexVehicleNumMap) {
		this.fileIndexVehicleNumMap = fileIndexVehicleNumMap;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getRootPathHttp() {
		return rootPathHttp;
	}

	public void setRootPathHttp(String rootPathHttp) {
		this.rootPathHttp = rootPathHttp;
	}

	public String getPhotoIndexVehicleNumMap() {
		return photoIndexVehicleNumMap;
	}

	public void setPhotoIndexVehicleNumMap(String photoIndexVehicleNumMap) {
		this.photoIndexVehicleNumMap = photoIndexVehicleNumMap;
	}

	public List<LsMonitorRouteAreaBO> getTargetZoonList() {
		return targetZoonList;
	}

	public void setTargetZoonList(List<LsMonitorRouteAreaBO> targetZoonList) {
		this.targetZoonList = targetZoonList;
	}

	public String[] getRiskStatus() {
		return riskStatus;
	}

	public void setRiskStatus(String[] riskStatus) {
		this.riskStatus = riskStatus;
	}

	public SystemModules getSystemModules() {
		return systemModules;
	}

	public void setSystemModules(SystemModules systemModules) {
		this.systemModules = systemModules;
	}

}
