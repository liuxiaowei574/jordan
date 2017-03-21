package com.nuctech.ls.model.service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemOperateLogBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.dao.SystemOperateLogDao;
import com.nuctech.ls.model.vo.system.SystemOperateLogVO;
import com.nuctech.util.NuctechUtil;
import com.nuctech.util.SystemUtil;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 系统操作日志 Service
 * </p>
 * 创建时间：2016年5月18日
 */
@Service
@Transactional
public class SystemOperateLogService extends LSBaseService {

    private final static String ORDER_BY = " /~ order by [sortColumns] ~/ ";

    @Resource
    private SystemOperateLogDao systemOperateLogDao;

    @Resource
    private SystemModules systemModules;// 系统配置模块

    /**
     * 添加操作日志
     * 
     * @param log
     *        操作日志对象
     */
    public void addLog(LsSystemOperateLogBO log) {
        systemOperateLogDao.save(log);
    }

    /**
     * 添加操作日志
     * 
     * @param operate
     *        操作内容描述
     * @param entity
     *        操作实体描述
     * @param operateUser
     *        操作人
     * @param operateClass
     *        操作实体类
     * @param transferData
     *        传输的数据
     */
    public void addLog(String operate, String entity, String operateUser, String operateClass, String transferData) {
        LsSystemOperateLogBO systemOperateLog = new LsSystemOperateLogBO();
        systemOperateLog.setOperateId(UUID.randomUUID().toString());
        systemOperateLog.setIpAddress(SystemUtil.getIpAddr(ServletActionContext.getRequest()));
        systemOperateLog.setOperateUser(operateUser);
        systemOperateLog.setOperateDesc(operate);
        systemOperateLog.setOperateType(entity);
        systemOperateLog.setOperateClass(operateClass);
        systemOperateLog.setTransferData(transferData);
        systemOperateLog.setOperateTime(new Date());
        systemOperateLogDao.save(systemOperateLog);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public JSONObject findLogList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        String queryString = "select t,u "
                + " from LsSystemOperateLogBO t,LsSystemUserBO u,LsSystemUserRoleBO r where 1=1 ";
        if (!systemModules.isPatrolOn()) {// 巡逻队模块不存在
            queryString += " and r.roleId not in(7,9,10)";
        }
        if (!systemModules.isRiskOn()) {// 风险分析模块不存在
            queryString += " and r.roleId not in(6)";
        }
        if (!systemModules.isApprovalOn()) {
            queryString += " and r.roleId not in(2,3,5) ";
        }
        queryString += "  and u.userId = t.operateUser and u.userId=r.userId "
                + "/~ and t.ipAddress like '%[ipAddress]%' ~/" + "/~ and t.operateDesc = '[operateDesc]' ~/"
                + "/~ and t.operateType = '[operateType]' ~/" + "/~ and t.operateTime >= '[operateStartTime]' ~/"
                + "/~ and t.operateTime <= '[operateEndTime]' ~/";
        // 获取logUserId条件
        Map<String, Object> filtersMap = pageQuery.getFilters();
        if (filtersMap != null) {
            if (filtersMap.get("logUserId") != null) {
                String logUserId = (String) filtersMap.get("logUserId");
                if (NuctechUtil.isNotNull(logUserId)) {
                    String inCondition = joinInCondition("t.operateUser", logUserId);
                    queryString += inCondition;
                }
            }
        }
        queryString += ORDER_BY;
        PageList<Object> queryList = systemOperateLogDao.pageQuery(queryString, pageQuery);
        PageList<SystemOperateLogVO> pageList = new PageList<SystemOperateLogVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                SystemOperateLogVO systemOperateLogVO = new SystemOperateLogVO();
                BeanUtils.copyProperties(objs[0], systemOperateLogVO);
                systemOperateLogVO.setOperateUserName(((LsSystemUserBO) objs[1]).getUserName());
                systemOperateLogVO.setUserName(((LsSystemUserBO) objs[1]).getUserName());
                pageList.add(systemOperateLogVO);
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
}
