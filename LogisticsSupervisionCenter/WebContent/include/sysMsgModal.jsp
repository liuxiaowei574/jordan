<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="elock.addElock" /></title>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="noticeModalTitle">
			<fmt:message key="system.notice.tip" />
		</h4>
	</div>
	
	<form id="systemMsgForm" class="form-horizontal row">
		<div class="modal-body">
			<input type="hidden" id="noticeId" name="noticeId">
			<div class="col-md-10">
				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="notice.title" /></label>
					<div class="col-sm-8">
						<input type="text" id="sysmsgTitle" readonly="readonly" name="sysmsgTitle"
							class="form-control input-sm" >
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="notice.content" /></label>
					<div class="col-sm-8">
						<textarea rows="3" cols="15" id="sysmsgContent" readonly="readonly" name="sysmsgContent"
							class="form-control input-sm">
						</textarea>
					</div>
				</div>
				<div class="modal-footer">
				<button type="button" class="btn btn-danger" data-dismiss="modal"
					onclick="msgClose()">
					<fmt:message key="common.button.close" />
				</button>
			</div>
			</div>
		</div>
	</form>
	
	<script type="text/javascript">
	function msgClose() {
			var url = '${root}/notice/noticeRead.action';
			var serialize = $("#systemMsgForm").serialize();
			$.post(url, serialize, function(data) {
				location.reload();
			});
		}
	</script>
</body>
</html>