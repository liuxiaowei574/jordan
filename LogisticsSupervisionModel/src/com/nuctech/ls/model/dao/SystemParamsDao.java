package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.system.LsSystemParamsBO;

/**
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 系统参数 DAO
 * </p>
 * 创建时间：2016年6月2日
 */
@Repository
public class SystemParamsDao extends LSBaseDao<LsSystemParamsBO, Serializable> {

    /**
     * 通过参数的Key值获取Value
     * 
     * @param paramCode
     *        参数Key
     * @return
     */
    public String findSystemParamsValueByKey(String paramCode) {
        Criteria criteria = getSession().createCriteria(LsSystemParamsBO.class);
        criteria.add(Restrictions.eq("paramCode", paramCode));
        LsSystemParamsBO systemParams = (LsSystemParamsBO) criteria.uniqueResult();
        if (systemParams != null) {
            return systemParams.getParamValue();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<LsSystemParamsBO> findSystemParams(String alarmTypeId) {
        String sql = "SELECT p.* FROM LS_SYSTEM_PARAMS p where ALARM_TYPE_ID='" + alarmTypeId + "'";
        List<LsSystemParamsBO> list = this.getSession().createSQLQuery(sql).addEntity("p", LsSystemParamsBO.class)
                .list();
        return list;
    }

}
