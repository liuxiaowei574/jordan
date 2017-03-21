<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="WarehouseSensor.edit"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="myModalLabel"><fmt:message key="WarehouseSensor.edit"/></h4>
</div>
<form class="form-horizontal row" id="sensorEditForm" method="post">
	<input type="hidden" class="form-control input-sm" id="warehouseSensorBO.sensorId" 
	   	      name="warehouseSensorBO.sensorId" value="${warehouseSensorBO.sensorId}">
	<input type="hidden" class="form-control input-sm" id="warehouseSensorBO.belongTo" 
	   	      name="warehouseSensorBO.belongTo" value="${warehouseSensorBO.belongTo}"> 
	   	       	      
	<div class="modal-body">
	  	<div class="col-md-6">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseSensorBO.sensorNumber"><em>*</em><fmt:message key="WarehouseSensor.sensorNumber"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseSensorBO.sensorNumber" 
	   	      name="warehouseSensorBO.sensorNumber" readonly="true" value="${warehouseSensorBO.sensorNumber}">
	   	    </div>
	   	  </div>
	   	  <div class="form-group">
				<label class="col-sm-4 control-label"><em>*</em><fmt:message key="WarehouseSensor.belongTo"/></label>
				<div class="col-sm-8">
					<%-- <select id="warehouseSensorBO.belongTo"
						name="warehouseSensorBO.belongTo" class="form-control">
						<option  value="${systemDepartmentBO.organizationId}">${systemDepartmentBO.organizationName}</option>
						<c:forEach var="SystemDepartmentBO" items="${sensorEditList}">
							<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
						</c:forEach>
					</select> --%>
					<input type="text" class="form-control input-sm" id="warehouseSensorBO_belongTo" 
	   	            name="warehouseSensorBO_belongTo" readonly="true" value="${systemDepartmentBO.organizationName}">
				</div>
			</div>
	   	  <%-- <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseSensorBO.sensorStatus"><fmt:message key="WarehouseSensor.sensorStatus"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseSensorBO.sensorStatus" 
	   	      name="warehouseSensorBO.sensorStatus" value="${warehouseSensorBO.sensorStatus}">
	   	    </div>
	   	  </div> --%>
	   	  
	   <%-- 	  <div class="form-group">
				<label class="col-sm-4 control-label"><fmt:message key="WarehouseSensor.sensorStatus"/></label>
				<div class="col-sm-8">
					<select id="warehouseSensorBO.sensorStatus"name="warehouseSensorBO.sensorStatus" class="form-control">
						<option value=${warehouseSensorBO.sensorStatus}>${warehouseSensorBO.sensorStatus}</option>
						<option value="0">报废</option>
						<option value="1">正常</option>	
						<option value="2">在途</option>
						<option value="3">损坏</option>
						<option value="4">维修</option>
					</select>
				</div>
			</div> --%> 
	   	  <div class="form-group ">
				<label class="col-sm-4 control-label"><em>*</em><fmt:message key="WarehouseSensor.sensorStatus"/></label>
					 <c:choose>
		   	    		<c:when test="${warehouseSensorBO.sensorStatus == '2' }">
			   	    		  <div class="col-sm-8">
									<s:select name="warehouseSensorBO.sensorStatus" 
									emptyOption="true"
									cssClass="form-control" theme="simple"
									list="@com.nuctech.ls.model.util.DeviceStatus@values()"
									listKey="text"
									listValue="key" 
									disabled="true"
									id="onWay"
									>
									</s:select>
							</div>
		   	    		</c:when>
		   	    		<c:when test="${warehouseSensorBO.sensorStatus != '2' }">
			   	    		  <div class="col-sm-8">
								<s:select name="warehouseSensorBO.sensorStatus" 
								emptyOption="false"
								cssClass="form-control" theme="simple"
								list="@com.nuctech.ls.model.util.DeviceStatusExceptOnway@values()"
								listKey="text"
								listValue="key" 
								>
							    </s:select>
						      </div>
		   	    		</c:when>
	   	    	    </c:choose>
			</div> 
	   	  <div class="form-group ">
	   	    <div class="col-sm-8">
	   	      <input type="hidden" class="form-control input-sm" id="warehouseSensorBO.sensorType" 
	   	      name="warehouseSensorBO.sensorType" value="${warehouseSensorBO.sensorType}">
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
	buildSensorEditForm();
});

/**
 * 
 */
function buildSensorEditForm(){
	//设置验证
	  $('#sensorEditForm').bootstrapValidator({
		  message: $.i18n.prop("common.message.form.validator"),
		  fields : {
				'warehouseSensorBO.sensorNumber' : {
					validators : {
						notEmpty : {},
						stringLength: {
							max: 50
						}
					}
				},
				'warehouseSensorBO_belongTo' : {
					validators : {
						notEmpty : {}
					}
				},
				'warehouseSensorBO.sensorStatus' : {
					validators : {
						notEmpty : {}
					}
				},
				'warehouseSensorBO.sensorType' : {
					validators : {
						notEmpty : {}
					}
				}
			}
		}).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
		    e.preventDefault();
		   // var $form = $(e.target);
		    $("#onWay").removeAttr("disabled");//提交前让下拉列表可用
	        var $form = $('#sensorEditForm');
		    var bv = $form.data('bootstrapValidator');
		    var serialize = $form.serialize();
		    var url = '${root }/sensorMgmt/editSensor.action'
			  $.post(url, serialize, function(data) {
				  if(!needLogin(data)) {
					if(data) {
						bootbox.success($.i18n.prop("sensor.modifySensor.success"));
						$('#updateSensorModal').modal('hide');
						$table.bootstrapTable('refresh', {});
					} else {
						bootbox.error($.i18n.prop("sensor.modifyFail.success"));
						$('#updateSensorModal').modal('hide');
						$table.bootstrapTable('refresh', {});
					}
				  }
			 }, "json");
		});
		}
</script>
</body>
</html>