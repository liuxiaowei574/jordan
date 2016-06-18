<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="system.notice.title"/></title>
</head>
<body>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
     <div class="wrapper-content margint95 margin60">
		
		<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message key="dispatch.management"/></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="DispatchForm" action=""
				onsubmit="return false;">
				<div class="form-group col-sm-4">
					<label for="roleIds" class="col-sm-4 control-label"><fmt:message key="Application.port"/> </label>
					<div class="col-sm-8">
						<select style="font-size:10px" id="s_toPort" name="s_toPort" class="form-control">
						<option  value=""><fmt:message key="please.choose"/></option>
							<c:forEach var="SystemDepartmentBO" items="${deptList}">
								<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-offset-9 col-md-3">
						<button type="button" class="btn btn-danger" onclick="doSearch();">
							<fmt:message key="common.button.query" />
						</button>
						<button id="resetSearchBtn" type="button" form="ElockForm" class="btn btn-darch" onclick="doRest();">
							<fmt:message key="common.button.reset" />
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>
			<!--/search form-->
			<!--my result-->
			<div class="tab-content">
			  <div class="tab-cotent-title">
			  	<div class="Features pull-right">
					<ul>
 						<li><a id="dispatch" class="btn btn-info" onclick="deviceDispatch();"><fmt:message key="common.button.dispatch"/></a></li>
 						<!-- 勾选已经调度过的任务即调度记录表的调配状态为1的行 -->
 						<li><a id="view" class="btn btn-info" onclick="viewDispatchDetail();"><fmt:message key="common.button.view"/></a></li>
					</ul>
				</div>
				<fmt:message key="dispatch.List"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="dispatchTable"></table>
					</div>
				</div>
			</div>
		</div>
      </div>
	</div>
	<script type="text/javascript">
		var selections = [];
		var $table = $('#dispatchTable');
		function doSearch() {
			var params = $table.bootstrapTable('getOptions');
			params.queryParams = function(params) {
				//遍历form 组装json  
				$.each($("#DispatchForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params[field.name] = field.value;
				});
				return params;
			}
			$table.bootstrapTable('refresh', params);
		}

		
		function getIdSelections() {
			return $table.bootstrapTable('getSelections');
		}
		$(function() {
			//设置传入参数
			function queryParams(params) {
				//遍历form 组装json  
				$.each($("#DispatchForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params += "&" + field.name + "=" + field.value;
				});
				return params;
			}
			$table.bootstrapTable({
					url:'${root}/dispatch/list.action',
					clickToSelect : true,
					showRefresh : false,
					search : false,
					showColumns : false,
					showExport : false,
					striped : true,
					height : "100%",
					method : "get",
					idfield: "userId",
					sortName:"userId",
					cache : false,
					pagination : true,
					sidePagination : 'server',
					pageNumber : 1,
					pageSize : 10,
					pageList : [ 10, 20, 30 ],
				columns: [{
			    	checkbox : true
			    },{
			    	field: 'fromPortName',
			    	title: "迁出口岸"
	
			    },{
			    	field: 'toPortName',
			    	title: "申请口岸"
			    },{
			    	field: 'deviceNumber',
			    	title: "关锁数量"
			    },{
			    	field: 'esealNumber',
			    	title: "子锁数量"
			    },{
			    	field: 'sensorNumber',
			    	title: "传感器数量"
			    },{
			    	field: 'dispatchStatus',
			    	title: "调配状态"
			    }],
 			});

		});
	</script>
	
	<%--搜索表单重置 --%>
	<script type="text/javascript">
		function doRest(){
			$("#resetSearchBtn").click(function() {
				$("#ElockForm")[0].reset();
				function resetQuery() {
					$table.bootstrapTable('refresh', {});
				}
			});
			window.location.reload();
		}
	</script>
	
	<!--调度按钮方法  -->
	<script type="text/javascript">
	function deviceDispatch(){
		var list = $('#dispatchTable').bootstrapTable('getSelections');
		/* 获取调配状态 ,只对调度状态为0的进行调度*/
		var dispatchStatus = new Array();
			var dispatchStatus = $.map($table
					.bootstrapTable('getSelections'),
					function(row) {
						return row.dispatchStatus
					});
		if(dispatchStatus.length==0){
			bootbox.alert("请选择想要调度的申请表");
		}	else if(dispatchStatus.length>1){
			bootbox.alert("请选择一条申请进行调度");
		}else if(dispatchStatus==1){
			bootbox.alert("该申请已处理，不可以重复调度");
		}else{
		
		/* 获得申请口岸 */
		var port = new Array();
			for ( var o in list) {
				port.push(list[o].toPortName);
			}
		
		/*获取关锁数量 */
		var devices = new Array();
			for ( var o in list) {
				devices.push(list[o].deviceNumber);
			}
		/* var devices = devices.join(","); */
			
		/*获得子锁数量 */
		var eseals = new Array();
			for ( var o in list) {
				eseals.push(list[o].esealNumber);
			}
			
		/* 获得传感器数量 */
		var sensors = new Array();
			for ( var o in list) {
				sensors.push(list[o].sensorNumber);
			}
			
		/* 获得记录表中的调配主键 */
		var dispatchIds = new Array();
			for(var o in list){
				dispatchIds.push(list[o].dispatchId);
			}
		var url = "${root}/dispatch/deviceDispatchIndex.action?deviceNumber="+devices+"&esealNumber="+eseals+"&sensors="+sensors+"&dispatchIds="+dispatchIds+"&toPort="+port;
		window.location.href=url;
		}
		/* var url = "${root}/dispatch/deviceDispatchIndex.action?deviceNumber="+devices; */
	}
	</script>
	
	
	<!--查看按钮方法  -->
	<script type="text/javascript">
	function viewDispatchDetail(){
		/* 勾选调配状态为1的条目 */
		var viewlist = $('#dispatchTable').bootstrapTable('getSelections');
		/* 获取调配状态 ,只查看调配状态为1的唯一的记录*/
		var dispatchStatus = new Array();
			var dispatchStatus = $.map($table
					.bootstrapTable('getSelections'),
					function(row) {
						return row.dispatchStatus
					});
		if(dispatchStatus.length==0){
			bootbox.alert("请选择想要查看的记录");
		}	else if(dispatchStatus.length>1){
			bootbox.alert("请选择一条记录进行查看");
		}else if(dispatchStatus!=1){
			bootbox.alert("请选择已经调度过的记录进行查看");
		}
		else{
		/* 获取调配Id */	
		var viewDispatchIds = new Array();
			for(var o in viewlist){
				viewDispatchIds.push(viewlist[o].dispatchId);
			}
		/* 获得申请口岸 */
		var port = new Array();
			for ( var o in viewlist) {
				port.push(viewlist[o].toPortName);
			}
		/*获取关锁数量 */
		var devices = new Array();
			for ( var o in viewlist) {
				devices.push(viewlist[o].deviceNumber);
			}
		/*获得子锁数量 */
		var eseals = new Array();
			for ( var o in viewlist) {
				eseals.push(viewlist[o].esealNumber);
			}
		/* 获得传感器数量 */
		var sensors = new Array();
			for ( var o in viewlist) {
				sensors.push(viewlist[o].sensorNumber);
			}	
		
		
		var url = "${root}/dispatch/ViewDispatchDetailIndex.action?viewDispatchIds="+viewDispatchIds+"&toPort="+port+"&deviceNumber="+devices+"&esealNumber="+eseals+"&sensors="+sensors;
		window.location.href=url;
		}
	}
	</script>
</body>
</html>