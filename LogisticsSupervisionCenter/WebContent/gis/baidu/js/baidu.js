/**
 * @author 赵磊峰
 * @date 2014-11-27
 */
var map;// 指定的地图容器，通常放在div中
var markerclusterer;// markerclusterer聚合对象
/*
 * 地图类型： BMAP_NORMAL_MAP 此地图类型展示普通街道视图。 BMAP_PERSPECTIVE_MAP 此地图类型展示透视图像视图。
 * BMAP_SATELLITE_MAP 此地图类型展示卫星视图。(自 1.2 新增) BMAP_HYBRID_MAP
 * 此地图类型展示卫星和路网的混合视图。(自 1.2 新增)
 * 
 * 系统支持以下控件位置： BMAP_ANCHOR_TOP_LEFT 控件将定位到地图的左上角。 BMAP_ANCHOR_TOP_RIGHT
 * 控件将定位到地图的右上角。 BMAP_ANCHOR_BOTTOM_LEFT 控件将定位到地图的左下角。 BMAP_ANCHOR_BOTTOM_RIGHT
 * 控件将定位到地图的右下角。
 */

/**
 * 初始化google地图
 * 参数属性及是否展示:ID，中心坐标，是否展示地图类型，地图级别，是否双击缩放，是否设置中心，滚轮缩放，拖拽地图，缩放控件，平移控件，街景视图，比例尺
 * mapId、point、isShowMapType、zoom、isClickZoom、isCenter、isScroll、isDarggle、isZoomControl、isPanControl、isStreetView、isScale
 */

var mapId;// div地图id
var isShowMapType = true;// 是否展示地图类型
var zoom = 16;// 地图级别，默认给16
var isClickZoom = true;// 是否双击缩放
var isScroll = true;// 滚轮缩放
var isDarggle = true;// 拖拽地图
var isZoomControl = true;// 缩放控件
var isPanControl = true;// 平移控件
var isStreetView = false;// 街景视图
var isScale = false;// 比例尺
var thisMapType = BMAP_NORMAL_MAP;// 地图类型,r为地图,s为卫星图
var mapTypeDirect = "top_right";
var zoomControlDirect = "top_left";
var panControlDirect = "top_left";
var streetViewDirect = "bottom_left";
var scaleDirect = "buttom_right";

/**
 * 参数处理函数
 * 
 * @param params
 */
function handleParams(params) {
	if (params.mapId != '' && params.mapId != null)
		mapId = params.mapId;
	if (params.isShowMapType == false)
		isShowMapType = false;
	if (params.zoom != '' && params.mapId != null)
		zoom = params.zoom;
	if (params.isClickZoom == false)
		isClickZoom = false;
	if (params.isScroll == false)
		isScroll = false;
	if (params.isDarggle == false)
		isDarggle = false;
	if (params.isZoomControl == false)
		isZoomControl = false;
	if (params.isPanControl == false)
		isPanControl = false;
	if (params.isStreetView == true)
		isStreetView = true;
	if (params.isScale == true)
		isScale = true;
	if (params.mapType == 'h')
		thisMapType = BMAP_HYBRID_MAP;
	if (params.mapType == 'r')
		thisMapType = BMAP_NORMAL_MAP;
	if (params.mapTypeDirect != '' && params.mapTypeDirect != null)
		mapTypeDirect = params.mapTypeDirect;
	if (params.zoomControlDirect != '' && params.zoomControlDirect != null)
		zoomControlDirect = params.zoomControlDirect;
	if (params.panControlDirect != '' && params.panControlDirect != null)
		panControlDirect = params.panControlDirect;
	if (params.streetViewDirect != '' && params.streetViewDirect != null)
		streetViewDirect = params.streetViewDirect;
	if (params.scaleDirect != '' && params.scaleDirect != null)
		scaleDirect = params.scaleDirect;

}

/**
 * 初始化地图设置展示
 * 
 * @param pointArr
 * @param params
 */
function initialize(pointArr, params) {
	var centerLatLng = new BMap.Point(pointArr.lng, pointArr.lat);
	handleParams(params);
	
	map = new BMap.Map(mapId, {
		enableMapClick : false //设置底图不可点
	});
	map.centerAndZoom(centerLatLng, zoom);
	if (isScroll) {
		map.enableScrollWheelZoom(); // 设置可以用鼠标滚轮缩放
	} else {
		map.disableScrollWheelZoom(); // 设置禁用鼠标滚轮缩放
	}
	if (isClickZoom) {
		map.enableDoubleClickZoom();// 允许双击放大
	} else {
		map.disableDoubleClickZoom();// 禁用双击放大
	}
	if (isDarggle) {
		map.enableDragging();// 启用拖拽地图功能
	} else {
		map.disableDragging();// 禁止拖拽地图功能
	}
	map.enableAutoResize();//启用自动适应容器尺寸变化
	if (isPanControl && isZoomControl) {
		map.addControl(new BMap.NavigationControl({
			type : BMAP_NAVIGATION_CONTROL_LARGE,
			anchor : getDirection(panControlDirect),
			offset : new BMap.Size(20, 60)
		})); // 放大缩小地图控件
	}else if(!isPanControl && isZoomControl){
		map.addControl(new BMap.NavigationControl({
			type : BMAP_NAVIGATION_CONTROL_ZOOM,
			anchor : getDirection(panControlDirect),
			offset : new BMap.Size(20, 60)
		})); // 放大缩小地图控件
	}else if(isPanControl && !isZoomControl){
		map.addControl(new BMap.NavigationControl({
			type : BMAP_NAVIGATION_CONTROL_PAN,
			anchor : getDirection(panControlDirect),
			offset : new BMap.Size(20, 60)
		})); // 放大缩小地图控件
	}
	if (isShowMapType) {
		map.addControl(new BMap.MapTypeControl({
			mapTypes : [ BMAP_NORMAL_MAP, BMAP_SATELLITE_MAP,
					BMAP_HYBRID_MAP ],
			anchor : getDirection(mapTypeDirect)
		})); // 地图类型控件
	}
	if (isScale) {
		map.addControl(new BMap.ScaleControl({
			anchor : getDirection(scaleDirect)
		}));
	}
	
	if(isStreetView){
		//var stCtrl = new BMap.PanoramaControl();  
		//stCtrl.setOffset(new BMap.Size(20, 20));  
		map.addControl(new BMap.PanoramaControl({
			anchor : getDirection(streetViewDirect)
		}));
	}

	
}

/**
 * 设置地图中心
 * @param homePointArr
 * @param zoom
 */
function setHomeCenter(homePointArr, zoom) {
	var homePoint = new BMap.Point(homePointArr.lng, homePointArr.lat);
	map.centerAndZoom(homePoint, zoom);
}

/**
 * 设置地图类型
 * 
 * @mapType 地图类型
 */
function setMapType(mapType) {
	if (mapType == 'h') {
		map.setMapType(BMAP_HYBRID_MAP);
	} else if (mapType == 'r') {
		map.setMapType(BMAP_NORMAL_MAP);
	} else {
		map.setMapType(BMAP_NORMAL_MAP);
	}

}

/**
 * 根据传递的值判断方向 参数：左上、右上、左下、右下
 * 
 * @param direct
 * @returns
 */
function getDirection(direct) {
	var direction = BMAP_ANCHOR_TOP_LEFT;
	if (direct == 'top_left' || direct == 'left_top') {
		direction = BMAP_ANCHOR_TOP_LEFT;
	}
	if (direct == 'top_right' || direct == 'right_top') {
		direction = BMAP_ANCHOR_TOP_RIGHT;
	}
	if (direct == 'bottom_left' || direct == 'left_bottom') {
		direction = BMAP_ANCHOR_BOTTOM_LEFT;
	}
	if (direct == 'buttom_right' || direct == 'right_buttom') {
		direction = BMAP_ANCHOR_BOTTOM_RIGHT;
	}
	return direction;
}

var markers = new Array();
/**
 * 地图添加覆盖物方法,如果要确定是否加入清楚覆盖物中，可再加一个boolean参数isDeleted
 * 
 * @param location
 * @param iconSrc
 * @returns {google.maps.Marker}
 */
function createMarker(location, iconSrc) {
	var showMarker = new BMap.Marker(
		new BMap.Point(location.lng, location.lat), {
			icon : iconSrc
	});
	map.addOverlay(showMarker);
	return showMarker;
}

// var markersArray = [];
/**
 * 地图上移除覆盖物方法，数组还存在
 * 
 * @param markersArray
 */
function clearOverlays(markersArray) {
	if (markersArray) {
		for (i in markersArray) {
			map.removeOverlay(markersArray[i]);
		}
	}
}
/**
 * 移除叠加层覆盖物方法（删除覆盖物及数组，相应数组的length 设为 0）
 * 
 * @param markersArray
 */
function deleteOverlays(markersArray) {
	if (markersArray) {
		for (i in markersArray) {
			map.removeOverlay(markersArray[i]);
		}
		markersArray.length = 0;
	}
}

/**
 * 清除覆盖物，选定覆盖物
 * 
 * @param overlays
 */
function clearOverlay(overlays) {
	for ( var i = 0; i < overlays.length; i++) {
		map.removeOverlay(overlays[i]);
	}
	overlays.length = 0;
}

/**
 * 清除地图上所有覆盖物
 */
function clearOverlays() {
	map.clearOverlays();
}

/**
 * 根据具体的位置定义marker
 * 
 * @param location
 * @param iconSrc
 * @param isFront
 *            是否显示在最前
 * @returns {google.maps.Marker}
 */
function createMarkerByPoint(location, iconSrc, isFront) {
	var showMarker = new BMap.Marker(
		new BMap.Point(location.lng, location.lat), {
			icon : iconSrc
	});
	map.addOverlay(showMarker);
	if (isFront) {
		showMarker.setZIndex(10000);//设置覆盖物的zIndex。
		//showMarker.setTop(true);//将标注置于其他标注之上。 
	}
	return showMarker;
}

/**
 * 点聚合对象-网格分组
 * 
 * @param clustererMarkers-分组的markers
 * @param gridSize
 *            聚合计算时网格的像素大小
 */
function markersClusterer(clustererMarkers, gridSize) {
	// 清除掉已经存在的聚合对象
	clearMarkerClusterer(markerclusterer);
	// 创建一个MarkerClusterer对象，将marker数组对象传递给它，网格分组
	// MarkerClusterer使用的正确方法，三个参数，第一个是地图的引用，第二个是一个marker数组
	markerclusterer = new MarkerClusterer(map, {markers:clustererMarkers});
	markerclusterer.setMinClusterSize(2);
	markerclusterer.setMaxZoom(18);
	markerclusterer.setGridSize(gridSize);
	/*
	 * gridSize {Number} 聚合计算时网格的像素大小，默认60 maxZoom {Number}
	 * 最大的聚合级别，大于该级别就不进行相应的聚合 MaxZoom {Number}
	 * 最小的聚合数量，小于该数量的不能成为一个聚合，默认为2 minimumClusterSize {Number}
	 * 聚合点的落脚位置是否是所有聚合在内点的平均值，默认为否，落脚在聚合内的第一个点 styles {Array} 自定义聚合后的图标风格 var
	 * markers = [...]; // Create the markers you want to add and collect them
	 */
}

/**
 * 清除聚合对象markerclusterer
 * 
 * @param markerclusterer聚合对象
 * @returns {String}
 */
function clearMarkerClusterer(markerclusterers) {
	// 在查询之前需要先将生成的MarkerClusterer对象中的marker清除掉
	if (typeof (markerclusterers) == "object"
			|| typeof (markerclusterers) != "undefined") {
		markerclusterers.clearMarkers();
	}
}

/**
 * 跟着鼠标获取坐标
 * 
 * @returns {String}
 */
function getPointByClick() {
	var lat = map.getCenter().lng;
	var lng = map.getCenter().lng;
	map.addEventListener("dblclick", function(event) {
		lat = event.point.lat;// 纬度
		lng = event.point.lng;// 经度
		// document.getElementById(showPointId).innerHTML=lng+','+lat;
		alert(lng + "," + lat);
	});
	return "{lng:" + lng + ",lat:" + lat + "}";
}

/**
 * 控制地图的缩放级别事件
 * 
 * @param num
 */
function setZoomLevel(num) {
	map.setZoom(num);
}

/**
 * 控制地图的最大最小缩放级别
 * 
 * @param MinZoomLevel
 * @param MaxZoomLevel
 */
function setZoomMinLevel(MinZoomLevel, MaxZoomLevel) {
	// 控制地图的缩放级别
	map.addEventListener("zoomstart", function() {
		if (map.getZoom() < MinZoomLevel)
			map.setZoom(MinZoomLevel);
		if (map.getZoom() > MaxZoomLevel)
			map.setZoom(MaxZoomLevel);
	});
	// map.setMinZoom(MinZoomLevel);
	// map.setMaxZoom(MaxZoomLevel)
}

/**
 * 设置地图的自适应，使其展示全貌
 * 
 * @param points
 *            传递的是多个点
 * @returns
 */
function setViewPort(points) {
	map.setViewport(points);
}

/**
 * 设置地图的自适应，使其展示全貌
 * 
 * @param pointArray
 *            传递的是多个点组成的数组
 * @returns
 */
function setViewPortByArray(pointArray) {
	var points = new Array();
	for ( var i = 0; i < pointArray.length; i++) {
		points.push(new BMap.Point(pointArray[i].lng, pointArray[i].lat));
	}
	map.setViewport(points);
}

/**
 * 创建一条线路
 * 
 * @param：polylineArr 连线点的json数据集合
 * @param：isShow 是否显示在地图上
 * @param：styleParam - strokeColor、strokeWeight、strokeOpacity
 */
function showPolyLineInMap(polylineArr, isShow, styleParam) {
	var pathPointArr = new Array();
	for ( var i = 0; i < polylineArr.length; i++) {
		pathPointArr
				.push(new BMap.Point(polylineArr[i].lng, polylineArr[i].lat));
	}
	var polyline = new BMap.Polyline(pathPointArr, {
		strokeColor : styleParam.color,
		strokeWeight : styleParam.weight,
		strokeOpacity : styleParam.opacity
	});
	if (isShow) {
		map.addOverlay(polyline);// 增加折线
		// 设置show或者隐藏也可以marker.show(); polyline.show(); circle.show();
		// marker.hide(); polyline.hide(); circle.hide();
	}
	return polyLine;
}

/**
 * 更新一条线路
 * 
 * @param：polyline 更新的路线
 * @param：polylineArr 连线点的json数据集合
 * @param：styleParam - strokeColor、strokeWeight、strokeOpacity
 */
function showUpdatePolyLine(polyline, polylineArr, styleParam) {
	var pathPointArr = new Array();
	for ( var i = 0; i < polylineArr.length; i++) {
		pathPointArr
				.push(new BMap.Point(polylineArr[i].lng, polylineArr[i].lat));
	}
	polyline.setPath(pathPointArr);
	return polyline;
}

/**
 * 创建多边形
 * 
 * @param：polygonArr 连多边形点的json数据集合
 * @param：isShow 是否显示在地图上
 */
function showPolygonInMap(polygonArr, isShow) {
	var polygonPointArr = new Array();
	for ( var i = 0; i < polygonArr.length; i++) {
		polygonPointArr.push(new BMap.Point(polygonArr[i].lng,
				polygonArr[i].lat));
	}
	// 区域的样式strokeStyle，solid或dashed。
	var polyGon = new BMap.Polygon(polygonPointArr, {
		strokeColor : mapPolygonStyle.color,
		strokeWeight : mapPolygonStyle.weight,
		strokeOpacity : mapPolygonStyle.opacity,
		fillColor : mapPolygonStyle.fillColor,
		fillOpacity : mapPolygonStyle.fillOpacity
	});
	if (isShow) {
		map.addOverlay(polyGon);
	}
	return polyGon;
}

/**
 * 判断点是否在多边形内
 * 
 * @param：pointArr 点
 * @param：polygonArr 生成多边形的数组
 * @return 在true，不在false
 */
function isWithinPolygon(pointArr, polygonArr) {
	var coordinate = new google.maps.LatLng(pointArr.lat, pointArr.lng);
	var polygon = showPolygonInMap(polygonArr, false);
	
	return BMapLib.GeoUtils.isPointInPolygon(coordinate,polygon);
}

/**
 * 创建口岸图标
 * 
 * @param labelIconSrc
 */
function createLabelIcon(labelIconSrc) {
	var labelIconOptions = {
		anchor:new BMap.Size(30, 30),
	};
	var labelIcon = new BMap.Icon(labelIconSrc, new BMap.Size(60, 60), labelIconOptions);
	return labelIcon;
}

/**
 * 创建label
 * 
 * @param labelpoint
 * @param name
 */
function createLabel(labelpoint, name, Labelstyle) {
	/*new LabeledText(map, new BMap.Point(labelpoint.lng, labelpoint.lat), name,
			new BMap.Size(20, -30));*/
	var portLabel = new BMap.Label(name, {position: new BMap.Point(labelpoint.lng, labelpoint.lat)});
	portLabel.setStyle(Labelstyle);
	portLabel.setOffset(new BMap.Size(-30,30));
	portLabel.disableMassClear();//设置为不可清除
	map.addOverlay(portLabel);
}

/**
 * 下面连续几个方法都是为了车辆轨迹回放调用准备 此方法：创建单车回放行驶轨迹
 * 
 * @param roadPath
 */
function createLineTrack(roadPath,plateNumber,iconSrc) {
	var pathPointArr = new Array();
	for ( var i = 0; i < roadPath.length; i++) {
		pathPointArr
				.push(new BMap.Point(roadPath[i].lng, roadPath[i].lat));
	}
	lushu = new BMapLib.LuShu(map,pathPointArr,{defaultContent:plateNumber,speed:50,landmarkPois:[],icon:iconSrc});
	showPolyLineInMap(roadPath, true, realPolylineStyle); 
}
/* 创建车辆位置图标 */
function createIcon(vehicleSrc) {
	var labelIconOptions = {
		anchor:new BMap.Size(16, 40),
	};
	var vehicleLocateIcon = new BMap.Icon(vehicleSrc, new BMap.Size(32, 40), labelIconOptions);
	return vehicleLocateIcon;
}
/* 创建Marker以及对应的label标签 */
function createMarkerAndLabel(localPoint, portSrc, name) {
	createMarker(localPoint, createLabelIcon(portSrc));
	createLabel(localPoint, name);
}
// 回放
function pathPlay(vehicleSrc) {
	lushu.start();
}
// 继续回放
function continuePlay(vehicleSrc) {
	lushu.start();
}
// 暂停
function pathPause() {
	lushu.pause();
}
// 停止回放
function pathStop() {
	lushu.stop();
}

var pointsArray = new Array();

/**
 * 处理接收的坐标点二维数组转化为要使用的baidu坐标点二维数组
 * 
 * @param pointsArrayData
 */
function analyzePointArray(pointsArrayData) {
	var len = pointsArrayData.length;
	for ( var j = 0; j < len; j++) {
		pointsArray[j] = new Array();
		var data = pointsArrayData[j].points;
		for ( var i = 0; i < data.length; i++) {
			pointsArray[j]
					.push(new BMap.Point(data[i].lng, data[i].lat));
		}
	}
}

/**
 * 显示多条路线,要走的路线底图
 * 
 * @param pointsArrayData
 */
function showRoadLineInMap(pointsArrayData) {
	analyzePointArray(pointsArrayData);
	for ( var i = 0; i < pointsArray.length; i++) {
		var polyline = new BMap.Polyline(pointsArray[i],{
			strokeColor : basePolylineStyle.color,
			strokeWeight : basePolylineStyle.weight,
			strokeOpacity : basePolylineStyle.opacity
		});
		map.addOverlay(polyline);
	}
}

var infowindows = [];// 定义车辆信息窗口
/**
 * 添加车辆信息窗口的事件
 * 
 * @param currentPosition
 *            坐标点
 * @param currentContent
 *            展示内容
 * @param currentMarker
 *            基于覆盖物
 */
function showInfoWindow(currentMarker, currentPosition, currentContent) {
	var opts = {
		//width : 250,     // 信息窗口宽度
		//height: 80,     // 信息窗口高度
		//title : "信息窗口" , // 信息窗口标题
		//offset：new BMap.Size(0,0),
		enableAutoPan:true, //允许窗口打开自动移动
		enableCloseOnClick:true, //开启点击地图关闭信息窗口
		enableMessage:false //关闭短信发送功能
	};
	// 创建信息窗口
	var infoWindow = new BMap.InfoWindow(currentContent,opts);
	
	currentMarker.addEventListener('mouseover', function() {
		// 需要在开启一个infoWindow的时候关闭其余的infoWindow
		if (typeof (infowindows) != "undefined") {
			for ( var int = 0; int < infowindows.length; int++) {
				this.closeInfoWindow(infowindows[int]);
			}
		}
		this.openInfoWindow(infoWindow);
	});

	currentMarker.addEventListener('mouseout', function() {
		//this.closeInfoWindow(infoWindow);
	});
	
	infowindows.push(infoWindow);
	return infoWindow;
}

var alarmInfowindows = [];// 定义报警信息窗口
/**
 * 添加车辆报警信息窗口的事件
 * 
 * @param currentContent
 *            展示内容
 * @param currentMarker
 *            基于覆盖物
 */
function showAlarmInfoWindow(currentMarker, currentContent) {
	var opts = {
		//width : 250,     // 信息窗口宽度
		//height: 80,     // 信息窗口高度
		//title : "信息窗口" , // 信息窗口标题
		//offset：new BMap.Size(0,0),
		enableAutoPan:true, //允许窗口打开自动移动
		enableCloseOnClick:true, //开启点击地图关闭信息窗口
		enableMessage:false //关闭短信发送功能
	};
	// 创建信息窗口
	var alarmInfoWindow = new BMap.InfoWindow(currentContent,opts);
	currentMarker.addEventListener('mouseover', function() {
		// 需要在开启一个infoWindow的时候关闭其余的infoWindow
		if (typeof (alarmInfowindows) != "undefined") {
			for ( var int = 0; int < alarmInfowindows.length; int++) {
				this.closeInfoWindow(alarmInfowindows[int]);
			}
		}
		this.openInfoWindow(alarmInfoWindow);
	});
	
	currentMarker.addEventListener('mouseout', function() {
		//this.closeInfoWindow(alarmInfoWindow);
	});
	alarmInfowindows.push(alarmInfoWindow);
	return alarmInfoWindow;
}

/**
 * 为具体的车辆添加点击事件
 * 
 * @param vehicleMarker
 *            添加事件的marker
 * @param pointArray
 *            轨迹点集合
 * @param isAlarm
 *            是否报警
 * @param warns
 *            报警数据
 * @param mapType
 *            地图类型
 */
function addClickForVehicle(vehicleMarker, pointArray, isAlarm, warns, mapType) {
	var status = "no";
	vehicleMarker.addEventListener('click', function() {
		status = "yes";
		setMapType(mapType);// 显示地图类型
		showPolyLineInMap(pointArray, true, realPolylineStyle);// 显示轨迹路线
		setViewPortByArray(pointArray); // 设置最佳视野
		if (isAlarm) {
			if (warns.length > 0) {
				for ( var i = 0; i < warns.length; i++) {
					var warnLocation = {
						lat : warns[i].location.latitude,
						lng : warns[i].location.longitude
					};
					var warnMarker;
					if (warns[i].level == 1) {
						warnMarker = createMarker(warnLocation, createIcon(alarm_yellow));
					} else {
						warnMarker = createMarker(warnLocation, createIcon(alarm_red));
					}
					var warnContent = getAlarmInfoWindowContent(warns[i]);

					showAlarmInfoWindow(warnMarker, warnContent);
				}
			}
		}
	});
	return status;
}

/**
 * 添加事件监听器
 * 
 * @param obj
 *            监听的事件对象
 * @param eventType
 *            监听的事件类型
 * @param fn
 *            监听事件触发的方法
 */
function addMyDomListener(obj, eventType, fn) {
	BMapLib.EventWrapper.addDomListener(obj, eventType, fn);
}

/**
 * 判断marker位置是否存在，存在放入markers
 * 
 * @param vehicleMarker
 */
function isAddMarker(currentMarker) {
	if (typeof (currentMarker.getPosition()) != "undefined")
		markers.push(currentMarker);
}

/**
 * 添加报警点，报警信息事件 param：alarmPoint 报警点 param：icon 报警图标 param：isCluterer 是否聚合图标
 * param：报警内容展示
 */
var alarmInfoWindow;
function addAlarmMarkerEvent(alarmPoint, icon, txt, isCluterer) {
	var alarmMarker = new BMap.Marker(alarmPoint,{
		icon : icon
	});// 新建标注
	map.addOverlay(alarmMarker);
	alarmMarker.setZIndex(10000);

	var opts = {
		enableAutoPan:true, //允许窗口打开自动移动
		enableCloseOnClick:true, //开启点击地图关闭信息窗口
		enableMessage:false //关闭短信发送功能
	};
	alarmInfoWindow = new BMap.InfoWindow(txt,opts
	// 信息窗口偏移设置
	);
	
	alarmMarker.addEventListener('mouseover', function() {
		//alarmInfoWindow.setPosition(alarmPoint);
		this.openInfoWindow(alarmInfoWindow);
	});
	alarmMarker.addEventListener('mouseout', function() {
		this.closeInfoWindow(alarmInfoWindow);
	});
	if (isCluterer) {
		markers.push(alarmMarker);
	}
}

/**
 * 地图类型改变触发事件
 * 
 * @param polyline
 */
function changeMapTypeEvent(polyline) {
	map.addEventListener("maptypechange", function() {
		// alert(map.getMapTypeId());
		if (map.getMapType() == BMAP_NORMAL_MAP) {
			polyline.setStrokeColor(mapBoundaryStyle.roadMapColor);
			polyline.setStrokeWeight(mapBoundaryStyle.weight);
		} else {
			polyline.setStrokeColor(mapBoundaryStyle.hybridColor);
			polyline.setStrokeWeight(mapBoundaryStyle.weight);
		}
	});
}

/**
 * 初始化地图
 * 
 * @param pointArr
 *            中心点对象
 * @param centerZoom
 *            zoom级别
 */
function resetMap(pointArr, centerZoom) {
	var centerLatLng = new BMap.Point(pointArr.lng, pointArr.lat);
	map.centerAndZoom(centerLatLng, zoom);
	map.setZoom(centerZoom);
	map.setMapType(BMAP_NORMAL_MAP);
}

/**
 * 设置marker的位置
 * 
 * @param currentMarker
 *            marker对象
 * @param currentPosition
 *            坐标(Point)
 */
function setMarkerPosition(currentMarker, currentPosition) {
	currentMarker.setPosition(currentPosition);
}

/**
 * 设置路线的的动态变化点
 * 
 * @param currentPolyline,
 *            currentPath(points:Array<Point>) 
 */
function setPolylinePath(currentPolyline, currentPath) {
	currentPolyline.setPath(currentPath);
}

/**
 * 设置弹出窗口的展示内容
 * 
 * @param currentInfoWindow,
 *            currentContent
 */
function setInfoWindowContent(currentInfoWindow, currentContent) {
	currentInfoWindow.setContent(currentContent);
}

/**
 * 处理json改为点坐标
 * 
 * @param point
 *            点对象
 * @returns BMap.Point 坐标
 */
function handlePoint(point) {
	return new BMap.Point(point.lng, point.lat);
}

/**
 * 路径查询功能，自助查找路径
 * 
 * @param oriPoint
 *            起点
 * @param desPoint
 *            终点
 * @param pointsArray
 *            路上必须经过的点数组
 * @readme points 路上必须经过的点转换后的数据 格式为[{location:new
 *         google.maps.LatLng(lat,lng)},{location:"或者具体地址"},......]
 */
function directSearch(oriPoint, desPoint, pointsArray) {
	var ori, des;
	if (typeof (oriPoint.lat) != "undefined"
			&& typeof (oriPoint.lng) != "undefined") {
		ori = handlePoint(oriPoint);
	} else {
		ori = oriPoint;
	}

	if (typeof (desPoint.lat) != "undefined"
			&& typeof (desPoint.lng) != "undefined") {
		des = handlePoint(desPoint);
	} else {
		des = desPoint;
	}

	var points = new Array();
	for ( var i = 0; i < pointsArray.length; i++) {
		points.push({
			location : new google.maps.LatLng(pointsArray[i].lat,
					pointsArray[i].lng),
			stopover : true
		});
	}
	
	var drivingOptions = {
		renderOptions:{map: map, autoViewport: true},
		onSearchComplete: function(results){
			if (driving.getStatus() == BMAP_STATUS_SUCCESS){
				// 获取第一条方案
				//var plan = results.getPlan(0);
				// 获取方案的驾车线路
				//var route = plan.getRoute(0);
			}else{
				alert("The way is bad......");
			}
		}
	};
	
	var driving = new BMap.DrivingRoute(map, drivingOptions);
	var options = {
		waypoints : points	
	};
	driving.search(ori,des,options);
}

/**
 * 计算总路程，单位是km
 * 
 * @param result
 *            调用事例：onSearchComplete: function(results){
 */
function computeTotalDistance(results) {
	var total = 0;
	var myroute = results.getPlan(0);
	total = myroute.getDistance(true);
	return total + ' km';
}

// 覆盖物
var overlays = new Array();
var drawingManager;
/**
 * 开启或关闭线的编辑功能
 * 
 * @param overlays
 * @param obj
 */
function enableOrDisableEdit(obj) {
	if (overlays.length > 0) {
		if (obj.name == "editRound") {
			// 开启线编辑功能
			overlays[0].enableEditing();
			obj.name = "viewRound";
		} else {
			// 关闭线编辑功能
			overlays[0].disableEditing();
			obj.name = "editRound";
		}
	}
}

/**
 * 开启线的编辑功能
 * 
 * @param overlays
 */
function enableEdit() {
	if (overlays.length > 0) {
		overlays[0].enableEditing();
	}
}

/**
 * 关闭线的编辑功能
 * 
 * @param overlays
 */
function disableEdit() {
	if (overlays.length > 0) {
		overlays[0].disableEditing();
	}
}

// 根据起终点，生成线路
function productLine() {
	if (overlays.length != 1) {
		return false;
	}
	var line = overlays[0];

	if (line instanceof google.maps.Polyline == false) {
		return false;
	}
	var drivingLine;// 生成后的路线
	// 得到点的值
	var points = overlays[0].getPath();

	// 路线服务
	var directionsDisplay = new google.maps.DirectionsRenderer();
	directionsDisplay.setMap(map);

	var directionsService = new google.maps.DirectionsService();

	var request = {
		origin : points.getAt(0),
		destination : points.getAt(points.length - 1),
		travelMode : google.maps.TravelMode.DRIVING
	};

	directionsService.route(request, function(result, status) {
		if (status == google.maps.DirectionsStatus.OK) {
			// directionsDisplay.setDirections(result);
			var myRoute = result.routes[0];
			// alert(myRoute.overview_path);
			for ( var i = 0; i < overlays.length; i++) {
				overlays[i].setMap(null);
			}
			overlays.length = 0;

			drivingLine = showPolyLineInMap(myRoute.overview_path, true,
					drawPolylineStyle);
			var polylineOption = {
				editable : true
			};
			drivingLine.setOptions(polylineOption);
			overlays.push(drivingLine);
		}
	});
}

function initDrawingManagerAndDriving(drawParams) {
	// 实例化鼠标绘制工具
	/*
	 * 设置当前的绘制模式，参数DrawingType，为5个可选常量: BMAP_DRAWING_MARKER 画点
	 * BMAP_DRAWING_CIRCLE 画圆 BMAP_DRAWING_POLYLINE 画线 BMAP_DRAWING_POLYGON 画多边形
	 * BMAP_DRAWING_RECTANGLE 画矩形
	 * 
	 * drawingManager = new BMapLib.DrawingManager(map, { isOpen: false,
	 * //是否开启绘制模式 enableDrawingTool: true, //是否显示工具栏 drawingToolOptions: {
	 * anchor: BMAP_ANCHOR_TOP_RIGHT, //位置 offset: new BMap.Size(5, 5), //偏离值
	 * drawingModes: [BMAP_DRAWING_POLYLINE,BMAP_DRAWING_POLYGON],
	 * //只显示画多边形和画线的工具 scale: 0.8 //工具栏缩放比例 }, polylineOptions: routesOption,
	 * //线的样式 polygonOptions: areaOption //多边形的样式 });
	 */
	/*
	 * DrawingManager 的 drawingMode 属性用于定义 DrawingManager 的初始绘图状态。该属性接受
	 * google.maps.drawing.OverlayType常量(Marker|Polygon|Polyline|Rectangle|Circle)，且默认为
	 * null（在此情况下启动 DrawingManager 时，光标会处于非绘图模式）。 DrawingManager 的
	 * drawingControl 属性用于定义地图上的绘图工具选择界面的可见性。该属性接受布尔值。 您还可以使用 DrawingManager 的
	 * drawingControlOptions 属性，定义控件的位置以及控件中应表示的叠加层的类型。 position
	 * 用于定义绘图控件在地图上的位置，且接受 google.maps.ControlPosition 常量。 drawingModes 是一组
	 * google.maps.drawing.OverlayType
	 * 常量，且用于定义绘图控件形状选择器中包含的叠加层类型。系统将始终显示手形图标，以便用户无需绘图即可与地图进行交互。
	 * 您可为每种叠加层类型都指定一组默认属性，以便定义首次创建相应叠加层时所采用的外观。这些属性可在叠加层的 {overlay}Options
	 * 属性（其中 {overlay} 表示叠加层的类型）中进行定义。例如，圆形的填充属性、笔触属性、zIndex 和可点击性可使用
	 * circleOptions 属性进行定义。如果已传递任何大小、位置或地图值，则系统会忽略这些默认属性。
	 */
	// 车辆行走预定区域样式
	var routesOption = {
		strokeColor : drawPolylineStyle.color,
		strokeWeight : drawPolylineStyle.weight,
		strokeOpacity : drawPolylineStyle.opacity
	};
	// 车辆行走预定区域样式
	var areaOption = {
		strokeColor : drawPolylineStyle.color,
		strokeWeight : drawPolylineStyle.weight,
		strokeOpacity : drawPolylineStyle.opacity,
		fillColor : drawPolylineStyle.fillColor,
		fillOpacity : drawPolylineStyle.fillOpacity
	};

	drawingManager = new google.maps.drawing.DrawingManager({
		// drawingMode: google.maps.drawing.OverlayType.MARKER,
		map : map,
		drawingControl : true,
		drawingControlOptions : {
			position : getDirection(drawParams.direct),
			drawingModes : [ google.maps.drawing.OverlayType.POLYLINE,
					google.maps.drawing.OverlayType.POLYGON ]
		// 只显示画多边形和画线的工具
		},
		polylineOptions : routesOption, // 线的样式
		polygonOptions : areaOption
	// 多边形的样式
	});
	// drawingManager.setMap(map);

	// 回调获得覆盖物信息
	var overlaycomplete = function(e) {
		if (overlays.length > 0) {
			clearOverlay(allOverlays);
		}
		overlays.push(e.overlay);

		if (e.type == google.maps.drawing.OverlayType.POLYLINE) {// 线

		} else if (e.type == google.maps.drawing.OverlayType.POLYGON) {// 多边形

		}
	};

	// 添加鼠标绘制工具监听事件，用于获取绘制结果
	google.maps.event.addListener(drawingManager, 'overlaycomplete',
			overlaycomplete);

}

/**
 * 自定义按钮控件
 * 
 * @param labelParams
 *            除了title、content、direct是常有量外，其余的均为可自行添加的属性，用在调用的事件中，可以自行设置 var
 *            labelParams = { title:"提示", content:"右上角", direct:"top_right",
 *            alarmValue:"Y", condition:"", buttonType:"back",
 *            pointArr:{lat:40.49631,lng:116.99787}, zoom:12, icon:"图标" };
 */
function HomeControl(labelParams) {
	// 定义一个控件类,即function
	function ZoomControl() {
		// 默认停靠位置和偏移量
		this.defaultAnchor = getDirection(labelParams.direct);
		this.defaultOffset = new BMap.Size(10, 10);
	}

	// 通过JavaScript的prototype属性继承于BMap.Control
	ZoomControl.prototype = new BMap.Control();

	// 创建一个DOM元素，创建个div元素作为控件的容器,并将其添加到地图容器中
	var controlDiv = document.createElement("div");
	controlDiv.index = 1;
	// 添加文字说明
	controlDiv.style.padding = HomeControlText.padding;
	// Set CSS for the control border.
	var controlUI = document.createElement('div');
	controlUI.style.backgroundColor = HomeControlText.backgroundColor;
	controlUI.style.borderStyle = HomeControlText.borderStyle;
	controlUI.style.borderWidth = HomeControlText.borderWidth;
	controlUI.style.cursor = HomeControlText.cursor;
	controlUI.style.textAlign = HomeControlText.textAlign;
	controlUI.title = labelParams.title;
	controlDiv.appendChild(controlUI);
	// Set CSS for the control interior.
	var controlText = document.createElement('div');
	controlText.style.fontFamily = HomeControlText.fontFamily;
	controlText.style.fontSize = HomeControlText.fontSize;
	controlText.style.paddingLeft = HomeControlText.paddingLeft;
	controlText.style.paddingRight = HomeControlText.paddingRight;
	controlText.innerHTML = labelParams.content;
	controlUI.appendChild(controlText);

	// 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
	// 在本方法中
	ZoomControl.prototype.initialize = function(map) {
		// 添加DOM元素到地图中
		map.getContainer().appendChild(controlDiv);
		// 将DOM元素返回
	};
	// 创建控件
	var myZoomCtrl = new ZoomControl();
	// 添加到地图当中
	map.addControl(myZoomCtrl);
	
	return controlDiv;
}

/**
 * label
 * 
 * @param map
 * @param latlng
 * @param labeledText
 * @param pixelOffset
 * @returns
 */
function LabeledText(map, point, labeledText, pixelOffset) {
	var opts = {
		position : point,// 指定文本标注所在的地理位置
		offset : pixelOffset || new BMap.Size(-30, 30) // 设置文本偏移量
	};
	var label = new BMap.Label("labeledText", opts); // 创建文本标注对象
	label.setStyle({
		color : LabelProperties.color,
		fontSize : LabelProperties.fontSize,
		height : LabeledText.style.height,
		lineHeight : LabeledText.style.lineHeight,
		fontFamily : LabeledText.style.fontFamily
	});
	map.addOverlay(label);
}

/**
 * 处理浮点数字，保证数字的正确性
 * 
 * @param obj
 */
function clearNoNum(obj) {
	// 先把非数字的都替换掉，除了数字和.
	obj.value = obj.value.replace(/[^\d.]/g, "");
	// 必须保证第一个为数字而不是.
	obj.value = obj.value.replace(/^\./g, "");
	// 保证只有出现一个.而没有多个.
	obj.value = obj.value.replace(/\.{2,}/g, ".");
	// 保证.只出现一次，而不能出现两次以上
	obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$",
			".");
}

/**
 * 替换字符串
 * 
 * @param str
 *            替换后的字符串
 * @param beStr
 *            被替换的字符串
 */
function strReplace(oristr, str, beStr) {
	var reg = new RegExp(beStr, "g"); // 创建正则RegExp对象
	return oristr.replace(reg, str);
}

/**
 * 将服务器端构建好的JSON数据转化为可用的js对象 param json
 */
function strToJson(json) {
	return eval("(" + json + ")");
}

// 地理坐标转换成世界坐标的方法：
// Projection.fromLatLngToPoint() 方法可将 LatLng 值转换为世界坐标。此方法用于在地图上放置叠加层（同时放置地图本身）。
// Projection.fromPointToLatLng() 方法可将世界坐标转换为 LatLng
// 值。此方法用于将地图上发生的事件（如点击）转换为地理坐标。

// 下面一句的意思是监听dom事件、打开浏览器加载地图，即去掉body元素的onload事件换成下面一句也可
// google.maps.event.addDomListener(window, "load", initialize);

var Testmarkers = [];
/**
 * 测试点聚合的效率
 * 
 * @param num
 * @param icon
 */
function testClusterer(num, icon) {
	google.maps.event.addListener(map, 'dblclick', function() {
		// 获取地图分界线
		var bounds = map.getBounds();
		// 获取地图的角
		var southWest = bounds.getSouthWest();
		var northEast = bounds.getNorthEast();
		// 计算地图从上到下的距离
		var latSpan = northEast.lat() - southWest.lat();
		// 计算地图从左到右的距离
		var lngSpan = northEast.lng() - southWest.lng();
		// 创建数据保存Marker对象
		// 创建一个循环
		for ( var i = 0; i < num; i++) {
			// 创建随机数
			var lat = southWest.lat() + latSpan * Math.random();
			var lng = southWest.lng() + lngSpan * Math.random();
			// var latlng = new google.maps.LatLng(lat, lng);
			var markerpoint = {
				lat : lat,
				lng : lng
			};
			// 创建Marker，注意它没有添加到地图上面
			/*
			 * var falsemarker = new google.maps.Marker({ position:latlng,
			 * icon:icon, map:map });
			 */
			var falsemarker = createMarker(markerpoint, icon);
			// 将Marker添加到数组中
			Testmarkers.push(falsemarker);
		}
		var mcOptions = {
			minimumClusterSize : 2,
			averageCenter : true,
			maxZoom : 20,
			gridSize : 100
		};
		new MarkerClusterer(map, Testmarkers, mcOptions);
	});
}
