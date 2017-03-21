package com.nuctech.ls.center.device.action;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.center.websocket.WebsocketService;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.system.LsSystemNoticeLogBO;
import com.nuctech.ls.model.bo.system.LsSystemNoticesBO;
import com.nuctech.ls.model.bo.system.LsSystemOrganizationUserBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceApplicationBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceDispatchBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemNoticeLogService;
import com.nuctech.ls.model.service.SystemNoticeService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.service.WarehouseDeviceApplicationService;
import com.nuctech.ls.model.service.WarehouseDispatchAnalysisService;
import com.nuctech.ls.model.util.NoticeType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.DeviceDemand;
import com.nuctech.ls.model.vo.warehouse.DeviceInventoryChartsVO;
import com.nuctech.ls.model.vo.warehouse.DispatchActualProgram;
import com.nuctech.ls.model.vo.warehouse.DispatchPlanVO;
import com.nuctech.ls.model.vo.warehouse.DispatchPortVO;
import com.nuctech.util.Constant;
import com.nuctech.util.MessageResourceUtil;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 调度分析 Action
 * </p>
 * 创建时间：2016年6月2日
 */
@Namespace("/warehouseDispatchAnalysis")
public class WarehouseDispatchAnalysisAction extends LSBaseAction {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * 
     */
    private static final long serialVersionUID = -7737991034005394499L;
    @Resource
    private SystemNoticeLogService systemNoticeLogService;
    @Resource
    private WarehouseDispatchAnalysisService warehouseDispatchAnalysisService;
    @Resource
    private WarehouseDeviceApplicationService warehouseDeviceApplicationService;
    @Resource
    private SystemDepartmentService systemDepartmentService;
    @Resource
    private SystemUserService systemUserService;
    @Resource
    private SystemNoticeService noticeService;

    private List<DispatchPortVO> dispatchPortList;
    private String portId;
    private String portName; // 查询口岸名称
    private List<DeviceInventoryChartsVO> deviceInventoryList; // 设备库存列表
    private DispatchActualProgram dispatchActualProgram;
    private String applicationId; // 申请ID
    private LsWarehouseDeviceApplicationBO deviceApplication;
    private LsSystemDepartmentBO applicationDepartment;
    private List<DispatchPlanVO> dispatchPlanList;
    private LsSystemNoticesBO notice;
    private JSONArray planPortNameArr = new JSONArray();
    private JSONArray planDistanceArr = new JSONArray();
    private LsSystemNoticeLogBO noticeLogBO;
    private String jsonData;
    // 查询用户未读取通知的数量
    private int needDealNoticeCount;
    // List<Userinfo> list = JSON.parseArray(jsonString, Userinfo.class);
    // 提交返回结果
    private String result;

    @Action(value = "index", results = { @Result(name = "success", location = "/device/dispatch/analysis.jsp") })
    public String index() {
        // 根据申请的ID查询申请对象
        try {
            // SessionUser sessionUser =
            // (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
            if (!NuctechUtil.isNull(applicationId)) {
                deviceApplication = warehouseDispatchAnalysisService.findWarehouseDeviceApplicationById(applicationId);
                applicationDepartment = systemDepartmentService.findById(deviceApplication.getApplcationPort());
                DeviceDemand deviceDemand = new DeviceDemand();
                deviceDemand.setPortId(deviceApplication.getApplcationPort());
                deviceDemand.setEsealNumber(Integer.parseInt(deviceApplication.getEsealNumber()));
                deviceDemand.setSensorNumber(Integer.parseInt(deviceApplication.getSensorNumber()));
                deviceDemand.setTrackDeviceNumber(Integer.parseInt(deviceApplication.getDeviceNumber()));

                dispatchPlanList = warehouseDispatchAnalysisService.findDispatchPlanList(deviceDemand);

                buildChartDataByList(dispatchPlanList);
            }
        } catch (Exception e) {
            logger.error(e);
        }

        return SUCCESS;
    }

    @Action(value = "listPort", results = { @Result(name = "success", location = "/device/dispatch/port_table.jsp") })
    public String listPort() {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        try {
            dispatchPortList = warehouseDispatchAnalysisService.findDispatchPortList(portName, sessionUser.getUserId());
        } catch (Exception e) {
            logger.error(e);
        }
        return SUCCESS;
    }

    /**
     * 查询设备库存报表
     * 
     * @return
     */
    @Action(value = "findDeviceInventoryList", results = { @Result(name = "success", type = "json") })
    public String findDeviceInventoryList() {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        deviceInventoryList = warehouseDispatchAnalysisService.findDispatchPortChartList(sessionUser.getUserId());
        return SUCCESS;
    }

    @Action(value = "findDispatchActualProgramByPortId", results = { @Result(name = "success", type = "json") })
    public String findDispatchActualProgramByPortId() {
        dispatchActualProgram = warehouseDispatchAnalysisService.findDispatchActualProgramByPortId(portId);
        return SUCCESS;
    }

    @Action(value = "submitDeviceDispatchInfo", results = { @Result(name = "success", type = "json") })
    public String submitDeviceDispatchInfo() {
        String data = request.getParameter("deviceData");
        String aid = request.getParameter("applicationId");
        String applicationPort = request.getParameter("applicationPort");
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        JSONArray jsonArray = JSONArray.fromObject(data);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray subArray = (JSONArray) jsonArray.get(i);
            // 保存实际提交方案
            LsWarehouseDeviceDispatchBO warehouseDeviceDispatch = new LsWarehouseDeviceDispatchBO();
            warehouseDeviceDispatch.setDispatchId(generatePrimaryKey());
            warehouseDeviceDispatch.setApplicationId(aid);
            warehouseDeviceDispatch.setFromPort((String) subArray.get(0));
            warehouseDeviceDispatch.setDeviceNumber((String) subArray.get(1));
            warehouseDeviceDispatch.setEsealNumber((String) subArray.get(2));
            warehouseDeviceDispatch.setSensorNumber((String) subArray.get(3));
            warehouseDeviceDispatch.setToPort(applicationPort);
            warehouseDeviceDispatch.setDispatchStatus(Constant.DEVICE_UN_DISPATCH);
            warehouseDispatchAnalysisService.saveWarehouseDeviceDispatch(warehouseDeviceDispatch);
            // 修改申请状态
            LsWarehouseDeviceApplicationBO warehouseDeviceApplication = warehouseDeviceApplicationService.findByID(aid);
            warehouseDeviceApplication.setApplyStatus(Constant.DEVICE_ALREADY_DEAL_WITH);
            warehouseDeviceApplication.setDealTime(new Date());
            warehouseDeviceApplication.setDealUser(sessionUser.getUserId());
            warehouseDeviceApplicationService.modify(warehouseDeviceApplication);

            // 给调度口岸发出通知，执行调度
            @SuppressWarnings("unused")
            List<LsSystemOrganizationUserBO> portuser 
            = systemUserService.findByPortId((String) subArray.get(0));// 根据口岸，查出该口岸的所有用户
            // 拼接通知人员
            String noticeUsers = "";
            for (int j = 0; j < portuser.size(); j++) {
                noticeUsers += portuser.get(j).getUserId() + ",";
            }
            // 创建给口岸用户发送 的调度通知
            notice = createDispatchNotice(noticeUsers, applicationPort, (String) subArray.get(1),
                    (String) subArray.get(2), (String) subArray.get(3));
            // 创建通知处理
            for (int j = 0; j < portuser.size(); j++) {
                noticeLogBO = new LsSystemNoticeLogBO();
                noticeLogBO.setNoticeRevId(generatePrimaryKey());
                noticeLogBO.setDealType(Constant.NOTICE_UN_HANDLED);
                noticeLogBO.setReceiveUser(portuser.get(j).getUserId());
                noticeService.addNoticeLog(noticeLogBO);
            }
            // websocket发送通知
            for (int z = 0; z < portuser.size(); z++) {
                JSONObject json = new JSONObject();
                json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_NOTICE_TOPORT);
                json.put("noticeId", notice.getNoticeId());
                json.put("title", notice.getNoticeTitle());
                json.put("content", notice.getNoticeContent());
                json.put("receiveUser", portuser.get(z).getUserId());
                WebsocketService.sendMessage(json.toString());
            }
        }
        result = "true";
        return SUCCESS;
    }

    /**
     * 构建计划报表的数据集合
     * 
     * @param dispatchPlanList
     */
    private void buildChartDataByList(List<DispatchPlanVO> dispatchPlanList) {
        if (dispatchPlanList != null && !dispatchPlanList.isEmpty()) {
            for (DispatchPlanVO dispatchPlan : dispatchPlanList) {
                planPortNameArr.add(dispatchPlan.getPortName());
                planDistanceArr.add(dispatchPlan.getDistance().toString());
            }
        }
    }

    /**
     * 控制中心用户执行调度后给口岸用户发送通知（模态框）
     * 
     * @return
     */
    @Action(value = "excutedispatch", results = { @Result(name = "success", location = "/include/dispatchToPort.jsp") })
    public String excutedispatch() {
        return SUCCESS;
    }

    public List<DispatchPortVO> getDispatchPortList() {
        return dispatchPortList;
    }

    public void setDispatchPortList(List<DispatchPortVO> dispatchPortList) {
        this.dispatchPortList = dispatchPortList;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public List<DeviceInventoryChartsVO> getDeviceInventoryList() {
        return deviceInventoryList;
    }

    public void setDeviceInventoryList(List<DeviceInventoryChartsVO> deviceInventoryList) {
        this.deviceInventoryList = deviceInventoryList;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public DispatchActualProgram getDispatchActualProgram() {
        return dispatchActualProgram;
    }

    public void setDispatchActualProgram(DispatchActualProgram dispatchActualProgram) {
        this.dispatchActualProgram = dispatchActualProgram;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public LsWarehouseDeviceApplicationBO getDeviceApplication() {
        return deviceApplication;
    }

    public void setDeviceApplication(LsWarehouseDeviceApplicationBO deviceApplication) {
        this.deviceApplication = deviceApplication;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LsSystemDepartmentBO getApplicationDepartment() {
        return applicationDepartment;
    }

    public void setApplicationDepartment(LsSystemDepartmentBO applicationDepartment) {
        this.applicationDepartment = applicationDepartment;
    }

    public List<DispatchPlanVO> getDispatchPlanList() {
        return dispatchPlanList;
    }

    public void setDispatchPlanList(List<DispatchPlanVO> dispatchPlanList) {
        this.dispatchPlanList = dispatchPlanList;
    }

    public JSONArray getPlanPortNameArr() {
        return planPortNameArr;
    }

    public void setPlanPortNameArr(JSONArray planPortNameArr) {
        this.planPortNameArr = planPortNameArr;
    }

    public JSONArray getPlanDistanceArr() {
        return planDistanceArr;
    }

    public void setPlanDistanceArr(JSONArray planDistanceArr) {
        this.planDistanceArr = planDistanceArr;
    }

    private LsSystemNoticesBO createDispatchNotice(String noticeUser, String applicationport, String elockNum,
            String esealNum, String sensorNum) {
        // 根据口岸id获取口岸的名称
        LsSystemDepartmentBO systemDepartment = systemDepartmentService.findById(applicationport);
        String content1 = MessageResourceUtil.getMessageInfo("Application.port");
        String content2 = MessageResourceUtil.getMessageInfo("dispatch.need.elock");
        String content3 = MessageResourceUtil.getMessageInfo("dispatch.need.eseal");
        String content4 = MessageResourceUtil.getMessageInfo("dispatch.need.sensor");
        String content5 = MessageResourceUtil.getMessageInfo("dispatch.excute");
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        notice = new LsSystemNoticesBO();
        notice.setNoticeId(generatePrimaryKey());
        notice.setDeployTime(new Date());
        notice.setPublisher(sessionUser.getUserId());
        notice.setNoticeContent(content1 + systemDepartment.getOrganizationName() + content2 + elockNum + content3
                + esealNum + content4 + sensorNum);
        notice.setNoticeState(Constant.NOTICE_STATE_PUBLISH);
        notice.setNoticeTitle(content5);
        notice.setNoticeUsers(noticeUser);// 通知接收人
        notice.setNoticeType(NoticeType.DispatchNotice.getType());
        noticeService.addNotice(notice);
        return notice;
    }

    @Action(value = "msgsure", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String msgsure() {
        // 控制中心用户接收到通知，更新左下角的通知图标上显示的通知数量。
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String userId = sessionUser.getUserId();
        HttpSession session = request.getSession();
        needDealNoticeCount = systemNoticeLogService.findCount(userId);
        session.setAttribute(Constant.needDealNoticeCount, needDealNoticeCount);
        return SUCCESS;
    }

}
