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
		<h4 class="modal-title" id="myModalLabel"><fmt:message key="elock.addElock" /></h4>
	</div>
	<form class="form-horizontal row" id="elockAddForm" method="post">
		<div class="col-md-6">
			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message key="WarehouseElock.elockNumber" /></label>
				<div class="col-sm-8">
					<input type="text" id="warehouseElockBO.elockNumber"
						name="warehouseElockBO.elockNumber" value="${param.elockNumber }"
						class="form-control input-sm">
				</div>
			</div>

			<div class="form-group">
				<label class="col-sm-4 control-label"><fmt:message
						key="WarehouseElock.belongTo" /></label>
				<div class="col-sm-8">
						<select style="font-size:10px" id="s_belongTo" name="s_belongTo" class="form-control">
						<option  value=""><fmt:message key="please.choose"/></option>
							<c:forEach var="SystemDepartmentBO" items="${deptList}">
								<option value='${SystemDepartmentBO.organizationId}' ${param.checkinPort==SystemDepartmentBO.organizationId ? 'selected' : ''}>
									${SystemDepartmentBO.organizationName}
								</option>
							</c:forEach>
						</select>
				</div>
			</div>
			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message
						key="WarehouseElock.simCard" /></label>
				<div class="col-sm-8">
					<input type="text" id="warehouseElockBO.simCard"
						name="warehouseElockBO.simCard" class="form-control input-sm">
				</div>
			</div>
			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message
						key="WarehouseElock.interval" /></label>
				<div class="col-sm-8">
					<input type="text" id="warehouseElockBO.interval"
						name="warehouseElockBO.interval" class="form-control input-sm">
				</div>
			</div>

			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message
						key="WarehouseElock.gatewayAddress" /></label>
				<div class="col-sm-8">
					<input type="text" id="warehouseElockBO.gatewayAddress"
						name="warehouseElockBO.gatewayAddress"
						class="form-control input-sm">
				</div>
			</div>
			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message
						key="WarehouseElock.elockStatus" /></label>
					<div class="col-sm-8">
						<select style="font-size:10px" id="s_elockStatus" name="s_elockStatus" class="form-control">
								<option  value=""><fmt:message key="please.choose"/></option>
								<option value="0"><fmt:message key="device.Scrap" /></option>
								<option value="1" ${param.fromTrip=='1' ? 'selected' : '' }><fmt:message key="device.Normal" /></option>
								<option value="3"><fmt:message key="device.Inway" /></option>
								<option value="4"><fmt:message key="device.Maintain" /></option>
						</select>
					</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger" id="addElockButton">
				<fmt:message key="common.button.add" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
	</form>

	<script type="text/javascript">
		$(function() {

			bindElockAddForm();
		});

		/**
		 * 添加关锁表单验证
		 */
		function bindElockAddForm() {
			//设置验证
			$('#elockAddForm').bootstrapValidator({
				message : $.i18n.prop("common.message.form.validator"),
				fields : {
					'warehouseElockBO.elockNumber' : {
						validators : {
							notEmpty : {}
						}
					},
					'warehouseElockBO.belongTo' : {
						validators : {
							notEmpty : {}
						}
					},
					'warehouseElockBO.simCard' : {
						validators : {
							notEmpty : {}
						}
					},
					'warehouseElockBO.interval' : {
						validators : {
							notEmpty : {}
						}
					},
					'warehouseElockBO.gatewayAddress' : {
						validators : {
							notEmpty : {}
						}
					},
					'warehouseElockBO.elockStatus' : {
						validators : {
							notEmpty : {}
						}
					}
				}/* ,
				submitHandler : function(validator, form, submitButton) {
					var serialize = $("#elockAddForm").serialize();
					var url = '${root }/warehouseElock/addElock.action';
					$.post(url, serialize, function(data) {
						if(data==true){
							bootbox.success($.i18n.prop("elock.add.success"));
							 
						};
						$('#table').bootstrapTable('refresh', {});
						$('#addElockModal').modal('hide');
						
					}, "json");
				} */
			}).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
			      e.preventDefault();
			      var $form = $(e.target);
			      var bv = $form.data('bootstrapValidator');
			     
			      var serialize = $form.serialize();
			      var url = '${root }/warehouseElock/addElock.action';
				  $.post(url, serialize, function(data) {
						if(data) {
							bootbox.alert($.i18n.prop("elock.add.success"));
							//如果来自行程激活页面，则将新号显示到页面
				  			if("${param.fromTrip }" == '1') {
								appendElockNums("${param.elockNumber }");
								validateTrackingDeviceNum();
							}
				  			$('#addElockModal').modal('hide');
				  			$table.bootstrapTable('refresh', {});
						} else {
							bootbox.error($.i18n.prop("elock.add.fail"));
							$('#addElockModal').modal('hide');
							$table.bootstrapTable('refresh', {});
						}
						
				  }, "json");
			  });
			}
	</script>
</body>
</html>