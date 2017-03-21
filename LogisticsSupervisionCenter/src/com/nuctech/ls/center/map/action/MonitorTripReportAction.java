package com.nuctech.ls.center.map.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;

import com.nuctech.ls.center.utils.DocumentHandler;
import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.service.CommonDriverService;
import com.nuctech.ls.model.service.CommonPatrolService;
import com.nuctech.ls.model.service.CommonVehicleService;
import com.nuctech.ls.model.service.MonitorAlarmService;
import com.nuctech.ls.model.service.MonitorRouteAreaService;
import com.nuctech.ls.model.service.MonitorTripReportService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.vo.monitor.CommonVehicleDriverVO;
import com.nuctech.ls.model.vo.monitor.MonitorTripVehicleVO;
import com.nuctech.ls.model.vo.monitor.ViewAlarmReportVO;
import com.nuctech.util.Constant;
import com.nuctech.util.DateUtils;
import com.nuctech.util.MessageResourceUtil;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;

/**
 * 行程监管信息Action
 * 
 * @author liushaowei
 *
 */
@Namespace("/monitortripreport")
public class MonitorTripReportAction extends LSBaseAction {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 1L;
	/**
	 * 列表排序字段
	 */
	private static final String DEFAULT_SORT_COLUMNS_WITH_ALIAS = "t.checkinTime DESC";

	/**
	 * 行程监管信息Service
	 */
	@Resource
	private MonitorTripReportService monitorTripReportService;

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
	 * 组织机构 Service
	 */
	@Resource
	private SystemDepartmentService systemDepartmentService;

	/**
	 * 系统用户相关 Service
	 */
	@Resource
	private SystemUserService systemUserService;

	/**
	 * 报警信息Service
	 */
	@Resource
	private MonitorAlarmService monitorAlarmService;
	/**
	 * 司机信息Service
	 */
	@Resource
	private CommonDriverService commonDriverService;

	@Resource
	private MonitorRouteAreaService monitorRouteAreaService;
	@Resource
	private CommonPatrolService commonPatrolService;
	@Resource
	private SystemModules systemModules;

	/**
	 * 行程信息及车辆信息
	 */
	private MonitorTripVehicleVO tripVehicleVO;

	private List<ViewAlarmReportVO> alarmList;

	/**
	 * 跳转到列表页面
	 * 
	 * @return
	 */
	@Action(value = "toList", results = { @Result(name = "success", location = "/monitor/tripReport/list.jsp") })
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
		
		String sortname = request.getParameter("sort");
		if ("vehiclePlateNumber".equals(sortname)) {
			pageQuery = this.newPageQuery("v.vehiclePlateNumber asc");
		} else if ("trackingDeviceNumber".equals(sortname)) {
			pageQuery = this.newPageQuery("v.trackingDeviceNumber asc");
		} else if ("esealNumber".equals(sortname)) {
			pageQuery = this.newPageQuery("v.esealNumber asc");
		} else if ("sensorNumber".equals(sortname)) {
			pageQuery = this.newPageQuery("v.sensorNumber asc");
		}
		
		JSONObject retJson = monitorTripReportService.findTripReportListData(pageQuery);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(retJson.toString());
		out.flush();
		out.close();
		return null;
	}

	/**
	 * 跳转到详情页面
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "toDetail", results = { @Result(name = "success", location = "/monitor/tripReport/detail.jsp"),
			@Result(name = "modal", location = "/include/tripDetail.jsp") })
	public String toDetail() {
		String tripId = request.getParameter("s_tripId");
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
				if (NuctechUtil.isNotNull(tripVehicleVO.getCheckinUser())) {
					tripVehicleVO.setCheckinUserName(
							systemUserService.findById(tripVehicleVO.getCheckinUser()).getUserName());
				}
				if (NuctechUtil.isNotNull(tripVehicleVO.getCheckoutUser())) {
					tripVehicleVO.setCheckoutUserName(
							systemUserService.findById(tripVehicleVO.getCheckoutUser()).getUserName());
				}
				if (NuctechUtil.isNotNull(tripVehicleVO.getRouteId())) {
					tripVehicleVO.setRouteName(monitorRouteAreaService
							.findMonitorRouteAreaById(tripVehicleVO.getRouteId()).getRouteAreaName());
					tripVehicleVO.setRouteCost(monitorRouteAreaService
							.findMonitorRouteAreaById(tripVehicleVO.getRouteId()).getRouteCost());
				}
				if (systemModules.isAreaOn()) {
					if (NuctechUtil.isNotNull(tripVehicleVO.getTargetZoonId())) {
						tripVehicleVO.setTargetZoonName(monitorRouteAreaService
								.findMonitorRouteAreaById(tripVehicleVO.getTargetZoonId()).getRouteAreaName());
					}
				}
				pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
				pageQuery.setPageSize(Constant.MAX_PAGE_SIZE);

				List<CommonVehicleDriverVO> commonVehicleDriverList = monitorTripService
						.findAllVehicleDriverByPageQuery(pageQuery, tripVehicleVO.getTripId());
				tripVehicleVO.setCommonVehicleDriverList(commonVehicleDriverList);
				copyProperties(tripVehicleVO, commonVehicleDriverList);

				alarmList = monitorAlarmService.findListByTripId(pageQuery);
			}
			String msgType = request.getParameter("msgType");
			if ("modal".equals(msgType)) {
				return "modal";
			}
			return SUCCESS;
		} else {
			return ERROR;
		}
	}

	/**
	 * 查找行程的巡逻队用户名和车牌号
	 * 
	 * @param tripVehicleVO
	 * @return
	 */
	@SuppressWarnings("unused")
	private String findPatrolNames(MonitorTripVehicleVO tripVehicleVO) {
		List<LsCommonPatrolBO> commonPatrolBOs = commonPatrolService.findAllByTripId(tripVehicleVO.getTripId());
		if (NuctechUtil.isNotNull(commonPatrolBOs) && commonPatrolBOs.size() > 0) {
			StringBuffer patrolNames = new StringBuffer();
			for (LsCommonPatrolBO commonPatrolBO : commonPatrolBOs) {
				LsSystemUserBO systemUser = systemUserService.findById(commonPatrolBO.getPotralUser());
				if (NuctechUtil.isNotNull(systemUser)) {
					String s = systemUser.getUserName() + "/" + commonPatrolBO.getVehiclePlateNumber(); // 巡逻队用户名/车牌号
					patrolNames.append(s + ",");
				}
			}
			if (patrolNames.length() > 0) {
				patrolNames.deleteCharAt(patrolNames.length() - 1);
				return patrolNames.toString();
			}
		}
		return null;
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
	 * 导出行程信息到Word
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "exportWord")
	public String exportWord() throws Exception {
		try {
			// 查询
			String tripId = request.getParameter("s_tripId");
			logger.info("查询行程信息并得到map数据");
			Map<String, Object> dataMap = getTripAndAlarmInfo(tripId);

			// 获得临时目录
			String tempDir = System.getProperty("java.io.tmpdir");
			String fileName = UUID.randomUUID().toString() + ".doc";
			String filePath = tempDir + fileName;
			// 写入word
			logger.info("开始写入文件" + filePath);
			DocumentHandler mdoc = new DocumentHandler();
			mdoc.createDoc(dataMap, filePath, "/com/nuctech/ls/center/utils/template/tripReport.ftl");

			String outputName = DateUtils.date2String(new Date(), "yyyyMMddHHmmss") + ".doc";
			response.reset();// 清空输出流
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + outputName);
			// 取得输出流
			OutputStream os = response.getOutputStream();
			byte[] b = new byte[1024 * 10];
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));
			int len = 0;
			while ((len = in.read(b)) > -1) {
				os.write(b, 0, len);
				os.flush();
			}
			os.flush();
			if (in != null) {
				in.close();
			}

			logger.info("文件写入完毕" + filePath);
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
				logger.info("删除临时文件" + filePath);
			}
		} catch (Exception e) {
			logger.info("导出Word异常！" + e);
			throw e;
		}
		return null;
	}

	/**
	 * 查询行程信息并组织map数据
	 * 
	 * @param tripId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getTripAndAlarmInfo(String tripId) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		if (!NuctechUtil.isNull(tripId)) {
			// 标题
			dataMap.put("title", MessageResourceUtil.getMessageInfo("trip.report.title"));
			dataMap.put("baseInfo", MessageResourceUtil.getMessageInfo("trip.report.basic"));
			dataMap.put("vehicleInfo", MessageResourceUtil.getMessageInfo("trip.report.vehicleInfo"));
			dataMap.put("checkinInfo", MessageResourceUtil.getMessageInfo("trip.report.checkinInfo"));
			dataMap.put("checkoutInfo", MessageResourceUtil.getMessageInfo("trip.report.checkoutInfo"));
			dataMap.put("alarmInfo", MessageResourceUtil.getMessageInfo("trip.report.alarmInfo"));

			tripVehicleVO = new MonitorTripVehicleVO();
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
				if (NuctechUtil.isNotNull(tripVehicleVO.getCheckinUser())) {
					tripVehicleVO.setCheckinUserName(
							systemUserService.findById(tripVehicleVO.getCheckinUser()).getUserName());
				}
				if (NuctechUtil.isNotNull(tripVehicleVO.getCheckoutUser())) {
					tripVehicleVO.setCheckoutUserName(
							systemUserService.findById(tripVehicleVO.getCheckoutUser()).getUserName());
				}

				pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
				alarmList = monitorAlarmService.findListByTripId(pageQuery);
				List<CommonVehicleDriverVO> commonVehicleDriverList = monitorTripService
						.findAllVehicleDriverByPageQuery(pageQuery, tripVehicleVO.getTripId());

				// 基本信息
				List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("tripIdLabel", MessageResourceUtil.getMessageInfo("trip.label.tripId"));
				map1.put("tripId", tripVehicleVO.getTripId());
				map1.put("declarationNumberLabel", MessageResourceUtil.getMessageInfo("trip.label.declarationNumber"));
				map1.put("declarationNumber", ifNull(tripVehicleVO.getDeclarationNumber(), ""));
				map1.put("routeNameLabel", MessageResourceUtil.getMessageInfo("trip.label.routeId"));
				map1.put("routeName", ifNull(tripVehicleVO.getRouteName(), ""));
				list1.add(map1);
				dataMap.put("table1", list1);

				// 车辆信息
				dataMap.put("serialNumLabel", MessageResourceUtil.getMessageInfo("trip.report.serialNumber"));
				dataMap.put("vPNumLabel", MessageResourceUtil.getMessageInfo("trip.label.vehiclePlateNumber"));
				dataMap.put("tDNumLabel", MessageResourceUtil.getMessageInfo("trip.label.trackingDeviceNumber"));
				dataMap.put("eNumLabel", MessageResourceUtil.getMessageInfo("trip.label.esealNumber"));
				dataMap.put("sNumLabel", MessageResourceUtil.getMessageInfo("trip.label.sensorNumber"));
				dataMap.put("vCountryLabel", MessageResourceUtil.getMessageInfo("trip.label.vehicleCountry"));
				dataMap.put("tNumLabel", MessageResourceUtil.getMessageInfo("trip.label.trailerNumber"));
				dataMap.put("dNameLabel", MessageResourceUtil.getMessageInfo("trip.label.driverName"));
				dataMap.put("dCountryLabel", MessageResourceUtil.getMessageInfo("trip.label.driverCountry"));
				dataMap.put("ctNumLabel", MessageResourceUtil.getMessageInfo("trip.label.containerNumber"));
				List<Map<String, Object>> list5 = new ArrayList<Map<String, Object>>();
				if (NuctechUtil.isNotNull(commonVehicleDriverList) && commonVehicleDriverList.size() > 0) {
					for (int i = 0, len = commonVehicleDriverList.size(); i < len; i++) {
						CommonVehicleDriverVO commonVehicleDriver = commonVehicleDriverList.get(i);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("serialNum1", i + 1);
						map.put("vehiclePlateNumber", ifNull(commonVehicleDriver.getVehiclePlateNumber(), ""));
						map.put("trackingDeviceNumber", ifNull(commonVehicleDriver.getTrackingDeviceNumber(), ""));
						map.put("esealNumber", ifNull(commonVehicleDriver.getEsealNumber(), ""));
						map.put("sensorNumber", ifNull(commonVehicleDriver.getSensorNumber(), ""));
						map.put("vehicleCountry", ifNull(commonVehicleDriver.getVehicleCountry(), ""));
						map.put("trailerNumber", ifNull(commonVehicleDriver.getTrailerNumber(), ""));
						map.put("driverName", ifNull(commonVehicleDriver.getDriverName(), ""));
						map.put("driverCountry", ifNull(commonVehicleDriver.getDriverCountry(), ""));
						map.put("containerNumber", ifNull(commonVehicleDriver.getContainerNumber(), ""));
						list5.add(map);
					}
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("serialNum", "");
					map.put("vehiclePlateNumber", "");
					map.put("vehicleCountry", "");
					map.put("trailerNumber", "");
					map.put("driverName", "");
					map.put("driverCountry", "");
					map.put("containerNumber", "");
					list5.add(map);
				}
				dataMap.put("table5", list5);

				// 检入信息
				List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("checkinTimeLabel", MessageResourceUtil.getMessageInfo("trip.label.checkinTime"));
				map2.put("checkinTime", ifNull(tripVehicleVO.getCheckinTime(), ""));
				map2.put("checkinPortNameLabel", MessageResourceUtil.getMessageInfo("trip.label.checkinPort"));
				map2.put("checkinPortName", ifNull(tripVehicleVO.getCheckinPortName(), ""));
				map2.put("checkinUserNameLabel", MessageResourceUtil.getMessageInfo("trip.label.checkinUser"));
				map2.put("checkinUserName", ifNull(tripVehicleVO.getCheckoutPortName(), ""));
				list2.add(map2);
				dataMap.put("table2", list2);

				// 检出信息
				List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
				Map<String, Object> map3 = new HashMap<String, Object>();
				map3.put("checkoutTimeLabel", MessageResourceUtil.getMessageInfo("trip.label.checkoutTime"));
				map3.put("checkoutTime", ifNull(tripVehicleVO.getCheckoutTime(), ""));
				map3.put("checkoutPortNameLabel", MessageResourceUtil.getMessageInfo("trip.label.checkoutPort"));
				map3.put("checkoutPortName", ifNull(tripVehicleVO.getCheckoutPortName(), ""));
				map3.put("checkoutUserNameLabel", MessageResourceUtil.getMessageInfo("trip.label.checkoutUser"));
				map3.put("checkoutUserName", ifNull(tripVehicleVO.getCheckoutUserName(), ""));
				map3.put("timeCostLabel", MessageResourceUtil.getMessageInfo("trip.label.timeCost"));
				map3.put("timeCost", ifNull(tripVehicleVO.getTimeCost(), ""));
				list3.add(map3);
				dataMap.put("table3", list3);

				// 报警信息
				dataMap.put("alarmTimeLabel", MessageResourceUtil.getMessageInfo("trip.report.alarmTime"));
				dataMap.put("receiveTimeLabel", MessageResourceUtil.getMessageInfo("alarm.label.receiveTime"));
				dataMap.put("userNameLabel", MessageResourceUtil.getMessageInfo("alarm.label.userName"));
				dataMap.put("alarmLongitudeLabel", MessageResourceUtil.getMessageInfo("trip.report.alarmLongitude"));
				dataMap.put("alarmLatitudeLabel", MessageResourceUtil.getMessageInfo("trip.report.alarmLatitude"));
				dataMap.put("alarmStatusLabel", MessageResourceUtil.getMessageInfo("alarm.label.alarmStatus"));
				dataMap.put("alarmLevelNameLabel", MessageResourceUtil.getMessageInfo("alarm.label.alarmLevelName"));
				dataMap.put("alarmTypeNameLabel", MessageResourceUtil.getMessageInfo("alarm.label.alarmTypeName"));
				List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
				if (NuctechUtil.isNotNull(alarmList) && alarmList.size() > 0) {
					for (int i = 0, len = alarmList.size(); i < len; i++) {
						ViewAlarmReportVO viewAlarm = alarmList.get(i);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("serialNum", i + 1);
						map.put("alarmTime", ifNull(viewAlarm.getAlarmTime(), ""));
						map.put("receiveTime", ifNull(viewAlarm.getReceiveTime(), ""));
						map.put("userName", ifNull(viewAlarm.getUserName(), ""));
						map.put("alarmLongitude", ifNull(viewAlarm.getAlarmLongitude(), ""));
						map.put("alarmLatitude", ifNull(viewAlarm.getAlarmLatitude(), ""));
						map.put("alarmStatus", ifNull(viewAlarm.getAlarmStatus(), ""));
						map.put("alarmLevelName", ifNull(viewAlarm.getAlarmLevelName(), ""));
						map.put("alarmTypeName", ifNull(viewAlarm.getAlarmTypeName(), ""));
						list4.add(map);
					}
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("serialNum", "");
					map.put("alarmTime", "");
					map.put("receiveTime", "");
					map.put("userName", "");
					map.put("alarmLongitude", "");
					map.put("alarmLatitude", "");
					map.put("alarmStatus", "");
					map.put("alarmLevelName", "");
					map.put("alarmTypeName", "");
					list4.add(map);
				}
				dataMap.put("table4", list4);
			}
		}
		return dataMap;
	}

	/**
	 * 将行程报告列表导出到excel
	 * 
	 * @throws Exception
	 */
	@Action(value = "exportExcel", results = { @Result(name = "success", type = "json") })
	public void exportExcel() throws Exception {
		// 行程状态
		String tripstatus0 = MessageResourceUtil.getMessageInfo("trip.report.label.tripStatus.toStart");
		String tripstatus1 = MessageResourceUtil.getMessageInfo("trip.report.label.tripStatus.started");
		String tripstatus2 = MessageResourceUtil.getMessageInfo("trip.report.label.tripStatus.toFinish");
		String tripstatus3 = MessageResourceUtil.getMessageInfo("trip.report.label.tripStatus.finished");
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 在浏览器下方显示下载
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-download");
		String filename = MessageResourceUtil.getMessageInfo("trip.report.list.search") + ".xls";
		filename = URLEncoder.encode(filename, "utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);

		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(MessageResourceUtil.getMessageInfo("trip.report.list.title"));
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		HSSFCell cell = row.createCell((int) 0);
		cell.setCellValue(MessageResourceUtil.getMessageInfo("risk.table.lpnId"));
		cell.setCellStyle(style);
		cell = row.createCell((int) 1);
		cell.setCellValue(MessageResourceUtil.getMessageInfo("monitorTrip.label.declarationNumber"));
		cell.setCellStyle(style);
		cell = row.createCell((int) 2);
		cell.setCellValue(MessageResourceUtil.getMessageInfo("trip.label.trackingDeviceNumber"));
		cell.setCellStyle(style);
		cell = row.createCell((int) 3);
		cell.setCellValue(MessageResourceUtil.getMessageInfo("warehouseEsealBO.esealNumber"));
		cell.setCellStyle(style);
		cell = row.createCell((int) 4);
		cell.setCellValue(MessageResourceUtil.getMessageInfo("WarehouseSensor.sensorNumber"));
		cell.setCellStyle(style);
		cell = row.createCell((int) 5);
		cell.setCellValue(MessageResourceUtil.getMessageInfo("trip.report.label.checkinUser"));
		cell.setCellStyle(style);
		cell = row.createCell((int) 6);
		cell.setCellValue(MessageResourceUtil.getMessageInfo("trip.report.label.checkinTime"));
		cell.setCellStyle(style);
		cell = row.createCell((int) 7);
		cell.setCellValue(MessageResourceUtil.getMessageInfo("trip.report.label.checkoutUser"));
		cell.setCellStyle(style);
		cell = row.createCell((int) 8);
		cell.setCellValue(MessageResourceUtil.getMessageInfo("trip.report.label.checkoutTime"));
		cell.setCellStyle(style);
		cell = row.createCell((int) 9);
		cell.setCellValue(MessageResourceUtil.getMessageInfo("trip.report.label.tripStatus"));
		cell.setCellStyle(style);
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		@SuppressWarnings("rawtypes")
		List list = monitorTripReportService.findStatisticData();
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((int) i + 1);
			HashMap<String, String> oHashMap = (HashMap<String, String>) list.get(i);

			row.createCell((int) 0).setCellValue(oHashMap.get("VEHICLE_PLATE_NUMBER"));
			row.createCell((int) 1).setCellValue(oHashMap.get("DECLARATION_NUMBER"));
			row.createCell((int) 2).setCellValue(oHashMap.get("TRACKING_DEVICE_NUMBER"));
			row.createCell((int) 3).setCellValue(oHashMap.get("ESEAL_NUMBER"));
			row.createCell((int) 4).setCellValue(oHashMap.get("SENSOR_NUMBER"));
			row.createCell((int) 5).setCellValue(oHashMap.get("CHECKIN_USER"));
			row.createCell((int) 6).setCellValue(oHashMap.get("CHECKIN_TIME"));
			row.createCell((int) 7).setCellValue(oHashMap.get("CHECKOUT_USER"));
			row.createCell((int) 8).setCellValue(oHashMap.get("CHECKOUT_TIME"));
			// 将数据库取出的数字转化为文字
			if (oHashMap.get("TRIP_STATUS").equals("0")) {
				row.createCell((int) 9).setCellValue(tripstatus0);
			}
			if (oHashMap.get("TRIP_STATUS").equals("1")) {
				row.createCell((int) 9).setCellValue(tripstatus1);
			}
			if (oHashMap.get("TRIP_STATUS").equals("2")) {
				row.createCell((int) 9).setCellValue(tripstatus2);
			}
			if (oHashMap.get("TRIP_STATUS").equals("3")) {
				row.createCell((int) 9).setCellValue(tripstatus3);
			}
			// 第四步，创建单元格，并设置值
			cell = row.createCell((int) 10);
		}
		// 第六步，将文件存到指定位置
		try {
			OutputStream outputStream = response.getOutputStream();
			wb.write(outputStream);
			outputStream.close();

			// 路径写死了；
			// FileOutputStream fout = new FileOutputStream("D:/students.xls");
			// wb.write(fout);
			// fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String ifNull(String value, String defaultValue) {
		return NuctechUtil.isNull(value) ? defaultValue : value;
	}

	protected Object ifNull(Object value, String defaultValue) {
		return NuctechUtil.isNull(value) ? defaultValue : value;
	}

	public List<ViewAlarmReportVO> getAlarmList() {
		return alarmList;
	}

	public void setAlarmList(List<ViewAlarmReportVO> alarmList) {
		this.alarmList = alarmList;
	}

	public MonitorTripVehicleVO getTripVehicleVO() {
		return tripVehicleVO;
	}

	public void setTripVehicleVO(MonitorTripVehicleVO tripVehicleVO) {
		this.tripVehicleVO = tripVehicleVO;
	}

	public SystemModules getSystemModules() {
		return systemModules;
	}

	public void setSystemModules(SystemModules systemModules) {
		this.systemModules = systemModules;
	}

}
