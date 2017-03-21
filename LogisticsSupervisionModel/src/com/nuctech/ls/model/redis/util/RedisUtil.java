package com.nuctech.ls.model.redis.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

/**
 * Redis工具类
 *
 */
public class RedisUtil {

	/**
	 * 将元素集合每个元素转为json字符串，并返回字符串集合
	 * 
	 * @param list
	 * @return
	 */
	public static <T> List<String> elements2Strs(List<T> list) {
		List<String> strs = new ArrayList<>(list.size());
		for (T t : list) {
			JSONObject json = JSONObject.fromObject(t);
			strs.add(json.toString());
		}
		return strs;
	}
}
