package com.nuctech.ls.center.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;

import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.util.Constant;
import com.nuctech.util.ConstantConfig;
import com.nuctech.util.LoginSystem;

/**
 * 
 * @author liuchao
 *
 */
public class CreateAdminListener implements ServletContextListener {
	
	private Logger logger = Logger.getLogger(CreateAdminListener.class);
	
    public void contextInitialized(ServletContextEvent sce) {

        try {
        	SystemUserService systemUserService = ContextLoader.getCurrentWebApplicationContext().getBean(SystemUserService.class);
        	if(systemUserService.isUserAccountExist("admin")) {
        		logger.info("超级管理员用户已经存在不需创建");
        		return ;
        	}
        	String validFlag = ConstantConfig.readValue(Constant.VALID_FLAG);
        	LsSystemUserBO admin = new LsSystemUserBO();
        	admin.setUserAccount("admin");
        	admin.setUserName("admin");
        	admin.setIsEnable(validFlag);
        	admin.setLogonSystem(LoginSystem.TRACKING);
        	//systemUser.setIpAddress(SystemUtil.getIpAddr(ServletActionContext.getRequest()));
        	systemUserService.save(admin);
            logger.info("创建超级管理员用户");

        } catch (Exception e) {
        	e.printStackTrace();
            logger.warn("创建超级管理员用户异常", e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }

}
