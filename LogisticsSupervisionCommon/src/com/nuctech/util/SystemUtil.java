package com.nuctech.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * 应用常用方法
 * 
 * @author WangShuo
 * */
public class SystemUtil {
	/**
	 * A common method for all enums since they can't have another base class
	 * 
	 * @param <T>
	 *            Enum type
	 * @param c
	 *            enum type. All enums must be all caps.
	 * @param string
	 *            case insensitive
	 * @return corresponding enum, or null
	 */
	public static <T extends Enum<T>> T getEnumFromString(Class<T> c,
			String string) {
		if (c != null && string != null) {
			try {
				return Enum.valueOf(c, string.trim().toUpperCase());
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将originalObj中的属性值赋给destinationObj中的属性
	 * 
	 **/
	public static void cloneObject(Object originalObj, Object destinationObj) {
		BeanUtils.copyProperties(originalObj, destinationObj);
	}

	public static String checkInputString(String inputValue) {
		if (StringUtils.isEmpty(inputValue)) {
			return "";
		}
		return inputValue;
	}

	public static Date checkInputDate(Date date) {
		if (null == date) {
			return new Date();
		}
		return date;
	}

	public static Date checkInputDate(Long timeMills) {
		if (null == timeMills) {
			return new Date();
		}
		return new Date(timeMills);
	}

	/**
	 * 获得本机IP
	 * 
	 * */
	public static String getLocalIP() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取客户端ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
