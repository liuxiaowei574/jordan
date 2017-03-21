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

import com.nuctech.ls.center.utils.IpUtil;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceDispatchBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDispatchDetailBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseEsealBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseSensorBO;
import com.nuctech.ls.model.service.DispatchDetailService;
import com.nuctech.ls.model.service.DispatchRecordService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.service.WarehouseElockService;
import com.nuctech.ls.model.service.WarehouseEsealService;
import com.nuctech.ls.model.service.WarehouseSensorService;
import com.nuctech.ls.model.util.DeviceStatus;
import com.nuctech.ls.model.util.DeviceType;
import com.nuctech.ls.model.util.OperateContentType;
import com.nuctech.ls.model.util.OperateEntityType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 作者：赵苏阳 描述：
 * <p>
 * 调度 action
 * <p>
 * 创建时间2016年6月12
 *
 */
@Namespace("/dispatch")
public class WarehouseDispatchAction extends LSBaseAction {
	protected static final String DEFAULT_SORT_COLUMNS = "e.dispatchId ASC";
	private static final long serialVersionUID = 1L;
	private List<LsSystemDepartmentBO> deptList = new ArrayList<LsSystemDepartmentBO>();
	private JSONObject jsonObject;
	private List<String> sensorarrayList = new ArrayList<String>();
	private List randomList = new ArrayList();
	private List randomEsealList = new ArrayList();
	private List randomSensorList = new ArrayList();

	@Resource
	protected SessionFactory sessionFactory;
	@Resource
	private WarehouseElockService warehouseElockService;
	@Resource
	private WarehouseEsealService warehouseEsealService;
	@Resource
	private WarehouseSensorService warehouseSensorService;

	/**
	 * 从DeviceDispatcherIndex页面跳转到DeviceDispatcher页面
	 */
	private String deviceNumber;// 传递需要调度的关锁数量；
	private String esealNumber;// 传递需要调度 的子锁 数量 ；
	private String sensors;// 传递需要调度的传感器数量；
	private String dispatchIds;// 传递调度记录表中的调配主键(dispatchId)

	/**
	 * 调度记录表修改
	 */
	private LsWarehouseDeviceDispatchBO dispatchRecordBo;
	@Resource
	private DispatchRecordService dispatchRecordService;
	@Resource
	private SystemDepartmentService systemDepartmentService;
	// String dispatchStatus = "1";

	/**
	 * 调度明细表
	 */
	private LsWarehouseDispatchDetailBO dispatchDetailBo;
	@Resource
	private DispatchDetailService dispatchDService;
	private String[] elockIds;
	// 申请口岸
	private String toPort;

	// 调配主键
	private String dispacthId;
	// 接收状态
	// String recviceStatus = "0";

	// 关锁号
	private String numbers;
	private String[] numbersList;
	// 关锁Id
	private String elockId;
	private String[] elockList;

	// 子锁编号
	private String esealNum;
	private String[] esealNumList;
	// 子锁Id
	private String esealId;
	private String[] esealIdList;
	// 传感器编号
	private String sensorNum;
	private String[] sensorNumList;
	// 传感器Id
	private String sensorId;
	private String[] sensorIdList;

	/**
	 * 把要调度的设备的状态改为在途
	 */
	private LsWarehouseElockBO lsWarehouseElockBO;
	private LsWarehouseEsealBO lsWarehouseEsealBO;
	private LsWarehouseSensorBO lsWarehouseSensorBO;

	/**
	 * 将已经调度的设备的详细信息显示调度页面
	 * 
	 */
	private String viewDispatchIds;
	private List elockDetailList = new ArrayList();// 根据调度详细表的调度Id，查询关锁的信息(前台用El表达式取出来)
	private List esealDetailList = new ArrayList();// 子锁
	private List sensorDetailList = new ArrayList();// 传感器

	/**
	 * 推送消息
	 */
	private String clientIPAddress;
	@Resource
	private SystemOperateLogService logService;

	/**
	 * 进入调度页面
	 * 
	 * @return
	 */
	@Action(value = "toList", results = {
			@Result(name = "success", location = "/device/dispatchhandle/DeviceDispatchIndex.jsp") })
	public String toList() {
		String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
		deptList = systemDepartmentService.findAllPortByUserId(userId);

		return SUCCESS;
	}

	/**
	 * 初始化调度页面，列表获取登陆用户所在的口岸的记录表的数据
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "list")
	public void list() throws IOException {

		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
		sessionUser.getOrganizationId();
		// 根据登陆用户的id，查询用户角色表获取其roleid
		String roleId = sessionUser.getRoleId();

		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
		JSONObject retJson = dispatchRecordService.fromObjectList(pageQuery, null, false,
				sessionUser.getOrganizationId(), roleId);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(retJson.toString());
		out.flush();
		out.close();
	}

	/**
	 * 将调度之后的设备信息显示到DeviceDispatch.jsp中
	 * 
	 * @return
	 */

	@Action(value = "ViewDispatchDetailIndex", results = {
			@Result(name = "success", location = "/device/dispatchhandle/DeviceDispatch.jsp") })
	public String ViewDispatchDetailIndex() {
		// request.setAttribute("viewDispatchIds",viewDispatchIds );
		/**
		 * 返回关锁信息List
		 */
		elockDetailList = dispatchDService.getElockDetailList(viewDispatchIds);

		/**
		 * 返回子锁信息的List
		 */
		esealDetailList = dispatchDService.getEsealDetaillist(viewDispatchIds);
		/**
		 * 返回传感器
		 */
		sensorDetailList = dispatchDService.getSensorDetaillist(viewDispatchIds);
		/**
		 * 将需求情况显示到出来
		 */
		request.setAttribute("toPort", toPort);
		request.setAttribute("deviceNumber", deviceNumber);
		request.setAttribute("esealNumber", esealNumber);
		request.setAttribute("sensors", sensors);
		return SUCCESS;
	}

	/**
	 * 从DeviceDispatchIndex界面进入DeviceDispatch界面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "deviceDispatchIndex", results = {
			@Result(name = "success", location = "/device/dispatchhandle/DeviceDispatch.jsp") })
	public String deviceDispatchIndex() throws Exception {
		request.setAttribute("deviceNumber", deviceNumber);
		request.setAttribute("esealNumber", esealNumber);
		request.setAttribute("sensors", sensors);
		request.setAttribute("dispatchIds", dispatchIds);
		request.setAttribute("toPort", toPort);
		return SUCCESS;
	}

	/**
	 * 手动选择
	 * 
	 * @return
	 */
	// 调度关锁Modal调用方法
	@Action(value = "addDispatchModal", results = {
			@Result(name = "success", location = "/device/dispatchhandle/elockDispatch.jsp") })
	public String addDispatchModal() {
		request.setAttribute("arrayList", numbers);
		return SUCCESS;
	}

	// 调度子锁Modal调用方法
	@Action(value = "addEsealDispatchModal", results = {
			@Result(name = "success", location = "/device/dispatchhandle/esealDispatch.jsp"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String addEsealDispatchModal() {
		request.setAttribute("esealarrayList", numbers);
		return SUCCESS;
	}

	// 调度传感器Modal调用方法
	@Action(value = "addSensorDispatchModal", results = {
			@Result(name = "success", location = "/device/dispatchhandle/sensorDispatch.jsp"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String addSensorDispatchModal() {
		request.setAttribute("sensorarrayList", numbers);
		return SUCCESS;
	}

	// 巡逻队模态框调用
	@Action(value = "addPatrolModal", results = {
			@Result(name = "success", location = "/device/dispatchhandle/patrol.jsp"),
			@Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
	public String addPatrolModal() {
		// 传一些参数如设备号，数量等消息到巡逻队页面
		clientIPAddress = IpUtil.getIpAddr(request);
		return SUCCESS;
	}

	/**
	 * 随机查询action
	 * 
	 * @throws IOException
	 */
	// 随机查询关锁
	@SuppressWarnings("unused")
	@Action(value = "RandomElockChoose")
	public void RandomElockChoose() throws IOException {

		String n = request.getParameter("a"); // 关锁的数量

		/* String n = request.getParameter("enumber"); */// 取到前台文本框中输入 的数量；
		int m = Integer.parseInt(n);
		// 只能随机选择所属节点为当前口岸的关锁
		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
		String organizationId = sessionUser.getOrganizationId();

		randomList = warehouseElockService.getELock(m, organizationId);
		JSONArray jsonArray = JSONArray.fromObject(randomList);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(jsonArray.toString());
		out.flush();
		out.close();
	}

	// 随机查询 子锁
	@Action(value = "RandomEsealChoose")
	public void RandomEsealChoose() throws IOException {
		String n = request.getParameter("b");// 获取需要调度的子锁数量
		int m = Integer.parseInt(n);

		// 只能随机选择所属节点为当前口岸的子锁
		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
		String organizationId = sessionUser.getOrganizationId();

		randomEsealList = warehouseEsealService.getEseal(m, organizationId);
		JSONArray jsonArray = JSONArray.fromObject(randomEsealList);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(jsonArray.toString());
		out.flush();
		out.close();
	}

	// 随机传感器
	@Action(value = "RandomSensorChoose")
	public void RandomSensorChoose() throws IOException {
		String n = request.getParameter("c");
		int m = Integer.parseInt(n);

		// 只能随机选择所属节点为当前口岸的传感器
		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
		String organizationId = sessionUser.getOrganizationId();

		randomSensorList = warehouseSensorService.getSensor(m, organizationId);
		JSONArray jsonArray = JSONArray.fromObject(randomSensorList);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(jsonArray.toString());
		out.flush();
		out.close();
	}

	/**
	 * 选好设备，确认调度；
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "deviceDispatch", results = { @Result(name = "success", type = "json"),
			@Result(name = "error", type = "json", params = { "root", "error" }) })
	public String deviceDispatch() throws Exception {
		try {
			/**
			 * 修改记录表("调配人"，"调配状态","调配时间 ")先根据Id查询再修改。
			 */
			// 根据调配主键(dispacthId)查找出需要修改的记录
			dispatchRecordBo = dispatchRecordService.findByDispatchID(dispacthId);
			// 修改调度日期
			dispatchRecordBo.setDispatchTime(new Date());
			// 修改调度人(当前用户)
			SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
			dispatchRecordBo.setDispatchUser(sessionUser.getUserName());
			// 修改调度人角色(当前用户的角色)
			dispatchRecordBo.setDispatchRole(sessionUser.getRoleName());
			// 修改调度状态从0改为1；
			dispatchRecordBo.setDispatchStatus(Constant.DEVICE_FINISH_DISPATCH);
			dispatchRecordService.modifyDispatchRecord(dispatchRecordBo);
			addLog(OperateContentType.EXECUTE.toString(), OperateEntityType.DEVICE_DISPATCH.toString(), dispatchRecordBo.toString());

			/**
			 * 设备调度明细表保存
			 */
			dispatchDetailBo = new LsWarehouseDispatchDetailBO();
			lsWarehouseElockBO = new LsWarehouseElockBO();
			lsWarehouseEsealBO = new LsWarehouseEsealBO();
			lsWarehouseSensorBO = new LsWarehouseSensorBO();
			// 保存关锁的详细信息
			if (numbers != null && !"".equals(numbers)) {
				// 获得关锁号字符串数
				numbers = numbers.substring(0, numbers.length() - 1);// 把字符串的最后一个字符删除(逗号)
				numbersList = numbers.split(",");
				// 获得关锁Id字符串数组
				elockId = elockId.substring(0, elockId.length() - 1);
				elockList = elockId.split(",");
				// 记录关锁的详细信息
				for (int i = 0; i < numbersList.length; i++) {
					dispatchDetailBo.setDetailId(generatePrimaryKey());// 调配明细主键
					dispatchDetailBo.setDispatchId(dispacthId);// 调配主键
					dispatchDetailBo.setDeviceId(elockList[i]);// 设备主键(关锁主键)
					dispatchDetailBo.setDeviceNumber(numbersList[i]);// 设备编号(关锁编号
																		// )
					dispatchDetailBo.setRecviceStatus(Constant.unRecviceStatus);// 接收状态(调度时，状态为未接收)

					dispatchDetailBo.setDeviceType(DeviceType.TRACKING_DEVICE.getType());

					// 接收人和接收时间在接收页面进行修改。
					dispatchDService.saveDetail(dispatchDetailBo);
					/**
					 * 将关锁的状态改为“在途”
					 */
					lsWarehouseElockBO = warehouseElockService.findById(elockList[i]);
					lsWarehouseElockBO.setElockStatus(DeviceStatus.Inway.getText());// 枚举类
					lsWarehouseElockBO.setLastUseTime(new Date());
					warehouseElockService.modify(lsWarehouseElockBO);
				}
			}
			// 保存子锁的详细内容
			if (esealNum != null && !"".equals(esealNum)) {
				// 获得子锁号字符串数组

				esealNum = esealNum.substring(0, esealNum.length() - 1);

				esealNumList = esealNum.split(",");
				// 获得子锁Id字符串数组
				esealId = esealId.substring(0, esealId.length() - 1);
				esealIdList = esealId.split(",");
				// 把子锁的信息存到调度详细表中
				for (int i = 0; i < esealNumList.length; i++) {
					dispatchDetailBo.setDetailId(generatePrimaryKey());
					dispatchDetailBo.setDispatchId(dispacthId);
					dispatchDetailBo.setDeviceId(esealIdList[i]);
					dispatchDetailBo.setDeviceNumber(esealNumList[i]);
					dispatchDetailBo.setRecviceStatus(Constant.unRecviceStatus);

					dispatchDetailBo.setDeviceType(DeviceType.ESEAL.getType());

					dispatchDService.saveDetail(dispatchDetailBo);

					/**
					 * 将子锁的状态改为在途
					 */
					lsWarehouseEsealBO = warehouseEsealService.findById(esealIdList[i]);
					lsWarehouseEsealBO.setEsealStatus(DeviceStatus.Inway.getText());
					warehouseEsealService.modify(lsWarehouseEsealBO);
				}
			}

			// 保存传感器的详细信息
			if (sensorNum != null && !"".equals(sensorNum)) {
				// 获得传感器编号字符串数组
				sensorNum = sensorNum.substring(0, sensorNum.length() - 1);
				sensorNumList = sensorNum.split(",");
				// 获得传感器Id字符串数组
				sensorId = sensorId.substring(0, sensorId.length() - 1);
				sensorIdList = sensorId.split(",");
				// 把传感器的信息存到调度详细表中
				for (int i = 0; i < sensorNumList.length; i++) {
					dispatchDetailBo.setDetailId(generatePrimaryKey());
					dispatchDetailBo.setDispatchId(dispacthId);
					dispatchDetailBo.setDeviceId(sensorIdList[i]);
					dispatchDetailBo.setDeviceNumber(sensorNumList[i]);
					dispatchDetailBo.setRecviceStatus(Constant.unRecviceStatus);
					dispatchDetailBo.setDeviceType(DeviceType.SENSOR.getType());
					dispatchDService.saveDetail(dispatchDetailBo);
					/**
					 * 将传感器的状态改为在途
					 */
					lsWarehouseSensorBO = warehouseSensorService.findById(sensorIdList[i]);
					lsWarehouseSensorBO.setSensorStatus(DeviceStatus.Inway.getText());
					warehouseSensorService.modify(lsWarehouseSensorBO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;

	}
	
	/**
	 * 日志记录方法
	 * @param content
	 * @param params 
	 */
	private void addLog(String operate, String entity, String params) {
		SessionUser sessionUser = (SessionUser)request.getSession().getAttribute(Constant.SESSION_USER);
		logService.addLog(operate, entity, sessionUser.getUserId(), this.getClass().toString(), params);
	}

	public LsWarehouseDeviceDispatchBO getDispatchRecordBo() {
		return dispatchRecordBo;
	}

	public void setDispatchRecordBo(LsWarehouseDeviceDispatchBO dispatchRecordBo) {
		this.dispatchRecordBo = dispatchRecordBo;
	}

	public LsWarehouseDispatchDetailBO getDispatchDetailBo() {
		return dispatchDetailBo;
	}

	public void setDispatchDetailBo(LsWarehouseDispatchDetailBO dispatchDetailBo) {
		this.dispatchDetailBo = dispatchDetailBo;
	}

	public String getNumbers() {
		return numbers;
	}

	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}

	public String getElockId() {
		return elockId;
	}

	public void setElockId(String elockId) {
		this.elockId = elockId;
	}

	public String getEsealNum() {
		return esealNum;
	}

	public void setEsealNum(String esealNum) {
		this.esealNum = esealNum;
	}

	public List<LsSystemDepartmentBO> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<LsSystemDepartmentBO> deptList) {
		this.deptList = deptList;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public List<String> getSensorarrayList() {
		return sensorarrayList;
	}

	public void setSensorarrayList(List<String> sensorarrayList) {
		this.sensorarrayList = sensorarrayList;
	}

	public List getRandomList() {
		return randomList;
	}

	public void setRandomList(List randomList) {
		this.randomList = randomList;
	}

	public WarehouseElockService getWarehouseElockService() {
		return warehouseElockService;
	}

	public void setWarehouseElockService(WarehouseElockService warehouseElockService) {
		this.warehouseElockService = warehouseElockService;
	}

	public String getEsealNumber() {
		return esealNumber;
	}

	public void setEsealNumber(String esealNumber) {
		this.esealNumber = esealNumber;
	}

	public WarehouseEsealService getWarehouseEsealService() {
		return warehouseEsealService;
	}

	public void setWarehouseEsealService(WarehouseEsealService warehouseEsealService) {
		this.warehouseEsealService = warehouseEsealService;
	}

	public List getRandomEsealList() {
		return randomEsealList;
	}

	public void setRandomEsealList(List randomEsealList) {
		this.randomEsealList = randomEsealList;
	}

	public List getRandomSensorList() {
		return randomSensorList;
	}

	public void setRandomSensorList(List randomSensorList) {
		this.randomSensorList = randomSensorList;
	}

	public WarehouseSensorService getWarehouseSensorService() {
		return warehouseSensorService;
	}

	public void setWarehouseSensorService(WarehouseSensorService warehouseSensorService) {
		this.warehouseSensorService = warehouseSensorService;
	}

	public String getSensors() {
		return sensors;
	}

	public void setSensors(String sensors) {
		this.sensors = sensors;
	}

	public String getDispatchIds() {
		return dispatchIds;
	}

	public void setDispatchIds(String dispatchIds) {
		this.dispatchIds = dispatchIds;
	}

	public String getDispacthId() {
		return dispacthId;
	}

	public void setDispacthId(String dispacthId) {
		this.dispacthId = dispacthId;
	}

	public String[] getEsealIdList() {
		return esealIdList;
	}

	public void setEsealIdList(String[] esealIdList) {
		this.esealIdList = esealIdList;
	}

	public String getSensorNum() {
		return sensorNum;
	}

	public void setSensorNum(String sensorNum) {
		this.sensorNum = sensorNum;
	}

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public String getEsealId() {
		return esealId;
	}

	public void setEsealId(String esealId) {
		this.esealId = esealId;
	}

	public String[] getSensorNumList() {
		return sensorNumList;
	}

	public void setSensorNumList(String[] sensorNumList) {
		this.sensorNumList = sensorNumList;
	}

	public String[] getSensorIdList() {
		return sensorIdList;
	}

	public void setSensorIdList(String[] sensorIdList) {
		this.sensorIdList = sensorIdList;
	}

	public LsWarehouseElockBO getLsWarehouseElockBO() {
		return lsWarehouseElockBO;
	}

	public void setLsWarehouseElockBO(LsWarehouseElockBO lsWarehouseElockBO) {
		this.lsWarehouseElockBO = lsWarehouseElockBO;
	}

	public LsWarehouseEsealBO getLsWarehouseEsealBO() {
		return lsWarehouseEsealBO;
	}

	public void setLsWarehouseEsealBO(LsWarehouseEsealBO lsWarehouseEsealBO) {
		this.lsWarehouseEsealBO = lsWarehouseEsealBO;
	}

	public LsWarehouseSensorBO getLsWarehouseSensorBO() {
		return lsWarehouseSensorBO;
	}

	public void setLsWarehouseSensorBO(LsWarehouseSensorBO lsWarehouseSensorBO) {
		this.lsWarehouseSensorBO = lsWarehouseSensorBO;
	}

	public String getToPort() {
		return toPort;
	}

	public void setToPort(String toPort) {
		this.toPort = toPort;
	}

	public String getViewDispatchIds() {
		return viewDispatchIds;
	}

	public void setViewDispatchIds(String viewDispatchIds) {
		this.viewDispatchIds = viewDispatchIds;
	}

	public List getElockDetailList() {
		return elockDetailList;
	}

	public void setElockDetailList(List elockDetailList) {
		this.elockDetailList = elockDetailList;
	}

	public List getEsealDetailList() {
		return esealDetailList;
	}

	public void setEsealDetailList(List esealDetailList) {
		this.esealDetailList = esealDetailList;
	}

	public List getSensorDetailList() {
		return sensorDetailList;
	}

	public void setSensorDetailList(List sensorDetailList) {
		this.sensorDetailList = sensorDetailList;
	}

	/*
	 * 关锁调度,elockDispatcher中表格的内容显示到deviceDispatcher中，前台用el表达式取值
	 * 
	 * @Action(value = "elockDispatch", results = {
	 * 
	 * @Result(name = "success", type = "json", params = { "root", "success" }),
	 * 
	 * @Result(name = "error", type = "json", params = { "root", "error" }) })
	 * public String elockDispatch() throws IOException { if (elockIds != null)
	 * { String s[] = elockIds[0].split(","); for (int i = 0; i < s.length; i++)
	 * { warehouseElockBO = warehouseElockService.findById(s[i]);
	 * list.add(warehouseElockBO); } } return SUCCESS; }
	 */
	public String getClientIPAddress() {
		return clientIPAddress;
	}

	public void setClientIPAddress(String clientIPAddress) {
		this.clientIPAddress = clientIPAddress;
	}
}
