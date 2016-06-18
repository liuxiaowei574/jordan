package com.nuctech.ls.center.utils;

/**
 * 作者： 徐楠
 *
 * 描述：<p>系统登出类型</p>
 * 创建时间：2016年5月18日
 */
public enum LogoutType {

	NORMAL("正常登出", "0"), KICKOUT("踢出", "1");
	
	private String desc; //描述
	private String value; //值
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
	
	
}
