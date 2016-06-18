<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
<fmt:setBundle basename="i18n.messages" var="commonBundle" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
.tuliDiv {
	border: 1px solid gray;
	min-width: 150px;
	background: #ffffff;
	position: absolute;
	right: 10px;
	bottom: 50px;
	z-index: 10000;
	line-height: 25px;
}

.tuliDiv table {
	width: 100%;
}

.hidden {
	display: none;
}

.show {
	display: "";
}
</style>
</head>
<body>
	<div id="allTuli" class="tuliDiv">
		<table>
			<tr>
				<td><img src="${root}/images/kouan.png" width="25" height="25" /></td>
				<td>口岸</td>
			</tr>
			<tr>
				<td><img src="${root}/images/car_blue.png" width="20"
					height="25" /></td>
				<td>正常车辆</td>
			</tr>
			<tr>
				<td><img src="${root}/images/alarm_vehicle.gif" width="20"
					height="25" /></td>
				<td nowrap="nowrap">报警车辆</td>
			</tr>
			<tr>
				<td align="center" colspan="2">图例</td>
			</tr>
		</table>
	</div>
	<div id="detailTuli" class="tuliDiv hidden">
		<table>
			<tr>
				<td><img src="${root}/images/kouan.png" width="25" height="25" /></td>
				<td>口岸</td>
			</tr>
			<tr>
				<td><img src="${root}/images/car_blue.png" width="20"
					height="25" /></td>
				<td>正常车辆</td>
			</tr>
			<tr>
				<td><img src="${root}/images/alarm_vehicle.gif" width="20"
					height="25" /></td>
				<td nowrap="nowrap">报警车辆</td>
			</tr>
			<tr>
				<td><img src="${root}/images/alarm_red.png" width="23"
					height="25" /></td>
				<td>一级报警</td>
			</tr>
			<tr>
				<td><img src="${root}/images/alarm_yellow.png" width="23"
					height="25" /></td>
				<td>二级报警</td>
			</tr>
			<%-- <tr><td><img src="${root}/gis/alarm_orange.png" width="23" height="25" /></td><td>三级报警</td></tr>
               <tr><td><img src="${root }/gis/alarm_blue.png" width="23" height="25" /></td><td>四级报警</td></tr> --%>
			<tr>
				<td><img src="${root}/images/real.png" width="23" height="15" /></td>
				<td>真实路线</td>
			</tr>
			<tr>
				<td><img src="${root}/images/yuding.png" width="23" height="15" /></td>
				<td>预定义路线</td>
			</tr>
			<tr>
				<td align="center" colspan="2">图例</td>
			</tr>
		</table>
	</div>
</body>
</html>