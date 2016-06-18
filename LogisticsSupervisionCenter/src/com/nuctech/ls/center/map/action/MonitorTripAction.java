package com.nuctech.ls.center.map.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseEsealBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseSensorBO;
import com.nuctech.ls.model.service.CommonVehicleService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.WarehouseElockService;
import com.nuctech.ls.model.service.WarehouseEsealService;
import com.nuctech.ls.model.service.WarehouseSensorService;
import com.nuctech.ls.model.util.DeviceStatus;
import com.nuctech.ls.model.vo.monitor.MonitorTripVehicleVO;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.DateUtils;
import com.nuctech.util.NuctechUtil;
import com.opensymphony.xwork2.ActionContext;

import jcifs.util.Base64;
import net.sf.json.JSONObject;

/**
 * 车辆行程信息Action
 * 
 * @author liushaowei
 *
 */
@Namespace("/monitortrip")
public class MonitorTripAction extends LSBaseAction {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private static final long serialVersionUID = 1L;
	/**
	 * 列表排序字段
	 */
	@SuppressWarnings("unused")
	private static final String DEFAULT_SORT_COLUMNS = "checkinTime DESC";
	private static final String DEFAULT_SORT_COLUMNS_WITH_ALIAS = "t.checkinTime DESC";

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
	 * 追踪终端号Service
	 */
	@Resource
	private WarehouseElockService warehouseElockService;
	/**
	 * 子锁增删改查 Service
	 */
	@Resource
	private WarehouseEsealService warehouseEsealService;
	/**
	 * 传感器增删改查 Service
	 */
	@Resource
	private WarehouseSensorService warehouseSensorService;
	/**
	 * 行程照片存储路径
	 */
	@Resource
	private String tripPhotoPath;
	/**
	 * 行程照片存储路径
	 */
	@Resource
	private String tripPhotoPathHttp;

	/**
	 * 行程信息及车辆信息
	 */
	private MonitorTripVehicleVO tripVehicleVO;
	
	/**
	 * 本地上传的行程照片列表
	 */
	private File[] tripPhotoLocal;
	
	/**
	 * 上传的行程照片名列表，tripPhotoLocal加"FileName"表示对应的文件名
	 */
	private String[] tripPhotoLocalFileName;
	
	/**
	 * 拍摄的行程照片Base64值列表
	 */
	private String[] tripCameraBase64;
	
	/**
	 * 组织机构 Service
	 */
	@Resource
	private SystemDepartmentService systemDepartmentService;
	
	/**
	 * 跳转到编辑页面
	 * @return
	 */
	@Action(value = "toEdit", results = {
			@Result(name = "success", location = "/trip/edit.jsp")})
	public String toEdit() {
		if (tripVehicleVO != null) {
			String tripId = tripVehicleVO.getTripId();
			if (!NuctechUtil.isNull(tripId)) {
				LsMonitorTripBO lsMonitorTripBO = monitorTripService.findMonitortripById(tripId);
				BeanUtils.copyProperties(lsMonitorTripBO, tripVehicleVO);
				if (lsMonitorTripBO != null) {
					LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService.findById(lsMonitorTripBO.getVehicleId());
					BeanUtils.copyProperties(lsCommonVehicleBO, tripVehicleVO);
				}
				return SUCCESS;
			} else {
				return ERROR;
			}
		} else {
			return ERROR;
		}
	}
	

	/**
	 * 跳转到编辑页面
	 * @return
	 */
	@Action(value = "toDetail", results = {
			@Result(name = "success", location = "/trip/detail.jsp")})
	public String toDetail() {
		String tripId = request.getParameter("tripId");
		tripVehicleVO = new MonitorTripVehicleVO();
		if (!NuctechUtil.isNull(tripId)) {
			LsMonitorTripBO lsMonitorTripBO = monitorTripService.findMonitortripById(tripId);
			if (lsMonitorTripBO != null) {
				LsCommonVehicleBO lsCommonVehicleBO = commonVehicleService.findById(lsMonitorTripBO.getVehicleId());
				BeanUtils.copyProperties(lsMonitorTripBO, tripVehicleVO);
				BeanUtils.copyProperties(lsCommonVehicleBO, tripVehicleVO);
				tripVehicleVO.setCheckinPortName(systemDepartmentService.findById(tripVehicleVO.getCheckinPort()).getOrganizationName());
				tripVehicleVO.setCheckoutPortName(systemDepartmentService.findById(tripVehicleVO.getCheckoutPort()).getOrganizationName());
			}
			return SUCCESS;
		} else {
			return ERROR;
		}
	}
	
	/**
	 * 跳转到列表页面
	 * @return
	 */
	@Action(value="toList", results = {@Result(name = "success", location = "/trip/list.jsp")})
	public String toList() {
		return SUCCESS;
	}
	
	/**
	 * 列表查询
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Action(value="list")
	public String list() throws IOException{
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
		Map filtersMap = pageQuery.getFilters();
		if (filtersMap == null) {
			filtersMap = new HashMap();
		}
		if (filtersMap.get("checkinUser") == null) {
			Map<String, Object> session = ActionContext.getContext().getSession();
			filtersMap.put("checkinUser", ((SessionUser) session.get(Constant.SESSION_USER)).getUserAccount());//存放用户Id，不是登录名
			pageQuery.setFilters(filtersMap);
		}
		JSONObject retJson = monitorTripService.findTripVehicleList(pageQuery);		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");		
		PrintWriter out = response.getWriter(); 
		out.print(retJson.toString());
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * 跳转到行程激活页面
	 * @return
	 */
	@Action(value = "toActivate", results = {@Result(name = "success", location = "/trip/activate.jsp")})
	public String toActivate() {
		return SUCCESS;
	}
	
	/**
	 * 行程激活
	 * @return
	 */
	@Action(value = "activate")
	public String activate() {
		JSONObject json = new JSONObject();
		try {
			//新增行程信息、车辆信息
			LsMonitorTripBO lsMonitorTripBO = addMonitorTripInfo(tripVehicleVO);
			String tripId = lsMonitorTripBO.getTripId();
			
			//保存上传的图片和拍摄的照片
			List<String> filePaths = saveTripPhotoLocal(tripId);
			List<String> filePaths1 = saveTripPhotoCamera(tripId);
			if(filePaths1.size() > 0) {
				filePaths.addAll(filePaths1);
			}
			lsMonitorTripBO.setCheckinPicture(StringUtils.join(filePaths, ","));
			monitorTripService.updateMonitorTrip(lsMonitorTripBO);
			
			//更新追踪终端号状态为在途
			String trackingDeviceNumber = tripVehicleVO.getTrackingDeviceNumber();
			if(NuctechUtil.isNotNull(trackingDeviceNumber)) {
				logger.info(String.format("更新追踪终端号状态为在途! trackingDeviceNumber: %s", trackingDeviceNumber));
				updateTrackingDeviceNumberStatus(trackingDeviceNumber, DeviceStatus.Inway.getText(), null);
			}
			//更新所有子锁号和传感器号状态为在途
			String esealNumber = tripVehicleVO.getEsealNumber();
			if(NuctechUtil.isNotNull(esealNumber)) {
				logger.info(String.format("更新所有子锁号状态为在途! esealNumber：%s esealOrder: %s", esealNumber, tripVehicleVO.getEsealOrder()));
				updateEsealNumberStatus(esealNumber, DeviceStatus.Inway.getText(), null);
			}
			String sensorNumber = tripVehicleVO.getSensorNumber();
			if(NuctechUtil.isNotNull(sensorNumber)) {
				logger.info(String.format("更新所有传感器号状态为在途! sensorNumber: %s sensorOrder: %s", sensorNumber, tripVehicleVO.getSensorOrder()));
				updateSensorNumberStatus(sensorNumber, DeviceStatus.Inway.getText(), null);
			}
			
			json.put("result", true);
			logger.info(String.format("行程激活成功! tripId=%s", tripId));
		} catch (Exception e) {
			json.put("message", e.getMessage());
			logger.error(e);
		}
		
		response.setCharacterEncoding("utf-8");
		// 禁止浏览器开辟缓存
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma", "no-cache");
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}
	
	/**
	 * 更新传感器状态为在途
	 * @param sensorNumber 传感器号，多个用逗号分隔
	 * @param status 状态
	 * @param belongTo 所属口岸
	 */
	private void updateSensorNumberStatus(String sensorNumber, String status, String belongTo) {
		String[] numbers = sensorNumber.split("\\s*,\\s*");
		for (String number : numbers) {
			LsWarehouseSensorBO warehouseSensorBO = warehouseSensorService.findBySensorNumber(number);
			if (warehouseSensorBO != null) {
				warehouseSensorBO.setSensorStatus(status);
				if(NuctechUtil.isNotNull(belongTo)) {
					warehouseSensorBO.setBelongTo(belongTo);
				}
				warehouseSensorService.modify(warehouseSensorBO);
			}else{
				logger.info(String.format("找不到传感器号记录! sensorNumber：%s", number));
			}
		}
	}

	/**
	 * 更新追踪终端号状态
	 * @param trackingDeviceNumber 追踪终端号
	 * @param status 状态
	 * @param belongTo 所属口岸
	 */
	private void updateTrackingDeviceNumberStatus(String trackingDeviceNumber, String status, String belongTo) {
		LsWarehouseElockBO warehouseElockBO = warehouseElockService.findByElockNumber(trackingDeviceNumber);
		if (warehouseElockBO != null) {
			warehouseElockBO.setElockStatus(status);
			if(NuctechUtil.isNotNull(belongTo)) {
				warehouseElockBO.setBelongTo(belongTo);
			}
			warehouseElockService.modify(warehouseElockBO);
		}else{
			logger.info(String.format("找不到追踪终端号记录! trackingDeviceNumber：%s", trackingDeviceNumber));
		}
	}

	/**
	 * 更新子锁状态
	 * @param esealNumber 子锁号，多个用逗号分隔
	 * @param status 状态
	 * @param belongTo 所属口岸
	 */
	private void updateEsealNumberStatus(String esealNumber, String status, String belongTo) {
		String[] numbers = esealNumber.split("\\s*,\\s*");
		for (String number : numbers) {
			LsWarehouseEsealBO warehouseEsealBO = warehouseEsealService.findByEsealNumber(number);
			if (warehouseEsealBO != null) {
				warehouseEsealBO.setEsealStatus(status);
				if(NuctechUtil.isNotNull(belongTo)) {
					warehouseEsealBO.setBelongTo(belongTo);
				}
				warehouseEsealService.modify(warehouseEsealBO);
			}else{
				logger.info(String.format("找不到子锁号记录! esealNumber：%s", number));
			}
		}
	}

	/**
	 * 保存拍照的照片
	 * @param tripId
	 * @return
	 */
	private List<String> saveTripPhotoCamera(String tripId) {
		List<String> filePaths = new ArrayList<String>();
		if(tripCameraBase64 != null) {
			long now = new Date().getTime();
			for(int i = 0; i < tripCameraBase64.length; i++) {
				String filePath = tripPhotoPath + "/" + tripId + "/" + now + "-" + i;
				base64ToFile(tripCameraBase64[i], filePath, ".jpg");
				filePaths.add(filePath.substring(tripPhotoPath.length()) + ".jpg");
			}
		}
		return filePaths;
	}

	/**
	 * 保存上传的本地图片
	 * @param tripId
	 * @return
	 * @throws Exception
	 */
	private List<String> saveTripPhotoLocal(String tripId) throws Exception {
		List<String> filePaths = new ArrayList<String>();
		if (tripPhotoLocal != null) {
			for (int i = 0; i < tripPhotoLocal.length; i++) {
				if(tripPhotoLocal[i] != null) {
					String imageName = tripPhotoLocalFileName[i].trim();
					
					imageName = renameFileIfExists(tripPhotoPath + "/" + tripId, imageName);
					File file = NuctechUtil.uploadFile(tripPhotoPath + "/" + tripId, imageName, tripPhotoLocal[i]);
					logger.info("上传文件：" + file.getAbsolutePath());
					filePaths.add(file.getAbsolutePath().substring(tripPhotoPath.length()).replaceAll("\\\\", "/"));
				}
			}
		}
		return filePaths;
	}
	
	/**
	 * 重命名同名文件：abc.jpg --> abc(1).jpg
	 * @param path
	 * @param name
	 * @return
	 */
	private String renameFileIfExists(String path, String name) {
		File file = new File(path, name);
		if (file != null && file.exists()) {
			name = name.replaceAll("(.*)\\.([^\\.]*)", "$1(1).$2");
		}
		return name;
	}
	
	/**
	 * 保存Base64内容为图片
	 * @param base64
	 * @param filePath 文件路径，不含后缀名
	 * @param format 格式(.jpg/.png/.jpeg)
	 */
	private void base64ToFile(String base64, String filePath, String format) {
        try {
            File file = new File(filePath + format);
            if(!file.getParentFile().exists()) {
            	file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
            	file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            out.write(Base64.decode(base64));
            out.close();
            logger.info("保存Base64到文件：" + file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            logger.error("保存图像出错！", e);
        } catch (IOException e) {
            logger.error("保存图像出错！", e);
        }
    }
	
	/**
	 * 新增行程及车辆信息，返回创建后的行程
	 * @param tripVehicleVO
	 * @return
	 * @throws Exception
	 */
	private LsMonitorTripBO addMonitorTripInfo(MonitorTripVehicleVO tripVehicleVO) throws Exception {
		// 保存车辆信息
		LsCommonVehicleBO lsCommonVehicleBO = new LsCommonVehicleBO();
		BeanUtils.copyProperties(tripVehicleVO, lsCommonVehicleBO);
		lsCommonVehicleBO.setVehicleId(generatePrimaryKey());
		commonVehicleService.addCommonVehicle(lsCommonVehicleBO);

		// 保存行程信息
		LsMonitorTripBO lsMonitorTripBO = new LsMonitorTripBO();
		BeanUtils.copyProperties(tripVehicleVO, lsMonitorTripBO);
		lsMonitorTripBO.setTripId(generatePrimaryKey());
		lsMonitorTripBO.setVehicleId(lsCommonVehicleBO.getVehicleId());
		lsMonitorTripBO.setTripStatus(Constant.TRIP_STATUS_STARTED);
		lsMonitorTripBO.setCheckinTime(new Date());
		lsMonitorTripBO.setCheckinUser(((SessionUser)session.get(Constant.SESSION_USER)).getUserId());
		monitorTripService.addMonitorTrip(lsMonitorTripBO);

		logger.info(String.format("保存车辆及行程信息，veHicleId：%s，tripId：%s", lsCommonVehicleBO.getVehicleId(), lsMonitorTripBO.getTripId()));
		return lsMonitorTripBO;
	}
	

	/**
	 * 更新行程及车辆信息
	 * @param tripVehicleVO
	 * @return
	 * @throws Exception
	 */
	private LsMonitorTripBO updateMonitorTripInfo(MonitorTripVehicleVO tripVehicleVO) throws Exception {
		// 更新行程信息
		LsMonitorTripBO lsMonitorTripBO = monitorTripService.findMonitortripById(tripVehicleVO.getTripId());
		if(lsMonitorTripBO != null) {
			lsMonitorTripBO.setTripStatus(Constant.TRIP_STATUS_FINISHED);
			lsMonitorTripBO.setCheckoutUser(((SessionUser)session.get(Constant.SESSION_USER)).getUserId()); //存放用户Id，不是登录名
			lsMonitorTripBO.setCheckoutTime(new Date());
			//计算行程耗时，单位：秒
//			long timeCost = (lsMonitorTripBO.getCheckoutTime().getTime() - lsMonitorTripBO.getCheckinTime().getTime()) / 1000;
			long timeCost = DateUtils.differenceBetweenDate(lsMonitorTripBO.getCheckoutTime(), lsMonitorTripBO.getCheckinTime()) / 1000;
			lsMonitorTripBO.setTimeCost(String.valueOf(timeCost));
			monitorTripService.updateMonitorTrip(lsMonitorTripBO);
		} else {
			logger.error(String.format("行程记录不存在！tripId: %s", tripVehicleVO.getTripId()));
		}

		logger.info(String.format("更新行程信息，tripId：%s", lsMonitorTripBO.getTripId()));
		return lsMonitorTripBO;
	}
	
	/**
	 * 跳转到行程结束页面
	 * @return
	 */
	@Action(value = "toFinish", results = {@Result(name = "success", location = "/trip/finish.jsp")})
	public String toFinish() {
		return SUCCESS;
	}
	
	/**
	 * 行程结束
	 * 
	 * @return
	 */
	@Action(value = "finish", results = { 
			@Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") 
		})
	public String finish() {
		JSONObject json = new JSONObject();
		try {
			String tripId = tripVehicleVO.getTripId();
			updateMonitorTripInfo(tripVehicleVO);
			
			LsMonitorTripBO lsMonitorTripBO = monitorTripService.findMonitortripById(tripId);
			
			List<String> filePaths = saveTripPhotoLocal(tripId);
			List<String> filePaths1 = saveTripPhotoCamera(tripId);
			if(filePaths1.size() > 0) {
				filePaths.addAll(filePaths1);
			}
			
			lsMonitorTripBO.setCheckoutPicture(StringUtils.join(filePaths, ","));
			monitorTripService.updateMonitorTrip(lsMonitorTripBO);
			
			//更新追踪终端号状态为正常，并修改所属口岸为检出口岸
			String trackingDeviceNumber = tripVehicleVO.getTrackingDeviceNumber();
			if(NuctechUtil.isNotNull(trackingDeviceNumber)) {
				logger.info(String.format("更新追踪终端号状态为正常! trackingDeviceNumber: %s", trackingDeviceNumber));
				updateTrackingDeviceNumberStatus(trackingDeviceNumber, DeviceStatus.Normal.getText(), tripVehicleVO.getCheckoutPort());
			}
			//更新所有子锁号和传感器号状态为正常，并修改所属口岸为检出口岸
			String esealNumber = tripVehicleVO.getEsealNumber();
			if(NuctechUtil.isNotNull(esealNumber)) {
				logger.info(String.format("更新所有子锁号状态为正常! esealNumber：%s", esealNumber));
				updateEsealNumberStatus(esealNumber, DeviceStatus.Normal.getText(), tripVehicleVO.getCheckoutPort());
			}
			String sensorNumber = tripVehicleVO.getSensorNumber();
			if(NuctechUtil.isNotNull(sensorNumber)) {
				logger.info(String.format("更新所有传感器号状态为正常! sensorNumber: %s", sensorNumber));
				updateSensorNumberStatus(sensorNumber, DeviceStatus.Normal.getText(), tripVehicleVO.getCheckoutPort());
			}
			
			json.put("result", true);
			logger.info(String.format("行程结束成功! tripId：%s", tripId));
		} catch (Exception e) {
			json.put("message", e.getMessage());
			logger.error(e);
		}
		
		response.setCharacterEncoding("utf-8");
		// 禁止浏览器开辟缓存
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma", "no-cache");
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}
	
	/**
	 * 查询行程信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "findOneTripVehicle", results = { 
			@Result(name = "success", type = "json"),
			@Result(name = "error", type = "json") 
		})
	public String findOneTripVehicle() {
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_WITH_ALIAS);
		pageQuery.setPageSize(1);
		pageQuery.setPage(1);
		JSONObject retJson = monitorTripService.findOneTripVehicle(pageQuery);
		
		response.setCharacterEncoding("utf-8");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().println(retJson.toString());
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}

	public MonitorTripVehicleVO getTripVehicleVO() {
		return tripVehicleVO;
	}

	public void setTripVehicleVO(MonitorTripVehicleVO tripVehicleVO) {
		this.tripVehicleVO = tripVehicleVO;
	}

	public File[] getTripPhotoLocal() {
		return tripPhotoLocal;
	}

	public void setTripPhotoLocal(File[] tripPhotoLocal) {
		this.tripPhotoLocal = tripPhotoLocal;
	}

	public String[] getTripPhotoLocalFileName() {
		return tripPhotoLocalFileName;
	}

	public void setTripPhotoLocalFileName(String[] tripPhotoLocalFileName) {
		this.tripPhotoLocalFileName = tripPhotoLocalFileName;
	}

	public String[] getTripCameraBase64() {
		return tripCameraBase64;
	}

	public void setTripCameraBase64(String[] tripCameraBase64) {
		this.tripCameraBase64 = tripCameraBase64;
	}

	public String getTripPhotoPathHttp() {
		return tripPhotoPathHttp;
	}

	public void setTripPhotoPathHttp(String tripPhotoPathHttp) {
		this.tripPhotoPathHttp = tripPhotoPathHttp;
	}
	
}
