package com.nuctech.ls.center.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.web.context.ContextLoader;

import com.nuctech.ls.model.bo.system.LsSystemUserLogBO;
import com.nuctech.ls.model.service.SystemUserLogService;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

/**
 * 用户是否已登录监听类
 * 
 * @author liuchao
 *
 */
public class UserOnlineListener implements   HttpSessionListener{
	
	//private static final Logger logger = Logger.getLogger(UserOnlineListener.class);
	    
    private static Map<String, String> onlineUsers = new HashMap<String, String>();
    
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        String onlineUserLogId = (String)session.getAttribute(Constant.USER_LOG_ID);
        if(!NuctechUtil.isNull(onlineUserLogId)) {
        	SystemUserLogService systemUserLogService = ContextLoader.getCurrentWebApplicationContext().getBean(SystemUserLogService.class);
        	LsSystemUserLogBO systemUserLog = systemUserLogService.findById(onlineUserLogId);
        	systemUserLog.setLogoutTime(new Date());
        	systemUserLog.setLogoutType(LogoutType.KICKOUT.getValue());
        	systemUserLogService.modify(systemUserLog);
        }
        onlineUsers.remove(session.getId());
    }
    
    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
        
    }
    
    public static boolean isAlreadyEnter(HttpSession session, String sUserName) {
        boolean flag = false;
        if (onlineUsers.containsValue(sUserName)) {
            flag = true;
            Iterator<String> iter = onlineUsers.keySet().iterator();
            while (iter.hasNext()) {
                if (onlineUsers.get(iter.next()).equals(sUserName)) {
                    iter.remove();
                }
            }
            onlineUsers.put(session.getId(), sUserName);
        } else {
            flag = false;
            onlineUsers.put(session.getId(), sUserName);
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
}
