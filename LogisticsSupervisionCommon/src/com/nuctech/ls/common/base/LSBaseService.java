/**
 * 
 */
package com.nuctech.ls.common.base;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import com.nuctech.ls.common.page.JsonDateValueProcessor;
import com.nuctech.ls.common.page.PageList;
import com.nuctech.util.Constant;

/**
 * 
 * Controller 基类
 * 
 * @author sunming
 *
 */
public class LSBaseService {
	
	
	/**
	 * 获取主键
	 * 
	 * @return
	 */
	public String generatePrimaryKey() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	/**
	 * @param pageList
	 * @param jsonConfig
	 * @param ignoreDefaultExcludes
	 * @return
	 */
	public JSONObject fromObjectList(PageList pageList,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();		
		JsonConfig config = new JsonConfig();
		if(jsonConfig!=null){
			config = jsonConfig;
		}
		config.setIgnoreDefaultExcludes(ignoreDefaultExcludes);     
		config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor(Constant.JordanTimeFormat));
		
		if(pageList!=null){
			for(Object obj : pageList){
				JSONObject jsonObj = JSONObject.fromObject(obj,config);
				jsonArray.add(jsonObj);
			}
			jsonObject.put("total", pageList.getTotalItems());
		}		
		jsonObject.put("rows", jsonArray);
		return jsonObject;
	}
	
	public JSONArray fromArrayList(List pageList,JsonConfig jsonConfig,boolean ignoreDefaultExcludes) {
		JSONArray jsonArray = new JSONArray();		
		JsonConfig config = new JsonConfig();
		if(jsonConfig!=null){
			config = jsonConfig;
		}
		config.setIgnoreDefaultExcludes(ignoreDefaultExcludes);     
		config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor(Constant.JordanTimeFormat));
		if(pageList!=null){
			for(Object obj : pageList){
				JSONObject jsonObj = JSONObject.fromObject(obj,config);
				jsonArray.add(jsonObj);
			}
		}		
		return jsonArray;
	}
}
