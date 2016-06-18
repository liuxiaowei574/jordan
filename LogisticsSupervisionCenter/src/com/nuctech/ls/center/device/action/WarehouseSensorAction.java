package com.nuctech.ls.center.device.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseSensorBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.WarehouseSensorService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.ElockDepartmentVO;
import com.nuctech.ls.model.vo.warehouse.SensorDepartmentVO;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 作者：赵苏阳 描述：
 * <p>
 * 传感器管理 增删改查
 * <p>
 * 创建时间2016年5月28
 *
 */
@Namespace("/sensorMgmt")
public class WarehouseSensorAction extends LSBaseAction{
	private static final long serialVersionUID = -3413844565457684391L;
	protected static final String DEFAULT_SORT_COLUMNS = "e.sensorId ASC";
	private List<LsSystemDepartmentBO> sensorMgmtList = new ArrayList<LsSystemDepartmentBO>();
	private List<LsSystemDepartmentBO> sensorEditList = new ArrayList<LsSystemDepartmentBO>();
	
	

	private List randomSensorList = new ArrayList();

	/**
	 * 传感器对象
	 */
	private LsWarehouseSensorBO warehouseSensorBO;
	private String[] sensorIds; // 删除的时候获取前台表格传到后台的Id，
	//set方式注入
	@Resource
	private WarehouseSensorService warehouseSensorService;
	@Resource
	private SystemDepartmentService systemDepartmentService;
	
	/**
	 * 传感器添加
	 * @return
	 * @throws Exception
	 */
	@Action(value = "addSensor", results = {
			@Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "error" }) })
		public String addSensor() throws Exception {
		try {
			if(warehouseSensorBO != null) {
				SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
				warehouseSensorBO.setSensorId(generatePrimaryKey());// 设置传感器的主键，自动存储到数据库中；
				warehouseSensorBO.setCreateTime(new Date());// 设置传感器的创建时间。
				warehouseSensorBO.setCreateUser(sessionUser.getUserName());// 获取子锁创建人即登陆人用户名
				warehouseSensorBO.setSensorStatus(request.getParameter("s_sensorStatus"));
				warehouseSensorBO.setBelongTo(request.getParameter("s_belongTo"));//获取前台下拉列表框中的内容
				warehouseSensorService.add(warehouseSensorBO);
				return SUCCESS;
			} else {
			    return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
				return ERROR;
		}
	}
	
	/**
	 * 添加modal的方法
	 * @return
	 */
		@Action(value = "addModal", results = {
				@Result(name = "success", location = "/device/sensorMgmt/sensorAdd.jsp"),
				@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
		public String addModal() {
			String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
			sensorMgmtList = systemDepartmentService.findAllPortByUserId(userId);
			warehouseSensorBO = new LsWarehouseSensorBO();
			return SUCCESS;
		}
		
		/**
		 * 编辑modal
		 * @return
		 */
		@Action(value = "editModal", results = {
				@Result(name = "success", location = "/device/sensorMgmt/sensorEdit.jsp"),
				@Result(name = "error", type = "json", params = { "root", "error" }) })
		public String editModal() {
			if(warehouseSensorBO != null) {
				String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
				sensorEditList = systemDepartmentService.findAllPortByUserId(userId);
				
				String sensorId= warehouseSensorBO.getSensorId();
				if(!NuctechUtil.isNull(sensorId)) {
					warehouseSensorBO = this.warehouseSensorService.findById(sensorId);
					return SUCCESS;
				} else {
					message = "Find Object Mis!";
					return ERROR;
				}
			} else {
				return ERROR;
			}
		}
		
		
		/**
		 * 传感器修改
		 * @return
		 * @throws Exception
		 */
		@Action(value = "editSensor", results = {
				@Result(name = "success", type = "json", params = { "root", "true" }),
				@Result(name = "error", type = "json", params = { "root", "error" }) })
		public String editSensor() throws Exception {
			if(warehouseSensorBO != null) {
				this.warehouseSensorService.modify(warehouseSensorBO);
	            return SUCCESS;
			} else {
	            return ERROR;
			}
		}
		
		/**
		 * 传感器删除
		 * 
		 * @param warehouseElockBO
		 * @return
		 * 
		 */
		@Action(value = "delSensorById", results = {
				@Result(name = "success", location = "/sensorMgmt/sensorList.jsp"), })
		public String delSensorById() {
			if (sensorIds!= null) {
				String s[] = sensorIds[0].split(",");
				for (int i=0;i<s.length;i++) {
					this.warehouseSensorService.deleteById(s[i]);
				}
			}
			return SUCCESS;
		}
		
		/**
		 * 传感器查询
		 * 
		 * @throws IOException
		 */
		@Action(value = "list")
		public void list() throws IOException {
			pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
			JSONObject retJson = warehouseSensorService.fromObjectList(pageQuery, null, false);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.print(retJson.toString());
			out.flush();
			out.close();
		}
		
		
		@Action(value = "dlist")
		public void dlist() throws IOException {
			/*List<LsWarehouseSensorBO> sensorBOList = warehouseSensorService.findAllelock();*///单表查询
			SessionUser sessionUser = (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
			String organizationId = sessionUser.getOrganizationId();
			
			List<SensorDepartmentVO> sensorBOList = warehouseSensorService.getSensorDepartmentlist(organizationId);
			JSONArray retJson = JSONArray.fromObject(sensorBOList);
			PrintWriter out = response.getWriter();
			out.print(retJson.toString());
			out.flush();
			out.close();
		}
		
		@Action(value = "toList", results = { @Result(name = "success", location = "/device/sensorMgmt/sensorList.jsp") })
		public String toList() {
			String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
			sensorMgmtList = systemDepartmentService.findAllPortByUserId(userId);
			return SUCCESS;
		}	
	
		/**
		 * 根据传感器号查询记录
		 * @throws IOException
		 */
		@Action(value = "findBySensorNumber")
		public void findBySensorNumber() throws IOException {
			String sensorNumber = request.getParameter("sensorNumber");
			LsWarehouseSensorBO warehouseSensorBO = warehouseSensorService.findBySensorNumber(sensorNumber);
			
			JSONObject json = new JSONObject();
			json.put("warehouseSensorBO", warehouseSensorBO);
			json.put("organizationId", ((SessionUser) session.get(Constant.SESSION_USER)).getOrganizationId());
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(json.toString());
			out.flush();
			out.close();
		}
		
		
		
		
		
	public LsWarehouseSensorBO getWarehouseSensorBO() {
		return warehouseSensorBO;
	}
	public void setWarehouseSensorBO(LsWarehouseSensorBO warehouseSensorBO) {
		this.warehouseSensorBO = warehouseSensorBO;
	}
	public String[] getSensorIds() {
		return sensorIds;
	}
	public void setSensorIds(String[] sensorIds) {
		this.sensorIds = sensorIds;
	}
	public WarehouseSensorService getWarehouseSensorService() {
		return warehouseSensorService;
	}
	public void setWarehouseSensorService(WarehouseSensorService warehouseSensorService) {
		this.warehouseSensorService = warehouseSensorService;
	}
	public List<LsSystemDepartmentBO> getSensorMgmtList() {
		return sensorMgmtList;
	}

	public void setSensorMgmtList(List<LsSystemDepartmentBO> sensorMgmtList) {
		this.sensorMgmtList = sensorMgmtList;
	}
	
	public List<LsSystemDepartmentBO> getSensorEditList() {
		return sensorEditList;
	}

	public void setSensorEditList(List<LsSystemDepartmentBO> sensorEditList) {
		this.sensorEditList = sensorEditList;
	}
	
}