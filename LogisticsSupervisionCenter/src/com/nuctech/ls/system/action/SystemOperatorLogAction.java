package com.nuctech.ls.system.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemUserLogBO;
import com.nuctech.ls.model.service.SystemOperateLogService;
import com.nuctech.ls.model.service.SystemUserLogService;

import net.sf.json.JSONObject;

/**
 * 
 * @author 徐楠
 * 
 *         描述：
 *         <p>
 *         系统操作日志信息
 *         </p>
 *         创建时间：2016年5月26日
 */
@Namespace("/operateLog")
public class SystemOperatorLogAction extends LSBaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = -7406768552424614197L;
    private Logger logger = Logger.getLogger(SystemOperatorLogAction.class);
    protected static final String DEFAULT_SORT_COLUMNS = "operateTime DESC";
    protected static final String DEFAULT_SORT_COLUMNS_ALIAS = "t.operateTime DESC";

    /**
     * 日志对象
     */
    private LsSystemUserLogBO userLog;
    @Resource
    private SystemUserLogService userLogService;
    @Resource
    private SystemOperateLogService logService;

    @Action(value = "toList", results = { @Result(name = "success", location = "/system/operateLog/list.jsp") })
    public String toList() {
        return SUCCESS;
    }

    /**
     * 列表查询
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Action(value = "list")
    public void list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS_ALIAS);

        String sortname = request.getParameter("sort");
        if ("userName".equals(sortname)) {
            pageQuery = this.newPageQuery("u.userName asc");
        }

        JSONObject retJson = logService.findLogList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    public LsSystemUserLogBO getUserLog() {
        return userLog;
    }

    public void setUserLog(LsSystemUserLogBO userLog) {
        this.userLog = userLog;
    }

}
