package com.nuctech.ls.center.device.action;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.center.websocket.WebsocketService;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemNoticesBO;
import com.nuctech.ls.model.bo.system.LsSystemTasksBO;
import com.nuctech.ls.model.bo.system.LsSystemTasksDealBO;
import com.nuctech.ls.model.service.SystemNoticeService;
import com.nuctech.ls.model.service.SystemTasksService;
import com.nuctech.ls.model.util.NoticeType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;

import net.sf.json.JSONObject;

/**
 * 
 * 作者： 赵苏阳
 *
 * 描述：followup user接到护送巡逻队(护送车队)到达临界区域时的通知时，选择其他巡逻队进行护送任务的交接
 * 
 * 创建时间：2016年10月25日
 */
@Namespace("/escortHandOver")
public class EscortHandOverAction extends LSBaseAction {

    private static final long serialVersionUID = 1L;
    private LsSystemNoticesBO notice;
    @Resource
    private SystemNoticeService noticeService;
    private LsSystemTasksDealBO systemTasksDealBO;
    @Resource
    private SystemTasksService systemTasksService;

    /**
     * 填写护送交接消息的模态框
     * 
     * @return
     */
    @Action(value = "escortHandOverTaskModal",
            results = { @Result(name = "success", location = "/include/escortHandOverNoticeModal.jsp") })
    public String escortHandOverTaskModal() {
        return SUCCESS;
    }

    @Action(value = "transferEscortTask",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String transferEscortTask() {
        if (notice != null) {
            // 1:followUp user创建一个交接护送车队的通知，将该通知存入系统通知表
            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            notice.setPublisher(sessionUser.getUserId());
            notice.setNoticeId(generatePrimaryKey());
            notice.setDeployTime(new Date());
            notice.setNoticeType(NoticeType.TripEscortNotice.getType());// 通知类型为行程护送
            noticeService.addNotice(notice);
            // 2:将该通知发送给选中的巡逻队(websocket)
            String receiver = notice.getNoticeUsers();// 通知接收巡逻队
            String receiveuser[] = receiver.split(",");
            for (int i = 0; i < receiveuser.length; i++) {
                JSONObject json = new JSONObject();
                json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_ESCORT_HANDOVER);
                json.put("noticeId", notice.getNoticeId());
                json.put("title", notice.getNoticeTitle());
                json.put("content", notice.getNoticeContent());
                json.put("receiveUser", receiveuser[i]);
                WebsocketService.sendMessage(json.toString());
            }
            // 3：将交接前的调度任务标记为已完成（需要传一个任务的id）
            String id = null;// 任务的id
            systemTasksDealBO = systemTasksService.findByProperty(id);
            systemTasksDealBO.setDealType("1");// 任务已完成

            // 4：为接收交接任务(设备调度交接)的巡逻队新建一个待办任务
            // 任务
            LsSystemTasksBO systemTasksBO = new LsSystemTasksBO();
            systemTasksBO.setTaskId(generatePrimaryKey());
            systemTasksBO.setDeployTime(new Date());
            systemTasksBO.setPriority("1");
            systemTasksBO.setPublisher(sessionUser.getUserId());
            systemTasksBO.setTaskContent(notice.getNoticeContent());
            systemTasksBO.setTaskTitle(notice.getNoticeTitle());
            systemTasksBO.setTaskReceiveUsers(receiveuser.toString());
            systemTasksService.addTask(systemTasksBO);
            // 任务处理
            LsSystemTasksDealBO tasksDealBO = new LsSystemTasksDealBO();
            tasksDealBO.setTaskDealId(generatePrimaryKey());
            tasksDealBO.setDealType("0");
            tasksDealBO.setReceiveUser(receiveuser.toString());
            tasksDealBO.setTaskId(systemTasksBO.getTaskId());
            systemTasksService.addTaskDeal(tasksDealBO);
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    @Action(value = "handOverTask", results = { @Result(name = "success", location = "/include/escortHandOver.jsp"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String handOverTask() {
        return SUCCESS;
    }

    public LsSystemNoticesBO getNotice() {
        return notice;
    }

    public void setNotice(LsSystemNoticesBO notice) {
        this.notice = notice;
    }

}
