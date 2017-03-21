<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/include.jsp" />
<link rel="stylesheet" href="css/style.css">
<title><fmt:message key="statistic.title"/></title>
</head>
<body>
<%@ include file="../include/left.jsp"%>
<div class="row site">
	<div class="wrapper-content margint95 margin60">
		<%--导航 --%>
		<c:set var="pageName"><fmt:message key="menu.statistic.analysis"/></c:set>
		<jsp:include page="../include/navigation.jsp" >
			<jsp:param value="${pageName }" name="pageName"/>
		</jsp:include>
		
		<div class="profile profile_box02">
			<div class="row Statistical">
			    <!-- 车辆监控统计 -->
				<div class="col-sm-4 media">
					<div class="media-left">
						<a href="#"> <img class="media-object"
							src="${root }/statistics/images/ss_03.png" alt="...">
						</a>
					</div>
					<div class="media-body">
						<h4 class="media-heading"><fmt:message key="statistic.index.vechileMonitor"/></h4>
						<p class="number redfont">${vehicleNum}</p>
						<p>
							<a class="btn btn-success" href="${root }/statisitc/cljgtjIndex.action"><fmt:message key="statistic.href"/></a>
						</p>
					</div>
				</div>
				 <!-- 监控行程统计 -->
				<div class="col-sm-4 media">
					<div class="media-left">
						<a href="#"> <img class="media-object"
							src="${root }/statistics/images/ss_05.png" alt="...">
						</a>
					</div>
					<div class="media-body">
						<h4 class="media-heading"><fmt:message key="statistic.index.tripMonitor"/></h4>
						<p class="number redfont">${tripNum}</p>
						<p>
							<a class="btn btn-success" href="${root }/statisitc/jgxctjIndex.action"><fmt:message key="statistic.href"/></a>
						</p>
					</div>
				</div>
				 <!-- 监管车辆分布 -->
				<div class="col-sm-4 media">
					<div class="media-left">
						<a href="#"> <img class="media-object"
							src="${root }/statistics/images/ss_07.png" alt="...">
						</a>
					</div>
					<div class="media-body">
						<h4 class="media-heading"><fmt:message key="statistic.index.vechileMonitorDistributed"/></h4>
						<p class="number redfont">${vehicleNum}</p>
						<p>
							<a class="btn btn-warning" href="${root }/include/vehicleDistribute.jsp"><fmt:message key="statistic.href"/></a>
						</p>
					</div>
				</div>
				 <!-- 设备使用统计 -->
				<div class="col-sm-4 media">
					<div class="media-left">
						<a href="#"> <img class="media-object"
							src="${root }/statistics/images/ss_12.png" alt="...">
						</a>
					</div>
					<div class="media-body">
						<h4 class="media-heading"><fmt:message key="statistic.index.device"/></h4>
						<p class="number redfont">${devicNum}</p>
						<p>
							<a class="btn btn-primary" href="${root }/statisitc/deviceUseList.action"><fmt:message key="statistic.href"/></a>
						</p>
					</div>
				</div>
				 <!-- 用户活跃度统计 -->
				<div class="col-sm-4 media">
					<div class="media-left">
						<a href="#"> <img class="media-object"
							src="${root }/statistics/images/ss_13.png" alt="...">
						</a>
					</div>
					<div class="media-body">
						<h4 class="media-heading"><fmt:message key="statistic.index.user"/></h4>
						<p class="number redfont">${userNum}</p>
						<p>
							<a class="btn btn-success" href="${root }/statisitc/yhhyd.action"><fmt:message key="statistic.href"/></a>
						</p>
					</div>
				</div>
				 <!-- 已对接国家公司统计 -->
				<div class="col-sm-4 media">
					<div class="media-left">
						<a href="#"> <img class="media-object"
							src="${root }/statistics/images/ss_14.png" alt="...">
						</a>
					</div>
					<div class="media-body">
						<h4 class="media-heading"><fmt:message key="statistic.index.country"/></h4>
						<p class="number redfont">${countryNum}</p>
						<p>
							<a class="btn btn-success" href="${root }/statisitc/gjgstj.action"><fmt:message key="statistic.href"/></a>
						</p>
					</div>
				</div>
				 <!-- 运输司机统计 -->
				<div class="col-sm-4 media">
					<div class="media-left">
						<a href="#"> <img class="media-object"
							src="${root }/statistics/images/ss_18.png" alt="...">
						</a>
					</div>
					<div class="media-body">
						<h4 class="media-heading"><fmt:message key="statistic.index.driver"/></h4>
						<p class="number redfont">${driverNum}</p>
						<p>
							<a class="btn btn-success" href="${root }/statistics/yssjtj.jsp"><fmt:message key="statistic.href"/></a>
						</p>
					</div>
				</div>
				 <!-- 设备流动统计 -->
				<c:if test="${systemModules.isDispatchOn() }">
					<div class="col-sm-4 media">
						<div class="media-left">
							<a href="#"> <img class="media-object"
								src="${root }/statistics/images/ss_19.png" alt="...">
							</a>
						</div>
						<div class="media-body">
							<h4 class="media-heading"><fmt:message key="statistic.index.flow"/></h4>
							<p class="number redfont">${devicNum}</p>
							<p>
								<a class="btn btn-success" href="${root }/include/deviceInOut.jsp"><fmt:message key="statistic.href"/></a>
							</p>
						</div>
					</div>
				</c:if>
				 <!-- 货物种类统计 -->
				<div class="col-sm-4 media">
					<div class="media-left">
						<a href="#"> <img class="media-object"
							src="${root }/statistics/images/ss_20.png" alt="...">
						</a>
					</div>
					<div class="media-body">
						<h4 class="media-heading"><fmt:message key="statistic.index.hs"/></h4>
						<p class="number redfont">${goodsTypeNum}</p>
						<p>
							<a class="btn btn-success" href="${root }/statistics/hwzltj.jsp"><fmt:message key="statistic.href"/></a>
						</p>
					</div>
				</div>
				
			</div>
		</div>
		<div class="clearfix"></div>
	</div>
</div>
</body>
</html>