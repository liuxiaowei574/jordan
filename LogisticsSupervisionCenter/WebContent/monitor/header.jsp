<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page deferredSyntaxAllowedAsLiteral="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@page import="java.util.*"%>
<%-- <fmt:setBundle basename="i18n.messages" var="commonBundle"/> --%>

<c:set var="iconpath" value="iconpath" />
<%-- <c:set var="root" value="${pageContext.request.contextPath}" /> --%>
<c:set var="basePath"
	value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />
<c:set var="httpHeader"
	value="http://${pageContext.request.serverName}:${pageContext.request.serverPort}" />

<link type="text/css" rel="stylesheet" href="${root}/static/css/bootstrap.css" />
<link rel="stylesheet" href="${root}/static/css/select2.css" />
<link rel="stylesheet" href="${root}/static/css/style.css" />
<script type="text/javascript" src="${root}/static/js/jquery.min.js"></script>
<script src="${root}/static/js/jquery-migrate-1.2.1.min.js"></script>
<script src="${root}/static/js/bootstrap.js"></script>
<script src="${root}/static/js/jquery.validate.js"></script>
<script src="${root}/static/js/messages_zh_CN.js"></script>
<script src="${root}/static/js/select2.js"></script>

<script type="text/javascript">
	var root = "${root}";
	var basePath = "${basePath}";
	var httpHeader = "${httpHeader}";
</script>
