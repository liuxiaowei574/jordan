<%@page import="jcifs.util.transport.Request"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="WarehouseElock.list.title"/></title>
</head>
<body>
	<%@ include file="../../include/left.jsp"%>
	<div class="row site">
		<div class="wrapper-content margint95 margin60">
			
			<!--存放 三个 表格  -->
			<form role="form" id="RandomForm"
				action="${root}/dispatch/deviceDispatch.action" method="post">
				<div class="profile profile_box02">
					<div class="tab-content m-b">
						<div class="xqka form-horizontal">
							<div class="tab-cotent-title">
								<fmt:message key="Demand.situation" />
							</div>
							<br>
							<div style="float:right;">
								<fmt:message key="warehouse.dispatch.port"/>
								：${fromPort}&nbsp&nbsp&nbsp&nbsp&nbsp
								<fmt:message key="Demand.port" />
								：${applicationPort}&nbsp&nbsp&nbsp&nbsp&nbsp
								<fmt:message key="elock.number" />
								：${warehouseDeviceDispatchBO.deviceNumber}&nbsp&nbsp&nbsp&nbsp&nbsp
								<fmt:message key="eseal.number" />
								：${warehouseDeviceDispatchBO.esealNumber}&nbsp&nbsp&nbsp&nbsp&nbsp
								<fmt:message key="sensor.number" />
								：${warehouseDeviceDispatchBO.sensorNumber}
							</div>
						</div>
					</div>
					<div class="tab-content m-b">
						<!--关锁调度   -->
						<div class="row">
							<div class="col-md-12 my_news">
								<div class="tab-cotent-title">
									<fmt:message key="elock.dispatch" />
								</div>
								<div class="search_table">
									<div>
										<table id="elockTable" class="table table-bordered table-striped table-hover">
										<thead>
										</thead> 
										<tbody>
										<c:forEach var="e" items="${elockDetailList}">
													<tr>
														<td>${e.ELOCK_NUMBER}</td>
														<td>${e.ORGANIZATION_NAME}</td>
														<td>${e.SIM_CARD}</td>
														<td>${e.INTERVAL}</td>
														<td>${e.GATEWAY_ADDRESS}</td>
														<td>${e.ELOCK_STATUS}</td>
													</tr>
												</c:forEach>
										</tbody>
												
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="tab-content m-b">
						<!--子锁调度   -->
						<div class="row">
							<div class="col-md-12 my_news">
								<div class="tab-cotent-title">
									<fmt:message key="eseal.dispatch" />
								</div>
								<div class="search_table">
									<div>
										<table class="table table-bordered table-striped" id="esealTable" >
										<thead></thead> 
										<tbody>
												<c:forEach var="e" items="${esealDetailList}">
													<tr>
														<td>${e.ESEAL_NUMBER}</td>
														<td>${e.ORGANIZATION_NAME}</td>
														<td>${e.ESEAL_STATUS}</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>

					</div>

					<div class="tab-content m-b">
						<!--传感器调度   -->
						<div class="row">
							<div class="col-md-12 my_news">
								<div class="tab-cotent-title">
									<fmt:message key="sensor.dispatch" />
								</div>
								<div class="search_table">
									<div>
										<table id="sensorTable" class="table table-bordered table-striped">
										<thead></thead> 
												<c:forEach var="s" items="${sensorDetailList}">
													<tr>
														<td>${s.SENSOR_NUMBER}</td>
														<td>${s.ORGANIZATION_NAME}</td>
														<td>${s.SENSOR_STATUS}</td>
													</tr>
												</c:forEach>
										</table>

									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-10 col-md-3">
							<button type="button" class="btn btn-danger"
								onclick="javascript: history.back(-1);">
								<fmt:message key="common.button.back" />
							</button>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- 关锁调度 -->
	<script type="text/javascript">
		var $elockTable = $('#elockTable');
		var $elockTableBootstraptable;
		$(function() {
			$elockTable.bootstrapTable({
				//url:'${root}/dispatch/RandomChoose.action?a=${deviceNumber}',
				pagination : true,
				pageSize : 5,
				smartDisplay : false,
				pageList : [ 10, 20, 30 ],
				maintainSelected : true,
				 columns : [{
					field : 'ELOCK_NUMBER',
					title : $.i18n.prop('WarehouseElock.elockNumber')
				}, {
					field : 'ORGANIZATION_NAME',//组织机构表里面的"机构名称"
					title : $.i18n.prop('WarehouseElock.belongTo')
				}, {
					field : 'SIM_CARD',
					title : $.i18n.prop('WarehouseElock.simCard')
				}, {
					field : 'INTERVAL',
					title : $.i18n.prop('WarehouseElock.interval')
				}, {
					field : 'GATEWAY_ADDRESS',
					title : $.i18n.prop('WarehouseElock.gatewayAddress')
				}, {
					field : 'ELOCK_STATUS',
					title : $.i18n.prop('WarehouseElock.elockStatus'),
					formatter : elockStatus
				} ], 
			});
			$elockTableBootstraptable = $('#elockTable').bootstrapTable();

		});
	</script>


	<!-- 子锁调度 -->
	<script type="text/javascript">
		var $esealTable = $('#esealTable');
		var $esealTableBootstraptable;
		function randomEsealSearch() {
			var b = "${esealNumber}"
			//alert(b)
			var params = $esealTable.bootstrapTable('getOptions');
					params.url = '${root}/dispatch/RandomEsealChoose.action?b=${esealNumber}',
					params.queryParams = function(params) {
						//遍历form 组装json  
						$.each($("#RandomForm").serializeArray(), function(i,
								field) {
							console.info(field.name + ":" + field.value + " ");
							//可以添加提交验证                   
							params[field.name] = field.value;
						});
						return params;
					}
			$esealTable.bootstrapTable('refresh', params);
		}
		$(function() {
			//设置传入参数
			function queryParams(params) {
				return params;
			}
			$esealTable.bootstrapTable({
				//url : '${root}/esealMgmt/RandomEsealChoose.action',
				dataType : "json",
				pagination : true,
				pageSize : 5,
				smartDisplay : false,
				pageList : [ 10, 20, 30 ],
				maintainSelected : true,
				columns : [ {
					field : 'ESEAL_NUMBER',
					title : $.i18n.prop('warehouseEsealBO.esealNumber')
				}, {
					field : 'ORGANIZATION_NAME',
					title : $.i18n.prop('warehouseEsealBO.belongTo')
				}, {
					field : 'ESEAL_STATUS',
					title : $.i18n.prop('warehouseEsealBO.esealStatus'),
					formatter :esealStatus
				}]
			});
			$esealTableBootstraptable = $('#esealTable').bootstrapTable();
		});
	</script>


	<!-- 传感器调度 -->
	<script type="text/javascript">
		var $sensorTable = $('#sensorTable');
		var $sensorTableBootstraptable;
		function randomSensorSearch() {
			var c = "${sensors}"
			var params = $sensorTable.bootstrapTable('getOptions');

			params.url = "${root}/dispatch/RandomSensorChoose.action?c="
					+ "${sensors}";

			params.queryParams = function(params) {
				//遍历form 组装json  
				$.each($("#RandomForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params[field.name] = field.value;
				});
				return params;
			}
			$('#sensorTable').bootstrapTable('refresh', params);

		}

		$(function() {
			//设置传入参数
			function queryParams(params) {
				return params;
			}
			$sensorTable.bootstrapTable({
				//url : '${root}/dispatch/RandomSensorChoose.action',
				pagination : true,
				pageSize : 5,
				smartDisplay : false,
				pageList : [ 10, 20, 30 ],
				maintainSelected : true,
				columns : [ {
					field : 'SENSOR_NUMBER',
					title : $.i18n.prop('WarehouseSensor.sensorNumber')
				}, {
					field : 'ORGANIZATION_NAME',
					title : $.i18n.prop('WarehouseSensor.belongTo')
				}, {
					field : 'SENSOR_STATUS',
					title : $.i18n.prop('WarehouseSensor.sensorStatus'),
					formatter : sensorStatus
				}]
			});
			$sensorTableBootstraptable = $('#sensorTable').bootstrapTable();
		});
	</script>
	
	 <script type="text/javascript">
		function elockStatus(value, row, index){
			var show;
			if(value =='0'){
				show = $.i18n.prop('DeviceStatus.Scrap');
			}else if(value =='1'){
				show =$.i18n.prop('DeviceStatus.Normal');
			}else if(value =='2'){
				show = $.i18n.prop('DeviceStatus.Inway');
			}else if(value =='3'){
				show = $.i18n.prop('DeviceStatus.Destory');
			}else if(value =='4'){
				show = $.i18n.prop('DeviceStatus.Maintain');
			}else{
				show = '--'
			}
			return [show].join('');
		}
		
		function esealStatus(value, row, index){
			var show;
			if(value =='0'){
				show = $.i18n.prop('DeviceStatus.Scrap');
			}else if(value =='1'){
				show =$.i18n.prop('DeviceStatus.Normal');
			}else if(value =='2'){
				show = $.i18n.prop('DeviceStatus.Inway');
			}else if(value =='3'){
				show = $.i18n.prop('DeviceStatus.Destory');
			}else if(value =='4'){
				show = $.i18n.prop('DeviceStatus.Maintain');
			}else{
				show = '--'
			}
			return [show].join('');
		}
		
		function sensorStatus(value, row, index){
			var show;
			if(value =='0'){
				show = $.i18n.prop('DeviceStatus.Scrap');
			}else if(value =='1'){
				show =$.i18n.prop('DeviceStatus.Normal');
			}else if(value =='2'){
				show = $.i18n.prop('DeviceStatus.Inway');
			}else if(value =='3'){
				show = $.i18n.prop('DeviceStatus.Destory');
			}else if(value =='4'){
				show = $.i18n.prop('DeviceStatus.Maintain');
			}else{
				show = '--'
			}
			return [show].join('');
		}
		
    </script> 
    <script type="text/javascript">
		var root = "${root}";
	</script>
	<script type="text/javascript" src="${root}/device/dispatchhandle/js/dispatchhandle.js"></script> 
</body>
</html>