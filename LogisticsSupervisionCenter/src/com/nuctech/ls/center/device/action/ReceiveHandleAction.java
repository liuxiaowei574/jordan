package com.nuctech.ls.center.device.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDeviceApplicationBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseDispatchDetailBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseElockBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseEsealBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseSensorBO;
import com.nuctech.ls.model.service.ReceiveMgmtService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.service.WarehouseElockService;
import com.nuctech.ls.model.service.WarehouseEsealService;
import com.nuctech.ls.model.service.WarehouseSensorService;
import com.nuctech.ls.model.util.DeviceStatus;
import com.nuctech.ls.model.util.OperateContentType;
import com.nuctech.ls.model.util.OperateEntityType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 作者：赵苏阳 描述：
 * <p>
 * 设备接收 action
 * <p>
 * 创建时间2016年6月20
 *
 */
@Namespace("/receive")
public class ReceiveHandleAction extends LSBaseAction {

    private static final long serialVersionUID = 4787413531368524581L;
    protected static final String DEFAULT_SORT_COLUMNS = "e.dispatchId ASC";
    private List<LsSystemDepartmentBO> deptList = new ArrayList<LsSystemDepartmentBO>();

    @Resource
    private SystemDepartmentService systemDepartmentService;

    @Resource
    private ReceiveMgmtService receiveMgmtService;

    @Resource
    private WarehouseElockService warehouseElockService;
    @Resource
    private WarehouseEsealService warehouseEsealService;
    @Resource
    private WarehouseSensorService warehouseSensorService;

    /**
     * 将调度记录信息显示到接收页面
     */
    private String dispatchId;// 记录表的调度ID
    private String applicationId;// 记录表的申请ID

    private List deviceDetailList = new ArrayList<>();
    private String id;

    /**
     * 手动选择接收
     */
    private String detailIds;
    private String[] detailIdList;
    private LsWarehouseDispatchDetailBO lsWarehouseDispatchDetailBO;
    // private String recviceStatus = "1";
    private String tableLength;
    private String recviceStatuS;
    private String[] recviceStatuSList;
    private LsWarehouseDeviceApplicationBO lsWarehouseDeviceApplicationBO;
    // 修改设备的所属口岸
    private String deviceIds;
    private String[] deviceIdList;
    private LsWarehouseElockBO lsWarehouseElockBO;
    private LsWarehouseEsealBO lsWarehouseEsealBO;
    private LsWarehouseSensorBO lsWarehouseSensorBO;
    /**
     * 全部接收
     */
    private String allDetailIds;
    private String[] allDetailIdsList;
    private String allDeviceIds;
    private String[] allDeviceIdsList;
    @Resource
    private SystemOperateLogService logService;

    /**
     * 进入设备接收页面
     * 
     * @return
     */
    @Action(value = "toList", results = { @Result(name = "success", location = "/device/receive/receiveIndex.jsp") })
    public String toList() {
        String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();// LS_SYSTEM_ORGANIZATION_USER与LS_SYSTEM_DEPARTMENT通过organizationId关联
        deptList = systemDepartmentService.findAllPortByUserId(userId);//
        return SUCCESS;
    }

    /**
     * 初始化接收页面，列表获取登陆用户所在的口岸的记录表的数据
     */
    @SuppressWarnings("unchecked")
    @Action(value = "list")
    public void list() throws IOException {
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        sessionUser.getOrganizationId();
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = receiveMgmtService.fromObjectList(pageQuery, null, false, sessionUser.getOrganizationId());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /**
     * 选中复选框将设备的详细信息显示到接收页面上
     * 
     * @return
     */
    @Action(value = "receiveReady", results = { @Result(name = "success", location = "/device/receive/receive.jsp") })
    public String receiveReady() {
        request.setAttribute("dispatchId", dispatchId);// 将记录表的调度id传到前台
        request.setAttribute("applicationId", applicationId);
        /*
         * deviceDetailList =
         * receiveMgmtService.getdeviceDetailList(dispatchId);
         */ // 前台通过el表达式往表格中塞数据；
        return SUCCESS;
    }

    /**
     * bootstrap tablede 的url方式获取数据
     * 
     * @throws IOException
     */
    @Action(value = "dlist", results = { @Result(name = "success", type = "json") })
    public void dlist() throws IOException {
        deviceDetailList = receiveMgmtService.getdeviceDetailList(id);// id从前台获取参数；
        JSONArray retJson = receiveMgmtService.fromArrayList(deviceDetailList, null, false);
        // JSONArray retJson = JSONArray.fromObject(deviceDetailList);//时间类型不对；
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /**
     * 手动选择接收
     * 
     * @throws IOException
     */
    @Action(value = "manualSelect", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String manualSelect() throws IOException {

        int count = 0;
        if (detailIds != null && !"".equals(detailIds)) {
            // 获得调配明细主键字符串数
            // detailIds = detailIds.substring(0, detailIds.length()-1);//
            // 把字符串的最后一个字符删除(逗号)当只有一个id时，这种方法会把id的最后一个字符删掉
            detailIdList = detailIds.split(",");
            // 获得接收状态为1的行数
            recviceStatuSList = recviceStatuS.split(",");
            for (int i = 0; i < recviceStatuSList.length; i++) {
                if (recviceStatuSList[i].equals("1") == true) {
                    count++;
                }
            }

            // 获得设备的主键
            deviceIdList = deviceIds.split(",");
            // 修改设备的所属口岸，修改为当前用户的口岸名称

            for (int i = 0; i < deviceIdList.length; i++) {
                if (NuctechUtil.isNotNull(deviceIdList[i])) {
                    // 获得当前用户组织结构代码即设备所属口岸
                    SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                    String organizationId = sessionUser.getOrganizationId();
                    // 修改关锁所属口岸
                    lsWarehouseElockBO = warehouseElockService.findById(deviceIdList[i]);
                    if (lsWarehouseElockBO != null) {
                        lsWarehouseElockBO.setBelongTo(organizationId);
                        lsWarehouseElockBO.setLastUseTime(new Date());// 设置关锁最后使用时间。
                        lsWarehouseElockBO.setElockStatus(DeviceStatus.Normal.getText());
                        warehouseElockService.modify(lsWarehouseElockBO);
                    }

                    // 修改子锁所属口岸
                    lsWarehouseEsealBO = warehouseEsealService.findById(deviceIdList[i]);
                    if (lsWarehouseEsealBO != null) {
                        lsWarehouseEsealBO.setBelongTo(organizationId);
                        lsWarehouseEsealBO.setEsealStatus(DeviceStatus.Normal.getText());
                        warehouseEsealService.modify(lsWarehouseEsealBO);
                    }
                    // 修改传感器所属口岸
                    lsWarehouseSensorBO = warehouseSensorService.findById(deviceIdList[i]);
                    if (lsWarehouseSensorBO != null) {
                        lsWarehouseSensorBO.setBelongTo(organizationId);
                        lsWarehouseSensorBO.setSensorStatus(DeviceStatus.Normal.getText());
                        warehouseSensorService.modify(lsWarehouseSensorBO);
                    }
                }
            }

            /**
             * 根据id选出被勾选的设备，然后循环改变其(接收人，接收状态和接收时间);
             * 
             */
            int test = detailIdList.length + count;
            // 只勾选接收状态为0的再加上接收状态为1的，和小于表格总数时，不修改申请表；
            if (test < Integer.parseInt(tableLength)) {
                for (int i = 0; i < detailIdList.length; i++) {
                    if (NuctechUtil.isNotNull(detailIdList[i])) {
                        lsWarehouseDispatchDetailBO = receiveMgmtService.findByDetailID(detailIdList[i]);
                        lsWarehouseDispatchDetailBO.setRecviceTime(new Date());
                        SessionUser sessionUser = (SessionUser) request.getSession()
                                .getAttribute(Constant.SESSION_USER);
                        lsWarehouseDispatchDetailBO.setRecviceUser(sessionUser.getUserName());
                        lsWarehouseDispatchDetailBO.setRecviceStatus(Constant.recviceStatus);
                        receiveMgmtService.modifyDeviceDetail(lsWarehouseDispatchDetailBO);
                    }
                }
            }
            // 只勾选接收状态为0的再加上接收状态为1的，和等于表格总数时，最后修改申请表的状态；
            if (test == Integer.parseInt(tableLength)) {
                for (int i = 0; i < detailIdList.length; i++) {
                    if (NuctechUtil.isNotNull(detailIdList[i])) {
                        lsWarehouseDispatchDetailBO = receiveMgmtService.findByDetailID(detailIdList[i]);
                        lsWarehouseDispatchDetailBO.setRecviceTime(new Date());
                        SessionUser sessionUser = (SessionUser) request.getSession()
                                .getAttribute(Constant.SESSION_USER);
                        lsWarehouseDispatchDetailBO.setRecviceUser(sessionUser.getUserName());
                        lsWarehouseDispatchDetailBO.setRecviceStatus(Constant.recviceStatus);
                        receiveMgmtService.modifyDeviceDetail(lsWarehouseDispatchDetailBO);
                    }
                }
                // 修改申请表的申请状态，改为已处理；
                // 根据申请id查出申请表并将“申请状态”改为已处理

                lsWarehouseDeviceApplicationBO = receiveMgmtService.findByApplicationId(applicationId);
                lsWarehouseDeviceApplicationBO.setApplyStatus(Constant.DEVICE_ALREADY_FINISH);
                receiveMgmtService.modifyDeviceApplication(lsWarehouseDeviceApplicationBO);
                addLog(OperateContentType.RECEIVE.toString(), OperateEntityType.DEVICE_DISPATCH.toString(),
                        lsWarehouseDeviceApplicationBO.toString());
            }

        }
        return SUCCESS;
    }

    /**
     * 一次性全部接收
     * 
     * @return
     * @throws IOException
     */
    @Action(value = "selectAll", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String selectAll() throws IOException {
        // 获得设备的主键
        allDeviceIdsList = allDeviceIds.split(",");
        // 修改设备的所属口岸，修改为当前用户的口岸名称

        for (int i = 0; i < allDeviceIdsList.length; i++) {
            if (NuctechUtil.isNotNull(allDeviceIdsList[i])) {
                // 获得当前用户组织结构代码即设备所属口岸
                SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                String organizationId = sessionUser.getOrganizationId();
                // 修改关锁所属口岸
                lsWarehouseElockBO = warehouseElockService.findById(allDeviceIdsList[i]);
                if (lsWarehouseElockBO != null) {
                    lsWarehouseElockBO.setBelongTo(organizationId);
                    lsWarehouseElockBO.setLastUseTime(new Date());// 设置关锁最后使用时间。
                    lsWarehouseElockBO.setElockStatus(DeviceStatus.Normal.getText());
                    warehouseElockService.modify(lsWarehouseElockBO);
                }

                // 修改子锁所属口岸
                lsWarehouseEsealBO = warehouseEsealService.findById(allDeviceIdsList[i]);
                if (lsWarehouseEsealBO != null) {
                    lsWarehouseEsealBO.setBelongTo(organizationId);
                    lsWarehouseEsealBO.setEsealStatus(DeviceStatus.Normal.getText());
                    warehouseEsealService.modify(lsWarehouseEsealBO);
                }
                // 修改传感器所属口岸
                lsWarehouseSensorBO = warehouseSensorService.findById(allDeviceIdsList[i]);
                if (lsWarehouseSensorBO != null) {
                    lsWarehouseSensorBO.setBelongTo(organizationId);
                    lsWarehouseSensorBO.setSensorStatus(DeviceStatus.Normal.getText());
                    warehouseSensorService.modify(lsWarehouseSensorBO);
                }
            }
        }

        /**
         * 将调度详细表中的接收状态和接收人 以及接收时间修改下
         */
        allDetailIdsList = allDetailIds.split(",");

        for (int i = 0; i < allDetailIdsList.length; i++) {
            if (NuctechUtil.isNotNull(allDetailIdsList[i])) {
                lsWarehouseDispatchDetailBO = receiveMgmtService.findByDetailID(allDetailIdsList[i]);
                lsWarehouseDispatchDetailBO.setRecviceTime(new Date());
                SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                lsWarehouseDispatchDetailBO.setRecviceUser(sessionUser.getUserName());
                lsWarehouseDispatchDetailBO.setRecviceStatus(Constant.recviceStatus);
                receiveMgmtService.modifyDeviceDetail(lsWarehouseDispatchDetailBO);
            }
        }
        /**
         * 将申请表的申请状态改为已接收
         */
        lsWarehouseDeviceApplicationBO = receiveMgmtService.findByApplicationId(applicationId);
        lsWarehouseDeviceApplicationBO.setApplyStatus(Constant.DEVICE_ALREADY_FINISH);
        receiveMgmtService.modifyDeviceApplication(lsWarehouseDeviceApplicationBO);
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

    public List<LsSystemDepartmentBO> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<LsSystemDepartmentBO> deptList) {
        this.deptList = deptList;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    public List getDeviceDetailList() {
        return deviceDetailList;
    }

    public void setDeviceDetailList(List deviceDetailList) {
        this.deviceDetailList = deviceDetailList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(String detailIds) {
        this.detailIds = detailIds;
    }

    public String[] getDetailIdList() {
        return detailIdList;
    }

    public void setDetailIdList(String[] detailIdList) {
        this.detailIdList = detailIdList;
    }

    public LsWarehouseDispatchDetailBO getLsWarehouseDispatchDetailBO() {
        return lsWarehouseDispatchDetailBO;
    }

    public void setLsWarehouseDispatchDetailBO(LsWarehouseDispatchDetailBO lsWarehouseDispatchDetailBO) {
        this.lsWarehouseDispatchDetailBO = lsWarehouseDispatchDetailBO;
    }

    public String getTableLength() {
        return tableLength;
    }

    public void setTableLength(String tableLength) {
        this.tableLength = tableLength;
    }

    public String[] getRecviceStatuSList() {
        return recviceStatuSList;
    }

    public void setRecviceStatuSList(String[] recviceStatuSList) {
        this.recviceStatuSList = recviceStatuSList;
    }

    public String getRecviceStatuS() {
        return recviceStatuS;
    }

    public void setRecviceStatuS(String recviceStatuS) {
        this.recviceStatuS = recviceStatuS;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public LsWarehouseDeviceApplicationBO getLsWarehouseDeviceApplicationBO() {
        return lsWarehouseDeviceApplicationBO;
    }

    public void setLsWarehouseDeviceApplicationBO(LsWarehouseDeviceApplicationBO lsWarehouseDeviceApplicationBO) {
        this.lsWarehouseDeviceApplicationBO = lsWarehouseDeviceApplicationBO;
    }

    public String getAllDetailIds() {
        return allDetailIds;
    }

    public void setAllDetailIds(String allDetailIds) {
        this.allDetailIds = allDetailIds;
    }

    public String[] getAllDetailIdsList() {
        return allDetailIdsList;
    }

    public void setAllDetailIdsList(String[] allDetailIdsList) {
        this.allDetailIdsList = allDetailIdsList;
    }

    public void setDeviceIds(String deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String[] getDeviceIdList() {
        return deviceIdList;
    }

    public String getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIdList(String[] deviceIdList) {
        this.deviceIdList = deviceIdList;
    }

    public LsWarehouseElockBO getLsWarehouseElockBO() {
        return lsWarehouseElockBO;
    }

    public void setLsWarehouseElockBO(LsWarehouseElockBO lsWarehouseElockBO) {
        this.lsWarehouseElockBO = lsWarehouseElockBO;
    }

    public LsWarehouseEsealBO getLsWarehouseEsealBO() {
        return lsWarehouseEsealBO;
    }

    public void setLsWarehouseEsealBO(LsWarehouseEsealBO lsWarehouseEsealBO) {
        this.lsWarehouseEsealBO = lsWarehouseEsealBO;
    }

    public LsWarehouseSensorBO getLsWarehouseSensorBO() {
        return lsWarehouseSensorBO;
    }

    public void setLsWarehouseSensorBO(LsWarehouseSensorBO lsWarehouseSensorBO) {
        this.lsWarehouseSensorBO = lsWarehouseSensorBO;
    }

    public String getAllDeviceIds() {
        return allDeviceIds;
    }

    public void setAllDeviceIds(String allDeviceIds) {
        this.allDeviceIds = allDeviceIds;
    }

    public String[] getAllDeviceIdsList() {
        return allDeviceIdsList;
    }

    public void setAllDeviceIdsList(String[] allDeviceIdsList) {
        this.allDeviceIdsList = allDeviceIdsList;
    }
}
