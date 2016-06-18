<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<link rel="stylesheet" href="${root}/static/css/bootstrap.css">
<script src="${root}/static/js/bootstrap.js"></script>
</head>
<body>
	<div class="col-md-9 sub_content_styles" id="cont">
		<div class="header">
			<div class="logo">
				<span> E-tracking</span> System
			</div>
		</div>
		<div id="header_title" class="header_title"></div>
		<ul class="Custom_list" id="panelList">

		</ul>
		<ul class="Custom_list" id="planRouteAreaList">

		</ul>
		<ul class="Custom_list" id="patrolList">

		</ul>

		<jsp:include page="/monitor/monitorRaPoint/add.jsp"></jsp:include>
	</div>

	<div class="col-md-11 sub_footer">
		<div class="row">
			<div class="col-md-4 left_ico">
				<ul id="classify" class="car_type">
					<li><a id="map_open" href="javascript:void(0)"><span
							class="glyphicon2 leftlist"></span></a></li>
					<li class="dropdown arrow_Port">     
					    <a href="" id="qidian" href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">  
					       <span class="glyphicon2 qidian"></span>
					    </a>
					    <div class="dropdown-menu Port_s_list" aria-labelledby="qidian" >
					    	<ul id="startPort"></ul>
					    </div>
					</li>
					<li class="dropdown arrow_Port"> 
						<a href="" id="zhongdian" href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">  
							<span class="glyphicon2 zhongdian"></span>
						</a>
						<div class="dropdown-menu Port_s_list" aria-labelledby="zhongdian" >
					    	<ul id="endPort"></ul>
					    </div>
					</li>
					<li class="dropdown">
						<a href="" id="jgcl" href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">  
							<span class="glyphicon2 jgcl"></span>
						</a>
						<div class="dropdown-menu Port_s_list" aria-labelledby="jgcl" >
					    	<ul>
					    		<li onclick="showVehicleInMap();">
					    			<label><input type="checkbox" value="0" name="vehicleTypes" checked="checked"><fmt:message key="map.checkbox.commonVehicle"/></label>
					    		</li>
					    		<li onclick="showPatrolInMap();">
					    			<label><input type="checkbox" value="1" name="vehicleTypes" checked="checked"><fmt:message key="map.checkbox.patrol"/></label>
					    		</li>
					    	</ul>
					    </div>
					</li>
					
				</ul>

				<ul id="addDelUpdate" class="car_type ">
					<li><a id="map_open1" href="javascript:void(0)"><span
							class="glyphicon2 leftlist"></span></a></li>
					<li><a id="addroutearea" href="javascript:void(0)"><span
							class="glyphicon2 add_route"></span></a></li>
					<li><a id="editRouteArea" href="javascript:void(0)"><span
							class="glyphicon2 edit_route"></span></a></li>
					<li><a id="deleteRouteArea" href="javascript:void(0)"><span
							class="glyphicon2 delete_route"></span></a></li>
				</ul>
			</div>
			<div class="col-md-6" style="">
				<ul id="classify" class="legend02">	
					<li class=""><span><fmt:message key="map.legend" />: </span></li>
					<li class="legend_port"><span><fmt:message key="map.legend.port" /></span></li>
					<li class="legend_normal"><span><fmt:message key="map.legend.normalVehicle" /></span></li>
					<li class="legend_warn"><span><fmt:message key="map.legend.warnVehicle" /></span></li>
					<li class="legend_alarm"><span><fmt:message key="map.legend.alarmVehicle" /></span></li>
					<li class="legend_trajec"><span><fmt:message key="map.legend.trajecLine" /></span></li>
					<li class="legend_schedu"><span><fmt:message key="map.legend.scheduLine" /></span></li>
				</ul>
			</div>
			<div class="col-md-4 right_ico">
				<ul class="car_type pull-right">
					<li><a id="raPointList" href="javascript:void(0)"><span
							class="glyphicon2 cllb"></span></a></li>
					<li><a id="routeAreaList" href="javascript:void(0)"><span
							class="glyphicon2 lxgl"></span></a></li>
					<li><a id="siteList" href="javascript:void(0)"><span 
							class="glyphicon2 cdgl"></span></a></li>
					<li><a id="areaList" href="javascript:void(0)"><span 
							class="glyphicon2 qygl"></span></a></li>
					<li><a id="vehicleHisList" href="javascript:void(0)"><span 
							class="glyphicon2 gjhf" aria-hidden="true"></span></a></li>
					<li><a id="patrolShowList" href="javascript:void(0)"><span
							class="glyphicon2 xld" aria-hidden="true"></span></a></li>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>