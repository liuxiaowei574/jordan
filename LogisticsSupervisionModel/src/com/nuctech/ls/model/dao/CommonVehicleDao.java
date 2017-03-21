package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;

/**
 * 监管车辆信息Dao
 * 
 * @author liushaowei
 *
 */
@Repository
public class CommonVehicleDao extends LSBaseDao<LsCommonVehicleBO, Serializable> {

    /**
     * 新增车辆信息
     * 
     * @param lsCommonVehicleBO
     */
    public void addCommonVehicle(LsCommonVehicleBO lsCommonVehicleBO) {
        persist(lsCommonVehicleBO);
    }

    /**
     * 更新车辆信息
     * 
     * @param lsCommonVehicleBO
     */
    public void updateCommonVehicle(LsCommonVehicleBO lsCommonVehicleBO) {
        update(lsCommonVehicleBO);
    }

    /**
     * 赵苏阳
     */
    @SuppressWarnings("unused")
    private ArrayList<Object> vehicle = new ArrayList<>();

    // 计算正在被监管的车辆的数量
    public int findCount() {
        Session session = this.getSession();
        String querySql = "SELECT v.*, d.*, t.* FROM LS_COMMON_VEHICLE v, LS_SYSTEM_DEPARTMENT d, LS_MONITOR_TRIP t WHERE v.TRIP_ID = t.TRIP_ID AND d.ORGANIZATION_ID = t.CHECKIN_PORT AND v.VEHICLE_PLATE_NUMBER IS NOT NULL";
        @SuppressWarnings("rawtypes")
        java.util.List vehicle = null;
        int count = 0;
        vehicle = session.createSQLQuery(querySql).addEntity(LsCommonVehicleBO.class).list();
        count = vehicle.size();
        return count;

    }

    // 查询监管车辆属于那些国家
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<LsCommonVehicleBO> findVehicleList() {
        Session session = this.getSession();
        String sql = "SELECT v.VEHICLE_COUNTRY   from LS_COMMON_VEHICLE v GROUP BY VEHICLE_COUNTRY";
        List countryNameList = null;
        countryNameList = session.createSQLQuery(sql).list();
        return countryNameList;
    }

    // 查询每个国家对应的车辆数
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<LsCommonVehicleBO> findCountryNamCountList() {
        Session session = this.getSession();
        String sql = "SELECT COUNT(v.VEHICLE_COUNTRY)   from LS_COMMON_VEHICLE v GROUP BY VEHICLE_COUNTRY";
        List countryNameCountList = null;
        countryNameCountList = session.createSQLQuery(sql).list();
        return countryNameCountList;
    }

    // 统计运输司机的数量
    @SuppressWarnings("rawtypes")
    public int findDriverCount() {
        Session session = this.getSession();
        String sql = "select count(DRIVER_NAME) from LS_COMMON_VEHICLE GROUP BY DRIVER_NAME";
        List deriverCountList = session.createSQLQuery(sql).list();
        int driverCount = deriverCountList.size();
        return driverCount;
    }

    // 统计司机所在的国家有哪些
    @SuppressWarnings("rawtypes")
    public List findDriverCountry() {
        Session session = this.getSession();
        String sql = "select v.DRIVER_COUNTRY from LS_COMMON_VEHICLE v GROUP BY DRIVER_COUNTRY";
        List list = session.createSQLQuery(sql).list();
        return list;
    }

    // 统计各个司机国家拥有的司机数量
    @SuppressWarnings("rawtypes")
    public List driverCountryCount() {
        Session session = this.getSession();
        String sql = "SELECT COUNT(v.DRIVER_COUNTRY)   from LS_COMMON_VEHICLE v GROUP BY DRIVER_COUNTRY";
        List driverCountryCountList = null;
        driverCountryCountList = session.createSQLQuery(sql).list();
        return driverCountryCountList;
    }

    /**
     * 根据tripId获取指定的车辆信息
     * 
     * @param tripId
     * @return
     * @throws Exception
     */
    public List<LsCommonVehicleBO> findAllByTripId(String tripId) {
        return findAllBy("tripId", tripId);
    }

    /**
     * 根据trackingDeviceNumber获取指定的车辆信息
     * 
     * @param trackingDeviceNumber
     * @return
     * @throws Exception
     */
    public List<LsCommonVehicleBO> findAllByTrackingDeviceNum(String trackingDeviceNumber) {
        return findAllBy("trackingDeviceNumber", trackingDeviceNumber);
    }

    /**
     * 根据vehiclePlateNumber获取所有的车辆信息
     * 
     * @param vehiclePlateNumber
     * @return
     */
    public List<LsCommonVehicleBO> findAllByVehiclePlateNumber(String vehiclePlateNumber) {
        return findAllBy("vehiclePlateNumber", vehiclePlateNumber);
    }

    public LsCommonVehicleBO findLatestCommonVehicleBo(String tripId, String trackingDeviceNumber) {
        if (null != trackingDeviceNumber && !"".equals(trackingDeviceNumber) && trackingDeviceNumber.length() > 10) {
            trackingDeviceNumber = trackingDeviceNumber.substring(trackingDeviceNumber.length() - 10,
                    trackingDeviceNumber.length());
        }
        String sql = "SELECT v.* FROM LS_MONITOR_TRIP t,LS_COMMON_VEHICLE v "
                + " WHERE t.TRIP_ID=v.TRIP_ID and v.TRACKING_DEVICE_NUMBER LIKE '%" + trackingDeviceNumber + "' "
                + " and v.TRIP_ID='" + tripId + "'" + " ORDER BY t.CHECKIN_TIME DESC";

        try {
            SQLQuery sqlQuery = getSession().createSQLQuery(sql).addEntity(LsCommonVehicleBO.class);
            @SuppressWarnings("unchecked")
            List<LsCommonVehicleBO> lsMonitorVehicleBOList = sqlQuery.list();
            if (lsMonitorVehicleBOList != null && lsMonitorVehicleBOList.size() > 0) {
                return lsMonitorVehicleBOList.get(0);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}