<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="system.user.edit.title"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="myModalLabel"><fmt:message key="system.user.edit.title"/></h4>
</div>
<form class="form-horizontal row" id="userEditForm" method="post">
	<input type="hidden" class="form-control input-sm" id="systemUser.userId" 
	   	      name="systemUser.userId" value="${systemUser.userId }">
	<div class="modal-body">
	  	<div class="col-md-6">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="systemUser.userAccount"><fmt:message key="user.userAccount"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="systemUser.userAccount" 
	   	      name="systemUser.userAccount" value="${systemUser.userAccount }">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="systemUser.userName"><fmt:message key="user.userName"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="systemUser.userName" 
	   	      name="systemUser.userName" value="${systemUser.userName }">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="systemUser.userPhone"><fmt:message key="user.userPhone"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="systemUser.userPhone" 
	   	      name="systemUser.userPhone" value="${systemUser.userPhone }">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="systemUser.userEmail"><fmt:message key="user.userEmail"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="systemUser.userEmail" 
	   	      name="systemUser.userEmail" value="${systemUser.userEmail }">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="systemUser.userAddress"><fmt:message key="user.userAddress"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="systemUser.userAddress" 
	   	      name="systemUser.userAddress" value="${systemUser.userAddress }">
	   	    </div>
	   	  </div>
	  	</div>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
	  <button type="submit" class="btn btn-danger" id="editUserButton" ><fmt:message key="common.button.save"/></button>
	  <button type="button" class="btn btn-darch" data-dismiss="modal"><fmt:message key="common.button.cancle"/></button>
	</div>
</form>
<script type="text/javascript">
$(function() {
	buildUserEditForm();
});

/**
 * 修改用户信息
 */
function buildUserEditForm() {
	//设置验证
	  $('#userEditForm').bootstrapValidator({
		  message: $.i18n.prop("common.message.form.validator"),
	      fields: {
	    	'systemUser.userAccount': {
	              validators: {
	            	  notEmpty: {}
	              }
	          },  
	      	'systemUser.userName': {
	              validators: {
	            	  notEmpty: {}
	              }
	          },  
	          'systemUser.userPhone': {
	              validators: {
	            	  notEmpty: {}
	              }
	          },
	          'systemUser.userEmail': {
	              validators: {
	            	   notEmpty: {}
	                }
	            }
	      }/* ,
	      submitHandler: function(validator, form, submitButton){
	    	var serialize = $("#userEditForm").serialize();
	  		var url = '${root }/userMgmt/editUser.action'
	  		$.post(url, serialize, function(data) {
	  			bootbox.alert($.i18n.prop("system.user.edit.success"));
	  			$('#userEditModal').modal('hide');
	  		}, "json");
	    	  
	      } 
	   */}).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
		      e.preventDefault();
		      var $form = $(e.target);
		      var bv = $form.data('bootstrapValidator');
		     
		      var serialize = $form.serialize();
		      var url = '${root }/userMgmt/editUser.action'
	  		  $.post(url, serialize, function(data) {
	  			bootbox.alert($.i18n.prop("system.user.edit.success"));
	  			$('#userEditModal').modal('hide');
	  		  }, "json");
	});
}
</script>
</body>
</html>