package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.system.LsSystemNoticeLogBO;
import com.nuctech.ls.model.bo.system.LsSystemNoticesBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.system.LsSystemUserRoleBO;
import com.nuctech.ls.model.dao.SystemDepartmentDao;
import com.nuctech.ls.model.dao.SystemNoticeDao;
import com.nuctech.ls.model.dao.SystemNoticeLogDao;
import com.nuctech.ls.model.dao.SystemRoleDao;
import com.nuctech.ls.model.dao.SystemUserDao;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.system.SystemNoticeLogVO;
import com.nuctech.ls.model.vo.ztree.TreeNode;
import com.nuctech.util.Constant;
import com.nuctech.util.MessageResourceUtil;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 系统通知 Service
 * </p>
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
    @Resource
    private SystemRoleDao systemRoleDao;
    @Resource
    private SystemDepartmentDao departmentDao;// 中心dao
    @Resource
    private SystemModules systemModules;// 系统配置模块

    /**
     * 查询通知信息列表
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findNoticeList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        HttpSession session = ServletActionContext.getRequest().getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
        String roleId = sessionUser.getRoleId();
        String userId = sessionUser.getUserId();
        String queryString = "";
        queryString = "select t from LsSystemNoticesBO t where 1=1 ";
        //口岸工作人员只能看到通知人为自己的通知，控制中心可以看到所有的通知
        if(roleId.equals(RoleType.portUser.getType())){
            queryString +=" and '"+userId+"' like noticeUsers";
        }
        queryString +="/~ and t.noticeId = '[noticeId]' ~/"
                + "/~ and t.noticeTitle like '%[noticeTitle]%' ~/" + "/~ and t.noticeState = '[noticeState]' ~/"
                + "/~ and t.noticeContent like '%[noticeContent]%' ~/" + "/~ and t.noticeType like '%[noticeType]%' ~/"
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
        List<LsSystemUserBO> list = new ArrayList<LsSystemUserBO>();
        List<String> roleIds = Arrays.asList(new String[] { "1", "4", "8" });
        if (systemModules.isPatrolOn()) {// 车载台和巡逻队模块
            roleIds.add("7");
            roleIds.add("9");
            roleIds.add("10");
        }
        if (systemModules.isApprovalOn()) {// 中心用户
            roleIds.add("2");
            roleIds.add("3");
            roleIds.add("5");
        }
        if (systemModules.isRiskOn()) {// 风险分析模块
            roleIds.add("6");
        }
        list = userDao.findUserByRoleIds(roleIds.toArray(new String[] {}));
        TreeNode root = new TreeNode();
        root.setId("0");
        root.setName(MessageResourceUtil.getMessageInfo("userTree.rootName"));
        root.setOpen(true);
        root.setpId(null);
        treeNodeList.add(root);
        if (list != null && !list.isEmpty()) {
            for (LsSystemUserBO user : list) {
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
     * 查询用户树(违规报告页面——当前负责人)
     * 
     * @return
     */
    public List<TreeNode> findReportUserTree() {
        List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
        List<LsSystemUserBO> list = new ArrayList<LsSystemUserBO>();
        List<String> roleIds = Arrays.asList(new String[] { "4" });
        if (systemModules.isPatrolOn()) {// 车载台和巡逻队模块
            roleIds.add("9");
            roleIds.add("10");
        }
        if (systemModules.isAlarmPushOn()) {// 报警模块
            roleIds.add("3");
        }
        list = userDao.findUserByRoleIds(roleIds.toArray(new String[] {}));
        @SuppressWarnings("unused")
        TreeNode root = new TreeNode();
        // root.setId("0");
        // root.setName(MessageResourceUtil.getMessageInfo("userTree.rootName"));
        // root.setOpen(true);
        // root.setpId(null);
        // treeNodeList.add(root);
        if (list != null && !list.isEmpty()) {
            for (LsSystemUserBO user : list) {
                TreeNode tree = new TreeNode();
                tree.setId(user.getUserId());
                tree.setName(user.getUserName());
                // tree.setpId("0");
                tree.setpId(null);
                tree.setChecked(false);
                tree.setOpen(false);
                treeNodeList.add(tree);
            }
        }
        return treeNodeList;
    }

    /**
     * 查询口岸用户
     * 
     * @return
     */
    public List<TreeNode> findPortUserTree() {
        List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
        List<LsSystemUserBO> list = userDao.findUserByRoleIds(RoleType.portUser.getType());// 查询口岸用户
        TreeNode root = new TreeNode();
        root.setId("0");
        root.setName(MessageResourceUtil.getMessageInfo("userTree.rootName"));
        root.setOpen(true);
        root.setpId(null);
        treeNodeList.add(root);
        if (list != null && !list.isEmpty()) {
            for (LsSystemUserBO user : list) {
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
     * 查找巡逻队角色的用户
     * 
     * @return
     */
    public List<TreeNode> findpatrolTree() {
        List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
        // 查找巡逻队
        List<SessionUser> list = userDao.findAllPatrolUser();
        TreeNode root = new TreeNode();
        root.setId("0");
        root.setName(MessageResourceUtil.getMessageInfo("userTree.patrolName"));
        root.setOpen(true);
        root.setpId(null);
        treeNodeList.add(root);
        if (list != null && !list.isEmpty()) {
            for (SessionUser user : list) {
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
        if (!NuctechUtil.isNull(receiveUsers)) {
            String[] receiveUserArray = receiveUsers.split(",");
            for (String receiveUser : receiveUserArray) {
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
     *        通知ID
     * @return
     */
    public String findNoticeUsersNameById(String noticeId) {
        String noticeUsersName = "";
        LsSystemNoticesBO notice = findById(noticeId);
        if (notice != null) {
            String noticeUsers = notice.getNoticeUsers();
            if (!NuctechUtil.isNull(noticeUsers)) {
                String[] noticeUsersArray = noticeUsers.split(",");
                for (String noticeUser : noticeUsersArray) {
                    noticeUsersName += userDao.findUserNameById(noticeUser) + ",";
                }
            }
            if (!NuctechUtil.isNull(noticeUsers)) {
                noticeUsersName = noticeUsersName.substring(0, noticeUsersName.length() - 1);
            } else {
                return noticeUsersName;
            }

        }
        return noticeUsersName;
    }

    /**
     * 根据通知的ID查询接收人信息
     * 
     * @param noticeId
     *        通知ID
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public JSONObject findReceiveUserList(String noticeId, PageQuery<Map> pageQuery) {
        PageList<SystemNoticeLogVO> pageList = new PageList<SystemNoticeLogVO>();
        List<SystemNoticeLogVO> noticeLogList = new ArrayList<SystemNoticeLogVO>();
        PageList<Object> list = noticeLogDao.findNoticeLogList(noticeId, pageQuery);
        if (list != null && !list.isEmpty()) {
            for (Object obj : list) {
                Object[] objs = (Object[]) obj;
                SystemNoticeLogVO vo = new SystemNoticeLogVO();
                BeanUtils.copyProperties(objs[0], vo);
                if (objs[1] != null) {
                    vo.setUserName(((LsSystemUserBO) objs[1]).getUserName());
                }
                noticeLogList.add(vo);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.addAll(noticeLogList);
        pageList.setTotalItems(list.getTotalItems());
        return fromObjectList(pageList, null, false);
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
        // 删除接收人日志(根据noticeId)
        noticeLogDao.deleteNoticeLogByNoticeId(notice.getNoticeId());
        // 添加接收人日志
        String receiveUsers = notice.getNoticeUsers();
        if (!NuctechUtil.isNull(receiveUsers)) {
            String[] receiveUserArray = receiveUsers.split(",");
            for (String receiveUser : receiveUserArray) {
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
        LsSystemNoticeLogBO noticeLog = noticeLogDao.findNoticeLogByNoticeIdAndReceiveUser(log.getNoticeId(),
                log.getReceiveUser());
        if (noticeLog != null) {
            noticeLog.setDealType(Constant.NOTICE_HANDLED);
            noticeLog.setReceiveTime(new Date());
            noticeLogDao.update(noticeLog);
        }
        // 判断是否还有log未读 如过没有 修改通知状态为完成
        List<LsSystemNoticeLogBO> list = noticeLogDao.findNoticeLogByNoticeIdAndType(log.getNoticeId(),
                Constant.NOTICE_UN_HANDLED);
        if (list == null || list.isEmpty()) {
            LsSystemNoticesBO notice = noticeDao.findById(log.getNoticeId());
            notice.setNoticeState(Constant.NOTICE_STATE_FINISH);
            noticeDao.update(notice);
        }
    }

    /**
     * 
     * @param userId
     * @return
     */
    public List<LsSystemNoticesBO> findSystemNoticesListByUserId(String userId) {
        return noticeDao.findSystemNoticesListByUserId(userId);
    }

    /**
     * zsy
     */
    // 巡逻队收到推送消息后，点击"同意按钮"将通知状态改为"完成"
    public void updateNotice(LsSystemNoticesBO notice) {
        noticeDao.update(notice);
    }

    public void updateNotice(LsSystemNoticeLogBO noticeLog) {
        noticeLogDao.merge(noticeLog);
    }

    /**
     * 查询用户树（分级，添加所属中心）
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TreeNode> findNewUserTree() {
        List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
        List<LsSystemDepartmentBO> systemDepartmentBOsList = departmentDao.findExceptCountry();// 查询所有机构(除了国家)
        TreeNode root = new TreeNode();
        root.setId("0000");
        root.setName(MessageResourceUtil.getMessageInfo("userTree.rootName.jordan"));
        root.setOpen(true);
        root.setpId(null);
        treeNodeList.add(root);
        for (LsSystemDepartmentBO systemDepartmentBO : systemDepartmentBOsList) {
            TreeNode tree = new TreeNode();
            tree.setId(systemDepartmentBO.getOrganizationId());
            tree.setpId("0000");
            tree.setName(systemDepartmentBO.getOrganizationName());
            treeNodeList.add(tree);
            List<SessionUser> userlist = new ArrayList<SessionUser>();
            String orgId = systemDepartmentBO.getOrganizationId();

            // 根据组织机构id查询用户
            String sql = "SELECT u.*,so.*,r.* "
                    + "FROM LS_SYSTEM_USER u,LS_SYSTEM_ORGANIZATION_USER so,LS_SYSTEM_USER_ROLE r "
                    + "where u.USER_ID=so.USER_ID and u.USER_ID=r.USER_ID";
            if (!systemModules.isPatrolOn()) {// 不包含巡逻队功能
                sql += " and r.ROLE_ID not in(7,9,10)";
            }
            if (!systemModules.isRiskOn()) {// 不包含风险分析模块
                sql += " and r.ROLE_ID not in(6)";
            }
            sql += " and so.ORGANIZATION_ID='" + orgId + "'";
            Query query = userDao.getSession().createSQLQuery(sql).addEntity("u", LsSystemUserBO.class)
                    .addEntity("so", LsSystemDepartmentBO.class).addEntity("r", LsSystemUserRoleBO.class);
            List<Object[]> list = query.list();
            for (int i = 0; i < list.size(); i++) {
                SessionUser sessionUser = new SessionUser();
                sessionUser.setUserName(((LsSystemUserBO) list.get(i)[0]).getUserName());
                sessionUser.setUserId(((LsSystemUserBO) list.get(i)[0]).getUserId());
                sessionUser.setOrganizationName(((LsSystemDepartmentBO) list.get(i)[1]).getOrganizationName());
                sessionUser.setOrganizationId(((LsSystemDepartmentBO) list.get(i)[1]).getOrganizationId());

                userlist.add(sessionUser);
            }

            for (SessionUser userBO : userlist) {
                TreeNode usertree = new TreeNode();
                usertree.setId(userBO.getUserId());
                usertree.setpId(systemDepartmentBO.getOrganizationId());
                usertree.setName(userBO.getUserName());
                treeNodeList.add(usertree);
            }
        }
        return treeNodeList;
    }

    /**
     * 查询用户未处理的通知(为普通通知且该通知已发布)
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findUnDealNoticeList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes, String userId) {
        String queryString = "select t,l,u from LsSystemNoticesBO t,LsSystemNoticeLogBO l,LsSystemUserBO u where 1=1 "
                + " and t.noticeId=l.noticeId " + " and t.publisher=u.userId " + " and l.receiveUser='" + userId + "'"
                + " and l.dealType='" + 0 + "'" + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = noticeDao.pageQuery(queryString, pageQuery);
        PageList<SystemNoticeLogVO> pageList = new PageList<SystemNoticeLogVO>();

        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                SystemNoticeLogVO systemNoticeLogVO = new SystemNoticeLogVO();
                BeanUtils.copyProperties(objs[0], systemNoticeLogVO);
                BeanUtils.copyProperties(objs[1], systemNoticeLogVO);
                BeanUtils.copyProperties(objs[2], systemNoticeLogVO);
                pageList.add(systemNoticeLogVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 查询待办任务
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findUndealMissionList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes, String userId) {
        String queryString = "select t,l,u from LsSystemNoticesBO t,LsSystemNoticeLogBO l,LsSystemUserBO u where 1=1 "
                + " and t.noticeId=l.noticeId " + " and t.publisher=u.userId " + " and l.receiveUser='" + userId + "'"
                + " and l.dealType='" + 0 + "'" + " and t.noticeType!='" + 0 + "'" + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = noticeDao.pageQuery(queryString, pageQuery);
        PageList<SystemNoticeLogVO> pageList = new PageList<SystemNoticeLogVO>();

        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                SystemNoticeLogVO systemNoticeLogVO = new SystemNoticeLogVO();
                BeanUtils.copyProperties(objs[0], systemNoticeLogVO);
                BeanUtils.copyProperties(objs[1], systemNoticeLogVO);
                BeanUtils.copyProperties(objs[2], systemNoticeLogVO);
                pageList.add(systemNoticeLogVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    public LsSystemNoticeLogBO findByIdAndReceiver(String noticeId, String userId) {
        return noticeLogDao.findLog(noticeId, userId);
    }

    public void addNoticeLog(LsSystemNoticeLogBO noticeLogBO) {
        noticeLogDao.persist(noticeLogBO);
    }
}
