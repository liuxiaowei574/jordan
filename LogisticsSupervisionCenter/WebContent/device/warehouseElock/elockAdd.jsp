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
			<fmt:message key="elock.addElock" />
		</h4>
	</div>
	<form class="form-horizontal row" id="elockAddForm" method="post">
		<input type="hidden" id="belong" />
		<div class="modal-body">
			<div class="col-md-6">
				<div class="form-group ">
					<label class="col-sm-4 control-label"><em>*</em><fmt:message
							key="WarehouseElock.elockNumber" /></label>
					<div class="col-sm-8">
						<input type="text" id="warehouseElockBO.elockNumber"
							name="warehouseElockBO.elockNumber" value="${param.elockNumber }"
							class="form-control input-sm">
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-4 control-label"><em>*</em><fmt:message
							key="WarehouseElock.belongTo" /></label>
					<div class="col-sm-8">
						<select style="font-size: 10px" id="s_belongTo" name="s_belongTo" 
							class="form-control">
							<option value=""><fmt:message key="please.choose" /></option>
							<c:forEach var="systemDepartmentBO" items="${deptList}">
								<option value='${systemDepartmentBO.organizationId}' 
								    <c:if test="${systemDepartmentBO.organizationId eq orgId ||param.checkinPort==systemDepartmentBO.organizationId}">selected </c:if>>
									 <!-- ${param.checkinPort==systemDepartmentBO.organizationId ? 'selected' : ''}> -->
									${systemDepartmentBO.organizationName}</option>
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
					<label class="col-sm-4 control-label"><em>*</em><fmt:message
							key="WarehouseElock.elockStatus" /></label>
					<div class="col-sm-8">
						<%-- <select style="font-size:10px" id="s_elockStatus" name="s_elockStatus" class="form-control">
								<option  value=""><fmt:message key="please.choose"/></option>
								<option value="0"><fmt:message key="device.Scrap" /></option>
								<option value="1"> ${param.fromTrip=='1' ? 'selected' : '' }<fmt:message key="device.Normal" /></option>
								<option value="3"><fmt:message key="device.Inway" /></option>
								<option value="4"><fmt:message key="device.Maintain" /></option>
						</select> --%>

						<s:select name="s_elockStatus" emptyOption="false"
							cssClass="form-control" theme="simple"
							list="@com.nuctech.ls.model.util.DeviceStatusExceptOnway@values()"
							listKey="text" listValue="key" value="" headerKey=""
							headerValue="">
						</s:select>
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger" id="addElockButton">
				<fmt:message key="common.button.add" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal"
				id="btnCancel">
				<fmt:message key="common.button.cancle" />
			</button>

			<!-- <button id="escortTransferModal" type="button" class="btn btn-darch" onclick="escortHandover()" data-dismiss="modal">测试(护送交接)</button> -->

		</div>
	</form>

	<script type="text/javascript">
		$(function() {
			var a = ${disabled};
			bindElockAddForm();
			//如果来自行程激活页面，默认设置号码状态为正常、只读
			if ("${param.fromTrip }" == '1') {
				$("#s_elockStatus").val(1).attr("disabled", true);
				$("#belong").val($("#s_belongTo").val());
				$("#s_belongTo").attr("disabled", true);
				//取消添加，清空输入值
				$("#btnCancel").on("click", function() {
					$("#tripVehicleVO_trackingDeviceNumber").val('');
				});
			}
			if(a==true){
				$("#s_belongTo").attr("disabled", true);
				$("#belong").val($("#s_belongTo").val());
			}
			 
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
							notEmpty : {},
							stringLength: {
								max: 50
							},
							remote : {
								url : '${root }/warehouseElock/repeate.action',
								message : $.i18n.prop("elock.number.repeat"),
								delay : 2000,
								type : 'post'
							},
							regexp: {
								regexp: /^[\da-zA-Z]+$/,
		                        message: $.i18n.prop('trip.message.input.letterNumber')
							}
						}
					},
					's_belongTo' : {
						validators : {
							notEmpty : {}
						}
					},
					'warehouseElockBO.simCard' : {
						validators : {
							stringLength: {
								max: 100
							}
						}
					},
					'warehouseElockBO.interval' : {
						validators : {
							stringLength: {
								max: 20
							}
						}
					},
					'warehouseElockBO.gatewayAddress' : {
						validators : {
							stringLength: {
								max: 20
							}
						}
					},
					's_elockStatus' : {
						validators : {
							notEmpty : {}
						}
					}
				}
			/* ,
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
				if ("${param.fromTrip }" == '1') {
					$("#s_elockStatus").removeAttr("disabled").val(1);
					$("#s_belongTo").removeAttr("disabled").val($("#belong").val());
				}
				
				if("${disabled}"=="true"){
					$("#s_belongTo").removeAttr("disabled").val($("#belong").val());
				}

				var serialize = $form.serialize();
				var url = '${root }/warehouseElock/addElock.action';
				$.post(url, serialize, function(data) {
					if (!needLogin(data)) {
						if (data) {
							bootbox.alert($.i18n.prop("elock.add.success"));
							//如果来自行程激活页面，则将新号显示到页面
							if ("${param.fromTrip }" == '1') {
								appendElockNums("${param.elockNumber }");
							}
							$('#addElockModal').modal('hide');
							$table.bootstrapTable('refresh', {});
						} else {
							bootbox.error($.i18n.prop("elock.add.fail"));
							$('#addElockModal').modal('hide');
							$table.bootstrapTable('refresh', {});
						}
					}

				}, "json");
			});
		}
	</script>

	<!-- 测试：followup user接到护送巡逻队到达临界区域时，follower user负责将护送任务转给其他巡逻队 -->
	<script type="text/javascript">
		function escortHandover() {
			var url = root + "/escortHandOver/escortHandOverTaskModal.action";
			$('#handOverEscortTask').removeData('bs.modal');
			$('#handOverEscortTask').modal({
				remote : url,
				show : false,
				backdrop : 'static',
				keyboard : false
			});

			$('#handOverEscortTask').on('loaded.bs.modal', function(e) {
				$('#handOverEscortTask').modal('show');
			});
		}
	</script>
	<!-- 控制中心用户添加关锁时有权选择所属口岸-->
	<script type="text/javascript">
		$(function() {
			var a = ${disabled};
			if(a !=true){
				$('#s_belongTo').removeAttr("disabled");
			}
		})
	</script>
</body>
</html>