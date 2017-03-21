/***************************表单验证方法*****************************/
/**
 * 添加用户表单验证
 */
function bindNoticeEditForm(){
  //设置验证
  $('#noticeEditForm').bootstrapValidator({
	  message: $.i18n.prop("common.message.form.validator"),
      fields: {
    	'notice.noticeTitle': {
              validators: {
            	  notEmpty: {}
              }
          }, 
          'notice.noticeContent': {
              validators: {
            	  notEmpty: {}
              }
          }
      }/*,
      submitHandler: function(validator, form, submitButton){
    	var serialize = $("#noticeEditForm").serialize();
  		var url = root+'/notice/editNotice.action'
  		$.post(url, serialize, function(data) {
  			if(data) {
  				bootbox.success($.i18n.prop('system.notice.edit.success'));
  	  			$('#noticeEditModal').modal('hide');
  	  			$table.bootstrapTable('refresh', {});
  			} else {
  				bootbox.error($.i18n.prop('system.notice.edit.error'));
  				$('#noticeEditModal').modal('hide');
  				$table.bootstrapTable('refresh', {});
  			}
  			
  		}, "json");
    	  
      } */
  }).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
      e.preventDefault();
      var $form = $(e.target);
      var bv = $form.data('bootstrapValidator');
     
      var serialize = $form.serialize();
      var url = root+'/notice/editNotice.action'
	  $.post(url, serialize, function(data) {
		  if(!needLogin(data)) {
			  if(data) {
				  bootbox.success($.i18n.prop('system.notice.edit.success'));
				  $('#noticeEditModal').modal('hide');
				  $table.bootstrapTable('refresh', {});
			  } else {
				  bootbox.error($.i18n.prop('system.notice.edit.error'));
				  $('#noticeEditModal').modal('hide');
				  $table.bootstrapTable('refresh', {});
			  }
		  }
	 }, "json");
  });
}
/*************************初始化方法******************************/
$(function() {
	bindNoticeEditForm();
});