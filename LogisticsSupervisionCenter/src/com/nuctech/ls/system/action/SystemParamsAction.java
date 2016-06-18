package com.nuctech.ls.system.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.nuctech.ls.common.base.LSBaseAction;
import com.nuctech.ls.model.bo.system.LsSystemParamsBO;
import com.nuctech.ls.model.service.SystemParamsService;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>TODO</p>
 * 创建时间：2016年6月2日
 */
@Namespace("/paramsMgmt")
public class SystemParamsAction extends LSBaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5442360399119339933L;
	
	/**
	 * 参数列表
	 */
	private List<LsSystemParamsBO> paramsList;
	
	@Resource
	private SystemParamsService systemParamsService;
	
	@Action(value="findSystemParamsList", results = {@Result(name = "success", location = "/system/params/index.jsp")})
	public String findSystemParamsList() {
		paramsList = systemParamsService.findAllList();
		return SUCCESS;
	}

	public List<LsSystemParamsBO> getParamsList() {
		return paramsList;
	}

	public void setParamsList(List<LsSystemParamsBO> paramsList) {
		this.paramsList = paramsList;
	}

}
