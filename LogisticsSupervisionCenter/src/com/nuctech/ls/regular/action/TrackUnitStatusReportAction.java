package com.nuctech.ls.regular.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.monitor.LsMonitorVehicleGpsBO;
import com.nuctech.ls.model.bo.system.LsRegularReportParaSetBO;
import com.nuctech.ls.model.service.MonitorVehicleGpsService;
import com.nuctech.ls.model.service.RegularReportParaSetService;
import com.nuctech.ls.model.service.WarehouseElockService;

import net.sf.json.JSONArray;

@Namespace("/trackUnitStatusReport")
public class TrackUnitStatusReportAction extends LSBaseAction {

    private static final long serialVersionUID = 1L;
    @Resource
    private MonitorVehicleGpsService monitorVehicleGpsService;
    protected static final String DEFAULT_SORT_COLUMNS = "e.elockId ASC";
    @Resource
    private WarehouseElockService warehouseElockService;
    private String gpsId;
    private LsMonitorVehicleGpsBO monitorVehicleGpsBO;
    @Resource
    private RegularReportParaSetService regularReportParaSetService;

    /**
     * 车载台状态变化报告查询页面
     * 
     * @return
     */
    @Action(value = "toList",
            results = { @Result(name = "success", location = "/regularreport/trackunitstatusreport/list.jsp") })
    public String toList() {
        return SUCCESS;
    }

    /**
     * 定时加载车载台状态变化的列表(定时请求action)
     * 
     * @throws IOException
     */
    @Action(value = "list")
    public void list() throws IOException {
        /*
         * Runnable runnable = new Runnable() {
         * @Override public void run() {
         * WebsocketService.sendMessage(json.toString());//或者用websocket的方式，
         * 这样就不会冲突
         * } }; edu.emory.mathcs.backport.java.util.concurrent.
         * ScheduledExecutorService service = Executors
         * .newSingleThreadScheduledExecutor();
         * service.scheduleAtFixedRate(runnable, 5, 5, TimeUnit.SECONDS);
         */
        LsRegularReportParaSetBO regularReportParaSetBO = regularReportParaSetService.findByType("0");
        List<?> list = monitorVehicleGpsService.getTrackUnitlist(regularReportParaSetBO.getCustomTime()); //
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            map.put("reportName", "车载台报告");
        }
        JSONArray retJson = JSONArray.fromObject(list);
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();

    }

    /**
     * 弹出车载台状态详细模态框
     * 
     * @return
     */
    @Action(value = "statusDetail",
            results = { @Result(name = "success", location = "/regularreport/trackunitstatusreport/statusDetail.jsp") })
    public String statusDetail() {
        monitorVehicleGpsBO = monitorVehicleGpsService.findById(gpsId);
        return SUCCESS;
    }

    public String getGpsId() {
        return gpsId;
    }

    public void setGpsId(String gpsId) {
        this.gpsId = gpsId;
    }

    public LsMonitorVehicleGpsBO getMonitorVehicleGpsBO() {
        return monitorVehicleGpsBO;
    }

    public void setMonitorVehicleGpsBO(LsMonitorVehicleGpsBO monitorVehicleGpsBO) {
        this.monitorVehicleGpsBO = monitorVehicleGpsBO;
    }

}
