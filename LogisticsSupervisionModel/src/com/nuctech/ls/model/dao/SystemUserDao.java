package com.nuctech.ls.model.dao;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.util.Encrypter;

/**
 * 系统用户 DAO
 * 
 * @author xunan
 * @2016年5月17日
 *
 */
@Repository
public class SystemUserDao extends LSBaseDao<LsSystemUserBO, Serializable> {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private Encrypter encrypter = Encrypter.getInstance(Encrypter.Algorithms.MD5);

	/**
	 * 登录
	 * 
	 * @param loginName
	 *            工号
	 * @param password
	 *            密码
	 * @return 要登录的用户信息，若为null，表示用户名或密码不正确
	 * @throws Exception
	 */
	public LsSystemUserBO login(String loginName, String password)
			throws Exception {
		Criteria criteria = getSession()
				.createCriteria(LsSystemUserBO.class)
				.add(Restrictions.eq("usreAccount", loginName))
				.add(Restrictions.eq("userPassword",
						encrypter.encrypt(password)));
		LsSystemUserBO user = (LsSystemUserBO) criteria.uniqueResult();
		if (user == null) {
			return null;
		}
		logger.info(String.format("用户登录，工号：%s", user.getUserAccount()));
		return user;
	}

	/**
	 * 通过用户账号查询用户信息
	 * 
	 * @param userAccount
	 * @return
	 */
	public LsSystemUserBO findSystemUserByUserAccount(String userAccount) {
		Criteria criteria = getSession().createCriteria(LsSystemUserBO.class);
		criteria.add(Restrictions.eq("userAccount", userAccount));
		LsSystemUserBO user = (LsSystemUserBO) criteria.uniqueResult();
		if (user == null) {
			return null;
		}
		logger.info(String.format("用户查询，工号：%s", user.getUserAccount()));
		return user;
	}

	/**
	 * 用户ID转化成用户名称
	 * 
	 * @param userId
	 * 			用户ID
	 * @return
	 */
	public String findUserNameById(String userId) {
		LsSystemUserBO user = findById(userId);
		if(user != null) {
			return user.getUserName();
		} else {
			return null;
		}
	}
}
