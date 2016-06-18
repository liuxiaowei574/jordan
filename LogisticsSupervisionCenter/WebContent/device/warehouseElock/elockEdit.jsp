<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="WarehouseElock.list.title"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="myModalLabel"><fmt:message key="elock.modifyElock"/></h4>
</div>
<form class="form-horizontal row" id="elockEditForm" method="post">
	<input type="hidden" class="form-control input-sm" id="warehouseElockBO.elockId" 
	   	      name="warehouseElockBO.elockId" value="${warehouseElockBO.elockId }">
	<div class="modal-body">
	  	<div class="col-md-6">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseElockBO.elockNumber"><fmt:message key="WarehouseElock.elockNumber"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseElockBO.elockNumber" 
	   	      name="warehouseElockBO.elockNumber" value="${warehouseElockBO.elockNumber}">
	   	    </div>
	   	  </div>
	   	  <div class="form-group">
				<label class="col-sm-4 control-label"><fmt:message
						key="WarehouseElock.belongTo" /></label>
				<div class="col-sm-8">
					<select id="warehouseElockBO.belongTo"
						name="warehouseElockBO.belongTo" class="form-control">
						
		<!-- 应该改为sysdepartment.organationName
		 -->			<option  value="${warehouseElockBO.belongTo}">${warehouseElockBO.belongTo}</option>
						
							<c:forEach var="SystemDepartmentBO" items="${deptEditList}">
								<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
							</c:forEach>
					</select>
				</div>
			</div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseElockBO.simCard"><fmt:message key="WarehouseElock.simCard"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseElockBO.simCard" 
	   	      name="warehouseElockBO.simCard" value="${warehouseElockBO.simCard}">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseElockBO.interval"><fmt:message key="WarehouseElock.interval"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseElockBO.interval" 
	   	      name="warehouseElockBO.interval" value="${warehouseElockBO.interval}">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseElockBO.gatewayAddress"><fmt:message key="WarehouseElock.gatewayAddress"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseElockBO.gatewayAddress" 
	   	      name="warehouseElockBO.gatewayAddress" value="${warehouseElockBO.gatewayAddress}">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="swarehouseElockBO.elockStatus"><fmt:message key="WarehouseElock.elockStatus"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseElockBO.elockStatus" 
	   	      name="warehouseElockBO.elockStatus" value="${warehouseElockBO.elockStatus}">
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
	buildElockEditForm();
});

/**
 * 
 */
function buildElockEditForm() {
	//设置验证
	  $('#elockEditForm').bootstrapValidator({
		  message: $.i18n.prop("common.message.form.validator"),
	      fields: {
	    	'warehouseElockBO.elockNumber': {
	              validators: {
	            	  notEmpty: {}
	              }
	          },  
	      	'warehouseElockBO.belongTo': {
	              validators: {
	            	  notEmpty: {}
	              }
	          },  
	          'warehouseElockBO.simCard': {
	              validators: {
	            	  notEmpty: {}
	              }
	          },
	          'warehouseElockBO.interval': {
	              validators: {
	            	   notEmpty: {}
	                }
	            },
	          'warehouseElockBO.gatewayAddress': {
	              validators: {
	            	   notEmpty: {}
	                }
	            },
	            'warehouseElockBO.elockStatus': {
		              validators: {
		            	   notEmpty: {}
		                }
		         }
	      }/* ,
	      
	      submitHandler: function(validator, form, submitButton){
	    	var serialize = $("#elockEditForm").serialize();
	  		var url = '${root }/warehouseElock/editElock.action'
	  		$.post(url, serialize, function(data) {
	  			bootbox.success($.i18n.prop("elock.modifyElock.success"));
	  			$('#table').bootstrapTable('refresh', {});
	  			$('#updateElockModal').modal('hide');
	  		}, "json");
	      }  */
	  }).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
	      e.preventDefault();
	      var $form = $(e.target);
	      var bv = $form.data('bootstrapValidator');
	      var serialize = $form.serialize();
	      var url = '${root }/warehouseElock/editElock.action'
		  $.post(url, serialize, function(data) {
			if(data) {
				bootbox.success($.i18n.prop("elock.modifyElock.success"));
	  			$('#updateElockModal').modal('hide');
	  			$table.bootstrapTable('refresh', {});
			} else {
				bootbox.error($.i18n.prop("elock.modify.fail"));
				$('#updateElockModal').modal('hide');
				$table.bootstrapTable('refresh', {});
			}
		 }, "json");
	  });
	}
</script>
</body>
</html>