package com.nuctech.ls.report.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.service.InventoryReportService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.vo.report.DeviceInventoryVO;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.DateUtils;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 库存管理 报告
 * </p>
 * 创建时间：2016年6月13日
 */
@Namespace("/inventoryReport")
public class InventoryReportAction extends LSBaseAction {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * 
     */
    private static final long serialVersionUID = 6225416451500664470L;

    // 关锁流入
    private final static String TRACK_DEVICE_FLOW_IN = "trackDeviceFlowIn";
    // 关锁流出
    private final static String TRACK_DEVICE_FLOW_OUT = "trackDeviceFlowOut";
    // 子锁流入
    private final static String ESEAL_FLOW_IN = "esealFlowIn";
    // 子锁流出
    private final static String ESEAL_FLOW_OUT = "esealFlowOut";
    // 传感器流入
    private final static String SENSOR_FLOW_IN = "sensorFlowIn";
    // 传感器流出
    private final static String SENSOR_FLOW_OUT = "sensorFlowOut";
    // 关锁转入
    private final static String TRACK_DEVICE_TURN_IN = "trackDeviceTurnIn";
    // 关锁转出
    private final static String TRACK_DEVICE_TURN_OUT = "trackDeviceTurnOut";
    // 子锁转入
    private final static String ESEAL_TURN_IN = "esealTurnIn";
    // 子锁转出
    private final static String ESEAL_TURN_OUT = "esealTurnOut";
    // 传感器转入
    private final static String SENSOR_TURN_IN = "sensorTurnIn";
    // 传感器转出
    private final static String SENSOR_TURN_OUT = "sensorTurnOut";
    // 关锁库存
    private final static String TRACK_DEVICE_INVENTORY = "trackDeviceInventory";
    // 子锁库存
    private final static String ESEAL_INVENTORY = "esealInventory";
    // 传感器库存
    private final static String SENSOR_INVENTORY = "sensorInventory";
    @Resource
    private InventoryReportService inventoryReportService;

    private DeviceInventoryVO deviceInventory;

    private String type;

    private List<LsSystemDepartmentBO> deptList = new ArrayList<LsSystemDepartmentBO>();// 设备所属口岸集合
    @Resource
    private SystemModules systemModules;// 系统配置模块
    @Resource
    private SystemDepartmentService systemDepartmentService;

    private SessionUser sessionU;

    /**
     * 通知首页链接 返回到通知的首页
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    @Action(value = "index", results = { @Result(name = "success", location = "/report/inventory.jsp") })
    public String index() {
        try {
            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            sessionU = sessionUser;
            pageQuery = this.newPageQuery("");
            Map<String, String> params = pageQuery.getFilters();
            if (sessionUser != null) {
                // 查询所有口岸(当前用户所属国家的所有口岸)
                deptList = systemDepartmentService.findAllPortByUserId(sessionUser.getUserId());
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    /**
     * 关锁、子锁及传感器的流入、流出、转入、转出及库存数量信息
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    @Action(value = "toList")
    public void toList() throws Exception {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        sessionU = sessionUser;
        pageQuery = this.newPageQuery("");
        Map<String, String> params = pageQuery.getFilters();
        if (sessionUser != null) {
            Map<String, Date> paramsMap = getInventoryParams(params);
            Date startDate = paramsMap.get("startDate");
            Date endDate = paramsMap.get("endDate");
            String portId = "";
            String s_portId = request.getParameter("s_belongTo");// 所属口岸id
            String userToOrgId = sessionUser.getOrganizationId();// 当前用户所属口岸id
            if (sessionUser.getRoleId().equals("4") || sessionUser.getRoleId().equals("5")
                    || sessionUser.getRoleName().equals("contromRoomManager")
                    || sessionUser.getRoleName().equals("contromRoomUser")) {// 中心用户
                portId = s_portId;
            } else {// 非中心用户
                portId = userToOrgId;
            }
            deviceInventory = inventoryReportService.statisticsDeviceInventory(portId, startDate, endDate);
        }
        JSONObject json = null;
        json = JSONObject.fromObject(deviceInventory);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
        out.close();
    }

    /**
     * 关锁、子锁及传感器的流入、流出、转入、转出及库存的具体信息列表
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    @Action(value = "list")
    public void list() throws IOException {
        pageQuery = this.newPageQuery("");
        Map<String, Object> filters = new HashMap<String, Object>();
        JSONArray retJson = null;
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String mode = request.getParameter("s_mode");
        String checkinStartTime = request.getParameter("s_checkinStartTime");
        String portId = "";
        String s_portId = request.getParameter("s_belongTo");// 所属口岸id
        String userToOrgId = sessionUser.getOrganizationId();// 当前用户所属口岸id
        if (sessionUser.getRoleId().equals("4") || sessionUser.getRoleId().equals("5")
                || sessionUser.getRoleName().equals("contromRoomManager")
                || sessionUser.getRoleName().equals("contromRoomUser")) {// 中心用户
            portId = s_portId;
        } else {// 非中心用户
            portId = userToOrgId;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("mode", mode);
        params.put("checkinStartTime", checkinStartTime);
        if (sessionUser != null) {
            Map<String, Date> paramsMap = getInventoryParams(params);
            Date startDate = paramsMap.get("startDate");
            Date endDate = paramsMap.get("endDate");
            try {
                if (NuctechUtil.isNull(type)) {
                    retJson = new JSONArray();
                } else if (type.equals(TRACK_DEVICE_FLOW_IN)) {
                    if (NuctechUtil.isNotNull(portId)) {
                        filters.put("checkoutPort", portId);
                    }
                    if (NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)) {
                        filters.put("checkoutStartTime", DateUtils.date2String(startDate));
                        filters.put("checkoutEndTime", DateUtils.date2String(endDate));
                    }
                    pageQuery.setFilters(filters);
                    retJson = inventoryReportService.findTrackDeviceFlowList(pageQuery, null, false);
                } else if (type.equals(TRACK_DEVICE_FLOW_OUT)) {
                    if (NuctechUtil.isNotNull(portId)) {
                        filters.put("checkinPort", portId);
                    }
                    if (NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)) {
                        filters.put("checkinStartTime", DateUtils.date2String(startDate));
                        filters.put("checkinEndTime", DateUtils.date2String(endDate));
                    }
                    pageQuery.setFilters(filters);
                    retJson = inventoryReportService.findTrackDeviceFlowList(pageQuery, null, false);
                } else if (type.equals(ESEAL_FLOW_IN)) {
                    if (NuctechUtil.isNotNull(portId)) {
                        filters.put("checkoutPort", portId);
                    }
                    if (NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)) {
                        filters.put("checkoutStartTime", DateUtils.date2String(startDate));
                        filters.put("checkoutEndTime", DateUtils.date2String(endDate));
                    }
                    pageQuery.setFilters(filters);
                    retJson = inventoryReportService.findEsealFlowList(pageQuery, null, false);
                } else if (type.equals(ESEAL_FLOW_OUT)) {
                    if (NuctechUtil.isNotNull(portId)) {
                        filters.put("checkinPort", portId);
                    }
                    if (NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)) {
                        filters.put("checkinStartTime", DateUtils.date2String(startDate));
                        filters.put("checkinEndTime", DateUtils.date2String(endDate));
                    }
                    pageQuery.setFilters(filters);
                    retJson = inventoryReportService.findEsealFlowList(pageQuery, null, false);
                } else if (type.equals(SENSOR_FLOW_IN)) {
                    if (NuctechUtil.isNotNull(portId)) {
                        filters.put("checkoutPort", portId);
                    }
                    if (NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)) {
                        filters.put("checkoutStartTime", DateUtils.date2String(startDate));
                        filters.put("checkoutEndTime", DateUtils.date2String(endDate));
                    }
                    pageQuery.setFilters(filters);
                    retJson = inventoryReportService.findSensorFlowList(pageQuery, null, false);
                } else if (type.equals(SENSOR_FLOW_OUT)) {
                    if (NuctechUtil.isNotNull(portId)) {
                        filters.put("checkinPort", portId);
                    }
                    if (NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)) {
                        filters.put("checkinStartTime", DateUtils.date2String(startDate));
                        filters.put("checkinEndTime", DateUtils.date2String(endDate));
                    }
                    pageQuery.setFilters(filters);
                    retJson = inventoryReportService.findSensorFlowList(pageQuery, null, false);
                } else if (type.equals(TRACK_DEVICE_TURN_IN)) {
                    if (NuctechUtil.isNotNull(portId)) {
                        filters.put("applcationPort", portId);
                    }
                    if (NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)) {
                        filters.put("finishStartTime", DateUtils.date2String(startDate));
                        filters.put("finishEndTime", DateUtils.date2String(endDate));
                    }
                    pageQuery.setFilters(filters);
                    retJson = inventoryReportService.findTrackDeviceTrunInList(pageQuery, null, false);
                } else if (type.equals(TRACK_DEVICE_TURN_OUT)) {
                    if (NuctechUtil.isNotNull(portId)) {
                        filters.put("fromPort", portId);
                    }
                    if (NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)) {
                        filters.put("dispatchStartTime", DateUtils.date2String(startDate));
                        filters.put("dispatchEndTime", DateUtils.date2String(endDate));
                    }
                    pageQuery.setFilters(filters);
                    retJson = inventoryReportService.findTrackDeviceTrunOutList(pageQuery, null, false);
                } else if (type.equals(ESEAL_TURN_IN)) {
                    if (NuctechUtil.isNotNull(portId)) {
                        filters.put("applcationPort", portId);
                    }
                    if (NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)) {
                        filters.put("finishStartTime", DateUtils.date2String(startDate));
                        filters.put("finishEndTime", DateUtils.date2String(endDate));
                    }
                    pageQuery.setFilters(filters);
                    retJson = inventoryReportService.findEsealTrunInList(pageQuery, null, false);
                } else if (type.equals(ESEAL_TURN_OUT)) {
                    if (NuctechUtil.isNotNull(portId)) {
                        filters.put("fromPort", portId);
                    }
                    if (NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)) {
                        filters.put("dispatchStartTime", DateUtils.date2String(startDate));
                        filters.put("dispatchEndTime", DateUtils.date2String(endDate));
                    }
                    pageQuery.setFilters(filters);
                    retJson = inventoryReportService.findEsealTrunOutList(pageQuery, null, false);
                } else if (type.equals(SENSOR_TURN_IN)) {
                    if (NuctechUtil.isNotNull(portId)) {
                        filters.put("applcationPort", portId);
                    }
                    if (NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)) {
                        filters.put("finishStartTime", DateUtils.date2String(startDate));
                        filters.put("finishEndTime", DateUtils.date2String(endDate));
                    }
                    pageQuery.setFilters(filters);
                    retJson = inventoryReportService.findSensorTrunInList(pageQuery, null, false);
                } else if (type.equals(SENSOR_TURN_OUT)) {
                    if (NuctechUtil.isNotNull(portId)) {
                        filters.put("fromPort", portId);
                    }
                    if (NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)) {
                        filters.put("dispatchStartTime", DateUtils.date2String(startDate));
                        filters.put("dispatchEndTime", DateUtils.date2String(endDate));
                    }
                    pageQuery.setFilters(filters);
                    retJson = inventoryReportService.findSensorTrunOutList(pageQuery, null, false);
                } else if (type.equals(TRACK_DEVICE_INVENTORY)) {
                    retJson = inventoryReportService.findTrackDeviceInventoryList(pageQuery, portId, startDate,
                            endDate);
                } else if (type.equals(ESEAL_INVENTORY)) {
                    retJson = inventoryReportService.findEsealInventoryList(pageQuery, portId, startDate, endDate);
                } else if (type.equals(SENSOR_INVENTORY)) {
                    retJson = inventoryReportService.findSensorInventoryList(pageQuery, portId, startDate, endDate);
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    public String findDeviceInventory() {
        return SUCCESS;
    }

    private Map<String, Date> getInventoryParams(Map<String, String> params) {
        Map<String, Date> map = new HashMap<String, Date>();
        Date startDate = null;
        Date endDate = null;
        String mode = params.get("mode");
        if (!NuctechUtil.isNull(mode) && mode.equals("year")) { // 按年统计
            String time = params.get("checkinStartTime");
            startDate = DateUtils.string2Date(time + "-01-01 00:00:00").getTime();
            endDate = DateUtils.string2Date(time + "-12-31 23:59:59").getTime();
        } else if (!NuctechUtil.isNull(mode) && mode.equals("month")) {
            // 按月统计
            String time = params.get("checkinStartTime");
            startDate = DateUtils.string2Date(time + "-01 00:00:00").getTime();
            endDate = DateUtils.monthLastDay(time + "-01 00:00:00");
        } else if (!NuctechUtil.isNull(mode) && mode.equals("day")) {
            // 按日统计
            String time = params.get("checkinStartTime");
            startDate = DateUtils.string2Date(time + " 00:00:00").getTime();
            endDate = DateUtils.string2Date(time + " 23:59:59").getTime();
        }
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        return map;
    }

    public DeviceInventoryVO getDeviceInventory() {
        return deviceInventory;
    }

    public void setDeviceInventory(DeviceInventoryVO deviceInventory) {
        this.deviceInventory = deviceInventory;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SystemModules getSystemModules() {
        return systemModules;
    }

    public void setSystemModules(SystemModules systemModules) {
        this.systemModules = systemModules;
    }

    public List<LsSystemDepartmentBO> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<LsSystemDepartmentBO> deptList) {
        this.deptList = deptList;
    }

    public SystemDepartmentService getSystemDepartmentService() {
        return systemDepartmentService;
    }

    public void setSystemDepartmentService(SystemDepartmentService systemDepartmentService) {
        this.systemDepartmentService = systemDepartmentService;
    }

    public SessionUser getSessionU() {
        return sessionU;
    }

    public void setSessionU(SessionUser sessionU) {
        this.sessionU = sessionU;
    }

}
