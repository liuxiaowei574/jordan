package com.nuctech.ls.center.alarm.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.dm.LsDmAlarmLevelBO;
import com.nuctech.ls.model.bo.dm.LsDmAlarmTypeBO;
import com.nuctech.ls.model.bo.system.LsSystemParamsBO;
import com.nuctech.ls.model.service.AlarmLevelModifyService;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.service.SystemParamsService;
import com.nuctech.ls.model.util.OperateContentType;
import com.nuctech.ls.model.util.OperateEntityType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 作者：赵苏阳 描述：
 * <p>
 * 报警级别修改
 * <p>
 * 创建时间2016年6月19
 *
 */
@Namespace("/alarmModifyMgmt")
public class AlarmMgmtAction extends LSBaseAction {

    private static final long serialVersionUID = 1l;
    protected static final String DEFAULT_SORT_COLUMNS = "l.alarmLevelId ASC";
    private LsDmAlarmTypeBO lsDmAlarmTypeBO;
    private LsDmAlarmLevelBO lsDmAlarmLevelBO;

    @Resource
    private AlarmLevelModifyService alarmLevelModifyService;
    @Resource
    private SystemParamsService systemParamsService;
    @Resource
    private SystemOperateLogService logService;
    /**
     * 获取前台传过来的报警名称和报警级别
     */
    private String alarmLevel;
    private String alarmName;
    private String alarmId;// 报警类型主键
    private String alarmLevelId;// 报警级别主键

    private String alarmTypeId;// 报警类型的主键
    private String createUser;

    private String[] paramIds;// 获取参数id
    private String[] paramvalue;// 参数值

    // 列表
    private String name;// 参数的名称
    private String value;// 参数的值

    // 进入报警管理页面
    @Action(value = "toList", results = { @Result(name = "success", location = "/alarm/alarmMgmt/alarmMgmtList.jsp") })
    public String toList() {
        return SUCCESS;
    }

    /**
     * 报警类型和报警级别两表的关联查询
     * 
     * @throws IOException
     */
    @Action(value = "list")
    public void list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = alarmLevelModifyService.findAllAlarmTypeLevel(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /**
     * 修改报警级别的模态框弹出
     * 
     * @return
     */
    @Action(value = "editAlarmModal",
            results = { @Result(name = "success", location = "/alarm/alarmMgmt/alarmLevelEdit.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editAlarmModal() {

        lsDmAlarmTypeBO = this.alarmLevelModifyService.find(alarmTypeId);
        lsDmAlarmLevelBO = this.alarmLevelModifyService.findAlarmLevel(alarmLevelId);

        // 将报警级别主键放到request中，前台用el表达式取出放在修改模态框的输入中

        request.setAttribute("alarmTypeId", alarmTypeId);

        return SUCCESS;
    }

    // 修改
    @Action(value = "editAlarm1", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editAlarm1() throws Exception {
        String value[] = paramvalue[0].split(",");
        if (lsDmAlarmTypeBO != null && !value[0].equals("") && value.length == 1) {
            LsDmAlarmTypeBO alarmTypeBO = alarmLevelModifyService.find(lsDmAlarmTypeBO.getAlarmTypeId());
            BeanUtils.copyProperties(lsDmAlarmTypeBO, alarmTypeBO,
                    new String[] { "alarmTypeId", "alarmTypeName", "alarmTypeCode", "createTime", "createUser" });

            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            alarmTypeBO.setUpdateUser(sessionUser.getUserId());
            alarmTypeBO.setUpdateTime(new Date());
            alarmTypeBO.setParamId("5");
            this.alarmLevelModifyService.modify(alarmTypeBO);

            // 修改参数列表的值，如果只有一个值，则直接修改参数“低电量预警值得参数值”如果是两个值则依次修改“长时间逗留”和“静止速度”两个参数的值
            LsSystemParamsBO systemParamsBO = systemParamsService.findById("5");
            systemParamsBO.setParamValue(value[0]);
            systemParamsService.modifyParams(systemParamsBO);
            return SUCCESS;
        } else if (lsDmAlarmTypeBO != null && !value[0].equals("") && value.length == 2) {
            LsDmAlarmTypeBO alarmTypeBO = alarmLevelModifyService.find(lsDmAlarmTypeBO.getAlarmTypeId());
            BeanUtils.copyProperties(lsDmAlarmTypeBO, alarmTypeBO,
                    new String[] { "alarmTypeId", "alarmTypeName", "alarmTypeCode", "createTime", "createUser" });

            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            alarmTypeBO.setUpdateUser(sessionUser.getUserId());
            alarmTypeBO.setUpdateTime(new Date());
            alarmTypeBO.setParamId("3,4");
            this.alarmLevelModifyService.modify(alarmTypeBO);

            // 依次修改“长时间逗留”和“静止速度”两个参数的值
            LsSystemParamsBO systemParamsBO = systemParamsService.findById("3");// 修改“长时间逗留”参数
            systemParamsBO.setParamValue(value[0]);
            systemParamsService.modifyParams(systemParamsBO);
            // 修改“静止速度”的参数值
            LsSystemParamsBO sBo = systemParamsService.findById("4");
            sBo.setParamValue(value[1]);
            systemParamsService.modifyParams(sBo);
            return SUCCESS;
        } else if (lsDmAlarmTypeBO != null) {
            LsDmAlarmTypeBO alarmTypeBO = alarmLevelModifyService.find(lsDmAlarmTypeBO.getAlarmTypeId());
            BeanUtils.copyProperties(lsDmAlarmTypeBO, alarmTypeBO,
                    new String[] { "alarmTypeId", "alarmTypeName", "alarmTypeCode", "createTime", "createUser" });

            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            alarmTypeBO.setUpdateUser(sessionUser.getUserId());
            alarmTypeBO.setUpdateTime(new Date());
            this.alarmLevelModifyService.modify(alarmTypeBO);
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    /**
     * 显示表格中的内容
     * 
     * @throws IOException
     */
    @Action(value = "dlist", results = { @Result(name = "success", type = "json") })
    public void dlist() throws IOException {

        List<LsSystemParamsBO> paramsBOsList = systemParamsService.getParams(alarmTypeId);

        JSONArray retJson = JSONArray.fromObject(paramsBOsList);
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /**
     * 对报警管理进行修改（报警级别修改和参数修改）
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "editAlarm", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editAlarm() throws Exception {
        // 修改修改报警类型表（"严重"或"轻微"）和系统参数表
        if (lsDmAlarmTypeBO != null && name != null) {
            // 修改修改报警类型表（"严重"或"轻微"）
            LsDmAlarmTypeBO alarmTypeBO = alarmLevelModifyService.find(alarmTypeId);
            alarmTypeBO.setAlarmLevelId(lsDmAlarmTypeBO.getAlarmLevelId());
            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            alarmTypeBO.setUpdateUser(sessionUser.getUserId());
            alarmTypeBO.setUpdateTime(new Date());
            this.alarmLevelModifyService.modify(alarmTypeBO);
            // 修改系统参数表
            String paramName[] = name.split(",");
            String paramValue[] = value.split(",");
            for (int i = 0; i < paramName.length; i++) {
                LsSystemParamsBO systemParamsBO = systemParamsService.findByName(paramName[i]);
                systemParamsBO.setParamValue(paramValue[i]);
                systemParamsService.modifyParams(systemParamsBO);
            }

            addLog(OperateContentType.EDIT.toString(), OperateEntityType.ALARMTYPR.toString(), alarmTypeBO.toString());

            return SUCCESS;
        } else if (lsDmAlarmTypeBO != null) {// 报警类型无对应的系统参数时，只修改报警类型表
            LsDmAlarmTypeBO alarmTypeBO = alarmLevelModifyService.find(alarmTypeId);
            alarmTypeBO.setAlarmLevelId(lsDmAlarmTypeBO.getAlarmLevelId());
            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            alarmTypeBO.setUpdateUser(sessionUser.getUserId());
            alarmTypeBO.setUpdateTime(new Date());
            this.alarmLevelModifyService.modify(alarmTypeBO);

            addLog(OperateContentType.EDIT.toString(), OperateEntityType.ALARMTYPR.toString(), alarmTypeBO.toString());

            return SUCCESS;
        } else {
            return ERROR;
        }

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

    public String getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public LsDmAlarmTypeBO getLsDmAlarmTypeBO() {
        return lsDmAlarmTypeBO;
    }

    public void setLsDmAlarmTypeBO(LsDmAlarmTypeBO lsDmAlarmTypeBO) {
        this.lsDmAlarmTypeBO = lsDmAlarmTypeBO;
    }

    public AlarmLevelModifyService getAlarmLevelModifyService() {
        return alarmLevelModifyService;
    }

    public void setAlarmLevelModifyService(AlarmLevelModifyService alarmLevelModifyService) {
        this.alarmLevelModifyService = alarmLevelModifyService;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmTypeId() {
        return alarmTypeId;
    }

    public void setAlarmTypeId(String alarmTypeId) {
        this.alarmTypeId = alarmTypeId;
    }

    public String getAlarmLevelId() {
        return alarmLevelId;
    }

    public void setAlarmLevelId(String alarmLevelId) {
        this.alarmLevelId = alarmLevelId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public LsDmAlarmLevelBO getLsDmAlarmLevelBO() {
        return lsDmAlarmLevelBO;
    }

    public void setLsDmAlarmLevelBO(LsDmAlarmLevelBO lsDmAlarmLevelBO) {
        this.lsDmAlarmLevelBO = lsDmAlarmLevelBO;
    }

    public String[] getParamIds() {
        return paramIds;
    }

    public void setParamIds(String[] paramIds) {
        this.paramIds = paramIds;
    }

    public String[] getParamvalue() {
        return paramvalue;
    }

    public void setParamvalue(String[] paramvalue) {
        this.paramvalue = paramvalue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
