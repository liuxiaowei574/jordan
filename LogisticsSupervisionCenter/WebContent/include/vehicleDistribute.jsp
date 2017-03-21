<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/include.jsp" />
<jsp:include page="/include/left.jsp" />
<script type="text/javascript" src="${root}/gis/map.js.jsp"></script>
<title><fmt:message key="gis.vehicle.distrbute" /></title>
<style type="text/css">
.trackingFont {
	color: #ffffff;
}
</style>
</head>

<body>
	<div class="row site">
		<div class="wrapper-content margint95 margin60">
			<%--导航 --%>
			<c:set var="parentName"><fmt:message key="menu.statistic.analysis"/></c:set>
			<c:set var="pageName"><fmt:message key="statistic.index.vechileMonitorDistributed"/></c:set>
			<jsp:include page="/include/navigation2.jsp" >
				<jsp:param value="${root }/statisitc/toList.action" name="parentUrl"/>
				<jsp:param value="${parentName }" name="parentName"/>
				<jsp:param value="${pageName }" name="pageName"/>
			</jsp:include>
			
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="Features pull-right">
						<ul>
							<li><a href="${root}/statisitc/toList.action" class="btn btn-info"><fmt:message key="common.button.back"/></a></li>
						</ul>
					</div>
					<fmt:message key="statistic.sub.vechileMonitorDistributed"/>
				</div>
				<div class="panel-body" id="map_vehicle"  style="height: 100%; width: 100%;">
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	   
	   $(function() {
		   var vehicleLocation  = [{lng:36.21980278,lat:31.76778889},
		                           {lng:35.83975,lat:31.66591667},
		                           {lng:36.22235833,lat:31.65118056},
		                           {lng:36.48928611,lat:32.42090278},
		                           {lng:36.01446944,lat:32.28922222},
		                           {lng:35.679825,lat:31.30863342},
		                           {lng:35.33793889,lat:31.81251389},
		                           {lng:36.11056389,lat:32.29569167},
		                           {lng:35.43099444,lat:31.55266667},
		                           {lng:36.32576667,lat:32.89629444},
		                           {lat : 31.8726438,lng : 35.9040163},
		                           {lat : 31.7726438,lng : 35.8540163},
		                           {lat : 31.8526438,lng : 35.8740163},
		                           {lat : 31.8726438,lng : 36.1740163},
		                           {lat : 31.9026438,lng : 36.2740163},
		                           {lat : 31.9326438,lng : 35.9040163},
		                           {lat : 31.9426438,lng : 35.9140163},
		                           {lat : 31.9526438,lng : 35.9340163},
		                           {lng:35.3659,lat:31.69775278}];
		       var pointArr = {
		   				lat : 31.9326438,
		   				lng : 35.9040163
		   			};
	   			var params = {
	   				mapId : "map_vehicle",// div地图id
	   				isShowMapType : false,// 是否展示地图类型
	   				mapTypeDirect : "top_left",
	   				zoom : 12,// 地图级别
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
	   	       getVehicleDistrbute(vehicleLocation);
	   		   
	   	});
	   function getVehicleDistrbute(vehicelArr){
		   for (var key in vehicelArr) {
				(function(i){
				 var vehicle = vehicelArr[key]; 
				 var iconpath = "../static/images/Safetruck1.png";
				 var vehicleMarker = GisCreateMarker({
						lat : vehicle.lat,
						lng : vehicle.lng
					}, iconpath, "","");
				})(key);
			}
		
	   }
    </script>

</html>