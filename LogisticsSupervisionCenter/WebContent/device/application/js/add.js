
/**
 * 添加用户表单验证
 */
function bindWarehouseDeviceApplicationAddForm(){
  //设置验证
  $('#warehouseDeviceApplicationAddForm').bootstrapValidator({
      fields: {
    	'warehouseDeviceApplication.deviceNumber': {
              validators: {
            	  notEmpty: {},
            	  digits: {}
              }
          }, 
          'warehouseDeviceApplication.esealNumber': {
              validators: {
            	  notEmpty: {},
            	  digits: {}
              }
          },
          'warehouseDeviceApplication.sensorNumber': {
              validators: {
            	  notEmpty: {},
            	  digits: {}
              }
          }
      }
  	}).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
      e.preventDefault();
      var $form = $(e.target);
      var bv = $form.data('bootstrapValidator');
     
      var serialize = $form.serialize();
      var url = root+'/warehouseDeviceApplication/addWarehouseDeviceApplication.action';
      $.post(url, serialize, function(data) {
    	  if(data.result) {
    		  bootbox.success($.i18n.prop('warehouse.device.application.add.success'));
  				$('#warehouseDeviceApplicationAddModal').modal('hide');
  				$table.bootstrapTable('refresh', {});
    	  } else {
    		  bootbox.error($.i18n.prop('warehouse.device.application.add.error'));
    		  $('#warehouseDeviceApplicationAddModal').modal('hide');
    		  $table.bootstrapTable('refresh', {});
    	  }
      }, "json");
  });
}

$(function() {
	bindWarehouseDeviceApplicationAddForm();
});