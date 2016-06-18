package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.system.LsSystemOrganizationUserBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceApplicationBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceDispatchBO;
import com.nuctech.ls.model.dao.MonitorTripDao;
import com.nuctech.ls.model.dao.SystemDepartmentDao;
import com.nuctech.ls.model.dao.SystemOrganizationUserDao;
import com.nuctech.ls.model.dao.SystemParamsDao;
import com.nuctech.ls.model.dao.WarehouseDeviceApplicationDao;
import com.nuctech.ls.model.dao.WarehouseDeviceDispatchDao;
import com.nuctech.ls.model.dao.WarehouseElockDao;
import com.nuctech.ls.model.dao.WarehouseEsealDao;
import com.nuctech.ls.model.dao.WarehouseSensorDao;
import com.nuctech.ls.model.vo.warehouse.DeviceInventoryChartsVO;
import com.nuctech.ls.model.vo.warehouse.DispatchActualProgram;
import com.nuctech.ls.model.vo.warehouse.DispatchPortVO;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>调度分析 Service</p>
 * 创建时间：2016年6月2日
 */
@Service
@Transactional
public class WarehouseDispatchAnalysisService extends LSBaseService {

	@Resource
	private SystemDepartmentDao systemDepartmentDao;
	@Resource
	private SystemOrganizationUserDao systemOrganizationUserDao;
	@Resource
	private SystemParamsDao systemParamsDao;
	@Resource
	private MonitorTripDao monitorTripDao;
	@Resource
	private WarehouseElockDao warehouseElockDao;
	@Resource
	private WarehouseEsealDao warehouseEsealDao;
	@Resource
	private WarehouseSensorDao warehouseSensorDao;
	@Resource
	private WarehouseDeviceApplicationDao warehouseDeviceApplicationDao;
	@Resource
	private WarehouseDeviceDispatchDao warehouseDeviceDispatchDao;
	
	/**
	 * 获取口岸列表
	 * 
	 * @param port
	 * @param currentUserId
	 * @return
	 */
	public List<DispatchPortVO> findDispatchPortList(String portName, String currentUserId) {
		int interval = Integer.parseInt(systemParamsDao.findSystemParamsValueByKey("dispatch.ststistics.interval"));
		List<DispatchPortVO> dispatchPortList = new ArrayList<DispatchPortVO>();
		List<LsSystemDepartmentBO> portList = findCurrentCountryPortList(currentUserId, portName);
		if(portList != null && !portList.isEmpty()) {
			for(LsSystemDepartmentBO systemDepartment : portList) {
				DispatchPortVO dispatchPort = new DispatchPortVO();
				dispatchPort.setPortId(systemDepartment.getOrganizationId());
				dispatchPort.setPortName(systemDepartment.getOrganizationName());
				List<Integer> deviceAverageList = monitorTripDao.findDeviceAverageCountByTimeInterval(systemDepartment.getOrganizationId(), interval);
				dispatchPort.setAverageTrackDevice(deviceAverageList.get(0));
				dispatchPort.setAverageEseal(deviceAverageList.get(1));
				dispatchPort.setAverageSensor(deviceAverageList.get(2));
				dispatchPortList.add(dispatchPort);
			}
		}
		return dispatchPortList;
	}
	
	/**
	 * 查询调度分析口岸柱形图数据
	 * 
	 * @param currentUserId
	 * @return
	 */
	public List<DeviceInventoryChartsVO> findDispatchPortChartList(String currentUserId) {
		List<DeviceInventoryChartsVO> deviceInventoryChartsList = new ArrayList<DeviceInventoryChartsVO>();
		float ratio = Float.parseFloat(systemParamsDao.findSystemParamsValueByKey("device.reservation.ratio"));
		List<LsSystemDepartmentBO> portList = findCurrentCountryPortList(currentUserId, null);
		if(portList != null) {
			for(LsSystemDepartmentBO systemDepartment : portList) {
				DeviceInventoryChartsVO deviceInventoryCharts = new DeviceInventoryChartsVO();
				String portId = systemDepartment.getOrganizationId();
				deviceInventoryCharts.setPortName(systemDepartment.getOrganizationName());
				Integer availableTrackDevice = warehouseElockDao.statisticsAvailableElockByPortId(portId);
				Integer destroyTrackDevice = warehouseElockDao.statisticsNotAvailableElockByPortId(portId);
				Integer reservationTrackDevice = (int)Math.ceil(availableTrackDevice * ratio);
				Integer availableEseal = warehouseEsealDao.statisticsAvailableEsealByPortId(portId);
				Integer destroyEseal = warehouseEsealDao.statisticsNotAvailableEsealByPortId(portId);
				Integer reservationEseal = (int)Math.ceil(availableEseal * ratio);
				Integer availableSensor = warehouseSensorDao.statisticsAvailableSensorByPortId(portId);
				Integer destroySensor = warehouseSensorDao.statisticsNotAvailableSensorByPortId(portId);
				Integer reservationSensor = (int)Math.ceil(availableSensor * ratio);
				Integer[] deviceArray = new Integer[] {availableTrackDevice, destroyTrackDevice, reservationTrackDevice,
						availableEseal, destroyEseal, reservationEseal, availableSensor, destroySensor, reservationSensor};
				deviceInventoryCharts.setDeviceArray(deviceArray);
				deviceInventoryChartsList.add(deviceInventoryCharts);
			}
		}
		return deviceInventoryChartsList;
	}
	
	/**
	 * 查询当前国家的口岸列表
	 * 
	 * @param currentUserId
	 * 				系统登录人ID
	 * @param portName
	 * 				口岸名称
	 * @return
	 */
	private List<LsSystemDepartmentBO> findCurrentCountryPortList(String currentUserId, String portName) {
		//查询一个国家所有口岸的列表
		List<LsSystemDepartmentBO> portList = new ArrayList<LsSystemDepartmentBO>();
		String deptId = findDepartmentIdByUserId(currentUserId);
		if (deptId != null) {
			LsSystemDepartmentBO departmentBO = systemDepartmentDao.findById(deptId);
			String countryId = findCountryIdByDeptId(departmentBO);
			portList = systemDepartmentDao.findCountryPortList(countryId, portName);
		}
		return portList;
	}
	
	/**
	 * 通过口岸ID查询实际方案预设列表
	 * 
	 * @param portId
	 * 			口岸ID
	 * @return
	 */
	public DispatchActualProgram findDispatchActualProgramByPortId(String portId) {
		float ratio = Float.parseFloat(systemParamsDao.findSystemParamsValueByKey("device.reservation.ratio"));
		DispatchActualProgram dispatchActualProgram = new DispatchActualProgram();
		LsSystemDepartmentBO port = systemDepartmentDao.findById(portId);
		
		dispatchActualProgram.setPortId(port.getOrganizationId());
		dispatchActualProgram.setPortName(port.getOrganizationName());
		Integer availableTrackDevice = warehouseElockDao.statisticsAvailableElockByPortId(portId);
		Integer reservationTrackDevice = (int)Math.ceil(availableTrackDevice * ratio);
		dispatchActualProgram.setAvailableTrackDevice(availableTrackDevice - reservationTrackDevice);
		Integer availableEseal = warehouseEsealDao.statisticsAvailableEsealByPortId(portId);
		Integer reservationEseal = (int)Math.ceil(availableEseal * ratio);
		dispatchActualProgram.setAvailableEseal(availableEseal - reservationEseal);
		Integer availableSensor = warehouseSensorDao.statisticsAvailableSensorByPortId(portId);
		Integer reservationSensor = (int)Math.ceil(availableSensor * ratio);
		dispatchActualProgram.setAvailableSensor(availableSensor - reservationSensor);
		dispatchActualProgram.setLatitude(port.getLatitude());
		dispatchActualProgram.setLongitude(port.getLongitude());
		return dispatchActualProgram;
	}
	
	/**
	 * 根据组织Id查找国家节点Id
	 * 
	 * @param deptId
	 * @return
	 */
	public String findCountryIdByDeptId(LsSystemDepartmentBO departmentBO) {
		if (departmentBO == null || departmentBO.getLevelCode() == null) {
			return null;
		}
		String levelCode = departmentBO.getLevelCode(); // 001.002.003.004
		return levelCode.split("\\.")[0];
	}
	
	/**
	 * 根据用户Id查询组织机构Id
	 * 
	 * @param userId
	 *            用户Id
	 * @return
	 */
	public String findDepartmentIdByUserId(String userId) {
		LsSystemOrganizationUserBO orgUser = systemOrganizationUserDao.findDepartIdByUserId(userId);
		if (orgUser != null) {
			return orgUser.getOrganizationId();
		}
		return null;
	}
	
	/**
	 * 通过主键查询申请记录
	 * 
	 * @param id
	 * 		主键ID
	 * @return
	 */
	public LsWarehouseDeviceApplicationBO findWarehouseDeviceApplicationById(String id) {
		return warehouseDeviceApplicationDao.findById(id);
	}
	
	/**
	 * 保存调配记录表
	 * 
	 * @param dispatch
	 */
	public void saveWarehouseDeviceDispatch(LsWarehouseDeviceDispatchBO dispatch) {
		warehouseDeviceDispatchDao.save(dispatch);
	}
}
