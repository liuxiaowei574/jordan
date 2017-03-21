<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
</head>
<body>
	<!-- 控制中心主管审批不通过交班请求，通知交班申请人。显示Modal -->
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
			<div class="col-md-10">
				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="notice.title" /></label>
					<div class="col-sm-8">
						<input type="text" id="notapprovalMsgTitle"
							class="form-control input-sm" readonly="readonly">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="notice.content" /></label>
					<div class="col-sm-8">
						<textarea rows="3" cols="15" id="notapprovalMsgContent"
							class="form-control input-sm" readonly="true">
						</textarea>
					</div>
				</div>
			</div>
		</div>
	</form>

	<div class="modal-footer">
		<button type="button" onclick="auditShiftTask();"class="btn btn-danger" data-dismiss="modal">
			<fmt:message key="common.button.sure"/>
		</button>
	</div>