package com.nuctech.ls.punish.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.sla.LsVehiclePunishBo;
import com.nuctech.ls.model.service.VehiclePunishService;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;

@Namespace("/vehiclePunish")
public class VehiclePunishAction extends LSBaseAction {

    protected static final String DEFAULT_SORT_COLUMNS = "s.vpunishId ASC";
    private static final long serialVersionUID = 1L;
    @Resource
    private VehiclePunishService vehiclePunishService;
    private LsVehiclePunishBo lsVehiclePunishBo;
    private String[] vpunishIds;

    /**
     * 进入初始页面
     */
    @Action(value = "toList",
            results = { @Result(name = "success", location = "/slaPunish/vehiclePunish/vehiclePunishList.jsp") })
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

        JSONArray retJson = vehiclePunishService.fromObjectList(pageQuery, null, false);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print(retJson.toString());
        out.flush();
        out.close();
    }

    /**
     * 添加Modal调用方法
     * 
     * @return
     */
    @Action(value = "addModal",
            results = { @Result(name = "success", location = "/slaPunish/vehiclePunish/vehiclePunishAdd.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "errorMessage" }) })
    public String addModal() {

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
        try {
            if (lsVehiclePunishBo != null) {
                lsVehiclePunishBo.setVpunishId(generatePrimaryKey());// 设置关锁的主键，自动存储到数据库中；
                vehiclePunishService.add(lsVehiclePunishBo);
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
     * 编辑模态框
     * 
     * @return
     */
    @Action(value = "editModal",
            results = { @Result(name = "success", location = "/slaPunish/vehiclePunish/vehiclePunishEdit.jsp"),
                    @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editModal() {

        if (lsVehiclePunishBo != null) {
            String vpunishid = lsVehiclePunishBo.getVpunishId();
            if (!NuctechUtil.isNull(vpunishid)) {
                lsVehiclePunishBo = this.vehiclePunishService.findById(vpunishid);
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
     * 修改车辆罚款类型管理
     * 
     * @return
     * @throws Exception
     */
    @Action(value = "editPunish", results = { @Result(name = "success", type = "json", params = { "root", "true" }),
            @Result(name = "error", type = "json", params = { "root", "error" }) })
    public String editPunish() throws Exception {
        if (lsVehiclePunishBo != null) {
            this.vehiclePunishService.modify(lsVehiclePunishBo);
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
    @Action(value = "delpunishById",
            results = { @Result(name = "success", type = "json", params = { "root", "true" }) })
    public String delwarehouseById() {
        if (vpunishIds != null) {
            String s[] = vpunishIds[0].split(",");
            for (int i = 0; i < s.length; i++) {
                vehiclePunishService.deleteById(s[i]);
            }
        }
        return SUCCESS;
    }

    public LsVehiclePunishBo getLsVehiclePunishBo() {
        return lsVehiclePunishBo;
    }

    public void setLsVehiclePunishBo(LsVehiclePunishBo lsVehiclePunishBo) {
        this.lsVehiclePunishBo = lsVehiclePunishBo;
    }

    public String[] getVpunishIds() {
        return vpunishIds;
    }

    public void setVpunishIds(String[] vpunishIds) {
        this.vpunishIds = vpunishIds;
    }
}
