<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/include.jsp" />
<title><fmt:message key="trip.finish.title"/></title>
<link rel="stylesheet" href="${root }/static/css/trip.css" />
<style>
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
.carousel-caption {
	overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
ul.nav {
	margin: 0 10px 0px 15px;
}
.suggest-text {
	width: 16.66666667%!important;
	float: left!important;
}
.dropdown-menu table td{
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
		<%--导航 --%>
		<c:set var="pageName"><fmt:message key="trip.finish.title"/></c:set>
		<jsp:include page="../include/navigation.jsp" >
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
		
		<!-- 特殊申请时输入原因Modal -->
		<div class="modal fade add_user_box" id="reasonModal" tabindex="-1"
			role="dialog" aria-labelledby="reasonTitle">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="reasonModalTitle"><fmt:message key="trip.info.inputReason"/></h4>
					</div>
					<div class="modal-body">
						<form id="msgForm" class="form-horizontal row">
							<div class="col-md-10 form-group" id="reasonDiv">
								<label class="col-sm-4 control-label"><fmt:message key="trip.info.reason"/></label>
								<div class="col-sm-8">
									<textarea rows="2" cols="15" id="specialReason" name="specialReason" class="form-control input-sm"></textarea>
								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-danger" data-dismiss="modal" id="nopassSubmit"><fmt:message key="trip.button.submit"/></button>
						<button type="button" class="btn btn-darch" data-dismiss="modal"><fmt:message key="trip.button.cancel"/></button>
					</div>
				</div>
			</div>
		</div>
		<!-- /Modal -->
		
		<div class="profile profile_box02">
			<form class="form-horizontal row" id="searchForm" action="${root }/monitortrip/toFinish.action" method="post" onsubmit="javascript: trimText();">
				<input type="hidden" name="s_tripStatus" id="s_tripStatus" value="1,2">
				<!-- button -->
				<div class="form-group col-md-12">
					<label class="col-sm-1 control-label">
						<fmt:message key="trip.label.declarationNumber"/>
					</label>
					<div class="col-sm-2">
						<input class="form-control" type="text" name="s_declarationNumber" id="s_declarationNumber" value="${param.s_declarationNumber }">
					</div>
					
					<%--
					<button type="button" class="btn btn-danger" id="btnGetDecNum"><fmt:message key="trip.activate.button.load"/></button>
					 --%>
					<label class="col-sm-1 control-label">
						<fmt:message key="trip.label.trackingDeviceNumber"/>
					</label>
					<div class="input-group suggest-text col-sm-2">
						<input class="form-control" type="text" name="s_trackingDeviceNumber" id="s_trackingDeviceNumber" value="${param.s_trackingDeviceNumber }">
						<div class="input-group-btn">
                           <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" style="display: none;">
                               <span class="caret"></span>
                           </button>
                           <ul class="dropdown-menu dropdown-menu-right" role="menu">
                           </ul>
                        </div>
	                </div>
					<%--
					<button type="button" class="btn btn-danger" id="btnGetDeviceNum"><fmt:message key="trip.activate.button.load"/></button>
					 --%>
					<div class="col-sm-3">
						<button type="submit" class="btn btn-danger" id="btnQuery"><fmt:message key="trip.button.query"/></button>
						<button type="button" class="btn btn-danger" id="btnReset"><fmt:message key="trip.button.reset"/></button>
					</div>
					<c:if test="${sessionUser.organizationId == tripVehicleVO.checkoutPort }">
						<c:if test="${!systemModules.isApprovalOn() }">
							<div class="col-sm-3">
								<button type="button" class="btn btn-danger" id="btnFinishDo"><fmt:message key="trip.finish.button.finish"/></button>
							</div>
						</c:if>
					</c:if>
				</div>
			</form>
			
			<form class="form-horizontal row" role="form" id="tripForm" action="" method="post" enctype="multipart/form-data">
			<div class="tab-content m-b">
				<div class="tab-cotent-title"><fmt:message key="trip.report.basic"/></div>
				<div class="search_form">
						<input type="hidden" name="tripVehicleVO.reason" id="tripVehicleVO.reason" value="">
						<input type="hidden" name="fileIndexVehicleNumMap" id="fileIndexVehicleNumMap" value="">
						<input type="hidden" name="photoIndexVehicleNumMap" id="photoIndexVehicleNumMap" value="">
						<input type="hidden" name="tripVehicleVO.tripId" id="tripVehicleVO.tripId" value="${tripVehicleVO.tripId }">
						<input type="hidden" name="tripVehicleVO.declarationNumber" id="tripVehicleVO.declarationNumber" value="${tripVehicleVO.declarationNumber }">
						<input type="hidden" name="tripVehicleVO.specialFlag" id="tripVehicleVO.specialFlag" value="${tripVehicleVO.specialFlag }">
						<%--判断用户是否属于检出口岸 --%>
						<c:if test="${sessionUser.organizationId == tripVehicleVO.checkoutPort }">
							<c:if test="${!systemModules.isApprovalOn() }">
								<button type="button" class="btn btn-danger" id="btnFinish" style="width: 0px;height:0px;display:none;"><fmt:message key="trip.finish.button.finish"/></button>
							</c:if>
							<c:if test="${systemModules.isApprovalOn() }">
								<div class="btn-group" style="position: relative; top: -46px; float: right; right: 180px;">
									<button type="submit" class="btn btn-default dropdown-toggle" data-toggle="dropdown"><fmt:message key="trip.finish.button.finish"/>
								      <span class="caret"></span>
								    </button>
									<ul class="dropdown-menu" id="esealMenu">
										<li><a href="javascript:;" id="btnFinish"><fmt:message key="trip.finish.button.finish"/></a></li>
										<li><a href="javascript:;" id="btnFinishSpecial"><fmt:message key="trip.button.finish.special"/></a></li>
									</ul>
								</div>
							</c:if>
						</c:if>
						
						<div class="form-group">
							<div class="" id="baseInfo">
								<!-- tripId -->
								<label class="col-sm-2 control-label" >
							         <fmt:message key="trip.label.tripId"/>
							    </label>
							    <div class="col-sm-2">
									<p class="form-control-static" style="word-break: break-all;">${tripVehicleVO.tripId }</p>
								</div>
								
								<!-- 报关单号 -->
								<label class="col-sm-2 control-label" >
									<fmt:message key="trip.label.declarationNumber"/>
								</label>
								<div class="col-sm-2">
									<p class="form-control-static">${tripVehicleVO.declarationNumber }</p>
								</div>
								
								<!-- 轨迹 -->
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
								
								<!-- 巡逻队 -->
								<c:if test="${systemModules.isPatrolOn() }">
									<label class="col-sm-2 control-label" >
								         <fmt:message key="foot.title.patrol"/>
								    </label>
								    <div class="col-sm-2">
										<p class="form-control-static">${tripVehicleVO.patrolName }</p>
									</div>
								</c:if>
								
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
								
								<div class="clearfix"></div>
								
							</div>
							<div class="clearfix"></div>
						</div>
					</div>
				</div>
				
				<div class="tab-content m-b">
			    	<div class="tab-cotent-title"><fmt:message key="trip.report.vehicleInfo"/></div>
					<div class="search_table">
						<%--Tab start --%>
					      	<div id="collapseVehicle" class="panel-collapse collapse in">
					      	<ul id="myTab" class="nav nav-tabs">
					      		<c:forEach var="commonVehicleDriver" items="${tripVehicleVO.commonVehicleDriverList }" varStatus="status">
									<li <c:if test="${status.first }">class="active"</c:if> >
										<a href="#${commonVehicleDriver.vehiclePlateNumber}" data-toggle="tab">${commonVehicleDriver.vehiclePlateNumber}</a>
									</li>
								</c:forEach>
							</ul>
							<c:if test="${fn:length(tripVehicleVO.commonVehicleDriverList)>0 }">
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
													<p class="form-control-static">
													<c:forEach var="goodsType" items="${fn:split(commonVehicleDriver.goodsType, ',') }" varStatus="status3">
														<c:if test="${goodsType != '' }">
															<fmt:message key="GoodsType.GOODS_TYPE${goodsType }"/>
															<c:if test="${!status3.last }">,</c:if>
														</c:if>
													</c:forEach>
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
													<p class="form-control-static" name="trackingDeviceNumber">${commonVehicleDriver.trackingDeviceNumber }</p>
												</div>
												
												<%--
												<div class="col-sm-4 button-margin">
													<c:if test="${sessionUser.organizationId == tripVehicleVO.checkoutPort }">
													<button type="button" class="btn btn-danger" name="btnSetLocked"><fmt:message key="trip.button.setLocked"/></button>
													<button type="button" class="btn btn-danger" name="btnSetUnlocked"><fmt:message key="trip.button.setUnlocked"/></button>
													<button type="button" class="btn btn-danger" name="btnClearAlarm"><fmt:message key="trip.button.clearAlarm"/></button>
													</c:if>
												</div>
												 --%>
												
												<!-- 电量 -->
												<c:if test="${systemModules.isAreaOn() }">
													<label class="col-sm-2 control-label">
														<fmt:message key="trip.info.inArea"/>
													</label>
													<div class="col-sm-2">
														<p class="form-control-static">
															<span name="elockInArea" class="glyphicon" style="padding-left: 15px;"></span>
														</p>
													</div>
												</c:if>
												
												 <div class="clearfix"></div>
											</div>
											<div class="form-group"> 
												<!-- 子锁号 -->
												<label class="col-sm-2 control-label">
												 <fmt:message key="trip.label.esealNumber"/>
												</label>
												<div class="col-sm-2">
													<p class="form-control-static">${commonVehicleDriver.esealNumber }</p>
												</div>
												<div class="clearfix"></div>
											</div>
											
											<div class="form-group">
												<!-- 传感器编号 -->
												<label class="col-sm-2 control-label">
													<fmt:message key="trip.label.sensorNumber"/>
												</label>
												<div class="col-sm-2">
													<p class="form-control-static">${commonVehicleDriver.sensorNumber }</p>
												</div>
											</div>
											
											<!-- image -->
											<div class="form-group vehicle-img">
												<label class="col-sm-2 control-label">
													<fmt:message key="trip.label.checkoutPicture"/>
												</label>
												<div class="col-sm-2">
													<div id="${commonVehicleDriver.vehiclePlateNumber }-out" class="carousel slide">
														<!-- 轮播（Carousel）指标 -->
														<ol class="carousel-indicators">
														</ol>   
														<!-- 轮播（Carousel）项目 -->
														<div class="carousel-inner">
														</div>
														<!-- 轮播（Carousel）导航 -->
														<a class="carousel-control left" href="#${commonVehicleDriver.vehiclePlateNumber }-out" 
														   data-slide="prev">&lsaquo;</a>
														<a class="carousel-control right" href="#${commonVehicleDriver.vehiclePlateNumber }-out" 
														   data-slide="next">&rsaquo;</a>
													</div> 
												</div>
												
												<c:if test="${sessionUser.organizationId == tripVehicleVO.checkoutPort }">
													<c:if test="${status.first }">
													<!-- <div class="btn-group col-sm-1 upload" id="photoMenu"> -->
													<div class="col-sm-1 upload" id="photoMenu">
														<button type="button" class="btn btn-danger" id="btnLocal"><fmt:message key="trip.activate.button.uploadImage"/></button>
														<input type="file" name="tripPhotoLocal" data-index='0' style="display: none;" accept=".jpeg,.jpg,.bmp"/>
														<%--
														<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
															<fmt:message key="trip.activate.button.uploadImage" />
															<span class="caret"></span>
														</button>
														<ul class="dropdown-menu" id="photoMenu">
															<li>
																<a href="javascript:;" id="btnLocal"><fmt:message key="trip.activate.button.fromLocal" /></a>
																<input type="file" name="tripPhotoLocal" data-index='0' style="display: none;" accept=".jpeg,.jpg,.bmp" />
															</li>
															<li><a href="javascript:;" id="btnCamera"><fmt:message key="trip.activate.button.fromCamera" /></a></li>
														</ul>
														 --%>
													</div>
													</c:if>
												</c:if>
											</div>
											
										</div>
									</div>
									</c:forEach>
								</div>
							</c:if>
						</div>
						<%--Tab end --%>
					</div>
				</div>
				
				<div class="tab-content m-b">
					<div class="search_table">
						<div class="form-group" style="/* height: 250px; */">
								<div class="col-sm-6" style="/* border-right: #ddd solid 1px; */">
									<div class="tab-cotent-title"><fmt:message key="trip.report.checkinInfo"/></div>
									<div class="col-sm-12" id="checkinInfo">
										<!-- 检入时间 -->
										<label class="col-sm-5 control-label" >
									         <fmt:message key="trip.label.checkinTime"/>
									      </label>
										<div class="col-sm-7 control-label">
											<fmt:formatDate value="${tripVehicleVO.checkinTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
										</div>
										<div class="clearfix"></div>
										
										<!-- 检入地点 -->
										<label class="col-sm-5 control-label" >
									         <fmt:message key="trip.label.checkinPort"/>
									      </label>
										<div class="col-sm-7 control-label">
											${tripVehicleVO.checkinPortName }
										</div>
										<div class="clearfix"></div>
										
										<!-- 检入用户 -->
										<label class="col-sm-5 control-label" >
									         <fmt:message key="trip.label.checkinUser"/>
									      </label>
										<div class="col-sm-7 control-label">
											${tripVehicleVO.checkinUserName }
										</div>
										<div class="clearfix"></div>
										
										<!-- 路线用时(分钟) -->
										<label class="col-sm-5 control-label" >
									         <fmt:message key="main.list.routeArea.routeCost"/>
									      </label>
										<div class="col-sm-7 control-label">
											${tripVehicleVO.routeCost }
										</div>
										<div class="clearfix"></div>
									</div>
									
								</div>
								
								<div class="col-sm-6" style="border-left: #ddd solid 1px;">
									<div class="tab-cotent-title" style="margin-left: -15px;"><fmt:message key="trip.report.checkoutInfo"/></div>
									<div class="col-sm-12" id="checkoutInfo">
										<!--  检出时间 -->
										<label class="col-sm-5 control-label">
									        <fmt:message key="trip.label.checkoutTime"/>
									      </label>
										<div class="col-sm-6 control-label">
											<fmt:formatDate value="${tripVehicleVO.checkoutTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
										</div>
										<div class="clearfix"></div>
										
										<!--  检出地点 -->
										<label class="col-sm-5 control-label" >
									        <fmt:message key="trip.label.checkoutPort"/>
									      </label>
										<div class="col-sm-6 control-label">
											${tripVehicleVO.checkoutPortName }
										</div>
										<div class="clearfix"></div>
										
										<!--  检出用户 -->
										<label class="col-sm-5 control-label" >
									        <fmt:message key="trip.label.checkoutUser"/>
									      </label>
										<div class="col-sm-6 control-label">
											${tripVehicleVO.checkoutUserName }
										</div>
										<div class="clearfix"></div>
										
										<!-- 行程耗时(分钟) -->
										<label class="col-sm-5 control-label" >
									         <fmt:message key="trip.label.timeCost"/>
									      </label>
										<div class="col-sm-6 control-label">
											${tripVehicleVO.timeCost }
										</div>
										<div class="clearfix"></div>
									</div>
									
								</div>
							</div>
						</div>
				</div>
			</form>
			
			<%--alarmList --%>
			<div class="tab-content m-b">
				<div class="tab-cotent-title">
					<fmt:message key="trip.report.alarmInfo"/>
			    </div>
		        <div class="search_table">
					<div class="fixed-table-container" style="padding-bottom: 16px;">
						<table class="table table-hover table-striped" id="alarmTable">
							<thead>
							  <th><div class="th-inner"><fmt:message key="trip.report.serialNumber"/></div></th>
						      <th><div class="th-inner"><fmt:message key="trip.report.alarmTime"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarm.label.vehiclePlateNumber"/></div></th>
						      <th><div class="th-inner"><fmt:message key="trip.report.alarmLongitude"/></div></th>
						      <th><div class="th-inner"><fmt:message key="trip.report.alarmLatitude"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarm.label.alarmTypeName"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarm.label.alarmLevelName"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarm.label.alarmStatus"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarmDeal.label.dealTime"/></div></th>
						      <th><div class="th-inner"><fmt:message key="alarmDeal.label.dealUser"/></div></th>
					        </thead>
					        <tbody>
								<c:forEach var="alarm" items="${alarmList }" varStatus="status">
								<tr>
									<td>${status.count }</td>
							      	<td><fmt:formatDate value="${alarm.alarmTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							      	<td>${alarm.vehiclePlateNumber }</td>
							      	<td>${alarm.alarmLongitude }</td>
							      	<td>${alarm.alarmLatitude }</td>
							      	<td><fmt:message key="AlarmType.${alarm.alarmTypeCode }"/></td>
							      	<td>
							      		<c:choose>
								      		<c:when test="${alarm.alarmLevelCode == '0' }">
								      			<fmt:message key="AlarmLevel.Light"/>
								      		</c:when>
								      		<c:when test="${alarm.alarmLevelCode == '1'}">
								      			<fmt:message key="AlarmLevel.Serious"/>
								      		</c:when>
							      		</c:choose>
							      	</td>
							      	<td>
							      		<c:choose>
								      		<c:when test="${alarm.alarmStatus == '0' }">
								      			<fmt:message key="alarm.label.alarmStatus.notProcessed"/>
								      		</c:when>
								      		<c:when test="${alarm.alarmStatus == '1'}">
								      			<fmt:message key="alarm.label.alarmStatus.processing"/>
								      		</c:when>
								      		<c:when test="${alarm.alarmStatus == '2'}">
								      			<fmt:message key="alarm.label.alarmStatus.processed"/>
								      		</c:when>
							      		</c:choose>
							      	</td>
							      	<td><fmt:formatDate value="${alarm.dealTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							      	<td>${alarm.dealUserName }</td>
							      </tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="${root }/static/js/gumwrapper.js"></script>
<script type="text/javascript" src="${root }/static/js/quagga.min.js"></script>
<script type="text/javascript">
var root = "${root}";
var language='${language}';
var locale='${userLocale}';
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
//		    	alert($.i18n.prop("common.message.form.validator"))
	    }
	});
}
var clientIPAddress = "${clientIPAddress}";
$(function(){
	//initWebSocket("${clientIPAddress}");
});
</script>
<script src="${root}/trip/js/elockUtil.js"></script>
<script src="${root}/trip/js/barcodeLive.js"></script>
<script src="${root }/static/js/ejs.min.js"></script>
<script src="${root }/static/js/bootstrap-suggest.min.js"></script>
<script src="${root}/trip/js/finish.js"></script>
<script type="text/javascript" src="${root}/gis/map.js.jsp"></script>
<script type="text/javascript" src="${root}/static/js/initMap.js"></script>

<%--ejs模板 --%>
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