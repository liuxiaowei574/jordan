package com.nuctech.ls.model.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.system.LsSystemUserLogBO;
import com.nuctech.ls.model.dao.SystemOperateLogDao;
import com.nuctech.ls.model.dao.SystemUserDao;
import com.nuctech.ls.model.dao.SystemUserLogDao;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.system.SystemOperateLogVO;
import com.nuctech.ls.model.vo.system.SystemUserLogVO;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 用户登录详细日志信息 Service
 * </p>
 * 创建时间：2016年5月17日
 */
@Service
@Transactional
public class SystemUserLogService extends LSBaseService {

    private final static String ORDER_BY = " /~ order by [sortColumns] ~/ ";
    @Resource
    private SystemModules systemModules;
    @Resource
    private SystemUserLogDao systemUserLogDao;
    @Resource
    private SystemUserDao systemUserDao;
    @Resource
    private SystemOperateLogDao systemOperateLogDao;

    /**
     * 保存用户详细日志信息
     * 
     * @param systemUserLog
     */
    public void save(LsSystemUserLogBO systemUserLog) {
        systemUserLogDao.save(systemUserLog);
    }

    /**
     * 修改用户日志信息
     * 
     * @param systemUserLog
     */
    public void modify(LsSystemUserLogBO systemUserLog) {
        systemUserLogDao.update(systemUserLog);
    }

    public LsSystemUserLogBO findById(String id) {
        return systemUserLogDao.findById(id);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findSystemUserLogList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) {
        /*
         * String queryString = "select t  from LsSystemUserBO t " +
         * " where t.logoutTime is null " +
         * "/~ and t.userName like '%[userName]%' ~/" +
         * "/~ and t.ipAddress like '%[ipAddress]%' ~/" +
         * "/~ order by [sortColumns] ~/"; PageList<LsSystemUserBO> pageList =
         * systemUserDao.pageQuery(queryString, pageQuery); return
         * fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
         */
        String queryString = "select t,r  "
                + "from LsSystemUserBO t,LsSystemUserRoleBO r where t.logoutTime is null and t.logonTime is not null "
                + "and t.userId=r.userId";
        if (!systemModules.isPatrolOn()) {
            queryString += " and r.roleId not in(7,9,10)";
        }
        if (!systemModules.isRiskOn()) {
            queryString += " and r.roleId not in(6)";
        }
        if (!systemModules.isApprovalOn()) {
            queryString += " and r.roleId not in(2,3,5) ";
        }
        queryString += "/~ and t.userName like '%[userName]%' ~/ " + " /~ and t.ipAddress like '%[ipAddress]%' ~/ "
                + " /~ order by [sortColumns] ~/";
        PageList<Object> queryList = systemUserDao.pageQuery(queryString, pageQuery);
        PageList<SessionUser> pageList = new PageList<SessionUser>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                SessionUser sessionUser = new SessionUser();
                BeanUtils.copyProperties(objs[1], sessionUser);
                BeanUtils.copyProperties(objs[0], sessionUser);
                pageList.add(sessionUser);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);

    }

    /**
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findUserOperateLogList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) {
        String queryString = "select t,u from LsSystemOperateLogBO t,LsSystemUserBO u,LsSystemUserRoleBO r where 1=1 ";
        if (!systemModules.isPatrolOn()) {
            queryString += " and r.roleId not in(7,9,10)";
        }
        if (!systemModules.isRiskOn()) {
            queryString += " and r.roleId not in(6)";
        }
        if (!systemModules.isApprovalOn()) {
            queryString += " and r.roleId not in(2,3,5) ";
        }
        queryString += " and u.userId=t.operateUser and u.userId=r.userId " + "/~ and t.ipAddress = '[ip]' ~/"
                + "/~ and t.operateTime > '[logonTime]' ~/" + "/~ and t.operateUser = '[logUser]' ~/"
                + "/~ order by [sortColumns] ~/";

        PageList<Object> queryList = systemOperateLogDao.pageQuery(queryString, pageQuery);
        PageList<SystemOperateLogVO> pageList = new PageList<SystemOperateLogVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                SystemOperateLogVO systemOperateLogVO = new SystemOperateLogVO();
                BeanUtils.copyProperties(objs[0], systemOperateLogVO);
                BeanUtils.copyProperties(objs[1], systemOperateLogVO);
                pageList.add(systemOperateLogVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 查询访问日志列表
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findUserLogList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        String queryString = "select t,u "
                + " from LsSystemUserLogBO t,LsSystemUserBO u,LsSystemUserRoleBO r  where 1=1 "
                + "  and u.userId=t.logUser and u.userId=r.userId ";

        if (!systemModules.isPatrolOn()) {
            queryString += " and r.roleId not in (7,9,10) ";
        }
        if (!systemModules.isRiskOn()) {
            queryString += " and r.roleId not in (6) ";
        }
        if (!systemModules.isApprovalOn()) {
            queryString += " and r.roleId not in(2,3,5) ";
        }
        queryString += "/~ and t.ipAddress like '%[ipAddress]%' ~/" + "/~ and t.logonSystem = '[logonSystem]' ~/"
                + "/~ and t.logoutType = '[logoutType]' ~/" + "/~ and t.logonTime >= '[logonStartTime]' ~/"
                + "/~ and t.logonTime <= '[logonEndTime]' ~/" + "/~ and t.logoutTime >= '[logoutStartTime]' ~/"
                + "/~ and t.logoutTime <= '[logoutEndTime]' ~/";
        // 获取logUserId条件
        Map<String, Object> filtersMap = pageQuery.getFilters();
        if (filtersMap != null) {
            if (filtersMap.get("logUser") != null) {
                String logUserId = (String) filtersMap.get("logUser");
                if (NuctechUtil.isNotNull(logUserId)) {
                    String inCondition = joinInCondition("t.logUser", logUserId);
                    queryString += inCondition;
                }
            }
        }
        queryString += ORDER_BY;
        PageList<Object> queryList = systemUserDao.pageQuery(queryString, pageQuery);
        PageList<SystemUserLogVO> pageList = new PageList<SystemUserLogVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                SystemUserLogVO systemUserLogVO = new SystemUserLogVO();
                BeanUtils.copyProperties(objs[0], systemUserLogVO);
                systemUserLogVO.setLogUserName(((LsSystemUserBO) objs[1]).getUserName());
                systemUserLogVO.setUserName(((LsSystemUserBO) objs[1]).getUserName());
                pageList.add(systemUserLogVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 拼接带in的条件语句：value1,value2,value3 --> and xxx in ('xxx','xxx')
     * 
     * @param columnName
     *        字段名
     * @param value
     *        值（value1,value2,value3...）
     * @return
     */
    private String joinInCondition(String columnName, String value) {
        StringBuffer inCondition = new StringBuffer();
        String[] values = filterStr(value).split("\\s*,\\s*");
        if (values != null && values.length > 0) {
            inCondition.append(" and ").append(columnName).append(" in ('").append(value.replaceAll("\\s*,\\s*", "','"))
                    .append("')");
        }
        return inCondition.toString();
    }

    /**
     * 过滤查询条件的特殊字符
     * 
     * @param str
     */
    protected String filterStr(String str) {
        return str.replaceAll("\\[", "[[]").replaceAll("\\^", "[^]").replaceAll("_", "[_]").replaceAll("%", "[%]");
    }

    public LsSystemUserLogBO findOneByUserId(String userId) {
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("logUser", userId);
        HashMap<String, String> orderby = new HashMap<String, String>();
        orderby.put("logonTime", "desc");
        return systemUserLogDao.findByProperties(propertiesMap, orderby);
    }
}
