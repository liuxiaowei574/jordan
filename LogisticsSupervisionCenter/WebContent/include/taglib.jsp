<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="root" value="${pageContext.request.contextPath}" />

<c:choose>
	<c:when test="${userLocale=='zh_CN'}">
		<c:set var="cssFolder" value="css/zh_CN" />
		<c:set var="language" value="zh_CN" />
		<fmt:setBundle basename="LocalizationResource_center_zh_CN" />
	</c:when>
	<c:when test="${userLocale=='en_US'}">
		<c:set var="cssFolder" value="css/en_US" />
		<c:set var="language" value="en_US" />
		<fmt:setBundle basename="LocalizationResource_center_en_US" />
	</c:when>
	<c:otherwise>
		<c:set var="cssFolder" value="css/zh_CN" />
		<c:set var="language" value="zh_CN" />
		<fmt:setBundle basename="LocalizationResource_center_zh_CN" />
	</c:otherwise>
</c:choose>

<script type="text/javascript">
	var root = "${root}";
	var language='${language}';
	var locale='${userLocale}'; 
</script>
