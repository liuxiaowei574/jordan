package com.nuctech.ls.model.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.dao.SystemUserDao;
import com.nuctech.util.Encrypter;

/**
 * 系统用户相关 Service
 * 
 * @author xunan
 * @2016年5月17日
 *
 */
@Service
@Transactional
public class SystemUserService extends LSBaseService {

	@Resource
	private SystemUserDao systemUserDao;

	/**
	 * 系统登录
	 * 
	 * @param loginName
	 * 		登录账号
	 * @param password
	 * 		密码
	 * @return
	 * @throws Exception
	 */
	public LsSystemUserBO login(String loginName, String password)
			throws Exception {
		return systemUserDao.login(loginName, password);
	}
	
	/**
	 * 修改用户
	 * 
	 * @param systemUser
	 */
	public void modify(LsSystemUserBO systemUser) {
		systemUserDao.merge(systemUser);
	}

	/**
	 * 通过用户账号查询用户信息
	 * 
	 * @param userAccount
	 * 				用户账号
	 * @return
	 */
	public LsSystemUserBO findSystemUserByUserAccount(String userAccount) {
		return systemUserDao.findSystemUserByUserAccount(userAccount);
	}
	
	/**
	 * 根据ID查询用户
	 * 
	 * @param id
	 * 		用户主键ID
	 * @return
	 */
	public LsSystemUserBO findById(String id) {
		return systemUserDao.findById(id);
	}
	
	/**
	 * 用户账号是否存在
	 * 
	 * @param userAccount
	 * 			用户账号
	 * @return
	 */
	public boolean isUserAccountExist(String userAccount) {
		LsSystemUserBO systemUser = systemUserDao.findSystemUserByUserAccount(userAccount);
		if(systemUser == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void save(LsSystemUserBO systemUser) {
		Encrypter encrypter = Encrypter.getInstance(Encrypter.Algorithms.MD5);
		systemUser.setUserPassword(encrypter.encrypt(LsSystemUserBO.DEFAULT_PASSWORD));
		systemUserDao.save(systemUser);
	}
	
	public PageList findUsers(PageQuery<Map> pageQuery) {
		String queryString = "select t from LsSystemUserBO t where 1=1 "
				+ "/~ and t.userId = '[userId]' ~/"
				+ "/~ and t.userAccount like '%[userAccount]%' ~/"
				+ "/~ and t.userName like '%[userName]%' ~/"
				+ "/~ and t.userPhone like '%[userPhone]%' ~/"
				+ "/~ and t.userEmail like '%[userEmail]%' ~/"
				+ "/~ order by [sortColumns] ~/";
		
		return systemUserDao.pageQuery(queryString, pageQuery);
	}

	public JSONObject fromObjectList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		PageList<LsSystemUserBO> pageList = findUsers(pageQuery);
		return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
	}

	/**
	 * 
	 * @param b
	 * @param ids
	 */
	public int batchLockOrUnlockUser(String enable, String[] ids) {
		String hql = "update LsSystemUserBO set isEnable=:enable where userId in :ids";
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("enable", enable);
        propertiesMap.put("ids", ids);
        int count = systemUserDao.batchUpdateOrDeleteByHql(hql, propertiesMap);
        return count;
     }

	/**
	 * 重置用户账号密码
	 * 
	 * @param ids
	 * 		账号ID数组
	 */
	public int resetUserPasswordByIds(String[] ids) {
		String hql = "update LsSystemUserBO set userPassword = :userPassword where userId in :ids";
		 HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("userPassword", LsSystemUserBO.DEFAULT_PASSWORD);
        propertiesMap.put("ids", ids);
        int count = systemUserDao.batchUpdateOrDeleteByHql(hql, propertiesMap);
        return count;
	}
}
