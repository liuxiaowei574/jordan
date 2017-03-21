package com.nuctech.ls.model.vo.statistic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.Assert;

import com.nuctech.ls.common.page.JsonDateValueProcessor;
import com.nuctech.util.Constant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

/**
 * @author liangpengfei
 * 
 * 
 *         动态报表基类
 * 
 *         用于vo的基本设置
 *
 * @param <T>
 */
public abstract class DynamicSetting<T> {

    private List<T> datas;
    @SuppressWarnings("unused")
    private ReportThemeVo theme = new ReportThemeVo();

    /**
     * @return
     * 
     *         返回bootstrap columns 的配置列
     * 
     *         注意复合表头的命名规则，必须以“_mutilple_select_”开头
     * 
     *         如：,{"+
     *         "field: '_mutilple_select_1',"+
     *         "title: '"+MessageResourceUtil.getMessageInfo("statistic.dynamic.port.shebei")+"',"+
     *         "align: 'center',"+
     *         "colspan:4"+
     *         "},
     */
    protected abstract String tgetColumns();

    /**
     * @return
     * 
     *         创建一个bootstrap表格数据
     */
    public JSONObject createTable() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JsonConfig config = new JsonConfig();
        config.setIgnoreDefaultExcludes(true);
        config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor(Constant.JordanTimeFormat));

        for (T t : datas) {
            JSONObject jsonObj = JSONObject.fromObject(t, config);
            jsonArray.add(jsonObj);
        }
        jsonObject.put("data", this.datas);
        jsonObject.put("columns", tgetColumns());
        return jsonObject;
    }

    /**
     * @return
     * 
     *         设置主题名称
     */
    public abstract String tgetTheamName();

    /**
     * @return
     *         设置主题维度
     *         每个维度必须有一个标识，该标识必须与复合列同名，必须以“_mutilple_select_”开头
     *         其他的列和field同名即可
     * 
     *         一个数组定义如:
     *         new ReportDimensionVo[]{new ReportDimensionVo().addColumns(new
     *         String[]{"_mutilple_select_1","weixius","keyongs","zaitus","sunhuais"}).addName(
     *         MessageResourceUtil.getMessageInfo("statistic.dynamic.port.shebei")),
     *         new ReportDimensionVo().addColumns(new
     *         String[]{"_mutilple_select_2","xingchengkaishi","xingchengjieshu","avgtimes",
     *         "zaixianshichang"}).addName(MessageResourceUtil.getMessageInfo(
     *         "statistic.dynamic.port.renyuan")),
     *         new ReportDimensionVo().addColumns(new
     *         String[]{"_mutilple_select_3","","departure","arrival"}).addName(MessageResourceUtil.
     *         getMessageInfo("statistic.dynamic.dim.xincheng"))
     *         };
     * 
     */
    public abstract ReportDimensionVo[] tgetDimentionsName();

    /**
     * @return
     *         设置主题请求
     */
    public abstract String tgetRequestUrl();

    public DynamicSetting<T> addDatas(List<T> datas) {
        this.datas = datas;
        return this;
    }

    /**
     * @param pid
     *        父节点ID
     * @param id
     *        根目录id
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * 
     *         创建主题维度的树形结构
     */
    public List<ReportThemeVo> createTheme(int pid, int id) throws InstantiationException, IllegalAccessException {
        Assert.hasText(tgetTheamName(), "theam required");
        Assert.hasText(tgetRequestUrl(), "theam url required");
        Assert.notEmpty(tgetDimentionsName(), "Dimentions required");
        ReportThemeVo parent = new ReportThemeVo();
        List<ReportThemeVo> result = new ArrayList<ReportThemeVo>();
        parent.setPid(pid);
        parent.setId(id);
        parent.setName(tgetTheamName());
        parent.setRequestUrl(tgetRequestUrl());
        result.add(parent);
        int newid = id;
        for (ReportDimensionVo vo : tgetDimentionsName()) {
            ReportThemeVo dim = new ReportThemeVo();
            dim.setId(++newid);
            dim.setName(vo.getName());
            dim.setPid(id);
            dim.setColumns(vo.getColumns());
            result.add(dim);
        }

        return result;
    }

}
