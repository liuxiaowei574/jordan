/**
 * 
 */
package com.nuctech.ls.model.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.ls.model.dao.MonitorVehicleGpsDao;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @author sunming
 *
 */
@Service
@Transactional
public class MonitorVehicleGpsService extends LSBaseService {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Resource
    public MonitorVehicleGpsDao monitorVehicleGpsDao;

    /**
     * 保存MonitorVehicleGPS信息
     * 
     * @param monitorVehicleGpsBO
     * @return
     */
    public void saveMonitorVehicleGps(LsMonitorVehicleGpsBO monitorVehicleGpsBO, String tableName) {
        monitorVehicleGpsBO.setGpsId(generatePrimaryKey());
        // 查找表是否存在
        boolean isExist = monitorVehicleGpsDao.findTableNameExsitorNot(tableName);
        if (isExist) {
            // 若表存在则直接插入数据
            logger.info("追踪设备对应的表" + tableName + "存在");
            monitorVehicleGpsDao.insertVehicleGpsData(tableName, monitorVehicleGpsBO);
            logger.info("插入" + tableName + "GPS坐标数据");
        } else {
            // 若表不存在，先创建表再插入数据
            logger.info("追踪设备对应的表" + tableName + "不存在");
            boolean create = monitorVehicleGpsDao.createVehicleGpsTable(tableName);
            if (create) {
                logger.info("追踪设备对应的表" + tableName + "创建成功");
                monitorVehicleGpsDao.insertVehicleGpsData(tableName, monitorVehicleGpsBO);
                logger.info("插入" + tableName + "GPS坐标数据");
            } else {
                logger.info("追踪设备对应的表" + tableName + "创建失败");
            }
        }
    }

    /**
     * 查找表是否存在
     * 
     * @param tableName
     * @return
     */
    public boolean findTableNameExsitorNot(String tableName) {
        return monitorVehicleGpsDao.findTableNameExsitorNot(tableName);
    }

    /**
     * 查询设备号开头的表
     * 
     * @param deviceNum
     * @return
     */
    public List<String> findTableNamesByDeviceNum(String deviceNum) {
        return monitorVehicleGpsDao.findTableNamesByDeviceNum(deviceNum);
    }

    /**
     * 查找所有日志
     * 
     * @param tablePrefix
     *        表名前缀，是车载台表还是设备表
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    public JSONObject findAll(String tablePrefix, PageQuery<Map> pageQuery, JsonConfig jsonConfig,
            boolean ignoreDefaultExcludes) {
        PageList<LsMonitorVehicleGpsBO> pageList = monitorVehicleGpsDao.findAll(tablePrefix, pageQuery);
        return fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    @SuppressWarnings("rawtypes")
    public List getTrackUnitlist(int time) {
        return monitorVehicleGpsDao.findTrackStatusList(time);
    }

    public LsMonitorVehicleGpsBO findById(String id) {
        return monitorVehicleGpsDao.findByGpsId(id);
    }

}
