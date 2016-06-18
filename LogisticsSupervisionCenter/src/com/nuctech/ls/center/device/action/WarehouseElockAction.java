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
import org.hibernate.SessionFactory;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.WarehouseElockService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.ElockDepartmentVO;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 作者：赵苏阳 描述：
 * <p>
 * 关锁管理 增删改查
 * <p>
 * 创建时间2016年5月19
 *
 */
@Namespace("/warehouseElock")
public class WarehouseElockAction extends LSBaseAction {

	private static final long serialVersionUID = 1l;
	protected static final String DEFAULT_SORT_COLUMNS = "e.elockId ASC";
	private LsWarehouseElockBO warehouseElockBO;
	private LsSystemDepartmentBO systemDepartmentBO;


	private String[] elockIds; // 删除的时候获取table行的Id
	private String numbers;
	private List<String> arrayList = new ArrayList<String>();
	private List<LsSystemDepartmentBO> deptList = new ArrayList<LsSystemDepartmentBO>();
	private List<LsSystemDepartmentBO> deptEditList = new ArrayList<LsSystemDepartmentBO>();
	
	


	private List randomList = new ArrayList();

	@Resource
	private WarehouseElockService warehouseElockService;
	@Resource
	private SystemDepartmentService systemDepartmentService;
	@Resource
	protected SessionFactory sessionFactory;
	
	List<LsWarehouseElockBO> list = new ArrayList<LsWarehouseElockBO>();
	private JSONArray retJson;
	

	/**
	 * 关锁添加
	 * 
	 * @param warehouseElockBO
	 * @return
	 */
	@Action(value = "addElock", results = {
			@Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "error" }) })
	public String addElock() throws Exception {
		try {
			if(warehouseElockBO != null) {
				SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
				warehouseElockBO.setElockId(generatePrimaryKey());// 设置关锁的主键，自动存储到数据库中；
				warehouseElockBO.setCreateTime(new Date()); // 设置关锁创建的时间。
				warehouseElockBO.setCreateUser(sessionUser.getUserName());// 设置创建人
				
				warehouseElockBO.setBelongTo(request.getParameter("s_belongTo"));//获取前台下拉列表框中的内容
				warehouseElockService.add(warehouseElockBO);
				return SUCCESS;
			} else {
			    return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
				return ERROR;
		}
	}
	
	//添加Modal调用方法
	@Action(value = "addModal", results = {
			@Result(name = "success", location = "/device/warehouseElock/elockAdd.jsp"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String addModal() {
		//所属节点，获取所有口岸，前台循环显示
		String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
		deptList = systemDepartmentService.findAllPortByUserId(userId);
		
		//关锁状态
		
		/*warehouseElockBO = new LsWarehouseElockBO();*/
		return SUCCESS;
	}
	
	//编辑modal
	@Action(value = "editModal", results = {
			@Result(name = "success", location = "/device/warehouseElock/elockEdit.jsp"),
			@Result(name = "error", type = "json", params = { "root", "error" }) })
	public String editModal() {
		if(warehouseElockBO != null) {
			
			
			String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
			deptEditList = systemDepartmentService.findAllPortByUserId(userId);
			String elockId= warehouseElockBO.getElockId();
			if(!NuctechUtil.isNull(elockId)) {
				warehouseElockBO = this.warehouseElockService.findById(elockId);
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
	 * 关锁修改
	 * 
	 * @param warehouseElockBO
	 * @return
	 */
	@Action(value = "editElock", results = {
			@Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "error" }) })
	public String editElock() throws Exception {
		if(warehouseElockBO != null) {
			this.warehouseElockService.modify(warehouseElockBO);
            return SUCCESS;
		} else {
            return ERROR;
		}
	}
	

	/**
	 * 关锁删除
	 * 
	 * @param warehouseElockBO
	 * @return
	 */
	@Action(value = "delwarehouseById", results = {
			@Result(name = "success", params = { "root", "true" },location = "/device/warehouseElock/elockList.jsp") })
	public String delwarehouseById() {
		if (elockIds != null) {
			String s[] = elockIds[0].split(",");
			for (int i=0;i<s.length;i++) {
				
				warehouseElockService.deleteById(s[i]);
			}
		}
		return SUCCESS;
	}




	
	/**
	 * 调度 页面前台分页 (手动选择)
	 * @throws IOException
	 */
	@Action(value = "dlist", results = { @Result(name = "success", type = "json")})
	public void dlist() throws IOException {
		//过滤：只选出登录用户所在的口岸的关锁 ；(登录用户的机构代码与关锁的所属机构是一样的)
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
		String organizationId = sessionUser.getOrganizationId();
		List<ElockDepartmentVO> elockList = warehouseElockService.getElockDapatmentlist(organizationId);
		
		/*List<ElockDepartmentVO> elockList = warehouseElockService.getElockDapatmentlist();*/
		
		JSONArray retJson = JSONArray.fromObject(elockList);
		PrintWriter out = response.getWriter();
		out.print(retJson.toString());
		out.flush();
		out.close();
	}
	
	/**
	 * 关锁表与机构组织表的关联查询
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "list")
	public void list() throws IOException {
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
		JSONObject retJson = warehouseElockService.fromObjectList(pageQuery, null, false);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(retJson.toString());
		out.flush();
		out.close();
	}
	
	//给下拉列表中动态赋值 
		@Action(value = "toList", results = { @Result(name = "success", location = "/device/warehouseElock/elockList.jsp") })
		public String toList() {
			String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
			deptList = systemDepartmentService.findAllPortByUserId(userId);
			 
			return SUCCESS;
		}
		
		
	/**
	 * 根据关锁号查询记录
	 * @throws IOException
	 */
	@Action(value = "findByElockNumber")
	public void findByElockNumber() throws IOException {
		String elockNumber = request.getParameter("elockNumber");
		LsWarehouseElockBO warehouseElockBO = warehouseElockService.findByElockNumber(elockNumber);
		
		JSONObject json = new JSONObject();
		json.put("warehouseElockBO", warehouseElockBO);
		json.put("organizationId", ((SessionUser) session.get(Constant.SESSION_USER)).getOrganizationId());
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(json.toString());
		out.flush();
		out.close();
	}
	
	/*//随机查询 
	@Action(value = "RandomChoose") 
	public void RandomChoose() throws IOException {
		String n = request.getParameter("enumber");//取到前台文本框中输入 的数量；
		int m = Integer.parseInt(n);
		randomList = warehouseElockService.getELock(m);
		JSONArray jsonArray = JSONArray.fromObject(randomList);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(jsonArray.toString());
		out.flush();
		out.close();
	}*/
	
	/*//随机查询(跳转location)
	@Action(value = "RandomChoose", results = { @Result(name = "success", type = "json")})
	public 	String RandomChoose() {
		String n = request.getParameter("enumber");//取到前台文本框中输入 的数量；
		int m = Integer.parseInt(n);
		randomList = warehouseElockService.getELock(m);
		return SUCCESS;
	}*/
		
	public LsWarehouseElockBO getWarehouseElockBO() {
		return warehouseElockBO;
	}

	public void setWarehouseElockBO(LsWarehouseElockBO warehouseElockBO) {
		this.warehouseElockBO = warehouseElockBO;
	}

	public String[] getElockIds() {
		return elockIds;
	}

	public void setElockIds(String[] elockIds) {
		this.elockIds = elockIds;
	}

	public WarehouseElockService getWarehouseElockService() {
		return warehouseElockService;
	}

	public void setWarehouseElockService(WarehouseElockService warehouseElockService) {
		this.warehouseElockService = warehouseElockService;
	}
	

	public List<LsWarehouseElockBO> getList() {
		return list;
		}

	public void setList(List<LsWarehouseElockBO> list) {
		this.list = list;
		}

	public List<String> getArrayList() {
			return arrayList;
		}
		
	public void setArrayList(List<String> arrayList) {
			this.arrayList = arrayList;
		}
		
	public String getNumbers() {
			return numbers;
		}
		
	public void setNumbers(String numbers) {
			this.numbers = numbers;
		}
	
	
	public List<LsSystemDepartmentBO> getDeptList() {
		return deptList;
	}


	public void setDeptList(List<LsSystemDepartmentBO> deptList) {
		this.deptList = deptList;
	}
	
	public LsSystemDepartmentBO getSystemDepartmentBO() {
		return systemDepartmentBO;
	}


	public void setSystemDepartmentBO(LsSystemDepartmentBO systemDepartmentBO) {
		this.systemDepartmentBO = systemDepartmentBO;
	}
	
	
	public List getRandomList() {
		return randomList;
	}


	public void setRandomList(List randomList) {
		this.randomList = randomList;
	}
	
	public List<LsSystemDepartmentBO> getDeptEditList() {
		return deptEditList;
	}

	public void setDeptEditList(List<LsSystemDepartmentBO> deptEditList) {
		this.deptEditList = deptEditList;
	}
}

