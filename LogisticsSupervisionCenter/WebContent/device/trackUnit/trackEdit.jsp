<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="track.Mgmt"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="myModalLabel"><fmt:message key="track.modify"/></h4>
</div>
<form class="form-horizontal row" id="trackEditForm" method="post">

	   	      
	<input type="hidden" class="form-control input-sm" id="lsWarehouseTrackUnitBO.createTime" 
	   	      name="lsWarehouseTrackUnitBO.createTime" value="${lsWarehouseTrackUnitBO.createTime }">
	   	      
 	<input type="hidden" class="form-control input-sm" id="lsWarehouseTrackUnitBO.createUser" 
   	      name="lsWarehouseTrackUnitBO.createUser" value="${lsWarehouseTrackUnitBO.createUser }">  	


	<input type="hidden" class="form-control input-sm" id="lsWarehouseTrackUnitBO.trackUnitId" 
	   	      name="lsWarehouseTrackUnitBO.trackUnitId" value="${lsWarehouseTrackUnitBO.trackUnitId }">
	<div class="modal-body">
	  	<div class="col-md-6">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="lsWarehouseTrackUnitBO.trackUnitNumber"><fmt:message key="track.number"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="lsWarehouseTrackUnitBO.trackUnitNumber" 
	   	      name="lsWarehouseTrackUnitBO.trackUnitNumber"readonly="true" value="${lsWarehouseTrackUnitBO.trackUnitNumber}">
	   	    </div>
	   	  </div>
	   	  <div class="form-group">
				<label class="col-sm-4 control-label"><fmt:message key="track.belongto"/></label>
				<div class="col-sm-8">
					<select id="lsWarehouseTrackUnitBO.belongTo"name="lsWarehouseTrackUnitBO.belongTo" class="form-control">
						
					<option  value="${systemDepartmentBO.organizationId}">${systemDepartmentBO.organizationName}</option>
						
							<c:forEach var="SystemDepartmentBO" items="${deptEditList}">
								<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
							</c:forEach>
					</select>
				</div>
			</div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="lsWarehouseTrackUnitBO.simCard"><fmt:message key="WarehouseElock.simCard"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="sWarehouseTrackUnitBOv.simCard" 
	   	      name="lsWarehouseTrackUnitBO.simCard" value="${lsWarehouseTrackUnitBO.simCard}">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="lsWarehouseTrackUnitBO.interval"><fmt:message key="WarehouseElock.interval"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="lsWarehouseTrackUnitBO.interval" 
	   	      name="lsWarehouseTrackUnitBO.interval" value="${lsWarehouseTrackUnitBO.interval}">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="lsWarehouseTrackUnitBO.gatewayAddress"><fmt:message key="WarehouseElock.gatewayAddress"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="lsWarehouseTrackUnitBO.gatewayAddress" 
	   	      name="lsWarehouseTrackUnitBO.gatewayAddress" value="${lsWarehouseTrackUnitBO.gatewayAddress}">
	   	    </div>
	   	  </div>
	   	  
	   	  <div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message key="track.status"/></label>
					<div class="col-sm-8">
						<s:select name="lsWarehouseTrackUnitBO.trackUnitStatus" 
						emptyOption="true"
						cssClass="form-control" theme="simple"
						list="@com.nuctech.ls.model.util.DeviceStatus@values()"
						listKey="text"
						listValue="key" 
						 >
					</s:select>
				</div>
			</div> 
	   	  
	   	  
	   	  
	  	</div>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
	  <button type="submit" class="btn btn-danger" id="modifyButton" ><fmt:message key="common.button.modify"/></button>
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
	  $('#trackEditForm').bootstrapValidator({
		  message: $.i18n.prop("common.message.form.validator"),
		  fields : {
				'lsWarehouseTrackUnitBO.trackUnitNumber' : {
					validators : {
						notEmpty : {}
					}
				},
				'lsWarehouseTrackUnitBO.belongTo' : {
					validators : {
						notEmpty : {}
					}
				},
				'lsWarehouseTrackUnitBO.simCard' : {
					validators : {
						notEmpty : {}
					}
				},
				'lsWarehouseTrackUnitBO.interval' : {
					validators : {
						notEmpty : {}
					}
				},
				'lsWarehouseTrackUnitBO.gatewayAddress' : {
					validators : {
						notEmpty : {}
					}
				},
				'lsWarehouseTrackUnitBO.trackUnitStatus' : {
					validators : {
						notEmpty : {}
					}
				}
			}
	      
	  }).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
	      e.preventDefault();
	      var $form = $(e.target);
	      var bv = $form.data('bootstrapValidator');
	      var serialize = $form.serialize();
	      var url = '${root }/trackMgmt/editTrack.action'
		  $.post(url, serialize, function(data) {
			  if(!needLogin(data)) {
				if(data) {
					bootbox.success($.i18n.prop('track.modify.success'));
		  			$('#updateTrackModal').modal('hide');
		  			$table.bootstrapTable('refresh', {});
				} else {
					bootbox.error($.i18n.prop('track.modify.fail'));
					$('#updateTrackModal').modal('hide');
					$table.bootstrapTable('refresh', {});
				}
			  }
		 }, "json");
	  });
	}
</script>
</body>
</html>