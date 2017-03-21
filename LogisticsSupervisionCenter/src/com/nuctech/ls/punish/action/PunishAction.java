package com.nuctech.ls.punish.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.sla.LsSlaPunishBO;
import com.nuctech.ls.model.service.SlaPunishService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;

@Namespace("/punish")
public class PunishAction extends LSBaseAction {

    protected static final String DEFAULT_SORT_COLUMNS = "s.punishId ASC";
    private static final long serialVersionUID = -6817449229409795069L;

    @Resource
    private SlaPunishService slaPunishService;
    private LsSlaPunishBO lsSlaPunishBO;

    private String[] punishIds;

    /**
     * 进入初始页面
     */
    @Action(value = "toList", results = { @Result(name = "success", location = "/slaPunish/punish/slaPunishList.jsp") })
    public String toList() {

        return SUCCESS;
    }

    /**
     * 显示列表中的数据
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Action(value = "list", results = { @Result(name = "success", type = "json") })
    public void list() throws IOException {
        pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);

        JSONArray retJson = slaPunishService.fromObjectList(pageQuery, null, false);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();

    }

    // 添加Modal调用方法
    @Action(value = "addModal", results = { @Result(name = "success", location = "/slaPunish/punish/slaPunishAdd.jsp"),
            @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String addModal() {
        lsSlaPunishBO = new LsSlaPunishBO();
        return SUCCESS;
    }

    /**
     * 添加罚单成功
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "addPunish", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String addPunish() throws Exception {
        lsSlaPunishBO = new LsSlaPunishBO();
        try {
            if (lsSlaPunishBO != null) {
                SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
                lsSlaPunishBO.setPunishId(generatePrimaryKey());
                lsSlaPunishBO.setCreateTime(new Date());
                lsSlaPunishBO.setCreateUser(sessionUser.getUserId());
                lsSlaPunishBO.setPunishName((request.getParameter("punishName")));
                lsSlaPunishBO.setSlaType(request.getParameter("slaType"));
                lsSlaPunishBO.setSlaContent(request.getParameter("slaContent"));
                slaPunishService.add(lsSlaPunishBO);
                return SUCCESS;
            } else {
                return ERROR;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    /**
     * 删除罚单记录
     * 
     * @return
     */
    @Action(value = "delpunishById",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }) })
    public String delwarehouseById() {
        if (punishIds != null) {
            String s[] = punishIds[0].split(",");
            for (int i = 0; i < s.length; i++) {

                slaPunishService.deleteById(s[i]);
            }
        }
        return SUCCESS;
    }

    // 编辑modal
    @Action(value = "editModal",
            results = { @Result(name = "success", location = "/slaPunish/punish/slaPunishEdit.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editModal() {
        if (lsSlaPunishBO != null) {
            String punishId = lsSlaPunishBO.getPunishId();
            if (!NuctechUtil.isNull(punishId)) {
                lsSlaPunishBO = slaPunishService.find(punishId);
                if (lsSlaPunishBO.getPunishValue() == null) {
                    lsSlaPunishBO.setPunishValue("0");
                    slaPunishService.modify(lsSlaPunishBO);
                } else {
                    return SUCCESS;
                }
                return SUCCESS;
            } else {
                message = "Find Object Mis!";
                return ERROR;
            }
        } else {
            return ERROR;
        }
    }

    /**
     * 修改罚款记录
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "editPunish", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editPunish() throws Exception {
        if (lsSlaPunishBO != null) {
            lsSlaPunishBO.setSolveTime(new Date());// 设置解决时间
            lsSlaPunishBO.setSlaType(request.getParameter("lsSlaPunishBO.slaType"));
            /*
             * Date date = lsSlaPunishBO.getCreateTime();//获取事故的创建时间 Date date1
             * = lsSlaPunishBO.getSolveTime();
             *//**
               * 获得毫秒数
               */
            /*
             * long createTime = date.getTime(); long solveTime =
             * date1.getTime(); long takeTime =
             * solveTime-createTime;//takeTime为事故从登记到解决所花的时间
             *//**
               * 将毫秒数转为小时差
               */
            /*
             * int days = (int) (takeTime / (1000 * 60 * 60 * 24)); int hours =
             * (int) ((takeTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
             * int spendHours = days*24+hours;
             *//**
               * 解决时间若超过12小时就罚款1000，否则罚款100
               *//*
                 * if(spendHours>12){
                 * lsSlaPunishBO.setPunishValue(SLAType.SERIOUS_SLA.getPunish())
                 * ; }else{
                 * lsSlaPunishBO.setPunishValue(SLAType.NORMAL_SLA.getPunish());
                 * }
                 */

            this.slaPunishService.modify(lsSlaPunishBO);
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    public LsSlaPunishBO getLsSlaPunishBO() {
        return lsSlaPunishBO;
    }

    public void setLsSlaPunishBO(LsSlaPunishBO lsSlaPunishBO) {
        this.lsSlaPunishBO = lsSlaPunishBO;
    }

    public String[] getPunishIds() {
        return punishIds;
    }

    public void setPunishIds(String[] punishIds) {
        this.punishIds = punishIds;
    }
}