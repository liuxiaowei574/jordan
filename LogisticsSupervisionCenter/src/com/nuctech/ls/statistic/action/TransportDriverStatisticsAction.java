package com.nuctech.ls.statistic.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.service.TransportDriverStatisService;
import com.nuctech.ls.model.vo.statistic.TransportDriverStatisVo;

import net.sf.json.JSONArray;

@Namespace("/transportDriverStatisitc")
public class TransportDriverStatisticsAction extends LSBaseAction {

    private static final long serialVersionUID = 1L;
    @Resource
    private TransportDriverStatisService transportDriverStatisService;
    private String driverName;

    @Action(value = "toList")
    public void list() throws IOException {
        List<TransportDriverStatisVo> driverList = transportDriverStatisService.getList(driverName);
        JSONArray retJson = JSONArray.fromObject(driverList);
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}
