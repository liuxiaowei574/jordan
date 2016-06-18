package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceApplicationBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceDispatchBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDispatchDetailBO;
import com.nuctech.ls.model.dao.DispatchDetailDao;
import com.nuctech.ls.model.dao.MonitorTripDao;
import com.nuctech.ls.model.dao.SystemDepartmentDao;
import com.nuctech.ls.model.dao.WarehouseDeviceApplicationDao;
import com.nuctech.ls.model.dao.WarehouseDeviceDispatchDao;
import com.nuctech.ls.model.util.DeviceType;
import com.nuctech.ls.model.vo.report.DeviceInventoryDetail;
import com.nuctech.ls.model.vo.report.DeviceInventoryVO;
import com.nuctech.util.Constant;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>库存报告 Service</p>
 * 创建时间：2016年6月13日
 */
@Service
@Transactional
public class InventoryReportService extends LSBaseService {
	
	@Resource
	private MonitorTripDao monitorTripDao;
	@Resource
	private WarehouseDeviceDispatchDao warehouseDeviceDispatchDao;
	@Resource
	private WarehouseDeviceApplicationDao warehouseDeviceApplicationDao;
	@Resource
	private SystemDepartmentDao systemDepartmentDao;
	@Resource
	private DispatchDetailDao dispatchDetailDao;

	/**
	 * 统计设备库存
	 * 
	 * @param portId
	 * 			口岸ID
	 * @return
	 */
	public DeviceInventoryVO statisticsDeviceInventory(String portId, Date startDate, Date endDate) {
		
		DeviceInventoryVO deviceInventory = new DeviceInventoryVO();
		//统计流入
		Map<String, Integer> flowInMap = monitorTripDao.findPortFlowDevices(portId, startDate, endDate, Constant.FLOW_IN);
		deviceInventory.setTrackDeviceFlowIn(flowInMap.get(DeviceType.TRACKING_DEVICE.getType()));
		deviceInventory.setEsealFlowIn(flowInMap.get(DeviceType.ESEAL.getType()));
		deviceInventory.setSensorFlowIn(flowInMap.get(DeviceType.SENSOR.getType()));
		//统计流出
		Map<String, Integer> flowOutMap = monitorTripDao.findPortFlowDevices(portId, startDate, endDate, Constant.FLOW_OUT);
		deviceInventory.setTrackDeviceFlowOut(flowOutMap.get(DeviceType.TRACKING_DEVICE.getType()));
		deviceInventory.setEsealFlowOut(flowOutMap.get(DeviceType.ESEAL.getType()));
		deviceInventory.setSensorFlowOut(flowOutMap.get(DeviceType.SENSOR.getType()));
		//统计转入
		Map<String, Integer> turnInMap = warehouseDeviceApplicationDao.findTurnInDevices(portId, startDate, endDate);
		deviceInventory.setTrackDeviceTurnIn(turnInMap.get(DeviceType.TRACKING_DEVICE.getType()));
		deviceInventory.setEsealTurnIn(turnInMap.get(DeviceType.ESEAL.getType()));
		deviceInventory.setSensorTurnIn(turnInMap.get(DeviceType.SENSOR.getType()));
		//统计转出
		Map<String, Integer> turnOutMap = warehouseDeviceDispatchDao.findTurnOutDevices(portId, startDate, endDate);
		deviceInventory.setTrackDeviceTurnOut(turnOutMap.get(DeviceType.TRACKING_DEVICE.getType()));
		deviceInventory.setEsealTurnOut(turnOutMap.get(DeviceType.ESEAL.getType()));
		deviceInventory.setSensorTurnOut(turnOutMap.get(DeviceType.SENSOR.getType()));
		return deviceInventory;
	}
	
	/**
	 * 传感器列表
	 * 
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONArray findSensorFlowList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		List<DeviceInventoryDetail> pageList = new ArrayList<DeviceInventoryDetail>();
		List<DeviceInventoryDetail> devicePageList = findDeviceFlowList(pageQuery);
		if(devicePageList != null && !devicePageList.isEmpty()) {
			for(DeviceInventoryDetail detail : devicePageList) {
				String strSeneorNumber = detail.getSensorNumber();
				if(strSeneorNumber.indexOf(",") != -1) {
					String[] arrSensorNumber = strSeneorNumber.split(",");
					for(String sensorNumber : arrSensorNumber) {
						DeviceInventoryDetail deviceInventoryDetail = new DeviceInventoryDetail();
						BeanUtils.copyProperties(detail, deviceInventoryDetail, new String[]{"sensorNumber"});
						deviceInventoryDetail.setSensorNumber(sensorNumber);
						pageList.add(deviceInventoryDetail);
					}
				} else {
					DeviceInventoryDetail deviceInventoryDetail = new DeviceInventoryDetail();
					BeanUtils.copyProperties(detail, deviceInventoryDetail);
					pageList.add(deviceInventoryDetail);
				}
			}
		}
		return fromArrayList(pageList, jsonConfig, ignoreDefaultExcludes);
	}
	
	/**
	 * 关锁列表
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONArray findTrackDeviceFlowList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		List<DeviceInventoryDetail> pageList = findDeviceFlowList(pageQuery);
		return fromArrayList(pageList, jsonConfig, ignoreDefaultExcludes);
	}
	
	/**
	 * 关锁转入
	 * 
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	public JSONArray findTrackDeviceTrunInList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		List<DeviceInventoryDetail> trackDeviceList = new ArrayList<DeviceInventoryDetail>();
		List<LsWarehouseDeviceApplicationBO> deviceDispatchList = findWarehouseDeviceApplication(pageQuery);
		if(deviceDispatchList != null && !deviceDispatchList.isEmpty()) {
			for(LsWarehouseDeviceApplicationBO application : deviceDispatchList) {
				//通过申请的ID查询记录的ID
				List<LsWarehouseDeviceDispatchBO> dispatchList = warehouseDeviceDispatchDao.findWarehouseDeviceDispatchListByApplicationId(application.getApplicationId());
				if(dispatchList != null && !dispatchList.isEmpty()) {
					for(LsWarehouseDeviceDispatchBO deviceDispatch : dispatchList) {
						List<LsWarehouseDispatchDetailBO> trackDeviceDispatchDetailList = dispatchDetailDao.findWarehouseDispatchDetailByDeviceDispatchIDAndType(deviceDispatch.getDispatchId(), DeviceType.TRACKING_DEVICE.getType());
						if(trackDeviceDispatchDetailList != null && !trackDeviceDispatchDetailList.isEmpty()) {
							for(LsWarehouseDispatchDetailBO detail : trackDeviceDispatchDetailList) {
								DeviceInventoryDetail trackDeviceInventoryDetail = new DeviceInventoryDetail();
								trackDeviceInventoryDetail.setCheckInDate(application.getFinishTime());
								trackDeviceInventoryDetail.setTrackDeviceNumber(detail.getDeviceNumber());
								LsSystemDepartmentBO fromPort = systemDepartmentDao.findById(deviceDispatch.getFromPort());
								trackDeviceInventoryDetail.setForm(fromPort.getOrganizationId());
								trackDeviceInventoryDetail.setFormName(fromPort.getOrganizationName());
								LsSystemDepartmentBO toPort = systemDepartmentDao.findById(deviceDispatch.getToPort());
								trackDeviceInventoryDetail.setTo(toPort.getOrganizationId());
								trackDeviceInventoryDetail.setToName(toPort.getOrganizationName());
								trackDeviceList.add(trackDeviceInventoryDetail);
							}
						}
					}
				}
			}
		}
		return fromArrayList(trackDeviceList, jsonConfig, ignoreDefaultExcludes);
	}
	
	/**
	 * 子锁转入
	 * 
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	public JSONArray findEsealTrunInList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		List<DeviceInventoryDetail> list = new ArrayList<DeviceInventoryDetail>();
		List<LsWarehouseDeviceApplicationBO> deviceDispatchList = findWarehouseDeviceApplication(pageQuery);
		if(deviceDispatchList != null && !deviceDispatchList.isEmpty()) {
			for(LsWarehouseDeviceApplicationBO application : deviceDispatchList) {
				//通过申请的ID查询记录的ID
				List<LsWarehouseDeviceDispatchBO> dispatchList = warehouseDeviceDispatchDao.findWarehouseDeviceDispatchListByApplicationId(application.getApplicationId());
				if(dispatchList != null && !dispatchList.isEmpty()) {
					for(LsWarehouseDeviceDispatchBO deviceDispatch : dispatchList) {
						List<LsWarehouseDispatchDetailBO> dispatchDetailList = dispatchDetailDao.findWarehouseDispatchDetailByDeviceDispatchIDAndType(deviceDispatch.getDispatchId(), DeviceType.ESEAL.getType());
						if(dispatchDetailList != null && !dispatchDetailList.isEmpty()) {
							for(LsWarehouseDispatchDetailBO detail : dispatchDetailList) {
								DeviceInventoryDetail inventoryDetail = new DeviceInventoryDetail();
								inventoryDetail.setCheckInDate(application.getFinishTime());
								inventoryDetail.setEsealNumber(detail.getDeviceNumber());
								LsSystemDepartmentBO fromPort = systemDepartmentDao.findById(deviceDispatch.getFromPort());
								inventoryDetail.setForm(fromPort.getOrganizationId());
								inventoryDetail.setFormName(fromPort.getOrganizationName());
								LsSystemDepartmentBO toPort = systemDepartmentDao.findById(deviceDispatch.getToPort());
								inventoryDetail.setTo(toPort.getOrganizationId());
								inventoryDetail.setToName(toPort.getOrganizationName());
								list.add(inventoryDetail);
							}
						}
					}
				}
			}
		}
		return fromArrayList(list, jsonConfig, ignoreDefaultExcludes);
	}
	
	/**
	 * 子锁转入
	 * 
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	public JSONArray findSensorTrunInList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		List<DeviceInventoryDetail> list = new ArrayList<DeviceInventoryDetail>();
		List<LsWarehouseDeviceApplicationBO> deviceDispatchList = findWarehouseDeviceApplication(pageQuery);
		if(deviceDispatchList != null && !deviceDispatchList.isEmpty()) {
			for(LsWarehouseDeviceApplicationBO application : deviceDispatchList) {
				//通过申请的ID查询记录的ID
				List<LsWarehouseDeviceDispatchBO> dispatchList = warehouseDeviceDispatchDao.findWarehouseDeviceDispatchListByApplicationId(application.getApplicationId());
				if(dispatchList != null && !dispatchList.isEmpty()) {
					for(LsWarehouseDeviceDispatchBO deviceDispatch : dispatchList) {
						List<LsWarehouseDispatchDetailBO> dispatchDetailList = dispatchDetailDao.findWarehouseDispatchDetailByDeviceDispatchIDAndType(deviceDispatch.getDispatchId(), DeviceType.SENSOR.getType());
						if(dispatchDetailList != null && !dispatchDetailList.isEmpty()) {
							for(LsWarehouseDispatchDetailBO detail : dispatchDetailList) {
								DeviceInventoryDetail inventoryDetail = new DeviceInventoryDetail();
								inventoryDetail.setCheckInDate(application.getFinishTime());
								inventoryDetail.setSensorNumber(detail.getDeviceNumber());
								LsSystemDepartmentBO fromPort = systemDepartmentDao.findById(deviceDispatch.getFromPort());
								inventoryDetail.setForm(fromPort.getOrganizationId());
								inventoryDetail.setFormName(fromPort.getOrganizationName());
								LsSystemDepartmentBO toPort = systemDepartmentDao.findById(deviceDispatch.getToPort());
								inventoryDetail.setTo(toPort.getOrganizationId());
								inventoryDetail.setToName(toPort.getOrganizationName());
								list.add(inventoryDetail);
							}
						}
					}
				}
			}
		}
		return fromArrayList(list, jsonConfig, ignoreDefaultExcludes);
	}
	
	/**
	 * 关锁转出
	 * 
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONArray findTrackDeviceTrunOutList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		List<DeviceInventoryDetail> trackDeviceList = new ArrayList<DeviceInventoryDetail>();
		List<LsWarehouseDeviceDispatchBO> deviceDispatchList = findWarehouseDeviceDispatchList(pageQuery);
		if(deviceDispatchList != null && !deviceDispatchList.isEmpty()) {
			for(LsWarehouseDeviceDispatchBO dispatch : deviceDispatchList) {
				//查询关锁
				String deviceDispatchID = dispatch.getDispatchId();
				List<LsWarehouseDispatchDetailBO> trackDeviceDispatchDetailList = dispatchDetailDao.findWarehouseDispatchDetailByDeviceDispatchIDAndType(deviceDispatchID, DeviceType.TRACKING_DEVICE.getType());
				if(trackDeviceDispatchDetailList != null && !trackDeviceDispatchDetailList.isEmpty()) {
					for(LsWarehouseDispatchDetailBO detail : trackDeviceDispatchDetailList) {
						DeviceInventoryDetail trackDeviceInventoryDetail = new DeviceInventoryDetail();
						trackDeviceInventoryDetail.setCheckOutDate(dispatch.getDispatchTime());
						trackDeviceInventoryDetail.setTrackDeviceNumber(detail.getDeviceNumber());
						LsSystemDepartmentBO fromPort = systemDepartmentDao.findById(dispatch.getFromPort());
						trackDeviceInventoryDetail.setForm(fromPort.getOrganizationId());
						trackDeviceInventoryDetail.setFormName(fromPort.getOrganizationName());
//						trackDeviceInventoryDetail.setTo(dispatch.getToPort());
						LsSystemDepartmentBO toPort = systemDepartmentDao.findById(dispatch.getToPort());
						trackDeviceInventoryDetail.setTo(toPort.getOrganizationId());
						trackDeviceInventoryDetail.setToName(toPort.getOrganizationName());
						trackDeviceList.add(trackDeviceInventoryDetail);
					}
				}
			}
		}
		return fromArrayList(trackDeviceList, jsonConfig, ignoreDefaultExcludes);
	}
	
	/**
	 * 子锁转出
	 * 
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	public JSONArray findEsealTrunOutList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		List<DeviceInventoryDetail> trackDeviceList = new ArrayList<DeviceInventoryDetail>();
		List<LsWarehouseDeviceDispatchBO> deviceDispatchList = findWarehouseDeviceDispatchList(pageQuery);
		if(deviceDispatchList != null && !deviceDispatchList.isEmpty()) {
			for(LsWarehouseDeviceDispatchBO dispatch : deviceDispatchList) {
				//查询关锁
				String deviceDispatchID = dispatch.getDispatchId();
				List<LsWarehouseDispatchDetailBO> trackDeviceDispatchDetailList = dispatchDetailDao.findWarehouseDispatchDetailByDeviceDispatchIDAndType(deviceDispatchID, DeviceType.ESEAL.getType());
				if(trackDeviceDispatchDetailList != null && !trackDeviceDispatchDetailList.isEmpty()) {
					for(LsWarehouseDispatchDetailBO detail : trackDeviceDispatchDetailList) {
						DeviceInventoryDetail esealInventoryDetail = new DeviceInventoryDetail();
						esealInventoryDetail.setCheckOutDate(dispatch.getDispatchTime());
						esealInventoryDetail.setEsealNumber(detail.getDeviceNumber());
						LsSystemDepartmentBO fromPort = systemDepartmentDao.findById(dispatch.getFromPort());
						esealInventoryDetail.setForm(fromPort.getOrganizationId());
						esealInventoryDetail.setFormName(fromPort.getOrganizationName());
						LsSystemDepartmentBO toPort = systemDepartmentDao.findById(dispatch.getToPort());
						esealInventoryDetail.setTo(toPort.getOrganizationId());
						esealInventoryDetail.setToName(toPort.getOrganizationName());
						trackDeviceList.add(esealInventoryDetail);
					}
				}
			}
		}
		return fromArrayList(trackDeviceList, jsonConfig, ignoreDefaultExcludes);
	}
	
	/**
	 * 子锁转出
	 * 
	 * @param pageQuery
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JSONArray findSensorTrunOutList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		List<DeviceInventoryDetail> trackDeviceList = new ArrayList<DeviceInventoryDetail>();
		List<LsWarehouseDeviceDispatchBO> deviceDispatchList = findWarehouseDeviceDispatchList(pageQuery);
		if(deviceDispatchList != null && !deviceDispatchList.isEmpty()) {
			for(LsWarehouseDeviceDispatchBO dispatch : deviceDispatchList) {
				//查询关锁
				String deviceDispatchID = dispatch.getDispatchId();
				List<LsWarehouseDispatchDetailBO> trackDeviceDispatchDetailList = dispatchDetailDao.findWarehouseDispatchDetailByDeviceDispatchIDAndType(deviceDispatchID, DeviceType.SENSOR.getType());
				if(trackDeviceDispatchDetailList != null && !trackDeviceDispatchDetailList.isEmpty()) {
					for(LsWarehouseDispatchDetailBO detail : trackDeviceDispatchDetailList) {
						DeviceInventoryDetail esealInventoryDetail = new DeviceInventoryDetail();
						esealInventoryDetail.setCheckOutDate(dispatch.getDispatchTime());
						esealInventoryDetail.setSensorNumber(detail.getDeviceNumber());
						LsSystemDepartmentBO fromPort = systemDepartmentDao.findById(dispatch.getFromPort());
						esealInventoryDetail.setForm(fromPort.getOrganizationId());
						esealInventoryDetail.setFormName(fromPort.getOrganizationName());
						LsSystemDepartmentBO toPort = systemDepartmentDao.findById(dispatch.getToPort());
						esealInventoryDetail.setTo(toPort.getOrganizationId());
						esealInventoryDetail.setToName(toPort.getOrganizationName());
						trackDeviceList.add(esealInventoryDetail);
					}
				}
			}
		}
		return fromArrayList(trackDeviceList, jsonConfig, ignoreDefaultExcludes);
	}
	
	
	/**
	 * 根据类型查询调度记录详细信息
	 * @param pageQuery
	 * @param type
	 * @return
	 */
//	private List<LsWarehouseDispatchDetailBO> findWarehouseDispatchDetailByType(PageQuery<Map> pageQuery, String type) {
//		List<LsWarehouseDispatchDetailBO> trackDeviceDispatchDetailList = new ArrayList<LsWarehouseDispatchDetailBO>();
//		List<LsWarehouseDeviceDispatchBO> deviceDispatchList = findWarehouseDeviceDispatchList(pageQuery);
//		if(deviceDispatchList != null && !deviceDispatchList.isEmpty()) {
//			for(LsWarehouseDeviceDispatchBO dispatch : deviceDispatchList) {
//				//查询关锁
//				String deviceDispatchID = dispatch.getDispatchId();
//				trackDeviceDispatchDetailList = dispatchDetailDao.findWarehouseDispatchDetailByDeviceDispatchIDAndType(deviceDispatchID, type);
//			}
//		}
//		return trackDeviceDispatchDetailList;
//	}
	
	@SuppressWarnings("rawtypes")
	public JSONArray findEsealFlowList(PageQuery<Map> pageQuery,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		List<DeviceInventoryDetail> pageList = new ArrayList<DeviceInventoryDetail>();
		List<DeviceInventoryDetail> devicePageList = findDeviceFlowList(pageQuery);
		if(devicePageList != null && !devicePageList.isEmpty()) {
			for(DeviceInventoryDetail detail : devicePageList) {
				String strEsealNumber = detail.getEsealNumber();
				if(strEsealNumber.indexOf(",") != -1) {
					String[] arrEsealNumber = strEsealNumber.split(",");
					for(String esealNumber : arrEsealNumber) {
						DeviceInventoryDetail deviceInventoryDetail = new DeviceInventoryDetail();
						BeanUtils.copyProperties(detail, deviceInventoryDetail, new String[]{"esealNumber"});
						deviceInventoryDetail.setEsealNumber(esealNumber);
						pageList.add(deviceInventoryDetail);
					}
				} else {
					DeviceInventoryDetail deviceInventoryDetail = new DeviceInventoryDetail();
					BeanUtils.copyProperties(detail, deviceInventoryDetail);
					pageList.add(deviceInventoryDetail);
				}
			}
		}
		return fromArrayList(pageList, jsonConfig, ignoreDefaultExcludes);
	}
	
	/**
	 * 查询设备列表
	 * @param pageQuery
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private List<DeviceInventoryDetail> findDeviceFlowList(PageQuery<Map> pageQuery) {
		List<DeviceInventoryDetail> pageList = new ArrayList<DeviceInventoryDetail>();
		List<LsMonitorTripBO> monitorTripPageList = findMonitorTripList(pageQuery);
		if(monitorTripPageList != null && !monitorTripPageList.isEmpty()) {
			for(LsMonitorTripBO monitorTrip : monitorTripPageList) {
				DeviceInventoryDetail deviceInventoryDetail = new DeviceInventoryDetail();
				deviceInventoryDetail.setTrackDeviceNumber(monitorTrip.getTrackingDeviceNumber());
				deviceInventoryDetail.setEsealNumber(monitorTrip.getEsealNumber());
				deviceInventoryDetail.setSensorNumber(monitorTrip.getSensorNumber());
				deviceInventoryDetail.setCheckInDate(monitorTrip.getCheckinTime());
				deviceInventoryDetail.setCheckOutDate(monitorTrip.getCheckoutTime());
				LsSystemDepartmentBO toPort = systemDepartmentDao.findById(monitorTrip.getCheckoutPort());
				deviceInventoryDetail.setTo(toPort.getOrganizationId());
				deviceInventoryDetail.setToName(toPort.getOrganizationName());
				LsSystemDepartmentBO fromPort = systemDepartmentDao.findById(monitorTrip.getCheckinPort());
				deviceInventoryDetail.setForm(fromPort.getOrganizationId());
				deviceInventoryDetail.setFormName(fromPort.getOrganizationName());
				pageList.add(deviceInventoryDetail);
			}
		}
		return pageList;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<LsMonitorTripBO> findMonitorTripList(PageQuery<Map> pageQuery) {
		String queryString = "select t from LsMonitorTripBO t where 1=1 "
				+ "/~ and t.tripId = '[tripId]' ~/"
				+ "/~ and t.checkinPort = '[checkinPort]' ~/"
				+ "/~ and t.checkinTime >= '[checkinStartTime]' ~/"
				+ "/~ and t.checkinTime <= '[checkinEndTime]' ~/"
				+ "/~ and t.checkoutPort = '[checkoutPort]' ~/"
				+ "/~ and t.checkoutTime >= '[checkoutStartTime]' ~/"
				+ "/~ and t.checkoutTime <= '[checkoutEndTime]' ~/"
				+ "/~ order by [sortColumns] ~/";
		List<LsMonitorTripBO> monitorTripPageList = monitorTripDao.findAllList(queryString, pageQuery);
		return monitorTripPageList;
	}
	
	/**
	 * 查询转入口岸数据
	 * 
	 * @param pageQuery
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<LsWarehouseDeviceApplicationBO> findWarehouseDeviceApplication(PageQuery<Map> pageQuery) {
		String queryString = "select t from LsWarehouseDeviceApplicationBO t where 1=1 "
				+ "/~ and t.applicationId = '[applicationId]' ~/" 		
				+ "/~ and t.applcationPort = '[applcationPort]' ~/"		//转入节点
				+ "/~ and t.finishTime >= '[finishStartTime]' ~/"		//转入时间
				+ "/~ and t.finishTime <= '[finishEndTime]' ~/"
				+ "/~ order by [sortColumns] ~/";
		List<LsWarehouseDeviceApplicationBO> warehouseDeviceApplicationList = warehouseDeviceApplicationDao.findAllList(queryString, pageQuery);
		return warehouseDeviceApplicationList;
	}
	
	/**
	 * 查询转出口岸数据
	 * 
	 * @param pageQuery
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LsWarehouseDeviceDispatchBO> findWarehouseDeviceDispatchList(PageQuery<Map> pageQuery) {
		String queryString = "select t from LsWarehouseDeviceDispatchBO t where 1=1 "
				+ "/~ and t.dispatchId = '[dispatchId]' ~/" 		
				+ "/~ and t.fromPort = '[fromPort]' ~/"		//转出节点
				+ "/~ and t.dispatchTime >= '[dispatchStartTime]' ~/"		//转出时间
				+ "/~ and t.dispatchTime <= '[dispatchEndTime]' ~/"
				+ "/~ order by [sortColumns] ~/";
		List<LsWarehouseDeviceDispatchBO> warehouseDeviceDispatchList = warehouseDeviceDispatchDao.findAllList(queryString, pageQuery);
		return warehouseDeviceDispatchList;
	}
}
