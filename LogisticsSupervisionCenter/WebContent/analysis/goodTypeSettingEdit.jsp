<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>货物类型风险参数修改</title>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel">
			货物类型风险参数修改
		</h4>
	</div>
	<form class="form-horizontal row" id="elockEditForm" method="post">
		<div class="modal-body">
			<div class="col-md-6">
				<div class="form-group ">
					<div class="col-sm-8">
						<input type="hidden" class="form-control input-sm"
							id="goodType.goodtypeId"
							name="goodType.goodtypeId"
							value="${goodType.goodtypeId}" readonly="readonly">
						<input type="hidden" class="form-control input-sm"
							id="goodType.iSerial"
							name="goodType.iSerial"
							value="${goodType.iSerial}" readonly="readonly">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label"
						for="goodType.iSerial">货物类型编码</label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm"
							id="goodType.iSerial"
							name="goodType.iSerial"
							value="${goodType.iSerial}" readonly="readonly">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label"
						for="goodType.gtypeName">货物类型名称</label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm" readonly="readonly"
							id="goodType.gtypeName"
							name="goodType.gtypeName"
							value="${goodType.gtypeName}">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label"
						for="goodType.lowRiskV">阀值</label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm"
							id="goodType.lowRiskV"
							name="goodType.lowRiskV"
							value="${goodType.lowRiskV}">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label"
						for="goodType.bak">说明</label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm" readonly="readonly"
							id="goodType.bak"
							name="goodType.bak"
							value="${goodType.bak}">
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
			$('#elockEditForm')
					.bootstrapValidator({
						message : $.i18n.prop("common.message.form.validator"),
						fields : {
							'goodType.goodtypeId' : {
								validators : {
									notEmpty : {}
								}
							},
							'goodType.gtypeName' : {
								validators : {
									notEmpty : {}
								}
							},
							'goodType.iSerial' : {
								validators : {
									notEmpty : {}
								}
							},
							'goodType.lowRiskV' : {
								validators : {
									notEmpty : {}
								}
							}
						}
					})
					.on(
							'success.form.bv',
							function(e) {//bootstrapvalidator 0.5.2用法
								e.preventDefault();
								var $form = $(e.target);
								var bv = $form.data('bootstrapValidator');
								var serialize = $form.serialize();
								var url = '${root }/analysis/updateGoodTypeRiskParamters.action'
								$.post(
										url,
										serialize,
										function(data) {
											if (data) {
												bootbox
														.success("修改货物类型参数成功");
												$('#updateElockModal')
														.modal('hide');
												$table.bootstrapTable(
														'refresh', {});
												
											} else {
												bootbox
														.error("修改货物类型参数失败");
												$('#updateElockModal').modal('hide');
												$table.bootstrapTable('refresh', {});
											}
										}, "json");
							});
		}
	</script>
</body>
</html>