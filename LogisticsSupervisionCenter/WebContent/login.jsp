<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${root}/static/css/bootstrap.css">
<link rel="stylesheet" href="${root}/static/css/select2.css">
<link rel="stylesheet" href="${root}/static/css/style.css">
<script src="${root}/static/js/jquery.min.js"></script>
<script src="${root}/static/js/jquery-migrate-1.2.1.min.js"></script>
<script src="${root}/static/js/bootstrap.js"></script>
<script src="${root}/static/js/jquery.validate.js"></script>
<script src="${root}/static/js/messages_zh_CN.js"></script>
<script src="${root}/static/js/select2.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>E-Tracking System</title>
<script type="text/javascript">
	$(document).ready(function() {
		$(".js-example-basic-single").select2();
	});
</script>
</head>
<body class="load">
	<div class="load_bg">
		<div class="load_title">E-tracking System</div>
		<div class="load_box">
			<form role="form" id="loginForm" action="${root }/security/login.action" >
				<div class="form-group">
					<input type="text" id="user.userAccount" name="user.userAccount" class="form-control user" >
				</div>
				<div class="form-group">
					<input type="password" id="user.userPassword" name="user.userPassword" class="form-control password">
				</div>
				<div class="form-group">
					<select class="js-example-basic-single form-control lang">
						<option value="AL">语言</option>
						<option value="WY">English</option>
						<option value="WY">English</option>
					</select>
				</div>
				<button type="submit" id="loginButton" class="btn btn-primary btn-lg btn-block">登录</button>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
$(function() {
	$("#loginButton").click(function() {
		$("#loginForm").submit();
		/* $("#loginForm").validate({
			rules : {
				userAccount : {
					required : true
				}
			}
		}); */
	});
});
setTimeout(function(){try{document.getElementById("user.userAccount").focus();}catch(e){}},0);
</script>
</html>