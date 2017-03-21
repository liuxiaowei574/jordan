package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.common.LsCommonDriverBO;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.dao.CommonDriverDao;
import com.nuctech.ls.model.dao.CommonVehicleDao;
import com.nuctech.ls.model.dao.SystemUserDao;
import com.nuctech.ls.model.dao.WarehouseElockDao;
import com.nuctech.ls.model.dao.WarehouseEsealDao;
import com.nuctech.ls.model.dao.WarehouseSensorDao;
import com.nuctech.ls.model.dao.WarehouseTrackUnitDao;
import com.nuctech.ls.model.vo.monitor.CommonVehicleDriverVO;
import com.nuctech.util.NuctechUtil;

/**
 * 车辆管理Service
 * 
 * @author liushaowei
 *
 */
@Service
@Transactional
public class CommonVehicleService extends LSBaseService {

    @Resource
    private CommonVehicleDao commonVehicleDao;
    @Resource
    private WarehouseElockDao warehouseElockDao;
    @Resource
    private WarehouseEsealDao warehouseEsealDao;
    @Resource
    private WarehouseSensorDao warehouseSensorDao;
    @Resource
    private WarehouseTrackUnitDao warehouseTrackUnitDao;
    @Resource
    private SystemUserDao systemUserDao;
    @Resource
    private CommonDriverDao commonDriverDao;

    /**
     * 新增车辆信息
     * 
     * @param commonVehicleBO
     */
    public void addCommonVehicle(LsCommonVehicleBO commonVehicleBO) {
        commonVehicleDao.addCommonVehicle(commonVehicleBO);
    }

    /**
     * 根据Id查找车辆信息
     * 
     * @param id
     * @return
     */
    public LsCommonVehicleBO findById(String id) {
        return commonVehicleDao.findById(id);
    }

    /**
     * 更新车辆信息
     * 
     * @param lsCommonVehicleBO
     */
    public void updateCommonVehicle(LsCommonVehicleBO lsCommonVehicleBO) {
        commonVehicleDao.updateCommonVehicle(lsCommonVehicleBO);
    }

    /**
     * 持久化车辆信息
     * 
     * @param lsCommonVehicleBO
     */
    public void merge(LsCommonVehicleBO lsCommonVehicleBO) {
        commonVehicleDao.merge(lsCommonVehicleBO);
    }

    /**
     * 赵苏阳
     */
    // 查找所有被监管的车辆
    public int findAllCommonVehicleBOCount() {
        return commonVehicleDao.findCount();
    }

    /**
     * 查找监管车辆所在国家返回list
     */
    @SuppressWarnings("rawtypes")
    public List findCommonVehicleList() {
        return commonVehicleDao.findVehicleList();
    }

    /**
     * 查找每个国家对应的车辆数
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findCountryNamCount() {
        return commonVehicleDao.findCountryNamCountList();
    }

    /**
     * 统计运输司机的数量
     * 
     * @return
     */
    public int findAllDriver() {
        return commonVehicleDao.findDriverCount();
    }

    /**
     * 统计运输司机国籍
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List finddriverCountryNameList() {
        return commonVehicleDao.findDriverCountry();
    }

    /**
     * 统计司机国家对应的数量
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List driverCountryCount() {
        return commonVehicleDao.driverCountryCount();
    }

    /**
     * 统计正在使用的设备的数量
     */
    public int findDeviceInUseCount() {
        int elockCount = warehouseElockDao.findElockCountInUse();// 正在使用的关锁的数量
        int esealCount = warehouseEsealDao.findEsealCountInUse();// 正在使用的子锁的数量
        int sensorCount = warehouseSensorDao.findElockCountInUse();// 正在使用的传感器数量
        int trackUnit = warehouseTrackUnitDao.findTrackCountInUse();// 正在使用的车载台的数量
        return (elockCount + esealCount + sensorCount + trackUnit);
    }

    /**
     * 统计用户的数量
     */
    public int findUsersCount() {
        return (systemUserDao.findUserNumber());
    }

    /**
     * 查询具体用户账号List
     */
    @SuppressWarnings("rawtypes")
    public List findUserAccountList() {
        return (systemUserDao.findUserNameList());
    }

    /**
     * 查询用户在线时长
     */
    @SuppressWarnings("rawtypes")
    public List findTime() {
        return (systemUserDao.findUserOnLineList());
    }

    /**
     * 根据tripId获取指定的车辆信息
     * 
     * @param tripId
     * @return
     * @throws Exception
     */
    public List<LsCommonVehicleBO> findAllByTripId(String tripId) {
        return commonVehicleDao.findAllByTripId(tripId);
    }

    /**
     * 根据车牌号和tripId查找车辆信息
     * 
     * @param id
     * @return
     */
    public LsCommonVehicleBO findByVehiclePlateNumber(String vehiclePlateNumber, String tripId) {
        HashMap<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("vehiclePlateNumber", vehiclePlateNumber);
        propertiesMap.put("tripId", tripId);
        return commonVehicleDao.findByProperties(propertiesMap, null);
    }

    /**
     * 根据tripId获取指定的车辆信息
     * 
     * @param tripId
     * @return
     * @throws Exception
     */
    public List<CommonVehicleDriverVO> findAllVehicleDriverByTripId(String tripId) throws Exception {
        List<LsCommonVehicleBO> commonVehicleBOList = commonVehicleDao.findAllByTripId(tripId);
        List<CommonVehicleDriverVO> commonVehicleDriverVOList = new ArrayList<>();
        if (null != commonVehicleBOList && commonVehicleBOList.size() > 0) {
            for (LsCommonVehicleBO commonVehicleBO : commonVehicleBOList) {
                CommonVehicleDriverVO commonVehicleDriverBO = new CommonVehicleDriverVO();
                BeanUtils.copyProperties(commonVehicleBO, commonVehicleDriverBO);
                LsCommonDriverBO commonDriverBO = commonDriverDao.findById(commonVehicleBO.getDriverId());
                BeanUtils.copyProperties(commonDriverBO, commonVehicleDriverBO);
                commonVehicleDriverVOList.add(commonVehicleDriverBO);
            }
        }
        return commonVehicleDriverVOList;
    }

    /**
     * 根据tripId获取指定的车辆信息
     * 
     * @param tripId
     * @return
     * @throws Exception
     */
    public List<LsCommonVehicleBO> findAllByTrackingDeviceNum(String trackingDeviceNumber) throws Exception {
        return commonVehicleDao.findAllByTripId(trackingDeviceNumber);
    }

    /**
     * 根据tripId和其他查询条件获取指定的车辆信息
     * 
     * @return
     * @throws Exception
     */
    public LsCommonVehicleBO findCommonVehicleBo(String tripId, String trackingDeviceNumber,
            String vehiclePlateNumber) {
        HashMap<String, Object> propertiesMap = new HashMap<>();
        if (NuctechUtil.isNotNull(trackingDeviceNumber)) {
            propertiesMap.put("trackingDeviceNumber", trackingDeviceNumber);
        }
        if (NuctechUtil.isNotNull(vehiclePlateNumber)) {
            propertiesMap.put("vehiclePlateNumber", vehiclePlateNumber);
        }
        propertiesMap.put("tripId", tripId);
        return commonVehicleDao.findByProperties(propertiesMap, null);
    }

    public void delete(String[] ids) {
        String hql = "delete LsCommonVehicleBO where vehicleId in :ids";
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("ids", ids);
        commonVehicleDao.batchUpdateOrDeleteByHql(hql, propertiesMap);
    }

    public LsCommonVehicleBO findLatestCommonVehicleBo(String tripId, String trackingDeviceNumber) {
        return commonVehicleDao.findLatestCommonVehicleBo(tripId, trackingDeviceNumber);
    }

    public List<LsCommonVehicleBO> findAllCommonVehicle() {
        return commonVehicleDao.findAll();
    }

    /**
     * 批量删除
     * 
     * @param tripId
     */
    public void deleteByTripId(String tripId) {
        String hql = "delete LsCommonVehicleBO where tripId = :tripId ";
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("tripId", tripId);
        commonVehicleDao.batchUpdateOrDeleteByHql(hql, propertiesMap);
    }

}
