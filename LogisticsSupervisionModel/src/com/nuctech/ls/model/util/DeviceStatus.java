package com.nuctech.ls.model.util;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>关锁状态 枚举类</p>
 * 创建时间：2016年6月3日
 */
public enum DeviceStatus {

	Scrap("0"), //报废
	Normal("1"), //正常
	Inway("2"), //在途
	Destory("3"), //损坏
	Maintain("4"); //维修

	private String text;
	
	private DeviceStatus(String text){
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
}
