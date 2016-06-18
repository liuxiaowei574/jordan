package com.nuctech.ls.system.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.ls.model.bo.system.LsSystemDepartmentBO;
import com.nuctech.ls.model.service.SystemDepartmentService;
import com.nuctech.ls.model.vo.system.SessionUser;
import com.nuctech.util.Constant;

import net.sf.json.JSONObject;

/**
 * 组织结构管理Action
 * 
 * @author liushaowei
 *
 */
@Namespace("/deptMgmt")
public class SystemDepartmentAction extends LSBaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1389238299592275611L;

	protected static final String DEFAULT_SORT_COLUMNS = "organizationId ASC";

	private Logger logger = Logger.getLogger(UserAction.class);

	@Resource
	private SystemDepartmentService systemDepartmentService;

	/**
	 * 根据用户Id查询所属口岸
	 * 
	 * @return
	 */
	@Action(value = "findPortByUserId")
	public String findPortByUserId() {
		LsSystemDepartmentBO systemDepartmentBO = systemDepartmentService
				.findPortByUserId(((SessionUser) session.get(Constant.SESSION_USER)).getUserId());
		if (systemDepartmentBO != null) {
			JSONObject json = new JSONObject();
			json.put("portId", systemDepartmentBO.getOrganizationId());
			json.put("portName", systemDepartmentBO.getOrganizationName());
			response.setCharacterEncoding("utf-8");
			try {
				response.getWriter().println(json.toString());
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return null;
	}

	/**
	 * 根据用户Id查询所在国家所有口岸，用户所属口岸除外
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "findAllPortByUserId")
	public String findAllPortByUserId() throws Exception {
		String userId = ((SessionUser) session.get(Constant.SESSION_USER)).getUserId();
		List<LsSystemDepartmentBO> deptList = systemDepartmentService.findAllPortByUserId(userId);
		LsSystemDepartmentBO systemDepartmentBO = systemDepartmentService.findPortByUserId(userId);
		String departId = "";
		if(systemDepartmentBO == null) {
			logger.error("用户所属口岸为空！");
		}else{
			departId = systemDepartmentBO.getOrganizationId();
		}
		
		List<LsSystemDepartmentBO> list = new ArrayList<LsSystemDepartmentBO>();
		if(deptList != null && deptList.size() > 0) {
			for(LsSystemDepartmentBO department: deptList) {
				if(!department.getOrganizationId().equals(departId)){
					list.add(department);
				}
			}
		}
		pageQuery = this.newPageQuery(DEFAULT_SORT_COLUMNS);
		JSONObject retJson = systemDepartmentService.fromObjectList(list, new PageList<LsSystemDepartmentBO>(), pageQuery);

		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(retJson.toString());
		return null;
	}

}
