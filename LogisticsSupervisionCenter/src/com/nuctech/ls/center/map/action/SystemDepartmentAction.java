package com.nuctech.ls.center.map.action;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.center.utils.BeansToJson;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.monitor.LsMonitorRaPointBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.service.MonitorRaPointService;
import com.nuctech.ls.model.service.MonitorRouteAreaService;
import com.nuctech.ls.model.service.SystemDepartmentTestService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Namespace("/systemdepartmenttest")
public class SystemDepartmentAction extends LSBaseAction {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 1L;

	@Resource
	SystemDepartmentTestService departmentTestService;
	@Resource
	private MonitorRouteAreaService monitorRouteAreaService;

	@Resource
	private MonitorRaPointService monitorRaPointService; 
	
	List<LsSystemDepartmentBO> lsSystemDepartmentBOTests;

	@Action(value = "findAllSystemDepartmentBOTest", results = { @Result(name = "success", type = "json") })
	public void findAllSystemDepartmentBOTest() {
		this.lsSystemDepartmentBOTests = departmentTestService.findAllSysDepartment();
		String result = new BeansToJson<LsSystemDepartmentBO, Serializable>()
				.beanToJson(this.lsSystemDepartmentBOTests);
		try {
			this.response.getWriter().println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 记录规划路线或区域
	 * 
	 * @throws IOException
	 */
	@Action(value = "beforPlanRouteArea", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public void beforPlanRouteArea() throws IOException {
		String aceeptData1 = request.getParameter("aceeptData");
		if(NuctechUtil.isNotNull(aceeptData)){
			try {
				JSONObject object = JSONObject.fromObject(aceeptData);
//				LsMonitorRouteAreaBO passport = (LsMonitorRouteAreaBO)JSONObject.toBean(object,
//						LsMonitorRouteAreaBO.class);
//				passport.setRouteAreaId(generatePrimaryKey());
//			    SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
//				if (sessionUser != null) {
//					passport.setCreateUser(sessionUser.getUserId());
//				}
//				passport.setCreateTime(new Date());
				 LsMonitorRouteAreaBO monitorRouteAreaBO = new LsMonitorRouteAreaBO();
				 monitorRouteAreaBO.setRouteAreaId(generatePrimaryKey());
				    monitorRouteAreaBO.setRouteAreaName(object.get("routeAreaName").toString());
				    monitorRouteAreaBO.setBelongToPort(object.get("belongToPort").toString());
				    SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
					if (sessionUser != null) {
						monitorRouteAreaBO.setCreateUser(sessionUser.getUserId());
					}
					monitorRouteAreaBO.setCreateTime(new Date());
					monitorRouteAreaBO.setRouteAreaType(object.get("routeAreaType").toString());
					monitorRouteAreaBO.setRouteAreaStatus(object.get("routeAreaStatus").toString());
					monitorRouteAreaBO.setRouteAreaBuffer(object.get("routeAreaBuffer").toString());
					monitorRouteAreaBO.setRouteCost(object.get("routeCost").toString());
					monitorRouteAreaBO.setRouteDistance(BigDecimal.valueOf(Double.parseDouble(object.get("routeDistance").toString())));
					monitorRouteAreaBO.setStartId(object.get("startId").toString());
					monitorRouteAreaBO.setStartName(object.get("startName").toString());
					monitorRouteAreaBO.setStartLatitude(object.get("startLatitude").toString());
					monitorRouteAreaBO.setStartLongtitude(object.get("startLongtitude").toString());
					monitorRouteAreaBO.setEndId(object.get("endId").toString());
					monitorRouteAreaBO.setEndName(object.get("endName").toString());
					monitorRouteAreaBO.setEndLatitude(object.get("endLatitude").toString());
					monitorRouteAreaBO.setEndLongtitude(object.get("endLongtitude").toString());
				    try {
						monitorRouteAreaService.addMonitorRouteArea(monitorRouteAreaBO);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				   /// routeAreaPtCol = data.
				   	if(NuctechUtil.isNotNull(routeAreaPtCol)){
				   		JSONArray json = JSONArray.fromObject(routeAreaPtCol); // 首先把字符串转成 JSONArray  对象
						if(json.size()>0){
						  for(int i=0;i<json.size();i++){
						    JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
						    LsMonitorRaPointBO bo = new LsMonitorRaPointBO();
						    bo.setPointId(generatePrimaryKey());
						    bo.setRouteAreaId(monitorRouteAreaBO.getRouteAreaId());
						    bo.setGpsSeq(Long.valueOf(i));
						    bo.setLatitude(job.get("lat").toString());
						    bo.setLongitude(job.getString("lng"));
						    try {
								monitorRaPointService.addMonitorRaPoint(bo);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						  }
					  }
					}
				    logger.info(String.format("add routeArea,初始化规划线路编号：%s", monitorRouteAreaBO.getRouteAreaId()));
				  //}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}     
			   
		}
	}

private String routeAreaPtCol;

public String getRouteAreaPtCol() {
	return routeAreaPtCol;
}

public void setRouteAreaPtCol(String routeAreaPtCol) {
	this.routeAreaPtCol = routeAreaPtCol;
}
	/* 路线区域主键 */
	private String routeAreaId;

	/* 路线区域名称 */
	private String routeAreaName;

	/*
	 * 路线区域类型 0-路线，1-安全区域，2-危险区域，3-监管区域，4-区域划分
	 */
	private String routeAreaType;

	/*
	 * 所属节点 每条路线或区域所属于的口岸 区划除外
	 */
	private String belongToPort;

	/* 创建人 */
	private String createUser;

	/* 更新人员 */
	private String updateUser;

	/*
	 * 路线区域状态 0-有效，1-无效
	 */
	private String routeAreaStatus;

	/* 缓冲区 */
	private String routeAreaBuffer;

	/* 路线用时 */
	private String routeCost;

	/* 距离 */
	private String routeDistance;

	/* 起点 */

	/* 起点名称 */
	private String startName;

	/* 起点经度 */
	private String startLongtitude;

	/* 起点纬度 */
	private String startLatitude;

	 /* 起点 */
    private String startId;
	
	 /* 终点 */
    private String endId;
	/* 终点名称 */
	private String endName;

	/* 终点经度 */
	private String endLongtitude;

	/* 终点纬度 */
	private String endLatitude;

	public String getRouteAreaId() {
		return routeAreaId;
	}

	public void setRouteAreaId(String routeAreaId) {
		this.routeAreaId = routeAreaId;
	}


	public String getRouteAreaName() {
		return routeAreaName;
	}

	public void setRouteAreaName(String routeAreaName) {
		this.routeAreaName = routeAreaName;
	}

	public String getRouteAreaType() {
		return routeAreaType;
	}

	public void setRouteAreaType(String routeAreaType) {
		this.routeAreaType = routeAreaType;
	}

	public String getBelongToPort() {
		return belongToPort;
	}

	public void setBelongToPort(String belongToPort) {
		this.belongToPort = belongToPort;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getRouteAreaStatus() {
		return routeAreaStatus;
	}

	public void setRouteAreaStatus(String routeAreaStatus) {
		this.routeAreaStatus = routeAreaStatus;
	}

	public String getStartName() {
		return startName;
	}

	public void setStartName(String startName) {
		this.startName = startName;
	}

	public String getStartLongtitude() {
		return startLongtitude;
	}

	public void setStartLongtitude(String startLongtitude) {
		this.startLongtitude = startLongtitude;
	}

	public String getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(String startLatitude) {
		this.startLatitude = startLatitude;
	}

	public String getEndName() {
		return endName;
	}

	public void setEndName(String endName) {
		this.endName = endName;
	}

	public String getEndLongtitude() {
		return endLongtitude;
	}

	public void setEndLongtitude(String endLongtitude) {
		this.endLongtitude = endLongtitude;
	}

	public String getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(String endLatitude) {
		this.endLatitude = endLatitude;
	}

	public String getRouteDistance() {
		return routeDistance;
	}

	public void setRouteDistance(String routeDistance) {
		this.routeDistance = routeDistance;
	}

	public String getStartId() {
		return startId;
	}

	public void setStartId(String startId) {
		this.startId = startId;
	}

	public String getEndId() {
		return endId;
	}

	public void setEndId(String endId) {
		this.endId = endId;
	}

	private String aceeptData;

	public String getAceeptData() {
		return aceeptData;
	}

	public void setAceeptData(String aceeptData) {
		this.aceeptData = aceeptData;
	}

	
	
}
