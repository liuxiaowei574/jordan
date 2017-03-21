<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include/include.jsp" />
<title><fmt:message key="trip.title.tripSearch"/></title>
<style>
#tripListTable > tbody > tr > td a {
	color: #00abff;
}
</style>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../include/tripMsgModal.jsp" %>
<%@ include file="../include/left.jsp" %>
<div class="row site">
	<div class="wrapper-content margint95 margin60">
		<div class="profile profile_box02">
			<div class="tab-content m-b">
				<div class="tab-cotent-title"><fmt:message key="trip.list.tripSearch"/></div>
				
				<!--search form-->
				<div class="search_form">
					<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="trip.label.trackingDeviceNumber"/></label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="s_trackingDeviceNumber"
									name="s_trackingDeviceNumber">
							</div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="trip.label.esealNumber"/></label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="s_esealNumber"
									name="s_esealNumber">
							</div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="trip.label.sensorNumber"/></label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="s_sensorNumber"
									name="s_sensorNumber">
							</div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="trip.label.tripStatus"/></label>
							<div class="col-sm-9">
								<select class="form-control" name="s_tripStatus" id="s_tripStatus">
									<option value=""></option>
						        	<option value="0"><fmt:message key="trip.report.label.tripStatus.toStart"/></option>
						        	<option value="1"><fmt:message key="trip.report.label.tripStatus.started"/></option>
						        	<option value="2"><fmt:message key="trip.report.label.tripStatus.toFinish"/></option>
						        	<option value="3"><fmt:message key="trip.report.label.tripStatus.finished"/></option>
						    	</select>
							</div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="trip.label.checkinUser"/></label>
							<div class="col-sm-9">
						   	    <div id="menuContent">
									<ul id="userTree" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
								</div>
					   	      <input type="text" class="form-control input-sm" id="userChecked" name="userChecked" readonly="readonly" onclick="showMenu()">
					   	      <input type="hidden" id="s_checkinUserId" name="s_checkinUserId" />
					   	    </div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="trip.label.checkoutUser"/></label>
							<div class="col-sm-9">
						   	    <div id="menuContent1">
									<ul id="userTree1" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
								</div>
					   	      <input type="text" class="form-control input-sm" id="userChecked1" name="userChecked1" readonly="readonly" onclick="showMenu1()">
					   	      <input type="hidden" id="s_checkoutUserId" name="s_checkoutUserId" />
					   	    </div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="trip.label.checkinPort"/></label>
							<div class="col-sm-9">
								<select class="form-control" name="s_checkinPort" id="s_checkinPort">
						    	</select>
							</div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="trip.label.checkoutPort"/></label>
							<div class="col-sm-9">
								<select class="form-control" name="s_checkoutPort" id="s_checkoutPort">
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
				<!--/search form-->
			</div>
				
			<!--my result-->
			<div class="tab-content m-b">
				<div class="tab-cotent-title">
					<%--buttons --%>
					<%--
					<div class="Features pull-right">
						<ul>
							<li><a id="editBtn" class="btn btn-info"><fmt:message key="common.button.edit"/></a></li>
						</ul>
					</div>
					 --%>
					<%--buttons end--%>
					<fmt:message key="trip.list.title"/>
				</div>
				<div class="search_table">
					<div>
						<table id="tripListTable"></table>
					</div>
				</div>
			</div>
			<!--my result end-->
		</div>
	</div>
</div>
<script type="text/javascript" src="${root}/trip/js/userTree.js"></script>
<script type="text/javascript" src="${root}/trip/js/list.js"></script>
</body>
</html>