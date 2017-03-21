<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<fmt:setLocale value="en_US"/>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${root}/static/css/bootstrap.css">
<link rel="stylesheet" href="${root}/static/css/select2.css">
<link rel="stylesheet" href="${root}/static/css/bootbox.css">
<link rel="stylesheet" href="${root}/static/css/style.css">

<script src="${root}/static/js/jquery.min.js"></script>
<script src="${root}/static/js/jquery-migrate-1.2.1.min.js"></script>
<script src="${root}/static/js/bootstrap.js"></script>
<%-- <script src="${root}/static/js/jquery.validate.js"></script> --%>
<script src="${root}/static/js/select2.js"></script>
<script src="${root}/static/js/bootstrap/bootbox/bootbox.js"></script>
<script src="${root}/static/js/jquery.i18n.properties-min-1.0.9.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key="common.message.project"/></title>
<style type="text/css">
.load_title img {display: none;}
.load_title {margin-top: -220px;}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$(".js-example-basic-single").select2();
	});
</script>
</head>
<body class="load">
	<div class="load_bg">
		<div class="load_title">
			<p style="/* display:none; */"><img src="${root }/static/images/logo.png"/></p>
			<fmt:message key="common.message.project"/>
		</div>
		<div class="load_box">
			<form role="form" id="loginForm" action="${root }/security/login.action" method="post">
				<input type="hidden" id="user.logLocation" name="user.logLocation" />
				<div class="form-group">
					<input type="text" id="user.userAccount" name="user.userAccount" class="form-control user" value="${user.userAccount }">
				</div>
				<div class="form-group">
					<input type="password" id="user.userPassword" name="user.userPassword" class="form-control password" value="${user.userPassword }">
				</div>
				<div class="form-group">
					<select class="js-example-basic-single form-control lang" id="language" name="language">
						<%--
						<option value="en_US" <c:if test="${userLocale == 'en_US' }">selected</c:if> ><fmt:message key="common.login.language.en"/></option>
						<option value="zh_CN" <c:if test="${userLocale == 'zh_CN' }">selected</c:if> ><fmt:message key="common.login.language.cn"/></option>
						 --%>
						 <%--登录页面只用英文 --%>
						<option value="en_US" selected><fmt:message key="common.login.language.en"/></option>
						<option value="zh_CN" ><fmt:message key="common.login.language.cn"/></option>
					</select>
				</div>
				<button type="submit" id="loginButton" class="btn btn-primary btn-lg btn-block"><fmt:message key="common.button.login"/></button>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
$(function() {
	initJqueryI18n();
	
	if("${param.sessionOutStatus }" == 'isOnline') {
		bootbox.alert($.i18n.prop("common.message.user.isonline"), function(){
			focusInput();
		});
	}
	if("${param.sessionOutStatus }" == 'timeout') {
		bootbox.alert($.i18n.prop("common.message.session.timeout"), function(){
			focusInput();
		});
	}
	if("${message }") {
		bootbox.alert("${message }", function(){
			focusInput();
		});
	}
	
	getUserLocation();
});
/**
 * 初始化Jquery i18n
 */
function initJqueryI18n(){
	jQuery.i18n.properties({//加载资浏览器语言对应的资源文件
	    name : 'LocalizationResource_center', //资源文件名称
	    path : "i18n/", //资源文件路径
	    mode : 'map', //用Map的方式使用资源文件中的值
	    //language : '${sessionScope.userLocale}',
	    language : 'en_US',
	    callback : function() {
	    }
	});
}
function focusInput(){
	setTimeout(function(){
		try{
			$("#user\\.userAccount, #user\\.userPassword").each(function(){
				if(!this.value) {
					$(this).focus();
					return false;
				}
			});
		}catch(e){}
	},0);
}
function getUserLocation(){
	$("#user\\.logLocation").val('');
}
focusInput();
</script>
</html>