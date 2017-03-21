package com.nuctech.ls.center.device.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceApplicationBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceDispatchBO;
import com.nuctech.ls.model.service.DispatchDetailService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.service.WarehouseDeviceApplicationService;
import com.nuctech.ls.model.util.OperateContentType;
import com.nuctech.ls.model.util.OperateEntityType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;

import net.sf.json.JSONObject;

@Namespace("/warehouseDeviceApplication")
public class WarehouseDeviceApplicationAction extends LSBaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 9216869044821261642L;

    private Logger logger = Logger.getLogger(WarehouseDeviceApplicationAction.class);
    private List elockDetailList = new ArrayList();// 根据调度详细表的调度Id，查询关锁的信息(前台用El表达式取出来)
    private List esealDetailList = new ArrayList();// 子锁
    private List sensorDetailList = new ArrayList();// 传感器
    @Resource
    private WarehouseDeviceApplicationService warehouseDeviceApplicationService;
    @Resource
    private SystemDepartmentService systemDepartmentService;
    private LsWarehouseDeviceApplicationBO warehouseDeviceApplication;

    private String result;
    @Resource
    private SystemOperateLogService logService;
    private String applicationId;
    @Resource
    private DispatchDetailService dispatchDService;
    private LsWarehouseDeviceDispatchBO warehouseDeviceDispatchBO;
    @Resource
    private SystemUserService systemUserService;
    private List<?> userList;
    /**
     * 列表排序字段
     */
    private static final String DEFAULT_SORT_COLUMNS = "t.applyStatus ASC";

    @Action(value = "index", results = { @Result(name = "success", location = "/device/application/list.jsp") })
    public String index() {
        // 获取登陆用户的角色，若为qualityCenter则可以点击分析按钮（笔状），若为portuser(可以点击申请按钮),其他角色看不到该菜单功能
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String roleName = sessionUser.getRoleName();
        request.setAttribute("roleName", roleName);
        
        //控制中心用户登陆，查询系统中所有用户；口岸用户登陆查询该口岸的所有用户
        String organizationId = sessionUser.getOrganizationId();
        userList = systemUserService.findUserByOrganizationId(organizationId);
        return SUCCESS;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "list")
    public void list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson;
        try {
            retJson = warehouseDeviceApplicationService.findDeviceApplicationList(pageQuery, null, false);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print(retJson.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    @Action(value = "addModal", results = { @Result(name = "success", location = "/device/application/add.jsp") })
    public String addModal() {
        warehouseDeviceApplication = new LsWarehouseDeviceApplicationBO();
        return SUCCESS;
    }

    @Action(value = "addWarehouseDeviceApplication",
            results = { @Result(name = "success", type = "json"), @Result(name = "error", type = "json") })
    public String addWarehouseDeviceApplication() {
        int deviceNumber = Integer.parseInt(warehouseDeviceApplication.getDeviceNumber());
        int esealNumber = Integer.parseInt(warehouseDeviceApplication.getEsealNumber());
        int sensorNumber = Integer.parseInt(warehouseDeviceApplication.getSensorNumber());
        if (warehouseDeviceApplication != null && (deviceNumber != 0 || esealNumber != 0 || sensorNumber != 0)) {
            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            if (sessionUser == null) {
                logger.error("请登录系统");
                result = "false";
                return ERROR;
            }
            LsSystemDepartmentBO systemDepartment = systemDepartmentService.findById(sessionUser.getOrganizationId());
            warehouseDeviceApplication.setApplicationId(generatePrimaryKey());
            warehouseDeviceApplication.setApplcationPort(sessionUser.getOrganizationId());
            warehouseDeviceApplication.setApplcationPortName(systemDepartment.getOrganizationName());
            warehouseDeviceApplication.setApplyUser(sessionUser.getUserId());
            warehouseDeviceApplication.setApplyTime(new Date());
            warehouseDeviceApplication.setApplyStatus(Constant.DEVICE_ALREADY_APPLIED);
            warehouseDeviceApplicationService.save(warehouseDeviceApplication);
            addLog(OperateContentType.REQUEST.toString(), OperateEntityType.DEVICE_DISPATCH.toString(),
                    warehouseDeviceApplication.toString());
            result = "true";
            return SUCCESS;
        } else if (deviceNumber == 0 && esealNumber == 0 && sensorNumber == 0) {
            result = "allzero";
            return ERROR;
        } else {
            logger.debug("调度申请设备为空");
            result = "false";
            return ERROR;
        }
    }

    /**
     * 调度申请"完成"之后，控制中心可以查看详细的调度信息
     * @return
     */
    @Action(value = "detailDispatch", results = {
            @Result(name = "success", location = "/device/dispatchhandle/controlSeeDispatchDetail.jsp") })
    public String detailDispatch() {
        warehouseDeviceDispatchBO = dispatchDService.findDispatchBo(applicationId);
        //根据申请口岸id获取申请口岸的name
        LsSystemDepartmentBO systemDepartment 
        = systemDepartmentService.findById(warehouseDeviceDispatchBO.getToPort());
        String applicationPort = systemDepartment.getOrganizationName();
        request.setAttribute("applicationPort", applicationPort);
        //根据调度口岸的id获取调度口岸的name
        LsSystemDepartmentBO systemDepartment1 
        = systemDepartmentService.findById(warehouseDeviceDispatchBO.getFromPort());
        String fromPort = systemDepartment1.getOrganizationName();
        request.setAttribute("fromPort", fromPort);
        //返回关锁信息List
        elockDetailList = dispatchDService.getElockDetailList(warehouseDeviceDispatchBO.getDispatchId());
        //返回子锁信息的List
        esealDetailList = dispatchDService.getEsealDetaillist(warehouseDeviceDispatchBO.getDispatchId());
        //返回传感器
        sensorDetailList = dispatchDService.getSensorDetaillist(warehouseDeviceDispatchBO.getDispatchId());
        return SUCCESS;
    }
    
    
    
    
    
    /**
     * 日志记录方法
     * 
     * @param content
     * @param params
     */
    private void addLog(String operate, String entity, String params) {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        logService.addLog(operate, entity, sessionUser.getUserId(), this.getClass().toString(), params);
    }

    public LsWarehouseDeviceApplicationBO getWarehouseDeviceApplication() {
        return warehouseDeviceApplication;
    }

    public void setWarehouseDeviceApplication(LsWarehouseDeviceApplicationBO warehouseDeviceApplication) {
        this.warehouseDeviceApplication = warehouseDeviceApplication;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    
    public LsWarehouseDeviceDispatchBO getWarehouseDeviceDispatchBO() {
        return warehouseDeviceDispatchBO;
    }

    
    public void setWarehouseDeviceDispatchBO(LsWarehouseDeviceDispatchBO warehouseDeviceDispatchBO) {
        this.warehouseDeviceDispatchBO = warehouseDeviceDispatchBO;
    }

    
    public List getElockDetailList() {
        return elockDetailList;
    }

    
    public void setElockDetailList(List elockDetailList) {
        this.elockDetailList = elockDetailList;
    }

    
    public List getEsealDetailList() {
        return esealDetailList;
    }

    
    public void setEsealDetailList(List esealDetailList) {
        this.esealDetailList = esealDetailList;
    }

    
    public List getSensorDetailList() {
        return sensorDetailList;
    }

    
    public void setSensorDetailList(List sensorDetailList) {
        this.sensorDetailList = sensorDetailList;
    }

    
    public List<?> getUserList() {
        return userList;
    }

    
    public void setUserList(List<?> userList) {
        this.userList = userList;
    }
}
