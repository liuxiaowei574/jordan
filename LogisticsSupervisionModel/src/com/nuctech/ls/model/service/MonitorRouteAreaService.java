package com.nuctech.ls.model.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.dao.MonitorRouteAreaDao;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service
@Transactional
public class MonitorRouteAreaService extends LSBaseService {

    @Resource
    private MonitorRouteAreaDao monitorRouteAreaDao;

    public List<LsMonitorRouteAreaBO> findAllRouteAreas(String menuType, String routeName) throws Exception {
        return monitorRouteAreaDao.findAllRouteAreas(menuType, routeName);
    }

    /**
     * 插入线路区域
     * 
     * @param lsMonitorRouteAreaBO
     */
    public void addMonitorRouteArea(LsMonitorRouteAreaBO lsMonitorRouteAreaBO) throws Exception {
        monitorRouteAreaDao.addMonitorRouteArea(lsMonitorRouteAreaBO);
    }

    /**
     * 更新线路区域
     * 
     * @param lsMonitorRouteAreaBO
     */
    public void updateaddMonitorRouteArea(LsMonitorRouteAreaBO lsMonitorRouteAreaBO) throws Exception {
        monitorRouteAreaDao.merge(lsMonitorRouteAreaBO);
    }

    /**
     * 根据编号获取区域或线路信息
     */
    public LsMonitorRouteAreaBO findMonitorRouteAreaById(String id) {
        return monitorRouteAreaDao.findById(id);
    }

    public int delRouteAreaByRAIds(String ids) {
        return monitorRouteAreaDao.delRouteAreaByRAIds(ids);
    }

    public List<LsMonitorRouteAreaBO> findAllPatrolArea(String routeAreaType) {
        return monitorRouteAreaDao.findAllPatrolArea(routeAreaType);
    }

    /**
     * 根据线路区域名称进行模糊查询
     * 
     * @param routeAreaName
     * @return
     */
    public List<LsMonitorRouteAreaBO> findRouteAreaByName(String routeAreaName) {
        return monitorRouteAreaDao.findRouteAreaByName(routeAreaName);
    }

    /**
     * 查找有效路线
     * 
     * @param pageQuery
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public JSONObject findRouteAreas(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        String queryString = "select d from LsMonitorRouteAreaBO d " + " where 1=1 and routeAreaStatus = '0' "
                + "/~ and d.routeAreaId = '[routeAreaId]' ~/" + "/~ and d.routeAreaName like '%[routeAreaName]%' ~/"
                + "/~ and d.routeAreaType = '[routeAreaType]' ~/" + "/~ and d.belongToPort = '[belongToPort]' ~/"
                + "/~ and d.startId = '[startId]' ~/" + "/~ and d.endId = '[endId]' ~/"
                + "/~ and d.routeDistance = [routeDistance] ~/" + "/~ order by [sortColumns] ~/";

        PageList<LsMonitorRouteAreaBO> pageList = monitorRouteAreaDao.pageQuery(queryString, pageQuery);
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 根据所属节点进行查询
     * 
     * @param belongToPort
     * @return
     */
    public List<LsMonitorRouteAreaBO> findRouteAreaByBelongTO(String belongToPort) {
        return monitorRouteAreaDao.findAllBy("belongToPort", belongToPort);
    }

    /**
     * 查询所有的规划区域
     */
    public List<LsMonitorRouteAreaBO> findAllArea() {
        return monitorRouteAreaDao.findAll();
    }

    /**
     * 根据所属节点查询target zoon
     * 
     * @param belongToPort
     * @return
     */
    public List<LsMonitorRouteAreaBO> findTargetZoonByPort(String belongToPort) {
        return monitorRouteAreaDao.findTargetZoonByPort(belongToPort);
    }

    /**
     * 根据所属节点查询监管区域
     * 
     * @param belongToPort
     * @return
     */
    public List<LsMonitorRouteAreaBO> findJGQYByPort(String belongToPort) {
        return monitorRouteAreaDao.findJGQYByPort(belongToPort);
    }
}
