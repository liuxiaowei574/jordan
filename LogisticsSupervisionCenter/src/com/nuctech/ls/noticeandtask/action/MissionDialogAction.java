package com.nuctech.ls.noticeandtask.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemTasksBO;
import com.nuctech.ls.model.bo.system.LsSystemTasksDealBO;
import com.nuctech.ls.model.service.SystemNoticeService;
import com.nuctech.ls.model.service.SystemTasksService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Namespace("/undealMission")
public class MissionDialogAction extends LSBaseAction {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_SORT_COLUMNS = "t.deployTime desc";
    private String userId;
    private String[] taskIds;
    @Resource
    private SystemNoticeService noticeService;

    @Resource
    private SystemTasksService tasksService;
    private List<?> missionList = new ArrayList();// 未处理任务

    private List<LsSystemTasksBO> alarmMissionList;// 未处理的报警类型的任务
    private List<LsSystemTasksBO> escortMissionList;// 未处理的巡逻队护送任务
    private List<LsSystemTasksBO> dispatchMissionList;// 未处理的设备调度任务

    /**
     * 显示待处理任务的dialog
     * 
     * @return
     */
    @Action(value = "toList", results = { @Result(name = "success", location = "/artdialogconyent/mission.jsp") })
    public String toList() {
        // 如果是控制中心的主管，则在任务列表页面显示"选择巡逻队按钮——给巡逻队推送任务"，如果是控制中心普通员工页面显示"已处理"和"交班任务按钮"，其他员工如巡逻队应该只有"已处理"按钮
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String userId = sessionUser.getUserId();
        request.setAttribute("userId", userId);
        request.setAttribute("roleName", sessionUser.getRoleName());

        // 如果是控制中心人员有权看到所有的任务
        if (sessionUser.getRoleName().equals("contromRoomManager")) {
            missionList = tasksService.findUndealMissionList();
        } else {
            missionList = tasksService.findUndealMissionList(sessionUser.getUserId());
        }
        // 判断任务数量
        request.setAttribute("taskNumber", missionList.size());
        return SUCCESS;
    }

    /**
     * 显示待办任务列表
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Action(value = "list")
    public void list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        // 当前登陆用户为通知的接收人
        String userId = sessionUser.getUserId();
        // 需求114:报警中心(contromRoomManager?)有权向巡逻队推送任务,所以该角色用户登录需要看到系统中所有的待办任务
        String roleName = sessionUser.getRoleName();
        JSONObject retJson = new JSONObject();
        if (roleName.equals("contromRoomManager")) {
            retJson = tasksService.findUndealMissionList(pageQuery, null, false);
        } else {
            retJson = tasksService.findUndealMissionList(pageQuery, null, false, userId);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /**
     * 处理任务，将已处理的任务标记为已读
     * 
     * @return
     * @throws IOException
     */
    @Action(value = "dealMission", results = { @Result(name = "success", type = "json") })
    public void dealMission() throws IOException {
        if (taskIds != null) {
            String s[] = taskIds[0].split(",");
            JSONObject a = new JSONObject();
            a.put("type", s[1]);
            String result = a.toString();
            this.response.getWriter().println(result);
            for (int i = 0; i < s.length; i += 2) {
                // 根据通知id和接收人(登录用户)来查询通知日志表
                LsSystemTasksDealBO tasksDealBO = tasksService.findByIdAndReceiver(s[i], userId);
                tasksDealBO.setDealType("1");
                tasksService.updateTask(tasksDealBO);
            }
        }
    }

    /**
     * 查询"报警处理类型"的任务且接收人为登陆用户
     * 
     * @throws Exception
     */
    @Action(value = "findUndealAlarmMission", results = { @Result(name = "success", type = "json") })
    public void findUndealAlarmMission() throws Exception {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String userId = sessionUser.getUserId();
        alarmMissionList = tasksService.findAlarmMissionList(userId);
        String result = JSONArray.fromObject(alarmMissionList).toString();
        this.response.getWriter().println(result);
    }

    /**
     * 查询所有未处理的"报警类型"的任务
     * 
     * @throws Exception
     */
    @Action(value = "findAllUndealAlarmMission", results = { @Result(name = "success", type = "json") })
    public void findAllUndealAlarmMission() throws Exception {
        alarmMissionList = tasksService.findAllAlarmMissionList();
        String result = JSONArray.fromObject(alarmMissionList).toString();
        this.response.getWriter().println(result);
    }

    /**
     * 查询巡逻队护送任务(接收人为登陆用户)
     * 
     * @throws Exception
     */
    @Action(value = "findUndealEscortMission", results = { @Result(name = "success", type = "json") })
    public void findUndealEscortMission() throws Exception {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String userId = sessionUser.getUserId();
        escortMissionList = tasksService.findEscortMissionList(userId);
        String result = JSONArray.fromObject(escortMissionList).toString();
        this.response.getWriter().println(result);
    }

    /**
     * 查询所有的巡逻队护送类型的任务
     * 
     * @throws Exception
     */
    @Action(value = "findAllUndealEscortMission", results = { @Result(name = "success", type = "json") })
    public void findAllUndealEscortMission() throws Exception {
        escortMissionList = tasksService.findEscortMissionList();
        String result = JSONArray.fromObject(escortMissionList).toString();
        this.response.getWriter().println(result);
    }

    /**
     * 查询设备调度任务(任务接收人为登陆用户)
     * 
     * @throws Exception
     */
    @Action(value = "findUndealDispatchMission", results = { @Result(name = "success", type = "json") })
    public void findUndealDispatchMission() throws Exception {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String userId = sessionUser.getUserId();
        dispatchMissionList = tasksService.findDispatchMissionList(userId);
        String result = JSONArray.fromObject(dispatchMissionList).toString();
        this.response.getWriter().println(result);
    }

    /**
     * 查询所有的调度任务
     * 
     * @throws Exception
     */
    @Action(value = "findAllUndealDispatchMission", results = { @Result(name = "success", type = "json") })
    public void findAllUndealDispatchMission() throws Exception {
        dispatchMissionList = tasksService.findDispatchMissionList();
        String result = JSONArray.fromObject(dispatchMissionList).toString();
        this.response.getWriter().println(result);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String[] getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(String[] taskIds) {
        this.taskIds = taskIds;
    }

    public List<?> getMissionList() {
        return missionList;
    }

    public void setMissionList(List<?> missionList) {
        this.missionList = missionList;
    }

    public List<LsSystemTasksBO> getAlarmMissionList() {
        return alarmMissionList;
    }

    public void setAlarmMissionList(List<LsSystemTasksBO> alarmMissionList) {
        this.alarmMissionList = alarmMissionList;
    }

    public List<LsSystemTasksBO> getEscortMissionList() {
        return escortMissionList;
    }

    public void setEscortMissionList(List<LsSystemTasksBO> escortMissionList) {
        this.escortMissionList = escortMissionList;
    }
}
