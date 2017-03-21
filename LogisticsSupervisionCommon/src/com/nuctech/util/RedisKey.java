package com.nuctech.util;

/**
 * Redis Key
 * 
 * @author liushaowei
 *
 */
public class RedisKey {
	/**
	 * 存放车辆报警list的key。用于首页单个车辆报警列表数据。格式：alarmlist:vehicleId:xxxxx(vehicleId的值)
	 */
	public static final String ALARMLIST_VEHICLEID_PREFIX = "alarmlist:vehicleId:";

	/**
	 * 存放行程报警list的key。用于首页单个车辆报警点信息。格式：alarmlist:tripId:xxxxx(tripId的值)
	 */
	public static final String ALARMLIST_TRIPID_PREFIX = "alarmlist:tripId:";
}
