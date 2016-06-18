package com.nuctech.ls.system.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.center.websocket.WebsocketService;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemNoticeLogBO;
import com.nuctech.ls.model.bo.system.LsSystemNoticesBO;
import com.nuctech.ls.model.service.SystemNoticeService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.system.SystemNoticeLogVO;
import com.nuctech.ls.model.vo.ztree.TreeNode;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>通知 Action</p>
 * 创建时间：2016年5月27日
 */
@Namespace("/notice")
public class NoticeAction extends LSBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6334232470226373348L;
	
	Logger logger = Logger.getLogger(NoticeAction.class);
	
	/**
	 * 列表排序字段
	 */
	private static final String DEFAULT_SORT_COLUMNS = "noticeId ASC";
	
	@Resource
	private SystemNoticeService noticeService;
	@Resource
	private SystemUserService userService;
	
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
	@Action(value="index", results = {
			@Result(name = "success", location = "/system/notice/list.jsp")
	})
	public String index() {
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value="list")
	public void list() throws IOException{
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);		
		JSONObject retJson = noticeService.findNoticeList(pageQuery,null,false);		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");		
		PrintWriter out = response.getWriter(); 
		out.print(retJson.toString());
		out.flush();
		out.close();
	}
	
	@Action(value = "addModal", results = {
			@Result(name = "success", location = "/system/notice/add.jsp")})
	public String addModal() {
		notice = new LsSystemNoticesBO();
		return SUCCESS;
	}
	
	@Action(value = "editModal", results = {
			@Result(name = "success", location = "/system/notice/edit.jsp")})
	public String editModal() {
		if(notice != null) {
			String noticeId = notice.getNoticeId();
			if(!NuctechUtil.isNull(noticeId)) {
				notice = noticeService.findById(noticeId);
				noticeUsersName = noticeService.findNoticeUsersNameById(noticeId);
				//systemUser = systemUserService.findById(userId);
				return SUCCESS;
			} else {
				return ERROR;
			}
		} else {
			return ERROR;
		}
	}
	
	@Action(value="findUserTree", results={
			@Result(name="success", type="json")
	})
	public String findUserTree() throws Exception {
		userTreeList = noticeService.findUserTree();
		return SUCCESS;
	}
	
	@Action(value = "addNotice", results = {
			@Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "false" }) })
	public String addNotice() {
		if(notice != null) {
			SessionUser sessionUser = (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
			if(sessionUser == null) {
				logger.error("请登录系统");
				return ERROR;
			}
			notice.setPublisher(sessionUser.getUserId());
			noticeService.addNotice(notice);
            return SUCCESS;
		} else {
            return ERROR;
		}
	}
	
	@Action(value = "editNotice", results = {
			@Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "false" }) })
	public String editNotice() {
		if(notice != null) {
			SessionUser sessionUser = (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
			if(sessionUser == null) {
				logger.error("请登录系统");
				return ERROR;
			}
			notice.setPublisher(sessionUser.getUserId());
			noticeService.modifyNotice(notice);
			return SUCCESS;
		} else {
			return ERROR;
		}
	}
	
	@Action(value = "toReceiveUserListModal", results = {
			@Result(name = "success", location = "/system/notice/receive_user_list.jsp")})
	public String toReceiveUserListModal() {
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	@Action(value="findReceiveUserList")
	public void findReceiveUserList() throws Exception {
		if(notice != null) {
			String noticeId = notice.getNoticeId();
			if(!NuctechUtil.isNull(noticeId)) {
				//systemUser = systemUserService.findById(userId);
				pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);		
				JSONObject retJson = noticeService.findReceiveUserList(noticeId, pageQuery);		
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");		
				PrintWriter out = response.getWriter(); 
				out.print(retJson.toString());
				out.flush();
				out.close();
			} 
		}
	}
	
	@Action(value = "publish", results = {
			@Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "false" }) })
	public String publish() {
		if(NuctechUtil.isNull(ids)) {
			message = "发布通知,参数[ids]为空";
			logger.error("发布通知,参数[ids]为空");
			return ERROR;
		} else {
			try {
				noticeService.publish(ids);
				/**
				 * 获取通知主题和内容进行推送
				 */
				LsSystemNoticesBO sysNotice = noticeService.findById(ids[0]);
				if(sysNotice != null) {
					JSONObject json = new JSONObject();
					json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_NOTICE);
					json.put("noticeId", sysNotice.getNoticeId());
			        json.put("title", sysNotice.getNoticeTitle());
			        json.put("content", sysNotice.getNoticeContent());
			        json.put("receiveUser", sysNotice.getNoticeUsers());
			        WebsocketService.sendMessage(json.toString());
				}
				//userLog("reset user password", ids.toString());
			} catch (Exception e) {
				message = e.getMessage();
				logger.error("发布通知异常", e);
				return ERROR;
			}
			return SUCCESS;
		}
	}
	
	@Action(value = "delete", results = {
			@Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "false" }) })
	public String delete() {
		if(NuctechUtil.isNull(ids)) {
			message = "删除通知,参数[ids]为空";
			logger.error("删除通知,参数[ids]为空");
			return ERROR;
		} else {
			try {
				noticeService.delete(ids);
			} catch (Exception e) {
				message = e.getMessage();
				logger.error("删除通知异常", e);
				return ERROR;
			}
			return SUCCESS;
		}
	}
	
	@Action(value="noticeRead")
	public void noticeRead() {
		if(log != null) {
			noticeService.readNotice(log);
		}
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

}
