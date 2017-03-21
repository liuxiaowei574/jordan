package com.nuctech.ls.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseTrackUnitBO;
import com.nuctech.ls.model.dao.WarehouseTrackUnitDao;
import com.nuctech.ls.model.vo.warehouse.TrackDepartmentVO;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 作者： 赵苏阳
 *
 * 描述：
 * <p>
 * 车载台增删改查 Service
 * </p>
 * 创建时间：2016年6月22日
 */

@Service
@Transactional
public class WarehouseTrackUnitService extends LSBaseService {

    @Resource
    private WarehouseTrackUnitDao trackUnitDao;

    /**
     * 添加车载台
     * 
     * @param lsWarehouseTrackUnitBO
     */
    public void add(LsWarehouseTrackUnitBO lsWarehouseTrackUnitBO) {
        trackUnitDao.save(lsWarehouseTrackUnitBO);
    }

    // 返回JSON单表查询
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONObject fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        String queryString = "select e,d from LsWarehouseTrackUnitBO e,LsSystemDepartmentBO d where 1=1"
                + " and e.belongTo=d.organizationId " + "/~ and e.trackUnitNumber like '%[trackUnitNumber]%' ~/"
                + "/~ and e.simCard like '%[simCard]%' ~/" + "/~ and e.belongTo like '%[belongTo]%' ~/"
                + "/~ and e.interval like  '%[interval]%' ~/"
                + "/~ and e.trackUnitStatus like  '%[trackUnitStatus]%' ~/" + "/~ order by [sortColumns] ~/";
        PageList<Object> queryList = trackUnitDao.pageQuery(queryString, pageQuery);
        PageList<TrackDepartmentVO> pageList = new PageList<TrackDepartmentVO>();
        if (queryList != null && queryList.size() > 0) {
            for (Object obj : queryList) {
                Object[] objs = (Object[]) obj;
                TrackDepartmentVO trackDepartmentVO = new TrackDepartmentVO();
                BeanUtils.copyProperties((LsWarehouseTrackUnitBO) objs[0], trackDepartmentVO);
                BeanUtils.copyProperties((LsSystemDepartmentBO) objs[1], trackDepartmentVO);
                pageList.add(trackDepartmentVO);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    /**
     * 根据车载台的id查找
     * 
     * @param trackUnitId
     * @return
     */
    public LsWarehouseTrackUnitBO findById(String trackUnitId) {
        return trackUnitDao.findById(trackUnitId);
    }

    /**
     * 修改车载台
     * 
     * @param warehouseElock
     */
    public void modify(LsWarehouseTrackUnitBO lsWarehouseTrackUnitBO) {
        trackUnitDao.merge(lsWarehouseTrackUnitBO);
    }

    /**
     * 根据Id删除车载台
     * 
     * @param elockId
     */
    public void deleteById(String trackUnitIds) {
        trackUnitDao.deleteById(trackUnitIds);
    }

    /**
     * 查找所有的车载台
     * 
     * @return
     */
    public List<LsWarehouseTrackUnitBO> findAllTrackUnits() {
        return trackUnitDao.findAll();
    }

    /**
     * 根据belongTo查找所有的车载台
     * 
     * @return
     */
    public List<LsWarehouseTrackUnitBO> findTrackUnitsByPortId(String portId) {
        HashMap<String, String> orderby = new HashMap<>();
        orderby.put("trackUnitNumber", "asc");
        return trackUnitDao.findAllBy("belongTo", portId, orderby);
    }

    /**
     * 根据车载台号查询记录
     * 
     * @param sensorNumber
     * @return
     */
    public LsWarehouseTrackUnitBO findBySensorNumber(String trackUnitNumber) {
        return trackUnitDao.findByProperty("trackUnitNumber", trackUnitNumber);
    }

    /**
     * 查询所有的车载台
     */
    public List<LsWarehouseTrackUnitBO> findAllTrackUnit() {
        return trackUnitDao.findAll();
    }

}
