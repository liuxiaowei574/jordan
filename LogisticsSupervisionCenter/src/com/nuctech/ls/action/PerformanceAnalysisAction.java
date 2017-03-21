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
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.service.PerformanceAnalysisService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.vo.analysis.PerformanceAnalysisVo;
import com.nuctech.util.DateUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Namespace("/analysis")
public class PerformanceAnalysisAction extends LSBaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1785639145635362459L;
    protected static final String DEFAULT_SORT_COLUMNS = "userId ASC";
    Logger logger = Logger.getLogger(this.getClass());

    @Resource
    private SystemUserService userService;
    @Resource
    private PerformanceAnalysisService performanceAnalysisService;
    public List<LsSystemUserBO> userList = null;
    public JSONArray userArr = new JSONArray();
    public JSONArray useronlineArr = new JSONArray();
    public JSONArray userDealAlarmArr = new JSONArray();

    @Action(value = "performanceAnalysis",
            results = { @Result(name = "success", location = "/analysis/PerformanceAnalysis.jsp") })
    public String performanceAnalysis() {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        pageQuery = dealDateParam(pageQuery);
        userList = userService.findUsers(pageQuery);
        userArr.clear();
        if (userList != null) {
            userArr.addAll(userList);
        }

        List<PerformanceAnalysisVo> pageList = performanceAnalysisService.findPerformanceAnalysis(pageQuery);
        // List<PerformanceAnalysisVo>
        if (pageList != null) {
            useronlineArr.addAll(pageList);
        }
        return SUCCESS;
    }

    @Action(value = "queryEcharts")
    public void queryEcharts() {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        pageQuery = dealDateParam(pageQuery);
        userList = userService.findUsers(pageQuery);
        userArr.clear();
        if (userList != null) {
            userArr.addAll(userList);
        }

        List<PerformanceAnalysisVo> pageList = performanceAnalysisService.findPerformanceAnalysis(pageQuery);
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

        if (startDate != null) {
            pageQuery.getFilters().put("dateStart", DateUtils.toStdString(startDate, timeFormat));
        }
        if (endDate != null) {
            String dateEnd = DateUtils.toStdString(endDate, timeFormat);
            pageQuery.getFilters().put("dateEnd", DateUtils.toStdString(endDate, timeFormat));
        }

        return pageQuery;
    }

    public List<LsSystemUserBO> getUserList() {
        return userList;
    }

    public void setUserList(List<LsSystemUserBO> userList) {
        this.userList = userList;
    }

    public JSONArray getUserArr() {
        return userArr;
    }

    public void setUserArr(JSONArray userArr) {
        this.userArr = userArr;
    }

}
