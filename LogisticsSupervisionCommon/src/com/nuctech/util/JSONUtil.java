package com.nuctech.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONUtil {
	/**
	 * json字符串（简单类型对象）集合转为json数组，大数据量时性能不高
	 * 
	 * @param list
	 * @return
	 */
	public static JSONArray toJsonArray(List<String> list) {
		JSONArray array = new JSONArray();
		for (String str : list) {
			JSONObject obj = JSONObject.fromObject(str);
			array.add(obj);
		}
		return array;
	}

	/**
	 * 将json数组每个元素转为字符串，返回最终的集合，大数据量时性能不高
	 * 
	 * @param array
	 * @return
	 */
	public static List<String> toStringList(JSONArray array) {
		List<String> list = new ArrayList<>();
		for (int i = 0, len = array.size(); i < len; i++) {
			JSONObject object = array.getJSONObject(i);
			list.add(object.toString());
		}
		return list;
	}

	/**
	 * JsonArray转为String数组
	 * 
	 * @param jsonArray
	 * @return
	 */
	public static String[] toArray(JSONArray jsonArray) {
		return toArray(jsonArray, false);
	}

	/**
	 * JsonArray转为String数组，如果reversed为真，返回结果数组的逆序排列
	 * 
	 * @param jsonArray
	 * @param reversed
	 * @return
	 */
	public static String[] toArray(JSONArray jsonArray, boolean reversed) {
		int size = jsonArray.size();
		String[] array = new String[size];
		if (reversed) {
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				array[size - i - 1] = object.toString();
			}
		} else {
			for (int i = 0; i < size; i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				array[i] = object.toString();
			}
		}
		return array;
	}
}
