<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="WarehouseSensor.edit"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="myModalLabel"><fmt:message key="WarehouseSensor.edit"/></h4>
</div>
<form class="form-horizontal row" id="sensorEditForm" method="post">
	<input type="hidden" class="form-control input-sm" id="warehouseSensorBO.sensorId" 
	   	      name="warehouseSensorBO.sensorId" value="${warehouseSensorBO.sensorId}">
	<div class="modal-body">
	  	<div class="col-md-6">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseSensorBO.sensorNumber"><fmt:message key="WarehouseSensor.sensorNumber"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseSensorBO.sensorNumber" 
	   	      name="warehouseSensorBO.sensorNumber" value="${warehouseSensorBO.sensorNumber}">
	   	    </div>
	   	  </div>
	   	  <div class="form-group">
				<label class="col-sm-4 control-label"><fmt:message key="WarehouseSensor.belongTo"/></label>
				<div class="col-sm-8">
					<select id="warehouseSensorBO.belongTo"
						name="warehouseSensorBO.belongTo" class="form-control">
						<option value="${warehouseSensorBO.belongTo}">${warehouseSensorBO.belongTo}</option>
						<c:forEach var="SystemDepartmentBO" items="${sensorEditList}">
							<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseSensorBO.sensorStatus"><fmt:message key="WarehouseSensor.sensorStatus"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseSensorBO.sensorStatus" 
	   	      name="warehouseSensorBO.sensorStatus" value="${warehouseSensorBO.sensorStatus}">
	   	    </div>
	   	  </div>
	   	  
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseSensorBO.sensorType"><fmt:message key="WarehouseSensor.sensorType"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseSensorBO.sensorType" 
	   	      name="warehouseSensorBO.sensorType" value="${warehouseSensorBO.sensorType}">
	   	    </div>
	   	  </div>
	  	</div>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
	  <button type="submit" class="btn btn-danger" id="modifyButton" ><fmt:message key="common.button.save"/></button>
	  <button type="button" class="btn btn-darch" data-dismiss="modal"><fmt:message key="common.button.cancle"/></button>
	</div>
</form>
<script type="text/javascript">
$(function() {
	buildSensorEditForm();
});

/**
 * 
 */
function buildSensorEditForm(){
	//设置验证
	  $('#sensorEditForm').bootstrapValidator({
		  message: $.i18n.prop("common.message.form.validator"),
		  fields : {
				'warehouseSensorBO.sensorNumber' : {
					validators : {
						notEmpty : {}
					}
				},
				'warehouseSensorBO.belongTo' : {
					validators : {
						notEmpty : {}
					}
				},
				'warehouseSensorBO.sensorStatus' : {
					validators : {
						notEmpty : {}
					}
				},
				'warehouseSensorBO.sensorType' : {
					validators : {
						notEmpty : {}
					}
				}
			}/* ,
	      submitHandler: function(validator, form, submitButton){
	    	var serialize = $("#sensorEditForm").serialize();
	  		var url = '${root }/sensorMgmt/editSensor.action'
	  		$.post(url, serialize, function(data) {
	  			bootbox.success($.i18n.prop("sensor.modifySensor.success"));
	  			$('#table').bootstrapTable('refresh', {});
	  			$('#updateSensorModal').modal('hide');
	  		}, "json");
	    	  
	      } 
	  });
} */

		}).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
		    e.preventDefault();
		    var $form = $(e.target);
		    var bv = $form.data('bootstrapValidator');
		    var serialize = $form.serialize();
		    var url = '${root }/sensorMgmt/editSensor.action'
			  $.post(url, serialize, function(data) {
				if(data) {
					bootbox.success($.i18n.prop("sensor.modifySensor.success"));
					$('#updateSensorModal').modal('hide');
					$table.bootstrapTable('refresh', {});
				} else {
					bootbox.error($.i18n.prop("sensor.modifyFail.success"));
					$('#updateSensorModal').modal('hide');
					$table.bootstrapTable('refresh', {});
				}
			 }, "json");
		});
		}
</script>
</body>
</html>