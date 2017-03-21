<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="Authority.management"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="myModalLabel"><fmt:message key="Authority.management.role.modify"/></h4>
</div>
<form class="form-horizontal row" id="elockEditForm" method="post">
	<div class="modal-body">
	  	<div class="col-md-6">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseElockBO.elockNumber"><fmt:message key="Authority.management.role.Id"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="systemRoleBO.roleId" readonly="readonly"
	   	      name="systemRoleBO.roleId" value="${systemRoleBO.roleId}">
	   	    </div>
	   	  </div>
	   	 
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseElockBO.simCard"><fmt:message key="Authority.management.role.name"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="systemRoleBO.roleName" 
	   	      name="systemRoleBO.roleName" value="${systemRoleBO.roleName}">
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
	    	'systemRoleBO.roleId': {
	              validators: {
	            	  notEmpty: {}
	              }
	          },  
	      	'systemRoleBO.roleName': {
	              validators: {
	            	  notEmpty: {}
	              }
	          }
	      }
	  }).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
	      e.preventDefault();
	      var $form = $(e.target);
	      var bv = $form.data('bootstrapValidator');
	      var serialize = $form.serialize();
	      var url = '${root }/roleDistribute/editSystemRole.action'
		  $.post(url, serialize, function(data) {
			var needLoginFlag = false;
			if(typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
				needLoginFlag = needLogin(data);
			}
			if(!needLoginFlag) {
				if(data) {
					bootbox.success($.i18n.prop('Authority.management.role.modify.success'));
		  			$('#updateElockModal').modal('hide');
		  			$table.bootstrapTable('refresh', {});
				} else {
					bootbox.error($.i18n.prop('Authority.management.role.modify.fail'));
					$('#updateElockModal').modal('hide');
					$table.bootstrapTable('refresh', {});
				}
			  }
		 }, "json");
	  });
	}
</script>
</body>
</html>