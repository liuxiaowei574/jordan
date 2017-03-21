package com.nuctech.ls.center.map.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Id;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.dm.LsDmAlarmTypeBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorAlarmBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.redis.RedisClientTemplate;
import com.nuctech.ls.model.service.AlarmLevelModifyService;
import com.nuctech.ls.model.service.CommonPatrolService;
import com.nuctech.ls.model.service.CommonVehicleService;
import com.nuctech.ls.model.service.MonitorAlarmDealService;
import com.nuctech.ls.model.service.MonitorAlarmService;
import com.nuctech.ls.model.service.MonitorRouteAreaService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.MonitorVehicleStatusService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.util.AlarmDealType;
import com.nuctech.ls.model.util.AlarmManualType;
import com.nuctech.ls.model.vo.monitor.CommonVehicleDriverVO;
import com.nuctech.ls.model.vo.monitor.MonitorAlarmDealVO;
import com.nuctech.ls.model.vo.monitor.MonitorTripVehicleVO;
import com.nuctech.ls.model.vo.monitor.ViewAlarmReportVO;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.CommonStringUtil;
import com.nuctech.util.Constant;
import com.nuctech.util.MessageResourceUtil;
import com.nuctech.util.NuctechUtil;
import com.nuctech.util.RedisKey;

import net.sf.json.JSONObject;

/**
 * 报警信息Action
 * 
 * @author liushaowei
 *
 */
@Namespace("/monitoralarm")
public class MonitorAlarmAction extends LSBaseAction {

    private static final long serialVersionUID = 1L;

    private static final String ALARM_STATUS_DEALED = "2";// 已处理
    private static final String ALARM_STATUS_UNDEAL = "0";// 未处理
    /**
     * 列表排序字段
     */
    private static final String DEFAULT_SORT_COLUMNS_WITH_ALIAS = "t.riskStatus DESC, t.alarmTime ASC, t.alarmLevelId DESC"; // 按风险级别由高到低，再按报警时间由远及近，最后按报警级别由高到低

    private LsMonitorAlarmBO lsMonitorAlarmBO;

    /**
     * 报警信息Service
     */
    @Resource
    private MonitorAlarmService monitorAlarmService;

    /**
     * 报警处理信息Service
     */
    @Resource
    private MonitorAlarmDealService monitorAlarmDealService;

    /**
     * 行程信息及车辆信息
     */
    private MonitorTripVehicleVO tripVehicleVO;
    // 车牌号
    private String vehiclePlateNumber;
    // 车辆主键
    private String vehicleId;

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
     * 行程信息Service
     */
    @Resource
    private MonitorTripService monitorTripService;
    @Resource
    private AlarmLevelModifyService alarmTypeService;

    /**
     * 报警信息处理集合
     */
    private List<MonitorAlarmDealVO> alarmDealList;

    @Resource
    private MonitorVehicleStatusService monitorVehicleStatusService;
    @Resource
    private MonitorRouteAreaService monitorRouteAreaService;
    @Resource
    private CommonPatrolService commonPatrolService;
    @Resource
    private SystemModules systemModules;
    @Resource
	private RedisClientTemplate redisClientTemplate;

    /**
     * 跳转到列表页面
     * 
     * @return
     */
    @Action(value = "toList", results = { @Result(name = "success", location = "/monitor/alarm/list.jsp") })
    public String toList() {
        return SUCCESS;
    }

    /**
     * 列表查询
     * 
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked" })
    @Action(value = "list")
    public String list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
        JSONObject retJson = monitorAlarmService.findAlarmList(pageQuery);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
        return null;
    }

    @Action(value = "addAlarmModalShow",
            results = { @Result(name = "success", location = "/monitor/alarm/addAlarm.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String addAlarmModalShow() throws Exception {
        this.lsMonitorAlarmBO = new LsMonitorAlarmBO();
        // 根据车牌号，查到车辆表获取车辆id
        String tripId = request.getParameter("tripId");
        String vehiclePlateNumber = request.getParameter("vehiclePlateNumber");
        LsCommonVehicleBO lsCommonVehicleBo = commonVehicleService.findByVehiclePlateNumber(vehiclePlateNumber, tripId);
        request.setAttribute("vehicleId", lsCommonVehicleBo.getVehicleId());
        return SUCCESS;
    }

    /**
     * 跳转到详情页面
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    @Action(value = "toDetail", results = { @Result(name = "success", location = "/monitor/alarm/detail.jsp") })
    public String toDetail() {
        String tripId = request.getParameter("s_tripId");
        String alarmId = request.getParameter("s_alarmId");
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
                // String patrolNames = findPatrolNames(tripVehicleVO);
                // if(NuctechUtil.isNotNull(patrolNames)) {
                // tripVehicleVO.setPatrolName(patrolNames);
                // }
                pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
                pageQuery.setPageSize(Constant.MAX_PAGE_SIZE);

                List<CommonVehicleDriverVO> commonVehicleDriverList = monitorTripService
                        .findAllVehicleDriverByPageQuery(pageQuery, tripVehicleVO.getTripId());
                tripVehicleVO.setCommonVehicleDriverList(commonVehicleDriverList);

                HashMap<String, String> orderby = new HashMap<>();
                orderby.put("receiveTime", "asc");
                alarmDealList = monitorAlarmDealService.findAllAlarmDealByAlarmId(alarmId, orderby);
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

    @Action(value = "addAlarmByManul")
    public String addAlarmByManul() throws Exception {
        if (this.lsMonitorAlarmBO != null) {
            JSONObject json = new JSONObject();
            try {
                this.lsMonitorAlarmBO.setAlarmId(generatePrimaryKey());
                if (ALARM_STATUS_UNDEAL.equalsIgnoreCase(AlarmDealType.Undeal.getText())) {
                    this.lsMonitorAlarmBO.setAlarmStatus(ALARM_STATUS_UNDEAL);
                } else if (ALARM_STATUS_DEALED.equalsIgnoreCase(AlarmDealType.Dealt.getText())) {
                    this.lsMonitorAlarmBO.setAlarmStatus(ALARM_STATUS_DEALED);
                }
                this.lsMonitorAlarmBO.setAlarmTime(new Date());
                SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                this.lsMonitorAlarmBO.setCreateUser(sessionUser.getUserId());
                this.lsMonitorAlarmBO.setIsManual(AlarmManualType.Manual.getAlarmType());
                this.lsMonitorAlarmBO.setReceiveTime(new Date());
                // this.lsMonitorAlarmBO.setUserId(sessionUser.getUserId());

                this.lsMonitorAlarmBO.setVehicleId(vehicleId);

                monitorAlarmService.addAlarm(lsMonitorAlarmBO);

                if (NuctechUtil.isNotNull(lsMonitorAlarmBO.getTripId())) {
                    LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO = monitorVehicleStatusService
                            .findOneByTripId(lsMonitorAlarmBO.getTripId(), lsMonitorAlarmBO.getVehicleId());
                    increaseRiskStatus1(lsMonitorVehicleStatusBO, lsMonitorAlarmBO.getAlarmTypeId());
                }

                // 手动添加报警，提高LsCommonVehicleBO表中的riskStatus的等级
                LsCommonVehicleBO lsCommonVehicleBO = null;
                if (NuctechUtil.isNotNull(lsMonitorAlarmBO.getTripId())) {
                	lsCommonVehicleBO = commonVehicleService.findById(lsMonitorAlarmBO.getVehicleId());
                    increaseRiskStatus(lsCommonVehicleBO, lsMonitorAlarmBO.getAlarmTypeId());
                }

                ViewAlarmReportVO alarmReportVO = new ViewAlarmReportVO();
                BeanUtils.copyProperties(lsMonitorAlarmBO, alarmReportVO);
                LsDmAlarmTypeBO alarmTypeBO = alarmTypeService.find(lsMonitorAlarmBO.getAlarmTypeId());
                if (alarmTypeBO != null) {
                    alarmReportVO.setAlarmLevelId(alarmTypeBO.getAlarmLevelId());
                }
                
                //将报警信息添加进缓存
                if(NuctechUtil.isNotNull(lsCommonVehicleBO)) {
                	pushAlarm(alarmReportVO, lsCommonVehicleBO);
                }

                json.put("result", true);
                json.put("lsMonitorAlarmBO", lsMonitorAlarmBO);
                json.put("alarmReportVO", alarmReportVO);
            } catch (Exception e) {
                json.put("result", false);
                json.put("message", e.getMessage());
            } finally {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.print(json.toString());
                out.flush();
                out.close();
            }
        }
        return null;

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
        LsDmAlarmTypeBO alarmTypeBO = alarmTypeService.find(alarmType);
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
            monitorVehicleStatusService.saveOrUpdate(lsMonitorVehicleStatusBO);
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
        LsDmAlarmTypeBO alarmTypeBO = alarmTypeService.find(alarmType);
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

    /**
     * 将违规报告列表导出到excel文件
     * 
     * @throws Exception
     */
    @Action(value = "exportExcel", results = { @Result(name = "success", type = "json") })
    public void exportExcel() throws Exception {
        // String title =
        // MessageResourceUtil.getMessageInfo("alarm.label.alarmDeal.msgSendTitle");

        String alarmLevel1 = MessageResourceUtil.getMessageInfo("AlarmLevel.Serious");// 报警等级
        String alarmLevel2 = MessageResourceUtil.getMessageInfo("AlarmLevel.Light");// 报警等级
        String alarmStatus0 = MessageResourceUtil.getMessageInfo("alarm.label.alarmStatus.notProcessed");// 报警处理状态
        String alarmStatus1 = MessageResourceUtil.getMessageInfo("alarm.label.alarmStatus.processing");// 报警处理状态
        String alarmStatus2 = MessageResourceUtil.getMessageInfo("alarm.label.alarmStatus.processed");// 报警处理状态
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 在浏览器下方显示下载
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");
        String filename = MessageResourceUtil.getMessageInfo("alarm.list.search") + ".xls";
        filename = URLEncoder.encode(filename, "utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);

        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(MessageResourceUtil.getMessageInfo("alarm.list.title"));
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow(0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(MessageResourceUtil.getMessageInfo("trip.report.label.declarationNumber"));
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue(MessageResourceUtil.getMessageInfo("trip.label.trackingDeviceNumber"));
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue(MessageResourceUtil.getMessageInfo("warehouseEsealBO.esealNumber"));
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue(MessageResourceUtil.getMessageInfo("WarehouseSensor.sensorNumber"));
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue(MessageResourceUtil.getMessageInfo("alarm.label.vehiclePlateNumber"));
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue(MessageResourceUtil.getMessageInfo("alarm.label.alarmTime"));
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue(MessageResourceUtil.getMessageInfo("alarm.label.userName"));
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue(MessageResourceUtil.getMessageInfo("alarm.label.alarmStatus"));
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue(MessageResourceUtil.getMessageInfo("alarm.label.alarmLevelName"));
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue(MessageResourceUtil.getMessageInfo("OperateEntityType.ALARMTYPR"));
        cell.setCellStyle(style);
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        @SuppressWarnings("rawtypes")
        List list = monitorAlarmService.findStatic();
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            HashMap<String, String> oHashMap = (HashMap<String, String>) list.get(i);

            row.createCell(0).setCellValue(oHashMap.get("DECLARATION_NUMBER"));
            row.createCell(1).setCellValue(oHashMap.get("TRACKING_DEVICE_NUMBER"));
            row.createCell(2).setCellValue(oHashMap.get("ESEAL_NUMBER"));
            row.createCell(3).setCellValue(oHashMap.get("SENSOR_NUMBER"));
            row.createCell(4).setCellValue(oHashMap.get("VEHICLE_PLATE_NUMBER"));
            row.createCell(5).setCellValue(oHashMap.get("ALARM_TIME"));
            row.createCell(6).setCellValue(oHashMap.get("USER_NAME"));

            if (oHashMap.get("ALARM_STATUS").equals("0")) {
                row.createCell(7).setCellValue(alarmStatus0);
            }
            if (oHashMap.get("ALARM_STATUS").equals("1")) {
                row.createCell(7).setCellValue(alarmStatus1);
            }
            if (oHashMap.get("ALARM_STATUS").equals("2")) {
                row.createCell(7).setCellValue(alarmStatus2);
            }

            if (oHashMap.get("ALARM_LEVEL_CODE").equals("1")) {
                row.createCell(8).setCellValue(alarmLevel1);
            } else {
                row.createCell(8).setCellValue(alarmLevel2);
            }
            row.createCell(9).setCellValue(oHashMap.get("ALARM_TYPE_NAME"));
            // 第四步，创建单元格，并设置值
            cell = row.createCell(10);
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

    /* 报警主键 */
    private String alarmId;

    /* 行程主键 */
    private String tripId;

    /* 类型主键 */
    private String alarmTypeId;

    /* 报警内容 */
    private String alarmContent;

    /* 报警时间 */

    /* 报警经度 */
    private String alarmLongitude;

    /* 报警纬度 */
    private String alarmLatitude;

    /* 是否罚款 */
    private String isPunish;

    /* 罚款内容 */
    private String punishContent;

    @Id
    @Column(name = "ALARM_ID", nullable = false, length = 50)
    public String getAlarmId() {
        return this.alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    @Column(name = "TRIP_ID", nullable = true, length = 50)
    public String getTripId() {
        return this.tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    @Column(name = "ALARM_TYPE_ID", nullable = true, length = 50)
    public String getAlarmTypeId() {
        return this.alarmTypeId;
    }

    public void setAlarmTypeId(String alarmTypeId) {
        this.alarmTypeId = alarmTypeId;
    }

    @Column(name = "ALARM_CONTENT", nullable = true, length = 200)
    public String getAlarmContent() {
        return this.alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    @Column(name = "ALARM_LONGITUDE", nullable = true, length = 20)
    public String getAlarmLongitude() {
        return this.alarmLongitude;
    }

    public void setAlarmLongitude(String alarmLongitude) {
        this.alarmLongitude = alarmLongitude;
    }

    @Column(name = "ALARM_LATITUDE", nullable = true, length = 20)
    public String getAlarmLatitude() {
        return this.alarmLatitude;
    }

    public void setAlarmLatitude(String alarmLatitude) {
        this.alarmLatitude = alarmLatitude;
    }

    @Column(name = "IS_PUNISH", nullable = true, length = 2)
    public String getIsPunish() {
        return this.isPunish;
    }

    public void setIsPunish(String isPunish) {
        this.isPunish = isPunish;
    }

    @Column(name = "PUNISH_CONTENT", nullable = true, length = 2000)
    public String getPunishContent() {
        return this.punishContent;
    }

    public void setPunishContent(String punishContent) {
        this.punishContent = punishContent;
    }

    public LsMonitorAlarmBO getLsMonitorAlarmBO() {
        return lsMonitorAlarmBO;
    }

    public void setLsMonitorAlarmBO(LsMonitorAlarmBO lsMonitorAlarmBO) {
        this.lsMonitorAlarmBO = lsMonitorAlarmBO;
    }

    public MonitorTripVehicleVO getTripVehicleVO() {
        return tripVehicleVO;
    }

    public void setTripVehicleVO(MonitorTripVehicleVO tripVehicleVO) {
        this.tripVehicleVO = tripVehicleVO;
    }

    public List<MonitorAlarmDealVO> getAlarmDealList() {
        return alarmDealList;
    }

    public void setAlarmDealList(List<MonitorAlarmDealVO> alarmDealList) {
        this.alarmDealList = alarmDealList;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public SystemModules getSystemModules() {
        return systemModules;
    }

    public void setSystemModules(SystemModules systemModules) {
        this.systemModules = systemModules;
    }

}
