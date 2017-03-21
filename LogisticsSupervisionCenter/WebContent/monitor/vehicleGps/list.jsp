<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="system.notice.title" /></title>
<style>
.suggest-text {
	width: 75%!important;
}
.dropdown-menu table td{
	text-align: left;
}
</style>
</head>
<body>
	<%--行程请求推送通知页面 --%>
	<%@ include file="../../include/tripMsgModal.jsp" %>
	<%@ include file="../../include/left.jsp"%>
	<div class="row site">
		<div class="wrapper-content margint95 margin60">
			<%--导航 --%>
			<c:set var="pageName"><fmt:message key="gpslog.gpslog.title"/></c:set>
			<jsp:include page="../../include/navigation.jsp" >
				<jsp:param value="${pageName }" name="pageName"/>
			</jsp:include>

			<div class="profile profile_box02">
				<!--search form-->
				<div class="tab-content m-b">
					<div class="tab-cotent-title">${pageName }</div>
			        
					<div class="search_form">
						<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
							<%--追踪终端号（设备号） --%>
							<div class="form-group col-md-6">
								<label class="col-sm-3 control-label"><em>*</em><fmt:message key="trip.report.label.trackingDeviceNumber"/></label>
								<div class="col-sm-9 input-group suggest-text">
									<input type="text" class="form-control" id="s_trackingDeviceNumber"
										name="s_trackingDeviceNumber">
									<div class="input-group-btn">
			                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" style="display: none;">
			                                <span class="caret"></span>
			                            </button>
			                            <ul class="dropdown-menu dropdown-menu-right" role="menu">
			                            </ul>
			                        </div>
								</div>
							</div>
							
							<%--s_tripId --%>
							<div class="form-group col-md-6">
								<label class="col-sm-3 control-label"><fmt:message key="gpslog.tripId"/></label>
								<div class="col-sm-9">
									<input type="text" class="form-control" id="s_tripId"
										name="s_tripId">
								</div>
							</div>
							
							<div class="form-group col-md-6">
								<label class="col-sm-3 control-label"><fmt:message key="gpslog.locationTime"/></label>
								<div class="input-group date col-sm-4" id="form_locationStartTime">
									<input type="text" class="form-control" id="s_locationStartTime" name="s_locationStartTime" readonly>
									<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
								</div>
								<label class="col-sm-1 control-label" style="text-align: center;">-</label>
								<div class="input-group date col-sm-4" id="form_locationEndTime">
									<input type="text" class="form-control" id="s_locationEndTime" name="s_locationEndTime" readonly>
									<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
								</div>
							</div>
							<%--位置类型 --%>
							<div class="form-group col-md-6">
								<label class="col-sm-3 control-label"><fmt:message key="gpslog.locationType"/></label>
								<div class="col-sm-9">
									<select id="s_locationType" name="s_locationType" class="form-control">
										<option value=""></option>
										<option value="0"><fmt:message key="gpslog.locationType.elock"/></option>
										<option value="1"><fmt:message key="gpslog.locationType.trackUnit"/></option>
									</select>
								</div>
							</div>
							<div class="form-group col-md-6">
								<label class="col-sm-3 control-label"><fmt:message key="gpslog.locationStatus"/></label>
								<div class="col-sm-9">
									<input type="text" class="form-control" id="s_locationStatus"
										name="s_locationStatus">
								</div>
							</div>
							<div class="form-group col-md-6">
								<label class="col-sm-3 control-label"><fmt:message key="gpslog.elockStatus"/></label>
								<div class="col-sm-9">
									<select id="s_elockStatus" name="s_elockStatus" class="form-control">
										<option value=""></option>
										<option value="0"><fmt:message key="gpslog.elockStatus.unbind"/></option>
										<option value="1"><fmt:message key="gpslog.elockStatus.bind"/></option>
									</select>
								</div>
							</div>
							
							<div class="clearfix"></div>
							<div class="form-group">
								<div class="col-sm-offset-9 col-md-3">
									<button type="submit" class="btn btn-danger" onclick="search();"><fmt:message key="common.button.query"/></button>
									<button type="reset" class="btn btn-darch" onclick="doRest();"><fmt:message key="common.button.reset"/></button>
								</div>
							</div>
						</form>
					</div>
				</div>
				<!--search form end-->
				
				
				<!--my result-->
				<div class="tab-content m-b">
					<div class="tab-cotent-title">
						<fmt:message key="gpslog.gpslog.list" />
					</div>
					<div class="search_table">
						<div>
							<table id="gpslogTable"></table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${root }/static/js/bootstrap-suggest.min.js"></script>
	<script type="text/javascript" src="${root }/monitor/vehicleGps/list.js"></script>
</body>
</html>