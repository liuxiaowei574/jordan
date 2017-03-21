<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
	<script type="text/javascript">
		if(window.dialogArguments) {
			window.dialogArguments.top.location.href = "${root }/login.jsp?sessionOutStatus=timeout";
			window.close();
		}
		window.top.location.href = "${root }/login.jsp?sessionOutStatus=timeout";
	</script>
</body>
</html>