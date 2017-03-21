<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="alarm.list.search"/></title>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/tripMsgModal.jsp" %>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
	<div class="wrapper-content margint95 margin60">
		<%--导航 --%>
		<c:set var="pageName"><fmt:message key="alarm.list.search"/></c:set>
		<jsp:include page="../../include/navigation.jsp" >
			<jsp:param value="${pageName }" name="pageName"/>
		</jsp:include>
		
		<div class="profile profile_box02">
		
			<div class="tab-content m-b">
				<div class="tab-cotent-title">${pageName }</div>
				
				<!--search form-->
				<div class="search_form">
					<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
						<%--车牌号 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="alarm.label.vehiclePlateNumber"/></label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="s_vehiclePlateNumber"
									name="s_vehiclePlateNumber">
							</div>
						</div>
						<%--报关单号 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="alarm.label.declarationNumber"/></label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="s_declarationNumber"
									name="s_declarationNumber">
							</div>
						</div>
						<%--追踪终端号（设备号） --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="alarm.label.trackingDeviceNumber"/></label>
							<div class="col-sm-9">
								<input type="text" class="form-control" id="s_trackingDeviceNumber"
									name="s_trackingDeviceNumber">
							</div>
						</div>
						<%--报警时间，起/止 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="alarm.label.alarmTime"/></label>
							<div class="input-group date col-sm-4" id="form_alarmStartTime">
								<input type="text" class="form-control" id="s_alarmStartTime" name="s_alarmStartTime" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
							<label class="col-sm-1 control-label" style="text-align: center;">-</label>
							<div class="input-group date col-sm-4" id="form_alarmEndTime">
								<input type="text" class="form-control" id="s_alarmEndTime" name="s_alarmEndTime" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
						</div>
						<%--报警负责人 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="alarm.label.userName"/></label>
							<div class="col-sm-9">
						   	    <div id="menuContent">
									<ul id="userTree" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
								</div>
					   	      <input type="text" class="form-control input-sm" id="userChecked" name="userChecked" readonly="readonly" onclick="showMenu()">
					   	      <input type="hidden" id="s_userId" name="s_userId" />
					   	    </div>
						</div>
						<%--报警状态 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="alarm.label.alarmStatus"/></label>
							<div class="col-sm-9">
								<s:select name="s_alarmStatus"
									theme="simple"
									emptyOption="true"
									cssClass="form-control"
									list="@com.nuctech.ls.model.util.AlarmDealType@values()"
									listKey="text"
									listValue="key"
									value="%{#request.pageQuery.filters.alarmStatus}"
									>
								</s:select>
							</div>
						</div>
						<%--报警级别 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="alarm.label.alarmLevelName"/></label>
							<div class="col-sm-9">
								<s:select name="s_alarmLevel"
									emptyOption="true"
									theme="simple"
									cssClass="form-control"
									list="@com.nuctech.ls.model.util.AlarmLevel@values()"
									listKey="text"
									listValue="key"
									value="%{#request.pageQuery.filters.alarmLevel}"
									>
								</s:select>
							</div>
						</div>
						<%--报警类型 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-3 control-label"><fmt:message key="alarm.label.alarmTypeName"/></label>
							<div class="col-sm-9">
								<s:select name="s_alarmType"
									emptyOption="true"
									cssClass="form-control"
									theme="simple"
									list="@com.nuctech.ls.model.util.AlarmType@values()"
									listKey="alarmType"
									listValue="key"
									value="%{#request.pageQuery.filters.alarmType}"
									>
								</s:select>
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
				<!--search form end-->
			</div>
			
			<!--my result-->
			<div class="tab-content m-b">
				<div class="tab-cotent-title">
					<%--buttons --%>
					<div class="Features pull-right">
						<ul>
							<li><a id="exportBtn" onclick="exportExcel();" class="btn btn-info"><fmt:message key="common.button.exportExcel"/></a></li>
						</ul>
					</div>
					<%--buttons end--%>
					<fmt:message key="alarm.list.title"/>
				</div>
				<div class="search_table">
					<div>
						<table id="alarmListTable"></table>
					</div>
				</div>
			</div>
			<!--my result end-->
			
		</div>
	</div>
</div>
<script type="text/javascript">
	var root = "${root}";
</script>

<!-- 将表格导出到excel文件 -->
<script type="text/javascript">
function exportExcel(){
	var ajaxUrl = "${root}/monitoralarm/exportExcel.action"; 
/* 	$.ajax({
		url : ajaxUrl,
		type : "post",
		dataType : "json",
		success : function(data) {
		}
	}); */
	window.location.href=ajaxUrl;
}
</script>

<script type="text/javascript" src="${root}/monitor/alarm/js/userTree.js"></script>
<script type="text/javascript" src="${root}/monitor/alarm/js/list.js"></script>
</body>
</html>