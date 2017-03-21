<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="system.notice.edit.title"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="noticeEditModalTitle"><fmt:message key="system.notice.edit.title"/></h4>
</div>
<form class="form-horizontal row" id="noticeEditForm" method="post">
	<input type="hidden" id="notice.noticeId" name="notice.noticeId" value="${notice.noticeId }"/>
	<input type="hidden" id="notice.noticeType" name="notice.noticeType" value="${notice.noticeType}"/>
	<input type="hidden" id="notice.deployTime" name="notice.deployTime" value="${notice.deployTime}"/>
	<div class="modal-body">
	  	<div class="col-md-10">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="notice.noticeTitle"><em>*</em><fmt:message key="notice.title"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="notice.noticeTitle" name="notice.noticeTitle" value="${notice.noticeTitle }">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="notice.noticeUsers"><fmt:message key="notice.user"/></label>
	   	    <div class="col-sm-8">
		   	    <div id="menuContent">
					<ul id="userTree" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
				</div>
	   	      <input type="text" class="form-control input-sm" id="userChecked" name="userChecked" onclick="showMenu()" value="${noticeUsersName }">
	   	      <input type="hidden" id="notice.noticeUsers" name="notice.noticeUsers" value="${notice.noticeUsers }"/> 
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="notice.noticeType"><fmt:message key="notice.type"/></label>
	   	    <div class="col-sm-8">
	   	    	<c:choose>
	   	    		<c:when test="${notice.noticeType == '0' }">
			   	      <input type="text" class="form-control input-sm" value="<fmt:message key="NoticeType.NomalNotice"/>" readonly="readonly">
	   	    		</c:when>
	   	    		<c:when test="${notice.noticeType == '1' }">
			   	      <input type="text" class="form-control input-sm" value="<fmt:message key="NoticeType.AlarmNotice"/>" readonly="readonly">
	   	    		</c:when>
	   	    		<c:when test="${notice.noticeType == '2' }">
			   	      <input type="text" class="form-control input-sm" value="<fmt:message key="NoticeType.TripNotice"/>" readonly="readonly">
	   	    		</c:when>
	   	    		<c:when test="${notice.noticeType == '3' }">
			   	      <input type="text" class="form-control input-sm" value="<fmt:message key="NoticeType.DispatchNotice"/>" readonly="readonly">
	   	    		</c:when>
	   	    		<c:when test="${notice.noticeType == '4' }">
			   	      <input type="text" class="form-control input-sm" value="<fmt:message key="NoticeType.TripEscortNotice"/>" readonly="readonly">
	   	    		</c:when>
	   	    	</c:choose>
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="notice.noticeContent"><em>*</em><fmt:message key="notice.content"/></label>
	   	    <div class="col-sm-8">
	   	    	<textarea rows="10" cols="15" class="form-control input-sm" id="notice.noticeContent" name="notice.noticeContent">${notice.noticeContent }</textarea>
	   	    </div>
	   	  </div>
	  	</div>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
	  <button type="submit" class="btn btn-danger" id="editNoticeButton" ><fmt:message key="common.button.save"/></button>
	  <button type="button" class="btn btn-darch" data-dismiss="modal"><fmt:message key="common.button.cancle"/></button>
	</div>
</form>

<script type="text/javascript">
	var root = "${root}";
</script>
<script type="text/javascript" src="${root}/system/notice/js/newUserTree.js"></script>
<script type="text/javascript" src="${root}/system/notice/js/edit.js"></script>
</body>
</html>