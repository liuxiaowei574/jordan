<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="warehouse.device.application.add.title"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="warehouseDeviceApplicationAddModalTitle"><fmt:message key="warehouse.device.application.add.title"/></h4>
</div>
<form class="form-horizontal row" id="warehouseDeviceApplicationAddForm" method="post">
	<div class="modal-body">
	  	<div class="col-md-10">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseDeviceApplication.deviceNumber"><fmt:message key="warehouse.device.application.deviceNumber"/></label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseDeviceApplication.deviceNumber" name="warehouseDeviceApplication.deviceNumber">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseDeviceApplication.esealNumber"><fmt:message key="warehouse.device.application.esealNumber"/></label>
	   	     <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseDeviceApplication.esealNumber" name="warehouseDeviceApplication.esealNumber">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseDeviceApplication.sensorNumber"><fmt:message key="warehouse.device.application.sensorNumber"/></label>
	   	     <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseDeviceApplication.sensorNumber" name="warehouseDeviceApplication.sensorNumber">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="warehouseDeviceApplication.otherNumber"><fmt:message key="warehouse.device.application.otherNumber"/></label>
	   	     <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="warehouseDeviceApplication.otherNumber" name="warehouseDeviceApplication.otherNumber">
	   	    </div>
	   	  </div>
	  	</div>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
	  <button type="submit" class="btn btn-danger" id="addWarehouseDeviceApplicationButton" ><fmt:message key="common.button.save"/></button>
	  <button type="button" class="btn btn-darch" data-dismiss="modal"><fmt:message key="common.button.cancle"/></button>
	</div>
</form>

<script type="text/javascript">
	var root = "${root}";
</script>
<script type="text/javascript" src="${root}/device/application/js/add.js"></script>
</body>
</html>