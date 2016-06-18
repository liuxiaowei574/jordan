/**
 * @author 赵磊峰
 * @date 2015-01-22
 */
var map;// 指定的地图容器，通常放在div中
var markerclusterer;// markerclusterer聚合对象
var point;//获取双击地图的定义值

dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.form.HorizontalRuleLabels");
dojo.require("dojox.form.RangeSlider");
dojo.require("esri.map");
dojo.require("esri.layers.GraphicsLayer");
dojo.require("esri.layers.FeatureLayer");
dojo.require("esri.renderer");
dojo.require("esri.Color");
dojo.require("esri.SpatialReference");
dojo.require("esri.dijit.Legend");
dojo.require("esri.dijit.InfoWindow");
dojo.require("esri.dijit.BasemapToggle");
dojo.require("esri.InfoTemplate");
dojo.require("esri.geometry.Polyline");
dojo.require("esri.geometry.Polygon");
dojo.require("esri.geometry.Point");
dojo.require("esri.symbols.SimpleLineSymbol");
dojo.require("esri.symbols.PictureMarkerSymbol");
dojo.require("esri.symbols.TextSymbol");
dojo.require("esri.symbols.Font");
dojo.require("dojo.dom-construct");
dojo.require("dojo.dom-class");
dojo.require("dojo.dom-style");
dojo.require("dojo._base.lang");
dojo.require("dojo.domReady!");

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
var thisMapType = "topo";// 地图类型,r为地图,h为卫星图
var mapTypeDirect = "top_right";
var zoomControlDirect = "top_left";
var panControlDirect = "top_left";
var streetViewDirect = "bottom_left";
var scaleDirect = "bottom_right";

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
		thisMapType = "hybrid";
	if (params.mapType == 'r')
		thisMapType = "topo";
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

function initialize(pointArr, params) {  
	var centerLatLng = new esri.geometry.Point(pointArr.lng,pointArr.lat);
	handleParams(params);
	//map.centerAndZoom(centerLatLng,zoom);
	var init = function(){
		map = new esri.Map(mapId, { 
	          basemap: thisMapType,
	          center: centerLatLng,//[longitude, latitude]
	          zoom: zoom,
	          //"top-left", "top-right", "bottom-left", "bottom-right". zoom控件位置
	          sliderPosition:getZoomDirection(zoomControlDirect), 
	          sliderStyle: "small"
	    });
		
		//添加地图类型切换视图
		if(isShowMapType){
			var BasemapDiv = document.createElement('div'); 
			BasemapDiv.id = getMapTypeDirection(mapTypeDirect);
			document.getElementById(mapId).appendChild(BasemapDiv); 
			var toggle = new esri.dijit.BasemapToggle({
		        map: map,
		        basemap: "hybrid"
		    }, BasemapDiv);
		    toggle.startup();
		    
		    toggle.on("error", function(msg) {
		        console.log("basemap gallery error:  ", msg);
		    });
		}
		
		//比例尺
		var scalebar = new esri.dijit.Scalebar({
			map: map,
			// "dual" displays both miles and kilmometers
            // "english" is the default, which displays miles
            // use "metric" for kilometers
            scalebarUnit: "dual"
        });
		
		str = "{lng:"+pointArr.lng+",lat:"+pointArr.lat+"}";
		//加载双击地图事件
		dojo.connect(map,"onDblClick",function(e){
	        var mp=e.mapPoint;//地理坐标
	        //var mp2=e.screenPoint;//屏幕坐标
	        str = "{lat:"+mp.x+",lng:"+mp.y+"}";
	    });
	};
	dojo.ready(init);
};

/**
 * 地图类型控件位置
 * 根据传递的值判断方向 参数：左上、右上、左下、右下
 * @param direct
 * @returns
 */
function getMapTypeDirection(direct) {
	var direction = "BasemapLeftTop";
	if (direct == 'top_left' || direct == 'left_top') {
		direction = "BasemapLeftTop";
	}
	if (direct == 'top_right' || direct == 'right_top') {
		direction = "BasemapRightTop";
	}
	if (direct == 'bottom_left' || direct == 'left_bottom') {
		direction = "BasemapLeftBottom";
	}
	if (direct == 'bottom_right' || direct == 'right_bottom') {
		direction = "BasemapRightBottom";
	}
	return direction;
}

/**
 * zoom控件位置
 * 根据传递的值判断方向 参数：左上、右上、左下、右下
 * @param direct
 * @returns
 */
function getZoomDirection(direct) {
	var direction = "top_left";
	if (direct == 'top_left' || direct == 'left_top') {
		direction = "top_left";
	}
	if (direct == 'top_right' || direct == 'right_top') {
		direction = "top_right";
	}
	if (direct == 'bottom_left' || direct == 'left_bottom') {
		direction = "bottom_left";
	}
	if (direct == 'bottom_right' || direct == 'right_bottom') {
		direction = "bottom_right";
	}
	return direction;
}

/**
 * 设置地图中心
 * 
 * @param homePointArr
 */
function setHomeCenter(homePointArr) {
	var homePoint = new esri.geometry.Point(homePointArr.lng,homePointArr.lat);
	map.centerAt(homePoint);
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
 * 设置地图类型
 * 
 * @param mapType 地图类型
 */
function setMapType(mapType) {
	if (mapType == 'h') {
		map.setBasemap("hybrid");
	} else if (mapType == 'r') {
		map.setBasemap("topo");
	} else {
		map.setBasemap("topo");
	}
}

/**
 * 双击鼠标左键获取坐标
 * @returns {String} 
 */
function getPointByClick(){	
    return str;
}

var lineSymbol;
/**
 * 创建一条线路
 * 
 * @param polylineArr 连线点的json数据集合
 * @param isShow 是否显示在地图上
 * @param styleParam - strokeColor、strokeWeight、strokeOpacity
 */
function showPolyLineInMap(polylineArr, isShow, styleParam) {
	var pathPointArr = new Array();
	for ( var i = 0; i < polylineArr.length; i++) {
		pathPointArr.push(new esri.geometry.Point(polylineArr[i].lng,polylineArr[i].lat));
	}
	// 区域的样式，solid或dashed。
	var line = new esri.geometry.Polyline({
		"paths" : pathPointArr,
		"spatialReference": {"wkid": 4326}
	});
	
	//strokeColor : styleParam.color,
	//strokeWeight : styleParam.weight,
	//strokeOpacity : styleParam.opacity
	lineSymbol = new esri.symbol.CartographicLineSymbol(
	    esri.symbol.CartographicLineSymbol.STYLE_SOLID,
	    new dojo.Color(styleParam.color), 
	    styleParam.weight,
	    esri.symbol.CartographicLineSymbol.CAP_ROUND,
	    esri.symbol.CartographicLineSymbol.JOIN_MITER, 5
	);
	
	var polyLine = new esri.Graphic(line, lineSymbol);
	if (isShow) {
		map.graphics.add(polyline);
	}
	
	return polyLine;
}

/**
 * 更新一条线路
 * 
 * @param polyline 更新的路线
 * @param polylineArr 连线点的json数据集合
 */
function showUpdatePolyLine(polyline, polylineArr) {
	var pathPointArr = new Array();
	for ( var i = 0; i < polylineArr.length; i++) {
		pathPointArr.push(new esri.geometry.Point(polylineArr[i].lng,polylineArr[i].lat));
	}
	
	// 区域的样式，solid或dashed。
	var line = new esri.geometry.Polyline({
		"paths" : pathPointArr,
		"spatialReference": {"wkid": 4326}
	});
	
	polyline = new esri.Graphic(line, lineSymbol);
	if (isShow) {
		map.graphics.add(polyline);
	}
	return polyline;
}

/**
 * 创建多边形
 * 
 * @param polygonArr 连多边形点的json数据集合
 * @param isShow 是否显示在地图上
 */
function showPolygonInMap(polygonArr, isShow, polygonStyle) {
	var polygonPointArr = new Array();
	for ( var i = 0; i < polygonArr.length; i++) {
		polygonPointArr.push(new esri.geometry.Point(polygonArr[i].lng, polygonArr[i].lat));
	}
	
	var gon = new esri.geometry.Polygon({
    	"rings": polygonPointArr,
        "spatialReference": {"wkid": 4326}
    });
	
	var polyGonSymbol = new esri.symbol.SimpleFillSymbol(
    	esri.symbol.SimpleFillSymbol.STYLE_SOLID, 
    	new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color(polygonStyle.color), polygonStyle.weight),
    	new dojo.Color(polygonStyle.fillColor)
    );
        
    var polyGon = new esri.Graphic(gon, polyGonSymbol);
	   
	if (isShow) {
		 map.graphics.add(polyGon);
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
	var coordinate = new esri.geometry.Point(pointArr.lng, pointArr.lat);
	var polygonPointArr = new Array();
	for ( var i = 0; i < polygonArr.length; i++) {
		polygonPointArr.push(new esri.geometry.Point(polygonArr[i].lng, polygonArr[i].lat));
	}
	var polygon = new esri.geometry.Polygon({
    	"rings": polygonPointArr,
        "spatialReference": {"wkid": 4326}
    });

	return polygon.contains(coordinate);
}