<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="warehouse.device.application.title"/></title>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/tripMsgModal.jsp" %>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
  <div class="wrapper-content margint95 margin60">
  	<%--导航 --%>
	<c:set var="pageName"><fmt:message key="link.device.dispatch.application"/></c:set>
	<jsp:include page="../../include/navigation.jsp" >
		<jsp:param value="${pageName }" name="pageName"/>
	</jsp:include>
		
	<div class="profile profile_box02">
		<div class="tab-content m-b">
			<div class="tab-cotent-title"><fmt:message key="warehouse.device.application.title"/></div>
			<div class="search_form">
				<form class="form-horizontal row" id="searchForm">
					<input type="hidden" class="form-control input-sm"id="roleName" name="roleName"	value="${roleName}">
					<!-- 申请时间 -->
					<div class="form-group col-md-6">
						<label class="col-sm-3 control-label"><fmt:message key="dispatch.applicate.time"/></label>
						<div class="input-group date col-sm-4" id="applicationStartTime">
							<input type="text" class="form-control" id="s_applyStartTime" name="s_applyStartTime" readonly>
							<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
						</div>
						<label class="col-sm-1 control-label" style="text-align: center;">-</label>
						<div class="input-group date col-sm-4" id="applicationEndTime">
							<input type="text" class="form-control" id="s_applyEndTime" name="s_applyEndTime" readonly>
							<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
						</div>
					</div>
					<!-- 申请人 -->
					<div class="form-group col-sm-6">
						<label for="roleIds" class="col-sm-4 control-label"><fmt:message
								key="warehouse.dispatch.application.user" /></label>
						<div class="col-sm-8">
							<select style="/* font-size:10px */" id="s_applyUser" name="s_applyUser" class="form-control">
							<option  value=""></option>
								<c:forEach var="systemUser" items="${userList}">
									<option value=${systemUser.userId}>${systemUser.userName}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="clearfix"></div>
					<div class="form-group">
						<div class="col-sm-offset-9 col-md-3">
							<button type="button" class="btn btn-danger" onclick="search();"><fmt:message key="common.button.query"/></button>
							<button type="button" class="btn btn-darch" onclick="rest();"><fmt:message key="common.button.reset"/></button>
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