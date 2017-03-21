<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="system.online.operateLog.title"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="onlineUserDetailListModalTitle"><fmt:message key="system.online.operateLog.title"/></h4>
</div>
<div class="modal-body">
<form id="searchForm">
    <input type="hidden" id="userId" name="userId" value="${param.userId }">
</form>
	<table id="userLogDetailTable"></table>
</div>
<div class="clearfix"></div>
<div class="modal-footer">
	<button type="button" class="btn btn-darch" data-dismiss="modal"><fmt:message key="common.button.cancle"/></button>
</div>
<script type="text/javascript">
	var root = "${root}";
</script>
<script type="text/javascript" src="${root}/system/online/js/detail.js"></script>
</body>
</html>