<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/include.jsp" />
<title><fmt:message key="trip.activate.title"/></title>
<link rel="stylesheet" href="${root }/static/css/trip.css" />
<style>
.esealNumber-list ul li, .sensorNumber-list ul li {
	float: left;
	border: #c3ced5 solid 1px;
}
.button-margin button{
	margin-bottom: 5px;
}
#barcode {
	position: relative;
}
#barcode > canvas, #barcode > video {
	/* max-width: 50%;
	width: 50%; */
}
canvas.drawing, canvas.drawingBuffer {
	position: absolute;
	/* left: 0; */
	left: 25%;
	top: 0;
}
.wrapper-content #collapseVehicle .panel-default > .panel-body {
	height: auto;
	width: auto;
	overflow: hidden;
}
.wrapper-content #collapseVehicle .panel-default > .panel-body .help-block{
	text-align: left;
}
li a.vehicle-remove {
	position: relative!important;
    float: right;
    display: inline!important;
    z-index: 1;
    width: 14px;
    height: 14px;
    padding: 0px!important;
	right: 4px;
    top: 1px;
    margin: 0px;
    border: 0px!important;
    cursor: pointer!important;
}
.activateDiv {
	position: relative; 
	top: 15px;
}
.activateDiv .panel-body{
	padding: 0px;
}
.activateDiv .panel-body .btnSubmit{
	margin: 0px -15px;
}
.carousel-caption {
	overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
.panel-title a{
	font-size: 22px;
	font-weight:bold;
}
ul.nav {
	margin: 0 10px 0px 15px;
}
</style>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../include/tripMsgModal.jsp" %>
<%@ include file="../include/left.jsp" %>
<div class="row site">
	<div class="wrapper-content margint95 margin60">
		<%--导航 --%>
		<c:set var="pageName"><fmt:message key="trip.activate.title"/></c:set>
		<jsp:include page="../include/navigation.jsp" >
			<jsp:param value="${pageName }" name="pageName"/>
		</jsp:include>
		
		<!-- Modal 关锁设备添加模态框-->
		<div class="modal  add_user_box" id="addElockModal" tabindex="-1"
			role="dialog" aria-labelledby="addElockModalTitle">
			<div class="modal-dialog" role="document">
				<div class="modal-content"></div>
			</div>
		</div>
		<!-- /Modal -->
		
		<!-- Modal 子锁添加模态框-->
		<div class="modal  add_user_box" id="addEsealModal" tabindex="-1"
			role="dialog" aria-labelledby="addEsealModalTitle">
			<div class="modal-dialog" role="document">
				<div class="modal-content"></div>
			</div>
		</div>
		<!-- /Modal -->
		
		<!-- Modal 传感器添加模态框-->
		<div class="modal  add_user_box" id="addSensorModal" tabindex="-1"
			role="dialog" aria-labelledby="addSensorModalTitle">
			<div class="modal-dialog" role="document">
				<div class="modal-content"></div>
			</div>
		</div>
		<!-- /Modal -->
		
		<!-- 等待模态框（Modal） -->
		<div class="modal fade" id="msgModal" tabindex="-1" role="dialog" 
		   aria-labelledby="msgModalLabel">
		   <div class="modal-dialog">
		      <div class="modal-content">
		         <div class="modal-header">
		            <h4 class="modal-title" id="msgModalLabel">
		              	 <fmt:message key="trip.info.message"/>
		            </h4>
		         </div>
		         <div class="modal-body">
		            	<img style="height: 35px; margin-right: 8px;" src="${root }/static/images/loading.gif"/>
		            	<fmt:message key="trip.info.wait.approval"/>
		         </div>
		      </div><!-- /.modal-content -->
			</div>
		</div>
		<!-- /.modal -->
		
		<!-- Modal 扫描条码模态框-->
		<div class="modal  add_user_box" id="scanModal" tabindex="-1"
			role="dialog" aria-labelledby="scanModalTitle">
			<div class="modal-dialog" role="document">
				<div class="modal-content" style="top: -50px;"></div>
			</div>
		</div>
		<!-- /Modal -->
		
		<!-- Modal 拍照模态框-->
		<div class="modal  add_user_box" id="cameraModal" tabindex="-1"
			role="dialog" aria-labelledby="cameraModalTitle">
			<div class="modal-dialog" role="document">
				<div class="modal-content" style="top: -50px;"></div>
			</div>
		</div>
		<!-- /Modal -->
		<div class="profile profile_box02">
		<div class="tab-content m-b">
			<form class="form-horizontal" role="form" id="tripForm" action="${root }/monitortrip/activate.action" method="post" enctype="multipart/form-data">
				<input type="hidden" name="fileIndexVehicleNumMap" id="fileIndexVehicleNumMap" value="">
				<input type="hidden" name="photoIndexVehicleNumMap" id="photoIndexVehicleNumMap" value="">
				<!-- elock -->
				<div class="panel panel-info" style="margin-bottom: 0px;">
			         <h4 class="tab-cotent-title">
			               	<fmt:message key="trip.activate.title.elockInfo"/>
			         </h4>
			         <div class="search_form">
						<div class="form-group col-sm-12">
							<!-- 报关单号 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.declarationNumber">
								<em>*</em><fmt:message key="trip.label.declarationNumber"/>
							</label>
							<div class="col-sm-3">
								<input class="form-control" name="tripVehicleVO.declarationNumber" 
									id="tripVehicleVO.declarationNumber" type="text" value="">
							</div>
							<%-- <div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnGetDecNum"><fmt:message key="trip.activate.button.load"/></button></div> --%>
							
							<!-- 检入地点 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.checkinPort">
						         <fmt:message key="trip.label.checkinPort"/>
						      </label>
							<div class="col-sm-3">
								<input class="form-control" readonly="readonly" name="tripVehicleVO.checkinPort" 
									id="tripVehicleVO.checkinPort" type="hidden" value="${checkinPort }">
								<input class="form-control" readonly="readonly" name="tripVehicleVO.checkinPortName" 
									id="tripVehicleVO.checkinPortName" type="text" value="${checkinPortName }">
							</div>
						
							<!--  检出地点 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.checkoutPort">
						        <em>*</em><fmt:message key="trip.label.checkoutPort"/>
						      </label>
							<div class="col-sm-3">
								<select class="form-control" name="tripVehicleVO.checkoutPort" id="tripVehicleVO.checkoutPort">
									<option value=""></option>
									<c:forEach var="checkoutPort" items="${checkoutPortList }" varStatus="status">
									<option value="${checkoutPort.organizationId }">${checkoutPort.organizationName }</option>
									</c:forEach>
						    	</select>
							</div>
							<div class="clearfix"></div>
						</div>
						<div class="form-group">
							
							<!-- 路线 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.routeId">
						         <em>*</em><fmt:message key="trip.label.routeId"/>
						      </label>
							<div class="col-sm-3">
								<select class="form-control" name="tripVehicleVO.routeId" id="tripVehicleVO.routeId">
						    	</select>
							</div>
							
							<!-- 耗时 -->
							<label class="col-sm-2 control-label">
						         <fmt:message key="trip.label.timeCost"/>
						      </label>
							<div class="col-sm-3 control-label" id="timeCost" name="timeCost" style="text-align: left;">
							</div>
							
							<!-- 巡逻队 -->
							<c:if test="${systemModules.isPatrolOn() }">
								<label class="col-sm-1 control-label" for="patrolId">
							         <fmt:message key="foot.title.patrol"/>
							      </label>
								<div class="col-sm-3">
									<select id="patrolId" name="patrolId" class="form-control" multiple="multiple">
										<option value=""></option>
										<c:forEach var="patrol" items="${patrolList}">
											<option value="${patrol.patrolId }">${patrol.potralUserName}/${patrol.vehiclePlateNumber}</option>
										</c:forEach>
									</select>
								</div>
							</c:if>
							
							<!-- target zoon -->
							<c:if test="${systemModules.isAreaOn() }">
								<label class="col-sm-1 control-label" for="tripVehicleVO.targetZoonId">
							        <fmt:message key="main.list.routeArea.routeAreaType.option.targetZoon"/>
							      </label>
								<div class="col-sm-3">
									<select class="form-control" name="tripVehicleVO.targetZoonId" id="tripVehicleVO.targetZoonId" multiple="multiple">
										<option value=""></option>
										<c:forEach var="targetZoon" items="${targetZoonList}">
											<option value="${targetZoon.routeAreaId }">${targetZoon.routeAreaName}</option>
										</c:forEach>
							    	</select>
								</div>
							</c:if>
						</div>
					</div>
						
				</div>
				
				<div class="panel panel-info" style="margin-bottom: 0px;margin-right: -10px;">
			         <h4 class="tab-cotent-title">
			               	<fmt:message key="trip.report.vehicleInfo"/>
			         </h4>
			      <div id="collapseVehicle" class="panel-collapse collapse in">
			      	<ul id="myTab" class="nav nav-tabs">
						<li class="active"><a href="#newVehicleTab" data-toggle="tab" id="btnNewVehicle">&nbsp;<span class="glyphicon glyphicon-plus"></span>&nbsp;</a></li>
					</ul>
					<div id="myTabContent" class="tab-content">
						<div class="tab-pane fade in active" id="newVehicleTab">
							<div class="panel-body" name="vehicle-panel">
								<div class="form-group">
									<label class="col-sm-1 control-label">
										<em>*</em><fmt:message key="trip.label.vehiclePlateNumber"/>
									</label>
									<div class="col-sm-3">
										<input class="form-control" name="tripVehicleVO_vehiclePlateNumber" 
											id="tripVehicleVO_vehiclePlateNumber" type="text" value="">
									</div>
									
									<label class="col-sm-1 control-label">
										<em>*</em><fmt:message key="trip.label.vehicleCountry"/>
									</label>
									<div class="col-sm-3">
										<input class="form-control" name="tripVehicleVO_vehicleCountry" 
											id="tripVehicleVO_vehicleCountry" type="text" value="">
									</div>
									
									<label class="col-sm-1 control-label">
										<!-- <em>*</em> -->
										<fmt:message key="trip.label.trailerNumber"/>
									</label>
									<div class="col-sm-3">
										<input class="form-control" name="tripVehicleVO_trailerNumber" 
											id="tripVehicleVO_trailerNumber" type="text" value="">
									</div>
									<div class="clearfix"></div>
								</div>
								<div class="form-group">
									<label class="col-sm-1 control-label">
										<em>*</em><fmt:message key="trip.label.driverName"/>
									</label>
									<div class="col-sm-3">
										<input class="form-control" name="tripVehicleVO_driverName" 
											id="tripVehicleVO_driverName" type="text" value="">
									</div>
									
									<label class="col-sm-1 control-label">
										<!-- <em>*</em> -->
										<fmt:message key="trip.label.driverCountry"/>
									</label>
									<div class="col-sm-3">
										<input class="form-control" name="tripVehicleVO_driverCountry" 
											id="tripVehicleVO_driverCountry" type="text" value="">
									</div>
									
									<label class="col-sm-1 control-label">
										<!-- <em>*</em> -->
										<fmt:message key="trip.label.driverIdCard"/>
									</label>
									<div class="col-sm-3">
										<input class="form-control" name="tripVehicleVO_driverIdCard" 
											id="tripVehicleVO_driverIdCard" type="text" value="">
									</div>
									<div class="clearfix"></div>
								</div>
								<div class="form-group">
									<label class="col-sm-1 control-label">
										<em>*</em><fmt:message key="trip.label.containerNumber"/>
									</label>
									<div class="col-sm-3">
										<input class="form-control" name="tripVehicleVO_containerNumber" 
											id="tripVehicleVO_containerNumber" type="text" value="">
									</div>
									
									<c:if test="${systemModules.isRiskOn() }">
										<label class="col-sm-1 control-label">
										 <em>*</em><fmt:message key="trip.label.riskStatus"/>
										</label>
										<div class="col-sm-3" style="height: 30px!important; padding: 0px;">
											<label class="riskLabel">
												<input type="radio" name="tripVehicleVO_riskStatus" id="riskStatus0" value="0" checked="checked"/>
												<span style="background-color: #31d231;"><fmt:message key="trip.label.riskStatus.low"/></span>
											</label>
											<label class="riskLabel">
												<input type="radio" name="tripVehicleVO_riskStatus" id="riskStatus1" value="1"/>
												<span style="background-color: #ffff00;"><fmt:message key="trip.label.riskStatus.middle"/></span>
											</label>
											<label class="riskLabel">
												<input type="radio" name="tripVehicleVO_riskStatus" id="riskStatus2" value="2"/>
												<span style="background-color: #ff0000;"><fmt:message key="trip.label.riskStatus.high"/></span>
											</label>
										</div>
									</c:if>
									
									<label class="col-sm-1 control-label">
									 <!-- <em>*</em> -->
									 <fmt:message key="trip.label.goodsType"/>
									</label>
									<div class="col-sm-3">
										<s:select name="tripVehicleVO_goodsType"
											emptyOption="true"
											cssClass="form-control"
											theme="simple"
											list="@com.nuctech.ls.model.util.GoodsType@values()"
											listKey="type"
											listValue="key"
											multiple="true"
											>
										</s:select>
									</div>
									<div class="clearfix"></div>
								</div>
								<div class="form-group">
									<!-- 追踪终端号，多个 -->
									<label class="col-sm-1 control-label">
										<em>*</em><fmt:message key="trip.label.trackingDeviceNumber"/>
									</label>
									<div class="col-sm-3">
										<input class="form-control" name="tripVehicleVO_trackingDeviceNumber" 
											id="tripVehicleVO_trackingDeviceNumber" type="text" value="">
									</div>
									<%--
									<div class="btn-group col-sm-1">
										<button type="button" class="btn btn-danger" id="btnAddDeviceNum"><fmt:message key="trip.activate.button.new"/></button>
									</div>
									<div class="col-sm-6 deviceNumber-list">
										<ul></ul>
									</div>
									 --%>
									<%--
									<div class="col-sm-4 button-margin">
										<button type="button" class="btn btn-danger" id="btnGetDeviceNum"><fmt:message key="trip.activate.button.load"/></button>
										<button type="button" class="btn btn-danger" id="btnSetLocked" disabled="disabled"><fmt:message key="trip.button.setLocked"/></button>
										<button type="button" class="btn btn-danger" id="btnSetUnlocked" disabled="disabled"><fmt:message key="trip.button.setUnlocked"/></button>
										<button type="button" class="btn btn-danger" id="btnClearAlarm" disabled="disabled"><fmt:message key="trip.button.clearAlarm"/></button>
									</div>
									 --%>
									
									<!-- 电量 -->
									<div class="col-sm-4">
									<table class="table">
									      <tr>
										  	  <th style="text-align: left;"><fmt:message key="trip.label.location"/></th>
										      <th style="text-align: left;"><fmt:message key="trip.label.communication"/></th>
										      <c:if test="${systemModules.isAreaOn() }">
											      <th style="text-align: left;"><fmt:message key="trip.info.inArea"/></th>
										      </c:if>
										      <th style="text-align: left;"><fmt:message key="trip.info.dump.energy"/></th>
									      </tr>
									      <tr>
										<td style="text-align: left;"><span name="elockLocation" class="glyphicon" style="padding-left: 15px;"></span></td>
										<td style="text-align: left;"><span name="elockCommuicate" class="glyphicon" style="padding-left: 15px;"></span></td>
										<c:if test="${systemModules.isAreaOn() }">
											<td style="text-align: left;"><span name="elockInArea" class="glyphicon" style="padding-left: 15px;"></span></td>
										</c:if>
										<td style="text-align: left;">
											<div class="progress">
												 <div class="progress-bar progress-bar-success" name="dianliang" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: 0%"></div> 
												  <div class="bar-head"></div>
												  <div class="percentage">0%</div>
												</div>
										</td>
									      </tr>
										</table>
									 </div>
									 <div class="clearfix"></div>
								</div>
								<div class="form-group"> 
									<!-- 子锁号，多个 -->
									<label class="col-sm-1 control-label">
									 <fmt:message key="trip.label.esealNumber"/>
									</label>
									<div class="col-sm-3">
										<input class="form-control" name="esealNumberInput" id="esealNumberInput" type="text" value="">
										<input class="form-control" type="hidden" name="tripVehicleVO_esealNumber" 
											id="tripVehicleVO_esealNumber" value="">
									</div>
									<div class="btn-group col-sm-1">
										<button type="button" class="btn btn-danger" id="btnAddEsealNum"><fmt:message key="trip.activate.button.new"/></button>
										<%--
										<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"><fmt:message key="trip.activate.button.new"/>
									      <span class="caret"></span>
									    </button>
										<ul class="dropdown-menu" id="esealMenu">
											<li><a href="javascript:;" id="btnGetEsealNum"><fmt:message key="trip.activate.button.load"/></a></li>
											<li><a href="javascript:;" id="btnAddEsealNum"><fmt:message key="trip.activate.button.add"/></a></li>
										</ul>
										 --%>
									</div>
									<div class="col-sm-6 esealNumber-list">
										<ul></ul>
									</div>
									<div class="clearfix"></div>
								</div>
								
								<div class="form-group">
									<!-- 传感器编号，多个 -->
									<label class="col-sm-1 control-label">
										<fmt:message key="trip.label.sensorNumber"/>
									</label>
									<div class="col-sm-3">
										<input class="form-control" name="sensorNumberInput" id="sensorNumberInput" type="text" value="">
										<input class="form-control" type="hidden" name="tripVehicleVO_sensorNumber" 
											id="tripVehicleVO_sensorNumber" value="">
									</div>
									<div class="btn-group col-sm-1">
										<button type="button" class="btn btn-danger" id="btnAddSensorNum"><fmt:message key="trip.activate.button.new"/></button>
										<%--
										<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"><fmt:message key="trip.activate.button.new"/>
									      <span class="caret"></span>
									    </button>
										<ul class="dropdown-menu" id="sensorMenu">
											<li><a href="javascript:;" id="btnGetSensorNum"><fmt:message key="trip.activate.button.load"/></a></li>
											<li><a href="javascript:;" id="btnAddSensorNum"><fmt:message key="trip.activate.button.add"/></a></li>
										</ul>
										 --%>
									</div>
									<div class="col-sm-6 sensorNumber-list">
										<ul></ul>
									</div>
								</div>
								
								<!-- image -->
								<div class="form-group vehicle-img">
									<label class="col-sm-1 control-label">
										<fmt:message key="trip.label.checkinPicture"/>
									</label>
									<div class="col-sm-3">
										<div id="myCarousel" class="carousel slide">
											<!-- 轮播（Carousel）指标 -->
											<ol class="carousel-indicators">
											</ol>   
											<!-- 轮播（Carousel）项目 -->
											<div class="carousel-inner">
											</div>
											<!-- 轮播（Carousel）导航 -->
											<a class="carousel-control left" href="#myCarousel" 
											   data-slide="prev">&lsaquo;</a>
											<a class="carousel-control right" href="#myCarousel" 
											   data-slide="next">&rsaquo;</a>
										</div> 
									</div>
									<!-- <div class="btn-group col-sm-1" id="photoMenu"> -->
									<div class="col-sm-1" id="photoMenu">
										<button type="button" class="btn btn-danger" id="btnLocal"><fmt:message key="trip.activate.button.uploadImage"/></button>
										<input type="file" name="tripPhotoLocal" data-index='0' style="display: none;" accept=".jpeg,.jpg,.bmp"/>
										<%--
										<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"><fmt:message key="trip.activate.button.uploadImage"/>
									      <span class="caret"></span>
									    </button>
										<ul class="dropdown-menu" id="photoMenu">
											<li>
												<a href="javascript:;" id="btnLocal"><fmt:message key="trip.activate.button.fromLocal"/></a>
												<input type="file" name="tripPhotoLocal" data-index='0' style="display: none;" accept=".jpeg,.jpg,.bmp"/>
											</li>
											<li><a href="javascript:;" id="btnCamera"><fmt:message key="trip.activate.button.fromCamera"/></a></li>
										</ul>
									</div>
									 --%>
									<div class="col-sm-12 has-error file-help-block" style="display: none;">
						        		<small class="help-block" style=""><fmt:message key="trip.activate.info.image.required"/></small>
						        	</div>
								</div>
								
								<div class="form-group col-sm-12">
									<button type="button" class="btn btn-danger" id="btnSaveVehicle"><fmt:message key="trip.activate.button.save"/></button>
									<button type="button" class="btn btn-danger" id="btnCancelVehicle"><fmt:message key="trip.activate.button.cancel"/></button>
								</div>
							</div>
						</div>
					</div>
			      	<div class="col-sm-12" id="vehicleDiv">
					</div>
				</div>
				</div>
				
				<!-- button -->
				<div class="panel panel-info activateDiv" style="background: none;">
					<div class="panel-collapse collapse in">
			        	<div class="panel-body">
							<div class="form-group col-sm-12 text-center btnSubmit">
								<button type="submit" class="btn btn-danger" id="btnActivate"><fmt:message key="trip.activate.button.activate"/></button>
							</div>
			        	</div>
			        </div>
				</div>
					
			</form>
		</div>
		</div>
	</div>
</div>
<script type="text/javascript" src="${root }/static/js/gumwrapper.js"></script>
<script type="text/javascript" src="${root }/static/js/quagga.min.js"></script>
<script type="text/javascript" src="${root }/static/js/ejs.min.js"></script>
<script type="text/javascript">
initJqueryI18n();
/**
 * 初始化Jquery i18n
 */
function initJqueryI18n(){
	jQuery.i18n.properties({//加载资浏览器语言对应的资源文件
	    name : 'LocalizationResource_center', //资源文件名称
	    path : _getRootPath() + "/i18n/", //资源文件路径
	    mode : 'map', //用Map的方式使用资源文件中的值
	    language :language,
	    callback : function() {
//	    	alert($.i18n.prop("common.message.form.validator"))
	    }
	});
}
$(function(){
	//initWebSocket("${clientIPAddress}");
});
</script>
<script src="${root}/trip/js/elockUtil.js"></script>
<script src="${root}/trip/js/barcodeLive.js"></script>
<script src="${root}/trip/js/activate.js"></script>

<%--ejs模板 --%>
<script id="vehicleInfo" type="text/template">
<div class="panel-body">
	<button type="button" class="btn btn-danger" name="btnEditVehicle"><fmt:message key="trip.activate.button.edit"/></button>
	<input type="hidden" name="vehiclePlateNumber" value="{{= vehiclePlateNumber }}">
	<input type="hidden" name="trackingDeviceNumber" value="{{= trackingDeviceNumber }}">
	<input type="hidden" name="esealNumber" value="{{= esealNumber }}">
	<input type="hidden" name="esealOrder" value="">
	<input type="hidden" name="sensorNumber" value="{{= sensorNumber }}">
	<input type="hidden" name="sensorOrder" value="">
	<input type="hidden" name="trailerNumber" value="{{= trailerNumber }}">
	<input type="hidden" name="vehicleCountry" value="{{= vehicleCountry }}">
	<input type="hidden" name="driverName" value="{{= driverName }}">
	<input type="hidden" name="driverCountry" value="{{= driverCountry }}">
	<input type="hidden" name="driverIdCard" value="{{= driverIdCard }}">
	<input type="hidden" name="containerNumber" value="{{= containerNumber }}">
	<input type="hidden" name="goodsType" value="{{= goodsType }}">
	<input type="hidden" name="uploadFilesCount" value="{{= uploadFilesCount }}">
	<div class="form-group">
		<label class="col-sm-1 control-label">
			<em>*</em><fmt:message key="trip.label.vehiclePlateNumber"/>
		</label>
		<div class="col-sm-3">
			<p class="form-control-static" name="vehiclePlateNumber">{{= vehiclePlateNumber }}</p>
		</div>
		
		<label class="col-sm-1 control-label">
			<em>*</em><fmt:message key="trip.label.vehicleCountry"/>
		</label>
		<div class="col-sm-3">
			<p class="form-control-static" name="vehicleCountry">{{= vehicleCountry }}</p>
		</div>
		
		<label class="col-sm-1 control-label">
			<em>*</em><fmt:message key="trip.label.trailerNumber"/>
		</label>
		<div class="col-sm-3">
			<p class="form-control-static" name="trailerNumber">{{= trailerNumber }}</p>
		</div>
		<div class="clearfix"></div>
	</div>
	<div class="form-group">
		<label class="col-sm-1 control-label">
			<em>*</em><fmt:message key="trip.label.driverName"/>
		</label>
		<div class="col-sm-3">
			<p class="form-control-static" name="driverName">{{= driverName }}</p>
		</div>
		
		<label class="col-sm-1 control-label">
			<em>*</em><fmt:message key="trip.label.driverCountry"/>
		</label>
		<div class="col-sm-3">
			<p class="form-control-static" name="driverCountry">{{= driverCountry }}</p>
		</div>
		
		<label class="col-sm-1 control-label">
			<em>*</em><fmt:message key="trip.label.driverIdCard"/>
		</label>
		<div class="col-sm-3">
			<p class="form-control-static" name="driverIdCard">{{= driverIdCard }}</p>
		</div>
		<div class="clearfix"></div>
	</div>
	<div class="form-group">
		<label class="col-sm-1 control-label">
			<em>*</em><fmt:message key="trip.label.containerNumber"/>
		</label>
		<div class="col-sm-3">
			<p class="form-control-static" name="containerNumber">{{= containerNumber }}</p>
		</div>
		
		<c:if test="${systemModules.isRiskOn() }">
		<label class="col-sm-1 control-label">
			<em>*</em><fmt:message key="trip.label.riskStatus"/>
		</label>
		<div class="col-sm-3">
			<p class="form-control-static" name="riskStatus">{{= riskStatusName }}</p>
			<input type="hidden" name="riskStatus" value="{{= riskStatus}}"/>
		</div>
		</c:if>
		
		<label class="col-sm-1 control-label">
			<em>*</em><fmt:message key="trip.label.goodsType"/>
		</label>
		<div class="col-sm-3">
			<p class="form-control-static" name="goodsTypeName">{{= goodsTypeName }}</p>
		</div>
		<div class="clearfix"></div>
	</div>
	<div class="form-group">
	<label class="col-sm-1 control-label">
		<em>*</em><fmt:message key="trip.label.trackingDeviceNumber"/>
	</label>
	<div class="col-sm-3">
		<p class="form-control-static" name="trackingDeviceNumber">{{= trackingDeviceNumber }}</p>
	</div>
	
	<div class="col-sm-4">
	<table class="table">
	      <tr>
		  	<th style="text-align: left;"><fmt:message key="trip.label.location"/></th>
		    <th style="text-align: left;"><fmt:message key="trip.label.communication"/></th>
			<c:if test="${systemModules.isAreaOn() }">
		    <th style="text-align: left;"><fmt:message key="trip.info.inArea"/></th>
			</c:if>
		    <th style="text-align: left;"><fmt:message key="trip.info.dump.energy"/></th>
	      </tr>
	      <tr>
		<td style="text-align: left;"><span name="elockLocation" class="glyphicon" style="padding-left: 15px;"></span></td>
		<td style="text-align: left;"><span name="elockCommuicate" class="glyphicon" style="padding-left: 15px;"></span></td>
		<c:if test="${systemModules.isAreaOn() }">
			<td style="text-align: left;"><span name="elockInArea" class="glyphicon" style="padding-left: 15px;"></span></td>
		</c:if>
		<td style="text-align: left;">
			<div class="progress">
				 <div class="progress-bar progress-bar-success" name="dianliang" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: 0%"></div> 
				  <div class="bar-head"></div>
				  <div class="percentage">0%</div>
				</div>
		</td>
	      </tr>
		</table>
	 </div>
	 <div class="clearfix"></div>
	</div>
	<div class="form-group"> 
		<label class="col-sm-1 control-label">
			<fmt:message key="trip.label.esealNumber"/>
		</label>
		<div class="col-sm-3">
			<p class="form-control-static" name="esealNumber">{{= esealNumber }}</p>
		</div>
	<div class="clearfix"></div>
	</div>
	
	<div class="form-group">
		<label class="col-sm-1 control-label">
			<fmt:message key="trip.label.sensorNumber"/>
		</label>
		<div class="col-sm-3">
			<p class="form-control-static" name="sensorNumber">{{= sensorNumber }}</p>
		</div>
	</div>

	<div class="form-group vehicle-img">
		<label class="col-sm-1 control-label">
			<fmt:message key="trip.label.checkinPicture"/>
		</label>
		<div class="col-sm-3">
			<div id="{{=carousel.slice(1)}}" class="carousel slide">
				<ol class="carousel-indicators">
				</ol>   
				<div class="carousel-inner">
				</div>
				<a class="carousel-control left" href="{{=carousel}}" 
				   data-slide="prev">&lsaquo;</a>
				<a class="carousel-control right" href="{{=carousel}}" 
				   data-slide="next">&rsaquo;</a>
			</div> 
		</div>
	</div>
	</div>
</div>
</script>
<!-- 
{{ if (files.length) { }}
					{{ 	files.forEach(function(file, index){ }}
						<li data-target="{{=carousel}}" data-slide-to="{{=index}}"></li>
					{{ }) }}
				{{ } }}
 -->
<!-- 
{{ if (files.length) { }}
					{{ 	files.forEach(function(file, index){ }}
						<div class="item">
							<img src="" alt="Image">
						</div>
					{{ }) }}
				{{ } }}
 -->
<script id="tabLi" type="text/template">
<li>
	<a href="javascript:;" class="vehicle-remove" title="<fmt:message key="trip.activate.button.delete"/>">
		<span class="glyphicon glyphicon-remove" style="top: 0px;"></span>
	</a>
	<a href="{{= tabHref}}" data-toggle="tab" aria-expanded="false">{{= tabName}}</a>
</li>
</script>
<script id="imageItem" type="text/template">
<div class="item active">
	<img src="{{= src}}" alt="Image">
	<div class="carousel-caption">
		<a href="javascript:void(0);" class="delete_image" name="{{= name}}" data-index="{{=index}}">
			<span class="glyphicon glyphicon-remove"></span>
		</a>
	</div>
</div>
</script>
</body>
</html>