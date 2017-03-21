package com.nuctech.ls.statistic.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.SystemModules;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.service.CommonVehicleService;
import com.nuctech.ls.model.service.GoodsStatisticsService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.StatisitcService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.service.TransportDriverStatisService;
import com.nuctech.ls.model.vo.statistic.DepartmentVo;
import com.nuctech.ls.model.vo.statistic.TransportDriverStatisVo;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.DateUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 作者： 梁朋飞
 *
 * 描述：
 * <p>
 * 报表相关 Action
 * </p>
 * 创建时间：2016年9月2日
 */
@Namespace("/statisitc")
public class StatiticAction extends LSBaseAction {

    @Resource
    private StatisitcService statisitcService;
    @Resource
    private MonitorTripService monitorTripService;
    @Resource
    private SystemModules systemModules;// 系统配置模块
    private String driverName;
    @Resource
    private TransportDriverStatisService transportDriverStatisService;
    @Resource
    private GoodsStatisticsService goodsStatisticsService;
    @Resource
    private SystemDepartmentService systemDepartmentService;
    private String userName;
    private List<LsSystemDepartmentBO> deptList = new ArrayList<LsSystemDepartmentBO>();
    protected static final String DEFAULT_SORT_COLUMNS = "deviceType ASC";
    private String deviceNum;
    private String belongTo;
    @Resource
    private SystemUserService systemUserService;
    private List<?> userList;
    
    @Resource
    private CommonVehicleService commonVehicleService;
    /**
     * 统计分析首页
     * @throws Exception 
     * 
     * @throws IOException
     */
    @Action(value = "toList", results = { @Result(name = "success", location = "/statistics/index.jsp") })
    public String toList() throws Exception {
        // 查询运输司机的数量
        List<TransportDriverStatisVo> driverList = transportDriverStatisService.getList(driverName);
        int driverNum = driverList.size();
        request.setAttribute("driverNum", driverNum);
        //货物种类
        List<?> goodsTypeList = goodsStatisticsService.findGoodsType();
        int goodsTypeNum = goodsTypeList.size();
        request.setAttribute("goodsTypeNum", goodsTypeNum);
        //车辆数量
        int vehicleNum = commonVehicleService.findAllCommonVehicleBOCount();
        request.setAttribute("vehicleNum", vehicleNum);
        //监控行程
        List<?> tripList= monitorTripService.findAllTrips();
        int tripNum = tripList.size();
        request.setAttribute("tripNum", tripNum);
        //统计被使用过的设备数量
        int elockNum =monitorTripService.findElockCount();
        int esealNum = monitorTripService.findEsealCount();
        int sensorNum = monitorTripService.findSensorCount();
        int devicNum = elockNum+esealNum+sensorNum;
        request.setAttribute("devicNum", devicNum);
        //统计用户数量
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String organizationId = sessionUser.getOrganizationId();
        userList = systemUserService.findUserByOrganizationId(organizationId);
        int userNum = userList.size();
        request.setAttribute("userNum", userNum);
        //已对接国家
        int countryNum = statisitcService.querycount();
        request.setAttribute("countryNum", countryNum);
        return SUCCESS;
    }

    /**
     * 
     * 车辆统计报表汇总
     * 
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Action(value = "cljgtj")
    public String cljgtj() throws InstantiationException, IllegalAccessException, IOException {
        pageQuery = this.newPageQuery("d.ORGANIZATION_NAME asc");
        pageQuery = dealDateParam(pageQuery);
        JSONArray sumArr = statisitcService.queryVehicleStaic(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(sumArr.toString());
        out.flush();
        out.close();

        return SUCCESS;

    }

    /**
     * 车辆统计报表汇明细
     * 
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Action(value = "cljgtjDetail")
    public String cljgtjDetail() throws InstantiationException, IllegalAccessException, IOException {
        pageQuery = this.newPageQuery("d.organizationShort asc");
        pageQuery = dealDateParam(pageQuery);
        JSONObject json = statisitcService.queryVehicleStaicDetail(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
        out.close();

        return SUCCESS;

    }

    /**
     * 
     * 行程统计报表汇总
     * 
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Action(value = "jgxctj")
    public String jgxctj() throws InstantiationException, IllegalAccessException, IOException {
        pageQuery = this.newPageQuery("d.ORGANIZATION_NAME asc");
        pageQuery = dealDateParam(pageQuery);
        JSONArray sumArr = statisitcService.queryTripStaic(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(sumArr.toString());
        out.flush();
        out.close();

        return SUCCESS;

    }

    /**
     * 行程统计报表汇明细
     * 
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Action(value = "jgxctjDetail")
    public String jgxctjDetail() throws InstantiationException, IllegalAccessException, IOException {
        pageQuery = this.newPageQuery("d.organizationShort asc");
        if (pageQuery.getSortColumns().contains("status") || pageQuery.getSortColumns().contains("ctime")) {
            pageQuery = this.newPageQuery("t.organizationShort asc");
        }
        pageQuery = dealDateParam(pageQuery);
        JSONObject json = statisitcService.queryTripStaicDetail(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
        out.close();

        return SUCCESS;

    }

    /**
     * 已对接国家/公司信息
     * 
     * @returnquerycountCountry
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Action(value = "duijieguojiaReport")
    public String djgjDetail() throws InstantiationException, IllegalAccessException, IOException {
        pageQuery = this.newPageQuery("d.organizationName asc");
        JSONObject json = statisitcService.queryDepartmentDetail(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
        out.close();

        return SUCCESS;

    }

    /**
     * 已对接国家/公司总数
     * 
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @Action(value = "countCountry")
    public String countCountry() throws InstantiationException, IllegalAccessException, IOException {
        String organizationType = "";
        List<DepartmentVo> departmentList = statisitcService.querycountCountry(organizationType);
        JSONArray retJson = JSONArray.fromObject(departmentList);
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();

        return SUCCESS;

    }

    /**
     * 根据用户名称查询用户信息及用户（历史累计）在线时长
     * 
     * @param userName
     * @return
     */
    @Action(value = "userOnline")
    public String userDetail() throws InstantiationException, IllegalAccessException, IOException {
        JSONArray json = (JSONArray) statisitcService.queryUserDetail(userName);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
        out.close();

        return SUCCESS;

    }

    @SuppressWarnings("unchecked")
    private PageQuery<Map> dealDateParam(PageQuery<Map> pageQuery) {
        String startDate = (String) pageQuery.getFilters().get("starttime");
        String endDate = (String) pageQuery.getFilters().get("endtime");
        String timeFormat = (String) pageQuery.getFilters().get("timeFormat");
        if (!StringUtils.isEmpty(startDate)) {
            pageQuery.getFilters().put("dateStart", DateUtils.toStdString(startDate, timeFormat));
        }
        if (!StringUtils.isEmpty(endDate)) {
            pageQuery.getFilters().put("dateEnd", DateUtils.toStdString(endDate, timeFormat));
        }
        return pageQuery;
    }

    /**
     * 跳转到设备使用统计
     * @return
     */
    @Action(value = "deviceUseList", results = { @Result(name = "success", location = "/statistics/sbsytj.jsp") })
    public String deviceUseList() {
        //查询系统机构中的所有口岸
        deptList = systemDepartmentService.findAllPort();
        return SUCCESS;
    }
    
    @Action(value = "cljgtjIndex", results = { @Result(name = "success", location = "/statistics/cljgtj.jsp") })
    public String cljgtjIndex() {
        //查询系统机构中的所有口岸/中心
        deptList = systemDepartmentService.findAllPort();
        return SUCCESS;
    }
    
    @Action(value = "jgxctjIndex", results = { @Result(name = "success", location = "/statistics/jgxctj.jsp") })
    public String jgxctjIndex() {
        //查询系统机构中的所有口岸/中心
        deptList = systemDepartmentService.findAllPort();
        return SUCCESS;
    }
    
    
    /**
     * 关锁使用统计列表展示
     * @throws IOException
     */
    @Action(value = "elockUseStatic")
    public void elockUseStatic() throws IOException {
        List<?> elockUseList = monitorTripService.findelockUse(deviceNum,belongTo);
        JSONArray retJson = JSONArray.fromObject(elockUseList);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
        
    }
    
    /**
     * 子锁使用统计列表展示
     * @throws IOException
     */
    @Action(value = "esealUseStatic")
    public void esealUseStatic() throws IOException {
        List<?> esealUseList = monitorTripService.findesealUse(deviceNum,belongTo);
        JSONArray retJson = JSONArray.fromObject(esealUseList);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }
    
    /**
     * 传感器使用统计列表展示
     * @throws IOException
     */
    @Action(value = "sensorUseStatic")
    public void sensorUseStatic() throws IOException {
        List<?> sensorUseList = monitorTripService.findsensorUse(deviceNum,belongTo);
        JSONArray retJson = JSONArray.fromObject(sensorUseList);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }
    
    
    /**
     * 用户活跃度
     * @return
     */
    @Action(value = "yhhyd", results = { @Result(name = "success", location = "/statistics/yhhyd.jsp") })
    public String yhhyd() {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String organizationId = sessionUser.getOrganizationId();
        userList = systemUserService.findUserByOrganizationId(organizationId);
        return SUCCESS;
    }
    
    /**
     * 已对接国家/公司统计
     * @return
     */
    @Action(value = "gjgstj", results = { @Result(name = "success", location = "/statistics/gjgstj.jsp") })
    public String gjgstj() {
        return SUCCESS;
    }
    
    public List<LsSystemDepartmentBO> getDeptList() {
        return deptList;
    }

    
    public void setDeptList(List<LsSystemDepartmentBO> deptList) {
        this.deptList = deptList;
    }

    public SystemModules getSystemModules() {
        return systemModules;
    }

    public void setSystemModules(SystemModules systemModules) {
        this.systemModules = systemModules;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getDeviceNum() {
        return deviceNum;
    }

    
    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getBelongTo() {
        return belongTo;
    }
    
    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    
    public List<?> getUserList() {
        return userList;
    }

    
    public void setUserList(List<?> userList) {
        this.userList = userList;
    }

}
