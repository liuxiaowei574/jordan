/***************************表单验证方法*****************************/
/**
 * 添加用户表单验证
 */
function bindNoticeAddForm(){
  //设置验证
  $('#noticeAddForm').bootstrapValidator({
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
    	var serialize = $("#noticeAddForm").serialize();
  		var url = root+'/notice/addNotice.action'
  		$.post(url, serialize, function(data) {
  			if(data) {
  				bootbox.alert($.i18n.prop("system.notice.add.success"));
  	  			$('#noticeAddModal').modal('hide');
  	  			$table.bootstrapTable('refresh', {});
  			} else {
  				bootbox.error($.i18n.prop("system.notice.add.error"));
  				$('#noticeAddModal').modal('hide');
  				$table.bootstrapTable('refresh', {});
  			}
  			
  		}, "json");
    	  
      } */
  }).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
      e.preventDefault();
      var $form = $(e.target);
      var bv = $form.data('bootstrapValidator');
     
      var serialize = $form.serialize();
      var url = root+'/notice/addNotice.action'
	  $.post(url, serialize, function(data) {
		  if(!needLogin(data)) {
			  if(data) {
				  bootbox.alert($.i18n.prop("system.notice.add.success"));
				  $('#noticeAddModal').modal('hide');
				  $table.bootstrapTable('refresh', {});
			  } else {
				  bootbox.error($.i18n.prop("system.notice.add.error"));
				  $('#noticeAddModal').modal('hide');
				  $table.bootstrapTable('refresh', {});
			  }
		  }
			
	  }, "json");
  });
}
/*************************初始化方法******************************/
$(function() {
	bindNoticeAddForm();
});