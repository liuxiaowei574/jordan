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
          'notice.noticeUsers': {
              validators: {
            	  notEmpty: {}
              }
          }, 
          'notice.noticeContent': {
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
      var url = root+'/dispatchHandOver/transferDispatchTask.action'
	  $.post(url, serialize, function(data) {
		  if(!needLogin(data)) {
			  if(data) {
				  bootbox.alert($.i18n.prop("system.notice.add.success"));
				  $('#handOverDispatchTask').modal('hide');
			  } else {
				  bootbox.error($.i18n.prop("system.notice.add.error"));
				  $('#handOverDispatchTask').modal('hide');
			  }
		  }
			
	  }, "json");
  });
}
/*************************初始化方法******************************/
$(function() {
	bindNoticeAddForm();
});