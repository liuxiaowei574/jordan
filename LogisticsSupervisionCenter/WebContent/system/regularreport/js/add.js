/***************************表单验证方法*****************************/
/**
 * 添加表单验证
 */
function bindNoticeAddForm(){
  //设置验证
  $('#reportAddForm').bootstrapValidator({
	  message: $.i18n.prop("common.message.form.validator"),
      fields: {
    	'regularReportParaSetBO.reportName': {
              validators: {
            	  notEmpty: {}
              }
          }, 
          'regularReportParaSetBO.reportType': {
              validators: {
            	  notEmpty: {}
              }
          }, 
          'regularReportParaSetBO.cycle': {
              validators: {
            	  notEmpty: {}
              }
          }, 
          'regularReportParaSetBO.customTime': {
              validators: {
            	  notEmpty: {}
              }
          }, 
          'regularReportParaSetBO.isEnable': {
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
      var url = root+'/regularReportParameter/addParameterSet.action'
	  $.post(url, serialize, function(data) {
		  if(!needLogin(data)) {
			  if(data) {
				  bootbox.alert("参数设置添加成功");
				  $('#parametersAddModal').modal('hide');
				  $table.bootstrapTable('refresh', {});
			  } else {
				  bootbox.error($.i18n.prop("system.notice.add.error"));
				  $('#parametersAddModal').modal('hide');
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