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
		<h4 class="modal-title" id="myModalLabel">
			<fmt:message key="punish.inform.register"/>
		</h4>
	</div>
	<form class="form-horizontal row" id="punishAddForm" method="post">
		<div class="modal-body">	
		<div class="col-md-6">
			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message key="punish.Name"/></label>
				<div class="col-sm-8">
					<input type="text" id="punishName"name="punishName" value=""class="form-control input-sm">
				</div>
			</div>


			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message key="sla.Type"/></label>
				<div class="col-sm-8">
					<s:select name="slaType" emptyOption="true"
						cssClass="form-control" theme="simple"
						list="@com.nuctech.ls.model.util.SLAType@values()"
						listKey="text"
						listValue="key"
						value="">
					</s:select>
				</div>
			</div>

			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message key="sla.Content"/></label>
				<div class="col-sm-8">
					<input type="text" id="slaContent"name="slaContent" class="form-control input-sm">
				</div>
			</div>
		</div>
	</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger" id="addPunishButton">
				<fmt:message key="common.button.add" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
	</form>

	<script type="text/javascript">
		$(function() {
			bindPunishAddForm();
		});

		/**
		 * 添加关锁表单验证
		 */
		function bindPunishAddForm() {
			//设置验证
			$('#punishAddForm').bootstrapValidator({
				message : $.i18n.prop("common.message.form.validator"),
				fields : {
					'punishName' : {
						validators : {
							notEmpty : {}
						}
					},
					'slaType' : {
						validators : {
							notEmpty : {}
						}
					},
					'slaContent' : {
						validators : {
							notEmpty : {}
						}
					}
				}
			}).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
				e.preventDefault();
				var $form = $(e.target);
				var bv = $form.data('bootstrapValidator');
				var serialize = $form.serialize();
				var url = '${root }/punish/addPunish.action';
				$.post(url, serialize, function(data) {
					if(!needLogin(data)) {
						if (data) {
							bootbox.alert($.i18n.prop("sla.add.success"));
							$('#addPunishModal').modal('hide');
							$table.bootstrapTable('refresh', {});
						} else {
							bootbox.error($.i18n.prop("sla.add.fail"));
							$('#addPunishModal').modal('hide');
							$table.bootstrapTable('refresh', {});
						}
					}

				}, "json");
			});
		}
	</script>
</body>
</html>