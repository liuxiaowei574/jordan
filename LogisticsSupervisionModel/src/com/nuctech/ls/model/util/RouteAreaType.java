package com.nuctech.ls.model.util;

/**
 * 预定义区域类型：点、线、面（多边形）
 *  0-路线，1-安全区域，2-危险区域，3-监管区域，4-区域划分
 * @author duyange
 */
public enum RouteAreaType {
	POINT(""),
	LINE("0"),
	SAFE_AREA("1"),
	DANGEROUS_AREA("2"),
	MONITOR_AREA("3"),
	SEPARATE_AREA("4");
	
	private String text;
	
	private RouteAreaType(String text){
		this.text = text;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
}
