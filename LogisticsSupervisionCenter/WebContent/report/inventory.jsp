<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include/include.jsp" />
<title><fmt:message key="report.inventory.title"/></title>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../include/tripMsgModal.jsp" %>
<%@ include file="../include/left.jsp"%>
<div class="row site">
	<div class="wrapper-content margint95 margin60">
		<%--导航 --%>
		<c:set var="pageName"><fmt:message key="link.chart.inventory"/></c:set>
		<jsp:include page="../include/navigation.jsp" >
			<jsp:param value="${pageName }" name="pageName"/>
		</jsp:include>
		
		<div class="profile profile_box02">
		<div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message key="report.inventory.conditions"/></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="searchForm" action="${root }/inventoryReport/index.action">
						<div class="form-group col-md-6">
							<label class="col-sm-4 control-label"><fmt:message key="report.inventory.mode"/>:</label>
							<div class="col-sm-8">
								<select id="s_mode" name="s_mode" class="form-control" onchange="changeStatisticsMode()">
									<option value=""><fmt:message key="report.inventory.mode.choose"/></option>
									<option value="year" <c:if test="${pageQuery.filters.mode == 'year' }">selected</c:if> ><fmt:message key="report.inventory.mode.year"/></option>
									<option value="month" <c:if test="${pageQuery.filters.mode == 'month' }">selected</c:if> ><fmt:message key="report.inventory.mode.month"/></option>
									<option value="day" <c:if test="${pageQuery.filters.mode == 'day' }">selected</c:if> ><fmt:message key="report.inventory.mode.day"/></option>
								</select>
							</div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label" ><fmt:message key="report.inventory.mode.time"/></label>
							<div class="input-group date col-sm-9" id="form_checkinStartTime">
								<input type="text" class="form-control" id="s_checkinStartTime" name="s_checkinStartTime" value="${pageQuery.filters.checkinStartTime }" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
						</div>
						<c:choose>
			   	    		<c:when test="${sessionU.roleId == '4' || sessionU.roleId == '5'}">
			   	    			<div class="form-group col-sm-6">
									<label for="roleIds" class="col-sm-4 control-label"><fmt:message
											key="WarehouseElock.belongTo" /></label>
									<div class="col-sm-8">
										<select style="/* font-size:10px */" id="s_belongTo" name="s_belongTo" class="form-control">
										<option  value=""></option>
											<c:forEach var="SystemDepartmentBO" items="${deptList}">
												<option value="${SystemDepartmentBO.organizationId}">${SystemDepartmentBO.organizationName}</option>
											</c:forEach>
										</select>
									</div>
						        </div>
			   	    		</c:when>	
			   	    	</c:choose>
						<div class="clearfix"></div>
						<div class="form-group">
							<div class="col-sm-offset-9 col-md-3">
								<%-- <button type="submit" class="btn btn-danger" onclick="searchReportIndex();"><fmt:message key="common.button.query"/></button> --%>
								<button type="button" class="btn btn-danger" onclick="searchReportIndex();"><fmt:message key="common.button.query"/></button>
								<button type="reset" class="btn btn-darch" onclick="doRest();"><fmt:message key="common.button.reset"/></button>
							</div>
						</div>
					</form>
				</div>
			</div>
				<div class="col-sm-4">
					<div class="dashboard-stat blue-madison">
						<div class="visual">
							<i class="fa fa-comments">T</i>
						</div>
						<div class="details">
							<div class="number"><fmt:message key="report.inventory.device.elock"/></div>
							<div class="desc">
								<ul>
									<li>
										<a href="javascript:void(0)" onclick="searchTrackDeviceFlowIn()">
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.flowIn"/> &nbsp;<span class="redfont" id="trackDeviceFlowIn"></span>
										</a>
									</li>
									<li>
										<a href="javascript:void(0)" onclick="searchTrackDeviceFlowOut()">
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.flowOut"/> &nbsp;<span class="greenfont" id="trackDeviceFlowOut"></span>
										</a>
									</li>
									<div class="clearfix"></div>
									<li>
										<a href="javascript:void(0)" onclick="searchTrackDeviceTurnIn()"> 
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.turnIn"/> &nbsp;<span class="redfont" id="trackDeviceTurnIn"></span>
										</a>
									</li>
									<li>
										<a href="javascript:void(0)" onclick="searchTrackDeviceTurnOut()">
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.turnOut"/> &nbsp;<span class="greenfont" id="trackDeviceTurnOut"></span>
										</a>
									</li>
									<div class="clearfix"></div>
									<li><!-- 库存 -->
										<a href="javascript:void(0)" onclick="searchTrackDeviceInventory()">
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.inventory"/> &nbsp;<span class="redfont" id="trackDeviceInventory"></span>
										</a>
									</li>
								</ul>
								<div class="clearfix"></div>
							</div>
						</div>
						<a class="more" href="javascript:;"> </a>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="dashboard-stat green-haze">
						<div class="visual">
							<i class="fa fa-comments">E</i>
						</div>
						<div class="details">
							<div class="number"><fmt:message key="report.inventory.device.eseal"/></div>
							<div class="desc">
								<ul>
									<li>
										<a href="javascript:void(0)" onclick="searchEsealFlowIn()">
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.flowIn"/> &nbsp;<span class="redfont" id="esealFlowIn"></span>
										</a>
									</li>
									<li>
										<a href="javascript:void(0)" onclick="searchEsealFlowOut()">
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.flowOut"/> &nbsp;<span class="greenfont" id="esealFlowOut"></span>
										</a>
									</li>
									<div class="clearfix"></div>
									<li>
										<a href="javascript:void(0)" onclick="searchEsealTurnIn()"> 
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.turnIn"/> &nbsp;<span class="redfont" id="esealTurnIn"></span>
										</a>
									</li>
									<li>
										<a href="javascript:void(0)" onclick="searchEsealTurnOut()">
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.turnOut"/> &nbsp;<span class="greenfont" id="esealTurnOut"></span>
										</a>
									</li>
									<div class="clearfix"></div>
									<li><!-- 库存 -->
										<a href="javascript:void(0)" onclick="searchEsealInventory()">
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.inventory"/> &nbsp;<span class="redfont" id="esealInventory"></span>
										</a>
									</li>
								</ul>
								<div class="clearfix"></div>
							</div>
						</div>
						<a class="more" href="javascript:;"> </a>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="dashboard-stat purple-plum">
						<div class="visual">
							<i class="fa fa-comments">S</i>
						</div>
						<div class="details">
							<div class="number"><fmt:message key="report.inventory.device.sensor"/></div>
							<div class="desc">
								<ul>
									<li>
										<a href="javascript:void(0)" onclick="searchSensorFlowIn()">
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.flowIn"/> &nbsp;<span class="redfont" id="sensorFlowIn"></span>
										</a>
									</li>
									<li>
										<a href="javascript:void(0)" onclick="searchSensorFlowOut()">
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.flowOut"/> &nbsp;<span class="greenfont" id="sensorFlowOut"></span>
										</a>
									</li>
									<div class="clearfix"></div>
									<li>
										<a href="javascript:void(0)" onclick="searchSensorTurnIn()"> 
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.turnIn"/> &nbsp;<span class="redfont" id="sensorTurnIn"></span>
										</a>
									</li>
									<li>
										<a href="javascript:void(0)" onclick="searchSensorTurnOut()">
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.turnOut"/> &nbsp;<span class="greenfont" id="sensorTurnOut"></span>
										</a>
									</li>
									<div class="clearfix"></div>
									<li><!-- 库存 -->
										<a href="javascript:void(0)" onclick="searchSensorInventory()">
											<i class="icon iconfont"></i> <fmt:message key="report.inventory.inventory"/> &nbsp;<span class="redfont" id="sensorInventory"></span>
										</a>
									</li>
								</ul>
								<div class="clearfix"></div>
							</div>
						</div>
						<a class="more" href="javascript:;"> </a>
					</div>
				</div>
				<div class="clearfix"></div>			
				<!-- 详细列表信息 -->
				<div class="tab-content">
				  <div class="tab-cotent-title" id="deviceListTable">
					<fmt:message key="report.inventory.detail.list"/>
				  </div>
				  	<div class="search_table">
						<div>
							<table id="inventoryListTable"></table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var root = "${root}";
		var s_mode = "${pageQuery.filters.mode }";
		var s_checkinStartTime = "${pageQuery.filters.checkinStartTime }";
	</script>
	<script type="text/javascript" src="${root}/report/js/inventory.js"></script>
</body>
</html>