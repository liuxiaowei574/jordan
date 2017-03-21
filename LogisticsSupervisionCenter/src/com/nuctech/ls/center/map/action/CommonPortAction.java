package com.nuctech.ls.center.map.action;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.center.utils.DateJsonValueProcessor;
import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.service.CommonPortService;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.util.RoleType;
import com.nuctech.ls.model.util.RouteAreaType;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

@ParentPackage("json-default")
@Namespace("/port")
public class CommonPortAction extends LSBaseAction{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private String message;//记录运行信息
	private static final long serialVersionUID = 1L;
    private List<LsSystemDepartmentBO> lsCommonPortBOs;
    private List<LsSystemDepartmentBO> lsSystemDepartmentBOs;
	@Resource
	private CommonPortService commonPortService;
	@Resource
	private SystemDepartmentService systemDepartmentService;
	
	private LsSystemDepartmentBO lsSystemDepartmentBO;
	
    private String organizationId;
	
    private boolean success;//结果
	
	@Action(value = "findAllCommonPorts", results = {
    		@Result(name="success",type = "json")
    		})
    public void findAllCommonPorts() throws Exception {
        this.lsCommonPortBOs = this.commonPortService.findAllCommonPort();
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); // 排除关联死循环
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));// 格式化日期
        JSONArray lineitemArray = JSONArray.fromObject(this.lsCommonPortBOs, jsonConfig);
        String result = JSONArray.fromObject(lineitemArray).toString();
        try {
            this.response.getWriter().println(result);
        } catch (IOException e) {
        	message = e.getMessage();
        	logger.error(message);
        }
    }
	
	@Action(value = "findPortById", results = {
            @Result(name="success",type = "json")
            })
    public void findPortById() throws Exception {
	    this.lsSystemDepartmentBO = systemDepartmentService.findById(organizationId);
    }
	
	@Action(value = "findSelectData", results = {@Result(name="success",type = "json")})
    public String findSelectData() throws Exception {
		logger.info("查询所有的口岸、区域划分以及车载台~");
		try {
			SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(Constant.SESSION_USER);
			String userId = sessionUser.getUserId();
			this.lsSystemDepartmentBOs = this.systemDepartmentService.findAllPortByUserId(userId);
			this.success = true;
		} catch (Exception e) {
			message = e.getMessage();
        	logger.error(message);
        	this.success = false;
		}
		return SUCCESS;
    }

	public List<LsSystemDepartmentBO> getLsSystemDepartmentBOs() {
		return lsSystemDepartmentBOs;
	}

	public void setLsSystemDepartmentBOs(List<LsSystemDepartmentBO> lsSystemDepartmentBOs) {
		this.lsSystemDepartmentBOs = lsSystemDepartmentBOs;
	}

	public boolean isSuccess() {
		return success;
	}

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public LsSystemDepartmentBO getLsSystemDepartmentBO() {
        return lsSystemDepartmentBO;
    }

    public void setLsSystemDepartmentBO(LsSystemDepartmentBO lsSystemDepartmentBO) {
        this.lsSystemDepartmentBO = lsSystemDepartmentBO;
    }

    
    public String getOrganizationId() {
        return organizationId;
    }

    
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
	
}
