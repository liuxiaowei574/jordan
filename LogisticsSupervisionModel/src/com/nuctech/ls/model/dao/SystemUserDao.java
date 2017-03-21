package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.system.LsSystemOrganizationUserBO;
import com.nuctech.ls.model.bo.system.LsSystemRoleBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.system.LsSystemUserRoleBO;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
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
     *        工号
     * @param password
     *        密码
     * @return 要登录的用户信息，若为null，表示用户名或密码不正确
     * @throws Exception
     */
    public LsSystemUserBO login(String loginName, String password) throws Exception {
        Criteria criteria = getSession().createCriteria(LsSystemUserBO.class)
                .add(Restrictions.eq("usreAccount", loginName))
                .add(Restrictions.eq("userPassword", encrypter.encrypt(password)));
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
     *        用户ID
     * @return
     */
    public String findUserNameById(String userId) {
        LsSystemUserBO user = findById(userId);
        if (user != null) {
            return user.getUserName();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<LsSystemUserBO> findUserByRoleId(String type) {
        String sql = "SELECT s.* FROM LS_SYSTEM_USER s,LS_SYSTEM_USER_ROLE t"
                + " WHERE s.USER_ID = t.USER_ID and t.ROLE_ID='" + type + "' "
                + " and s.USER_ID not in(select POTRAL_USER from " + " LS_COMMON_PATROL WHERE DELETE_MARK='"
                + Constant.MARK_UN_DELETED + "') ORDER BY s.USER_NAME ASC ";
        try {
            logger.info("执行查询语句：" + sql);
            Query query = getSession().createSQLQuery(sql).addEntity(LsSystemUserBO.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查询失败" + sql);
        }

        return null;
    }

    /**
     * 查询指定国家内，指定角色的所有用户
     * 
     * @param countryId
     * @param types
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LsSystemUserBO> findUserByRoleIds(String countryId, String[] types) {
        StringBuffer typeSql = new StringBuffer("");
        if (types != null && types.length > 0) {
            typeSql.append(" and t.ROLE_ID in (");
            for (String t : types) {
                typeSql.append("'").append(t).append("',");
            }
            typeSql.deleteCharAt(typeSql.length() - 1);
            typeSql.append(") ");
        }
        String sql = "SELECT s.* "
                + "FROM LS_SYSTEM_USER s,LS_SYSTEM_USER_ROLE t,LS_SYSTEM_ORGANIZATION_USER ou,LS_SYSTEM_DEPARTMENT d"
                + " WHERE s.USER_ID = t.USER_ID "
                + " AND s.USER_ID = ou.USER_ID AND d.ORGANIZATION_ID = ou.ORGANIZATION_ID AND d.PARENT_ID='" + countryId
                + "' " + typeSql.toString() + " and s.USER_ID not in(select POTRAL_USER from "
                + " LS_COMMON_PATROL WHERE DELETE_MARK='" + Constant.MARK_UN_DELETED + "') ORDER BY s.USER_NAME ASC ";
        try {
            logger.info("执行查询语句：" + sql);
            Query query = getSession().createSQLQuery(sql).addEntity(LsSystemUserBO.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查询失败" + sql);
        }

        return null;
    }

    /**
     * 查询当前用户国家内指定角色的所有用户
     * 
     * @param types
     * @return
     */
    public List<LsSystemUserBO> findUserByRoleIds(String... types) {
        // 查询当前用户所在国家的
        String countryId = findCurrentRequestCountryId();
        return findUserByRoleIds(countryId, types);
    }

    /**
     * 赵苏阳 7.19
     */
    // 统计系统中的用户的数量
    @SuppressWarnings("rawtypes")
    public int findUserNumber() {
        Session session = this.getSession();
        String sql = "select count(USER_ACCOUNT) from LS_SYSTEM_USER GROUP BY USER_ACCOUNT";
        List userCountList = session.createSQLQuery(sql).list();
        int userCount = userCountList.size();
        return userCount;
    }

    // 查询用户账号
    @SuppressWarnings("rawtypes")
    public List findUserNameList() {
        Session session = this.getSession();
        String sql = "select USER_ACCOUNT from LS_SYSTEM_USER GROUP BY USER_ACCOUNT";
        List userCountList = session.createSQLQuery(sql).list();
        return userCountList;
    }

    // 查询用户在线时长
    @SuppressWarnings("rawtypes")
    public List findUserOnLineList() {
        /*
         * Session session = this.getSession(); String sql =
         * "SELECT * from LS_SYSTEM_USER";
         * @SuppressWarnings("rawtypes") List userList =
         * session.createSQLQuery(sql).addEntity(LsSystemUserBO.).list();
         */
        return null;
    }

    /**
     * 查询控制中心的用户
     */
    @SuppressWarnings("unchecked")
    public List<SessionUser> findContromRoomList(String userId) {
        // 查询contromRoomUser;id=4
        String queryString1 = "SELECT {r.*},{u.*},{s.*} "
                + "FROM LS_SYSTEM_ROLE r,LS_SYSTEM_USER u ,LS_SYSTEM_USER_ROLE s "
                + "where r.ROLE_ID='4' and r.ROLE_ID=s.ROLE_ID and s.USER_ID=u.USER_ID and u.USER_ID!='" + userId + "'";
        Query query = this.getSession().createSQLQuery(queryString1).addEntity("r", LsSystemRoleBO.class)
                .addEntity("u", LsSystemUserBO.class).addEntity("s", LsSystemUserRoleBO.class);
        List<Object[]> list = query.list();
        List<SessionUser> contromList = new ArrayList<SessionUser>();
        for (int i = 0; i < list.size(); i++) {
            SessionUser sessionUser = new SessionUser();
            sessionUser.setUserId(((LsSystemUserBO) list.get(i)[1]).getUserId());
            sessionUser.setRoleName(((LsSystemRoleBO) list.get(i)[0]).getRoleName());
            sessionUser.setUserAccount(((LsSystemUserBO) list.get(i)[1]).getUserAccount());
            sessionUser.setUserName(((LsSystemUserBO) list.get(i)[1]).getUserName());

            contromList.add(sessionUser);
        }

        // 查询controlRoomManager;id=5
        String queryString2 = "SELECT {a.*},{b.*},{c.*} "
                + "FROM LS_SYSTEM_ROLE a,LS_SYSTEM_USER b ,LS_SYSTEM_USER_ROLE c "
                + "where a.ROLE_ID='5' and a.ROLE_ID=c.ROLE_ID and c.USER_ID=b.USER_ID";
        Query query2 = this.getSession().createSQLQuery(queryString2).addEntity("a", LsSystemRoleBO.class)
                .addEntity("b", LsSystemUserBO.class).addEntity("c", LsSystemUserRoleBO.class);
        List<Object[]> list2 = query2.list();
        for (int i = 0; i < list2.size(); i++) {
            SessionUser sessionUser2 = new SessionUser();
            sessionUser2.setUserId(((LsSystemUserBO) list2.get(i)[1]).getUserId());
            sessionUser2.setRoleName(((LsSystemRoleBO) list2.get(i)[0]).getRoleName());
            sessionUser2.setUserAccount(((LsSystemUserBO) list2.get(i)[1]).getUserAccount());
            sessionUser2.setUserName(((LsSystemUserBO) list2.get(i)[1]).getUserName());

            contromList.add(sessionUser2);
        }
        return contromList;
    }

    /**
     * 根据组织机构id查询用户
     */
    @SuppressWarnings("unchecked")
    public List<SessionUser> findByOrgId(String orgId) {
        String sql = "SELECT u.*,so.* " + "FROM LS_SYSTEM_USER u,LS_SYSTEM_ORGANIZATION_USER so "
                + "where u.USER_ID=so.USER_ID and so.ORGANIZATION_ID='" + orgId + "'";
        Query query = this.getSession().createSQLQuery(sql).addEntity("u", LsSystemUserBO.class).addEntity("so",
                LsSystemDepartmentBO.class);
        List<Object[]> list = query.list();
        List<SessionUser> orgUserList = new ArrayList<SessionUser>();
        for (int i = 0; i < list.size(); i++) {
            SessionUser sessionUser = new SessionUser();
            sessionUser.setUserName(((LsSystemUserBO) list.get(i)[0]).getUserName());
            sessionUser.setUserId(((LsSystemUserBO) list.get(i)[0]).getUserId());
            sessionUser.setOrganizationName(((LsSystemDepartmentBO) list.get(i)[1]).getOrganizationName());
            sessionUser.setOrganizationId(((LsSystemDepartmentBO) list.get(i)[1]).getOrganizationId());

            orgUserList.add(sessionUser);
        }

        return orgUserList;
    }

    /**
     * 查询角色为巡逻队的用户
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SessionUser> findAllPatrolUser() {
        String sql = "SELECT u.*, r.*, ur.* " + "FROM LS_SYSTEM_USER u, LS_SYSTEM_ROLE r, LS_SYSTEM_USER_ROLE ur "
                + "WHERE u.USER_ID = ur.USER_ID AND r.ROLE_ID = ur.ROLE_ID AND (r.ROLE_ID ='"
                + RoleType.enforcementPatrol.getType() + "'" + "OR r.ROLE_ID ='" + RoleType.escortPatrol.getType()
                + "')";
        Query query = this.getSession().createSQLQuery(sql).addEntity("u", LsSystemUserBO.class)
                .addEntity("r", LsSystemRoleBO.class).addEntity("ur", LsSystemUserRoleBO.class);
        List<Object[]> list = query.list();
        List<SessionUser> patrolList = new ArrayList<SessionUser>();
        for (int i = 0; i < list.size(); i++) {
            SessionUser sessionUser = new SessionUser();
            sessionUser.setUserId(((LsSystemUserBO) list.get(i)[0]).getUserId());
            sessionUser.setRoleName(((LsSystemRoleBO) list.get(i)[1]).getRoleName());
            sessionUser.setUserAccount(((LsSystemUserBO) list.get(i)[0]).getUserAccount());
            sessionUser.setUserName(((LsSystemUserBO) list.get(i)[0]).getUserName());

            patrolList.add(sessionUser);
        }
        return patrolList;
    }

    /**
     * 查询 控制中心普通工作人员(contromRoomUser)、控制中心监管人员(followupUser)
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SessionUser> findAllCenterUser() {
        String sql = "SELECT u.*, r.*, ur.* " + "FROM LS_SYSTEM_USER u, LS_SYSTEM_ROLE r, LS_SYSTEM_USER_ROLE ur "
                + "WHERE u.USER_ID = ur.USER_ID AND r.ROLE_ID = ur.ROLE_ID AND (r.ROLE_ID ='"
                + RoleType.contromRoomUser.getType() + "'" + "OR r.ROLE_ID ='" + RoleType.followupUser.getType() + "')";
        Query query = this.getSession().createSQLQuery(sql).addEntity("u", LsSystemUserBO.class)
                .addEntity("r", LsSystemRoleBO.class).addEntity("ur", LsSystemUserRoleBO.class);
        List<Object[]> list = query.list();
        List<SessionUser> patrolList = new ArrayList<SessionUser>();
        for (int i = 0; i < list.size(); i++) {
            SessionUser sessionUser = new SessionUser();
            sessionUser.setUserId(((LsSystemUserBO) list.get(i)[0]).getUserId());
            sessionUser.setRoleName(((LsSystemRoleBO) list.get(i)[1]).getRoleName());
            sessionUser.setUserAccount(((LsSystemUserBO) list.get(i)[0]).getUserAccount());
            sessionUser.setUserName(((LsSystemUserBO) list.get(i)[0]).getUserName());

            patrolList.add(sessionUser);
        }
        return patrolList;
    }

    /**
     * 随机的选择一位控制中心的主管用户，获取其id
     * 
     * @return
     */
    public String findOneManagerUser() {
        Session session = this.getSession();
        String sql = "SELECT top 1 * FROM LS_SYSTEM_USER_ROLE where ROLE_ID='5' order by NEWID()";
        LsSystemUserRoleBO systemUserRoleBO = (LsSystemUserRoleBO) session.createSQLQuery(sql)
                .addEntity(LsSystemUserRoleBO.class).uniqueResult();
        return systemUserRoleBO.getUserId();
    }

    /**
     * 查询当前用户所属国家，必须有来自客户端的请求
     * 
     * @return
     */
    public String findCurrentRequestCountryId() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        String userId = ((SessionUser) session.getAttribute(Constant.SESSION_USER)).getUserId();
        return findCountryIdByUserId(userId);
    }

    /**
     * 根据用户Id查询其国家
     * 
     * @param userId
     * @return
     */
    public String findCountryIdByUserId(String userId) {
        String queryString = "select {p.*} "
                + "from LS_SYSTEM_USER u,LS_SYSTEM_DEPARTMENT d,LS_SYSTEM_ORGANIZATION_USER ou,LS_SYSTEM_DEPARTMENT p "
                + " where u.USER_ID=ou.USER_ID " + " and d.ORGANIZATION_ID=ou.ORGANIZATION_ID "
                + " and d.PARENT_ID=p.ORGANIZATION_ID " + " and p.ORGANIZATION_TYPE='1' " + " and u.USER_ID='" + userId
                + "' ";
        Query query = this.getSession().createSQLQuery(queryString).addEntity("p", LsSystemDepartmentBO.class);
        @SuppressWarnings("unchecked")
        List<Object> list = query.list();
        if (list != null && list.size() > 0) {
            LsSystemDepartmentBO departmentBO = (LsSystemDepartmentBO) list.get(0);
            return departmentBO.getOrganizationId();
        }
        return null;
    }

    /**
     * 根据口岸id查询，所属该口岸的所有用户
     * @param orgId
     * @return
     */
    public List<LsSystemOrganizationUserBO> findByPortId(String orgId){
        String sql = "SELECT * FROM LS_SYSTEM_ORGANIZATION_USER where ORGANIZATION_ID='"+orgId+"'";
        Session session = this.getSession();
        @SuppressWarnings("unchecked")
        List<LsSystemOrganizationUserBO> list 
        = session.createSQLQuery(sql).addEntity(LsSystemOrganizationUserBO.class).list();
        return list;
    }
    
    /**
     * 控制中心用户登陆，查询系统中所有用户；口岸用户登陆查询该口岸的所有用户
     * @return
     */
    public List<?> findUserByOrganizationId(String orgId){
        HttpSession session = ServletActionContext.getRequest().getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
        String orgnizationId = sessionUser.getOrganizationId();
        String roleId = sessionUser.getRoleId();
        String sql = "";
        sql = "SELECT s.*,u.* FROM LS_SYSTEM_ORGANIZATION_USER s ,LS_SYSTEM_USER u where s.USER_ID = u.USER_ID";
        //口岸用户登陆查询该口岸的所有用户
        if(roleId.equals(RoleType.portUser.getType())){
            sql += " and s.ORGANIZATION_ID='"+orgnizationId+"'";
        }
        List<?> list 
        = this.getSession().createSQLQuery(sql).addEntity(LsSystemUserBO.class).list();
        return list;
    }
    
    
}
