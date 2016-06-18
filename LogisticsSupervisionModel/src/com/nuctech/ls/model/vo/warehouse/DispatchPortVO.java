package com.nuctech.ls.model.vo.warehouse;

import java.io.Serializable;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>调度分析 口岸 VO</p>
 * 创建时间：2016年6月3日
 */
public class DispatchPortVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7487656973278736843L;

	//口岸ID
	private String portId;
	
	/**
	 * 口岸名称
	 */
	private String portName;
	
	/**
	 * 关锁平均值
	 */
	private Integer averageTrackDevice;
	
	/**
	 * 子锁平均值
	 */
	private Integer averageEseal;
	
	/**
	 * 传感器平均值
	 */
	private Integer averageSensor;
	

	public String getPortId() {
		return portId;
	}

	public void setPortId(String portId) {
		this.portId = portId;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public Integer getAverageTrackDevice() {
		return averageTrackDevice;
	}

	public void setAverageTrackDevice(Integer averageTrackDevice) {
		this.averageTrackDevice = averageTrackDevice;
	}

	public Integer getAverageEseal() {
		return averageEseal;
	}

	public void setAverageEseal(Integer averageEseal) {
		this.averageEseal = averageEseal;
	}

	public Integer getAverageSensor() {
		return averageSensor;
	}

	public void setAverageSensor(Integer averageSensor) {
		this.averageSensor = averageSensor;
	}
	
	
}
