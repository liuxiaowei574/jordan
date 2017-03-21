package com.nuctech.ls.model.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsRegularReportParaSetBO;
import com.nuctech.ls.model.dao.RegularReportParaSetDao;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 
 * 作者： zsy
 *
 * 描述：
 * <p>
 * 定时报告参数设置 Service
 * </p>
 * 创建时间：2016年10月26日
 */
@Service
@Transactional
public class RegularReportParaSetService extends LSBaseService {

    @Resource
    private RegularReportParaSetDao regularReportParaSetDao;

    /**
     * 查找定时报告参数设置列表
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findReportPasSetList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) {
        String queryString = "select t from LsRegularReportParaSetBO t where 1=1 "
                + "/~ and t.reportId = '[reportId]' ~/" + "/~ and t.noticeTitle like '%[noticeTitle]%' ~/"
                + "/~ and t.reportName = '[reportName]' ~/" + "/~ and t.reportType like '%[reportType]%' ~/"
                + "/~ and t.cycle like '%[cycle]%' ~/" + "/~ order by [sortColumns] ~/";
        PageList<LsRegularReportParaSetBO> pageList = regularReportParaSetDao.pageQuery(queryString, pageQuery);
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 添加报告参数设置表
     * 
     * @param regularReportParaSetBO
     */
    public void add(LsRegularReportParaSetBO regularReportParaSetBO) {
        regularReportParaSetDao.save(regularReportParaSetBO);
    }

    /**
     * 根据id查询
     * 
     * @param reportId
     * @return
     */
    public LsRegularReportParaSetBO findById(String reportId) {
        return regularReportParaSetDao.findById(reportId);
    }

    public void modifyReportParas(LsRegularReportParaSetBO regularReportParaSetBO) {
        regularReportParaSetDao.merge(regularReportParaSetBO);
    }

    public void delete(String[] ids) {
        String hql = "delete LsRegularReportParaSetBO where reportId in :ids";
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("ids", ids);
        regularReportParaSetDao.batchUpdateOrDeleteByHql(hql, propertiesMap);
    }

    public LsRegularReportParaSetBO findByType(String type) {
        return regularReportParaSetDao.findByProperty("reportType", type);
    }

}
