//websocket
var wsVehicleAcceptUrl = wsNoticeUrl;
var webSocket = null;
function initWebSocket() {
	if (!webSocket) {
		webSocket = new ReconnectingWebSocket(wsVehicleAcceptUrl);
	}
	webSocket.onopen = function(event) {
		webSocket.send("Test!");
	};
	webSocket.onclose = function(event) {
		// alert("close");
	};
	webSocket.onmessage = function(event) {
		var data = strToJson(event.data);
		if (data.msgType == '0') {
			getSocketCoods(data);
		} else if (data.msgType == '1') {
			loadWebSocketNoticeData(data);
		} else if (data.msgType == '2') {
			loadTripRequest(data);
		} else if (data.msgType == '3') {
			loadTripResult(data); // 行程激活/结束页面
		} else if (data.msgType == '4') {
			loadWebSocketAlarmData(data);
		} else if (data.msgType == '5') {
			loadWebSocketDispatchData(data);
		} else if (data.msgType == '6') {
			loadWebSocketKickOfflineData(data);
		} else if (data.msgType == '7') {
			loadWebSocketDispatchDataToContorlRoom(data);
		} else if (data.msgType == '8') {
			loadWebsocketSendMissionFromContromRoom(data);
		} else if (data.msgType == '9') {
			loadWebsocketShiftTaskAudit(data)
		} else if (data.msgType == '10') {
			loadWebsocketThroghAudit(data)
		} else if (data.msgType == '11') {
			loadWebsocketTaskMsgToReceiver(data)
		} else if (data.msgType == '12') {
			loadWebsocketNotAllowTaskShift(data)
		}else if (data.msgType == '13') {
			loadWebsocketTransferDispatchTask(data)
		}else if (data.msgType == '14') {
			loadWebsocketTransferEscortTask(data)
		}else if (data.msgType == '15') {
			loadWebsocketDispatchToPort(data)
		}
	};
	webSocket.onerror = doError;
}
function doError(event) {
	// alert("error");
}
function strToJson(json) {
	return eval("(" + json + ")");
}

/**
 * 加载WebSocket的通知数据
 * 
 * @param data
 */
function loadWebSocketAlarmData(data) {
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i, value) {
		if (sessionUserId == value) {// 接收人有自己弹出框
			$("#log\\.noticeId").val(data.noticeId);
			bootbox.alert(data.content);
		}
	});
}
/**
 * 加载WebSocket的通知数据
 * 
 * @param data
 */
function loadWebSocketNoticeData(data) {
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i, value) {
		if (typeof sessionUserId != 'undefined' && sessionUserId == value) {// 接收人有自己弹出框
			var url = root	+ "/notice/sysMsgModal.action";
			$('#systemMsgModal').removeData('bs.modal');
			$('#systemMsgModal').modal({
				remote : url,
				show : false,
				backdrop : 'static',
				keyboard : false
			});

			$('#systemMsgModal').on('loaded.bs.modal', function(e) {
				$('#systemMsgModal').modal('show');
				$("#noticeId").val(data.noticeId);
				$("#sysmsgTitle").val(data.title);
				$("#sysmsgContent").val(data.content);
				$("#log\\.receiveUser").val(value);
			});
		}
	});
}
/**
 * 加载WebSocket的行程请求通知
 * 
 * @param data
 */
function loadTripRequest(data) {
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i, value) {
		if (typeof sessionUserId != 'undefined' && sessionUserId == value) {// 接收人有自己弹出框
			$("#log\\.noticeId").val(data.noticeId);
			$("#log\\.receiveUser").val(value);
			$("#msgTitle").val(data.title);
			
			var obj = parseTripRequest(data);
			$("#requestTripId").val(obj.tripId);
			$("#content").html(data.content);
			$("#msgInfo").html(obj.link);
			
			$('#tripMsgModal').removeData('bs.modal');
			$('#tripMsgModal').modal({
				backdrop : 'static',
				keyboard : false
			});
			$('#tripMsgModal').on('loaded.bs.modal', function(e) {
				$('#tripMsgModal').modal('show');
			});
			
			$("#msgInfo").on("click", "a", function() {
				var url = root + "/monitortripreport/toDetail.action?s_tripId=" + obj.tripId + "&msgType=modal";
				var a = dialog({
					id : 'tripInfoDialog',
					title : $.i18n.prop('trip.info.tripInfo'),
					url : url,
					width : '900px',
					height : '530px',
					left : '10%',
					fixed : 'false',
					zIndex: 1051,
					padding: 5
				});
				a.show();
			})
		}
	});
}

/**
 * 加载WebSocket的调度完成通知
 * 
 * @param data
 */
function loadWebSocketDispatchData(data) {
	var dispacthId = data.dispacthId;
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i, value) {
		if (typeof sessionUserId != 'undefined' && sessionUserId == value) {// 接收人有自己弹出框
			var url = root
					+ "/dispatchSendMsg/msgToPatrolModal.action?dispacthid="
					+ dispacthId;
			$('#dispatchMsgModal').removeData('bs.modal');
			$('#dispatchMsgModal').modal({
				remote : url,
				show : false,
				backdrop : 'static',
				keyboard : false
			});

			$('#dispatchMsgModal').on('loaded.bs.modal', function(e) {
				$('#dispatchMsgModal').modal('show');
				$("#noticeId").val(data.noticeId);
				$("#log\\.receiveUser").val(value);
				$("#dispatchMsgTitle").val(data.title);
				$("#dispatchMsgContent").val(data.content);
			});
		}
	});
}

/**
 * 控制中心接到调度车辆到达临界区域时，给另外的巡逻队发送准备交接的通知
 * @param data
 */
function loadWebsocketTransferDispatchTask(data){
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i, value) {
		if (typeof sessionUserId != 'undefined' && sessionUserId == value) {// 接收人有自己弹出框
			var url = root+ "/dispatchHandOver/handOverTask.action";
			$('#handOverTaskMsgModal').removeData('bs.modal');
			$('#handOverTaskMsgModal').modal({
				remote : url,
				show : false,
				backdrop : 'static',
				keyboard : false
			});

			$('#handOverTaskMsgModal').on('loaded.bs.modal', function(e) {
				$('#handOverTaskMsgModal').modal('show');
				$("#noticeId").val(data.noticeId);
				$("#log\\.receiveUser").val(value);
				$("#dispatchMsgTitle").val(data.title);
				$("#dispatchMsgContent").val(data.content);
			});
		}
	});
}

/**
 * 控制中心接到行程护送车辆到达临界区域时，给另外的巡逻队发送准备交接的通知
 * @param data
 */
function loadWebsocketTransferEscortTask(data){
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i, value) {
		if (typeof sessionUserId != 'undefined' && sessionUserId == value) {// 接收人有自己弹出框
			var url = root+ "/escortHandOver/handOverTask.action";
			$('#escorthandOverTaskMsgModal').removeData('bs.modal');
			$('#escorthandOverTaskMsgModal').modal({
				remote : url,
				show : false,
				backdrop : 'static',
				keyboard : false
			});

			$('#escorthandOverTaskMsgModal').on('loaded.bs.modal', function(e) {
				$('#escorthandOverTaskMsgModal').modal('show');
				$("#noticeId").val(data.noticeId);
				$("#log\\.receiveUser").val(value);
				$("#dispatchMsgTitle").val(data.title);
				$("#dispatchMsgContent").val(data.content);
			});
		}
	});
}
/**
 * 给调度口岸发出通知，执行调度
 * @param data
 */
function loadWebsocketDispatchToPort(data){
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i, value) {
		if (typeof sessionUserId != 'undefined' && sessionUserId == value) {// 接收人有自己弹出框
			var url = root+ "/warehouseDispatchAnalysis/excutedispatch.action";
			$('#dispatchNoticeToPort').removeData('bs.modal');
			$('#dispatchNoticeToPort').modal({
				remote : url,
				show : false,
				backdrop : 'static',
				keyboard : false
			});

			$('#dispatchNoticeToPort').on('loaded.bs.modal', function(e) {
				$('#dispatchNoticeToPort').modal('show');
				$("#noticeId").val(data.noticeId);
				$("#log\\.receiveUser").val(value);
				$("#dispatchMsgTitle").val(data.title);
				$("#dispatchMsgContent").val(data.content);
			});
		}
	});
}

/**
 * 报警中心向巡逻队推送任务的通知
 * 
 * @param data
 */
function loadWebsocketSendMissionFromContromRoom(data) {
	var taskIds = data.taskIds;
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i, value) {
		if (typeof sessionUserId != 'undefined' && sessionUserId == value) {// 接收人有自己弹出框
			var url = root
					+ "/dispatchSendMsg/msgToPatrolFromControlRoom.action";
			$('#missionFromcontrolRoom').removeData('bs.modal');
			$('#missionFromcontrolRoom').modal({
				remote : url,
				show : false,
				backdrop : 'static',
				keyboard : false
			});

			$('#missionFromcontrolRoom').on('loaded.bs.modal', function(e) {
				$('#missionFromcontrolRoom').modal('show');
				$("#taskId").val(taskIds);
				$("#log\\.receiveUser").val(value);
				$("#dispatchMsgTitle").val(data.title);
				$("#dispatchMsgContent").val(data.content);
			});
		}
	});
}

/**
 * 控制中心主管审批通过，给申请交班的工作人员发回审批通过的消息
 * 
 * @param data
 */
function loadWebsocketThroghAudit(data) {
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i, value) {
		if (typeof sessionUserId != 'undefined' && sessionUserId == value) {
			var url = root + "/dispatchSendMsg/thoughAudit.action";
			$('#allowShift').removeData('bs.modal');
			$('#allowShift').modal({
				remote : url,
				show : false,
				backdrop : 'static',
				keyboard : false
			});

			$('#allowShift').on('loaded.bs.modal', function(e) {
				$('#allowShift').modal('show');
				$("#approvalMsgTitle").val(data.title);
				$("#approvalMsgContent").val(data.content);
			});
		}
	});
}

/**
 * 控制中心主管审批通过，给交班任务接收人发送通知
 * 
 * @param data
 */
function loadWebsocketTaskMsgToReceiver(data) {
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i, value) {
		//控制通知发送的对象与receiver是一致的；(给用户id为receiveUsers的用户发送通知)
		if (typeof sessionUserId != 'undefined' && sessionUserId == value) {
			var url = root + "/dispatchSendMsg/taskToReceiver.action";
			$('#msgToTaskReceiver').removeData('bs.modal');
			$('#msgToTaskReceiver').modal({
				remote : url,
				show : false,
				backdrop : 'static',
				keyboard : false
			});

			$('#msgToTaskReceiver').on('loaded.bs.modal', function(e) {
				$('#msgToTaskReceiver').modal('show');
				$("#MsgFromManagerTitle").val(data.title);
				$("#MsgFromManagerContent").val(data.content);
			});
		}
	});
}

/**
 * 控制中心主管审核不通过，给交班申请人返回通知
 * 
 * @param data
 */
function loadWebsocketNotAllowTaskShift(data) {
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i, value) {
		if (typeof sessionUserId != 'undefined' && sessionUserId == value) {
			var url = root + "/dispatchSendMsg/notAllowShiftTask.action";
			$('#notAllowShift').removeData('bs.modal');
			$('#notAllowShift').modal({
				remote : url,
				show : false,
				backdrop : 'static',
				keyboard : false
			});

			$('#notAllowShift').on('loaded.bs.modal', function(e) {
				$('#notAllowShift').modal('show');
				$("#notapprovalMsgTitle").val(data.title);
				$("#notapprovalMsgContent").val(data.content);
			});
		}
	});
}

/**
 * 向控制中心主管发出交班审核请求
 * 
 * @param data
 */
function loadWebsocketShiftTaskAudit(data) {
	var taskIds = data.taskIds;
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i, value) {
		if (typeof sessionUserId != 'undefined' && sessionUserId == value) {
			var url = root + "/dispatchSendMsg/msgFromUserToManager.action";
			$('#shiftTaskAudit').removeData('bs.modal');
			$('#shiftTaskAudit').modal({
				remote : url,
				show : false,
				backdrop : 'static',
				keyboard : false
			});

			$('#shiftTaskAudit').on('loaded.bs.modal', function(e) {
				$('#shiftTaskAudit').modal('show');
				$("#taskId").val(taskIds);
				$("#receiverId").val(data.contromuserId);
				$("#launchuser").val(data.launchuser);
				$("#log\\.receiveUser").val(value);
				$("#auditMsgTitle").val(data.title);
				$("#auditMsgContent").val(data.content);
			});
		}
	});
}

/**
 * 加载WebSocket的调度完成通知(推送给控制中心用户)
 * 
 * @param data
 */
function loadWebSocketDispatchDataToContorlRoom(data) {
	var dispacthId = data.dispacthId;
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(	receiveUserArray,function(i, value) {
						if (typeof sessionUserId != 'undefined'
								&& sessionUserId == value) {// 接收人有自己弹出框
							var url = root
									+ "/dispatchSendMsg/msgToControlRoomModal.action?dispacthid="
									+ dispacthId+"&receiveUserArray="+receiveUserArray;
							$('#controlRoomDispatchMsgModal').removeData(
									'bs.modal');
							$('#controlRoomDispatchMsgModal').modal({
								remote : url,
								show : false,
								backdrop : 'static',
								keyboard : false
							});
							$('#controlRoomDispatchMsgModal').on(
									'loaded.bs.modal',
									function(e) {
										$('#controlRoomDispatchMsgModal')
												.modal('show');
										$("#noticeId").val(data.noticeId);
										$("#log\\.receiveUser").val(value);
										$("#dispatchMsgTitle").val(data.title);
										$("#dispatchMsgContent").val(data.content);
									});
						}
					});
}

/**
 * 加载WebSocket的下线通知数据
 * 
 * @param data
 */
function loadWebSocketKickOfflineData(data) {
	var receiveUsers = data.receiveUser;// sessionIds
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i, value) {
		if (sessionId == value) {// 接收人有自己弹出框
			bootbox.alert($.i18n.prop(data.content), function() {
				window.top.location.href = _getRootPath() + '/login.jsp';
			});
		}
	});
}
