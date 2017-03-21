package com.nuctech.ls.system.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.center.websocket.WebsocketService;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemNoticeLogBO;
import com.nuctech.ls.model.bo.system.LsSystemNoticesBO;
import com.nuctech.ls.model.service.SystemNoticeLogService;
import com.nuctech.ls.model.service.SystemNoticeService;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.util.NoticeType;
import com.nuctech.ls.model.util.OperateContentType;
import com.nuctech.ls.model.util.OperateEntityType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.system.SystemNoticeLogVO;
import com.nuctech.ls.model.vo.ztree.TreeNode;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 通知 Action
 * </p>
 * 创建时间：2016年5月27日
 */
@Namespace("/notice")
public class NoticeAction extends LSBaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = -6334232470226373348L;

    Logger logger = Logger.getLogger(NoticeAction.class);
    // 查询用户未读取通知的数量
    private int needDealNoticeCount;
    @Resource
    private SystemNoticeLogService systemNoticeLogService;
    /**
     * 列表排序字段
     */
    private static final String DEFAULT_SORT_COLUMNS = "deployTime desc";
    private static final String DEFAULT_SORT_COLUMNS_ALIAS = "t.deployTime desc";

    @Resource
    private SystemNoticeService noticeService;
    @Resource
    private SystemUserService userService;
    @Resource
    private SystemOperateLogService logService;

    private LsSystemNoticesBO notice;
    private List<TreeNode> userTreeList;

    /**
     * 接收通知列表vo List
     */
    private List<SystemNoticeLogVO> noticeLogVoList;
    private String ids[];
    private String noticeUsersName;
    private LsSystemNoticeLogBO log;

    /**
     * 通知首页链接 返回到通知的首页
     * 
     * @return
     */
    @Action(value = "index", results = { @Result(name = "success", location = "/system/notice/list.jsp") })
    public String index() {
        return SUCCESS;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "list")
    public void list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = noticeService.findNoticeList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    @Action(value = "addModal", results = { @Result(name = "success", location = "/system/notice/add.jsp") })
    public String addModal() {
        notice = new LsSystemNoticesBO();
        return SUCCESS;
    }

    @Action(value = "editModal", results = { @Result(name = "success", location = "/system/notice/edit.jsp") })
    public String editModal() {
        if (notice != null) {
            String noticeId = notice.getNoticeId();
            if (!NuctechUtil.isNull(noticeId)) {
                notice = noticeService.findById(noticeId);
                noticeUsersName = noticeService.findNoticeUsersNameById(noticeId);
                // systemUser = systemUserService.findById(userId);
                return SUCCESS;
            } else {
                return ERROR;
            }
        } else {
            return ERROR;
        }
    }

    @Action(value = "findUserTree", results = { @Result(name = "success", type = "json") })
    public String findUserTree() throws Exception {
        userTreeList = noticeService.findReportUserTree();
        return SUCCESS;
    }

    /**
     * 查询当前用户国家的所有口岸用户
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "findPortUserTree", results = { @Result(name = "success", type = "json") })
    public String findPortUserTree() throws Exception {
        userTreeList = noticeService.findPortUserTree();
        return SUCCESS;
    }

    /**
     * 日志记录方法
     * 
     * @param operate
     *        操作内容对象
     * @param entity
     *        操作实体对象
     */
    private void userLog(String operate, String entity, String params) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        logService.addLog(operate, entity, sessionUser.getUserId(), UserAction.class.toString(), params);
    }

    @Action(value = "addNotice", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String addNotice() {
        if (notice != null) {
            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            if (sessionUser == null) {
                logger.error("请登录系统");
                return ERROR;
            }
            notice.setPublisher(sessionUser.getUserId());
            notice.setNoticeType(NoticeType.NomalNotice.getType());// 用户添加的通知为普通通知
            noticeService.addNotice(notice);
            userLog(OperateContentType.ADD.toString(), OperateEntityType.NOTICE.toString(), notice.toString());
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    @Action(value = "editNotice", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String editNotice() {
        if (notice != null) {
            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            if (sessionUser == null) {
                logger.error("请登录系统");
                return ERROR;
            }
            notice.setPublisher(sessionUser.getUserId());
            noticeService.modifyNotice(notice);
            userLog(OperateContentType.EDIT.toString(), OperateEntityType.NOTICE.toString(), notice.toString());
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    @Action(value = "toReceiveUserListModal",
            results = { @Result(name = "success", location = "/system/notice/receive_user_list.jsp") })
    public String toReceiveUserListModal() {
        return SUCCESS;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "findReceiveUserList")
    public void findReceiveUserList() throws Exception {
        String noticeId = request.getParameter("s_noticeId");
        if (!NuctechUtil.isNull(noticeId)) {
            // systemUser = systemUserService.findById(userId);
            pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_ALIAS);
            String sortname = request.getParameter("sort");
            if ("userName".equals(sortname)) {
                pageQuery = this.newPageQuery("u.userName asc");
            }
            JSONObject retJson = noticeService.findReceiveUserList(noticeId, pageQuery);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print(retJson.toString());
            out.flush();
            out.close();
        }
    }

    /**
     * 判断该通知是否有接收人
     * 
     * @return
     */
    @Action(value = "judgePublish", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String judgePublish() {
        if (NuctechUtil.isNull(ids)) {
            message = "发布通知,参数[ids]为空";
            logger.error("发布通知,参数[ids]为空");
            return ERROR;
        } else {
            LsSystemNoticesBO sysNotice = noticeService.findById(ids[0]);
            if (sysNotice != null) {
                if (!(sysNotice.getNoticeUsers().length() > 0)) {
                    logger.error("通知接收人为空！");
                    return ERROR;
                }
            }
        }
        return SUCCESS;
    }

    /**
     * 通知发布
     * 
     * @return
     */
    @Action(value = "publish", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String publish() {
        if (NuctechUtil.isNull(ids)) {
            message = "发布通知,参数[ids]为空";
            logger.error("发布通知,参数[ids]为空");
            return ERROR;
        } else {
            try {
                /**
                 * 获取通知主题和内容进行推送
                 */
                LsSystemNoticesBO sysNotice = noticeService.findById(ids[0]);
                if (sysNotice != null) {
                    // 修改通知状态
                    noticeService.publish(ids);
                    userLog(OperateContentType.PUBLISH.toString(), OperateEntityType.NOTICE.toString(), ids.toString());
                    // 推送相关接收人
                    JSONObject json = new JSONObject();
                    json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_NOTICE);
                    json.put("noticeId", sysNotice.getNoticeId());
                    json.put("title", sysNotice.getNoticeTitle());
                    json.put("content", sysNotice.getNoticeContent());
                    json.put("receiveUser", sysNotice.getNoticeUsers());
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

    @Action(value = "delete", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String delete() {
        if (NuctechUtil.isNull(ids)) {
            message = "删除通知,参数[ids]为空";
            logger.error("删除通知,参数[ids]为空");
            return ERROR;
        } else {
            try {
                String[] idArr = ids[0].split(",");
                noticeService.delete(idArr);
                userLog(OperateContentType.DELETE.toString(), OperateEntityType.NOTICE.toString(), ids.toString());
            } catch (Exception e) {
                message = e.getMessage();
                logger.error("删除通知异常", e);
                return ERROR;
            }
            return SUCCESS;
        }
    }

    @Action(value = "noticeRead")
    public void noticeRead() {
        if (log != null) {
            noticeService.readNotice(log);
        }
        // 控制中心用户接收到通知，更新左下角的通知图标上显示的通知数量。
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String userId = sessionUser.getUserId();
        HttpSession session = request.getSession();
        needDealNoticeCount = systemNoticeLogService.findCount(userId);
        session.setAttribute(Constant.needDealNoticeCount, needDealNoticeCount);
    }

    /**
     * 给用户tree分级（添加所属中心）
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "findNewUserTree", results = { @Result(name = "success", type = "json") })
    public String findNewUserTree() throws Exception {
        userTreeList = noticeService.findNewUserTree();
        return SUCCESS;
    }

    @Action(value = "sysMsgModal", results = { @Result(name = "success", location = "/include/sysMsgModal.jsp"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String sysMsgModal() {
        return SUCCESS;
    }

    public LsSystemNoticesBO getNotice() {
        return notice;
    }

    public void setNotice(LsSystemNoticesBO notice) {
        this.notice = notice;
    }

    public List<TreeNode> getUserTreeList() {
        return userTreeList;
    }

    public void setUserTreeList(List<TreeNode> userTreeList) {
        this.userTreeList = userTreeList;
    }

    public List<SystemNoticeLogVO> getNoticeLogVoList() {
        return noticeLogVoList;
    }

    public void setNoticeLogVoList(List<SystemNoticeLogVO> noticeLogVoList) {
        this.noticeLogVoList = noticeLogVoList;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getNoticeUsersName() {
        return noticeUsersName;
    }

    public void setNoticeUsersName(String noticeUsersName) {
        this.noticeUsersName = noticeUsersName;
    }

    public LsSystemNoticeLogBO getLog() {
        return log;
    }

    public void setLog(LsSystemNoticeLogBO log) {
        this.log = log;
    }

    public int getNeedDealNoticeCount() {
        return needDealNoticeCount;
    }

    public void setNeedDealNoticeCount(int needDealNoticeCount) {
        this.needDealNoticeCount = needDealNoticeCount;
    }
}
