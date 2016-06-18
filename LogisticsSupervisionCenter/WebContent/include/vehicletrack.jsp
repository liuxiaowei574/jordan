<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/include.jsp" />
<title><fmt:message key="gis.label.vehicleTrack" /></title>
<style type="text/css">
.trackingFont {
	color: #ffffff;
}
</style>
</head>

<body>
	<div style="position: absolute; top: 60px; right: 0px; z-index: 99999; width: 180px; white-space: nowrap;">
		<table class="table" id="trackingTable">
			<tr>
				<th style="width: 50%;"><fmt:message key="eclock.label.electricityValue" /></th>
				<td id="electricityValue"></td>
			</tr>
			<tr>
				<th><fmt:message key="eclock.label.altitude" /></th>
				<td id="trackingAltitude"></td>
			</tr>
			<tr>
				<th><fmt:message key="eclock.label.elockSpeed" /></th>
				<td id="trackingElockSpeed"></td>
			</tr>
			<tr>
				<th><fmt:message key="eclock.label.latitude" /></th>
				<td id="trackingLatitude"></td>
			</tr>
			<tr>
				<th><fmt:message key="eclock.label.longitude" /></th>
				<td id="trackingLongitude"></td>
			</tr>
			<tr>
				<th><fmt:message key="eclock.label.direction" /></th>
				<td id="trackingDirection"></td>
			</tr>
		</table>
	</div>
	<div class="modal-header" style="height: 41px;">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel"
			style="margin-top: 0; display: none;">
			<fmt:message key="gis.label.vehicleTrack" />
		</h4>
	</div>
	<!-- <form class="form-horizontal row" id="routeModalForm" method="post"> -->
	<div id="map_vehicle" style="height: 596px; width: 1000px;"></div>
	<div id="vehicelTrackPanel">
		<input type="button" value="<fmt:message key="monitor.replace"/>">
		<input type="button" value="<fmt:message key="monitor.replay"/>">
		<input type="button" value="<fmt:message key="monitor.pause"/>">
		<input type="button" value="<fmt:message key="monitor.continuePlay"/>">
		<input type="button" value="<fmt:message key="monitor.stopPlay"/>">
		<input type="button" value="<fmt:message key="monitor.accelerate"/>">
		<input type="button" value="<fmt:message key="monitor.decelerate"/>">
	</div>
	<!-- </form> -->

</body>
<script type="text/javascript">
	   $("#myModalLabel").css("margin", 0).fadeIn(5);
	   var routeAreaId='${routeAreaId}';
	   var tripId='${tripId}';
	   var pointArray = [];
	   var replayIcon= "../static/images/Safetruck.png";//安全车辆图标
	   $(function() {
		   $("#vehicelTrackPanel input").eq(0).on("click", function(){
			   //debugger;
			   	var pointArray = GisGetDrawPath();
			   	if(pointArray.length>0){
			   		//debugger;
			   		GisSetViewPortByArray(pointArray);
			   	}
		   });
		   $("#vehicelTrackPanel input").eq(1).on("click", function(){
			   GisPathPlay(replayIcon,showMarkerDetilInfo);
		   });
		   $("#vehicelTrackPanel input").eq(2).on("click", function(){
			   GisPathPause(showMarkerDetilInfo);
		   });
		   $("#vehicelTrackPanel input").eq(3).on("click", function(){
			   GisContinuePlay(replayIcon,showMarkerDetilInfo);
		   });
		   $("#vehicelTrackPanel input").eq(4).on("click", function(){
			   GisPathStop(replayIcon,showMarkerDetilInfo);
		   });
		   $("#vehicelTrackPanel input").eq(5).on("click", function(){
			   GisPathAccelaratePaly(replayIcon,showMarkerDetilInfo);
		   });
		   $("#vehicelTrackPanel input").eq(6).on("click", function(){
			   GisPathDecelaratePaly(replayIcon,showMarkerDetilInfo);
		   });
		   var pointArr = {
   				lat : 40.399660000000004,
   				lng : 116.85329000000002
   			};
   			var params = {
   				mapId : "map_vehicle",// div地图id
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
   	       GisEventCallBack("maptypeid_changed",changeFontSize);
   	      
   	      setTimeout(function(){
			   if(""==routeAreaId){
				  bootbox.alert($.i18n.prop("gis.label.routeAreaId.canntFind"));
			   }
		      if(""!=routeAreaId&&null!=routeAreaId&&undefined!=routeAreaId){
		    	  GisClearOverlays(overlays);
		    	  getTrackingPathByRouteAreaId(routeAreaId);
		    	  getVehicleTrackingPathByTripId(tripId);
		      }
	   	   }, 10);
	    });
	     /**
		   * 地图类型切换，详细信息文字样式
		   **/
	   function changeFontSize(){
			  $("#trackingTable tr th").toggleClass("trackingFont");
			  $("#trackingTable tr td").toggleClass("trackingFont");
	   }
	   /**
	   *  回调显示轨迹点详细信息
	   **/
	   function showMarkerDetilInfo(markerInfo){
		   if(markerInfo){
			   $("#electricityValue").html(markerInfo.electricityValue);
			   $("#trackingElockSpeed").html(markerInfo.elockSpeed);
			   $("#trackingAltitude").html(markerInfo.altitude);
			   $("#trackingLatitude").html(markerInfo.latitude);
			   $("#trackingLongitude").html(markerInfo.longitude);
			   $("#trackingDirection").html(markerInfo.direction);
		   }else{
			   $("#electricityValue").html("");
			   $("#trackingElockSpeed").html("");
			   $("#trackingAltitude").html("");
			   $("#trackingLatitude").html("");
			   $("#trackingLongitude").html("");
			   $("#trackingDirection").html("");
		   }
	   }
	   /**
	    * 根据区域或线路编号获取
	    */
	   function getTrackingPathByRouteAreaId(routeAreaId) {
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
	   			//debugger;
	   			//GisClearOverlays(overlays);
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
	   							"color" : "#ff0000",
	   							"weight" : 2,
	   							"opacity" : 1
	   						}
	   					var polyline = GisShowPolyLineInMap(pointArray,true, drawPolylineStyle);
	   					overlays.push(polyline);
	   				} else {
	   					var polygon;
	   					if ("1" == jsonObj.routeAreaStatus) {
	   						polygon = GisShowPolygonInMap(pointArray, true, polygonStyle);
	   					} else {
	   						polygon = GisShowPolygonInMap(pointArray, true, polygonStyleDanger);
	   					}
	   					overlays.push(polygon);
	   				}
	   				//GisSetViewPortByArray(pointArray);
	   				//GisCreateLineTrack(pointArray);
	   			}else{
	   				bootbox.alert($.i18n.prop("map.routeArea.select.noPlanRoute"));
	   			}

	   		}
	   	});
	   }
	   
	  
	   /**
	    * 根据行程ID获取历史轨迹
	    */
	   function getVehicleTrackingPathByTripId(tripId) {
	   	var Url = getRootPath()
	   			+ "vehicletrack/findVehicleTrackingGpsByTripId.action?tripId="
	   			+ tripId;
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
	   			//debugger;
	   			//GisClearOverlays(overlays);
	   			var points = jsonObj.jsonData;
	   			var riskStatus = jsonObj.riskStatus;
	   			if("0"==riskStatus){
	   				replayIcon = "../static/images/Safetruck.png";
	   			}else if("1"==riskStatus){
	   				replayIcon = "../static/images/warningtruck.png";
	   			}else{
	   				replayIcon = "../static/images/alarmtruck.png";
	   			}
	   			pointArray = [];
	   			if (points.length > 0) {
	   				for (var i = 0; i < points.length; i++) {
	   					//var pointMarker = GisCreateMarker({lat:points[i].latitude,lng:points[i].longitude},vehicleOnWayIcon,""+points[i].gpsId);//更新上一次图标
 				        //var content = createCurrentContent(vehicleData);
 				       // GisShowInfoWindow(pointMarker,content);//用于构建弹出框信息，待补充
	   					pointArray.push({
	   						lat : points[i].latitude,
	   						lng : points[i].longitude
	   					});
	   				}
	   					var drawPolylineStyle = {
	   							"color" : "#ffff00",
	   							"weight" : 2,
	   							"opacity" : 1
	   						}
	   					//var polyline = GisShowPolyLineInMap(pointArray,true, drawPolylineStyle);
	   					//overlays.push(polyline);
	   					
	   					GisSetViewPortByArray(pointArray);
	   					GisCreateLineTrackShowDetailInfo(points);
	   			
	   			}else{
	   				bootbox.alert($.i18n.prop("map.routeArea.select.noPlanRoute"));
	   			}

	   		}
	   	});
	   }
	   
	   /**
	    * 构建车辆信息弹出框
	    */
	   function createCurrentContent(obj){
	  	 if(obj){
	  		 //var locationTime = formatDateTime(new Date(obj.locationTime.time));
	  		    var html = '';
	  			html += '<div class="alert_box" style="border:#d7d5d6 solid 1px;box-shadow: 0 0 5px rgba(0, 0, 0, 0.2); position: absolute; left: 50%; bottom: 20%;">';
	  			html += '	<div class="alert_box_content">';
	  			html += '		<div class="alert-title">';
	  			html += '		    <div class="pull-left">';
	  			html += '			    <div class="Vehicle_bubble alarm"></div>';
	  			html += '				<b>'+$.i18n.prop("gis.label.vehicleTrack")+'</b>';
	  			html += '           </div>';
	  			html += '			<div class="pull-right">'+$.i18n.prop("eclock.label.locationTime")+':'+obj.locationTime+'</div>';
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
	  			html += '			         <td>'+obj.locationTime+'</td></tr>';
	  			html += '		    </table>';
	  			html += '	    </div>';
	  			html += '   </div>';
	  			html += '</div>';
	  			return html;
	  	 }
	   }
    </script>

</html>