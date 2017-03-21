package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.dao.TransportDriverStatisDao;
import com.nuctech.ls.model.vo.statistic.TransportDriverStatisVo;

@Service
@Transactional
public class TransportDriverStatisService extends LSBaseService {

    @Resource
    private TransportDriverStatisDao transportDriverStatisDao;

    public List<TransportDriverStatisVo> getList(String driverName) {
        return transportDriverStatisDao.getDriverList(driverName);
    }
}
