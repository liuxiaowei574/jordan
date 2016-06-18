package com.nuctech.util;

/**
 * Constant
 *
 */

public class Constant {

	/**
	 * 有效标记
	 */
	public static final String VALID_FLAG = "validFlag";
	
	/**
	 * 无效标记
	 */
	public static final String INVALID_FLAG = "invalidFlag";
	
	/**
	 * session 用户
	 */
	public static final String SESSION_USER = "sessionUser";
	
	/**
	 * 登录用户日志ID
	 */
	public static final String USER_LOG_ID = "userLogId";
	/**
	 * 约旦日期格式化字符串
	 */
	public static final String JordanTimeFormat = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 行程状态-进行中-0
	 */
	public static final String TRIP_STATUS_STARTED = "0";
	/**
	 * 行程状态-已结束-1
	 */
	public static final String TRIP_STATUS_FINISHED = "1";
	
	/**
	 * 通知状态 -起草 -0
	 */
	public static final String NOTICE_STATE_DRAFT = "0";
	/**
	 * 通知状态 -发布 -1
	 */
	public static final String NOTICE_STATE_PUBLISH = "1";
	/**
	 * 通知状态 -完成 -2
	 */
	public static final String NOTICE_STATE_FINISH = "2";
	
	/**
	 * 通知接收人 -未处理接收通知 -0
	 */
	public static final String NOTICE_UN_HANDLED = "0";
	
	/**
	 * 通知接收人 -已处理接收通知 -1
	 */
	public static final String NOTICE_HANDLED = "1";
	/**
	 * GPS推送数据
	 */
	public static final String WEBSOCKET_DATA_TYPE_GPS = "0";
	/**
	 * 通知推送数据
	 */
	public static final String WEBSOCKET_DATA_TYPE_NOTICE = "1";
	/**
	 * 组织机构类型-国家-1
	 */
	public static final String ORGANIZATION_TYPE_COUNTRY = "1";
	/**
	 * 组织机构类型-口岸-2
	 */
	public static final String ORGANIZATION_TYPE_PORT = "2";
	/**
	 * 组织机构类型-监管场所-3
	 */
	public static final String ORGANIZATION_TYPE_JGCS = "3";
	/**
	 * 组织机构类型-建管中心-4
	 */
	public static final String ORGANIZATION_TYPE_CENTER = "4";
	
	/**
	 * 设备申请状态-已申请-1
	 */
	public static final String DEVICE_ALREADY_APPLIED = "1";
	
	/**
	 * 设备申请状态-已处理-2
	 */
	public static final String DEVICE_ALREADY_DEAL_WITH = "2";
	
	/**
	 * 设备申请状态-已完成-3
	 */
	public static final String DEVICE_ALREADY_FINISH = "3";
	
	/**
	 * 设备调度状态-未调度-0
	 */
	public static final String DEVICE_UN_DISPATCH = "0";
	/**
	 * 设备调度状态-调度完成-1
	 */
	public static final String DEVICE_FINISH_DISPATCH = "1";
	
	/**
	 * 库存统计-流入
	 */
	public static final String FLOW_IN = "flowIn";
	
	/**
	 * 库存统计-流出
	 */
	public static final String FLOW_OUT = "flowOut";
	/**
	 * 删除标记-未删除-0
	 */
	public static final String MARK_UN_DELETED = "0";
	/**
	 * 删除标记-已删除-0
	 */
	public static final String MARK_DELETED = "1";
	/**
	 * jsp页面foot按钮点击类型划分：
	 * 路线规划0：路线；
	 * 场地管理1：监管区域；
	 * 区域管理2：安全区域、危险区域、区域划分；
	 * 其他：7 巡逻队列表；8 历史车辆列表； 9 车辆列表
	 */
	public static final String BUTTON_TYPE_LINE = "0";
	public static final String BUTTON_TYPE_CDGL = "1";
	public static final String BUTTON_TYPE_QYGL = "2";
	public static final String BUTTON_TYPE_XLD = "7";
	public static final String BUTTON_TYPE_LSCL = "8";
	public static final String BUTTON_TYPE_CLLB = "9";
	/**
	 * 路线规划：0-路线；场地管理1：3-监管区域；区域管理2：1-安全区域，2-危险区域，4-区域划分；其他
	 * 地图规划类型-路线
	 */
	public static final String ROUTEAREA_TYPE_LINE = "0";
	/**
	 * 地图规划类型-安全区域
	 */
	public static final String ROUTEAREA_TYPE_AQQY = "1";
	/**
	 * 地图规划类型-危险区域
	 */
	public static final String ROUTEAREA_TYPE_WXQY = "2";
	/**
	 * 地图规划类型-监管区域
	 */
	public static final String ROUTEAREA_TYPE_JGQY = "3";
	/**
	 * 地图规划类型-区域划分
	 */
	public static final String ROUTEAREA_TYPE_QYHF = "4";

}
