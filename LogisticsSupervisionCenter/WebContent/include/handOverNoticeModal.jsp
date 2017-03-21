<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="system.notice.add.title"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="noticeAddModalTitle"><fmt:message key="warehouse.dispatch.patrol.shift.notice"/></h4>
</div>
<form class="form-horizontal row" id="noticeAddForm" method="post">
	<div class="modal-body">
	  	<div class="col-md-10">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="notice.noticeTitle"><fmt:message key="notice.title"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="notice.noticeTitle" name="notice.noticeTitle">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="notice.noticeUsers"><fmt:message key="notice.user"/></label>
	   	    <div class="col-sm-8">
		   	    <div id="menuContent">
					<ul id="userTree" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
				</div>
	   	      <input type="text" class="form-control input-sm" id="userChecked" name="userChecked" onclick="showMenu()">
	   	      <input type="hidden" id="notice.noticeUsers" name="notice.noticeUsers" />
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="notice.noticeContent"><fmt:message key="notice.content"/></label>
	   	    <div class="col-sm-8">
	   	    	<textarea maxlength="50" rows="10" cols="15" class="form-control input-sm" id="notice.noticeContent" name="notice.noticeContent"></textarea>
	   	    </div>
	   	  </div>
	  	</div>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
	  <button type="submit" class="btn btn-danger" id="addNoticeButton" ><fmt:message key="common.button.save"/></button>
	  <button type="button" class="btn btn-darch" data-dismiss="modal"><fmt:message key="common.button.cancle"/></button>
	</div>
</form>

<script type="text/javascript">
	var root = "${root}";
</script>

<script type="text/javascript" src="${root}/system/notice/js/patrolRoleTree.js"></script>
<script type="text/javascript" src="${root}/include/js/noticeToPatrol.js"></script>

</body>
</html>