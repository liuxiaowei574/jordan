package com.nuctech.ls.center.utils;

import com.nuctech.util.MessageResourceUtil;

/**
 * 作者： 徐楠
 *
 * 描述：
 * <p>
 * 系统登出类型
 * </p>
 * 创建时间：2016年5月18日
 */
public enum LogoutType {

	/**
	 * 正常登出
	 */
	NORMAL("NORMAL", "0"),
	/**
	 * 踢出
	 */
	KICKOUT("KICKOUT", "1"),
	/**
	 * 会话超时
	 */
	SESSION_TIMEOUT("SESSION_TIMEOUT", "2");

	private String desc; // 描述-国际化key
	private String value; // 值

	private LogoutType(String desc, String value) {
		this.desc = desc;
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 从资源文件读取国际化值
	 * 
	 * @return
	 */
	public String getKey() {
		return MessageResourceUtil.getMessageInfo(this.getClass().getSimpleName() + "." + this.name());
	}
}
