/**
 * 
 */
package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.common.LsCommonDriverBO;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.dao.CommonDriverDao;
import com.nuctech.ls.model.dao.CommonVehicleDao;
import com.nuctech.ls.model.dao.MonitorTripReportDao;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.monitor.CommonVehicleDriverVO;
import com.nuctech.ls.model.vo.monitor.MonitorTripVehicleVO;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;

/**
 * 监管报告Service
 * 
 * @author liushaowei
 *
 */
@Service
@Transactional
public class MonitorTripReportService extends LSBaseService {

    private final static String ORDER_BY = " /~ order by [sortColumns] ~/ ";
    @Resource
    public MonitorTripReportDao monitorTripReportDao;
    @Resource
    private CommonVehicleDao commonVehicleDao;
    @Resource
    private CommonDriverDao commonDriverDao;

    /**
     * 查询监管行程信息及从属信息列表。查询主体为行程时，一单多车会导致有重复数据
     * 
     * @param pageQuery
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findTripReportList(PageQuery<Map> pageQuery) throws Exception {
        // 查询条件：车牌号、关锁号、司机姓名、检入地点、检出地点、检入时间、检出时间、检入人员、检出人员，点击“详情”进入详情页，展示该行程所有的报警信息
        String queryString = "select t, " + "(select userName from LsSystemUserBO where userId = t.checkinUser), "
                + "(select userName from LsSystemUserBO where userId = t.checkoutUser) "
                + " from LsMonitorTripBO t, LsCommonVehicleBO v where 1=1 " + " and t.tripId = v.tripId "
                + "/~ and v.trackingDeviceNumber like '%[trackingDeviceNumber]%' ~/"
                + "/~ and v.vehiclePlateNumber like '%[vehiclePlateNumber]%' ~/"
                + "/~ and t.checkinUser like '%[checkinUser]%' ~/"
                + "/~ and t.declarationNumber like '%[declarationNumber]%' ~/"
                + "/~ and t.checkinTime >= '[checkinStartTime]' ~/" + "/~ and t.checkinTime <= '[checkinEndTime]' ~/"
                + "/~ and t.checkoutUser like '%[checkoutUser]%' ~/"
                + "/~ and t.checkoutTime >= '[checkoutStartTime]' ~/"
                + "/~ and t.checkoutTime <= '[checkoutEndTime]' ~/" + "/~ and t.checkinPort = '[checkinPort]' ~/"
                + "/~ and t.checkoutPort = '[checkoutPort]' ~/" + "/~ and t.tripStatus = '[tripStatus]' ~/";

        // 获取checkinUserId、checkoutUserId条件
        Map<String, Object> filtersMap = pageQuery.getFilters();
        if (filtersMap != null) {
            if (filtersMap.get("checkinUserId") != null) {
                String checkinUserId = (String) filtersMap.get("checkinUserId");
                if (NuctechUtil.isNotNull(checkinUserId)) {
                    String inCondition = joinInCondition("t.checkinUser", checkinUserId);
                    queryString += inCondition;
                }
            }
            if (filtersMap.get("checkoutUserId") != null) {
                String checkoutUserId = (String) filtersMap.get("checkoutUserId");
                if (NuctechUtil.isNotNull(checkoutUserId)) {
                    String inCondition = joinInCondition("t.checkoutUser", checkoutUserId);
                    queryString += inCondition;
                }
            }
        }
        queryString += ORDER_BY;

        PageList<Object> queryList = monitorTripReportDao.pageQuery(queryString, pageQuery);
        PageList<MonitorTripVehicleVO> pageList = new PageList<MonitorTripVehicleVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                MonitorTripVehicleVO tripVehicleVO = new MonitorTripVehicleVO();
                BeanUtils.copyProperties(objs[0], tripVehicleVO);
                tripVehicleVO.setCheckinUserName((objs[1] == null) ? "" : (String) objs[1]);
                tripVehicleVO.setCheckoutUserName((objs[2] == null) ? "" : (String) objs[2]);

                List<CommonVehicleDriverVO> commonVehicleDriverBOList = findAllVehicleDriverByPageQuery(pageQuery,
                        tripVehicleVO.getTripId());
                tripVehicleVO.setCommonVehicleDriverList(commonVehicleDriverBOList);
                copyProperties(tripVehicleVO, commonVehicleDriverBOList);
                pageList.add(tripVehicleVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 查询监管行程信息及从属信息列表。
     * 
     * @param pageQuery
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject findTripReportListData(PageQuery<Map> pageQuery) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        String sortColumNname = request.getParameter("sort");
        String columnName = "t.checkinTime";
        List<String> vehicleColumns = Arrays
                .asList(new String[] { "vehiclePlateNumber", "trackingDeviceNumber", "esealNumber", "sensorNumber" });
        List<String> tripColumns = Arrays
                .asList(new String[] { "declarationNumber", "checkinTime", "checkoutTime", "tripStatus" });
        if (vehicleColumns.indexOf(sortColumNname) > -1) {
            columnName = "v." + sortColumNname;
        } else if (tripColumns.indexOf(sortColumNname) > -1) {
            columnName = "t." + sortColumNname;
        }
        ////口岸用户只能看到从该口岸检入/检出的行程报告
        HttpSession session = ServletActionContext.getRequest().getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
        String organizationId = sessionUser.getOrganizationId();
        String queryString = "";
        // 查询条件：车牌号、关锁号、司机姓名、检入地点、检出地点、检入时间、检出时间、检入人员、检出人员，点击“详情”进入详情页，展示该行程所有的报警信息

        queryString = "select distinct t.tripId, " + columnName
                + " from LsMonitorTripBO t, LsCommonVehicleBO v where 1=1 " + " and t.tripId = v.tripId ";
        //口岸用户只能看到从该口岸检入/检出的行程报告
		 if(sessionUser.getRoleId().equals(RoleType.portUser.getType())){
	         queryString += "and '"+organizationId+"' in (t.checkinPort ,t.checkoutPort)"; 
	     }
		 queryString += "/~ and v.trackingDeviceNumber like '%[trackingDeviceNumber]%' ~/"
	                + "/~ and v.vehiclePlateNumber like '%[vehiclePlateNumber]%' ~/"
	                + "/~ and t.checkinUser like '%[checkinUser]%' ~/"
	                + "/~ and t.declarationNumber like '%[declarationNumber]%' ~/"
	                + "/~ and t.checkinTime >= '[checkinStartTime]' ~/" + "/~ and t.checkinTime <= '[checkinEndTime]' ~/"
	                + "/~ and t.checkoutUser like '%[checkoutUser]%' ~/"
	                + "/~ and t.checkoutTime >= '[checkoutStartTime]' ~/"
	                + "/~ and t.checkoutTime <= '[checkoutEndTime]' ~/" + "/~ and t.checkinPort = '[checkinPort]' ~/"
	                + "/~ and t.checkoutPort = '[checkoutPort]' ~/" + "/~ and t.tripStatus = '[tripStatus]' ~/";

        // 获取checkinUserId、checkoutUserId条件
        Map<String, Object> filtersMap = pageQuery.getFilters();
        if (filtersMap != null) {
            if (filtersMap.get("checkinUserId") != null) {
                String checkinUserId = (String) filtersMap.get("checkinUserId");
                if (NuctechUtil.isNotNull(checkinUserId)) {
                    String inCondition = joinInCondition("t.checkinUser", checkinUserId);
                    queryString += inCondition;
                }
            }
            if (filtersMap.get("checkoutUserId") != null) {
                String checkoutUserId = (String) filtersMap.get("checkoutUserId");
                if (NuctechUtil.isNotNull(checkoutUserId)) {
                    String inCondition = joinInCondition("t.checkoutUser", checkoutUserId);
                    queryString += inCondition;
                }
            }
        }
        queryString += ORDER_BY;

        PageList<MonitorTripVehicleVO> pageList = new PageList<MonitorTripVehicleVO>();
        PageList<Object> tripIdList = monitorTripReportDao.pageQueryDistinct(queryString, pageQuery);
        if (tripIdList != null && tripIdList.size() > 0) {
            pageList.setPage(pageQuery.getPage());
            pageList.setPageSize(pageQuery.getPageSize());
            pageList.setTotalItems(tripIdList.getTotalItems());

            int len = tripIdList.size();
            String[] tripIds = new String[tripIdList.size()]; // 带顺序的tripId数组，后面查询结果也要按此顺序排序
            for (int i = 0; i < len; i++) {
                Object[] objs = (Object[]) tripIdList.get(i);
                tripIds[i] = (String) objs[0];
            }
            String sql = "select t, " + "(select userName from LsSystemUserBO where userId = t.checkinUser), "
                    + "(select userName from LsSystemUserBO where userId = t.checkoutUser) "
                    + " from LsMonitorTripBO t, LsCommonVehicleBO v where 1=1 " + " and t.tripId = v.tripId ";
            String inCondition = joinInCondition("t.tripId", StringUtils.join(tripIds, ","));
            sql += inCondition;

            PageQuery<Map> pageQuery1 = new PageQuery();
            BeanUtils.copyProperties(pageQuery, pageQuery1);
            pageQuery1.setPage(1);
            pageQuery1.setPageSize(Constant.MAX_PAGE_SIZE);
            PageList<Object> queryList = monitorTripReportDao.pageQuery(sql, pageQuery1);
            if (queryList != null && queryList.size() > 0) {
                Map<String, MonitorTripVehicleVO> map = new HashMap<>(queryList.size());
                for (Object obj : queryList) {
                    Object[] objs = (Object[]) obj;
                    MonitorTripVehicleVO tripVehicleVO = new MonitorTripVehicleVO();
                    BeanUtils.copyProperties(objs[0], tripVehicleVO);
                    boolean exists = false;
                    for (MonitorTripVehicleVO tripVehicleVO2 : pageList) {
                        if (tripVehicleVO2.getTripId().equals(tripVehicleVO.getTripId())) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) {
                        continue;
                    }
                    tripVehicleVO.setCheckinUserName((objs[1] == null) ? "" : (String) objs[1]);
                    tripVehicleVO.setCheckoutUserName((objs[2] == null) ? "" : (String) objs[2]);

                    List<CommonVehicleDriverVO> commonVehicleDriverBOList = findAllVehicleDriverByPageQuery(pageQuery1,
                            tripVehicleVO.getTripId());
                    tripVehicleVO.setCommonVehicleDriverList(commonVehicleDriverBOList);
                    copyProperties(tripVehicleVO, commonVehicleDriverBOList);
                    map.put(tripVehicleVO.getTripId(), tripVehicleVO);
                }

                // 按顺序获取
                for (String tripId : tripIds) {
                    MonitorTripVehicleVO tripVehicleVO = map.get(tripId);
                    pageList.add(tripVehicleVO);
                }
            }
        }

        return fromObjectList(pageList, null, false);
    }

    /**
     * 从所有车辆中拷贝属性到VO类
     * 
     * @param tripVehicleVO
     * @param commonVehicleDriverBOList
     */
    private void copyProperties(MonitorTripVehicleVO tripVehicleVO,
            List<CommonVehicleDriverVO> commonVehicleDriverBOList) {
        if (null != commonVehicleDriverBOList && commonVehicleDriverBOList.size() > 0) {
            List<String> vehiclePlateNumber = new ArrayList<>();
            List<String> trackingDeviceNumber = new ArrayList<>();
            List<String> esealNumber = new ArrayList<>();
            List<String> sensorNumber = new ArrayList<>();
            for (CommonVehicleDriverVO vehicleDriverVO : commonVehicleDriverBOList) {
                if (NuctechUtil.isNotNull(vehicleDriverVO.getVehiclePlateNumber())) {
                    vehiclePlateNumber.add(vehicleDriverVO.getVehiclePlateNumber());
                }
                if (NuctechUtil.isNotNull(vehicleDriverVO.getTrackingDeviceNumber())) {
                    trackingDeviceNumber.add(vehicleDriverVO.getTrackingDeviceNumber());
                }
                if (NuctechUtil.isNotNull(vehicleDriverVO.getEsealNumber())) {
                    esealNumber.add(vehicleDriverVO.getEsealNumber());
                }
                if (NuctechUtil.isNotNull(vehicleDriverVO.getSensorNumber())) {
                    sensorNumber.add(vehicleDriverVO.getSensorNumber());
                }
            }
            tripVehicleVO.setVehiclePlateNumber(StringUtils.join(vehiclePlateNumber, ","));
            tripVehicleVO.setTrackingDeviceNumber(StringUtils.join(trackingDeviceNumber, ","));
            tripVehicleVO.setEsealNumber(StringUtils.join(esealNumber, ","));
            tripVehicleVO.setSensorNumber(StringUtils.join(sensorNumber, ","));
        }
    }

    /**
     * 根据tripId获取指定的车辆信息
     * 
     * @param tripId
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes" })
    public List<CommonVehicleDriverVO> findAllVehicleDriverByPageQuery(PageQuery<Map> pageQuery, String tripId) {
        HashMap<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("tripId", tripId);
        HashMap<String, Object> propertiesLikeMap = new HashMap<>();
        /*
         * if (NuctechUtil.isNotNull(filters.get("vehiclePlateNumber"))) {
         * propertiesLikeMap.put("vehiclePlateNumber",
         * filters.get("vehiclePlateNumber")); } if
         * (NuctechUtil.isNotNull(filters.get("driverName"))) {
         * propertiesLikeMap.put("driverName", filters.get("driverName")); }
         */

        List<LsCommonVehicleBO> commonVehicleBOList = commonVehicleDao.findAllBy(propertiesMap, propertiesLikeMap,
                null);
        List<CommonVehicleDriverVO> vehicleDriverVOList = new ArrayList<>();
        if (commonVehicleBOList != null && commonVehicleBOList.size() > 0) {
            for (LsCommonVehicleBO commonVehicleBO : commonVehicleBOList) {
                CommonVehicleDriverVO vehicleDriverVO = new CommonVehicleDriverVO();
                BeanUtils.copyProperties(commonVehicleBO, vehicleDriverVO);
                LsCommonDriverBO commonDriverBO = commonDriverDao.findById(commonVehicleBO.getDriverId());
                if (null != commonDriverBO) {
                    BeanUtils.copyProperties(commonDriverBO, vehicleDriverVO);
                }
                vehicleDriverVOList.add(vehicleDriverVO);
            }
        }

        return vehicleDriverVOList;
    }

    /**
     * 拼接带in的条件语句：value1,value2,value3 --> and xxx in ('xxx','xxx')
     * 
     * @param columnName
     *        字段名
     * @param value
     *        值（value1,value2,value3...）
     * @return
     */
    private String joinInCondition(String columnName, String value) {
        StringBuffer inCondition = new StringBuffer();
        String[] values = filterStr(value).split("\\s*,\\s*");
        if (values != null && values.length > 0) {
            inCondition.append(" and ").append(columnName).append(" in ('").append(value.replaceAll("\\s*,\\s*", "','"))
                    .append("')");
        }
        return inCondition.toString();
    }

    /**
     * 过滤查询条件的特殊字符
     * 
     * @param str
     */
    protected String filterStr(String str) {
        return str.replaceAll("\\[", "[[]").replaceAll("\\^", "[^]").replaceAll("_", "[_]").replaceAll("%", "[%]");
    }

    public List<?> findStatisticData() {
        return monitorTripReportDao.findTripData();
    }

}
