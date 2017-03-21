package com.nuctech.ls.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.service.PerformanceAnalysisService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.util.OrganizationType;
import com.nuctech.ls.model.vo.analysis.PerformanceAnalysisVo;
import com.nuctech.util.DateUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author liangpengfei
 * 
 *         口岸绩效分析
 *
 */
@Namespace("/analysis")
public class PortAnalysisAction extends LSBaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1785639145635362459L;
    protected static final String DEFAULT_SORT_COLUMNS = "d.organizationId ASC";
    Logger logger = Logger.getLogger(this.getClass());

    @Resource
    private SystemUserService userService;
    @Resource
    private SystemDepartmentService deptService;
    @Resource
    private PerformanceAnalysisService performanceAnalysisService;
    public List<LsSystemDepartmentBO> deptList = null;
    public JSONArray deptArr = new JSONArray();
    public JSONArray useronlineArr = new JSONArray();
    public JSONArray userDealAlarmArr = new JSONArray();

    @Action(value = "portAnalysis", results = { @Result(name = "success", location = "/analysis/portAnalysis.jsp") })
    public String portAnalysis() {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        pageQuery = dealDateParam(pageQuery);
        deptList = deptService.findDepartement(pageQuery, new String[] { "" });
        deptArr.clear();
        if (deptList != null) {
            deptArr.addAll(deptList);
        }

        List<PerformanceAnalysisVo> pageList = performanceAnalysisService.findPortlAnalysis(pageQuery);
        // List<PerformanceAnalysisVo>
        if (pageList != null) {
            useronlineArr.addAll(pageList);
        }
        return SUCCESS;
    }

    @Action(value = "queryPortEcharts")
    public void queryPortEcharts() {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        pageQuery = dealDateParam(pageQuery);
        deptList = deptService.findDepartement(pageQuery, new String[] { "" });
        deptArr.clear();
        if (deptList != null) {
            deptArr.addAll(deptList);
        }

        List<PerformanceAnalysisVo> pageList = performanceAnalysisService.findPortlAnalysis(pageQuery);
        // List<PerformanceAnalysisVo>
        if (pageList != null) {
            useronlineArr.addAll(pageList);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", useronlineArr);
        try {
            response.getWriter().write(jsonObject.toString());
        } catch (IOException e) {
        }
    }

    private PageQuery<Map> dealDateParam(PageQuery<Map> pageQuery) {
        String startDate = (String) pageQuery.getFilters().get("timeStart");
        String endDate = (String) pageQuery.getFilters().get("timeEnd");
        String timeFormat = (String) pageQuery.getFilters().get("timeFormat");
        String organizationType = OrganizationType.Port.getText();

        if (startDate != null) {
            pageQuery.getFilters().put("dateStart", DateUtils.toStdString(startDate, timeFormat));
        }
        if (endDate != null) {
            pageQuery.getFilters().put("dateEnd", DateUtils.toStdString(endDate, timeFormat));
        }
        if (organizationType != null) {
            pageQuery.getFilters().put("organizationType", organizationType);
        }

        return pageQuery;
    }

    public JSONArray getDeptArr() {
        return deptArr;
    }

    public void setDeptArr(JSONArray deptArr) {
        this.deptArr = deptArr;
    }

    public List<LsSystemDepartmentBO> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<LsSystemDepartmentBO> deptList) {
        this.deptList = deptList;
    }

}
