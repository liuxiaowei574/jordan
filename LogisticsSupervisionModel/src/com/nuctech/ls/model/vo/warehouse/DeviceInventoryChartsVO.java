package com.nuctech.ls.model.vo.warehouse;

import java.io.Serializable;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>设备库存情况 报表</p>
 * 创建时间：2016年6月6日
 */
public class DeviceInventoryChartsVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5025480440386056639L;

	private String portName; //口岸名称
	
	private Integer[] deviceArray; //设备数组

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public Integer[] getDeviceArray() {
		return deviceArray;
	}

	public void setDeviceArray(Integer[] deviceArray) {
		this.deviceArray = deviceArray;
	}

	
}
