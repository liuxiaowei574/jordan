package com.nuctech.ls.system.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemUserLogBO;
import com.nuctech.ls.model.service.SystemUserLogService;

import net.sf.json.JSONObject;

/**
 * 
 * @author liushaowei
 * 
 *         描述：
 *         <p>
 *         系统访问日志信息
 *         </p>
 *         创建时间：2016年5月26日
 */
@Namespace("/userLog")
public class SystemUserLogAction extends LSBaseAction {

    private static final long serialVersionUID = -7406768552424614197L;
    protected static final String DEFAULT_SORT_COLUMNS = "logonTime DESC";
    protected static final String DEFAULT_SORT_COLUMNS_ALIAS = "t.logonTime DESC";

    /**
     * 日志对象
     */
    private LsSystemUserLogBO userLog;
    @Resource
    private SystemUserLogService userLogService;

    @Action(value = "toList", results = { @Result(name = "success", location = "/system/userLog/list.jsp") })
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

        JSONObject retJson = userLogService.findUserLogList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

}
