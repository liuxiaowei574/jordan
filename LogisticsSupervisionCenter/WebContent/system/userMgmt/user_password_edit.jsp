<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="user.password.edit"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="userPasswordEditModalTitle"><fmt:message key="user.password.edit"/></h4>
</div>
<form class="form-horizontal row" id="userPasswordEditForm" method="post">
	<div class="modal-body">
	  	<div class="col-md-10">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="s_oldPassword"><em>*</em><fmt:message key="user.initial.password"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="s_oldPassword" name="s_oldPassword">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="s_newPassword"><em>*</em><fmt:message key="user.new.password"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="s_newPassword" name="s_newPassword">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="s_confirmPassword"><em>*</em><fmt:message key="user.ensure.password"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="s_confirmPassword" name="s_confirmPassword">
	   	    </div>
	   	  </div>
	  	</div>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
	  <button type="submit" class="btn btn-danger" id="userPasswordEditButton" ><fmt:message key="common.button.save"/></button>
	  <button type="button" class="btn btn-darch" data-dismiss="modal"><fmt:message key="common.button.cancle"/></button>
	</div>
</form>

<script type="text/javascript">
	//var root = "${root}";
function bindUserPasswordEditForm(){
  //设置验证
  $('#userPasswordEditForm').bootstrapValidator({
	  message: $.i18n.prop("common.message.form.validator"),
      fields: {
    	's_oldPassword': {
              validators: {
            	  notEmpty: {},
            	  remote : {
            		  message : $.i18n.prop("user.password.wrong"),
            		  url : '${root}/security/validUserPassword.action',
            	  }
              }
          }, 
          's_newPassword': {
              validators: {
            	  notEmpty: {},
            	/*   identical: {
                      field: 's_confirmPassword',
                   	  message: $.i18n.prop("user.two.password.are.not.consistent")
                  }, */
            	  stringLength: {
                      max: 6,
                      message: $.i18n.prop("user.enter.a.six.bit.string")
                  }
              }
          },
          's_confirmPassword' : {
        	  validators: {
        		  notEmpty: {}, 
        		  identical: {
                      field: 's_newPassword',
                      message:$.i18n.prop("user.two.password.are.not.consistent")
                  }
        	  }
          }
      }
  }).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
      e.preventDefault();
      var $form = $(e.target);
      var bv = $form.data('bootstrapValidator');
     
      var serialize = $form.serialize();
      var url = '${root}/security/editUserPassword.action'
	  $.post(url, serialize, function(data) {
		if(data) {
			bootbox.success($.i18n.prop("user.password.edit.success"),function(){
				$('#userPasswordEditModal').modal('hide');
				//密码修改成功后，返回到登陆页面；
	  			var url = '${root}/login.jsp'
	  			window.location.href=url;
			});
		} else {
			bootbox.error($.i18n.prop("user.password.edit.fail"));
			$('#userPasswordEditModal').modal('hide');
		}
	  }, "json");
  });
}
	$(function() {
		bindUserPasswordEditForm();
	});
</script>
</body>
</html>