package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.monitor.LsMonitorLandMarkerBO;
import com.nuctech.ls.model.dao.MonitorLandMarkerDao;
import com.nuctech.util.NuctechUtil;

@Service
@Transactional
public class MonitorLandMarkerService extends LSBaseService {

    @Resource
    private MonitorLandMarkerDao landMarkerDao;

    private List<LsMonitorLandMarkerBO> lsMonitorLandMarkerBOs;

    @SuppressWarnings("unchecked")
    public List<LsMonitorLandMarkerBO> findAllLandMarkers(String landName) {
        if (NuctechUtil.isNull(landName)) {
            this.lsMonitorLandMarkerBOs = landMarkerDao.findAll();
        } else {
            this.lsMonitorLandMarkerBOs = landMarkerDao.findByName(landName);
        }

        return this.lsMonitorLandMarkerBOs;
    }

    public void addLanderMarker(LsMonitorLandMarkerBO lsMonitorLandMarkerBO) {
        landMarkerDao.saveOrUpdate(lsMonitorLandMarkerBO);
    }

    public void saveOrUpdateLanderMarker(LsMonitorLandMarkerBO lsMonitorLandMarkerBO) {
        landMarkerDao.saveOrUpdate(lsMonitorLandMarkerBO);
    }

    public LsMonitorLandMarkerBO findLandMarkerById(String id) {
        return landMarkerDao.findById(id);
    }

    public void deleteLandMarkerById(String id) {
        landMarkerDao.deleteById(id);
    }

    public int delLanderMarkerByIds(String ids) {
        return landMarkerDao.delLandMarkerByRAIds(ids);
    }

}
