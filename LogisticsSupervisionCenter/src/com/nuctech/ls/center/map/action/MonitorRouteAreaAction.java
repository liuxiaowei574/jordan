package com.nuctech.ls.center.map.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.criterion.Restrictions;

import com.nuctech.ls.center.utils.DateJsonValueProcessor;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.monitor.LsMonitorRaPointBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.service.MonitorRaPointService;
import com.nuctech.ls.model.service.MonitorRouteAreaService;
import com.nuctech.ls.model.util.RouteAreaType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

@ParentPackage("json-default")
@Namespace("/monitorroutearea")
/**
 * 所有规划线路区域
 * @return
 */
public class MonitorRouteAreaAction extends LSBaseAction {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private static final long serialVersionUID = 1L;

	@Resource
	private MonitorRouteAreaService monitorRouteAreaService;
	
	@Resource
	private MonitorRaPointService monitorRaPointService;
	
	private String message;
	private String ids;//id集合
	private boolean success;//结果
	private LsMonitorRouteAreaBO lsMonitorRouteAreaBO;
	private List<LsMonitorRaPointBO> lsMonitorRaPointBOs;
	/**
	 * 所有线路区域
	 */
	private List<LsMonitorRouteAreaBO> lsMonitorRouteAreaBOs;
	 /**
     * 界面初始化获取所有线路区域
     * @return
     */
	
    @Action(value = "findAllRouteAreas", results = {
    		@Result(name="success",type = "json")
    		})
    public void findAllRouteAreas() throws Exception {
    	logger.info("查询路线区域获取到的routeAreaType组合的类型是："+ids);
        this.lsMonitorRouteAreaBOs = this.monitorRouteAreaService.findAllRouteAreas(ids);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); // 排除关联死循环
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
        JSONArray lineitemArray = JSONArray.fromObject(this.lsMonitorRouteAreaBOs, jsonConfig);
        String result = JSONArray.fromObject(lineitemArray).toString();
        logger.info(String.format("查询所有线路或区域信息"));
        try {
            this.response.getWriter().println(result);
        } catch (IOException e) {
        	message = e.getMessage();
        	logger.error(message);
        }
    }

	/**
	 * 记录规划路线或区域
	 * 
	 * @throws IOException 
	 */
	@Action(value = "planRouteArea", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public void planRouteArea() throws IOException {
		LsMonitorRouteAreaBO monitorRouteAreaBO = new LsMonitorRouteAreaBO();
		monitorRouteAreaBO.setRouteAreaId(generatePrimaryKey());
		monitorRouteAreaBO.setRouteAreaName(routeAreaName);
		monitorRouteAreaBO.setBelongToPort(belongToPort);
		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
		if(sessionUser!=null){
			monitorRouteAreaBO.setCreateUser(sessionUser.getUserName());
		}
		monitorRouteAreaBO.setCreateTime(new Date());
		if(NuctechUtil.isNotNull(routeAreaType)){
			monitorRouteAreaBO.setRouteAreaType(routeAreaType);
		}else{
			if(Constant.BUTTON_TYPE_LINE.equals(ids)) {
				//monitorRouteAreaBO.setRouteAreaType(Constant.ROUTEAREA_TYPE_LINE);
				monitorRouteAreaBO.setRouteAreaType(RouteAreaType.LINE.getText());
    		}else if(Constant.BUTTON_TYPE_CDGL.equals(ids)) {
    			//monitorRouteAreaBO.setRouteAreaType(Constant.ROUTEAREA_TYPE_JGQY);
    			monitorRouteAreaBO.setRouteAreaType(RouteAreaType.MONITOR_AREA.getText());
    		}else {
    			//monitorRouteAreaBO.setRouteAreaType(Constant.ROUTEAREA_TYPE_LINE);
    			monitorRouteAreaBO.setRouteAreaType(RouteAreaType.LINE.getText());
    		}
		}
		monitorRouteAreaBO.setRouteAreaStatus(routeAreaStatus);
		monitorRouteAreaBO.setRouteAreaBuffer(routeAreaBuffer);
		monitorRouteAreaBO.setRouteCost(routeCost);
		
		try {
			monitorRouteAreaService.addMonitorRouteArea(monitorRouteAreaBO);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	   	  logger.info(String.format("add routeArea，区域线路编号：%s", monitorRouteAreaBO.getRouteAreaId()));
			
	   	System.out.println(routeAreaPtCol);
	   	if(NuctechUtil.isNotNull(routeAreaPtCol)){
	   		JSONArray json = JSONArray.fromObject(routeAreaPtCol); // 首先把字符串转成 JSONArray  对象
			if(json.size()>0){
			  for(int i=0;i<json.size();i++){
			    JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
			    LsMonitorRaPointBO bo = new LsMonitorRaPointBO();
			    bo.setPointId(generatePrimaryKey());
			    bo.setRouteAreaId(monitorRouteAreaBO.getRouteAreaId());
			    bo.setGpsSeq(i);
			    bo.setLatitude(job.get("lat").toString());
			    bo.setLongitude(job.getString("lng"));
			    try {
					monitorRaPointService.addMonitorRaPoint(bo);
				} catch (Exception e) {
					message = e.getMessage();
					logger.error(message);
					
				}
			    
			  }
		  }
		  logger.info(String.format("添加规划路线或区域"));
		  this.response.getWriter().println(SUCCESS);
		  //return SUCCESS;
		}
	}
	
	/**
	 * 根据线路或区域编号获取坐标集合
	 * 
	 * @param routeAreaId
	 * @return
	 */
	@Action(value = "editRouteArea", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public String editRouteArea() {
		logger.info("编辑路线获取到的routeAreaId是："+ids);
		try {
			this.lsMonitorRouteAreaBO = this.monitorRouteAreaService.findMonitorRouteAreaById(ids);
			this.lsMonitorRaPointBOs = this.monitorRaPointService.findAllMonitorRaPointByRouteAreaId(ids);
			this.success = true;
		} catch (Exception e1) {
			message = e1.getMessage();
			logger.error(message);
			this.success = false;
		}
		return SUCCESS;
	}
	
	
	/**
	 * 修改规划路线或区域
	 * 
	 * @throws IOException 
	 */
	@Action(value = "updateRouteArea", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
	public void updateRouteArea() throws IOException {
		LsMonitorRouteAreaBO monitorRouteAreaBO = new LsMonitorRouteAreaBO();
		monitorRouteAreaBO.setRouteAreaId(routeAreaId);
		monitorRouteAreaBO.setRouteAreaName(routeAreaName);
		monitorRouteAreaBO.setBelongToPort(belongToPort);
		
		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
		if(sessionUser!=null){
			monitorRouteAreaBO.setUpdateUser(sessionUser.getUserName());
		}
		monitorRouteAreaBO.setUpdateTime(new Date());
		if(NuctechUtil.isNotNull(routeAreaType)){
			monitorRouteAreaBO.setRouteAreaType(routeAreaType);
		}else{
			if(Constant.BUTTON_TYPE_LINE.equals(ids)) {
				//monitorRouteAreaBO.setRouteAreaType(Constant.ROUTEAREA_TYPE_LINE);
				monitorRouteAreaBO.setRouteAreaType(RouteAreaType.LINE.getText());
    		}else if(Constant.BUTTON_TYPE_CDGL.equals(ids)) {
    			//monitorRouteAreaBO.setRouteAreaType(Constant.ROUTEAREA_TYPE_JGQY);
    			monitorRouteAreaBO.setRouteAreaType(RouteAreaType.MONITOR_AREA.getText());
    		}else {
    			//monitorRouteAreaBO.setRouteAreaType(Constant.ROUTEAREA_TYPE_LINE);
    			monitorRouteAreaBO.setRouteAreaType(RouteAreaType.LINE.getText());
    		}
		}
		
		monitorRouteAreaBO.setRouteAreaStatus(routeAreaStatus);
		monitorRouteAreaBO.setRouteAreaBuffer(routeAreaBuffer);
		monitorRouteAreaBO.setRouteCost(routeCost);
		logger.info(String.format("update routeArea，区域线路编号：%s", monitorRouteAreaBO.getRouteAreaId()));
		try {
			monitorRouteAreaService.updateaddMonitorRouteArea(monitorRouteAreaBO);
			System.out.println(routeAreaPtCol);
			if(NuctechUtil.isNotNull(routeAreaPtCol)){
				JSONArray json = JSONArray.fromObject(routeAreaPtCol); // 首先把字符串转成 JSONArray  对象
				if(json.size()>0){
					//删除原有的Point数据
					monitorRaPointService.deleteMonitorRaPoint(routeAreaId);
					
					for(int i=0;i<json.size();i++){
					    JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
					    LsMonitorRaPointBO bo = new LsMonitorRaPointBO();
					    bo.setPointId(generatePrimaryKey());
					    bo.setRouteAreaId(monitorRouteAreaBO.getRouteAreaId());
					    bo.setGpsSeq(i);
					    bo.setLatitude(job.get("lat").toString());
					    bo.setLongitude(job.getString("lng"));
						monitorRaPointService.addMonitorRaPoint(bo);
					  }
						
					  logger.info(String.format("添加规划路线或区域"));
					  this.response.getWriter().println(SUCCESS);
					  //return SUCCESS;
				}
			}
		} catch (Exception e) {
			message = e.getMessage();
			logger.error(message);
			this.success = false;
		}
	}

	/**
     * 删除规划路线或区域
     * @return
     */
    @Action(value = "delRouteArea", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") })
    public String delRouteArea() throws Exception {
    	logger.info("删除路线获取到的routeAreaIds是："+ids);
    	
    	int count = monitorRouteAreaService.delRouteAreaByRAIds(getStrResult(ids));
    	if(count>0){
    		monitorRaPointService.deleteMonitorRaPointByRAIds(getStrResult(ids));
    		this.success = true;
    	}else{
    		this.success = false;
    	}

        return SUCCESS;
    }
	
    /**
     * 对接收到的字符串进行处理
     * @return
     */
    public String getStrResult(String str){
    	StringBuffer sb = new StringBuffer();
    	if(NuctechUtil.isNotNull(str)){
        	if(str.indexOf(",")>0){
        		String[] strs = str.split(",");
        		for (int i = 0; i < strs.length-1; i++) {
        			sb.append("'"+strs[i]+"',");
    			}
        		sb.append("'"+strs[strs.length-1]+"'");
        	}else{
        		sb.append("'"+str+"'");
        	}
    	}
    	return sb.toString();
    }
    
    /**
	 * 传递点json值
	 */
	private String pointJson;

	public String getPointJson() {
		return pointJson;
	}

	public void setPointJson(String pointJson) {
		this.pointJson = pointJson;
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

	public String getRouteAreaId() {
		return routeAreaId;
	}

	public void setRouteAreaId(String routeAreaId) {
		this.routeAreaId = routeAreaId;
	}

	private String routeAreaPtCol;

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

	public String getRouteAreaPtCol() {
		return routeAreaPtCol;
	}

	public void setRouteAreaPtCol(String routeAreaPtCol) {
		this.routeAreaPtCol = routeAreaPtCol;
	}

	public String getMessage() {
		return message;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
	public boolean getSuccess() {
        return this.success;
    }

	public LsMonitorRouteAreaBO getLsMonitorRouteAreaBO() {
		return lsMonitorRouteAreaBO;
	}

	public void setLsMonitorRouteAreaBO(LsMonitorRouteAreaBO lsMonitorRouteAreaBO) {
		this.lsMonitorRouteAreaBO = lsMonitorRouteAreaBO;
	}

	public List<LsMonitorRaPointBO> getLsMonitorRaPointBOs() {
		return lsMonitorRaPointBOs;
	}

	public void setLsMonitorRaPointBOs(List<LsMonitorRaPointBO> lsMonitorRaPointBOs) {
		this.lsMonitorRaPointBOs = lsMonitorRaPointBOs;
	}

	public String getRouteAreaBuffer() {
		return routeAreaBuffer;
	}

	public void setRouteAreaBuffer(String routeAreaBuffer) {
		this.routeAreaBuffer = routeAreaBuffer;
	}

	public String getRouteCost() {
		return routeCost;
	}

	public void setRouteCost(String routeCost) {
		this.routeCost = routeCost;
	}
	
}
