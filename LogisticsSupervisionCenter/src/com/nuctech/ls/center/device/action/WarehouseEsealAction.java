package com.nuctech.ls.center.device.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.ws.Response;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseEsealBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.WarehouseEsealService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.ElockDepartmentVO;
import com.nuctech.ls.model.vo.warehouse.EsealDepartementVO;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 作者：赵苏阳 描述：
 * <p>
 * 子锁管理 增删改查
 * <p>
 * 创建时间2016年5月27
 *
 */
@Namespace("/esealMgmt")
public class WarehouseEsealAction extends LSBaseAction{
	
	private static final long serialVersionUID = 5281350780559863860L;
	protected static final String DEFAULT_SORT_COLUMNS = "e.esealId ASC";
	/*
	 * 子锁对象
	 */
	private LsWarehouseEsealBO warehouseEsealBO;
	private String[] esealIds; // 删除的时候获取table行的Id
	private String numbers;
	List<LsWarehouseEsealBO> esealList = new ArrayList<LsWarehouseEsealBO>();
	private List<LsSystemDepartmentBO> esealBelongtoList = new ArrayList<LsSystemDepartmentBO>();
	private List<LsSystemDepartmentBO> esealEditList = new ArrayList<LsSystemDepartmentBO>();
	

	

	@Resource
	private WarehouseEsealService warehouseEsealService;
	@Resource
	private SystemDepartmentService systemDepartmentService;
	
	/**
	 * 子锁添加
	 * @return
	 * @throws Exception
	 */
	@Action(value = "addEseal", results = {
			@Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "error" }) })
		public String addElock() throws Exception {
		try {
			if(warehouseEsealBO != null) {
				SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
				warehouseEsealBO.setEsealId(generatePrimaryKey());// 设置子锁的主键，自动存储到数据库中；
				warehouseEsealBO.setCreateTime(new Date()); // 设置子锁的创建时间。
				warehouseEsealBO.setCreateUser(sessionUser.getUserName());// 获取子锁创建人即登陆人用户名
				
				warehouseEsealBO.setBelongTo(request.getParameter("s_belongTo"));//获取前台下拉列表框中的内容(所属节点)
				warehouseEsealBO.setEsealStatus(request.getParameter("s_esealStatus"));//获取前台下拉列表框中的内容(状态)	
				warehouseEsealService.add(warehouseEsealBO);
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
				@Result(name = "success", location = "/device/esealMgmt/esealAdd.jsp"),
				@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
		public String addModal() {
			
			String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
			esealBelongtoList = systemDepartmentService.findAllPortByUserId(userId);
			warehouseEsealBO = new LsWarehouseEsealBO();
			return SUCCESS;
		}
		
		/**
		 * 编辑modal
		 * @return
		 */
		@Action(value = "editModal", results = {
				@Result(name = "success", location = "/device/esealMgmt/esealEdit.jsp"),
				@Result(name = "error", type = "json", params = { "root", "error" }) })
		public String editModal() {
			if(warehouseEsealBO != null) {
				String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
				esealEditList = systemDepartmentService.findAllPortByUserId(userId);
				
				
				String esealId= warehouseEsealBO.getEsealId();
				if(!NuctechUtil.isNull(esealId)) {
					warehouseEsealBO = this.warehouseEsealService.findById(esealId);
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
		 * 子锁修改
		 * @return
		 * @throws Exception
		 */
		@Action(value = "editEseal", results = {
				@Result(name = "success", type = "json", params = { "root", "true" }),
				@Result(name = "error", type = "json", params = { "root", "error" }) })
		public String editEseal() throws Exception {
			if(warehouseEsealBO != null) {
				this.warehouseEsealService.modify(warehouseEsealBO);
	            return SUCCESS;
			} else {
	            return ERROR;
			}
		}
		
		
		/**
		 * 子锁删除
		 * 
		 * @param warehouseElockBO
		 * @return
		 * 
		 * 
		 * 
		 */
		@Action(value = "delEsealById", results = {
				@Result(name = "success", location = "/device/esealMgmt/eseaList.jsp")})
		public String delEsealById() {
			if (esealIds!= null) {
				String s[] = esealIds[0].split(",");
				for (int i=0;i<s.length;i++) {
					
					this.warehouseEsealService.deleteById(s[i]);
				}
			}
			
			return SUCCESS;
		}
		
		/**
		 * 子锁表与机构表的关联查询
		 * 
		 * @throws IOException
		 */
		@Action(value = "list")
		public void list() throws IOException {
			pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
			JSONObject retJson = warehouseEsealService.fromObjectList(pageQuery, null, false);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.print(retJson.toString());
			out.flush();
			out.close();
		}
		
		/**
		 * 前台 页面 分页 (手动选择)
		 * @return
		 */
		@Action(value = "dlist")
		public void dlist() throws IOException {
			/*List<LsWarehouseEsealBO> esealList = warehouseEsealService.findAllEseal();单表查询*/
			
			SessionUser sessionUser = (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
			String organizationId = sessionUser.getOrganizationId();
			
			List<EsealDepartementVO> esealList = warehouseEsealService.getEsealDepartmentlist(organizationId);//两张表联合查询
			
			JSONArray jsonArray = JSONArray.fromObject(esealList);
			PrintWriter out = response.getWriter();
			out.print(jsonArray.toString());
			out.flush();
			out.close();
		}
		
		
		
		@Action(value = "toList", results = { @Result(name = "success", location = "/device/esealMgmt/esealList.jsp") })
		public String toList() {
			String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
			esealBelongtoList = systemDepartmentService.findAllPortByUserId(userId);
			return SUCCESS;
		}
		
		
	/*	//调度Modal调用方法
		@Action(value = "addDispatchModal", results = {
				@Result(name = "success", location = "/device/esealMgmt/esealDispatch.jsp"),
				@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
		public String addDispatchModal() {
			request.setAttribute("arrayList", numbers);
			return SUCCESS;
		}*/
				
		
		//子锁调度
		@Action(value = "esealDispatch", results = {
				@Result(name = "success", location = "/device/warehouseElock/DeviceDispatch.jsp"),
				@Result(name = "error", type = "json", params = { "root", "error" }) })
		public String elockDispatch() {
			if(esealIds !=null){
				String s[] = esealIds[0].split(",");
					for (int i=0;i<s.length;i++) {
						warehouseEsealBO = warehouseEsealService.findById(s[i]);
						esealList.add(warehouseEsealBO);
						
					}
				}
					return  SUCCESS;
			}
		
		/**
		 * 根据子锁号查询记录
		 * 
		 * @throws IOException
		 */
		@Action(value = "findByEsealNumber")
		public void findByEsealNumber() throws IOException {
			String esealNumber = request.getParameter("esealNumber");
			LsWarehouseEsealBO warehouseEsealBO = warehouseEsealService.findByEsealNumber(esealNumber);
			
			JSONObject json = new JSONObject();
			json.put("warehouseEsealBO", warehouseEsealBO);
			json.put("organizationId", ((SessionUser) session.get(Constant.SESSION_USER)).getOrganizationId());
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(json.toString());
			out.flush();
			out.close();
		}
		
		public LsWarehouseEsealBO getWarehouseEsealBO() {
			return warehouseEsealBO;
		}
		public void setWarehouseEsealBO(LsWarehouseEsealBO warehouseEsealBO) {
			this.warehouseEsealBO = warehouseEsealBO;
		}

		public WarehouseEsealService getWarehouseEsealService() {
			return warehouseEsealService;
		}
		public void setWarehouseEsealService(WarehouseEsealService warehouseEsealService) {
			this.warehouseEsealService = warehouseEsealService;
		}
		
		public String[] getEsealIds() {
			return esealIds;
		}
		public void setEsealIds(String[] esealIds) {
			this.esealIds = esealIds;
		}
		
		
		public String getNumbers() {
			return numbers;
		}

		public void setNumbers(String numbers) {
			this.numbers = numbers;
		}
		public List<LsWarehouseEsealBO> getEsealList() {
			return esealList;
		}

		public void setEsealList(List<LsWarehouseEsealBO> esealList) {
			this.esealList = esealList;
		}
		public List<LsSystemDepartmentBO> getEsealBelongtoList() {
			return esealBelongtoList;
		}

		public void setEsealBelongtoList(List<LsSystemDepartmentBO> esealBelongtoList) {
			this.esealBelongtoList = esealBelongtoList;
		}
		
		public List<LsSystemDepartmentBO> getEsealEditList() {
			return esealEditList;
		}

		public void setEsealEditList(List<LsSystemDepartmentBO> esealEditList) {
			this.esealEditList = esealEditList;
		}
		
}
