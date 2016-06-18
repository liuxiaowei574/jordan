<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title><fmt:message key="trip.edit.title"/></title>
		<link rel="stylesheet" href="${root }/static/css/bootstrap.css" />
		<link rel="stylesheet" href="${root }/static/js/bootstrap/validator0.5.2/css/bootstrapValidator.min.css" />
		<link rel="stylesheet" href="${root }/static/css/bootbox.css">
		<link rel="stylesheet" href="${root }/static/css/trip.css" />
	</head>

	<body style="width: 98%;">
	<form class="form-horizontal" role="form" id="tripForm" action="${root }/monitortrip/activate.action" method="post" enctype="multipart/form-data">
		<input type="hidden" id="tripVehicleVO.tripId" name="tripVehicleVO.tripId" value="${tripVehicleVO.tripId }">
		<!-- button -->
		<div class="btn-group" style="padding: 10px;">
			<div class="btn-group">
				<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"><fmt:message key="trip.activate.button.uploadImage"/>
			      <span class="caret"></span>
			    </button>
				<ul class="dropdown-menu" id="photoMenu">
					<li>
						<a href="#" id="btnLocal"><fmt:message key="trip.activate.button.fromLocal"/></a>
						<input type="file" name="tripPhotoLocal" data-index='0' style="display: none;" accept=".jpeg,.jpg,.bmp"/>
					</li>
					<li><a href="#" id="btnCamera"><fmt:message key="trip.activate.button.fromCamera"/></a></li>
				</ul>
			</div>
			
		</div>
		<button type="submit" class="btn btn-danger" id="btnActivate"><fmt:message key="trip.activate.button.activate"/></button>
		
			<!-- elock -->
			<div class="panel panel-info">
		      <div class="panel-heading">
		         <h4 class="panel-title">
		            <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
		               	<fmt:message key="trip.activate.title.elockInfo"/>
		            </a>
		         </h4>
		      </div>
		      <div id="collapseOne" class="panel-collapse collapse in">
		         <div class="panel-body">
					<div class="form-group">
						<!-- 追踪终端号 -->
						<label class="col-sm-2 control-label" for="tripVehicleVO.trackingDeviceNumber">
							<fmt:message key="trip.label.trackingDeviceNumber"/>
						</label>
						<div class="col-sm-2">
							<input class="form-control" name="tripVehicleVO.trackingDeviceNumber" 
								id="tripVehicleVO.trackingDeviceNumber" type="text" value="${tripVehicleVO.trackingDeviceNumber }">
						</div>
						<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnGetDeviceNum"><fmt:message key="trip.activate.button.load"/></button></div>
						
						<!-- 电量 -->
						<label class="col-sm-2 control-label">
							<fmt:message key="trip.label.electricity"/>
					      </label>
					      <div class="col-sm-2">
					      	  <p><fmt:message key="trip.label.location"/>
						          <span class="glyphicon glyphicon-ok" style="color: #428bca; padding-left: 15px;"></span>
						      </p>
						      <p><fmt:message key="trip.label.communication"/>
						          <span class="glyphicon glyphicon-remove" style="color: red; padding-left: 15px;"></span>
						      </p>
						      <p><fmt:message key="trip.label.location"/>
						          <span class="glyphicon glyphicon-ok" style="color: #428bca; padding-left: 15px;"></span>
						      </p>
					      </div>
						<div class="col-sm-1">
							<div class="progress">
							 <div class="progress-bar progress-bar-success" id="dianliang" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: 80%"></div> 
							  <!-- <div class="progress-bar progress-bar-danger" id="dianliang" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: 20%"></div> -->
							  <div class="bar-head"></div>
							  <div class="percentage">80%</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<!-- 子锁号 -->
						<label class="col-sm-2 control-label" for="tripVehicleVO.esealNumber">
					         <fmt:message key="trip.label.esealNumber"/>
					      </label>
						<div class="col-sm-2">
							<input class="form-control" name="esealNumberInput" id="esealNumberInput" type="text" value="${tripVehicleVO.esealNumber }">
							<input class="form-control" type="hidden" name="tripVehicleVO.esealNumber" 
								id="tripVehicleVO.esealNumber" type="text" value="${tripVehicleVO.esealNumber }">
						</div>
						<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnAddEsealNum"><fmt:message key="trip.activate.button.add"/></button></div>
						<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnGetEsealNum"><fmt:message key="trip.activate.button.load"/></button></div>
						<div class="col-sm-6 esealNumber-list">
							<ul></ul>
						</div>
					</div>
					<div class="form-group">
						<!-- 传感器编号 -->
						<label class="col-sm-2 control-label" for="tripVehicleVO.sensorNumber">
					         <fmt:message key="trip.label.sensorNumber"/>
					      </label>
						<div class="col-sm-2">
							<input class="form-control" name="sensorNumberInput" id="sensorNumberInput" type="text" value="${tripVehicleVO.sensorNumber }">
							<input class="form-control" type="hidden" name="tripVehicleVO.sensorNumber" 
								id="tripVehicleVO.sensorNumber" type="text" value="${tripVehicleVO.sensorNumber }">
						</div>
						<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnAddSensorNum"><fmt:message key="trip.activate.button.add"/></button></div>
						<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnGetSensorNum"><fmt:message key="trip.activate.button.load"/></button></div>
						<div class="col-sm-6 sensorNumber-list">
							<ul></ul>
						</div>
					</div>
					<div class="form-group">
						<!-- 报关单号 -->
						<label class="col-sm-2 control-label" for="tripVehicleVO.declarationNumber">
							<fmt:message key="trip.label.declarationNumber"/>
						</label>
						<div class="col-sm-2">
							<input class="form-control" name="tripVehicleVO.declarationNumber" 
								id="tripVehicleVO.declarationNumber" type="text" value="${tripVehicleVO.declarationNumber }">
						</div>
						<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnGetDecNum"><fmt:message key="trip.activate.button.load"/></button></div>
						
						<!-- 车牌号 -->
						<label class="col-sm-2 control-label" for="tripVehicleVO.vehiclePlateNumber">
					         <fmt:message key="trip.label.vehiclePlateNumber"/>
					      </label>
						<div class="col-sm-3">
							<input class="form-control" name="tripVehicleVO.vehiclePlateNumber" 
								id="tripVehicleVO.vehiclePlateNumber" type="text" value="${tripVehicleVO.vehiclePlateNumber }">
						</div>
					</div>
					<div class="form-group">
						<!-- 拖车号 -->
						<label class="col-sm-2 control-label" for="tripVehicleVO.trailerNumber">
					         <fmt:message key="trip.label.trailerNumber"/>
					      </label>
						<div class="col-sm-3">
							<input class="form-control" name="tripVehicleVO.trailerNumber" 
								id="tripVehicleVO.trailerNumber" type="text" value="${tripVehicleVO.trailerNumber }">
						</div>
						
						<!-- 车辆国家 -->
						<label class="col-sm-2 control-label" for="tripVehicleVO.vehicleCountry">
					         <fmt:message key="trip.label.vehicleCountry"/>
					      </label>
						<div class="col-sm-3">
							<input class="form-control" name="tripVehicleVO.vehicleCountry" 
								id="tripVehicleVO.vehicleCountry" type="text" value="${tripVehicleVO.vehicleCountry }">
						</div>
					</div>
					<div class="form-group">
						<!-- 司机姓名 -->
						<label class="col-sm-2 control-label" for="tripVehicleVO.driverName">
					         <fmt:message key="trip.label.driverName"/>
					      </label>
						<div class="col-sm-3">
							<input class="form-control" name="tripVehicleVO.driverName" 
								id="tripVehicleVO.driverName" type="text" value="${tripVehicleVO.driverName }">
						</div>
						
						<!-- 司机国籍 -->
						<label class="col-sm-2 control-label" for="tripVehicleVO.driverCountry">
					         <fmt:message key="trip.label.driverCountry"/>
					      </label>
						<div class="col-sm-3">
							<input class="form-control" name="tripVehicleVO.driverCountry" 
								id="tripVehicleVO.driverCountry" type="text" value="${tripVehicleVO.driverCountry }">
						</div>
					</div>
					<div class="form-group">
						<!-- 集装箱号 -->
						<label class="col-sm-2 control-label" for="tripVehicleVO.containerNumber">
					         <fmt:message key="trip.label.containerNumber"/>
					      </label>
						<div class="col-sm-3">
							<input class="form-control" name="tripVehicleVO.containerNumber" 
								id="tripVehicleVO.containerNumber" type="text" value="${tripVehicleVO.containerNumber }">
						</div>
					</div>
					<div class="form-group">
						<!-- 检入地点 -->
						<!-- 从session的缓存用户信息取 -->
						<label class="col-sm-2 control-label" for="tripVehicleVO.checkinPort">
					         <fmt:message key="trip.label.checkinPort"/>
					      </label>
						<div class="col-sm-3">
							<input class="form-control" name="tripVehicleVO.checkinPort" 
								id="tripVehicleVO.checkinPort" type="text" value="${tripVehicleVO.checkinPort }">
						</div>
						
						<!--  检出地点 -->
						<label class="col-sm-2 control-label" for="tripVehicleVO.checkoutPort">
					        <fmt:message key="trip.label.checkoutPort"/>
					      </label>
						<div class="col-sm-3">
							<select class="form-control" name="tripVehicleVO.checkoutPort" 
								id="tripVehicleVO.checkoutPort" readonly="readonly">
								<option value=""></option>
					    	</select>
						</div>
					</div>
					<div class="form-group">
					<!-- 路线 -->
						<label class="col-sm-2 control-label" for="tripVehicleVO.routeId">
					         <fmt:message key="trip.label.routeId"/>
					      </label>
						<div class="col-sm-3">
							<select class="form-control" name="tripVehicleVO.routeId" 
								id="tripVehicleVO.routeId" readonly="readonly">
								<option value=""></option>
					        	<option value="1">Neimenggu -> Kazakhstan -> Jordan</option>
					    	</select>
						</div>
					</div>
				</div>
			</div>
			</div>
			
			<!-- upload -->
			<div class="panel panel-info">
		      <div class="panel-heading">
		         <h4 class="panel-title">
		            <a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">
		               	<fmt:message key="trip.activate.title.imageInfo"/>
		            </a>
		         </h4>
		      </div>
		      <div id="collapseTwo" class="panel-collapse collapse in">
		        <div class="panel-body">
		        	<div class="col-sm-12 has-error file-help-block" style="display: none;">
		        		<small class="help-block" style=""><fmt:message key="trip.activate.info.image.required"/></small>
		        	</div>
		        	<div class="row" id="row">
			    	</div>
			    	<span name="info-upload" style="padding-left: 15px;">
			    		<fmt:message key="trip.activate.info.selectFiles">
			    			<fmt:param>0</fmt:param>
			    			<fmt:param>0KB</fmt:param>
			    		</fmt:message>
			    	</span>
			    	,
			    	<span name="info-camera">
			    		<fmt:message key="trip.activate.info.cameraPhotos">
			    			<fmt:param>0</fmt:param>
			    			<fmt:param>0KB</fmt:param>
			    		</fmt:message>
			    	</span>
				</div>
			</div>
			</div>
				
			<!-- camara -->
			<div id="main" class="masthead" style="display: none; text-align: center;">
				<div id="face_scan_camera" class="container blackbg" style="height:500px; ">
					<div style="width:600px; margin:0 auto;">
						<video id="video" width="600" height="460" autoplay="autoplay" style="margin:0 auto; position:relative; z-index:100;"></video>
					</div>
					<div class="scan-area" style="height:600px; width:500px; display:none; ">
						<canvas id="canvas" width="600" height="460" style="display:inline-block; margin:0 auto; position:relative; left:13px; top:70px; z-index:100;"></canvas>
					</div>
					<div class="btn-group">
						<button type="button" class="btn btn-default" id="btnSnap"><fmt:message key="trip.activate.button.snap"/></button>
						<button type="button" class="btn btn-default" id="btnCameraClose"><fmt:message key="trip.activate.button.closeCamera"/></button>
					</div>
				</div>
				<div id="cream_loading" style="display:none;position:relative; margin-top: -25%;left:48%;height:124px;width:124px;z-index:2001;">
					<img src="${root }/static/images/loading.gif" />
				</div>
			</div>
		</form>
		<div id="Results" style="display: none;"></div>
	
		<script type="text/javascript" src="${root }/static/js/jquery.min.js"></script>
		<script type="text/javascript" src="${root }/static/js/bootstrap.js"></script>
		<script type="text/javascript" src="${root }/static/js/gumwrapper.js"></script>
		<script type="text/javascript" src="${root }/static/js/common/common.js"></script>
		<script type="text/javascript" src="${root }/static/js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="${root }/static/js/bootstrap/validator0.5.2/js/bootstrapValidator.min.js"></script>
		<script type="text/javascript" src="${root }/static/js/bootstrap/validator0.5.2/js/language/${userLocale }.js"></script>
		<script type="text/javascript" src="${root }/static/js/bootstrap/bootbox/bootbox.js"></script>
		<script type="text/javascript">
			var root = "${root}";
			var language='${language}';
			var locale='${userLocale}'; 
		</script>
		<script src="${root}/trip/js/edit.js"></script>
	</body>
</html>