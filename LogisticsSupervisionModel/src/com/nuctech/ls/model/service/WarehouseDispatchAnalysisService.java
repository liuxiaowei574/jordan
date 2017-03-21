package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.system.LsSystemOrganizationUserBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceApplicationBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceDispatchBO;
import com.nuctech.ls.model.dao.MonitorRouteAreaDao;
import com.nuctech.ls.model.dao.MonitorTripDao;
import com.nuctech.ls.model.dao.SystemDepartmentDao;
import com.nuctech.ls.model.dao.SystemOrganizationUserDao;
import com.nuctech.ls.model.dao.SystemParamsDao;
import com.nuctech.ls.model.dao.WarehouseDeviceApplicationDao;
import com.nuctech.ls.model.dao.WarehouseDeviceDispatchDao;
import com.nuctech.ls.model.dao.WarehouseElockDao;
import com.nuctech.ls.model.dao.WarehouseEsealDao;
import com.nuctech.ls.model.dao.WarehouseSensorDao;
import com.nuctech.ls.model.util.DeviceStatus;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.DeviceDemand;
import com.nuctech.ls.model.vo.warehouse.DeviceInventoryChartsVO;
import com.nuctech.ls.model.vo.warehouse.DispatchActualProgram;
import com.nuctech.ls.model.vo.warehouse.DispatchPlanVO;
import com.nuctech.ls.model.vo.warehouse.DispatchPortVO;
import com.nuctech.ls.model.vo.warehouse.PortElockStatisitcVO;
import com.nuctech.ls.model.vo.warehouse.PortEsealStatisitcVO;
import com.nuctech.ls.model.vo.warehouse.PortSensorStatisitcVO;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 调度分析 Service
 * </p>
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
    @Resource
    private MonitorRouteAreaDao monitorRouteAreaDao;
    private LsSystemDepartmentBO systemDepartmentBO;

    /**
     * 获取口岸列表
     * 
     * @param port
     * @param currentUserId
     * @return
     * @throws Exception
     */
    public List<DispatchPortVO> findDispatchPortList(String portName, String currentUserId) throws Exception {
        int interval = Integer.parseInt(systemParamsDao.findSystemParamsValueByKey("DISPATCH_STATISTICS_INTERVAL"));
        List<DispatchPortVO> dispatchPortList = new ArrayList<DispatchPortVO>();
        List<LsSystemDepartmentBO> portList = findCurrentCountryPortList(currentUserId, portName);
        if (portList != null && !portList.isEmpty()) {
            for (LsSystemDepartmentBO systemDepartment : portList) {
                DispatchPortVO dispatchPort = new DispatchPortVO();
                dispatchPort.setPortId(systemDepartment.getOrganizationId());
                dispatchPort.setPortName(systemDepartment.getOrganizationName());
                /*
                 * List<Integer> deviceAverageList = monitorTripDao
                 * .findDeviceAverageCountByTimeInterval(systemDepartment.
                 * getOrganizationId(), interval);
                 * dispatchPort.setAverageTrackDevice(deviceAverageList.get(0));
                 * dispatchPort.setAverageEseal(deviceAverageList.get(1));
                 * dispatchPort.setAverageSensor(deviceAverageList.get(2));
                 * dispatchPortList.add(dispatchPort);
                 */

                // 查询各个口岸可用的处于正常状态的设备数量
                List<Integer> deviceNomallist = monitorTripDao.findDeviceNomal(systemDepartment.getOrganizationId());
                dispatchPort.setTrackDeviceNumber(deviceNomallist.get(0));
                dispatchPort.setEseal(deviceNomallist.get(1));
                dispatchPort.setSensor(deviceNomallist.get(2));
                dispatchPortList.add(dispatchPort);
            }
        }
        return dispatchPortList;
    }

    /**
     * 获取口岸列表和仓库
     * 
     * @param port
     * @param currentUserId
     * @return
     */
    public List<PortElockStatisitcVO> findDispatchPortAndRoomList(String portName, String currentUserId) {
        // int interval =
        // Integer.parseInt(systemParamsDao.findSystemParamsValueByKey("DISPATCH_STATISTICS_INTERVAL"));
        List<PortElockStatisitcVO> dispatchPortList = new ArrayList<PortElockStatisitcVO>();
        List<LsSystemDepartmentBO> portList = findCurrentCountryPortList(currentUserId, portName);
        List<LsSystemDepartmentBO> roomlist = findCurrentCountryRoomList(currentUserId, portName);
        portList.addAll(roomlist);
        List<PortElockStatisitcVO> portvoList = new ArrayList<PortElockStatisitcVO>();
        List<PortEsealStatisitcVO> esealList = new ArrayList<PortEsealStatisitcVO>();// 子锁各个状态的数量
        List<PortSensorStatisitcVO> sensorList = new ArrayList<PortSensorStatisitcVO>();// 传感器各个状态的数量
        if (portList != null && !portList.isEmpty()) {
            for (LsSystemDepartmentBO systemDepartment : portList) {
                PortElockStatisitcVO dispatchPort = new PortElockStatisitcVO();
                dispatchPort.setPortId(systemDepartment.getOrganizationId());
                dispatchPort.setPortName(systemDepartment.getOrganizationName());
                dispatchPort.setPid(systemDepartment.getParentId());
                portvoList = systemDepartmentDao.countElcokByDepartment(systemDepartment.getOrganizationId());
                for (PortElockStatisitcVO pvo : portvoList) {
                    if (StringUtils.equals(pvo.getElockstatus(), DeviceStatus.Scrap.getText())) {// 报废
                        dispatchPort.setBaofeis(pvo.getKeyongs());
                    }
                    if (StringUtils.equals(pvo.getElockstatus(), DeviceStatus.Destory.getText())) {// 损坏
                        dispatchPort.setSunhuais(pvo.getKeyongs());
                    }
                    if (StringUtils.equals(pvo.getElockstatus(), DeviceStatus.Normal.getText())) {// 可用
                        dispatchPort.setKeyongs(pvo.getKeyongs());
                    }
                    if (StringUtils.equals(pvo.getElockstatus(), DeviceStatus.Inway.getText())) {// 在途
                        dispatchPort.setZaitus(pvo.getKeyongs());
                    }
                    if (StringUtils.equals(pvo.getElockstatus(), DeviceStatus.Maintain.getText())) {// 维修
                        dispatchPort.setWeixius(pvo.getKeyongs());
                    }
                }

                // 子锁
                esealList = systemDepartmentDao.countEsealByDepartment(systemDepartment.getOrganizationId());
                for (PortEsealStatisitcVO pvo : esealList) {
                    if (StringUtils.equals(pvo.getEsealstatus(), DeviceStatus.Scrap.getText())) {// 报废
                        dispatchPort.setEbaofeis(pvo.getEkeyongs());
                    }
                    if (StringUtils.equals(pvo.getEsealstatus(), DeviceStatus.Destory.getText())) {// 损坏
                        dispatchPort.setEsunhuais(pvo.getEkeyongs());
                    }
                    if (StringUtils.equals(pvo.getEsealstatus(), DeviceStatus.Normal.getText())) {// 可用
                        dispatchPort.setEkeyongs(pvo.getEkeyongs());
                    }
                    if (StringUtils.equals(pvo.getEsealstatus(), DeviceStatus.Inway.getText())) {// 在途
                        dispatchPort.setEzaitus(pvo.getEkeyongs());
                    }
                    if (StringUtils.equals(pvo.getEsealstatus(), DeviceStatus.Maintain.getText())) {// 维修
                        dispatchPort.setEweixius(pvo.getEkeyongs());
                    }
                }
                // 传感器
                sensorList = systemDepartmentDao.countSensorByDepartment(systemDepartment.getOrganizationId());
                for (PortSensorStatisitcVO pvo : sensorList) {
                    if (StringUtils.equals(pvo.getSensorstatus(), DeviceStatus.Scrap.getText())) {// 报废
                        dispatchPort.setSbaofeis(pvo.getSkeyongs());
                    }
                    if (StringUtils.equals(pvo.getSensorstatus(), DeviceStatus.Destory.getText())) {// 损坏
                        dispatchPort.setSsunhuais(pvo.getSkeyongs());
                    }
                    if (StringUtils.equals(pvo.getSensorstatus(), DeviceStatus.Normal.getText())) {// 可用
                        dispatchPort.setSkeyongs(pvo.getSkeyongs());
                    }
                    if (StringUtils.equals(pvo.getSensorstatus(), DeviceStatus.Inway.getText())) {// 在途
                        dispatchPort.setSzaitus(pvo.getSkeyongs());
                    }
                    if (StringUtils.equals(pvo.getSensorstatus(), DeviceStatus.Maintain.getText())) {// 维修
                        dispatchPort.setSweixius(pvo.getSkeyongs());
                    }
                }

                dispatchPortList.add(dispatchPort);
            }
        }

        return dispatchPortList;
    }

    private List<LsSystemDepartmentBO> findCurrentCountryRoomList(String curUserid, String portName) {
        // 查询一个国家所有仓库的列表
        List<LsSystemDepartmentBO> portList = new ArrayList<LsSystemDepartmentBO>();
        String deptId = findDepartmentIdByUserId(curUserid);
        if (deptId != null) {
            LsSystemDepartmentBO departmentBO = systemDepartmentDao.findById(deptId);
            String countryId = findCountryIdByDeptId(departmentBO);
            portList = systemDepartmentDao.findCountryRoomList(countryId, portName);
        }
        return portList;
    }

    /**
     * 查询调度分析口岸柱形图数据
     * 
     * @param currentUserId
     * @return
     */
    public List<DeviceInventoryChartsVO> findDispatchPortChartList(String currentUserId) {
        List<DeviceInventoryChartsVO> deviceInventoryChartsList = new ArrayList<DeviceInventoryChartsVO>();
        // 从参数表中取出“设备预留比例”的值
        /*
         * float ratio
         * =Float.parseFloat(systemParamsDao.findSystemParamsValueByKey(
         * "DEVICE_RESERV_RATIO"));
         */

        // 每个口岸设置"预留比例"。根据登陆用户查询所属口岸再取出口岸设置的“预留比例”
        HttpSession session = ServletActionContext.getRequest().getSession();
        SessionUser sessionUser = ((SessionUser) session.getAttribute(Constant.SESSION_USER));
        String orgId = sessionUser.getOrganizationId();// 口岸id
        systemDepartmentBO = systemDepartmentDao.findById(orgId);
        String r = systemDepartmentBO.getReservationRatio();
        float ratio = 0;
        if (NuctechUtil.isNotNull(r)) {
            ratio = Float.parseFloat(r);// 获取“预留比例”的值
        } else {
            ratio = Float.parseFloat(systemParamsDao.findSystemParamsValueByKey("DEVICE_RESERV_RATIO"));// 获取“预留比例”的值
        }

        List<LsSystemDepartmentBO> portList = findCurrentCountryPortList(currentUserId, null);
        if (portList != null) {
            for (LsSystemDepartmentBO systemDepartment : portList) {
                DeviceInventoryChartsVO deviceInventoryCharts = new DeviceInventoryChartsVO();
                String portId = systemDepartment.getOrganizationId();
                deviceInventoryCharts.setPortName(systemDepartment.getOrganizationName());
                Integer availableTrackDevice = warehouseElockDao.statisticsAvailableElockByPortId(portId);
                Integer destroyTrackDevice = warehouseElockDao.statisticsNotAvailableElockByPortId(portId);
                Integer reservationTrackDevice = (int) Math.ceil(availableTrackDevice * ratio);
                Integer availableEseal = warehouseEsealDao.statisticsAvailableEsealByPortId(portId);
                Integer destroyEseal = warehouseEsealDao.statisticsNotAvailableEsealByPortId(portId);
                Integer reservationEseal = (int) Math.ceil(availableEseal * ratio);
                Integer availableSensor = warehouseSensorDao.statisticsAvailableSensorByPortId(portId);
                Integer destroySensor = warehouseSensorDao.statisticsNotAvailableSensorByPortId(portId);
                Integer reservationSensor = (int) Math.ceil(availableSensor * ratio);
                Integer[] deviceArray = new Integer[] { availableTrackDevice, destroyTrackDevice,
                        reservationTrackDevice, availableEseal, destroyEseal, reservationEseal, availableSensor,
                        destroySensor, reservationSensor };
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
     *        系统登录人ID
     * @param portName
     *        口岸名称
     * @return
     */
    private List<LsSystemDepartmentBO> findCurrentCountryPortList(String currentUserId, String portName) {
        // 查询一个国家所有口岸的列表
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
     *        口岸ID
     * @return
     */
    public DispatchActualProgram findDispatchActualProgramByPortId(String portId) {

        // float ratio =
        // Float.parseFloat(systemParamsDao.findSystemParamsValueByKey("DEVICE_RESERV_RATIO"));
        // 每个口岸设置"预留比例"。根据登陆用户查询所属口岸再取出口岸设置的“预留比例”
        HttpSession session = ServletActionContext.getRequest().getSession();
        SessionUser sessionUser = ((SessionUser) session.getAttribute(Constant.SESSION_USER));
        String orgId = sessionUser.getOrganizationId();// 口岸id
        systemDepartmentBO = systemDepartmentDao.findById(orgId);
        String r = systemDepartmentBO.getReservationRatio();
        float ratio = 0;
        if (NuctechUtil.isNotNull(r)) {
            ratio = Float.parseFloat(r);// 获取“预留比例”的值
        } else {
            ratio = Float.parseFloat(systemParamsDao.findSystemParamsValueByKey("DEVICE_RESERV_RATIO"));// 获取“预留比例”的值
        }
        DispatchActualProgram dispatchActualProgram = new DispatchActualProgram();
        LsSystemDepartmentBO port = systemDepartmentDao.findById(portId);

        dispatchActualProgram.setPortId(port.getOrganizationId());
        dispatchActualProgram.setPortName(port.getOrganizationName());
        Integer availableTrackDevice = warehouseElockDao.statisticsAvailableElockByPortId(portId);
        Integer reservationTrackDevice = (int) Math.ceil(availableTrackDevice * ratio);
        dispatchActualProgram.setAvailableTrackDevice(availableTrackDevice - reservationTrackDevice);
        Integer availableEseal = warehouseEsealDao.statisticsAvailableEsealByPortId(portId);
        Integer reservationEseal = (int) Math.ceil(availableEseal * ratio);
        dispatchActualProgram.setAvailableEseal(availableEseal - reservationEseal);
        Integer availableSensor = warehouseSensorDao.statisticsAvailableSensorByPortId(portId);
        Integer reservationSensor = (int) Math.ceil(availableSensor * ratio);
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
     *        用户Id
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
     *        主键ID
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

    /**
     * 调度方案推荐
     * 
     * @param deviceDemand
     * @return
     */
    public List<DispatchPlanVO> findDispatchPlanList(DeviceDemand deviceDemand) {
        List<DispatchPlanVO> list = new ArrayList<DispatchPlanVO>();
        List<LsMonitorRouteAreaBO> monitorRouteAreaList = monitorRouteAreaDao
                .findMonitorRouteAreaOrderByDistance(deviceDemand.getPortId());
        if (monitorRouteAreaList != null && !monitorRouteAreaList.isEmpty()) {
            for (LsMonitorRouteAreaBO monitorRouteArea : monitorRouteAreaList) {
                DispatchPlanVO dispatchPlan = new DispatchPlanVO();
                DispatchActualProgram dispatchProgram = findDispatchActualProgramByPortId(
                        monitorRouteArea.getStartId());
                if (dispatchProgram.getAvailableEseal() > 0 || dispatchProgram.getAvailableSensor() > 0
                        || dispatchProgram.getAvailableTrackDevice() > 0) {
                    DeviceDemand dd = new DeviceDemand();
                    BeanUtils.copyProperties(deviceDemand, dd);
                    if (dd.getEsealNumber() == 0 && dd.getSensorNumber() == 0 && dd.getTrackDeviceNumber() == 0) {
                        continue;
                    } else {
                        deviceDemand = differCal(deviceDemand, dispatchProgram);
                        dispatchPlan.setDistance(monitorRouteArea.getRouteDistance());
                        dispatchPlan.setPortId(monitorRouteArea.getStartId());
                        if (NuctechUtil.isNull(monitorRouteArea.getStartName())
                                && NuctechUtil.isNotNull(monitorRouteArea.getStartId())) {
                            dispatchPlan.setPortName(
                                    systemDepartmentDao.findById(monitorRouteArea.getStartId()).getOrganizationName());
                        }
                        if (dd.getTrackDeviceNumber() > 0) {
                            dispatchPlan.setTrackDeviceNumber(
                                    dd.getTrackDeviceNumber() - deviceDemand.getTrackDeviceNumber() + "");
                        } else {
                            dispatchPlan.setTrackDeviceNumber("0");
                        }
                        if (dd.getEsealNumber() > 0) {
                            dispatchPlan.setEsealNumber(dd.getEsealNumber() - deviceDemand.getEsealNumber() + "");
                        } else {
                            dispatchPlan.setEsealNumber("0");
                        }
                        if (dd.getSensorNumber() > 0) {
                            dispatchPlan.setSensor(dd.getSensorNumber() - deviceDemand.getSensorNumber() + "");
                        } else {
                            dispatchPlan.setSensor("0");
                        }
                        list.add(dispatchPlan);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 计算实际和需求差值
     * 
     * @param deviceDemand
     * @param dispatchActualProgram
     * @return
     */
    private DeviceDemand differCal(DeviceDemand deviceDemand, DispatchActualProgram dispatchActualProgram) {
        Integer tdn = deviceDemand.getTrackDeviceNumber() - dispatchActualProgram.getAvailableTrackDevice();
        Integer en = deviceDemand.getEsealNumber() - dispatchActualProgram.getAvailableEseal();
        Integer sn = deviceDemand.getSensorNumber() - dispatchActualProgram.getAvailableSensor();
        if (tdn < 0) {
            tdn = 0;
        }
        if (en < 0) {
            en = 0;
        }
        if (sn < 0) {
            sn = 0;
        }
        deviceDemand.setTrackDeviceNumber(tdn);
        deviceDemand.setEsealNumber(en);
        deviceDemand.setSensorNumber(sn);
        return deviceDemand;
    }

    public LsSystemDepartmentBO getSystemDepartmentBO() {
        return systemDepartmentBO;
    }

    public void setSystemDepartmentBO(LsSystemDepartmentBO systemDepartmentBO) {
        this.systemDepartmentBO = systemDepartmentBO;
    }
}
