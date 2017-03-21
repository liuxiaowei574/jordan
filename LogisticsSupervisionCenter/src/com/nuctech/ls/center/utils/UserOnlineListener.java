package com.nuctech.ls.center.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoader;

import com.nuctech.ls.center.websocket.WebsocketService;
import com.nuctech.ls.model.bo.system.LsSystemUserLogBO;
import com.nuctech.ls.model.service.SystemUserLogService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;

/**
 * 用户是否已登录监听类
 * 
 * @author liuchao
 *
 */
public class UserOnlineListener implements HttpSessionListener {

	// private static final Logger logger =
	// Logger.getLogger(UserOnlineListener.class);

	private static Map<String, String> onlineUsers = new HashMap<String, String>();
	private static Map<String, HttpSession> onlineSessions = new HashMap<String, HttpSession>();

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		String sessionId = session.getId();
		String onlineUserLogId = (String) session.getAttribute(Constant.USER_LOG_ID);
		if (!NuctechUtil.isNull(onlineUserLogId)) {
			timeoutNotice(sessionId);
			SystemUserLogService systemUserLogService = ContextLoader.getCurrentWebApplicationContext().getBean(SystemUserLogService.class);
			LsSystemUserLogBO systemUserLog = systemUserLogService.findById(onlineUserLogId);
			if (systemUserLog != null) {
				systemUserLog.setLogoutTime(new Date());
				systemUserLog.setLogoutType(LogoutType.SESSION_TIMEOUT.getValue());
				systemUserLogService.modify(systemUserLog);
			}
		}
		SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
		if (sessionUser != null) {
			session.removeAttribute(Constant.SESSION_USER);
		}
		onlineUsers.remove(sessionId);
		onlineSessions.remove(sessionId);
	}

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {

	}

	public static boolean isAlreadyEnter(HttpSession session, String sUserName) {
		boolean flag = false;
		String newSessionId = session.getId();
		//如果和之前是同一次会话，不需处理
		if(onlineUsers.containsKey(newSessionId) && onlineUsers.get(newSessionId).equals(sUserName)) {
			return true;
		}
		if (onlineUsers.containsValue(sUserName)) {
			flag = true;
			List<String> sessionIds = new ArrayList<>(); // 被踢下线的sessionId
			List<HttpSession> sessions = new ArrayList<>(); // 被踢下线的session
			Iterator<String> iter = onlineUsers.keySet().iterator();
			while (iter.hasNext()) {
				String sessionId = iter.next();
				if (onlineUsers.get(sessionId).equals(sUserName)) {
					sessionIds.add(sessionId);
					sessions.add(onlineSessions.get(sessionId));
					iter.remove();
					onlineSessions.remove(sessionId);
				}
			}
			if (sessionIds.size() > 0) {
				kickOutNotice(sessionIds, sessions);
			}
			onlineUsers.put(session.getId(), sUserName);
			onlineSessions.put(session.getId(), session);
		} else {
			flag = false;
			onlineUsers.put(session.getId(), sUserName);
			onlineSessions.put(session.getId(), session);
		}
		return flag;

	}

	public static boolean isOnline(HttpSession session) {
		boolean flag = true;
		if (onlineUsers.containsKey(session.getId())) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * 会话超时通知
	 * 
	 * @param sessionIds
	 */
	private static void timeoutNotice(String sessionId) {
		List<String> sessionIds = new ArrayList<>();
		sessionIds.add(sessionId);
		sendOfflineMsg(sessionIds, "common.message.session.timeout");
	}

	/**
	 * 踢出通知
	 * 
	 * @param sessionIds
	 */
	private static void kickOutNotice(List<String> sessionIds, List<HttpSession> sessions) {
		sendOfflineMsg(sessionIds, "common.message.user.isonline");
		modifyUserLog(sessions);
	}

	private static void modifyUserLog(List<HttpSession> sessions) {
		for (HttpSession session : sessions) {
			if (null != session) {
				String onlineUserLogId = (String) session.getAttribute(Constant.USER_LOG_ID);
				if (!NuctechUtil.isNull(onlineUserLogId)) {
					SystemUserLogService systemUserLogService = ContextLoader.getCurrentWebApplicationContext().getBean(SystemUserLogService.class);
					LsSystemUserLogBO systemUserLog = systemUserLogService.findById(onlineUserLogId);
					if (systemUserLog != null) {
						systemUserLog.setLogoutTime(new Date());
						systemUserLog.setLogoutType(LogoutType.KICKOUT.getValue());
						systemUserLogService.modify(systemUserLog);
					}
					session.removeAttribute(Constant.USER_LOG_ID);
				}
				SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
				if (sessionUser != null) {
					session.removeAttribute(Constant.SESSION_USER);
				}
				session.invalidate();
			}
		}
	}

	/**
	 * 向指定的session用户推送下线消息
	 * 
	 * @param sessionIds
	 *            sessionId集合
	 * @param message
	 *            消息内容
	 */
	private static void sendOfflineMsg(List<String> sessionIds, String message) {
		JSONObject json = new JSONObject();
		json.put("msgType", Constant.WEBSOCKET_DATA_TYPE_OFFLINE);
		json.put("content", message); // 资源文件key
		json.put("receiveUser", StringUtils.join(sessionIds, ","));
		WebsocketService.sendMessage(json.toString());
	}
}
