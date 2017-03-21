package com.nuctech.ls.center.device.action;

import java.util.Date;
import java.util.List;

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
import com.nuctech.ls.model.vo.ztree.TreeNode;
import com.nuctech.util.Constant;

import net.sf.json.JSONObject;

/**
 * 
 * 作者： 赵苏阳
 *
 * 描述：控制中心接到巡逻队(调度)到达临界区域时的通知时，选择其他巡逻队进行调度任务的交接
 * 
 * 创建时间：2016年10月24日
 */
@Namespace("/dispatchHandOver")
public class DispatchHandOverAction extends LSBaseAction {

    private static final long serialVersionUID = 1L;
    private List<TreeNode> userTreeList;
    @Resource
    private SystemNoticeService noticeService;
    private LsSystemNoticesBO notice;
    @Resource
    private SystemTasksService systemTasksService;
    private LsSystemTasksDealBO systemTasksDealBO;

    /**
     * 给准备交接调度任务的巡逻队发送通知（模态框）
     * 
     * @return
     */
    @Action(value = "handOverTaskModal",
            results = { @Result(name = "success", location = "/include/escortHandOverNoticeModal.jsp") })
    public String handOverTaskModal() {
        return SUCCESS;
    }

    /**
     * 通知人员对象是巡逻队角色
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "findPatrolTree", results = { @Result(name = "success", type = "json") })
    public String findPatrolTree() throws Exception {
        userTreeList = noticeService.findpatrolTree();
        return SUCCESS;
    }

    @Action(value = "transferDispatchTask",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String transferDispatchTask() {
        if (notice != null) {
            // 1:控制中心创建一个交接调度设备的通知，将该通知存入系统通知表
            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            notice.setPublisher(sessionUser.getUserId());
            notice.setNoticeId(generatePrimaryKey());
            notice.setDeployTime(new Date());
            notice.setNoticeType(NoticeType.DispatchNotice.getType());// 控制中心添加的通知为调度通知
            noticeService.addNotice(notice);
            // 2:将该通知发送给选中的巡逻队(websocket)
            String receiver = notice.getNoticeUsers();// 通知接收巡逻队
            String receiveuser[] = receiver.split(",");
            for (int i = 0; i < receiveuser.length; i++) {
                JSONObject json = new JSONObject();
                json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_DISPATCH_HANDOVER);
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

    /**
     * 向巡逻队发出交接调度任务的通知
     * 
     * @return
     */
    @Action(value = "handOverTask", results = { @Result(name = "success", location = "/include/dispatchHandOver.jsp"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String handOverTask() {
        return SUCCESS;
    }

    public List<TreeNode> getUserTreeList() {
        return userTreeList;
    }

    public void setUserTreeList(List<TreeNode> userTreeList) {
        this.userTreeList = userTreeList;
    }

    public LsSystemNoticesBO getNotice() {
        return notice;
    }

    public void setNotice(LsSystemNoticesBO notice) {
        this.notice = notice;
    }

}
