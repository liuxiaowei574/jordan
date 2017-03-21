package com.nuctech.ls.model.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.nuctech.ls.common.base.LSBaseDao;
import com.nuctech.ls.model.bo.dm.LsDmAlarmLevelBO;

@Repository
public class AlarmLevelDao extends LSBaseDao<LsDmAlarmLevelBO, Serializable> {

    @SuppressWarnings("unused")
    private LSBaseDao<LsDmAlarmLevelBO, Serializable> basedao = null;
}
