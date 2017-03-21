package com.nuctech.ls.model.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.sla.LsVehiclePunishBo;
import com.nuctech.ls.model.dao.VehiclePunishDao;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

@Service
@Transactional
public class VehiclePunishService extends LSBaseService {

    @Resource
    private VehiclePunishDao vehiclePunishDao;

    // 返回JSON单表查询
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONArray fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        String queryString = "select s from LsVehiclePunishBo s where 1=1"
                + "/~ and s.vpunishType like '%[vpunishType]%' ~/" + "/~ and s.vpunishValue like '%[vpunishValue]%' ~/"
                + "/~ order by [sortColumns] ~/";
        PageList<Object> pageList = vehiclePunishDao.pageQuery(queryString, pageQuery);
        return fromArrayList(pageList, jsonConfig, ignoreDefaultExcludes);
    }

    /**
     * 增加车辆罚款类型
     * 
     * @param warehouseElock
     */
    public void add(LsVehiclePunishBo lsVehiclePunishBo) {
        vehiclePunishDao.save(lsVehiclePunishBo);
    }

    /**
     * 根据Id查询
     * 
     * @param elockId
     * @return
     */
    public LsVehiclePunishBo findById(String id) {
        return vehiclePunishDao.findById(id);
    }

    /**
     * 修改
     * 
     * @param LsVehiclePunishBo
     */
    public void modify(LsVehiclePunishBo lsVehiclePunishBo) {
        vehiclePunishDao.merge(lsVehiclePunishBo);
    }

    /**
     * 删除
     * 
     * @param LsVehiclePunishBo
     */
    public void deleteById(String punishId) {
        vehiclePunishDao.deleteById(punishId);
    }

    /**
     * 查出所有记录
     */
    public List<LsVehiclePunishBo> findAll() {
        return (vehiclePunishDao.findAll());
    }

    /**
     * 根据罚款类型查出车辆罚款记录
     * 
     * @return
     */
    public LsVehiclePunishBo findByPro(String type) {
        return (vehiclePunishDao.findByPropertyName(type));

    }
}
