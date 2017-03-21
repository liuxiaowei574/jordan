package com.nuctech.ls.center.map.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.gis.GisPoint;
import com.nuctech.ls.center.utils.DateJsonValueProcessor;
import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.monitor.LsMonitorRaPointBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.memcached.MemcachedUtil;
import com.nuctech.ls.model.redis.RedisClientTemplate;
import com.nuctech.ls.model.service.MonitorRaPointService;
import com.nuctech.ls.model.service.MonitorRouteAreaService;
import com.nuctech.ls.model.service.MonitorTripGpsService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.MonitorVehicleGpsService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.util.RouteAreaType;
import com.nuctech.ls.model.vo.monitor.MonitorRouteAreaRaPointVO;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import jcifs.util.Base64;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

@ParentPackage("json-default")
@Namespace("/monitorroutearea")
/**
 * 所有规划线路区域
 * 
 * @return
 */
public class MonitorRouteAreaAction extends LSBaseAction {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 1L;
	private String routeName;
	@Resource
	private MonitorTripGpsService monitorVehicleTripGpsService;
	@Resource
	private MonitorTripService monitorTripService;
	@Resource
	private MonitorRouteAreaService monitorRouteAreaService;
	@Resource
	private MonitorRaPointService monitorRaPointService;
	@Resource
	private SystemDepartmentService systemDepartmentService;
	@Resource
	private MonitorVehicleGpsService monitorVehicleGpsService;

	private String message;
	private String ids;// id集合
	private boolean success;// 结果
	private LsMonitorRouteAreaBO lsMonitorRouteAreaBO;
	private List<LsMonitorRaPointBO> lsMonitorRaPointBOs;
	private List<LsSystemDepartmentBO> lsSystemDepartmentBOs;
	private List<LsMonitorVehicleGpsBO> lsMonitorVehicleGpsBOs;// 某辆车轨迹表
	/**
	 * 所有线路区域
	 */
	private List<LsMonitorRouteAreaBO> lsMonitorRouteAreaBOs;
	@Resource
	public MemcachedUtil memcachedUtil;
	@Resource
	private RedisClientTemplate redisClientTemplate;
	@Resource
	private SystemModules systemModules;

	/**
	 * 线路区域及坐标集合
	 */
	private List<MonitorRouteAreaRaPointVO> monitorRouteAreaRaPointVOs;
	protected static final String DEFAULT_SORT_COLUMNS = "routeAreaName ASC";

	/**
	 * 界面初始化获取所有线路区域
	 * 
	 * @return
	 */

	@Action(value = "findAllRouteAreas", results = { @Result(name = "success", type = "json") })
	public void findAllRouteAreas() throws Exception {
		logger.info("查询路线区域获取到的routeAreaType组合的类型是：" + ids);
		this.lsMonitorRouteAreaBOs = this.monitorRouteAreaService.findAllRouteAreas(ids, routeName);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); // 排除关联死循环
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray lineitemArray = JSONArray.fromObject(this.lsMonitorRouteAreaBOs, jsonConfig);
		String result = JSONArray.fromObject(lineitemArray).toString();
		logger.info(String.format("查询所有线路或区域信息"));
		try {
			this.response.getWriter().println(result);
		} catch (IOException e) {
			message = e.getMessage();
			logger.error(message);
		}
	}

	/**
	 * 记录规划路线或区域
	 * 
	 * @throws IOException
	 */
	@Action(value = "planRouteArea")
	public void planRouteArea() throws IOException {
		JSONObject resultJson = new JSONObject();
		// 每个口岸只能有一个监管区域
		if (Constant.ROUTEAREA_TYPE_JGQY.equals(routeAreaType)) {
			List<LsMonitorRouteAreaBO> monitorRouteAreaBOs = monitorRouteAreaService.findJGQYByPort(belongToPort);
			if (monitorRouteAreaBOs != null && monitorRouteAreaBOs.size() > 0) {
				resultJson.put("success", false);
				resultJson.put("message", getLocaleString("common.message.portHasMonitorArea"));
				try {
					response.getWriter().println(resultJson.toString());
				} catch (IOException e) {
					logger.error(e);
				}
				return;
			}
		}
		boolean successFlag = true;
		LsMonitorRouteAreaBO monitorRouteAreaBO = new LsMonitorRouteAreaBO();
		monitorRouteAreaBO.setRouteAreaId(generatePrimaryKey());
		monitorRouteAreaBO.setRouteAreaName(routeAreaName);
		monitorRouteAreaBO.setBelongToPort(belongToPort);
		monitorRouteAreaBO.setStartId(startId);
		monitorRouteAreaBO.setEndId(endId);
		if (NuctechUtil.isNull(routeDistance)) {
			routeDistance = Double.toString(0);
		}
		monitorRouteAreaBO.setRouteDistance(BigDecimal.valueOf(Double.valueOf(routeDistance)));
		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
		if (sessionUser != null) {
			monitorRouteAreaBO.setCreateUser(sessionUser.getUserId());
		}
		monitorRouteAreaBO.setCreateTime(new Date());
		if (NuctechUtil.isNotNull(routeAreaType)) {
			monitorRouteAreaBO.setRouteAreaType(routeAreaType);
		} else {
			if (Constant.BUTTON_TYPE_LINE.equals(ids)) {
				monitorRouteAreaBO.setRouteAreaType(RouteAreaType.LINE.getText());
			} else if (Constant.BUTTON_TYPE_CDGL.equals(ids)) {
				monitorRouteAreaBO.setRouteAreaType(RouteAreaType.MONITOR_AREA.getText());
			} else {
				monitorRouteAreaBO.setRouteAreaType(RouteAreaType.LINE.getText());
			}
		}
		monitorRouteAreaBO.setRouteAreaColor(routeAreaColor);
		monitorRouteAreaBO.setRouteAreaStatus(routeAreaStatus);
		monitorRouteAreaBO.setRouteAreaBuffer(routeAreaBuffer);
		monitorRouteAreaBO.setRouteCost(routeCost);

		try {
			monitorRouteAreaService.addMonitorRouteArea(monitorRouteAreaBO);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String routeAreaId = monitorRouteAreaBO.getRouteAreaId();
		logger.info(String.format("add routeArea，区域线路编号：%s", routeAreaId));

		if (NuctechUtil.isNotNull(routeAreaPtCol)) {
			JSONArray json = JSONArray.fromObject(routeAreaPtCol); // 首先把字符串转成
																	// JSONArray
																	// 对象
			if (json.size() > 0) {
				int size = json.size();
				List<GisPoint> points = new ArrayList<>(size);
				for (int i = 0; i < size; i++) {
					JSONObject job = json.getJSONObject(i); // 遍历
															// jsonarray数组，把每一个对象转成
															// json对象
					LsMonitorRaPointBO bo = new LsMonitorRaPointBO();
					bo.setPointId(generatePrimaryKey());
					bo.setRouteAreaId(routeAreaId);
					bo.setGpsSeq(Long.valueOf(i));
					bo.setLatitude(job.get("lat").toString());
					bo.setLongitude(job.getString("lng"));
					points.add(new GisPoint(Double.parseDouble(job.getString("lng")),
							Double.parseDouble(job.get("lat").toString())));
					try {
						monitorRaPointService.addMonitorRaPoint(bo);
					} catch (Exception e) {
						message = e.getMessage();
						logger.error(message);
						successFlag = false;
					}
				}
				// 如果是target zoon，将该区域坐标存入缓存
				if (Constant.ROUTEAREA_TYPE_TARGET.equals(routeAreaType)) {
//					memcachedUtil.add(routeAreaId, points);
					JSONArray jsonArray = new JSONArray();
					jsonArray.addAll(points);
					redisClientTemplate.set(routeAreaId, jsonArray.toString());
				}
			}
			logger.info(String.format("添加规划路线或区域"));
		}
		resultJson.put("success", successFlag);
		if (!successFlag) {
			resultJson.put("message", message);
		}
		try {
			response.getWriter().println(resultJson.toString());
		} catch (IOException e) {
			logger.error(e);
		}
	}

	/**
	 * 根据线路或区域编号获取坐标集合
	 * 
	 * @param routeAreaId
	 * @return
	 */
	@Action(value = "editRouteArea", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String editRouteArea() {
		logger.info("编辑路线获取到的routeAreaId是：" + ids);
		try {
			SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
			String userId = sessionUser.getUserId();
			this.lsSystemDepartmentBOs = this.systemDepartmentService.findAllPortByUserId(userId);

			this.lsMonitorRouteAreaBO = this.monitorRouteAreaService.findMonitorRouteAreaById(ids);
			this.lsMonitorRaPointBOs = this.monitorRaPointService.findAllMonitorRaPointByRouteAreaId(ids);

			this.success = true;
		} catch (Exception e1) {
			message = e1.getMessage();
			logger.error(message);
			this.success = false;
		}
		return SUCCESS;
	}

	/**
	 * 修改规划路线或区域
	 * 
	 * @throws IOException
	 */
	@Action(value = "updateRouteArea", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public void updateRouteArea() throws IOException {
		JSONObject resultJson = new JSONObject();
		// 每个口岸只能有一个监管区域
		if (Constant.ROUTEAREA_TYPE_JGQY.equals(routeAreaType)) {
			List<LsMonitorRouteAreaBO> monitorRouteAreaBOs = monitorRouteAreaService.findJGQYByPort(belongToPort);
			if (monitorRouteAreaBOs != null && monitorRouteAreaBOs.size() > 0) {
				LsMonitorRouteAreaBO monitorRouteAreaBO = monitorRouteAreaBOs.get(0);
				if (!monitorRouteAreaBO.getRouteAreaId().equals(routeAreaId)) {
					resultJson.put("success", false);
					resultJson.put("message", getLocaleString("common.message.portHasMonitorArea"));
					try {
						response.getWriter().println(resultJson.toString());
					} catch (IOException e) {
						logger.error(e);
					}
					return;
				}
			}
		}
		boolean successFlag = true;
		LsMonitorRouteAreaBO monitorRouteAreaBO = new LsMonitorRouteAreaBO();
		monitorRouteAreaBO.setRouteAreaId(routeAreaId);
		monitorRouteAreaBO.setRouteAreaName(routeAreaName);
		monitorRouteAreaBO.setBelongToPort(belongToPort);
		monitorRouteAreaBO.setStartId(startId);
		monitorRouteAreaBO.setEndId(endId);
		if (NuctechUtil.isNotNull(routeDistance)) {
			monitorRouteAreaBO.setRouteDistance(BigDecimal.valueOf(Double.valueOf(routeDistance)));
		}

		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
		if (sessionUser != null) {
			monitorRouteAreaBO.setUpdateUser(sessionUser.getUserId());
		}
		monitorRouteAreaBO.setUpdateTime(new Date());
		if (NuctechUtil.isNotNull(routeAreaType)) {
			monitorRouteAreaBO.setRouteAreaType(routeAreaType);
		} else {
			if (Constant.BUTTON_TYPE_LINE.equals(ids)) {
				monitorRouteAreaBO.setRouteAreaType(RouteAreaType.LINE.getText());
			} else if (Constant.BUTTON_TYPE_CDGL.equals(ids)) {
				monitorRouteAreaBO.setRouteAreaType(RouteAreaType.MONITOR_AREA.getText());
			} else {
				monitorRouteAreaBO.setRouteAreaType(RouteAreaType.LINE.getText());
			}
		}
		monitorRouteAreaBO.setRouteAreaColor(routeAreaColor);
		monitorRouteAreaBO.setRouteAreaStatus(routeAreaStatus);
		monitorRouteAreaBO.setRouteAreaBuffer(routeAreaBuffer);
		monitorRouteAreaBO.setRouteCost(routeCost);
		logger.info(String.format("update routeArea，区域线路编号：%s", monitorRouteAreaBO.getRouteAreaId()));
		try {
			monitorRouteAreaService.updateaddMonitorRouteArea(monitorRouteAreaBO);
			if (NuctechUtil.isNotNull(routeAreaPtCol)) {
				JSONArray json = JSONArray.fromObject(routeAreaPtCol); // 首先把字符串转成
																		// JSONArray
																		// 对象
				if (json.size() > 0) {
					// 删除原有的Point数据
					monitorRaPointService.deleteMonitorRaPoint(routeAreaId);

					int size = json.size();
					List<GisPoint> points = new ArrayList<>(size);
					for (int i = 0; i < size; i++) {
						JSONObject job = json.getJSONObject(i); // 遍历 jsonarray
																// 数组，把每一个对象转成
																// json 对象
						LsMonitorRaPointBO bo = new LsMonitorRaPointBO();
						bo.setPointId(generatePrimaryKey());
						bo.setRouteAreaId(monitorRouteAreaBO.getRouteAreaId());
						bo.setGpsSeq(Long.valueOf(i));
						bo.setLatitude(job.get("lat").toString());
						bo.setLongitude(job.getString("lng"));
						points.add(new GisPoint(Double.parseDouble(job.getString("lng")),
								Double.parseDouble(job.get("lat").toString())));
						monitorRaPointService.addMonitorRaPoint(bo);
					}
					logger.info(String.format("添加规划路线或区域"));
					// 如果是target zoon，将该区域坐标存入缓存
					if(systemModules.isAreaOn()) {
						if (Constant.ROUTEAREA_TYPE_TARGET.equals(routeAreaType)) {
//							memcachedUtil.replace(routeAreaId, points);
							JSONArray jsonArray = new JSONArray();
							jsonArray.addAll(points);
							redisClientTemplate.set(routeAreaId, jsonArray.toString());
						}
					}
				}
			}
		} catch (Exception e) {
			message = e.getMessage();
			logger.error(message);
			successFlag = false;
		}
		resultJson.put("success", successFlag);
		if (!successFlag) {
			resultJson.put("message", message);
		}
		try {
			response.getWriter().println(resultJson.toString());
		} catch (IOException e) {
			logger.error(e);
		}
	}

	/**
	 * 根据实际行驶路线保存为规划路线
	 * 
	 * @return
	 */
	@Action(value = "saveRaPointByVehicle", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String saveRaPointByVehicle() throws Exception {
		LsMonitorTripBO lsMonitorTripBO = this.monitorTripService.findById(tripId);
		this.lsMonitorVehicleGpsBOs = this.monitorVehicleTripGpsService
				.findLsMonitorVehicleGpsByEclockNum(trackingDeviceNumber, lsMonitorTripBO);
		// this.lsMonitorRaPointBOs =
		// this.monitorRaPointService.findAllMonitorRaPointByRouteAreaId(routeAreaId);
		monitorRaPointService.deleteMonitorRaPoint(routeAreaId);
		logger.info(String.format("delete mointorRapoint By MonitorRouteId, MonitorRouteId is：%s", routeAreaId));
		if (null != this.lsMonitorVehicleGpsBOs && this.lsMonitorVehicleGpsBOs.size() > 0) {
			this.lsMonitorRaPointBOs = new ArrayList<LsMonitorRaPointBO>();
			for (int i = 0; i < this.lsMonitorVehicleGpsBOs.size(); i++) {
				LsMonitorVehicleGpsBO gpsBO = this.lsMonitorVehicleGpsBOs.get(i);
				// logger.info(String.format("add MonitorRaPoint, MonitorRouteId
				// is：%s", routeAreaId));
				LsMonitorRaPointBO bo = new LsMonitorRaPointBO();
				bo.setPointId(generatePrimaryKey());
				bo.setRouteAreaId(routeAreaId);
				bo.setGpsSeq(gpsBO.getGpsSeq());
				bo.setLatitude(gpsBO.getLatitude());
				bo.setLongitude(gpsBO.getLongitude());
				this.lsMonitorRaPointBOs.add(bo);
				// monitorRaPointService.addMonitorRaPoint(bo);
			}
			monitorRaPointService.batchUpdateOrDeleteByList(this.lsMonitorRaPointBOs);
			return SUCCESS;
		} else {
			message = "add addMonitorRaPoints failed";
			logger.error(message);
			return ERROR;
		}
	}

	/**
	 * 删除规划路线或区域
	 * 
	 * @return
	 */
	@Action(value = "delRouteArea", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String delRouteArea() throws Exception {
		logger.info("删除路线获取到的routeAreaIds是：" + ids);

		String routeAreaIds = getStrResult(ids);
		// 删除target zoon坐标缓存
		if (systemModules.isAreaOn()) {
			String[] strs = routeAreaIds.split(",");
			for (String routeAreaId : strs) {
				LsMonitorRouteAreaBO lsMonitorRouteAreaBO = monitorRouteAreaService
						.findMonitorRouteAreaById(routeAreaId);
				if (NuctechUtil.isNotNull(lsMonitorRouteAreaBO)
						&& Constant.ROUTEAREA_TYPE_TARGET.equals(lsMonitorRouteAreaBO.getRouteAreaType())) {
//					memcachedUtil.delete(lsMonitorRouteAreaBO.getRouteAreaId());
					redisClientTemplate.del(lsMonitorRouteAreaBO.getRouteAreaId());
				}
			}
		}

		int count = monitorRouteAreaService.delRouteAreaByRAIds(routeAreaIds);
		if (count > 0) {
			monitorRaPointService.deleteMonitorRaPointByRAIds(routeAreaIds);
			this.success = true;
		} else {
			this.success = false;
		}

		return SUCCESS;
	}

	/**
	 * 删除规划路线或区域
	 * 
	 * @return
	 */
	@Action(value = "checkRouteAreaByName", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String checkRouteAreaByName() throws Exception {
		this.monitorRouteAreaRaPointVOs = new ArrayList<MonitorRouteAreaRaPointVO>();
		logger.info("模糊查询线路区域的名称是：" + routeAreaName);
		if (!"".equalsIgnoreCase(routeAreaName)) {
			this.lsMonitorRouteAreaBOs = monitorRouteAreaService.findRouteAreaByName(routeAreaName);
			if (this.lsMonitorRouteAreaBOs.size() > 0) {
				for (Iterator iterator = lsMonitorRouteAreaBOs.iterator(); iterator.hasNext();) {
					LsMonitorRouteAreaBO lsMonitorRouteAreaBO = (LsMonitorRouteAreaBO) iterator.next();
					this.lsMonitorRaPointBOs = this.monitorRaPointService
							.findAllMonitorRaPointByRouteAreaId(lsMonitorRouteAreaBO.getRouteAreaId());
					MonitorRouteAreaRaPointVO routeAreaRaPointVO = new MonitorRouteAreaRaPointVO();
					routeAreaRaPointVO.setLsMonitorRouteAreaBO(lsMonitorRouteAreaBO);
					routeAreaRaPointVO.setLsMonitorRaPointBOs(lsMonitorRaPointBOs);
					this.monitorRouteAreaRaPointVOs.add(routeAreaRaPointVO);

				}
			}
			return SUCCESS;
		}
		return ERROR;
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

	/**
	 * 根据检入口岸和检出口岸查询对应路线
	 * 
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "findRouteByCheckinoutPort", results = { @Result(name = "success", type = "json") })
	public String findRouteByCheckinoutPort() throws IOException {
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
		JSONObject retJson = monitorRouteAreaService.findRouteAreas(pageQuery, null, false);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(retJson.toString());
		out.flush();
		out.close();
		return SUCCESS;
	}

	@Action(value = "simulateVehicleTrack", results = { @Result(name = "success", type = "json") })
	public void simulateVehicleTrack() {
		if (NuctechUtil.isNotNull(routeAreaPtCol)) {
			JSONArray json = JSONArray.fromObject(routeAreaPtCol); // 首先把字符串转成
																	// JSONArray
																	// 对象
			if (json.size() > 0) {
				// 删除原有的Point数据

				for (int i = 0; i < json.size(); i++) {
					JSONObject job = json.getJSONObject(i); // 遍历 jsonarray
															// 数组，把每一个对象转成 json
															// 对象
					LsMonitorVehicleGpsBO bo = new LsMonitorVehicleGpsBO();
					bo.setGpsId(generatePrimaryKey());
					bo.setTripId("");
					bo.setLocationType("0");
					bo.setGpsSeq(Long.parseLong(i + ""));
					bo.setTrackingDeviceNumber("");
					bo.setLocationTime(new Date());
					bo.setLocationStatus("11000");
					bo.setElockStatus("1");
					bo.setPoleStatus("1");
					bo.setBrokenStatus("0");
					bo.setEventUpload("0");
					bo.setLongitude(job.get("lng").toString());
					bo.setLatitude(job.get("lat").toString());
					bo.setAltitude(String.valueOf(Math.floor(1000)).toString());
					bo.setElockSpeed(String.valueOf(Math.floor(10)).toString());
					bo.setDirection(String.valueOf(Math.floor(360)).toString());
					bo.setElectricityValue("418");
					bo.setRelatedDevice("[0, 1, 2, 3, 4, 5, 0, 0, 0, 0, 0, 0]");
					bo.setCreateTime(new Date());
					// monitorVehicleGpsService.saveMonitorVehicleGps(bo,
					// tableName);
				}

				// logger.info(String.format("添加规划路线或区域"));
				// this.response.getWriter().println(SUCCESS);
				// return SUCCESS;
			}
		}
	}

	/**
	 * 将base64编码转换成PDF
	 * 
	 * @param base64sString
	 *            1.使用BASE64Decoder对编码的字符串解码成字节数组
	 *            2.使用底层输入流ByteArrayInputStream对象从字节数组中获取数据；
	 *            3.建立从底层输入流中读取数据的BufferedInputStream缓冲输出流对象；
	 *            4.使用BufferedOutputStream和FileOutputSteam输出数据到指定的文件中
	 */
	/**
	 * 根据检入口岸和检出口岸查询对应路线
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action(value = "imageToPDF", results = { @Result(name = "success", type = "json") })
	public String imageToPDF() throws IOException {
		base64StringToPDF();
		return SUCCESS;
	}

	private String base64sString;

	public String getBase64sString() {
		return base64sString;
	}

	public void setBase64sString(String base64sString) {
		this.base64sString = base64sString;
	}

	private void base64StringToPDF() throws IOException {

		try {
			File file = new File("d:/test.jpg");
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			out.write(Base64.decode(base64sString));
			out.close();
			logger.info("保存Base64到文件：" + file.getAbsolutePath());
		} catch (FileNotFoundException e) {
			logger.error("保存图像出错！", e);
		} catch (IOException e) {
			logger.error("保存图像出错！", e);
		}
	}

	private String tripId;
	private String trackingDeviceNumber;

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	/**
	 * 传递点json值
	 */
	private String pointJson;

	public String getPointJson() {
		return pointJson;
	}

	public void setPointJson(String pointJson) {
		this.pointJson = pointJson;
	}

	/* 路线区域主键 */
	private String routeAreaId;

	/* 路线区域名称 */
	private String routeAreaName;

	/*
	 * 路线区域类型 0-路线，1-安全区域，2-危险区域，3-监管区域，4-区域划分
	 */
	private String routeAreaType;

	/*
	 * 所属节点 每条路线或区域所属于的口岸 区划除外
	 */
	private String belongToPort;

	/* 创建人 */
	private String createUser;

	/* 更新人员 */
	private String updateUser;

	/*
	 * 路线区域状态 0-有效，1-无效
	 */
	private String routeAreaStatus;

	/* 缓冲区 */
	private String routeAreaBuffer;

	/* 路线用时 */
	private String routeCost;

	private String startId;

	private String endId;

	private String routeDistance;

	private String routeAreaColor;

	public String getRouteAreaColor() {
		return routeAreaColor;
	}

	public void setRouteAreaColor(String routeAreaColor) {
		this.routeAreaColor = routeAreaColor;
	}

	public String getRouteAreaId() {
		return routeAreaId;
	}

	public void setRouteAreaId(String routeAreaId) {
		this.routeAreaId = routeAreaId;
	}

	private String routeAreaPtCol;

	public String getRouteAreaName() {
		return routeAreaName;
	}

	public void setRouteAreaName(String routeAreaName) {
		this.routeAreaName = routeAreaName;
	}

	public String getRouteAreaType() {
		return routeAreaType;
	}

	public void setRouteAreaType(String routeAreaType) {
		this.routeAreaType = routeAreaType;
	}

	public String getBelongToPort() {
		return belongToPort;
	}

	public void setBelongToPort(String belongToPort) {
		this.belongToPort = belongToPort;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getRouteAreaStatus() {
		return routeAreaStatus;
	}

	public void setRouteAreaStatus(String routeAreaStatus) {
		this.routeAreaStatus = routeAreaStatus;
	}

	public String getRouteAreaPtCol() {
		return routeAreaPtCol;
	}

	public void setRouteAreaPtCol(String routeAreaPtCol) {
		this.routeAreaPtCol = routeAreaPtCol;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public boolean getSuccess() {
		return this.success;
	}

	public LsMonitorRouteAreaBO getLsMonitorRouteAreaBO() {
		return lsMonitorRouteAreaBO;
	}

	public void setLsMonitorRouteAreaBO(LsMonitorRouteAreaBO lsMonitorRouteAreaBO) {
		this.lsMonitorRouteAreaBO = lsMonitorRouteAreaBO;
	}

	public List<LsMonitorRaPointBO> getLsMonitorRaPointBOs() {
		return lsMonitorRaPointBOs;
	}

	public void setLsMonitorRaPointBOs(List<LsMonitorRaPointBO> lsMonitorRaPointBOs) {
		this.lsMonitorRaPointBOs = lsMonitorRaPointBOs;
	}

	public String getRouteAreaBuffer() {
		return routeAreaBuffer;
	}

	public void setRouteAreaBuffer(String routeAreaBuffer) {
		this.routeAreaBuffer = routeAreaBuffer;
	}

	public String getRouteCost() {
		return routeCost;
	}

	public void setRouteCost(String routeCost) {
		this.routeCost = routeCost;
	}

	public List<MonitorRouteAreaRaPointVO> getMonitorRouteAreaRaPointVOs() {
		return monitorRouteAreaRaPointVOs;
	}

	public void setMonitorRouteAreaRaPointVOs(List<MonitorRouteAreaRaPointVO> monitorRouteAreaRaPointVOs) {
		this.monitorRouteAreaRaPointVOs = monitorRouteAreaRaPointVOs;
	}

	public List<LsSystemDepartmentBO> getLsSystemDepartmentBOs() {
		return lsSystemDepartmentBOs;
	}

	public void setLsSystemDepartmentBOs(List<LsSystemDepartmentBO> lsSystemDepartmentBOs) {
		this.lsSystemDepartmentBOs = lsSystemDepartmentBOs;
	}

	public String getTrackingDeviceNumber() {
		return trackingDeviceNumber;
	}

	public void setTrackingDeviceNumber(String trackingDeviceNumber) {
		this.trackingDeviceNumber = trackingDeviceNumber;
	}

	public String getStartId() {
		return startId;
	}

	public void setStartId(String startId) {
		this.startId = startId;
	}

	public String getEndId() {
		return endId;
	}

	public void setEndId(String endId) {
		this.endId = endId;
	}

	public String getRouteDistance() {
		return routeDistance;
	}

	public void setRouteDistance(String routeDistance) {
		this.routeDistance = routeDistance;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
}
