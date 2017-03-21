package com.nuctech.ls.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.bouncycastle.jce.PrincipalUtil;
import org.springframework.util.ObjectUtils;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.common.LsCommonGoodsTypeBO;
import com.nuctech.ls.model.service.GoodsTypeService;
import com.nuctech.ls.model.service.MonitorTripService;
import com.nuctech.ls.model.service.RiskAnalysisService;
import com.nuctech.ls.model.vo.analysis.RiskSettingPageVo;
import com.nuctech.ls.model.vo.analysis.RiskSettingVo;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Namespace("/analysis")
public class RiskSettingAction extends LSBaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = -298396097160676049L;
    protected static final String DEFAULT_SORT_COLUMNS = "g.lowRiskV ASC";
    @Resource
    private GoodsTypeService goodsTypeService;
    private RiskSettingVo settingVo;
    // 风险参数值数组
    private List<RiskSettingPageVo> riskArr;
    private JSONArray treeArr = new JSONArray();
    private String id;
    private LsCommonGoodsTypeBO goodType;

    /**
     * @return
     * 
     *         风险参数设定
     * @throws IOException
     * 
     */
    @Action(value = "riskSetting", results = { @Result(name = "success", location = "/analysis/riskSetting.jsp") })
    public String riskSetting() throws IOException {
        List<RiskSettingVo> riskArr = goodsTypeService.findAllGoodsRiskSetting();

        List<RiskSettingPageVo> trees = new ArrayList<RiskSettingPageVo>();
        RiskSettingPageVo treeArr1 = new RiskSettingPageVo();
        treeArr1.setId("1");
        treeArr1.setName(getLocaleString("bi.risk.type.goodtype"));
        treeArr1.setRequestUrl("bi.risk.type.goodtype.url");
        treeArr1.setSetting(riskArr);

        trees.add(treeArr1);
        this.treeArr.addAll(trees);
        this.riskArr = trees;

        // 查询货物类型风险参数

        return SUCCESS;
    }

    @Action(value = "goodTyleriskSetting")
    public void goodTyleriskSetting() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject datas = goodsTypeService.fromObjectList(pageQuery, null, false);
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print(datas.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
        }
    }

    /**
     * 修改模态框弹出
     * 
     * @return
     */
    @Action(value = "editGoodType",
            results = { @Result(name = "success", location = "/analysis/goodTypeSettingEdit.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editGoodType() {
        String result = SUCCESS;
        if (!NuctechUtil.isNull(id)) {
            try {
                goodType = this.goodsTypeService.findByid(Integer.parseInt(StringUtils.trim(id)));
            } catch (Exception e) {
                result = ERROR;
            }

            result = SUCCESS;
        } else {
            message = "Find Object Mis!";
            result = ERROR;
        }
        return result;
    }

    @Action(value = "updateGoodTypeRiskParamters",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String updateGoodTypeRiskParamters() throws IOException {
        List<LsCommonGoodsTypeBO> goodtypes = new ArrayList<LsCommonGoodsTypeBO>(0);
        goodtypes.add(this.goodType);
        goodsTypeService.updatelGoodsRiskParams(goodtypes);

        return SUCCESS;
    }

    public List<RiskSettingPageVo> getRiskArr() {
        return riskArr;
    }

    public void setRiskArr(List<RiskSettingPageVo> riskArr) {
        this.riskArr = riskArr;
    }

    public JSONArray getTreeArr() {
        return treeArr;
    }

    public void setTreeArr(JSONArray treeArr) {
        this.treeArr = treeArr;
    }

    public RiskSettingVo getSettingVo() {
        return settingVo;
    }

    public void setSettingVo(RiskSettingVo settingVo) {
        this.settingVo = settingVo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LsCommonGoodsTypeBO getGoodType() {
        return goodType;
    }

    public void setGoodType(LsCommonGoodsTypeBO goodType) {
        this.goodType = goodType;
    }
}
