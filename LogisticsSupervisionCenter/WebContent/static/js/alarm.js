/**
 * 根据报警类型和报警类型级别，获取对应图标
 * 
 * @param alarmTypeId
 * @param alarmLevelId
 * @returns
 */
function getAlarmIconByTypeAndLevel(alarmTypeId, alarmLevelId) {
	var alarmIcons = {
		// 长时间滞留报警
		"LONG_STAY" : {
			"0" : "changshijiantingliu0.png",
			"1" : "changshijiantingliu1.png"
		},
		// 路线偏移报警
		"ROUTE_DEVIATION" : {
			"0" : "luxianpianyi0.png",
			"1" : "luxianpianyi1.png"
		},
		// 卫星信号丢失报警
		"SATELLITE_LOSS" : {
			"0" : "weixingxinhaodiushi0.png",
			"1" : "weixingxinhaodiushi1.png"
		},
		// GSM信号丢失报警
		"GSM_LOSS" : {
			"0" : "xinhao0.png",
			"1" : "xinhao1.png"
		},
		// 开锁报警
		"OPEN_LOCK" : {
			"0" : "kaisuo0.png",
			"1" : "kaisuo1.png"
		},
		// 开锁又重新关闭报警
		"CLOSED_LOCK" : {
			"0" : "kaisuoguaisuo0.png",
			"1" : "kaisuoguaisuo1.png"
		},
		// 反向行驶报警
		"OPPOSITE_ROUTE" : {
			"0" : "nixing0.png",
			"1" : "nixing1.png"
		},
		// 强拆报警
		"DEVICE_BROKEN" : {
			"0" : "qiangchai0.png",
			"1" : "qiangchai1.png"
		},
		// 危险区域报警
		"ENTER_DANGEROUS_AREA" : {
			"0" : "weixianbaojing0.png",
			"1" : "weixianbaojing1.png"
		},
		// 误报警报警
		"FALSE_ALARM" : {
			"0" : "wubaojing0.png",
			"1" : "wubaojing1.png"
		},
		// 低电量报警
		"LOW_BATTERY" : {
			"0" : "dianliangdi0.png",
			"1" : "dianliangdi1.png"
		},
		// 行程超时报警
		"EXCEEDING_TRIP_ALLOWED_TIME" : {
			"0" : "xingcheng0.png",
			"1" : "xingcheng1.png"
		},
		// 子锁失联报警
		"ESEAL_LOSS" : {
			"0" : "zisuoshilian0.png",
			"1" : "zisuoshilian1.png"
		},
		// 子锁破坏报警
		"TAMPERING_WITH_ESEAL" : {
			"0" : "zisuopohuai0.png",
			"1" : "zisuopohuai1.png"
		},
		// 传感器失联报警
		"SENSOR_LOSS" : {
			"0" : "chuanganqishilian0.png",
			"1" : "chuanganqishilian1.png"
		},
		// 传感器破坏报警
		"TAMPERING_WITH_SENSOR" : {
			"0" : "chuanganqipohuai0.png",
			"1" : "chuanganqipohuai1.png"
		},
		// 车载台报警
		"TRACK_UNIT_ALARM" : {
			"0" : "chezaitaibaojing0.png",
			"1" : "chezaitaibaojing1.png"
		},
		// Target Zoon报警
		"TARGET_ZOON" : {
			"0" : "targetZoon0.png",
			"1" : "targetZoon1.png"
		}

	};
	var alarmIcon = alarmIcons[alarmTypeId][String(alarmLevelId)];
	if (alarmIcon) {
		return "static/images/" + alarmIcon;
	} else {
		return "";
	}
}
