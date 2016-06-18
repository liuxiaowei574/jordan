<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
</head>
<style>
.planroute {
	position: absolute;
	right: -2px;
	bottom: 25px;
	max-height: 570px;
	max-width: 300px;
	overflow: hidden;
	border: #121212 solid 1px;
	border-color: rgba(223, 223, 223, 0.61);
	margin-left: 10px;
	background: #fff url(static/images/clouds.jpg) no-repeat;
}
.dvPanelshadow {
	box-shadow: 1px 0px 10px rgba(0, 0, 0, 0.28);
	margin-bottom: 0px;
}
.dvPaneloverflow {
	max-height: 570px;
	max-width: 298px;
	overflow: auto;
}
</style>
<body>

	<div id="map_canvas" class="map col-md-10"></div>
	<div class="app-right-top">
		<div class="toolscontainer">
			<ul>
				<li id="ceju"><a href="javascript:void(0)"><span
						class="ceju"></span>
					<fmt:message key="monitor.measure" /></a></li>
				<%-- 					<li><a href=""><span class="luxian"></span><fmt:message key="monitor.tracking"/></a></li> --%>
				<!-- 					<li><a href=""><span class="quyu"></span>区域规划</a></li> -->
			</ul>
		</div>
		<div class="search_box">
			<form onsubmit="submitFn(this, event);">
				<div class="search-wrapper">
					<div class="input-holder">
						<input type="text" class="search-input"
							placeholder="Type to search" />
						<button class="search-icon" onclick="searchToggle(this, event);">
							<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>

	<div id="bottomPanel" class="hidden"
		style="position: absolute; right: 400px; top: 20px; height: 2%;">
		<input type="button" value="<fmt:message key="monitor.replace"/>" onclick="replaceLoc();"> 
		<input type="button" value="<fmt:message key="monitor.replay"/>" onclick="replayStart();">
		<input type="button" value="<fmt:message key="monitor.pause"/>" onclick="replayPause();"> 
		<input type="button" value="<fmt:message key="monitor.continuePlay"/>" onclick="replayContinue();"> 
		<input type="button" value="<fmt:message key="monitor.stopPlay"/>" onclick="replayStop();">
		<input type="button" value="<fmt:message key="monitor.accelerate"/>" onclick="replaySpeedUp();"> 
		<input type="button" value="<fmt:message key="monitor.decelerate"/>" onclick="replaySpeedDown();">
	</div>

	<!-- 用于地图添加模态框 -->
	<div class="modal fade add_user_box" id="monitorAddModal" tabindex="-1"
		role="dialog" aria-labelledby="monitorAddModalTitle">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>

	<div id="dvPanelParent" style="border-radius: 5px;"
		class="panel panel-default planroute hidden">
		<div class="panel-heading">Distance DetailInfo</div>
		<div id="dvPanel" class="panel-body dvPaneloverflow"></div>
	</div>
	
	<%-- <div id="legend" style="" class="legend">
		<ul id="mainLegend">
			<li class="legend_port"><span><fmt:message key="map.legend.port" /></span></li>
			<li class="legend_normal"><span><fmt:message key="map.legend.normalVehicle" /></span></li>
			<li class="legend_warn"><span><fmt:message key="map.legend.warnVehicle" /></span></li>
			<li class="legend_alarm"><span><fmt:message key="map.legend.alarmVehicle" /></span></li>
			<li class="legend_trajec"><span><fmt:message key="map.legend.trajecLine" /></span></li>
			<li class="legend_schedu"><span><fmt:message key="map.legend.scheduLine" /></span></li>
			<li><span><fmt:message key="map.legend" /></span></li>
		</ul>
	</div> --%>

	<script type="text/javascript">
	function searchToggle(obj, evt){
		var container = $(obj).closest('.search-wrapper');

		if(!container.hasClass('active')){
			  container.addClass('active');
			  evt.preventDefault();
		}
		else if(container.hasClass('active') && $(obj).closest('.input-holder').length == 0){
			  container.removeClass('active');
			  // clear input
			  container.find('.search-input').val('');
			  // clear and hide result container when we press close
			  container.find('.result-container').fadeOut(100, function(){$(this).empty();});
		}
	}
   
</script>

</body>

</html>