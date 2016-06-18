package com.nuctech.ls.center.utils;

import java.io.Serializable;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

public class BeansToJson <T, PK extends Serializable>{

	/**
	 * 对象转json
	 * 
	 * @param list
	 */
	public String beanToJson(List<T> list) {
		JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT); // 排除关联死循环
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));// 
        JSONArray lineitemArray = JSONArray.fromObject(list, jsonConfig);
        String result = JSONArray.fromObject(lineitemArray).toString();
        return result;
	}

}
