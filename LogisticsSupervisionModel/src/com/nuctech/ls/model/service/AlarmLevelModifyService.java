package com.nuctech.ls.model.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.dm.LsDmAlarmLevelBO;
import com.nuctech.ls.model.bo.dm.LsDmAlarmTypeBO;
import com.nuctech.ls.model.dao.AlarmLevelDao;
import com.nuctech.ls.model.dao.AlarmLevelModifyDao;
import com.nuctech.ls.model.vo.alarm.AlarmLevelTypeVO;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 作者： 赵苏阳
 *
 * 描述：
 * <p>
 * 报警级别修改 Service
 * </p>
 * 创建时间：2016年6月19日
 */

@Service
@Transactional
public class AlarmLevelModifyService extends LSBaseService {

    @Resource
    private AlarmLevelModifyDao alarmLevelModifyDao;

    @Resource
    private AlarmLevelDao alarmLevelDao;

    @Resource
    private SystemModules systemModules;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public JSONObject fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        String queryString = "select l,t,u,r "
                + "from LsDmAlarmLevelBO l,LsDmAlarmTypeBO t,LsSystemUserBO u,LsSystemUserRoleBO r where 1=1"
                + " and l.alarmLevelId=t.alarmLevelId " + " and t.updateUser=u.userId " + " and u.userId=r.userId ";
        if (!systemModules.isPatrolOn()) {// 巡逻队模块不存在
            queryString += " and r.roleId not in(7,9,10)";
        }
        if (!systemModules.isRiskOn()) {// 风险分析模块不存在
            queryString += " and r.roleId not in(6)";
        }
        queryString += "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = alarmLevelModifyDao.pageQuery(queryString, pageQuery);
        PageList<AlarmLevelTypeVO> pageList = new PageList<AlarmLevelTypeVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                AlarmLevelTypeVO alarmLevelTypeVO = new AlarmLevelTypeVO();
                BeanUtils.copyProperties(objs[0], alarmLevelTypeVO);
                BeanUtils.copyProperties(objs[1], alarmLevelTypeVO);
                BeanUtils.copyProperties(objs[2], alarmLevelTypeVO);
                pageList.add(alarmLevelTypeVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 查找所有报警(left join)
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings("rawtypes")
    public JSONObject findAllAlarmTypeLevel(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) {
        PageList<AlarmLevelTypeVO> pageList = alarmLevelModifyDao.findAllAlarmTypeLevel(pageQuery);
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 修改报警级别
     * 
     * @param lsDmAlarmLevelBO
     */
    public void modify(LsDmAlarmTypeBO lsDmAlarmTypeBO) {
        alarmLevelModifyDao.merge(lsDmAlarmTypeBO);
    }

    public LsDmAlarmTypeBO find(String id) {
        return alarmLevelModifyDao.findById(id);
    }

    public LsDmAlarmLevelBO findAlarmLevel(String id) {
        return (alarmLevelDao.findById(id));
    }

}
