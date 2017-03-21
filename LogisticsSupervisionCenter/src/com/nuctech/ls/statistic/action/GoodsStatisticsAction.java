package com.nuctech.ls.statistic.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.service.GoodsStatisticsService;
import com.nuctech.ls.model.vo.statistic.GoodsTypeStatisVo;

import net.sf.json.JSONArray;

@Namespace("/goodsStatisticsAction")
public class GoodsStatisticsAction extends LSBaseAction {

    private static final long serialVersionUID = 1L;
    private String goodsType;
    @Resource
    private GoodsStatisticsService goodsStatisticsService;

    @Action(value = "toList")
    public void list() throws IOException {
        java.util.List<GoodsTypeStatisVo> goodsList = goodsStatisticsService.getList(goodsType);
        JSONArray retJson = JSONArray.fromObject(goodsList);
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }
}
