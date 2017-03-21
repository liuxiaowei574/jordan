package com.nuctech.ls.system.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemParamsBO;
import com.nuctech.ls.model.service.SystemParamsService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * TODO
 * </p>
 * 创建时间：2016年6月2日
 */
@Namespace("/paramsMgmt")
public class SystemParamsAction extends LSBaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 5442360399119339933L;
    private static Logger logger = Logger.getLogger(SystemParamsAction.class);

    /**
     * 列表排序字段
     */
    private static final String DEFAULT_SORT_COLUMNS = "paramId ASC";

    /**
     * 参数列表
     */
    private List<LsSystemParamsBO> paramsList;

    @Resource
    private SystemParamsService systemParamsService;
    private LsSystemParamsBO systemParams;

    @Action(value = "toList", results = { @Result(name = "success", location = "/system/params/list.jsp") })
    public String toList() {
        return SUCCESS;
    }

    @Action(value = "toParamEditModal", results = { @Result(name = "success", location = "/system/params/edit.jsp") })
    public String toParamEditModal() {
        if (systemParams != null) {
            String paramId = systemParams.getParamId();
            if (!NuctechUtil.isNull(paramId)) {
                systemParams = systemParamsService.findById(paramId);
                return SUCCESS;
            } else {
                return ERROR;
            }
        } else {
            return ERROR;
        }
    }

    @SuppressWarnings("unchecked")
    @Action(value = "list")
    public void list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = systemParamsService.findParamsList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    @Action(value = "editParams",
            results = { @Result(name = "success", type = "json"), @Result(name = "error", type = "json") })
    public String editParams() {
        if (systemParams != null) {
            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
            if (sessionUser == null) {
                logger.error("请登录系统");
                return ERROR;
            }
            // systemParams.set
            // notice.setPublisher(sessionUser.getUserId());
            systemParamsService.modifyParams(systemParams);
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    public List<LsSystemParamsBO> getParamsList() {
        return paramsList;
    }

    public void setParamsList(List<LsSystemParamsBO> paramsList) {
        this.paramsList = paramsList;
    }

    public LsSystemParamsBO getSystemParams() {
        return systemParams;
    }

    public void setSystemParams(LsSystemParamsBO systemParams) {
        this.systemParams = systemParams;
    }

}
