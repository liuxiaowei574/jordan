package com.nuctech.ls.model.vo.analysis;

import java.util.List;

/**
 * @author liangpengfei
 * 
 *         风险参数模型
 *
 */
public class RiskSettingPageVo extends RiskTypeVo {

    private List<RiskSettingVo> setting;

    public List<RiskSettingVo> getSetting() {
        return setting;
    }

    public void setSetting(List<RiskSettingVo> setting) {
        this.setting = setting;
    }

}
