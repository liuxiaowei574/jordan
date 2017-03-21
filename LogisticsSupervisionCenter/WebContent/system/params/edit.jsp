<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="system.params.title"/></title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="paramsEditModalTitle"><fmt:message key="system.params.title"/></h4>
</div>
<form class="form-horizontal row" id="paramEditForm" method="post">
	<input type="hidden" id="systemParams.paramId" name="systemParams.paramId" value="${systemParams.paramId }">
	<input type="hidden" id="systemParams.paramStatus" name="systemParams.paramStatus" value="${systemParams.paramStatus }">
	<div class="modal-body">
	  	<div class="col-md-10">
	   	  <div class="form-group">
	   	    <label class="col-sm-4 control-label" for="paramName"><fmt:message key="system.params.paramName"/>：</label>
	   	    <div class="col-sm-8">
	   	      <input type="text" readonly="readonly" class="form-control input-sm" id="paramName" name="paramName" value="<fmt:message key="params.${systemParams.paramCode }"/>">
	   	      <input type="hidden" readonly="readonly" class="form-control input-sm" id="systemParams.paramName" name="systemParams.paramName" value="${systemParams.paramName }">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="systemParams.paramCode"><fmt:message key="system.params.paramCode"/>：</label>
	   	    <div class="col-sm-8">
	   	      <input type="text" readonly="readonly" class="form-control input-sm" id="systemParams.paramCode" name="systemParams.paramCode" value="${systemParams.paramCode }">
	   	    </div>
	   	  </div>
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="systemParams.paramValue"><em>*</em><fmt:message key="system.params.value"/>：</label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="systemParams.paramValue" name="systemParams.paramValue" value="${systemParams.paramValue }">
	   	    </div>
	   	  </div>
	   	   <div class="form-group">
	   	    <label class="col-sm-4 control-label" for="paramType"><fmt:message key="system.params.type"/>：</label>
	   	    <div class="col-sm-8">
	   	    	<c:if test="${systemParams.paramType == '0' }">
	   	    	<input type="text" readonly="readonly" class="form-control input-sm" id="paramType" name="paramType" value="<fmt:message key="system.params.type.nomal"/>">
	   	    	</c:if>
	   	    	<c:if test="${systemParams.paramType == '1' }">
	   	    	<input type="text" readonly="readonly" class="form-control input-sm" id="paramType" name="paramType" value="<fmt:message key="system.params.type.alarm"/>">
	   	    	</c:if>
	   	    	
	   	    	<input type="hidden" readonly="readonly" class="form-control input-sm" id="systemParams.paramType" name="systemParams.paramType" value="${systemParams.paramType }">
	   	    </div>
	   	  </div>
	  	</div>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
	  <button type="submit" class="btn btn-danger" id="editParamsButton" ><fmt:message key="common.button.save"/></button>
	  <button type="button" class="btn btn-darch" data-dismiss="modal"><fmt:message key="common.button.cancle"/></button>
	</div>
</form>

<script type="text/javascript">
	var root = "${root}";
</script>
<script type="text/javascript" src="${root}/system/params/js/edit.js"></script>
</body>
</html>