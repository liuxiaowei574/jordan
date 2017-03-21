var webSocketGPS;//
var CIRCLE_BUFFER = "CIRCLE_BUFFER";
var RouteAreaType = "POLYLINE";
var POLYLINE = "POLYLINE";
var POLYGON = "POLYGON";
var EXCEEDING_TRIP_ALLOWED_TIME = "EXCEEDING_TRIP_ALLOWED_TIME";// 行程超时报警
var CLOSED_LOCK = "CLOSED_LOCK";// 开锁又重新关闭
var DEVICE_BROKEN = "DEVICE_BROKEN";// 强拆报警
var ENTER_DANGEROUS_AREA = "ENTER_DANGEROUS_AREA";// 危险区域报警
var ESEAL_LOSS = "ESEAL_LOSS";// 子锁失联
var GSM_LOSS = "GSM_LOSS";// GSM信号丢失
var LONG_STAY = "LONG_STAY";// 长时间滞留
var LOW_BATTERY = "LOW_BATTERY";// 低电量
var OPEN_LOCK = "OPEN_LOCK";// 开锁
var OPPOSITE_ROUTE = "OPPOSITE_ROUTE";// 反向行驶
var ROUTE_DEVIATION = "ROUTE_DEVIATION";// 路线偏移
var SATELLITE_LOSS = "SATELLITE_LOSS";// 卫星信号丢失
var SENSOR_LOSS = "SENSOR_LOSS";// 传感器失联
var TAMPERING_WITH_ESEAL = "TAMPERING_WITH_ESEAL";// 子锁破坏
var TAMPERING_WITH_SENSOR = "TAMPERING_WITH_SENSOR";// 传感器破坏
var TRACK_UNIT_ALARM = "TRACK_UNIT_ALARM";// 车载台报警
var VEHICLE_INFO;
// var cirlceBuffer ="";
var routeAreaCol = "";
var drawPolylineStyle = {
    "color": "#00ff00",
    "weight": 4,
    "opacity": 1
}
var drawParams = {
    direct: "right_top",
    darwStyle: drawPolylineStyle
};

var editPolylineStyle = {
    "color": "#00ff00",
    "weight": 4,
    "opacity": 1
}
/**
 * 画图工具类 true ,表示是否显示画图工具 getDrawRouteArea 回调函数获取绘制图形坐标集合
 */
// GisInitDrawingManagerAndDriving(drawParams,
// true,getDrawRouteArea,RouteAreaType);//初始化Google地图画图工具
function getDrawRouteArea(routeAreaCollection) {
    routeAreaCol = routeAreaCollection;
    $("#routeAreaPtCol").val(routeAreaCol);
    // var pointArray = JSON.parse(routeAreaCollection);
    // var polyline = GisShowPolyLineInMap(pointArray, false,
    // editPolylineStyle);
    // overlaysArray.push(polyline);
}
$('#map_open').on('click', function() {
    $("#cont").toggle();
    var isHidden = $("#cont").is(":hidden");
    /*
     * if(isHidden){ $(".gm-bundled-control .gmnoprint,.gm-china
     * .gmnoprint:last-child").css({ "left": "0px" }); }else{ $(".gm-china
     * .gmnoprint:last-child,.gm-china .gmnoprint:last-child").css({ "left":
     * "270px" }); }
     */
    /*
     * if(isHidden){ $("#legend").css({ "left": "65px", }); }else{
     * $("#legend").css({ "left": "335px", }); }
     */
});

$('#map_open1').on('click', function() {
    $("#cont").toggle();
    var isHidden = $("#cont").is(":hidden");
    /*
     * if(isHidden){ $(".gm-bundled-control .gmnoprint,.gm-china
     * .gmnoprint:last-child").css({ "left": "0px" }); }else{ $(".gm-china
     * .gmnoprint:last-child,.gm-china .gmnoprint:last-child").css({ "left":
     * "270px" }); }
     */
    /*
     * if(isHidden){ $("#legend").css({ "left": "65px", }); }else{
     * $("#legend").css({ "left": "335px", }); }
     */
});
$('#map_open2').on('click', function() {
    $("#cont").toggle();
    var isHidden = $("#cont").is(":hidden");
    if (isHidden) {
        $(".gm-bundled-control .gmnoprint,.gm-china .gmnoprint:last-child").css({
            "left": "0px"
        });
    } else {
        $(".gm-china .gmnoprint:last-child,.gm-china .gmnoprint:last-child").css({
            "left": "270px"
        });

    }
    /*
     * if(isHidden){ $("#legend").css({ "left": "65px", }); }else{
     * $("#legend").css({ "left": "335px", }); }
     */
});
$('#raPointList').on('click', function() {
    $("#trackingTable").addClass("hidden");
    $(".play-slider-range").css("width", '0%');
    GisDrawManagerVisible(false);
    $("#playerbox").addClass("hidden");
    $("#vehicelfilter").removeClass("hidden");
    $("#header_title_div").removeClass("hidden");
    menuType = "9";
    tripStatus = "1";
    mapInitPort();
    queryAndResetMainPage();// 定时刷新，包含下面被注释的
    /*
     * clearAllOverlays(false); findAllVehicleStatus(true);
     */
    // $("#trackSeleCtr").addClass("hidden");
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
    $("#addLandMarker").addClass("hidden");
    $("#handlePatrol").addClass("hidden");
    $("#handleLanderMarker").addClass("hidden");
    $("#mergePatrol").addClass("hidden");
    $("#bottomPanel").addClass("hidden");
    $("#vehicelStatusList").addClass("hidden");
    $("#vehicelTrackList").addClass("hidden");
    $("#vehicelTrackList").addClass("hidden");
});
$('#routeAreaList').on('click', function() {
    $('#trackingTable').addClass("hidden");
    $(".play-slider-range").css("width", '0%');
    clearAllOverlays(true);
    $("#vehicelfilter").addClass("hidden");
    $("#playerbox").addClass("hidden");
    $("#header_title_div").removeClass("hidden");
    RouteAreaType = POLYLINE;
    $("#dvPanelParent").animate({
        right: -400
    }, "slow");
    menuType = "0";
    $("#header_title").html($.i18n.prop("link.system.routeList"));
    findAllRouteAreaList();
    $(".app-right-top").addClass("hidden");
    $(".search_box").addClass("hidden");
    drawManagerControlVisible(true);
    mapInitPort();
    $("#planRouteAreaList").removeClass("hidden");
    $("#panelList").addClass("hidden");
    $("#patrolList").addClass("hidden");
    $("#classify").addClass("hidden");
    $("#addDelUpdate").removeClass("hidden");
    $("#addRapoint").addClass("hidden");
    $("#addLandMarker").addClass("hidden");
    $("#handlePatrol").addClass("hidden");
    $("#handleLanderMarker").addClass("hidden");
    $("#mergePatrol").addClass("hidden");
    $("#vehicelStatusList").addClass("hidden");
    $("#bottomPanel").addClass("hidden");
    GisInitDrawingManagerAndDriving(drawParams, true, getDrawRouteArea, POLYLINE);// 初始化Google地图画图工具
    $("#vehicelTrackList").addClass("hidden");
    $("#trackquery").addClass("hidden");
    //角色权限控制
    /*
    if(['contromRoomUser'].indexOf(roleName) < 0) {
    	$("#addroutearea, #editRouteArea, #deleteRouteArea").parent("li").remove();
    }
    */

});
$('#siteList').on('click', function() {
    $('#trackingTable').addClass("hidden");
    $(".play-slider-range").css("width", '0%');
    clearAllOverlays(true);
    GisDrawManagerVisible(false);
    $("#vehicelfilter").addClass("hidden");
    $("#playerbox").addClass("hidden");
    $("#header_title_div").removeClass("hidden");
    RouteAreaType = POLYGON;
    // $("#trackSeleCtr").addClass("hidden");
    menuType = "1";
    $("#header_title").html($.i18n.prop("link.system.siteList"));
    findAllRouteAreaList();
    $(".app-right-top").addClass("hidden");
    $(".search_box").addClass("hidden");
    drawManagerControlVisible(true);
    mapInitPort();
    $("#planRouteAreaList").removeClass("hidden");
    $("#panelList").addClass("hidden");
    $("#patrolList").addClass("hidden");
    $("#classify").addClass("hidden");
    $("#addDelUpdate").removeClass("hidden");
    $("#addRapoint").addClass("hidden");
    $("#addLandMarker").addClass("hidden");
    $("#handlePatrol").addClass("hidden");
    $("#handleLanderMarker").addClass("hidden");
    $("#mergePatrol").addClass("hidden");
    $("#bottomPanel").addClass("hidden");
    $("#vehicelStatusList").addClass("hidden");
    GisInitDrawingManagerAndDriving(drawParams, true, getDrawRouteArea, POLYGON);// 初始化Google地图画图工具
    $("#vehicelTrackList").addClass("hidden");
    $("#trackquery").addClass("hidden");
});
/**
 * 添加landmarker保留六位小数
 */
function getLandMarkerCoordByClick(e) {
    var latlng = e.latLng;
    $('#latitude').val((parseFloat(latlng.lat())).toFixed(6));
    $('#longitude').val((parseFloat(latlng.lng())).toFixed(6));
}
$('#areaList').on('click', function() {
    $('#trackingTable').addClass("hidden");
    $(".play-slider-range").css("width", '0%');
    clearAllOverlays(true);
    GisDrawManagerVisible(false);
    $("#vehicelfilter").addClass("hidden");
    $("#playerbox").addClass("hidden");
    $("#header_title_div").removeClass("hidden");
    RouteAreaType = POLYGON;
    // $("#trackSeleCtr").addClass("hidden");
    menuType = "2";
    $("#header_title").html($.i18n.prop("link.system.markerList"));
    // findAllRouteAreaList();
    findAllLandMarkers();
    $(".app-right-top").addClass("hidden");
    $(".search_box").addClass("hidden");
    drawManagerControlVisible(true);
    mapInitPort();
    $("#planRouteAreaList").removeClass("hidden");
    $("#panelList").addClass("hidden");
    $("#patrolList").addClass("hidden");
    $("#classify").addClass("hidden");
    $("#addDelUpdate").addClass("hidden");
    $("#addRapoint").addClass("hidden");
    $("#addLandMarker").addClass("hidden");
    $("#handlePatrol").addClass("hidden");
    $("#handleLanderMarker").removeClass("hidden");
    $("#mergePatrol").addClass("hidden");
    $("#bottomPanel").addClass("hidden");
    $("#vehicelStatusList").addClass("hidden");
    GisInitDrawingManagerAndDriving(drawParams, false, getDrawRouteArea, POLYGON);// 初始化Google地图画图工具
    $("#vehicelTrackList").addClass("hidden");
    $("#trackquery").addClass("hidden");
    //角色权限控制
    /*
    if(['contromRoomUser'].indexOf(roleName) < 0) {
    	$("#addLanderMarker, #editLanderMarker, #deleteLanderMarker").parent("li").remove();
    }
    */
});
$('#vehicleHisList').on('click', function() {
    $('#trackingTable').removeClass("hidden");
    clearAllOverlays(false);
    GisDrawManagerVisible(false);
    $("#vehicelfilter").removeClass("hidden");
    $("#playerbox").removeClass("hidden");
    // $("#trackSeleCtr").addClass("hidden");
    menuType = "8";
    tripStatus = "3";
    findVehicleTrackStatus(false);
    $("#header_title_div").removeClass("hidden");
    $(".app-right-top").addClass("hidden");
    $(".search_box").addClass("hidden");
    drawManagerControlVisible(false);
    GisInitDrawingManagerAndDriving(drawParams, false, getDrawRouteArea, POLYGON);// 初始化Google地图画图工具
    mapInitPort();
    $("#planRouteAreaList").addClass("hidden");
    $("#panelList").addClass("hidden");
    $("#patrolList").addClass("hidden");
    $("#addDelUpdate").addClass("hidden");
    $("#classify").removeClass("hidden");
    $("#addRapoint").addClass("hidden");
    $("#addLandMarker").addClass("hidden");
    $("#handlePatrol").addClass("hidden");
    $("#handleLanderMarker").addClass("hidden");
    $("#mergePatrol").addClass("hidden");
    $("#bottomPanel").addClass("hidden");
    $("#vehicelStatusList").addClass("hidden");
    $("#vehicelTrackList").removeClass("hidden");
    $("#trackquery").removeClass("hidden");
});
$('#patrolShowList').on('click', function() {
    GisDrawManagerVisible(false);
    $("#vehicelfilter").addClass("hidden");
    $("#playerbox").addClass("hidden");
    // $("#trackSeleCtr").addClass("hidden");
    $("#header_title_div").removeClass("hidden");
    menuType = "7";
    clearAllOverlays(true);
    findAllPatrolStatus(true);
    $("#header_title").html($.i18n.prop("link.system.patrolList"));
    $(".app-right-top").removeClass("hidden");
    $(".search_box").removeClass("hidden");
    drawManagerControlVisible(false);
    mapInitPort();
    $("#planRouteAreaList").addClass("hidden");
    $("#panelList").addClass("hidden");
    $("#patrolList").removeClass("hidden");
    $("#addDelUpdate").addClass("hidden");
    $("#addRapoint").addClass("hidden");
    $("#addLandMarker").addClass("hidden");
    $("#handlePatrol").removeClass("hidden");
    $("#handleLanderMarker").addClass("hidden");
    $("#mergePatrol").addClass("hidden");
    $("#bottomPanel").addClass("hidden");
    $("#vehicelStatusList").addClass("hidden");
    $("#vehicelTrackList").addClass("hidden");
});
$('#patrolTrackList').on('click', function() {
    $("#vehicelfilter").addClass("hidden");
    $("#playerbox").addClass("hidden");
    // $("#trackSeleCtr").addClass("hidden");
    menuType = "10";
    clearAllOverlays(true);
    findAllPatrolStatus(true);
    $("#header_title").html($.i18n.prop("link.system.patrolList"));
    $(".app-right-top").removeClass("hidden");
    $(".search_box").removeClass("hidden");
    mapInitPort();
    $("#planRouteAreaList").addClass("hidden");
    $("#panelList").addClass("hidden");
    $("#patrolList").removeClass("hidden");
    $("#addDelUpdate").addClass("hidden");
    $("#addRapoint").addClass("hidden");
    $("#addLandMarker").addClass("hidden");
    $("#classify").removeClass("hidden");
    $("#mergePatrol").addClass("hidden");
    $("#bottomPanel").addClass("hidden");
    $("#vehicelStatusList").addClass("hidden");
});

$('#addroutearea').on(
        'click',
        function() {
            GisdirectionsDisplayVisible(false);
            $("#vehicelfilter").addClass("hidden");
            $("#playerbox").addClass("hidden");
            $("#trackquery").addClass("hidden");
            $("#vehicelTrackList").addClass("hidden");
            $("#planRouteAreaList").addClass("hidden");
            $("#panelList").addClass("hidden");
            $("#addRapoint").removeClass("hidden");
            $("#addLandMarker").addClass("hidden");
            $("#handlePatrol").addClass("hidden");
            $("#handleLanderMarker").addClass("hidden");
            $("#addRouteAreaBtn").removeClass("hidden");
            $("#editRouteAreaBtn").addClass("hidden");
            clearAllOverlays(true);
            // mapInitPort();
            $("#routeAreaId").val("");
            $("#routeAreaName").val("");
            $("#routeAreaType").val("");
            $("#belongToPort").val("");
            $("#createUser").val("");
            $("#updateUser").val("");
            $("#routeAreaStatus").val("0");
            $("#routeAreaPtCol").val("");
            $("#routeAreaBuffer").val("100");
            $("#routeCost").val("");
            $("#belongToPort").html("");
            $("#vehicelStatusList").addClass("hidden");
            // 清除选中值
            $('input[name="routeAreaIds"]:checked').each(function() {
                $(this).attr("checked", false);
            });

            $.ajax({
                type: "POST",
                url: getRootPath() + "port/findSelectData.action",
                cache: false,
                async: false,
                error: function(e, message, response) {
                    console.log("Status: " + e.status + " message: " + message);
                },
                success: function(obj) {
                    if (obj.success) {
                        var lsSystemDepartmentBOs = obj.lsSystemDepartmentBOs;
                        if (lsSystemDepartmentBOs != null) {
                            var belongToPorts = "<option value=''></option>";
                            for (var i = 0; i < lsSystemDepartmentBOs.length; i++) {
                                var baseObject = lsSystemDepartmentBOs[i];
                                belongToPorts += "<option value='" + baseObject.organizationId + "'>"
                                        + baseObject.organizationName + "</option>";
                            }
                            $("#belongToPort").append(belongToPorts);
                        }
                    }
                }
            });

            if (menuType == "1") {
                $("#routeAreaTypeDiv").removeClass("hidden");
            } else {
                $("#routeAreaTypeDiv").addClass("hidden");
            }
            if (menuType == "0") {
                $("#routeAreaBufferDiv").removeClass("hidden");
                $("#colorSelect").removeClass("hidden");
                $("#previewBtn").removeClass("hidden");

                getAllPorts(loadPorts);
                
            } else {
                $("#routeAreaBufferDiv").addClass("hidden");
                $("#colorSelect").addClass("hidden");
                $("#previewBtn").addClass("hidden");
            }
        });
/**
 * 根据起止口岸自动获取规划路线
 */

function getPlanRouteByStartEndPort() {
    var startId = "";
    var endId = "";
    $("#startId").on(
            "change",
            function() {
                startId = $(this).val();
                if ("" != startId && null != startId) {
                    var startVal = $("#startId option[value='" + startId + "']")[0].title.split(",");
                    if ($("#endId").val()) {
                        if(startId==endId){
                            bootbox.alert($.i18n.prop("common.message.startEndPortSame"));
                            return;
                          }
                        var endVal = $("#endId option[value='" + endId + "']")[0].title.split(",");
                        GisDirectSearchForPlanRoute(GisHandlePointByJson(changArrLatLngToObj(startVal)),
                                GisHandlePointByJson(changArrLatLngToObj(endVal)), [], null, getDrawRouteArea);
                    }
                }
            });
    $("#endId").on(
            "change",
            function() {
                endId = $(this).val();
                if ("" != endId && null != endId) {
                    var endVal = $("#endId option[value='" + endId + "']")[0].title.split(",");
                    if ($("#startId").val()) {
                        if(startId==endId){
                            bootbox.alert($.i18n.prop("common.message.startEndPortSame"));
                            return;
                          }
                        var startVal = $("#startId option[value='" + startId + "']")[0].title.split(",");
                        GisDirectSearchForPlanRoute(GisHandlePointByJson(changArrLatLngToObj(startVal)),
                                GisHandlePointByJson(changArrLatLngToObj(endVal)), [], null, getDrawRouteArea);
                    }

                }
            });
}
/**
 * 将数组经纬度转为对象
 */
function changArrLatLngToObj(arr){
    return {lat:arr[0],lng:arr[1]};
}

$('#editRouteArea').on(
        'click',
        function() {
            GisdirectionsDisplayVisible(false);
            $("#vehicelfilter").addClass("hidden");
            $("#playerbox").addClass("hidden");
            // 获取checkbox的值，如果为0，则需要选中|如果>1，则需要重新选择|如果==1，则获取选中值并加载
            // 获取选中的值
            var checkValues = [], startId, endId;
            $('input[name="routeAreaIds"]:checked').each(function() {
                checkValues.push($(this).val());
                startId = $(this).attr("startId");
                endId = $(this).attr("endId");
            });
            if (checkValues.length == 1) {
            	if(['contromRoomUser'].indexOf(roleName) < 0 && startId != organizationId && endId != organizationId) {
            		bootbox.alert($.i18n.prop('map.routeArea.mustBeCurrentPort'));
            		return false;
            	}
                $("#routeAreaId").val(checkValues[0]);
                $("#planRouteAreaList").addClass("hidden");
                $("#panelList").addClass("hidden");
                $("#addRapoint").removeClass("hidden");
                $("#addLandMarker").addClass("hidden");
                $("#handlePatrol").addClass("hidden");
                $("#handleLanderMarker").addClass("hidden");
                $("#handleLanderMarker").addClass("hidden");
                $("#addRouteAreaBtn").addClass("hidden");
                $("#editRouteAreaBtn").removeClass("hidden");
                $("#belongToPort").html("");
                $("#vehicelTrackList").addClass("hidden");
                GisClearOverlays(overlaysArray);
                $.ajax({
                    type: "POST",
                    url: getRootPath() + "monitorroutearea/editRouteArea.action?ids=" + checkValues,
                    cache: false,
                    async: false,
                    error: function(e, message, response) {
                        console.log("Status: " + e.status + " message: " + message);
                    },
                    success: function(obj) {
                        if (obj.success) {
                            var lsMonitorRouteAreaBO = obj.lsMonitorRouteAreaBO;
                            var lsSystemDepartmentBOs = obj.lsSystemDepartmentBOs;
                            $("#routeAreaName").val(lsMonitorRouteAreaBO.routeAreaName);
                            $("#routeDistance").val(lsMonitorRouteAreaBO.routeDistance);
                            $("#routeDistanceFormatted").val(toMoney(lsMonitorRouteAreaBO.routeDistance));
                            // $("#routeAreaType").val(lsMonitorRouteAreaBO.routeAreaType);
                            $("#routeAreaType option[value='" + lsMonitorRouteAreaBO.routeAreaType + "']").attr(
                                    "selected", "selected");

                            // $("#belongToPort").val(lsMonitorRouteAreaBO.belongToPort);
                            if (lsSystemDepartmentBOs != null) {
                                var belongToPorts = "<option value=''></option>";
                                for (var i = 0; i < lsSystemDepartmentBOs.length; i++) {
                                    var baseObject = lsSystemDepartmentBOs[i];
                                    if (baseObject.organizationId == lsMonitorRouteAreaBO.belongToPort) {
                                        belongToPorts += "<option value='" + baseObject.organizationId + "' selected>"
                                                + baseObject.organizationName + "</option>";
                                    } else {
                                        belongToPorts += "<option value='" + baseObject.organizationId + "'>"
                                                + baseObject.organizationName + "</option>";
                                    }
                                }
                                $("#belongToPort").append(belongToPorts);
                                if (['3', '5'].indexOf(lsMonitorRouteAreaBO.routeAreaType) < 0) {
                                    $("#belongToPort").attr("disabled", true);
                                } else {
                                    $("#belongToPort").attr("disabled", false);
                                }
                            }

                            $("#createUser").val(lsMonitorRouteAreaBO.createUser);
                            $("#updateUser").val(lsMonitorRouteAreaBO.updateUser);
                            // $("#routeAreaStatus").val(lsMonitorRouteAreaBO.routeAreaStatus);
                            $("#routeAreaStatus option[value='" + lsMonitorRouteAreaBO.routeAreaStatus + "']").attr(
                                    "selected", "selected");
                            bufferColor = '#' + lsMonitorRouteAreaBO.routeAreaColor;
                            $("#routeAreaBuffer").val(lsMonitorRouteAreaBO.routeAreaBuffer);
                            $("#jscolor").val(lsMonitorRouteAreaBO.routeAreaColor);
                            $("#jscolor")[0].style.backgroundColor = '#' + lsMonitorRouteAreaBO.routeAreaColor
                            editPolylineStyle = {
                                "color": "#" + lsMonitorRouteAreaBO.routeAreaColor || '#00ff00',
                                "weight": lsMonitorRouteAreaBO.routeAreaBuffer,
                                "opacity": 0.4
                            }
                            $("#routeCost").val(lsMonitorRouteAreaBO.routeCost);
                            var points = obj.lsMonitorRaPointBOs;
                            // var originPt = JSON.stringify(points[0]);
                            // var endPt =
                            // JSON.stringify(points[points.length-1]);

                            $("#routeAreaPtCol").val("");
                            var pointArray = [];
                            if (points.length > 0) {
                                var jsonPoint = "";
                                for (var i = 0; i < points.length; i++) {
                                    pointArray.push({
                                        lat: points[i].latitude,
                                        lng: points[i].longitude
                                    });
                                    var str = "{\"lat\":\"" + points[i].latitude + "\",\"lng\":\""
                                            + points[i].longitude + "\"},";
                                    jsonPoint += str;
                                }
                                jsonPoint = jsonPoint.substring(0, jsonPoint.length - 1)
                                jsonPoint = "[" + jsonPoint + "]";

                                $("#routeAreaPtCol").val(routeAreaCol);
                                if ("0" == lsMonitorRouteAreaBO.routeAreaType) {
                                    // if(points.length>8){
                                    // var polyline =
                                    // GisShowPolyLineInMap(pointArray, false,
                                    // editPolylineStyle);
                                    // overlaysArray.push(polyline);
                                    // }else{
                                    var originPt = points[0];
                                    var origin = {
                                        lat: originPt.latitude,
                                        lng: originPt.longitude
                                    };
                                    var endPt = points[points.length - 1];
                                    var end = {
                                        lat: endPt.latitude,
                                        lng: endPt.longitude
                                    };
                                    points.shift();
                                    // points.splice(0,1,null);
                                    points.pop();
                                    var ggPts = [];
                                    for ( var key in points) {
                                        var obj = points[key];
                                        var objjson = {
                                            lat: obj.latitude,
                                            lng: obj.longitude
                                        };
                                        var pt = GisHandlePointByJson(objjson);
                                        ggPts.push(pt);
                                    }

                                    /* 用于自动分析 但中间点最多只能有8个 */
                                    // GisDirectSearchForPlanRoute(GisHandlePointByJson(origin),GisHandlePointByJson(end),ggPts,null,getDrawRouteArea);
                                    /* 根据分析路径获取坐标自己画线 */
                                    // var pointArray =
                                    // JSON.parse(routeAreaCollection);
                                    var polyline = GisShowPolyLineInMap(pointArray, true, editPolylineStyle);
                                    overlaysArray.push(polyline);
                                    // }
                                    GisSetViewPortByArray(pointArray);
                                    GisEnableEdit();
                                } else {
                                    var polygon;
                                    if ("1" == lsMonitorRouteAreaBO.routeAreaType) {
                                        polygon = GisShowPolygonInMap(pointArray, true, polygonStyle);
                                        overlaysArray.push(polygon);
                                        GisEnableEdit();
                                        GisSetViewPortByArray(pointArray);
                                    } else {
                                        polygon = GisShowPolygonInMap(pointArray, true, polygonStyleDanger);
                                        overlaysArray.push(polygon);
                                        GisEnableEdit();
                                        GisSetViewPortByArray(pointArray);
                                    }

                                }

                            }
                            // 加载起始口岸、结束口岸并设定默认值
                            getAllPorts(function(ports) {
                                loadPorts(ports);
                                $("#startId option[value='" + lsMonitorRouteAreaBO.startId + "']").attr("selected",
                                        "selected");
                                $("#endId option[value='" + lsMonitorRouteAreaBO.endId + "']").attr("selected",
                                        "selected");
                            });
                        } else {
                            bootbox.alert($.i18n.prop("map.routeArea.edit.getInfoFailure"));
                        }
                    }

                });

                // 清除选中值
                $('input[name="routeAreaIds"]:checked').each(function() {
                    $(this).attr("checked", false);
                });

                if (menuType == "1") {
                    $("#routeAreaTypeDiv").removeClass("hidden");
                } else {
                    $("#routeAreaTypeDiv").addClass("hidden");
                }

                if (menuType == "0") {
                    $("#routeAreaBufferDiv").removeClass("hidden");
                    $("#colorSelect").removeClass("hidden");
                    $("#previewBtn").removeClass("hidden");
                    $("#vehicelTrackList").addClass("hidden");
                } else {
                    $("#routeAreaBufferDiv").addClass("hidden");
                    $("#colorSelect").addClass("hidden");
                    $("#previewBtn").addClass("hidden");
                    $("#vehicelTrackList").addClass("hidden");
                }

            } else {
                bootbox.alert($.i18n.prop("map.routeArea.edit.oneChoice"));
            }
        });

$('#deleteRouteArea').on('click', function() {
    // GisDrawManagerVisible(false);
    // 获取选中的值
    var checkValues = [], startId, endId;
    $('input[name="routeAreaIds"]:checked').each(function() {
        checkValues.push($(this).val());
        startId = $(this).attr("startId");
        endId = $(this).attr("endId");
    });
    if (checkValues.length == 0) {
        bootbox.alert($.i18n.prop("map.routeArea.edit.noChoice"));
    } else {
    	if(['contromRoomUser'].indexOf(roleName) < 0 && startId != organizationId && endId != organizationId) {
    		bootbox.alert($.i18n.prop('map.routeArea.mustBeCurrentPort'));
    		return false;
    	}
        bootbox.confirm($.i18n.prop("map.routeArea.delete.confirm"), function(result) {
            if (result) {
                $.ajax({
                    type: "POST",
                    url: getRootPath() + "monitorroutearea/delRouteArea.action?ids=" + checkValues,
                    cache: false,
                    async: false,
                    error: function(e, message, response) {
                        console.log("Status: " + e.status + " message: " + message);
                    },
                    success: function(obj) {
                        if (obj.success) {
                            GisClearOverlays(overlaysArray);
                            bootbox.success($.i18n.prop("map.routeArea.delete.success"));
                            findAllRouteAreaList();// 更新列表
                        } else {
                            bootbox.error($.i18n.prop("map.routeArea.delete.failure"));
                        }
                    }
                });
            }
        });
    }
    // 刷新列表
});
$('#addPatrol').on(
        'click',
        function() {
            $("#patrolList").addClass("hidden");
            $("#addRapoint").addClass("hidden");
            $("#addLandMarker").addClass("hidden");
            $("#mergePatrol").removeClass("hidden");
            $("#addPatrolBtn").removeClass("hidden");
            $("#editPatrolBtn").addClass("hidden");
            $("#vehicelTrackList").addClass("hidden");

            $("#patrolId").val("");

            $("#potralUser").html("");
            $("#belongToPort1").html("");
            $("#belongToArea").html("");
            $("#trackUnitNumber").html("");
            // 清除选中值
            $('input[name="patrolIds"]:checked').each(function() {
                $(this).attr("checked", false);
            });
            $.ajax({
                type: "POST",
                url: getRootPath() + "patrol/findSelectData.action",
                cache: false,
                async: false,
                error: function(e, message, response) {
                    console.log("Status: " + e.status + " message: " + message);
                },
                success: function(obj) {
                    if (obj.success) {
                        var lsSystemDepartmentBOs = obj.lsSystemDepartmentBOs;
                        var lsMonitorRouteAreaBOs = obj.lsMonitorRouteAreaBOs;
                        var lsWarehouseTrackUnitBOs = obj.lsWarehouseTrackUnitBOs;
                        var lsSystemUserBOs = obj.lsSystemUserBOs;
                        if (lsSystemUserBOs != null) {
                            var potralUsers = "<option value=''></option>";
                            for (var i = 0; i < lsSystemUserBOs.length; i++) {
                                var baseObject = lsSystemUserBOs[i];
                                potralUsers += "<option value='" + baseObject.userId + "'>" + baseObject.userAccount
                                        + "</option>";
                            }
                            $("#potralUser").append(potralUsers);
                        }
                        if (lsSystemDepartmentBOs != null) {
                            var belongToPorts = "<option value=''></option>";
                            for (var i = 0; i < lsSystemDepartmentBOs.length; i++) {
                                var baseObject = lsSystemDepartmentBOs[i];
                                belongToPorts += "<option value='" + baseObject.organizationId + "'>"
                                        + baseObject.organizationName + "</option>";
                            }
                            $("#belongToPort1").append(belongToPorts);
                        }
                        if (lsMonitorRouteAreaBOs != null) {
                            var belongToAreas = "<option value=''></option>";
                            for (var i = 0; i < lsMonitorRouteAreaBOs.length; i++) {
                                var baseObject = lsMonitorRouteAreaBOs[i];
                                belongToAreas += "<option value='" + baseObject.routeAreaId + "'>"
                                        + baseObject.routeAreaName + "</option>";
                            }
                            $("#belongToArea").append(belongToAreas);
                        }
                        if (lsWarehouseTrackUnitBOs != null) {
                            var trackUnitNumbers = "<option value=''></option>";
                            for (var i = 0; i < lsWarehouseTrackUnitBOs.length; i++) {
                                var baseObject = lsWarehouseTrackUnitBOs[i];
                                trackUnitNumbers += "<option value='" + baseObject.trackUnitNumber + "'>"
                                        + baseObject.trackUnitNumber + "</option>";
                            }
                            $("#trackUnitNumber").append(trackUnitNumbers);
                        }

                    }
                }
            });
        });

$('#editPatrol').on(
        'click',
        function() {
            var checkValues = [];
            $('input[name="patrolIds"]:checked').each(function() {
                checkValues.push($(this).val());
            });
            if (checkValues.length == 1) {
                $("#patrolId").val(checkValues[0]);
                $("#patrolList").addClass("hidden");
                $("#addRapoint").addClass("hidden");
                $("#addLandMarker").addClass("hidden");
                $("#mergePatrol").removeClass("hidden");
                $("#addPatrolBtn").addClass("hidden");
                $("#vehicelTrackList").addClass("hidden");
                $("#editPatrolBtn").removeClass("hidden");
                $("#potralUser").html("");
                $("#belongToPort1").html("");
                $("#belongToArea").html("");
                $("#trackUnitNumber").html("");

                $.ajax({
                    type: "POST",
                    url: getRootPath() + "patrol/editPatrol.action?ids=" + checkValues,
                    cache: false,
                    async: false,
                    error: function(e, message, response) {
                        console.log("Status: " + e.status + " message: " + message);
                    },
                    success: function(obj) {
                        if (obj.success) {
                            var lsCommonPatrolBO = obj.lsCommonPatrolBO;
                            $("#potralUser").val(lsCommonPatrolBO.potralUser);

                            var lsSystemDepartmentBOs = obj.lsSystemDepartmentBOs;
                            var lsMonitorRouteAreaBOs = obj.lsMonitorRouteAreaBOs;
                            var lsWarehouseTrackUnitBOs = obj.lsWarehouseTrackUnitBOs;
                            var lsSystemUserBOs = obj.lsSystemUserBOs;
                            if (lsSystemUserBOs != null) {
                                var potralUsers = "<option value='" + lsCommonPatrolBO.potralUser + "' selected>"
                                        + obj.patrolUserName + "</option>";
                                for (var i = 0; i < lsSystemUserBOs.length; i++) {
                                    var baseObject = lsSystemUserBOs[i];
                                    potralUsers += "<option value='" + baseObject.userId + "'>" + baseObject.userName
                                            + "</option>";
                                }
                                $("#potralUser").append(potralUsers);
                            }
                            if (lsSystemDepartmentBOs != null) {
                                var belongToPorts = "";
                                for (var i = 0; i < lsSystemDepartmentBOs.length; i++) {
                                    var baseObject = lsSystemDepartmentBOs[i];
                                    if (baseObject.organizationId == lsCommonPatrolBO.belongToPort) {
                                        belongToPorts += "<option value='" + baseObject.organizationId + "' selected>"
                                                + baseObject.organizationName + "</option>";
                                    } else {
                                        belongToPorts += "<option value='" + baseObject.organizationId + "'>"
                                                + baseObject.organizationName + "</option>";
                                    }
                                }
                                $("#belongToPort1").append(belongToPorts);
                            }
                            if (lsMonitorRouteAreaBOs != null) {
                                var belongToAreas = "";
                                for (var i = 0; i < lsMonitorRouteAreaBOs.length; i++) {
                                    var baseObject = lsMonitorRouteAreaBOs[i];
                                    if (baseObject.routeAreaId == lsCommonPatrolBO.belongToArea) {
                                        belongToAreas += "<option value='" + baseObject.routeAreaId + "' selected>"
                                                + baseObject.routeAreaName + "</option>";
                                    } else {
                                        belongToAreas += "<option value='" + baseObject.routeAreaId + "'>"
                                                + baseObject.routeAreaName + "</option>";
                                    }
                                }
                                $("#belongToArea").append(belongToAreas);
                            }
                            if (lsWarehouseTrackUnitBOs != null) {
                                var trackUnitNumbers = "";
                                for (var i = 0; i < lsWarehouseTrackUnitBOs.length; i++) {
                                    var baseObject = lsWarehouseTrackUnitBOs[i];
                                    if (baseObject.trackUnitId == lsCommonPatrolBO.trackUnitNumber) {
                                        trackUnitNumbers += "<option value='" + baseObject.trackUnitNumber
                                                + "' selected>" + baseObject.trackUnitNumber + "</option>";
                                    } else {
                                        trackUnitNumbers += "<option value='" + baseObject.trackUnitNumber + "'>"
                                                + baseObject.trackUnitNumber + "</option>";
                                    }
                                }
                                $("#trackUnitNumber").append(trackUnitNumbers);
                            }
                            /*
                             * $("#belongToArea
                             * option[value='"+lsCommonPatrolBO.belongToArea+"']").attr("selected","selected");
                             * $("#belongToPort1
                             * option[value='"+lsCommonPatrolBO.belongToPort+"']").attr("selected","selected");
                             * $("#trackUnitNumber
                             * option[value='"+lsCommonPatrolBO.trackUnitNumber+"']").attr("selected","selected");
                             */
                        } else {
                            bootbox.alert($.i18n.prop("map.routeArea.edit.getInfoFailure"));
                        }
                    }

                });
            } else {
                bootbox.alert($.i18n.prop("map.routeArea.edit.oneChoice"));
            }
            // 清除选中值
            $('input[name="patrolIds"]:checked').each(function() {
                $(this).attr("checked", false);
            });
        });
$('#deletePatrol').on('click', function() {
    // 获取选中的值
    var checkValues = [];
    $('input[name="patrolIds"]:checked').each(function() {
        checkValues.push($(this).val());
    });
    if (checkValues.length == 0) {
        bootbox.alert($.i18n.prop("map.routeArea.edit.noChoice"));
    } else {
        bootbox.confirm($.i18n.prop("map.routeArea.delete.confirm"), function(result) {
            if (result) {
                $.ajax({
                    type: "POST",
                    url: getRootPath() + "patrol/deletePatrols.action?ids=" + checkValues,
                    cache: false,
                    async: false,
                    error: function(e, message, response) {
                        console.log("Status: " + e.status + " message: " + message);
                    },
                    success: function(obj) {
                        if (obj.success) {
                            clearAllOverlays(true);
                            bootbox.success($.i18n.prop("map.routeArea.delete.success"));
                            findAllPatrolStatus(true);
                        } else {
                            bootbox.error($.i18n.prop("map.routeArea.delete.failure"));
                        }
                    }
                });
            }
        });
    }
});

$("#patrolReset").click(function() {
    $("#patrolForm")[0].reset();
});

$('#addLanderMarker').on('click', function() {
    GisEventCallBack("click", getLandMarkerCoordByClick);
    $("#patrolList").addClass("hidden");
    $("#addRapoint").addClass("hidden");
    $("#addLandMarker").removeClass("hidden");
    $("#addLandMarkerBtn").removeClass("hidden");
    $("#editLandMarkerBtn").addClass("hidden");
    $("#mergePatrol").addClass("hidden");
    $("#addPatrolBtn").addClass("hidden");
    $("#editPatrolBtn").addClass("hidden");
    $("#vehicelTrackList").addClass("hidden");
    $("#planRouteAreaList").addClass("hidden");
    $("#landId").val("");
    $("#landName").val("");
    // $("#landImage").html("");
    $("#latitude").val("");
    $("#longitude").val("");
    $("#description").val("");
    // 清除选中值
    $('input[name="landIds"]:checked').each(function() {
        $(this).attr("checked", false);
    });
    // $.ajax({
    // type: "POST",
    // url: getRootPath() + "patrol/findSelectData.action",
    // cache : false,
    // async : false,
    // error : function(e, message, response) {
    // console.log("Status: " + e.status + " message: " + message);
    // },
    // success: function(obj){
    //            
    // }
    // })
});
$('#editLanderMarker').on('click', function() {
    var checkValues = [];
    $('input[name="landIds"]:checked').each(function() {
        checkValues.push($(this).val());
    });
    if (checkValues.length == 1) {
        $("#landId").val(checkValues[0]);
        $("#patrolList").addClass("hidden");
        $("#addRapoint").addClass("hidden");
        $("#addLandMarkerBtn").addClass("hidden");
        $("#addLandMarker").removeClass("hidden");
        $("#editLandMarkerBtn").removeClass("hidden");
        $("#planRouteAreaList").addClass("hidden");
        $("#mergePatrol").addClass("hidden");
        $("#addPatrolBtn").addClass("hidden");
        $("#vehicelTrackList").addClass("hidden");
        $("#editPatrolBtn").removeClass("hidden");
        $("#landId").val("");
        $("#landName").html("");
        // $("#landImage").html("");
        $("#latitude").html("");
        $("#longitude").html("");
        $("#description").html("");
        $.ajax({
            type: "POST",
            url: getRootPath() + "landmarker/findLandMarkerById.action?landId=" + checkValues,
            cache: false,
            async: false,
            error: function(e, message, response) {
                console.log("Status: " + e.status + " message: " + message);
            },
            success: function(obj) {
                var lsMonitorLandMarkerBO = obj.lsMonitorLandMarkerBO;
                if (lsMonitorLandMarkerBO) {
                    $("#landId").val(lsMonitorLandMarkerBO.landId);
                    $("#landName").val(lsMonitorLandMarkerBO.landName);
                    // $("#landImage").val(lsMonitorLandMarkerBO.landImage);
                    $('#landImage option[value=\"' + lsMonitorLandMarkerBO.landImage + '\"]').addClass("selected");
                    $("#latitude").val(lsMonitorLandMarkerBO.latitude);
                    $("#longitude").val(lsMonitorLandMarkerBO.longitude);
                    $("#description").val(lsMonitorLandMarkerBO.description);
                }
            }
        })
    }
});

$('#deleteLanderMarker').on('click', function() {
    // 获取选中的值
    var checkValues = [];
    $('input[name="landIds"]:checked').each(function() {
        checkValues.push($(this).val());
    });
    if (checkValues.length == 0) {
        bootbox.alert($.i18n.prop("map.routeArea.edit.noChoice"));
    } else {
        bootbox.confirm($.i18n.prop("map.routeArea.delete.confirm"), function(result) {
            if (result) {
                $.ajax({
                    type: "POST",
                    url: getRootPath() + "landmarker/deleteLandMarkerById.action?landId=" + checkValues,
                    cache: false,
                    async: false,
                    error: function(e, message, response) {
                        console.log("Status: " + e.status + " message: " + message);
                    },
                    success: function(obj) {
                        if (obj.result = "success") {
                            GisClearOverlays(overlaysArray);
                            bootbox.success($.i18n.prop("map.routeArea.delete.success"));
                            findAllLandMarkers();// 更新列表
                        } else {
                            bootbox.error($.i18n.prop("map.routeArea.delete.failure"));
                        }
                    }
                });
            }
        });
    }
    // 刷新列表
});

/**
 * html5 websocket获取推送报警信息
 */
function connectWebSocketInfo() {
    if (!webSocketGPS) {
        webSocketGPS = new ReconnectingWebSocket(wsGpsUrl);
    }
    webSocketGPS.onopen = function(event) {
        // alert("Connect Sussess!");
    };
    webSocketGPS.onmessage = function(event) {
        // var alarm = event.data;//获取的是对象
        var gpsdata = strToJson(event.data);// 将获取的json对象转为可用的对象
        var messageContent = gpsdata.messageContent;
        if (gpsdata.messageType == "VEHICLE_ALARM") {
            if (typeof (trackingTripId) != "undefined" && trackingTripId == messageContent.tripId) {
                var alarmIcon = getAlarmIconByTypeAndLevel(messageContent.alarmTypeId, messageContent.alarmLevelId);
                var alarmMarker = GisCreateMarker({
                    lat: messageContent.alarmLatitude,
                    lng: messageContent.alarmLongitude
                }, alarmIcon, $.i18n.prop('AlarmType.' + messageContent.alarmTypeId), JSON.stringify(messageContent));
                $("#bsound")[0].play();
                // var alarmContent =
                // createAlarmContent(messageContent.alarmTypeId);
                var alarmContent = createAlarmContent(messageContent);
                GisAddEventForVehicle(alarmMarker, "click", function() {
                    var d = dialog({
                        id: messageContent.alarmId,
                        title: vehiclePlateNUmber,// $.i18n.prop('trip.info.message'),
                        content: alarmContent,
                        resize: true
                    });
                    d.show();
                });
                // GisShowInfoWindow(alarmMarker,alarmContent,false);

                alarmArr.push(alarmMarker);
                //findAlarmsByTripId(messageContent.tripId)
                pop(VEHICLE_INFO.vehicleId, false);
            }
        } else if (gpsdata.messageType == "VEHICLE_GPS") {
            // alert(gpsdata.messageType +
            // "--------"+messageContent+"-----"+trackingTripId+"------"+messageContent.direction);

            if (typeof (trackingTripId) != "undefined" && trackingTripId == messageContent.tripId) {
                GisClearOverlays(lengthArr);
                GisClearOverlays(svgMarkers);
                GisClearOverlays(vehicleMarkers);
                var localPoint = {
                    lat: messageContent.latitude,
                    lng: messageContent.longitude
                };
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
                    trackingLine = GisShowPolyLineInMap(tripBufferPoints, true, drawPolylineStyle);
                    overlaysArray.push(trackingLine);
                    /*var pointMarker = GisCreateMarker(localPoint,"static/images/pointIcon.png",""+messageContent.gpsId);
                    GisShowInfoWindow(pointMarker,""+messageContent.gpsId,false);*/
                    pointMarkers.push(pointMarker);
//                    var len = $.i18n.prop('map.label.deviationDistance')
//                            + (parseFloat(GisptToPolylineLength(localPoint, tripRoutePoints)) / 1000).toFixed(2)
//                            + "(KM)";
//                    var lengthLabel = GisCreateLabel(localPoint, len);
//                    lengthArr.push(lengthLabel);
                } else {
                    trackingLine = GisUpdatePolyLine(trackingLine, localPoint);
                    //var len = GisptToPolylineLength(localPoint,tripBufferPoints);
                    // GisCreateLabel(localPoint,len);
                }
                if (typeof (trackingMaker) != "undefined") {
                    trackingMaker.setMap(null);
                    //trackingMaker = undefined;
                }
                  
                var vehicleInfo = updateVehicleRealInfo(VEHICLE_INFO,messageContent); 
                createVehicleMarker(localPoint, vehicleInfo, "blue", "white");
            }

        }

        /* else if(gpsdata.messageType=="PORTAL_GPS"){
         	GisClearOverlays(patrolMarkers);
         	var data = gpsdata.messageContent;
         	var commonPatrolBO = data.commonPatrolBO;
         	var monitorVehicleGpsBO = data.monitorVehicleGpsBO;
         	var lsSystemRoleBO = data.lsSystemRoleBO;
         	var patrolIcon = "";
         	if(lsSystemRoleBO.roleName=="enforcementPatrol"){
        		patrolIcon = "images/gis/xunluo.png";
        	}else if(lsSystemRoleBO.roleName=="escortPatrol"){
        		patrolIcon = "images/gis/husongxunluo.png";
        	}else{
        		patrolIcon = "images/gis/husongxunluo.png";
        	}
         	var pstrolMaker =  GisCreateMarker({
        		lat : monitorVehicleGpsBO.latitude,
        		lng : monitorVehicleGpsBO.longitude
        	}, patrolIcon, ""+monitorVehicleGpsBO.gpsId,JSON.stringify(data.commonPatrolBO) );//
         	patrolMarkers.push(pstrolMaker);
         	
         }*/
    };
    webSocketGPS.onclose = function(event) {

    };
}

/**
 * 更新车辆关于关锁实时信息
 * @param oldMsg
 * @param newMsg
 */
function updateVehicleRealInfo(oldMsg,newMsg){
    oldMsg.electricityValue = newMsg.electricityValue;
    oldMsg.elockSpeed = newMsg.elockSpeed;
    oldMsg.elockStatus = newMsg.elockStatus;
    oldMsg.locationTime = formatDateTime(new Date(newMsg.locationTime.time));
    oldMsg.poleStatus = newMsg.poleStatus;
    oldMsg.brokenStatus = newMsg.brokenStatus;
    oldMsg.latitude = newMsg.latitude;
    oldMsg.longitude = newMsg.longitude;
    return oldMsg;
}

function createRealTimeContent(vehicle) {
    var html = '';
    html += '<div class="alert_box">';
    html += '	<div class="alert_box_content">';
    html += '		<div class="alert-title">';
    html += '		    <div class="pull-left">';
    html += '			    <div class="Vehicle_bubble"><img alt="" src="static/images/Safetruck.png"/></div>';
    html += '				<b>' + vehicle.trackingDeviceNumber + '</b>';
    html += '           </div>';
    html += '			<div class="pull-right">checkinTime:' + vehicle.checkinTime + '</div>';
    html += '		</div>';
    html += '	    <div class="block-location">'
    html += '           <h4>Custom Metrics</h4>'
    html += '            <p><label><input type="checkbox">Apsum is simply dummy text of the printing</label></p>'
    html += '      </div>'
    html += '		<div class="alert_table">';
    html += '			<table class="table table-condensed table-striped table-hover">';
    html += '			     <tr><th>checkinTime:</th>';
    html += '			         <td>' + vehicle.checkinTime + '</td></tr>';
    html += '		    </table>';
    html += '	    </div>';
    html += '	  <div class="clearfix"></div>';
    html += "	    <a   id=\"vehicleHandlerBtn_" + vehicle.vehicleId
            + "\" style=\"text-decoration:underline\"  href=\"javascript:void(0)\">";
    html += $.i18n.prop("vehicle.label.track") + "";
    html += '	     </a>';
    //	html += "	    <a  id=\"addAlarmBtn\" onclick=\"addAlarmByManual()\" href=\"javascript:void(0)\" style=\"text-decoration:underline;margin-left:15px\">";
    //	html +=                 $.i18n.prop("添加报警")+"";
    //	html += '	     </a>';
    html += '     </div>';
    html += '   </div>';
    html += '</div>';
}

function getSysParam() {
    $.get(root + "/paramsMgmt/list.action", "", function(obj) {
        if (obj) {
            var data = JSON.parse(obj);
            for (var int = 0; int < data.rows.length; int++) {
                var param = data.rows[int];
                if (param.paramCode == CIRCLE_BUFFER) {
                    cirlceBuffer = param.paramValue;
                }
            }
        }

    });
}

/**
 * 获取当前国家所有口岸
 * @param callback
 */
function getAllPorts(callback) {
    $.get(root + "/deptMgmt/findAllPorts.action", "", function(obj) {
        if (obj) {
            var data = JSON.parse(obj);
            callback(data.rows);
        }
    });
}


function loadPorts(ports) {
    var html = [];
    html.push('<option value=""></option>');
    ports.forEach(function(port) {
        html.push('<option title="'+port.latitude+','+port.longitude+'" value="' + port.organizationId + '">' + port.organizationName + "</option>");
    });
    $("#startId, #endId").html(html.join(''));
    getPlanRouteByStartEndPort();
}

$.fn.extend({
    /**
     * 检测数组中的某些元素是否满足指定函数的判断。
     * func(index, element, array)：元素索引，元素值，被遍历的数组
     */
    some: function(func) {
        var _self = this;
        if ($.isFunction(func)) {
            return $.grep(this, function(elem, i) {
                return !!func.call(elem, i, elem, _self) !== false;
            }).length > 0;
        }
        return true;
    }
});

/**
 * 校验路径规划输入框,menuType=="0"
 * @returns
 */
function checkRouteAreaInput() {
    if ($("#routeAreaName, #routeAreaStatus, #routeAreaBuffer, #startId, #endId, #distance, #routeCost").some(
            inputIsEmpty)) {
        bootbox.alert($.i18n.prop("trip.message.input.required"));
        return false;
    }
    //口岸用户只能添加本口岸相关路线
    if(['contromRoomUser'].indexOf(roleName) < 0) {
    	if ($("#startId").val() != organizationId && $("#endId").val() != organizationId) {
            bootbox.alert($.i18n.prop('map.routeArea.mustBeCurrentPort'));
            return false;
        }
    }
    //缓冲区只能是数字
    if (!/^\d+$/.test($("#routeAreaBuffer").val())) {
        bootbox.alert($.i18n.prop('common.message.bufferMustDigit'));
        return false;
    }
    //距离只能是浮点数
    var distance = formatFloat($("#routeDistanceFormatted").val());
    if (!/^[\d,\.]*$/.test($("#routeDistanceFormatted").val()) || isNaN(distance) || !/^\d*\.?\d*$/g.test(distance)) {
        bootbox.alert($.i18n.prop('common.message.distanceMustDigit'));
        return false;
    }
    //路线用时只能是数字
    if (!/^\d+$/g.test($("#routeCost").val())) {
        bootbox.alert($.i18n.prop('common.message.timecostMustDigit'));
        return false;
    }
    if ($("#startId").val() == $("#endId").val()) {
        bootbox.alert($.i18n.prop('common.message.startEndPortSame'));
        return false;
    }
    return true;
}
/**
 * 将格式化的数值转为浮点数。如-123,456.78 -> -123456.78
 * @param s
 * @returns
 */
function formatFloat(s) {
	return parseFloat(s.replace(/[^-?\d\.]/g, ""));
}
/**
 * 将浮点数转为格式化的数值。如-123456.78 -> -123,456.78
 * @param s
 * @returns
 */
function toMoney(s) {
	function f(s) {
		s = String(s);
		if (s.length <= 3) {
			return s;
		}
		return f(s.slice(0, s.length - 3)) + ',' + s.slice(s.length - 3);
	}
	s = parseFloat(s) || 0;
	var u = ''; // 负数符号
	if (/^-/.test(s)) {
		u = '-';
		s = Math.abs(s);
	}
	return u + f(parseInt(s)) + s.toString().replace(/-?\d*(\.\d*)?/, '$1');
}
/**
 * 校验规划场地输入框是否有空值,menuType=="1"
 * 
 * @returns
 */
function checkSiteInput() {
    if ($("#routeAreaName, #routeAreaType, #routeAreaStatus").some(inputIsEmpty)) {
        bootbox.alert($.i18n.prop("trip.message.input.required"));
        return false;
    }
    return true;
}

/**
 * 校验区域规划输入框是否有空值,menuType=="2"
 * @returns
 */
function checkLandMarkerInput() {
    if ($("#landName, #latitude, #longitude, #description").some(inputIsEmpty)) {
        bootbox.alert($.i18n.prop("trip.message.input.required"));
        return false;
    }
    return true;
}

/**
 * 判断文本框是否空值
 * @param {Object} index
 * @param {Object} element
 * @param {Object} array
 */
function inputIsEmpty(index, element, array) {
    return /^\s*$/.test(element.value);
}

function trimText() {
    $("input[type=text]").each(function() {
        $(this).val($.trim($(this).val()));
    });
}

$(function() {
    $(".gm-china .gmnoprint:last-child").css({
        "right": "15px"
    });
    $(".gm-bundled-control .gmnoprint,.gm-china .gmnoprint:last-child").css({
        "right": "15px"
    });

    $("#resetMap").on('click', function() {
        mapInitPort();
    });
    $("#saveVehicleTrack").on('click', function() {
        saveVehicleTrack();
    });
    $("#resetVehicle").on('click', function() {
        findAllVehicleStatusInit(true);
    });

    $("#panelList").removeClass("hidden");//车辆列表
    $("#addRapoint").addClass("hidden");//添加车辆历史轨迹界面
    $("#addLandMarker").addClass("hidden");
    $("#planRouteAreaList").addClass("hidden");//路径规划列表
    $("#patrolList").addClass("hidden");//路径规划列表
    $("#addDelUpdate").addClass("hidden");//行程增删改
    $("#classify").removeClass("hidden");//车辆分级图标
    getSysParam();
    initMap();
    mapInitPort();
    tripStatus = "1";
    queryAndResetMainPage();//添加主页面定时刷新，增加下面的内容显示
    $('#handlerPoint').on('click', function() {
        showOrHiddenPoint1();
    });
    $('#ceju').on('click', function() {
        mapMeasure();
    });
    getPatrolLocationInit();
    connectWebSocketInfo();

    //返回按钮
    $("#backBtn").on("click", function() {
        if (menuType == '0') {
            var id = $("#routeAreaId").val();
            $("#routeAreaList").click();
            $("#planRouteAreaList").find("[name=routeAreaIds][value=" + id + "]").click();
        } else if (menuType == '1') {
            $("#siteList").click();
        } else if (menuType == '2') {
            $("#areaList").click();
        }
    });
    $("#planRouteAreaList").delegate("input", "click", function() {
        $(this).closest("li").toggleClass("li-selected");
    });
});
