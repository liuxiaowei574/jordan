package com.nuctech.ls.center.map.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.common.LsCommonPatrolBO;
import com.nuctech.ls.model.bo.monitor.LsMonitorRouteAreaBO;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.bo.warehouse.LsWarehouseTrackUnitBO;
import com.nuctech.ls.model.service.CommonPatrolService;
import com.nuctech.ls.model.service.MonitorRouteAreaService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.ls.model.service.WarehouseTrackUnitService;
import com.nuctech.ls.model.util.RouteAreaType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.ls.model.vo.warehouse.PatrolDepartmentVO;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;

@ParentPackage("json-default")
@Namespace("/patrol")
public class CommonPatrolAction extends LSBaseAction {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String message;// 记录运行信息
    private static final long serialVersionUID = 1L;
    private List<LsCommonPatrolBO> lsCommonPatrolBOs;
    private List<LsSystemDepartmentBO> lsSystemDepartmentBOs;
    private List<LsMonitorRouteAreaBO> LsMonitorRouteAreaBOs;
    private List<LsWarehouseTrackUnitBO> LsWarehouseTrackUnitBOs;
    private List<SessionUser> lsSystemUserBOs;

    private LsCommonPatrolBO lsCommonPatrolBO;
    private List<LsCommonPatrolBO> patrolList = new ArrayList<LsCommonPatrolBO>();
    @Resource
    private CommonPatrolService commonPatrolService;
    @Resource
    private MonitorRouteAreaService monitorRouteAreaService;
    @Resource
    private SystemDepartmentService systemDepartmentService;
    @Resource
    private WarehouseTrackUnitService warehouseTrackUnitService;
    @Resource
    private SystemUserService systemUserService;

    private boolean success;// 结果
    private String ids;// id集合
    private String patrolUserName;

    @Action(value = "findAllCommonPatrols", results = { @Result(name = "success", type = "json") })
    public String findAllCommonPatrols() throws Exception {
        logger.info("查询巡逻队列表");
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        String organizationId = "";
        if (sessionUser != null) {
            organizationId = sessionUser.getOrganizationId();
        }
        try {
            this.lsCommonPatrolBOs = this.commonPatrolService.findAllCommonPatrol(organizationId);
            this.success = true;
        } catch (Exception e) {
            message = e.getMessage();
            logger.error(message);
            this.success = false;
        }
        return SUCCESS;
    }

    @Action(value = "findSelectData", results = { @Result(name = "success", type = "json") })
    public String findSelectData() throws Exception {
        logger.info("查询所有的口岸、区域划分以及车载台~");
        try {
            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            String userId = sessionUser.getUserId();
            this.lsSystemDepartmentBOs = this.systemDepartmentService.findAllPortByUserId(userId);
            this.LsMonitorRouteAreaBOs = this.monitorRouteAreaService
                    .findAllPatrolArea(RouteAreaType.SEPARATE_AREA.getText());

            // this.LsWarehouseTrackUnitBOs =
            // this.warehouseTrackUnitService.findAllTrackUnits();
            // 不可以为巡逻队添加已经被绑定的车载台
            // 获取所有车载台
            LsWarehouseTrackUnitBOs = warehouseTrackUnitService.findAllTrackUnit();
            // 获取所有的巡逻队
            patrolList = commonPatrolService.findAllCommonPatrol();

            for (int i = 0; i < patrolList.size(); i++) {
                for (int j = 0; j < LsWarehouseTrackUnitBOs.size(); j++) {
                    if (patrolList.get(i).getTrackUnitNumber()
                            .equals(LsWarehouseTrackUnitBOs.get(j).getTrackUnitNumber())) {
                        LsWarehouseTrackUnitBOs.remove(LsWarehouseTrackUnitBOs.get(j));
                    }
                }
            }

            // this.lsSystemUserBOs =
            // this.systemUserService.findUserByRoleIds(RoleType.enforcementPatrol.getType(),RoleType.escortPatrol.getType());

            // 获取所有负责人列表，即用户名(负责人只能是巡逻队角色)
            lsSystemUserBOs = systemUserService.findAllPatrolUsers();
            this.success = true;
        } catch (Exception e) {
            message = e.getMessage();
            logger.error(message);
            this.success = false;
        }
        return SUCCESS;
    }

    @Action(value = "editPatrol", results = { @Result(name = "success", type = "json") })
    public String editPatrol() throws Exception {
        logger.info("编辑巡逻队获取到的patrolId是：" + ids);
        try {
            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            String userId = sessionUser.getUserId();
            this.lsCommonPatrolBO = this.commonPatrolService.findCommonPatrolById(ids);
            if (this.lsCommonPatrolBO != null) {
                if (this.lsCommonPatrolBO.getPotralUser() != null) {
                    this.patrolUserName = this.systemUserService.findById(lsCommonPatrolBO.getPotralUser())
                            .getUserName();
                }
            }
            this.lsSystemDepartmentBOs = this.systemDepartmentService.findAllPortByUserId(userId);
            this.LsMonitorRouteAreaBOs = this.monitorRouteAreaService
                    .findAllPatrolArea(RouteAreaType.SEPARATE_AREA.getText());
                    // this.LsWarehouseTrackUnitBOs =
                    // this.warehouseTrackUnitService.findAllTrackUnits();

            // 不可以为巡逻队添加已经被绑定的车载台
            // 获取所有车载台
            LsWarehouseTrackUnitBOs = warehouseTrackUnitService.findAllTrackUnit();
            // 获取所有负责人列表，即用户名(负责人只能是巡逻队角色)
            patrolList = commonPatrolService.findAllCommonPatrol();

            for (int i = 0; i < patrolList.size(); i++) {
                for (int j = 0; j < LsWarehouseTrackUnitBOs.size(); j++) {
                    if (patrolList.get(i).getTrackUnitNumber()
                            .equals(LsWarehouseTrackUnitBOs.get(j).getTrackUnitNumber())) {
                        LsWarehouseTrackUnitBOs.remove(LsWarehouseTrackUnitBOs.get(j));
                    }
                }
            }

            // this.lsSystemUserBOs =
            // this.systemUserService.findUserByRoleIds(RoleType.enforcementPatrol.getType(),RoleType.escortPatrol.getType());
            lsSystemUserBOs = systemUserService.findAllPatrolUsers();

            this.success = true;
        } catch (Exception e) {
            message = e.getMessage();
            logger.error(message);
            this.success = false;
        }

        return SUCCESS;
    }

    @Action(value = "deletePatrols", results = { @Result(name = "success", type = "json") })
    public String deletePatrols() throws Exception {
        logger.info("删除巡逻队获取到的patrolId是：" + ids);
        int count = commonPatrolService.delPatrolsByIds(getStrResult(ids));
        if (count > 0) {
            this.success = true;
        } else {
            this.success = false;
        }

        return SUCCESS;
    }

    @Action(value = "addPatrol", results = { @Result(name = "success", type = "json") })
    public String addPatrol() throws Exception {
        LsCommonPatrolBO lsCommonPatrolBO = new LsCommonPatrolBO();
        lsCommonPatrolBO.setPatrolId(generatePrimaryKey());
        lsCommonPatrolBO.setPotralUser(potralUser);
        lsCommonPatrolBO.setBelongToArea(belongToArea);
        lsCommonPatrolBO.setBelongToPort(belongToPort1);
        lsCommonPatrolBO.setTrackUnitNumber(trackUnitNumber);
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        if (sessionUser != null) {
            lsCommonPatrolBO.setCreateUser(sessionUser.getUserId());
        }
        lsCommonPatrolBO.setCreateTime(new Date());
        lsCommonPatrolBO.setDeleteMark(Constant.MARK_UN_DELETED);
        try {
            commonPatrolService.addPatrol(lsCommonPatrolBO);
            this.success = true;
        } catch (Exception e1) {
            e1.printStackTrace();
            this.success = false;
        }
        logger.info(String.format("add Patrol，巡逻队编号：%s", lsCommonPatrolBO.getPatrolId()));

        return SUCCESS;
    }

    @Action(value = "updatePatrol", results = { @Result(name = "success", type = "json") })
    public String updatePatrol() throws Exception {
        LsCommonPatrolBO lsCommonPatrolBO = commonPatrolService.findCommonPatrolById(patrolId);
        lsCommonPatrolBO.setPatrolId(patrolId);
        lsCommonPatrolBO.setPotralUser(potralUser);
        lsCommonPatrolBO.setBelongToArea(belongToArea);
        lsCommonPatrolBO.setBelongToPort(belongToPort1);

        lsCommonPatrolBO.setTrackUnitNumber(trackUnitNumber);
        SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
        if (sessionUser != null) {
            lsCommonPatrolBO.setUpdateUser(sessionUser.getUserId());
        }
        lsCommonPatrolBO.setUpdateTime(new Date());
        lsCommonPatrolBO.setDeleteMark(Constant.MARK_UN_DELETED);
        try {
            commonPatrolService.updatePatrol(lsCommonPatrolBO);
            this.success = true;
        } catch (Exception e1) {
            e1.printStackTrace();
            this.success = false;
        }
        logger.info(String.format("update Patrol，巡逻队编号：%s", patrolId));

        return SUCCESS;
    }

    /**
     * 对接收到的字符串进行处理
     * 
     * @return
     */
    public String getStrResult(String str) {
        StringBuffer sb = new StringBuffer();
        if (NuctechUtil.isNotNull(str)) {
            if (str.indexOf(",") > 0) {
                String[] strs = str.split(",");
                for (int i = 0; i < strs.length - 1; i++) {
                    sb.append("'" + strs[i] + "',");
                }
                sb.append("'" + strs[strs.length - 1] + "'");
            } else {
                sb.append("'" + str + "'");
            }
        }
        return sb.toString();
    }

    /**
     * 在显示巡逻队的消息
     */
    @Action(value = "dlist", results = { @Result(name = "success", type = "json") })
    public void dlist() throws IOException {

        List<PatrolDepartmentVO> patrolDepartmentList = commonPatrolService.getPatrolDapatmentlist();

        JSONArray retJson = JSONArray.fromObject(patrolDepartmentList);
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /* 巡逻队主键 */
    private String patrolId;

    /* 车载台编号 */
    private String trackUnitNumber;

    /* 所属区域 */
    private String belongToArea;

    /* 所属节点 */
    private String belongToPort1;

    /* 负责人 */
    private String potralUser;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public String getPatrolId() {
        return patrolId;
    }

    public void setPatrolId(String patrolId) {
        this.patrolId = patrolId;
    }

    public String getTrackUnitNumber() {
        return trackUnitNumber;
    }

    public void setTrackUnitNumber(String trackUnitNumber) {
        this.trackUnitNumber = trackUnitNumber;
    }

    public String getBelongToArea() {
        return belongToArea;
    }

    public void setBelongToArea(String belongToArea) {
        this.belongToArea = belongToArea;
    }

    public String getBelongToPort1() {
        return belongToPort1;
    }

    public void setBelongToPort1(String belongToPort1) {
        this.belongToPort1 = belongToPort1;
    }

    public String getPotralUser() {
        return potralUser;
    }

    public void setPotralUser(String potralUser) {
        this.potralUser = potralUser;
    }

    public List<LsCommonPatrolBO> getLsCommonPatrolBOs() {
        return lsCommonPatrolBOs;
    }

    public void setLsCommonPatrolBOs(List<LsCommonPatrolBO> lsCommonPatrolBOs) {
        this.lsCommonPatrolBOs = lsCommonPatrolBOs;
    }

    public LsCommonPatrolBO getLsCommonPatrolBO() {
        return lsCommonPatrolBO;
    }

    public void setLsCommonPatrolBO(LsCommonPatrolBO lsCommonPatrolBO) {
        this.lsCommonPatrolBO = lsCommonPatrolBO;
    }

    public List<LsSystemDepartmentBO> getLsSystemDepartmentBOs() {
        return lsSystemDepartmentBOs;
    }

    public void setLsSystemDepartmentBOs(List<LsSystemDepartmentBO> lsSystemDepartmentBOs) {
        this.lsSystemDepartmentBOs = lsSystemDepartmentBOs;
    }

    public List<LsMonitorRouteAreaBO> getLsMonitorRouteAreaBOs() {
        return LsMonitorRouteAreaBOs;
    }

    public void setLsMonitorRouteAreaBOs(List<LsMonitorRouteAreaBO> lsMonitorRouteAreaBOs) {
        LsMonitorRouteAreaBOs = lsMonitorRouteAreaBOs;
    }

    public List<LsWarehouseTrackUnitBO> getLsWarehouseTrackUnitBOs() {
        return LsWarehouseTrackUnitBOs;
    }

    public void setLsWarehouseTrackUnitBOs(List<LsWarehouseTrackUnitBO> lsWarehouseTrackUnitBOs) {
        LsWarehouseTrackUnitBOs = lsWarehouseTrackUnitBOs;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getPatrolUserName() {
        return patrolUserName;
    }

    public void setPatrolUserName(String patrolUserName) {
        this.patrolUserName = patrolUserName;
    }

    public List<SessionUser> getLsSystemUserBOs() {
        return lsSystemUserBOs;
    }

    public void setLsSystemUserBOs(List<SessionUser> lsSystemUserBOs) {
        this.lsSystemUserBOs = lsSystemUserBOs;
    }
}
