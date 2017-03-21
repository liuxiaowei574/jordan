<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%-- <link rel="stylesheet" href="${root}/static/css/bootstrap.css"> --%>
<%-- <script src="${root}/static/js/bootstrap.js"></script> --%>
</head>
<body>
	<div class="col-md-9 sub_content_styles" id="cont">
		<div class="header">
			<div class="logo">
				<span> E-Tracking</span> System
			</div>
		</div>
		<div id="header_title_div" class="header_title">
			<span id="header_title"></span><span id="account_num"></span>
		</div>
<!-- 		        <label class=" control-label ">Start Time:</label> -->
<!-- 				<div class="input-group date " id="trackStartTime"> -->
<!-- 					<input type="text" class="form-control " id="s_trackStartTime" -->
<!-- 						name="s_trackStartTime" readonly> <span -->
<!-- 						class="input-group-addon"><span -->
<!-- 						class="glyphicon glyphicon-remove"></span></span> -->
<!-- 				</div>  -->
<!-- 				<label class=" control-label ">End Time:</label> -->
<!-- 				<div class="input-group date" id="trackEndTime"> -->
<!-- 					<input type="text" class="form-control" id="s_trackEndTime" -->
<!-- 						name="s_trackEndTime" readonly> <span -->
<!-- 						class="input-group-addon"><span -->
<!-- 						class="glyphicon glyphicon-remove"></span></span> -->
<!-- 				</div> -->
		<div id="vehicelfilter" class="vehicelfilter"></div>

		<ul class="Custom_list" id="panelList">

		</ul>
		<ul class="Custom_list" id="planRouteAreaList">

		</ul>
		<ul class="Custom_list" id="patrolList">

		</ul>

		<ul class="Custom_list" id="vehicelStatusList"></ul>

		<ul class="Custom_list" id="trackquery">
		<c:if test="${systemModulesLqx.isPatrolOn()}">
			<li ><label class=" control-label hidden">Start Time:</label>
				<div class="input-group date hidden" id="trackStartTime">
					<input type="text" class="form-control " id="s_trackStartTime"
						name="s_trackStartTime" readonly> <span
						class="input-group-addon"><span
						class="glyphicon glyphicon-remove"></span></span>
				</div> <label class=" control-label hidden">End Time:</label>
				<div class="input-group date hidden" id="trackEndTime">
					<input type="text" class="form-control" id="s_trackEndTime"
						name="s_trackEndTime" readonly> <span
						class="input-group-addon"><span
						class="glyphicon glyphicon-remove"></span></span>
				</div>
				</li>
				<div>
					<label class="control-label">Track Type:</label> 
					<select class="form-control input-sm" id="TrackType"
						name="TrackType">
						<option value=""><fmt:message key="main.list.option.select" /></option> 
						<option value="1">Vehicle</option>
						<option value="2">Patrol</option>
					</select>
					<button id="patrolQuery" type="button" class="btn btn-info" >
						<fmt:message key="common.button.query" />
					</button>
				</div>
				</li>
				</c:if>
		</ul>
       



	<jsp:include page="/monitor/monitorRaPoint/add.jsp"></jsp:include>
	<jsp:include page="/monitor/mergePatrol.jsp"></jsp:include>
	<jsp:include page="/include/addLandMarker.jsp"></jsp:include>
	</div>
      
	<div class="col-md-11 sub_footer">
		<div class="row">
			<div class="col-md-4 left_ico">
 			
				<ul id="classify" class="car_type">
					<li><a id="map_open" href="javascript:void(0)"><span
							class="glyphicon2 leftlist"></span></a></li>
				</ul>
				<ul id="addDelUpdate" class="car_type ">
					<li><a id="map_open1" href="javascript:void(0)"><span
							class="glyphicon2 leftlist"></span></a></li>
					<li><a id="addroutearea" href="javascript:void(0)"><span
							title="<fmt:message key="monitor.operate.addRouteArea" />" class="glyphicon2 add_route"></span></a></li>
					<li><a id="editRouteArea" href="javascript:void(0)"><span
							title="<fmt:message key="monitor.operate.editRouteArea" />" class="glyphicon2 edit_route"></span></a></li>
					<li><a id="deleteRouteArea" href="javascript:void(0)"><span
							title="<fmt:message key="monitor.operate.deleteRouteArea" />" class="glyphicon2 delete_route"></span></a></li>
				</ul>

				<ul id="handlePatrol" class="car_type hidden">
					<li><a id="map_open2" href="javascript:void(0)"><span
							class="glyphicon2 leftlist"></span></a></li>
					<li><a id="addPatrol" href="javascript:void(0)"><span
							title="<fmt:message key="monitor.operate.addPatrol" />" class="glyphicon2 add_route"></span></a></li>
					<li><a id="editPatrol" href="javascript:void(0)"><span
							title="<fmt:message key="monitor.operate.editPatrol" />" class="glyphicon2 edit_route"></span></a></li>
					<li><a id="deletePatrol" href="javascript:void(0)"><span
							title="<fmt:message key="monitor.operate.deletePatrol" />" class="glyphicon2 delete_route"></span></a></li>
				</ul>

               <ul id="handleLanderMarker" class="car_type hidden">
					<li><a id="map_open3" href="javascript:void(0)"><span
							class="glyphicon2 leftlist"></span></a></li>
					<li><a id="addLanderMarker" href="javascript:void(0)"><span
							title="<fmt:message key="monitor.operate.addLandMarker" />"  class="glyphicon2 add_route"></span></a></li>
					<li><a id="editLanderMarker" href="javascript:void(0)"><span
							title="<fmt:message key="monitor.operate.editLandMarker" />" class="glyphicon2 edit_route"></span></a></li>
					<li><a id="deleteLanderMarker" href="javascript:void(0)"><span
							title="<fmt:message key="monitor.operate.deleteLandMarker" />" class="glyphicon2 delete_route"></span></a></li>
				</ul>
				<ul id="reInit" class="reInit">
					<li><a id="resetMap" href="javascript:void(0)"><span
						title="<fmt:message key="foot.title.bestView" />"	class="glyphicon glyphicon-move" style=""></span></a></li>
					<li><a id="resetVehicle" href="javascript:void(0)"><span
						title="<fmt:message key="foot.title.allVehicle" />"	class="glyphicon glyphicon-screenshot" style=""></span></a></li>
					<li>
                      <div class="dropup" >
					  <a id="sss" type="button" href="javascript:void()"  data-toggle="dropdown" data-submenu><span title="<fmt:message key="foot.title.vehicleFilter" />"	 class="glyphicon2 jgcl " aria-hidden="true"></span></a>
					      <ul class="dropdown-menu">
					     <!-- 检入口岸 -->
					     
					  <li class="dropdown-submenu">
					    <a href="javascript:void()" tabindex="0">Start Port</a>
					     <ul class="dropdown-menu Port_s_list" id="startPort"></ul>
					   </li>
					   <li class="divider"></li>
					<!-- 检出口岸 -->
					<li class="dropdown-submenu">
					  <a href="javascript:void()" tabindex="0">End Port</a>
					  <ul class="dropdown-menu Port_s_list" id="endPort"></ul>
					</li>
					<li class="divider"></li>
					<!-- 车辆状态 -->
					<c:if test="${systemModulesLqx.isPatrolOn()}">
					<li class="dropdown-submenu">
					  <a href="javascript:void()" tabindex="0">Vehicle Status</a>
					  <ul class="dropdown-menu Port_s_list" id="vehicleStatus">
					  		<li><label><input  type="checkbox" 
								value="0" name="vehicleStatus" checked="checked"><span>On Way</span></label>
							</li>
								<li><label><input
									 type="checkbox"
									value="1" name="vehicleStatus" ><span>End Trip</span></label>
								</li>
					  </ul>
					</li>
					</c:if>
					<!-- 巡逻队类型 -->
<!-- 					<li class="divider"></li> -->
					<c:if test="${systemModulesLqx.isPatrolOn()}">
					<li class="dropdown-submenu">
					  <a href="javascript:void()" tabindex="0">Patrol Style</a>
					  <ul class="dropdown-menu Port_s_list" id="patrolStatus">
					  		<li><label><input onclick="findFilterPatrolStatus('escortPatrol');" type="checkbox" 
								value="0" name="vehicleTypes" checked="checked"><span>Escort Patrol</span></label>
							</li>
								<li><label><input
									onclick="findFilterPatrolStatus('enforcementPatrol');" type="checkbox"
									value="1" name="vehicleTypes" checked="checked"><span>Enforcemenet Patrol</span></label>
								</li>
					  </ul>
					  
					</li>
					</c:if>
					</ul>
					    </div>
                    </li>	
				</ul>

			</div>
			
			 
			<div class="col-md-6" style="width: 70%;">
				<ul id="classify" class="legend02">
					<li class=""><span style="margin-left: 0px;"><fmt:message key="map.legend" />: </span></li>
					<li class="legend_port"><span><fmt:message
								key="map.legend.port" /></span></li>
					<li class="legend_normal"><span><fmt:message
								key="map.legend.normalVehicle" /></span></li>
					<li class="legend_warn"><span><fmt:message
								key="map.legend.warnVehicle" /></span></li>
					<li class="legend_alarm"><span><fmt:message
								key="map.legend.alarmVehicle" /></span></li>
					<li class="legend_trajec"><span><fmt:message
								key="map.legend.trajecLine" /></span></li>
					<li class="legend_schedu"><span><fmt:message
								key="map.legend.scheduLine" /></span></li>
				</ul>
			</div>
               
			<div class="col-md-4 right_ico">
				<ul class="car_type pull-right">
					<li><a id="raPointList" href="javascript:void(0)"><span
							title="<fmt:message key="foot.title.monitorVehicl" />"
							class="glyphicon2 cllb"></span></a></li>
					<li><a id="routeAreaList" href="javascript:void(0)"><span
							title="<fmt:message key="foot.title.planRoute" />"
							class="glyphicon2 lxgl"></span></a></li>
					<c:if test="${systemModulesLqx.isAreaOn()}">
					<li><a id="siteList" href="javascript:void(0)"><span
							title="<fmt:message key="foot.title.planSite" />"
							class="glyphicon2 cdgl"></span></a></li>
					</c:if>		
					<li><a id="areaList" href="javascript:void(0)"><span
							title="<fmt:message key="foot.title.landmarker" />"
							class="glyphicon2 qygl"></span></a></li>
					<li><a id="vehicleHisList" href="javascript:void(0)"><span
							title="<fmt:message key="foot.title.tracking" />"
							class="glyphicon2 gjhf" aria-hidden="true"></span></a></li>
					<c:if test="${systemModulesLqx.isPatrolOn()}">
					<li><a id="patrolShowList" href="javascript:void(0)"><span
							title="<fmt:message key="foot.title.patrol" />"
							class="glyphicon2 xld" aria-hidden="true"></span></a></li>
					</c:if>
				</ul>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
$(function() {
	$('[data-submenu]').submenupicker();
	//时间控件
	$("#s_trackStartTime, #s_trackEndTime").datetimepicker({
		format: "yyyy-mm-dd hh:ii",//ii:分钟
		autoclose: true,
		todayBtn: true,
		language: 'en'
	});
	var statusCheckValues = [];
	$('input[name="vehicleStatus"]:checked').each(function(){
	    statusCheckValues.push($(this).val());
	});
	 $("input[name='vehicleStatus']").click( function(){
	     statusCheckValues = [];
	     $('input[name="vehicleStatus"]:checked').each(function(){
	 	    statusCheckValues.push($(this).val());
	 	});
         if(statusCheckValues.length>0){
             if(statusCheckValues.indexOf("0")>-1){
                 tripStatus = "1";
             }
             if(statusCheckValues.indexOf("1")>-1){
                 tripStatus = "3";
             } if(statusCheckValues.indexOf("0")>-1&&statusCheckValues.indexOf("1")>-1){
                 tripStatus = "1,3";
             }
         }
         else {
             tripStatus = "-1";
         }
         findAllVehicleStatus(true);
     });
	$("#patrolQuery").on('click',function(e){
		if($("#TrackType").val()=="1"){
			findVehicleTrackStatus(false);
		}else if($("#TrackType").val()=="2"){
			getPatrolTrack();
		}
	})
});

</script>
</html>