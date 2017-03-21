package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.common.LsCommonDriverBO;

/**
 * 监管司机信息Dao
 * 
 * @author liushaowei
 *
 */
@Repository
public class CommonDriverDao extends LSBaseDao<LsCommonDriverBO, Serializable> {

    /**
     * 新增司机信息
     * 
     * @param lsCommonDriverBO
     */
    public void addCommonDriver(LsCommonDriverBO lsCommonDriverBO) {
        persist(lsCommonDriverBO);
    }

    /**
     * 更新司机信息
     * 
     * @param lsCommonDriverBO
     */
    public void updateCommonDriver(LsCommonDriverBO lsCommonDriverBO) {
        update(lsCommonDriverBO);
    }

    /**
     * 统计各个司机国家拥有的司机数量
     * 
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<LsCommonDriverBO> findCountryCountList() {
        Session session = this.getSession();
        String sql = "SELECT VEHICLE_COUNTRY, COUNT(v.DRIVER_COUNTRY) "
                + "from LS_COMMON_DRIVER v GROUP BY DRIVER_COUNTRY";
        List countryNameCountList = null;
        countryNameCountList = session.createSQLQuery(sql).list();
        return countryNameCountList;
    }

}