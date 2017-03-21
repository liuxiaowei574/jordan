<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="slapunish.list.title" /></title>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel">
			<fmt:message key="punish.inform.modify" />
		</h4>
	</div>
	<form class="form-horizontal row" id="punishEditForm" method="post">
		<input type="hidden" class="form-control input-sm"
			id="lsSlaPunishBO.punishId" name="lsSlaPunishBO.punishId"
			value="${lsSlaPunishBO.punishId }"> <input type="hidden"
			class="form-control input-sm" id="lsSlaPunishBO.createUser"
			name="lsSlaPunishBO.createUser" value="${lsSlaPunishBO.createUser}">

		<input type="hidden" class="form-control input-sm"
			id="lsSlaPunishBO.createTime" name="lsSlaPunishBO.createTime"
			value="${lsSlaPunishBO.createTime}"> 
			
			<%-- <input type="hidden"class="form-control input-sm" id="lsSlaPunishBO.slaType"name="lsSlaPunishBO.slaType" value="${lsSlaPunishBO.slaType}"> --%>

		<div class="modal-body">
			<div class="col-md-6">
				<div class="form-group ">
					<label class="col-sm-4 control-label"
						for="lsSlaPunishBO.punishName"><fmt:message
							key="punish.Name" /></label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm"
							id="lsSlaPunishBO.punishName" name="lsSlaPunishBO.punishName"
							value="${lsSlaPunishBO.punishName}">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message key="sla.Type" /></label>
					<div class="col-sm-8">
						<s:select name="lsSlaPunishBO.slaType" emptyOption="true"
							cssClass="form-control" theme="simple"
							list="@com.nuctech.ls.model.util.SLAType@values()" 
							listKey="text"
							listValue="key">
						</s:select>
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-4 control-label"
						for="lsSlaPunishBO.slaContent"><fmt:message
							key="sla.Content" /></label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm"
							id="lsSlaPunishBO.slaContent" name="lsSlaPunishBO.slaContent"
							value="${lsSlaPunishBO.slaContent}">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label" for="lsSlaPunishBO.solveName"><fmt:message
							key="sla.solveName" /></label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm"
							id="lsSlaPunishBO.solveName" name="lsSlaPunishBO.solveName"
							value="${lsSlaPunishBO.solveName}">
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-4 control-label"
						for="lsSlaPunishBO.slaContent"><fmt:message
							key="sla.punishValue" /></label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm"
							id="lsSlaPunishBO.punishValue" name="lsSlaPunishBO.punishValue"
							value="${lsSlaPunishBO.punishValue}">
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger" id="modifyButton">
				<fmt:message key="common.button.save" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
	</form>
	<script type="text/javascript">
		$(function() {
			buildElockEditForm();
		});

		/**
		 * 
		 */
		function buildElockEditForm() {
			//设置验证
			$('#punishEditForm').bootstrapValidator({
				message : $.i18n.prop("common.message.form.validator"),
				fields : {
					'lsSlaPunishBO.punishName' : {
						validators : {
							notEmpty : {}
						}
					},
					'lsSlaPunishBO.slaContent' : {
						validators : {
							notEmpty : {}
						}
					},
					'lsSlaPunishBO.solveName' : {
						validators : {
							notEmpty : {}
						}
					},
					'lsSlaPunishBO.punishValue' : {
						validators : {
							notEmpty : {}
						}
					},
					'lsSlaPunishBO.slaType' : {
						validators : {
							notEmpty : {}
						}
					}
				}
			}).on(
					'success.form.bv',
					function(e) {//bootstrapvalidator 0.5.2用法
						e.preventDefault();
						var $form = $(e.target);
						var bv = $form.data('bootstrapValidator');
						var serialize = $form.serialize();
						var url = '${root }/punish/editPunish.action'
						$.post(url, serialize, function(data) {
							if (!needLogin(data)) {
								if (data) {
									bootbox.success($.i18n
											.prop("punish.modify.success"));
									$('#updatePunishModal').modal('hide');
									$table.bootstrapTable('refresh', {});
								} else {
									bootbox.error($.i18n
											.prop("punish.modify.fail"));
									$('#updatePunishModal').modal('hide');
									$table.bootstrapTable('refresh', {});
								}
							}
						}, "json");
					});
		}
	</script>

</body>
</html>