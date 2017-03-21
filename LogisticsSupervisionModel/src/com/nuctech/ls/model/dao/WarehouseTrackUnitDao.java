package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseTrackUnitBO;
import com.nuctech.ls.model.util.DeviceStatus;

/**
 * 作者赵苏阳
 * 
 * 描述
 * <p>
 * 车载台Dao
 * <p>
 * 
 * @author zsy
 *         创建时间2016年6月22
 *
 */

@Repository
public class WarehouseTrackUnitDao extends LSBaseDao<LsWarehouseTrackUnitBO, Serializable> {

    @SuppressWarnings("unused")
    private LSBaseDao<LsWarehouseTrackUnitBO, Serializable> basedao = null;

    /**
     * 统计正在使用的（在途）的车载台
     * 
     * @return
     */
    public int findTrackCountInUse() {
        Session session = this.getSession();
        @SuppressWarnings("rawtypes")
        List list = session.createCriteria(LsWarehouseTrackUnitBO.class)
                .add(Restrictions.eq("trackUnitStatus", DeviceStatus.Inway.getText())).list();
        int trackCount = list.size();
        return trackCount;
    }
}
