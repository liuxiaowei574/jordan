package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceApplicationBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceDispatchBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDispatchDetailBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseEsealBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseSensorBO;
import com.nuctech.ls.model.dao.CommonVehicleDao;
import com.nuctech.ls.model.dao.DispatchDetailDao;
import com.nuctech.ls.model.dao.MonitorTripDao;
import com.nuctech.ls.model.dao.SystemDepartmentDao;
import com.nuctech.ls.model.dao.WarehouseDeviceApplicationDao;
import com.nuctech.ls.model.dao.WarehouseDeviceDispatchDao;
import com.nuctech.ls.model.dao.WarehouseElockDao;
import com.nuctech.ls.model.dao.WarehouseEsealDao;
import com.nuctech.ls.model.dao.WarehouseSensorDao;
import com.nuctech.ls.model.util.DeviceType;
import com.nuctech.ls.model.vo.report.DeviceInventoryDetail;
import com.nuctech.ls.model.vo.report.DeviceInventoryVO;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 库存报告 Service
 * </p>
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
    @Resource
    private CommonVehicleDao commonVehicleDao;
    @Resource
    private WarehouseElockDao warehouseElockDao;
    @Resource
    private WarehouseEsealDao warehouseEsealDao;
    @Resource
    private WarehouseSensorDao warehouseSensorDao;

    /**
     * 统计设备库存
     * 
     * @param portId
     *        口岸ID
     * @return
     * @throws Exception
     */
    public DeviceInventoryVO statisticsDeviceInventory(String portId, Date startDate, Date endDate) throws Exception {

        List<LsWarehouseElockBO> elockList = new ArrayList<LsWarehouseElockBO>();
        List<LsWarehouseEsealBO> esealList = new ArrayList<LsWarehouseEsealBO>();
        List<LsWarehouseSensorBO> sensorList = new ArrayList<LsWarehouseSensorBO>();

        if (NuctechUtil.isNull(portId)) {
            elockList = warehouseElockDao.getAllElock();// 当前所有(非在途)的关锁
            esealList = warehouseEsealDao.getAllEseal();// 当前所有(非在途)的子锁
            sensorList = warehouseSensorDao.getAllSensor();// 当前所有(非在途)的传感器
        } else {
            elockList = warehouseElockDao.findElockByOrgId(portId);// 当前属于口岸本身(非在途)的关锁
            esealList = warehouseEsealDao.findEsealByOrgId(portId);// 当前属于口岸本身(非在途)的子锁
            sensorList = warehouseSensorDao.findSensorByOrgId(portId);// 当前属于口岸本身(非在途)的传感器
        }

        DeviceInventoryVO deviceInventory = new DeviceInventoryVO();
        // 统计流入
        Map<String, Integer> flowInMap = monitorTripDao.findPortFlowDevices(portId, startDate, endDate,
                Constant.FLOW_IN);
        deviceInventory.setTrackDeviceFlowIn(flowInMap.get(DeviceType.TRACKING_DEVICE.getType()));
        deviceInventory.setEsealFlowIn(flowInMap.get(DeviceType.ESEAL.getType()));
        deviceInventory.setSensorFlowIn(flowInMap.get(DeviceType.SENSOR.getType()));
        // 统计流出
        Map<String, Integer> flowOutMap = monitorTripDao.findPortFlowDevices(portId, startDate, endDate,
                Constant.FLOW_OUT);
        deviceInventory.setTrackDeviceFlowOut(flowOutMap.get(DeviceType.TRACKING_DEVICE.getType()));
        deviceInventory.setEsealFlowOut(flowOutMap.get(DeviceType.ESEAL.getType()));
        deviceInventory.setSensorFlowOut(flowOutMap.get(DeviceType.SENSOR.getType()));
        // 统计转入
        Map<String, Integer> turnInMap = warehouseDeviceApplicationDao.findTurnInDevices(portId, startDate, endDate);
        deviceInventory.setTrackDeviceTurnIn(turnInMap.get(DeviceType.TRACKING_DEVICE.getType()));
        deviceInventory.setEsealTurnIn(turnInMap.get(DeviceType.ESEAL.getType()));
        deviceInventory.setSensorTurnIn(turnInMap.get(DeviceType.SENSOR.getType()));
        // 统计转出
        Map<String, Integer> turnOutMap = warehouseDeviceDispatchDao.findTurnOutDevices(portId, startDate, endDate);
        deviceInventory.setTrackDeviceTurnOut(turnOutMap.get(DeviceType.TRACKING_DEVICE.getType()));
        deviceInventory.setEsealTurnOut(turnOutMap.get(DeviceType.ESEAL.getType()));
        deviceInventory.setSensorTurnOut(turnOutMap.get(DeviceType.SENSOR.getType()));

        // //统计设备库存
        // elockTotal = elockList.size() + deviceInventory.getTrackDeviceFlowIn() +
        // deviceInventory.getTrackDeviceTurnIn()
        // - deviceInventory.getTrackDeviceFlowOut() - deviceInventory.getTrackDeviceTurnOut();
        // esealTotal = esealList.size() + deviceInventory.getEsealFlowIn() +
        // deviceInventory.getEsealTurnIn()
        // - deviceInventory.getEsealFlowOut() - deviceInventory.getEsealTurnOut();
        // sensorTotal = sensorList.size() + deviceInventory.getSensorFlowIn() +
        // deviceInventory.getSensorTurnIn()
        // - deviceInventory.getSensorFlowOut() - deviceInventory.getSensorTurnOut();
        // 统计设备库存
        long elockTotal = 0;// 关锁库存量
        long esealTotal = 0;// 子锁库存量
        long sensorTotal = 0; // 传感器库存量
        elockTotal = elockList.size();
        esealTotal = esealList.size();
        sensorTotal = sensorList.size();
        deviceInventory.setTrackDeviceInventory(elockTotal);
        deviceInventory.setEsealInventory(esealTotal);
        deviceInventory.setSensorInventory(sensorTotal);

        return deviceInventory;
    }

    /**
     * 关锁列表（流入、流出）
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public JSONArray findTrackDeviceFlowList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) throws Exception {
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
    @SuppressWarnings("rawtypes")
    public JSONArray findTrackDeviceTrunInList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) throws Exception {
        List<DeviceInventoryDetail> pageList = findTrackDeviceTrunInList2(pageQuery);
        return fromArrayList(pageList, jsonConfig, ignoreDefaultExcludes);
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
    public JSONArray findTrackDeviceTrunOutList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) throws Exception {
        List<DeviceInventoryDetail> pageList = findTrackDeviceTrunOutList2(pageQuery);
        return fromArrayList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 关锁库存
     * 
     * @param pageQuery
     * @param portId
     * @param startDate
     * @param endDate
     * @return
     */
    @SuppressWarnings("rawtypes")
    public JSONArray findTrackDeviceInventoryList(PageQuery<Map> pageQuery, String portId, Date startDate, Date endDate)
            throws Exception {
        List<DeviceInventoryDetail> pageList = findTrackDeviceInventoryList2(pageQuery, portId, startDate, endDate);
        return fromArrayList(pageList, null, false);
    }

    /**
     * 子锁列表（流入、流出）
     * 
     * @param pageQuery
     * @param type
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public JSONArray findEsealFlowList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes)
            throws Exception {
        List<DeviceInventoryDetail> pageList = findEsealFlowList2(pageQuery);
        return fromArrayList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 子锁转入
     * 
     * @param pageQuery
     * @param type
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public JSONArray findEsealTrunInList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes)
            throws Exception {
        List<DeviceInventoryDetail> pageList = findEsealTrunInList2(pageQuery);
        return fromArrayList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 子锁转出
     * 
     * @param pageQuery
     * @param type
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public JSONArray findEsealTrunOutList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) throws Exception {
        List<DeviceInventoryDetail> pageList = findEsealTrunOutList2(pageQuery);
        return fromArrayList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 子锁库存
     * 
     * @param pageQuery
     * @param portId
     * @param startDate
     * @param endDate
     * @return
     */
    @SuppressWarnings("rawtypes")
    public JSONArray findEsealInventoryList(PageQuery<Map> pageQuery, String portId, Date startDate, Date endDate)
            throws Exception {
        List<DeviceInventoryDetail> pageList = findEsealInventoryList2(pageQuery, portId, startDate, endDate);
        return fromArrayList(pageList, null, false);
    }

    /**
     * 传感器列表（流入、流出）
     * 
     * @param pageQuery
     * @param type
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public JSONArray findSensorFlowList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes)
            throws Exception {
        List<DeviceInventoryDetail> pageList = findSensorFlowList2(pageQuery);
        return fromArrayList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 传感器转入
     * 
     * @param pageQuery
     * @param type
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public JSONArray findSensorTrunInList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) throws Exception {
        List<DeviceInventoryDetail> pageList = findSensorTrunInList2(pageQuery);
        return fromArrayList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 传感器转出
     * 
     * @param pageQuery
     * @param type
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public JSONArray findSensorTrunOutList(PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) throws Exception {
        List<DeviceInventoryDetail> pageList = findSensorTrunOutList2(pageQuery);
        return fromArrayList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 传感器库存
     * 
     * @param pageQuery
     * @param portId
     * @param startDate
     * @param endDate
     * @return
     */
    @SuppressWarnings("rawtypes")
    public JSONArray findSensorInventoryList(PageQuery<Map> pageQuery, String portId, Date startDate, Date endDate)
            throws Exception {
        List<DeviceInventoryDetail> pageList = findSensorInventoryList2(pageQuery, portId, startDate, endDate);
        return fromArrayList(pageList, null, false);
    }

    /**
     * 查询设备列表
     * 
     * @param pageQuery
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes" })
    private List<DeviceInventoryDetail> findDeviceFlowList(PageQuery<Map> pageQuery) throws Exception {
        List<DeviceInventoryDetail> pageList = new ArrayList<DeviceInventoryDetail>();
        List<LsMonitorTripBO> monitorTripPageList = findMonitorTripList(pageQuery);
        if (monitorTripPageList != null && !monitorTripPageList.isEmpty()) {
            for (LsMonitorTripBO monitorTrip : monitorTripPageList) {
                List<LsCommonVehicleBO> commonVehicleBOs = new ArrayList<>();
                commonVehicleBOs = commonVehicleDao.findAllByTripId(monitorTrip.getTripId());
                if (commonVehicleBOs != null && commonVehicleBOs.size() > 0) {
                    // 每辆车一把关锁。
                    for (LsCommonVehicleBO commonVehicleBO : commonVehicleBOs) {
                        DeviceInventoryDetail deviceInventoryDetail = new DeviceInventoryDetail();
                        deviceInventoryDetail.setTrackDeviceNumber(commonVehicleBO.getTrackingDeviceNumber());
                        deviceInventoryDetail.setEsealNumber(commonVehicleBO.getEsealNumber());
                        deviceInventoryDetail.setSensorNumber(commonVehicleBO.getSensorNumber());
                        deviceInventoryDetail.setCheckInDate(monitorTrip.getCheckoutTime());
                        deviceInventoryDetail.setCheckOutDate(monitorTrip.getCheckinTime());
                        LsSystemDepartmentBO toPort = systemDepartmentDao.findById(monitorTrip.getCheckoutPort());
                        deviceInventoryDetail.setTo(toPort.getOrganizationId());
                        deviceInventoryDetail.setToName(toPort.getOrganizationName());
                        LsSystemDepartmentBO fromPort = systemDepartmentDao.findById(monitorTrip.getCheckinPort());
                        deviceInventoryDetail.setForm(fromPort.getOrganizationId());
                        deviceInventoryDetail.setFormName(fromPort.getOrganizationName());
                        pageList.add(deviceInventoryDetail);
                    }
                }
            }
        }
        return pageList;
    }

    /**
     * 关锁转入(2)
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<DeviceInventoryDetail> findTrackDeviceTrunInList2(PageQuery<Map> pageQuery) {
        List<DeviceInventoryDetail> trackDeviceList = new ArrayList<DeviceInventoryDetail>();
        List<LsWarehouseDeviceApplicationBO> deviceDispatchList = findWarehouseDeviceApplication(pageQuery);
        if (deviceDispatchList != null && !deviceDispatchList.isEmpty()) {
            for (LsWarehouseDeviceApplicationBO application : deviceDispatchList) {
                // 通过申请的ID查询记录的ID
                List<LsWarehouseDeviceDispatchBO> dispatchList = warehouseDeviceDispatchDao
                        .findWarehouseDeviceDispatchListByApplicationId(application.getApplicationId());
                if (dispatchList != null && !dispatchList.isEmpty()) {
                    for (LsWarehouseDeviceDispatchBO deviceDispatch : dispatchList) {
                        List<LsWarehouseDispatchDetailBO> trackDeviceDispatchDetailList = dispatchDetailDao
                                .findWarehouseDispatchDetailByDeviceDispatchIDAndType(deviceDispatch.getDispatchId(),
                                        DeviceType.TRACKING_DEVICE.getType());
                        if (trackDeviceDispatchDetailList != null && !trackDeviceDispatchDetailList.isEmpty()) {
                            for (LsWarehouseDispatchDetailBO detail : trackDeviceDispatchDetailList) {
                                DeviceInventoryDetail trackDeviceInventoryDetail = new DeviceInventoryDetail();
                                trackDeviceInventoryDetail.setCheckInDate(application.getFinishTime());
                                trackDeviceInventoryDetail.setTrackDeviceNumber(detail.getDeviceNumber());
                                LsSystemDepartmentBO fromPort = systemDepartmentDao
                                        .findById(deviceDispatch.getFromPort());
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
        return trackDeviceList;
    }

    /**
     * 关锁转出(2)
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<DeviceInventoryDetail> findTrackDeviceTrunOutList2(PageQuery<Map> pageQuery) {
        List<DeviceInventoryDetail> trackDeviceList = new ArrayList<DeviceInventoryDetail>();
        List<LsWarehouseDeviceDispatchBO> deviceDispatchList = findWarehouseDeviceDispatchList(pageQuery);
        if (deviceDispatchList != null && !deviceDispatchList.isEmpty()) {
            for (LsWarehouseDeviceDispatchBO dispatch : deviceDispatchList) {
                // 查询关锁
                String deviceDispatchID = dispatch.getDispatchId();
                List<LsWarehouseDispatchDetailBO> trackDeviceDispatchDetailList = dispatchDetailDao
                        .findWarehouseDispatchDetailByDeviceDispatchIDAndType(deviceDispatchID,
                                DeviceType.TRACKING_DEVICE.getType());
                if (trackDeviceDispatchDetailList != null && !trackDeviceDispatchDetailList.isEmpty()) {
                    for (LsWarehouseDispatchDetailBO detail : trackDeviceDispatchDetailList) {
                        DeviceInventoryDetail trackDeviceInventoryDetail = new DeviceInventoryDetail();
                        trackDeviceInventoryDetail.setCheckOutDate(dispatch.getDispatchTime());
                        trackDeviceInventoryDetail.setTrackDeviceNumber(detail.getDeviceNumber());
                        LsSystemDepartmentBO fromPort = systemDepartmentDao.findById(dispatch.getFromPort());
                        trackDeviceInventoryDetail.setForm(fromPort.getOrganizationId());
                        trackDeviceInventoryDetail.setFormName(fromPort.getOrganizationName());
                        LsSystemDepartmentBO toPort = systemDepartmentDao.findById(dispatch.getToPort());
                        trackDeviceInventoryDetail.setTo(toPort.getOrganizationId());
                        trackDeviceInventoryDetail.setToName(toPort.getOrganizationName());
                        trackDeviceList.add(trackDeviceInventoryDetail);
                    }
                }
            }
        }
        return trackDeviceList;
    }

    /**
     * 关锁库存(2)
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public List<DeviceInventoryDetail> findTrackDeviceInventoryList2(PageQuery<Map> pageQuery, String portId,
            Date startDate, Date endDate) throws Exception {
        List<DeviceInventoryDetail> pageList = new ArrayList<DeviceInventoryDetail>();
        List<LsWarehouseElockBO> elockList = new ArrayList<LsWarehouseElockBO>();

        if (NuctechUtil.isNotNull(portId)) {// 当前口岸本身库存列表
            elockList = warehouseElockDao.findElockByOrgId(portId);// 当前属于口岸本身(非在途)的关锁
        } else {
            elockList = warehouseElockDao.getAllElock();// 当前所有口岸(非在途)的关锁
        }

        // //流入口岸的关锁列表
        // Map<String, Object> flowInfilters = new HashMap<String, Object>();
        // if(NuctechUtil.isNotNull(portId)){
        // flowInfilters.put("checkoutPort", portId);
        // }
        // if(NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)){
        // flowInfilters.put("checkoutStartTime", DateUtils.date2String(startDate));
        // flowInfilters.put("checkoutEndTime", DateUtils.date2String(endDate));
        // }
        // pageQuery.setFilters(flowInfilters);
        // List<DeviceInventoryDetail> flowInFList = findDeviceFlowList(pageQuery);
        //
        // //转入口岸的关锁列表
        // Map<String, Object> turnInfilters = new HashMap<String, Object>();
        // if(NuctechUtil.isNotNull(portId)){
        // turnInfilters.put("applcationPort", portId);
        // }
        // if(NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)){
        // turnInfilters.put("finishStartTime", DateUtils.date2String(startDate));
        // turnInfilters.put("finishEndTime", DateUtils.date2String(endDate));
        // }
        // pageQuery.setFilters(turnInfilters);
        // List<DeviceInventoryDetail> turnInList = findTrackDeviceTrunInList2(pageQuery);
        //
        // //流出口岸的关锁列表
        // Map<String, Object> flowOutfilters = new HashMap<String, Object>();
        // if(NuctechUtil.isNotNull(portId)){
        // flowOutfilters.put("checkinPort", portId);
        // }
        // if(NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)){
        // flowOutfilters.put("checkinStartTime", DateUtils.date2String(startDate));
        // flowOutfilters.put("checkinEndTime", DateUtils.date2String(endDate));
        // }
        // pageQuery.setFilters(flowOutfilters);
        // List<DeviceInventoryDetail> flowOutList = findDeviceFlowList(pageQuery);
        //
        // //转出口岸的关锁列表
        // Map<String, Object> turnOutfilters = new HashMap<String, Object>();
        // if(NuctechUtil.isNotNull(portId)){
        // turnOutfilters.put("fromPort", portId);
        // }
        // if(NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)){
        // turnOutfilters.put("dispatchStartTime", DateUtils.date2String(startDate));
        // turnOutfilters.put("dispatchEndTime", DateUtils.date2String(endDate));
        // }
        // pageQuery.setFilters(turnOutfilters);
        // List<DeviceInventoryDetail> turnOutList = findTrackDeviceTrunOutList2(pageQuery);
        //
        // 添加口岸本身关锁到集合
        for (LsWarehouseElockBO elockBO : elockList) {
            DeviceInventoryDetail deviceInventory = new DeviceInventoryDetail();
            deviceInventory.setTrackDeviceNumber(elockBO.getElockNumber());
            deviceInventory.setTrackDeviceStatus(elockBO.getElockStatus());
            pageList.add(deviceInventory);
        }
        //
        // //添加流入口岸的关锁列表到集合
        // for (DeviceInventoryDetail e : flowInFList) {
        // pageList.add(e);
        // }
        //
        // //添加转入口岸的关锁列表到集合
        // for (DeviceInventoryDetail e : turnInList) {
        // pageList.add(e);
        // }
        //
        // //从集合中剔除流出口岸的关锁
        // for (int i = 0; i < pageList.size(); i++) {
        // DeviceInventoryDetail e = pageList.get(i);
        // for (int j = 0; j < flowOutList.size(); j++) {
        // if(flowOutList.get(j).getTrackDeviceNumber().equals(e.getTrackDeviceNumber())){
        // pageList.remove(e);
        // flowOutList.remove(flowOutList.get(j));
        // i--;
        // break;
        // }
        // }
        // }
        //
        //
        // //从集合中剔除转出口岸的关锁
        // for (int i = 0; i < pageList.size(); i++) {
        // DeviceInventoryDetail e = pageList.get(i);
        // for (int j = 0; j < turnOutList.size(); j++) {
        // if(turnOutList.get(j).getTrackDeviceNumber().equals(e.getTrackDeviceNumber())){
        // pageList.remove(e);
        // turnOutList.remove(turnOutList.get(j));
        // i--;
        // break;
        // }
        // }
        // }

        return pageList;
    }

    /**
     * 子锁流入、流出（2）
     * 
     * @param pageQuery
     * @param type
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public List<DeviceInventoryDetail> findEsealFlowList2(PageQuery<Map> pageQuery) throws Exception {
        List<DeviceInventoryDetail> pageList = new ArrayList<DeviceInventoryDetail>();
        List<DeviceInventoryDetail> devicePageList = findDeviceFlowList(pageQuery);
        if (devicePageList != null && !devicePageList.isEmpty()) {
            for (DeviceInventoryDetail detail : devicePageList) {
                String strEsealNumber = detail.getEsealNumber();
                if (NuctechUtil.isNotNull(strEsealNumber)) {// 子锁号不为空（即车辆上至少有一把子锁）
                    if (strEsealNumber.indexOf(",") != -1) {
                        String[] arrEsealNumber = strEsealNumber.split(",");
                        for (String esealNumber : arrEsealNumber) {
                            DeviceInventoryDetail deviceInventoryDetail = new DeviceInventoryDetail();
                            BeanUtils.copyProperties(detail, deviceInventoryDetail, new String[] { "esealNumber" });
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
        }
        return pageList;
    }

    /**
     * 子锁转入（2）
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<DeviceInventoryDetail> findEsealTrunInList2(PageQuery<Map> pageQuery) {
        List<DeviceInventoryDetail> list = new ArrayList<DeviceInventoryDetail>();
        List<LsWarehouseDeviceApplicationBO> deviceDispatchList = findWarehouseDeviceApplication(pageQuery);
        if (deviceDispatchList != null && !deviceDispatchList.isEmpty()) {
            for (LsWarehouseDeviceApplicationBO application : deviceDispatchList) {
                // 通过申请的ID查询记录的ID
                List<LsWarehouseDeviceDispatchBO> dispatchList = warehouseDeviceDispatchDao
                        .findWarehouseDeviceDispatchListByApplicationId(application.getApplicationId());
                if (dispatchList != null && !dispatchList.isEmpty()) {
                    for (LsWarehouseDeviceDispatchBO deviceDispatch : dispatchList) {
                        List<LsWarehouseDispatchDetailBO> dispatchDetailList = dispatchDetailDao
                                .findWarehouseDispatchDetailByDeviceDispatchIDAndType(deviceDispatch.getDispatchId(),
                                        DeviceType.ESEAL.getType());
                        if (dispatchDetailList != null && !dispatchDetailList.isEmpty()) {
                            for (LsWarehouseDispatchDetailBO detail : dispatchDetailList) {
                                DeviceInventoryDetail inventoryDetail = new DeviceInventoryDetail();
                                inventoryDetail.setCheckInDate(application.getFinishTime());// 转入时间
                                inventoryDetail.setEsealNumber(detail.getDeviceNumber());
                                LsSystemDepartmentBO fromPort = systemDepartmentDao
                                        .findById(deviceDispatch.getFromPort());
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
        return list;
    }

    /**
     * 子锁转出(2)
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<DeviceInventoryDetail> findEsealTrunOutList2(PageQuery<Map> pageQuery) {
        List<DeviceInventoryDetail> trackDeviceList = new ArrayList<DeviceInventoryDetail>();
        List<LsWarehouseDeviceDispatchBO> deviceDispatchList = findWarehouseDeviceDispatchList(pageQuery);
        if (deviceDispatchList != null && !deviceDispatchList.isEmpty()) {
            for (LsWarehouseDeviceDispatchBO dispatch : deviceDispatchList) {
                // 查询子锁
                String deviceDispatchID = dispatch.getDispatchId();
                List<LsWarehouseDispatchDetailBO> trackDeviceDispatchDetailList = dispatchDetailDao
                        .findWarehouseDispatchDetailByDeviceDispatchIDAndType(deviceDispatchID,
                                DeviceType.ESEAL.getType());
                if (trackDeviceDispatchDetailList != null && !trackDeviceDispatchDetailList.isEmpty()) {
                    for (LsWarehouseDispatchDetailBO detail : trackDeviceDispatchDetailList) {
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
        return trackDeviceList;
    }

    /**
     * 子锁库存
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public List<DeviceInventoryDetail> findEsealInventoryList2(PageQuery<Map> pageQuery, String portId, Date startDate,
            Date endDate) throws Exception {

        List<DeviceInventoryDetail> pageList = new ArrayList<DeviceInventoryDetail>();

        List<LsWarehouseEsealBO> esealList = new ArrayList<LsWarehouseEsealBO>();
        if (NuctechUtil.isNotNull(portId)) {// 当前口岸本身库存列表
            esealList = warehouseEsealDao.findEsealByOrgId(portId);// 当前属于口岸本身的关锁
        } else {
            esealList = warehouseEsealDao.getAllEseal();
        }

        // //流入口岸的子锁列表
        // Map<String, Object> flowInfilters = new HashMap<String, Object>();
        // if(NuctechUtil.isNotNull(portId)){
        // flowInfilters.put("checkoutPort", portId);
        // }
        // if(NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)){
        // flowInfilters.put("checkoutStartTime", DateUtils.date2String(startDate));
        // flowInfilters.put("checkoutEndTime", DateUtils.date2String(endDate));
        // }
        // pageQuery.setFilters(flowInfilters);
        // List<DeviceInventoryDetail> flowInFList = findEsealFlowList2(pageQuery);
        //
        // //转入口岸的子锁列表
        // Map<String, Object> turnInfilters = new HashMap<String, Object>();
        // if(NuctechUtil.isNotNull(portId)){
        // turnInfilters.put("applcationPort", portId);
        // }
        // if(NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)){
        // turnInfilters.put("finishStartTime", DateUtils.date2String(startDate));
        // turnInfilters.put("finishEndTime", DateUtils.date2String(endDate));
        // }
        // pageQuery.setFilters(turnInfilters);
        // List<DeviceInventoryDetail> turnInList = findEsealTrunInList2(pageQuery);
        //
        // //流出口岸的子锁列表
        // Map<String, Object> flowOutfilters = new HashMap<String, Object>();
        // if(NuctechUtil.isNotNull(portId)){
        // flowOutfilters.put("checkinPort", portId);
        // }
        // if(NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)){
        // flowOutfilters.put("checkinStartTime", DateUtils.date2String(startDate));
        // flowOutfilters.put("checkinEndTime", DateUtils.date2String(endDate));
        // }
        // pageQuery.setFilters(flowOutfilters);
        // List<DeviceInventoryDetail> flowOutList = findEsealFlowList2(pageQuery);
        //
        // //转出口岸的子锁列表
        // Map<String, Object> turnOutfilters = new HashMap<String, Object>();
        // if(NuctechUtil.isNotNull(portId)){
        // turnOutfilters.put("fromPort", portId);
        // }
        // if(NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)){
        // turnOutfilters.put("dispatchStartTime", DateUtils.date2String(startDate));
        // turnOutfilters.put("dispatchEndTime", DateUtils.date2String(endDate));
        // }
        // pageQuery.setFilters(turnOutfilters);
        // List<DeviceInventoryDetail> turnOutList = findEsealTrunOutList2(pageQuery);
        //
        // 添加口岸本身关锁到集合
        for (LsWarehouseEsealBO esealBO : esealList) {
            DeviceInventoryDetail deviceInventory = new DeviceInventoryDetail();
            deviceInventory.setEsealNumber(esealBO.getEsealNumber());
            deviceInventory.setEsealStatus(esealBO.getEsealStatus());
            pageList.add(deviceInventory);
        }
        //
        // //添加流入口岸的关锁列表到集合
        // for (DeviceInventoryDetail e : flowInFList) {
        // pageList.add(e);
        // }
        //
        // //添加转入口岸的关锁列表到集合
        // for (DeviceInventoryDetail e : turnInList) {
        // pageList.add(e);
        // }
        //
        // //从集合中剔除流出口岸的关锁
        // for (int i = 0; i < pageList.size(); i++) {
        // DeviceInventoryDetail e = pageList.get(i);
        // for (int j = 0; j < flowOutList.size(); j++) {
        // if(flowOutList.get(j).getEsealNumber().equals(e.getEsealNumber())){
        // pageList.remove(e);
        // i--;
        // break;
        // }
        // }
        // }
        //
        // //从集合中剔除转出口岸的关锁
        // for (int i = 0; i < pageList.size(); i++) {
        // DeviceInventoryDetail e = pageList.get(i);
        // for (int j = 0; j < turnOutList.size(); j++) {
        // if(turnOutList.get(j).getEsealNumber().equals(e.getEsealNumber())){
        // pageList.remove(e);
        // i--;
        // break;
        // }
        // }
        // }
        //
        return pageList;

    }

    /**
     * 传感器列表(2 流入、流出)
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public List<DeviceInventoryDetail> findSensorFlowList2(PageQuery<Map> pageQuery) throws Exception {
        List<DeviceInventoryDetail> pageList = new ArrayList<DeviceInventoryDetail>();
        List<DeviceInventoryDetail> devicePageList = findDeviceFlowList(pageQuery);
        if (devicePageList != null && !devicePageList.isEmpty()) {
            for (DeviceInventoryDetail detail : devicePageList) {
                String strSeneorNumber = detail.getSensorNumber();
                if (NuctechUtil.isNotNull(strSeneorNumber)) {// 传感器号不为空（即车辆上至少有一把子锁）
                    if (strSeneorNumber.indexOf(",") != -1) {
                        String[] arrSensorNumber = strSeneorNumber.split(",");
                        for (String sensorNumber : arrSensorNumber) {
                            DeviceInventoryDetail deviceInventoryDetail = new DeviceInventoryDetail();
                            BeanUtils.copyProperties(detail, deviceInventoryDetail, new String[] { "sensorNumber" });
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
        }
        return pageList;
    }

    /**
     * 传感器转入(2)
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<DeviceInventoryDetail> findSensorTrunInList2(PageQuery<Map> pageQuery) {
        List<DeviceInventoryDetail> list = new ArrayList<DeviceInventoryDetail>();
        List<LsWarehouseDeviceApplicationBO> deviceDispatchList = findWarehouseDeviceApplication(pageQuery);
        if (deviceDispatchList != null && !deviceDispatchList.isEmpty()) {
            for (LsWarehouseDeviceApplicationBO application : deviceDispatchList) {
                // 通过申请的ID查询记录的ID
                List<LsWarehouseDeviceDispatchBO> dispatchList = warehouseDeviceDispatchDao
                        .findWarehouseDeviceDispatchListByApplicationId(application.getApplicationId());
                if (dispatchList != null && !dispatchList.isEmpty()) {
                    for (LsWarehouseDeviceDispatchBO deviceDispatch : dispatchList) {
                        List<LsWarehouseDispatchDetailBO> dispatchDetailList = dispatchDetailDao
                                .findWarehouseDispatchDetailByDeviceDispatchIDAndType(deviceDispatch.getDispatchId(),
                                        DeviceType.SENSOR.getType());
                        if (dispatchDetailList != null && !dispatchDetailList.isEmpty()) {
                            for (LsWarehouseDispatchDetailBO detail : dispatchDetailList) {
                                DeviceInventoryDetail inventoryDetail = new DeviceInventoryDetail();
                                inventoryDetail.setCheckInDate(application.getFinishTime());
                                inventoryDetail.setSensorNumber(detail.getDeviceNumber());
                                LsSystemDepartmentBO fromPort = systemDepartmentDao
                                        .findById(deviceDispatch.getFromPort());
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
        return list;
    }

    /**
     * 传感器转出(2)
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<DeviceInventoryDetail> findSensorTrunOutList2(PageQuery<Map> pageQuery) {
        List<DeviceInventoryDetail> trackDeviceList = new ArrayList<DeviceInventoryDetail>();
        List<LsWarehouseDeviceDispatchBO> deviceDispatchList = findWarehouseDeviceDispatchList(pageQuery);
        if (deviceDispatchList != null && !deviceDispatchList.isEmpty()) {
            for (LsWarehouseDeviceDispatchBO dispatch : deviceDispatchList) {
                // 查询关锁
                String deviceDispatchID = dispatch.getDispatchId();
                List<LsWarehouseDispatchDetailBO> trackDeviceDispatchDetailList = dispatchDetailDao
                        .findWarehouseDispatchDetailByDeviceDispatchIDAndType(deviceDispatchID,
                                DeviceType.SENSOR.getType());
                if (trackDeviceDispatchDetailList != null && !trackDeviceDispatchDetailList.isEmpty()) {
                    for (LsWarehouseDispatchDetailBO detail : trackDeviceDispatchDetailList) {
                        DeviceInventoryDetail sensorInventoryDetail = new DeviceInventoryDetail();
                        sensorInventoryDetail.setCheckOutDate(dispatch.getDispatchTime());
                        sensorInventoryDetail.setSensorNumber(detail.getDeviceNumber());
                        LsSystemDepartmentBO fromPort = systemDepartmentDao.findById(dispatch.getFromPort());
                        sensorInventoryDetail.setForm(fromPort.getOrganizationId());
                        sensorInventoryDetail.setFormName(fromPort.getOrganizationName());
                        LsSystemDepartmentBO toPort = systemDepartmentDao.findById(dispatch.getToPort());
                        sensorInventoryDetail.setTo(toPort.getOrganizationId());
                        sensorInventoryDetail.setToName(toPort.getOrganizationName());
                        trackDeviceList.add(sensorInventoryDetail);
                    }
                }
            }
        }
        return trackDeviceList;
    }

    /**
     * 传感器库存（2）
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public List<DeviceInventoryDetail> findSensorInventoryList2(PageQuery<Map> pageQuery, String portId, Date startDate,
            Date endDate) throws Exception {
        List<DeviceInventoryDetail> pageList = new ArrayList<DeviceInventoryDetail>();

        List<LsWarehouseSensorBO> sensorList = new ArrayList<LsWarehouseSensorBO>();
        if (NuctechUtil.isNotNull(portId)) {// 当前口岸本身库存列表
            sensorList = warehouseSensorDao.findSensorByOrgId(portId);// 当前属于口岸本身的关锁
        } else {
            sensorList = warehouseSensorDao.getAllSensor();
        }

        // //流入口岸的传感器列表
        // Map<String, Object> flowInfilters = new HashMap<String, Object>();
        // if(NuctechUtil.isNotNull(portId)){
        // flowInfilters.put("checkoutPort", portId);
        // }
        // if(NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)){
        // flowInfilters.put("checkoutStartTime", DateUtils.date2String(startDate));
        // flowInfilters.put("checkoutEndTime", DateUtils.date2String(endDate));
        // }
        // pageQuery.setFilters(flowInfilters);
        // List<DeviceInventoryDetail> flowInFList = findSensorFlowList2(pageQuery);
        //
        // //转入口岸的传感器列表
        // Map<String, Object> turnInfilters = new HashMap<String, Object>();
        // if(NuctechUtil.isNotNull(portId)){
        // turnInfilters.put("applcationPort", portId);
        // }
        // if(NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)){
        // turnInfilters.put("finishStartTime", DateUtils.date2String(startDate));
        // turnInfilters.put("finishEndTime", DateUtils.date2String(endDate));
        // }
        // pageQuery.setFilters(turnInfilters);
        // List<DeviceInventoryDetail> turnInList = findSensorTrunInList2(pageQuery);
        //
        // //流出口岸的传感器列表
        // Map<String, Object> flowOutfilters = new HashMap<String, Object>();
        // if(NuctechUtil.isNotNull(portId)){
        // flowOutfilters.put("checkinPort", portId);
        // }
        // if(NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)){
        // flowOutfilters.put("checkinStartTime", DateUtils.date2String(startDate));
        // flowOutfilters.put("checkinEndTime", DateUtils.date2String(endDate));
        // }
        // pageQuery.setFilters(flowOutfilters);
        // List<DeviceInventoryDetail> flowOutList = findSensorFlowList2(pageQuery);
        //
        // //转出口岸的传感器列表
        // Map<String, Object> turnOutfilters = new HashMap<String, Object>();
        // if(NuctechUtil.isNotNull(portId)){
        // turnOutfilters.put("fromPort", portId);
        // }
        // if(NuctechUtil.isNotNull(startDate) && NuctechUtil.isNotNull(endDate)){
        // turnOutfilters.put("dispatchStartTime", DateUtils.date2String(startDate));
        // turnOutfilters.put("dispatchEndTime", DateUtils.date2String(endDate));
        // }
        // pageQuery.setFilters(turnOutfilters);
        // List<DeviceInventoryDetail> turnOutList = findSensorTrunOutList2(pageQuery);
        //
        // 添加口岸本身传感器到集合
        for (LsWarehouseSensorBO sensorBO : sensorList) {
            DeviceInventoryDetail deviceInventory = new DeviceInventoryDetail();
            deviceInventory.setSensorNumber(sensorBO.getSensorNumber());
            deviceInventory.setSensorStatus(sensorBO.getSensorStatus());
            pageList.add(deviceInventory);
        }
        //
        // //添加流入口岸的传感器列表到集合
        // for (DeviceInventoryDetail e : flowInFList) {
        // pageList.add(e);
        // }
        //
        // //添加转入口岸的传感器列表到集合
        // for (DeviceInventoryDetail e : turnInList) {
        // pageList.add(e);
        // }
        //
        // //从集合中剔除流出口岸的传感器
        // for (int i = 0; i < pageList.size(); i++) {
        // DeviceInventoryDetail e = pageList.get(i);
        // for (int j = 0; j < flowOutList.size(); j++) {
        // if(flowOutList.get(j).getSensorNumber().equals(e.getSensorNumber())){
        // pageList.remove(e);
        // i--;
        // break;
        // }
        // }
        // }
        //
        // //从集合中剔除转出口岸的传感器
        // for (int i = 0; i < pageList.size(); i++) {
        // DeviceInventoryDetail e = pageList.get(i);
        // for (int j = 0; j < turnOutList.size(); j++) {
        // if(turnOutList.get(j).getSensorNumber().equals(e.getSensorNumber())){
        // pageList.remove(e);
        // i--;
        // break;
        // }
        // }
        // }

        return pageList;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private List<LsMonitorTripBO> findMonitorTripList(PageQuery<Map> pageQuery) {
        String queryString = "select t from LsMonitorTripBO t where 1=1 " + "/~ and t.tripId = '[tripId]' ~/"
                + "/~ and t.checkinPort = '[checkinPort]' ~/" + "/~ and t.checkinTime >= '[checkinStartTime]' ~/"
                + "/~ and t.checkinTime <= '[checkinEndTime]' ~/" + "/~ and t.checkoutPort = '[checkoutPort]' ~/"
                + "/~ and t.checkoutTime >= '[checkoutStartTime]' ~/"
                + "/~ and t.checkoutTime <= '[checkoutEndTime]' ~/" + "/~ order by [sortColumns] ~/";
        List<LsMonitorTripBO> monitorTripPageList = monitorTripDao.findAllList(queryString, pageQuery);
        return monitorTripPageList;
    }

    /**
     * 查询转入口岸数据
     * 
     * @param pageQuery
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private List<LsWarehouseDeviceApplicationBO> findWarehouseDeviceApplication(PageQuery<Map> pageQuery) {
        String queryString = "select t from LsWarehouseDeviceApplicationBO t where 1=1 "
                + "/~ and t.applicationId = '[applicationId]' ~/"// id
                + "/~ and t.applcationPort = '[applcationPort]' ~/" // 转入节点
                + "/~ and t.finishTime >= '[finishStartTime]' ~/" // 转入时间
                + "/~ and t.finishTime <= '[finishEndTime]' ~/" + "/~ order by [sortColumns] ~/";
        List<LsWarehouseDeviceApplicationBO> warehouseDeviceApplicationList = warehouseDeviceApplicationDao
                .findAllList(queryString, pageQuery);
        return warehouseDeviceApplicationList;
    }

    /**
     * 查询转出口岸数据
     * 
     * @param pageQuery
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<LsWarehouseDeviceDispatchBO> findWarehouseDeviceDispatchList(PageQuery<Map> pageQuery) {
        String queryString = "select t from LsWarehouseDeviceDispatchBO t where 1=1 "
                + "/~ and t.dispatchId = '[dispatchId]' ~/" + "/~ and t.fromPort = '[fromPort]' ~/" // 转出节点
                + "/~ and t.dispatchTime >= '[dispatchStartTime]' ~/" // 转出时间
                + "/~ and t.dispatchTime <= '[dispatchEndTime]' ~/" + "/~ order by [sortColumns] ~/";
        List<LsWarehouseDeviceDispatchBO> warehouseDeviceDispatchList = warehouseDeviceDispatchDao
                .findAllList(queryString, pageQuery);
        return warehouseDeviceDispatchList;
    }
}
