package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemNoticeLogBO;
import com.nuctech.ls.model.bo.system.LsSystemNoticesBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.dao.SystemNoticeDao;
import com.nuctech.ls.model.dao.SystemNoticeLogDao;
import com.nuctech.ls.model.dao.SystemUserDao;
import com.nuctech.ls.model.vo.system.SystemNoticeLogVO;
import com.nuctech.ls.model.vo.ztree.TreeNode;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>系统通知 Service</p>
 * 创建时间：2016年5月27日
 */
@Service
@Transactional
public class SystemNoticeService extends LSBaseService {

	@Resource
	private SystemNoticeDao noticeDao;
	@Resource
	private SystemNoticeLogDao noticeLogDao;
	@Resource
	private SystemUserDao userDao;
	
	/**
	 * 查询通知信息列表
	 * 
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject findNoticeList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		String queryString = "select t from LsSystemNoticesBO t where 1=1 "
				+ "/~ and t.noticeId = '[noticeId]' ~/"
				+ "/~ and t.noticeTitle like '%[noticeTitle]%' ~/"
				+ "/~ and t.noticeContent like '%[noticeContent]%' ~/"
				+ "/~ order by [sortColumns] ~/";
		PageList<LsSystemNoticesBO> pageList = noticeDao.pageQuery(queryString, pageQuery);
		return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
	}
	
	/**
	 * 查询用户树
	 * 
	 * @return
	 */
	public List<TreeNode> findUserTree() {
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
		List<LsSystemUserBO> list = userDao.findAll();
		TreeNode root = new TreeNode();
		root.setId("0");
		root.setName("所有人员");
		root.setOpen(true);
		root.setpId(null);
		treeNodeList.add(root);
		if(list != null && !list.isEmpty()) {
			for(LsSystemUserBO user : list) {
				TreeNode tree = new TreeNode();
				tree.setId(user.getUserId());
				tree.setName(user.getUserName());
				tree.setpId("0");
				tree.setChecked(false);
				tree.setOpen(false);
				treeNodeList.add(tree);
			}
		}
		return treeNodeList;
	}
	
	/**
	 * 添加通知
	 * 
	 * @param notice
	 */
	public void addNotice(LsSystemNoticesBO notice) {
		notice.setNoticeId(UUID.randomUUID().toString());
		notice.setNoticeState(Constant.NOTICE_STATE_DRAFT);
		noticeDao.save(notice);
		String receiveUsers = notice.getNoticeUsers();
		if(!NuctechUtil.isNull(receiveUsers)) {
			String[] receiveUserArray = receiveUsers.split(",");
			for(String receiveUser : receiveUserArray) {
				LsSystemNoticeLogBO noticeLog = new LsSystemNoticeLogBO();
				noticeLog.setNoticeRevId(UUID.randomUUID().toString());
				noticeLog.setNoticeId(notice.getNoticeId());
				noticeLog.setReceiveUser(receiveUser);
				noticeLog.setDealType(Constant.NOTICE_UN_HANDLED);
				noticeLogDao.save(noticeLog);
			}
		}
	}
	
	public LsSystemNoticesBO findById(String noticeId) {
		return noticeDao.findById(noticeId);
	}
	
	/**
	 * 根据通知的ID查询页面VO
	 * 
	 * @param noticeId
	 * 				通知ID
	 * @return
	 */
	public String findNoticeUsersNameById(String noticeId) {
		String noticeUsersName = "";
		LsSystemNoticesBO notice = findById(noticeId);
		if(notice != null) {
			String noticeUsers = notice.getNoticeUsers();
			if(!NuctechUtil.isNull(noticeUsers)) {
				String[] noticeUsersArray = noticeUsers.split(",");
				for(String noticeUser : noticeUsersArray) {
					noticeUsersName += userDao.findUserNameById(noticeUser) + ",";
				}
			}
			noticeUsersName = noticeUsersName.substring(0, noticeUsersName.length() - 1);
		}
		return noticeUsersName;
	}
	
	/**
	 * 根据通知的ID查询接收人信息
	 * 
	 * @param noticeId
	 * 			通知ID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONObject findReceiveUserList(String noticeId, PageQuery<Map> pageQuery) {
		PageList<SystemNoticeLogVO> pageList = new PageList<SystemNoticeLogVO>();
		List<SystemNoticeLogVO> noticeLogList = new ArrayList<SystemNoticeLogVO>();
		List<LsSystemNoticeLogBO> list = noticeLogDao.findNoticeLogListByNoticeId(noticeId);
		if(list != null && !list.isEmpty()) {
			for(LsSystemNoticeLogBO noticeLog : list) {
				SystemNoticeLogVO vo = new SystemNoticeLogVO();
				BeanUtils.copyProperties(noticeLog, vo);
				vo.setReceiveUserName(userDao.findUserNameById(noticeLog.getReceiveUser()));
				noticeLogList.add(vo);
			}
		}
		pageList.setPage(pageQuery.getPage());
	    pageList.setPageSize(pageQuery.getPageSize());
	    pageList.addAll(noticeLogList);
	    pageList.setTotalItems(noticeLogList.size());
	    return fromObjectList(pageList, null,false);
	}

	/**
	 * 发布通知
	 * 
	 * @param ids
	 */
	public int publish(String[] ids) {
		String hql = "update LsSystemNoticesBO set noticeState = :noticeState, deployTime =:deployTime"
				+ " where noticeId in :ids";
	   HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
       propertiesMap.put("noticeState", Constant.NOTICE_STATE_PUBLISH);
       propertiesMap.put("deployTime", new Date());
       propertiesMap.put("ids", ids);
       int count = noticeDao.batchUpdateOrDeleteByHql(hql, propertiesMap);
       return count;
	}

	/**
	 * 删除通知
	 * 
	 * @param ids
	 */
	public void delete(String[] ids) {
		String hql = "delete LsSystemNoticesBO where noticeId in :ids";
		String logHQL = "delete LsSystemNoticeLogBO where noticeId in :ids";
		HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
		propertiesMap.put("ids", ids);
		noticeDao.batchUpdateOrDeleteByHql(hql, propertiesMap);
		noticeLogDao.batchUpdateOrDeleteByHql(logHQL, propertiesMap);
	}

	/**
	 * 
	 * @param notice
	 */
	public void modifyNotice(LsSystemNoticesBO notice) {
		notice.setNoticeState(Constant.NOTICE_STATE_DRAFT);
		noticeDao.update(notice);
		//删除接收人日志(根据noticeId)
		noticeLogDao.deleteNoticeLogByNoticeId(notice.getNoticeId());
		//添加接收人日志
		String receiveUsers = notice.getNoticeUsers();
		if(!NuctechUtil.isNull(receiveUsers)) {
			String[] receiveUserArray = receiveUsers.split(",");
			for(String receiveUser : receiveUserArray) {
				LsSystemNoticeLogBO noticeLog = new LsSystemNoticeLogBO();
				noticeLog.setNoticeRevId(UUID.randomUUID().toString());
				noticeLog.setNoticeId(notice.getNoticeId());
				noticeLog.setReceiveUser(receiveUser);
				noticeLog.setDealType(Constant.NOTICE_UN_HANDLED);
				noticeLogDao.save(noticeLog);
			}
		}
	}
	
	/**
	 * 消息读取
	 * 
	 * @param log
	 */
	public void readNotice(LsSystemNoticeLogBO log) {
		LsSystemNoticeLogBO noticeLog = noticeLogDao.findNoticeLogByNoticeIdAndReceiveUser(log.getNoticeId(), log.getReceiveUser());
		if(noticeLog != null) {
			noticeLog.setDealType(Constant.NOTICE_HANDLED);
			noticeLog.setReceiveTime(new Date());
			noticeLogDao.update(noticeLog);
		}
		//判断是否还有log未读 如过没有 修改通知状态为完成
		List<LsSystemNoticeLogBO> list = noticeLogDao.findNoticeLogByNoticeIdAndType(log.getNoticeId(), Constant.NOTICE_UN_HANDLED);
		if(list == null || list.isEmpty()) {
			LsSystemNoticesBO notice = noticeDao.findById(log.getNoticeId());
			notice.setNoticeState(Constant.NOTICE_STATE_FINISH);
			noticeDao.update(notice);
		}
	}
}
