package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.dao.MonitorRouteAreaDao;

@Service
@Transactional
public class MonitorRouteAreaService extends LSBaseService {

	@Resource
	private MonitorRouteAreaDao monitorRouteAreaDao;
	
	
	public List<LsMonitorRouteAreaBO> findAllRouteAreas(String menuType) throws Exception{
		return monitorRouteAreaDao.findAllRouteAreas(menuType);
	}
	
	/**
     * 插入线路区域
     * @param lsMonitorRouteAreaBO
     */
    public void addMonitorRouteArea(LsMonitorRouteAreaBO lsMonitorRouteAreaBO) throws Exception{
    	monitorRouteAreaDao.addMonitorRouteArea(lsMonitorRouteAreaBO);
    }
    
    /**
     * 更新线路区域
     * @param lsMonitorRouteAreaBO
     */
    public void updateaddMonitorRouteArea(LsMonitorRouteAreaBO lsMonitorRouteAreaBO) throws Exception{
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
       
       
}
