package com.nuctech.ls.model.vo.report;

import java.io.Serializable;

/**
 * 
 * 作者： 徐楠
 *
 * 描述：<p>设备库存 VO对象</p>
 * 创建时间：2016年6月13日
 */
public class DeviceInventoryVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5335808907156799766L;
	
	/**
	 * 流入量
	 */
	private long trackDeviceFlowIn;
	
	/**
	 * 流出量
	 */
	private long trackDeviceFlowOut;
	
	/**
	 * 转入量
	 */
	private long trackDeviceTurnIn;
	
	/**
	 * 转出量
	 */
	private long trackDeviceTurnOut;

	/**
	 * 流入量
	 */
	private long esealFlowIn;
	
	/**
	 * 流出量
	 */
	private long esealFlowOut;
	
	/**
	 * 转入量
	 */
	private long esealTurnIn;
	
	/**
	 * 转出量
	 */
	private long esealTurnOut;
	
	/**
	 * 流入量
	 */
	private long sensorFlowIn;
	
	/**
	 * 流出量
	 */
	private long sensorFlowOut;
	
	/**
	 * 转入量
	 */
	private long sensorTurnIn;
	
	/**
	 * 转出量
	 */
	private long sensorTurnOut;

	public long getTrackDeviceFlowIn() {
		return trackDeviceFlowIn;
	}

	public void setTrackDeviceFlowIn(long trackDeviceFlowIn) {
		this.trackDeviceFlowIn = trackDeviceFlowIn;
	}

	public long getTrackDeviceFlowOut() {
		return trackDeviceFlowOut;
	}

	public void setTrackDeviceFlowOut(long trackDeviceFlowOut) {
		this.trackDeviceFlowOut = trackDeviceFlowOut;
	}

	public long getTrackDeviceTurnIn() {
		return trackDeviceTurnIn;
	}

	public void setTrackDeviceTurnIn(long trackDeviceTurnIn) {
		this.trackDeviceTurnIn = trackDeviceTurnIn;
	}

	public long getTrackDeviceTurnOut() {
		return trackDeviceTurnOut;
	}

	public void setTrackDeviceTurnOut(long trackDeviceTurnOut) {
		this.trackDeviceTurnOut = trackDeviceTurnOut;
	}

	public long getEsealFlowIn() {
		return esealFlowIn;
	}

	public void setEsealFlowIn(long esealFlowIn) {
		this.esealFlowIn = esealFlowIn;
	}

	public long getEsealFlowOut() {
		return esealFlowOut;
	}

	public void setEsealFlowOut(long esealFlowOut) {
		this.esealFlowOut = esealFlowOut;
	}

	public long getEsealTurnIn() {
		return esealTurnIn;
	}

	public void setEsealTurnIn(long esealTurnIn) {
		this.esealTurnIn = esealTurnIn;
	}

	public long getEsealTurnOut() {
		return esealTurnOut;
	}

	public void setEsealTurnOut(long esealTurnOut) {
		this.esealTurnOut = esealTurnOut;
	}

	public long getSensorFlowIn() {
		return sensorFlowIn;
	}

	public void setSensorFlowIn(long sensorFlowIn) {
		this.sensorFlowIn = sensorFlowIn;
	}

	public long getSensorFlowOut() {
		return sensorFlowOut;
	}

	public void setSensorFlowOut(long sensorFlowOut) {
		this.sensorFlowOut = sensorFlowOut;
	}

	public long getSensorTurnIn() {
		return sensorTurnIn;
	}

	public void setSensorTurnIn(long sensorTurnIn) {
		this.sensorTurnIn = sensorTurnIn;
	}

	public long getSensorTurnOut() {
		return sensorTurnOut;
	}

	public void setSensorTurnOut(long sensorTurnOut) {
		this.sensorTurnOut = sensorTurnOut;
	}
	
	
}
