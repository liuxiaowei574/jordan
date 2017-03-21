<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="link.system.operate.log"/></title>
</head>
<body>
	<%--行程请求推送通知页面 --%>
	<%@ include file="../../include/tripMsgModal.jsp" %>
	<%@ include file="../../include/left.jsp" %>
	<div class="row site">
	    <div class="wrapper-content margint95 margin60">
	    	<%--导航 --%>
			<c:set var="pageName"><fmt:message key="link.system.operate.log"/></c:set>
			<jsp:include page="../../include/navigation.jsp" >
				<jsp:param value="${pageName }" name="pageName"/>
			</jsp:include>
		
		<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message key="link.system.operate.log"/></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
					    <%--姓名 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="system.operate.log.logUserName"/></label>
							<div class="col-sm-9">
						   	    <div id="menuContent">
									<ul id="userTree" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
								</div>
					   	      <input type="text" class="form-control input-sm" id="userChecked" name="userChecked" readonly="readonly" onclick="showMenu()">
					   	      <input type="hidden" id="s_logUserId" name="s_logUserId" />
					   	    </div>
						</div>
						<%--操作时间，起/止 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="system.operate.log.operateTime"/></label>
							<div class="input-group date col-sm-4" id="form_operateStartTime">
								<input type="text" class="form-control" id="s_operateStartTime" name="s_operateStartTime" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
							<label class="col-sm-1 control-label" style="text-align: center;">-</label>
							<div class="input-group date col-sm-4" id="form_operateEndTime">
								<input type="text" class="form-control" id="s_operateEndTime" name="s_operateEndTime" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
						</div>
						<%--操作内容 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="system.operate.log.operateDesc"/>:</label>
							<div class="col-sm-9">
								<s:select name="s_operateDesc"
									theme="simple"
									emptyOption="true"
									cssClass="form-control"
									list="@com.nuctech.ls.model.util.OperateContentType@values()"
									listKey="desc"
									listValue="key"
									value="%{#request.pageQuery.filters.operateDesc}"
									>
								</s:select>
							</div>
						</div>
						<%--操作对象 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="system.operate.log.operateType"/>:</label>
							<div class="col-sm-9">
								<s:select name="s_operateType"
									theme="simple"
									emptyOption="true"
									cssClass="form-control"
									list="@com.nuctech.ls.model.util.OperateEntityType@values()"
									listKey="desc"
									listValue="key"
									value="%{#request.pageQuery.filters.operateType}"
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
				<fmt:message key="system.operate.log.list"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="operateLogListTable"></table>
					</div>
				</div>
			</div>
		</div>
	   </div>
	</div>
	<script type="text/javascript" src="${root}/system/operateLog/js/userTree.js"></script>
	<script type="text/javascript" src="${root}/system/operateLog/js/list.js"></script>
	<script type="text/javascript">
	function doRest(){
		$("#searchForm")[0].reset();
	}
	</script>
</body>
</html>