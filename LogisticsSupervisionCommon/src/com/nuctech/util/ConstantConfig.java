package com.nuctech.util;

import java.io.InputStream;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class ConstantConfig {
	static Properties props = null;

	// 根据key读取value
	public static String readValue(String key) {
		try {
			if (props == null) {
				props = new Properties();
				InputStream in = ConstantConfig.class
						.getResourceAsStream("/ConstantConfig.properties");
				props.load(in);
			}
			String value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object parseObject(String value, Class parseClass,
			Object defauleValue) {
		if (value == null)
			return defauleValue;
		if (parseClass.equals(Integer.class)) {
			return Integer.parseInt(value);
		} else if (parseClass.equals(Long.class)) {
			return Long.parseLong(value);
		} else if (parseClass.equals(Double.class)) {
			return Double.parseDouble(value);
		} else if (parseClass.equals(Float.class)) {
			return Float.parseFloat(value);
		}
		return value;
	}

	public static String getEnumDisplayName(Enum enumObj) {
		if (enumObj == null)
			return "";
		String key = enumObj.getClass().getName() + "." + enumObj.name();
		return readValue(key);
	}

}
