<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/include.jsp" />
<title><fmt:message key="trip.edit.title"/></title>
<link rel="stylesheet" href="${root }/static/css/trip.css" />
<style>
.esealNumber-list ul li, .sensorNumber-list ul li {
	float: left;
	border: #c3ced5 solid 1px;
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
		            	<fmt:message key="trip.info.wait.approval"/>
		         </div>
		      </div><!-- /.modal-content -->
			</div>
		</div>
		<!-- /.modal -->
	
		<div class="tab-content m-b">
			<form class="form-horizontal" role="form" id="tripForm" action="${root }/monitortrip/update.action" method="post" enctype="multipart/form-data">
				<input type="hidden" id="tripVehicleVO.tripId" name="tripVehicleVO.tripId" value="${tripVehicleVO.tripId }">
				<input type="hidden" id="tripVehicleVO.vehicleId" name="tripVehicleVO.vehicleId" value="${tripVehicleVO.vehicleId }">
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
				<button type="submit" class="btn btn-danger" id="btnSave"><fmt:message key="common.button.save"/></button>
				<button type="button" class="btn btn-danger" id="btnBack" onclick="javascript: history.back(-1);"><fmt:message key="common.button.back"/></button>
			
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
							<label class="col-sm-1 control-label" for="tripVehicleVO.trackingDeviceNumber">
								<fmt:message key="trip.label.trackingDeviceNumber"/>
							</label>
							<div class="col-sm-2">
								<input class="form-control" name="tripVehicleVO.trackingDeviceNumber" 
									id="tripVehicleVO.trackingDeviceNumber" type="text" value="${tripVehicleVO.trackingDeviceNumber }">
							</div>
							<%-- <div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnAddElockNum"><fmt:message key="trip.activate.button.add"/></button></div> --%>
							<div class="col-sm-3"><button type="button" class="btn btn-danger" id="btnGetDeviceNum"><fmt:message key="trip.activate.button.load"/></button></div>
							
							<!-- 电量 -->
							<label class="col-sm-1 control-label">
								<%-- <fmt:message key="trip.label.electricity"/> --%>
						    </label>
							<div class="col-sm-3">
						      	<table class="table">
							      <th><fmt:message key="trip.label.location"/></th>
							      <th><fmt:message key="trip.label.communication"/></th>
							      <th><fmt:message key="trip.info.inArea"/></th>
							      <th><fmt:message key="trip.info.dump.energy"/></th>
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
						</div>
						<div class="form-group">
							<!-- 子锁号 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.esealNumber">
						         <fmt:message key="trip.label.esealNumber"/>
						      </label>
							<div class="col-sm-2">
								<input class="form-control" name="esealNumberInput" id="esealNumberInput" type="text" value="">
								<input class="form-control" type="hidden" name="tripVehicleVO.esealNumber" 
									id="tripVehicleVO.esealNumber" value="${tripVehicleVO.esealNumber } ">
								<input class="form-control" type="hidden" name="tripVehicleVO.esealOrder" 
									id="tripVehicleVO.esealOrder" value="${tripVehicleVO.esealOrder }">
							</div>
							<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnAddEsealNum"><fmt:message key="trip.activate.button.add"/></button></div>
							<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnGetEsealNum"><fmt:message key="trip.activate.button.load"/></button></div>
							<div class="col-sm-7 esealNumber-list">
								<ul></ul>
							</div>
						</div>
						<div class="form-group">
							<!-- 传感器编号 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.sensorNumber">
						         <fmt:message key="trip.label.sensorNumber"/>
						      </label>
							<div class="col-sm-2">
								<input class="form-control" name="sensorNumberInput" id="sensorNumberInput" type="text" value="">
								<input class="form-control" type="hidden" name="tripVehicleVO.sensorNumber" 
									id="tripVehicleVO.sensorNumber" value="${tripVehicleVO.sensorNumber }">
								<input class="form-control" type="hidden" name="tripVehicleVO.sensorOrder" 
									id="tripVehicleVO.sensorOrder" value="${tripVehicleVO.sensorOrder }">
							</div>
							<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnAddSensorNum"><fmt:message key="trip.activate.button.add"/></button></div>
							<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnGetSensorNum"><fmt:message key="trip.activate.button.load"/></button></div>
							<div class="col-sm-7 sensorNumber-list">
								<ul></ul>
							</div>
						</div>
						<div class="form-group">
							<!-- 报关单号 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.declarationNumber">
								<fmt:message key="trip.label.declarationNumber"/>
							</label>
							<div class="col-sm-2">
								<input class="form-control" name="tripVehicleVO.declarationNumber" 
									id="tripVehicleVO.declarationNumber" type="text" value="${tripVehicleVO.declarationNumber }">
							</div>
							<div class="col-sm-1"><button type="button" class="btn btn-danger" id="btnGetDecNum"><fmt:message key="trip.activate.button.load"/></button></div>
							
							<!-- 车牌号 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.vehiclePlateNumber">
						         <fmt:message key="trip.label.vehiclePlateNumber"/>
						      </label>
							<div class="col-sm-2">
								<input class="form-control" name="tripVehicleVO.vehiclePlateNumber" 
									id="tripVehicleVO.vehiclePlateNumber" type="text" value="${tripVehicleVO.vehiclePlateNumber }">
							</div>
						
							<!-- 拖车号 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.trailerNumber">
						         <fmt:message key="trip.label.trailerNumber"/>
						      </label>
							<div class="col-sm-2">
								<input class="form-control" name="tripVehicleVO.trailerNumber" 
									id="tripVehicleVO.trailerNumber" type="text" value="${tripVehicleVO.trailerNumber }">
							</div>
							
						</div>
						<div class="form-group">							
							<!-- 车辆国家 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.vehicleCountry">
						         <fmt:message key="trip.label.vehicleCountry"/>
						      </label>
							<div class="col-sm-2">
								<input class="form-control" name="tripVehicleVO.vehicleCountry" 
									id="tripVehicleVO.vehicleCountry" type="text" value="${tripVehicleVO.vehicleCountry }">
							</div>
							<!-- 司机姓名 -->
							<label class="col-sm-2 control-label" for="tripVehicleVO.driverName">
						         <fmt:message key="trip.label.driverName"/>
						      </label>
							<div class="col-sm-2">
								<input class="form-control" name="tripVehicleVO.driverName" 
									id="tripVehicleVO.driverName" type="text" value="${tripVehicleVO.driverName }">
							</div>
							
							<!-- 司机国籍 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.driverCountry">
						         <fmt:message key="trip.label.driverCountry"/>
						      </label>
							<div class="col-sm-2">
								<input class="form-control" name="tripVehicleVO.driverCountry" 
									id="tripVehicleVO.driverCountry" type="text" value="${tripVehicleVO.driverCountry }">
							</div>
						</div>
						<div class="form-group">
							<!-- 集装箱号 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.containerNumber">
						         <fmt:message key="trip.label.containerNumber"/>
						      </label>
							<div class="col-sm-2">
								<input class="form-control" name="tripVehicleVO.containerNumber" 
									id="tripVehicleVO.containerNumber" type="text" value="${tripVehicleVO.containerNumber }">
							</div>
							
							<!-- 检入地点 -->
							<label class="col-sm-2 control-label" for="tripVehicleVO.checkinPort">
						         <fmt:message key="trip.label.checkinPort"/>
						      </label>
							<div class="col-sm-2">
								<input class="form-control" readonly="readonly" name="tripVehicleVO.checkinPort" 
									id="tripVehicleVO.checkinPort" type="hidden" value="${tripVehicleVO.checkinPort }">
								<input class="form-control" readonly="readonly" name="tripVehicleVO.checkinPortName" 
									id="tripVehicleVO.checkinPortName" type="text" value="${tripVehicleVO.checkinPortName }">
							</div>
							
							<!--  检出地点 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.checkoutPort">
						        <fmt:message key="trip.label.checkoutPort"/>
						      </label>
							<div class="col-sm-2">
								<select class="form-control" name="tripVehicleVO.checkoutPort" 
									id="tripVehicleVO.checkoutPort">
						    	</select>
							</div>
						</div>
						<div class="form-group">
							<!-- 路线 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.routeId">
						         <fmt:message key="trip.label.routeId"/>
						      </label>
							<div class="col-sm-2">
								<select class="form-control" name="tripVehicleVO.routeId" 
									id="tripVehicleVO.routeId">
						    	</select>
							</div>
							
							<!-- 耗时 -->
							<label class="col-sm-2 control-label" for="tripVehicleVO.checkinPort">
						         <fmt:message key="trip.label.timeCost"/>
						      </label>
							<div class="col-sm-2 control-label" id="timeCost" name="timeCost" style="text-align: left;">
							</div>
							
							<!-- 风险等级 -->
							<label class="col-sm-1 control-label" for="tripVehicleVO.riskStatus">
						         <fmt:message key="trip.label.riskStatus"/>
						      </label>
							<div class="col-sm-2" style="height: 30px!important; padding: 0px;">
						    	<label class="riskLabel">
							    	<input type="radio" name="tripVehicleVO.riskStatus" id="riskStatus0" value="0" checked="checked"/>
							    	<span style="background-color: #31d231;"><fmt:message key="trip.label.riskStatus.low"/></span>
						    	</label>
						    	<label class="riskLabel">
							    	<input type="radio" name="tripVehicleVO.riskStatus" id="riskStatus1" value="1"/>
							    	<span style="background-color: #ffff00;"><fmt:message key="trip.label.riskStatus.middle"/></span>
						    	</label>
						    	<label class="riskLabel">
							    	<input type="radio" name="tripVehicleVO.riskStatus" id="riskStatus2" value="2"/>
							    	<span style="background-color: #ff0000;"><fmt:message key="trip.label.riskStatus.high"/></span>
						    	</label>
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
			        		<c:if test="${tripVehicleVO.checkinPicture != null && fn:length(tripVehicleVO.checkinPicture) > 0 }">
				        		<c:forEach var="checkinPicture" items="${fn:split(tripVehicleVO.checkinPicture, ',') }" varStatus="status">
				        		  <div class="col-sm-6 col-md-3"> 
									<div class="thumbnail"> 
										<c:set var="paths" value="${fn:split(checkinPicture, '/') }" />
				        				<c:set var="index" value="${fn:length(paths) - 1}"/>
				        				<c:set var="imageName" value="${paths[index] }" />
										<img src="${rootPathHttp + tripPhotoPath + '/' }${checkinPicture }" name="localImage" /> 
										<div class="caption"> 
											<p title="${imageName }"> 
												<span>${imageName }</span> 
											</p> 
										</div> 
									</div>
								  </div>
				        		</c:forEach>
			        		</c:if>
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
		</div>
	</div>
</div>
<script type="text/javascript" src="${root }/static/js/gumwrapper.js"></script>
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
<script src="${root}/trip/js/edit.js"></script>
<script>
var checkoutPort = "${tripVehicleVO.checkoutPort }";
var routeId = "${tripVehicleVO.routeId}";
$("input[name=tripVehicleVO\\.riskStatus]").eq("${tripVehicleVO.riskStatus}").attr("checked", 'checked');
</script>
</body>
</html>