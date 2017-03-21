package com.nuctech.ls.model.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemParamsBO;
import com.nuctech.ls.model.dao.SystemParamsDao;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 系统参数 Service
 * </p>
 * 创建时间：2016年6月2日
 */
@Service
@Transactional
public class SystemParamsService extends LSBaseService {

    @Resource
    private SystemModules systemModules;
    /**
     * 
     */
    @Resource
    private SystemParamsDao systemParamsDao;

    /**
     * @return
     */
    public List<LsSystemParamsBO> findAllList() {
        return systemParamsDao.findAll();
    }

    /**
     * 通过参数的Key值获取Value
     * 
     * @param paramCode
     * @return
     */
    public String findSystemParamsValueByKey(String paramCode) {
        return systemParamsDao.findSystemParamsValueByKey(paramCode);
    }

    public LsSystemParamsBO findById(String paramId) {
        return systemParamsDao.findById(paramId);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findParamsList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        String queryString = "select t from LsSystemParamsBO t where 1=1 " + "/~ and t.paramId = '[paramId]' ~/"
                + "/~ and t.paramCode like '%[paramCode]%' ~/" + "/~ and t.paramName like '%[paramName]%' ~/";
        if (!systemModules.isDispatchOn()) {
            queryString += " and t.paramCode!='DISPATCH_STATISTICS_INTERVAL' ";
        }
        if (!systemModules.isPatrolOn()) {
            queryString += " and t.paramCode!='CIRCLE_BUFFER' ";
        }
        queryString += "/~ order by [sortColumns] ~/";
        PageList<LsSystemParamsBO> pageList = systemParamsDao.pageQuery(queryString, pageQuery);
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    public void modifyParams(LsSystemParamsBO systemParams) {
        systemParamsDao.update(systemParams);
    }

    public List<LsSystemParamsBO> getParams(String alarmTypeId) {
        return systemParamsDao.findSystemParams(alarmTypeId);
    }

    /**
     * 根据系统参数的名称查找参数
     */
    public LsSystemParamsBO findByName(String name) {
        name = name.replace(" ", "");
        LsSystemParamsBO systemParamsBO = systemParamsDao.findByProperty("paramName", name);
        return systemParamsBO;
    }
}
