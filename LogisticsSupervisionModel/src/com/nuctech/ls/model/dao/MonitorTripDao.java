package com.nuctech.ls.model.dao;

import static com.nuctech.util.SqlRemoveUtils.removeFetchKeyword;
import static com.nuctech.util.SqlRemoveUtils.removeOrders;
import static com.nuctech.util.SqlRemoveUtils.removeSelect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorTripBO;
import com.nuctech.ls.model.service.CommonVehicleService;
import com.nuctech.ls.model.util.DeviceType;
import com.nuctech.util.Constant;
import com.nuctech.util.DateUtils;
import com.nuctech.util.NuctechUtil;

import javacommon.xsqlbuilder.XsqlBuilder;
import javacommon.xsqlbuilder.XsqlBuilder.XsqlFilterResult;

/**
 * 车辆行程信息Dao
 * 
 * @author liushaowei
 *
 */
@Repository
public class MonitorTripDao extends LSBaseDao<LsMonitorTripBO, Serializable> {

    /**
     * 车辆信息Service
     */
    @Resource
    private CommonVehicleService commonVehicleService;

    /**
     * 新增车辆行程信息
     * 
     * @param lsMonitorTripBO
     */
    public void addMonitorTrip(LsMonitorTripBO lsMonitorTripBO) {
        persist(lsMonitorTripBO);
    }

    /**
     * 查找所有的行程信息
     * 
     * @return
     */
    public List<LsMonitorTripBO> findAllTrips() {
        List<LsMonitorTripBO> list = findAll();
        return list;
    }

    /**
     * 查找设备终端关联的最新的车辆行程
     * 
     * @param trackingDeviceNumber
     * @return
     * 
     * @author sunming
     */
    public LsMonitorTripBO findLastestMonitortripByDeviceNumber(String trackingDeviceNumber) {
        if (null != trackingDeviceNumber && !"".equals(trackingDeviceNumber) && trackingDeviceNumber.length() > 10) {
            trackingDeviceNumber = trackingDeviceNumber.substring(trackingDeviceNumber.length() - 10,
                    trackingDeviceNumber.length());
        }

        String sql = "SELECT t.* FROM LS_MONITOR_TRIP t,LS_COMMON_VEHICLE v "
                + "WHERE t.TRIP_ID=v.TRIP_ID and v.TRACKING_DEVICE_NUMBER LIKE '%" + trackingDeviceNumber
                + "' and t.TRIP_STATUS='1' ORDER BY t.CHECKIN_TIME DESC";

        try {
            // logger.info("执行查询语句：" + sql);
            SQLQuery sqlQuery = getSession().createSQLQuery(sql).addEntity(LsMonitorTripBO.class);
            @SuppressWarnings("unchecked")
            List<LsMonitorTripBO> lsMonitorTripBOList = sqlQuery.list();
            if (lsMonitorTripBOList != null && lsMonitorTripBOList.size() > 0) {
                return lsMonitorTripBOList.get(0);

            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查询失败" + sql);
        }

        return null;
    }

    /**
     * 查找设备终端关联的最新的车辆行程
     * 
     * @param trackingDeviceNumber
     * @return
     * 
     * @author sunming
     */
    public LsMonitorTripBO findLastestPatroltripByDeviceNumber(String trackingDeviceNumber) {
        String sql = "SELECT t.* FROM LS_MONITOR_TRIP t,LS_COMMON_VEHICLE v "
                + "WHERE t.TRIP_ID=v.TRIP_ID and v.TRACKING_DEVICE_NUMBER LIKE '%" + trackingDeviceNumber
                + "' ORDER BY t.CHECKIN_TIME DESC";
        try {
            // logger.info("执行查询语句：" + sql);
            SQLQuery sqlQuery = getSession().createSQLQuery(sql).addEntity(LsMonitorTripBO.class);
            @SuppressWarnings("unchecked")
            List<LsMonitorTripBO> lsMonitorTripBOList = sqlQuery.list();
            if (lsMonitorTripBOList != null && lsMonitorTripBOList.size() > 0) {
                return lsMonitorTripBOList.get(0);

            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查询失败" + sql);
        }

        return null;
    }

    /**
     * SQL语句分页查询
     * 
     * @param sqlQueryString
     * @param pageQuery
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public PageList pageQuerySql(final String sqlQueryString, final PageQuery<Map> pageQuery) {
        XsqlBuilder builder = this.getXsqlBuilder();
        Map filtersMap = pageQuery.getFilters();
        filtersMap.put("sortColumns", pageQuery.getSortColumns());

        XsqlFilterResult queryXsqlResult = builder.generateSql(sqlQueryString, filtersMap);
        final String countQueryString = "select count(*) " + removeSelect(removeFetchKeyword((sqlQueryString)));
        XsqlFilterResult countQueryXsqlResult = builder.generateSql(countQueryString, pageQuery.getFilters());
        Query query = setQueryParameters(
                this.sessionFactory.getCurrentSession().createSQLQuery(queryXsqlResult.getXsql()),
                queryXsqlResult.getAcceptedFilters());
        Query countQuery = setQueryParameters(
                this.sessionFactory.getCurrentSession().createSQLQuery(removeOrders(countQueryXsqlResult.getXsql())),
                countQueryXsqlResult.getAcceptedFilters());
        PageList pageList = new PageList();
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        int firstRow = pageList.getFirstRecordIndex();
        int maxResults = pageQuery.getPageSize();
        pageList.addAll(query.setFirstResult(firstRow).setMaxResults(maxResults).list());
        pageList.setTotalItems(((Number) countQuery.uniqueResult()).intValue());
        return pageList;
    }

    // -------------------调度分析 相关统计-------------------

    /**
     * 统计时间区间内日用设备均值
     * 
     * @param checkinPort
     *        检入口岸
     * @param interval
     *        时间区间
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Integer> findDeviceAverageCountByTimeInterval(String checkinPort, int interval) throws Exception {
        List<Integer> deviceAverageCountList = new ArrayList<Integer>();
        int totalTrackingDeviceNumber = 0; // xx区间内使用关锁总数
        int totalEsealNumber = 0; // XX时间区间内使用子锁总数
        int totalSensorNumber = 0; // xx时间区间内使用的传感器
        String strEsealNumber = "";
        String strSensorNumber = "";
        Date beginDate = calBeginDate(interval);
        Criteria criteria = getSession().createCriteria(LsMonitorTripBO.class);
        criteria.add(Restrictions.eq("checkinPort", checkinPort));
        criteria.add(Restrictions.between("checkinTime", beginDate, new Date()));
        List<LsMonitorTripBO> monitorTripList = criteria.list();
        if (monitorTripList != null) {
            for (LsMonitorTripBO monitorTrip : monitorTripList) {
                if (monitorTrip != null) {
                    List<LsCommonVehicleBO> commonVehicleBOs = commonVehicleService
                            .findAllByTripId(monitorTrip.getTripId());
                    if (commonVehicleBOs != null && commonVehicleBOs.size() > 0) {
                        for (LsCommonVehicleBO commonVehicle : commonVehicleBOs) {
                            strEsealNumber += commonVehicle.getEsealNumber() + ",";
                            strSensorNumber += commonVehicle.getSensorNumber() + ",";
                        }
                    }
                }
            }
        }
        totalTrackingDeviceNumber = monitorTripList.size();
        String[] esealNumberArray = strEsealNumber.split(",");
        totalEsealNumber = esealNumberArray.length;
        String[] sensorNumberArray = strSensorNumber.split(",");
        totalSensorNumber = sensorNumberArray.length;
        // 关锁在interval时间区间内的平均使用次数(进一法)
        deviceAverageCountList.add((int) Math.ceil(totalTrackingDeviceNumber / (float) interval));
        // 子锁在interval时间区间内的平均使用次数(进一法)
        deviceAverageCountList.add((int) Math.ceil(totalEsealNumber / (float) interval));
        // 传感器在interval时间区间内的平均使用次数(进一法)
        deviceAverageCountList.add((int) Math.ceil(totalSensorNumber / (float) interval));
        return deviceAverageCountList;
    }

    /**
     * 查询处于特定口岸"正常状态"的设备数量
     * 
     * @param checkinPort
     * @return
     */
    public List<Integer> findDeviceNomal(String checkinPort) {
        List<Integer> deviceCountList = new ArrayList<Integer>();
        Session session = this.getSession();
        // "正常"状态下关锁的数量
        String sql = "SELECT count(*) FROM LS_WAREHOUSE_ELOCK where BELONG_TO='" + checkinPort
                + "'and ELOCK_STATUS='1'";
        Integer elockNum = (int) session.createSQLQuery(sql).uniqueResult();
        // "正常"状态下子锁的数量
        String sql1 = "SELECT count(*) FROM LS_WAREHOUSE_ESEAL where BELONG_TO='" + checkinPort
                + "'and ESEAL_STATUS='1'";
        Integer esealNum = (int) session.createSQLQuery(sql1).uniqueResult();
        // "正常"状态下传感器的数量
        String sql2 = "SELECT count(*) FROM LS_WAREHOUSE_SENSOR where BELONG_TO='" + checkinPort
                + "'and SENSOR_STATUS='1'";
        Integer sensorNum = (int) session.createSQLQuery(sql2).uniqueResult();
        deviceCountList.add(elockNum);
        deviceCountList.add(esealNum);
        deviceCountList.add(sensorNum);
        return deviceCountList;

    }

    /**
     * 计算区间开始时间
     * 
     * @param interval
     *        区间(天)
     * @return
     */
    private Date calBeginDate(int interval) {
        String strBeginDate = DateUtils.getTimeBefore(interval, new Date());
        Date beginDate = DateUtils.stringToDate(strBeginDate);
        return beginDate;
    }

    /**
     * 根据设备号和其他条件查询设备
     * 
     * @param tripId
     * @param filtersMap
     * @return
     */
    @SuppressWarnings("rawtypes")
    public LsMonitorTripBO findByIdAndFilters(String tripId, Map filtersMap) {
        HashMap<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("tripId", tripId);

        HashMap<String, Object> propertiesLikeMap = new HashMap<String, Object>();
        String trackingDeviceNumber = (String) filtersMap.get("trackingDeviceNumber");
        if (NuctechUtil.isNotNull(trackingDeviceNumber)) {
            propertiesLikeMap.put("trackingDeviceNumber", trackingDeviceNumber);
        }
        return findByProperties(propertiesMap, propertiesLikeMap, null, null);
    }

    // -----------------库存报告相关统计start-----------
    /**
     * 查询某个口岸的流入/流出设备数
     * 
     * @param checkoutPort
     *        口岸ID
     * @param startDate
     *        开始时间
     * @param endDate
     *        结束时间
     * @param type
     *        统计类型(流入/流出)
     * @return 设备对应的总数
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Integer> findPortFlowDevices(String portId, Date startDate, Date endDate, String type)
            throws Exception {
        Map<String, Integer> map = new HashMap<String, Integer>();
        int totalTrackingDeviceNumber = 0; // xx区间内使用关锁总数
        int totalEsealNumber = 0; // XX时间区间内使用子锁总数
        int totalSensorNumber = 0; // xx时间区间内使用的传感器
        String strEsealNumber = "";
        String strSensorNumber = "";
        Criteria criteria = getSession().createCriteria(LsMonitorTripBO.class);
        if (type.equals(Constant.FLOW_IN)) {
            if (NuctechUtil.isNotNull(portId)) {
                criteria.add(Restrictions.eq("checkoutPort", portId));
            }
            if (!NuctechUtil.isNull(startDate) && !NuctechUtil.isNull(endDate)) {
                criteria.add(Restrictions.between("checkoutTime", startDate, endDate));
            }
        }
        if (type.equals(Constant.FLOW_OUT)) {
            if (NuctechUtil.isNotNull(portId)) {
                criteria.add(Restrictions.eq("checkinPort", portId));
            }
            if (!NuctechUtil.isNull(startDate) && !NuctechUtil.isNull(endDate)) {
                criteria.add(Restrictions.between("checkinTime", startDate, endDate));
            }
        }
        List<LsMonitorTripBO> monitorTripList = criteria.list();
        if (monitorTripList != null && !monitorTripList.isEmpty()) {
            for (LsMonitorTripBO monitorTrip : monitorTripList) {
                if (monitorTrip != null) {
                    List<LsCommonVehicleBO> commonVehicleBOs = commonVehicleService
                            .findAllByTripId(monitorTrip.getTripId());
                    if (commonVehicleBOs != null && commonVehicleBOs.size() > 0) {
                        for (LsCommonVehicleBO commonVehicle : commonVehicleBOs) {
                            if (NuctechUtil.isNotNull(commonVehicle.getEsealNumber())) {// 子锁号不为空
                                strEsealNumber += commonVehicle.getEsealNumber() + ",";
                            }
                            if (NuctechUtil.isNotNull(commonVehicle.getSensorNumber())) {// 传感器号不为空
                                strSensorNumber += commonVehicle.getSensorNumber() + ",";
                            }
                            totalTrackingDeviceNumber++;
                        }
                    }
                }
            }
        }
        // totalTrackingDeviceNumber = monitorTripList.size();
        if (!NuctechUtil.isNull(strEsealNumber)) {
            String[] esealNumberArray = strEsealNumber.split(",");
            totalEsealNumber = esealNumberArray.length;
        }
        if (!NuctechUtil.isNull(strSensorNumber)) {
            String[] sensorNumberArray = strSensorNumber.split(",");
            totalSensorNumber = sensorNumberArray.length;
        }
        map.put(DeviceType.TRACKING_DEVICE.getType(), totalTrackingDeviceNumber);
        map.put(DeviceType.ESEAL.getType(), totalEsealNumber);
        map.put(DeviceType.SENSOR.getType(), totalSensorNumber);
        return map;
    }

    @SuppressWarnings("unchecked")
    public List<String> findUnFinishedDeviceNum(String trackingDeviceNumber, String checkoutPort) {
        String hql = "select v.trackingDeviceNumber from LsMonitorTripBO t, LsCommonVehicleBO v"
                + " where t.tripId = v.tripId " + " and t.tripStatus in (1,2) " + " and t.checkoutPort = '"
                + checkoutPort + "' " + " and v.trackingDeviceNumber like '%" + trackingDeviceNumber + "%' "
                + " order by v.trackingDeviceNumber asc ";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        return query.list();
    }

    public String findRealTrackingDeviceNumberByUpload(String uploadTrackingDeviceNum) {
        if (null != uploadTrackingDeviceNum && !"".equals(uploadTrackingDeviceNum)
                && uploadTrackingDeviceNum.length() > 10) {
            uploadTrackingDeviceNum = uploadTrackingDeviceNum.substring(uploadTrackingDeviceNum.length() - 10,
                    uploadTrackingDeviceNum.length());
        }

        String sql = "SELECT v.* FROM LS_MONITOR_TRIP t,LS_COMMON_VEHICLE v "
                + "WHERE t.TRIP_ID=v.TRIP_ID and v.TRACKING_DEVICE_NUMBER LIKE '%" + uploadTrackingDeviceNum
                + "' ORDER BY t.CHECKIN_TIME DESC";

        try {
            // logger.info("执行查询语句：" + sql);
            SQLQuery sqlQuery = getSession().createSQLQuery(sql).addEntity(LsCommonVehicleBO.class);
            @SuppressWarnings("unchecked")
            List<LsCommonVehicleBO> lsCommonVehicleBOList = sqlQuery.list();
            if (lsCommonVehicleBOList != null && lsCommonVehicleBOList.size() > 0) {
                return lsCommonVehicleBOList.get(0).getTrackingDeviceNumber().toString();

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("查询失败" + sql);
        }

        return "";
    }

    /**
     * 查询关锁的使用次数，查询行程表
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<?> findelockUse(String elockNum,String belongTo) {
        Session session = this.getSession();
        String queryString = "";
        queryString =  "SELECT * FROM ( SELECT t.TRACKING_DEVICE_NUMBER, COUNT (*) c FROM LS_MONITOR_TRIP t where t.TRACKING_DEVICE_NUMBER !='' GROUP BY TRACKING_DEVICE_NUMBER ) g "
                + "LEFT JOIN  LS_WAREHOUSE_ELOCK e ON g.TRACKING_DEVICE_NUMBER = e.ELOCK_NUMBER "
                + "LEFT JOIN LS_SYSTEM_DEPARTMENT s on e.BELONG_TO=s.ORGANIZATION_ID where 1=1";
        //前台过滤查询
        if(NuctechUtil.isNotNull(elockNum)){
            queryString +=" and g.TRACKING_DEVICE_NUMBER like'%"+elockNum+"%'";
        }
        if(NuctechUtil.isNotNull(belongTo)){
            queryString +=" and e.BELONG_TO ='"+belongTo+"'";
        }
        List list = null;
        list = session.createSQLQuery(queryString).addScalar("TRACKING_DEVICE_NUMBER", StandardBasicTypes.STRING)
                .addScalar("c", StandardBasicTypes.STRING).addScalar("ORGANIZATION_NAME", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }
    
    /**
     * 查询子锁的使用次数，查询行程表
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<?> findesealUse(String deviceNum,String belongTo) {
        Session session = this.getSession();
        String queryString ="";
        queryString =  "SELECT * FROM ( SELECT t.ESEAL_NUMBER, COUNT (*) c FROM LS_MONITOR_TRIP t where t.ESEAL_NUMBER !='' GROUP BY ESEAL_NUMBER ) g "
                + "LEFT JOIN LS_WAREHOUSE_ESEAL e ON g.ESEAL_NUMBER = e.ESEAL_NUMBER "
                + "LEFT JOIN LS_SYSTEM_DEPARTMENT s ON e.BELONG_TO = s.ORGANIZATION_ID where 1=1";
        //前台过滤查询
        if(NuctechUtil.isNotNull(deviceNum)){
            queryString +=" and g.ESEAL_NUMBER like'%"+deviceNum+"%'";
        }
        if(NuctechUtil.isNotNull(belongTo)){
            queryString +=" and e.BELONG_TO ='"+belongTo+"'";
        }
        
        List list = null;
        list = session.createSQLQuery(queryString).addScalar("ESEAL_NUMBER", StandardBasicTypes.STRING)
                .addScalar("c", StandardBasicTypes.STRING).addScalar("ORGANIZATION_NAME", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }
    
    
    /**
     * 查询传感器的使用次数，查询行程表
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<?> findsensorUse(String deviceNum,String belongTo) {
        Session session = this.getSession();
        String queryString ="";
        queryString =  "SELECT * FROM ( SELECT t.SENSOR_NUMBER, COUNT (*) c FROM LS_MONITOR_TRIP t where t.SENSOR_NUMBER !='' GROUP BY SENSOR_NUMBER ) g "
                + "LEFT JOIN LS_WAREHOUSE_SENSOR e ON g.SENSOR_NUMBER = e.SENSOR_NUMBER "
                + "LEFT JOIN LS_SYSTEM_DEPARTMENT s ON e.BELONG_TO = s.ORGANIZATION_ID where 1=1";
        //前台过滤查询
        if(NuctechUtil.isNotNull(deviceNum)){
            queryString +=" and g.SENSOR_NUMBER like'%"+deviceNum+"%'";
        }
        if(NuctechUtil.isNotNull(belongTo)){
            queryString +=" and e.BELONG_TO ='"+belongTo+"'";
        }
        List list = null;
        list = session.createSQLQuery(queryString).addScalar("SENSOR_NUMBER", StandardBasicTypes.STRING)
                .addScalar("c", StandardBasicTypes.STRING).addScalar("ORGANIZATION_NAME", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }
    public int findElockCount() {
        Session session = this.getSession();
        String sql = "SELECT count(*) FROM ( SELECT t.TRACKING_DEVICE_NUMBER, COUNT (*) c FROM LS_MONITOR_TRIP t GROUP BY TRACKING_DEVICE_NUMBER ) g "
                + "LEFT JOIN  LS_WAREHOUSE_ELOCK e ON g.TRACKING_DEVICE_NUMBER = e.ELOCK_NUMBER "
                + "LEFT JOIN LS_SYSTEM_DEPARTMENT s on e.BELONG_TO=s.ORGANIZATION_ID where 1=1";
        int elockNum = (int) session.createSQLQuery(sql).uniqueResult();
        return elockNum;
    }
    
    public int findEsealCount() {
        Session session = this.getSession();
        String sql = "SELECT count(*) FROM ( SELECT t.ESEAL_NUMBER, COUNT (*) c FROM LS_MONITOR_TRIP t GROUP BY ESEAL_NUMBER ) g LEFT JOIN LS_WAREHOUSE_ESEAL e ON g.ESEAL_NUMBER = e.ESEAL_NUMBER LEFT JOIN LS_SYSTEM_DEPARTMENT s ON e.BELONG_TO = s.ORGANIZATION_ID where 1=1";
        int esealNum = (int) session.createSQLQuery(sql).uniqueResult();
        return esealNum;
    }

    public int findSensorCount() {
        Session session = this.getSession();
        String sql = "SELECT count(*) FROM ( SELECT t.SENSOR_NUMBER, COUNT (*) c FROM LS_MONITOR_TRIP t GROUP BY SENSOR_NUMBER ) g LEFT JOIN LS_WAREHOUSE_SENSOR e ON g.SENSOR_NUMBER = e.SENSOR_NUMBER LEFT JOIN LS_SYSTEM_DEPARTMENT s ON e.BELONG_TO = s.ORGANIZATION_ID where 1=1";
        int sensorNum = (int) session.createSQLQuery(sql).uniqueResult();
        return sensorNum;
    }
}