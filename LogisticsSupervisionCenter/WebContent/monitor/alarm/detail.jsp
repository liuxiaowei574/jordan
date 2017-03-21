<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="alarm.report.title"/></title>
<link rel="stylesheet" href="${root }/static/css/trip.css" />
<style>
.carousel-caption {
	overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
ul.nav {
	margin: 0 10px 0px 15px;
}
</style>
</head>
<body>
<c:set var="pageName"><fmt:message key="dispatch.detail"/></c:set>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/tripMsgModal.jsp" %>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
	<div class="wrapper-content margint95 margin60">
		<%--导航 --%>
		<c:set var="parentName"><fmt:message key="alarm.list.search"/></c:set>
		<c:set var="pageName"><fmt:message key="trip.finish.title"/></c:set>
		<jsp:include page="../../include/navigation2.jsp" >
			<jsp:param value="${root }/monitoralarm/toList.action" name="parentUrl"/>
			<jsp:param value="${parentName }" name="parentName"/>
			<jsp:param value="${pageName }" name="pageName"/>
		</jsp:include>
		
		<!-- 查看图像的模态窗口 -->
		<div class="modal fade" id="imageModal" tabindex="-1" role="dialog" aria-hidden="true">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		    </div>
		  </div>
		</div>
		
		<!-- 查看地图轨迹的模态窗口-->
		<div class="modal add_user_box" id="routeModal" tabindex="-1" role="dialog" aria-labelledby="routeModalTitle">
		  <div class="modal-dialog " role="document">
		    <div class="modal-content">
		    </div>
		  </div>
		</div>
		
		<div class="profile profile_box02">
			<%--alarmList --%>
			<div class="tab-content m-b" style="padding-right:0;">
				<div class="tab-cotent-title">
					<fmt:message key="alarmDeal.label.alarmDealInfo"/>
			    </div>
		        <div class="search_table">
					<div class="fixed-table-container" style="padding-bottom: 16px;">
						<table class="table table-hover table-striped">
							<thead>
							  <th><div class="th-inner"><fmt:message key="trip.report.serialNumber"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarmDeal.label.recipientsUser"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarmDeal.label.receiveTime"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarmDeal.label.dealResult"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarmDeal.label.dealUser"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarmDeal.label.dealTime"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarmDeal.label.dealMethod"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarmDeal.label.dealDesc"/></div></th>
					        </thead>
					        <tbody>
								<c:forEach var="alarmDeal" items="${alarmDealList }" varStatus="status">
								<tr>
									<td>${status.count }</td>
							      	<td>${alarmDeal.recipientsUserName }</td>
							      	<td><fmt:formatDate value="${alarmDeal.receiveTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							      	<td>
							      		<c:choose>
								      		<c:when test="${alarmDeal.dealResult == '0' }">
							      				<fmt:message key="AlarmDealType.Undeal"/>
							      			</c:when>
								      		<c:when test="${alarmDeal.dealResult == '1'}">
								      			<fmt:message key="AlarmDealType.Dealing"/>
								      		</c:when>
								      		<c:when test="${alarmDeal.dealResult == '2'}">
								      			<fmt:message key="AlarmDealType.Dealt"/>
								      		</c:when>
							      		</c:choose>
							      	</td>
							      	<td>${alarmDeal.dealUserName }</td>
							      	<td><fmt:formatDate value="${alarmDeal.dealTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							      	<td>
							      		<c:choose>
								      		<c:when test="${alarmDeal.dealMethod == '0' }">
								      			<fmt:message key="AlarmDealMethod.FailAlarm"/>
								      		</c:when>
								      		<c:when test="${alarmDeal.dealMethod == '1'}">
								      			<fmt:message key="AlarmDealMethod.NormalDeal"/>
							      			</c:when>
							      			<c:when test="${alarmDeal.dealMethod == '2'}">
								      			<fmt:message key="AlarmDealMethod.Forward"/>
							      			</c:when>
							      			<c:when test="${alarmDeal.dealMethod == '3'}">
								      			<fmt:message key="AlarmDealMethod.Revoke"/>
							      			</c:when>
						      			</c:choose>
							      	</td>
							      	<td>${alarmDeal.dealDesc }</td>
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
							<div class="col-sm-12" id="baseInfo">
								<!-- tripId -->
								<label class="col-sm-2 control-label" >
							         <fmt:message key="trip.label.tripId"/>
							    </label>
							    <div class="col-sm-2">
									<p class="form-control-static" style="word-break: break-all;">${tripVehicleVO.tripId }</p>
								</div>
							
								<!-- 报关单号 -->
								<label class="col-sm-2 control-label">
									<fmt:message key="trip.label.declarationNumber"/>
								</label>
								<div class="col-sm-2">
									<p class="form-control-static">${tripVehicleVO.declarationNumber }</p>
								</div>
								
								<!-- 轨迹路线 -->
								<label class="col-sm-2 control-label">
							         <fmt:message key="trip.label.routeId"/>
							    </label>
						      	 <div class="col-sm-2">
									<p class="form-control-static">
										<a id="route" style="cursor: pointer;" data-routeid="${tripVehicleVO.routeId }" data-tripid="${tripVehicleVO.tripId }">
											${tripVehicleVO.routeName }
										</a>
									</p>
								</div>
								
								<div class="clearfix"></div>
								
								<%--
								<!-- 巡逻队 -->
								<label class="col-sm-1 control-label" >
							         <fmt:message key="foot.title.patrol"/>
							    </label>
							    <div class="col-sm-3">
									<p class="form-control-static" style="/* white-space: nowrap; */">${tripVehicleVO.patrolName }</p>
								</div>
								 --%>
								
								<!-- targetZoon -->
								<c:if test="${systemModules.isAreaOn() }">
									<label class="col-sm-2 control-label" >
										<fmt:message key="main.list.routeArea.routeAreaType.option.targetZoon"/>
									</label>
									<div class="col-sm-2">
										<p class="form-control-static">${tripVehicleVO.targetZoonName }</p>
									</div>
								</c:if>
								
								<!-- 行程状态 -->
								<label class="col-sm-2 control-label" >
							         <fmt:message key="trip.report.label.tripStatus"/>
							    </label>
							    <div class="col-sm-2">
									<p class="form-control-static">
										<c:if test="${tripVehicleVO.tripStatus == '0' }"><fmt:message key="trip.report.label.tripStatus.toStart"/></c:if>
										<c:if test="${tripVehicleVO.tripStatus == '1' }"><fmt:message key="trip.report.label.tripStatus.started"/></c:if>
										<c:if test="${tripVehicleVO.tripStatus == '2' }"><fmt:message key="trip.report.label.tripStatus.toFinish"/></c:if>
										<c:if test="${tripVehicleVO.tripStatus == '3' }"><fmt:message key="trip.report.label.tripStatus.finished"/></c:if>
									</p>
								</div>
								
								<c:if test="${systemModules.isApprovalOn() }">
									<!-- 是否特殊申请 -->
									<label class="col-sm-2 control-label" >
								         <fmt:message key="trip.finish.title.special"/>
								    </label>
								    <div class="col-sm-2">
										<p class="form-control-static">
											<c:if test="${tripVehicleVO.specialFlag == '1' }"><fmt:message key="user.canDealAlarm.yes"/></c:if>
											<c:if test="${tripVehicleVO.specialFlag == '0' }"><fmt:message key="user.canDealAlarm.no"/></c:if>
										</p>
									</div>
									
									<!-- 特殊申请理由 -->
									<label class="col-sm-2 control-label" >
								         <fmt:message key="trip.finish.title.special.reason"/>
								    </label>
								    <div class="col-sm-2">
										<p class="form-control-static">${tripVehicleVO.reason }</p>
									</div>
								</c:if>
								
								
								<div class="clearfix"></div>
							</div>
							<div class="clearfix"></div>
						</div>
						
						<%--Tab start --%>
						<div class="panel panel-info" style="margin-bottom: 0px;">
					    	<div class="tab-cotent-title">
				               	<fmt:message key="trip.report.vehicleInfo"/>
					        </div>
					      <div id="collapseVehicle" class="panel-collapse collapse in">
					      	<ul id="myTab" class="nav nav-tabs">
					      		<c:forEach var="commonVehicleDriver" items="${tripVehicleVO.commonVehicleDriverList }" varStatus="status">
									<li <c:if test="${status.first }">class="active"</c:if> >
										<a href="#${commonVehicleDriver.vehiclePlateNumber}" data-toggle="tab">${commonVehicleDriver.vehiclePlateNumber}</a>
									</li>
								</c:forEach>
							</ul>
							<div id="myTabContent" class="tab-content">
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
											
											<c:if test="${systemModules.isRiskOn() }">
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
											</c:if>
											
											<label class="col-sm-2 control-label">
											 <fmt:message key="trip.label.goodsType"/>
											</label>
											<div class="col-sm-2">
												<p class="form-control-static" style="/* white-space: nowrap; overflow: hidden; text-overflow: ellipsis; */" title="${commonVehicleDriver.goodsTypeName }">
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
						</div>
						<%--Tab end --%>
						
						<hr/>
							
						<div class="form-group">
							<div class="col-sm-6" style="border-right: #ddd solid 1px;">
							  <div class="tab-cotent-title"><fmt:message key="trip.report.checkinInfo"/></div>
							  	<div class="col-sm-12" id="checkinInfo">
									<!-- 检入时间 -->
									<label class="col-sm-5 control-label">
								         <fmt:message key="trip.label.checkinTime"/>
								      </label>
									<div class="col-sm-4 control-label">
										<fmt:formatDate value="${tripVehicleVO.checkinTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
									</div>
									<div class="clearfix"></div>
									
									<!--  检入地点 -->
									<label class="col-sm-5 control-label">
								        <fmt:message key="trip.label.checkinPort"/>
								      </label>
									<div class="col-sm-4 control-label">
										${tripVehicleVO.checkinPortName }
									</div>
									<div class="clearfix"></div>
									
									<!--  检入用户 -->
									<label class="col-sm-5 control-label">
								        <fmt:message key="trip.label.checkinUser"/>
								      </label>
									<div class="col-sm-4 control-label">
										${tripVehicleVO.checkinUserName }
									</div>
									<div class="clearfix"></div>
									
									<!-- 路线用时(分钟) -->
									<label class="col-sm-5 control-label" >
								         <fmt:message key="main.list.routeArea.routeCost"/>
								      </label>
									<div class="col-sm-4 control-label">
										${tripVehicleVO.routeCost }
									</div>
									<div class="clearfix"></div>
									
								</div>
							</div>
							
							<div class="col-sm-6">
								<div class="tab-cotent-title"><fmt:message key="trip.report.checkoutInfo"/></div>
								<div class="col-sm-12" id="checkoutInfo">
									<!-- 检出时间 -->
									<label class="col-sm-5 control-label">
								         <fmt:message key="trip.label.checkoutTime"/>
								      </label>
									<div class="col-sm-4 control-label">
										<fmt:formatDate value="${tripVehicleVO.checkoutTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
									</div>
									<div class="clearfix"></div>
									
									<!--  检出地点 -->
									<label class="col-sm-5 control-label">
								        <fmt:message key="trip.label.checkoutPort"/>
								      </label>
									<div class="col-sm-4 control-label">
										${tripVehicleVO.checkoutPortName }
									</div>
									<div class="clearfix"></div>
									
									<!--  检出用户 -->
									<label class="col-sm-5 control-label">
								        <fmt:message key="trip.label.checkoutUser"/>
								      </label>
									<div class="col-sm-4 control-label">
										${tripVehicleVO.checkoutUserName }
									</div>
									<div class="clearfix"></div>
									
									<!-- 行程耗时(分钟) -->
									<label class="col-sm-5 control-label">
								         <fmt:message key="trip.label.timeCost"/>
								      </label>
									<div class="col-sm-4 control-label">
										${tripVehicleVO.timeCost }
									</div>
									<div class="clearfix"></div>
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
</script>
<script src="${root}/monitor/alarm/js/detail.js"></script>
<script type="text/javascript" src="${root}/gis/map.js.jsp"></script>
<script type="text/javascript" src="${root}/static/js/initMap.js"></script>
</body>
</html>