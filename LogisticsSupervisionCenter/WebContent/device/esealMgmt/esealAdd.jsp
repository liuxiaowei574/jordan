<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="eseal.add"/></title>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel"><fmt:message key="eseal.add"/></h4>
	</div>
	<form class="form-horizontal row" id="esealAddForm" method="post">
		<div class="col-md-6">
			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message key="warehouseEsealBO.esealNumber"/></label>
				<div class="col-sm-8">
					<input type="text" id="warehouseEsealBO.esealNumber"
						name="warehouseEsealBO.esealNumber" value="${param.esealNumber }"
						class="form-control input-sm">
				</div>
			</div>

			<div class="form-group">
				<label class="col-sm-4 control-label"><fmt:message key="warehouseEsealBO.belongTo"/></label>
				<div class="col-sm-8">
						<select style="font-size:10px" id="s_belongTo" name="s_belongTo" class="form-control">
							<option  value=""><fmt:message key="please.choose"/></option>
							<c:forEach var="SystemDepartmentBO" items="${esealBelongtoList}">
								<option value='${SystemDepartmentBO.organizationId}' ${param.checkinPort==SystemDepartmentBO.organizationId ? 'selected' : ''}>
									${SystemDepartmentBO.organizationName}
								</option>
							</c:forEach>
						</select>
					</div>
			</div>
			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message key="warehouseEsealBO.esealStatus"/></label>
				<div class="col-sm-8">
						<select style="font-size:10px" id="s_esealStatus" name="s_esealStatus" class="form-control">
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
			<button type="submit" class="btn btn-danger" id="addEsealButton">
				<fmt:message key="common.button.add" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
	</form>

	<script type="text/javascript">
		$(function() {

			bindEsealAddForm();
		});

		/**
		 * 添加关锁表单验证
		 */
		function bindEsealAddForm() {
			//设置验证
			$('#esealAddForm').bootstrapValidator({
				message : $.i18n.prop("common.message.form.validator"),
				fields : {
					'warehouseEsealBO.esealNumber' : {
						validators : {
							notEmpty : {}
						}
					},
					'warehouseEsealBO.belongTo' : {
						validators : {
							notEmpty : {}
						}
					},
					'warehouseEsealBO.esealStatus' : {
						validators : {
							notEmpty : {}
						}
					}
				}/* ,
				submitHandler : function(validator, form, submitButton) {
					var serialize = $("#esealAddForm").serialize();
					var url = '${root }/esealMgmt/addEseal.action';
					$.post(url, serialize, function(data) {
						if(data==true){
							bootbox.alert($.i18n.prop("eseal.add.success"));
							 
						};
						$('#table').bootstrapTable('refresh', {});
						$('#addEsealModal').modal('hide');
						
					}, "json");
				} */
			}).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
			      e.preventDefault();
			      var $form = $(e.target);
			      var bv = $form.data('bootstrapValidator');
			     
			      var serialize = $form.serialize();
			      var url = '${root }/esealMgmt/addEseal.action';
				  $.post(url, serialize, function(data) {
						if(data) {
							bootbox.alert($.i18n.prop("eseal.add.success"));
							//如果来自行程激活页面，则将新号显示到页面
				  			if("${param.fromTrip }" == '1') {
								appendEsealNums("${param.esealNumber }");
								validateEsealNumber();
							}
				  			$('#addEsealModal').modal('hide');
				  			$table.bootstrapTable('refresh', {});
						} else {
							bootbox.error($.i18n.prop("eseal.add.fail"));
							$('#addEsealModal').modal('hide');
							$table.bootstrapTable('refresh', {});
						}
						
				  }, "json");
			  });
			}
	</script>
</body>
</html>