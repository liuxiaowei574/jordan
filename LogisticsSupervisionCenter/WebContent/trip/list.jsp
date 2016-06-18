<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include/include.jsp" />
<title><fmt:message key="trip.title.tripSearch"/></title>
</head>
<body>
	<div class="profile profile_box02">
		<div class="my_news col-md-12">
        	<div class="title_news">
              <h2><fmt:message key="trip.list.tripSearch"/></h2>
          	</div>
        </div>
        <div class="clearfix"></div>
		<div class="search_form">
			<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message key="trip.label.trackingDeviceNumber"/></label>
					<div class="col-sm-8">
						<input type="text" class="form-control" id="s_trackingDeviceNumber"
							name="s_trackingDeviceNumber">
					</div>
				</div>
				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message key="trip.label.esealNumber"/></label>
					<div class="col-sm-8">
						<input type="text" class="form-control" id="s_esealNumber"
							name="s_esealNumber">
					</div>
				</div>
				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message key="trip.label.sensorNumber"/></label>
					<div class="col-sm-8">
						<input type="text" class="form-control" id="s_sensorNumber"
							name="s_sensorNumber">
					</div>
				</div>
				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message key="trip.label.checkinUser"/></label>
					<div class="col-sm-8">
						<input type="text" class="form-control" id="s_checkinUser" readonly="readonly"
							name="s_checkinUser" value="${sessionUser.userAccount }">
					</div>
				</div>
				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message key="trip.label.checkinPort"/></label>
					<div class="col-sm-8">
						<input type="text" class="form-control" id="s_checkinPort"
							name="s_checkinPort">
					</div>
				</div>
				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message key="trip.label.tripStatus"/></label>
					<div class="col-sm-8">
						<select class="form-control" name="s_tripStatus" id="s_tripStatus">
							<option value=""></option>
				        	<option value="0"><fmt:message key="trip.label.tripStatus.started"/></option>
				        	<option value="1"><fmt:message key="trip.label.tripStatus.finished"/></option>
				    	</select>
					</div>
				</div>
				<div class="clearfix"></div>
				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message key="trip.label.checkinTime"/></label>
					<div class="col-sm-8">
						<input type="text" class="form-control" id="s_checkinTime"
							name="s_checkinTime">
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-9 col-md-3">
						<button type="submit" class="btn btn-danger" onclick="search();"><fmt:message key="common.button.query"/></button>
						<button type="reset" class="btn btn-darch"><fmt:message key="common.button.reset"/></button>
					</div>
				</div>
			</form>
		</div>
		<!--/search form-->
		<!--my result-->
		<div class="row">
			<div class="col-md-12 my_news">
				<div class="title_news">
					<div class="Features pull-right">
						<ul>
							<li><a id="editBtn"><fmt:message key="common.button.edit"/></a></li>
						</ul>
					</div>
					<h2><fmt:message key="trip.list.title"/></h2>
				</div>
				<div class="search_table">
					<div>
						<table id="tripListTable"></table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${root}/trip/js/list.js"></script>
	<script type="text/javascript">
		var root = "${root}";
	</script>
</body>
</html>