<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%-- <jsp:include page="/include/include.jsp" /> --%>
<title><fmt:message key="gis.label.vehicleTrack" /></title>
<style type="text/css">
.trackingFont {
	color: #ffffff;
}
</style>
</head>

<body>
	<div id="playerbox" class="player-box" style="z-index: 99999;">
			<div class="play-btn-box">
				<ul>
					<li><a onclick="replaceLoc();" title="Replace" href="javascript:void()"><span class="play-img play-img-weizhi"></span></a></li>
					<li><a onclick="replayStart();" title="Start" href="javascript:void()"><span class="play-img play-img-bofang"></span></a></li>
					<li><a onclick="replayPause();" title="Pause" href="javascript:void()"><span class="play-img play-img-zanting"></span></a></li>
					<li><a onclick="replayContinue();" title="Continue" href="javascript:void()"><span class="play-img play-img-chongbo"></span></a></li>
					<li><a onclick="replayStop();" title="Stop" href="javascript:void()"><span class="play-img play-img-tingzhi"></span></a></li>
					<li><a onclick="replaySpeedDown();" title="SpeedDown" href="javascript:void()"><span class="play-img play-img-houtui"></span></a></li>
					<li><a onclick="replaySpeedUp();" title="SpeedUp" href="javascript:void()"><span class="play-img play-img-qianjin"></span></a></li>
				</ul>
			</div>
			<div class="clearfix"></div>
			<div class="play-slider">
				<div id="play-slider-range" class="play-slider-range" style="width: 0%;"></div>
			</div>
	</div>

	<div class="alert_box" style="position: absolute; top: 390px; right: 10px; z-index: 99999; width: 210px; white-space: nowrap;">
		
			<table class="table table-condensed table-striped table-hover"id="trackingTable">
				<tr>
					<th style="width: 50%;"><fmt:message
							key="eclock.label.electricityValue" /></th>
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
	<div id="map_vehicle" style="height: 596px; width: 1000px;"></div>
<!-- 	<div id="vehicelTrackPanel"> -->
<%-- 		<input type="button" class="btn btn-info" value="<fmt:message key="monitor.replace"/>"> --%>
<%-- 		<input type="button" class="btn btn-info" value="<fmt:message key="monitor.replay"/>"> --%>
<%-- 		<input type="button" class="btn btn-info" value="<fmt:message key="monitor.pause"/>"> --%>
<%-- 		<input type="button" class="btn btn-info" value="<fmt:message key="monitor.continuePlay"/>"> --%>
<%-- 		<input type="button" class="btn btn-info" value="<fmt:message key="monitor.stopPlay"/>"> --%>
<%-- 		<input type="button" class="btn btn-info" value="<fmt:message key="monitor.accelerate"/>"> --%>
<%-- 		<input type="button" class="btn btn-info" value="<fmt:message key="monitor.decelerate"/>"> --%>
<!-- 	</div> -->

</body>
<script type="text/javascript">
	   $("#myModalLabel").css("margin", 0).fadeIn(5);
	   var routeAreaId='${routeAreaId}';
	   var tripId='${tripId}';
	   var pointArray = [];
	   var replayIcon= "../images/gis/truck1.png";//安全车辆图标
	   $(function() {
// 		   $("#vehicelTrackPanel input").eq(0).on("click", function(){
// 			   	var pointArray = GisGetDrawPath();
// 			   	if(pointArray.length>0){
// 			   		GisSetViewPortByArray(pointArray);
// 			   	}
// 		   });
// 		   $("#vehicelTrackPanel input").eq(1).on("click", function(){
// 			   GisPathPlay(replayIcon,showMarkerDetilInfo);
// 		   });
// 		   $("#vehicelTrackPanel input").eq(2).on("click", function(){
// 			   GisPathPause(showMarkerDetilInfo);
// 		   });
// 		   $("#vehicelTrackPanel input").eq(3).on("click", function(){
// 			   GisContinuePlay(replayIcon,showMarkerDetilInfo);
// 		   });
// 		   $("#vehicelTrackPanel input").eq(4).on("click", function(){
// 			   GisPathStop(replayIcon,showMarkerDetilInfo);
// 		   });
// 		   $("#vehicelTrackPanel input").eq(5).on("click", function(){
// 			   GisPathAccelaratePaly(replayIcon,showMarkerDetilInfo);
// 		   });
// 		   $("#vehicelTrackPanel input").eq(6).on("click", function(){
// 			   GisPathDecelaratePaly(replayIcon,showMarkerDetilInfo);
// 		   });
         
      
        function replaceLoc() {
            var pointArray = GisGetDrawPath();
            if (pointArray.length > 0) {
                GisSetViewPortByArray(pointArray);
            }
        }

        function replayStart() {
            GisPathPlay(replayIcon,showMarkerDetilInfo);
        }
        function replayPause() {
            GisPathPause(showMarkerDetilInfo);
        }
        function replayContinue() {
            GisContinuePlay(replayIcon,showMarkerDetilInfo);
        }
        function replayStop() {
            GisPathStop(replayIcon,showMarkerDetilInfo);
        }
        function replaySpeedDown() {
            GisPathDecelaratePaly(replayIcon,showMarkerDetilInfo);
        }
        function replaySpeedUp() {
            GisPathAccelaratePaly(replayIcon,showMarkerDetilInfo);
        }

        var pointArr = {
            lat: 31.9326438,
            lng: 35.9040163
        };
        var params = {
            mapId: "map_vehicle",// div地图id
            isShowMapType: true,// 是否展示地图类型
            mapTypeDirect: "top_left",
            zoom: 19,// 地图级别
            isClickZoom: true,// 是否双击缩放
            isScroll: true,// 滚轮缩放
            isDarggle: true,// 拖拽地图
            isZoomControl: true,// 缩放控件
            zoomControlDirect: "left_top",
            isPanControl: true,// 平移控件
            panControlDirect: "left_top",
            isStreetView: false,// 街景视图
            streetViewDirect: "bottom_left",
            isScale: false,// 比例尺
            scaleDirect: "bottom_right",
            mapType: "r"// 地图类型
        };
        // 初始化google地图
        GisInitialize(pointArr, params);
        GisEventCallBack("maptypeid_changed", changeFontSize);
        setTimeout(function() {
            if ("" == routeAreaId) {
                bootbox.alert($.i18n.prop("gis.label.routeAreaId.canntFind"));
            }
            if ("" != routeAreaId && null != routeAreaId && undefined != routeAreaId) {
                GisClearOverlays(overlays);
                getTrackingPathByRouteAreaId(routeAreaId);
                getVehicleTrackingPathByTripId(tripId);
                findAlarmsByTripId(tripId);
            }
        }, 10);
    });
    /**
     * 地图类型切换，详细信息文字样式
     **/
    function changeFontSize() {
        $("#trackingTable tr th").toggleClass("trackingFont");
        $("#trackingTable tr td").toggleClass("trackingFont");
    }
    /**
     *  回调显示轨迹点详细信息
     **/
    function showMarkerDetilInfo(markerInfo) {
        if (markerInfo) {
            $("#electricityValue").html(markerInfo.electricityValue);
            $("#trackingElockSpeed").html(markerInfo.elockSpeed);
            $("#trackingAltitude").html(markerInfo.altitude);
            $("#trackingLatitude").html(markerInfo.latitude);
            $("#trackingLongitude").html(markerInfo.longitude);
            $("#trackingDirection").html(markerInfo.direction);
        } else {
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
        var Url = getRootPath() + "monitorRaPoint/getPointsByRouteAreaId.action?routeAreaId=" + routeAreaId;
        $.ajax({
            type: "POST",
            url: Url,
            dataType: "json",
            cache: false,
            async: false,
            error: function(e, message, response) {
                console.log("Status: " + e.status + " message: " + message);
            },
            success: function(jsonObj) {
                var needLoginFlag = false;
                if (typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
                    needLoginFlag = needLogin(jsonObj);
                }
                if (!needLoginFlag) {
                    //GisClearOverlays(overlays);
                    var points = jsonObj.lsMonitorRaPointBOs;
                    var drawType = jsonObj.lsMonitorRouteAreaBO.routeAreaType;
                    var pointArray = [];
                    if (points.length > 0) {
                        for (var i = 0; i < points.length; i++) {
                            pointArray.push({
                                lat: points[i].latitude,
                                lng: points[i].longitude
                            });
                        }
                        if ("0" == drawType) {
                            var drawPolylineStyle = {
                                "color": "#ff0000",
                                "weight": 2,
                                "opacity": 1
                            }
                            var polyline = GisShowPolyLineInMap(pointArray, true, drawPolylineStyle);
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
                    } else {
                        bootbox.alert($.i18n.prop("map.routeArea.select.noPlanRoute"));
                    }
                }

            }
        });
    }

    /**
     * 根据行程ID获取历史轨迹
     */
    function getVehicleTrackingPathByTripId(tripId) {
        var Url = getRootPath() + "vehicletrack/findVehicleTrackingGpsByTripId.action?tripId=" + tripId;
        $.ajax({
            type: "POST",
            url: Url,
            dataType: "json",
            cache: false,
            async: false,
            error: function(e, message, response) {
                console.log("Status: " + e.status + " message: " + message);
            },
            success: function(jsonObj) {
                var needLoginFlag = false;
                if (typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
                    needLoginFlag = needLogin(jsonObj);
                }
                if (!needLoginFlag) {
                    //GisClearOverlays(overlays);
                    var riskStatus = jsonObj.riskStatus;
                    if ("0" == riskStatus) {
                        replayIcon = "../images/gis/truck1.png";
                    } else if ("1" == riskStatus) {
                        replayIcon = "../images/gis/truck2.png";
                    } else {
                        replayIcon = "../images/gis/truck3.png";
                    }

                    pointArray = [];
                    var vehicles = jsonObj.jsonData;
                    if (vehicles.length > 0) {
                        for (var j = 0; j < vehicles.length; j++) {
                            var keys = Object.keys(vehicles[j]);
                            var trackingDeviceNumber = keys[0];
                            var points = vehicles[j][trackingDeviceNumber];
                            if (points.length > 0) {
                                for (var i = 0; i < points.length; i++) {
                                    //var pointMarker = GisCreateMarker({lat:points[i].latitude,lng:points[i].longitude},vehicleOnWayIcon,""+points[i].gpsId);//更新上一次图标
                                    //var content = createCurrentContent(vehicleData);
                                    // GisShowInfoWindow(pointMarker,content);//用于构建弹出框信息，待补充
                                    pointArray.push({
                                        lat: points[i].latitude,
                                        lng: points[i].longitude
                                    });
                                }
                                var drawPolylineStyle = {
                                    "color": "#ffff00",
                                    "weight": 2,
                                    "opacity": 1
                                }
                                var polyline = GisShowPolyLineInMap(pointArray, true, drawPolylineStyle);
                                overlays.push(polyline);

                                //GisSetViewPortByArray(pointArray);
                                GisCreateLineTrackShowDetailInfo(points);

                            } else {
                                bootbox.alert(keys[0] + $.i18n.prop("map.routeArea.select.noVehicleLine"));
                            }
                        }
                        GisSetViewPortByArray(pointArray);
                    }
                }
            }
        });
    }
    /**
     *查询报警点
     */
    function findAlarmsByTripId(tripId) {
        $.get(root + "/vehicletrack/findAlarmVOByTripId.action", {
            "tripId": tripId
        }, function(obj) {
            var needLoginFlag = false;
            if (typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
                needLoginFlag = needLogin(obj);
            }
            if (!needLoginFlag) {
                obj = JSON.parse(obj);
                if (obj.success) {
                    //车辆报警
                    var alarmPoints = obj.lsMonitorAlarmVOs;//车辆报警信息
                    for (var i = 0; i < alarmPoints.length; i++) {
                        //(function(x){
                        var data = alarmPoints[i];
                        var alarmContent = createAlarmContent(data);
                        var loction = {
                            lat: data.alarmLatitude,
                            lng: data.alarmLongitude
                        };
                        var alarmIcon = getAlarmIconByTypeAndLevel(data.alarmTypeId, data.alarmLevelId);
                        var alarmMarker = GisCreateMarker(loction, "${root}/" + alarmIcon, $.i18n.prop('AlarmType.'
                                + data.alarmTypeId), "");
                        //var alarmContent = createAlarmContent(messageContent);
                        // 				                GisAddEventForVehicle(alarmMarker, "click", function() {
                        // 				                    var d = dialog({
                        // 				                        id: data.alarmId,
                        // 				                        title: " ",// $.i18n.prop('trip.info.message'),
                        // 				                        content: alarmContent,
                        // 				                        resize: true
                        // 				                    });
                        // 				                    d.show();
                        // 				                });
                        GisShowInfoWindow(alarmMarker, alarmContent, false);
                        //})(i)
                    }
                }
            }

        });
    }

    /**
     * 构建车辆信息弹出框
     */
    function createAlarmContent(obj) {
        alarmDealId = obj.alarmId;
        if (obj) {
            var html = '';
            var alarmTime = formatDateTime(new Date(obj.alarmTime.time));
            html += '<div class="alert_box">';
            html += '	<div class="alert_box_content">';
            html += '		<div class="alert-title">';
            html += '		    <div class="pull-left">';
            html += '			    <div class="Vehicle_bubble alarm"></div>';
            html += '				<b>' + obj.vehiclePlateNumber + '</b>&nbsp;';
            html += '           </div>';
            html += '			<div class="pull-right">' + $.i18n.prop("alarm.label.alarmTime") + ':' + alarmTime + '</div>';
            html += '		</div>';
            html += '		<div class="alert_table">';
            html += '			<table class="table table-condensed table-striped table-hover">';
            html += '			     <tr><th>' + $.i18n.prop("alarm.label.alarmLongtitude") + '</th>';
            html += '			         <td>' + obj.alarmLongitude + '</td></tr>';
            html += '			     <tr><th>' + $.i18n.prop("alarm.label.alarmLatitude") + '</th>';
            html += '			         <td>' + obj.alarmLatitude + '</td></tr>';
            html += '			     <tr><th>' + $.i18n.prop("alarm.label.alarmTime") + '</th>';
            html += '			         <td>' + alarmTime + '</td></tr>';
            html += '			     <tr class = "hidden"><th></th>';
            html += '			         <td>' + obj.alarmId + '</td></tr>';
            html += '		    </table>';
            html += '	    </div>';
            html += '	  <div class="clearfix"></div>';
            html += '     </div>';
            html += '</div>';
            html += '</div>';
            return html;
        }
    }
    /**
     * 构建车辆信息弹出框
     */
    function createCurrentContent(obj) {
        if (obj) {
            //var locationTime = formatDateTime(new Date(obj.locationTime.time));
            var html = '';
            html += '<div class="alert_box" style="border:#d7d5d6 solid 1px;box-shadow: 0 0 5px rgba(0, 0, 0, 0.2); position: absolute; left: 50%; bottom: 20%;">';
            html += '	<div class="alert_box_content">';
            html += '		<div class="alert-title">';
            html += '		    <div class="pull-left">';
            html += '			    <div class="Vehicle_bubble alarm"></div>';
            html += '				<b>' + $.i18n.prop("gis.label.vehicleTrack") + '</b>';
            html += '           </div>';
            html += '			<div class="pull-right">' + $.i18n.prop("eclock.label.locationTime") + ':' + obj.locationTime
                    + '</div>';
            html += '		</div>';
            html += '		<div class="alert_table">';
            html += '			<table class="table table-condensed table-striped table-hover">';
            html += '			     <tr><th>' + $.i18n.prop("eclock.label.electricityValue") + '</th>';
            html += '			         <td>' + obj.electricityValue + '</td></tr>';
            html += '			     <tr><th>' + $.i18n.prop("eclock.label.altitude") + '</th>';
            html += '			         <td>' + obj.altitude + '</td></tr>';
            html += '			     <tr><th>' + $.i18n.prop("eclock.label.elockSpeed") + '</th>';
            html += '			         <td>' + obj.elockSpeed + '</td></tr>';
            html += '			     <tr><th>' + $.i18n.prop("eclock.label.latitude") + '</th>';
            html += '			         <td>' + obj.latitude + '</td></tr>';
            html += '			     <tr><th>' + $.i18n.prop("eclock.label.longitude") + '</th>';
            html += '			         <td>' + obj.longitude + '</td></tr>';
            html += '			     <tr><th>' + $.i18n.prop("eclock.label.direction") + '</th>';
            html += '			         <td>' + obj.direction + '</td></tr>';
            html += '			     <tr><th>' + $.i18n.prop("eclock.label.locationTime") + '</th>';
            html += '			         <td>' + obj.locationTime + '</td></tr>';
            html += '		    </table>';
            html += '	    </div>';
            html += '   </div>';
            html += '</div>';
            return html;
        }
    }
    var formatDateTime = function(date) {
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = date.getHours();
        var minute = date.getMinutes();
        minute = minute < 10 ? ('0' + minute) : minute;
        return y + '-' + m + '-' + d + ' ' + h + ':' + minute;
    };
</script>

</html>