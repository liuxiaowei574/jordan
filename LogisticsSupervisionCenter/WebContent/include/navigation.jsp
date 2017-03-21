<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./taglib.jsp"%>
<%--二级导航 --%>
<ol class="breadcrumb">
	<li>
		<%--admin角色，主页将来为其他页面 --%>
		<c:choose>
			<c:when test="${sessionUser.roleName == 'admin'}">
				<span class="glyphicon glyphicon-home"></span>&nbsp;
				<fmt:message key="link.main" />
			</c:when>
			<c:otherwise>
				<span class="glyphicon glyphicon-home"></span>&nbsp;
				<a href="${root }/index.jsp"><fmt:message key="link.main" /></a>
			</c:otherwise>
		</c:choose>
	</li>
	<li class="active">${param.pageName }</li>
</ol>

