package com.nuctech.ls.patrolmanagement.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.common.LsCommonVehicleBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.system.LsSystemUserBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseTrackUnitBO;
import com.nuctech.ls.model.service.CommonPatrolService;
import com.nuctech.ls.model.service.CommonVehicleService;
import com.nuctech.ls.model.service.MonitorRouteAreaService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.service.WarehouseTrackUnitService;
import com.nuctech.ls.model.util.OperateContentType;
import com.nuctech.ls.model.util.OperateEntityType;
import com.nuctech.ls.model.util.RouteAreaType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;
import com.nuctech.util.UploadDownloadUtil;

import jcifs.util.Base64;
import net.sf.json.JSONObject;

/**
 * 作者：赵苏阳 描述：
 * <p>
 * 巡逻队管理 增删改查
 * <p>
 * 创建时间2016年9月8
 *
 */
@Namespace("/patrolMgmt")
public class PatrolMagmentAction extends LSBaseAction {

    private static final long serialVersionUID = 1L;
    protected static final String DEFAULT_SORT_COLUMNS = "p.patrolId ASC ";
    private List<LsSystemDepartmentBO> deptList = new ArrayList<LsSystemDepartmentBO>();
    private List<LsMonitorRouteAreaBO> areaList = new ArrayList<LsMonitorRouteAreaBO>();
    private List<LsWarehouseTrackUnitBO> trackList = new ArrayList<LsWarehouseTrackUnitBO>();
    private List<LsCommonVehicleBO> vehicleList = new ArrayList<LsCommonVehicleBO>();
    private List<SessionUser> userList = new ArrayList<SessionUser>();

    private List<LsCommonPatrolBO> patrolList = new ArrayList<LsCommonPatrolBO>();

    private LsCommonPatrolBO lsCommonPatrolBO;
    private String patrolId;

    private LsWarehouseTrackUnitBO lsWarehouseTrackUnitBO;
    private LsMonitorRouteAreaBO lsMonitorRouteAreaBO;
    private LsSystemDepartmentBO lsSystemDepartmentBO;
    private LsSystemUserBO lsSystemUserBO;

    @Resource
    private CommonPatrolService commonPatrolService;
    @Resource
    private SystemDepartmentService systemDepartmentService;
    @Resource
    private MonitorRouteAreaService monitorRouteAreaService;
    @Resource
    private WarehouseTrackUnitService warehouseTrackUnitService;
    @Resource
    private CommonVehicleService vehicleService;
    @Resource
    private SystemUserService systemUserService;
    private String[] patrolIds; // 删除的时候获取table行的Id
    @Resource
    private SystemOperateLogService logService;
    /**
     * 车载台安装照片
     */
    /**
     * 拍摄的行程照片Base64值列表
     */
    private String[] trackUnitCameraBase64;
    /**
     * 文件传输方式。0：本地/网络共享路径，1：FTP协议。
     */
    @Resource
    private String fileTransferType;
    /**
     * 车载台安装照片存储路径
     */
    @Resource
    private String trackUnitPhotoPath;
    /**
     * 系统存储根目录，绝对路径
     */
    @Resource
    private String rootPath;
    /**
     * 系统存储根目录访问路径
     */
    @Resource
    private String rootPathHttp;

    private Logger logger = Logger.getLogger(this.getClass().getName());
    /**
     * FTP主机名
     */
    @Resource
    private String ftpHostName;
    /**
     * FTP端口
     */
    @Resource
    private String ftpPort;
    /**
     * FTP登录用户名
     */
    @Resource
    private String ftpUserName;
    /**
     * FTP登录密码
     */
    @Resource
    private String ftpPassword;

    /**
     * 初始化页面
     * 
     * @return
     */
    @Action(value = "toList", results = { @Result(name = "success", location = "/patrolmgmt/patrolList.jsp") })
    public String toList() {
        // 所属节点，获取所有口岸，前台循环显示
        String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
        deptList = systemDepartmentService.findAllPortByUserId(userId);
        // 获取所有区域
        areaList = monitorRouteAreaService.findAllArea();
        // 获取所有车载台编号
        trackList = warehouseTrackUnitService.findAllTrackUnit();
        // 获取所有负责人列表，即用户名
        userList = systemUserService.findAllPatrolUsers();
        return SUCCESS;
    }

    /**
     * 列表展现
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Action(value = "list")
    public void list() throws IOException {

        /*
         * List<PatrolDepartmentVO> patrolDepartmentList =
         * commonPatrolService.getPatrolDapatmentlist(); JSONArray retJson =
         * JSONArray.fromObject(patrolDepartmentList); PrintWriter out =
         * response.getWriter(); out.print(retJson.toString()); out.flush();
         * out.close();
         */

        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = commonPatrolService.fromObjectList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();

    }

    /**
     * 添加模态框显示
     * 
     * @return
     */
    @Action(value = "addModal", results = { @Result(name = "success", location = "/patrolmgmt/patrolAdd.jsp"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String addModal() {
        // 所属节点，获取所有口岸，前台循环显示
        // String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
        // deptList = systemDepartmentService.findAllPortByUserId(userId);
        // 获取所有区域(区域类型为"4")
        // areaList = monitorRouteAreaService.findAllArea();
        this.areaList = this.monitorRouteAreaService.findAllPatrolArea(RouteAreaType.SEPARATE_AREA.getText());
        // 不可以为巡逻队添加已经被绑定的车载台
        // 获取所有车载台
        trackList = warehouseTrackUnitService.findAllTrackUnit();
        // 获取所有的巡逻队
        patrolList = commonPatrolService.findAllCommonPatrol();

        for (int i = 0; i < patrolList.size(); i++) {
            for (int j = 0; j < trackList.size(); j++) {
                if (NuctechUtil.isNotNull(patrolList.get(i).getTrackUnitNumber())
                        && patrolList.get(i).getTrackUnitNumber().equals(trackList.get(j).getTrackUnitNumber())) {
                    trackList.remove(trackList.get(j));
                }
            }
        }
        // 不可以为巡逻队添加已经绑定的车牌号
        // 获取所有的车辆
        vehicleList = vehicleService.findAllCommonVehicle();

        for (int i = 0; i < patrolList.size(); i++) {
            for (int j = 0; j < vehicleList.size(); j++) {
                if (NuctechUtil.isNotNull(patrolList.get(i).getVehiclePlateNumber()) && patrolList.get(i)
                        .getVehiclePlateNumber().equals(vehicleList.get(j).getVehiclePlateNumber())) {
                    vehicleList.remove(vehicleList.get(j));
                }

            }
        }

        // 获取所有负责人列表，即用户名(负责人只能是巡逻队角色)
        userList = systemUserService.findAllPatrolUsers();
        return SUCCESS;

    }

    /**
     * 添加巡逻队
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "addPatrol", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String addPatrol() throws Exception {
        try {
            if (lsCommonPatrolBO != null) {
                lsCommonPatrolBO.setPatrolId(generatePrimaryKey());
                lsCommonPatrolBO.setCreateTime(new Date());
                String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
                lsCommonPatrolBO.setCreateUser(userId);
                lsCommonPatrolBO.setDeleteMark(Constant.MARK_UN_DELETED);

                // 保存车载台安装照片(拍照)
                String patrolId = lsCommonPatrolBO.getPatrolId();
                List<String> filePaths = saveTripPhotoCamera(patrolId);
                lsCommonPatrolBO.setPatrolInstalPicture(StringUtils.join(filePaths, ","));
                commonPatrolService.add(getLsCommonPatrolBO());

                addLog(OperateContentType.ADD.toString(), OperateEntityType.PATROL.toString(),
                        lsCommonPatrolBO.toString());

                return SUCCESS;
            }
            return ERROR;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    /**
     * 修改模态框弹出
     * 
     * @return
     */
    @Action(value = "editModal", results = { @Result(name = "success", location = "/patrolmgmt/patrolEdit.jsp"),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editModal() {
        // 所属节点，获取所有口岸，前台循环显示
        // String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
        // deptList = systemDepartmentService.findAllPortByUserId(userId);
        // 获取所有区域(区域分类型)
        // areaList = monitorRouteAreaService.findAllArea();
        this.areaList = this.monitorRouteAreaService.findAllPatrolArea(RouteAreaType.SEPARATE_AREA.getText());
        // 不可以为巡逻队添加已经被绑定的车载台
        // 获取所有车载台
        trackList = warehouseTrackUnitService.findAllTrackUnit();
        // 获取所有负责人列表，即用户名(负责人只能是巡逻队角色)
        patrolList = commonPatrolService.findAllCommonPatrol();

        for (int i = 0; i < patrolList.size(); i++) {
            for (int j = 0; j < trackList.size(); j++) {
                if (NuctechUtil.isNotNull(patrolList.get(i).getTrackUnitNumber())
                        && patrolList.get(i).getTrackUnitNumber().equals(trackList.get(j).getTrackUnitNumber())) {
                    trackList.remove(trackList.get(j));
                }
            }
        }

        // 不可以为巡逻队添加已经绑定的车牌号
        // 获取所有的车辆
        vehicleList = vehicleService.findAllCommonVehicle();

        for (int i = 0; i < patrolList.size(); i++) {
            for (int j = 0; j < vehicleList.size(); j++) {
                try {
                    if (NuctechUtil.isNotNull(patrolList.get(i).getVehiclePlateNumber()) && patrolList.get(i)
                            .getVehiclePlateNumber().equals(vehicleList.get(j).getVehiclePlateNumber())) {
                        vehicleList.remove(vehicleList.get(j));
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        // 获取所有负责人列表，即用户名
        userList = systemUserService.findAllPatrolUsers();

        lsCommonPatrolBO = commonPatrolService.findCommonPatrolById(patrolId);

        lsMonitorRouteAreaBO = monitorRouteAreaService.findMonitorRouteAreaById(lsCommonPatrolBO.getBelongToArea());
        // lsSystemDepartmentBO =
        // systemDepartmentService.findById(lsCommonPatrolBO.getBelongToPort());
        lsSystemUserBO = systemUserService.findById(lsCommonPatrolBO.getPotralUser());
        return SUCCESS;
    }

    /**
     * 修改巡逻队
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "editPatrol", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editPatrol() throws Exception {
        if (lsCommonPatrolBO != null) {
            lsCommonPatrolBO.setUpdateTime(new Date());
            String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
            lsCommonPatrolBO.setUpdateUser(userId);

            // 保存车载台安装照片(拍照)
            String patrolId = lsCommonPatrolBO.getPatrolId();
            List<String> filePaths = saveTripPhotoCamera(patrolId);
            lsCommonPatrolBO.setPatrolInstalPicture(StringUtils.join(filePaths, ","));

            commonPatrolService.updatePatrol(lsCommonPatrolBO);

            addLog(OperateContentType.EDIT.toString(), OperateEntityType.PATROL.toString(),
                    lsCommonPatrolBO.toString());

            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    /**
     * 删除巡逻队
     * 
     * @return
     */
    @Action(value = "delPatrolById",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }) })
    public String delPatrolById() {
        if (patrolIds != null) {
            String s[] = patrolIds[0].split(",");
            for (int i = 0; i < s.length; i++) {
                commonPatrolService.deleteById(s[i]);
                addLog(OperateContentType.DELETE.toString(), OperateEntityType.PATROL.toString(), "patrolId:" + s[i]);
            }
        }
        return SUCCESS;
    }

    /**
     * 巡逻队编号重复验证
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "repeate")
    public void repeate() throws Exception {
        JSONObject jsonObject = new JSONObject();
        String patrolNumber = lsCommonPatrolBO.getPatrolNumber();
        LsCommonPatrolBO lsCommonPatrolBO = commonPatrolService.findCommonPatrolByPatrolNumber(patrolNumber);
        if (NuctechUtil.isNull(lsCommonPatrolBO)) {
            jsonObject.put("valid", true);
        } else {
            jsonObject.put("valid", false);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(jsonObject.toString());
        out.flush();
        out.close();
    }

    public List<LsSystemDepartmentBO> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<LsSystemDepartmentBO> deptList) {
        this.deptList = deptList;
    }

    public List<LsMonitorRouteAreaBO> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<LsMonitorRouteAreaBO> areaList) {
        this.areaList = areaList;
    }

    public List<LsWarehouseTrackUnitBO> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<LsWarehouseTrackUnitBO> trackList) {
        this.trackList = trackList;
    }

    public LsCommonPatrolBO getLsCommonPatrolBO() {
        return lsCommonPatrolBO;
    }

    public void setLsCommonPatrolBO(LsCommonPatrolBO lsCommonPatrolBO) {
        this.lsCommonPatrolBO = lsCommonPatrolBO;
    }

    public String getPatrolId() {
        return patrolId;
    }

    public void setPatrolId(String patrolId) {
        this.patrolId = patrolId;
    }

    public LsWarehouseTrackUnitBO getLsWarehouseTrackUnitBO() {
        return lsWarehouseTrackUnitBO;
    }

    public void setLsWarehouseTrackUnitBO(LsWarehouseTrackUnitBO lsWarehouseTrackUnitBO) {
        this.lsWarehouseTrackUnitBO = lsWarehouseTrackUnitBO;
    }

    public LsMonitorRouteAreaBO getLsMonitorRouteAreaBO() {
        return lsMonitorRouteAreaBO;
    }

    public void setLsMonitorRouteAreaBO(LsMonitorRouteAreaBO lsMonitorRouteAreaBO) {
        this.lsMonitorRouteAreaBO = lsMonitorRouteAreaBO;
    }

    public LsSystemDepartmentBO getLsSystemDepartmentBO() {
        return lsSystemDepartmentBO;
    }

    public void setLsSystemDepartmentBO(LsSystemDepartmentBO lsSystemDepartmentBO) {
        this.lsSystemDepartmentBO = lsSystemDepartmentBO;
    }

    public LsSystemUserBO getLsSystemUserBO() {
        return lsSystemUserBO;
    }

    public void setLsSystemUserBO(LsSystemUserBO lsSystemUserBO) {
        this.lsSystemUserBO = lsSystemUserBO;
    }

    public String[] getPatrolIds() {
        return patrolIds;
    }

    public void setPatrolIds(String[] patrolIds) {
        this.patrolIds = patrolIds;
    }

    public List<LsCommonPatrolBO> getPatrolList() {
        return patrolList;
    }

    public void setPatrolList(List<LsCommonPatrolBO> patrolList) {
        this.patrolList = patrolList;
    }

    public String[] getTrackUnitCameraBase64() {
        return trackUnitCameraBase64;
    }

    public void setTrackUnitCameraBase64(String[] trackUnitCameraBase64) {
        this.trackUnitCameraBase64 = trackUnitCameraBase64;
    }

    public String getTrackUnitPhotoPath() {
        return trackUnitPhotoPath;
    }

    public void setTrackUnitPhotoPath(String trackUnitPhotoPath) {
        this.trackUnitPhotoPath = trackUnitPhotoPath;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getRootPathHttp() {
        return rootPathHttp;
    }

    public void setRootPathHttp(String rootPathHttp) {
        this.rootPathHttp = rootPathHttp;
    }

    public List<SessionUser> getUserList() {
        return userList;
    }

    public void setUserList(List<SessionUser> userList) {
        this.userList = userList;
    }

    public List<LsCommonVehicleBO> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<LsCommonVehicleBO> vehicleList) {
        this.vehicleList = vehicleList;
    }

    /**
     * 保存拍照的照片
     * 
     * @param tripId
     * @return
     */
    private List<String> saveTripPhotoCamera(String patrolId) {
        List<String> filePaths = new ArrayList<String>();
        if (trackUnitCameraBase64 != null) {
            long now = new Date().getTime();
            for (int i = 0; i < trackUnitCameraBase64.length; i++) {
                String fileName = now + "-" + i + ".jpg";
                if ("0".equals(fileTransferType)) {
                    String targetPath = rootPath + "/" + trackUnitPhotoPath + "/" + patrolId;
                    uploadBase64ByLocal(targetPath, fileName, trackUnitCameraBase64[i]);
                } else if ("1".equals(fileTransferType)) {
                    String targetPath = "/" + trackUnitPhotoPath + "/" + patrolId;
                    uploadBase64ByFTP(targetPath, fileName, trackUnitCameraBase64[i]);
                }
                filePaths.add("/" + patrolId + "/" + fileName);
            }
        }
        return filePaths;
    }

    /**
     * 本地/网络共享保存base64编码到图片
     * 
     * @param targetPath
     *        目标路径
     * @param fileName
     *        文件名
     * @param base64
     *        编码
     * @return
     */
    private String uploadBase64ByLocal(String targetPath, String fileName, String base64) {
        base64ToFile(base64, targetPath + "/" + fileName);
        return fileName;
    }

    /**
     * 保存Base64内容为图片
     * 
     * @param base64
     * @param filePath
     *        文件绝对路径
     */
    private void base64ToFile(String base64, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            out.write(Base64.decode(base64));
            out.close();
            logger.info("保存Base64到文件：" + file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            logger.error("保存图像出错！", e);
        } catch (IOException e) {
            logger.error("保存图像出错！", e);
        }
    }

    /**
     * 保存base64编码到图片，并通过FTP上传
     * 
     * @param targetPath
     * @param fileName
     * @param base64
     * @return
     */
    private String uploadBase64ByFTP(String targetPath, String fileName, String base64) {
        String tempDir = System.getProperty("java.io.tmpdir");
        String localPath = tempDir + targetPath + "/" + fileName;
        String remotePath = targetPath + "/" + fileName;

        base64ToFile(base64, localPath);
        boolean flag = UploadDownloadUtil.ftpUpload(ftpHostName, Integer.parseInt(ftpPort), ftpUserName, ftpPassword,
                remotePath, localPath);
        logger.info(String.format("FTP上传文件 %s -> %s 上传结果：%s", localPath, remotePath, flag));
        return remotePath;
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

    /**
     * 在地图上显示巡逻队
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Action(value = "findAllPatrols",
            results = { @Result(name = "success", type = "json"), @Result(name = "error", type = "json") })
    public void findAllPatrols() throws Exception {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = commonPatrolService.fromObjectList(pageQuery, null, false);
        this.response.getWriter().println(retJson);
    }
}
