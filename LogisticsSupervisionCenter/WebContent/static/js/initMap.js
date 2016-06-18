/**
 * 特别注意事项：overlays是从调用地图之后特有的数组参数，可以进行重写，但是针对同一个地图页面，只可以放同一种类型的覆盖物，多种类型会发生混乱
 * 为了避免混乱，尽量重新建立覆盖物数组参数，然后拿来使用，用完之后要记得销毁。
 */
var vehicleMarkers = []; //删除车辆覆盖物marker时用
var patrolMarkers = []; //删除巡逻队覆盖物marker时用
var pointMarkers = [];//删除点marker时用
var overlaysArray = [];//删除其他覆盖物时用

var trackingLine = undefined;//车辆的行走路线
var trackingTripId = undefined;//轨迹页面的行程Id
var trackingMaker = undefined;//轨迹车辆的Maker
var tripBufferPoints = new Array();;//存储缓冲车辆数据
var markerclusterer;// 车辆markers聚合对象
var partolclusterer;// 巡逻车markers聚合对象

//车辆svg图
var svgPath = "M 731.543 348.852 h -27.41 V 93.735 c 0 -5.04 -2.865 -9.64 -7.405 -11.847 c 0 0 -15.48 -17.325 -31.985 -25.78 c -24.055 -12.32 -70.95 -23.78 -72.965 -23.78 H 455.096 c -1.575 0 -32.057 0 -56.377 10.34 c -19.697 9.61 -28.215 12.87 -54.592 30.745 c -5.105 1.955 -8.507 6.837 -8.507 12.32 v 263.119 h -27.412 v 26.657 h 27.412 v 583.959 c 0 3.75 1.637 7.34 4.442 9.865 l 39.322 34.815 c 2.392 2.14 5.512 3.31 8.727 3.31 h 251.937 c 2.96 0 5.825 -1.01 8.16 -2.84 l 50.915 -40.2 c 3.185 -2.49 5.01 -6.3 5.01 -10.335 V 375.509 h 27.41 V 348.852 Z M 422.139 318.637 l 204.859 -0.63 l 44.775 40.077 l -47.705 47.01 H 418.704 l -48.017 -46.38 l 51.485 -40.045 L 422.139 318.637 Z M 361.959 593.858 h 41.022 l 0.095 142.065 l -41.117 39.765 v -181.8 V 593.858 Z M 402.889 426.424 l 0.092 141.095 h -40.99 V 386.914 L 402.889 426.424 Z M 421.476 754.768 l 201.082 3.4 l 42.88 38.47 l -75.46 46 h -136.9 l -81.605 -39.54 l 49.972 -48.33 H 421.476 Z M 640.868 739.198 v -145.34 h 36.955 v 178.49 l -36.925 -33.15 H 640.868 Z M 677.788 567.518 h -36.955 V 425.541 l 36.99 -36.422 v 178.43 L 677.788 567.518 Z M 677.788 101.96 v 226.159 l -33.87 -30.34 L 589.633 59.045 l 88.155 42.882 V 101.96 Z M 457.521 58.635 h 2.552 l -54.287 239.302 l -43.795 34.09 V 94.807 l 95.562 -36.172 H 457.521 Z";

/**
 * menuType footer菜单类型
 * menuType 路线规划0：0-路线；场地管理1：3-监管区域；区域管理2：1-安全区域，2-危险区域，4-区域划分；其他
 */
var menuType = "9";

var tripStatus = "0";//0-行程进行中，1-行程结束
/**
 * 地图初始化
 */
var portIconSrc = "static/images/kouan.png";
function initMap() {
	var pointArr = {
		lat : 40.399660000000004,
		lng : 116.85329000000002
	};
	var params = {
		mapId : "map_canvas",// div地图id
		isShowMapType : true,// 是否展示地图类型
		mapTypeDirect : "top_left",
		zoom : 9,// 地图级别
		isClickZoom : true,// 是否双击缩放
		isScroll : true,// 滚轮缩放
		isDarggle : true,// 拖拽地图
		isZoomControl : true,// 缩放控件
		zoomControlDirect : "left_top",
		isPanControl : true,// 平移控件
		panControlDirect : "left_top",
		isStreetView : false,// 街景视图
		streetViewDirect : "bottom_left",
		isScale : false,// 比例尺
		scaleDirect : "bottom_right",
		mapType : "r"// 地图类型
	};
	// 初始化google地图
	GisInitialize(pointArr, params);
}
/**
 * 设置地图中心
 * 
 * @param homePointArr
 */
function replay() {
	// var pointArr={lat:40.36140605,lng:116.823673};
	var params = {
		mapId : "map_canvas",// div地图id
		isShowMapType : true,// 是否展示地图类型
		mapTypeDirect : "top_left",
		zoom : 16,// 地图级别
		isClickZoom : true,// 是否双击缩放
		isScroll : true,// 滚轮缩放
		isDarggle : true,// 拖拽地图
		isZoomControl : true,// 缩放控件
		zoomControlDirect : "left_top",
		isPanControl : true,// 平移控件
		panControlDirect : "left_top",
		isStreetView : false,// 街景视图
		streetViewDirect : "bottom_left",
		isScale : false,// 比例尺
		scaleDirect : "bottom_right",
		mapType : "r"// 地图类型
	};
	// var homePoint = new google.maps.LatLng(40.36140605, 116.823673);
	var homePoint = new google.maps.LatLng(40.4009914, 116.915584);

	map.setCenter(homePoint);
	map.setZoom(16);
	// GisInitialize(pointArr,params);
}

var drawPolylineStyle = {
	"color" : "#ffff00",
	"weight" : 2,
	"opacity" : 1
}
/**
 * 初始化画图工具
 */
function mapInitDrawingManagerAndDriving() {
	var drawPolylineStyle = {
		"color" : "#ff0000",
		"weight" : 2,
		"opacity" : 0.36
	}
	var drawParams = {
		direct : "right_top",
		darwStyle : drawPolylineStyle
	};
	initDrawingManagerAndDriving(drawParams);
}

var labelArray = [];//口岸label数组
var portArray = [];//口岸图标数组
/**
 * 初始化口岸
 */
function mapInitPort() {
	var portUrl = getRootPath() + "port/findAllCommonPorts.action";
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
			var gpsData = jsonObj;
			if (gpsData.length > 0) {
				GisClearOverlays(labelArray);
				GisClearOverlays(portArray);
				var qdPortHTML = "";
				var zdPortHTML = "";
				$("#startPort").append("");
				$("#endPort").append("");
				var pointArray = [];
				labelArray = [];//重新初始化
				portArray = [];//重新初始化
				for ( var key in gpsData) {
					var data = gpsData[key];
					qdPortHTML += '<li onclick="findAllMonitorTrip()" ><label>';
					qdPortHTML += '<input type="checkbox" value='+data.portId+' name="qdPorts">';
					qdPortHTML += '<img alt="'+data.portName+'" src="static/images/ic_04.png"/>';
					qdPortHTML += data.portName+'</label></li>';
					
					zdPortHTML += '<li onclick="findAllMonitorTrip()" ><label>';
					zdPortHTML += '<input type="checkbox" value='+data.portId+' name="zdPorts">';
					zdPortHTML += '<img alt="'+data.portName+'" src="static/images/ic_04.png"/>';
					zdPortHTML += data.portName+'</label></li>';
					
					if(isNotNull(data.latitude) && isNotNull(data.longitude)){
						pointArray.push({
							lat : data.latitude,
							lng : data.longitude
						});
						var localPoint = {lng:data.longitude,lat:data.latitude};
	                    var portMarker = GisCreateMarkerAndLabel(localPoint, portIconSrc, ""+port.portId);
	                    var portLabel = GisCreateLabel(localPoint,data.portName);
	                	labelArray.push(portLabel);
	                	portArray.push(portMarker);
						GisSetShowFront(portMarker);//谷歌地图设置数字越大，越显示在最前
						var currentContent ="test";
										
						GisShowInfoWindow(portMarker,currentContent);
					}
				}
				GisSetViewPortByArray(pointArray);
				$("#startPort").append(qdPortHTML);
				$("#endPort").append(zdPortHTML);
				
			}

		}
	});
}

/**
 * 判断点是否在面内
 */
function mapIsMapinPolygon() {
	// 显示地图边界
	// getAndShowBoundary(yanshiBoundary);
	var pointArr = {
		lat : 40.36140605,
		lng : 116.823673
	};
	alert(isWithinPolygon(pointArr, yanshiBoundary));

}
var polygonStyle = {
	"color" : "#ff0000",
	"weight" : 2,
	"opacity" : 0.2,
	"fillColor" : "#00ff00",
	"fillOpacity" : 0.36,
}
var polygonStyleDanger = {
	"color" : "#ffff00",
	"weight" : 2,
	"opacity" : 0.2,
	"fillColor" : "#ff0000",
	"fillOpacity" : 0.36,
}
/**
 * 地图添加面
 */
function mapShowPolygonInMap() {

	showPolygonInMap(yanshiBoundary, true, polygonStyle);
}

/**
 * 创建线路演示
 */
var measureFlag = true;
function mapMeasure() {
	console.log(measureFlag);
	if(measureFlag){
		if(undefined!=$("#dvPanel")&&null!=$("#dvPanel")){
			measureFlag = false;
			$("#dvPanel").attr("disabled",false)
			$("#dvPanel").empty();
			$("#legend").addClass("hidden")
			//$("#dvPanel").addClass("dvPanelshadow");
			GismapMeasure("dvPanel",getDistDuration);
			
		}
	}else{
		measureFlag = true;
		$("#legend").removeClass("hidden")
		$("#dvPanelParent").animate({right:-400},"slow");
		GisClearDirectionsDisplay();
		//$("#dvPanelParent").addClass("hidden");
	}
	
}

function getDistDuration(obj){
	$("#ceju").attr("disabled",true)
	$("#dvPanelParent").removeClass("hidden").animate({right:0},"slow").addClass("dvPanelshadow");
}
/**
 * 根据区域或线路编号获取
 */
function mapShowPoygonByRouteAreaId(routeAreaId) {
	var Url = getRootPath()
			+ "monitorRaPoint/getPointsByRouteAreaId.action?routeAreaId="
			+ routeAreaId;
	$.ajax({
		type : "POST",
		url : Url,
		dataType : "json",
		cache : false,
		async : false,
		error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
		success : function(jsonObj) {
			var points = jsonObj.jsonData;
			var drawType = jsonObj.routeType;
			var pointArray = [];
			if (points.length > 0) {
				for (var i = 0; i < points.length; i++) {
					pointArray.push({
						lat : points[i].latitude,
						lng : points[i].longitude
					});
				}
				if ("0" == drawType) {
					showRoadLineInMap(pointArray, drawPolylineStyle);
				} else {
					if ("1" == jsonObj.routeAreaStatus) {
						showPolygonInMap(pointArray, true, polygonStyle);
					} else {
						showPolygonInMap(pointArray, true, polygonStyleDanger);
					}

				}
				//

				// createLineTrack(pointArray);
			}

		}
	});
}

/**
 * 添加规划路线
 */
function addMonitorRouteArea(jsondata) {
	var param = $("#routeAreaForm").serialize()
	var portUrl = getRootPath() + "monitorRaPoint/addMonitorRaPoint.action";
	$.ajax({
		type : "POST",
		url : portUrl,
		data : param,
		dataType : "json",
		cache : false,
		async : false,
		error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
		success : function(jsonObj) {
			alert("add sucess");

		}
	});
}

/**
 * 查询所有车辆
 */
function findAllMonitorTrip() {
	$("#header_title").html($.i18n.prop("link.system.vehicleList"));
	$("#panelList").html("");
	
	var qdCheckValues = [];
	$('input[name="qdPorts"]:checked').each(function(){
		qdCheckValues.push($(this).val());
	});
	
	var zdCheckValues = [];
	$('input[name="zdPorts"]:checked').each(function(){
		zdCheckValues.push($(this).val());
	});
	
	/*var vehicleTypes = [];
	$('input[name="vehicleTypes"]:checked').each(function(){
		vehicleTypes.push($(this).val());
	});*/
	
	var param = "&qdPorts="+qdCheckValues+"&zdPorts="+zdCheckValues;
	var portUrl = getRootPath() + "monitorTripVehicle/findAllTripVelhicle.action?tripStatus="+tripStatus + param;
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
			var vehicleArray = [];
			// areaText = "<ul class=\"Custom_list\">";
			if (vehicleData.length > 0) {
				for (var i = 0; i < vehicleData.length; i++) {
					var gpsVehicleInfo = vehicleData[i];
					if (gpsVehicleInfo.trackingDeviceNumber != null
							&& gpsVehicleInfo.trackingDeviceNumber != "") {
						var iconpath = "";
						if ("2" == gpsVehicleInfo.riskStatus) {
							iconpath = "static/images/alarmtruck.png";
						} else if ("1" == gpsVehicleInfo.riskStatus)
							iconpath = "static/images/warningtruck.png";
						else {
							iconpath = "static/images/Safetruck.png";
						}
						var areaText = "<li onclick=\"getVehiclePaths('"
								+ gpsVehicleInfo.trackingDeviceNumber 
								+ "','" + gpsVehicleInfo.vehicleId
								+ "');\"><label><input type=\"checkbox\"><img alt=\""
								+ gpsVehicleInfo.vehiclePlateNumber
								+ "\"src=\""
								+ iconpath
								+ "\"/>"
								+ "<a href='javascript:void(0);' class=\"vehicle_label\">"+ gpsVehicleInfo.vehiclePlateNumber+" </a>"
								
								+ "</label></li>";

						$("#panelList").append(areaText);
					}
				}
			}else{
				bootbox.alert($.i18n.prop("map.routeArea.select.noVehicleLine"));
			}

		}
	});
}

/**
 * 根据关锁号获取轨迹坐标
 */
function getVehiclePaths(trackingDeviceNumber,vehicleId) {
	vehicleRealPathPointsArr = new Array();
	realVehicleDetailMarker = undefined;
	trackingDeviceNumberParam = trackingDeviceNumber;
	var portUrl = getRootPath()
			+ "monitorvehicle/findAllMonitorVehicleGpsByEclockNum.action?trackingDeviceNumber="
			+ trackingDeviceNumber + "&vehicleId="+vehicleId;
	$.ajax({
		type : "POST",
		url : portUrl,
		cache : false,
		async : false,
		error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
		success : function(obj) {
			var alarmArray=[];
			if (null != obj) {
				alert(obj.success);
				if(obj.success){
					//车辆轨迹
					GisClearOverlays(vehicleMarkers);
					GisClearOverlays(patrolMarkers);
					trackingTripId= obj.lsMonitorTripBO.tripId;
					var jsonObj = obj.lsMonitorVehicleGpsBOs;
					if(jsonObj!=null){
						for (var i = 0; i < jsonObj.length; i++) {
							tripBufferPoints.push({
								lat : jsonObj[i].latitude,
								lng : jsonObj[i].longitude,
								direction: jsonObj[i].direction
							});
							//显示隐藏轨迹上的点
							/*var pointMarker = GisCreateMarker({lat:jsonObj[i].latitude,lng:jsonObj[i].longitude},"static/images/pointIcon.png",""+jsonObj[i].gpsId);
							GisShowInfoWindow(pointMarker,""+jsonObj[i].gpsId);
							pointMarkers.push(pointMarker);*/
						}
						mapCreateTracking(tripBufferPoints);
						//车辆报警
						var alarmPoints = obj.lsMonitorAlarmBOs;
						for (var i = 0; i < alarmPoints.length; i++) {
							var alarmMarker = GisCreateMarker({
								lat : alarmPoints[i].alarmLatitude,
								lng : alarmPoints[i].alarmLongitude
							}, "static/images/1_03.png", ""+alarmPoints[i].alarmId);
							
							var alarmContent = alarmPoints[i].alarmStatus;
			 				GisShowInfoWindow(alarmMarker,alarmContent);
							
			 				overlaysArray.push(alarmMarker);
						}
					}
				}else{
					bootbox.alert($.i18n.prop("map.routeArea.select.noVehicleLine"));
				}
				
			}else{
				bootbox.alert($.i18n.prop("map.routeArea.select.noVehicleLine"));
			}
		}
	});
}
function mapCreateTracking(pointArray) {
	var lineStyle = {
		color:"#1800ff", 
		weight:4, 
		opacity:1
	};
	/*if($("#handlePoint").attr("name")=="showPoint"){
		GisHiddenOverlays(pointMarkers);
	}else{
		GisShowOverlays(pointMarkers);
	}*/
	
	if(menuType=="8"){
		GisCreateLineTrack(pointArray);
	}else{
		trackingLine = GisShowPolyLineInMap(pointArray, true, lineStyle);
		GisSetViewPortByArray(pointArray);
		overlaysArray.push(trackingLine);
	}
	
	var localPoint = pointArray[pointArray.length-1];
	trackingMaker =  GisCreateMarker(localPoint, GisCreateSvgIcon(svgPath,0.05,localPoint.direction,"yellow","green"), ""+localPoint.gpsId);
	vehicleMarkers.push(trackingMaker);
}

function findAllRouteAreaList() {
	$("#planRouteAreaList").html("");
	var portUrl = getRootPath() + "monitorroutearea/findAllRouteAreas.action?ids="+menuType;
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
			var routeArea = jsonObj;

			// areaText = "<ul class=\"Custom_list\">";
			if (routeArea.length > 0) {
				for (var i = 0; i < routeArea.length; i++) {
					var routeAreainfo = routeArea[i];
					if (routeAreainfo.routeAreaId != null
							&& routeAreainfo.routeAreaId != "") {
						var iconpath = "";
						if("0"==routeAreainfo.routeAreaType){
							if ("0" == routeAreainfo.routeAreaStatus) 
								iconpath = "static/images/1_10.png";
							else 
								iconpath = "static/images/1_10gray.png";
						}else if("1"==routeAreainfo.routeAreaType){
							if ("0" == routeAreainfo.routeAreaStatus) 
								iconpath = "static/images/1_09.png";
							else 
								iconpath = "static/images/1_09gray.png";
						}else if("2"==routeAreainfo.routeAreaType){
							if ("0" == routeAreainfo.routeAreaStatus) 
								iconpath = "static/images/1_03.png";
							else 
								iconpath = "static/images/1_03gray.png";
						}else if("3"==routeAreainfo.routeAreaType){
							if ("0" == routeAreainfo.routeAreaStatus) 
								iconpath = "static/images/changdi.png";
							else 
								iconpath = "static/images/changdigray.png";
						}else{
							if ("0" == routeAreainfo.routeAreaStatus) 
								iconpath = "static/images/1_05.png";
							else 
								iconpath = "static/images/1_05gray.png";
						}
						var areaText = "<li onclick=\"getPointsByRouteAreaId('"
								+ routeAreainfo.routeAreaId
								+ "');\"><label><input type=\"checkbox\" "
								+ "name=\"routeAreaIds\" value=\""
								+ routeAreainfo.routeAreaId+"\"><img  alt=\""
								+ routeAreainfo.routeAreaName
								+ "\" src=\""
								+ iconpath
								+ "\"/>"
								+ "<a href='javascript:void(0);' onclick=\"getPointsByRouteAreaId('"
								+ routeAreainfo.routeAreaId
								+ "');\"> </a>"
								+ routeAreainfo.routeAreaName
								+ "</label></li>";

						$("#planRouteAreaList").append(areaText);
					}
				}
			}
			if(menuType=="0" || menuType=="1" || menuType=="2"){
				$("#planRouteAreaList").removeClass("hidden");
				$("#panelList").addClass("hidden");
				$("#patrolList").addClass("hidden");
				$("#classify").addClass("hidden");
				$("#addDelUpdate").removeClass("hidden");
				$("#addRapoint").addClass("hidden");
			}
		}
	});

}

/**
 * 根据区域或线路编号获取
 */
function getPointsByRouteAreaId(routeAreaId) {
	var Url = getRootPath()
			+ "monitorRaPoint/getPointsByRouteAreaId.action?routeAreaId="
			+ routeAreaId;
	$.ajax({
		type : "POST",
		url : Url,
		dataType : "json",
		cache : false,
		async : false,
		error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
		success : function(jsonObj) {
			GisClearOverlays(overlaysArray);
			var points = jsonObj.jsonData;
			var drawType = jsonObj.routeType;
			var pointArray = [];
			if (points.length > 0) {
				for (var i = 0; i < points.length; i++) {
					pointArray.push({
						lat : points[i].latitude,
						lng : points[i].longitude
					});
				}
				if ("0" == drawType) {
					var drawPolylineStyle = {
							"color" : "#ffff00",
							"weight" : 2,
							"opacity" : 1
						}
					var polyline = GisShowPolyLineInMap(pointArray,true, drawPolylineStyle);
					overlaysArray.push(polyline);
				} else {
					var polygon;
					if ("1" == jsonObj.routeAreaStatus) {
						polygon = GisShowPolygonInMap(pointArray, true, polygonStyle);
					} else {
						polygon = GisShowPolygonInMap(pointArray, true, polygonStyleDanger);
					}
					overlaysArray.push(polygon);
				}
				GisSetViewPortByArray(pointArray);
			}else{
				bootbox.alert($.i18n.prop("map.routeArea.select.noPlanRoute"));
			}

		}
	});
}



function getAllSessionCarforInit(){

}


/**
 * 接受后台传入坐标标注车辆
 */

function getSocketCoods(coods){
	var vehicleSrc = "static/images/ic_03.png";
	var point = new google.maps.LatLng(coods.lat,coods.lng);
	GisCreateMarker(point, vehicleSrc, "vehicle");
	//createSVGMarker(point, vehicleSrc, "vehicle");
}

function showVehicleInMap(){
	var vehicleType = [];
	$('input[name="vehicleTypes"]:checked').each(function(){
		vehicleType.push($(this).val());
	});
	if(vehicleType.indexOf("0")>-1){
		GisShowOverlays(vehicleMarkers);
		markerclusterer = GisMarkersClusterer(vehicleMarkers, 60, markerclusterer);
	}else{
		GisHiddenOverlays(vehicleMarkers);
		GisClearMarkerClusterer(markerclusterer);
	}
}

function showPatrolInMap(){
	var vehicleType = [];
	$('input[name="vehicleTypes"]:checked').each(function(){
		vehicleType.push($(this).val());
	});
	if(vehicleType.indexOf("1")>-1){
		GisShowOverlays(patrolMarkers);
		partolclusterer = GisMarkersClusterer(patrolMarkers, 60, partolclusterer);
	}else{
		GisHiddenOverlays(patrolMarkers);
		GisClearMarkerClusterer(partolclusterer);
	}
}

function findAllPartrols() {
	$("#patrolList").html("");
	var portUrl = getRootPath() + "patrol/findAllCommonPatrols.action";
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
			var commonPatrols = jsonObj;
			if (commonPatrols.length > 0) {
				for (var i = 0; i < commonPatrols.length; i++) {
					var commonPatrol = commonPatrols[i];
					if (commonPatrol.patrolId != null
							&& commonPatrol.patrolId != "") {
						var iconpath = "static/images/ic_08.png";
						var areaText = "<li onclick=\"getPatrolLocation('"
								+ commonPatrol.trackUnitNumber 
								+ "');\"><label><input type=\"checkbox\" "
								+ "name=\"patrolIds\" value=\""
								+ commonPatrol.patrolId+"\"><img  alt=\""
								+ commonPatrol.trackUnitNumber
								+ "\" src=\""
								+ iconpath
								+ "\"/>"
								+ "<a href='javascript:void(0);' class=\"vehicle_label\">"+ commonPatrol.trackUnitNumber+" </a>"
								+ "</label></li>";
						$("#patrolList").append(areaText);
					}
				}
			}
			if(menuType=="7"){
				$("#patrolList").addClass("hidden");
				$("#planRouteAreaList").addClass("hidden");
				$("#panelList").addClass("hidden");
				$("#classify").addClass("hidden");
				$("#addDelUpdate").removeClass("hidden");
				$("#addRapoint").addClass("hidden");
			}
		}
	});

}

/**
 * 点击巡逻队定位
 * @param trackUnitNumber
 */
function getPatrolLocation(trackUnitNumber){
	var getUrl = getRootPath() + "vehiclestatus/findPatrolByNumber.action";
	$.ajax({
		type : "POST",
		url : getUrl,
		dataType : "json",
		cache : false,
		async : false,
		error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
		success : function(obj) {
			if(obj.success){
				var patrolStatus = obj.lsMonitorVehicleStatusBO;
				if(vehicleMarkers){
		    		for ( var i = 0; i < vehicleMarkers.length; i++) {
		    			if(vehicleMarkers[i].getTitle() == (""+patrolStatus.vehicleId)){
		    				//GisRemoveMarkerByName(vehicleMarkers[i],true);
		    				var location = {lat:patrolStatus.latitude,lng:patrolStatus.longitude};
		    				GisSetHomeCenter(location);
		    				GisShowInfoWindow(vehicleMarkers[i],"",false);  
		    			}
		    		}
		    	}else{
		    		bootbox.alert($.i18n.prop("map.routeArea.select.noPatrol"));
		    	}
			}else{
				bootbox.alert($.i18n.prop("map.routeArea.select.noPatrol"));
			}
		}
	});
}

/**
 * @param flag 是否清除巡逻队
 */
function clearAllOverlays(flag){
	GisClearOverlays(vehicleMarkers);
	GisClearOverlays(patrolMarkers);
	GisClearOverlays(pointMarkers);
	GisClearOverlays(overlaysArray);
	GisClearLineTrack();
	trackingLine = undefined;//车辆的行走路线
	trackingTripId = undefined;//详细车辆的行程id
	trackingMaker = undefined;//轨迹车辆的Maker
	tripBufferPoints = new Array();;//存储缓冲车辆数据
	if(true) GisClearOverlays(xlcArray);
}

/******************************轨迹回放按钮功能******************************************/
var replayIcon= "static/images/Safetruck.png";
/**
 * 重定位
 */
function replaceLoc(){
	var pointArray = GisGetDrawPath();
	if(pointArray.length>0){
		GisSetViewPortByArray(pointArray);
	}
}
/**
 * 回放
 */
function replayStart(){
	GisPathPlay(replayIcon);
}
/**
 * 暂停
 */
function replayPause(){
	GisPathPause();
}
/**
 * 继续回放
 */
function replayContinue(){
	GisContinuePlay(replayIcon);
}
/**
 * 停止回放
 */
function replayStop(){
	GisPathStop(replayIcon);
}
/**
 * 加速
 */
function replaySpeedUp(){
	GisPathAccelaratePaly(replayIcon);
}
/**
 * 减速
 */
function replaySpeedDown(){
	GisPathDecelaratePaly(replayIcon);
}


