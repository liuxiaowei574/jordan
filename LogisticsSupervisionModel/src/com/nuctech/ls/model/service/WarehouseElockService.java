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
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.dao.WarehouseElockDao;
import com.nuctech.ls.model.util.DeviceStatus;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.ElockDepartmentVO;
import com.nuctech.ls.model.vo.warehouse.ElockDispatchReportVO;
import com.nuctech.util.Constant;
import com.nuctech.util.MessageResourceUtil;
import com.nuctech.util.NuctechUtil;

import jcifs.util.transport.Request;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 作者： 赵苏阳
 *
 * 描述：
 * <p>
 * 关锁增删改查 Service
 * </p>
 * 创建时间：2016年5月18日
 */

@Service
@Transactional
public class WarehouseElockService extends LSBaseService {

    @Resource
    private WarehouseElockDao warehouseDao;
    @Resource
    private SystemUserService systemUserService;

    /**
     * 增加关锁
     * 
     * @param warehouseElock
     */
    public void add(LsWarehouseElockBO warehouseElock) {
        warehouseDao.save(warehouseElock);
    }

    /**
     * 删除关锁
     * 
     * @param warehouseElock
     */
    public void delete(LsWarehouseElockBO warehouseElock) {
        warehouseDao.delete(warehouseElock);
    }

    public void deleteById(String elockId) {
        warehouseDao.deleteById(elockId);
    }

    /**
     * 修改关锁
     * 
     * @param warehouseElock
     */
    public void modify(LsWarehouseElockBO warehouseElock) {
        warehouseDao.update(warehouseElock);
    }

    /**
     * 根据关锁Id查询
     * 
     * @param elockId
     * @return
     */
    public LsWarehouseElockBO findById(String elockId) {
        return warehouseDao.findById(elockId);
    }

    // 查询数据库中所有的关锁记录
    /*
     * public List<LsWarehouseElockBO> findAllelock(){ return
     * (List<LsWarehouseElockBO>) warehouseDao.findAll(); }
     */
    // 查询关锁表和机构表
    public List<ElockDepartmentVO> getElockDapatmentlist(String organizationId) {
        return warehouseDao.getElockSystem(organizationId);
    }

    /**
     * 返回JSON单表查询
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        HttpSession session = ServletActionContext.getRequest().getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
        String organizationId = sessionUser.getOrganizationId();
        String queryString = "";
        queryString =  "select e,d from LsWarehouseElockBO e,LsSystemDepartmentBO d" + " where 1=1"
                + " and e.belongTo=d.organizationId ";
        //口岸用户只能看到属于自身口岸的关锁
        if(sessionUser.getRoleId().equals(RoleType.portUser.getType())){
            queryString +="and d.organizationId='"+organizationId+"'"; 
        }
        queryString +="/~ and e.elockNumber like '%[elockNumber]%' ~/"
                + "/~ and e.simCard like '%[simCard]%' ~/" + "/~ and e.belongTo like '%[belongTo]%' ~/"
                + "/~ and e.interval like  '%[interval]%' ~/" + "/~ and e.elockStatus like  '%[elockStatus]%' ~/"
                + "/~ and e.simCard like '%[simCard]%' ~/" + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = warehouseDao.pageQuery(queryString, pageQuery);
        PageList<ElockDepartmentVO> pageList = new PageList<ElockDepartmentVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                ElockDepartmentVO elockDepartmentVO = new ElockDepartmentVO();
                BeanUtils.copyProperties(objs[1], elockDepartmentVO);
                BeanUtils.copyProperties(objs[0], elockDepartmentVO);
                if (NuctechUtil.isNotNull(elockDepartmentVO.getLastUser())) {
                    LsSystemUserBO systemUser = systemUserService.findById(elockDepartmentVO.getLastUser());
                    if (NuctechUtil.isNotNull(systemUser)) {
                        elockDepartmentVO.setLastUserName(systemUser.getUserName());
                    }
                }
                pageList.add(elockDepartmentVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 设备统计，显示指定口岸的关锁信息
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject statiselock(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes,
            String portName,String seriesName) {
    	String elockStatus = "";
    	String queryString = "select e,d from LsWarehouseElockBO e,LsSystemDepartmentBO d" + " where 1=1"
                 + " and e.belongTo=d.organizationId " + " and d.organizationName='" + portName + "'";
    	String keyongElock = MessageResourceUtil.getMessageInfo("statistic.elock.keyong");//可用关锁
    	String zaituElock = MessageResourceUtil.getMessageInfo("statistic.elock.zaitu");//在途关锁
    	String sunhuaiElock = MessageResourceUtil.getMessageInfo("statistic.elock.sunhuai");//损坏关锁
    	String weixiuElock = MessageResourceUtil.getMessageInfo("statistic.elock.weixiu");//维修关锁
    	String baofeiElock = MessageResourceUtil.getMessageInfo("statistic.elock.baofei");//报废关锁
    	
    	if(seriesName.equals(keyongElock)){
    		elockStatus = DeviceStatus.Normal.getText();
    		queryString +=" and e.elockStatus='" +elockStatus+"'";
    	}
    	if(seriesName.equals(zaituElock)){
    		elockStatus = DeviceStatus.Inway.getText();
    		queryString +=" and e.elockStatus='" +elockStatus+"'";
    	}
    	if(seriesName.equals(sunhuaiElock)){
    		elockStatus = DeviceStatus.Destory.getText();
    		queryString +=" and e.elockStatus='" +elockStatus+"'";
    	}
    	if(seriesName.equals(weixiuElock)){
    		elockStatus = DeviceStatus.Maintain.getText();
    		queryString +=" and e.elockStatus='" +elockStatus+"'";
    	}
    	if(seriesName.equals(baofeiElock)){
    		elockStatus = DeviceStatus.Scrap.getText();
    		queryString +=" and e.elockStatus='" +elockStatus+"'";
    	}
    	queryString += "/~ and e.elockNumber like '%[elockNumber]%' ~/" + "/~ and e.simCard like '%[simCard]%' ~/"
                + "/~ and e.belongTo like '%[belongTo]%' ~/" + "/~ and e.interval like  '%[interval]%' ~/"
                + "/~ and e.elockStatus like  '%[elockStatus]%' ~/" + "/~ and e.simCard like '%[simCard]%' ~/"
                + "/~ order by [sortColumns] ~/";
    	
        PageList<Object> queryList = warehouseDao.pageQuery(queryString, pageQuery);
        PageList<ElockDepartmentVO> pageList = new PageList<ElockDepartmentVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                ElockDepartmentVO elockDepartmentVO = new ElockDepartmentVO();
                BeanUtils.copyProperties(objs[1], elockDepartmentVO);
                BeanUtils.copyProperties(objs[0], elockDepartmentVO);
                if (NuctechUtil.isNotNull(elockDepartmentVO.getLastUser())) {
                    LsSystemUserBO systemUser = systemUserService.findById(elockDepartmentVO.getLastUser());
                    if (NuctechUtil.isNotNull(systemUser)) {
                        elockDepartmentVO.setLastUserName(systemUser.getUserName());
                    }
                }
                pageList.add(elockDepartmentVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 
     * 关锁调度报告
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject elockReport(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        String queryString = "select e,d,ea,ep,dp "
                + "from LsWarehouseElockBO e,LsSystemDepartmentBO d,LsWarehouseDeviceApplicationBO ea,"
                + "LsWarehouseDeviceDispatchBO ep,LsWarehouseDispatchDetailBO dp" + "  where dp.deviceId=e.elockId "
                + " and ea.applicationId=ep.applicationId " + " and ep.dispatchId=dp.dispatchId "
                + " and ep.fromPort=d.organizationId " + "/~ and e.simCard like '%[simCard]%' ~/"
                + "/~ and ep.fromPort = '[fromPort]' ~/" + "/~ and ep.toPort = '[toPort]' ~/"
                + "/~ and e.elockNumber like '%[elockNumber]%' ~/"
                + "/~ and dp.recviceTime between '%[applyTime]%' and '%[recviceTime]%' ~/"
                + "/~ and ea.applyTime like '%[applyTime]%' and '%[recviceTime]%' ~/" + "/~ order by [sortColumns] ~/";
        // "select e,d from LsWarehouseElockBO e,LsSystemDepartmentBO d where
        // 1=1"
        // + " and e.belongTo=d.organizationId "
        // +"/~ and e.elockNumber like '%[elockNumber]%' ~/"
        // +"/~ and e.simCard like '%[simCard]%' ~/"
        // +"/~ and e.belongTo like '%[belongTo]%' ~/"
        // +"/~ and e.interval like '%[interval]%' ~/"
        // +"/~ and e.elockStatus like '%[elockStatus]%' ~/"
        // +"/~ and e.simCard like '%[simCard]%' ~/"
        // + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = warehouseDao.pageQuery(queryString, pageQuery);
        // try {
        // queryList = warehouseDao.pageQuery(queryString, pageQuery);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        PageList<ElockDispatchReportVO> pageList = new PageList<ElockDispatchReportVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                ElockDispatchReportVO elockDepartmentVO = new ElockDispatchReportVO();
                BeanUtils.copyProperties(objs[1], elockDepartmentVO);

                BeanUtils.copyProperties(objs[2], elockDepartmentVO);

                BeanUtils.copyProperties(objs[3], elockDepartmentVO);
                BeanUtils.copyProperties(objs[4], elockDepartmentVO);
                BeanUtils.copyProperties(objs[0], elockDepartmentVO);
                pageList.add(elockDepartmentVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    // 调度的随机查询
    @SuppressWarnings("rawtypes")
    public List getELock(int m, String organizationId) {
        return warehouseDao.getList(m, organizationId);
    }

    /**
     * 根据关锁号查询记录
     * 
     * @param elockNumber
     * @return
     */
    public LsWarehouseElockBO findByElockNumber(String elockNumber) {
        return warehouseDao.findByProperty("elockNumber", elockNumber);
    }

    /**
     * 查找正在使用的设备
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findInWay() {
        return (warehouseDao.findElockInWay());
    }

}
