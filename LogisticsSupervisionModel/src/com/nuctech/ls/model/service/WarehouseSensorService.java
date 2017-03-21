package com.nuctech.ls.model.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseSensorBO;
import com.nuctech.ls.model.dao.WarehouseSensorDao;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.SensorDepartmentVO;
import com.nuctech.util.Constant;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 作者： 赵苏阳
 *
 * 描述：
 * <p>
 * 传感器增删改查 Service
 * </p>
 * 创建时间：2016年5月28日
 */

@Service
@Transactional
public class WarehouseSensorService extends LSBaseService {

    @Resource
    private WarehouseSensorDao sensorDao;

    /**
     * 添加传感器
     * 
     * @param warehouseSensor
     */
    public void add(LsWarehouseSensorBO warehouseSensor) {
        sensorDao.save(warehouseSensor);
    }

    /**
     * 根据id删除传感器
     * 
     * @param sensorId
     */
    public void deleteById(String sensorId) {
        sensorDao.deleteById(sensorId);
    }

    /**
     * 修改传感器
     * 
     * @param warehouseSensor
     */
    public void modify(LsWarehouseSensorBO warehouseSensor) {
        sensorDao.merge(warehouseSensor);
    }

    /**
     * 根据Id查询
     * 
     * @param sensorId
     * @return
     */
    public LsWarehouseSensorBO findById(String sensorId) {
        return sensorDao.findById(sensorId);
    }

    /**
     * 单表查询 返回JSON
     * 
     * @param pageQuery
     * @return
     */
    /*
     * public PageList findWarehouseSensor(PageQuery<Map> pageQuery) { String
     * queryString = "select t from LsWarehouseSensorBO t where 1=1 " +
     * "/~ and t.sensorNumber = '[sensorNumber]' ~/" +
     * "/~ and t.belongTo = '[belongTo]' ~/" +
     * "/~ and t.sensorStatus = '[sensorStatus]' ~/" +
     * "/~ and t.sensorType = '[sensorType]' ~/" +
     * "/~ order by [sortColumns] ~/";
     * return sensorDao.pageQuery(queryString, pageQuery); }
     * public JSONObject fromObjectList(PageQuery<Map> pageQuery,JsonConfig
     * jsonConfig,boolean ignoreDefaultExcludes) { PageList<LsWarehouseSensorBO>
     * pageList = findWarehouseSensor(pageQuery); return
     * fromObjectList(pageList, jsonConfig, ignoreDefaultExcludes); }
     */
    // 传感器表与组织机构表关联查询
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public JSONObject fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        HttpSession session = ServletActionContext.getRequest().getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
        String organizationId = sessionUser.getOrganizationId();
        String queryString = "";
        queryString = "select e,d from LsWarehouseSensorBO e,LsSystemDepartmentBO d where 1=1"
                + " and e.belongTo=d.organizationId ";
        //口岸用户只能看到属于自身口岸的关锁
        if(sessionUser.getRoleId().equals(RoleType.portUser.getType())){
            queryString +="and d.organizationId='"+organizationId+"'"; 
        }
        queryString +="/~ and e.sensorNumber like '%[sensorNumber]%' ~/"
                + "/~ and e.sensorStatus like '%[sensorStatus]%' ~/" + "/~ and e.belongTo like '%[belongTo]%' ~/"
                + "/~ and e.sensorType like '%[sensorType]%' ~/" + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = sensorDao.pageQuery(queryString, pageQuery);
        PageList<SensorDepartmentVO> pageList = new PageList<SensorDepartmentVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                SensorDepartmentVO sensorDepartmentVO = new SensorDepartmentVO();
                BeanUtils.copyProperties((LsWarehouseSensorBO) objs[0], sensorDepartmentVO);
                BeanUtils.copyProperties((LsSystemDepartmentBO) objs[1], sensorDepartmentVO);
                pageList.add(sensorDepartmentVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 根据传感器号查询记录
     * 
     * @param sensorNumber
     * @return
     */
    public LsWarehouseSensorBO findBySensorNumber(String sensorNumber) {
        return sensorDao.findByProperty("sensorNumber", sensorNumber);
    }

    // 查询数据库中所有的关锁记录
    public List<LsWarehouseSensorBO> findAllelock() {
        return (List<LsWarehouseSensorBO>) sensorDao.findAll();
    }

    // 查询传感器表和机构表
    public List<SensorDepartmentVO> getSensorDepartmentlist(String organizationId) {
        return sensorDao.getSensorSystemList(organizationId);
    }

    // 调度的随机查询
    @SuppressWarnings("rawtypes")
    public List getSensor(int m, String organizationId) {
        return sensorDao.getSensorList(m, organizationId);
    }

    public List<LsWarehouseSensorBO> statisSensor(String portName,String seriesName) {
        return sensorDao.findPortSensor(portName,seriesName);
    }

}
