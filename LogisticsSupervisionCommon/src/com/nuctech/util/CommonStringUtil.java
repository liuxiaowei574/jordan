package com.nuctech.util;

public class CommonStringUtil {
	public static final String ifNull(String value, String defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static final Object ifNull(Object value, String defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
}
