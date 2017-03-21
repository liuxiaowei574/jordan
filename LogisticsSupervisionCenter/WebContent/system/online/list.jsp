<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="system.online.title"/></title>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/tripMsgModal.jsp" %>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
    <div class="wrapper-content margint95 margin60">
    	<%--导航 --%>
		<c:set var="pageName"><fmt:message key="system.online.title"/></c:set>
		<jsp:include page="../../include/navigation.jsp" >
			<jsp:param value="${pageName }" name="pageName"/>
		</jsp:include>
		
		<!-- 查看接收人列表-->
		<div class="modal add_user_box" id="onlineUserDetailListModal" tabindex="-1" role="dialog" aria-labelledby="onlineUserDetailListModalTitle">
		  <div class="modal-dialog width1200" role="document">
		    <div class="modal-content">
		    </div>
		  </div>
		</div>
		
		<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message key="system.online.title"/></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
						<div class="form-group col-md-6">
							<label class="col-sm-4 control-label"><fmt:message key="user.userName"/>:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="s_userName"
									name="s_userName">
							</div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-sm-4 control-label"><fmt:message key="user.ipAddress"/>:</label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="s_ipAddress"
									name="s_ipAddress">
							</div>
						</div>
						<div class="clearfix"></div>
						<div class="form-group">
							<div class="col-sm-offset-9 col-md-3">
								<button type="submit" class="btn btn-danger" onclick="search();"><fmt:message key="common.button.query"/></button>
								<button type="button" class="btn btn-darch" onclick="doRest();"><fmt:message key="common.button.reset"/></button>
							</div>
						</div>
					</form>
				</div>
			</div>
			
			<!--/search form-->
			<!--my result-->
			<div class="tab-content">
			  <div class="tab-cotent-title">
				<fmt:message key="system.online.list.title"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="onlineUserListTable"></table>
					</div>
				</div>
			</div>
		</div>
	   </div>
	</div>
	<script type="text/javascript" src="${root}/system/online/js/list.js"></script>
	<script type="text/javascript">
	function doRest(){
		$("#searchForm")[0].reset();
	}
	</script>
</body>
</html>