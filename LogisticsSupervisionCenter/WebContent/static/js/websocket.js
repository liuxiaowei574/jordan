//websocket
var wsVehicleAcceptUrl="ws://192.168.120.149:8080/LSCenter/websocket";
var webSocket=null;
function initWebSocket(){
	if(!webSocket){
		webSocket = new WebSocket(wsVehicleAcceptUrl);
	}
	webSocket.onopen = function(event){
		webSocket.send("Test!");
	};
	webSocket.onclose = function(event){
		alert("close");
	};
	webSocket.onmessage = function(event){
		var data=strToJson(event.data);
		if(data.msgType == '0') {
			getSocketCoods(data);
		} else if(data.msgType == '1') {
			loadWebSocketNoticeData(data);
		}
	};
	webSocket.onerror =doError;
}
function doError(event) {
	alert("error");

	}
function strToJson(json) {
	return eval("(" + json + ")");
}

/**
 * 加载WebSocket的通知数据
 * 
 * @param data
 */
function loadWebSocketNoticeData(data) {
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i,value){  
	  if(sessionUserId == value) {//接收人有自己弹出框
		  $("#log\\.noticeId").val(data.noticeId);
		  $("#log\\.receiveUser").val(value);
		  $("#msgTitle").val(data.title);
		  $("#msgContent").val(data.content);
		  $('#noticeMsgModal').modal({
	  			backdrop: 'static', 
	  			keyboard: false
	  		});
	  		$('#noticeMsgModal').on('loaded.bs.modal', function(e) {
	  			$('#noticeMsgModal').modal('show');
	  		});
	  }
	}); 
}
