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
<%@ include file="../../include/left.jsp" %>
<div class="row site">
	<div class="wrapper-content margint95 margin60">
		<!-- Modal 子锁添加模态框-->
	<div class="modal  add_user_box" id="addEsealModal" tabindex="-1"
		role="dialog" aria-labelledby="addEsealModalTitle">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- /Modal -->


	<!-- Modify Modal子锁修改模态框 -->
	<div class="modal  add_user_box" id="updateEsealModal" tabindex="-1"
		role="dialog" aria-labelledby="updateEsealModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- Modify Modal -->
	
		<div class="profile profile_box02">
			<div class="tab-content m-b">
				<div class="tab-cotent-title"><fmt:message key="eseal.management"/></div>
				<div class="search_form">
					<form class="form-horizontal row" id="EsealForm" action=""	onsubmit="return false;">
				<div class="form-group col-md-4">
					<label class="col-sm-4 control-label"><fmt:message key="warehouseEsealBO.esealNumber"/></label>
					<div class="col-sm-8">
						<input type="text" id="s_esealNumber" name="s_esealNumber"
							class="form-control">
					</div>
				</div>

				<div class="form-group col-sm-4">
					<label for="roleIds" class="col-sm-4 control-label"><fmt:message key="warehouseEsealBO.belongTo"/></label>
					<div class="col-sm-8">
						<select style="font-size:10px" id="s_belongTo" name="s_belongTo" class="form-control">
							<option  value=""><fmt:message key="please.choose"/></option>
							<c:forEach var="SystemDepartmentBO" items="${esealBelongtoList}">
								<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="form-group col-md-4">
					<label class="col-sm-4 control-label"><fmt:message key="warehouseEsealBO.esealStatus"/></label>
					<div class="col-sm-8">
						<select style="font-size:10px" id="s_esealStatus" name="s_esealStatus" class="form-control">
								<option  value=""><fmt:message key="please.choose"/></option>
								<option value="0"><fmt:message key="device.Scrap" /></option>
								<option value="1"><fmt:message key="device.Normal" /></option>
								<option value="3"><fmt:message key="device.Inway" /></option>
								<option value="4"><fmt:message key="device.Maintain" /></option>
						</select>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-offset-9 col-md-3">
						<button type="button" class="btn btn-danger" onclick="doSearch();">
							<fmt:message key="common.button.query" />
						</button>
						<button id="resetSearchBtn" type="button" class="btn btn-darch" onclick="doRest();">
							<fmt:message key="common.button.reset" />
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>
		 <!--/search form-->
		 <div class="tab-content">
		 	<div class="tab-cotent-title">
			  	<div class="Features pull-right">
					<ul>
						<li><a id="addEsealBtn" class="btn btn-info"><fmt:message key="common.button.add"/></a></li>
	         			<li><a id="editEsealBtn" class="btn btn-info"><fmt:message key="common.button.modify"/></a></li>
	         			<li><a id="deletea" class="btn btn-info" onclick="delObject();"><fmt:message key="common.button.delete"/></a></li>
	         			<li><a href="${root}/dispatch/toList.action"; id="dispatch" class="btn btn-info" ><fmt:message key="common.button.dispatch"/></a></li>
					</ul>
				</div>
				<fmt:message key="eseal.list"/>
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
		function doSearch() {
			var params = $table.bootstrapTable('getOptions');
			params.queryParams = function(params) {
				//遍历form 组装json  
				$.each($("#EsealForm").serializeArray(), function(i, field) {
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
				$.each($("#EsealForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params += "&" + field.name + "=" + field.value;
				});
				return params;
			}
			$table.bootstrapTable({
				url : '${root}/esealMgmt/list.action',
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
			    	field: 'esealNumber',
			    	title:  $.i18n.prop('warehouseEsealBO.esealNumber')
			    },{
			    	field: 'organizationName',//组织机构表里面的机构名称
			    	title:  $.i18n.prop('warehouseEsealBO.belongTo')
			    },{
			    	field: 'esealStatus',
			    	title:  $.i18n.prop('warehouseEsealBO.esealStatus')
			    },]
			});

			//添加Modal调用方法
			$("#addEsealBtn").click(function() {
				var url = "${root}/esealMgmt/addModal.action";
				$('#addEsealModal').removeData('bs.modal');
				$('#addEsealModal').modal({
					remote : url,
					show : false,
					backdrop : 'static',
					keyboard : false
				});
			});

			$('#addEsealModal').on('loaded.bs.modal', function(e) {
				$('#addEsealModal').modal('show');
			});

			//编辑Modal调用方法
			$("#editEsealBtn")
					.click(
							function() {
								var ids = $.map($table
										.bootstrapTable('getSelections'),
										function(row) {
											return row.esealId
										});
								if (ids.length == 0) {
									bootbox.alert($.i18n.prop("eseal.modify.choose"));
								} else if (ids.length > 1) {
									bootbox.alert($.i18n.prop("eseal.modify.choose.only"));
								} else {
									var url = "${root}/esealMgmt/editModal.action?warehouseEsealBO.esealId="+ ids;
									$('#updateEsealModal').removeData('bs.modal');
									$('#updateEsealModal').modal({
										remote : url,
										show : false,
										backdrop : 'static',
										keyboard : false
									});
								}
							});

			$('#updateEsealModal').on('loaded.bs.modal', function(e) {
				$('#updateEsealModal').modal('show');
			});
		});
	</script>
	
	
	
	<!-- 删除记录 -->
	<script type="text/javascript">
		function delObject() {
			var list = $('#table').bootstrapTable('getSelections'); //获取表的行
			var ids = new Array();
			for ( var o in list) {
				ids.push(list[0].esealId);
			}
			var esealIds = ids.join(",");
			if(list.length<=0){
				bootbox.alert($.i18n.prop("eseal.delete.choose"));
				return;
			}else{
				bootbox.confirm($.i18n.prop("are.you.sure"), function(result){
					if(result){
						var ajaxUrl = "${root}/esealMgmt/delEsealById.action";  /*路径改过，原先是相对路径 */
						$.ajax({
							url : ajaxUrl,
							type : "post",
							dataType : "json",
							data : {
								esealIds : esealIds
							},
							success : function(data) {
							}
							
						});
					
					
					window.location.reload();
					$('#table').bootstrapTable('refresh', {});
					}
				})
			}
				
			
		}
	</script>
	<script type="text/javascript">
		function doRest(){
			$("#resetSearchBtn").click(function() {
				$("#EsealForm")[0].reset();
				resetQuery();
			});
			window.location.reload();
		}
</script>
</body>
</html>