<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/include.jsp" />
<style>
#tripDetail table {
	white-space: nowrap;
    table-layout: fixed;
    text-align: right;
}
#tripDetail table tr:nth-of-type(even) {
	background-color: #f9f9f9;
}
#tripDetail table th {
	width: 20%;
}
#tripDetail table td {
	width: 30%;
}
#tripDetail table td {
	border-right-color: rgb(221, 221, 221);
	border-right-style: solid;
	border-right-width: 1px;
	border: 1px solid #ddd;
	white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}
#myTabContent1 .form-group {
	margin-bottom: 0px;
}
#myTabContent1 p {
	text-align: right;
	padding: 0px;
}
#myTabContent1 label {
	text-align: left;
}
</style>

<!-- 行程详情DIV -->
<!-- <div id="tripDetail" class="fixed-table-container" style="display: none;position: fixed;right: 10px;top: 22%;z-index: 1051; background-color: #f9f9f9; text-align: center;"> -->
<div id="tripDetail" class="fixed-table-container" 
	style="position: fixed;left: 50%;top: 50%;z-index: 1051; text-align: center; transform: translate(-50%, -50%); width: 900px; height: 530px;">
	<table class="table table-hover table-striped">
		<tr>
			<th><div class="th-inner"><fmt:message key="trip.label.vehiclePlateNumber"/></div></th>
			<td id="_tripVehicleVO.vehiclePlateNumber" title="${tripVehicleVO.vehiclePlateNumber }">${tripVehicleVO.vehiclePlateNumber }</td>
			<th><div class="th-inner"><fmt:message key="trip.label.routeId"/></div></th>
			<td id="_tripVehicleVO.routeId" title="${tripVehicleVO.routeName }">${tripVehicleVO.routeName }</td>
		</tr>
		<tr>
			<th><div class="th-inner"><fmt:message key="trip.label.checkinTime"/></div></th>
			<td id="_tripVehicleVO.checkinTime" title="${tripVehicleVO.checkinTime }">${tripVehicleVO.checkinTime }</td>
			<th><div class="th-inner"><fmt:message key="trip.label.checkoutTime"/></div></th>
			<td id="_tripVehicleVO.checkoutTime" title="${tripVehicleVO.checkoutTime }">${tripVehicleVO.checkoutTime }</td>
		</tr>
		<tr>
			<th><div class="th-inner"><fmt:message key="trip.label.checkinPort"/></div></th>
			<td id="_tripVehicleVO.checkinPortName" title="${tripVehicleVO.checkinPortName }">${tripVehicleVO.checkinPortName }</td>
			<th><div class="th-inner"><fmt:message key="trip.label.checkoutPort"/></div></th>
			<td id="_tripVehicleVO.checkoutPortName" title="${tripVehicleVO.checkoutPortName }">${tripVehicleVO.checkoutPortName }</td>
		</tr>
		<tr>
			<th><div class="th-inner"><fmt:message key="trip.label.checkinUser"/></div></th>
			<td id="_tripVehicleVO.checkinUserName" title="${tripVehicleVO.checkinUserName }">${tripVehicleVO.checkinUserName }</td>
			<th><div class="th-inner"><fmt:message key="trip.label.checkoutUser"/></div></th>
			<td id="_tripVehicleVO.checkoutUserName" title="${tripVehicleVO.checkoutUserName }">${tripVehicleVO.checkoutUserName }</td>
		</tr>
	</table>
	
	<ul id="myTab1" class="nav nav-tabs">
     		<c:forEach var="commonVehicleDriver" items="${tripVehicleVO.commonVehicleDriverList }" varStatus="status">
			<li <c:if test="${status.first }">class="active"</c:if> >
				<a href="#${commonVehicleDriver.vehiclePlateNumber}" data-toggle="tab">${commonVehicleDriver.vehiclePlateNumber}</a>
			</li>
		</c:forEach>
	</ul>
	
	<div id="myTabContent1" class="tab-content">
		<c:forEach var="commonVehicleDriver" items="${tripVehicleVO.commonVehicleDriverList }" varStatus="status">
		<div class="tab-pane fade <c:if test="${status.first }">in active</c:if>" id="${commonVehicleDriver.vehiclePlateNumber}">
			<div class="panel-body" name="vehicle-panel">
				<div class="form-group">
					<label class="col-sm-2 control-label">
						<fmt:message key="trip.label.vehiclePlateNumber"/>
					</label>
					<div class="col-sm-2">
						<p class="form-control-static">${commonVehicleDriver.vehiclePlateNumber }</p>
					</div>
					
					<label class="col-sm-2 control-label">
						<fmt:message key="trip.label.vehicleCountry"/>
					</label>
					<div class="col-sm-2">
						<p class="form-control-static">${commonVehicleDriver.vehicleCountry }</p>
					</div>
					
					<label class="col-sm-2 control-label">
						<fmt:message key="trip.label.trailerNumber"/>
					</label>
					<div class="col-sm-2">
						<p class="form-control-static">${commonVehicleDriver.trailerNumber }</p>
					</div>
					<div class="clearfix"></div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">
						<fmt:message key="trip.label.driverName"/>
					</label>
					<div class="col-sm-2">
						<p class="form-control-static">${commonVehicleDriver.driverName }</p>
					</div>
					
					<label class="col-sm-2 control-label">
						<fmt:message key="trip.label.driverCountry"/>
					</label>
					<div class="col-sm-2">
						<p class="form-control-static">${commonVehicleDriver.driverCountry }</p>
					</div>
					
					<label class="col-sm-2 control-label">
						<fmt:message key="trip.label.driverIdCard"/>
					</label>
					<div class="col-sm-2">
						<p class="form-control-static">${commonVehicleDriver.driverIdCard }</p>
					</div>
					<div class="clearfix"></div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">
						<fmt:message key="trip.label.containerNumber"/>
					</label>
					<div class="col-sm-2">
						<p class="form-control-static">${commonVehicleDriver.containerNumber }</p>
					</div>
					
					<label class="col-sm-2 control-label">
					 <fmt:message key="trip.label.riskStatus"/>
					</label>
					<div class="col-sm-2">
						<p class="form-control-static">
							<c:if test="${commonVehicleDriver.riskStatus == '0' }"><fmt:message key="trip.label.riskStatus.low"/></c:if>
							<c:if test="${commonVehicleDriver.riskStatus == '1' }"><fmt:message key="trip.label.riskStatus.middle"/></c:if>
							<c:if test="${commonVehicleDriver.riskStatus == '2' }"><fmt:message key="trip.label.riskStatus.high"/></c:if>
						</p>
					</div>
					
					<label class="col-sm-2 control-label">
					 <fmt:message key="trip.label.goodsType"/>
					</label>
					<div class="col-sm-2">
						<p class="form-control-static" style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;" title="${commonVehicleDriver.goodsTypeName }">
							${commonVehicleDriver.goodsTypeName }
						</p>
					</div>
					<div class="clearfix"></div>
				</div>
				<div class="form-group">
					<!-- 追踪终端号 -->
					<label class="col-sm-2 control-label">
						<fmt:message key="trip.label.trackingDeviceNumber"/>
					</label>
					<div class="col-sm-2">
						<p class="form-control-static">${commonVehicleDriver.trackingDeviceNumber }</p>
					</div>
					
					<!-- 子锁号 -->
					<label class="col-sm-2 control-label">
					 <fmt:message key="trip.label.esealNumber"/>
					</label>
					<div class="col-sm-2">
						<p class="form-control-static">${commonVehicleDriver.esealNumber }</p>
					</div>
					
					<!-- 传感器编号 -->
					<label class="col-sm-2 control-label">
						<fmt:message key="trip.label.sensorNumber"/>
					</label>
					<div class="col-sm-2">
						<p class="form-control-static">${commonVehicleDriver.sensorNumber }</p>
					</div>
					
					 <div class="clearfix"></div>
				</div>
				
				<!-- image -->
				<div class="form-group vehicle-img">
					<label class="col-sm-2 control-label">
						<fmt:message key="trip.label.checkinPicture"/>
					</label>
					<div class="col-sm-2">
						<c:if test="${commonVehicleDriver.checkinPicture != null && fn:length(commonVehicleDriver.checkinPicture) > 0 }">
						<div id="${status.index}-in" class="carousel slide">
							<!-- 轮播（Carousel）指标 -->
							<ol class="carousel-indicators">
								<c:forEach var="checkinPicture" items="${fn:split(commonVehicleDriver.checkinPicture, ',') }" varStatus="status1">
								<li data-target="#${status.index}-in" data-slide-to="${status1.index }" ${(status1.first) ? 'class="active"' : '' }></li>
								</c:forEach>
							</ol>   
							<!-- 轮播（Carousel）项目 -->
							<div class="carousel-inner">
								<c:forEach var="checkinPicture" items="${fn:split(commonVehicleDriver.checkinPicture, ',') }" varStatus="status2">
								<c:set var="paths" value="${fn:split(checkinPicture, '/') }" />
		        				<c:set var="index" value="${fn:length(paths) - 1}"/>
		        				<c:set var="imageName" value="${paths[index] }" />
								<div class="item ${(status2.index == 0) ? 'active' : '' }">
									<a href="javascript:void(0);">
										<img src="${tripPhotoPathHttp }${checkinPicture }" alt="Image">
									</a>
								</div>
								</c:forEach>
							</div>
							<!-- 轮播（Carousel）导航 -->
							<a class="carousel-control left" href="#${status.index}-in" 
							   data-slide="prev">&lsaquo;</a>
							<a class="carousel-control right" href="#${status.index}-in" 
							   data-slide="next">&rsaquo;</a>
						</div>
						</c:if> 
					</div>
					
					<label class="col-sm-2 control-label">
						<fmt:message key="trip.label.checkoutPicture"/>
					</label>
					<div class="col-sm-2">
						<c:if test="${commonVehicleDriver.checkoutPicture != null && fn:length(commonVehicleDriver.checkoutPicture) > 0 }">
						<div id="${status.index}-out" class="carousel slide">
							<!-- 轮播（Carousel）指标 -->
							<ol class="carousel-indicators">
								<c:forEach var="checkoutPicture" items="${fn:split(commonVehicleDriver.checkoutPicture, ',') }" varStatus="status1">
								<li data-target="#${status.index}-out" data-slide-to="${status1.index }" ${(status1.first) ? 'class="active"' : '' }></li>
								</c:forEach>
							</ol>   
							<!-- 轮播（Carousel）项目 -->
							<div class="carousel-inner">
								<c:forEach var="checkoutPicture" items="${fn:split(commonVehicleDriver.checkoutPicture, ',') }" varStatus="status2">
								<c:set var="paths" value="${fn:split(checkoutPicture, '/') }" />
		        				<c:set var="index" value="${fn:length(paths) - 1}"/>
		        				<c:set var="imageName" value="${paths[index] }" />
								<div class="item ${(status2.index == 0) ? 'active' : '' }">
									<a href="javascript:void(0);">
										<img src="${tripPhotoPathHttp }${checkoutPicture }" alt="Image">
									</a>
								</div>
								</c:forEach>
							</div>
							<!-- 轮播（Carousel）导航 -->
							<a class="carousel-control left" href="#${status.index}-out" 
							   data-slide="prev">&lsaquo;</a>
							<a class="carousel-control right" href="#${status.index}-out" 
							   data-slide="next">&rsaquo;</a>
						</div>
						</c:if> 
					</div>
					
				</div>
				
			</div>
		</div>
		</c:forEach>
	</div>
</div>
<!-- 行程详情DIV end-->

<script>
var bootstrapValidator;
$(function(){
	$("#pass").on("click", function(){
		$.ajax({
			url : root + '/monitortrip/passTrip.action',
			type : "post",
			dataType : "json",
			data : {'tripId': $("#requestTripId").val(), "userId": '${sessionScope.sessionUser.userId}'},
			success : function(data) {
				var needLoginFlag = false;
				if(typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
					needLoginFlag = needLogin(data);
				}
				if(!needLoginFlag) {
					if(data.result) {
						$("#tripDetail, #tripDetailCheckOut").hide();
						bootbox.success($.i18n.prop('trip.info.success'), function() {
							initModal();
						});
					}else{
						data.message && bootbox.alert($.i18n.prop('trip.info.failed') + ':' + data.message);
					}
				}
			}
		});
	});
	$("#nopass, #nopassCancel").on("click", function(){
		$("#pass, #nopass, #nopassSubmit, #nopassCancel, #reasonDiv").toggleClass("hidden");
		$("#reason").val('');
	});
	$("#nopassSubmit").on("click", function(){
		$.ajax({
			url : root + '/monitortrip/noPassTrip.action',
			type : "post",
			dataType : "json",
			data : {'tripId': $("#requestTripId").val(), "userId": '${sessionScope.sessionUser.userId}', "reason": $("#reason").val() || ''},
			success : function(data) {
				var needLoginFlag = false;
				if(typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
					needLoginFlag = needLogin(data);
				}
				if(!needLoginFlag) {
					if(data.result) {
						$("#tripDetail, #tripDetailCheckOut").hide();
						bootbox.success($.i18n.prop('trip.info.success'), function() {
							initModal();
						});
					}else{
						data.message && bootbox.alert($.i18n.prop('trip.info.failed') + ':' + data.message);
					}
				}
			}
		});
	});
	$("#checkinPictureDiv, #checkinPicture1Div, #checkoutPictureDiv").on("click", "img", function(){
		var clone = $(this).clone();
		clone.css("max-height", "").css("border-radius", "6px");
		$('#imageModal').removeData('bs.modal');
		$("#imageModal .modal-content").append(clone);
		$('#imageModal').modal({
			show : false,
			backdrop: true, 
			keyboard: true
		}).modal('show').css("z-index", 1052);
		$("#imageModal .modal-content").css("background-color", "rgba(255, 255, 255, 0)");
		
		$('#imageModal img').on("click", function(){
			$("#imageModal img").empty();
			$('#imageModal').modal("hide");
		});
	});
	$("#btnCloseDetail, #btnCloseDetail1").on("click", function(){
		$("#tripDetail, #tripDetailCheckOut").hide();
	});
});
function initModal(){
	$("#pass, #nopass").removeClass("hidden");
	$("#nopassSubmit, #nopassCancel, #reasonDiv").addClass("hidden");
	$("#reason").val('');
}
function bootstrapValidatorForm() {
	$('#msgForm').bootstrapValidator({
		fields: {
		}
	}).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
        // Prevent form submission
        e.preventDefault();

        // Get the form instance
        var $form = $(e.target);

        // Get the BootstrapValidator instance
        var bv = $form.data('bootstrapValidator');

        // Use Ajax to submit form data
		var formData = new FormData($form[0]);
		$.ajax({
			url: $form[0].action,
			type: 'POST',
			contentType: false,
			data: formData,
			dataType: 'JSON',
			processData: false,
			success: function(data) {
				try {
					var needLoginFlag = false;
					if(typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
						needLoginFlag = needLogin(data);
					}
					if(!needLoginFlag) {
						if (data && data.result) {
							/*
							bootbox.success($.i18n.prop("trip.activate.success"), function() {  
								location.href = location.href.replace(/#$/, '');
							});
							*/
						} else if (data.message) {
							bootbox.alert($.i18n.prop("trip.activate.failed") + ":" + data.message);
						}
					}
				} catch (e) {}
			}
		});
    });
	bootstrapValidator = $('#msgForm').data('bootstrapValidator');
}

/**
 * 列出检入图像
 * @param {Object} checkinPicture
 */
function showCheckinPicture(checkinPicture, type){
	checkinPicture = checkinPicture && $.trim(checkinPicture).split(/\s*,\s*/);
	if(checkinPicture && checkinPicture.length > 0) {
		var liHtml = [], innerHtml = [], controlHtml = [], outterHtml = [];
		outterHtml.push('<div id="' + type + '" class="carousel slide" data-ride="carousel">');
		outterHtml.push('	<ol class="carousel-indicators">');
		
		for(var i = 0; i < checkinPicture.length; i++) {
			var imageName = checkinPicture[i].slice(checkinPicture[i].lastIndexOf('/') + 1);
			//轮播指标
			liHtml.push('<li data-target="#' + type + '" data-slide-to="' + i + '" ' + (i == 0 ? 'class="active"': '') + '></li>');
			
			//轮播项目
			innerHtml.push('<div class="item ' + (i == 0 ? 'active' : '' ) + '">');
			innerHtml.push('	<a href="javascript:void(0);">');
			innerHtml.push('		<img style="max-height: 120px;" src="' + tripPhotoPathHttp + checkinPicture[i] + '" alt="' + imageName + '">');
			innerHtml.push('	</a>');
			innerHtml.push('</div>');
		}
		outterHtml.push(liHtml.join(''));
		outterHtml.push('</ol>');
		outterHtml.push('<div class="carousel-inner" role="listbox">');
		outterHtml.push(innerHtml.join(''));
		outterHtml.push('</div>');
		
		//轮播导航
		outterHtml.push('<a class="left carousel-control" href="#' + type + '" role="button" data-slide="prev">');
		outterHtml.push('	<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>');
		outterHtml.push('	<span class="sr-only">Previous</span>');
		outterHtml.push('</a>');
		outterHtml.push('<a class="right carousel-control" href="#' + type + '" role="button" data-slide="next">');
		outterHtml.push('	<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>');
		outterHtml.push('	<span class="sr-only">Next</span>');
		outterHtml.push('</a>');
		
		outterHtml.push('</div>');
		
		//$("#checkinPicture>.carousel-indicators").html(liHtml.join(''));
		//$("#checkinPicture>.carousel-inner").html(innerHtml.join('')).after(controlHtml.join(''));
		$("#" + type + "Div").html(outterHtml.join(''));
	}
}
</script>