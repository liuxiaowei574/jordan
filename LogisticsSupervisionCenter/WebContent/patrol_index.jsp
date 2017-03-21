<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="include/include.jsp" />
<title><fmt:message key="dispatch.patrol.index.title"/></title>
</head>
<body>
<%@ include file="../include/left.jsp"%>

<div class="row site">
<%--调度完成向巡逻队推送 --%>
<div class="modal  add_user_box" id="dispatchMsgModal" tabindex="-1"
		role="dialog" aria-labelledby="dispatchMsgModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
</div>

	<div class="wrapper-content margint95">
		<div class="col-md-11 page_wrapper">
			<div class="wrapper-content">
				<div class="row">
					<div class="col-sm-6">
						<div class="panel panel-default">
							<div class="panel-heading"><fmt:message key="patrol.index.tripStatistic"/></div>
							<div class="panel-body" id="tripNumberDiv">
							</div>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="panel panel-default">
							<div class="panel-heading"><fmt:message key="patrol.index.illegalStatistic"/></div>
							<div class="panel-body" id="illegalProcessDiv">
							</div>
						</div>
					</div>
				</div>
				<div class="row row_three">
					<div class="col-sm-4">
						<div class="panel panel-default">
							<div class="panel-heading heading_ico01"><fmt:message key="patrol.index.messageList"/></div>
							<div class="panel-body ">
								<c:forEach var="notice" items="${noticeList }">
									<div class="media">
										<div class="media-body text-left">
											<h4 class="media-heading">${notice.noticeTitle }</h4>
											${notice.noticeContent }
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
					</div>
					<div class="col-sm-4">
						<div class="panel panel-default">
							<div class="panel-heading heading_ico02"><fmt:message key="patrol.index.monitorVechileList"/></div>
							<div class="panel-body ">
								<table class="table">
									<thead>
										<tr>
											<th><fmt:message key="patrol.index.sequence"/></th>
											<th><fmt:message key="patrol.index.vechileNo"/></th>
											<th><fmt:message key="patrol.index.vechileType"/></th>
											<th><fmt:message key="patrol.index.driver"/></th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<th scope="row">1</th>
											<td>316A1</td>
											<td>cruiser</td>
											<td>James Bond</td>
										</tr>
										<tr>
											<th scope="row">2</th>
											<td>316A2</td>
											<td>Container</td>
											<td>Ja</td>
										</tr>
										<tr>
											<th scope="row">3</th>
											<td>316A3</td>
											<td>Container</td>
											<td>twitter</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
	
					</div>
					<div class="col-sm-4">
						<div class="panel panel-default">
							<div class="panel-heading heading_ico03"><fmt:message key="patrol.index.handledVechileList"/></div>
							<div class="panel-body ">
								<table class="table">
									<thead>
										<tr>
											<th><fmt:message key="patrol.index.sequence"/></th>
											<th><fmt:message key="patrol.index.vechileNo"/></th>
											<th><fmt:message key="patrol.index.vechileType"/></th>
											<th><fmt:message key="patrol.index.driver"/></th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<th scope="row">1</th>
											<td>316A1</td>
											<td>cruiser</td>
											<td>James Bond</td>
										</tr>
										<tr>
											<th scope="row">2</th>
											<td>316A2</td>
											<td>Container</td>
											<td>Ja</td>
										</tr>
										<tr>
											<th scope="row">3</th>
											<td>316A3</td>
											<td>Container</td>
											<td>twitter</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
<script src="${root}/static/js/echarts/echarts.min.js"></script>
<script type="text/javascript">
$(function () {
	var tripNumberDiv = $("#tripNumberDiv")[0];
	var tripCharts = echarts.init(tripNumberDiv);
	tripOption = {
		   /*  title: {
		        text: '行程数量统计'
		    }, */
		    tooltip: {
		        trigger: 'axis'
		    },
		    legend: {
		        data:['<fmt:message key="patrol.index.chart.number"/>']
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    toolbox: {
		        feature: {
		            saveAsImage: {}
		        }
		    },
		    xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: ['<fmt:message key="patrol.index.monday"/>','<fmt:message key="patrol.index.tuesday"/>','<fmt:message key="patrol.index.wednesday"/>','<fmt:message key="patrol.index.thursday"/>','<fmt:message key="patrol.index.friday"/>','<fmt:message key="patrol.index.saturday"/>','<fmt:message key="patrol.index.sunday"/>']
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: [
		        {
		            name:'<fmt:message key="patrol.index.chart.number"/>',
		            type:'line',
		            stack: '<fmt:message key="patrol.index.chart.total"/>',
		            data:[820, 932, 901, 934, 1290, 1330, 1320]
		        }
		    ]
		};
	tripCharts.setOption(tripOption);
	
	var illegalProcessDiv = $("#illegalProcessDiv")[0];
	var illegalProcessCharts = echarts.init(illegalProcessDiv);
	illegalProcessOption = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		        data:['<fmt:message key="patrol.index.chart.illegalNumber"/>']
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis : [
		        {
		            type : 'category',
		            data : ['<fmt:message key="patrol.index.monday"/>','<fmt:message key="patrol.index.tuesday"/>','<fmt:message key="patrol.index.wednesday"/>','<fmt:message key="patrol.index.thursday"/>','<fmt:message key="patrol.index.friday"/>','<fmt:message key="patrol.index.saturday"/>','<fmt:message key="patrol.index.sunday"/>']
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : [
		        {
		            name:'<fmt:message key="patrol.index.chart.illegalNumber"/>',
		            type:'bar',
		            data:[20, 32, 11, 34, 90, 30, 20]
		        }
		    ]
		};

	illegalProcessCharts.setOption(illegalProcessOption);
	

});
</script>
</html>