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
			<fmt:message key="NoticeType.DispatchNotice" />
		</h4>
	</div>
	
	<form id="dispatchMsgForm" class="form-horizontal row">
		<div class="modal-body">
			<input type="hidden" id="noticeId" name="noticeId">
			<div class="col-md-10">
				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="notice.title" /></label>
					<div class="col-sm-8">
						<input type="text" id="dispatchMsgTitle"
							class="form-control input-sm" readonly="readonly">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="notice.content" /></label>
					<div class="col-sm-8">
						<textarea rows="3" cols="15" id="dispatchMsgContent"
							class="form-control input-sm" readonly="true">
						</textarea>
					</div>
				</div>
			</div>
		</div>
	</form>
	
	<div class="modal-footer">
			<button type="button" onclick="dispatchSure();"
				class="btn btn-danger" data-dismiss="modal">
				<fmt:message key="dispatch.sure" />
			</button>
	</div>
	
	<!-- 控制中心同意调度方案（基础版本，无需再向巡逻队推送消息） -->
	<script type="text/javascript">
		function dispatchSure() {
		 	$.ajax({
				url : "${root}/warehouseDispatchAnalysis/msgsure.action",
				type : "post",
				dataType : "json",
				data : {
				},
				success : function(data) {
					var url = "${root}/dispatch/toList.action";
					window.location.href=url;
				}
			}); 
		}
	</script>
	
</body>
</html>