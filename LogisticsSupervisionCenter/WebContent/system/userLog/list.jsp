<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="link.system.user.log"/></title>
</head>
<body>
	<%--行程请求推送通知页面 --%>
	<%@ include file="../../include/tripMsgModal.jsp" %>
	<%@ include file="../../include/left.jsp" %>
	<div class="row site">
	    <div class="wrapper-content margint95 margin60">
	    	<%--导航 --%>
			<c:set var="pageName"><fmt:message key="link.system.user.log"/></c:set>
			<jsp:include page="../../include/navigation.jsp" >
				<jsp:param value="${pageName }" name="pageName"/>
			</jsp:include>
		
		<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message key="link.system.user.log"/></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="system.user.log.logUser"/></label>
							<div class="col-sm-9">
						   	    <div id="menuContent">
									<ul id="userTree" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
								</div>
					   	      <input type="text" class="form-control input-sm" id="userChecked" name="userChecked" readonly="readonly" onclick="showMenu()">
					   	      <input type="hidden" id="s_logUser" name="s_logUser" />
					   	    </div>
						</div>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="system.user.log.ipAddress"/>:</label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="s_ipAddress"
									name="s_ipAddress">
							</div>
						</div>
						<%--登录时间，起/止 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="system.user.log.logonTime"/></label>
							<div class="input-group date col-sm-4" id="form_logonStartTime">
								<input type="text" class="form-control" id="s_logonStartTime" name="s_logonStartTime" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
							<label class="col-sm-1 control-label" style="text-align: center;">-</label>
							<div class="input-group date col-sm-4" id="form_logonEndTime">
								<input type="text" class="form-control" id="s_logonEndTime" name="s_logonEndTime" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
						</div>
						<%--登出时间，起/止 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="system.user.log.logoutTime"/></label>
							<div class="input-group date col-sm-4" id="form_logoutStartTime">
								<input type="text" class="form-control" id="s_logoutStartTime" name="s_logoutStartTime" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
							<label class="col-sm-1 control-label" style="text-align: center;">-</label>
							<div class="input-group date col-sm-4" id="form_logoutEndTime">
								<input type="text" class="form-control" id="s_logoutEndTime" name="s_logoutEndTime" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
						</div>
						<%--登录系统 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="system.user.log.logonSystem"/>:</label>
							<div class="col-sm-9">
								<s:select name="s_logonSystem"
									theme="simple"
									emptyOption="true"
									cssClass="form-control"
									list="@com.nuctech.util.LoginSystem@values()"
									listKey="desc"
									listValue="key"
									value="%{#request.pageQuery.filters.logonSystem}"
									>
								</s:select>
							</div>
						</div>
						<%--登出类型 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="system.user.log.logoutType"/>:</label>
							<div class="col-sm-9">
								<s:select name="s_logoutType"
									theme="simple"
									emptyOption="true"
									cssClass="form-control"
									list="@com.nuctech.ls.center.utils.LogoutType@values()"
									listKey="value"
									listValue="key"
									value="%{#request.pageQuery.filters.logoutType}"
									>
								</s:select>
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
				<fmt:message key="system.user.log.list"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="userLogListTable"></table>
					</div>
				</div>
			</div>
		</div>
	   </div>
	</div>
	<script type="text/javascript" src="${root}/system/userLog/js/userTree.js"></script>
	<script type="text/javascript" src="${root}/system/userLog/js/list.js"></script>
	<script type="text/javascript">
	function doRest(){
		$("#searchForm")[0].reset();
	}
	</script>
</body>
</html>