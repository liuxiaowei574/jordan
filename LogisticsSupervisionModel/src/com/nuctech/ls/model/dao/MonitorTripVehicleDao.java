package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.vo.monitor.LsMonitorTripVehicleVo;
import com.nuctech.util.NuctechUtil;

/**
 * 
 * @author liqingxian
 *
 */
@Repository
public class MonitorTripVehicleDao extends LSBaseDao<LsMonitorTripVehicleVo, Serializable> {

    public List<LsMonitorTripVehicleVo> findAllTripVehicleBySql(String tripStatus, String qdPorts, String zdPorts,
            String organizationId) {
        String sql = "select vehicle.VEHICLE_ID  vehicleId, "
                + "vehicle.VEHICLE_PLATE_NUMBER as vehiclePlateNumber,trip.RISK_STATUS as riskStatus, "
                + "trip.TRACKING_DEVICE_NUMBER as trackingDeviceNumber, trip.TRIP_STATUS as tripStatus "
                + " from LS_COMMON_VEHICLE vehicle, LS_MONITOR_TRIP trip where vehicle.TRIP_ID = trip.TRIP_ID";
        if (NuctechUtil.isNotNull(tripStatus)) {
            sql = sql + " and trip.TRIP_STATUS = '" + tripStatus + "' ";
        }
        if (NuctechUtil.isNotNull(qdPorts)) {
            sql = sql + " and trip.CHECKIN_PORT in (" + qdPorts + ") ";
        }
        if (NuctechUtil.isNotNull(zdPorts)) {
            sql = sql + " and trip.CHECKOUT_PORT in (" + zdPorts + ") ";
        }
        if (NuctechUtil.isNotNull(organizationId)) {
            // sql = sql + " and trip.CHECKIN_PORT = '" + organizationId + "' ";
        }
        /*
         * if(NuctechUtil.isNotNull(vehicleTypes)){
         * sql = sql + " and VEHICLE_TYPE in (" + vehicleTypes + ") ";
         * }
         */

        Query query = this.sessionFactory.getCurrentSession().createSQLQuery(sql);
        @SuppressWarnings("rawtypes")
        List list = query.list();
        if (list != null && list.size() > 0) {
            List<LsMonitorTripVehicleVo> result = new ArrayList<LsMonitorTripVehicleVo>();
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = (Object[]) list.get(i);

                LsMonitorTripVehicleVo vo = new LsMonitorTripVehicleVo();
                if (obj[0] != null) {
                    vo.setVehicleId(obj[0].toString());
                } else {
                    vo.setVehicleId("");
                }
                if (obj[1] != null) {
                    vo.setVehiclePlateNumber(obj[1].toString());
                } else {
                    vo.setVehiclePlateNumber("");
                }
                if (obj[2] != null) {
                    vo.setRiskStatus(obj[2].toString());
                } else {
                    vo.setRiskStatus("");
                }
                if (obj[3] != null) {
                    vo.setTrackingDeviceNumber(obj[3].toString());
                } else {
                    vo.setTrackingDeviceNumber("");
                }
                if (obj[4] != null) {
                    vo.setTripStatus(obj[4].toString());
                } else {
                    vo.setTripStatus("");
                }

                result.add(vo);
            }
            return result;
        }
        return null;
    }

}
