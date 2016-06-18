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
import com.nuctech.ls.model.bo.common.LsCommonPortBO;
import com.nuctech.ls.model.service.CommonPortService;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

@ParentPackage("json-default")
@Namespace("/port")
public class CommonPortAction extends LSBaseAction{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private String message;//记录运行信息
	private static final long serialVersionUID = 1L;
    private List<LsCommonPortBO> lsCommonPortBOs;
	@Resource
	private CommonPortService commonPortService;
	
	@Action(value = "findAllCommonPorts", results = {
    		@Result(name="success",type = "json")
    		})
    public void findAllCommonPorts() throws Exception {
        this.lsCommonPortBOs = this.commonPortService.findAllCommonPort();
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); // 排除关联死循环
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));// 鏃堕棿瑙ｆ瀽
        JSONArray lineitemArray = JSONArray.fromObject(this.lsCommonPortBOs, jsonConfig);
        String result = JSONArray.fromObject(lineitemArray).toString();
        try {
            this.response.getWriter().println(result);
        } catch (IOException e) {
        	message = e.getMessage();
        	logger.error(message);
        }
    }
}
