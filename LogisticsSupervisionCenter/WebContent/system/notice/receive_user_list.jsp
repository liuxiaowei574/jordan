<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="system.notice.log.list.title"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="receiveUserListModalTitle"><fmt:message key="system.notice.log.list.title"/></h4>
</div>
<div class="modal-body">
<form id="searchForm1">
    <input type="hidden" id="receiveNoticeId" name="receiveNoticeId" value="${notice.noticeId }">
</form>

    
	<table id="receiveUsersTable"></table>
</div>
<div class="clearfix"></div>
<div class="modal-footer">
	<button type="button" class="btn btn-darch" data-dismiss="modal"><fmt:message key="common.button.cancle"/></button>
</div>
<script type="text/javascript">
	var root = "${root}";
</script>
<script type="text/javascript" src="${root}/system/notice/js/receive_user_list.js"></script>
</body>
</html>