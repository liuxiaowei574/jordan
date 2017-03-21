package com.nuctech.ls.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.common.base.LSBaseService;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.common.page.PageQuery;
import com.nuctech.ls.model.bo.common.LsCommonGoodsTypeBO;
import com.nuctech.ls.model.dao.goodty.GoodsTypeDao;
import com.nuctech.ls.model.vo.analysis.RiskSettingVo;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 作者： anka liang
 *
 * 描述：
 * <p>
 * 商品类别维护 Service
 * </p>
 * 创建时间：2016年6月19日
 */

@Service
@Transactional
public class GoodsTypeService extends LSBaseService {

    @Resource
    private GoodsTypeDao goodsTypeDao;

    /**
     * 
     * 获取所有货物类别对应的风险参数
     * 
     * @return
     */
    public List<RiskSettingVo> findAllGoodsRiskSetting() {
        List<RiskSettingVo> result = new ArrayList<RiskSettingVo>();

        List<LsCommonGoodsTypeBO> goodTypes = goodsTypeDao.findAll();
        for (LsCommonGoodsTypeBO gtype : goodTypes) {
            RiskSettingVo set = new RiskSettingVo();
            set.setId(gtype.getGoodtypeId() + "");
            set.setLowv(gtype.getLowRiskV() + "");
            set.setMidv(gtype.getMidRiskV() + "");
            set.setName(gtype.getGtypeName());
            set.setSecnm(gtype.getGtypeName());
            result.add(set);
        }
        return result;
    }

    public JSONObject fromObjectList(PageQuery<Map> pageQuery, JsonConfig jsonConfig, boolean ignoreDefaultExcludes) {
        String queryString = "select g from LsCommonGoodsTypeBO g where (1=1"

                + "/~ and g.goodtypeId like '%[goodtypeId]%' ~/" + "/~ or g.iSerial like '%[goodtypeId]%' ~/" + ")"
                + "/~ and g.gtypeName like '%[gtypeName]%' ~/" + "/~ order by [sortColumns] ~/";

        PageList<LsCommonGoodsTypeBO> queryList = goodsTypeDao.pageQuery(queryString, pageQuery);

        PageList<LsCommonGoodsTypeBO> pageList = new PageList<LsCommonGoodsTypeBO>();
        if (queryList != null && queryList.size() > 0) {
            for (LsCommonGoodsTypeBO obj : queryList) {
                pageList.add(obj);
            }
        }
        pageList.setPage(pageQuery.getPage());
        pageList.setPageSize(pageQuery.getPageSize());
        pageList.setTotalItems(queryList.getTotalItems());
        return fromObjectList(pageList, null, false);
    }

    public LsCommonGoodsTypeBO findByid(int id) {
        LsCommonGoodsTypeBO bo = new LsCommonGoodsTypeBO();
        bo = goodsTypeDao.findById(id);

        return bo;
    }

    public boolean updatelGoodsRiskParams(List<LsCommonGoodsTypeBO> gts) {
        LsCommonGoodsTypeBO gt = null;
        for (LsCommonGoodsTypeBO gtype : gts) {
            gt = goodsTypeDao.findById(gtype.getGoodtypeId());
            if (gt != null) {
                gt.setLowRiskV(gtype.getLowRiskV());
                gt.setMidRiskV(gtype.getMidRiskV());
            }
            goodsTypeDao.update(gt);
        }
        return true;
    }

}
