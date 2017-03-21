<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="mapType"><fmt:message key="MapType"/></c:set>
var pathdir = window.location.pathname.substring(1);
var webdir = pathdir == '' ? '' : pathdir.substring(0, pathdir.indexOf('/'));
var webroot = window.location.protocol + '//' + window.location.host + '/' + webdir + '/';
var commonMapType = "${mapType}";
if (commonMapType == "google") {
	document.write('<script type="text/javascript" src="http://ditu.google.cn/maps/api/js?key=AIzaSyB26TtMhWyMQ1VkqnqUZkFrZKi7qbkW4Go&sensor=false&libraries=drawing&language=en-US"><' + '/script>');
	/*http://maps.google.com/maps/api/js?key=AIzaSyB26TtMhWyMQ1VkqnqUZkFrZKi7qbkW4Go&sensor=false&libraries=drawing&language=en-US*/
	/*https://maps.googleapis.com/maps/api/js?key=AIzaSyB26TtMhWyMQ1VkqnqUZkFrZKi7qbkW4Go&sensor=false&libraries=drawing&language=en-US*/
	document.write('<script type="text/javascript" src="' + webroot + '/gis/google/js/GoogleUtil.js"><' + '/script>');
	var script = '<script type="text/javascript" src="' + webroot + '/gis/google/js/markerclusterer';
	if (document.location.search.indexOf('compiled') !== -1) {
		script += '_compiled';
	}
	script += '.js"><' + '/script>';
	document.write(script);
	document.write('<script type="text/javascript" src="' + webroot + '/gis/google/js/google.js"><' + '/script>');
	document.write('<script type="text/javascript" src="' + webroot + '/gis/google/js/portDistance.js"><' + '/script>');
} else if (commonMapType == "baidu") {
	document.write('<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=dNunWpXR8N0UcE2eDDtVGKzv"><' + '/script>');
	document.write('<script type="text/javascript" src="' + webroot + '/gis/baidu/js/TextIconOverlay_min.js"><' + '/script>');
	document.write('<script type="text/javascript" src="' + webroot + '/gis/baidu/js/MarkerClusterer_min.js"><' + '/script>');
	document.write('<script type="text/javascript" src="' + webroot + '/gis/baidu/js/DrawingManager.js"><' + '/script>');
	document.write('<script type="text/javascript" src="' + webroot + '/gis/baidu/js/EventWrapper.js"><' + '/script>');
	var script = '<script type="text/javascript" src="' + webroot + '/gis/baidu/js/LuShu';
	if (document.location.search.indexOf('min') !== -1) {
		script += '_min';
	}
	script += '.js"><' + '/script>';
	document.write(script);
	document.write('<script type="text/javascript" src="' + webroot + '/gis/baidu/js/baidu.js"><' + '/script>');
} else if (commonMapType == "arcgis") {
	document.write('<link rel="stylesheet" href="http://js.arcgis.com/3.12/esri/css/esri.css">');
    document.write('<script src="http://js.arcgis.com/3.12/"><' + '/script>');
    document.write('<script type="text/javascript" src="' + webroot + '/gis/arcgis/js/arcgis.js"><' + '/script>');
}

/*********--------------------1.地图基本功能的添加----------------------************/
/**
 * 初始化地图设置展示
 * @param localPoint
 * @param params
 */
function GisInitialize(localPoint,params){
	initialize(localPoint, params);
}

/**
 * 设置地图中心
 * @param localPoint 中心点
 */
function GisSetHomeCenter(localPoint){
	 setHomeCenter(localPoint);
}

/**
 * 设置地图类型
 * @param mapType 地图类型
 */
function GisSetMapType(mapType){
	setMapType(mapType);
}

/**
 * 设置最佳视野
 * @param pointArr 传递的是数组
 */
function GisSetViewPortByArray(pointArr){
	setViewPortByArray(pointArr);
}

/**
 * 控制地图的缩放级别事件 
 * @param num
 */
function GisSetZoomLevel(num){
    setZoomLevel(num);
}

/**
 * 地图级别改变时控制地图的最大最小缩放级别
 * @param MinZoomLevel
 * @param MaxZoomLevel
 */
function GisSetZoomMinLevel(MinZoomLevel, MaxZoomLevel) {
    setZoomMinLevel(MinZoomLevel, MaxZoomLevel);
}

/**
 * 重置地图中心和级别，并且设置为卫星地图
 * @param localPoint 中心点
 * @param centerZoom zoom级别
 */
function GisResetMap(localPoint, centerZoom) {
    resetMap(localPoint, centerZoom);
}

function GisDrawManagerVisible(flag){
    drawManagerVisible(flag);
}
/**
 * 添加地图事件并返回
 */
 function GisEventCallBack(eventType,callback){
	 eventCallBack(eventType,callback);
 }
/**
 * 鼠标双击获取坐标
 * @returns {String} 格式为：{lng:**,lat:**}
 */
function GisGetPointByClick(){
    return getPointByClick();
}

/**
 * 添加覆盖物Marker方法
 * @param localPoint
 * @param iconSrc
 * @returns Marker
 */
function GisCreateMarker(localPoint, iconSrc, titleContent, sourceContent){
	return createMarker(localPoint, iconSrc, titleContent, sourceContent);
}

function GisCreateVehicleSVGMarker(localPoint, iconSrc, titleContent, sourceContent){
	return createVehicleSVGMarker(localPoint, iconSrc, titleContent, sourceContent);
}

/**
 * 规划线路显隐
 */
function GisdirectionsDisplayVisible(flag){
    directionsDisplayVisible(flag);
}

function GisCreateVehicelMarker(localPoint, iconSrc, titleContent, sourceContent){
	return createVehicelMarker(localPoint, iconSrc, titleContent, sourceContent);
}
/*
 *  获取Marker的attribution的值
 *  @param Marker
 *  @returns sourceContent
 */
function GisGetAttribution(marker){
	return getAttribution(marker);
}

/**
 * 覆盖物显示顺序
 * @param overlay
 */
function GisSetShowFront(overlay,zindex){
	setShowFront(overlay,zindex);
}

/**
 * 创建一条线路
 * @param pointArr 连线点的json数据集合
 * @param isShow boolean类型，是否显示在地图上
 * @param lineStyle - strokeColor、strokeWeight、strokeOpacity
 */
function GisShowPolyLineInMap(pointArr, isShow, lineStyle) {
	return showPolyLineInMap(pointArr, isShow, lineStyle);
}

/**
 * 在原有线路的基础上更新线路
 * @param polyline 更新的路线
 * @param pointArr 连线点的json数据数组
 */
function GisUpdatePolyLine(polyline, pointArr) {
	return showUpdatePolyLine(polyline, pointArr);
}

/**
 * 显示多条路线,要走的路线底图
 * @param pointsArrayData 多条线的点数组
 * @param lineStyle 线的样式
 */
function GisShowRoadLineInMap(pointsArrData,lineStyle) {
    showRoadLineInMap(pointsArrData,lineStyle);
}

/**
 * 地图类型改变触发事件，改变地图地图上的线路样式也要变化
 * @param lineStyle
 * @param polyline
 */
function GisChangeMapTypeEvent(polyline,lineStyle) {
    changeMapTypeEvent(polyline,lineStyle);
}

/**
 * 创建多边形
 * @param pointArr 连多边形点的json数据数组
 * @param isShow 是否显示在地图上
 * @param polygonStyle 多边形的样式
 */
function GisShowPolygonInMap(pointArr, isShow, polygonStyle) {
	return showPolygonInMap(pointArr, isShow, polygonStyle);
}

/**
 * 判断点是否在多边形内
 * @param localPoint 需要判断的点
 * @param pointArr 生成多边形的点数组
 * @return 在true，不在false
 */
function GisIsWithinPolygon(localPoint, pointArr) {
	return isWithinPolygon(localPoint, pointArr);
}

/**
 * 创建Marker以及对应的label标签
 * @param localPoint 坐标点
 * @param portSrc 图标
 * @param name label内容
 */
function GisCreateMarkerAndLabel(localPoint, portSrc, name, sourceContent) {
	return createMarkerAndLabel(localPoint, portSrc, name, sourceContent);
}

function GisCreateLabel(localPoint, name) {
	return createLabel(localPoint, name);
}
/**
 * json转点
 */
function GisHandlePointByJson(loction){
	return handlePointByJson(loction);
}

/**
 * 判断点到直线的距离
 */
function GisptToPolylineLength(point,polyline){
   return ptToPolylineLength(point,polyline)
}

function GisClearPolygonLength(){
    clearPolygonLength();
}
/**
 * 创建圆元素
 * @param position 圆心位置
 * @param param 展示参数   radius--半径   opacity--透明度
 */
function GisCreateCircle(position,param,callback){
	return createCircle(position,param,callback);
}
/**
 * 添加车辆信息窗口的事件
 * @param currentContent 展示内容
 * @param currentMarker 基于覆盖物
 */
function GisShowInfoWindow(currentMarker, currentContent, isOpen) {
	return showInfoWindow(currentMarker, currentContent, isOpen);
}
function GisCloseAllInfowindow(){
    closeAllInfowindow();
}

/**
 * 添加车辆报警信息窗口的事件
 * @param currentContent 展示内容
 * @param currentMarker 基于覆盖物
 */
function GisShowAlarmInfoWindow(currentMarker, currentContent, isOpen, callback) {
	return showAlarmInfoWindow(currentMarker, currentContent, isOpen, callback);
}

/**
 * 添加口岸信息窗口的事件
 * @param currentContent 展示内容
 * @param currentMarker 基于覆盖物
 */
function GisShowPortInfoWindow(currentMarker, currentContent) {
	return showPortInfoWindow(currentMarker, currentContent);
}

/**
 * 添加具体对象事件监听器
 * @param obj 事件对象
 * @param eventType 事件类型
 * @param fn 事件触发的方法
 */
function GisAddEventForVehicle(obj, eventType, fn) {
	addEventForVehicle(obj, eventType, fn);
}

/**
 * 添加dom事件监听器
 * @param obj 监听的事件对象
 * @param eventType 监听的事件类型
 * @param fn 监听事件触发的方法
 */
function GisAddMyDomListener(obj, eventType, fn) {
	addMyDomListener(obj, eventType, fn);
}

/**
 * 添加报警点，报警信息事件 
 * @param localPoint  报警点 
 * @param icon 报警图标 
 * @param isCluterer 是否聚合图标
 * @param txt 报警内容展示
 */
function GisAlarmMarkerEvent(localPoint , icon, txt, isCluterer) {
	addAlarmMarkerEvent(localPoint , icon, txt, isCluterer);
}

/**
 * 地图上展示覆盖物方法
 * @param overlays
 */
function GisShowOverlays(overlays) {
	showOverlays(overlays);
}

/**
 * 地图上隐藏覆盖物方法
 * @param overlays
 */
function GisHiddenOverlays(overlays) {
    hiddenOverlays(overlays);
}

/**
 * 地图上清除覆盖物方法
 * @param overlays
 */
function GisClearOverlays(overlays) {
    clearOverlays(overlays);
}

/**
 * 路径查询功能，自助查找路径
 * @param oriPoint 起点
 * @param desPoint 终点
 * @param pointArr 路上必须经过的点数组
 * @param panel 返回结果要显示的面板
 * @param callback 回调函数
 */
function GisDirectSearch(oriPoint, desPoint, pointArr,panel,callback) {
	directSearch(oriPoint, desPoint, pointArr,panel,callback);
}


/**
 * marker 定位
 * @param marker
 * @param location
 */
function GisSetMarkerPosition(marker, location){
	setMarkerPosition(marker, location);
}

/**
 * marker 图标变换
 * @param marker
 * @param location
 */
function GisSetMarkerIcon(marker, icon){
	setMarkerIcon(marker, icon);
}

/**
 * 设置infowindow内容
 * @param infowindow
 * @param content
 */
function GisSetInfoWindowContent(infowindow, content){
	setInfoWindowContent(infowindow, content);
}

/**
 * 将svg图转为icon使用
 */
function GisCreateSvgIcon(svgPath,svgScale,svgRotation,svgFillColor,svgStrokeColor){
	return createSvgIcon(svgPath,svgScale,svgRotation,svgFillColor,svgStrokeColor);
}

/*********--------------------2.marker聚合功能的添加----------------------************/
/**
 * 点聚合对象-网格分组 
 * @param clustererMarkers-分组的markers
 * @param gridSize 聚合计算时网格的像素大小
 */
function GisMarkersClusterer(clustererMarkers, gridSize, markerclusterer){
	return markersClusterer(clustererMarkers, gridSize, markerclusterer);
}

/**
 * 清除聚合对象markerclusterer
 * @param markerclusterer聚合对象
 * @returns {String}
 */
function GisClearMarkerClusterer(markerclusterer){
	clearMarkerClusterer(markerclusterer);
}
 
 /**
  * 移除Marker从聚合对象中
  * @param marker对象
  */
 function GisRemoveMarkerByName(marker,markerclusterer){
	 removeMarkerByName(marker,markerclusterer);
 }

/*********--------------------3.轨迹回放功能的添加，注意依托性----------------------************/

/**
 * 创建单车回放行驶轨迹并且车立即运行
 * @param pointArr 连接回放路径的点数组
 * @param vehicleSrc 车图标
 */
function GisCreateLineAndPlay(pointArr,vehicleSrc) {
	createLineAndPlay(pointArr,vehicleSrc);
}

/**
 * 创建单车回放行驶轨迹，下面连续几个方法对车的运行暂停等都必须依托本方法画线
 * @param pointArr
 */
function GisCreateMoreLineTrack(pointArr) {
	createmoreLineTrack(pointArr);
}

/**
 * 创建单车回放行驶轨迹，下面连续几个方法对车的运行暂停等都必须依托本方法画线
 * @param pointArr
 */
function GisCreateLineTrack(pointArr) {
	createLineTrack(pointArr);
}

/**
 * 轨迹回放并显示详细信息,points为从数据库查询到的详细信息
 */
 function GisCreateLineTrackShowDetailInfo(points){
	 createLineTrackShowDetailInfo(points);
 }
/**
 * 回放
 * @param vehicleSrc 回放图标
 */
function GisPathPlay(vehicleSrc,callback) {
	pathPlay(vehicleSrc,callback);
}
/**
 * 继续回放
 * @param vehicleSrc 回放图标
 */
function GisContinuePlay(vehicleSrc,callback) {
	continuePlay(vehicleSrc,callback);
}
/**
 * 暂停
 */
function GisPathPause(callback) {
	pathPause(callback);
}
/**
 * 停止回放
 */
function GisPathStop(vehicleSrc,callback) {
	pathStop(vehicleSrc,callback);
}
/**
 * 加速
 */
function GisPathAccelaratePaly(vehicleSrc,callback) {
	accelerate(vehicleSrc,callback);
}
/**
 * 减速
 */
function GisPathDecelaratePaly(vehicleSrc,callback) {
	decelerate(vehicleSrc,callback);
}

/**
 * 清除轨迹线条
 */
function GisClearLineTrack() {
	removeLineTrack();
}

function GisClearInterValTrack(){
	clearInterValTrackLine();
}

/*********--------------------4.画图功能的添加，注意依托性----------------------************/

/**
 * 实例化画图工具，并塑造overlays，下面的生成路线以及线的编辑功能都需要在此实例化基础上操作
 * @params drawParams：direct位置方位 darwStyle样式
 */
function GisInitDrawingManagerAndDriving(drawParams,isShow,callback,RouteAreaType){
	initDrawingManagerAndDriving(drawParams,isShow,callback,RouteAreaType);
}

function GisDirectSearchForPlanRoute(oriPoint, desPoint, pointsArray,panel,callback){
	directSearchForPlanRoute(oriPoint, desPoint, pointsArray,panel,callback)
}

/**
 * 根据起终点，生成线路
 * @param overlays 依托实例化方法生成
 */
function GisProductLine(){
	productLine();
}

/**
 * 获取画图后获得的坐标，返回一个数组 格式为[{lat:*,lng:*},{lat:*,lng:*}]
 * @param overlays 依托实例化方法生成
 */
function GisGetDrawPath(){
	return getDrawPath();
}

/**
 * 获取画的图的类型,返回String：线为POLYLINE，区域为POLYGON
 * @param overlays 依托实例化方法生成
 */
function GisGetDrawType(){
	return getDrawType();
}

function GisGetEditRouteArray(overlay){
	return getEditRouteArray(overlay);
}

/**
 * 开启线的编辑功能
 * @param overlays 依托实例化方法生成
 */
function GisEnableEdit() {
	enableEdit();
}

/**
 * 关闭线的编辑功能
 * @param overlays 依托实例化方法生成
 */
function GisDisableEdit() {
	disableEdit();
}

/*********--------------------5.自定义控件的添加，注意依托性----------------------************/
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
function GisHomeControl(labelParams) {
	return HomeControl(labelParams);
}

/**
 * 自定义LabeledText
 * @param localPoint 单点数组，位置
 * @param labeledText 内容
 * @param pixelOffset 格式为{x:*,y:*}数组
 * @returns
 */
function GisLabeledText(localPoint, labeledText, pixelOffset){
	return  new LabeledText(localPoint, labeledText, pixelOffset);
}
 
/**
 * 自定义TextAreaOnMap
 * @param localPoint 单点数组，位置
 * @param areaText 内容
 * @param classname 样式classname
 * @param pixelOffset 格式为{x:*,y:*}数组
 * @returns
 */
function GisTextAreaOnMap(localPoint, areaText, classname, pixelOffset){
	//显示或者消失在地图上可以调用相应的TextAreaOnMap.setMap(map)和TextAreaOnMap.setMap(null);
	var textAreaLabel = new TextAreaOnMap(localPoint, areaText, classname,  pixelOffset);
	return textAreaLabel;
}

 function GismapMeasure(panel,callback){
	 measurePath(panel,callback);
 }
 /**
 * 地理编码根据经纬度获取地址
 **/
 function GisReverseGeocoder(location){
	 reverseGeocoder(location)
 }
 
 /**
  * 清除路径分析结果
  **/
  function GisClearDirectionsDisplay(){
	  clearDirectionsDisplay()
  }
 
/*********--------------------6.其他js处理功能的添加，注意依托性----------------------************/
/**
 * 获取项目root路径
 * @returns {String}
 */
function getRootPath() {
	 //方法一
	var pathName = window.location.pathname.substring(1);
	var webName = pathName == '' ? '' : pathName.substring(0, pathName.indexOf('/'));
	return window.location.protocol + '//' + window.location.host + '/' + webName + '/';
	
	//方法二
	/* //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath=window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPaht=curWwwPath.substring(0,pos);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    //返回根路径，如： http://localhost:8083/uimcardprj
    return (localhostPaht+projectName); */
	
}

/**
 * 处理浮点数字，保证数字的正确性
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
	return obj.value;
}

/**
 * 替换字符串
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


function isNotNull(str){
	var flag = false;
	if(null != str && "" != str){
		flag = true;
	}
	return flag;
}











