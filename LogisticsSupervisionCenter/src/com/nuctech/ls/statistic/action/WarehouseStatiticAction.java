package com.nuctech.ls.statistic.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseEsealBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseSensorBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.WarehouseDeviceApplicationService;
import com.nuctech.ls.model.service.WarehouseDispatchAnalysisService;
import com.nuctech.ls.model.service.WarehouseElockService;
import com.nuctech.ls.model.service.WarehouseEsealService;
import com.nuctech.ls.model.service.WarehouseSensorService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.PortElockStatisitcVO;
import com.nuctech.util.Constant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 作者： 梁朋飞
 *
 * 描述：
 * <p>
 * 关锁统计分析 Action
 * </p>
 * 创建时间：2016年9月2日
 */
@Namespace("/statisitc")
public class WarehouseStatiticAction extends LSBaseAction {

    private static final long serialVersionUID = -7737991034005394499L;
    protected static final String DEFAULT_SORT_COLUMNS = "e.elockNumber ASC";
    private static final String DEFAULT_ESORTE_COLUMNS = "e.esealNumber ASC";
    @Resource
    private WarehouseDispatchAnalysisService warehouseDispatchAnalysisService;
    @Resource
    private WarehouseDeviceApplicationService warehouseDeviceApplicationService;
    @Resource
    private SystemDepartmentService systemDepartmentService;

    private List<PortElockStatisitcVO> dispatchPortList;

    private String portName;
    public JSONArray deptArr = new JSONArray();
    @Resource
    private WarehouseElockService warehouseElockService;
    @Resource
    private WarehouseEsealService warehouseEsealService;
    @Resource
    private WarehouseSensorService warehouseSensorService;
    //设备状态（如"可用关锁"）
    private String seriesName;

  

	/**
     * @return
     * 
     *         口岸关锁统计 1 ,根据用户查询所有的口岸， 2，查询所有口岸的设备情况，包括可用关锁，损坏关锁，预留关锁
     * 
     */
    @Action(value = "elockStatistic",
            results = { @Result(name = "success", location = "/statistics/elockStatistic.jsp") })
    public String elockStatistic() {
        // 1
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        dispatchPortList = warehouseDispatchAnalysisService.findDispatchPortAndRoomList(portName,
                sessionUser.getUserId());
        //只显示口岸
        for(int i=0; i<dispatchPortList.size();i++){
        	if(dispatchPortList.get(i).getPortName().equals("Control Room")||
        			dispatchPortList.get(i).getPortName().equals("Quality Center")||
        			dispatchPortList.get(i).getPortName().equals("Admin Center")){
        		dispatchPortList.remove(i);
        	}
        }
        if (dispatchPortList != null) {
            deptArr.addAll(dispatchPortList);
        }
        // 2
        // warehouseDeviceApplicationService.findDeviceApplicationList(pageQuery,
        // null, null);
        return SUCCESS;
    }

    /**
     * 点击口岸显示设备的明细列表(关锁)
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Action(value = "elockdetails")
    public void elockdetails() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = warehouseElockService.statiselock(pageQuery, null, false, portName,seriesName);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /**
     * 点击口岸显示子锁的明细列表(子锁)
     * 
     * @throws IOException
     */

    @Action(value = "esealdetails")
    public void esealdetails() throws IOException {
        List<LsWarehouseEsealBO> esealList = new ArrayList<LsWarehouseEsealBO>();
        esealList = warehouseEsealService.statisEseal(portName,seriesName);
        JSONArray retJson = JSONArray.fromObject(esealList);
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /**
     * 点击口岸显示传感器的明细列表
     * 
     * @throws IOException
     */
    @Action(value = "sensordetails")
    public void sensordetails() throws IOException {
        List<LsWarehouseSensorBO> sensorList = new ArrayList<LsWarehouseSensorBO>();
        sensorList = warehouseSensorService.statisSensor(portName,seriesName);
        JSONArray retJson = JSONArray.fromObject(sensorList);
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public JSONArray getDeptArr() {
        return deptArr;
    }

    public void setDeptArr(JSONArray deptArr) {
        this.deptArr = deptArr;
    }
    public String getSeriesName() {
  		return seriesName;
  	}

  	public void setSeriesName(String seriesName) {
  		this.seriesName = seriesName;
  	}
}
