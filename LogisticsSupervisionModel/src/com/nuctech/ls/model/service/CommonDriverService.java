package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.bo.common.LsCommonDriverBO;
import com.nuctech.ls.model.dao.CommonDriverDao;

/**
 * 司机管理Service
 * 
 * @author liushaowei
 *
 */
@Service
@Transactional
public class CommonDriverService extends LSBaseService {

    @Resource
    private CommonDriverDao commonDriverDao;

    /**
     * 新增司机信息
     * 
     * @param lsCommonDriverBO
     */
    public void addCommonDriver(LsCommonDriverBO lsCommonDriverBO) {
        commonDriverDao.addCommonDriver(lsCommonDriverBO);
    }

    /**
     * 根据Id查找司机信息
     * 
     * @param id
     * @return
     */
    public LsCommonDriverBO findById(String id) {
        return commonDriverDao.findById(id);
    }

    /**
     * 更新司机信息
     * 
     * @param lsCommonDriverBO
     */
    public void updateCommonDriver(LsCommonDriverBO lsCommonDriverBO) {
        commonDriverDao.updateCommonDriver(lsCommonDriverBO);
    }

    /**
     * 持久化司机信息
     * 
     * @param lsCommonDriverBO
     */
    public void merge(LsCommonDriverBO lsCommonDriverBO) {
        commonDriverDao.merge(lsCommonDriverBO);
    }

    /**
     * 查找每个国家对应的司机数
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findCountryNamCount() {
        return commonDriverDao.findCountryCountList();
    }

    /**
     * 根据driverIdCard查找司机信息
     * 
     * @param driverIdCard
     * @return
     */
    public LsCommonDriverBO findByIdCard(String driverIdCard) {
        return commonDriverDao.findByProperty("driverIdCard", driverIdCard);
    }
}
