<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="system.user.add.title"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="myModalLabel"><fmt:message key="system.user.add.title"/></h4>
</div>
<form class="form-horizontal row" id="userAddForm" method="post">
	<div class="modal-body">
	  	<div class="col-md-6">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="systemUser.userAccount"><fmt:message key="user.userAccount"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="systemUser.userAccount" name="systemUser.userAccount">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="systemUser.userName"><fmt:message key="user.userName"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="systemUser.userName" name="systemUser.userName">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="systemUser.userPhone"><fmt:message key="user.userPhone"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="systemUser.userPhone" name="systemUser.userPhone">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="systemUser.userEmail"><fmt:message key="user.userEmail"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="systemUser.userEmail" name="systemUser.userEmail">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="systemUser.userAddress"><fmt:message key="user.userAddress"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="systemUser.userAddress" name="systemUser.userAddress">
	   	    </div>
	   	  </div>
	  	</div>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
	  <button type="submit" class="btn btn-danger" id="addUserButton" ><fmt:message key="common.button.add"/></button>
	  <button type="button" class="btn btn-darch" data-dismiss="modal"><fmt:message key="common.button.cancle"/></button>
	</div>
</form>

<script type="text/javascript">
$(function() {
	bindUserAddForm();
});

/**
 * 添加用户表单验证
 */
function bindUserAddForm(){
  //设置验证
  $('#userAddForm').bootstrapValidator({
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
    	var serialize = $("#userAddForm").serialize();
  		var url = '${root }/userMgmt/addUser.action'
  		$.post(url, serialize, function(data) {
  			//alert(data);
  			bootbox.alert($.i18n.prop("system.user.add.success"));
  			$('#userAddModal').modal('hide');
  		}, "json");
    	  
      }  */
  }).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
      e.preventDefault();
      var $form = $(e.target);
      var bv = $form.data('bootstrapValidator');
     
      var serialize = $form.serialize();
      var url = '${root }/userMgmt/addUser.action'
	  $.post(url, serialize, function(data) {
		bootbox.alert($.i18n.prop("system.user.add.success"));
		$('#userAddModal').modal('hide');
	  }, "json");
  });
}
</script>
</body>
</html>