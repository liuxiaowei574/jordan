<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="system.user.list.title" /></title>
</head>
<body>
	<%--行程请求推送通知页面 --%>
	<%@ include file="../../include/tripMsgModal.jsp"%>
	<%@ include file="../../include/left.jsp"%>
	<div class="row site">
		<div class="wrapper-content margint95 margin60">
			<%--导航 --%>
			<c:set var="pageName"><fmt:message key="WarehouseSensor.list.title" /></c:set>
			<jsp:include page="../../include/navigation.jsp" >
				<jsp:param value="${pageName }" name="pageName"/>
			</jsp:include>

			<!-- Modal 传感器添加模态框-->
			<div class="modal  add_user_box" id="addSensorModal" tabindex="-1"
				role="dialog" aria-labelledby="addSensorModal">
				<div class="modal-dialog" role="document">
					<div class="modal-content"></div>
				</div>
			</div>
			<!-- /Modal -->


			<!-- Modify Modal子锁修改模态框 -->
			<div class="modal  add_user_box" id="updateSensorModal" tabindex="-1"
				role="dialog" aria-labelledby="updateSensorModal">
				<div class="modal-dialog" role="document">
					<div class="modal-content"></div>
				</div>
			</div>
			<!-- Modify Modal -->
			<div class="profile profile_box02">
				<div class="tab-content m-b">
					<div class="tab-cotent-title">
						<fmt:message key="WarehouseSensor.list.title" />
					</div>
					<div class="search_form">
						<form class="form-horizontal row" id="SensorForm" action=""
							onsubmit="return false;">
							<div class="form-group col-md-4">
								<label class="col-sm-4 control-label"><fmt:message
										key="WarehouseSensor.sensorNumber" /></label>
								<div class="col-sm-8">
									<input type="text" id="s_sensorNumber" name="s_sensorNumber"
										class="form-control">
								</div>
							</div>
							<c:if test="${filter}">
								<div class="col-md-4">
									<label for="roleIds" class="col-sm-4 control-label"><fmt:message
											key="WarehouseSensor.belongTo" /></label>
									<div class="col-sm-8">
										<select style="" id="s_belongTo" name="s_belongTo"
											class="form-control">
											<option value=""></option>
											<c:forEach var="SystemDepartmentBO" items="${sensorMgmtList}">
												<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</c:if>
							<div class="col-md-4">
								<label class="col-sm-4 control-label"><fmt:message
										key="WarehouseSensor.sensorStatus" /></label>
								<div class="col-sm-8">
									<%-- <select style="font-size: 10px" id="s_sensorStatus"
										name="s_sensorStatus" class="form-control">
										<option value=""><fmt:message key="please.choose" /></option>
										<option value="0"><fmt:message key="device.Scrap" /></option>
										<option value="1"><fmt:message key="device.Normal" /></option>
										<option value="3"><fmt:message key="device.Inway" /></option>
										<option value="4"><fmt:message key="device.Maintain" /></option>
									</select> --%>

									<s:select name="s_sensorStatus" emptyOption="true"
										cssClass="form-control" theme="simple"
										list="@com.nuctech.ls.model.util.DeviceStatus@values()"
										listKey="text" listValue="key" value="">
									</s:select>
								</div>
							</div>

							<div class="form-group">
								<div class="col-sm-offset-9 col-md-3">
									<button type="submit" class="btn btn-danger"
										onclick="doSearch();">
										<fmt:message key="common.button.query" />
									</button>
									<button id="resetSearchBtn" type="button" form="SensorForm"
										class="btn btn-darch" onclick="doRest();">
										<fmt:message key="common.button.reset" />
									</button>
								</div>
							</div>
						</form>
						<input type="file" id="fileToUpload" name="file"
							style="filter: alpha(opacity = 0); opacity: 0; width: 0; height: 0;" />
					</div>
				</div>
				<!--/search form-->
				<div class="tab-content">
					<div class="tab-cotent-title">
						<div class="Features pull-right">
							<ul>
								<li><a id="addSensorBtn" class="btn btn-info btn-weight"><fmt:message
											key="common.button.add" /></a></li>
								<li><a id="deletea" class="btn btn-info btn-weight"
									onclick="delObject();"><fmt:message
											key="common.button.delete" /></a></li>
								<li><a id="editSensorBtn" class="btn btn-info btn-weight"><fmt:message
											key="common.button.modify" /></a></li>
								<li><a id="downloadBtn" class="btn btn-info btn-weight"
									onclick="downloadTem();"><fmt:message
											key="WarehouseElock.download.template" /></a></li>
								<li><a id="upload" class="btn btn-info btn-weight"><fmt:message
											key="WarehouseElock.batch.import" /></a></li>
								<c:if test="${systemModules.isDispatchOn()}">
									<li><a href="${root}/dispatch/toList.action"
										; id="dispatch" class="btn btn-info btn-weight"><fmt:message
												key="common.button.dispatch" /></a></li>
								</c:if>
							</ul>
						</div>
						<fmt:message key="WarehouseSensor.list" />
					</div>
					<div class="search_table">
						<div>
							<table id="table"></table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		var selections = [];
		var $table = $('#table');
		//刷新tale
		$(window).resize(function() {
			$table.bootstrapTable("resetView");
		});
		function doSearch() {
			var params = $table.bootstrapTable('getOptions');
			params.queryParams = function(params) {
				//遍历form 组装json  
				$.each($("#SensorForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params[field.name] = field.value;
				});
				return params;
			}
			$table.bootstrapTable('refresh', params);
		}

		function resetQuery() {
			$table.bootstrapTable('refresh', {});
		}
		function getIdSelections() {
			return $table.bootstrapTable('getSelections');
		}
		$(function() {
			//设置传入参数
			function queryParams(params) {
				//遍历form 组装json  
				$.each($("#SensorForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params += "&" + field.name + "=" + field.value;
				});
				return params;
			}
			$table.bootstrapTable({
				url : '${root}/sensorMgmt/list.action',

				//height: $(window).height() - 200,
				clickToSelect : true,
				showRefresh : false,
				search : false,
				showColumns : false,
				showExport : false,
				striped : true,
				//height : "100%",
				method : "get",
				idfield : "sensorId",
				sortName : "sensorNumber",
				sortable : true,
				cache : false,
				pagination : true,
				sidePagination : 'server',
				pageNumber : 1,
				pageSize : 10,
				pageList : [ 10, 20, 30 ],
				columns : [ {
					checkbox : true
				}, {
					field : 'sensorNumber',
					title : $.i18n.prop('WarehouseSensor.sensorNumber'),
					sortable : true
				}, {
					field : 'organizationName',//组织机构中的"机构名称"字段
					title : $.i18n.prop('WarehouseSensor.belongTo'),
					sortable : true
				}, {
					field : 'sensorStatus',
					title : $.i18n.prop('WarehouseSensor.sensorStatus'),
					formatter : sensorStatusFormat,
					sortable : true
				}, ]
			});

			//添加Modal调用方法
			$("#addSensorBtn").click(function() {
				var url = "${root}/sensorMgmt/addModal.action";
				$('#addSensorModal').removeData('bs.modal');
				$('#addSensorModal').modal({
					remote : url,
					show : false,
					backdrop : 'static',
					keyboard : false
				});
			});

			$('#addSensorModal').on('loaded.bs.modal', function(e) {
				$('#addSensorModal').modal('show');
			});
			//模态框登录判断
			$('#addSensorModal').on('show.bs.modal', function(e) {
				var content = $(this).find(".modal-content").html();
				needLogin(content);
			});

			//编辑Modal调用方法
			$("#editSensorBtn")
					.click(
							function() {
								var ids = $.map($table
										.bootstrapTable('getSelections'),
										function(row) {
											return row.sensorId
										});
								if (ids.length == 0) {
									bootbox.alert($.i18n
											.prop("sensor.modify.choose"));
								} else if (ids.length > 1) {
									bootbox.alert($.i18n
											.prop("sensor.modify.choose.only"));
								} else {
									var url = "${root}/sensorMgmt/editModal.action?warehouseSensorBO.sensorId="
											+ ids;
									$('#updateSensorModal').removeData(
											'bs.modal');
									$('#updateSensorModal').modal({
										remote : url,
										show : false,
										backdrop : 'static',
										keyboard : false
									});

								}
							});

			$('#updateSensorModal').on('loaded.bs.modal', function(e) {
				$('#updateSensorModal').modal('show');
			});
			//模态框登录判断
			$('#updateSensorModal').on('show.bs.modal', function(e) {
				var content = $(this).find(".modal-content").html();
				needLogin(content);
			});
		});
	</script>



	<!-- 删除记录 -->
	<script type="text/javascript">
		function delObject() {
			var list = $('#table').bootstrapTable('getSelections'); //获取表的行
			var sensorStatus = new Array();
			for ( var o in list) {
				sensorStatus.push(list[o].sensorStatus);
			}
			var ids = $.map($table.bootstrapTable('getSelections'), function(
					row) {
				return row.sensorId
			});
			var sensorIds = ids.join(",");
			if (list.length <= 0) {
				bootbox.alert($.i18n.prop("eseal.delete.choose"));
				return;
			} else if (sensorStatus.indexOf('2') != -1) {
				bootbox.alert($.i18n.prop("sensor.delete.not.Inway"));
				return;
			} else {
				bootbox
						.confirm(
								$.i18n.prop("are.you.sure"),
								function(result) {
									if (result) {
										$('#table').bootstrapTable('refresh',
												{});
										var ajaxUrl = "${root}/sensorMgmt/delSensorById.action"; /*路径改过，原先是相对路径 */
										$
												.ajax({
													url : ajaxUrl,
													type : "post",
													dataType : "json",
													data : {
														sensorIds : sensorIds
													},
													success : function(data) {
														if (data == true) {
															bootbox
																	.alert($.i18n
																			.prop("sensor.delete.success"));
															$('#table')
																	.bootstrapTable(
																			'refresh',
																			{});
														}
													}
												});
									}
								})
			}
		}
	</script>
	<script type="text/javascript">
		function doRest() {
			$("#SensorForm")[0].reset();
		}
	</script>
	<!-- 将原先从数据库中查出来的值换为中文 -->
	<script type="text/javascript">
		function sensorStatusFormat(value, row, index) {
			var show;
			if (value == '0') {
				show = $.i18n.prop('DeviceStatus.Scrap');
			} else if (value == '1') {
				show = $.i18n.prop('DeviceStatus.Normal');
			} else if (value == '2') {
				show = $.i18n.prop('DeviceStatus.Inway');
			} else if (value == '3') {
				show = $.i18n.prop('DeviceStatus.Destory');
			} else if (value == '4') {
				show = $.i18n.prop('DeviceStatus.Maintain');
			} else {
				show = '--'
			}
			return [ show ].join('');
		}
	</script>

	<!-- 下载excel模板 -->
	<script type="text/javascript">
		function downloadTem() {
			//var filePath = "${root}/template/Elock_${sessionScope.userLocale}.xlsx";
			var filePath = "${root}/WebContent/template";//(excel模板存储的位置)
			var url = "${root}/sensorMgmt/downTemplate.action?filePath="
					+ filePath;
			window.location.href = url;
		}
	</script>
	<!-- 批量导入excel数据 -->
	<script type="text/javascript">
		$(function() {
			//点击打开文件选择器  
			$("#upload").on('click', function() {
				$('#fileToUpload').click();
			});
			var url = "${root}/sensorMgmt/batchImportexcel.action";
			//选择文件之后执行上传  
			$('#fileToUpload')
					.on(
							'change',
							function() {
								$
										.ajaxFileUpload({
											url : url,
											secureuri : false,
											fileElementId : 'fileToUpload',//file标签的id  
											dataType : 'json',//返回数据的类型  
											data : {
												name : 'logan'
											},//一同上传的数据  
											success : function(data, status) {
												if (data.result == "true") {
													bootbox
															.alert(
																	$.i18n
																			.prop("sensor.batch.import.data.sucess"),
																	function() {
																		$(
																				'#table')
																				.bootstrapTable(
																						'refresh',
																						{});
																	});
												}
												if (data.result == "excelEnd") {
													bootbox
															.alert($.i18n
																	.prop("please.select.excel.type.file"));
												}

												if (data.result == "false") {
													bootbox
															.alert($.i18n
																	.prop("sensor.batch.import.data.fail"));
												}
												
												if(data.result=="existed"){
								            		bootbox.alert($.i18n.prop("sensor.batch.import.sensornumber.existed"));
								            	}
											},
											error : function(data, status, e) {
											}
										});
							});

		});
	</script>
</body>
</html>