package com.nuctech.ls.center.device.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.center.utils.IpUtil;
import com.nuctech.ls.center.websocket.WebsocketService;
import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.system.LsSystemNoticeLogBO;
import com.nuctech.ls.model.bo.system.LsSystemNoticesBO;
import com.nuctech.ls.model.bo.system.LsSystemTasksBO;
import com.nuctech.ls.model.bo.system.LsSystemTasksDealBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceDispatchBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseEsealBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseSensorBO;
import com.nuctech.ls.model.service.DispatchDetailService;
import com.nuctech.ls.model.service.DispatchRecordService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemNoticeLogService;
import com.nuctech.ls.model.service.SystemNoticeService;
import com.nuctech.ls.model.service.SystemTasksService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.util.NoticeType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.MessageResourceUtil;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;

@Namespace("/dispatchSendMsg")
public class DeviceDipatchSendMegAction extends LSBaseAction {

    private static final long serialVersionUID = 1L;
    Logger logger = Logger.getLogger(DeviceDipatchSendMegAction.class);

    /**
     * 向巡逻队推送消息
     */
    private String clientIPAddress;// 客户端Ip地址
    private String[] potralUserIds;// 巡逻队负责人Id
    private String dispacthId;// 获取调配记录表主键
    private LsWarehouseDeviceDispatchBO warehouseDeviceDispatchBO;
    private LsSystemDepartmentBO systemDepartmentBO;
    private LsSystemNoticesBO notice;
    private LsSystemNoticeLogBO noticeLogBO;
    private String noticeId;
    private String dispacthid;// 获取调配记录表主键

    @Resource
    private SystemUserService systemUserService;
    @Resource
    private SystemNoticeService noticeService;
    @Resource
    private DispatchRecordService dispatchRecordService;
    @Resource
    private SystemDepartmentService systemDepartmentService;
    @Resource
    private SystemNoticeLogService systemNoticeLogService;
    // 查询用户未读取通知的数量
    private int needDealNoticeCount;
    /**
     * 在向巡逻队推送消息的模态框中显示设备的详细信息
     */
    @Resource
    private DispatchDetailService dispatchDetailService;
    private List<LsWarehouseElockBO> elockDetailLit = new ArrayList<LsWarehouseElockBO>();
    private List<LsWarehouseEsealBO> esealDetailLit = new ArrayList<LsWarehouseEsealBO>();
    private List<LsWarehouseSensorBO> sensorDetailLit = new ArrayList<LsWarehouseSensorBO>();
    /**
     * 向控制中心人员推送消息
     */
    private String[] userIds;
    private String contromuserId;
    private String receiverId;
    private String launchuser;// 发起交班的工作人员
    /**
     * 把多个任务推送给巡逻队
     */
    private String taskIds;
    private LsSystemTasksBO systemTasksBO;
    @Resource
    private SystemTasksService systemTasksService;
    private LsSystemUserBO systemUserBO;
    private String taskId;
    private LsSystemTasksDealBO systemTasksDealBO;
    @Resource
    private SystemModules systemModules;

    /**
     * 巡逻队模态框调用
     * 
     * @return
     */
    @Action(value = "addPatrolModal",
            results = { @Result(name = "success", location = "/device/dispatchhandle/patrol.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String addPatrolModal() {
        // 传一些参数如设备号，数量等消息到巡逻队页面
        clientIPAddress = IpUtil.getIpAddr(request);
        request.setAttribute("dispacthId", dispacthId);
        return SUCCESS;
    }

    /**
     * 向巡逻队推送消息
     * 
     * @return
     */
    @Action(value = "sendToPatrol", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String publish() {
        if (NuctechUtil.isNull(potralUserIds)) {
            message = "发布通知,参数[ids]为空";
            logger.error("发布通知,参数[ids]为空");
            return ERROR;
        } else {
            try {
                SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                // 把巡逻队负责人的id号用逗号分隔，将其作为noticeUsers
                String str = "";
                for (int i = 0; i < potralUserIds.length; i++) {
                    str += potralUserIds[i] + ",";
                }

                // 往多个巡逻队用户同时推送消息
                notice = createDispatchNotice(dispacthId, str);
                notice.setNoticeState(Constant.NOTICE_STATE_PUBLISH);
                noticeService.updateNotice(notice);

                for (int i = 0; i < potralUserIds.length; i++) {
                    JSONObject json = new JSONObject();
                    json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_DISPATCH_TOPATROL);
                    json.put("noticeId", notice.getNoticeId());
                    json.put("title", notice.getNoticeTitle());
                    json.put("content", notice.getNoticeContent());
                    json.put("receiveUser", potralUserIds[i]);
                    json.put("dispacthId", dispacthId);
                    WebsocketService.sendMessage(json.toString());
                }
                // 新建任务记录(任务接收人为接收消息的巡逻队，任务内容即通知内容)
                systemTasksBO = new LsSystemTasksBO();
                systemTasksBO.setTaskId(generatePrimaryKey());
                systemTasksBO.setDeployTime(new Date());
                systemTasksBO.setPriority("1");
                systemTasksBO.setPublisher(sessionUser.getUserId());
                systemTasksBO.setTaskContent(notice.getNoticeContent());
                systemTasksBO.setTaskTitle(notice.getNoticeTitle());
                systemTasksBO.setTaskReceiveUsers(str);
                systemTasksBO.setTaskType("3");
                systemTasksService.addTask(systemTasksBO);
                // 新建任务处理记录
                systemTasksDealBO = new LsSystemTasksDealBO();
                systemTasksDealBO.setTaskDealId(generatePrimaryKey());
                systemTasksDealBO.setTaskId(systemTasksBO.getTaskId());
                systemTasksDealBO.setDealType("0");
                systemTasksDealBO.setReceiveUser(str);
                systemTasksService.addTaskDeal(systemTasksDealBO);
            } catch (Exception e) {
                message = e.getMessage();
                logger.error("发布通知异常", e);
                return ERROR;
            }
            return SUCCESS;
        }
    }

    private LsSystemNoticesBO createDispatchNotice(String dispacthId, String string) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        warehouseDeviceDispatchBO = dispatchRecordService.findByDispatchID(dispacthId);
        String organizationId = warehouseDeviceDispatchBO.getFromPort();
        systemDepartmentBO = systemDepartmentService.findById(organizationId);
        String title = MessageResourceUtil.getMessageInfo("dispatch.sendMsg.toPatrol.over");
        notice = new LsSystemNoticesBO();
        notice.setDeployTime(new Date());
        notice.setPublisher(sessionUser.getUserId());
        notice.setNoticeId(generatePrimaryKey());
        notice.setNoticeState(Constant.NOTICE_STATE_PUBLISH);
        String content1 = MessageResourceUtil.getMessageInfo("dispatch.please.goTo.patrol");
        String content2 = MessageResourceUtil.getMessageInfo("dispatch.transport.equipment.elock");
        String content3 = MessageResourceUtil.getMessageInfo("dispatch.transport.equipment.eseal");
        String content4 = MessageResourceUtil.getMessageInfo("dispatch.transport.equipment.sensor");
        String content5 = MessageResourceUtil.getMessageInfo("dispatch.transport.equipment.numbers");
        notice.setNoticeContent(content1 + systemDepartmentBO.getOrganizationName() + " " + content2
                + warehouseDeviceDispatchBO.getDeviceNumber() + content3 + warehouseDeviceDispatchBO.getEsealNumber()
                + content4 + warehouseDeviceDispatchBO.getSensorNumber() + ".");
        notice.setNoticeTitle(title);
        notice.setNoticeUsers(string);// 通知接收人
        notice.setNoticeType(NoticeType.DispatchNotice.getType());
        noticeService.addNotice(notice);
        return notice;
    }

    /**
     * 巡逻队收到消息后，点击同意，通知状态改为“完成”
     * 
     * @return
     */
    @Action(value = "aggreeReceive", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String aggreeReceive() {
        notice = noticeService.findById(noticeId);
        notice.setNoticeState(Constant.NOTICE_STATE_FINISH);
        noticeService.updateNotice(notice);
        return SUCCESS;
    }

    /**
     * 控制中心收到调度完成的消息
     * 
     * @return
     */
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

    /**
     * 向巡逻队推送消息，包含通知内容的模态框显示
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    @Action(value = "msgToPatrolModal",
            results = { @Result(name = "success", location = "/include/dispatchMsgModal.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String msgToPatrolModal() {
        // 根据调度id到调度细节表中查出相应的设备，取出设备编号放到模态框中
        elockDetailLit = dispatchDetailService.getElockDetailList(dispacthid);
        esealDetailLit = dispatchDetailService.getEsealDetaillist(dispacthid);
        sensorDetailLit = dispatchDetailService.getSensorDetaillist(dispacthid);
        return SUCCESS;
    }

    /**
     * 控制中心人员模态框调用
     * 
     * @return
     */
    @Action(value = "addControlUserModal",
            results = { @Result(name = "success", location = "/device/dispatchhandle/controlRoomUser.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String addControlUserModal() {
        // 传一些参数如设备号，数量等消息到巡逻队页面
        clientIPAddress = IpUtil.getIpAddr(request);
        request.setAttribute("dispacthId", dispacthId);
        return SUCCESS;
    }

    /**
     * 向控制中心推送消息
     * 
     * @return
     */

    @Action(value = "sendToContromRoom",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String sendToContromRoom() {
        if (NuctechUtil.isNull(userIds)) {
            message = "发布通知,参数[ids]为空";
            logger.error("发布通知,参数[ids]为空");
            return ERROR;
        } else {
            try {
                SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                // 把控制中心用户的id号用逗号分隔，将其作为noticeUsers
                String str = "";
                for (int i = 0; i < userIds.length; i++) {
                    str += userIds[i] + ",";
                }

                /**
                 * 往多个控制中心用户同时推送消息
                 */
                notice = createDispatchNotice(dispacthId, str);
                notice.setPublisher(sessionUser.getUserId());
                notice.setNoticeState(Constant.NOTICE_STATE_PUBLISH);
                noticeService.updateNotice(notice);
                // 新建与通知对应的通知接收类；接收人为中心用户
                /*
                 * noticeLogBO = new LsSystemNoticeLogBO();
                 * noticeLogBO.setNoticeRevId(generatePrimaryKey());
                 * noticeLogBO.setNoticeId(notice.getNoticeId());
                 * noticeLogBO.setReceiveUser(userIds[0]);
                 * noticeLogBO.setDealType(Constant.NOTICE_UN_HANDLED);
                 * noticeService.addNoticeLog(noticeLogBO);
                 */

                for (int i = 0; i < userIds.length; i++) {
                    JSONObject json = new JSONObject();
                    json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_DISPATCH_TOCONTROLROOM);
                    json.put("noticeId", notice.getNoticeId());
                    json.put("title", notice.getNoticeTitle());
                    json.put("content", notice.getNoticeContent());
                    json.put("receiveUser", userIds[i]);
                    json.put("dispacthId", dispacthId);
                    WebsocketService.sendMessage(json.toString());
                }
            } catch (Exception e) {
                message = e.getMessage();
                logger.error("发布通知异常", e);
                return ERROR;
            }
            return SUCCESS;
        }
    }

    /**
     * 向控制中心推送消息，包含通知内容的模态框显示
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    @Action(value = "msgToControlRoomModal",
            results = { @Result(name = "success", location = "/include/msgToControlRoomModal.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String msgToControlRoomModal() {
        // 根据调度id到调度细节表中查出相应的设备，取出设备编号放到模态框中
        elockDetailLit = dispatchDetailService.getElockDetailList(dispacthid);
        esealDetailLit = dispatchDetailService.getEsealDetaillist(dispacthid);
        sensorDetailLit = dispatchDetailService.getSensorDetaillist(dispacthid);
        request.setAttribute("dispacthId", dispacthid);

        return SUCCESS;
    }

    /**
     * 报警中心给巡逻队推送消息，显示巡逻队的模态框
     * 
     * @return
     */
    @Action(value = "missionPatrolModal",
            results = { @Result(name = "success", location = "/artdialogconyent/missionPatrol.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String missionPatrolModal() {
        request.setAttribute("taskIds", taskIds);
        return SUCCESS;
    }

    /**
     * 报警中心的人员把向巡逻队推送任务
     * 
     * @return
     */
    @Action(value = "sendMisionToPatrol",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String sendMisionToPatrol() {
        String patrol = "";
        for (int i = 0; i < potralUserIds.length; i++) {
            patrol += potralUserIds[i] + ",";
        }
        // 向巡逻队推送任务
        notice = createMissionNotice(taskIds, patrol);
        //
        String receiveUser = patrol.substring(0, patrol.length() - 1);
        JSONObject json = new JSONObject();
        json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_SEND_MISSION);
        json.put("noticeId", notice.getNoticeId());
        json.put("title", notice.getNoticeTitle());
        json.put("content", notice.getNoticeContent());
        json.put("receiveUser", receiveUser);
        json.put("taskIds", taskIds);
        WebsocketService.sendMessage(json.toString());

        return SUCCESS;
    }

    /**
     * 创建一个通知
     * 
     * @param taskIds
     * @param patrol
     * @return
     */
    private LsSystemNoticesBO createMissionNotice(String taskIds, String patrol) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        // 根据巡逻队负责人查出该巡逻队的名称；
        if (NuctechUtil.isNotNull(patrol)) {
            String patrolUser = patrol.substring(0, patrol.length() - 1);
            systemUserBO = systemUserService.findById(patrolUser);
        }

        StringBuffer noticeContent = new StringBuffer();
        String content1 = systemUserBO.getUserName()
                + MessageResourceUtil.getMessageInfo("mission.you.have.mission.as.follows");
        noticeContent.append(content1);
        // 根据taskIds查询任务记录
        String ids[] = taskIds.split(",");
        String content2 = "";
        for (int i = 0; i < ids.length; i = i + 2) {
            systemTasksBO = systemTasksService.findById(ids[i]);
            content2 = content2 + (i + 2) / 2 + systemTasksBO.getTaskContent() + ";";
        }
        noticeContent.append(content2);
        String content = noticeContent.toString();
        notice = new LsSystemNoticesBO();
        notice.setNoticeId(generatePrimaryKey());
        notice.setDeployTime(new Date());
        notice.setNoticeUsers(sessionUser.getUserName());
        notice.setNoticeTitle(MessageResourceUtil.getMessageInfo("mission.send.form.contromRoom"));
        notice.setNoticeContent(content);
        notice.setNoticeState(Constant.NOTICE_STATE_PUBLISH);
        noticeService.addNotice(notice);
        return notice;
    }

    /**
     * 报警中心向巡逻队推送任务，巡逻队收到通知，显示通知模态框
     * 
     * @return
     */
    @Action(value = "msgToPatrolFromControlRoom",
            results = { @Result(name = "success", location = "/include/missionFromContromroom.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String msgToPatrolFromControlRoom() {
        return SUCCESS;
    }

    /**
     * 控制中心普通员工发起交班任务，通知主管等待审批
     * 
     * @return
     */
    @Action(value = "msgFromUserToManager",
            results = { @Result(name = "success", location = "/include/managerAudit.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String msgFromUserToManager() {
        return SUCCESS;
    }

    /**
     * 接收任务，更改负责人
     * 
     * @return
     */
    @Action(value = "handelMission", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String handelMission() {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String recId = sessionUser.getUserId();
        String ids[] = taskId.split(",");
        for (int i = 0; i < ids.length; i = i + 2) {
            // 更改任务的接收人
            systemTasksBO = systemTasksService.findById(ids[i]);
            systemTasksBO.setTaskReceiveUsers(recId);
            systemTasksService.updateTask(systemTasksBO);
            // 更改任务处理的接收人
            systemTasksDealBO = systemTasksService.findByProperty(ids[i]);
            systemTasksDealBO.setReceiveUser(recId);
            systemTasksService.updateTask(systemTasksDealBO);
        }

        return SUCCESS;
    }

    /**
     * 控制中心普通人员换班转发任务前，先向控制中心管理人员发出申请等待审核通过(选择交班对象)
     * 
     * @return
     */
    @Action(value = "chooseContromRoomUser",
            results = { @Result(name = "success", location = "/artdialogconyent/controlroomUser.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String chooseContromRoomUser() {
        request.setAttribute("taskIds", taskIds);
        return SUCCESS;
    }

    /**
     * 向controlroomManager提交审核（随机的向某个在线的controlroomManager推送通知）
     * 
     * @return
     */
    @Action(value = "forwardMisionToControlroomUser",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String forwardMisionToControlroomUser() {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String launchuser = sessionUser.getUserId();
        // 随机的选择一位在线的控制中心主管用户
        String controlRoomManagerId = systemUserService.randomFindOneManager();
        // String controlRoomManagerId = "9f6d9674-26e8-4aba-947f-8ae6399ef410";
        // 向控制中心主管发送通知(交班审核)
        notice = createReviewNotice(taskIds, controlRoomManagerId);

        JSONObject json = new JSONObject();
        json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_MANAGER_AUDIT);
        json.put("noticeId", notice.getNoticeId());
        json.put("title", notice.getNoticeTitle());
        json.put("content", notice.getNoticeContent());
        json.put("receiveUser", controlRoomManagerId);// 接收人为控制中心主管
        json.put("taskIds", taskIds);
        json.put("contromuserId", contromuserId);// 交班任务的接收人
        json.put("launchuser", launchuser);// 发起交班任务的工作人员
        WebsocketService.sendMessage(json.toString());

        return SUCCESS;
    }

    /**
     * 创建一个通知(请求控制中心管理人员审核)
     * 
     * @param taskIds
     * @param patrol
     * @return
     */
    private LsSystemNoticesBO createReviewNotice(String taskIds, String controlRoomManagerId) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        // 根据userId查出控制中心主管
        if (NuctechUtil.isNotNull(controlRoomManagerId)) {
            systemUserBO = systemUserService.findById(controlRoomManagerId);
        }
        StringBuffer noticeContent = new StringBuffer();
        String content1 = systemUserBO.getUserName() + MessageResourceUtil.getMessageInfo("mission.shift.audit");
        noticeContent.append(content1);
        // 根据taskIds查询任务记录
        String ids[] = taskIds.split(",");
        // 当前用户将任务交班给其他控制中心普通员工
        String content2 = sessionUser.getUserName()
                + MessageResourceUtil.getMessageInfo("mission.request.forward.task.to")
                + systemUserService.findById(contromuserId).getUserName()
                + MessageResourceUtil.getMessageInfo("mission.title.as.follows");
        noticeContent.append(content2);
        String content3 = "";
        for (int i = 0; i < ids.length; i = i + 2) {
            systemTasksBO = systemTasksService.findById(ids[i]);
            content3 = content3 + (i + 2) / 2 + systemTasksBO.getTaskTitle() + ";";
        }
        noticeContent.append(content3);
        String content = noticeContent.toString();
        notice = new LsSystemNoticesBO();
        notice.setNoticeId(generatePrimaryKey());
        notice.setDeployTime(new Date());
        notice.setNoticeUsers(systemUserBO.getUserName());// 通知人为控制中心的主管
        notice.setNoticeTitle(MessageResourceUtil.getMessageInfo("mission.shift.review"));
        notice.setNoticeContent(content);
        noticeService.addNotice(notice);
        return notice;
    }

    /**
     * 控制中心主管同意任务交班之后，更改任务的接收人同时向接收任务的工作人员推送通知且同时向申请交班的工作人员发出申请通过的推送消息
     * 
     * @return
     */
    @Action(value = "auditShitTask", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String auditShitTask() {
        // 变更任务的接收人
        String ids[] = taskId.split(",");
        for (int i = 0; i < ids.length; i = i + 2) {
            // 更改任务的接收人
            systemTasksBO = systemTasksService.findById(ids[i]);
            systemTasksBO.setTaskReceiveUsers(receiverId);
            systemTasksService.updateTask(systemTasksBO);
            // 更改任务处理的接收人
            systemTasksDealBO = systemTasksService.findByProperty(ids[i]);
            systemTasksDealBO.setReceiveUser(receiverId);
            systemTasksService.updateTask(systemTasksDealBO);
        }
        // 主管向申请交班的人员发送审批结果：同意交班！
        notice = createApprovalNotice(receiverId);
        JSONObject json = new JSONObject();
        json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_MANAGER_ALLOW_AUDIT);
        json.put("noticeId", notice.getNoticeId());
        json.put("title", notice.getNoticeTitle());
        json.put("content", notice.getNoticeContent());
        json.put("receiveUser", launchuser);// 接收人为交班任务的申请人
        WebsocketService.sendMessage(json.toString());

        // 主管向接收交班任务的工作人员发送提醒通知！
        notice = createNoticeToTaskReceiver(receiverId);
        JSONObject json2 = new JSONObject();
        json2.put("msgType", Constant.WEBSOCKET_DATA_TYPE_TASK_TO_RECEIVER);
        json2.put("noticeId", notice.getNoticeId());
        json2.put("title", notice.getNoticeTitle());
        json2.put("content", notice.getNoticeContent());
        json2.put("receiveUser", receiverId);// 接收人为交班任务的接收人
        WebsocketService.sendMessage(json2.toString());

        return SUCCESS;
    }

    /**
     * 控制中心主管不同意交班任务，给交班任务发起人返回通知
     * 
     * @return
     */
    @Action(value = "NotAllowShitTask",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String NotAllowShitTask() {
        notice = createNotApprovalNotice(receiverId);
        JSONObject json = new JSONObject();
        json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_MANAGER_NOT_ALLOW_AUDIT);
        json.put("noticeId", notice.getNoticeId());
        json.put("title", notice.getNoticeTitle());
        json.put("content", notice.getNoticeContent());
        json.put("receiveUser", launchuser);// 通知的接收人为交班任务的申请人
        WebsocketService.sendMessage(json.toString());
        return SUCCESS;
    }

    /**
     * 控制中心管理人员审核交班任务，通过审批后向交班发起人发送审核通过的通知
     * 
     * @param taskIds
     * @param controlRoomManagerId
     * @return
     */
    private LsSystemNoticesBO createApprovalNotice(String receiverId) {
        String content = MessageResourceUtil.getMessageInfo("mission.approval.promit.shift.task");

        notice = new LsSystemNoticesBO();
        notice.setNoticeId(generatePrimaryKey());
        notice.setDeployTime(new Date());
        notice.setNoticeUsers(receiverId);// 通知人为控制中心的主管
        notice.setNoticeTitle(MessageResourceUtil.getMessageInfo("mission.controlRoomManager.shift"));
        notice.setNoticeContent(content);
        noticeService.addNotice(notice);
        return notice;
    }

    /**
     * 控制中心管理人员审核交班任务，通过审批后向交班发起人发送审核不通过的通知
     * 
     * @param receiverId
     * @return
     */
    private LsSystemNoticesBO createNotApprovalNotice(String receiverId) {
        String content = MessageResourceUtil.getMessageInfo("mission.not.approval.promit.shift.task");

        notice = new LsSystemNoticesBO();
        notice.setNoticeId(generatePrimaryKey());
        notice.setDeployTime(new Date());
        notice.setNoticeUsers(receiverId);// 通知人为控制中心的主管
        notice.setNoticeTitle(MessageResourceUtil.getMessageInfo("mission.controlRoomManager.shift"));
        notice.setNoticeContent(content);
        noticeService.addNotice(notice);
        return notice;
    }

    /**
     * 控制中心管理人员审核交班任务，通过审批后向交班任务接收人发送接收任务的通知
     * 
     * @param receiverId
     * @return
     */
    private LsSystemNoticesBO createNoticeToTaskReceiver(String receiverId) {
        String content = MessageResourceUtil.getMessageInfo("mission.to.transfer.check.you.tasklist");

        notice = new LsSystemNoticesBO();
        notice.setNoticeId(generatePrimaryKey());
        notice.setDeployTime(new Date());
        notice.setNoticeUsers(receiverId);// 通知人为控制中心的主管
        notice.setNoticeTitle(MessageResourceUtil.getMessageInfo("mission.to.shift"));
        notice.setNoticeContent(content);
        noticeService.addNotice(notice);
        return notice;
    }

    /**
     * 控制中心审批通过后，给交班申请人发回通知消息，告知允许交班
     * 
     * @return
     */
    @Action(value = "thoughAudit", results = { @Result(name = "success", location = "/include/managerApproval.jsp"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String thoughAudit() {
        return SUCCESS;
    }

    /**
     * 控制中心审批通过后，给交班任务接收人发出提醒通知
     * 
     * @return
     */
    @Action(value = "taskToReceiver", results = { @Result(name = "success", location = "/include/msgFromManager.jsp"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String taskToReceiver() {
        return SUCCESS;
    }

    /**
     * 控制中心审批不通过后，给交班任务接收人发出提醒通知
     * 
     * @return
     */
    @Action(value = "notAllowShiftTask", results = { @Result(name = "success", location = "/include/auditNotPass.jsp"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String notAllowShiftTask() {
        return SUCCESS;
    }

    public String getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(String taskIds) {
        this.taskIds = taskIds;
    }

    public String getContromuserId() {
        return contromuserId;
    }

    public void setContromuserId(String contromuserId) {
        this.contromuserId = contromuserId;
    }

    public String getClientIPAddress() {
        return clientIPAddress;
    }

    public void setClientIPAddress(String clientIPAddress) {
        this.clientIPAddress = clientIPAddress;
    }

    public String[] getPotralUserIds() {
        return potralUserIds;
    }

    public void setPotralUserIds(String[] potralUserIds) {
        this.potralUserIds = potralUserIds;
    }

    public String getDispacthId() {
        return dispacthId;
    }

    public void setDispacthId(String dispacthId) {
        this.dispacthId = dispacthId;
    }

    public LsWarehouseDeviceDispatchBO getWarehouseDeviceDispatchBO() {
        return warehouseDeviceDispatchBO;
    }

    public void setWarehouseDeviceDispatchBO(LsWarehouseDeviceDispatchBO warehouseDeviceDispatchBO) {
        this.warehouseDeviceDispatchBO = warehouseDeviceDispatchBO;
    }

    public LsSystemDepartmentBO getSystemDepartmentBO() {
        return systemDepartmentBO;
    }

    public void setSystemDepartmentBO(LsSystemDepartmentBO systemDepartmentBO) {
        this.systemDepartmentBO = systemDepartmentBO;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getDispacthid() {
        return dispacthid;
    }

    public void setDispacthid(String dispacthid) {
        this.dispacthid = dispacthid;
    }

    public List<LsWarehouseElockBO> getElockDetailLit() {
        return elockDetailLit;
    }

    public void setElockDetailLit(List<LsWarehouseElockBO> elockDetailLit) {
        this.elockDetailLit = elockDetailLit;
    }

    public List<LsWarehouseEsealBO> getEsealDetailLit() {
        return esealDetailLit;
    }

    public void setEsealDetailLit(List<LsWarehouseEsealBO> esealDetailLit) {
        this.esealDetailLit = esealDetailLit;
    }

    public List<LsWarehouseSensorBO> getSensorDetailLit() {
        return sensorDetailLit;
    }

    public void setSensorDetailLit(List<LsWarehouseSensorBO> sensorDetailLit) {
        this.sensorDetailLit = sensorDetailLit;
    }

    public String[] getUserIds() {
        return userIds;
    }

    public void setUserIds(String[] userIds) {
        this.userIds = userIds;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getLaunchuser() {
        return launchuser;
    }

    public void setLaunchuser(String launchuser) {
        this.launchuser = launchuser;
    }

    public SystemModules getSystemModules() {
        return systemModules;
    }

    public void setSystemModules(SystemModules systemModules) {
        this.systemModules = systemModules;
    }
}
