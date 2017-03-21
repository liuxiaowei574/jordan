/**
 * 特别注意事项：overlays是从调用地图之后特有的数组参数，可以进行重写，但是针对同一个地图页面，只可以放同一种类型的覆盖物，多种类型会发生混乱
 * 为了避免混乱，尽量重新建立覆盖物数组参数，然后拿来使用，用完之后要记得销毁。
 */
var bufferColor = "";// 缓冲区颜色
var bufferWidth = "";// 缓冲区颜色
var cirlceBuffer = "";
var previewArr = [];// 图层预览
var vehicleMarkers = []; // 删除车辆覆盖物marker时用
var svgMarkers = []; // 删除车辆覆盖物marker时用
var patrolMarkers = []; // 删除巡逻队覆盖物marker时用
var pointMarkers = [];// 删除点marker时用
var showPointMarkers = [];// 用于是否显示轨迹坐标点详细信息
var overlaysArray = [];// 删除其他覆盖物时用
var alarmDealArray = [];// 删除报警点及巡逻车时使用
var overlaysArrayRoute = [];// 删除其他覆盖物时用
var overlaysPlanRoute = [];// 删除预定义路线
var landerMarkers = [];// 添加地物时使用
var alarmArr = [];// 用于清楚报警点
var circleArr = new Array();// 用于清除圆
var lengthArr = new Array();// 用于清楚长度显示

var trackingLine = undefined;// 车辆的行走路线
var trackingTripId = undefined;// 轨迹页面的行程Id
var trackingMaker = undefined;// 轨迹车辆的Maker
var tripBufferPoints = new Array();// 存储缓冲车辆数据
var tripRoutePoints = new Array();// 存储预定义路线
var markerclusterer;// 车辆markers聚合对象
var partolclusterer;// 巡逻车markers聚合对象
var alarmMakerHasForDeal = null;// 用于添加处理点公共变量
var refreshMainTime = 10000;// 10秒钟更新一次数据
var refreshTimeoutValue = -1; // 主页面定时刷新id
var patrolLocation = null;
var replayIcon = "images/gis/truck1.png";
var alarmIdForRecord = undefined;
var routeAreaIdByVehicle = "";
var raPointsArr = [];
var tripIdByVehicle = "";
var vehiclePlateNUmber = "";
var showPointMsg;
// 车辆svg图
var patrolPath = "M 731.543 348.852 h -27.41 V 93.735 c 0 -5.04 -2.865 -9.64 -7.405 -11.847 c 0 0 -15.48 -17.325 -31.985 -25.78 c -24.055 -12.32 -70.95 -23.78 -72.965 -23.78 H 455.096 c -1.575 0 -32.057 0 -56.377 10.34 c -19.697 9.61 -28.215 12.87 -54.592 30.745 c -5.105 1.955 -8.507 6.837 -8.507 12.32 v 263.119 h -27.412 v 26.657 h 27.412 v 583.959 c 0 3.75 1.637 7.34 4.442 9.865 l 39.322 34.815 c 2.392 2.14 5.512 3.31 8.727 3.31 h 251.937 c 2.96 0 5.825 -1.01 8.16 -2.84 l 50.915 -40.2 c 3.185 -2.49 5.01 -6.3 5.01 -10.335 V 375.509 h 27.41 V 348.852 Z M 422.139 318.637 l 204.859 -0.63 l 44.775 40.077 l -47.705 47.01 H 418.704 l -48.017 -46.38 l 51.485 -40.045 L 422.139 318.637 Z M 361.959 593.858 h 41.022 l 0.095 142.065 l -41.117 39.765 v -181.8 V 593.858 Z M 402.889 426.424 l 0.092 141.095 h -40.99 V 386.914 L 402.889 426.424 Z M 421.476 754.768 l 201.082 3.4 l 42.88 38.47 l -75.46 46 h -136.9 l -81.605 -39.54 l 49.972 -48.33 H 421.476 Z M 640.868 739.198 v -145.34 h 36.955 v 178.49 l -36.925 -33.15 H 640.868 Z M 677.788 567.518 h -36.955 V 425.541 l 36.99 -36.422 v 178.43 L 677.788 567.518 Z M 677.788 101.96 v 226.159 l -33.87 -30.34 L 589.633 59.045 l 88.155 42.882 V 101.96 Z M 457.521 58.635 h 2.552 l -54.287 239.302 l -43.795 34.09 V 94.807 l 95.562 -36.172 H 457.521 Z";
var truckPath = "M841.728 702.0544c6.5536 0 11.8784 5.3248 11.8784 12.288l0 26.624c0 6.5536-5.3248 12.288-11.8784 12.288l-104.8576 0c-6.5536 0-11.8784-5.3248-11.8784-12.288l0-26.624c0-6.5536 5.3248-12.288 11.8784-12.288L841.728 702.0544zM841.728 303.5136c6.5536 0 11.8784 5.3248 11.8784 11.8784l0 26.624c0 6.5536-5.3248 12.288-11.8784 12.288l-104.8576 0c-6.5536 0-11.8784-5.3248-11.8784-12.288L724.992 315.392c0-6.5536 5.3248-11.8784 11.8784-11.8784L841.728 303.5136zM295.3216 303.5136c6.5536 0 12.288 5.3248 12.288 11.8784l0 26.624c0 6.5536-5.3248 12.288-12.288 12.288L190.464 354.304c-6.5536 0-11.8784-5.3248-11.8784-12.288L178.5856 315.392c0-6.5536 5.3248-11.8784 11.8784-11.8784L295.3216 303.5136zM448.9216 303.5136c6.5536 0 11.8784 5.3248 11.8784 11.8784l0 26.624c0 6.5536-5.3248 12.288-11.8784 12.288L344.064 354.304c-6.5536 0-11.8784-5.3248-11.8784-12.288L332.1856 315.392c0-6.5536 5.3248-11.8784 11.8784-11.8784L448.9216 303.5136zM295.3216 698.7776c6.5536 0 12.288 5.3248 12.288 12.288l0 26.624c0 6.5536-5.3248 11.8784-12.288 11.8784L190.464 749.568c-6.5536 0-11.8784-5.3248-11.8784-11.8784l0-26.624c0-6.5536 5.3248-12.288 11.8784-12.288L295.3216 698.7776zM448.9216 698.7776c6.5536 0 11.8784 5.3248 11.8784 12.288l0 26.624c0 6.5536-5.3248 11.8784-11.8784 11.8784L344.064 749.568c-6.5536 0-11.8784-5.3248-11.8784-11.8784l0-26.624c0-6.5536 5.3248-12.288 11.8784-12.288L448.9216 698.7776zM953.5488 303.9232c30.72 0 55.296 24.9856 55.296 55.7056 0 0 2.8672 60.2112 2.8672 165.888 0 105.6768-2.8672 170.3936-2.8672 170.3936 0 30.72-24.9856 55.7056-55.296 55.7056l-28.672 0c-30.72 0-30.72-447.2832 0-447.2832L953.5488 304.3328zM917.0944 741.376c0 0 33.9968-1.2288 44.2368-2.048 19.6608-2.048 32.768-13.9264 32.768-31.5392 0 0 4.5056-105.6768 4.5056-183.5008 0-77.824-4.5056-180.224-4.5056-180.224 0-17.2032-14.7456-29.4912-32.768-31.5392-12.288-1.2288-42.5984-2.4576-42.5984-2.4576L917.0944 741.376zM943.3088 310.8864c17.6128 0 31.9488 14.336 31.9488 31.9488 0 0 15.5648 72.9088 15.5648 184.32 0 111.4112-15.5648 184.32-15.5648 184.32 0 17.6128-14.336 31.9488-31.9488 31.9488l-249.4464 0c-17.6128 0-19.6608-14.336-19.6608-31.9488l0-368.64c0-17.6128 4.096-31.9488 21.7088-31.9488L943.3088 310.8864zM892.928 697.5488c0 0 9.0112-78.6432 9.0112-169.984 0-91.7504-9.0112-171.6224-9.0112-171.6224l53.6576-22.1184c0 0 9.0112 107.7248 9.0112 192.512 0 84.7872-9.0112 193.3312-9.0112 193.3312L892.928 697.5488zM888.4224 697.5488c0 0 9.0112-78.6432 9.0112-169.984 0-91.7504-9.0112-171.6224-9.0112-171.6224l53.248-22.1184c0 0 9.0112 107.7248 9.0112 192.512 0 84.7872-9.0112 193.3312-9.0112 193.3312L888.4224 697.5488zM781.9264 734.0032 924.0576 734.0032 862.6176 712.2944 781.9264 712.2944ZM781.9264 321.536 924.0576 321.536 862.6176 343.2448 781.9264 343.2448ZM878.1824 354.304c0 0 10.24 59.8016 10.24 174.08 0 114.2784-10.24 171.6224-10.24 171.6224L684.032 700.0064 684.032 354.304 878.1824 354.304zM871.6288 354.304c0 0 10.24 59.8016 10.24 174.08 0 114.2784-10.24 171.6224-10.24 171.6224l-193.7408 0L677.888 354.304 871.6288 354.304zM975.2576 345.2928l-13.5168 2.048 5.3248 51.6096 17.2032 9.8304c0 0-1.6384-18.0224-4.096-33.5872C978.1248 358.8096 975.2576 345.2928 975.2576 345.2928zM938.3936 320.7168l3.2768-10.24c20.8896 0 33.5872 17.2032 33.5872 34.4064l-13.5168 2.048c0 0-2.048-10.6496-7.7824-16.7936C948.224 324.4032 938.3936 320.7168 938.3936 320.7168zM975.2576 709.0176l-13.5168-2.048 5.3248-51.6096 17.2032-9.8304c0 0-1.6384 18.0224-4.096 33.5872C978.1248 695.5008 975.2576 709.0176 975.2576 709.0176zM938.3936 733.5936l3.2768 10.24c20.8896 0 33.5872-17.2032 33.5872-34.4064l-13.5168-2.048c0 0-2.048 10.6496-7.7824 16.7936C948.224 729.9072 938.3936 733.5936 938.3936 733.5936zM956.8256 441.5488c0 0 4.096 38.912 4.096 82.3296 0 43.4176-4.096 82.7392-4.096 82.7392l30.3104 14.336c0 0 3.6864-51.6096 3.6864-96.256 0-44.6464-3.6864-97.4848-3.6864-97.4848L956.8256 441.5488zM978.1248 438.6816l4.096 0 0 170.8032-4.096 0 0-170.8032ZM970.3424 442.368l4.096 0 0 163.4304-4.096 0 0-163.4304ZM10.24 310.8864l615.6288 0 0 430.8992-615.6288 0 0-430.8992ZM20.0704 730.7264l596.3776 0L616.448 321.536 20.0704 321.536 20.0704 730.7264zM604.16 333.824l0 385.024L31.9488 718.848l0-385.024L604.16 333.824zM625.8688 310.8864l32.3584 0 0 430.8992-32.3584 0 0-430.8992ZM641.8432 400.5888l31.9488 0 0 250.6752-31.9488 0 0-250.6752ZM696.7296 712.2944l66.3552 0 0 21.2992-66.3552 0 0-21.2992ZM696.7296 321.536l66.3552 0 0 21.2992-66.3552 0 0-21.2992ZM781.9264 734.0032 781.9264 740.5568 888.4224 740.5568 924.0576 734.0032ZM696.7296 734.0032 709.4272 740.5568 762.6752 740.5568 762.6752 734.0032ZM871.6288 355.5328 935.5264 326.8608 931.84 325.2224 859.7504 354.304ZM871.6288 696.32 935.5264 726.2208 931.84 727.8592 856.8832 700.0064ZM803.6352 447.6928c4.9152 0 9.4208 4.096 9.4208 9.0112l0 134.7584c0 4.9152-4.096 9.0112-9.4208 9.0112l-65.9456 0c-4.9152 0-9.4208-4.096-9.4208-9.0112l0-134.7584c0-4.9152 4.096-9.0112 9.4208-9.0112L803.6352 447.6928zM802.4064 451.3792c3.6864 0 6.9632 3.2768 6.9632 6.9632l0 131.8912c0 3.6864-3.2768 6.9632-6.9632 6.9632l-50.3808 0c-3.6864 0-6.9632-3.2768-6.9632-6.9632l0-131.8912c0-3.6864 3.2768-6.9632 6.9632-6.9632L802.4064 451.3792zM788.0704 451.3792c3.6864 0 6.9632 3.2768 6.9632 6.9632l0 131.8912c0 3.6864-3.2768 6.9632-6.9632 6.9632l-50.3808 0c-3.6864 0-6.9632-3.2768-6.9632-6.9632l0-131.8912c0-3.6864 3.2768-6.9632 6.9632-6.9632L788.0704 451.3792zM728.2688 456.704l0 134.7584c0 4.9152 4.096 9.0112 9.4208 9.0112l9.4208 0c-4.9152 0-9.4208-4.096-9.4208-9.0112l0-134.7584c0-4.9152 4.096-9.0112 9.4208-9.0112l-9.4208 0C732.3648 447.6928 728.2688 451.7888 728.2688 456.704zM625.8688 310.8864l7.7824 0 0 430.8992-7.7824 0 0-430.8992ZM498.4832 737.28 10.24 741.7856 633.6512 741.7856 633.6512 737.28ZM48.3328 405.9136l539.4432 0 0 5.3248-539.4432 0 0-5.3248ZM48.3328 635.2896l539.4432 0 0 5.3248-539.4432 0 0-5.3248ZM48.3328 674.6112l531.6608 0 0 5.3248-531.6608 0 0-5.3248ZM48.3328 368.2304l531.6608 0 0 5.3248-531.6608 0 0-5.3248ZM48.3328 597.1968l539.4432 0 0 5.3248-539.4432 0 0-5.3248ZM48.3328 444.0064l539.4432 0 0 5.3248-539.4432 0 0-5.3248ZM48.3328 482.5088l539.4432 0 0 5.3248-539.4432 0 0-5.3248ZM48.3328 559.104l539.4432 0 0 5.3248-539.4432 0 0-5.3248ZM48.3328 520.6016l539.4432 0 0 5.3248-539.4432 0 0-5.3248Z";
var patrolPathNew = "M138.63,63.305h-5.48V12.282c0-1.007-0.58-1.927-1.485-2.37c0,0-3.095-3.465-6.395-5.155 C120.46,2.292,111.08,0,110.675,0H83.34c-0.315,0-6.412,0-11.275,2.07c-3.94,1.92-5.645,2.572-10.92,6.147 c-1.02,0.392-1.7,1.367-1.7,2.465v52.622h-5.482v5.332h5.482V185.43c0,0.75,0.327,1.465,0.887,1.975l7.865,6.96 c0.478,0.43,1.103,0.66,1.745,0.66h50.387c0.59,0,1.165-0.2,1.63-0.565l10.185-8.04c0.635-0.5,1.005-1.26,1.005-2.065V68.637 h5.48V63.305z M76.747,57.262l40.972-0.127l8.955,8.017l-9.545,9.402H76.06l-9.602-9.277l10.297-8.007L76.747,57.262z M64.712,112.31h8.202l0.02,28.41l-8.222,7.955V112.31L64.712,112.31z M72.897,78.82l0.018,28.22h-8.197V70.917L72.897,78.82z M76.615,144.49l40.215,0.68l8.58,7.695l-15.095,9.2h-27.38l-16.32-7.91l9.995-9.665H76.615z M120.49,141.375V112.31h7.395 v35.695l-7.385-6.63H120.49z M127.88,107.04h-7.39V78.642l7.395-7.282v35.685L127.88,107.04z M127.88,13.927V59.16l-6.78-6.07 L110.25,5.345l17.63,8.575V13.927z M83.825,5.262h0.51l-10.857,47.86l-8.76,6.817V12.495L83.83,5.262H83.825z";
/**
 * 普通车辆图标
 * @param direction
 */
function createSafeIcon(direction) {
    return "static/images/Safetruck.png";
    // GisCreateSvgIcon(truckPath,0.05,parseInt(direction)-90,"#ccc","#31b59f");
}
/**
 * 风险车辆图标
 * @param direction
 */
function createRiskIcon(direction) {
    return "static/images/warningtruck.png";
    // return
    // GisCreateSvgIcon(truckPath,0.05,parseInt(direction)-90,"#ccc","#ff9f00");
}
/**
 * 危险车辆图标
 * @param direction
 */
function createDangerIcon(direction) {
    return "static/images/alarmtruck.png";
    // GisCreateSvgIcon(truckPath,0.05,parseInt(direction)-90,"#ccc","#d32e00");
}
/**
 * 巡逻队图标
 * @param direction
 */
function createPatrolIcon(direction) {
    // return GisCreateSvgIcon(patrolPath,0.05,direction,"black","#ccc");
    return "images/gis/xunluo.png";
    // GisCreateSvgIcon(patrolPathNew,0.26,direction,"#000000","#696969");
}

/**
 * menuType footer菜单类型 menuType
 * 路线规划0：0-路线；场地管理1：3-监管区域；区域管理2：1-安全区域，2-危险区域，4-区域划分；其他
 */
var menuType = "9";

var tripStatus = "1";// 1-行程进行中，3-行程结束
/**
 * 地图初始化
 */
var portIconSrc = "static/images/kouan.png";
function initMap() {
    var pointArr = {
        lat: 40.399660000000004,
        lng: 116.85329000000002
    };
    var params = {
        mapId: "map_canvas",// div地图id
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
}
$("#resetMap").on('click', function() {
    replay();

});
/**
 * 设置地图中心
 * @param homePointArr
 */
function replay() {
    // var pointArr={lat:40.36140605,lng:116.823673};
    var params = {
        mapId: "map_canvas",// div地图id
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
    // var homePoint = new google.maps.LatLng(40.36140605, 116.823673);
    var homePoint = new google.maps.LatLng(40.4009914, 116.915584);

    map.setCenter(homePoint);
    map.setZoom(16);
    // GisInitialize(pointArr,params);
}

var drawPolylineStyle = {
    "color": "#ffff00",
    "weight": 2,
    "opacity": 1
}
/**
 * 初始化画图工具
 */
function mapInitDrawingManagerAndDriving() {
    var drawPolylineStyle = {
        "color": "#ff0000",
        "weight": 2,
        "opacity": 0.36
    }
    var drawParams = {
        direct: "right_top",
        darwStyle: drawPolylineStyle
    };
    initDrawingManagerAndDriving(drawParams);
}

var labelArray = [];// 口岸label数组
var portArray = [];// 口岸图标数组
/**
 * 初始化口岸
 */
function mapInitPort() {
    var portUrl = getRootPath() + "port/findAllCommonPorts.action";
    $.ajax({
        type: "POST",
        url: portUrl,
        dataType: "json",
        cache: false,
        async: false,
        error: function(e, message, response) {
            console.log("Status: " + e.status + " message: " + message);
        },
        success: function(jsonObj) {
            var gpsData = jsonObj;
            if (gpsData.length > 0) {
                GisClearOverlays(labelArray);
                GisClearOverlays(portArray);
                var qdPortHTML = "";
                var zdPortHTML = "";
                $("#startPort").html("");
                $("#endPort").html("");
                var pointArray = [];
                labelArray = [];// 重新初始化
                portArray = [];// 重新初始化
                for ( var key in gpsData) {
                    var data = gpsData[key];
                    qdPortHTML += '<li><label>';
                    qdPortHTML += '<input type="checkbox" value=' + data.organizationId + ' name="qdPorts">';
                    qdPortHTML += '<img alt="' + data.organizationName + '" src="static/images/ic_04.png"/><span>';
                    qdPortHTML += data.organizationName + '</span></label></li>';

                    zdPortHTML += '<li><label>';
                    zdPortHTML += '<input type="checkbox" value=' + data.organizationId + ' name="zdPorts">';
                    zdPortHTML += '<img alt="' + data.organizationName + '" src="static/images/ic_04.png"/><span>';
                    zdPortHTML += data.organizationName + '</span></label></li>';

                    if (isNotNull(data.latitude) && isNotNull(data.longitude)) {
                        pointArray.push({
                            lat: data.latitude,
                            lng: data.longitude
                        });
                        var localPoint = {
                            lng: data.longitude,
                            lat: data.latitude
                        };
                        var portMarker = GisCreateMarkerAndLabel(localPoint, portIconSrc, "" + data.organizationName,
                                JSON.stringify(data));
                        var portLabel = GisCreateLabel(localPoint, data.organizationName);
                        labelArray.push(portLabel);
                        portArray.push(portMarker);
                        // GisSetShowFront(portMarker);//谷歌地图设置数字越大，越显示在最前

                        // 口岸弹出内容，预留
                        /*
                         * var currentContent ="test";
                         * GisShowInfoWindow(portMarker,currentContent,false);
                         */
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
        lat: 40.36140605,
        lng: 116.823673
    };
    alert(isWithinPolygon(pointArr, yanshiBoundary));

}
var polygonStyle = {
    "color": "#ff0000",
    "weight": 2,
    "opacity": 0.2,
    "fillColor": "#00ff00",
    "fillOpacity": 0.36,
}
var polygonStyleDanger = {
    "color": "#ffff00",
    "weight": 2,
    "opacity": 0.2,
    "fillColor": "#ff0000",
    "fillOpacity": 0.36,
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
    if (measureFlag) {
        if (undefined != $("#dvPanel") && null != $("#dvPanel")) {
            measureFlag = false;
            $("#dvPanel").attr("disabled", false)
            $("#dvPanel").empty();
            $("#legend").addClass("hidden")
            // $("#dvPanel").addClass("dvPanelshadow");
            GismapMeasure("dvPanel", getDistDuration);
            $(".toolscontainer").css({
                "background": "rgba(201,201,201,0.9)"
            });
        }
    } else {
        measureFlag = true;
        $("#legend").removeClass("hidden")
        $("#dvPanelParent").animate({
            right: -400
        }, "slow");
        GisClearDirectionsDisplay();
        $(".toolscontainer").css({
            "background": "rgba(241,241,241,0.9)"
        });
        // $("#dvPanelParent").addClass("hidden");
    }

}

function getDistDuration(obj) {
    $("#ceju").attr("disabled", true)
    $("#dvPanelParent").removeClass("hidden").animate({
        right: 0
    }, "slow").addClass("dvPanelshadow");
}
/**
 * 根据区域或线路编号获取
 */
function mapShowPoygonByRouteAreaId(routeAreaId) {
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
            GisClearOverlays(overlays);
            var points = jsonObj.jsonData;
            var drawType = jsonObj.routeType;
            var pointArray = [];
            if (points.length > 0) {
                for (var i = 0; i < points.length; i++) {
                    pointArray.push({
                        lat: points[i].latitude,
                        lng: points[i].longitude
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
        type: "POST",
        url: portUrl,
        data: param,
        dataType: "json",
        cache: false,
        async: false,
        error: function(e, message, response) {
            console.log("Status: " + e.status + " message: " + message);
        },
        success: function(jsonObj) {
            alert("add sucess");

        }
    });
}
/**
 * 查询报警点
 */
function findAlarmsByTripId(tripId) {

    $.get(root + "/vehicletrack/findAlarmsByTripId.action", {
        "tripId": tripId
    }, function(obj) {
        var needLoginFlag = false;
        if (typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
            needLoginFlag = needLogin(obj);
        }
        if (!needLoginFlag) {
            obj = JSON.parse(obj);
            if (obj.success) {
                // 车辆报警
                var alarmPoints = obj.lsMonitorAlarmVOs;// 车辆报警信息
                for (var i = 0; i < alarmPoints.length; i++) {
                    (function(x) {
                        var data = alarmPoints[i];
                        var alarmContent = createAlarmContent(data);
                        var loction = {
                            lat: data.alarmLatitude,
                            lng: data.alarmLongitude
                        };
                        var alarmIcon = getAlarmIconByTypeAndLevel(data.alarmTypeId, data.alarmLevelId);
                        var alarmMarker = GisCreateMarker(loction, alarmIcon, $.i18n.prop('AlarmType.'
                                + data.alarmTypeId), "");
                        GisAddEventForVehicle(alarmMarker, "click", function() {
                            var obj = {
                                data: data
                            };
                            var d = dialog({
                                id: data.vehicleStatusId,
                                title: vehiclePlateNUmber, // $.i18n.prop('trip.info.message'),
                                                            // $.i18n.prop("alarm.label.alarmContent")
                                content: alarmContent,
                                // okValue: '确 定',
                                resize: true,
                                left: '90%',
                                top: '20%'
                            }

                            );
                            d.show();
//                            // 点击报警点，弹出右上角该报警详细信息
//                            var alarmId = data.alarmId;
//                            var url = getRootPath() + "vehiclestatus/appointedAlarm.action?alarmId=" + alarmId;
//                            $("#alarmListTbody").html("");
//                            $.ajax({
//                                type: "POST",
//                                url: url,
//                                dataType: "json", // 数据类型
//                                success: function(obj) {
//                                    var obj = obj;
//                                    if (obj.success) {
//                                        var alarmData = obj.alarmList;
//                                        if (null != alarmData && alarmData.length > 0) {
//                                            $("#messagenumber").html(alarmData.length);
//                                            for ( var key in alarmData) {
//                                                (function(i) {
//                                                    var alarmReportVO = alarmData[i];
//                                                    var alarmIcon = getAlarmIconByTypeAndLevel(
//                                                            alarmReportVO.ALARM_TYPE_ID, alarmReportVO.ALARM_LEVEL_ID);
//
//                                                    var alarmTime = formatDateTime(new Date(alarmReportVO.ALARM_TIME));
//                                                    var areaText = "<tr><td style=\"cursor: pointer;\" id=\"vehicle_"
//                                                            + alarmReportVO.alarmId
//                                                            + "\">"
//                                                            + alarmReportVO.VEHICLE_PLATE_NUMBER
//                                                            + "</td><td style=\"text-align:left\"><img src="
//                                                            + alarmIcon
//                                                            + " alt='Alarm_Level_Icon'>"
//                                                            + (alarmReportVO.ALARM_TYPE_ID
//                                                                    && $.i18n.prop("AlarmType."
//                                                                            + alarmReportVO.ALARM_TYPE_ID) || '')
//                                                            + "</td><td>" + alarmTime + "</td></tr>"
//                                                    $("#alarmListTbody").append(areaText);
//                                                })(key);
//                                            }
//                                        }
//                                    }
//                                }
//                            });
//                            // 弹出右上角的报警信息
//                            document.getElementById("a").style.display = "block";
                        });
                        // GisShowInfoWindow(alarmMarker,alarmContent);
                        alarmArr.push(alarmMarker);
                    })(i)
                }
            }
        }

    });
}
/**
 * 隐藏蒙版
 */
function hideMask() {

    $("#mask").hide();
}
var alarmDataConfig = null;
/**
 * 根据关锁号获取轨迹坐标
 */
var alarmData = null;// 后台查询到的报警信息
function getVehiclePaths(data, type) {
    if (typeof (data) != "object") {
        data = JSON.parse(data);
    }
    $("#colorPanelParent").removeClass("hidden").animate({
        top: 0
    }, "slow").addClass("dvPanelshadow");
    if (data == null)
        return;
    var gpsVehicleInfo = data.data;
    // 如果是历史列表中的车辆，需要将回放的按钮等展示出来
    if (menuType == "8") {
        $("#bottomPanel").removeClass("hidden");
    }
    vehicleRealPathPointsArr = new Array();
    realVehicleDetailMarker = undefined;
    trackingDeviceNumberParam = gpsVehicleInfo.trackingDeviceNumber;
    var portUrl = getRootPath() + "monitorvehicle/findAllMonitorVehicleGpsByEclockNum.action?trackingDeviceNumber="
            + gpsVehicleInfo.trackingDeviceNumber + "&vehicleId=" + gpsVehicleInfo.vehicleId;
    $.ajax({
        type: "POST",
        url: portUrl,
        cache: false,
        //async: false,
        async: true,
        dataType: 'JSON',
        error: function(e, message, response) {
            console.log("Status: " + e.status + " message: " + message);
        },
        success: function(obj) {
            var alarmArray = [];
            if (null != obj) {
                if (obj.success) {
                    // 车辆轨迹
                    GisClearMarkerClusterer(markerclusterer);
                    GisClearOverlays(vehicleMarkers);
                    GisClearOverlays(alarmArr);
                    GisClearOverlays(svgMarkers);
                    GisClearOverlays(overlaysArrayRoute);
                    GisClearOverlays(overlaysPlanRoute);
                    // GisClearMarkerClusterer(partolclusterer);
                    // GisClearOverlays(patrolMarkers);
                    trackingTripId = obj.lsMonitorTripBO.tripId;
                    var jsonObj = obj.lsMonitorVehicleGpsBOs;// 车辆实时轨迹
                    if (jsonObj != null && jsonObj.length > 0) {
                        /*var lsMonitorRaPointBOs = obj.lsMonitorRaPointBOs;// 车辆预定义路线
                        if (lsMonitorRaPointBOs != null) {
                            mapShowRouteLine(lsMonitorRaPointBOs, type);
                        }*/
                        clearAllTimeout();// 清除所有的定时任务
                        //findOneVehicleGpsPlanRoute(jsonObj, obj.lsMonitorTripBO, gpsVehicleInfo, type);//显示车辆预定义路线
                        findOneVehicleGpsPlanRoute(gpsVehicleInfo);//显示车辆预定义路线
                        mapCreateTracking(jsonObj, obj.lsMonitorTripBO, gpsVehicleInfo, type);// 地图上展示车辆
                        vehiclePlateNUmber = gpsVehicleInfo.vehiclePlateNumber;
                        //findAlarmsByTripId(obj.lsMonitorTripBO.tripId);
                       
                    }
                  //  $('#vehicle_loading').addClass("hidden");
                    hideMask();
                    $('#progressModal').modal('hide');
                } else {
                    hideMask();
                    $('#progressModal').modal('hide');
                    bootbox.alert($.i18n.prop("map.routeArea.select.noVehicleLine"));
                }
            } else {
                hideMask();
                $('#progressModal').modal('hide');
                bootbox.alert($.i18n.prop("map.routeArea.select.noVehicleLine"));
            }
        }
    });
}

window.alarmDealId = "";
window.alarmDealObj = null;
/**
 * 弹出框点击处理
 */
var alarmMakerWaitForDeal = null;
var patrolAlarmMaker = null;
function alarmDealClick(data) {
    alarmData = data;
    var url = root + "/alarmdeal/alarmDealModalShow.action?alarmId=" + data.alarmId + "&vehicleId=" + data.vehicleId;
    $('#alarmHandlerModal').removeData('bs.modal');
    $('#alarmHandlerModal').modal({
        remote: url,
        show: false,
        backdrop: 'static',
        keyboard: false
    });
    $('#alarmHandlerModal').on('loaded.bs.modal', function(e) {
        $('#alarmHandlerModal').modal('show');
    });
    // 模态框登录判断
    $('#alarmHandlerModal').on('show.bs.modal', function(e) {
        var content = $(this).find(".modal-content").html();
        if (typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
            needLogin(content);
        }
    });
}

/**
 * 推送巡逻队
 */
var circle = null;// 报警点处理范围
var patrolLatLng = null;
function patrolHandlerClick(data) {
    alarmDataConfig = data;
    alarmData = data;
    if (cirlceBuffer == "") {
        cirlceBuffer = 5000;
    }
    var patrolUrl = root + "/alarmdeal/getPatrolsInCircle.action?radius=" + cirlceBuffer + "&alarmLatitude="
            + data.alarmLatitude + "&alarmLongitude=" + data.alarmLongitude;
    /*
     * var patrolUrl = root
     * +"/alarmdeal/getPatrolsInCircle.action?radius="+cirlceBuffer+"&alarmLatitude=" +
     * data.alarmLatitude
     * +"&alarmLongitude="+data.alarmLongitude+"&userId="+data.userId;
     */
    $.ajax({
        type: "POST",
        url: patrolUrl,
        dataType: "json",
        cache: false,
        async: false,
        error: function(e, message, response) {
            console.log("Status: " + e.status + " message: " + message);
        },
        success: function(jsonObj) {
            GisClearOverlays(circleArr);
            GisClearMarkerClusterer(partolclusterer);
            GisClearOverlays(patrolMarkers);
            patrolMarkers = new Array();
            if (jsonObj && jsonObj.length > 0) {
                for ( var key in jsonObj) {
                    (function(x) {
                        var obj = jsonObj[x];
                        $(document).undelegate("#centerSenMsgBtn_" + obj.patrolId, "click");
                        var loction = {
                            lat: obj.latitude,
                            lng: obj.longitude
                        };
                        var patrolMarker = GisCreateMarker(loction, "images/gis/xunluo.png", "" + obj.patrolId, JSON
                                .stringify(obj));
                        patrolMarkers.push(patrolMarker);
                        GisSetShowFront(patrolMarker);
                        var patrolContent = createPatrolContentBycircle(obj)
                        $(document).delegate("#centerSenMsgBtn_" + obj.patrolId, "click", function(e) {
                            // $(document).undelegate("#centerSenMsgBtn_"+obj.patrolId,"click");
                            // e.preventDefault();
                            patrolAlarmDeal(obj, data)
                            patrolLatLng = {
                                lat: obj.latitude,
                                lng: obj.longitude
                            };
                            patrolAlarmMaker = patrolMarker;
                        });

                        // 点击撤销(车辆上的撤销按钮)
                        $(document).undelegate("#centerRevokeMsgBtn_" + obj.patrolId, "click");
                        $(document).delegate("#centerRevokeMsgBtn_" + obj.patrolId, "click", function(e) {
                            patrolAlarmRevoke(obj, data) // 控制中心撤回推送，负责人改为自己

                        });
                        GisShowInfoWindow(patrolMarker, patrolContent, false);
                    })(key)

                }

            }
            var circleParam = {
                color: "#000000",
                opacity: 0.3,
                radius: Number(cirlceBuffer)
            };
            var center = {
                lat: data.alarmLatitude,
                lng: data.alarmLongitude
            };
            circle = GisCreateCircle(center, circleParam, getCircleByDrag);
            circleArr.push(circle);
            GisEventCallBack("dblclick", clearCircle);
        }

    });

}
function getCircleByDrag(circle) {
    // GisClearOverlays(circleArr);
    var cirlceBuffer = circle.radius;
    var alarmLatitude = circle.centerLat;
    var alarmLongitude = circle.centerLng;
    if (cirlceBuffer == "") {
        cirlceBuffer = 5000;
    }
    var patrolUrl = root + "/alarmdeal/getPatrolsInCircle.action?radius=" + cirlceBuffer + "&alarmLatitude="
            + alarmLatitude + "&alarmLongitude=" + alarmLongitude;
    /*
     * var patrolUrl = root
     * +"/alarmdeal/getPatrolsInCircle.action?radius="+cirlceBuffer+"&alarmLatitude=" +
     * data.alarmLatitude
     * +"&alarmLongitude="+data.alarmLongitude+"&userId="+data.userId;
     */
    $.ajax({
        type: "POST",
        url: patrolUrl,
        dataType: "json",
        cache: false,
        async: false,
        error: function(e, message, response) {
            console.log("Status: " + e.status + " message: " + message);
        },
        success: function(jsonObj) {
            GisClearMarkerClusterer(partolclusterer);
            GisClearOverlays(patrolMarkers);
            patrolMarkers = new Array();
            if (jsonObj && jsonObj.length > 0) {
                for ( var key in jsonObj) {
                    (function(x) {
                        var obj = jsonObj[x];
                        $(document).undelegate("#centerSenMsgBtn_" + obj.patrolId, "click");
                        var loction = {
                            lat: obj.latitude,
                            lng: obj.longitude
                        };
                        var patrolMarker = GisCreateMarker(loction, "images/gis/xunluo.png", "" + obj.patrolId, JSON
                                .stringify(obj));
                        patrolMarkers.push(patrolMarker);
                        GisSetShowFront(patrolMarker);
                        var patrolContent = createPatrolContentBycircle(obj)
                        $(document).delegate("#centerSenMsgBtn_" + obj.patrolId, "click", function(e) {
                            // $(document).undelegate("#centerSenMsgBtn_"+obj.patrolId,"click");
                            // e.preventDefault();
                            patrolAlarmDeal(obj, alarmDataConfig)
                            patrolLatLng = {
                                lat: obj.latitude,
                                lng: obj.longitude
                            };
                            patrolAlarmMaker = patrolMarker;
                        });

                        // 点击撤销(车辆上的撤销按钮)
                        $(document).undelegate("#centerRevokeMsgBtn_" + obj.patrolId, "click");
                        $(document).delegate("#centerRevokeMsgBtn_" + obj.patrolId, "click", function(e) {
                            patrolAlarmRevoke(obj, data) // 控制中心撤回推送，负责人改为自己

                        });
                        GisShowInfoWindow(patrolMarker, patrolContent, false);
                    })(key)

                }

            } else {
                bootbox.alert("Find No Patrols");
            }
            // var circleParam = {
            // color:"#000000",
            // opacity:0.3,
            // radius: Number( cirlceBuffer)
            // };
            // var center = {lat:data.alarmLatitude,lng:data.alarmLongitude};
            // circle = GisCreateCircle(center,circleParam,getCircleByDrag);
            // overlays.push(circle);
            // GisEventCallBack("dblclick",clearCircle);
        }

    });
}
function clearCircle() {
    GisClearOverlays(circleArr);
}
/**
 * 构建巡逻队车辆弹出框
 */
function createPatrolContentBycircle(obj) {
    var html = '';
    var iconpath = "static/images/ic_08.png";
    if (obj) {
        var createTime = formatDateTime(new Date(obj.createTime));
        html += '<div class="alert_box">';
        html += '	<div class="alert_box_content">';
        html += '		<div class="alert-title">';
        html += '		    <div class="pull-left">';
        html += '			    <div class="Vehicle_bubble"><img alt="' + obj.trackUnitNumber + '" src="' + iconpath
                + '"/></div>';
        html += '				<b>' + obj.trackUnitNumber + '</b>';
        html += '           </div>';
        html += '			<div class="pull-right">' + $.i18n.prop("monitorTrip.label.createTime") + ':' + createTime
                + '</div>';
        html += '		</div>';
        html += '		<div class="alert_table"><tbody>';
        html += '			<table class="table table-condensed table-striped table-hover">';
        html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.potralUser") + '</th>';
        html += '			         <td>' + obj.potralUser + '</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.routeAreaName") + '</th>';
        html += '			         <td>' + obj.belongToArea + '</td></tr>';
        // html += '
        // <tr><th>'+$.i18n.prop("monitorTrip.label.belongToPortName")+'</th>';
        // html += ' <td>'+obj.belongToPort+'</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.locationAttr") + '</th>';
        html += '			         <td>' + obj.longitude + ',' + obj.latitude + '</td></tr>';
        /*
         * if(userId==obj.userId) { html += ' <tr><td colspan="4" style="text-align:center;">';
         * html += " <a id=\"centerSenMsgBtn_"+obj.patrolId+"\"
         * href=\"javascript:void(0)\" style=\"text-decoration:underline\">";
         * html += $.i18n.prop("vehicle.label.pushMsg")+""; html += ' </a>';
         * html += ' </td></tr>'; }else{ html += ' <tr><td colspan="4" style="text-align:center;">';
         * html += " <a id=\"centerRevokeMsgBtn_"+obj.patrolId+"\"
         * href=\"javascript:void(0)\"
         * style=\"text-decoration:underline;margin-left:15px\">"; html +=
         * $.i18n.prop("vehicle.label.revokeMsg")+""; html += ' </a>'; html += '
         * </td></tr>'; }
         */

        html += '			     <tr><td colspan="4" style="text-align:center;">';
        html += "	    			<a  id=\"centerSenMsgBtn_" + obj.patrolId
                + "\" href=\"javascript:void(0)\" style=\"text-decoration:underline\">";
        html += $.i18n.prop("vehicle.label.pushMsg") + "";
        html += '	     			</a>';
        html += '			     </td></tr>';
        html += '		    </tbody></table>';
        html += '	    </div>';
        html += '   </div>';
        html += '</div>';
    }
    return html;
}
function patrolAlarmDeal(obj, data) {
    var patrolAlarmUrl = root + "/alarmdeal/patrolAlarmDeal.action?alarmId=" + data.alarmId + "&patrolId="
            + obj.patrolId;
    $.ajax({
        type: "POST",
        url: patrolAlarmUrl,
        dataType: "json",
        cache: false,
        async: false,
        error: function(e, message, response) {
            console.log("Status: " + e.status + " message: " + message);
        },
        success: function(data) {
            if (data) {
                alarmDealArray.push(alarmMakerWaitForDeal);
                var lsMonitorAlarmDealBO = data.alarmDealResult.lsMonitorAlarmDealBO;
                var alarmBO = data.alarmDealResult.alarmReportVO;
                if (alarmBO == null) {
                    bootbox.alert($.i18n.prop("alarm.label.alarmId.cannotFind"));
                } else {
                    alarmDealArray.push(circle);
                    GisClearOverlays(alarmDealArray);
                    var loction = {
                        lat: alarmBO.alarmLatitude,
                        lng: alarmBO.alarmLongitude
                    };
                    // var alarmMarkerHander =
                    // GisCreateMarker(loction,"static/images/1_03gray.png",
                    // "",JSON.stringify(alarmBO));
                    var alarmIcon = getAlarmIconByTypeAndLevel(alarmBO.alarmTypeId, alarmBO.alarmLevelId);
                    var alarmMarkerHander = GisCreateMarker(loction, alarmIcon, "", JSON.stringify(alarmBO));
                    //GisSetShowFront(alarmMarker);
                    var alarmContent = createAlarmDealContent(alarmBO, lsMonitorAlarmDealBO);

                    GisSetShowFront(alarmMarkerHander);
                    // bootbox.alert($.i18n.prop("alarm.label.alarmDeal.sendDealSuccess"));

                    bootbox.success($.i18n.prop("alarm.label.alarmDeal.sendDealSuccess"), function() {
                        // 刷新页面，避免重复“推送”
                        window.location.reload();
                    })

                    // GisEventCallBack("dblclick",hiddenDivPanel);
                    // GisShowInfoWindow(alarmMarkerHander,alarmContent,false);
                }
            } else {
                bootbox.alert($.i18n.prop("alarm.label.alarmDeal.sendDealFailed"));
            }
        }
    });
}

function hiddenDivPanel() {
    GisClearDirectionsDisplay();
    $("#ceju").attr("disabled", true)
    $("#dvPanelParent").animate({
        right: -400
    }, "slow");
}
/**
 * 构建报警处理弹出框信息
 */
function createAlarmDealContent(alarmBO, lsMonitorAlarmDealBO) {
    alarmDealId = alarmBO.alarmId;
    var dealTime = formatDateTime(new Date(lsMonitorAlarmDealBO.dealTime.time));
    var alarmTime = formatDateTime(new Date(alarmBO.alarmTime.time));
    if (lsMonitorAlarmDealBO && alarmBO) {
        var html = '';
        html += '<div class="alert_box">';
        html += '	<div class="alert_box_content">';
        html += '		<div class="alert-title">';
        html += '		    <div class="pull-left">';
        html += '			    <div class="Vehicle_bubble alarm"></div>';
        html += '				<b>' + $.i18n.prop("alarm.label.alarmHandler") + '</b>';
        html += '           </div>';
        html += '			<div class="pull-right">' + $.i18n.prop("alarm.label.alarmDeal.dealTime") + ':' + dealTime
                + '</div>';
        html += '		</div>';
        html += '		<div class="alert_table">';
        html += '			<table class="table table-condensed table-striped table-hover">';
        html += '			     <tr><th>' + $.i18n.prop("alarm.label.alarmLongtitude") + '</th>';
        html += '			         <td>' + alarmBO.alarmLongitude + '</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("alarm.label.alarmLatitude") + '</th>';
        html += '			         <td>' + alarmBO.alarmLatitude + '</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("alarm.label.alarmTime") + '</th>';
        html += '			         <td>' + alarmTime + '</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("alarm.label.alarmDeal.dealTime") + '</th>';
        html += '			         <td>' + dealTime + '</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("alarm.label.alarmDeal.dealDesc") + '</th>';
        html += '			         <td>' + lsMonitorAlarmDealBO.dealDesc + '</td></tr>';
        html += '		    </table>';
        html += '	    </div>';
        html += '   </div>';
        html += '</div>';
        return html;
    }
}
var formatDateTime = function(formateTime) {
	if("number"===typeof(formateTime)){
		return new Date(parseInt(formateTime) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
	}else{
		 var y = formateTime.getFullYear();
		    var m = formateTime.getMonth() + 1;
		    m = m < 10 ? ('0' + m) : m;
		    var d = formateTime.getDate();
		    d = d < 10 ? ('0' + d) : d;
		    var h = formateTime.getHours();
		    var minute = formateTime.getMinutes();
		    minute = minute < 10 ? ('0' + minute) : minute;
		    var seconds = formateTime.getSeconds() < 10 ? ('0' + formateTime.getSeconds()) : formateTime.getSeconds();
		    return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + seconds;
	}
   
};
//var formatDateTime = function(date) {
//    var y = date.getFullYear();
//    var m = date.getMonth() + 1;
//    m = m < 10 ? ('0' + m) : m;
//    var d = date.getDate();
//    d = d < 10 ? ('0' + d) : d;
//    var h = date.getHours();
//    var minute = date.getMinutes();
//    minute = minute < 10 ? ('0' + minute) : minute;
//    var seconds = date.getSeconds() < 10 ? ('0' + date.getSeconds()) : date.getSeconds();
//    return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + seconds;
//};
/**
 * 回调函数获取处理的报警点对象
 * @param marker
 */
function getAlarmMarker(marker) {
    // var target = event;
    alarmMakerWaitForDeal = marker;
}
/**
 * 构建报警信息弹出框
 */
function createAlarmContent(obj) {
    alarmDealId = obj.alarmId;
    if (obj) {
        var alarmTime = obj.alarmTime;
        if (typeof obj.alarmTime !== 'string') {
            alarmTime = formatDateTime(new Date(obj.alarmTime.time));
        }
        var html = '';
        html += '<div class="alert_box">';
        html += '	<div class="alert_box_content">';
        html += '		<div class="alert_table">';
        html += '			<table class="table table-condensed table-striped table-hover">';
        html += '			     <tr><th>' + $.i18n.prop("alarm.label.alarmLocation") + '</th>';
        html += '			         <td>' + (parseFloat(obj.alarmLongitude)).toFixed(6) + ","
                + (parseFloat(obj.alarmLatitude)).toFixed(6) + '</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("Alarm.type") + '</th>';
        html += '			         <td>' + $.i18n.prop("AlarmType." + (obj.alarmTypeCode || obj.alarmTypeId) ) + '</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("alarm.label.alarmContent") + '</th>';
        html += '			         <td>' + obj.alarmContent + '</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("alarm.label.alarmTime") + '</th>';
        html += '			         <td>' + alarmTime + '</td></tr>';
        html += '			     <tr class = "hidden"><th></th>';
        html += '			         <td>' + obj.alarmId + '</td></tr>';
        html += '			     <tr class = "hidden"><th></th>';
        html += '			         <td>' + obj.alarmId + '</td></tr>';
        // 当用户有处理报警权限时 obj.alarmTime.replace('T'," ")
        if (userCanDealAlarm == '1') {
            html += '			     <tr><td colspan="2" style="text-align:center;">';
            if (userId == obj.userId || !obj.userId) {
                html += "	                    <a  href=\"javascript:void(0)\"  id=\"alarmHandlerBtn_" + obj.alarmId
                        + "\" type=\"button\" style=\"text-decoration:underline\">";
                html += $.i18n.prop("alarm.label.alarmHandler") + "";
                html += '	                    </a>';
                if (systemModules.isPatrolOn) {
                    html += "                        <a href=\"javascript:void(0)\" id=\"patrolHandlerBtn_"
                            + obj.alarmId + "\" type=\"button\" style=\"text-decoration:underline;margin-left:15px\">";
                    html += $.i18n.prop("alarm.label.patrolHandler") + "";
                    html += '                       </a>';
                }
            } else {
                html += "	    				<a  id=\"centerRevokeMsgBtn_" + obj.alarmId
                        + "\" href=\"javascript:void(0)\" style=\"text-decoration:underline;margin-left:15px\">";
                html += $.i18n.prop("vehicle.label.revokeMsg") + "";
                html += '	     				</a>';
            }
            html += '			      </td></tr>';
        }

        html += '		    	</table>';
        html += '	         <div class="clearfix"></div>';
        html += '	    </div>';
        html += '	</div>';
        html += '</div>';
        return html;
    }
}

function getMeasureInfo(gpsMeasure) {
    var lntlng = "";
    if (gpsMeasure.latitude != "" && gpsMeasure.latitude != null && gpsMeasure.longitude != ""
            && gpsMeasure.longitude != null) {
        lntlng = parseFloat(gpsMeasure.latitude).toFixed(6) + "," + parseFloat(gpsMeasure.longitude).toFixed(6);
    }
    // var measureTimeStr = (gpsMeasure.locationTime.replace("T", " "));
    // measureTimeStr = measureTimeStr.replace(/-/g, "/");
    var measureTimeStr = formatDateTime(new Date(gpsMeasure.locationTime.time));
    var txt = "<div><table>" + "<tr><th align='left'>Time:</th><td>" + measureTimeStr + "</td></tr>"
            + "<tr><th align='left'>Location: </th><td>" + lntlng + "</td></tr>" + "</table></div>";
    return txt;
}

/**
 * 操作展示与隐藏路线上的点标注
 * @param obj
 * @param hidePoint
 * @param showPoint
 */
var showOrHiddenPoint = false;
function showOrHiddenPoint1() {
    GisClearOverlays(showPointMarkers);
    var pointIcon = "static/images/pointIcon.png";
    var tripBufferPoints1 = [];
    for (var i = 0; i < raPointsArr.length; i++) {
        tripBufferPoints1.push({
            lat: raPointsArr[i].latitude,
            lng: raPointsArr[i].longitude,
            direction: raPointsArr[i].direction
        });
        var pointMarker = GisCreateMarker({
            lat: raPointsArr[i].latitude,
            lng: raPointsArr[i].longitude
        }, pointIcon, "", JSON.stringify(raPointsArr[i]));
        GisShowInfoWindow(pointMarker, getMeasureInfo(raPointsArr[i]));
        showPointMarkers.push(pointMarker);
    }

    if (!showPointMarkers.length > 0)
        return;
    if (showOrHiddenPoint) {
        if ($('#handlerPoint').text() == "Hidden Points") {
            $('#handlerPoint').text("Show Points");
        }
        showOrHiddenPoint = false;
        // $('#showOrHiddenPoint').text
        GisHiddenOverlays(showPointMarkers);
    } else {
        if ($('#handlerPoint').text() == "Show Points") {
            $('#handlerPoint').text("Hidden Points");
        }
        showOrHiddenPoint = true;
        GisShowOverlays(showPointMarkers);
    }
}
function mapCreateTracking(jsonObj, lsMonitorTripBO, gpsVehicleInfo, type) {
    $("#handlePoint").attr("name", "hiddenPoint");
    var pointIcon = "static/images/pointIcon.png";
    GisClearOverlays(vehicleMarkers);
    GisClearOverlays(svgMarkers);
    GisClearOverlays(lengthArr);
    raPointsArr = [];
    raPointsArr = jsonObj;
    tripBufferPoints = [];
    // GisClearOverlays(vehicleMarkers);
    // tripBufferPoints = [];
    if (ALL_POINT == type) {
        for (var i = 0; i < jsonObj.length; i++) {
            tripBufferPoints.push({
                lat: jsonObj[i].latitude,
                lng: jsonObj[i].longitude,
                direction: jsonObj[i].direction
            });

            // 显示隐藏轨迹上的点
            /*
             * var pointMarker =
             * GisCreateMarker({lat:jsonObj[i].latitude,lng:jsonObj[i].longitude},"static/images/pointIcon.png",""+jsonObj[i].gpsId);
             * GisShowInfoWindow(pointMarker,""+jsonObj[i].gpsId,false);
             * pointMarkers.push(pointMarker);
             */
        }
        var lineStyle = {
            color: "#1800ff",
            weight: 4,
            opacity: 1,
            zIndex: 999
        };
    } else {
        for (var i = 0; i < selectNumber; i++) {
            tripBufferPoints.push({
                lat: jsonObj[jsonObj.length - selectNumber + i].latitude,
                lng: jsonObj[jsonObj.length - selectNumber + i].longitude,
                direction: jsonObj[jsonObj.length - selectNumber + i].direction
            });
            var pointMarker = GisCreateMarker({
                lat: jsonObj[jsonObj.length - selectNumber + i].latitude,
                lng: jsonObj[jsonObj.length - selectNumber + i].longitude
            }, pointIcon, "");
            GisShowInfoWindow(pointMarker, getMeasureInfo(jsonObj[jsonObj.length - selectNumber + i]));
            showPointMarkers.push(pointMarker);
            // 显示隐藏轨迹上的点
            /*
             * var pointMarker =
             * GisCreateMarker({lat:jsonObj[i].latitude,lng:jsonObj[i].longitude},"static/images/pointIcon.png",""+jsonObj[i].gpsId);
             * GisShowInfoWindow(pointMarker,""+jsonObj[i].gpsId,false);
             * pointMarkers.push(pointMarker);
             */
        }
        var lineStyle = {
            color: "#ff00ff",
            weight: 4,
            opacity: 1,
            zIndex: 2
        };
    }

    // if($("#handlePoint").attr("name")=="showPoint"){
    // GisHiddenOverlays(showPointMarkers);
    // }else{
    // GisShowOverlays(showPointMarkers);
    // }
    //	
    var localPoint = tripBufferPoints[tripBufferPoints.length - 1];
    if ("0" == gpsVehicleInfo.riskStatus) {
        replayIcon = "images/gis/truck1.png";
    } else if ("1" == gpsVehicleInfo.riskStatus) {
        replayIcon = "images/gis/truck2.png";
    } else {
        replayIcon = "images/gis/truck1.png";
    }

    if (menuType == "8") {
        GisCreateLineTrackShowDetailInfo(jsonObj);
    } else {
        trackingLine = GisShowPolyLineInMap(tripBufferPoints, true, lineStyle);
        // GisSetShowFront(trackingLine,1);
        overlaysArrayRoute.push(trackingLine);
        var len = $.i18n.prop('map.label.deviationDistance')
                + (parseFloat(GisptToPolylineLength(localPoint, tripRoutePoints)) / 1000).toFixed(2) + "(KM)";
        var lengthLabel = GisCreateLabel(localPoint, len);
        lengthArr.push(lengthLabel);

    }
    GisSetViewPortByArray(tripBufferPoints);

    createVehicleMarker(localPoint, gpsVehicleInfo, "blue", "white");
    var currentContent = creatRealVehcleContent(gpsVehicleInfo);
    findOneVehicleAlarm(gpsVehicleInfo);

}

/**
 * 查询一辆车的规划路线
 * @param gpsVehicleInfo
 */
//function findOneVehicleGpsPlanRoute(jsonObj, lsMonitorTripBO, gpsVehicleInfo, type){
function findOneVehicleGpsPlanRoute(gpsVehicleInfo){
    var vehicleUrl = getRootPath()
    + "monitorvehicle/findOneVehicleGpsPlanRoute.action?vehicleId="+gpsVehicleInfo.vehicleId;
    $.ajax({
           type : "POST",
           url : vehicleUrl,
           dataType : "json",
           cache : false,
           //async : false,
           error : function(e, message, response) {
               console.log("Status: " + e.status + " message: " + message);
           },
           success : function(obj) {
               if(obj&&obj.success){
                    var lsMonitorRaPointBOs = obj.lsMonitorRaPointBOs;//车辆预定义路线
                    if(lsMonitorRaPointBOs!=null){
                       mapShowRouteLine(lsMonitorRaPointBOs,ALL_POINT);
                       //mapCreateTracking(jsonObj, lsMonitorTripBO, gpsVehicleInfo, type);// 地图上展示车辆
                      
                   }
               }
           }
    });
}

/**
 * 查询一辆车的报警  显示到地图和报警下拉列表
 * @param gpsVehicleInfo
 */
function findOneVehicleAlarm(gpsVehicleInfo){
    var vehicleUrl = getRootPath()
    + "monitorvehicle/findOneVehicleAlarm.action?vehicleId="+gpsVehicleInfo.vehicleId;
    $.ajax({
           type : "POST",
           url : vehicleUrl,
           dataType : "json",
           cache : false,
           async : false,
           error : function(e, message, response) {
               console.log("Status: " + e.status + " message: " + message);
           },
           success : function(obj) {
               if(obj&&obj.success){
                 //车辆报警
                    var alarmPoints = obj.lsMonitorAlarmVOs;//车辆报警信息
                    //for (var i = 0; i < alarmPoints.length; i++) {
                        //var alarmContent = alarmPoints[i].alarmStatus;
                    for (var i = 0; i < alarmPoints.length; i++) {
                        (function(x){
                            var data = alarmPoints[x];
                            data.vehicleId = gpsVehicleInfo.vehicleId;
                            //$(document).undelegate("#alarmHandlerBtn_"+data.alarmId,"click"); 
                            //$(document).undelegate("#patrolHandlerBtn_"+data.alarmId,"click"); 
                            var alarmIcon = getAlarmIconByTypeAndLevel(data.alarmTypeCode, data.alarmLevelCode);
                            var alarmMarker = GisCreateMarker({
                                lat : data.alarmLatitude,
                                lng : data.alarmLongitude
                            }, alarmIcon, $.i18n.prop('AlarmType.' + data.alarmTypeCode),JSON.stringify(data));
                            //var alarmContent = alarmPoints[i].alarmStatus;
                            var alarmContent =createAlarmContent(data);
                            var loction = {lat:data.alarmLatitude,lng:data.alarmLongitude};
                            //var alarmMarker = GisCreateMarker(loction,"static/images/1_03.png", "");
                            alarmArr.push(alarmMarker);
                            GisSetShowFront(alarmMarker);
                            $(document).undelegate("#alarmHandlerBtn_"+data.alarmId,"click");
                            $(document).delegate("#alarmHandlerBtn_"+data.alarmId,"click", function(e){
                                if(circleArr.length>0)
                                {
                                    GisClearOverlays(circleArr); 
                                }
                                //$(document).undelegate("#alarmHandlerBtn_"+data.alarmId,"click");
                                //e.preventDefault();
                                getUserInfo(sessionUserId, function(result) {
                                    var flag = true;
                                    //中心用户且有处理报警权限的才可处理报警
                                    if(['qualityCenterUser', 'followupUser', 'contromRoomUser', 'riskAnalysisUser', 'patrolManager'].indexOf(result.roleName) > -1 && result.canDealAlarm == '1'){
                                    } else {
                                        bootbox.alert($.i18n.prop('user.info.no.permission'));
                                        flag = false;
                                        return false;
                                    }
                                    if(flag) {
                                        alarmDealClick(data);
                                        alarmMakerWaitForDeal = alarmMarker;
                                    }
                                });
                            });
                            $(document).delegate("#patrolHandlerBtn_"+data.alarmId,"click", function(e){
                                if(circleArr.length>0)
                               {
                                   GisClearOverlays(circleArr); 
                               }
                                //$(document).undelegate("#patrolHandlerBtn_"+data.alarmId,"click"); 
                                //e.preventDefault();
                                getUserInfo(sessionUserId, function(result) {
                                    var flag = true;
                                    //中心用户且有处理报警权限的才可处理报警
                                    if(['qualityCenterUser', 'followupUser', 'contromRoomUser', 'riskAnalysisUser', 'patrolManager'].indexOf(result.roleName) > -1 && result.canDealAlarm == '1'){
                                    } else {
                                        bootbox.alert($.i18n.prop('user.info.no.permission'));
                                        flag = false;
                                        return false;
                                    }
                                    if(flag) {
                                        alarmDataConfig = data;
                                        patrolHandlerClick(data);
                                        //$("#patrolHandlerBtn_"+data.alarmId).attr("disabled",true)
                                        alarmMakerWaitForDeal = alarmMarker;
                                    }
                                });
                            });
                            //撤销按钮
                            $(document).undelegate("#centerRevokeMsgBtn_"+data.alarmId,"click"); 
                            $(document).delegate("#centerRevokeMsgBtn_"+data.alarmId,"click", function(e){
                                patrolAlarmRevoke(obj,data)     //控制中心撤回推送，负责人改为自己
                            });
                            if(typeof(alarmIdForRecord)!=undefined && alarmIdForRecord==data.alarmId){
                                //GisShowInfoWindow(alarmMarker,alarmContent,true);
                                GisAddEventForVehicle(alarmMarker, "click", function() {
                                    var d = dialog({
                                           id: alarmContent,
                                           title: vehiclePlateNUmber,//$.i18n.prop('trip.info.message'),
                                           content: alarmContent,
                                           resize:true
                                       });
                                       d.show();
                               });
                                GisSetHomeCenter(loction);
                            }else{
                                GisAddEventForVehicle(alarmMarker, "click", function() {
                                   var d = dialog({
                                          id: alarmContent,
                                          title: vehiclePlateNUmber,//$.i18n.prop('trip.info.message'),
                                          content: alarmContent,
                                          resize:true
                                      });
                                      d.show();
                              });
                                //GisShowInfoWindow(alarmMarker,alarmContent,false);
                            }
                            overlaysArray.push(alarmMarker);
                        })(i);
                    }

               }
           }
    });
}

/**
 * 创建车辆信息及弹出框并在地图显示
 * @param localPoint
 * @param gpsVehicleInfo
 * @param fillColor
 * @param strokeColor
 */
function createVehicleMarker(localPoint, gpsVehicleInfo, fillColor, strokeColor) {
    var trackingMaker;
    if ("0" == gpsVehicleInfo.riskStatus) {
        trackingMaker = GisCreateVehicelMarker(localPoint, createSafeIcon(gpsVehicleInfo.direction), ""
                + gpsVehicleInfo.vehiclePlateNumber, JSON.stringify(gpsVehicleInfo));
    } else if ("1" == gpsVehicleInfo.riskStatus) {
        trackingMaker = GisCreateVehicelMarker(localPoint, createRiskIcon(gpsVehicleInfo.direction), ""
                + gpsVehicleInfo.vehiclePlateNumber, JSON.stringify(gpsVehicleInfo));
    } else if ("2" == gpsVehicleInfo.riskStatus) {
        trackingMaker = GisCreateVehicelMarker(localPoint, createDangerIcon(gpsVehicleInfo.direction), ""
                + gpsVehicleInfo.vehiclePlateNumber, JSON.stringify(gpsVehicleInfo));
    } else {
        trackingMaker = GisCreateVehicelMarker(localPoint, createSafeIcon(gpsVehicleInfo.direction), ""
                + gpsVehicleInfo.vehiclePlateNumber, JSON.stringify(gpsVehicleInfo));
    }
    var svgMaker = GisCreateVehicleSVGMarker(localPoint, GisCreateSvgIcon(google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
            3, gpsVehicleInfo.direction, fillColor, strokeColor), "", "111");
    // vehicleMarkers.push(svgMaker);
    svgMarkers.push(svgMaker);
    vehicleMarkers.push(trackingMaker);
    var html = creatRealVehcleContent(gpsVehicleInfo);
    GisAddEventForVehicle(trackingMaker, "click", function() {
        var obj = {
            data: gpsVehicleInfo
        };
        var d = dialog({
            id: gpsVehicleInfo.vehicleStatusId,
            title: " ",// vehicle.vehicleStatusId,
                        // //$.i18n.prop('trip.info.message'),
            content: html,
            // okValue: '确 定',
            resize: true,
            left: '90%',
            top: '20%'
        }

        );
        d.show();
    });

    $(document).delegate("#vehicleHandlerBtn_" + gpsVehicleInfo.vehicleId, "click", function() {
        var startPoint = GisHandlePointByJson(patrolLatLng);
        var endPoint = GisHandlePointByJson({
            lng: gpsVehicleInfo.longitude,
            lat: gpsVehicleInfo.latitude
        });
        GisDirectSearch(startPoint, endPoint, null, "dvPanel", getDistDuration);
    });
}

/**
 * , 构建车辆实时轨迹信息弹出框
 */
function creatRealVehcleContent(obj) {
    var iconpath = "static/images/Safetruck.png";
    if ("0" == obj.riskStatus) {
        iconpath = "static/images/Safetruck.png";
    } else if ("1" == obj.riskStatus) {
        iconpath = "static/images/warningtruck.png";
    } else if ("2" == obj.riskStatus) {
        iconpath = "static/images/alarmtruck.png";
    } else {
        iconpath = "static/images/Safetruck.png";
    }
    var html = '';
    if (obj) {
        var locationTime = formatDateTime(new Date(obj.locationTime));
        html += '<div class="alert_box">';
        html += '	<div class="alert_box_content">';
        html += '		<div class="alert-title">';
        html += '		    <div class="pull-left">';
        html += '			    <div class="Vehicle_bubble"><img style=\"width:24px\" title="' + obj.vehiclePlateNumber
                + '" src="' + iconpath + '"/></div>';
        html += '				<b>' + obj.vehiclePlateNumber + '</b>';
        html += '           </div>';
        html += '			<div class="pull-right">' + $.i18n.prop("eclock.label.locationTime") + ':' + locationTime
                + '</div>';
        html += '		</div>';
        html += '		<div class="alert_table"><tbody>';
        html += '			<table class="table table-condensed table-striped table-hover">';
        html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.trackingDeviceNumber") + '</th>';
        html += '			         <td>' + obj.trackingDeviceNumber + '</td>';
        html += '			     <th>' + $.i18n.prop("monitorTrip.label.containerNumber") + '</th>';
        html += '			         <td>' + obj.containerNumber + '</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.declarationNumber") + '</th>';
        html += '			         <td>' + obj.declarationNumber + '</td>';
        html += '			     <th>' + $.i18n.prop("monitorTrip.label.trailerNumber") + '</th>';
        html += '			         <td>' + obj.trailerNumber + '</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.driverName") + '</th>';
        html += '			         <td>' + obj.driverName + '</td>';
        html += '			     <th>' + $.i18n.prop("monitorTrip.label.driverCountry") + '</th>';
        html += '			         <td>' + obj.driverCountry + '</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.vehicleCountry") + '</th>';
        html += '			         <td>' + obj.vehicleCountry + '</td>';
        html += '			     <th>' + $.i18n.prop("monitorTrip.label.esealNumber") + '</th>';
        html += '			         <td>' + obj.esealNumber + '</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.sensorNumber") + '</th>';
        html += '			         <td>' + obj.sensorNumber + '</td>';
        html += '			     <th>' + $.i18n.prop("monitorTrip.label.elockStatus") + '</th>';

        if (obj.elockStatus == "1") {
            html += '			         <td>' + $.i18n.prop("monitorTrip.label.elockStatus.lock") + '</td></tr>';
        } else {
            html += '			         <td>' + $.i18n.prop("monitorTrip.label.elockStatus.unlock") + '</td></tr>';
        }

        html += '			     <tr>';
        html += '			     <th>' + $.i18n.prop("monitorTrip.label.poleStatus") + '</th>';

        if (obj.poleStatus == "1") {
            html += '			         <td>' + $.i18n.prop("monitorTrip.label.poleStatus.closed") + '</td>';
        } else {
            html += '			         <td>' + $.i18n.prop("monitorTrip.label.poleStatus.open") + '</td>';
        }
        html += '			     <th>' + $.i18n.prop("monitorTrip.label.brokenStatus") + '</th>';

        if (obj.brokenStatus == "0") {
            html += '			         <td>' + $.i18n.prop("monitorTrip.label.brokenStatus.normal") + '</td>';
        } else {
            html += '			         <td>' + $.i18n.prop("monitorTrip.label.brokenStatus.broken") + '</td>';
        }
        html += '                </tr>';
        html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.FromTo") + '</th>';
        html += '			         <td>' + obj.checkinPortName + ' - ' + obj.checkoutPortName + '</td><th>'
                + $.i18n.prop("trip.label.routeId") + '</th> <td>' + obj.routeAreaName + '</td></tr>';
        // html += ' <tr><th>monitorRouteArea</th>';
        // html += ' <td>'+obj.routeAreaName+'</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.locationAttr") + '</th>';
        html += '			         <td>' + (parseFloat(obj.longitude)).toFixed(6) + ','
                + (parseFloat(obj.latitude)).toFixed(6) + '</td>';
        html += '                    <th>' + '<img src="static/images/clockwise.png"  alt="clockwise(N-S)" />'
                + $.i18n.prop("monitorTrip.label.direction") + '</th>';
        html += '			         <td>' + obj.direction + '</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.checkinTime") + '</th>';
        html += '			         <td>' + formatDateTime(new Date(obj.checkinTime)) + '</td>';
        html += '			     <th>' + $.i18n.prop("monitorTrip.label.checkinUser") + '</th>';
        html += '			         <td>' + obj.checkinUserName + '</td></tr>';
        html += '			     <tr><th>' + $.i18n.prop("monitorTrip.label.electricityValue") + '</th>';
        html += '			         <td>' + createSealElectric(parseInt(obj.electricityValue)) + '</td>';
        html += '			     <th>' + $.i18n.prop("monitorTrip.label.elockSpeed") + '</th>';
        html += '			         <td>' + parseInt(obj.elockSpeed) + '</td></tr>';

        if (systemModules.isPatrolOn || canAddAlarm()) {
            html += '			     <tr><td colspan="4" style="text-align:center;">';
        }
        if (systemModules.isPatrolOn) {
            html += "                    <a onclick=\"vehicleHandlerBtnClick('"
                    + obj.latitude
                    + "','"
                    + obj.longitude
                    + "','"
                    + obj.vehicleId
                    + "')\"  id=\"vehicleHandlerBtn\" style=\"text-decoration:underline\"  href=\"javascript:void(0)\">";
            html += $.i18n.prop("vehicle.label.track") + "";
            html += '                   </a>';
        }
        if (canAddAlarm()) {
            html += "	    			<a  id=\"addAlarmBtn\" onclick=\"addAlarmByManual('" + obj.latitude + "','"
                    + obj.longitude + "','" + obj.tripId + "','" + obj.vehiclePlateNumber
                    + "')\" href=\"javascript:void(0)\" style=\"text-decoration:underline;margin-left:15px\">";
            html += $.i18n.prop("alarm.label.addTitle") + "";
            html += '	     			</a>';
        }
        if (systemModules.isPatrolOn || canAddAlarm()) {
            html += '			     </td></tr>';
        }

        html += '		    </tbody></table>';
        html += '	    </div>';
        html += '   </div>';
        html += '</div>';
    }
    return html;
}
/**
 * 天朗关锁电量计算百分比
 * @param voltage
 * @returns {String}
 */
function createSealElectric(voltage) {
    if (voltage >= 450) {
        voltage = 100;
    } else if (voltage <= 320) {
        voltage = 0;
    } else if (parseInt((voltage - 320) / (415 - 320) * 100) >= 100) {
        voltage = 100;
    } else {
        voltage = parseInt((voltage - 320) / (415 - 320) * 100);
    }

    return voltage + "%";
}

function canAddAlarm() {
    return ['qualityCenterUser', 'followupUser', 'contromRoomUser', 'riskAnalysisUser', 'patrolManager']
            .indexOf(roleName) > -1;
}
function vehicleHandlerBtnClick(latitude, longtitude, vehicleId) {
    var localPoint = {
        lng: longtitude,
        lat: latitude
    };
    if (null == patrolLocation) {
        bootbox.alert($.i18n.prop("alarm.label.patrol.hasNoFind"));
    } else {
        // var startPoint = GisHandlePointByJson(patrolLatLng);
        var startPoint = GisHandlePointByJson(patrolLocation);
        var endPoint = GisHandlePointByJson(localPoint);
        GisDirectSearch(startPoint, endPoint, null, "dvPanel", getDistDuration);
    }
}
/**
 * 手动添加报警
 * @param alarmLatitude
 * @param alarmLongitude
 * @param tripId
 */
function addAlarmByManual(alarmLatitude, alarmLongitude, tripId, vehiclePlateNumber) {
    var url = root + "/monitoralarm/addAlarmModalShow.action?alarmLatitude=" + alarmLatitude + "&alarmLongitude="
            + alarmLongitude + "&tripId=" + tripId + "&vehiclePlateNumber=" + vehiclePlateNumber;
    $('#addAlarmModal').removeData('bs.modal');
    $('#addAlarmModal').modal({
        remote: url,
        show: false,
        backdrop: 'static',
        keyboard: false
    });
    $('#addAlarmModal').on('loaded.bs.modal', function(e) {
        $('#addAlarmModal').modal('show');
    });
    // 模态框登录判断
    $('#addAlarmModal').on('show.bs.modal', function(e) {
        var content = $(this).find(".modal-content").html();
        needLogin(content);
    });
}

function mapShowRouteLine(lsMonitorRaPointBOs, type) {
    GisClearOverlays(overlaysPlanRoute);
    tripRoutePoints = [];
    if (ALL_POINT == type) {
        for (var j = 0; j < lsMonitorRaPointBOs.length; j++) {
            if (isNotNull(lsMonitorRaPointBOs[j].latitude) && isNotNull(lsMonitorRaPointBOs[j].longitude)) {
                tripRoutePoints.push({
                    lat: lsMonitorRaPointBOs[j].latitude,
                    lng: lsMonitorRaPointBOs[j].longitude
                });
            }
        }
        var lineStyle = {
            color: "#00ff00",
            weight: 10,
            opacity: 1
        };
    } else {

        if (selectNumber != null && selectNumber != "") {
            for (var j = 0; j < selectNumber; j++) {
                if (isNotNull(lsMonitorRaPointBOs[j].latitude) && isNotNull(lsMonitorRaPointBOs[j].longitude)) {
                    tripRoutePoints.push({
                        lat: lsMonitorRaPointBOs[j].latitude,
                        lng: lsMonitorRaPointBOs[j].longitude
                    });
                }
            }
        }
        var lineStyle = {
            color: "#00ff00",
            weight: 15,
            opacity: 1
        };
    }

    if (menuType == "9" || menuType == "8") {
        var routeLineLine = GisShowPolyLineInMap(tripRoutePoints, true, lineStyle);
       // GisSetShowFront(routeLineLine,-1000)
        overlaysPlanRoute.push(routeLineLine);
    }
}
function findAllLandMarkers() {
    $("#planRouteAreaList").html("");
    $("#vehicelfilter").addClass("hidden");
    $("#playerbox").addClass("hidden");
    $("#header_title_div").removeClass("hidden");
    // $("#trackSeleCtr").addClass("hidden");
    menuType = "2";
    $("#header_title").html($.i18n.prop("link.system.markerList"));
    // findAllRouteAreaList();
    $(".app-right-top").addClass("hidden");
    $(".search_box").addClass("hidden");
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
    GisInitDrawingManagerAndDriving(drawParams, true, getDrawRouteArea, POLYGON);// 初始化Google地图画图工具
    $("#vehicelTrackList").addClass("hidden");
    $("#trackquery").addClass("hidden");
    var portUrl = getRootPath() + "landmarker/findAllLandMarkers.action";
    $
            .ajax({
                type: "POST",
                url: portUrl,
                dataType: "json",
                cache: false,
                async: false,
                error: function(e, message, response) {
                    console.log("Status: " + e.status + " message: " + message);
                },
                success: function(jsonObj) {
                    if (jsonObj.length > 0) {
                        var areaText = '<div class="list_search"><input type="text" class="form-control" id="areaSearch" placeholder="Query..."  name="areaSearch"><button onclick="fliterArea()" type="button" class="btn btn-default "><span class="glyphicon glyphicon-search"></span></button></div>';
                        $("#planRouteAreaList").append(areaText);
                        $("#account_num").html("(" + jsonObj.length + ")");
                        for (var i = 0; i < jsonObj.length; i++) {
                            var data = jsonObj[i];
                            var iconpath = "static/images/" + data.landImage + ".png";
                            var areaText = "<li><label><input type=\"checkbox\"  " + "name=\"landIds\" value=\""
                                    + data.landId + "\"><img  title=\"" + data.landName + "\" src=\"" + iconpath
                                    + "\"/>" + "<a   onclick=\"setLanderMarkerLocation('"
                                    + (parseFloat(data.latitude)).toFixed(6) + "','"
                                    + (parseFloat(data.longitude)).toFixed(6)
                                    + "');\" href='javascript:void(0);' class=\"vehicle_label\"  >" + data.landName
                                    + "</a>" + "</label></li>";
                            $("#planRouteAreaList").append(areaText);
                        }
                        addLanderMarkerToMap(jsonObj)
                    }
                }
            });
}

/**
 * 模糊查询
 */
function fliterArea() {
    var landName = $("#areaSearch").val();
    $("#planRouteAreaList").html("");
    $("#vehicelfilter").addClass("hidden");
    $("#playerbox").addClass("hidden");
    $("#header_title_div").removeClass("hidden");
    // $("#trackSeleCtr").addClass("hidden");
    menuType = "2";
    $("#header_title").html($.i18n.prop("link.system.markerList"));
    // findAllRouteAreaList();
    $(".app-right-top").addClass("hidden");
    $(".search_box").addClass("hidden");
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
    GisInitDrawingManagerAndDriving(drawParams, true, getDrawRouteArea, POLYGON);// 初始化Google地图画图工具
    $("#vehicelTrackList").addClass("hidden");
    $("#trackquery").addClass("hidden");
    var portUrl = getRootPath() + "landmarker/findAllLandMarkers.action?landName=" + landName;
    $
            .ajax({
                type: "POST",
                url: portUrl,
                dataType: "json",
                cache: false,
                async: false,
                error: function(e, message, response) {
                    console.log("Status: " + e.status + " message: " + message);
                },
                success: function(jsonObj) {
                    if (jsonObj.length > 0) {
                        var areaText = '<div class="list_search"><input type="text" class="form-control" id="areaSearch" placeholder="Query..."  name="areaSearch"><button onclick="fliterArea()" type="button" class="btn btn-default "><span class="glyphicon glyphicon-search"></span></button></div>';
                        $("#planRouteAreaList").append(areaText);
                        $("#account_num").html("(" + jsonObj.length + ")");
                        for (var i = 0; i < jsonObj.length; i++) {
                            var data = jsonObj[i];
                            var iconpath = "static/images/" + data.landImage + ".png";
                            var areaText = "<li><label><input type=\"checkbox\"  " + "name=\"landIds\" value=\""
                                    + data.landId + "\"><img  title=\"" + data.landName + "\" src=\"" + iconpath
                                    + "\"/>" + "<a   onclick=\"setLanderMarkerLocation('" + data.latitude + "','"
                                    + data.longitude + "');\" href='javascript:void(0);' class=\"vehicle_label\"  >"
                                    + data.landName + "</a>" + "</label></li>";
                            $("#planRouteAreaList").append(areaText);
                        }
                        addLanderMarkerToMap(jsonObj)
                    }
                }
            });
}

function addLanderMarkerToMap(landerMarkerArr) {
    if (landerMarkers.length > 0) {
        GisClearOverlays(landerMarkers);
    }
    for ( var key in landerMarkerArr) {
        (function(i) {
            var messageContent = landerMarkerArr[i];
            var landIcon = "static/images/" + messageContent.landImage + ".png";
            var landMarker = GisCreateVehicelMarker({
                lat: messageContent.latitude,
                lng: messageContent.longitude
            }, landIcon, "" + messageContent.landName, JSON.stringify(messageContent));//
            var html = createLandMarkerContent(messageContent);
            // GisShowInfoWindow(trackingMaker,html,false);
            GisAddEventForVehicle(landMarker, "click", function() {
                var d = dialog({
                    id: messageContent.landId,
                    title: "LandMarker Content",
                    content: html,
                    resize: true
                });
                d.show();
            });
            landerMarkers.push(landMarker);

        })(key)
    }
}
/**
 * 创建landermarker弹出框信息
 */
function createLandMarkerContent(obj) {
    if (obj) {
        var html = '';
        html += '<div class="alert_box">';
        html += '   <div class="alert_box_content">';
        // html += ' <div class="alert-title">';
        // html += ' <div class="pull-left">';
        // html += ' <div class="Vehicle_bubble alarm"></div>';
        // html += ' <b>'+$.i18n.prop("alarm.label.alarmContent")+'</b>';
        // html += ' </div>';
        // //html += ' <div
        // class="pull-right">'+$.i18n.prop("alarm.label.alarmTime")+':'+obj.alarmTime+'</div>';
        // html += ' </div>';
        html += '       <div class="alert_table">';
        html += '           <table class="table table-condensed table-striped table-hover">';
        html += '                <tr><th>LandMarker Name:</th>';
        html += '                    <td>' + obj.landName + '</td></tr>';
        html += '                <tr><th>Landmarker Location:</th>';
        html += '                    <td>' + (parseFloat(obj.longitude)).toFixed(6) + ","
                + (parseFloat(obj.latitude)).toFixed(6) + '</td></tr>';
        html += '                <tr><th>Description:</th>';
        html += '                    <td>' + obj.description + '</td></tr>';
        html += '               </table>';
        html += '            <div class="clearfix"></div>';
        html += '       </div>';
        html += '   </div>';
        html += '</div>';
        return html;
    }
}
function setLanderMarkerLocation(latitude, longitude) {
    var location = {
        lat: latitude,
        lng: longitude
    };
    GisSetHomeCenter(location);
}
function findAllRouteAreaList() {
    $("#planRouteAreaList").html("");
    var portUrl = getRootPath() + "monitorroutearea/findAllRouteAreas.action?ids=" + menuType;
    $
            .ajax({
                type: "POST",
                url: portUrl,
                dataType: "json",
                cache: false,
                async: false,
                error: function(e, message, response) {
                    console.log("Status: " + e.status + " message: " + message);
                },
                success: function(jsonObj) {
                    var routeArea = jsonObj;
                    // areaText = "<ul class=\"Custom_list\">";
                    if (routeArea.length > 0) {
                        var routeSearch = '<div class="list_search"><input type="text"  id="routeSearch" class="form-control"  name=\"routeSearch\" placeholder="Query..." ><button onclick="fliter()" type="submit" class="btn btn-default "><span class="glyphicon glyphicon-search" aria-hidden="true"></span></button></div>';
                        $("#planRouteAreaList").append(routeSearch);
                        $("#account_num").html("(" + routeArea.length + ")");
                        for (var i = 0; i < routeArea.length; i++) {
                            var routeAreainfo = routeArea[i];
                            if (routeAreainfo.routeAreaId != null && routeAreainfo.routeAreaId != "") {
                                var iconpath = "";
                                if ("0" == routeAreainfo.routeAreaType) {
                                    if ("0" == routeAreainfo.routeAreaStatus)
                                        iconpath = "static/images/1_10.png";
                                    else
                                        iconpath = "static/images/1_10gray.png";
                                } else if ("1" == routeAreainfo.routeAreaType) {
                                    if ("0" == routeAreainfo.routeAreaStatus)
                                        iconpath = "static/images/1_09.png";
                                    else
                                        iconpath = "static/images/1_09gray.png";
                                } else if ("2" == routeAreainfo.routeAreaType) {
                                    if ("0" == routeAreainfo.routeAreaStatus)
                                        iconpath = "static/images/1_03.png";
                                    else
                                        iconpath = "static/images/1_03gray.png";
                                } else if ("3" == routeAreainfo.routeAreaType) {
                                    if ("0" == routeAreainfo.routeAreaStatus)
                                        iconpath = "static/images/changdi.png";
                                    else
                                        iconpath = "static/images/changdigray.png";
                                } else {
                                    if ("0" == routeAreainfo.routeAreaStatus)
                                        iconpath = "static/images/1_05.png";
                                    else
                                        iconpath = "static/images/1_05gray.png";
                                }
                                var areaText = "<li><label><input type=\"checkbox\" "
                                		+ " startId='" + routeAreainfo.startId + "' endId='" + routeAreainfo.endId + "' "
                                        + "name=\"routeAreaIds\" value=\"" + routeAreainfo.routeAreaId
                                        + "\"><img  title=\"" + routeAreainfo.routeAreaName + "\" src=\"" + iconpath
                                        + "\"/>" + "<a   onclick=\"getPointsByRouteAreaId('"
                                        + routeAreainfo.routeAreaId + "','" + routeAreainfo.routeAreaBuffer
                                        + "');\" href='javascript:void(0);' class=\"vehicle_label\"  >"
                                        + routeAreainfo.routeAreaName + "</a>" + "</label></li>";
                                $("#planRouteAreaList").append(areaText);
                            }
                        }
                        $(".gm-style>div:last").css("right", "15px");
                    } else {
                        $("#account_num").html("(0)");
                    }
                    if (menuType == "0" || menuType == "1" || menuType == "2") {
                        $("#planRouteAreaList").removeClass("hidden");
                        $("#panelList").addClass("hidden");
                        $("#patrolList").addClass("hidden");
                        $("#classify").addClass("hidden");
                        $("#addDelUpdate").removeClass("hidden");
                        $("#addRapoint").addClass("hidden");
                        $("#addLandMarker").addClass("hidden");
                    }
                }
            });

}

/**
 * 根据输入条件，模糊查询路线
 */
function fliter() {
    var routeName = $("#routeSearch").val();
    $("#planRouteAreaList").html("");
    var portUrl = getRootPath() + "monitorroutearea/findAllRouteAreas.action?ids=" + menuType + "&routeName="
            + routeName;
    $
            .ajax({
                type: "POST",
                url: portUrl,
                dataType: "json",
                cache: false,
                async: false,
                error: function(e, message, response) {
                    console.log("Status: " + e.status + " message: " + message);
                },
                success: function(jsonObj) {
                    var routeArea = jsonObj;
                    // areaText = "<ul class=\"Custom_list\">";
                    if (routeArea.length > 0) {
                        var routeSearch = '<div class="list_search"><input class="form-control" type="text" id="routeSearch" name="routeSearch"><button onclick="fliter()" type="button" class="btn btn-default "><span class="glyphicon glyphicon-search"></span></button></div>';
                        $("#planRouteAreaList").append(routeSearch);
                        $("#account_num").html("(" + routeArea.length + ")");
                        for (var i = 0; i < routeArea.length; i++) {
                            var routeAreainfo = routeArea[i];
                            if (routeAreainfo.routeAreaId != null && routeAreainfo.routeAreaId != "") {
                                var iconpath = "";
                                if ("0" == routeAreainfo.routeAreaType) {
                                    if ("0" == routeAreainfo.routeAreaStatus)
                                        iconpath = "static/images/1_10.png";
                                    else
                                        iconpath = "static/images/1_10gray.png";
                                } else if ("1" == routeAreainfo.routeAreaType) {
                                    if ("0" == routeAreainfo.routeAreaStatus)
                                        iconpath = "static/images/1_09.png";
                                    else
                                        iconpath = "static/images/1_09gray.png";
                                } else if ("2" == routeAreainfo.routeAreaType) {
                                    if ("0" == routeAreainfo.routeAreaStatus)
                                        iconpath = "static/images/1_03.png";
                                    else
                                        iconpath = "static/images/1_03gray.png";
                                } else if ("3" == routeAreainfo.routeAreaType) {
                                    if ("0" == routeAreainfo.routeAreaStatus)
                                        iconpath = "static/images/changdi.png";
                                    else
                                        iconpath = "static/images/changdigray.png";
                                } else {
                                    if ("0" == routeAreainfo.routeAreaStatus)
                                        iconpath = "static/images/1_05.png";
                                    else
                                        iconpath = "static/images/1_05gray.png";
                                }
                                var areaText = "<li><label><input type=\"checkbox\"  "
                                        + "name=\"routeAreaIds\" value=\"" + routeAreainfo.routeAreaId
                                        + "\"><img  title=\"" + routeAreainfo.routeAreaName + "\" src=\"" + iconpath
                                        + "\"/>" + "<a   onclick=\"getPointsByRouteAreaId('"
                                        + routeAreainfo.routeAreaId + "','" + routeAreainfo.routeAreaBuffer
                                        + "');\" href='javascript:void(0);' class=\"vehicle_label\"  >"
                                        + routeAreainfo.routeAreaName + "</a>" + "</label></li>";
                                $("#planRouteAreaList").append(areaText);
                            }
                        }
                    } else {
                        $("#account_num").html("(0)");
                    }
                    if (menuType == "0" || menuType == "1" || menuType == "2") {
                        $("#planRouteAreaList").removeClass("hidden");
                        $("#panelList").addClass("hidden");
                        $("#patrolList").addClass("hidden");
                        $("#classify").addClass("hidden");
                        $("#addDelUpdate").removeClass("hidden");
                        $("#addRapoint").addClass("hidden");
                        $("#addLandMarker").addClass("hidden");
                    }
                }
            });

}
/**
 * 根据区域或线路编号获取
 */
function getPointsByRouteAreaId(routeAreaId, routeAreaBuffer) {
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
            var lsMonitorRouteAreaBO = jsonObj.lsMonitorRouteAreaBO;
            var lsMonitorRaPointBOs = jsonObj.lsMonitorRaPointBOs
            GisClearOverlays(overlaysArray);
            var points = lsMonitorRaPointBOs;
            var drawType = lsMonitorRouteAreaBO.routeAreaType;
            var pointArray = [];
            if (points.length > 0) {
                for (var i = 0; i < points.length; i++) {
                    pointArray.push({
                        lat: points[i].latitude,
                        lng: points[i].longitude
                    });
                }
                if ("0" == drawType) {
                    var routeColor = "";
                    if (lsMonitorRouteAreaBO.routeAreaColor != null) {
                        routeColor = "#" + lsMonitorRouteAreaBO.routeAreaColor;
                    } else {
                        routeColor = "#00ff00";
                    }
                    var drawPolylineStyle = {
                        "color": routeColor,
                        "weight": lsMonitorRouteAreaBO.routeAreaBuffer || 10,
                        "opacity": 0.5
                    }
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
                    //points.splice(0,1,null);
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
                    /*google自动分析*/
                    //GisDirectSearchForPlanRoute(GisHandlePointByJson(origin),GisHandlePointByJson(end),ggPts,null,getDrawRouteArea);
                    var polyline = GisShowPolyLineInMap(pointArray, true, drawPolylineStyle);
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
            } else {
                bootbox.alert($.i18n.prop("map.routeArea.select.noPlanRoute"));
            }

        }
    });
}

function getAllSessionCarforInit() {

}

/**
 * 接受后台传入坐标标注车辆
 */

function getSocketCoods(coods) {
    var vehicleSrc = "static/images/ic_03.png";
    var point = new google.maps.LatLng(coods.lat, coods.lng);
    GisCreateMarker(point, vehicleSrc, "vehicle");
    //createSVGMarker(point, vehicleSrc, "vehicle");
}

function showVehicleInMap() {
    //	var vehicleType = [];
    //	$('input[name="vehicleTypes"]:checked').each(function(){
    //		vehicleType.push($(this).val());
    //	});
    //	if(vehicleType.indexOf("0")>-1){
    //		GisShowOverlays(vehicleMarkers);
    //		//markerclusterer = GisMarkersClusterer(vehicleMarkers, 60, markerclusterer);
    //	}else{
    //		GisHiddenOverlays(vehicleMarkers);
    //		GisClearMarkerClusterer(markerclusterer);
    //	}
}

function showPatrolInMap() {
    //	var vehicleType = [];
    //	$('input[name="vehicleTypes"]:checked').each(function(){
    //		vehicleType.push($(this).val());
    //	});
    //	if(vehicleType.indexOf("1")>-1){
    //		GisShowOverlays(patrolMarkers);
    //		partolclusterer = GisMarkersClusterer(patrolMarkers, 60, partolclusterer);
    //	}else{
    //		GisHiddenOverlays(patrolMarkers);
    //		GisClearMarkerClusterer(partolclusterer);
    //	}
}
/**
 * 点击巡逻队定位
 * @param trackUnitNumber
 */
function getPatrolLocation(patrolId, longitude, latitude) {
    if (pointMarkers) {
        for (var i = 0; i < patrolMarkers.length; i++) {
            if (patrolMarkers[i].getTitle() == ("" + patrolId)) {
                //GisRemoveMarkerByName(vehicleMarkers[i],true);
                var location = {
                    lat: latitude,
                    lng: longitude
                };
                GisSetHomeCenter(location);
            }
        }
    }
    /*var getUrl = getRootPath() + "vehiclestatus/findPatrolByNumber.action";
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
    			if(pointMarkers){
    	    		for ( var i = 0; i < pointMarkers.length; i++) {
    	    			if(pointMarkers[i].getTitle() == (""+patrolStatus.vehicleId)){
    	    				//GisRemoveMarkerByName(vehicleMarkers[i],true);
    	    				var location = {lat:patrolStatus.latitude,lng:patrolStatus.longitude};
    	    				GisSetHomeCenter(location);
    	    				GisShowInfoWindow(pointMarkers[i],"",false);  
    	    			}
    	    		}
    	    	}else{
    	    		//bootbox.error($.i18n.prop("map.routeArea.select.noPatrol"));
    	    	}
    		}else{
    			//bootbox.error($.i18n.prop("map.routeArea.select.noPatrol"));
    		}
    	}
    });*/
}

/**
 * @param flag 是否清除巡逻队
 */
function clearAllOverlays(flag) {
    GisClearOverlays(showPointMarkers);
    //GisdirectionsDisplayVisible(false);
    GisClearOverlays(vehicleMarkers);
    GisClearOverlays(svgMarkers);
    GisClearMarkerClusterer(markerclusterer);
    GisClearOverlays(pointMarkers);
    GisClearOverlays(overlaysArray);
    GisClearOverlays(overlaysArrayRoute);
    GisClearOverlays(lengthArr);
    GisClearOverlays(lenArr);
    GisClearOverlays(circleArr);
    GisClearOverlays(alarmArr);
    GisClearOverlays(overlays);
    GisClearOverlays(landerMarkers);
    GisClearPolygonLength();
    GisClearLineTrack();
    GisClearInterValTrack();
    trackingLine = undefined;//车辆的行走路线
    trackingTripId = undefined;//详细车辆的行程id
    trackingMaker = undefined;//轨迹车辆的Maker
    tripBufferPoints = new Array();//存储缓冲车辆数据
    tripRoutePoints = new Array();
    alarmIdForRecord = undefined;
    clearAllTimeout();//清除所有的定时任务
    if (true) {
        //GisClearOverlays(patrolMarkers);
        GisClearMarkerClusterer(partolclusterer);
    }
}

/**
 * 查询所有车辆信息并定时刷新
 * @param 各种参数信息 where
 */
function queryAndResetMainPage() {
    resetVehicleInfo();
    function resetVehicleInfo() {
        clearAllOverlays(false);
        findAllVehicleStatus(true);//查询所有车辆状态，显示在地图，列表显示
        if (systemModules.isPatrolOn) {
            findAllPatrolStatus(false);
        }
        //---findAllPatrolStatus(false);//查询所有巡逻队状态，显示在地图，列表不显示  后期根据配置
        findAlarmList();
        showVehicleInMap();
        if (systemModules.isPatrolOn) {
            showPatrolInMap();
        }
        //---showPatrolInMap();
        refreshTimeoutValue = setTimeout(function() {
            resetVehicleInfo();
        }, refreshMainTime);
    }
}

/**
 * 清除所有定时任务
 */
function clearAllTimeout() {
    if (refreshTimeoutValue != -1) {
        window.clearTimeout(refreshTimeoutValue);
        refreshTimeoutValue = -1;
    }
}

/******************************轨迹回放按钮功能******************************************/
/**
 * 重定位
 */
function replaceLoc() {
    //var pointArray = GisGetDrawPath();
    if (tripBufferPoints.length > 0) {
        GisSetViewPortByArray(tripBufferPoints);
    }
}
/**
 * 回放
 */
function replayStart() {
    GisPathPlay(replayIcon, getTrackingVehicleInfo);
}
/**
 * 暂停
 */
function replayPause() {
    GisPathPause();
}
/**
 * 继续回放
 */
function replayContinue() {
    GisContinuePlay(replayIcon, getTrackingVehicleInfo);
}
/**
 * 停止回放
 */
function replayStop() {
    GisPathStop(replayIcon, getTrackingVehicleInfo);
}
/**
 * 加速
 */
function replaySpeedUp() {
    GisPathAccelaratePaly(replayIcon, getTrackingVehicleInfo);
}
/**
 * 减速
 */
function replaySpeedDown() {
    GisPathDecelaratePaly(replayIcon, getTrackingVehicleInfo);
}
/**
 * 用于接收轨迹回放点的信息
 * @param obj
 */
function getTrackingVehicleInfo(markerInfo) {
    if(markerInfo){
        $("#electricityValue").html(markerInfo.electricityValue);
        $("#trackingElockSpeed").html((parseFloat(markerInfo.elockSpeed)).toFixed(2));
        $("#trackingAltitude").html(markerInfo.altitude);
        $("#trackingLatitude").html(markerInfo.latitude);
        $("#trackingLongitude").html(markerInfo.longitude);
        $("#trackingDirection").html(markerInfo.direction);
    }else{
        $("#electricityValue").html("0.0");
        $("#trackingElockSpeed").html("0.0");
        $("#trackingAltitude").html("0.0");
        $("#trackingLatitude").html("0.0");
        $("#trackingLongitude").html("0.0");
        $("#trackingDirection").html("0.0");
    }
}

/**
 * 获取巡逻队位置
 */
function getPatrolLocationInit() {
    var patrolLocationUrl = getRootPath() + "alarmdeal/getPatrolLocation.action";
    $.ajax({
        type: "POST",
        url: patrolLocationUrl,
        dataType: "json",
        cache: false,
        async: false,
        error: function(e, message, response) {
            console.log("Status: " + e.status + " message: " + message);
        },
        success: function(data) {
            if (data != null) {
                var patrolLatitude = data.patrolLatitude;
                var patrolLongitude = data.patrolLongitude;
                patrolLocation = {
                    lat: patrolLatitude,
                    lng: patrolLongitude
                };
            }
        }
    });
}

function findOnWayVehicleByTravelStatus(travleStatus) {
    var online = "static/images/online.png";
    var offline = "static/images/offline.png";
    $("#header_title").html($.i18n.prop("link.system.vehicleList"));
    $("#panelList").addClass("hidden");
    $("#vehicelTrackList").addClass("hidden");
    var travlStatusUrl = getRootPath() + "vehiclestatus/findOnWayVehicleByTravelStatus.action?travleStatus="
            + travleStatus;
    $.ajax({
        type: "POST",
        url: travlStatusUrl,
        dataType: "json",
        cache: false,
        async: false,
        error: function(e, message, response) {
            console.log("Status: " + e.status + " message: " + message);
        },
        success: function(data) {
            $("#vehicelTrackList").addClass("hidden");
            $("#vehicelStatusList").removeClass("hidden");
            $("#vehicelStatusList").html("");
            if (data.vehicleInfoVOs) {
                var html = '';
                html += '		<div class="alert_table">';
                html += '			<table class="table table-condensed table-striped table-hover">';

                for ( var key in data.vehicleInfoVOs) {
                    (function(i) {
                        var gpsVehicleInfo = data.vehicleInfoVOs[key];
                        if (gpsVehicleInfo.trackingDeviceNumber != null && gpsVehicleInfo.trackingDeviceNumber != "") {
                            var iconpath = "";
                            var obj = {
                                data: gpsVehicleInfo
                            };
                            if ("0" == gpsVehicleInfo.riskStatus) {
                                iconpath = "static/images/Safetruck.png";
                            } else if ("1" == gpsVehicleInfo.riskStatus)
                                iconpath = "static/images/warningtruck.png";
                            else if ("2" == gpsVehicleInfo.riskStatus)
                                iconpath = "static/images/alarmtruck.png";
                            else {
                                iconpath = "static/images/Safetruck.png";
                            }
                            html += '<tr><td style="width:207px">' + "<input type=\"checkbox\"><img alt=\""
                                    + gpsVehicleInfo.vehiclePlateNumber + "\"src=\"" + iconpath + "\"/>"
                                    + "<a  id=\"vehicle_" + gpsVehicleInfo.vehicleStatusId
                                    + "\" href='javascript:void(0);' class=\"vehicle_label\">"
                                    + gpsVehicleInfo.vehiclePlateNumber + " </a>" + "" + '</td>';
                            if (travleStatus == 0) {
                                html += '<td><img src=' + online + ' title=\"online\"/></td></tr>';
                            } else {
                                html += '<td><img src=' + offline + ' title=\"offline\"/></td></tr>';
                            }

                            //						
                            //							$("#vehicle_"+gpsVehicleInfo.vehicleStatusId).on("click", function(){
                            //								if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
                            //									var obj = {data:gpsVehicleInfo};
                            //				                	getVehiclePaths(obj,ALL_POINT);
                            //								}
                            //			            	});
                            $(document).undelegate("#vehicle_" + gpsVehicleInfo.vehicleStatusId, "click");
                            $(document).delegate("#vehicle_" + gpsVehicleInfo.vehicleStatusId, "click", function(e) {
                                if (typeof (gpsVehicleInfo.vehicleStatusId) != "undefined") {
                                    var obj = {
                                        data: gpsVehicleInfo
                                    };
                                    getVehiclePaths(obj, ALL_POINT);
                                }
                            });
                        }
                        //onclick=\"getVehiclePaths("+JSON.stringify(gpsVehicleInfo)+",'"+ALL_POINT+"')\"
                    })(key);
                }
                html += '		    	</table>';
                html += '	         <div class="clearfix"></div>';
                $("#vehicelStatusList").append(html);
            }
        }
    });

    //	$.post(travlStatusUrl,"",function(data){
    //		
    //	})
}

/**
 * 控制中心撤回推送消息方法
 * @param obj
 * @param data
 */
function patrolAlarmRevoke(obj, data) {
    var url = root + "/alarmdeal/patrolAlarmRevoke.action?alarmId=" + data.alarmId + "&patrolId=" + obj.patrolId;
    $.ajax({
        type: "POST",
        url: url,
        dataType: "json",
        cache: false,
        async: false,
        error: function(e, message, response) {
            console.log("Status: " + e.status + " message: " + message);
        },
        success: function(data) {
            if (data) {
                if (data.alarmId == null) {
                    bootbox.alert($.i18n.prop("alarm.label.alarmId.cannotFind"));
                } else {
                    //bootbox.alert($.i18n.prop("alarm.label.alarmDeal.msgRevokeTitle.success"));
                    bootbox.success($.i18n.prop("alarm.label.alarmDeal.msgRevokeTitle.success"), function() {
                        //刷新页面，避免重复“撤销”
                        window.location.reload();
                    })

                }
            }
        }
    });
}
