<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${root}/static/css/bootstrap.css">
<link rel="stylesheet" href="${root}/static/css/style.css">
<script src="${root}/static/js/jquery.min.js"></script>
<script src="${root}/static/js/jquery.i18n.properties-min-1.0.9.js"></script>
<script src="${root}/static/js/bootstrap.js"></script>
<title>首页</title>
</head>
<body>
<%@ include file="../../include/left.jsp" %>
	<div class="row site">
		<div class="col-md-11 page_wrapper">
            <div class="wrapper-content margint95 body-error">
         			<div class="container">
					<section class="error-wrapper">
						<i class="icon-404"></i>
						<h1><fmt:message key="exception.h2"/></h1>
						<p class="page-404"><fmt:message key="exception.content"/></p>
					</section>
					</div>
            	<div class="clearfix"></div>
            </div>
		</div>
	</div>
</body>
</html>