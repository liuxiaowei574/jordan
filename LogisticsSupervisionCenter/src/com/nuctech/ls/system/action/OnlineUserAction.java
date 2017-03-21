package com.nuctech.ls.system.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemUserLogBO;
import com.nuctech.ls.model.service.SystemUserLogService;
import com.nuctech.ls.model.service.SystemUserService;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;

@Namespace("/onlineUser")
public class OnlineUserAction extends LSBaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = -7374369190345994183L;

    private static Logger logger = Logger.getLogger(OnlineUserAction.class);
    protected static final String DEFAULT_SORT_COLUMNS = "t.logonTime DESC";
    protected static final String USER_LOG_DEFAULT_SORT_COLUMNS = "";

    @Resource
    private SystemUserLogService systemUserLogService;
    @Resource
    private SystemUserService systemUserService;
    private LsSystemUserLogBO systemUserLog;

    @Action(value = "toList", results = { @Result(name = "success", location = "/system/online/list.jsp") })
    public String toList() {
        return SUCCESS;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "list")
    public void list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = systemUserLogService.findSystemUserLogList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    @Action(value = "toOnlineUserDetailModal",
            results = { @Result(name = "success", location = "/system/online/detail.jsp") })
    public String toOnlineUserDetailModal() {
        return SUCCESS;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "findUserOperateList")
    public void findUserOperateList() throws Exception {
        String userId = request.getParameter("userId");
        LsSystemUserLogBO systemUserLog = systemUserLogService.findOneByUserId(userId);
        if (NuctechUtil.isNotNull(systemUserLog)) {
            pageQuery = this.newPageQuery("t.operateTime DESC");
            Map<String, Object> map = pageQuery.getFilters();
            map.put("ip", systemUserLog.getIpAddress());
            map.put("logonTime", systemUserLog.getLogonTime());
            map.put("logUser", userId);
            JSONObject retJson = systemUserLogService.findUserOperateLogList(pageQuery, null, false);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print(retJson.toString());
            out.flush();
            out.close();
        }

    }

    public LsSystemUserLogBO getSystemUserLog() {
        return systemUserLog;
    }

    public void setSystemUserLog(LsSystemUserLogBO systemUserLog) {
        this.systemUserLog = systemUserLog;
    }

}
