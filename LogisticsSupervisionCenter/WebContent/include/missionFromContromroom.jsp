<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
</head>
<body>
	<!-- 报警中心向巡逻队推送任务，显示Modal -->
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="noticeModalTitle">
			<fmt:message key="system.notice.tip" />
		</h4>
	</div>
	<form id="dispatchMsgForm" class="form-horizontal row">
		<div class="modal-body">
			<input type="hidden" id="taskId" name="taskId">
			<!--对应websocket.js中的log\\.taskId  -->
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
		<button type="button" onclick="handelMission();"class="btn btn-danger" data-dismiss="modal">
			<fmt:message key="dispatch.aggree.receive" />
		</button>
		<button type="button" class="btn btn-danger" data-dismiss="modal">
			<fmt:message key="dispatch.refuse.receive" />
		</button>
	</div>

	<!-- /Modal -->

	<script type="text/javascript">
		function handelMission() {
			$.ajax({url : root + '/dispatchSendMsg/handelMission.action',
						type : "post",
						dataType : "json",
						data : {
							'taskId' : $("#taskId").val()
						},
						success : function(data) {
							var needLoginFlag = false;
							if (typeof needLogin != 'undefined'
									&& $.isFunction(needLogin)) {
								needLoginFlag = needLogin(data);
							}
							if (!needLoginFlag) {
								if (data) {
									bootbox.success($.i18n.prop('dispatch.Consent.to.port.transport.equipment'));
								} else {
									bootbox.error($.i18n.prop('dispatch.notConsent.to.port.transport.equipment'));
								}
							}
						}
					});
		}
	</script>
	</body>
</html>