/**
 * 
 */
package com.nuctech.ls.model.util;

import com.nuctech.util.MessageResourceUtil;

/**
 * @author sunming
 *
 */
public enum AlarmDealType {
	
	//未处理
	Undeal("0"),
	//处理中
	Dealing("1"),
	//已处理
	Dealt("2");
	
	private String text;
	
	private AlarmDealType(String text){
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	/**
     * 从资源文件读取国际化值
     * @return
     */
    public String getKey() {
        return MessageResourceUtil.getMessageInfo(this.getClass().getSimpleName()+"." +this.name());
    }

}
