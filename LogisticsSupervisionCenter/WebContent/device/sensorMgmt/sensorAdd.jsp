<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="sensor.addSensor" /></title>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel">
			<fmt:message key="sensor.addSensor" />
		</h4>
	</div>
	<form class="form-horizontal row" id="sensorAddForm" method="post">
		<input type="hidden" id="belong" />
		<div class="modal-body">
			<div class="col-md-6">
				<div class="form-group ">
					<label class="col-sm-4 control-label"><em>*</em><fmt:message
							key="WarehouseSensor.sensorNumber" /></label>
					<div class="col-sm-8">
						<input type="text" id="warehouseSensorBO.sensorNumber"
							name="warehouseSensorBO.sensorNumber"
							value="${param.sensorNumber }" class="form-control input-sm">
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-4 control-label"><em>*</em><fmt:message
							key="WarehouseSensor.belongTo" /></label>
					<div class="col-sm-8">
						<select style="font-size: 10px" id="s_belongTo" name="s_belongTo"
							class="form-control">
							<option value=""><fmt:message key="please.choose" /></option>
							<c:forEach var="SystemDepartmentBO" items="${sensorMgmtList}">
								<option value='${SystemDepartmentBO.organizationId}'
								   <c:if test="${SystemDepartmentBO.organizationId eq orgId || param.checkinPort==SystemDepartmentBO.organizationId}">selected </c:if>>
								   <!-- ${param.checkinPort==SystemDepartmentBO.organizationId ? 'selected' : ''}> -->
									${SystemDepartmentBO.organizationName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="form-group ">
					<label class="col-sm-4 control-label"><em>*</em><fmt:message
							key="WarehouseSensor.sensorStatus" /></label>
					<div class="col-sm-8">
						<%-- <select style="font-size:10px" id="s_sensorStatus" name="s_sensorStatus" class="form-control">
								<option  value=""><fmt:message key="please.choose"/></option>
								<option value="0"><fmt:message key="device.Scrap" /></option>
								<option value="1" ${param.fromTrip=='1' ? 'selected' : '' }><fmt:message key="device.Normal" /></option>
								<option value="3"><fmt:message key="device.Inway" /></option>
								<option value="4"><fmt:message key="device.Maintain" /></option>
						</select> --%>

						<s:select name="s_sensorStatus" emptyOption="true"
							cssClass="form-control" theme="simple"
							list="@com.nuctech.ls.model.util.DeviceStatusExceptOnway@values()"
							listKey="text" listValue="key"
							value="%{#request.pageQuery.filters.device}">
						</s:select>

					</div>
				</div>
				<div class="form-group ">
					<div class="col-sm-8">
						<input type="hidden" id="warehouseSensorBO.sensorType"
							name="warehouseSensorBO.sensorType" class="form-control input-sm"
							value="1">
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger" id="addSensorButton">
				<fmt:message key="common.button.add" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
	</form>

	<script type="text/javascript">
		$(function() {
			var a = ${disabled};
			bindSensorAddForm();
			//如果来自行程激活页面，设置号码状态为正常、只读
			if ("${param.fromTrip }" == '1') {
				$("#s_sensorStatus").val(1).attr("disabled", true);
				$("#belong").val($("#s_belongTo").val());
				$("#s_belongTo").attr("disabled", true);
			}
			
			if(a==true){
				$("#s_belongTo").attr("disabled", true);
				$("#belong").val($("#s_belongTo").val());
			}
		});

		/**
		 * 添加关锁表单验证
		 */
		function bindSensorAddForm() {
			//设置验证
			$('#sensorAddForm').bootstrapValidator({
				message : $.i18n.prop("common.message.form.validator"),
				fields : {
					'warehouseSensorBO.sensorNumber' : {
						validators : {
							notEmpty : {},
							stringLength: {
								max: 50
							},
							remote : {
								url : '${root }/sensorMgmt/repeate.action',
								message : $.i18n.prop("senson.number.repeat"),
								delay : 2000,
								type : 'post'
							}
						}
					},
					's_belongTo' : {
						validators : {
							notEmpty : {}
						}
					},
					's_sensorStatus' : {
						validators : {
							notEmpty : {}
						}
					},
					'warehouseSensorBO.sensorType' : {
						validators : {
							notEmpty : {}
						}
					}
				}
			}).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
				e.preventDefault();
				var $form = $(e.target);
				var bv = $form.data('bootstrapValidator');
				
				if ("${param.fromTrip }" == '1') {
					$("#s_sensorStatus").removeAttr("disabled").val(1);
					$("#s_belongTo").removeAttr("disabled").val($("#belong").val());
				}
				if("${disabled}"=="true"){
					$("#s_belongTo").removeAttr("disabled").val($("#belong").val());
				}

				var serialize = $form.serialize();
				{
					var url = '${root }/sensorMgmt/addSensor.action';
					$.post(url, serialize, function(data) {
						if(!needLogin(data)) {
							if (data) {
								bootbox.alert($.i18n.prop("sensor.add.success"));
								//如果来自行程激活页面，则将新号显示到页面
								if ("${param.fromTrip }" == '1') {
									appendSensorNums("${param.sensorNumber }");
									$("#sensorNumberInput").val('');
								}
								$('#addSensorModal').modal('hide');
								$table.bootstrapTable('refresh', {});
							} else {
								bootbox.error($.i18n.prop("sensor.add.fail"));
								$('#addSensorModal').modal('hide');
								$table.bootstrapTable('refresh', {});
							}
						}

					}, "json");
				}
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