<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title>调度申请</title>
</head>
<body>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
  <div class="wrapper-content margint95 margin60">
	<div class="profile profile_box02">
		<div class="tab-content m-b">
			<div class="tab-cotent-title"><fmt:message key="warehouse.device.application.title"/></div>
			<div class="search_form">
				<form class="form-horizontal row" id="searchForm">
					<div class="form-group col-md-6">
						<label class="col-sm-4 control-label"><fmt:message key="warehouse.device.application.portName"/></label>
						<div class="col-sm-8">
							<input type="text" class="form-control" id="s_applcationPortName"
								name="s_applcationPortName">
						</div>
					</div>
					<div class="clearfix"></div>
					<div class="form-group">
						<div class="col-sm-offset-9 col-md-3">
							<button type="button" class="btn btn-danger" onclick="search();"><fmt:message key="common.button.query"/></button>
							<button type="submit" class="btn btn-darch"><fmt:message key="common.button.reset"/></button>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!--/search form-->
		<!--my result-->
		<div class="tab-content">
			 <div class="tab-cotent-title">
			  	<div class="Features pull-right">
					<ul>
						<li><a id="addWarehouseDeviceApplicationButton" class="btn btn-info"><fmt:message key="warehouse.device.application.add.title"/></a></li>
					</ul>
				</div>
				<fmt:message key="warehouse.device.application.list"/>
			  </div>
			  <div class="search_table">
				<div>
					<table id="warehouseDeviceApplicationTable"></table>
				</div>
			  </div>
		</div>
		
		<!-- 通知添加-->
		<div class="modal add_user_box" id="warehouseDeviceApplicationAddModal" tabindex="-1" role="dialog" aria-labelledby="warehouseDeviceApplicationAddModalTitle">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		    </div>
		  </div>
		</div>
	</div>
  </div>
</div>
	<script type="text/javascript" src="${root}/device/application/js/list.js"></script>
	<script type="text/javascript">
		var root = "${root}";
	</script>
</body>
</html>