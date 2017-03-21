package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.common.LsCommonDriverBO;
import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleStatusBO;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.monitor.VehicleInfoVO;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

/**
 * 用于记录最新车辆状态信息
 * 
 * @author liqingxian
 *
 */
@Repository
public class MonitorVehicleStatusDao extends LSBaseDao<LsMonitorVehicleStatusBO, Serializable> {

    private final static String TRIP_STATUS_ONWAY = "1";
    @Resource
    private SystemDepartmentDao systemDepartmentDao;

    @SuppressWarnings("unchecked")
    public List<LsMonitorVehicleStatusBO> findAllOnWayVehicleStatus(String locationType) {
        Criteria crit = sessionFactory.getCurrentSession().createCriteria(LsMonitorVehicleStatusBO.class);
        crit.add(Restrictions.eq("tripStatus", TRIP_STATUS_ONWAY));
        if (NuctechUtil.isNotNull(locationType)) {
            crit.add(Restrictions.eq("locationType", locationType));
        }
        crit.addOrder(Order.asc("checkinTime"));
        return crit.list();
    }

    public List<VehicleInfoVO> findAllByProperty(String locationType, String organizationId, String tripStatus,
            String qdPorts, String zdPorts, String vehicleplatename) {
        HttpSession session = ServletActionContext.getRequest().getSession();
        // 巡逻队只能看到自己跟踪的车辆
        SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
        String patrolSql = "";
        String patrolWhere = "";
        String patrolFilter = "";
        String patrolFilter2 = "";
        if (RoleType.escortPatrol.toString().equalsIgnoreCase(sessionUser.getRoleName())
                || RoleType.enforcementPatrol.toString().equalsIgnoreCase(sessionUser.getRoleName())) {
            patrolSql = "left JOIN (SELECT DISTINCT i.TRIP_ID , i.USER_ID  FROM LS_MONITOR_ALARM i) j "
                    + "on j.TRIP_ID = e.TRIP_ID";
            patrolWhere = " AND  j.USER_ID='" + sessionUser.getUserId() + "'";
            patrolFilter = "LEFT JOIN LS_COMMON_PATROL c ON c.TRIP_ID=e.TRIP_ID ";
            patrolFilter2 = " AND  c.POTRAL_USER='" + sessionUser.getUserId() + "'";
        }
        String sql = "select {e.*},{tr.*},{d.*},{dr.*},{f.*},{g.*},{h.*},{ar.*} from LS_MONITOR_VEHICLE_STATUS e "
                + "LEFT JOIN LS_MONITOR_TRIP tr on e.TRIP_ID = tr.TRIP_ID "
                + "LEFT JOIN LS_COMMON_VEHICLE d on e.VEHICLE_ID=d.VEHICLE_ID "
                + "LEFT JOIN LS_COMMON_DRIVER dr on d.DRIVER_ID = dr.DRIVER_ID "
                + "LEFT JOIN LS_SYSTEM_DEPARTMENT f on e.CHECKIN_PORT=f.ORGANIZATION_ID "
                + "LEFT JOIN LS_SYSTEM_DEPARTMENT g on e.CHECKOUT_PORT=g.ORGANIZATION_ID "
                + "LEFT JOIN LS_MONITOR_ROUTE_AREA ar on e.ROUTE_ID=ar.ROUTE_AREA_ID "
                + "LEFT JOIN LS_SYSTEM_USER h on e.CHECKIN_USER=h.USER_ID " + patrolSql + patrolWhere + patrolFilter
                + " where 1=1  and e.LOCATION_TYPE= '" + locationType + "' " + patrolFilter2;

        if (NuctechUtil.isNotNull(tripStatus)) {
            // sql += " and e.TRIP_STATUS = '" + tripStatus + "' ";
            sql += " and e.TRIP_STATUS in (" + tripStatus + ") ";
        }

        // sql += " and (e.TRIP_STATUS ='1' or e.TRIP_STATUS ='2')";

        if (NuctechUtil.isNotNull(qdPorts)) {
            sql += " and e.CHECKIN_PORT in (" + qdPorts + ") ";
        }
        if (NuctechUtil.isNotNull(zdPorts)) {
            sql += " and e.CHECKOUT_PORT in (" + zdPorts + ") ";
        }
        // 中心用户可以看到本国所有的车辆，口岸用户所在口岸必须为车辆检入口岸或者是检出口岸
        if (RoleType.portUser.getType().equals(sessionUser.getRoleId())) {
        	if(NuctechUtil.isNotNull(organizationId)) {
        		sql += " and (e.CHECKIN_PORT = '" + organizationId + "' or e.CHECKOUT_PORT='" + organizationId + "')";
        	}
        } else if (RoleType.contromRoomUser.getType().equals(sessionUser.getRoleId())) {
        	if(NuctechUtil.isNotNull(organizationId)) {
        		String countryId = systemDepartmentDao.findCountryIdByDeptId(organizationId);
        		List<LsSystemDepartmentBO> portList = systemDepartmentDao.findAllPortByCountryId(countryId);
        		if(null != portList && portList.size() > 0) {
        			List<String> portIds = getAllPortIds(portList);
        			sql += " and e.CHECKIN_PORT in ( " + StringUtils.join(portIds, ",") + ") ";
        		}
        	}
        }

        if (NuctechUtil.isNotNull(vehicleplatename)) {
            sql += " and d.VEHICLE_PLATE_NUMBER like '" + "%" + vehicleplatename + "%" + "'";
        }

        sql += " order by tr.CHECKOUT_TIME desc";

        Query query = this.getSession().createSQLQuery(sql).addEntity("e", LsMonitorVehicleStatusBO.class)
                .addEntity("tr", LsMonitorTripBO.class).addEntity("d", LsCommonVehicleBO.class)
                .addEntity("dr", LsCommonDriverBO.class).addEntity("f", LsSystemDepartmentBO.class)
                .addEntity("g", LsSystemDepartmentBO.class).addEntity("h", LsSystemUserBO.class)
                .addEntity("ar", LsMonitorRouteAreaBO.class);

        List<VehicleInfoVO> vehicleInfolist = new ArrayList<VehicleInfoVO>();
        @SuppressWarnings("unchecked")
        List<Object[]> list = query.list();
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                Object[] objs = (Object[]) obj;
                VehicleInfoVO vehicleInfoVO = new VehicleInfoVO();
                LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO = (LsMonitorVehicleStatusBO) objs[0];
                BeanUtils.copyProperties(lsMonitorVehicleStatusBO, vehicleInfoVO);
                if (objs[1] != null) {
                    LsMonitorTripBO lsMonitorTripBO = (LsMonitorTripBO) objs[1];
                    BeanUtils.copyProperties(lsMonitorTripBO, vehicleInfoVO);
                }
                if (objs[2] != null) {
                    LsCommonVehicleBO lsCommonVehicleBO = (LsCommonVehicleBO) objs[2];
                    BeanUtils.copyProperties(lsCommonVehicleBO, vehicleInfoVO);
                }
                if (objs[3] != null) {
                    LsCommonDriverBO lsCommonDriverBO = (LsCommonDriverBO) objs[3];
                    BeanUtils.copyProperties(lsCommonDriverBO, vehicleInfoVO);
                }
                if (objs[4] != null) {
                    LsSystemDepartmentBO lsCheckinPort = (LsSystemDepartmentBO) objs[4];
                    vehicleInfoVO.setCheckinPortName(lsCheckinPort.getOrganizationName());
                }
                if (objs[5] != null) {
                    LsSystemDepartmentBO lsCheckoutPort = (LsSystemDepartmentBO) objs[5];
                    vehicleInfoVO.setCheckoutPortName(lsCheckoutPort.getOrganizationName());
                }
                if (objs[6] != null) {
                    LsSystemUserBO lsSystemUserBO = (LsSystemUserBO) objs[6];
                    vehicleInfoVO.setCheckinUserName(lsSystemUserBO.getUserName());
                }
                if (objs[7] != null) {
                    LsMonitorRouteAreaBO lsMonitorRouteAreaBO = (LsMonitorRouteAreaBO) objs[7];
                    vehicleInfoVO.setRouteAreaName(lsMonitorRouteAreaBO.getRouteAreaName());
                }
                vehicleInfolist.add(vehicleInfoVO);
            }
        }
        return vehicleInfolist;
    }

    /**
     * 获得所有口岸的Id
     * @param portList
     * @return
     */
	private List<String> getAllPortIds(List<LsSystemDepartmentBO> portList) {
		List<String> portIds = new ArrayList<>(portList.size());
		for(LsSystemDepartmentBO departmentBO: portList) {
			portIds.add("'" + departmentBO.getOrganizationId() + "'");
		}
		return portIds;
	}

    public List<VehicleInfoVO> findAllPatrolStatus(String locationType, String organizationId, String roleNameValue,
            String tripStatus) {
        String roleName1 = "";
        String roleNameSql = "";
        if (!"".equalsIgnoreCase(roleNameValue) && null != roleNameValue) {
            roleName1 = roleNameValue;
            roleNameSql = "and sysrole.ROLE_NAME='" + roleName1 + "'";
        }
        String sql = "select {e.*},{d.*},f.ORGANIZATION_NAME,g.ROUTE_AREA_NAME,s.USER_NAME ,sysrole.ROLE_NAME "
                + "from LS_MONITOR_VEHICLE_STATUS d  "
                + "Inner JOIN LS_COMMON_PATROL e on e.TRACK_UNIT_NUMBER=d.TRACKING_DEVICE_NUMBER "
                + "and d.LOCATION_TYPE= '" + locationType + "' "
                + "LEFT JOIN LS_SYSTEM_DEPARTMENT f on e.BELONG_TO_PORT=f.ORGANIZATION_ID "
                + "LEFT JOIN LS_MONITOR_ROUTE_AREA g on e.BELONG_TO_AREA=g.ROUTE_AREA_ID "
                + "LEFT JOIN LS_SYSTEM_USER s on e.POTRAL_USER=s.USER_ID left JOIN  "
                + "(SELECT r.ROLE_NAME,ur.USER_ID FROM LS_SYSTEM_USER_ROLE ur ,LS_SYSTEM_ROLE r "
                + "WHERE ur.ROLE_ID=r.ROLE_ID ) sysrole ON sysrole.USER_ID = s.USER_ID "
                // + "where 1=1 and d.TRIP_STATUS='"+tripStatus+"' and
                // e.DELETE_MARK='"+ Constant.MARK_UN_DELETED +"' "
                // +roleNameSql;
                + "where 1=1  and e.DELETE_MARK='" + Constant.MARK_UN_DELETED + "' " + roleNameSql;
        if (NuctechUtil.isNotNull(organizationId)) {
            // sql += " and e.CHECKIN_PORT = '"+organizationId+"' ";
        }
        sql += " order by e.CREATE_TIME desc";

        Query query = this.getSession().createSQLQuery(sql).addEntity("e", LsCommonPatrolBO.class)
                .addEntity("d", LsMonitorVehicleStatusBO.class)
                .addScalar("ORGANIZATION_NAME", StandardBasicTypes.STRING)
                .addScalar("ROUTE_AREA_NAME", StandardBasicTypes.STRING)
                .addScalar("USER_NAME", StandardBasicTypes.STRING).addScalar("ROLE_NAME", StandardBasicTypes.STRING);

        List<VehicleInfoVO> vehicleInfolist = new ArrayList<VehicleInfoVO>();

        @SuppressWarnings("unchecked")
        List<Object[]> list = query.list();
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                Object[] objs = (Object[]) obj;
                VehicleInfoVO vehicleInfoVO = new VehicleInfoVO();
                if (objs[1] != null) {
                    LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO = (LsMonitorVehicleStatusBO) objs[1];
                    BeanUtils.copyProperties(lsMonitorVehicleStatusBO, vehicleInfoVO);
                }
                if (objs[2] != null) {
                    String belongToPortName = (String) objs[2];
                    vehicleInfoVO.setBelongToPortName(belongToPortName);
                }
                if (objs[3] != null) {
                    String routeAreaName = (String) objs[3];
                    vehicleInfoVO.setRouteAreaName(routeAreaName);
                }
                if (objs[4] != null) {
                    String routeAreaName = (String) objs[4];
                    vehicleInfoVO.setPotralUserName(routeAreaName);
                }
                if (objs[5] != null) {
                    String roleName = (String) objs[5];
                    vehicleInfoVO.setRoleName(roleName);
                }
                LsCommonPatrolBO lsCommonPatrolBO = (LsCommonPatrolBO) objs[0];
                BeanUtils.copyProperties(lsCommonPatrolBO, vehicleInfoVO);

                vehicleInfolist.add(vehicleInfoVO);
            }
        }
        return vehicleInfolist;
    }

    public List<VehicleInfoVO> findVehicleInfoBySearchNum(String searchNumber) {
        String sql = "select {e.*},{tr.*},{d.*},{dr.*},{f.*},{g.*},{h.*} from LS_MONITOR_VEHICLE_STATUS e "
                + "LEFT JOIN LS_MONITOR_TRIP tr on e.TRIP_ID = tr.TRIP_ID "
                + "LEFT JOIN LS_COMMON_VEHICLE d on e.VEHICLE_ID=d.VEHICLE_ID "
                + "LEFT JOIN LS_COMMON_DRIVER dr on d.DRIVER_ID = dr.DRIVER_ID "
                + "LEFT JOIN LS_SYSTEM_DEPARTMENT f on e.CHECKIN_PORT=f.ORGANIZATION_ID "
                + "LEFT JOIN LS_SYSTEM_DEPARTMENT g on e.CHECKOUT_PORT=g.ORGANIZATION_ID "
                + "LEFT JOIN LS_SYSTEM_USER h on e.CHECKIN_USER=h.USER_ID "
                + " where 1=1 and e.TRACKING_DEVICE_NUMBER like '%" + searchNumber
                + "%' OR d.VEHICLE_PLATE_NUMBER like  '%" + searchNumber + "%'";
        sql += " order by e.CHECKIN_TIME desc";
        Query query = this.getSession().createSQLQuery(sql).addEntity("e", LsMonitorVehicleStatusBO.class)
                .addEntity("tr", LsMonitorTripBO.class).addEntity("d", LsCommonVehicleBO.class)
                .addEntity("dr", LsCommonDriverBO.class).addEntity("f", LsSystemDepartmentBO.class)
                .addEntity("g", LsSystemDepartmentBO.class).addEntity("h", LsSystemUserBO.class);

        List<VehicleInfoVO> vehicleInfolist = new ArrayList<VehicleInfoVO>();
        @SuppressWarnings("unchecked")
        List<Object[]> list = query.list();
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                Object[] objs = (Object[]) obj;
                VehicleInfoVO vehicleInfoVO = new VehicleInfoVO();
                LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO = (LsMonitorVehicleStatusBO) objs[0];
                BeanUtils.copyProperties(lsMonitorVehicleStatusBO, vehicleInfoVO);
                if (objs[1] != null) {
                    LsMonitorTripBO lsMonitorTripBO = (LsMonitorTripBO) objs[1];
                    BeanUtils.copyProperties(lsMonitorTripBO, vehicleInfoVO);
                }
                if (objs[2] != null) {
                    LsCommonVehicleBO lsCommonVehicleBO = (LsCommonVehicleBO) objs[2];
                    BeanUtils.copyProperties(lsCommonVehicleBO, vehicleInfoVO);
                }
                if (objs[3] != null) {
                    LsCommonDriverBO lsCommonDriverBO = (LsCommonDriverBO) objs[3];
                    BeanUtils.copyProperties(lsCommonDriverBO, vehicleInfoVO);
                }
                if (objs[4] != null) {
                    LsSystemDepartmentBO lsCheckinPort = (LsSystemDepartmentBO) objs[4];
                    vehicleInfoVO.setCheckinPortName(lsCheckinPort.getOrganizationName());
                }
                if (objs[5] != null) {
                    LsSystemDepartmentBO lsCheckoutPort = (LsSystemDepartmentBO) objs[5];
                    vehicleInfoVO.setCheckoutPortName(lsCheckoutPort.getOrganizationName());
                }
                if (objs[6] != null) {
                    LsSystemUserBO lsSystemUserBO = (LsSystemUserBO) objs[6];
                    vehicleInfoVO.setCheckinUserName(lsSystemUserBO.getUserName());
                }
                vehicleInfolist.add(vehicleInfoVO);
            }
        }
        return vehicleInfolist;
    }

    public VehicleInfoVO findVehicleStatusByAlarm(String tripId) {
        String sql = "select {e.*},{tr.*},{d.*},{dr.*},{f.*},{g.*},{h.*} from LS_MONITOR_VEHICLE_STATUS e "
                + "LEFT JOIN LS_MONITOR_TRIP tr on e.TRIP_ID = tr.TRIP_ID "
                + "LEFT JOIN LS_COMMON_VEHICLE d on e.VEHICLE_ID=d.VEHICLE_ID "
                + "LEFT JOIN LS_COMMON_DRIVER dr on d.DRIVER_ID = dr.DRIVER_ID "
                + "LEFT JOIN LS_SYSTEM_DEPARTMENT f on e.CHECKIN_PORT=f.ORGANIZATION_ID "
                + "LEFT JOIN LS_SYSTEM_DEPARTMENT g on e.CHECKOUT_PORT=g.ORGANIZATION_ID "
                + "LEFT JOIN LS_SYSTEM_USER h on e.CHECKIN_USER=h.USER_ID " + " where 1=1 and e.TRIP_ID = '" + tripId
                + "' ";
        sql += " order by e.CHECKIN_TIME desc";

        Query query = this.getSession().createSQLQuery(sql).addEntity("e", LsMonitorVehicleStatusBO.class)
                .addEntity("tr", LsMonitorTripBO.class).addEntity("d", LsCommonVehicleBO.class)
                .addEntity("dr", LsCommonDriverBO.class).addEntity("f", LsSystemDepartmentBO.class)
                .addEntity("g", LsSystemDepartmentBO.class).addEntity("h", LsSystemUserBO.class);

        Object[] objs = (Object[]) query.uniqueResult();

        VehicleInfoVO vehicleInfoVO = new VehicleInfoVO();
        LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO = (LsMonitorVehicleStatusBO) objs[0];
        BeanUtils.copyProperties(lsMonitorVehicleStatusBO, vehicleInfoVO);
        if (objs[1] != null) {
            LsMonitorTripBO lsMonitorTripBO = (LsMonitorTripBO) objs[1];
            BeanUtils.copyProperties(lsMonitorTripBO, vehicleInfoVO);
        }
        if (objs[2] != null) {
            LsCommonVehicleBO lsCommonVehicleBO = (LsCommonVehicleBO) objs[2];
            BeanUtils.copyProperties(lsCommonVehicleBO, vehicleInfoVO);
        }
        if (objs[3] != null) {
            LsCommonDriverBO lsCommonDriverBO = (LsCommonDriverBO) objs[3];
            BeanUtils.copyProperties(lsCommonDriverBO, vehicleInfoVO);
        }
        if (objs[4] != null) {
            LsSystemDepartmentBO lsCheckinPort = (LsSystemDepartmentBO) objs[4];
            vehicleInfoVO.setCheckinPortName(lsCheckinPort.getOrganizationName());
        }
        if (objs[5] != null) {
            LsSystemDepartmentBO lsCheckoutPort = (LsSystemDepartmentBO) objs[5];
            vehicleInfoVO.setCheckoutPortName(lsCheckoutPort.getOrganizationName());
        }
        if (objs[6] != null) {
            LsSystemUserBO lsSystemUserBO = (LsSystemUserBO) objs[6];
            vehicleInfoVO.setCheckinUserName(lsSystemUserBO.getUserName());
        }

        return vehicleInfoVO;
    }

    /**
     * 根据车辆行驶状态查询车辆列表
     * 
     * @param trvelStatus
     * @return
     */
    public List<VehicleInfoVO> findOnWayVehicleByTravelStatus(String trvelStatus) {
        String sql = "select {e.*},{tr.*},{d.*},{dr.*},{f.*},{g.*},{h.*} from LS_MONITOR_VEHICLE_STATUS e "
                + "LEFT JOIN LS_MONITOR_TRIP tr on e.TRIP_ID = tr.TRIP_ID "
                + "LEFT JOIN LS_COMMON_VEHICLE d on e.VEHICLE_ID=d.VEHICLE_ID "
                + "LEFT JOIN LS_COMMON_DRIVER dr on d.DRIVER_ID = dr.DRIVER_ID "
                + "LEFT JOIN LS_SYSTEM_DEPARTMENT f on e.CHECKIN_PORT=f.ORGANIZATION_ID "
                + "LEFT JOIN LS_SYSTEM_DEPARTMENT g on e.CHECKOUT_PORT=g.ORGANIZATION_ID "
                + "LEFT JOIN LS_SYSTEM_USER h on e.CHECKIN_USER=h.USER_ID "
                + " where 1=1 and e.LOCATION_TYPE='0' and e.TRAVEL_STATUS = '" + trvelStatus + "' ";// and
                                                                                                    // e.TRIP_STATUS
                                                                                                    // =
                                                                                                    // '1'
        sql += " order by e.CHECKIN_TIME desc";
        Query query = this.getSession().createSQLQuery(sql).addEntity("e", LsMonitorVehicleStatusBO.class)
                .addEntity("tr", LsMonitorTripBO.class).addEntity("d", LsCommonVehicleBO.class)
                .addEntity("dr", LsCommonDriverBO.class).addEntity("f", LsSystemDepartmentBO.class)
                .addEntity("g", LsSystemDepartmentBO.class).addEntity("h", LsSystemUserBO.class);

        List<VehicleInfoVO> vehicleInfolist = new ArrayList<VehicleInfoVO>();
        @SuppressWarnings("unchecked")
        List<Object[]> list = query.list();
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                Object[] objs = (Object[]) obj;
                VehicleInfoVO vehicleInfoVO = new VehicleInfoVO();
                LsMonitorVehicleStatusBO lsMonitorVehicleStatusBO = (LsMonitorVehicleStatusBO) objs[0];
                BeanUtils.copyProperties(lsMonitorVehicleStatusBO, vehicleInfoVO);
                if (objs[1] != null) {
                    LsMonitorTripBO lsMonitorTripBO = (LsMonitorTripBO) objs[1];
                    BeanUtils.copyProperties(lsMonitorTripBO, vehicleInfoVO);
                }
                if (objs[2] != null) {
                    LsCommonVehicleBO lsCommonVehicleBO = (LsCommonVehicleBO) objs[2];
                    BeanUtils.copyProperties(lsCommonVehicleBO, vehicleInfoVO);
                }
                if (objs[3] != null) {
                    LsCommonDriverBO lsCommonDriverBO = (LsCommonDriverBO) objs[3];
                    BeanUtils.copyProperties(lsCommonDriverBO, vehicleInfoVO);
                }
                if (objs[4] != null) {
                    LsSystemDepartmentBO lsCheckinPort = (LsSystemDepartmentBO) objs[4];
                    vehicleInfoVO.setCheckinPortName(lsCheckinPort.getOrganizationName());
                }
                if (objs[5] != null) {
                    LsSystemDepartmentBO lsCheckoutPort = (LsSystemDepartmentBO) objs[5];
                    vehicleInfoVO.setCheckoutPortName(lsCheckoutPort.getOrganizationName());
                }
                if (objs[6] != null) {
                    LsSystemUserBO lsSystemUserBO = (LsSystemUserBO) objs[6];
                    vehicleInfoVO.setCheckinUserName(lsSystemUserBO.getUserName());
                }
                vehicleInfolist.add(vehicleInfoVO);
            }
        }
        return vehicleInfolist;
    }

    public LsMonitorVehicleStatusBO findLatestCommonVehicleStatusBo(String tripId, String trackingDeviceNumber) {
        if (null != trackingDeviceNumber && !"".equals(trackingDeviceNumber) && trackingDeviceNumber.length() > 10) {
            trackingDeviceNumber = trackingDeviceNumber.substring(trackingDeviceNumber.length() - 10,
                    trackingDeviceNumber.length());
        }
        String sql = "SELECT t.* FROM LS_MONITOR_VEHICLE_STATUS t " + " WHERE t.TRACKING_DEVICE_NUMBER LIKE '%"
                + trackingDeviceNumber + "' " + " and t.TRIP_ID='" + tripId + "'" + " ORDER BY t.CHECKIN_TIME DESC";

        try {
            SQLQuery sqlQuery = getSession().createSQLQuery(sql).addEntity(LsMonitorVehicleStatusBO.class);
            @SuppressWarnings("unchecked")
            List<LsMonitorVehicleStatusBO> lsMonitorVehicleStatusBOList = sqlQuery.list();
            // logger.info("sql：" + sql);
            if (lsMonitorVehicleStatusBOList != null && lsMonitorVehicleStatusBOList.size() > 0) {
                return lsMonitorVehicleStatusBOList.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
