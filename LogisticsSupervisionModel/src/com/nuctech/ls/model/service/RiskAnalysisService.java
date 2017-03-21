package com.nuctech.ls.model.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.dao.RiskAnalysisDao;
import com.nuctech.ls.model.vo.analysis.RiskAnalysisVo;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service
@Transactional
public class RiskAnalysisService extends LSBaseService {

    @Resource
    private RiskAnalysisDao riskAnalysisDao;

    @SuppressWarnings("rawtypes")
    public JSONObject fromLpnObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) {
        PageList<RiskAnalysisVo> pageList = findLpnRiskAnalysisList(pageQuery);
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    @SuppressWarnings("rawtypes")
    private PageList<RiskAnalysisVo> findLpnRiskAnalysisList(PageQuery<Map> pageQuery) {
        return riskAnalysisDao.findLpnRiskAnalysisList(pageQuery);
    }

    @SuppressWarnings("rawtypes")
    public JSONObject fromDriverObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) {
        PageList<RiskAnalysisVo> pageList = findDriverRiskAnalysisList(pageQuery);
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    private PageList<RiskAnalysisVo>
            findDriverRiskAnalysisList(@SuppressWarnings("rawtypes") PageQuery<Map> pageQuery) {
        return riskAnalysisDao.findDriverRiskAnalysisList(pageQuery);
    }

    @SuppressWarnings("rawtypes")
    public JSONObject fromLpnObjectListByVehiclePlateNum(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) {
        PageList<RiskAnalysisVo> pageList = new PageList<RiskAnalysisVo>();
        pageList.add(findLpnRiskAnalysisByVehiclePlateNum(pageQuery));
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    @SuppressWarnings("rawtypes")
    private RiskAnalysisVo findLpnRiskAnalysisByVehiclePlateNum(PageQuery<Map> pageQuery) {
        return riskAnalysisDao.findLpnRiskAnalysisByVehiclePlateNum(pageQuery);
    }

    @SuppressWarnings("rawtypes")
    public JSONObject fromDriverObjectListByDriverName(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) {
        PageList<RiskAnalysisVo> pageList = new PageList<RiskAnalysisVo>();
        pageList.add(findDriverRiskAnalysisByDriverName(pageQuery));
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    @SuppressWarnings("rawtypes")
    private RiskAnalysisVo findDriverRiskAnalysisByDriverName(PageQuery<Map> pageQuery) {
        return riskAnalysisDao.findDriverRiskAnalysisByDriverName(pageQuery);
    }
}
