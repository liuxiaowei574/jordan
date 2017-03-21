package com.nuctech.ls.noticeandtask.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemNoticeLogBO;
import com.nuctech.ls.model.bo.system.LsSystemNoticesBO;
import com.nuctech.ls.model.service.SystemNoticeLogService;
import com.nuctech.ls.model.service.SystemNoticeService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;

import net.sf.json.JSONObject;

@Namespace("/undealNotice")
public class NoticeDialogAction extends LSBaseAction {

    private static final long serialVersionUID = 1L;

    @Resource
    private SystemNoticeService noticeService;
    private static final String DEFAULT_SORT_COLUMNS = "t.deployTime desc";
    private String[] noticeIds;
    private String userId;
    @Resource
    private SystemNoticeLogService systemNoticeLogService;
    // 查询用户未读取通知的数量
    private int needDealNoticeCount;

    /**
     * 显示非模态框
     * 
     * @return
     */
    @Action(value = "toList", results = { @Result(name = "success", location = "/artdialogconyent/notice.jsp") })
    public String toList() {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String userId = sessionUser.getUserId();
        request.setAttribute("userId", userId);
        return SUCCESS;
    }

    /**
     * 显示未当前用户未得处理通知列表
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
        JSONObject retJson = noticeService.findUnDealNoticeList(pageQuery, null, false, userId);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /**
     * 处理"已读"的通知
     * 
     * @return
     * @throws IOException
     */
    @Action(value = "dealNotice",
            results = { @Result(name = "success", type = "json", params = { "root", "needDealNoticeCount" }) })
    public String dealNotice() throws IOException {
        if (noticeIds != null) {
            HttpSession session = request.getSession();
            String s[] = noticeIds[0].split(",");
            for (int i = 0; i < s.length; i++) {
                // 根据通知id和接收人(登录用户)来查询通知日志表,更改通知日志表中通知的状态为"已处理"；
                LsSystemNoticeLogBO noticeLog;
                try {
                    noticeLog = noticeService.findByIdAndReceiver(s[i], userId);
                    noticeLog.setDealType("1");
                    noticeLog.setReceiveTime(new Date());
                    noticeService.updateNotice(noticeLog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 根据通知Id查询通知表，更改通知表中的通知状态为"完成"
                LsSystemNoticesBO systemNoticesBO = noticeService.findById(s[i]);
                systemNoticesBO.setNoticeState("2");
                noticeService.updateNotice(systemNoticesBO);
                // 将通知标记为"已读"查询未读通知数量，并通过ajax传值，把值传到前台
                SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                String userId = sessionUser.getUserId();
                needDealNoticeCount = systemNoticeLogService.findCount(userId);
                session.setAttribute(Constant.needDealNoticeCount, needDealNoticeCount);
                /*
                 * 这种往后台传值的方式不建议使用，可以使用params = { "root", "needDealNoticeCount"
                 * })的方法 JSONObject retJson = new JSONObject();
                 * retJson.put("needDealNoticeCount", needDealNoticeCount);
                 * response.setCharacterEncoding("UTF-8");
                 * response.setContentType("text/html"); PrintWriter out =
                 * response.getWriter(); out.print(retJson.toString());
                 * out.flush(); out.close();
                 */
            }
        }
        return SUCCESS;
    }

    public String[] getNoticeIds() {
        return noticeIds;
    }

    public void setNoticeIds(String[] noticeIds) {
        this.noticeIds = noticeIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getNeedDealNoticeCount() {
        return needDealNoticeCount;
    }

    public void setNeedDealNoticeCount(int needDealNoticeCount) {
        this.needDealNoticeCount = needDealNoticeCount;
    }
}
