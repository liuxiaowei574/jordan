<%@page import="jcifs.util.transport.Request"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="WarehouseElock.list.title" /></title>
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
				role="dialog" aria-labelledby="addEsealModalTitle">
				<div class="modal-dialog" role="document">
					<div class="modal-content"></div>
				</div>
			</div>
			<!-- /Modal -->

			<!--手动选择传感器，模态框显示-->
			<div class="modal  add_user_box" id="addSensorModal" tabindex="-1"
				role="dialog" aria-labelledby="addSensorModalTitle">
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
						<div class="tab-cotent-title">需求情况</div>
						<div>需求口岸：${toPort}&nbsp&nbsp&nbsp&nbsp&nbsp   关锁数量：${deviceNumber}&nbsp&nbsp&nbsp&nbsp&nbsp
						   子锁数量：${esealNumber}&nbsp&nbsp&nbsp&nbsp&nbsp 传感器数量：${sensors} </div>
            		</div>
					</div>
					<div class="tab-content m-b">
						<!--关锁调度   -->
						<div class="row">
							<div class="col-md-12 my_news">

								<div class="tab-cotent-title">
									<div class="Features pull-right">
										<ul>
											<li><a id="RandomElockBtn" class="btn btn-info"
												onclick="randomSearch()">随机选择关锁</a></li>
											<li><a id="addElockBtn" class="btn btn-info">手动选择关锁</a></li>
										</ul>
									</div>
									关锁调度
								</div>
								<div class="search_table">
									<div>
										<table id="elockTable">
											<tbody id="elockTbody">
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
									<div class="Features pull-right">
										<ul>
											<li><a id="RandomElockBtn" class="btn btn-info"
												onclick="randomEsealSearch()">随机选择子锁</a></li>
											<li><a id="addEsealBtn" class="btn btn-info">手动选择子锁</a></li>
										</ul>
									</div>
									子锁调度
								</div>
								<div class="search_table">
									<div>
										<table id="esealTable">
											<tBody id=esealId>
												<c:forEach var="e" items="${esealDetailList}">
													<tr>
														 <td>${e.ESEAL_NUMBER}</td>
												         <td>${e.ORGANIZATION_NAME}</td>
												         <td>${e.ESEAL_STATUS}</td>
									  				</tr>
									  			</c:forEach>
											</tBody>
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
									<div class="Features pull-right">
										<ul>
											<li><a id="RandomSensorBtn" class="btn btn-info"
												onclick="randomSensorSearch()">随机选择传感器</a></li>
											<li><a id="addSensorBtn" class="btn btn-info">手动选择传感器</a></li>
										</ul>
									</div>
									传感器调度
								</div>
								<div class="search_table">
									<div>
										<table id="sensorTable">
											<tBody id=sensorId>
												<c:forEach var="s" items="${sensorDetailList}">
													<tr>
														 <td>${s.SENSOR_NUMBER}</td>
												         <td>${s.ORGANIZATION_NAME}</td>
												         <td>${s.SENSOR_STATUS}</td>
												         <td>${s.SENSOR_TYPE}</td>
									  				</tr>
									  			</c:forEach>
											</tBody>
										</table>
										
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-10 col-md-3">
							<button type="button" class="btn btn-danger"
								onclick="deviceDispatchSuccess();">调度</button>
							<button id="cancelDispacthBtn" type="button" class="btn btn-darch"
								onclick="cancel();">取消</button>
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
				pageList : [ "10", "25", "50", "100", "All" ],
				maintainSelected : true,
				columns : [
				/* {
				field: 'ELOCK_ID',
				title: "关锁Id"
				}, */{
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
					title : $.i18n.prop('WarehouseElock.elockStatus')
				} ],
			});
			$elockTableBootstraptable = $('#elockTable').bootstrapTable();

		});
		//添加Modal调用方法
		$("#addElockBtn")
				.click(
						function() {
							var list = $elockTable.bootstrapTable('getData')
							var num = "";
							for ( var o in list) {
								num = num + list[o].ELOCK_NUMBER + ",";
							}
							var url = "${root}/dispatch/addDispatchModal.action?numbers="
									+ num;
							$('#addElockModal').modal({
								remote : url,
								show : false,
								backdrop : 'static',
								keyboard : false
							}).modal("show");

							/* $('#addElockModal').on('hidden.bs.modal', function(e) {
							});  */
							/* $('#addElockModal').on('loaded.bs.modal', function(e) {
								$('#addElockModal').modal('show');
							});  */
						});
	</script>


	<!-- 子锁调度 -->
	<script type="text/javascript">
		var selections = [];
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
				pageList : [ "10", "25", "50", "100", "All" ],
				maintainSelected : true,
				columns : [ {
					field : 'ESEAL_NUMBER',
					title : $.i18n.prop('warehouseEsealBO.esealNumber')
				}, {
					field : 'ORGANIZATION_NAME',
					title : $.i18n.prop('warehouseEsealBO.belongTo')
				}, {
					field : 'ESEAL_STATUS',
					title : $.i18n.prop('warehouseEsealBO.esealStatus')
				}, ]
			});
			$esealTableBootstraptable = $('#esealTable').bootstrapTable();
		});

		//添加子锁Modal调用方法
		$("#addEsealBtn")
				.click(
						function() {
							var list = $esealTable.bootstrapTable('getData')
							var num = "";
							for ( var o in list) {
								num = num + list[o].ESEAL_NUMBER + ",";
							}
							var url = "${root}/dispatch/addEsealDispatchModal.action?numbers="
									+ num;
							$('#addEsealModal').modal({
								remote : url,
								show : false,
								backdrop : 'static',
								keyboard : false
							}).modal("show");

						});
	</script>


	<!-- 传感器调度 -->
	<script type="text/javascript">
		var selections = [];
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
				pageList : [ "10", "25", "50", "100", "All" ],
				maintainSelected : true,
				columns : [ {
					field : 'SENSOR_NUMBER',
					title : $.i18n.prop('WarehouseSensor.sensorNumber')
				}, {
					field : 'ORGANIZATION_NAME',
					title : $.i18n.prop('WarehouseSensor.belongTo')
				}, {
					field : 'SENSOR_STATUS',
					title : $.i18n.prop('WarehouseSensor.sensorStatus')
				}, {
					field : 'SENSOR_TYPE',
					title : $.i18n.prop('WarehouseSensor.sensorType')
				}, ]
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

							$('#addSensorModal').on('hidden.bs.modal',
									function(e) {
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
			debugger;
			for ( var o in sensorList) {
				sensorId = sensorId + sensorList[o].SENSOR_ID + ",";
			}
			/* 获取传感器编号 */
			var sensorNum = "";
			for ( var o in sensorList) {
				sensorNum = sensorNum + sensorList[o].SENSOR_NUMBER + ",";
			}
			/* debugger; */

			/*添加校验，调度的数量要和申请表中的数量相等  */
			if (ecount != e) {
				bootbox.alert("请输入正确的需要调度的关锁数量");
			} else if (esealCount != eseal) {
				bootbox.alert("请输入正确的需要调度的子锁数量");
			} else if (sensor != sensorCount) {
				bootbox.alert("请输入正确的需要调度的传感器数量");
			} else {
				bootbox.confirm("你确定要调度吗？", function(result) {
					var url = "${root}/dispatch/deviceDispatch.action?numbers="
							+ num + "&elockId=" + elockId + "&esealNum="
							+ esealNum + "&dispacthId=" + dispacthId
							+ "&esealId=" + esealId + "&sensorId=" + sensorId
							+ "&sensorNum=" + sensorNum;
					$.post(url, serialize, function(data) {
					}, "json");

				})
			}

			/* var url = "${root}/dispatch/deviceDispatch.action?numbers=num&elockId="+elockId; */
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
	function cancel(){
		var url ="${root}/dispatch/toList.action"
			window.location.href=url;
	}
	
	</script>
</body>
</html>