<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="eseal.edit"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="myModalLabel"><fmt:message key="eseal.edit"/></h4>
</div>
<form class="form-horizontal row" id="esealEditForm" method="post">
	<input type="hidden" class="form-control input-sm" id="warehouseEsealBO.esealId" 
	   	      name="warehouseEsealBO.esealId" value="${warehouseEsealBO.esealId}">
	<div class="modal-body">
	  	<div class="col-md-6">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseEsealBO.esealNumber"><fmt:message key="warehouseEsealBO.esealNumber"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseEsealBO.esealNumber" 
	   	      name="warehouseEsealBO.esealNumber" value="${warehouseEsealBO.esealNumber}">
	   	    </div>
	   	  </div>
	   	  <div class="form-group">
				<label class="col-sm-4 control-label"><fmt:message key="warehouseEsealBO.belongTo"/></label>
				<div class="col-sm-8">
					<select id="warehouseEsealBO.belongTo"
						name="warehouseEsealBO.belongTo" class="form-control">
						<option  value="${warehouseEsealBO.belongTo}">${warehouseEsealBO.belongTo}</option>
						
						<c:forEach var="SystemDepartmentBO" items="${esealEditList}">
							<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseElockBO.esealStatus"><fmt:message key="warehouseEsealBO.esealStatus"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseEsealBO.esealStatus" 
	   	      name="warehouseEsealBO.esealStatus" value="${warehouseEsealBO.esealStatus}">
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
	buildEsealEditForm();
});

/**
 * 
 */
function buildEsealEditForm() {
	//设置验证
	  $('#esealEditForm').bootstrapValidator({
		  message: $.i18n.prop("common.message.form.validator"),
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
	      submitHandler: function(validator, form, submitButton){
	    	var serialize = $("#elockEditForm").serialize();
	  		var url = '${root }/esealMgmt/editEseal.action'
	  		$.post(url, serialize, function(data) {
	  			bootbox.success($.i18n.prop("eseal.modifyEseal.success"));
	  			$('#table').bootstrapTable('refresh', {});
	  			$('#updateEsealModal').modal('hide');
	  		}, "json");
	    	  
	      }  */
	  }).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
	      e.preventDefault();
	      var $form = $(e.target);
	      var bv = $form.data('bootstrapValidator');
	      var serialize = $form.serialize();
	      var url = '${root }/esealMgmt/editEseal.action'
		  $.post(url, serialize, function(data) {
			if(data) {
				bootbox.success($.i18n.prop("eseal.modifyEseal.success"));
	  			$('#updateEsealModal').modal('hide');
	  			$table.bootstrapTable('refresh', {});
			} else {
				bootbox.error($.i18n.prop("eseal.modifyEseal.fail"));
				$('#updateEsealModal').modal('hide');
				$table.bootstrapTable('refresh', {});
			}
		 }, "json");
	  });
	}
	      
	      
	      
	      
</script>
</body>
</html>