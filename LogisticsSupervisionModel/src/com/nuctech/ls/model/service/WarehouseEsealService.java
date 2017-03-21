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
import com.nuctech.ls.model.bo.warehouse.LsWarehouseEsealBO;
import com.nuctech.ls.model.dao.WarehouseEsealDao;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.EsealDepartementVO;
import com.nuctech.util.Constant;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 作者： 赵苏阳
 *
 * 描述：
 * <p>
 * 子锁增删改查 Service
 * </p>
 * 创建时间：2016年5月27日
 */
@Service
@Transactional
public class WarehouseEsealService extends LSBaseService {

    @Resource
    private WarehouseEsealDao warehouseEsealDao;

    /**
     * 添加子锁
     * 
     * @param warehouseEseal
     */
    public void add(LsWarehouseEsealBO warehouseEseal) {
        warehouseEsealDao.save(warehouseEseal);
    }

    /**
     * 根据Id删除子锁
     * 
     * @param esealId
     */
    public void deleteById(String esealId) {
        warehouseEsealDao.deleteById(esealId);
    }

    /**
     * 修改子锁
     * 
     * @param warehouseEseal
     */
    public void modify(LsWarehouseEsealBO warehouseEseal) {
        warehouseEsealDao.merge(warehouseEseal);
    }

    /**
     * 根据子锁Id查询
     * 
     * @param esealId
     * @return
     */
    public LsWarehouseEsealBO findById(String esealId) {
        return warehouseEsealDao.findById(esealId);
    }

    /**
     * 查找所有的子锁
     * 
     * @return
     */
    public List<LsWarehouseEsealBO> findAllEseal() {
        return (List<LsWarehouseEsealBO>) warehouseEsealDao.findAll();
    }

    /**
     * 单表查询 返回JSON
     * 
     * @param pageQuery
     * @return
     */
    /*
     * public PageList findWarehouseEseal(PageQuery<Map> pageQuery) { String
     * queryString =
     * "select warehouseEsealBO from LsWarehouseEsealBO warehouseEsealBO where 1=1 "
     * + "/~ and warehouseEsealBO.esealId = '[esealId]' ~/" +
     * "/~ and warehouseEsealBO.esealNumber = '[esealNumber]' ~/" +
     * "/~ and warehouseEsealBO.belongTo = '[belongTo]' ~/" +
     * "/~ and warehouseEsealBO.esealStatus = '[esealStatus]' ~/" +
     * "/~ and warehouseEsealBO.createUser = '[createUser]' ~/" +
     * "/~ and warehouseEsealBO.createTime = '[createTime]' ~/" +
     * "/~ order by [sortColumns] ~/";
     * return warehouseEsealDao.pageQuery(queryString, pageQuery); }
     * public JSONObject fromObjectList(PageQuery<Map> pageQuery,JsonConfig
     * jsonConfig,boolean ignoreDefaultExcludes) { PageList<LsWarehouseEsealBO>
     * pageList = findWarehouseEseal(pageQuery); return fromObjectList(pageList,
     * jsonConfig, ignoreDefaultExcludes); }
     */

    /**
     * 两个表关联查询
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public JSONObject fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        HttpSession session = ServletActionContext.getRequest().getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute(Constant.SESSION_USER);
        String organizationId = sessionUser.getOrganizationId();
        String queryString = "";
        queryString = "select e,d from LsWarehouseEsealBO e,LsSystemDepartmentBO d where 1=1"
                + " and e.belongTo=d.organizationId ";
        //口岸用户只能看到属于自身口岸的子锁
        if(sessionUser.getRoleId().equals(RoleType.portUser.getType())){
            queryString +="and d.organizationId='"+organizationId+"'"; 
        }
        queryString += "/~ and e.esealNumber like '%[esealNumber]%' ~/"
                + "/~ and e.esealStatus like '%[esealStatus]%' ~/" + "/~ and e.belongTo like '%[belongTo]%' ~/"
                + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = warehouseEsealDao.pageQuery(queryString, pageQuery);
        PageList<EsealDepartementVO> pageList = new PageList<EsealDepartementVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                EsealDepartementVO esealDepartmentVO = new EsealDepartementVO();
                BeanUtils.copyProperties((LsWarehouseEsealBO) objs[0], esealDepartmentVO);
                BeanUtils.copyProperties((LsSystemDepartmentBO) objs[1], esealDepartmentVO);
                pageList.add(esealDepartmentVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 查询指定口岸的子锁详细信息
     * 
     * @param pageQuery
     * @param jsonConfig
     * @param ignoreDefaultExcludes
     * @return
     */
    public List<LsWarehouseEsealBO> statisEseal(String portName,String seriesName) {
        return warehouseEsealDao.findPortEseal(portName,seriesName);
    }

    /**
     * 根据子锁号查询记录
     * 
     * @param esealNumber
     * @return
     */
    public LsWarehouseEsealBO findByEsealNumber(String esealNumber) {
        return warehouseEsealDao.findByProperty("esealNumber", esealNumber);
    }

    // 调度的随机查询
    @SuppressWarnings("rawtypes")
    public List getEseal(int m, String organizationId) {
        return warehouseEsealDao.getEsealList(m, organizationId);
    }

    public List<EsealDepartementVO> getEsealDepartmentlist(String organizationId) {
        return warehouseEsealDao.getEsealSystem(organizationId);
    }

}
