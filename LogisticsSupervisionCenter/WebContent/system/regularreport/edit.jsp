<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<title>报告参数修改</title>
</head>
<body>
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <h4 class="modal-title" id="reportEditModalTitle">修改报告参数设置</h4>
</div>
<form class="form-horizontal row" id="reportEditForm" method="post">
	<input type="hidden" id="regularReportParaSetBO.reportId" name="regularReportParaSetBO.reportId" value="${regularReportParaSetBO.reportId}"/>
	<div class="modal-body">
	  	<div class="col-md-10">
	   	  <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="regularReportParaSetBO.reportName">报告名称</label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="regularReportParaSetBO.reportName" name="regularReportParaSetBO.reportName" value="${regularReportParaSetBO.reportName}">
	   	    </div>
	   	  </div>
	   	  
	   	  <div class="form-group ">
				<label class="col-sm-4 control-label">报告类型</label>
					<div class="col-sm-8">
						<s:select name="regularReportParaSetBO.reportType" 
						emptyOption="true"
						cssClass="form-control" theme="simple"
						list="@com.nuctech.ls.model.util.ReportType@values()"
						listKey="text"
						listValue="key" 
						 >
					</s:select>
				</div>
			</div> 
	   	  
	   	  
	   	 <%--   <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="regularReportParaSetBO.cycle">周期</label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="regularReportParaSetBO.cycle" name="regularReportParaSetBO.cycle" value="${regularReportParaSetBO.cycle}">
	   	    </div>
	   	  </div> --%>
	   	  
	   	   <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="regularReportParaSetBO.customTime">定制时间</label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="regularReportParaSetBO.customTime" name="regularReportParaSetBO.customTime" value="${regularReportParaSetBO.customTime}">
	   	    </div>
	   	  </div>
	   	  
	   	   <div class="form-group ">
	   	    <label class="col-sm-4 control-label" for="regularReportParaSetBO.isEnable">是否可用</label>
	   	    <div class="col-sm-8">
	   	      <input type="text" class="form-control input-sm" id="regularReportParaSetBO.isEnable" name="regularReportParaSetBO.isEnable" value="${regularReportParaSetBO.isEnable}">
	   	    </div>
	   	  </div>
	  	</div>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
	  <button type="submit" class="btn btn-danger" id="editNoticeButton" ><fmt:message key="common.button.save"/></button>
	  <button type="button" class="btn btn-darch" data-dismiss="modal"><fmt:message key="common.button.cancle"/></button>
	</div>
</form>

<script type="text/javascript">
	var root = "${root}";
</script>
<script type="text/javascript" src="${root}/system/regularreport/js/edit.js"></script>
</body>
</html>