<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include/include.jsp" />
<title><fmt:message key="alarm.report.title"/></title>
<link rel="stylesheet" href="${root }/static/css/trip.css" />
</head>
<body>
<%@ include file="../include/left.jsp" %>
<div class="row site">
	<div class="wrapper-content margint95 margin60">
		<div class="profile profile_box02">
			
			<%--alarmList --%>
			<div class="tab-content m-b">
				<div class="tab-cotent-title">
					<fmt:message key="trip.report.alarmInfo"/>
			    </div>
		        <div class="search_table">
					<div class="fixed-table-container" style="padding-bottom: 16px;">
						<table class="table table-hover table-striped">
							<thead>
							  <th><div class="th-inner"><fmt:message key="trip.report.serialNumber"/></div></th>
						      <th><div class="th-inner"><fmt:message key="trip.report.alarmTime"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarm.label.receiveTime"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarm.label.userName"/></div></th>
						      <th><div class="th-inner"><fmt:message key="trip.report.alarmLongitude"/></div></th>
						      <th><div class="th-inner"><fmt:message key="trip.report.alarmLatitude"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarm.label.alarmStatus"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarm.label.alarmLevelName"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarm.label.alarmTypeName"/></div></th>
					        </thead>
					        <tbody>
								<c:forEach var="alarm" items="${alarmDealList }" varStatus="status">
								<tr>
									<td>${status.count }</td>
							      	<td>${alarm.receiveTime }</td>
							      	<td>${alarm.recipientsUser }</td>
							      	<td>${alarm.dealUser }</td>
							      	<td>${alarm.dealMethod }</td>
							      	<td>${alarm.dealTime }</td>
							      	<td>${alarm.dealResult }</td>
							      	<td>${alarm.dealDesc }</td>
							      </tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		
			<!-- tab-content -->
			<div class="tab-content m-b">
			  <div class="tab-cotent-title">
			  	<div class="Features pull-right">
					<ul>
						<li><a id="btnBack" class="btn btn-info"><fmt:message key="common.button.back"/></a></li>
					</ul>
				</div>
			  	<fmt:message key="trip.report.basic"/>
			  </div>
			  
			  <!-- search_form -->
			  <div class="search_form">
					<form class="form-horizontal" role="form" id="tripForm">
						<!-- elock -->
						<div class="form-group">
							<div class="col-sm-8">
								<div class="col-sm-12" id="baseInfo">
									<!-- 报关单号 -->
									<label class="col-sm-3 control-label">
										<fmt:message key="trip.label.declarationNumber"/>
									</label>
									<div class="col-sm-3 control-label">&nbsp;
										${tripVehicleVO.declarationNumber }
									</div>
									
									<!-- 车牌号 -->
									<label class="col-sm-3 control-label">
								         <fmt:message key="trip.label.vehiclePlateNumber"/>
								      </label>
									<div class="col-sm-3 control-label">&nbsp;
										${tripVehicleVO.vehiclePlateNumber }
									</div>
									
									<!-- 拖车号 -->
									<label class="col-sm-3 control-label">
								         <fmt:message key="trip.label.trailerNumber"/>
								      </label>
									<div class="col-sm-3 control-label">&nbsp;
										${tripVehicleVO.trailerNumber }
									</div>
									
									<!-- 车辆国家 -->
									<label class="col-sm-3 control-label">
								         <fmt:message key="trip.label.vehicleCountry"/>
								      </label>
									<div class="col-sm-3 control-label">&nbsp;
										${tripVehicleVO.vehicleCountry }
									</div>
									
									<!-- 司机姓名 -->
									<label class="col-sm-3 control-label">
								         <fmt:message key="trip.label.driverName"/>
								      </label>
									<div class="col-sm-3 control-label">&nbsp;
										${tripVehicleVO.driverName }
									</div>
									
									<!-- 司机国籍 -->
									<label class="col-sm-3 control-label">
								         <fmt:message key="trip.label.driverCountry"/>
								      </label>
									<div class="col-sm-3 control-label">&nbsp;
										${tripVehicleVO.driverCountry }
									</div>
									
									<!-- 集装箱号 -->
									<label class="col-sm-3 control-label">
								         <fmt:message key="trip.label.containerNumber"/>
								      </label>
									<div class="col-sm-3 control-label">&nbsp;
										${tripVehicleVO.containerNumber }
									</div>
									
									<!-- 轨迹路线 -->
									<label class="col-sm-3 control-label">
								         <fmt:message key="trip.label.routeId"/>
								      </label>
									<div class="col-sm-3 control-label">&nbsp;
										<a id="route" style="cursor: pointer;" data-routeId="${tripVehicleVO.routeId }" data-tripId="${tripVehicleVO.tripId }">点击查看</a>
									</div>
								</div>
							</div>
							
							<div class="col-sm-4" style="border-left: #ddd solid 1px;" id="deviceInfo">
								<!-- 追踪终端号 -->
								<label class="col-sm-3 control-label" >
									<fmt:message key="trip.label.trackingDeviceNumber"/>
								</label>
								<div class="col-sm-9 control-label">&nbsp;
									${tripVehicleVO.trackingDeviceNumber }
								</div>
								
								<!-- 子锁号 -->
								<label class="col-sm-3 control-label" >
							         <fmt:message key="trip.label.esealNumber"/>
							    </label>
								<div class="col-sm-9 control-label esealNumber-list">
									<ul>	
										<c:forEach var="esealNumber" items="${tripVehicleVO.esealNumber }" varStatus="status">
										<li>${esealNumber }</li>
										</c:forEach>
									</ul>
								</div>
								
								<!-- 传感器编号 -->
								<label class="col-sm-3 control-label" >
							         <fmt:message key="trip.label.sensorNumber"/>
							      </label>
								<div class="col-sm-9 control-label sensorNumber-list">
									<ul>
										<c:forEach var="sensorNumber" items="${tripVehicleVO.sensorNumber }" varStatus="status">
										<li>${sensorNumber }</li>
										</c:forEach>
									</ul>
								</div>
							</div>
						</div>
						
						<hr/>
							
						<div class="form-group" style="height: 250px;">
							<div class="col-sm-6" style="border-right: #ddd solid 1px;">
							  <div class="tab-cotent-title"><fmt:message key="trip.report.checkinInfo"/></div>
							  	<div class="col-sm-7" id="checkinInfo">
									<!-- 检入时间 -->
									<label class="col-sm-4 control-label">
								         <fmt:message key="trip.label.checkinTime"/>
								      </label>
									<div class="col-sm-8 control-label">&nbsp;
										${tripVehicleVO.checkinTime }
									</div>
									
									<!--  检入地点 -->
									<label class="col-sm-4 control-label">
								        <fmt:message key="trip.label.checkinPort"/>
								      </label>
									<div class="col-sm-8 control-label">&nbsp;
										${tripVehicleVO.checkinPortName }
									</div>
									
									<!--  检入用户 -->
									<label class="col-sm-4 control-label">
								        <fmt:message key="trip.label.checkinUser"/>
								      </label>
									<div class="col-sm-8 control-label">&nbsp;
										${tripVehicleVO.checkinUserName }
									</div>
								</div>
								
								<!-- 检入图片 -->
								<div class="col-sm-5">
					        		<div id="checkinPictures" class="carousel slide" data-ride="carousel">
					        			<!-- 轮播（Carousel）指标 -->
										<ol class="carousel-indicators">
											<c:forEach var="checkinPicture" items="${fn:split(tripVehicleVO.checkinPicture, ',') }" varStatus="status">
											<li data-target="#checkinPictures" data-slide-to="${status.index }" ${(status.index == 0) ? 'class="active"' : '' }></li>
											</c:forEach>
										</ol>
					        			
					        			<!-- 轮播（Carousel）项目 -->
										<div class="carousel-inner" role="listbox">
											<c:forEach var="checkinPicture" items="${fn:split(tripVehicleVO.checkinPicture, ',') }" varStatus="status">
											<c:set var="paths" value="${fn:split(checkinPicture, '/') }" />
					        				<c:set var="index" value="${fn:length(paths) - 1}"/>
					        				<c:set var="imageName" value="${paths[index] }" />
											<div class="item ${(status.index == 0) ? 'active' : '' }">
												<a href="javascript:void(0);">
													<img src="${tripPhotoPathHttp }${checkinPicture }" alt="${imageName }">
												</a>
											</div>
											</c:forEach>
										</div>
										
										<!-- 轮播（Carousel）导航 -->
										<a class="left carousel-control" href="#checkinPictures" role="button" data-slide="prev">
											<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
											<span class="sr-only">Previous</span>
										</a>
										<a class="right carousel-control" href="#checkinPictures" role="button" data-slide="next">
											<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
											<span class="sr-only">Next</span>
										</a>
					        		</div>
								</div>
							</div>
							
							<div class="col-sm-6">
								<div class="tab-cotent-title"><fmt:message key="trip.report.checkoutInfo"/></div>
								<div class="col-sm-7" id="checkoutInfo">
									<!-- 检出时间 -->
									<label class="col-sm-4 control-label">
								         <fmt:message key="trip.label.checkoutTime"/>
								      </label>
									<div class="col-sm-8 control-label">&nbsp;
										${tripVehicleVO.checkoutTime }
									</div>
									
									<!--  检出地点 -->
									<label class="col-sm-4 control-label">
								        <fmt:message key="trip.label.checkoutPort"/>
								      </label>
									<div class="col-sm-8 control-label">&nbsp;
										${tripVehicleVO.checkoutPortName }
									</div>
									
									<!--  检出用户 -->
									<label class="col-sm-4 control-label">
								        <fmt:message key="trip.label.checkoutUser"/>
								      </label>
									<div class="col-sm-8 control-label">&nbsp;
										${tripVehicleVO.checkoutUserName }
									</div>
									<!-- 行程耗时 -->
									<label class="col-sm-4 control-label">
								         <fmt:message key="trip.label.timeCost"/>
								      </label>
									<div class="col-sm-8 control-label">&nbsp;
										${tripVehicleVO.timeCost }
									</div>
								</div>
								
								<!-- 检出图片 -->
								<div class="col-sm-5">
				        			<c:if test="${tripVehicleVO.tripStatus == '1' }">
					        		<div id="checkoutPictures" class="carousel slide" data-ride="carousel">
					        			<!-- 轮播（Carousel）指标 -->
										<ol class="carousel-indicators">
											<c:forEach var="checkoutPicture" items="${fn:split(tripVehicleVO.checkoutPicture, ',') }" varStatus="status">
											<li data-target="#checkoutPictures" data-slide-to="${status.index }" ${(status.index == 0) ? 'class="active"' : '' }></li>
											</c:forEach>
										</ol>
					        			
					        			<!-- 轮播（Carousel）项目 -->
										<div class="carousel-inner" role="listbox">
											<c:forEach var="checkoutPicture" items="${fn:split(tripVehicleVO.checkoutPicture, ',') }" varStatus="status">
											<c:set var="paths" value="${fn:split(checkoutPicture, '/') }" />
					        				<c:set var="index" value="${fn:length(paths) - 1}"/>
					        				<c:set var="imageName" value="${paths[index] }" />
											<div class="item ${(status.index == 0) ? 'active' : '' }">
												<a href="javascript:void(0);">
													<img src="${tripPhotoPathHttp }${checkoutPicture }" alt="${imageName }">
												</a>
											</div>
											</c:forEach>
										</div>
										
										<!-- 轮播（Carousel）导航 -->
										<a class="left carousel-control" href="#checkoutPictures" role="button" data-slide="prev">
											<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
											<span class="sr-only">Previous</span>
										</a>
										<a class="right carousel-control" href="#checkoutPictures" role="button" data-slide="next">
											<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
											<span class="sr-only">Next</span>
										</a>
					        		</div>
				        		</c:if>
						      </div>
							</div>
						</div>
					</form>
				</div>
				<!-- search_form end -->
				
			</div>
			<!-- tab-content end -->
			
		</div>
	</div>
</div>
<script type="text/javascript">
	var root = "${root}";
	var language='${language}';
	var locale='${userLocale}';
	var tripPhotoPathHttp='${tripPhotoPathHttp}';
</script>
<script src="${root}/trip/js/detail.js"></script>
</body>
</html>