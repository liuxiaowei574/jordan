var userCanDealAlarm = '0';
/**
 * 获取当前项目路径
 * 
 * @returns
 */
function _getRootPath() {
	// 获取当前网址，如： http://localhost:8088/test/test.jsp
	var curPath = window.document.location.href;
	// 获取主机地址之后的目录，如： test/test.jsp
	var pathName = window.document.location.pathname;
	var pos = curPath.indexOf(pathName);
	// 获取主机地址，如： http://localhost:8088
	var localhostPaht = curPath.substring(0, pos);
	// 获取带"/"的项目名，如：/test
	var projectName = pathName
			.substring(0, pathName.substr(1).indexOf('/') + 1);
	return (localhostPaht + projectName);
}
/**
 * 对ajax请求结果判断是否需要登录，如是，则跳转到登录页。bootstrap模态框需要将div.modal-content的内容传进来。可用在用户点击或表单提交请求结果中
 * 
 * @param data
 *            ajax请求响应结果，适用字符串或json对象
 * @returns {Boolean}
 */
function needLogin(data) {
	var forwardTo = getForwardTo(data);
	if (forwardTo) {
		window.top.location.href = _getRootPath() + '/' + forwardTo;
		return true;
	}
	return false;
}
function getForwardTo(data) {
	var forwardTo = data && data.forwardTo || '';
	if (!forwardTo) {
		try {
			data = JSON.parse(data);
			forwardTo = data.forwardTo;
		} catch (e) {
		}
	}
	return forwardTo;
}
/**
 * 初始化行程信息模态框事件
 */
function initTripInfoModal() {
	$("#msgInfo").on("click", "a", function() {
		var url = $(this).attr("href");
		$('#tripInfoModal').removeData('bs.modal');
		$('#tripInfoModal').modal({
			remote : url,
			show : false,
			backdrop : 'static',
			keyboard : false
		});
	});
	$('#tripInfoModal').on('loaded.bs.modal', function(e) {
		$(this).modal('show');
	});
	// 模态框登录判断
	$('#tripInfoModal').on('show.bs.modal', function(e) {
		var content = $(this).find(".modal-content").html();
		needLogin(content);
	});
}
/**
 * 解析行程请求通知
 */
function parseTripRequest(data) {
	var tripId = findUUID(data.content);
	var link = '<a href="javascript:void(0);" data-tripid="' + tripId + '" >'
			+ $.i18n.prop('trip.report.list.seeDetail') + '</a>';
	return {
		"tripId": tripId,
		"link": link
	};
}
/**
 * 从字符串查找uuid
 */
function findUUID(str) {
	var s = str.match(/\w{8}-(\w{4}-){3}\w{12}/);
	if (s) {
		return s[0];
	}
	return null;
}
/**
 * 获取用户信息
 */
function getUserInfo(userId, callback) {
	$.ajax({
		url : root + '/userMgmt/getUserById.action',
		type : "get",
		dataType : "json",
		data : {
			's_userId' : userId
		},
		success : function(data) {
			data.total && data.total > 0 && $.isFunction(callback) && callback(data.rows[0]);
		}
	});
}
/**
 * 显示行程详细信息
 */
function showTripDetail(a) {
	var tripId = $(a).data("tripid");
	getTripDetail({
		"s_tripId" : tripId
	}, showDetail);
}
/**
 * 获取行程详细信息
 */
function getTripDetail(params, callback) {
	$.get(root + "/monitortrip/findOneTripVehicleAlarm.action",
		params,
		function(data) {
			var needLoginFlag = false;
			if (typeof needLogin != 'undefined'
					&& $.isFunction(needLogin)) {
				needLoginFlag = needLogin(data);
			}
			if (!needLoginFlag) {
				callback(data);
				if (data.tripVehicleVO) {
					if ([ '2', '3' ]
							.indexOf(data.tripVehicleVO.tripStatus) > -1) {
						// $("#routeId").text($.i18n.prop('trip.label.see'));
						$("#timeCost").text(
								data.tripVehicleVO.timeCost);
					}
				}
			}
		}, 'json');
}
/**
 * 向行程各属性赋值
 */
function showDetail(data) {
	drillProps(data.tripVehicleVO, '', function(obj, objName) {
		if ([ 'checkinPicture', 'checkoutPicture' ].indexOf(objName) > -1) {
			showCheckinPicture(obj, objName);
			if (objName == 'checkinPicture') {
				showCheckinPicture(obj, 'checkinPicture1');
			}
		} else if (objName == 'goodsType') {
			if (!!obj && obj.length > 0) {
				var map = obj.split(/\s*,\s*/).map(function(v, i) {
					return $.i18n.prop('GoodsType.' + v) || '';
				});
				$('#_tripVehicleVO\\.goodsType').text(map.join()).val(
						map.join());
			}
		} else {
			$('#_tripVehicleVO\\.' + objName.replace(/\./g, '\\\.')).text(obj)
					.val(obj);
		}
	});
	if ([ '0', '1' ].indexOf(data.tripVehicleVO.tripStatus) > -1) {
		$("#tripDetail").show();
	} else if ([ '2', '3' ].indexOf(data.tripVehicleVO.tripStatus) > -1) {
		$("#tripDetailCheckOut").show();
	}
}
/**
 * 向对象属性赋值
 */
function drillProps(obj, objName, callback) {
	if (Object.prototype.toString.call(obj) === '[object String]') {
		callback(obj, objName);
	} else if (Object.prototype.toString.call(obj) === '[object Object]') {
		for ( var k in obj) {
			if (obj.hasOwnProperty(k)) {
				drillProps(obj[k], (objName && (objName + ".")) + k, callback);
			}
		}
	}
}
/**
 * 每隔一分钟查询一次用户是否有处理报警权限
 */
function intervalGetDealAlarm() {
	getUserInfo(sessionUserId, function(data) {
		//中心用户，需有处理报警权限才可处理报警
		if(systemModules.isAlarmPushOn) {
			if(['qualityCenterUser', 'followupUser', 'contromRoomUser', 'riskAnalysisUser', 'patrolManager', 'enforcementPatrol'].indexOf(data.roleName) > -1){
				if ([ '0', '1' ].indexOf(data.canDealAlarm) > -1) {
					userCanDealAlarm = data.canDealAlarm;
				}
			}
		} else {
			if(['contromRoomUser'].indexOf(data.roleName) > -1){
				if ([ '0', '1' ].indexOf(data.canDealAlarm) > -1) {
					userCanDealAlarm = data.canDealAlarm;
				}
			}
		}
		setTimeout(function() {
			intervalGetDealAlarm();
		}, 1000 * 60);
	});
}
/**
 * 生成UUID
 * @returns
 */
function uuid() {
	function S4() {
		return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
	}
	return (function() {
		return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
	})();
}


