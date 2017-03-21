package com.nuctech.ls.model.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.model.dao.GoodsStatisticsDao;
import com.nuctech.ls.model.vo.statistic.GoodsTypeStatisVo;

@Service
@Transactional
public class GoodsStatisticsService extends LSBaseService {

    @Resource
    private GoodsStatisticsDao goodsStatisticsDao;

    public List<GoodsTypeStatisVo> getList(String goodsType) {
        return goodsStatisticsDao.getGoodsList(goodsType);
    }

    public List<?> findGoodsType() {
        return goodsStatisticsDao.findAll();
    }

}
