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
      <!-- Modal 关锁添加模态框-->
	<div class="modal  add_user_box" id="addElockModal" tabindex="-1"
		role="dialog" aria-labelledby="addElockModalTitle">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- /Modal -->


	<!-- Modify Modal关锁修改模态框 -->
	<div class="modal  add_user_box" id="updateElockModal" tabindex="-1"
		role="dialog" aria-labelledby="updateElockModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- Modify Modal -->
	
	
		<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message
							key="WarehouseElock.Management" /></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="ElockForm" action=""
				onsubmit="return false;">
				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message
							key="WarehouseElock.elockNumber" /></label>
					<div class="col-sm-8">
						<input type="text" id="s_elockNumber" name="s_elockNumber"
							class="form-control">
					</div>
				</div>
				
				<div class="form-group col-sm-6">
					<label for="roleIds" class="col-sm-4 control-label"><fmt:message
							key="WarehouseElock.belongTo" /></label>
					<div class="col-sm-8">
						<select style="font-size:10px" id="s_belongTo" name="s_belongTo" class="form-control">
						<option  value=""><fmt:message key="please.choose"/></option>
							<c:forEach var="SystemDepartmentBO" items="${deptList}">
								<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message
							key="WarehouseElock.simCard" /></label>
					<div class="col-sm-8">
						<input type="text" id="s_simCard" name="s_simCard"
							class="form-control">
					</div>
				</div>

				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message
							key="WarehouseElock.interval" /></label>
					<div class="col-sm-8">
						<input type="text" id="s_interval" name="s_interval"
							class="form-control">
					</div>
				</div>
				<div class="clearfix"></div>
				<div class="form-group">
					<div class="col-sm-offset-9 col-md-3">
						<button type="button" class="btn btn-danger" onclick="doSearch();"><fmt:message key="common.button.query"/></button>
						<button  type="button"  class="btn btn-darch" onclick="doRest();">
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
						<li><a id="addElockBtn"class="btn btn-info"><fmt:message key="common.button.add"/></a></li>
	         			<li><a id="editElockBtn"class="btn btn-info"><fmt:message key="common.button.modify"/></a></li>
	         			<li><a id="deletea"class="btn btn-info" onclick="delObject();"><fmt:message key="common.button.delete"/></a></li>
	<%-- 	 					<li><a href='${root}/device/warehouseElock/DeviceDispatch.jsp' id="dispatch" ><fmt:message key="common.button.dispatch"/></a></li>
 --%>       		
						<li><a id="dispatch"class="btn btn-info" onclick="deviceDispatch();"><fmt:message key="common.button.dispatch"/></a></li>
					</ul>
				</div>
				<fmt:message key="elock.list"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="table" >	</table>
					</div>
				</div>
			</div>
		</div>
      </div>
	</div>
	<script type="text/javascript">
		var selections = [];
		var $table = $('#table');
		function doSearch() {
			var params = $table.bootstrapTable('getOptions');
			params.queryParams = function(params) {
				//遍历form 组装json  
				$.each($("#ElockForm").serializeArray(), function(i, field) {
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
				$.each($("#ElockForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params += "&" + field.name + "=" + field.value;
				});
				return params;
			}
			$table.bootstrapTable({
					url:'${root}/warehouseElock/list.action',
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
			    	field: 'elockNumber',
			    	title: $.i18n.prop('WarehouseElock.elockNumber')
			    },{
			    	field: 'organizationName',//组织机构表里面的"机构名称"
			    	title: $.i18n.prop('WarehouseElock.belongTo')
			    },{
			    	field: 'simCard',
			    	title: $.i18n.prop('WarehouseElock.simCard')
			    },{
			    	field: 'interval',
			    	title: $.i18n.prop('WarehouseElock.interval')
			    },{
			    	field: 'gatewayAddress',
			    	title: $.i18n.prop('WarehouseElock.gatewayAddress')
			    },{
			    	field: 'elockStatus',
			    	title: $.i18n.prop('WarehouseElock.elockStatus')
			    }],
/* 			    onLoadSuccess: function (data) {
				debugger;}
 */			});

			//添加Modal调用方法
			$("#addElockBtn").click(function() {
				var url = "${root}/warehouseElock/addModal.action";
				$('#addElockModal').removeData('bs.modal');
				$('#addElockModal').modal({
					remote : url,
					show : false,
					backdrop : 'static',
					keyboard : false
				});
			});

			$('#addElockModal').on('loaded.bs.modal', function(e) {
				$('#addElockModal').modal('show');
			});

			//编辑Modal调用方法
		 	$("#editElockBtn")
					.click(
							function() {
								var ids = $.map($table
										.bootstrapTable('getSelections'),
										function(row) {
											return row.elockId
										});
								if (ids.length == 0) {
									bootbox.alert($.i18n.prop("elock.modify.choose"));
								} else if (ids.length > 1) {
									bootbox.alert($.i18n.prop("elock.modify.choose.only"));
								} else {
									var url = "${root}/warehouseElock/editModal.action?warehouseElockBO.elockId="+ ids;
									$('#updateElockModal').removeData('bs.modal');
									$('#updateElockModal').modal({
										remote : url,
										show : false,
										backdrop : 'static',
										keyboard : false
									});
								}
							}); 
			/* $("#editElockBtn").click(
					function(){
						var list = $('#table').bootstrapTable('getSelections');
						var ids = new Array();
						for ( var o in list) {
							ids.push(list[o].elockId);
							debugger;
						}					
						
					}
				)	 */			
			$('#updateElockModal').on('loaded.bs.modal', function(e) {
				$('#updateElockModal').modal('show');
			});
		});
	</script>
	
	
	
	<!-- 删除记录 -->
	<script type="text/javascript">
		function delObject() {
			var list = $('#table').bootstrapTable('getSelections'); //获取表的行
			var ids = new Array();
			for ( var o in list) {
				ids.push(list[o].elockId);
			}
			var elockIds = ids.join(",");
			if(list.length<=0){
				bootbox.alert($.i18n.prop("warehouseElock.delete.choose"));
				return;
			}else{
				var ajaxUrl = "${root}/warehouseElock/delwarehouseById.action";  /*路径改过，原先是相对路径 */
				bootbox.confirm($.i18n.prop("are.you.sure"), function(result){
					if (result){
						$.ajax({
							url : ajaxUrl,
							type : "post",
							dataType : "json",
							data : {
								elockIds : elockIds
								},
							success : function(data) {
								if(data == true){
									bootbox.alert($.i18n.prop("warehouseElock.delete.choose"));
								}
							}
						});
						window.location.reload();
						$('#table').bootstrapTable('refresh', {});
					}
				})
			}
		}
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
		var url = "${root}/dispatch/toList.action";
 		window.location.href=url;
	}
	</script>
</body>
</html>