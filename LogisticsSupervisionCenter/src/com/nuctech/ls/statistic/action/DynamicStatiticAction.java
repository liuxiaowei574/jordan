package com.nuctech.ls.statistic.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.JSONUtil;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.common.page.JsonDateValueProcessor;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceApplicationBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceDispatchBO;
import com.nuctech.ls.model.service.PerformanceAnalysisService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.WarehouseDeviceApplicationService;
import com.nuctech.ls.model.service.WarehouseDispatchAnalysisService;
import com.nuctech.ls.model.util.OrganizationType;
import com.nuctech.ls.model.vo.analysis.PerformanceAnalysisVo;
import com.nuctech.ls.model.vo.analysis.RiskSettingPageVo;
import com.nuctech.ls.model.vo.statistic.PortReportVo;
import com.nuctech.ls.model.vo.statistic.ReportDimensionVo;
import com.nuctech.ls.model.vo.statistic.ReportThemeVo;
import com.nuctech.ls.model.vo.statistic.ReportVo;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.DeviceDemand;
import com.nuctech.ls.model.vo.warehouse.DeviceInventoryChartsVO;
import com.nuctech.ls.model.vo.warehouse.DispatchActualProgram;
import com.nuctech.ls.model.vo.warehouse.DispatchPlanVO;
import com.nuctech.ls.model.vo.warehouse.DispatchPortVO;
import com.nuctech.ls.model.vo.warehouse.PortElockStatisitcVO;
import com.nuctech.util.Constant;
import com.nuctech.util.DateUtils;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;

/**
 * 
 * 作者： 梁朋飞
 *
 * 描述：
 * <p>
 * 动态报表相关 Action
 * </p>
 * 创建时间：2016年9月2日
 */
@Namespace("/statisitc")
public class DynamicStatiticAction extends LSBaseAction {

    // 报表
    private List<ReportDimensionVo> dimension;
    private JSONArray theme = new JSONArray();
    private String data;
    private String columns;
    @Resource
    private PerformanceAnalysisService performanceAnalysisService;
    @Resource
    private WarehouseDispatchAnalysisService warehouseDispatchAnalysisService;

    protected static final String DEFAULT_SORT_COLUMNS = "d.organizationId ASC";

    // 口岸动态报表VO
    PortReportVo p1 = new PortReportVo();

    /**
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * 
     *         动态报表入口
     * 
     *         1.初始化主题维度树
     * 
     */
    @Action(value = "dynamic", results = { @Result(name = "success", location = "/statistics/dynamic.jsp") })
    public String dynamic() throws InstantiationException, IllegalAccessException {

        List<ReportThemeVo> trees = new ArrayList<ReportThemeVo>();
        ReportThemeVo root = new ReportThemeVo();
        root.setId(0);
        root.setName(getLocaleString("link.statistic.dynamic"));
        List<ReportThemeVo> rp1 = p1.createTheme(0, 1);
        trees.addAll(rp1);
        trees.add(root);
        this.theme.addAll(trees);

        return SUCCESS;

    }

    /**
     * @throws IOException
     * 
     *         动态报表-口岸报表
     *         1.口岸行程维度查询
     *         2，口岸设备维度查询
     *         3，行程查询
     * 
     */
    @Action(value = "queryData")
    public void queryData() throws IOException {
        // 1
        List<PortReportVo> plist = new ArrayList<PortReportVo>();
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        pageQuery = dealDateParam(pageQuery);
        List<PerformanceAnalysisVo> pageList = performanceAnalysisService.findPortlAnalysis(pageQuery);
        if (pageList != null) {
            for (PerformanceAnalysisVo p : pageList) {
                PortReportVo pvo = new PortReportVo();
                pvo.setName(p.getUserName());
                pvo.setXingchengjieshu(p.getDealCheckins() + "");
                pvo.setXingchengkaishi(p.getDealCheckouts() + "");
                pvo.setAvgtimes(p.getAvglineTime() + "");
                pvo.setZaixianshichang(p.getOnlineTime() + "");
                plist.add(pvo);
            }
            // 2
            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            List<PortElockStatisitcVO> dispatchPortList = warehouseDispatchAnalysisService
                    .findDispatchPortAndRoomList(null, sessionUser.getUserId());
            for (PortElockStatisitcVO elock : dispatchPortList) {
                for (PortReportVo pr : plist) {
                    if (StringUtils.equals(pr.getName(), elock.getPortName())) {
                        pr.setKeyongs(elock.getKeyongs() + "");
                        pr.setSunhuais(elock.getSunhuais() + "");
                        pr.setWeixius(elock.getWeixius() + "");
                        pr.setZaitus(elock.getZaitus() + "");

                    }
                }
            }
        }
        // 3 模拟数据
        Random r = new Random(2);
        for (PortReportVo pr : plist) {
            pr.setDeparture(r.nextInt(10) + "");
            pr.setArrival(r.nextInt(10) + "");
        }

        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(p1.addDatas(plist).createTable().toString());
    }

    private PageQuery<Map> dealDateParam(PageQuery<Map> pageQuery) {
        String startDate = (String) pageQuery.getFilters().get("timeStart");
        String endDate = (String) pageQuery.getFilters().get("timeEnd");
        String timeFormat = (String) pageQuery.getFilters().get("timeFormat");
        if (!StringUtils.isEmpty(startDate)) {
            pageQuery.getFilters().put("dateStart", DateUtils.toStdString(startDate, timeFormat));
        }
        if (!StringUtils.isEmpty(endDate)) {
            pageQuery.getFilters().put("dateEnd", DateUtils.toStdString(endDate, timeFormat));
        }
        return pageQuery;
    }

    public List<ReportDimensionVo> getDimension() {
        return dimension;
    }

    public void setDimension(List<ReportDimensionVo> dimension) {
        this.dimension = dimension;
    }

    public JSONArray getTheme() {
        return theme;
    }

    public void setTheme(JSONArray theme) {
        this.theme = theme;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

}
