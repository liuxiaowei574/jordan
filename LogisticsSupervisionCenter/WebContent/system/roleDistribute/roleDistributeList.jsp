<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="Authority.management"/></title>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/tripMsgModal.jsp" %>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
     <div class="wrapper-content margint95 margin60">
     	<%--导航 --%>
		<c:set var="pageName"><fmt:message key="Authority.management"/></c:set>
		<jsp:include page="../../include/navigation.jsp" >
			<jsp:param value="${pageName }" name="pageName"/>
		</jsp:include>


	<!-- Modify Modal角色分配模态框 -->
	<div class="modal  add_user_box" id="updateElockModal" tabindex="-1"
		role="dialog" aria-labelledby="updateElockModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- Modify Modal -->
	
	<!-- Modify Modal功能分配模态框列表 -->
	<div class="modal  add_user_box" id="functionModal" tabindex="-1"
		role="dialog" aria-labelledby="functionModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- Modify Modal -->
	
	<!-- Modify Modal功能分配模态框ztree -->
	<div class="modal  add_user_box" id="treeFunctionModal" tabindex="-1"
		role="dialog" aria-labelledby="treeFunctionModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- Modify Modal -->
	
		<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message key="Authority.management"/></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="ElockForm" action=""
				onsubmit="return false;">
				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message key="Authority.management.role.name"/></label>
					<div class="col-sm-8">
						<input type="text" id="s_roleName" name="s_roleName"
							class="form-control">
					</div>
				</div>
				

				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message key="Authority.management.role.Id"/></label>
					<div class="col-sm-8">
						<input type="text" id="s_roleId" name="s_roleId"
							class="form-control">
					</div>
				</div>

				
				<div class="clearfix"></div>
				<div class="form-group">
					<div class="col-sm-offset-9 col-md-3">
						<button type="submit" class="btn btn-danger" onclick="doSearch();"><fmt:message key="common.button.query"/></button>
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
						<%--
	         			<li><a id="editElockBtn"class="btn btn-info"><fmt:message key="common.button.modify"/></a></li>
	         			<li><a id="deletea"class="btn btn-info" onclick="delObject();"><fmt:message key="common.button.delete"/></a></li>
	         			 --%>
						<li><a id="roleDistribute"class="btn btn-info" ><fmt:message key="Authority.management.distribute"/></a></li>
						<li><a id="treeDistribute"class="btn btn-info" ><fmt:message key="Authority.management.distribute.tree"/></a></li>
					</ul>
				</div>
				<fmt:message key="Authority.management.role.Mgmt"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="roleTable" >	</table>
					</div>
				</div>
			</div>
		</div>
      </div>
	</div>
	<script type="text/javascript">
		var selections = [];
		var $table = $('#roleTable');
		//刷新tale
		$(window).resize(function(){
			$table.bootstrapTable("resetView");
		});
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
					url:'${root}/roleDistribute/list.action',
					clickToSelect : true,
					showRefresh : false,
					search : false,
					showColumns : false,
					showExport : false,
					striped : true,
					method : "get",
					sortable:true,
					cache : false,
					pagination : true,
					sidePagination : 'server',
					pageNumber : 1,
					pageSize : 10,
					pageList : [ 10, 20, 30 ],
					columns: [{
			    	checkbox : true
			    },{
			    	field: 'roleId',
			    	title: $.i18n.prop('Authority.management.role.Id'),
					sortable:true
			    },{
			    	field: 'roleName',
			    	title: $.i18n.prop('Authority.management.role.name'),
					sortable:true
			    },{
			    	field: '',
			    	title: $.i18n.prop('user.role'),
			    	formatter : roleFormatter,
					sortable:true
			    }],
			});


			//编辑Modal调用方法
		 	$("#editElockBtn").click(function() {
				var id = $.map($table
						.bootstrapTable('getSelections'),
						function(row) {
							return row.roleId
						});
				if (id.length == 0) {
					bootbox.alert($.i18n.prop('Authority.management.choose.modify.role'));
				} else if (id.length > 1) {
					bootbox.alert($.i18n.prop('Authority.management.only.choose.modify.role'));
				} else {
					var url = "${root}/roleDistribute/editModal.action?roleId="+ id;
					$('#updateElockModal').removeData('bs.modal');
					$('#updateElockModal').modal({
						remote : url,
						show : false,
						backdrop : 'static',
						keyboard : false
					});
				}
			}); 
			$('#updateElockModal').on('loaded.bs.modal', function(e) {
				$('#updateElockModal').modal('show');
			});
			//模态框登录判断
			$('#updateElockModal').on('show.bs.modal', function(e) {
				var content = $(this).find(".modal-content").html();
				needLogin(content);
			});
		});
		//分配功能模态框
	 	$("#roleDistribute").click(function() {
			var ids = $.map($table
					.bootstrapTable('getSelections'),
					function(row) {
						return row.roleId
					});
			if (ids.length == 0) {
				bootbox.alert($.i18n.prop('Authority.management.chooseRole.toDistribute.function'));
			} else if (ids.length > 1) {
				bootbox.alert($.i18n.prop('Authority.management.chooseOnlyRole.toDistribute.function'));
			} else {
				var url = "${root}/roleDistribute/roleFunctions.action?roleId="+ ids;
				$('#functionModal').removeData('bs.modal');
				$('#functionModal').modal({
					remote : url,
					show : false,
					backdrop : 'static',
					keyboard : false
				});
			}
		}); 
		$('#functionModal').on('loaded.bs.modal', function(e) {
			$('#functionModal').modal('show');
		});
		//模态框登录判断
		$('#functionModal').on('show.bs.modal', function(e) {
			var content = $(this).find(".modal-content").html();
			needLogin(content);
		});
		
		/* 跳转权限分配（功能树形式） */
		$("#treeDistribute").click(function() {
			/* 把角色的roleId传到后台 */
			//获取roleID
			var ids = $.map($table.bootstrapTable('getSelections'),
				function(row) {
					return row.roleId
				});
			if (ids.length == 0) {
				bootbox.alert($.i18n.prop('Authority.management.chooseRole.toDistribute.function'));
			} else if (ids.length > 1) {
				bootbox.alert($.i18n.prop('Authority.management.chooseOnlyRole.toDistribute.function'));
			} else {
			var url = "${root}/roleDistribute/roleFunctionsTree.action?roleId="+ids;
			$('#treeFunctionModal').removeData('bs.modal');
			$('#treeFunctionModal').modal({
					remote : url,
					show : false,
					backdrop : 'static',
					keyboard : false
				});
			}
		});
		$('#treeFunctionModal').on('loaded.bs.modal', function(e) {
			$('#treeFunctionModal').modal('show');
		});
		//模态框登录判断
		$('#treeFunctionModal').on('show.bs.modal', function(e) {
			var content = $(this).find(".modal-content").html();
			needLogin(content);
		});
	</script>
	
	<!-- 删除记录 -->
	<script type="text/javascript">
		function delObject() {
			var list = $('#roleTable').bootstrapTable('getSelections'); //获取表的行
			var ids = new Array();
			for ( var o in list) {
				ids.push(list[o].roleId);
			}
			var roleIds = ids.join(",");
			if(list.length<=0){
				bootbox.alert($.i18n.prop('Authority.management.choose.delete.role'));
				return;
			}else{
				var ajaxUrl = "${root}/roleDistribute/delRoleById.action";  /*路径改过，原先是相对路径 */
				bootbox.confirm($.i18n.prop("are.you.sure"), function(result){
					if (result){
						$.ajax({
							url : ajaxUrl,
							type : "post",
							dataType : "json",
							data : {
								roleIds : roleIds
								},
							success : function(data) {
								if(!needLogin(data)) {
									if(data == true){
										bootbox.alert($.i18n.prop('Authority.management.choose.delete.success'));
									}
								}
							}
						});
						$('#roleTable').bootstrapTable('refresh', {});
						window.location.reload();
					}
				})
			}
		}
	</script>
	<%--搜索表单重置 --%>
	<script type="text/javascript">
		function doRest(){
			//$("#resetSearchBtn").click(function() {
				$("#ElockForm")[0].reset();
				/* function resetQuery() {
					$table.bootstrapTable('refresh', {});
				} */
			//});
		}
		function roleFormatter(value, row, index) {
			var show;
			 if (!!row.roleName) {
				show = $.i18n.prop('system.role.' + row.roleName);
			} else {
				show = '--';
			}
			return [show].join('');
		}
	</script>
</body>
</html>