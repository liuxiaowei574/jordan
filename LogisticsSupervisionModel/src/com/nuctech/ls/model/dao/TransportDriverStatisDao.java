package com.nuctech.ls.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.common.LsCommonDriverBO;
import com.nuctech.ls.model.vo.statistic.TransportDriverStatisVo;
import com.nuctech.util.NuctechUtil;

@Repository
public class TransportDriverStatisDao extends LSBaseDao<LsCommonDriverBO, Serializable> {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<TransportDriverStatisVo> getDriverList(String driverName) {
        Session session = this.getSession();
        String queryString = "";
        if (NuctechUtil.isNull(driverName)) {
            queryString = "select T.DRIVER_NAME ,count(*) as tripNum "
                    + "from (SELECT d.DRIVER_NAME,v.TRIP_ID FROM LS_COMMON_DRIVER  d ,LS_COMMON_VEHICLE v "
                    + "where d.DRIVER_ID=v.DRIVER_ID) as T GROUP BY T.DRIVER_NAME";
        }

        if (NuctechUtil.isNotNull(driverName)) {
            queryString = "SELECT T.DRIVER_NAME, COUNT (*) AS tripNum "
                    + "FROM ( SELECT d.DRIVER_NAME, v.TRIP_ID FROM LS_COMMON_DRIVER d, LS_COMMON_VEHICLE v "
                    + "WHERE d.DRIVER_ID = v.DRIVER_ID AND d.DRIVER_NAME LIKE'%" + driverName
                    + "%') AS T GROUP BY T.DRIVER_NAME";
        }
        List driverTransportList = null;
        driverTransportList = session.createSQLQuery(queryString).addScalar("DRIVER_NAME", StandardBasicTypes.STRING)
                .addScalar("tripNum", StandardBasicTypes.STRING).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
        return driverTransportList;
    }
}
