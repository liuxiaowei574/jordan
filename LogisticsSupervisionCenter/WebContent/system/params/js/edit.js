/***************************表单验证方法*****************************/
/**
 * 添加用户表单验证
 */
function bindParamEditForm(){
	/** 必须为数字的参数代码 */
	var numberParams = ['DISPATCH_STATISTICS_INTERVAL','DEVICE_RESERV_RATIO','STAY_TIME','STATIONARY_SPEED','LOW_BATTERY','CIRCLE_BUFFER'];
  //设置验证
  $('#paramEditForm').bootstrapValidator({
	  message: $.i18n.prop("common.message.form.validator"),
      fields: {
          'systemParams.paramValue': {
              validators: {
            	  notEmpty: {},
            	  callback: {
                      message: $.i18n.prop("common.message.integers.required"),
                      callback: function(value, validator) {
                    	  var paramCode = $("#systemParams\\.paramCode").val();
                    	  if(numberParams.indexOf(paramCode) > -1) {
                    		  //如果当前参数代码必须为数字，进行校验
                    		  return /^\d+$/.test(value);
                    	  }
                    	  return true;
                      }
                  }
              }
          }
      }
  }).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
      e.preventDefault();
      var $form = $(e.target);
      var bv = $form.data('bootstrapValidator');
     
      var serialize = $form.serialize();
      var url = root+'/paramsMgmt/editParams.action'
	  $.post(url, serialize, function(data) {
		  if(!needLogin(data)) {
			  if(data) {
				  bootbox.success($.i18n.prop('system.params.edit.success'));
				  $('#paramEditModal').modal('hide');
				  $table.bootstrapTable('refresh', {});
			  } else {
				  bootbox.error($.i18n.prop('system.params.edit.error'));
				  $('#paramEditModal').modal('hide');
				  $table.bootstrapTable('refresh', {});
			  }
		  }
	 }, "json");
  });
}
/*************************初始化方法******************************/
$(function() {
	bindParamEditForm();
});