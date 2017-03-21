package com.nuctech.ls.model.bo;

/**
 * 数据验证结果
 * 
 */
public class ValidateResult {
	/** 是否保存 */
	private boolean saveFlag;
	/** 是否推送 */
	private boolean sendFlag;

	public ValidateResult() {
		this(false, false);
	}

	/**
	 * 数据验证结果
	 * 
	 * @param saveFlag
	 *            是否保存
	 * @param sendFlag
	 *            是否推送
	 */
	public ValidateResult(boolean saveFlag, boolean sendFlag) {
		super();
		this.saveFlag = saveFlag;
		this.sendFlag = sendFlag;
	}

	public boolean isSaveFlag() {
		return saveFlag;
	}

	public void setSaveFlag(boolean saveFlag) {
		this.saveFlag = saveFlag;
	}

	public boolean isSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(boolean sendFlag) {
		this.sendFlag = sendFlag;
	}

}
