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
.alarmButton{
	width:38px;
	heihet:29px;
	background-color: transparent;
}
</style>
<style type="text/css">
.dropdown02{
	overflow: visible;
	float:left;
	margin-left:10px;
	height:32px;
	width:auto;
}
.Sear_list{
	top:100%;
	left:auto;
	bottom: auto;
	right: 5px;
	width: 700px;
	padding:10px;
	height:400px;
	overflow:auto;
}
.dropdown02 .btn{
	padding:0;
	background-color:rgba(241,241,241,0.5);
	box-shadow: 0 0 3px rgba(0,0,0,0.5);
}

.dropdown02 .btn:hover, .dropdown02 .btn:focus{
	background-color:white;
}

.transform-group{
	filter:alpha(opacity=100); /*IE*/
	opacity:1;
	-moz-opacity:1;
	-khtml-opacity:1; 
}
.Sear_list table{
	margin-bottom: 0;
	font-size: 16px;
}
.Sear_list table thead{
	
}
.Sear_list table tbody{
	
}
.messagenumber{
	display: block;
    padding: 0px;
    border-radius: 100px;
    background: #ff2800; /* Old browsers */
    background: -moz-linear-gradient(top, #ff2800 0%, #bf0000 100%); /* FF3.6+ */
    background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#ff2800), color-stop(100%,#bf0000)); /* Chrome,Safari4+ */
    background: -webkit-linear-gradient(top, #ff2800 0%,#bf0000 100%); /* Chrome10+,Safari5.1+ */
    background: -o-linear-gradient(top, #ff2800 0%,#bf0000 100%); /* Opera 11.10+ */
    background: -ms-linear-gradient(top, #ff2800 0%,#bf0000 100%); /* IE10+ */
    background: linear-gradient(to bottom, #ff2800 0%,#bf0000 100%); /* W3C */
    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ff2800', endColorstr='#bf0000',GradientType=0 ); /* IE6-9 */
    color: #ffffff;
    text-shadow: none;
    display: block;
    height: 25px;
    font-size: 10px;
    line-height: 1.9;
    width: 25px;
    border: 2px solid #ffffff;
    position: absolute;
    margin-top: -41px;
    margin-left: 22px;
}
.Sear_list:after{
	position: absolute;
    top:-10px;
    left:auto;
    right: 5px;
    display: inline-block;
    font-family: 'Glyphicons Halflings';
    font-style: normal;
    font-weight: 400;
    line-height: 1;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
     content: "\e253";
     color: #fff;
     text-shadow: 0 -1px 0px rgba(0,0,0,0.3);
}

#a a:hover
{
  text-decoration:underline
}
</style>
<body>
    <audio  id="bsound" src="${root }/static/images/ALARM3.WAV" ></audio>
    <div id="mask" class="maskLqx" style="display: none;">
		<iframe id="align-center" name="align-center" class="align-center"
			frameborder="0" src="${root}/include/indexGDT.jsp"
			scrolling="no"></iframe>
	</div>
	<form id="saveVehicelPtsForm" method="post" action="${root }/monitorroutearea/saveRaPointByVehicle.action">
	     <input class="hidden" type="text" id="pointJson"  name ="pointJson"/>
	</form>
	<form id="imagetopdf" method="post" action="${root }/monitorroutearea/imageToPDF.action">
	     <input class="hidden" type="text" id="base64sString"  name ="base64sString"/>
	</form>
	<div id="map_canvas" class="map col-md-10"></div>
	<div class="app-right-top">
	    <div class="toolscontainershowpoint">
			<ul>
				<li><a id="handlerPoint"  href="javascript:void(0)">Show Points</a></li>
			</ul>
		</div>
		<div class="toolscontainer">
			<ul>
				<li id="ceju"><a href="javascript:void(0)"><span
						class="ceju"></span>
					<fmt:message key="monitor.measure" /></a></li>
			</ul>
		</div>
		<div class="search_box">
				<div class="search-wrapper">
					<div class="input-holder">
						<input type="text" id = "search-input" class="search-input"
							placeholder="Type to search" />
						<button id="search_btn" class="search-icon" onclick="searchToggle(this, event);">
							<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						</button>
					</div>
				</div>
		</div>
		<div id="rightAlarm" class="dropdown02">
			<button id="dropdownAlarm" class= "btn 	 alarmButton " onclick="showAlarm()" role="button" aria-haspopup="true" aria-expanded="false">
				<img src="${root}/static/images/alarm.gif">
				<span class="messagenumber" id="messagenumber">0</span></button>
				<div  id ="a" class="dropdown-menu Sear_list" aria-labelledby="drop3" style="left:auto;padding:10px; ">
		            <table class="table table-bordered table-striped">
       			         <thead>
       			           <tr>
       			             <td><fmt:message key="alarm.label.vehiclePlateNumber"/></td>
       			             <td><fmt:message key="alarm.label.alarmTypeName"/></td>
       			             <td><fmt:message key="alarm.label.alarmTime"/></td>
       			           </tr>
       			         </thead>
       			         <tbody id="alarmListTbody">
       			           	
       			         </tbody>
       			      </table>
       			      <!-- <div id="loading">Loading...</div> -->
	              </div>
			</div>
		</div>

	
	<div id="playerbox" class="player-box hidden">
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
	
	
	<!-- 查看图像的模态窗口 -->
	<div class="modal fade" id="imageModal1" tabindex="-1" role="dialog" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content" style="background-color: rgba(255, 255, 255, 0); border: 0px solid rgba(0, 0, 0, 0); box-shadow: 0 0 0 rgba(0, 0, 0, 0); text-align: center; border-radius: 6px;">
	    </div>
	  </div>
	</div>

    <div class="modal fade add_user_box" id="propertySetModal" tabindex="-1"
		role="dialog" aria-labelledby="propertySetTitle">
		<div class="modal-dialog"  role="document">
			<div class="modal-content"></div>
		</div>
	</div>
    
	<!-- 用于地图添加模态框 -->
	<div class="modal fade add_user_box" id="alarmHandlerModal" tabindex="-1" style="overflow: auto;"
		role="dialog" aria-labelledby="alarmHandlerTitle">
		<div class="modal-dialog" style="width:700px" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	
	<div class="modal fade add_user_box" id="addAlarmModal" tabindex="-1"
		role="dialog" aria-labelledby="addAlarmTitle">
		<div class="modal-dialog" style="width:500px" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<div id="dvPanelParent" style="border-radius: 5px;"
		class="panel panel-default planroute hidden">
		<div class="panel-heading">Distance DetailInfo</div>
		<div id="dvPanel" class="panel-body dvPaneloverflow"></div>
	</div>
	
	<!-- 轨迹回放详细信息 -->
	<div class="alert_box trackInfoStyle">
		<div class="alert_box_content">
			<table class="table table-condensed table-striped table-hover hidden"
				id="trackingTable">
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
	</div>
	<!-- 供后续图例位置改变使用 -->
<!-- 	<div style="position:relative;float: right; top:100px;right:20px;"> -->
<!-- 	   <input type="text" autocomplete="off" id = "placeSearch" class="search-input" -->
<!-- 							placeholder="Place to search" onkeyup="treeFilter('tree','userName',this.value)"/> -->
<!-- 	</div> -->
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
	function saveVehicleTrack(){
			var portUrl = getRootPath() + "monitorroutearea/saveRaPointByVehicle.action?tripId="+tripIdByVehicle+"&routeAreaId="+routeAreaIdByVehicle;
			 $.ajax({
					type : "POST",
					url : portUrl,
					dataType : "json",
					cache : false,
					async : false,
					error : function(e, message, response) {
						console.log("Status: " + e.status + " message: " + message);
					},
					success : function(result) {
						bootbox.alert($.i18n.prop("map.routeArea.save.success"));
					  }	
					});
	}
	
	function print(link)
	{
		html2canvas( $('#map_canvas') , 
		{
	  		onrendered: function(canvas) 
	  		{
	    		//document.body.appendChild(canvas);
	    		$('#imageCut').attr( 'href' , canvas.toDataURL() ) ;
	    		$('#share_button').css('display','inline-block');
	    		$('#imageCut').on( 'click' , function()
	    		{
	    			
	    		} );
	    		var b64 = canvas.toDataURL() .substring(22);
// 	    		$.post(getRootPath() + 'monitorroutearea/imageToPDF.action',{'base64sString':b64},function(){
	    			
// 	    		});
       $("input[id='base64sString']").val(b64);
         $('#imagetopdf').ajaxSubmit({
		 type: 'post',
		 success : function(data) {
			if(!needLogin(data)) {
				//alert(data);
			}
		}
	   });
	  		}
	  	});
	}
	$(function() {
		$("#imageCut").on('click',function(){
			print($('#imageCut'));
		});
	    
		var $showAll = $(".trackSeleCtr ul li input[id='showAllPts']");
		var $selectPts = $(".trackSeleCtr ul li input[id='seleVehiclPts']");
		var $selectNum = $(".trackSeleCtr ul li input[id='selectNum']");
		$showAll.click(function(){
			$selectPts.prop("checked", false);
			//nucVehicleStatusData = obj;
			if(typeof(nucVehicleStatusData)!="undefined"){
				getVehiclePaths(nucVehicleStatusData,ALL_POINT);
			}
		})
		$selectPts.click(function(){
			$showAll.prop("checked", false);
			if(typeof(nucVehicleStatusData)!="undefined"){
				
				selectNumber = Number($selectNum.val());
				getVehiclePaths(nucVehicleStatusData,SELECT_POINT);
			}
		})
		
	});
	
	function searchToggle(obj, evt){
		var container = $(obj).closest('.search-wrapper');

		if(!container.hasClass('active')){
			  container.addClass('active');
			  evt.preventDefault();
		}
		else if(container.hasClass('active') && $(obj).closest('.input-holder').length > 0){
			
			  container.removeClass('active');
			  container.find('.result-container').fadeOut(100, function(){$(this).empty();});
			  var searchText = $("#search-input").val();
			  searchVehicle(searchText);
		}
	}
	function searchVehicle(searchText){
		var pointArray = [];
		var flag = false;
		for ( var index in vehicleMarkers) {
			var vehicleMarker = vehicleMarkers[index];
			var vehicleAttr = vehicleMarker.attribution.source;
			var objAttr = JSON.parse(vehicleAttr);//.parseJSON(); 
			if(objAttr.vehiclePlateNumber==searchText){
				//var localPoint = {lng:vehicle.longitude,lat:vehicle.latitude};
				 pointArray.push({
						lat : objAttr.latitude,
						lng : objAttr.longitude
					});
				var html = creatRealVehcleContent(objAttr);
				GisShowInfoWindow(vehicleMarker,html,true);
				GisSetViewPortByArray(pointArray);
				flag = true;
			}
			
		}
	    if(flag){
	    	
	    }else{
	    	bootbox.alert($.i18n.prop("vehicle.search.hasNoFind"));
	    }   
     }
</script>

<!--弹出报警信息  -->
 <script type="text/javascript">
	var flag =true;
	function showAlarm(){
		$('#a').toggle();
		//$('#dropdownAlarm').dropdown('toggle');
	}
</script> 
<script type="text/javascript" src="${root}/static/js/initMap.js"></script>
</body>
</html>