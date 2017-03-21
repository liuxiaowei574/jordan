<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../include/include.jsp"%>
<title></title>
</head>
<body>
	<%@ include file="../include/left.jsp"%>
	<div class="row site">
		<div class="wrapper-content margint95 margin60">
			<%--导航 --%>
			<c:set var="parentName"><fmt:message key="menu.statistic.analysis"/></c:set>
			<c:set var="pageName"><fmt:message key="statistic.index.hs"/></c:set>
			<jsp:include page="../include/navigation2.jsp" >
				<jsp:param value="${root }/statisitc/toList.action" name="parentUrl"/>
				<jsp:param value="${parentName }" name="parentName"/>
				<jsp:param value="${pageName }" name="pageName"/>
			</jsp:include>
			
			<div class="profile profile_box02">
				<div class="row">
					<div class="tab-content m-b">
						<div class="tab-cotent-title">
							<fmt:message key="statics.jsp.condition" />
						</div>
						<div class="search_form">
							<form class="form-horizontal row" id="searchForm"
								onsubmit="return false;">
								<div class="form-group col-md-5">
									<label class="col-sm-4 control-label"><fmt:message
											key="statistic.index.hs" /></label>
									<div class="col-sm-8">
										<s:select name="goodsType" emptyOption="true"
											cssClass="form-control" theme="simple"
											list="@com.nuctech.ls.model.util.GoodsType@values()"
											listKey="type" listValue="key" value="">
										</s:select>
									</div>
								</div>

								<div class="form-group col-sm-3"></div>
								<div class="form-group col-sm-4">
									<button type="button" class="btn btn-danger" name="a"
										onclick="query();">
										<fmt:message key="common.button.query" />
									</button>
									<button type="button" class="btn btn-darch" onclick="resert();">
										<fmt:message key="common.button.reset" />
									</button>
								</div>
								<div class="clearfix"></div>
							</form>
						</div>
					</div>
					<div class="col-sm-12">
						<div class="panel panel-default">
							<div class="panel-heading">
								<div class="Features pull-right">
									<ul>
										<li><a href="${root}/statisitc/toList.action"
											class="btn btn-info"><fmt:message
													key="common.button.back" /></a></li>
									</ul>
								</div>
								<fmt:message key="statistic.sub.hs" />
							</div>
							<div class="panel-body" id="driverDiv"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="clearfix"></div>
			<div class="profile profile_box02">
				<!--/search form-->
				<!--my result-->
				<div class="tab-content">
					<div class="tab-cotent-title">
						<fmt:message key="statistic.sub.hs" />
					</div>
					<div class="search_table">
						<div>
							<table id="driverTransport"></table>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script src="${root}/static/js/echarts/echarts.min.js"></script>

<script type="text/javascript">
	var $table = $('#driverTransport');
	function chart(x, y) {
		var driverDiv = $("#driverDiv")[0];
		var driverCharts = echarts.init(driverDiv);
		driverOption = {
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			legend : {
				data : [ 'number' ]
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			xAxis : [ {
				type : 'category',
				data : x
			} ],
			yAxis : [ {
				type : 'value'
			} ],
			series : [ {
				name : 'number',
				type : 'bar',
				data : y
			} ]
		};
		driverCharts.setOption(driverOption);
	}
</script>

<script type="text/javascript">
	var goodsType = "";
	function query() {
		//过滤表格
		goodsType = $("#goodsType").val();
		var params = $table.bootstrapTable('getOptions');
		params.url = '${root}/goodsStatisticsAction/toList.action?goodsType='
				+ goodsType;
		$table.bootstrapTable('refresh', params);
		//过滤echar
		search();
	}

	function resert() {
		$("#searchForm")[0].reset();
	}
</script>

<script type="text/javascript">
	$(function() {
		$table.bootstrapTable({
			clickToSelect : true,
			url : '${root}/goodsStatisticsAction/toList.action',
			height : $(window).height() - 300,//固定模态框 的 宽度 
			pagination : true,
			pageSize : 5,
			maintainSelected : true,
			pageList : [ 10, 20, 30 ],
			columns : [ {
				field : 'gtypeName',
				title : $.i18n.prop('statics.jsp.goodtypename'),
				sortable : true
			}, {
				field : 'gtypeNum',
				title : $.i18n.prop('cligti.jsp.number'),
				sortable : true
			} ]
		});
		//初始化日期组件
		$("#form_operateStartTime, #form_operateEndTime").datetimepicker({
			format : "yyyy-mm-dd hh:ii",//ii:分钟
			autoclose : true,
			todayBtn : true,
			language : 'en'
		}).on(
				'changeDate',
				function(ev) {
					var startTime = $('#s_operateStartTime').val();//获得开始时间
					$('#form_operateEndTime').datetimepicker('setStartDate',
							startTime);//设置结束时间（大于开始时间）
				});
		//加载echar数据
		search();
	});

	//表格展示：
	var selections = [];
	//刷新tale
	$(window).resize(function() {
		$table.bootstrapTable("resetView");
	});

	function search() {
		var param = $("#searchForm").serializeArray();
		var url = root + "/goodsStatisticsAction/toList.action";
		$.ajax({
					type : "post",
					url : url,
					dataType : "json",
					async : false,
					cache : false,
					data : {
						goodsType : goodsType,
					},
					success : function(v) {
						var x = [];
						var y = [];
						for (var i = 0; i < v.length; i++) {
							x.push(v[i].gtypeName);
							y.push(v[i].gtypeNum);
						}
						chart(x, y);
					},
					error : function(e, v) {
						bootbox.alert('<fmt:message key="statics.report.cljgtj.fail"/>');
					}
				});

	}
</script>
</html>