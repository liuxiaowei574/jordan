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
			<!--手动选择关锁，模态框显示-->
			<div class="modal  add_user_box" id="addElockModal" tabindex="-1"
				data-toggle="modal" role="dialog"
				aria-labelledby="addElockModalTitle">
				<div class="modal-dialog" role="document">
					<div class="modal-content"></div>
				</div>
			</div>
			<!-- /Modal -->

			<!-- 手动选择子锁，模态框显示-->
			<div class="modal  add_user_box" id="addEsealModal" tabindex="-1"
				data-toggle="modal" role="dialog" aria-labelledby="addEsealModalTitle">
				<div class="modal-dialog" role="document">
					<div class="modal-content"></div>
				</div>
			</div>
			<!-- /Modal -->

			<!--手动选择传感器，模态框显示-->
			<div class="modal  add_user_box" id="addSensorModal" tabindex="-1"
				data-toggle="modal" role="dialog" aria-labelledby="addSensorModalTitle">
				<div class="modal-dialog" role="document">
					<div class="modal-content"></div>
				</div>
			</div>
			<!-- /Modal -->
			
			<!--巡逻队模态框显示-->
			<div class="modal  add_user_box" id="addPatrolModal" tabindex="-1"
				data-toggle="modal" role="dialog" aria-labelledby="addPatrolModalTitle">
				<div class="modal-dialog" role="document">
					<div class="modal-content"></div>
				</div>
			</div>
			<!-- /Modal -->

			<!--控制中心模态框显示-->
			<div class="modal  add_user_box" id="addControlRoomModal" tabindex="-1"
				data-toggle="modal" role="dialog" aria-labelledby="addControlRoomModalTitle">
				<div class="modal-dialog" role="document">
					<div class="modal-content"></div>
				</div>
			</div>
			<!-- /Modal -->
			
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
								<fmt:message key="Demand.port" />
								：${toPort}&nbsp&nbsp&nbsp&nbsp&nbsp
								<fmt:message key="elock.number" />
								：${deviceNumber}&nbsp&nbsp&nbsp&nbsp&nbsp
								<fmt:message key="eseal.number" />
								：${esealNumber}&nbsp&nbsp&nbsp&nbsp&nbsp
								<fmt:message key="sensor.number" />
								：${sensors}
							</div>
						</div>
					</div>
					<div class="tab-content m-b">
						<!--关锁调度   -->
						<div class="row">
							<div class="col-md-12 my_news">
								<div class="tab-cotent-title">
									<c:if test="${param.viewDispatchIds == null }">
									<div class="Features pull-right">
										<ul>
											<li><a id="RandomElockBtn" class="btn btn-info"
												onclick="randomSearch()"><fmt:message
														key="random.select.elock" /></a></li>
											<li><a id="addElockBtn" class="btn btn-info"><fmt:message
														key="manual.select.elock" /></a></li>
											<!-- <li><a id="button" class="btn btn-info">移除</a></li> -->
										</ul>
									</div>
									</c:if>
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
									<c:if test="${param.viewDispatchIds == null }">
									<div class="Features pull-right">
										<ul>
											<li><a id="RandomElockBtn" class="btn btn-info"
												onclick="randomEsealSearch()"><fmt:message
														key="random.select.eseal" /></a></li>
											<li><a id="addEsealBtn" class="btn btn-info"><fmt:message
														key="manual.select.eseal" /></a></li>
										</ul>
									</div>
									</c:if>
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
									<c:if test="${param.viewDispatchIds == null }">
									<div class="Features pull-right">
										<ul>
											<li><a id="RandomSensorBtn" class="btn btn-info"
												onclick="randomSensorSearch()"><fmt:message
														key="random.select.sensor" /></a></li>
											<li><a id="addSensorBtn" class="btn btn-info"><fmt:message
														key="manual.select.sensor" /></a></li>
										</ul>
									</div>
									</c:if>
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
							<c:if test="${param.viewDispatchIds == null }">
							<button type="button" class="btn btn-danger"
								onclick="deviceDispatchSuccess();">
								<fmt:message key="common.button.dispatch" />
							</button>
							<button id="cancelDispacthBtn" type="button"
								class="btn btn-darch" onclick="cancel();">
								<fmt:message key="button.cancel" />
							</button>
							</c:if>
							<c:if test="${param.viewDispatchIds != null }">
							<button type="button" class="btn btn-danger"
								onclick="javascript: history.back(-1);">
								<fmt:message key="common.button.back" />
							</button>
							</c:if>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- 关锁调度 -->
	<script type="text/javascript">
		var selections = [];
		var $elockTable = $('#elockTable');
		var $elockTableBootstraptable;
		function randomSearch() {

			var a = "${deviceNumber}"
			var params = $elockTable.bootstrapTable('getOptions');
			params.url = "${root}/dispatch/RandomElockChoose.action?a=${deviceNumber}";
			params.queryParams = function(params) {
				//遍历form 组装json  
				$.each($("#RandomForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params[field.name] = field.value;
				});
				return params;
			}
			$elockTable.bootstrapTable('refresh', params);
		}

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
		//添加Modal调用方法
		$("#addElockBtn").click(function() {
							var list = $elockTable.bootstrapTable('getData')
							var num = "";
							for ( var o in list) {
								num = num + list[o].ELOCK_NUMBER + ",";
							}
							var url = "${root}/dispatch/addDispatchModal.action?numbers="
									+ num;
							$('#addElockModal').removeData('bs.modal');
							$('#addElockModal').modal({
								remote : url,
								show : false,
								backdrop : 'static',
								keyboard : false
							});
							$('#addElockModal').on('loaded.bs.modal', function(e) {
								$('#addElockModal').modal('show');
							});
							//模态框登录判断
							$('#addElockModal').on('show.bs.modal', function(e) {
								var content = $(this).find(".modal-content").html();
								needLogin(content);
							});

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

		//添加子锁Modal调用方法
		$("#addEsealBtn").click(function() {
							var list = $esealTable.bootstrapTable('getData')
							var num = "";
							for ( var o in list) {
								num = num + list[o].ESEAL_NUMBER + ",";
							}
							var url = "${root}/dispatch/addEsealDispatchModal.action?numbers="
									+ num;
							$('#addElockModal').removeData('bs.modal');
							$('#addEsealModal').modal({
								remote : url,
								show : true,
								backdrop : 'static',
								keyboard : false
							});
							
							$('#addEsealModal').on('hidden.bs.modal', function(e) {
							});
							
							$('#addEsealModal').on('loaded.bs.modal', function(e) {
								$('#addEsealModal').modal('show');
							});
							//模态框登录判断
							$('#addEsealModal').on('show.bs.modal', function(e) {
								var content = $(this).find(".modal-content").html();
								needLogin(content);
							});

						});
	</script>


	<!-- 传感器调度 -->
	<script type="text/javascript">
		var $sensorTable = $('#sensorTable');
		var $sensorTableBootstraptable;
		function randomSensorSearch() {
			var c = "${sensors}"
			//alert(c)
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
		//添加传感器Modal调用方法
		$("#addSensorBtn")
				.click(
						function() {
							var list = $sensorTable.bootstrapTable('getData')
							var num = "";
							for ( var o in list) {
								num = num + list[o].SENSOR_NUMBER + ",";
							}
							var url = "${root}/dispatch/addSensorDispatchModal.action?numbers="
									+ num;
							$('#addSensorModal').modal({
								remote : url,
								show : false,
								backdrop : 'static',
								keyboard : false
							}).modal("show");

							$('#addSensorModal').on('hidden.bs.modal', function(e) {
							});
							$('#addSensorModal').on('loaded.bs.modal', function(e) {
								$('#addSensorModal').modal('show');
							});
							//模态框登录判断
							$('#addSensorModal').on('show.bs.modal', function(e) {
								var content = $(this).find(".modal-content").html();
								needLogin(content);
							});
						});
	</script>



	<!--选好关锁,子锁,传感器后点击调度按钮方法  -->
	<script type="text/javascript">
		var $elockTable = $('#elockTable');
		var $esealTable = $('#esealTable');
		var $sensorTable = $('#sensorTable');
		function deviceDispatchSuccess() {
			var serialize = $("#RandomForm").serialize();/* 可以获取到选择关锁，子锁，传感器的数量 */
			/* 获取调配主键 */
			var dispacthId = "${dispatchIds}";

			/* 获取关锁号 */
			var list = $elockTable.bootstrapTable('getData')
			var ecount = list.length;/* 获取表格中已经选中的关锁数量和申请表中的数量作比较(校验) */
			var e = "${deviceNumber}"/* 申请表中的关锁数量 */
			var num = "";
			for ( var o in list) {
				num = num + list[o].ELOCK_NUMBER + ",";

			}

			/* 获取关锁Id */
			var elockId = "";
			for ( var o in list) {
				elockId = elockId + list[o].ELOCK_ID + ",";
			}

			/*获取子锁号 */
			var esealList = $esealTable.bootstrapTable('getData')
			var esealCount = esealList.length;
			var eseal = "${esealNumber}"

			var esealNum = "";
			for ( var o in esealList) {
				esealNum = esealNum + esealList[o].ESEAL_NUMBER + ",";
			}

			/* 获取子锁Id */
			var esealId = "";
			for ( var o in esealList) {
				esealId = esealId + esealList[o].ESEAL_ID + ",";
			}
			/* 获取传感器Id */
			var sensorList = $sensorTable.bootstrapTable('getData')
			var sensorCount = sensorList.length;
			var sensor = "${sensors}"
			var sensorId = "";
			for ( var o in sensorList) {
				sensorId = sensorId + sensorList[o].SENSOR_ID + ",";
			}
			/* 获取传感器编号 */
			var sensorNum = "";
			for ( var o in sensorList) {
				sensorNum = sensorNum + sensorList[o].SENSOR_NUMBER + ",";
			}

			/*添加校验，调度的数量要和申请表中的数量相等  */
			if (ecount != e) {
				bootbox.alert($.i18n.prop('please.input.elock.number'),function(){
				});
			} else if (esealCount != eseal) {
				bootbox.alert($.i18n.prop('please.input.eseal.number'),function(){
				});
			} else if (sensor != sensorCount) {
				bootbox.alert($.i18n.prop('please.input.sensor.number'),function(){
				});
			} else {
				bootbox.confirm($.i18n.prop('are.you.sure.dispatch'), function(
						result) {
					if(result==true){
						var url = "${root}/dispatch/deviceDispatch.action?numbers="
							+ num + "&elockId=" + elockId + "&esealNum="
							+ esealNum + "&dispacthId=" + dispacthId
							+ "&esealId=" + esealId + "&sensorId=" + sensorId
							+ "&sensorNum=" + sensorNum;
						//直接推送给巡逻队
					/*  $.post(url, serialize, function(data) {
						 if(!needLogin(data)) {
							 var urla = "${root}/dispatchSendMsg/addPatrolModal.action?dispacthId="+dispacthId;
							 $('#addPatrolModal').modal({
								remote : urla,
								show : false,
								backdrop : 'static',
								keyboard : false
							}).modal("show");
							$('#addPatrolModal').on('loaded.bs.modal', function(e) {
								$('#addPatrolModal').modal('show');
							});
							//模态框登录判断
							$('#addPatrolModal').on('show.bs.modal', function(e) {
								var content = $(this).find(".modal-content").html();
								needLogin(content);
							});
							 }
						}, "json");  */
						//先推送给controlRoom的用户
						$.post(url, serialize, function(data) {
							 if(!needLogin(data)) {
								 var urla = "${root}/dispatchSendMsg/addControlUserModal.action?dispacthId="+dispacthId;
								 $('#addPatrolModal').modal({
									remote : urla,
									show : false,
									backdrop : 'static',
									keyboard : false
								}).modal("show");
								$('#addControlRoomModal').on('loaded.bs.modal', function(e) {
									$('#addControlRoomModal').modal('show');
								});
								//模态框登录判断
								$('#addControlRoomModal').on('show.bs.modal', function(e) {
									var content = $(this).find(".modal-content").html();
									needLogin(content);
								});
								 }
							}, "json");
					}
				})
			}
			
		}
	</script>
	<!--从模态框往表格中加载数据  -->
	<script type="text/javascript">
		function addElockTable(data) {
			$elockTableBootstraptable.bootstrapTable('load', data)
		}

		function addEsealTable(data) {
			$esealTableBootstraptable.bootstrapTable('load', data)
		}

		function addSensorTable(data) {
			$sensorTableBootstraptable.bootstrapTable('load', data)
		}
	</script>

	<!-- 取消按钮方法 -->
	<script type="text/javascript">
		function cancel() {
			var url = "${root}/dispatch/toList.action"
			window.location.href = url;
		}
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