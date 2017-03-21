<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<%@ include file="../include/include.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="statistic.index.device" /></title>
</head>
<body>
	<%@ include file="../include/left.jsp"%>
	<div class="row site">
		<div class="wrapper-content margint95 margin60">
			<%--导航 --%>
			<c:set var="parentName"><fmt:message key="menu.statistic.analysis"/></c:set>
			<c:set var="pageName"><fmt:message key="statistic.index.device" /></c:set>
			<jsp:include page="../include/navigation2.jsp" >
				<jsp:param value="${root }/statisitc/toList.action" name="parentUrl"/>
				<jsp:param value="${parentName }" name="parentName"/>
				<jsp:param value="${pageName }" name="pageName"/>
			</jsp:include>

			<!-- 过滤条件 -->
			<div class="clearfix"></div>
			<div class="profile profile_box02">
				<div class="tab-content m-b">
					<div class="tab-cotent-title">
						<fmt:message key="statics.jsp.condition" />
					</div>
					<div class="search_form">
						<form class="form-horizontal row" id="searchForm"
							onsubmit="return false;">
							<!-- 过滤表单 -->
							<div class="form-group col-md-6">
								<label class="col-sm-4 control-label"><fmt:message
										key="device.Number" /></label>
								<div class="col-sm-8">
									<input type="text" id="s_elockNumber" name="s_elockNumber"
										class="form-control">
								</div>
							</div>
							<div class="form-group col-sm-6">
								<label for="roleIds" class="col-sm-4 control-label"><fmt:message
										key="WarehouseElock.belongTo" /></label>
								<div class="col-sm-8">
									<select style="" id="s_belongTo" name="s_belongTo"
										class="form-control">
										<option value=""></option>
										<c:forEach var="SystemDepartmentBO" items="${deptList}">
											<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group col-sm-6">
								<label for="roleIds" class="col-sm-4 control-label"><fmt:message
										key="device.Type" /></label>
								<div class="col-sm-8">
									<s:select name="devicetype" emptyOption="true"
										cssClass="form-control" theme="simple"
										list="@com.nuctech.ls.model.util.DeviceType@values()"
										listKey="type" listValue="key" value="">
									</s:select>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-offset-9 col-md-3">
									<button type="submit" class="btn btn-danger"
										onclick="doSearch();">
										<fmt:message key="common.button.query" />
									</button>
									<button type="button" class="btn btn-darch" onclick="doRest();">
										<fmt:message key="common.button.reset" />
									</button>
								</div>
							</div>
						</form>
					</div>
				</div>
				<!-- 关锁使用统计列表 -->
				<div class="profile profile_box02" id="elock">
					<!--my result-->
					<div class="tab-content">
						<div class="tab-cotent-title">
							<div class="Features pull-right">
								<ul>
									<li><a href="${root}/statisitc/toList.action"
										class="btn btn-info"><fmt:message key="common.button.back" /></a></li>
								</ul>
							</div>
							<fmt:message key="statistic.sub.elock" />
						</div>
						<div class="search_table">
							<div>
								<table id="deviceUsableTable">
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="clearfix"></div>
				<!-- 子锁使用统计  -->
				<div class="profile profile_box02" id="eseal">
					<!--my result-->
					<div class="tab-content">
						<div class="tab-cotent-title">
							<div class="Features pull-right">
								<ul>
									<li><a href="${root}/statisitc/toList.action"
										class="btn btn-info"><fmt:message key="common.button.back" /></a></li>
								</ul>
							</div>
							<fmt:message key="statistic.sub.eseal" />
						</div>
						<div class="search_table">
							<div>
								<table id="esealUsableTable">
								</table>
							</div>
						</div>
					</div>
				</div>
				<!-- 传感器 -->
				<div class="profile profile_box02" id="sensor">
					<!--my result-->
					<div class="tab-content">
						<div class="tab-cotent-title">
							<div class="Features pull-right">
								<ul>
									<li><a href="${root}/statisitc/toList.action"
										class="btn btn-info"><fmt:message key="common.button.back" /></a></li>
								</ul>
							</div>
							<fmt:message key="statistic.sub.sensor" />
						</div>
						<div class="search_table">
							<div>
								<table id="sensorUsableTable">
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>	
</body>
<script type="text/javascript">
	$(window).resize(function() {
		$("#deviceUsableTable").bootstrapTable("resetView");
		$("#esealUsableTable").bootstrapTable("resetView");
	});

	$(function() {
		//关锁
		var $table = $("#deviceUsableTable");
		$table.bootstrapTable({
			url:'${root}/statisitc/elockUseStatic.action',
			clickToSelect : true,
			showRefresh : false,
			search : false,
			showColumns : false,
			showExport : false,
			striped : true,
			height : "100%",
			method : "get",
			cache : false,
			pagination : true,
			idfield : "deviceNumber",
			sortName : "deviceType",
			sortOrder : 'asc',
			sortable : true,
			pageNumber : 1,
			pageSize : 5,
			pageList : [ 10, 20, 30 ],
			columns : [ {
				field : 'TRACKING_DEVICE_NUMBER',
				title : '<fmt:message key="WarehouseElock.elockNumber"/>',
				sortable : true
			},{
				field : 'c',
				title : '<fmt:message key="statistic.frequence"/>',
				sortable : true
			}, {
				field : 'ORGANIZATION_NAME',
				title : '<fmt:message key="statistic.port"/>',
				sortable : true
			} ],
		});
		
		//子锁
		$('#esealUsableTable').bootstrapTable({
			url:'${root}/statisitc/esealUseStatic.action',
			showRefresh : false,
			search : false,
			showColumns : false,
			showExport : false,
			striped : true,
			height : "100%",
			method : "get",
			cache : false,
			pagination : true,
			sortOrder : 'asc',
			sortable : true,
			pageNumber : 1,
			pageSize : 5,
			pageList : [ 10, 20, 30 ],
			columns : [ {
				field : 'ESEAL_NUMBER',
				title : '<fmt:message key="warehouseEsealBO.esealNumber"/>',
				sortable : true
			},{
				field : 'c',
				title : '<fmt:message key="statistic.frequence"/>',
				sortable : true
			}, {
				field : 'ORGANIZATION_NAME',
				title : '<fmt:message key="statistic.port"/>',
				sortable : true
			} ],
		});
		
		//传感器
		$('#sensorUsableTable').bootstrapTable({
			url:'${root}/statisitc/sensorUseStatic.action',
			showRefresh : false,
			search : false,
			showColumns : false,
			showExport : false,
			striped : true,
			height : "100%",
			method : "get",
			cache : false,
			pagination : true,
			sortOrder : 'asc',
			sortable : true,
			pageNumber : 1,
			pageSize : 5,
			pageList : [ 10, 20, 30 ],
			columns : [ {
				field : 'SENSOR_NUMBER',
				title : '<fmt:message key="WarehouseSensor.sensorNumber"/>',
				sortable : true
			},{
				field : 'c',
				title : '<fmt:message key="statistic.frequence"/>',
				sortable : true
			}, {
				field : 'ORGANIZATION_NAME',
				title : '<fmt:message key="statistic.port"/>',
				sortable : true
			} ],
		});
	});
	
	//根据过滤条件刷新表格
	function doSearch() {
		var deviceNum = $("#s_elockNumber").val();
		var belongTo = $("#s_belongTo").val();
		var deviceType = $("#devicetype").val();
		//根据设备类型显示或隐藏关锁/子锁/传感器列表
		if(deviceType=="TRACKING_DEVICE"){
			$("#elock").show();
			$("#eseal").hide();
			$("#sensor").hide();
		}
		if(deviceType=="ESEAL"){
			$("#elock").hide();
			$("#eseal").show();
			$("#sensor").hide();
		}
		if(deviceType=="SENSOR"){
			$("#elock").hide();
			$("#eseal").hide();
			$("#sensor").show();
		}
		if(deviceType==""){
			$("#elock").show();
			$("#eseal").show();
			$("#sensor").show();
		}
		//根据过滤条件过滤关锁表
		var params = $('#deviceUsableTable').bootstrapTable('getOptions');
		params.url = "${root}/statisitc/elockUseStatic.action?deviceNum="+deviceNum+"&belongTo="+belongTo;
		$("#deviceUsableTable").bootstrapTable('refresh', params);
		//根据过滤条件过滤子锁表
		var eparams = $('#esealUsableTable').bootstrapTable('getOptions');
		eparams.url = "${root}/statisitc/esealUseStatic.action?deviceNum="+deviceNum+"&belongTo="+belongTo;
		$("#esealUsableTable").bootstrapTable('refresh', eparams);
		//根据过滤条件过滤传感器表
		var sparams = $('#sensorUsableTable').bootstrapTable('getOptions');
		sparams.url = "${root}/statisitc/sensorUseStatic.action?deviceNum="+deviceNum+"&belongTo="+belongTo;
		$("#sensorUsableTable").bootstrapTable('refresh', sparams);
	}
	
	
	//重置查询form
	function doRest(){
		$("#searchForm")[0].reset();
	}
</script>
</html>