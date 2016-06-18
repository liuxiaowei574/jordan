/**
 * gis业务操作与gis基本操作区分
 */

var portIcon = "images/gis/kouan.png";
var vehicleOnWayIcon = "static/images/ic_03.png";
var vehicleEndIcon = "static/images/ic_06.png";
var trackingDeviceNumberParam = undefined;//车牌号
var wsSocketUrl="ws://192.168.120.149:8080/LogisticsSupervisionServices/websocket/service";
var vehicleWebSocket;
var realVehicleUpdatespeed = 10000;//10秒钟更新一次数据
var vehicleRealPath= undefined;//车辆真实路线
var realVehicleDetailMarker= undefined;//车辆当前最新marker
/**
 * 报警车辆图标
 * @param direction
 */
function createAlarmIcon(direction){
	var alarmPath = "M 731.543 348.852 h -27.41 V 93.735 c 0 -5.04 -2.865 -9.64 -7.405 -11.847 c 0 0 -15.48 -17.325 -31.985 -25.78 c -24.055 -12.32 -70.95 -23.78 -72.965 -23.78 H 455.096 c -1.575 0 -32.057 0 -56.377 10.34 c -19.697 9.61 -28.215 12.87 -54.592 30.745 c -5.105 1.955 -8.507 6.837 -8.507 12.32 v 263.119 h -27.412 v 26.657 h 27.412 v 583.959 c 0 3.75 1.637 7.34 4.442 9.865 l 39.322 34.815 c 2.392 2.14 5.512 3.31 8.727 3.31 h 251.937 c 2.96 0 5.825 -1.01 8.16 -2.84 l 50.915 -40.2 c 3.185 -2.49 5.01 -6.3 5.01 -10.335 V 375.509 h 27.41 V 348.852 Z M 422.139 318.637 l 204.859 -0.63 l 44.775 40.077 l -47.705 47.01 H 418.704 l -48.017 -46.38 l 51.485 -40.045 L 422.139 318.637 Z M 361.959 593.858 h 41.022 l 0.095 142.065 l -41.117 39.765 v -181.8 V 593.858 Z M 402.889 426.424 l 0.092 141.095 h -40.99 V 386.914 L 402.889 426.424 Z M 421.476 754.768 l 201.082 3.4 l 42.88 38.47 l -75.46 46 h -136.9 l -81.605 -39.54 l 49.972 -48.33 H 421.476 Z M 640.868 739.198 v -145.34 h 36.955 v 178.49 l -36.925 -33.15 H 640.868 Z M 677.788 567.518 h -36.955 V 425.541 l 36.99 -36.422 v 178.43 L 677.788 567.518 Z M 677.788 101.96 v 226.159 l -33.87 -30.34 L 589.633 59.045 l 88.155 42.882 V 101.96 Z M 457.521 58.635 h 2.552 l -54.287 239.302 l -43.795 34.09 V 94.807 l 95.562 -36.172 H 457.521 Z";
	return GisCreateSvgIcon(alarmPath,0.05,direction,"red","pink");
}
/**
 * 巡逻队图标
 * @param direction
 */
function createPatrolIcon(direction){
	var patrolPath = "M 731.543 348.852 h -27.41 V 93.735 c 0 -5.04 -2.865 -9.64 -7.405 -11.847 c 0 0 -15.48 -17.325 -31.985 -25.78 c -24.055 -12.32 -70.95 -23.78 -72.965 -23.78 H 455.096 c -1.575 0 -32.057 0 -56.377 10.34 c -19.697 9.61 -28.215 12.87 -54.592 30.745 c -5.105 1.955 -8.507 6.837 -8.507 12.32 v 263.119 h -27.412 v 26.657 h 27.412 v 583.959 c 0 3.75 1.637 7.34 4.442 9.865 l 39.322 34.815 c 2.392 2.14 5.512 3.31 8.727 3.31 h 251.937 c 2.96 0 5.825 -1.01 8.16 -2.84 l 50.915 -40.2 c 3.185 -2.49 5.01 -6.3 5.01 -10.335 V 375.509 h 27.41 V 348.852 Z M 422.139 318.637 l 204.859 -0.63 l 44.775 40.077 l -47.705 47.01 H 418.704 l -48.017 -46.38 l 51.485 -40.045 L 422.139 318.637 Z M 361.959 593.858 h 41.022 l 0.095 142.065 l -41.117 39.765 v -181.8 V 593.858 Z M 402.889 426.424 l 0.092 141.095 h -40.99 V 386.914 L 402.889 426.424 Z M 421.476 754.768 l 201.082 3.4 l 42.88 38.47 l -75.46 46 h -136.9 l -81.605 -39.54 l 49.972 -48.33 H 421.476 Z M 640.868 739.198 v -145.34 h 36.955 v 178.49 l -36.925 -33.15 H 640.868 Z M 677.788 567.518 h -36.955 V 425.541 l 36.99 -36.422 v 178.43 L 677.788 567.518 Z M 677.788 101.96 v 226.159 l -33.87 -30.34 L 589.633 59.045 l 88.155 42.882 V 101.96 Z M 457.521 58.635 h 2.552 l -54.287 239.302 l -43.795 34.09 V 94.807 l 95.562 -36.172 H 457.521 Z";
	return GisCreateSvgIcon(patrolPath,0.05,direction,"black","black");
}

var indexFalse = false;
/**
 * 初始化地图车辆状态
 */
function findAllVehicleStatus() {
	var portUrl = getRootPath() + "vehiclestatus/findAllOnWayVehicleStatus.action?locationType=0";
	$.ajax({
		type : "POST",
		url : portUrl,
		dataType : "json",
		cache : false,
		async : false,
		error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
		success : function(jsonObj) {
			var vehicleData = jsonObj;
			var pointArray = [];
			if (null!=vehicleData&&vehicleData.rows.length > 0) {
				addVehicleToMap(vehicleData.rows);
			}
		}
	});
}

/**
 *添加车辆到地图
*/

 function addVehicleToMap(vehicleArr){
	 for ( var key in vehicleArr) {
		 var vehicle = vehicleArr[key];
		 if(vehicle.longitude!='0' && vehicle.latitude!=0 && vehicle.latitude!=null &&vehicle.longitude!=null){
			var localPoint = {lng:vehicle.longitude,lat:vehicle.latitude};
			var vehicleMarker = null;
			if("0"==vehicle.tripStatus){
				vehicleMarker =  GisCreateMarker(localPoint, createAlarmIcon(vehicle.direction),""+vehicle.vehicleStatusId);
			}else {
				vehicleMarker =  GisCreateMarker(localPoint, createAlarmIcon(vehicle.direction),""+vehicle.vehicleStatusId);
			}
			GisSetShowFront(vehicleMarker);//谷歌地图设置数字越大，越显示在最前
			vehicleMarkers.push(vehicleMarker);
			var html = '';
			html += '<div class="alert_box">';
			html += '	<div class="alert_box_content">';
			html += '		<div class="alert-title">';
			html += '		    <div class="pull-left">';
			html += '			    <div class="Vehicle_bubble"><img alt="" src="static/images/alarmtruck.png"/></div>';
			html += '				<b>'+vehicle.trackingDeviceNumber+'</b>';
			html += '           </div>';
			html += '			<div class="pull-right">checkinTime:'+vehicle.checkinTime+'</div>';
			html += '		</div>';
			html += '	    <div class="block-location">'
			html += '           <h4>Custom Metrics</h4>'
			html += '            <p><label><input type="checkbox">Apsum is simply dummy text of the printing</label></p>'
			html += '      </div>'
			html += '		<div class="alert_table">';
			html += '			<table class="table table-condensed table-striped table-hover">';
			html += '			     <tr><th>checkinTime:</th>';
			html += '			         <td>'+vehicle.checkinTime+'</td></tr>';
			html += '		    </table>';
			html += '	    </div>';
			html += '   </div>';
			html += '</div>';
			GisShowInfoWindow(vehicleMarker,html);  
			markerclusterer = GisMarkersClusterer(vehicleMarkers, 60, markerclusterer);
		}
	}
 }
 
 
/**
 * 初始化巡逻队车辆状态
 */
function findAllPatrolStatus() {
 	var portUrl = getRootPath() + "vehiclestatus/findAllOnWayVehicleStatus.action?locationType=1";
 	$.ajax({
 		type : "POST",
 		url : portUrl,
 		dataType : "json",
 		cache : false,
 		async : false,
 		error : function(e, message, response) {
 			console.log("Status: " + e.status + " message: " + message);
 		},
 		success : function(jsonObj) {
 			var patrolData = jsonObj;
 			var pointArray = [];
 			if (null!=patrolData&&patrolData.rows.length > 0) {
 				addPatrolToMap(patrolData.rows);
 			}
 		}
 	});
 }
 
/**
 *添加巡逻队车辆到地图
*/
 function addPatrolToMap(patrolData){
	 for ( var key in patrolData) {
		 var patrol = patrolData[key];
		 if(patrol.longitude!='0' && patrol.latitude!=0 && patrol.latitude!=null &&patrol.longitude!=null){
			var localPoint = {lng:patrol.longitude,lat:patrol.latitude};
			var patrolMarker =  GisCreateMarker(localPoint, createPatrolIcon(patrol.direction),""+patrol.vehicleStatusId);
			GisSetShowFront(patrolMarker);//谷歌地图设置数字越大，越显示在最前
			patrolMarkers.push(patrolMarker);
			var html = '';
			html += '<div class="alert_box">';
			html += '	<div class="alert_box_content">';
			html += '		<div class="alert-title">';
			html += '		    <div class="pull-left">';
			html += '			    <div class="Vehicle_bubble"><img alt="" src="static/images/alarmtruck.png"/></div>';
			html += '				<b>'+patrol.trackingDeviceNumber+'</b>';
			html += '           </div>';
			html += '			<div class="pull-right">checkinTime:'+patrol.checkinTime+'</div>';
			html += '		</div>';
			html += '	    <div class="block-location">'
			html += '           <h4>Custom Metrics</h4>'
			html += '            <p><label><input type="checkbox">Apsum is simply dummy text of the printing</label></p>'
			html += '      </div>'
			html += '		<div class="alert_table">';
			html += '			<table class="table table-condensed table-striped table-hover">';
			html += '			     <tr><th>checkinTime:</th>';
			html += '			         <td>'+patrol.checkinTime+'</td></tr>';
			html += '		    </table>';
			html += '	    </div>';
			html += '   </div>';
			html += '</div>';
			GisShowInfoWindow(patrolMarker,html,false);  
			partolclusterer = GisMarkersClusterer(patrolMarkers, 60, partolclusterer);
		}
	}
 }
 
 
 /**
  * 定时刷新车辆列表与地图车辆显示
  */
 function timeIntervalInit(){
		vehicelList("");
		function vehicelList(condition){
			findAllOnWayMonitorTrip(condition);
			allRealVehicleTimeoutValue = setTimeout(function(){
				vehicelList(condition);
			},realVehicleUpdatespeed);
		}
 }
 
 /**
  * html5 websocket获取推送报警信息
  */
 /*function connectWebSocketInfo(){
 	if(!vehicleWebSocket){
 	  vehicleWebSocket =  new WebSocket(wsSocketUrl);  
 		//webSocket =  new ReconnectingWebSocket();  
 	}
 	vehicleWebSocket.onopen = function(event) {
     };   
     vehicleWebSocket.onmessage = function(event) {   
         var jsonObj =  strToJson(event.data);//将获取的json对象转为可用的对象
         //报警
         if(jsonObj.status=="alarm"){
              
         }else{
       
         	if(indexFalse){
         		
         		if (typeof (textAreaLabels) != "undefined") {
         			GisClearOverlays(textAreaLabels);
         		}
             	queryAllRealVehicleAndShow(alarmValue,condition,portCodeCon,true); //根据条件将查询出的车辆展示在地图上
             }else{
             	if(jsonObj.trackingDeviceNumber!=null && jsonObj.trackingDeviceNumber!=""){
             		//alert(jsonObj.id+"----"+trackingDeviceNumber);
             		if(jsonObj.trackingDeviceNumber==trackingDeviceNumberParam){
                 		//realvehicleIdInDetailPage = alarm.vehicleId;//用于展示获取的id判断
                 		//isRefreshDetail = true;//用于判断是更新路线还是第一次生成路线.
             			updateRealVehiclePathInMap(jsonObj);//查询更新车辆详细信息
                 	}
             	}
             }
         }
     };   
     vehicleWebSocket.onclose = function(event) { 
     	
     };
 }*/

 /**
  *用于动态记录更新真实车辆
  */
 function updateRealVehiclePathInMap(vehicleData){
 	 if(vehicleData.longitude!=null && vehicleData.longitude!="" && vehicleData.latitude!=null && vehicleData.latitude!=""){
 		 vehicleRealPathPointsArr.push({lng:vehicleData.longitude,lat:vehicleData.latitude});
 				var pointMarker = GisCreateMarker({lat:vehicleData.latitude,lng:vehicleData.longitude},vehicleOnWayIcon);//更新上一次图标
 				var content = createCurrentContent(vehicleData);
 				GisShowInfoWindow(pointMarker,content);//用于构建弹出框信息，待补充
 				overlays.push(pointMarker);
 			
 		}	
 		if (vehicleRealPathPointsArr.length <= 0) {
 			return;
 		}
 		var increasePoint = null;//在轨迹上添加新的点
 		increasePoint = {lng:vehicleData.longitude,lat:vehicleData.latitude};
 		//if(typeof(trackingLine) == "undefined" )return;
 		if(typeof(trackingLine) == "undefined" ){
 			var lineStyle = {
 					color:"#ffff00", weight:4, opacity:1
 			};
 			trackingLine = GisShowPolyLineInMap(vehicleRealPathPointsArr, true , lineStyle);
 			overlays.push(trackingLine);
 		}else{
 			GisUpdatePolyLine(trackingLine, increasePoint);
 			overlays.push(trackingLine);
 		}
 		if(typeof(realVehicleDetailMarker) == "undefined" ){
 			//第一次添加车辆,显示初始状态
 			realVehicleDetailMarker = addRealVehicleDetailInMap(vehicleData, vehicleRealPathPointsArr[vehicleRealPathPointsArr.length-1],vehicleData.vvv);
 		}else{
 			//显示初始状态
 			GisSetMarkerPosition(realVehicleDetailMarker,vehicleRealPathPointsArr[vehicleRealPathPointsArr.length-1]);
 			//若报警，图标改为红色
 			//if("Y" == vehicleNew.alarmValue || "y" == vehicleNew.alarmValue){
 				GisSetMarkerIcon(realVehicleDetailMarker, vehicleEndIcon);
 			//}
 		}
 		//设置路线展示的最佳视野
 		GisSetViewPortByArray(vehicleRealPathPointsArr);
 		
 }



 /**
  * 在地图中添加车辆标注
  * @param vehicle
  */
 function addRealVehicleDetailInMap(vehicle, point, vvv){
	 var localPoint = {lng:vehicle.longitude,lat:vehicle.latitude};
	 	if(typeof(point)!= "undefined"){
	 		localPoint = point;
	 	}
	 	var vehicleIcon = vehicleEndIcon
	 	//若报警，字体为红色
	 	if("Y" == vehicle.alarmValue || "y" == vehicle.alarmValue){
	 		vehicleIcon = vehicleOnWayIcon;
	 	}
	 	
	 	var vehicleMarker =  GisCreateMarker(localPoint, vehicleIcon,""+ vehicle.vehicleStatusId);
	 	GisSetShowFront(vehicleMarker);//谷歌地图设置数字越大，越显示在最前
	 	
	 	GisAddEventForVehicle(vehicleMarker, "click", function() {
	 		/*showContainer(localPoint,vehicle.vehicleId);
	 		$("#vehicleIdCon").attr("value",vehicle.vehicleId);
	 		$("#alarmId").attr("value","");
	 		searchAlarmList();//查询报警
	 		*/		
	 		/*var obj = {data:vehicle};
	 		showDetail(obj);*/
	 	});
	 	 
	 	GisAddEventForVehicle(vehicleMarker, "mouseover", function() {
	 		//showVehicleInfo(vehicle.vehicleId);
	 		//showElockInfoById(vehicle.id);
	 	});
	 	
	 	overlays.push(vehicleMarker);
	 	return vehicleMarker;
 }
 
 /**
  * 构建车辆信息弹出框
  */
 function createCurrentContent(obj){
	 if(obj){
		 var locationTime = formatDateTime(new Date(obj.locationTime.time));
		    var html = '';
			html += '<div class="alert_box">';
			html += '	<div class="alert_box_content">';
			html += '		<div class="alert-title">';
			html += '		    <div class="pull-left">';
			html += '			    <div class="Vehicle_bubble alarm"></div>';
			html += '				<b>'+obj.trackingDeviceNumber+'</b>';
			html += '           </div>';
			html += '			<div class="pull-right">'+$.i18n.prop("eclock.label.locationTime")+':'+locationTime+'</div>';
			html += '		</div>';
			html += '		<div class="alert_table">';
			html += '			<table class="table table-condensed table-striped table-hover">';
			html += '			     <tr><th>'+$.i18n.prop("eclock.label.electricityValue")+'</th>';
			html += '			         <td>'+obj.electricityValue+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("eclock.label.altitude")+'</th>';
			html += '			         <td>'+obj.altitude+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("eclock.label.elockSpeed")+'</th>';
			html += '			         <td>'+obj.elockSpeed+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("eclock.label.latitude")+'</th>';
			html += '			         <td>'+obj.latitude+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("eclock.label.longitude")+'</th>';
			html += '			         <td>'+obj.longitude+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("eclock.label.direction")+'</th>';
			html += '			         <td>'+obj.direction+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("eclock.label.locationTime")+'</th>';
			html += '			         <td>'+locationTime+'</td></tr>';
			html += '		    </table>';
			html += '	    </div>';
			html += '   </div>';
			html += '</div>';
			return html;
	 }
 }
 var formatDateTime = function (date) {
	    var y = date.getFullYear();
	    var m = date.getMonth() + 1;
	    m = m < 10 ? ('0' + m) : m;
	    var d = date.getDate();
	    d = d < 10 ? ('0' + d) : d;
	    var h = date.getHours();
	    var minute = date.getMinutes();
	    minute = minute < 10 ? ('0' + minute) : minute;
	    return y + '-' + m + '-' + d+' '+h+':'+minute;
	};
 