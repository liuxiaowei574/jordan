/**
 * 
 */
package com.nuctech.ls.model.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.monitor.LsMonitorElockGpslogBO;
import com.nuctech.ls.model.dao.MonitorElockGPSLogDao;

/**
 * 关锁原始上传数据记录
 * 
 * @author sunming
 *
 */
@Service
@Transactional
public class MonitorElockGPSLogService extends LSBaseService {

    @Resource
    public MonitorElockGPSLogDao monitorElockGPSLogDao;

    /**
     * 保存信息
     * 
     * @param entity
     */
    public void save(LsMonitorElockGpslogBO entity) {
        monitorElockGPSLogDao.save(entity);
    }

}
