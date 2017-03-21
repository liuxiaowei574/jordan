/***************************表单验证方法*****************************/
/**
 * 添加用户表单验证
 */
function bindNoticeEditForm(){
  //设置验证
  $('#reportEditForm').bootstrapValidator({
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
      var url = root+'/regularReportParameter/editReportParameter.action'
	  $.post(url, serialize, function(data) {
		  if(!needLogin(data)) {
			  if(data) {
				  bootbox.success("报告参数修改成功");
				  $('#parametersEditModal').modal('hide');
				  $table.bootstrapTable('refresh', {});
			  } else {
				  bootbox.error($.i18n.prop('system.notice.edit.error'));
				  $('#parametersEditModal').modal('hide');
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