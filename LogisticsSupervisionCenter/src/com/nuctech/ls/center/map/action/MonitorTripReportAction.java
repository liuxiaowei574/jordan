package com.nuctech.ls.center.map.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.service.CommonVehicleService;
import com.nuctech.ls.model.service.MonitorAlarmService;
import com.nuctech.ls.model.service.MonitorTripReportService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.vo.monitor.MonitorTripVehicleVO;
import com.nuctech.ls.model.vo.monitor.ViewAlarmReportVO;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;

/**
 * 行程监管信息Action
 * 
 * @author liushaowei
 *
 */
@Namespace("/monitortripreport")
public class MonitorTripReportAction extends LSBaseAction {

	private static final long serialVersionUID = 1L;
	/**
	 * 列表排序字段
	 */
	private static final String DEFAULT_SORT_COLUMNS_WITH_ALIAS = "t.checkoutTime DESC";

	/**
	 * 行程监管信息Service
	 */
	@Resource
	private MonitorTripReportService monitorTripReportService;

	/**
	 * 行程信息Service
	 */
	@Resource
	private MonitorTripService monitorTripService;

	/**
	 * 车辆信息Service
	 */
	@Resource
	private CommonVehicleService commonVehicleService;

	/**
	 * 组织机构 Service
	 */
	@Resource
	private SystemDepartmentService systemDepartmentService;
	
	/**
	 * 系统用户相关 Service
	 */
	@Resource
	private SystemUserService systemUserService;

	/**
	 * 报警信息Service
	 */
	@Resource
	private MonitorAlarmService monitorAlarmService;

	/**
	 * 行程信息及车辆信息
	 */
	private MonitorTripVehicleVO tripVehicleVO;

	private List<ViewAlarmReportVO> alarmList;
	/**
	 * 行程照片存储路径
	 */
	@Resource
	private String tripPhotoPathHttp;

	/**
	 * 跳转到列表页面
	 * 
	 * @return
	 */
	@Action(value = "toList", results = { @Result(name = "success", location = "/monitor/tripReport/list.jsp") })
	public String toList() {
		return SUCCESS;
	}

	/**
	 * 列表查询
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked" })
	@Action(value = "list")
	public String list() throws IOException {
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
		JSONObject retJson = monitorTripReportService.findTripReportList(pageQuery);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(retJson.toString());
		out.flush();
		out.close();
		return null;
	}

	/**
	 * 跳转到编辑页面
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "toDetail", results = { @Result(name = "success", location = "/monitor/tripReport/detail.jsp") })
	public String toDetail() {
		String tripId = request.getParameter("s_tripId");
		tripVehicleVO = new MonitorTripVehicleVO();
		if (!NuctechUtil.isNull(tripId)) {
			LsMonitorTripBO lsMonitorTripBO = monitorTripService.findMonitortripById(tripId);
			if (lsMonitorTripBO != null) {
				LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService.findById(lsMonitorTripBO.getVehicleId());
				BeanUtils.copyProperties(lsMonitorTripBO, tripVehicleVO);
				BeanUtils.copyProperties(lsCommonVehicleBO, tripVehicleVO);
				tripVehicleVO.setCheckinPortName(systemDepartmentService.findById(tripVehicleVO.getCheckinPort()).getOrganizationName());
				tripVehicleVO.setCheckoutPortName(systemDepartmentService.findById(tripVehicleVO.getCheckoutPort()).getOrganizationName());
				tripVehicleVO.setCheckinUserName(systemUserService.findById(tripVehicleVO.getCheckinUser()).getUserName());
				if(NuctechUtil.isNotNull(tripVehicleVO.getCheckoutUser())) {
					tripVehicleVO.setCheckoutUserName(systemUserService.findById(tripVehicleVO.getCheckoutUser()).getUserName());
				}

				pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
				alarmList = monitorAlarmService.findListByTripId(pageQuery);
			}
			return SUCCESS;
		} else {
			return ERROR;
		}
	}

	public List<ViewAlarmReportVO> getAlarmList() {
		return alarmList;
	}

	public void setAlarmList(List<ViewAlarmReportVO> alarmList) {
		this.alarmList = alarmList;
	}

	public MonitorTripVehicleVO getTripVehicleVO() {
		return tripVehicleVO;
	}

	public void setTripVehicleVO(MonitorTripVehicleVO tripVehicleVO) {
		this.tripVehicleVO = tripVehicleVO;
	}

	public String getTripPhotoPathHttp() {
		return tripPhotoPathHttp;
	}

	public void setTripPhotoPathHttp(String tripPhotoPathHttp) {
		this.tripPhotoPathHttp = tripPhotoPathHttp;
	}

}
