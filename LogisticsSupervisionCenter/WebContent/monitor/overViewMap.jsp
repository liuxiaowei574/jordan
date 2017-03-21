<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../../include/include.jsp"%>
<script type="text/javascript" src="${root}/gis/map.js.jsp"></script>
<%-- <jsp:include page="../../include/include.jsp" /> --%>
<title></title>
</head>

<body>

	<div id="overViewMap" style="width: 300px; height: 300px"></div>

	<script>
	 var trackingDeviceNumber = '${trackingDeviceNumber}';
     var tripId = '${tripId}';
     var vehicleStatus;
        $(function() {
            var params = {
                mapId: "overViewMap",// div地图id
                //isShowMapType : true,// 是否展示地图类型
                //mapTypeDirect : "top_left",
                zoom: 15,// 地图级别
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
                mapType: "h"// 地图类型
            };
            var localpoint = {
                lat: 32.323,
                lng: 35.323
            };
            // 初始化google地图
            GisInitialize(localpoint, params);
            findDblClickVehicleInMap(trackingDeviceNumber, tripId);
            queryAndResetMainPage(trackingDeviceNumber, tripId);
            getVehiclePath();
        });

        var VehicleId = "";
        /**
         * 查询所有车辆信息并定时刷新
         * @param 各种参数信息 where
         */
        function queryAndResetMainPage(trackingDeviceNumber, tripId) {
            resetVehicleInfo();
            function resetVehicleInfo() {
                findDblClickVehicleInMap(trackingDeviceNumber, tripId);
                refreshTimeoutValue = setTimeout(function() {
                    resetVehicleInfo();
                }, 10000);
            }
        }
        
        function findDblClickVehicleInMap(trackingDeviceNumber, tripId) {
            var freezeAlarmUrl = root
                    + "/vehiclestatus/queryMonitorVehicleStatus.action?trackingDeviceNumber="
                    + trackingDeviceNumber + "&tripId=" + tripId;
            $.ajax({
                type: "POST",
                url: freezeAlarmUrl,
                dataType: "json",
                cache: false,
                async: false,
                error: function(e, message, response) {
                    console.log("Status: " + e.status + " message: " + message);
                },
                success: function(jsonObj) {
                    if (jsonObj.lsMonitorVehicleStatusBO) {
                        var data = jsonObj.lsMonitorVehicleStatusBO;
                        vehicleStatus = data;
                        var vehicle = jsonObj.lsCommonVehicleBO;
                        VehicleId = vehicle.vehicleId;
                        var pointArr = {
                            lat: data.latitude,
                            lng: data.longitude
                        };
                        GisSetHomeCenter(pointArr);
                        var iconpath = "../static/images/alarmtruck.png";
                        if ("0" == data.riskStatus) {
                            iconpath = "../static/images/Safetruck.png";
                        } else if ("1" == data.riskStatus) {
                            iconpath = "../static/images/warningtruck.png";
                        } else {
                            var iconpath = "../static/images/Safetruck.png";
                        }
                        //var html = createCurrentContent()
//                         var vehicleMarker = GisCreateVehicelMarker(pointArr, iconpath, ""
//                                 + vehicle.vehicleStatusId, JSON.stringify(vehicle));
//                         var svgMaker = GisCreateVehicleSVGMarker(pointArr, GisCreateSvgIcon(
//                                 google.maps.SymbolPath.FORWARD_CLOSED_ARROW, 3, data.direction,
//                                 "green", "green"), "" + data.vehicleStatusId, "111");

                        //svgMarkers.push(svgMaker);
                        //GisShowInfoWindow(vehicleMarker,html,false);
                        // var   vehicleMarker =  GisCreateVehicelMarker(pointArr, iconsrc,"","");
                    }

                }
            });

        }

        var overViewSocketGPS;//
        /**
         * 构建车辆信息弹出框
         */
        function createCurrentContent(obj) {
            var html = '';
            var iconpath = "static/images/alarmtruck.png";
            if ("0" == obj.riskStatus) {
                iconpath = "static/images/Safetruck.png";
            } else if ("1" == obj.riskStatus) {
                iconpath = "static/images/warningtruck.png";
            }
            if (obj) {
                var locationTime = formatDateTime(new Date(obj.locationTime));
                html += '<div class="alert_box">';
                html += '	<div class="alert_box_content">';
                html += '		<div class="alert-title">';
                html += '		    <div class="pull-left">';
                html += '			    <div class="Vehicle_bubble"><img alt="'+ obj.vehiclePlateNumber+ '" src="'+ iconpath+ '"/></div>';
                html += '				<b>' + obj.vehiclePlateNumber + '</b>';
                html += '           </div>';
                html += '			<div class="pull-right">' + $.i18n.prop("eclock.label.locationTime")
                        + ':' + locationTime + '</div>';
                html += '		</div>';
                html += '		<div class="alert_table"><tbody>';
                html += '			<table class="table table-condensed table-striped table-hover">';
                html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.trackingDeviceNumber")
                        + '</th>';
                html += '			         <td>' + obj.trackingDeviceNumber + '</td>';
                html += '			     <th>' + $.i18n.prop("monitorTrip.label.containerNumber") + '</th>';
                html += '			         <td>' + obj.containerNumber + '</td></tr>';
                html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.declarationNumber")
                        + '</th>';
                html += '			         <td>' + obj.declarationNumber + '</td>';
                html += '			     <th>' + $.i18n.prop("monitorTrip.label.trailerNumber") + '</th>';
                html += '			         <td>' + obj.trailerNumber + '</td></tr>';
                html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.driverName") + '</th>';
                html += '			         <td>' + obj.driverName + '</td>';
                html += '			     <th>' + $.i18n.prop("monitorTrip.label.driverCountry") + '</th>';
                html += '			         <td>' + obj.driverCountry + '</td></tr>';
                html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.vehicleCountry")
                        + '</th>';
                html += '			         <td>' + obj.vehicleCountry + '</td>';
                html += '			     <th>' + $.i18n.prop("monitorTrip.label.esealNumber") + '</th>';
                html += '			         <td>' + obj.esealNumber + '</td></tr>';
                html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.sensorNumber")
                        + '</th>';
                html += '			         <td>' + obj.sensorNumber + '</td>';
                html += '			     <th>' + $.i18n.prop("monitorTrip.label.elockStatus") + '</th>';
                if (obj.altitude == "1") {
                    html += '			         <td>' + $.i18n.prop("monitorTrip.label.elockStatus.lock")
                            + '</td></tr>';
                } else {
                    html += '			         <td>'
                            + $.i18n.prop("monitorTrip.label.elockStatus.unlock") + '</td></tr>';
                }
                html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.FromTo") + '</th>';
                html += '			         <td colspan="3">' + obj.checkinPortName + ' - '
                        + obj.checkoutPortName + '</td></tr>';
                html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.locationAttr")
                        + '</th>';
                html += '			         <td>' + obj.longitude + ',' + obj.latitude + '</td>';
                html += '                    <th>' + $.i18n.prop("monitorTrip.label.direction")
                        + '(' + '<img src="static/images/clockwise.png"  alt="clockwise(N-S)" />'
                        + ')' + '</th>';
                html += '			         <td>' + obj.direction + '</td></tr>';
                html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.checkinTime") + '</th>';
                html += '			         <td>' + formatDateTime(new Date(obj.checkinTime)) + '</td>';
                html += '			     <th>' + $.i18n.prop("monitorTrip.label.checkinUser") + '</th>';
                html += '			         <td>' + obj.checkinUserName + '</td></tr>';
                html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.electricityValue")
                        + '</th>';
                html += '			         <td>' + obj.electricityValue + '</td>';
                html += '			     <th>' + $.i18n.prop("monitorTrip.label.elockSpeed") + '</th>';
                html += '			         <td>' + obj.elockSpeed + '</td></tr>';
                html += '		    </tbody></table>';
                html += '	    </div>';
                html += '   </div>';
                html += '</div>';
            }
            return html;
        }

        /**
         * html5 websocket获取推送报警信息
         */
        function connectWebSocketInfo() {
            if (!overViewSocketGPS) {
                overViewSocketGPS = new ReconnectingWebSocket(wsGpsUrl);
            }
            overViewSocketGPS.onopen = function(event) {
                //alert("Connect Sussess!");
            };
            overViewSocketGPS.onmessage = function(event) {
                //var alarm =  event.data;//获取的是对象
                var gpsdata = strToJson(event.data);//将获取的json对象转为可用的对象
                var messageContent = gpsdata.messageContent;
                if (gpsdata.messageType == "VEHICLE_ALARM") {
                    if (typeof (VehicleId) != "undefined") {
                        var alarmIcon = getAlarmIconByTypeAndLevel(messageContent.alarmTypeId, messageContent.alarmLevelId);
                        var alarmMarker = GisCreateMarker({
                            lat: messageContent.alarmLatitude,
                            lng: messageContent.alarmLongitude
                        }, alarmIcon, $.i18n.prop('AlarmType.' + messageContent.alarmTypeId), JSON.stringify(messageContent));
                        //$("#bsound").src="static/images/ALARM3.WAV";
                        //$("#bsound")[0].play();
                        var alarmContent = createAlarmContent(messageContent);
                        GisShowInfoWindow(alarmMarker, alarmContent, false);

                        pointMarkers.push(alarmMarker);
                    }
                } else if (gpsdata.messageType == "VEHICLE_GPS") {
                    //alert(gpsdata.messageType + "--------"+messageContent+"-----"+trackingTripId+"------"+messageContent.direction);
                    var localPoint = {
                        lat: messageContent.latitude,
                        lng: messageContent.longitude
                    };
                    if (typeof (VehicleId) != "undefined") {
                        tripBufferPoints.push({
                            lat: messageContent.latitude,
                            lng: messageContent.longitude
                        });
                        if (typeof (trackingLine) == "undefined") {
                            var drawPolylineStyle = {
                                "color": "#ffff00",
                                "weight": 2,
                                "opacity": 1
                            }
                            trackingLine = GisShowPolyLineInMap(tripBufferPoints, true,
                                    drawPolylineStyle);
                            overlaysArray.push(trackingLine);
                            /*var pointMarker = GisCreateMarker(localPoint,"static/images/pointIcon.png",""+messageContent.gpsId);
                            GisShowInfoWindow(pointMarker,""+messageContent.gpsId,false);*/
                            pointMarkers.push(pointMarker);
                        } else {
                            trackingLine = GisUpdatePolyLine(trackingLine, localPoint);
                        }
                        if (typeof (trackingMaker) != "undefined") {
                            trackingMaker.setMap(null);
                            trackingMaker = undefined;
                        }
                        var vehicleIcon = createSafeIcon(messageContent.direction);
                        if (messageContent.riskStatus == "1") {
                            vehicleIcon = createRiskIcon(messageContent.direction);
                        } else if (messageContent.riskStatus == "2") {
                            vehicleIcon = createDangerIcon(messageContent.direction);
                        }
                        //alert(GisGetAttribution(trackingMaker));//转化为JSON：var jsonStr = JSON.parse(GisGetAttribution(trackingMaker));
                        trackingMaker = GisCreateMarker({
                            lat: messageContent.latitude,
                            lng: messageContent.longitude
                        }, vehicleIcon, "" + messageContent.gpsId, GisGetAttribution(trackingMaker));
                        var html = createUpdateContent(
                                JSON.parse(GisGetAttribution(trackingMaker)), messageContent);
                        GisShowInfoWindow(trackingMaker, html, false);
                        vehicleMarkers.push(trackingMaker);

                    }

                } else if (gpsdata.messageType == "PORTAL_GPS") {
                    GisClearOverlays(patrolMarkers);
                    var data = gpsdata.messageContent;
                    var commonPatrolBO = data.commonPatrolBO;
                    var monitorVehicleGpsBO = data.monitorVehicleGpsBO;
                    var lsSystemRoleBO = data.lsSystemRoleBO;
                    var patrolIcon = "";
                    if (lsSystemRoleBO.roleName == "enforcementPatrol") {
                        patrolIcon = "images/gis/xunluo.png";
                    } else if (lsSystemRoleBO.roleName == "escortPatrol") {
                        patrolIcon = "images/gis/husongxunluo.png";
                    } else {
                        patrolIcon = "images/gis/husongxunluo.png";
                    }
                    var pstrolMaker = GisCreateMarker({
                        lat: monitorVehicleGpsBO.latitude,
                        lng: monitorVehicleGpsBO.longitude
                    }, patrolIcon, "" + monitorVehicleGpsBO.gpsId, GisGetAttribution(pstrolMaker));
                    patrolMarkers.push(pstrolMaker);

                }
            };
            overViewSocketGPS.onclose = function(event) {

            };
        }
        function getVehiclePath() {
            var portUrl = getRootPath()
                    + "monitorvehicle/findAllMonitorVehicleGpsByEclockNum.action?trackingDeviceNumber="
                    + trackingDeviceNumber + "&vehicleId="
                    + VehicleId;
            $.ajax({
                type: "POST",
                url: portUrl,
                cache: false,
                async: false,
                
                error: function(e, message, response) {
                    console.log("Status: " + e.status + " message: " + message);
                },
                success: function(obj) {
                    if(obj.success){
    					//车辆轨迹
    					trackingTripId= obj.lsMonitorTripBO.tripId;
    					var jsonObj = obj.lsMonitorVehicleGpsBOs;//车辆实时轨迹
    					if(jsonObj!=null){
    						clearAllTimeout();//清除所有的定时任务
    						overmapCreateTracking(jsonObj,obj.lsMonitorTripBO,vehicleStatus);//地图上展示车辆
    						var lsMonitorRaPointBOs = obj.lsMonitorRaPointBOs;//车辆预定义路线
    						if(lsMonitorRaPointBOs!=null){
    							mapShowRouteLine(lsMonitorRaPointBOs,ALL_POINT);
    						}
    					}
                    }
                }
            })
        }
        var ALL_POINT = "ALL_POINT";
        function mapShowRouteLine(lsMonitorRaPointBOs,type) {
        	tripRoutePoints = [];
        	if(ALL_POINT == type){
        		for (var j = 0; j < lsMonitorRaPointBOs.length; j++) {
        			if(isNotNull(lsMonitorRaPointBOs[j].latitude)&&isNotNull(lsMonitorRaPointBOs[j].longitude)){
        				tripRoutePoints.push({
        					lat : lsMonitorRaPointBOs[j].latitude,
        					lng : lsMonitorRaPointBOs[j].longitude
        				});
        			}
        		}
        		var lineStyle = {
        				color:"#ffff00", 
        				weight:5, 
        				opacity:1
        			};
        	}
        	
        	//if(menuType=="9" || menuType=="8" ){
        		var routeLineLine = GisShowPolyLineInMap(tripRoutePoints, true, lineStyle);
        		
        		//overlaysArray.push(routeLineLine);
        	//}
        }
        /**
         * 清除所有定时任务
         */
         var refreshTimeoutValue = -1;
        function clearAllTimeout(){
        	if(refreshTimeoutValue != -1){
        		window.clearTimeout(refreshTimeoutValue);
        		refreshTimeoutValue = -1;
        	}
        }
        function overmapCreateTracking(jsonObj,lsMonitorTripBO,gpsVehicleInfo) {
        	tripBufferPoints = [];
        		for (var i = 0; i < jsonObj.length; i++) {
        			tripBufferPoints.push({
        				lat : jsonObj[i].latitude,
        				lng : jsonObj[i].longitude,
        				direction: jsonObj[i].direction
        			});
        			//显示隐藏轨迹上的点
        			/*var pointMarker = GisCreateMarker({lat:jsonObj[i].latitude,lng:jsonObj[i].longitude},"static/images/pointIcon.png",""+jsonObj[i].gpsId);
        			GisShowInfoWindow(pointMarker,""+jsonObj[i].gpsId,false);
        			pointMarkers.push(pointMarker);*/
        		}
        		var lineStyle = {
        				color:"#1800ff", 
        				weight:4, 
        				opacity:1,
        				zIndex:999
        			};
        	
        	
        	
        	/*if($("#handlePoint").attr("name")=="showPoint"){
        		GisHiddenOverlays(pointMarkers);
        	}else{
        		GisShowOverlays(pointMarkers);
        	}*/
        	
        	var localPoint = tripBufferPoints[tripBufferPoints.length-1];
        	if("0"==gpsVehicleInfo.riskStatus){
        		replayIcon = "images/gis/truck1.png";
        	}else if("1"==gpsVehicleInfo.riskStatus){
        		replayIcon = "images/gis/truck2.png";
        	}else{
        		replayIcon =  "images/gis/truck3.png";
        	}
        	
        	//if(menuType=="8"){
        	//	GisCreateLineTrack(tripBufferPoints);
        	//}else{
        		trackingLine = GisShowPolyLineInMap(tripBufferPoints, true, lineStyle);
        		//GisSetShowFront(trackingLine,1);
        		//overlaysArrayRoute.push(trackingLine);
        	//}
        	GisSetViewPortByArray(tripBufferPoints);
        	
        	if("0"==gpsVehicleInfo.riskStatus){
        		
        		trackingMaker =  GisCreateVehicelMarker(localPoint, createSafeIcon(gpsVehicleInfo.direction),""+lsMonitorTripBO.tripId,JSON.stringify(gpsVehicleInfo));
        	}else if("1"==gpsVehicleInfo.riskStatus){
        		trackingMaker =  GisCreateVehicelMarker(localPoint, createRiskIcon(gpsVehicleInfo.direction),""+lsMonitorTripBO.tripId,JSON.stringify(gpsVehicleInfo));
        	}else{
        		trackingMaker =  GisCreateVehicelMarker(localPoint, createDangerIcon(gpsVehicleInfo.direction),""+lsMonitorTripBO.tripId,JSON.stringify(gpsVehicleInfo));
        	}
        	var svgMaker =  GisCreateVehicleSVGMarker(localPoint, GisCreateSvgIcon(google.maps.SymbolPath.FORWARD_CLOSED_ARROW,3,gpsVehicleInfo.direction,"green","green"),""+gpsVehicleInfo.vehicleStatusId,"111");
           // vehicleMarkers.push(svgMaker);
//        	GisSetShowFront(trackingMaker,1000000);
        	//var currentContent = creatRealVehcleContent(gpsVehicleInfo);
        	
        	//GisShowInfoBox(trackingMaker,currentContent);//自定义弹出框
        	//GisShowInfoWindow(trackingMaker,currentContent,false); 
        	//vehicleMarkers.push(trackingMaker);
        }
        
        /**
         * 普通车辆图标
         * @param direction
         */
        function createSafeIcon(direction){
        	//return "images/gis/truck1.png";
        	return "../static/images/Safetruck.png";
        	//return GisCreateSvgIcon(truckPath,0.05,parseInt(direction)-90,"#ccc","#31b59f");
        }
        /**
         * 风险车辆图标
         * @param direction
         */
        function createRiskIcon(direction){
        	//return "images/gis/truck2.png";
        	return "../static/images/warningtruck.png";
        	//return GisCreateSvgIcon(truckPath,0.05,parseInt(direction)-90,"#ccc","#ff9f00");
        }
        /**
         * 危险车辆图标
         * @param direction
         */
        function createDangerIcon(direction){
        	//return "images/gis/truck3.png";
        	return "../static/images/alarmtruck.png";
        	//return GisCreateSvgIcon(truckPath,0.05,parseInt(direction)-90,"#ccc","#d32e00");
        }
    </script>

</body>