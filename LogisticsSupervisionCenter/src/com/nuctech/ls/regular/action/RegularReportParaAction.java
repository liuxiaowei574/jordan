package com.nuctech.ls.regular.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsRegularReportParaSetBO;
import com.nuctech.ls.model.service.RegularReportParaSetService;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONObject;

@Namespace("/regularReportParameter")
public class RegularReportParaAction extends LSBaseAction {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_SORT_COLUMNS = "customTime desc";
    @Resource
    private RegularReportParaSetService regularReportParaSetService;
    private LsRegularReportParaSetBO regularReportParaSetBO;
    private String ids[];

    /**
     * 定时报告参数设置
     * 
     * @return
     */
    @Action(value = "toList",
            results = { @Result(name = "success", location = "/system/regularreport/reportparameter.jsp") })
    public String toList() {
        return SUCCESS;
    }

    /**
     * 显示参数设置列表
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Action(value = "list")
    public void list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
        JSONObject retJson = regularReportParaSetService.findReportPasSetList(pageQuery, null, false);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /**
     * 报告参数设置参数添加模态框
     * 
     * @return
     */
    @Action(value = "addModal", results = { @Result(name = "success", location = "/system/regularreport/add.jsp") })
    public String addModal() {
        regularReportParaSetBO = new LsRegularReportParaSetBO();
        return SUCCESS;
    }

    @Action(value = "addParameterSet",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String addParameterSet() {
        if (regularReportParaSetBO != null) {
            regularReportParaSetBO.setReportId(generatePrimaryKey());
            regularReportParaSetService.add(regularReportParaSetBO);
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    /**
     * 修改模态框
     * 
     * @return
     */
    @Action(value = "editModal", results = { @Result(name = "success", location = "/system/regularreport/edit.jsp") })
    public String editModal() {
        if (regularReportParaSetBO != null) {
            String reportId = regularReportParaSetBO.getReportId();
            if (!NuctechUtil.isNull(reportId)) {
                regularReportParaSetBO = regularReportParaSetService.findById(reportId);
                return SUCCESS;
            } else {
                return ERROR;
            }
        } else {
            return ERROR;
        }
    }

    /**
     * 修改报告参数设置
     * 
     * @return
     */
    @Action(value = "editReportParameter",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }),
                    @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String editReportParameter() {
        if (regularReportParaSetBO != null) {
            regularReportParaSetService.modifyReportParas(regularReportParaSetBO);
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    /**
     * 删除记录
     * 
     * @return
     */
    @Action(value = "delete", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "false" }) })
    public String delete() {
        try {
            String[] idArr = ids[0].split(",");
            regularReportParaSetService.delete(idArr);
        } catch (Exception e) {
            message = e.getMessage();
            return ERROR;
        }
        return SUCCESS;
    }

    public LsRegularReportParaSetBO getRegularReportParaSetBO() {
        return regularReportParaSetBO;
    }

    public void setRegularReportParaSetBO(LsRegularReportParaSetBO regularReportParaSetBO) {
        this.regularReportParaSetBO = regularReportParaSetBO;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }
}
