package com.nuctech.ls.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.RiskAnalysisService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Namespace("/analysis")
public class RiskAnalysisAction extends LSBaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = -298396097160676049L;
    protected static final String DEFAULT_SORT_COLUMNS = "userId ASC";
    @Resource
    private RiskAnalysisService riskAnalysisService;
    @Resource
    private MonitorTripService monitorTripService;
    private JSONArray lprArr = new JSONArray();
    private JSONArray driverArr = new JSONArray();

    public JSONArray tripArr = new JSONArray();

    @Action(value = "listTrip", results = { @Result(name = "success", location = "/analysis/tripList.jsp") })
    public String listTrip() {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = monitorTripService.fromObjectList(pageQuery, null, false);
        tripArr = retJson.getJSONArray("rows");
        return SUCCESS;
    }

    @Action(value = "listRisk", results = { @Result(name = "success", location = "/analysis/riskAnalysis.jsp") })
    public String list() {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = riskAnalysisService.fromLpnObjectList(pageQuery, null, false);
        lprArr = retJson.getJSONArray("rows");
        JSONObject retDriverJson = riskAnalysisService.fromDriverObjectList(pageQuery, null, false);
        driverArr = retDriverJson.getJSONArray("rows");
        return SUCCESS;
    }

    @Action(value = "getRisk")
    public String getRisk() {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = riskAnalysisService.fromLpnObjectListByVehiclePlateNum(pageQuery, null, false);
        JSONArray lprArr = retJson.getJSONArray("rows");
        JSONObject retDriverJson = riskAnalysisService.fromDriverObjectListByDriverName(pageQuery, null, false);
        JSONArray driverArr = retDriverJson.getJSONArray("rows");

        JSONObject riskParams = getRiskParams();

        JSONObject json = new JSONObject();
        json.put("lprArr", lprArr);
        json.put("driverArr", driverArr);
        json.put("riskParams", riskParams);
        response.setCharacterEncoding("utf-8");
        try {
            response.getWriter().println(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject getRiskParams() {
        JSONObject riskParams = new JSONObject();
        riskParams.put("RISK_ALARM_TRIP_PERCENT_YELLOW", 33);
        riskParams.put("RISK_ALARM_TRIP_PERCENT_RED", 67); // TODO 是不是100-33
        riskParams.put("RISK_SERIOUS_ALARM_TRIP_PERCENT_YELLOW", 33);
        riskParams.put("RISK_SERIOUS_ALARM_TRIP_PERCENT_RED", 67);
        riskParams.put("RISK_NORMAL_ALARM_TRIP_PERCENT_YELLOW", 33);
        riskParams.put("RISK_NORMAL_ALARM_TRIP_PERCENT_RED", 67);
        riskParams.put("RISK_SERIOUS_ALARM_NUMBER_PERCENT_YELLOW", 33);
        riskParams.put("RISK_SERIOUS_ALARM_NUMBER_PERCENT_RED", 67);
        riskParams.put("RISK_NORMAL_ALARM_NUMBER_PERCENT_YELLOW", 33);
        riskParams.put("RISK_NORMAL_ALARM_NUMBER_PERCENT_RED", 67);
        riskParams.put("RISK_FINAL_YELLOW", 33);
        riskParams.put("RISK_FINAL_RED", 67);
        return riskParams;
    }

    @Action(value = "lpnRiskAnalysis")
    public void lpnRiskAnalysis() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = riskAnalysisService.fromLpnObjectList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    @Action(value = "driverRiskAnalysis")
    public void driverRiskAnalysis() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = riskAnalysisService.fromDriverObjectList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    public JSONArray getLprArr() {
        return lprArr;
    }

    public void setLprArr(JSONArray lprArr) {
        this.lprArr = lprArr;
    }

    public JSONArray getDriverArr() {
        return driverArr;
    }

    public void setDriverArr(JSONArray driverArr) {
        this.driverArr = driverArr;
    }

    public JSONArray getTripArr() {
        return tripArr;
    }

    public void setTripArr(JSONArray tripArr) {
        this.tripArr = tripArr;
    }
}
