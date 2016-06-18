<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<fmt:setBundle basename="i18n.messages" var="commonBundle" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="initial-scale=1, maximum-scale=1,user-scalable=no">
<title>GisDemo</title>
<style type="text/css">
html {
	height: 100%
}

body {
	height: 100%;
	margin: 0;
	padding: 0
}
</style>
<link rel="stylesheet" href="${root}/gis/arcgis/css/arcgis.css">
<link rel="stylesheet"
	href="http://js.arcgis.com/3.12/esri/css/esri.css">
<script type="text/javascript" src="${root}/js/jquery-1.10.2.min.js"></script>
<script src="http://js.arcgis.com/3.12/"></script>
<script type="text/javascript">
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
dojo.require("esri.dijit.BasemapToggle");
dojo.require("esri.dijit.Legend");
dojo.require("esri.dijit.InfoWindow");
dojo.require("esri.dijit.BasemapToggle");
dojo.require("esri.dijit.HomeButton");
dojo.require("esri.dijit.Scalebar");
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
var map;
var str;

//websocket html5

//var pointArr={lat:40.36140605,lng:116.823673};
var pointArr={lat:45.58,lng:-122.57}
function initialize() {  
	var init = function(){
		var centerLatLng = new esri.geometry.Point(pointArr.lng,pointArr.lat,new esri.SpatialReference({wkid:4326}));
		map = new esri.Map("map_canvas", { 
	          basemap: "topo",
	          center: centerLatLng,//[longitude, latitude]
	          zoom: 12,
	          sliderPosition:"top-left", //"top-left", "top-right", "bottom-left", "bottom-right".
	          sliderStyle: "small"
	    });
		
		var BasemapDiv = document.createElement('div'); 
		BasemapDiv.id = "BasemapLeftTop";
		
		document.getElementById("map").appendChild(BasemapDiv); 
		 
		var toggle = new esri.dijit.BasemapToggle({
	        map: map,
	        basemap: "hybrid"
	    }, BasemapDiv);
	    toggle.startup();
		
		str = "{lng:"+pointArr.lng+",lat:"+pointArr.lat+"}";
		dojo.connect(map,"onLoad",function(){
			//map.disableMapNavigation();//地图平移控件
		    //map.disableScrollWheelZoom();//鼠标滚轮放大缩小
		    //禁止双击放大
		    map.disableDoubleClickZoom();//鼠标双击放大
		    //alert(map.isScrollWheelZoom);
		    map.disableKeyboardNavigation();//键盘平移
		    //map.disablePan();//鼠标拖动平移
		    
		    //var simpleMarkerSymbol = new esri.symbol.SimpleMarkerSymbol();
	        //var graphic = new esri.Graphic(centerLatLng, simpleMarkerSymbol);
	        //map.graphics.add(graphic);
	        
        });
		dojo.connect(map,"onDblClick",function(e){
	        var mp=e.mapPoint;//地理坐标
	        //var mp2=e.screenPoint;//屏幕坐标
	        str = "{lat:"+mp.x+",lng:"+mp.y+"}";
	    });
		
		//比例尺
		var scalebar = new esri.dijit.Scalebar({
			map: map,
			// "dual" displays both miles and kilmometers
            // "english" is the default, which displays miles
            // use "metric" for kilometers
            scalebarUnit: "dual"
        });
		
		var HomeButtonDiv = document.createElement('div' ); 
		HomeButtonDiv.id = "BasemapRightTop";
        
        document.getElementById("map").appendChild(HomeButtonDiv); 
		
		 var home = new esri.dijit.HomeButton({
             map: map
         }, HomeButtonDiv);
         home.startup();
		
         //var mapPoint = new esri.geometry.Point(140,40);
         //map.centerAt(mapPoint);
		
		//var mapPoint = new esri.geometry.Point(116,40);
		//map.centerAndZoom(mapPoint, 4);
	};
	dojo.ready(init);
	
};

function dxInit(){
	try{
		alert(map.isDoubleClickZoom);
	   
	   //比例尺显示
	   //window.setTimeout("queryMapScale.init(map);", 1000);
	   
	   //禁止双击放大
	   map.disableDoubleClickZoom();
	   
	   //初始化导航工具条
	        navToolbar = new esri.toolbars.Navigation(map);
	        dojo.connect(navToolbar, "onExtentHistoryChange", extentHistoryChangeHandler);
	        
	        //初始化在线编辑工具条
	        editToolbar = new esri.toolbars.Edit(map);
	        dojo.connect(map.graphics, "onClick", function(evt) {
	            dojo.stopEvent(evt); 
	            activateToolbar(evt.graphic);              
	        });
	        
	        //初始化绘制工具条
	        toolbar = new esri.toolbars.Draw(map);           
	        //dojo.connect(toolbar, "onDrawEnd", addToMap);
	        
	        //显示坐标
	        dojo.connect(map, "onMouseMove", showCoordinates);
	        dojo.connect(map, "onMouseOut", hideCoordinates);          
	        
	        //加载鹰眼图、加载图层列表
	        dojo.connect(map, "onLayerAdd", showLayers);
	        
	        //加载之前记录标注
	        //window.setTimeout("getBz();", 1000);------------------------------
	        
	        //地图窗口更新
	        //alert(document.getElementById('map1'));
	        resizeMap();
	   dojo.connect(document.getElementById('map1'), 'resize', resizeMap);
	   
	   //双击map，定位街景
	   dojo.connect(map, 'onDblClick', zoomTo3D);
	   
	   //图层控制
	   layersCotrol([0]);
	}catch(e){
	alert("地图初始化失败："+e.message);
	}
}


function drawLine(){
	var line = new esri.geometry.Polyline({
	   "paths": [[[-122.68,45.53], [-122.58,45.55],[-122.57,45.58],[-122.53,45.6]]],
	   "spatialReference": { "wkid": 4326 }
	});
	var lineSymbol = new esri.symbol.CartographicLineSymbol(
	  esri.symbol.CartographicLineSymbol.STYLE_SOLID,
	  new dojo.Color("#0000FF"), 2,
	  esri.symbol.CartographicLineSymbol.CAP_ROUND,
	  esri.symbol.CartographicLineSymbol.JOIN_MITER, 5
	);
	var polyline = new esri.Graphic(line, lineSymbol);
	map.graphics.add(polyline);
		
		
}

function drawGon(){
    var gon = new esri.geometry.Polygon({
    	"rings": [[[-122.68,45.53], [-122.58,45.55],[-122.57,45.58],[-122.53,45.6]]],
        "spatialReference": {"wkid": 4326}
    });
    
    var polyGonSymbol = new esri.symbol.SimpleFillSymbol(
    	esri.symbol.SimpleFillSymbol.STYLE_SOLID, 
    	new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([255, 0, 0]), 2),
    	new dojo.Color("green")
    );
        
    var polyGon = new esri.Graphic(gon, polyGonSymbol);
    map.graphics.add(polyGon);
}

function clearGraphic() {
     map.graphics.clear();
}

function testSocket(){
	var ws = new WebSocket("ws://localhost:8080/GisDemo/websocket");
	ws.onopen = function(){ws.send("Test!"); };
	ws.onmessage = function(evt){console.log(evt.data);ws.close();};
	ws.onclose = function(evt){console.log("WebSocketClosed!");};
	ws.onerror = function(evt){console.log("WebSocketError!");};
}
//显示鼠标坐标
function showCoordinates(){	
    alert(str);
    //map.
    return str;
}

jQuery(document).ready(function() {
	//dojo.addOnLoad(initialize);
	initialize();
}); 

function xxx(){
	//map.setBasemap("hybrid");
	drawGon();
	testSocket();
	document.getElementById("count").innerHTML = showCoordinates();
}


//dojo.ready(init);

/* require(["esri/map", "dojo/domReady!"], function(Map) { 
      map = new Map("map", {
        center: [116.823673, 40.36140605],//longitude, latitude
        zoom: 8,
        basemap: "streets"
      });
      
      function xxx(){
    	  alert("1111");
      }

});  */


</script>
</head>
<body>
	<div id="map" style="width: 100%; height: 90%;"></div>
	<div id="count" style="width: 100%; height: 5%;"></div>
	<div>
		<input type="button" value="2222" onclick="xxx();">
	</div>
	</div>
</body>
</html>