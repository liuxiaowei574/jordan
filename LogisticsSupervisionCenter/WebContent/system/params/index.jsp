<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%> 
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title>参数设置</title>
</head>
<body>
	<div class="panel panel-default">
		<div class="panel-heading">参数设置</div>
		<div class="panel-body">
		     &nbsp;&nbsp;参数列表
		</div>
		<table class="table">
			<c:forEach var="params" items="${paramsList }">
			 	<tr>
				 	<td width="30%" align="right">
				 		<label class="col-sm-10 control-label">${params.paramName }&nbsp;:</label>
				 	</td>
				 	<td width="70%" align="left">
				 		<div class="col-sm-10">
					         <input class="form-control" id="focusedInput" type="text" 
					            value="${params.paramValue }">
					      </div>
				 	</td>
				 </tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>