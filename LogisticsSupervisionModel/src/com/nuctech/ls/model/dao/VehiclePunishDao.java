package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.sla.LsVehiclePunishBo;

@Repository
public class VehiclePunishDao extends LSBaseDao<LsVehiclePunishBo, Serializable> {

    public LsVehiclePunishBo findByPropertyName(String punishType) {
        Session session = this.getSession();
        String sql = "SELECT * FROM LS_VEHICLE_PUNISH where VPUNISH_TYPE='" + punishType + "'";
        @SuppressWarnings("unchecked")
        List<LsVehiclePunishBo> list = session.createSQLQuery(sql).addEntity(LsVehiclePunishBo.class).list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
