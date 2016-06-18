var wsGpsUrl="ws://192.168.24.21:8080/LogisticsSupervisionServices/websocket/service";
var webSocket;
var routeAreaCol="";
var drawPolylineStyle = {
	"color" : "#ff0000",
	"weight" : 2,
	"opacity" : 0.36
}
var drawParams = {
	direct : "right_top",
	darwStyle : drawPolylineStyle
};
/**
 * 画图工具类
 * true ,表示是否显示画图工具
 * getDrawRouteArea 回调函数获取绘制图形坐标集合
 */
GisInitDrawingManagerAndDriving(drawParams, true,getDrawRouteArea);//初始化Google地图画图工具
function getDrawRouteArea(routeAreaCollection){
	routeAreaCol = routeAreaCollection;
}
$('#map_open').on('click', function () {
    $("#cont").toggle();
    var isHidden = $("#cont").is(":hidden");
    if(isHidden){
    	$(".gm-bundled-control .gmnoprint,.gm-china .gmnoprint:last-child").css({
    		"left": "0px"
    	});
    }else{
    	$(".gm-china .gmnoprint:last-child,.gm-china .gmnoprint:last-child").css({
    		"left": "270px"
    	});
    }
    /*if(isHidden){
    	$("#legend").css({
    		"left": "65px",
    	});
    }else{
    	$("#legend").css({
    		"left": "335px",
    	});
    }*/
});

$('#map_open1').on('click', function() {
	$("#cont").toggle();
	var isHidden = $("#cont").is(":hidden");
    if(isHidden){
    	$(".gm-bundled-control .gmnoprint,.gm-china .gmnoprint:last-child").css({
    		"left": "0px"
    	});
    }else{
    	$(".gm-china .gmnoprint:last-child,.gm-china .gmnoprint:last-child").css({
    		"left": "270px"
    	});
    }
	/*if(isHidden){
    	$("#legend").css({
    		"left": "65px",
    	});
    }else{
    	$("#legend").css({
    		"left": "335px",
    	});
    }*/
});
$('#raPointList').on('click', function() {
	menuType = "9";
	tripStatus = "0";
	clearAllOverlays(false);
	findAllMonitorTrip();
	findAllVehicleStatus();
	mapInitPort();
	$("#header_title").html($.i18n.prop("link.system.vehicleList"));
	$(".app-right-top").removeClass("hidden");
	$(".search_box").removeClass("hidden");
	drawManagerControlVisible(false);
	$("#planRouteAreaList").addClass("hidden");
	$("#panelList").removeClass("hidden");
	$("#patrolList").addClass("hidden");
	$("#addDelUpdate").addClass("hidden");
	$("#classify").removeClass("hidden");
	$("#addRapoint").addClass("hidden");
	$("#bottomPanel").addClass("hidden");
});
$('#routeAreaList').on('click', function() {
	menuType = "0";
	$("#header_title").html($.i18n.prop("link.system.routeList"));
	findAllRouteAreaList();
	$(".app-right-top").addClass("hidden");
	$(".search_box").addClass("hidden");
	drawManagerControlVisible(true);
	clearAllOverlays(true);
	mapInitPort();
	$("#planRouteAreaList").removeClass("hidden");
	$("#panelList").addClass("hidden");
	$("#patrolList").addClass("hidden");
	$("#classify").addClass("hidden");
	$("#addDelUpdate").removeClass("hidden");
	$("#addRapoint").addClass("hidden");
	$("#bottomPanel").addClass("hidden");
});
$('#siteList').on('click', function() {
	menuType = "1";
	$("#header_title").html($.i18n.prop("link.system.siteList"));
	findAllRouteAreaList();
	$(".app-right-top").addClass("hidden");
	$(".search_box").addClass("hidden");
	drawManagerControlVisible(true);
	clearAllOverlays(true);
	mapInitPort();
	$("#planRouteAreaList").removeClass("hidden");
	$("#panelList").addClass("hidden");
	$("#patrolList").addClass("hidden");
	$("#classify").addClass("hidden");
	$("#addDelUpdate").removeClass("hidden");
	$("#addRapoint").addClass("hidden");
	$("#bottomPanel").addClass("hidden");
});
$('#areaList').on('click', function() {
	menuType = "2";
	$("#header_title").html($.i18n.prop("link.system.areaList"));
	findAllRouteAreaList();
	$(".app-right-top").addClass("hidden");
	$(".search_box").addClass("hidden");
	drawManagerControlVisible(true);
	clearAllOverlays(true);
	mapInitPort();
	$("#planRouteAreaList").removeClass("hidden");
	$("#panelList").addClass("hidden");
	$("#patrolList").addClass("hidden");
	$("#classify").addClass("hidden");
	$("#addDelUpdate").removeClass("hidden");
	$("#addRapoint").addClass("hidden");
	$("#bottomPanel").addClass("hidden");
});
$('#vehicleHisList').on('click', function() {
	menuType = "8";
	tripStatus = "1";
	findAllMonitorTrip();
	$("#header_title").html($.i18n.prop("link.system.historyList"));
	$(".app-right-top").removeClass("hidden");
	$(".search_box").removeClass("hidden");
	drawManagerControlVisible(false);
	clearAllOverlays(false);
	mapInitPort();
	$("#planRouteAreaList").addClass("hidden");
	$("#panelList").removeClass("hidden");
	$("#patrolList").addClass("hidden");
	$("#addDelUpdate").addClass("hidden");
	$("#classify").removeClass("hidden");
	$("#addRapoint").addClass("hidden");
	$("#bottomPanel").removeClass("hidden");
});
$('#patrolShowList').on('click', function() {
	menuType = "7";
	clearAllOverlays(true);
	findAllPartrols();
	$("#header_title").html($.i18n.prop("link.system.patrolList"));
	$(".app-right-top").removeClass("hidden");
	$(".search_box").removeClass("hidden");
	drawManagerControlVisible(false);
	mapInitPort();
	$("#planRouteAreaList").addClass("hidden");
	$("#panelList").addClass("hidden");
	$("#patrolList").removeClass("hidden");
	$("#addDelUpdate").addClass("hidden");
	$("#addDelUpdate").removeClass("hidden");
	$("#addRapoint").addClass("hidden");
	$("#bottomPanel").addClass("hidden");
});

$('#addroutearea').on('click', function() {
	$("#planRouteAreaList").addClass("hidden");
	$("#panelList").addClass("hidden");
	$("#addRapoint").removeClass("hidden");
	$("#addRouteAreaBtn").removeClass("hidden");
	$("#editRouteAreaBtn").addClass("hidden");
	clearAllOverlays(true);
	mapInitPort();
	$("#routeAreaId").val("");
	$("#routeAreaName").val("");
	$("#routeAreaType").val("");
	$("#belongToPort").val("");
	$("#createUser").val("");
	$("#updateUser").val("");
	$("#routeAreaStatus").val("");
	$("#routeAreaPtCol").val("");
	$("#routeAreaBuffer").val("");
	$("#routeCost").val("");
	//清除选中值
	$('input[name="routeAreaIds"]:checked').each(function(){
		$(this).attr("checked",false);
	});
	
	if(menuType=="2"){
		$("#routeAreaTypeDiv").removeClass("hidden");
	}else{
		$("#routeAreaTypeDiv").addClass("hidden");
	}
});

$('#editRouteArea').on('click', function() {
	//获取checkbox的值，如果为0，则需要选中|如果>1，则需要重新选择|如果==1，则获取选中值并加载
	//获取选中的值
	var checkValues = [];
	$('input[name="routeAreaIds"]:checked').each(function(){
		checkValues.push($(this).val());
	});
	if(checkValues.length==1){
		$("#routeAreaId").val(checkValues[0]);
		$("#planRouteAreaList").addClass("hidden");
		$("#panelList").addClass("hidden");
		$("#addRapoint").removeClass("hidden");
		$("#addRouteAreaBtn").addClass("hidden");
		$("#editRouteAreaBtn").removeClass("hidden");
		
		GisClearOverlays(overlaysArray);
		$.ajax({
	        type: "POST",
	        url: getRootPath() + "monitorroutearea/editRouteArea.action?ids="+checkValues,
	        cache : false,
			async : false,
	        error : function(e, message, response) {
				console.log("Status: " + e.status + " message: " + message);
			},
	        success: function(obj){
	            if(obj.success){
	            	var lsMonitorRouteAreaBO = obj.lsMonitorRouteAreaBO;
	            	$("#routeAreaName").val(lsMonitorRouteAreaBO.routeAreaName);
	            	//$("#routeAreaType").val(lsMonitorRouteAreaBO.routeAreaType);
	            	$("#routeAreaType option[value='"+lsMonitorRouteAreaBO.routeAreaType+"']").attr("selected","selected");
	            	
	            	$("#belongToPort").val(lsMonitorRouteAreaBO.belongToPort);
	            	$("#createUser").val(lsMonitorRouteAreaBO.createUser);
	            	$("#updateUser").val(lsMonitorRouteAreaBO.updateUser);
	            	//$("#routeAreaStatus").val(lsMonitorRouteAreaBO.routeAreaStatus);
	            	$("#routeAreaStatus option[value='"+lsMonitorRouteAreaBO.routeAreaStatus+"']").attr("selected","selected");
	            	
	            	$("#routeAreaBuffer").val(lsMonitorRouteAreaBO.routeAreaBuffer);
	            	$("#routeCost").val(lsMonitorRouteAreaBO.routeCost);
	            	
	            	var points = obj.lsMonitorRaPointBOs;
	            	$("#routeAreaPtCol").val("");
	            	var pointArray = [];
	            	if (points.length > 0) {
		            	var jsonPoint = "";
	    				for (var i = 0; i < points.length; i++) {
	    					pointArray.push({
	    						lat : points[i].latitude,
	    						lng : points[i].longitude
	    					});
	    					var str = "{\"lat\":\""+points[i].latitude+"\",\"lng\":\""+points[i].longitude+"\"},";
							jsonPoint+=str;
	    				}
	    				jsonPoint = jsonPoint.substring(0,jsonPoint.length-1)
						jsonPoint = "["+jsonPoint+"]";
						$("#routeAreaPtCol").val(jsonPoint);
	    				if ("0" == lsMonitorRouteAreaBO.routeAreaType) {
	    					var polyline = GisShowRoadLineInMap(pointArray, drawPolylineStyle);
	    					overlaysArray.push(polyline);
	    				} else {
	    					var polygon;
	    					if ("1" == lsMonitorRouteAreaBO.routeAreaType) {
	    						polygon = GisShowPolygonInMap(pointArray, true, polygonStyle);
	    					} else {
	    						polygon = GisShowPolygonInMap(pointArray, true, polygonStyleDanger);
	    					}
	    					overlaysArray.push(polygon);
	    				}

	    			}
	             }else{
	            	 bootbox.alert($.i18n.prop("map.routeArea.edit.getInfoFailure"));
	             }
	        }
			
	     });
		
		//清除选中值
		$('input[name="routeAreaIds"]:checked').each(function(){
			$(this).attr("checked",false);
		});	
		
		if(menuType=="2"){
			$("#routeAreaTypeDiv").removeClass("hidden");
		}else{
			$("#routeAreaTypeDiv").addClass("hidden");
		}
		
	}else{
		bootbox.alert($.i18n.prop("map.routeArea.edit.oneChoice"));
	}
});

$('#deleteRouteArea').on('click', function() {
	//获取选中的值
	var checkValues = [];
	$('input[name="routeAreaIds"]:checked').each(function(){
		checkValues.push($(this).val());
	});
	if(checkValues.length==0){
		bootbox.alert($.i18n.prop("map.routeArea.edit.noChoice"));
	}else{
		bootbox.confirm($.i18n.prop("map.routeArea.delete.confirm"),function(result){
			if(result){
				$.ajax({
			        type: "POST",
			        url: getRootPath() + "monitorroutearea/delRouteArea.action?ids="+checkValues,
			        cache : false,
					async : false,
			        error : function(e, message, response) {
						console.log("Status: " + e.status + " message: " + message);
					},
			        success: function(obj){
			            if(obj.success){
			            	GisClearOverlays(overlaysArray);
			            	bootbox.success($.i18n.prop("map.routeArea.delete.success"));
			                findAllRouteAreaList();//更新列表
			             }else{
			            	 bootbox.alert($.i18n.prop("map.routeArea.delete.failure"));
			             }
			        }
			     });
			}
		});
	}
	//刷新列表
});


/**
 * html5 websocket获取推送报警信息
 */
function connectWebSocketInfo(){
	if(!webSocket){
		webSocket =  new ReconnectingWebSocket(wsGpsUrl);  
	}
    webSocket.onopen = function(event) {
    };   
    webSocket.onmessage = function(event) {   
        //var alarm =  event.data;//获取的是对象
        var gpsdata =  strToJson(event.data);//将获取的json对象转为可用的对象
        var messageContent = gpsdata.messageContent;
        if(gpsdata.messageType=="VEHICLE_ALARM"){
        	if(typeof(trackingTripId) == "undefined"){
        		var alarmMarker = GisCreateMarker({
					lat : messageContent.alarmLatitude,
					lng : messageContent.alarmLongitude
				}, "static/images/1_03.png", ""+messageContent.alarmId);
				
				var alarmContent = messageContent.alarmStatus;
 				GisShowInfoWindow(alarmMarker,alarmContent);
				
				pointMarkers.push(alarmMarker);
        	}
        }else if(gpsdata.messageType=="VEHICLE_GPS"){
        	if(typeof(trackingTripId) == "undefined"){
        		tripBufferPoints.push({
        			lat : messageContent.latitude,
					lng : messageContent.longitude
        		});
        		if(typeof(trackingLine) == "undefined" ){
            		var drawPolylineStyle = {
        				"color" : "#ffff00",
        				"weight" : 2,
        				"opacity" : 1
        			}
            		trackingLine = GisShowPolyLineInMap(tripBufferPoints, true, drawPolylineStyle);
            		overlaysArray.push(trackingLine);
            		/*var localPoint = tripBufferPoints[tripBufferPoints.length-1];
            		var pointMarker = GisCreateMarker(localPoint,"static/images/pointIcon.png",""+messageContent.gpsId);
					GisShowInfoWindow(pointMarker,""+messageContent.gpsId);
					pointMarkers.push(pointMarker);*/
            	}else{
            		trackingLine = GisUpdatePolyLine(trackingLine, tripBufferPoints);
            	}
        		if(typeof(trackingMaker) == "undefined" ){
        			trackingMaker =  GisCreateMarker({
            			lat : messageContent.latitude,
    					lng : messageContent.longitude
            		}, "static/images/alarmtruck.png", ""+messageContent.gpsId);
        			vehicleMarkers.push(trackingMaker);
        		}else{
            		GisSetMarkerPosition(trackingMaker,localPoint);
            	}
        	}
        	
        }else if(gpsdata.messageType=="PORTAL_GPS"){
        	/*if(indexFalse){
            	
            }else{
            	
            }*/
        }
    };   
    webSocket.onclose = function(event) { 
    	
    };
}

$(function() {
	$("#panelList").removeClass("hidden");//车辆列表
	$("#addRapoint").addClass("hidden");//添加车辆历史轨迹界面
	$("#planRouteAreaList").addClass("hidden");//路径规划列表
	$("#patrolList").addClass("hidden");//路径规划列表
	$("#addDelUpdate").addClass("hidden");//行程增删改
	$("#classify").removeClass("hidden");//车辆分级图标
	initMap();
	// mapInitDrawingManagerAndDriving();
	mapInitPort();
	tripStatus = "0";
	// mapDirectSearch();
	findAllMonitorTrip();
	findAllVehicleStatus();//查询所有车辆状态，显示在地图
	findAllPatrolStatus();//查询所有巡逻队状态，显示在地图
	//findAllRouteAreaList();
	$('#ceju').on('click', function() {
		mapMeasure();
	});
	initWebSocket();
	connectWebSocketInfo();
});
