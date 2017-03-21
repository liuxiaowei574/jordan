<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="slapunish.list.title"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="myModalLabel"><fmt:message key="punish.inform.modify"/></h4>
</div>
<form class="form-horizontal row" id="punishEditForm" method="post">
	<input type="hidden" class="form-control input-sm" id="lsVehiclePunishBo.vpunishId" 
	   	      name="lsVehiclePunishBo.vpunishId" value="${lsVehiclePunishBo.vpunishId}">
	   	      
	   	       
	<div class="modal-body">
	  	<div class="col-md-6">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="lsSlaPunishBO.punishName"><fmt:message key="Vehicle.type.punish.management"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="lsVehiclePunishBo.vpunishType" 
	   	      name="lsVehiclePunishBo.vpunishType" value="${lsVehiclePunishBo.vpunishType}">
	   	    </div>
	   	  </div>
	   	  
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="lsSlaPunishBO.slaContent"><fmt:message key="Vehicle.type.punish.value"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="lsVehiclePunishBo.vpunishValue" 
	   	      name="lsVehiclePunishBo.vpunishValue" value="${lsVehiclePunishBo.vpunishValue}">
	   	    </div>
	   	  </div>
	  	</div>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
	  <button type="submit" class="btn btn-danger" id="modifyButton" ><fmt:message key="common.button.save"/></button>
	  <button type="button" class="btn btn-darch" data-dismiss="modal"><fmt:message key="common.button.cancle"/></button>
	</div>
</form>
<script type="text/javascript">
$(function() {
	buildElockEditForm();
});

/**
 * 
 */
function buildElockEditForm() {
	//设置验证
	  $('#punishEditForm').bootstrapValidator({
		  message: $.i18n.prop("common.message.form.validator"),
	      fields: {
	    	'lsVehiclePunishBo.vpunishType': {
	              validators: {
	            	  notEmpty: {}
	              }
	          },  
	          'lsVehiclePunishBo.vpunishValue': {
	              validators: {
	            	  notEmpty: {},
	            	  numeric:{},
	            	  stringLength:{
							min:0,
							max:5,
						}
	              }
	          }
	      }
	  }).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
	      e.preventDefault();
	      var $form = $(e.target);
	      var bv = $form.data('bootstrapValidator');
	      var serialize = $form.serialize();
	      var url = '${root }/vehiclePunish/editPunish.action'
		  $.post(url, serialize, function(data) {
			  if(!needLogin(data)) {
				if(data) {
					bootbox.success($.i18n.prop("Vehicle.punish.modify.success"));
		  			$('#updatePunishModal').modal('hide');
		  			$table.bootstrapTable('refresh', {});
				} else {
					bootbox.error($.i18n.prop("Vehicle.punish.modify.fail"));
					$('#updatePunishModal').modal('hide');
					$table.bootstrapTable('refresh', {});
				}
			  }
		 }, "json");
	  });
	}
</script>
</body>
</html>