<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
</head>
<body>
	<!-- 控制中心主管审批交班任务，显示Modal -->
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="auditModalTitle">
			<fmt:message key="system.notice.tip" />
		</h4>
	</div>
	<form id="auditMsgForm" class="form-horizontal row">
		<div class="modal-body">
			<!-- 交班任务的id -->
			<input type="hidden" id="taskId" name="taskId">
			<!-- 任务接收人的id -->
			<input type="hidden" id="receiverId" name="receiverId">
			<!-- 发起交班的工作人员id -->
			<input type="hidden" id="launchuser" name="launchuser">
			<!--对应websocket.js中的log\\.taskId  -->
			<div class="col-md-10">
				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="notice.title" /></label>
					<div class="col-sm-8">
						<input type="text" id="auditMsgTitle"
							class="form-control input-sm" readonly="readonly">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="notice.content" /></label>
					<div class="col-sm-8">
						<textarea rows="3" cols="15" id="auditMsgContent"
							class="form-control input-sm" readonly="true">
						</textarea>
					</div>
				</div>
			</div>
		</div>
	</form>

	<div class="modal-footer">
		<button type="button" onclick="auditShiftTask();"class="btn btn-danger" data-dismiss="modal">
			<fmt:message key="mission.allow.audit"/>
		</button>
		<button type="button" onclick="notAllowShiftTask()" class="btn btn-danger" data-dismiss="modal">
			<fmt:message key="mission.not.allow.audit"/>
		</button>
	</div>

	<!-- /Modal -->
	<!-- 审批通过之后，变更任务和任务处理表的接收人 -->
	<script type="text/javascript">
		 function auditShiftTask() {
			$.ajax({url : root + '/dispatchSendMsg/auditShitTask.action',
						type : "post",
						dataType : "json",
						data : {
							'taskId' : $("#taskId").val(),
							'receiverId':$("#receiverId").val(),
							'launchuser':$("#launchuser").val()
						},
						success : function(data) {
							
						}
					});
				} 
	</script>
	
	<!-- 主管不同意交班任务 -->
	<script type="text/javascript">
		 function notAllowShiftTask() {
			$.ajax({url : root + '/dispatchSendMsg/NotAllowShitTask.action',
						type : "post",
						dataType : "json",
						data : {
							'receiverId':$("#receiverId").val(),
							'launchuser':$("#launchuser").val()
						},
						success : function(data) {
							
						}
					});
				} 
	</script>
	
	</body>
</html>