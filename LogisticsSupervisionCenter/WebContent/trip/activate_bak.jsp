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
	max-width: 50%;
	width: 50%;
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
</style>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../include/tripMsgModal.jsp" %>
<%@ include file="../include/left.jsp" %>
<div class="row site">
	<div class="wrapper-content margint95 margin60">
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
	
		<div class="tab-content m-b">
			<form class="form-horizontal" role="form" id="tripForm" action="${root }/monitortrip/activate.action" method="post" enctype="multipart/form-data">
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
				<div class="panel panel-info" style="margin-bottom: 0px;">
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
							<!-- 报关单号 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.declarationNumber">
								<fmt:message key="trip.label.declarationNumber"/>
							</label>
							<div class="col-sm-2">
								<input class="form-control" name="tripVehicleVO.declarationNumber" 
									id="tripVehicleVO.declarationNumber" type="text" value="">
							</div>
							<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnGetDecNum"><fmt:message key="trip.activate.button.load"/></button></div>
							
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
						        <fmt:message key="trip.label.checkoutPort"/>
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
						         <fmt:message key="trip.label.routeId"/>
						      </label>
							<div class="col-sm-3">
								<select class="form-control" name="tripVehicleVO.routeId" id="tripVehicleVO.routeId">
						    	</select>
							</div>
							
							<!-- 耗时 -->
							<label class="col-sm-1 control-label">
						         <fmt:message key="trip.label.timeCost"/>
						      </label>
							<div class="col-sm-3 control-label" id="timeCost" name="timeCost" style="text-align: left;">
							</div>
							
							<!-- 巡逻队 -->
							<label class="col-sm-1 control-label" for="patrolId">
						         <fmt:message key="foot.title.patrol"/>
						      </label>
							<div class="col-sm-3">
								<select id="patrolId" name="patrolId" class="form-control">
									<option value=""></option>
									<c:forEach var="patrol" items="${patrolList}">
										<option value="${patrol.patrolId }">${patrol.potralUserName}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					</div>
				</div>
				
				<div class="panel panel-info">
			      <div class="panel-heading">
			         <h4 class="panel-title">
			            <a data-toggle="collapse" data-parent="#accordion" href="#collapseVehicle">
			               	<fmt:message key="trip.report.vehicleInfo"/>
			            </a>
			         </h4>
			      </div>
			      <div id="collapseVehicle" class="panel-collapse collapse in">
			      	
			      	<div class="col-sm-12" id="vehicleDiv">
					</div>
					<div class="panel panel-default">
					    <div class="panel-body" style="display: none;">
						    <div class="form-group">
						    	<!-- 车牌号 -->
								<label class="col-sm-1 control-label" for="tripVehicleVO_vehiclePlateNumber">
							         <fmt:message key="trip.label.vehiclePlateNumber"/>
							      </label>
								<div class="col-sm-3">
									<input class="form-control" name="tripVehicleVO_vehiclePlateNumber" 
										id="tripVehicleVO_vehiclePlateNumber" type="text" value="">
								</div>
								
								<!-- 车辆国家 -->
								<label class="col-sm-1 control-label" for="tripVehicleVO_vehicleCountry">
							         <fmt:message key="trip.label.vehicleCountry"/>
							      </label>
								<div class="col-sm-3">
									<input class="form-control" name="tripVehicleVO_vehicleCountry" 
										id="tripVehicleVO_vehicleCountry" type="text" value="">
								</div>
								
								<!-- 拖车号 -->
								<label class="col-sm-1 control-label" for="tripVehicleVO_trailerNumber">
							         <fmt:message key="trip.label.trailerNumber"/>
							      </label>
								<div class="col-sm-3">
									<input class="form-control" name="tripVehicleVO_trailerNumber" 
										id="tripVehicleVO_trailerNumber" type="text" value="">
								</div>
								<div class="clearfix"></div>
							
							</div>
							<div class="form-group">
								<!-- 司机姓名 -->
								<label class="col-sm-1 control-label" for="tripVehicleVO_driverName">
							         <fmt:message key="trip.label.driverName"/>
							      </label>
								<div class="col-sm-3">
									<input class="form-control" name="tripVehicleVO_driverName" 
										id="tripVehicleVO_driverName" type="text" value="">
								</div>
								
								<!-- 司机国籍 -->
								<label class="col-sm-1 control-label" for="tripVehicleVO_driverCountry">
							         <fmt:message key="trip.label.driverCountry"/>
							      </label>
								<div class="col-sm-3">
									<input class="form-control" name="tripVehicleVO_driverCountry" 
										id="tripVehicleVO_driverCountry" type="text" value="">
								</div>
								
								<!-- 司机IdCard -->
								<label class="col-sm-1 control-label" for="tripVehicleVO_driverIdCard">
							         <fmt:message key="trip.label.driverIdCard"/>
							      </label>
								<div class="col-sm-3">
									<input class="form-control" name="tripVehicleVO_driverIdCard" 
										id="tripVehicleVO_driverIdCard" type="text" value="">
								</div>
								<div class="clearfix"></div>
							</div>
							<div class="form-group">
								<!-- 集装箱号 -->
								<label class="col-sm-1 control-label" for="tripVehicleVO_containerNumber">
							         <fmt:message key="trip.label.containerNumber"/>
							      </label>
								<div class="col-sm-3">
									<input class="form-control" name="tripVehicleVO_containerNumber" 
										id="tripVehicleVO_containerNumber" type="text" value="">
								</div>
								
								<!-- 风险等级 -->
								<label class="col-sm-1 control-label" for="tripVehicleVO_riskStatus">
							         <fmt:message key="trip.label.riskStatus"/>
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
								
								<%--货物类型 --%>
								<label class="col-sm-1 control-label" for="tripVehicleVO_goodsType">
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
								<!-- 追踪终端号 -->
								<label class="col-sm-1 control-label" for="tripVehicleVO_trackingDeviceNumber">
									<fmt:message key="trip.label.trackingDeviceNumber"/>
								</label>
								<div class="col-sm-3">
									<input class="form-control" name="tripVehicleVO_trackingDeviceNumber" 
										id="tripVehicleVO_trackingDeviceNumber" type="text" value="">
								</div>
								<div class="col-sm-4 button-margin">
									<button type="button" class="btn btn-danger" id="btnGetDeviceNum"><fmt:message key="trip.activate.button.load"/></button>
									<button type="button" class="btn btn-danger" id="btnSetLocked"><fmt:message key="trip.button.setLocked"/></button>
									<button type="button" class="btn btn-danger" id="btnSetUnlocked"><fmt:message key="trip.button.setUnlocked"/></button>
									<button type="button" class="btn btn-danger" id="btnClearAlarm"><fmt:message key="trip.button.clearAlarm"/></button>
								</div>
								
								<!-- 电量 -->
								<div class="col-sm-4">
							      	<table class="table">
								      <tr>
								      	  <th><fmt:message key="trip.label.location"/></th>
									      <th><fmt:message key="trip.label.communication"/></th>
									      <th><fmt:message key="trip.info.inArea"/></th>
									      <th><fmt:message key="trip.info.dump.energy"/></th>
								      </tr>
								      <tr>
								      	<td><span id="elockLocation" class="glyphicon" style="padding-left: 15px;"></span></td>
								      	<td><span id="elockCommuicate" class="glyphicon" style="padding-left: 15px;"></span></td>
								      	<td><span id="elockInArea" class="glyphicon" style="padding-left: 15px;"></span></td>
								      	<td>
									      	<div class="progress">
											 <div class="progress-bar progress-bar-success" id="dianliang" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: 0%"></div> 
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
								 <!-- 子锁号 -->
								<label class="col-sm-1 control-label" for="tripVehicleVO_esealNumber">
							         <fmt:message key="trip.label.esealNumber"/>
							      </label>
								<div class="col-sm-3">
									<input class="form-control" name="esealNumberInput" id="esealNumberInput" type="text" value="">
									<input class="form-control" type="hidden" name="tripVehicleVO_esealNumber" 
										id="tripVehicleVO_esealNumber" value="">
								</div>
								<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnAddEsealNum"><fmt:message key="trip.activate.button.add"/></button></div>
								<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnGetEsealNum"><fmt:message key="trip.activate.button.load"/></button></div>
								<div class="col-sm-6 esealNumber-list">
									<ul></ul>
								</div>
								<div class="clearfix"></div>
								
							</div>
							<div class="form-group">
								<!-- 传感器编号 -->
								<label class="col-sm-1 control-label" for="tripVehicleVO_sensorNumber">
							         <fmt:message key="trip.label.sensorNumber"/>
							      </label>
								<div class="col-sm-3">
									<input class="form-control" name="sensorNumberInput" id="sensorNumberInput" type="text" value="">
									<input class="form-control" type="hidden" name="tripVehicleVO_sensorNumber" 
										id="tripVehicleVO_sensorNumber" value="">
								</div>
								<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnAddSensorNum"><fmt:message key="trip.activate.button.add"/></button></div>
								<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnGetSensorNum"><fmt:message key="trip.activate.button.load"/></button></div>
								<div class="col-sm-6 sensorNumber-list">
									<ul></ul>
								</div>
						    </div>
						    
						    <button type="button" class="btn btn-danger" id="btnSaveVehicle"><fmt:message key="trip.activate.button.save"/></button>
						    <button type="button" class="btn btn-danger" id="btnCancelVehicle"><fmt:message key="trip.activate.button.cancel"/></button>
						   </div>
					</div>
					<button type="button" class="btn btn-danger" id="btnNewVehicle"><fmt:message key="trip.activate.button.newVehicle"/></button>
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
				
				<%--barcode --%>
				<div id="barcodeView" style="display: none; text-align: center;">
					<button type="button" class="btn btn-default" id="btnCloseBarcode" style="margin-bottom: 5px;"><fmt:message key="trip.activate.button.closeCamera"/></button>
					<div id="barcode"></div>
				</div>
			</form>
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
	initWebSocket("${clientIPAddress}");
});
</script>
<script src="${root}/trip/js/elockUtil.js"></script>
<script src="${root}/trip/js/barcodeLive.js"></script>
<script src="${root}/trip/js/activate.js"></script>

<%--ejs模板 --%>
<script id="vehicleInfo" type="text/template">
<tr>
	<td>
		{{= vehiclePlateNumber }}
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
	</td>
	<td>{{= trackingDeviceNumber }}</td>
	<td>{{= esealNumber }}</td>
	<td>{{= sensorNumber }}</td>
	<td>{{= trailerNumber }}</td>
	<td>{{= vehicleCountry }}</td>
	<td>{{= driverName }}</td>
	<td>{{= driverCountry }}</td>
	<td>{{= driverIdCard }}</td>
	<td>{{= containerNumber }}</td>
	<td>{{= goodsTypeName }}</td>
	<td><a href="javascript:void(0);" class="vehicle-edit" title="<fmt:message key="trip.activate.button.edit"/>"><span class="glyphicon glyphicon-edit" ></span></a>&nbsp;
		<a href="javascript:void(0);" class="vehicle-delete" title="<fmt:message key="trip.activate.button.delete"/>"><span class="glyphicon glyphicon-remove" ></span></a>
	</td>
</tr>
</script>
<script id="vehicleHeader" type="text/template">
<table id="vehicleTable" class="table table-hover table-striped">
	<thead>
		<tr>
			<th><fmt:message key="trip.label.vehiclePlateNumber"/></th>
			<th><fmt:message key="trip.label.trackingDeviceNumber"/></th>
			<th><fmt:message key="trip.label.esealNumber"/></th>
			<th><fmt:message key="trip.label.sensorNumber"/></th>
			<th><fmt:message key="trip.label.trailerNumber"/></th>
			<th><fmt:message key="trip.label.vehicleCountry"/></th>
			<th><fmt:message key="trip.label.driverName"/></th>
			<th><fmt:message key="trip.label.driverCountry"/></th>
			<th><fmt:message key="trip.label.driverIdCard"/></th>
			<th><fmt:message key="trip.label.containerNumber"/></th>
			<th><fmt:message key="trip.label.goodsType"/></th>
			<th><fmt:message key="trip.report.list.operate"/></th>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
</script>
</body>
</html>