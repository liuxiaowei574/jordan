var map;// 指定的地图容器，通常放在div中
var times = 1;
var routeAreaType = 0;

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
var thisMapType;// 地图类型,r为地图,h为卫星图
var mapTypeDirect = "top_right";
var zoomControlDirect = "top_left";
var panControlDirect = "top_left";
var streetViewDirect = "bottom_left";
var scaleDirect = "bottom_right";

var markerArr = new Array();//用于记录添加的marker
var polyLineArr = new Array();//用于记录添加的线
var polyGonArr = new Array();//用于记录添加的面
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
		thisMapType = google.maps.MapTypeId.HYBRID;
	if (params.mapType == 'r')
		thisMapType = google.maps.MapTypeId.ROADMAP;
	if(params.mapType != 'h' && params.mapType == 'r')
		thisMapType = google.maps.MapTypeId.ROADMAP;
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
	var centerLatLng = new google.maps.LatLng(pointArr.lat, pointArr.lng);
	handleParams(params);
	var mapOptions = {
		center : centerLatLng,
		zoom : zoom,
		// disableDefaultUI: true, //停用默认用户界面，去掉默认控件
		scrollwheel : isScroll,// 关闭滚轮缩放，默认为true开启
		disableDoubleClickZoom : isClickZoom,// 禁止双击缩放地图
		draggable : isDarggle,// 禁止拖拽地图功能，默认可以true
		panControl : true, // 方向盘平移
		
		mapTypeControl : isShowMapType, // 地图类型
		mapTypeControlOptions : {
			//mapTypeIds: [google.maps.MapTypeId.ROADMAP, google.maps.MapTypeId.HYBRID],
			style : google.maps.MapTypeControlStyle.DEFAULT,
			position : getDirection(mapTypeDirect)
		},
		panControlOptions : {
			position : getDirection(panControlDirect)
		},
		zoomControl : isZoomControl,// 缩放，默认true开启
		zoomControlOptions : {
			style : google.maps.ZoomControlStyle.LARGE,
			position : getDirection(zoomControlDirect)
		},
		scaleControl : isScale, // 比例
		streetViewControl : isStreetView, // 街景视图
		streetViewControlOptions : {
			position : getDirection(streetViewDirect)
		},
		// overviewMapControl: false, //总览图
		mapTypeId : thisMapType
	};
	map = new google.maps.Map(document.getElementById(mapId), mapOptions);
	 google.maps.event.addListener(map, 'zoom_changed',function() {
         if (map.getZoom() > zoom) map.setZoom(zoom);
      });
}
/**
 * 设置地图中心
 * 
 * @param homePointArr
 */
function setHomeCenter(homePointArr) {
	var homePoint = new google.maps.LatLng(homePointArr.lat, homePointArr.lng);
	map.setCenter(homePoint);
}

/**
 * 设置地图类型
 * 
 * @param mapType 地图类型
 */
function setMapType(mapType) {
	if (mapType == 'h') {
		map.setMapTypeId(google.maps.MapTypeId.HYBRID);
	} else if (mapType == 'r') {
		map.setMapTypeId(google.maps.MapTypeId.ROADMAP);
	} else {
		map.setMapTypeId(google.maps.MapTypeId.ROADMAP);
	}

}

/**
 * 根据传递的值判断方向 参数：左上、右上、左下、右下
 * 
 * @param direct
 * @returns
 */
function getDirection(direct) {
	var direction = google.maps.ControlPosition.TOP_LEFT;
	if (direct == 'top_left') {
		direction = google.maps.ControlPosition.TOP_LEFT;
	}
	if (direct == 'top_right') {
		direction = google.maps.ControlPosition.TOP_RIGHT;
	}
	if (direct == 'bottom_left') {
		direction = google.maps.ControlPosition.BOTTOM_LEFT;
	}
	if (direct == 'bottom_right') {
		direction = google.maps.ControlPosition.BOTTOM_RIGHT;
	}
	if (direct == 'left_top') {
		direction = google.maps.ControlPosition.LEFT_TOP;
	}
	if (direct == 'right_top') {
		direction = google.maps.ControlPosition.RIGHT_TOP;
	}
	if (direct == 'left_bottom') {
		direction = google.maps.ControlPosition.LEFT_BOTTOM;
	}
	if (direct == 'right_bottom') {
		direction = google.maps.ControlPosition.RIGHT_BOTTOM;
	}
	if (direct == 'top_center') {
		direction = google.maps.ControlPosition.TOP_CENTER;
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
function createMarker(location, iconSrc, titleContent,sourceContent) {
	var showMarker = new google.maps.Marker({
		position : new google.maps.LatLng(location.lat, location.lng),
		draggable : false, //不可拖动的
		icon : iconSrc,
		anchor: new google.maps.Point(0,6),
		flat : false, //如果为true则显示标记阴影
		//animation: google.maps.Animation.DROP,//使gif图有动画效果--google.maps.Animation.BOUNCE一直上下跳动的效果
		visible: true,
		title : titleContent,//滚动文本
		optimized: false,
		attribution:{source:sourceContent},
		map : map
	});
	return showMarker;
}

/**
 * 地图添加覆盖物方法,如果要确定是否加入清楚覆盖物中，可再加一个boolean参数isDeleted
 * 
 * @param location
 * @param iconSrc
 * @returns {google.maps.Marker}
 */
function createVehicelMarker(location, iconSrc, titleContent,sourceContent) {
	var image = new google.maps.MarkerImage(iconSrc,
		      // This marker is 20 pixels wide by 32 pixels tall.
		      new google.maps.Size(32,32),
		      // The origin for this image is 0,0.
		      new google.maps.Point(0,0),
		      // The anchor for this image is the base of the flagpole at 0,32.
		      new google.maps.Point(15,15));
	var showMarker = new google.maps.Marker({
		position : new google.maps.LatLng(location.lat, location.lng),
		draggable : false, //不可拖动的
		icon : image,
		//anchor: new google.maps.Point(0,6),
		flat : false, //如果为true则显示标记阴影
		//animation: google.maps.Animation.DROP,//使gif图有动画效果--google.maps.Animation.BOUNCE一直上下跳动的效果
		visible: true,
		title : titleContent,//滚动文本
		optimized: false,
		attribution:{source:sourceContent},
		map : map
	});
	return showMarker;
}
function createVehicleSVGMarker(location, iconSrc, titleContent,sourceContent) {
	var showMarker = new google.maps.Marker({
		position : new google.maps.LatLng(location.lat, location.lng),
		//draggable : true, //不可拖动的
		icon : iconSrc,
		map : map
	});
	return showMarker;
}
/*
 *  获取Marker的attribution的值
 *  @param Marker
 *  @returns sourceContent
 */
function getAttribution(marker){
	return marker.getAttribution().source||"";
}


/**
 * marker 定位
 * @param marker
 * @param location
 */
function setMarkerPosition(marker, location){
	var pos = new google.maps.LatLng(location.lat, location.lng);
	marker.setPosition(pos);
}

/**
 * marker 图标变换
 * @param marker
 * @param location
 */
function setMarkerIcon(marker, icon){
	marker.setIcon(icon);
}

/**
 * 设置infowindow内容
 * @param infowindow
 * @param content
 */
function setInfoWindowContent(infowindow, content){
	infowindow.setContent(content);
}

/**
 * 覆盖物显示顺序
 * @param overlay
 */
function setShowFront(overlay,zindex){
	overlay.setZIndex(zindex);
}

/**
 * 地图上展示覆盖物方法
 * @param overlays
 */
function showOverlays(overlays) {
	if (overlays) {
		for (i in overlays) {
			overlays[i].setMap(map);
		}
	}
}

/**
 * 地图上隐藏覆盖物方法
 * @param overlays
 */
function hiddenOverlays(overlays) {
	if (overlays) {
		for (i in overlays) {
			overlays[i].setMap(null);
		}
	}
}

/**
 * 移除叠加层覆盖物方法（删除,marker放入数组，相应数组的 length 设为 0）
 * @param overlay
 */
function clearOverlays(overlays) {
	if(overlays){
		for ( var i = 0; i < overlays.length; i++) {
			overlays[i].setMap(null);
		}
		overlays.length = 0;
	}
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
	var showMarker = new google.maps.Marker({
		position : location,
		draggable : false, 
		animation: google.maps.Animation.BOUNCE,//使gif图有动画效果
		icon : iconSrc,
		map : map
	});
	if (isFront) {
		showMarker.setZIndex(10000);
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
function markersClusterer(clustererMarkers, gridSize, markerclusterer) {
	// 清除掉已经存在的聚合对象
	clearMarkerClusterer(markerclusterer);
	var mcOptions = {
		minimumClusterSize : 2,
		averageCenter : true,
		maxZoom : 1200,
		gridSize : gridSize
	};
	// 创建一个MarkerClusterer对象，将marker数组对象传递给它，网格分组
	// MarkerClusterer使用的正确方法，三个参数，第一个是地图的引用，第二个是一个marker数组，第三个被选择的对象与文字mcOptions。只有第一个参数是必须的。
	/*
	 * gridSize {Number} 聚合计算时网格的像素大小，默认60 maxZoom {Number}
	 * 最大的聚合级别，大于该级别就不进行相应的聚合 minimumClusterSize {Number}
	 * 最小的聚合数量，小于该数量的不能成为一个聚合，默认为2 averageCenter {Boolean}
	 * 聚合点的落脚位置是否是所有聚合在内点的平均值，默认为否，落脚在聚合内的第一个点 styles {Array} 自定义聚合后的图标风格 var
	 * markers = [...]; // Create the markers you want to add and collect them
	 * into a array. var markerclusterer = new MarkerClusterer(map, markers,
	 * mcOptions);
	 */
	markerclusterer = new MarkerClusterer(map, clustererMarkers, mcOptions)
	return markerclusterer;
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
 * 移除Marker从聚合对象中
 * @param marker对象
 * @param opt_nodraw 是否重画聚合对象
 */
function removeMarkerByName(marker,/*opt_nodraw,*/markerclusterer){
	markerclusterer.removeMarker(marker/*,opt_nodraw*/);
}

/**
 * 添加Marker到聚合对象中
 * @param marker对象
 * @param opt_nodraw 是否重画聚合对象
 */
function addMarkerByName(marker,opt_nodraw,markerclusterer){
	markerclusterer.addMarker(marker,opt_nodraw);
}

/**
 * 跟着鼠标获取坐标
 * 
 * @returns {String}
 */
function getPointByClick() {
	var lat = map.getCenter().lat();
	var lng = map.getCenter().lng();
	google.maps.event.addListener(map, 'dblclick', function(event) {
		lat = event.latLng.lat();// 纬度
		lng = event.latLng.lng();// 经度
		// document.getElementById(showPointId).innerHTML=lng+','+lat;
		//alert(lat + "," + lng);
	});
	return "{lng:" + lng + ",lat:" + lat + "}";
}

/**
 * 控制地图的缩放级别事件
 * 
 * @param num
 */
function setZoomLevel(num) {
	if(num>20){
		map.setZoom(20);
	}else if(num<1){
		map.setZoom(1);
	}else{
		map.setZoom(num);
	}
}

/**
 * 控制地图的最大最小缩放级别
 * 
 * @param MinZoomLevel
 * @param MaxZoomLevel
 */
function setZoomMinLevel(MinZoomLevel, MaxZoomLevel) {
	// 控制地图的缩放级别
	google.maps.event.addListener(map, 'zoom_changed', function() {
		if (map.getZoom() < MinZoomLevel)
			map.setZoom(MinZoomLevel);
		if (map.getZoom() > MaxZoomLevel)
			map.setZoom(MaxZoomLevel);
	});
}

/**
 * 设置地图的自适应，使其展示全貌
 * 
 * @param points
 *            传递的是点
 * @returns
 */
function setViewPort(points) {
	var bounds = new google.maps.LatLngBounds();
	if (points) {
		for ( var i = 0; i < points.length; i++) {
			var point = points[i];
			if (point) {
				bounds.extend(point);
			}
		}
	}
	if (bounds)
		map.fitBounds(bounds);
	// if(bounds) map.panToBounds(bounds);
	// 下面是将覆盖物的中心设置为地图的中心
	// map.setCenter(bounds.getCenter());
}

/**
 * 设置地图的自适应，使其展示全貌
 * 
 * @param pointArray
 *            传递的是数组
 */
function setViewPortByArray(pointArray) {
	var bounds = new google.maps.LatLngBounds();

	var points = new Array();
	for ( var i = 0; i < pointArray.length; i++) {
		points
				.push(new google.maps.LatLng(pointArray[i].lat,
						pointArray[i].lng));
	}

	if (points) {
		for ( var i = 0; i < points.length; i++) {
			var point = points[i];
			if (point) {
				bounds.extend(point);
			}
		}
	}
	if (bounds)
		map.fitBounds(bounds);
	// if(bounds) map.panToBounds(bounds);
	// 下面是将覆盖物的中心设置为地图的中心
	// map.setCenter(bounds.getCenter());
}


/**
 * 更新一条线路
 * 
 * @param polyline 更新的路线
 * @param polylineArr 连线点的json数据集合
 */
function showUpdatePolyLine(polyline, polylineArr) {
	var pathArr = polyline.getPath();
	var pathPointArr = pathArr.getArray();
	
	if(typeof(pathPointArr)=="Array"){
		//for ( var i = 0; i < polylineArr.length; i++) {
			pathPointArr.push(new google.maps.LatLng(polylineArr[i].lat,
					polylineArr[i].lng));
		//}
	}else{
		pathPointArr.push(new google.maps.LatLng(polylineArr.lat,
				polylineArr.lng));
	}
	
	polyline.setPath(pathPointArr);
	return polyline;
}


/**
 * 创建多边形
 * 
 * @param polygonArr 连多边形点的json数据集合
 * @param isShow 是否显示在地图上
 */
var polyGon = null;
function showPolygonInMap(polygonArr, isShow, polygonStyle) {
	if(null!=polyLine){
		polyLine.setMap(null);
	}
	if(null!=polyGon){
		polyGon.setMap(null);
	}
	var polygonPointArr = new Array();
	for ( var i = 0; i < polygonArr.length; i++) {
		polygonPointArr.push(new google.maps.LatLng(polygonArr[i].lat,
				polygonArr[i].lng));
	// 区域的样式，solid或dashed。
	}
	polyGon = new google.maps.Polygon({
		paths : polygonPointArr,
		strokeColor : polygonStyle.color,
		strokeWeight : polygonStyle.weight,
		strokeOpacity : polygonStyle.opacity,
		fillColor : polygonStyle.fillColor,
		fillOpacity : polygonStyle.fillOpacity
	});
	GoogleUtil.getViewport(polygonPointArr);
	google.maps.event.addListener(polyGon, 'dblclick', function(event) {
		enableOrDisableEdit(polyGon,true);
	});
	google.maps.event.addListener(polyGon, 'dragend', function(event) {
		alert(event.target);
	});
	google.maps.event.addListener(map, 'click', function(event) {
		enableOrDisableEdit(polyGon,false);
	});
	
	if (isShow) {
		polyGon.setMap(map);
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
	var bounds = new google.maps.LatLngBounds();
	var coordinate = new google.maps.LatLng(pointArr.lat, pointArr.lng);
	var polygonPointArr = new Array();
	for ( var i = 0; i < polygonArr.length; i++) {
		polygonPointArr.push(new google.maps.LatLng(polygonArr[i].lat,
				polygonArr[i].lng));
	}
	var polygon = new google.maps.Polygon({
		paths : polygonPointArr
	});
	polygon.setMap(map);
	var allLineLength = polygon.getPaths().length;
	for ( var j = 0; j < allLineLength; j++) {
		var everyPath = polygon.getPaths().getAt(j);
		var polygonLength = everyPath.length;
		for ( var i = 0; i < polygonLength; i++) {
			bounds.extend(everyPath.getAt(i));
		}
	}

	return bounds.contains(coordinate);
}
function searchmap(address){  
    //先从输入框中取出要搜的地名  
   // var address=$("#check_box").val();  
    if(" "==address){  
         alert("请输入要定位的地名！");  
         return false;  
    }else{  
     geocoder = new google.maps.Geocoder(); //注意:还有一个全局的 var geocoder 对象  
     if(geocoder){  
         geocoder.geocode({'address': address }, function(results, status) {  
             if (status == google.maps.GeocoderStatus.OK) {  
                 for ( var index in results) {
                     var GeoCode = ((results[index].geometry.location).toString().replace(/[()]/g, '')).split(",",2);  
                     var lat = parseFloat(GeoCode[0]);//纬度  
                     var lng = parseFloat(GeoCode[1]);//经度  
                     var mylatlng = new google.maps.LatLng(lat, lng);      
                     map.setCenter(mylatlng);                                           
                     //对搜索到的这个点进行标注  
                                 var marker = new google.maps.Marker({  
                         map: map,  
                         position: mylatlng,  
                         title:address  
                     });  
                 //点击事件,下面的经度和纬度是我要使用的，点击的时候弹出层  
                                     google.maps.event.addListener(marker, 'click', function(){  
                           showLayer('add_button');  
                           $("#weidu_id").val(lat);  
                           $("#jingdu_id").val(lng);  
                         });  
                           
                     }
                } else {  
                    // alert("谷歌地图没有找到的原因是:" + status);  
                }  
                 
             });  
         }  
    }  
 }      
/**
 * 创建口岸图标
 * 
 * @param labelIconSrc
 */
function createLabelIcon(labelIconSrc) {
	var labelIcon = new google.maps.MarkerImage(labelIconSrc,
			new google.maps.Size(40, 50),// 大小
			new google.maps.Point(0, 0), new google.maps.Point(14, 40),// 锚点，偏移
			new google.maps.Size(40, 50));
	return labelIcon;
}

/**
 * 创建label
 * 
 * @param labelpoint
 * @param name
 */
function createLabel(labelpoint, name) {
   // var overlay = new MyMarker(map,{latlng:new google.maps.LatLng(labelpoint.lat, labelpoint.lng),image:"static/images/custom.png"});  
	return new LabeledText(new google.maps.LatLng(labelpoint.lat, labelpoint.lng), name, {x:-(name.length*2), y:5});
}

/**
 * 创建Marker以及对应的label标签
 * @param localPoint
 * @param portSrc
 * @param name
 */
/* 创建Marker以及对应的label标签 */  
function createMarkerAndLabel(localPoint, portSrc, name, sourceContent) {
	//createLabel(localPoint, name);
	return createMarker(localPoint, createLabelIcon(portSrc), name,sourceContent);
}

/**
 * 添加多条路线
 * 
 * @param roadPath
 */
function createmoreLineTrack(roadPath) {
	var yanshiRoadPoints = new Array();
	for ( var i = 0; i < roadPath.length; i++) {
		yanshiRoadPoints.push(new google.maps.LatLng(roadPath[i].lat,
				roadPath[i].lng));
	}
	GoogleUtil.tools.track.addmoreLineTrack(yanshiRoadPoints);
}


/**
 * 下面连续几个方法都是为了车辆轨迹回放调用准备 此方法：创建单车回放行驶轨迹
 * 
 * @param roadPath
 */
function createLineTrack(roadPath) {
	var yanshiRoadPoints = new Array();
	for ( var i = 0; i < roadPath.length; i++) {
		yanshiRoadPoints.push(new google.maps.LatLng(roadPath[i].lat,
				roadPath[i].lng));
	}
	GoogleUtil.tools.track.addLineTrack(yanshiRoadPoints);
}


/**
 * 创建回放行驶轨迹并展示详细信息
 * 
 * @param roadPath
 */
function createLineTrackShowDetailInfo(roadPath) {
	var yanshiRoadPoints = new Array();
	for ( var i = 0; i < roadPath.length; i++) {
		yanshiRoadPoints.push(new google.maps.LatLng(roadPath[i].latitude,
				roadPath[i].longitude));
	}
	GoogleUtil.tools.track.addLineTrack(yanshiRoadPoints,roadPath);
}

/**
 * 创建单车回放行驶轨迹并且车运行
 * @param roadPath，vehicleSrc
 */
function createLineAndPlay(roadPath,vehicleSrc) {
	var yanshiRoadPoints = new Array();
	for ( var i = 0; i < roadPath.length; i++) {
		yanshiRoadPoints.push(new google.maps.LatLng(roadPath[i].lat,
				roadPath[i].lng));
	}
	GoogleUtil.tools.track.addLineTrack(yanshiRoadPoints);
	GoogleUtil.tools.track.operate.play(createIcon(vehicleSrc));
}

/**
 * 创建车辆位置图标
 * @param vehicleSrc
 * @returns {google.maps.MarkerImage}
 */
function createIcon(vehicleSrc) {
	// 车辆图标
	var vehicleLocateIcon = new google.maps.MarkerImage(vehicleSrc,
			new google.maps.Size(32, 40),// 大小
			new google.maps.Point(0, 0), new google.maps.Point(16, 40),// 锚点，偏移
			new google.maps.Size(32, 40));
	return vehicleLocateIcon;
}

// 回放
function pathPlay(vehicleSrc,callback) {
	times = 1;
	GoogleUtil.tools.track.operate.play(createIcon(vehicleSrc),times,callback);
}
// 继续回放
function continuePlay(vehicleSrc,callback) {
	
	GoogleUtil.tools.track.operate.continuePlay(createIcon(vehicleSrc),times,callback);
}
// 暂停
function pathPause(callback) {
	GoogleUtil.tools.track.operate.pause(callback);
}
// 停止回放
function pathStop(vehicleSrc,callback) {
	GoogleUtil.tools.track.operate.stop(createIcon(vehicleSrc),times,callback);
}

//加速
function accelerate(vehicleSrc,callback) {
	times = times*1.15;
	GoogleUtil.tools.track.operate.accelerate(createIcon(vehicleSrc),times,callback)
}
//减速
function decelerate(vehicleSrc,callback) {
	times = times/1.15;
	GoogleUtil.tools.track.operate.decelerate(createIcon(vehicleSrc),times,callback);
}

//清除轨迹线条
function removeLineTrack() {
	GoogleUtil.tools.track.clearLineTrack();
}
//清除轨迹线条
function clearInterValTrackLine() {
	GoogleUtil.tools.track.operate.clearInterValTrack();
}

var pointsArray = new Array();
/**
 * 处理接收的坐标点二维数组转化为要使用的google坐标点二维数组
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
					.push(new google.maps.LatLng(data[i].lat, data[i].lng));
		}
	}
}

/**
 * 显示多条路线,要走的路线底图
 * 
 * @param pointsArrayData
 */
function showRoadLineInMap(pointsArrayData,lineStyle) {
	analyzePointArray(pointsArrayData);
	for ( var i = 0; i < pointsArray.length; i++) {
		new google.maps.Polyline({
			path : pointsArray[i],
			map : map,
			strokeColor : lineStyle.color,
			strokeWeight : lineStyle.weight,
			strokeOpacity : lineStyle.opacity
		});
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
function showInfoWindow(currentMarker, currentContent, isOpen) {
	// 创建信息窗口
	var infoWindow = new google.maps.InfoWindow({
		content : currentContent
	// 信息窗口偏移设置
	});
	google.maps.event.addListener(currentMarker, 'mouseover', function() {
		// 需要在开启一个infoWindow的时候关闭其余的infoWindow
		if (typeof (infowindows) != "undefined") {
			for ( var int = 0; int < infowindows.length; int++) {
				infowindows[int].close();
			}
		}
		infoWindow.open(map, currentMarker);
	});
//	google.maps.event.addListener(currentMarker, "mouseout", function() {
//		infoWindow.close();
//	});
	google.maps.event.addListener(currentMarker, 'click', function() {
		// 需要在开启一个infoWindow的时候关闭其余的infoWindow
		if (typeof (infowindows) != "undefined") {
			for ( var int = 0; int < infowindows.length; int++) {
				infowindows[int].close();
			}
		}
		infoWindow.open(map, currentMarker);
    });
	if(isOpen){
		infoWindow.open(map, currentMarker);
	}
	infowindows.push(infoWindow);
	return infoWindow;
}

function closeAllInfowindow(){
    if(infowindows.length>0){
        if (typeof (infowindows) != "undefined") {
            for ( var int = 0; int < infowindows.length; int++) {
                infowindows[int].close();
            }
        }
    }
       
}

var alarmInfowindows = [];// 定义报警信息窗口
/**
 * 添加车辆报警信息窗口的事件
 * 
 * @param currentContent
 *            展示内容
 * @param currentMarker
 *            基于覆盖物
 * @param isOpen
 *             是否直接开启
 */
function showAlarmInfoWindow(currentMarker, currentContent, isOpen,callback) {
	// 创建信息窗口
	var alarmInfoWindow = new google.maps.InfoWindow({
		content : currentContent,
		pixelOffset : new google.maps.Size(0, -0)
	// 信息窗口偏移设置
	});
	google.maps.event.addListener(currentMarker, 'mouseover', function() {
		// 需要在开启一个infoWindow的时候关闭其余的infoWindow
		if (typeof (alarmInfowindows) != "undefined") {
			for ( var int = 0; int < alarmInfowindows.length; int++) {
				alarmInfowindows[int].close();
			}
		}
		alarmInfoWindow.open(map, currentMarker);
		callback(currentMarker);
	});
//	google.maps.event.addListener(currentMarker, "mouseout", function() {
//		alarmInfoWindow.close();
//	});
	
	google.maps.event.addListener(currentMarker, 'click', function() {
		// 需要在开启一个infoWindow的时候关闭其余的infoWindow
		if (typeof (alarmInfowindows) != "undefined") {
			for ( var int = 0; int < alarmInfowindows.length; int++) {
				alarmInfowindows[int].close();
			}
		}
		alarmInfoWindow.open(map, currentMarker);
    });
	
	if(isOpen){
		alarmInfoWindow.open(map, currentMarker);
	}
	
	alarmInfowindows.push(alarmInfoWindow);
	return alarmInfoWindow;
}


var portInfowindows = [];// 定义口岸信息窗口
/**
 * 添加口岸信息窗口的事件
 * 
 * @param currentContent
 *            展示内容
 * @param currentMarker
 *            基于覆盖物
 */
function showPortInfoWindow(currentMarker, currentContent) {
	// 创建信息窗口
	var portInfoWindow = new google.maps.InfoWindow({
		content : currentContent,
		pixelOffset : new google.maps.Size(0, -0)
	// 信息窗口偏移设置
	});
	google.maps.event.addListener(currentMarker, 'mouseover', function() {
		// 需要在开启一个infoWindow的时候关闭其余的infoWindow
		if (typeof (portInfowindows) != "undefined") {
			for ( var int = 0; int < portInfowindows.length; int++) {
				portInfowindows[int].close();
			}
		}
		portInfoWindow.open(map, currentMarker);
	});
	google.maps.event.addListener(currentMarker, "mouseout", function() {
		portInfoWindow.close();
	});
	portInfowindows.push(portInfoWindow);
	return alarmInfoWindow;
}

/**
 * 添加事件监听器
 * 
 * @param obj
 *            事件对象
 * @param eventType
 *            事件类型
 * @param fn
 *            事件触发的方法
 */
function addEventForVehicle(obj, eventType, fn) {
	google.maps.event.addListener(obj, eventType, fn);
}

/**
 * 添加dom事件监听器
 * 
 * @param obj
 *            监听的事件对象
 * @param eventType
 *            监听的事件类型
 * @param fn
 *            监听事件触发的方法
 */
function addMyDomListener(obj, eventType, fn) {
	google.maps.event.addDomListener(obj, eventType, fn);
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
 * 添加报警点，报警信息事件 
 * @param alarmPoint 报警点 
 * @param icon 报警图标 
 * @param isCluterer 是否聚合图标
 * @param txt 报警内容展示
 */
var alarmInfoWindow;
function addAlarmMarkerEvent(alarmPoint, icon, txt, isCluterer) {
	var coordinate = new google.maps.LatLng(alarmPoint.lat, alarmPoint.lng);
	var alarmMarker = new google.maps.Marker({
		position : coordinate,
		draggable : false, 
		//animation: google.maps.Animation.DROP,//使gif图有动画效果
		icon : icon,
		map : map
	});// 新建标注
	alarmMarker.setZIndex(10000);

	alarmInfoWindow = new google.maps.InfoWindow({
		content : txt,
		pixelOffset : new google.maps.Size(0, 0)
	// 信息窗口偏移设置
	});

	google.maps.event.addListener(alarmMarker, 'mouseover', function() {
		alarmInfoWindow.setPosition(alarmPoint);
		alarmInfoWindow.open(map, alarmMarker);
	});
	google.maps.event.addListener(alarmMarker, 'mouseout', function() {
		alarmInfoWindow.close();
	});
	if (isCluterer) {
		isAddMarker(alarmMarker);
	}
}

/**
 * 地图类型改变触发事件
 * @param lineStyle
 * @param polyline
 */
function changeMapTypeEvent(polyline,lineStyle) {
	google.maps.event.addListener(map, "maptypeid_changed", function() {
		// alert(map.getMapTypeId());
		if (map.getMapTypeId() == google.maps.MapTypeId.ROADMAP) {
			polyline.setOptions({
				strokeColor : lineStyle.roadMapColor,
				strokeWeight : lineStyle.weight
			});
		} else {
			polyline.setOptions({
				strokeColor : lineStyle.hybridColor,
				strokeWeight : lineStyle.weight
			});
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
	var centerLatLng = new google.maps.LatLng(pointArr.lat, pointArr.lng);
	map.setCenter(centerLatLng);
	setZoomLevel(centerZoom);
	map.setMapTypeId(google.maps.MapTypeId.ROADMAP);
}

/**
 * 设置marker的位置
 * 
 * @param currentMarker
 *            marker对象
 * @param currentPosition
 *            坐标
 */
//function setMarkerPosition(currentMarker, currentPosition) {
//	currentMarker.setPosition(currentPosition);
//}

/**
 * 设置路线的的动态变化点
 * 
 * @param currentPolyline,
 *            currentPath
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
 * @returns google.maps.LatLng 坐标
 */
function handlePointByJson(point) {
	return new google.maps.LatLng(point.lat, point.lng);
}


function handlePoint(point) {
	return new google.maps.LatLng(point.lat(), point.lng());
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

var draggableOptions = {
	draggable: true
};
var flag = true;
var startMarker=null;
var endMarker = null;
var startIconSrc = "images/gis/yellow.png";
var endIconSrc = "images/gis/red.png";
var drivingLine = null;


var startPt = new google.maps.LatLng(40.4011162, 116.9162513);
var endPt = new google.maps.LatLng(40.36128620535971, 116.82321433920724);
function mapDirectSearch(){
	directSearch(startPt, endPt, null);
}
/**
 * 计算总路程，单位是km
 * 
 * @param result
 *            调用事例：computeTotalDistance(directionsDisplay.getDirections()); 
 *            var directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);
 */
function computeTotalDistance(result) {
	var total = 0;
	var myroute = result.routes[0];
	for ( var i = 0; i < myroute.legs.length; i++) {
		total += myroute.legs[i].distance.value;
	}
	total = total / 1000.0;
	return total + ' km';
}

// 覆盖物
var overlays = new Array();
var drawingManager;
var drawType;


/**
 * 开启线的编辑功能
 * @param overlays
 */
function enableEdit() {
	if (overlaysArray.length > 0) {
		var enableOptions = {
			editable : true
		};
		overlaysArray[0].setOptions(enableOptions);
//		drawingManager.setOptions({
//			drawingMode : google.maps.drawing.OverlayType.POLYLINE
//		});
	}
}

/**
 * 关闭线的编辑功能
 * @param overlays
 */
function disableEdit() {
	if (overlays.length > 0) {
		var disableOptions = {
			editable : false
		};
		overlays[0].setOptions(disableOptions);
	}
}

function drawManagerControlVisible(isShow){
	if(typeof(drawingManager)=="undefined")return;
	if(isShow){
		drawingManager.setMap(map);
	}else{
		drawingManager.setMap(null);
	}
}
function drawManagerVisible(flag){
    if(flag){
        directionsDisplay.setMap(map);
        drawingManager.setMap(map);
    }else{
    	if(null!=directionsDisplay&&typeof(directionsDisplay)!="undefined"){
    		directionsDisplay.setMap(null);
    	}
        if(null!=drawingManager&&typeof(drawingManager)!="undefined")
        {
        	drawingManager.setMap(null);
        }
    }
   
}
function directionsDisplayVisible(flag){
    if(flag){
        directionsDisplay.setMap(map);
    }else{
        if(null!=directionsDisplay&&typeof(directionsDisplay)!="undefined"){
            directionsDisplay.setMap(null);
        }
    }
   
}
function drawManagerVisible(flag){
    if(flag){
        directionsDisplay.setMap(map);
        drawingManager.setMap(map);
    }else{
        if(null!=directionsDisplay&&typeof(directionsDisplay)!="undefined"){
            directionsDisplay.setMap(null);
        }
        if(null!=drawingManager&&typeof(drawingManager)!="undefined")
        {
            drawingManager.setMap(null);
        }
    }
   
}
function initDrawingManagerAndDriving(drawParams,isShow,callback,drawType) {
	// 实例化鼠标绘制工具
	// 车辆行走预定区域样式
	var routesOption = {
		strokeColor : drawParams.darwStyle.color,
		strokeWeight : drawParams.darwStyle.weight,
		strokeOpacity : drawParams.darwStyle.opacity
	};
	// 车辆行走预定区域样式
	var areaOption = {
		editable: true, 
		strokeColor : drawParams.darwStyle.color,
		strokeWeight : drawParams.darwStyle.weight,
		strokeOpacity : drawParams.darwStyle.opacity,
		fillColor : drawParams.darwStyle.fillColor,
		fillOpacity : drawParams.darwStyle.fillOpacity
	};
	if(typeof(drawingManager)!="undefined"&&null!=drawingManager){
		drawingManager.setMap(null);
	}
	if(RouteAreaType==POLYLINE){
		
		drawingManager = new google.maps.drawing.DrawingManager({
			// drawingMode: google.maps.drawing.OverlayType.MARKER,
			drawingControl : isShow,
			drawingControlOptions : {
				position : getDirection(drawParams.direct),
				drawingModes : [ google.maps.drawing.OverlayType.POLYLINE]
			// 只显示画多边形和画线的工具
			},
			polylineOptions : routesOption, // 线的样式
		// 多边形的样式
		});
		drawingManager.setMap(map);
	}else if(RouteAreaType==POLYGON){
		drawingManager = new google.maps.drawing.DrawingManager({
			// drawingMode: google.maps.drawing.OverlayType.MARKER,
			drawingControl : isShow,
			drawingControlOptions : {
				position : getDirection(drawParams.direct),
				drawingModes : [ 
						google.maps.drawing.OverlayType.POLYGON,
						google.maps.drawing.OverlayType.RECTANGLE
						//,google.maps.drawing.OverlayType.CIRCLE
						]
			// 只显示画多边形和画线的工具
			},
			polylineOptions : routesOption, // 线的样式
			polygonOptions : areaOption
//			//设置图形显示样式  
//		    circleOptions: {  
//		      fillColor: '#ffff00',  
//		      fillOpacity: 1,  
//		      strokeWeight: 5,  
//		      clickable: false,  
//		      editable: true,  
//		      zIndex: 1  
//		    }, 
		// 多边形的样式
		});
		drawingManager.setMap(map);
	}
	
	

	// 回调获得覆盖物信息
	var overlaycomplete = function(e) {
		drawingManager.setOptions({
			drawingMode : null
		});
		  //$("#routeAreaList").removeClass("none");
	     // $("#panelList").addClass("none");
		//drawingManager.setMap(null);
		if (overlaysArray.length > 0) {
			clearOverlays(overlaysArray);
		}
		if(overlays.length>0){
			clearOverlays(overlays);
		}
		overlaysArray.push(e.overlay);
		//enableEdit();
		if (e.type == google.maps.drawing.OverlayType.POLYLINE) {// 线
		    clearOverlays(lenArr);
			drawType = "POLYLINE";
			//根据绘制图形获取paths坐标点
			 var paths = e.overlay.getPath();
			 var pathMid = new Array();
			 for(var i=1;i<=paths.length-2;i++){
				 var pointmid = paths.getArray()[i];
				 pathMid.push(pointmid);
			 }
			 
			 directSearchForPlanRoute(paths.getArray()[0],paths.getArray()[paths.length-1],pathMid,"",callback);
		} else if (e.type == google.maps.drawing.OverlayType.POLYGON) {// 多边形;
			 var paths = e.overlay.getPath();
			 var route = new Array();
			 for(var i=0;i<paths.length;i++){
				 var pointmid = paths.getArray()[i];
				 route.push(pointmid);
			 }
			 var jsonPoint = getPlanAreaArray(route);
			    drawType = "POLYGON";
			    callback(jsonPoint);
			    var array= paths.getArray();
			    google.maps.event.addListener( e.overlay,'changed',showinfo);
			    showLonLat(array);
			    google.maps.event.addListener(paths, 'set_at', function() {
			        var arr = paths.getArray();
			            showLonLat(arr);
			      });
			   
			      google.maps.event.addListener(paths, 'insert_at', function() {
			          var arr = paths.getArray();
                      showLonLat(arr);
			      });
		}else{
		    clearOverlays(lenArr);
		}
	};
	function showinfo(){
	    alert("11");
	}
	
	
	
    
	// 添加鼠标绘制工具监听事件，用于获取绘制结果
	google.maps.event.addListener(drawingManager, 'overlaycomplete',
			overlaycomplete);
	google.maps.event.addListener(drawingManager, 'circlecomplete',
			circlecomplete);
	
}
var lenArr = [];
function showLonLat(array){
    clearOverlays(lenArr);
    for(var i=0;i<array.length;i++){
        if(i==array.length-1){
            var point1 = array[i];
            var point2 = array[0];
            var len = 0;
            if(point1&&point2){
                 len = getDistanceByLnglat(point1.lng(),point1.lat(),point2.lng(),point2.lat());
            }
             var ptMid = {};
             ptMid.lng = (point1.lng()+point2.lng())/2;
             ptMid.lat = (point1.lat()+point2.lat())/2;
            var lenLabel= createLabel(ptMid,len+"m");
            lenArr.push(lenLabel);
        }else{
            var point1 = array[i];
            var point2 = array[i+1];
            var len = 0;
            if(point1&&point2){
                 len = getDistanceByLnglat(point1.lng(),point1.lat(),point2.lng(),point2.lat());
            }
             var ptMid = {};
             ptMid.lng = (point1.lng()+point2.lng())/2;
             ptMid.lat = (point1.lat()+point2.lat())/2;
             var lenLabel = createLabel(ptMid,len+"m");
             lenArr.push(lenLabel);
        }
       
    }
}
function circlecomplete(){
    //alert("111")
}
function Rad(d){
    return d * Math.PI / 180.0;
}
function getDistanceByLnglat(lng1,lat1,lng2,lat2){
    var radLat1 = Rad(lat1);
    var radLat2 = Rad(lat2);
    var a = radLat1 - radLat2;
    var b = Rad(lng1) - Rad(lng2);
    var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + 
        Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
    s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
    s = Math.round(s * 10000) / 10000;
    return s;
}
function clearPolygonLength(){
    clearOverlays(lenArr);
}

/**
 * 获取画图后获得的坐标
 * 返回一个数组
 */
function getDrawPath(){
	var pointArray = new Array();
	 if(overlays.length !=1){
        return pointArray;
     }
	 var points = overlays[0].getPath();
     //检索绘图的各个点
     for (var i = 0; i < points.length; i++) {
         //var p = points[i];
         var p = points.getAt(i);
         pointArray.push({lng:p.lng(),lat:p.lat()});
     }
	return pointArray;
}

/**
 * 获取画的图的类型
 * 返回String
 */
function getDrawType(){
	return drawType;
}

/**
 * 自定义按钮控件
 * @param labelParams
 *     除了title、content、direct是常有量外，其余的可自行添加属性，用来完善控件
 *     var labelParams = { 
 *            title:"提示", 
 *            content:"右上角", 
 *            direct:"top_right"
 *      };
 */



/**
 * 测距
 * @param panel用于展示路径详细信息 若为空则不展示
 * @param callback回调函数 为空则不回调
 */
var clickCount = 0;//用于记录点击次数
var measureClickCount=flag;//判断测距开启关闭
var animateMarker = null;

function measurePath(panel,callback){
	var circleParam = {
		    color:"#000000",
		    opacity:0.5,
		    radius:15
		};
	var clickCount = 0;//用于记录点击次数
    var startPt = null;
    var endPt = null;
	var arrayPaths = new Array();
	var click_lis =
		 google.maps.event.addListener(map, 'click', function (e) {
			 clickCount = clickCount +1;
				if(clickCount<=2){
					if(clickCount==1){
						if(null!=startMarker){
							startMarker.setMap(null);
						}
						startPt = new google.maps.LatLng(e.latLng.lat(), e.latLng.lng());
						var startLoction = {lat:e.latLng.lat(), lng:e.latLng.lng()};
						
						var startCircle = createCircle(startLoction,circleParam);
						//startAnimateMarker = createMarkerByPoint(startPt, null, true);//(startPt, startIconSrc);
						reverseGeocoder(startLoction);
						if(overlays.length>0){
							overlays[0].setMap(null);
						}
						 //overlays.push(startCircle);
						//overlays.push(startAnimateMarker);
						//startMarker.setOptions(enableOptions);
					}else if(clickCount==2){
						if(null!=endMarker){
							endMarker.setMap(null);
						}
						var endLoction = {lat:e.latLng.lat(), lng:e.latLng.lng()};
						endPt = new google.maps.LatLng(e.latLng.lat(), e.latLng.lng());
						endAnimateMarker = createMarkerByPoint(endPt,null,true); 
						var endCircle = createCircle(endLoction,circleParam);
						reverseGeocoder(endLoction);
						if(overlays.length>0){
							overlays[0].setMap(null);
						}
						//overlays.push(endCircle);
						overlays.push(endAnimateMarker);
						//endMarker = createMarkerByPoint(endPt, endIconSrc,true);
						//endMarker.setOptions(enableOptions);
					}
				}else{
					clickCount = 0;
					startPt=null;
					endPt=null;
					google.maps.event.removeListener(click_lis);
				}
				if(null!=startPt&&null!=endPt){
					directSearch(startPt, endPt, null,panel,callback);
				}
			});
	
	var dblclick_lis=
		 google.maps.event.addListener(map, 'dblclick', function (e) {
			 google.maps.event.removeListener(click_lis);
		});
}

function clearDirectionsDisplay(){
	directionsDisplay.setMap(null);
}
function createCircle(location,param,callback){
	var postion = new google.maps.LatLng(location.lat, location.lng);
	var cityCircle = new google.maps.Circle({
	      strokeColor: '#000000',
	      strokeOpacity: 0.8,
	      strokeWeight: 1,
	      fillColor: param.color||"#000000",
	      fillOpacity:param.opacity||.2,
	      map: map,
	      editable : true,
	      center: postion,
	      radius: param.radius||10
	    });
//	var circle =  new google.maps.Marker({
//	      position: postion,
//	      map: map,
//	      icon: {
//	        path: google.maps.SymbolPath.CIRCLE,
//	        fillColor: param.color||"#fffff",
//	        fillOpacity: param.opacity||.2,
//	        strokeColor: 'black',
//	        strokeWeight: .5,
//	        scale: param.radius||5
//	      }
//	    });
	var dblclick_lis=
        google.maps.event.addListener(cityCircle, 'radius_changed', function () {
        	var circleParam = {};
        	circleParam.radius = this.getRadius();
        	circleParam.centerLat = this.getCenter().lat();
        	circleParam.centerLng = this.getCenter().lng();
        	callback(circleParam);
       });
	overlays.push(cityCircle);
	return cityCircle;
}


/**
 * 创建一条线路,并清除线路
 * 
 * @param polylineArr 连线点的json数据集合
 * @param isShow 是否显示在地图上
 * @param styleParam - strokeColor、strokeWeight、strokeOpacity
 */
var polyLine = null;
function showPolyLineInMap(polylineArr, isShow, styleParam) {
	var pathPointArr = new Array();
	for ( var i = 0; i < polylineArr.length; i++) {
		pathPointArr.push(new google.maps.LatLng(polylineArr[i].lat,
				polylineArr[i].lng));
	}
	// 区域的样式，solid或dashed。
	var polyLine = new google.maps.Polyline({
		path : pathPointArr,
		strokeColor : styleParam.color,
		strokeWeight : styleParam.weight,
		strokeOpacity : styleParam.opacity,
		zIndex:   styleParam.zIndex||1
	});
	if (isShow) {
		polyLine.setMap(map);
	}
	var paths = polyLine.getPath();
	google.maps.event.addListener(paths, 'set_at', function(num){
	    var arr = [];
	    arr.push(paths.getArray()[num]);
	    directSearchForPlanRoute(paths.getArray()[0],paths.getArray()[paths.length-1],arr,"",getDrawRouteArea)
	  });
	return polyLine;
}

/**
 * 创建一条轨迹回放路线
 * 
 * @param polylineArr 连线点的json数据集合
 * @param isShow 是否显示在地图上
 * @param styleParam - strokeColor、strokeWeight、strokeOpacity
 * @param isTrackling
 */
function creatCyclingInMap(polylineArr, isShow, styleParam) {
	GoogleUtil.tools.track.clearLineTrack();
	if(null!=polyGon){
		polyGon.setMap(null);
	}
	if(null!=polyLine){
		polyLine.setMap(null);
	}
	for ( var i = 0; i < overlays.length; i++) {
		overlays[i].setMap(null);
	}
	overlays.length = 0;
//	var pathPointArr = new Array();
//	for ( var i = 0; i < polylineArr.length; i++) {
//		pathPointArr.push(new google.maps.LatLng(polylineArr[i].lat,
//				polylineArr[i].lng));
//	}
	
	
	GoogleUtil.getViewport(polylineArr);
	GoogleUtil.tools.track.addLineTrack(polylineArr);

	return polyLine;
}

function createSVGMarker(location, iconSrc, titleContent) {
	   var icon = {
		    path: "M 731.543 348.852 h -27.41 V 93.735 c 0 -5.04 -2.865 -9.64 -7.405 -11.847 c 0 0 -15.48 -17.325 -31.985 -25.78 c -24.055 -12.32 -70.95 -23.78 -72.965 -23.78 H 455.096 c -1.575 0 -32.057 0 -56.377 10.34 c -19.697 9.61 -28.215 12.87 -54.592 30.745 c -5.105 1.955 -8.507 6.837 -8.507 12.32 v 263.119 h -27.412 v 26.657 h 27.412 v 583.959 c 0 3.75 1.637 7.34 4.442 9.865 l 39.322 34.815 c 2.392 2.14 5.512 3.31 8.727 3.31 h 251.937 c 2.96 0 5.825 -1.01 8.16 -2.84 l 50.915 -40.2 c 3.185 -2.49 5.01 -6.3 5.01 -10.335 V 375.509 h 27.41 V 348.852 Z M 422.139 318.637 l 204.859 -0.63 l 44.775 40.077 l -47.705 47.01 H 418.704 l -48.017 -46.38 l 51.485 -40.045 L 422.139 318.637 Z M 361.959 593.858 h 41.022 l 0.095 142.065 l -41.117 39.765 v -181.8 V 593.858 Z M 402.889 426.424 l 0.092 141.095 h -40.99 V 386.914 L 402.889 426.424 Z M 421.476 754.768 l 201.082 3.4 l 42.88 38.47 l -75.46 46 h -136.9 l -81.605 -39.54 l 49.972 -48.33 H 421.476 Z M 640.868 739.198 v -145.34 h 36.955 v 178.49 l -36.925 -33.15 H 640.868 Z M 677.788 567.518 h -36.955 V 425.541 l 36.99 -36.422 v 178.43 L 677.788 567.518 Z M 677.788 101.96 v 226.159 l -33.87 -30.34 L 589.633 59.045 l 88.155 42.882 V 101.96 Z M 457.521 58.635 h 2.552 l -54.287 239.302 l -43.795 34.09 V 94.807 l 95.562 -36.172 H 457.521 Z",
		    fillColor: '#FF0000',
		    fillOpacity: .6,
		    //anchor: new google.maps.Point(location.lat,location.lng),
		    strokeWeight: 0,
		    scale:0.05
		}
	   var point = new google.maps.LatLng(location.lat,location.lng);
		var showMarker = new google.maps.Marker({
		    position: point,
		    map: map,
		    draggable: false,
		    icon: icon
		});

	
	var message="hello"  ;
	showInfoWindow(showMarker,message);
	
	return showMarker;
}

/**
 * 将svg图转为icon使用
 * @param svgPath  svg图的path
 * @param svgScale svg取大小
 * @param svgFillColor svg颜色
 * @param svgStrokeColor svg颜色
 * @param svgRotation svg方向
 */
function createSvgIcon(svgPath,svgScale,svgRotation,svgFillColor,svgStrokeColor){
	var icon = {
	    path: svgPath,
	    fillColor: svgFillColor,
	    fillOpacity: 1,
	    anchor: new google.maps.Point(0,10),
	    strokeColor: svgStrokeColor,
	    strokeWeight: 2,
	    strokeOpacity: 1,
	    scale: svgScale,
	    rotation:parseInt(svgRotation)
	}
	return icon;
}


/**
 * 开启或关闭线的编辑功能
 * 
 * @param overlays
 * @param obj
 */
function enableOrDisableEdit(overlay,isEnable) {
	if (overlay) {
		if (isEnable) {
			// 开启线编辑功能
//			var enableOptions = {
//				editable : true
//			};
			overlay.setOptions(enableOptions);
			
		} else {
			// 关闭线编辑功能
			var disableOptions = {
				editable : false
			};
			overlay.setOptions(disableOptions);
		}
	}
}

/**
 * 自定义LabeledText
 * @param latlng 单点数组，位置
 * @param labeledText 内容
 * @param pixelOffset 格式为{x:*,y:*}数组
 * @returns
 */
function LabeledText(latlng, labeledText, pixelOffset,image) {
   // this.image_ = image;  //设置图标的图片
	this.latlng_ = latlng;
	this.labeledText_ = labeledText;
	this.map_ = map;
	this.div_ = null;
	if(pixelOffset){
		this.pixelOffset_ = new google.maps.Size(pixelOffset.x, pixelOffset.y);
	}else{
		new google.maps.Size(-30, 30);
	}
	this.setMap(map);
	return this.labeledText_;
}
LabeledText.prototype = new google.maps.OverlayView();

LabeledText.prototype.onAdd = function() {
	var div = document.createElement("div");
	/*
	 * var span = document.createElement("span"); var text =
	 * document.createTextNode(this.labeledText_); span.appendChild(text);
	 * div.appendChild(span);
	 */
	// div.style.border = "1px solid #5B5BFF";
	div.style.borderStyle = "solid";
	div.style.borderWidth = "1px";
	div.style.padding = "2px";
	div.style.color = "#5B5BFF";
	div.style.background = "#ffffff";
	div.style.fontSize = "12px";
	div.style.fontWeight = "bold";
	div.style.textAlign = "center";
	div.style.position = "absolute";// 无此则定位不起作用
	div.innerHTML = "" + this.labeledText_ + "";
	
//	 // Create an IMG element and attach it to the DIV.  
//    var img = document.createElement("img"); //创建图片元素
//    img.src = getRootPath()+this.image_;  
//    img.style.width = "100%";   
//    img.style.height = "100%";  
//    div.appendChild(img);
	this.div_ = div;
	var panes = this.getPanes();
	panes.overlayLayer.appendChild(div);
};

LabeledText.prototype.draw = function() {
	var overlayProjection = this.getProjection();
	var sw = overlayProjection.fromLatLngToDivPixel(this.latlng_);
	var div = this.div_;
	div.style.left = (sw.x + this.pixelOffset_.width) + 'px';
	div.style.top = (sw.y + this.pixelOffset_.height) + 'px';
	// div.style.width = '80px';
	// div.style.height = '20px';
};

LabeledText.prototype.onRemove = function() {
	this.div_.parentNode.removeChild(this.div_);
	this.div_ = null;
};


/***************自定义叠加层，可作为站点显示在地图上******************/
function MyMarker(map, options) {   
  // Now initialize all properties.   
  this.latlng = options.latlng; //设置图标的位置
  this.image_ = options.image;  //设置图标的图片
  this.labelText = options.labelText || '标记';
  this.labelClass = options.labelClass || 'shadow';//设置文字的样式
  this.clickFun = options.clickFun ;//注册点击事件
//    this.labelOffset = options.labelOffset || new google.maps.Size(8, -33);
  this.map_ = map;    

  this.div_ = null;   
  // Explicitly call setMap() on this overlay   
  this.setMap(map); 
} 
MyMarker.prototype = new google.maps.OverlayView();
//初始化图标
MyMarker.prototype.onAdd = function() {    
    // Note: an overlay's receipt of onAdd() indicates that  
    // the map's panes are now available for attaching   
    // the overlay to the map via the DOM.    
    // Create the DIV and set some basic attributes.  
    var div = document.createElement('DIV'); //创建存放图片和文字的div
    div.style.border = "none";   
    div.style.borderWidth = "0px";   
    div.style.position = "absolute";
    div.style.cursor = "hand";
    div.onclick = this.clickFun ||function(){};//注册click事件，没有定义就为空函数
    // Create an IMG element and attach it to the DIV.  
    var img = document.createElement("img"); //创建图片元素
    img.src = this.image_;  
    img.style.width = "20px";   
    img.style.height = "10px";  
    //初始化文字标签
    var label = document.createElement('div');//创建文字标签
        label.className = this.labelClass;
        label.innerHTML = this.labelText;
        label.style.position = 'absolute';
        label.style.width = '200px';
    //  label.style.fontWeight = "bold";
        label.style.textAlign = 'left';
        label.style.padding = "2px";
        label.style.fontSize = "10px";
    //  label.style.fontFamily = "Courier New";

    div.appendChild(img);   
    div.appendChild(label);   
    
    this.div_ = div;   
    // We add an overlay to a map via one of the map's panes.  
    // We'll add this overlay to the overlayImage pane.  
    var panes = this.getPanes();  
    panes.overlayLayer.appendChild(div); 
  }
  //绘制图标，主要用于控制图标的位置
MyMarker.prototype.draw = function() {    
      // Size and position the overlay. We use a southwest and northeast   
      // position of the overlay to peg it to the correct position and size.  
      // We need to retrieve the projection from this overlay to do this.  
      var overlayProjection = this.getProjection();   
      // Retrieve the southwest and northeast coordinates of this overlay  
      // in latlngs and convert them to pixels coordinates.  
      // We'll use these coordinates to resize the DIV.  
      var position = overlayProjection.fromLatLngToDivPixel(this.latlng);   //将地理坐标转换成屏幕坐标
    //  var ne = overlayProjection.fromLatLngToDivPixel(this.bounds_.getNorthEast());    
      // Resize the image's DIV to fit the indicated dimensions.   
      var div = this.div_;  
      div.style.left =position.x-5 + 'px';  
      div.style.top  =position.y-5 + 'px';  
    //控制图标的大小
      div.style.width = '10px';  
      div.style.height ='10px';
  }
MyMarker.prototype.onRemove = function() {   
      this.div_.parentNode.removeChild(this.div_);   
      this.div_ = null; 
  }

  //Note that the visibility property must be a string enclosed in quotes 
MyMarker.prototype.hide = function() {   
      if (this.div_) {     
        this.div_.style.visibility = "hidden";   
      } 
  }  
MyMarker.prototype.show = function() {   
      if (this.div_) {     
      this.div_.style.visibility = "visible";   
      } 
  } 
  //显示或隐藏图标
MyMarker.prototype.toggle = function() {   
    if (this.div_) {     
        if (this.div_.style.visibility == "hidden") {       
        this.show();     
        } else {       
        this.hide();     
        }   
    }
  }  


var directionsDisplay = new google.maps.DirectionsRenderer({ 'draggable': true });
var directionsService = new google.maps.DirectionsService();

/**
 * 为路径或区域规划提供路径分析
 * @param oriPoint
 * @param desPoint
 * @param pointsArray
 * @param panel
 * @param callback
 */
function directSearchForPlanRoute(oriPoint, desPoint, pointsArray,panel,callback) {
	directionsService  = new google.maps.DirectionsService();
	var ori, des;
	if (typeof (oriPoint.lat()) != "undefined"
			&& typeof (oriPoint.lng()) != "undefined") {
		ori = handlePoint(oriPoint);
	} else {
		ori = oriPoint;
	}

	if (typeof (desPoint.lat()) != "undefined"
			&& typeof (desPoint.lng()) != "undefined") {
		des = handlePoint(desPoint);
	} else {
		des = desPoint;
	}

	var points = new Array();
	if(pointsArray!=null&&pointsArray.length>0){
		for ( var i = 0; i < pointsArray.length; i++) {
			points.push({
				location : new google.maps.LatLng(pointsArray[i].lat(),
						pointsArray[i].lng()),
				stopover : true
			});
		}
	}
	// 路线服务
	directionsDisplay.setMap(map);
	directionsDisplay.setPanel(document.getElementById(panel));
	google.maps.event.addListener(directionsDisplay, 'directions_changed', function () {
		var obj = directionsDisplay;
		var route = obj.directions.routes[0];
		//var jsonPoint = getPlanRouteArray(obj.directions.request);//myRoute
		var jsonPoint = getPlanRouteArray(route);
		showRouteInmap(route);
		 var distance = route.legs[0].distance.text;
         var duration = route.legs[0].duration.text;
         $("#routeCost").val(convertToMinutes(duration));
         var indexspace = distance.lastIndexOf(' ');
	        distance = distance.substring(0,indexspace);
         $("#routeDistanceFormatted").val(distance);
         $("#routeDistance").val(formatFloat(distance));
		if(callback==null){
			
		}else{
			callback(jsonPoint);
		}
	});
	// 自助寻找路径
	var request = {
		origin : ori,
		destination : des,
		optimizeWaypoints : true,// 降低路线的总体成本
		waypoints : points,
		travelMode : google.maps.TravelMode.DRIVING
	};
	directionsService.route(request, function(response, status) {
		if (status == google.maps.DirectionsStatus.OK) {
//			for ( var i = 0; i < overlaysArray.length; i++) {
//				overlaysArray[i].setMap(null);
//			}
//			overlays.length = 0;
			clearOverlays(overlaysArray);
			directionsDisplay.setDirections(response);
			var myRoute = response.routes[0];
			var jsonPoint = getPlanRouteArray(myRoute);//myRoute
			showRouteInmap(myRoute);
			//enableEdit();
            var distance = response.routes[0].legs[0].distance.text;
            var duration = response.routes[0].legs[0].duration.text;
            $("#routeCost").val(convertToMinutes(duration));
            var indexspace = distance.lastIndexOf(' ');
	        distance = distance.substring(0,indexspace);
	        $("#routeDistanceFormatted").val(distance);
	        $("#routeDistance").val(formatFloat(distance));
            callback(myRoute);
			if(callback==null){
				
			}else{
				callback(jsonPoint);
			}
		} else {
		    bootbox.alert("The Way Is Bad......");
			//alert("The way is bad......");
		}
	});
}

/**
 * 时间转为分钟
 */
function convertToMinutes(time) {
    if (!time) {
        return 0;
    }
    //所有支持语言的相关单位，来源谷歌地图
    var originMinute = ['分钟,分', 'min,mins', 'قيقة']; //中文、英文、阿拉伯语
    var originHour = ['小时,时', 'hour,hours', 'اعة'];
    var originDay = ['天', 'day,days', 'يوم '];

    var unitMinute = unitHour = unitDay = [];
    originMinute.forEach(function(value, index) {
        unitMinute = unitMinute.concat(value.split(/\s*,\s*/));
    });
    originHour.forEach(function(value, index) {
        unitHour = unitHour.concat(value.split(/\s*,\s*/));
    });
    originDay.forEach(function(value, index) {
        unitDay = unitDay.concat(value.split(/\s*,\s*/));
    });
    var patternMinute = '(' + unitMinute.join('|') + ')';
    var patternHour = '(' + unitHour.join('|') + ')';
    var patternDay = '(' + unitDay.join('|') + ')';
    var minutes = 0;
    if (new RegExp('(\\d*)\\s*' + patternDay).test(time)) {
        minutes += Number(new RegExp('(\\d*)\\s*' + patternDay).exec(time)[1]) * 24 * 60;
    }
    if (new RegExp('(\\d*)\\s*' + patternHour).test(time)) {
        minutes += Number(new RegExp('(\\d*)\\s*' + patternHour).exec(time)[1]) * 60;
    }
    if (new RegExp('(\\d*)\\s*' + patternMinute).test(time)) {
        minutes += Number(new RegExp('(\\d*)\\s*' + patternMinute).exec(time)[1]);
    }
    return minutes;
}
//console.log(convertToMinutes('1day 2hours 2minutes'));


function saveGps(jsonPoint){
    var Url = getRootPath()
    + "monitorroutearea/simulateVehicleTrack.action?routeAreaPtCol="
    + jsonPoint;
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
    
}
})
}
function showRouteInmap(route){
	clearOverlays(overlaysArray);
	var drawPolylineStyle = {
			"color" : bufferColor||"#66FF00",
			"weight" : bufferWidth ||20,
			"opacity" : 0.5,
			"zIndex":-1
		}
	var routeArr  = createLineArr(route);
	setViewPortByArray(routeArr);
	var planRoute = showPolyLineInMap (routeArr,true,drawPolylineStyle);
	overlaysArray.push(planRoute);
}

function createLineArr(myRoute){
	var LineArr = [];
	for (var i = 0; i < myRoute.overview_path.length; i++) {
		LineArr.push({
			lat : myRoute.overview_path[i].lat(),
			lng : myRoute.overview_path[i].lng(),
		});
	}
	return LineArr;
}
/**
 * 路径计算
 * @param oriPoint //起点坐标
 * @param desPoint  //终点坐标
 * @param pointsArray //中间点集合
 * @param panel  //面板
 * @param callback //回调函数
 */
function directSearch(oriPoint, desPoint, pointsArray,panel,callback) {
	
	var ori, des;
	if (typeof (oriPoint.lat()) != "undefined"
			&& typeof (oriPoint.lng()) != "undefined") {
		ori = handlePoint(oriPoint);
	} else {
		ori = oriPoint;
	}

	if (typeof (desPoint.lat()) != "undefined"
			&& typeof (desPoint.lng()) != "undefined") {
		des = handlePoint(desPoint);
	} else {
		des = desPoint;
	}

	var points = new Array();
	if(pointsArray!=null&&pointsArray.length>0){
		for ( var i = 0; i < pointsArray.length; i++) {
			points.push({
				location : new google.maps.LatLng(pointsArray[i].lat(),
						pointsArray[i].lng()),
				stopover : true
			});
		}
	}
	// 路线服务
	directionsDisplay.setMap(map);
	directionsDisplay.setPanel(document.getElementById(panel));
	// 自助寻找路径
	var request = {
		origin : ori,
		destination : des,
		optimizeWaypoints : true,// 降低路线的总体成本
		waypoints : points,
		travelMode : google.maps.TravelMode.DRIVING
	};
	directionsService.route(request, function(response, status) {
		if (status == google.maps.DirectionsStatus.OK) {
			for ( var i = 0; i < overlays.length; i++) {
				overlays[i].setMap(null);
			}
			overlays.length = 0;
			directionsDisplay.setDirections(response);
			var myRoute = response.routes[0];
			var distance = response.routes[0].legs[0].distance.text;
	        var duration = response.routes[0].legs[0].duration.text;
	        var distDuration={};
	        distDuration.distance = distance;
	        distDuration.duration = duration;
	        callback(myRoute);
		} else {
			alert("The way is bad......");
		}
	});
}
/**
 * 根据经纬度获取实地名称
 * @param location
 */
function reverseGeocoder(location){
	var icon = "images/geocode.png";
	var geocoder = new google.maps.Geocoder;
	var infowindow = new google.maps.InfoWindow;
	  geocoder.geocode({'location': location}, function(results, status) {
		    if (status === google.maps.GeocoderStatus.OK) {
		      if (results[1]) {
		        //map.setZoom(11);
		        var marker = new google.maps.Marker({
		          position: location,
		          map: map,
		          icon:icon
		        });
		        overlays.push(marker);
		    	var latLng = new google.maps.LatLng(location.lat,location.lng) 
		        infowindow.setContent(results[1].formatted_address);
		        infowindow.open(map, marker);
		       // marker.setMap(null);
		      } else {
		        window.alert('No results found');
		      }
		    } else {
		      window.alert('Geocoder failed due to: ' + status);
		    }
		  });
}
 /**
 * Google事件回调
 * @param eventType
 * @param callback
 */
function  eventCallBack(eventType,callback){
	google.maps.event.addListener(map,eventType, function(e) {
		callback(e);
	});
}


/**
 * 获取规划路径集合
 * @param myRoute
 * @returns {String}
 */
function getEditRouteArray(overlay){
	if(!overlay) {
		return "";
	}
	var jsonPoint = "";
	for(var i=0;i<overlay.getPath().length;i++){
		var point = overlay.getPath().getArray()[i];
		var str = "{\"lat\":\""+point.lat()+"\",\"lng\":\""+point.lng()+"\"},";
		jsonPoint+=str;
	}
	jsonPoint = jsonPoint.substring(0,jsonPoint.length-1)
	jsonPoint = "["+jsonPoint+"]";
	return jsonPoint;
}


function getEditRouteArrayByDirection(overlay){
	var jsonPoint = "";
	for(var i=0;i<overlay.getPath().length;i++){
		var point = overlay.getPath().getArray()[i];
		var str = "{\"lat\":\""+point.lat()+"\",\"lng\":\""+point.lng()+"\"},";
		jsonPoint+=str;
	}
	jsonPoint = jsonPoint.substring(0,jsonPoint.length-1)
	jsonPoint = "["+jsonPoint+"]";
	return jsonPoint;
}
/**
 * 获取规划路径集合
 * @param myRoute
 * @returns {String}
 */
//function getPlanRouteArray(request){
//	debugger;
//	var jsonPoint = "";
//	var routearr = [];
//	var wayPts = request.waypoints;
//	routearr.push(request.origin);
//	for ( var index in wayPts) {
//		var waypoint = wayPts[index];
//		routearr.push(waypoint.location)
//	}
//	routearr.push(request.destination);
//	for(var i=0;i<routearr.length;i++){
//		var point = routearr[i];
//		var str = "{\"lat\":\""+point.lat()+"\",\"lng\":\""+point.lng()+"\"},";
//		jsonPoint+=str;
//	}
//	jsonPoint = jsonPoint.substring(0,jsonPoint.length-1)
//	jsonPoint = "["+jsonPoint+"]";
//	console.log(jsonPoint);
//	return jsonPoint;
//}


function getPlanRouteArray(myRoute){
	var jsonPoint = "";
	for(var i=0;i<myRoute.overview_path.length;i++){
		var point = myRoute.overview_path[i];
		var str = "{\"lat\":\""+point.lat()+"\",\"lng\":\""+point.lng()+"\"},";
		jsonPoint+=str;
	}
	jsonPoint = jsonPoint.substring(0,jsonPoint.length-1)
	jsonPoint = "["+jsonPoint+"]";
	return jsonPoint;
}


/**
 * 获取规划区域集合
 * @param myRoute
 * @returns {String}
 */
function getPlanAreaArray(myRoute){
	var jsonPoint = "";
	for(var i=0;i<myRoute.length;i++){
		var point = myRoute[i];
		var str = "{\"lat\":\""+point.lat()+"\",\"lng\":\""+point.lng()+"\"},";
		jsonPoint+=str;
	}
	jsonPoint = jsonPoint.substring(0,jsonPoint.length-1)
	jsonPoint = "["+jsonPoint+"]";
	return jsonPoint;
}




function ShowInfoBox(trackingMaker,currentContent){
	 var myOptions = {
	            content: currentContent
	            ,disableAutoPan: false
	            ,maxWidth: 0
	            ,pixelOffset: new google.maps.Size(10, 10)
	            ,zIndex: null
	            ,boxStyle: {
	                background: ""
	                ,opacity: 1
	                   // ,width: "280px"
	               }
	               ,closeBoxMargin: "10px 2px 2px 2px"
	               // ,closeBoxURL: ""
	               ,infoBoxClearance: new google.maps.Size(1, 1)
	               ,isHidden: false
	               ,pane: "floatPane"
	               ,enableEventPropagation: false
	           };
	           var ib = null;
	           google.maps.event.addListener(trackingMaker, "click", function (e) {
	               ib = new InfoBox(myOptions);
	               ib.open(map, this);
	           });
//	              google.maps.event.addListener(trackingMaker, "mouseout", function (e) {
//	               if(ib!=null){
//	                   ib.close(map, this);
//	               }
//	           });
}



/** 
 * 判断点距折线的最短距离 
 * @param {Point} point 点对象 
 * @param {Polyline} polyline 折线对象 
 * @returns {Boolean} 点在折线上返回true,否则返回false 
 */  
var minLength = 0;
function ptToPolylineLength (point, polyline) {  
    var minflag = true;
     //检查类型  
//     if (!(point instanceof BMap.Point) ||  
//          !(polyline instanceof BMap.Polyline)) {  
//         return false;  
//     }  

//     //首先判断点是否在线的外包矩形内，如果在，则进一步判断，否则返回false  
//     var lineBounds = polyline.getBounds();  
//     if (!this.isPointInRect(point, lineBounds)) {  
//         return minLength;  
//     }  

     //判断点是否在线段上，设点为Q，线段为P1P2 ，  
     //判断点Q在该线段上的依据是：( Q - P1 ) × ( P2 - P1 ) = 0，且 Q 在以 P1，P2为对角顶点的矩形内  
    // var pts = polyline.getPath();  
     for (var i = 0; i < polyline.length - 1; i++) {  
         var curPt = polyline[i];  
         var nextPt = polyline[i + 1];  
         //首先判断point是否在curPt和nextPt之间，即：此判断该点是否在该线段的外包矩形内  
//         if (point.lng >= Math.min(curPt.lng, nextPt.lng) && point.lng <= Math.max(curPt.lng, nextPt.lng) &&  
//              point.lat >= Math.min(curPt.lat, nextPt.lat) && point.lat <= Math.max(curPt.lat, nextPt.lat)) {  
             //判断点是否在直线上公式  
         var precisionPre = getDistanceByLnglat(curPt.lng,curPt.lat,point.lng,point.lat);
         var precisionNext = getDistanceByLnglat(nextPt.lng,nextPt.lat,point.lng,point.lat);
         if(precisionPre>precisionNext){
             precision = precisionNext
         }else{
             precision = precisionPre;
         }
            // var precision = (curPt.lng - point.lng) * (nextPt.lat - point.lat) - (nextPt.lng - point.lng) * (curPt.lat - point.lat); 
             if(minflag){
                 minLength = precision;  
                 minflag = false;
             }else{
                 if(precision<minLength){
                     minLength = precision;  
                 }
             }
            
//             if (precision < 2e-10 && precision > -2e-10) {//实质判断是否接近0  
//                 return true;  
//             }  
       //  }  
     }  
     return minLength+"";  
 }  
/**
 * 将格式化的数值转为浮点数。如123,456.78 -> 123456.78
 * @param s
 * @returns
 */
function formatFloat(s) {
	return parseFloat(s.replace(/[^\d\.]/g, ""));
}