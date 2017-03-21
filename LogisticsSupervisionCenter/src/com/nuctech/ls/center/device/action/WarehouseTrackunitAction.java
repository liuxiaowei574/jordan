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
import com.nuctech.ls.model.bo.warehouse.LsWarehouseTrackUnitBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.service.WarehouseTrackUnitService;
import com.nuctech.ls.model.util.OperateContentType;
import com.nuctech.ls.model.util.OperateEntityType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;

/**
 * 作者：赵苏阳 描述：
 * <p>
 * 车载台 增删改查
 * <p>
 * 创建时间2016年6月22
 *
 */
@Namespace("/trackMgmt")
public class WarehouseTrackunitAction extends LSBaseAction {
	private static final long serialVersionUID = 1L;
	protected static final String DEFAULT_SORT_COLUMNS = "e.trackUnitId ASC";
	@Resource
	private SystemDepartmentService systemDepartmentService;
	private List<LsSystemDepartmentBO> deptList = new ArrayList<LsSystemDepartmentBO>();
	private List<LsSystemDepartmentBO> deptEditList = new ArrayList<LsSystemDepartmentBO>();
	@Resource
	private WarehouseTrackUnitService trackService;
	private LsWarehouseTrackUnitBO lsWarehouseTrackUnitBO;
	private LsSystemDepartmentBO systemDepartmentBO;
	@Resource
	private SystemOperateLogService logService;
	private String[] trackUnitIds;

	/**
	 * 进入初始页面
	 */
	@Action(value = "toList", results = { @Result(name = "success", location = "/device/trackUnit/trackList.jsp") })
	public String toList() {
		String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
		deptList = systemDepartmentService.findAllPortByUserId(userId);

		return SUCCESS;
	}

	/**
	 * 车载台与机构组织表的关联查询
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "list")
	public void list() throws IOException {
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
		JSONObject retJson = trackService.fromObjectList(pageQuery, null, false);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(retJson.toString());
		out.flush();
		out.close();
	}

	/**
	 * 添加模态框
	 * 
	 * @return
	 */
	@Action(value = "addModal", results = { @Result(name = "success", location = "/device/trackUnit/trackAdd.jsp"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String addModal() {
		// 所属节点，获取所有口岸，前台循环显示
		String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
		deptList = systemDepartmentService.findAllPortByUserId(userId);
		return SUCCESS;
	}

	/**
	 * 车载台添加
	 * 
	 * @param warehouseElockBO
	 * @return
	 */
	@Action(value = "addTrack", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "error" }) })
	public String addTrack() throws Exception {
		// lsWarehouseTrackUnitBO = new LsWarehouseTrackUnitBO();
		try {
			if (lsWarehouseTrackUnitBO != null) {
				// 判断车载台号是否已经存在，不得重复添加
				String trackUnitNumber = lsWarehouseTrackUnitBO.getTrackUnitNumber();
				LsWarehouseTrackUnitBO trackUnitBO = trackService.findBySensorNumber(trackUnitNumber);
				if (NuctechUtil.isNotNull(trackUnitBO)) {
					return ERROR;
				} else {
					SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
					lsWarehouseTrackUnitBO.setTrackUnitId(generatePrimaryKey());// 设置关锁的主键，自动存储到数据库中；
					lsWarehouseTrackUnitBO.setCreateTime(new Date()); // 设置关锁创建的时间。

					lsWarehouseTrackUnitBO.setCreateUser(sessionUser.getUserId());// 设置创建人
					lsWarehouseTrackUnitBO
							.setTrackUnitNumber(request.getParameter("lsWarehouseTrackUnitBO.trackUnitNumber"));
					lsWarehouseTrackUnitBO.setInterval(request.getParameter("lsWarehouseTrackUnitBO.interval"));
					lsWarehouseTrackUnitBO
							.setGatewayAddress(request.getParameter("lsWarehouseTrackUnitBO.gatewayAddress"));
					lsWarehouseTrackUnitBO.setSimCard(request.getParameter("lsWarehouseTrackUnitBO.simCard"));
					lsWarehouseTrackUnitBO.setBelongTo(request.getParameter("lsWarehouseTrackUnitBO.belongTo"));// 获取前台下拉列表框中的内容
					lsWarehouseTrackUnitBO
							.setTrackUnitStatus(request.getParameter("lsWarehouseTrackUnitBO.trackUnitStatus"));

					trackService.add(lsWarehouseTrackUnitBO);

					addLog(OperateContentType.ADD.toString(), OperateEntityType.TRACKUNIT.toString(), lsWarehouseTrackUnitBO.toString());

					return SUCCESS;
				}
			} else {
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	// 编辑modal
	@Action(value = "editModal", results = { @Result(name = "success", location = "/device/trackUnit/trackEdit.jsp"),
			@Result(name = "error", type = "json", params = { "root", "error" }) })
	public String editModal() {

		if (lsWarehouseTrackUnitBO != null) {

			String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
			deptEditList = systemDepartmentService.findAllPortByUserId(userId);
			String trackUnitId = lsWarehouseTrackUnitBO.getTrackUnitId();
			if (!NuctechUtil.isNull(trackUnitId)) {
				lsWarehouseTrackUnitBO = this.trackService.findById(trackUnitId);
				systemDepartmentBO = systemDepartmentService.findById(lsWarehouseTrackUnitBO.getBelongTo());
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
	 * 车载台修改
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "editTrack", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
			@Result(name = "error", type = "json", params = { "root", "error" }) })
	public String editTrack() throws Exception {
		if (lsWarehouseTrackUnitBO != null) {
			SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
			lsWarehouseTrackUnitBO.setUpdateUser(sessionUser.getUserId());
			lsWarehouseTrackUnitBO.setUpdateTime(new Date());
			this.trackService.modify(lsWarehouseTrackUnitBO);

			addLog(OperateContentType.EDIT.toString(), OperateEntityType.TRACKUNIT.toString(), lsWarehouseTrackUnitBO.toString());

			return SUCCESS;
		} else {
			return ERROR;
		}
	}

	@Action(value = "delTrackById", results = {
			@Result(name = "success", params = { "root", "true" }, location = "/device/trackUnit/trackList.jsp") })
	public String delTrackById() {
		if (trackUnitIds != null) {
			String s[] = trackUnitIds[0].split(",");
			for (int i = 0; i < s.length; i++) {

				trackService.deleteById(s[i]);
			}
		}

		addLog(OperateContentType.DELETE.toString(), OperateEntityType.TRACKUNIT.toString(), lsWarehouseTrackUnitBO.toString());
		return SUCCESS;
	}

	/**
	 * 验证车载台号是否重复
	 * 
	 * @throws Exception
	 */
	@Action(value = "repeate")
	public void repeate() throws Exception {
		JSONObject jsonObject = new JSONObject();
		String trackUnitNumber = lsWarehouseTrackUnitBO.getTrackUnitNumber();
		LsWarehouseTrackUnitBO trackUnitBO = trackService.findBySensorNumber(trackUnitNumber);
		if (NuctechUtil.isNull(trackUnitBO)) {
			jsonObject.put("valid", true);
		} else {
			jsonObject.put("valid", false);
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(jsonObject.toString());
		out.flush();
		out.close();
	}

	/**
	 * 日志记录方法
	 * 
	 * @param content
	 * @param params
	 */
	private void addLog(String operate, String entity, String params) {
		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
		logService.addLog(operate, entity, sessionUser.getUserId(), this.getClass().toString(), params);
	}

	public List<LsSystemDepartmentBO> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<LsSystemDepartmentBO> deptList) {
		this.deptList = deptList;
	}

	public LsWarehouseTrackUnitBO getLsWarehouseTrackUnitBO() {
		return lsWarehouseTrackUnitBO;
	}

	public void setLsWarehouseTrackUnitBO(LsWarehouseTrackUnitBO lsWarehouseTrackUnitBO) {
		this.lsWarehouseTrackUnitBO = lsWarehouseTrackUnitBO;
	}

	public List<LsSystemDepartmentBO> getDeptEditList() {
		return deptEditList;
	}

	public void setDeptEditList(List<LsSystemDepartmentBO> deptEditList) {
		this.deptEditList = deptEditList;
	}

	public LsSystemDepartmentBO getSystemDepartmentBO() {
		return systemDepartmentBO;
	}

	public void setSystemDepartmentBO(LsSystemDepartmentBO systemDepartmentBO) {
		this.systemDepartmentBO = systemDepartmentBO;
	}

	public String[] getTrackUnitIds() {
		return trackUnitIds;
	}

	public void setTrackUnitIds(String[] trackUnitIds) {
		this.trackUnitIds = trackUnitIds;
	}
}
