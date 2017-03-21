<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="Vehicle.type.management"/></title>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/tripMsgModal.jsp" %>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
     <div class="wrapper-content margint95 margin60">
      <!-- Modal 罚款添加模态框-->
	<div class="modal  add_user_box" id="addPunishModal" tabindex="-1"
		role="dialog" aria-labelledby="addPunishModalTitle">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- /Modal -->


	<!-- Modify Modal罚款修改模态框 -->
	<div class="modal  add_user_box" id="updatePunishModal" tabindex="-1"
		role="dialog" aria-labelledby="updatePunishModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- Modify Modal -->
	
	
		<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message key="Vehicle.type.management"/></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="VPunishForm" action=""onsubmit="return false;">
				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message key="Vehicle.type.punish.management"/></label>
					<div class="col-sm-8">
						<input type="text" id="s_vpunishType" name="s_vpunishType"
							class="form-control">
					</div>
				</div>
				
			<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message key="Vehicle.type.punish.value"/></label>
					<div class="col-sm-8">
						<input type="text" id="s_vpunishValue" name="s_vpunishValue"
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
						<li><a id="addPunishBtn"class="btn btn-info"><fmt:message key="common.button.add"/></a></li>
	         			<li><a id="editPunishBtn"class="btn btn-info"><fmt:message key="common.button.modify"/></a></li>
	         			<li><a id="deletea"class="btn btn-info" onclick="delObject();"><fmt:message key="common.button.delete"/></a></li>
					</ul>
				</div>
				<fmt:message key="Vehicle.type.punish.table"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="punishTable"></table>
					</div>
				</div>
			</div>
		</div>
      </div>
	</div>
	<script type="text/javascript">
		var selections = [];
		var $table = $('#punishTable');
		//刷新tale
		$(window).resize(function(){
			$table.bootstrapTable("resetView");
		});
		function doSearch() {
			var params = $table.bootstrapTable('getOptions');
			params.queryParams = function(params) {
				//遍历form 组装json  
				$.each($("#VPunishForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params[field.name] = field.value;
				});
				return params;
			}
			$table.bootstrapTable('refresh', params);
		}
		
		$(function() {
			//设置传入参数
			function queryParams(params) {
				//遍历form 组装json  
				$.each($("#VPunishForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params += "&" + field.name + "=" + field.value;
				});
				return params;
			}
			$table.bootstrapTable({
					clickToSelect : true,
					url:'${root}/vehiclePunish/list.action',
					pagination : true,
					pageSize : 10,
			    	sortable:true,
					smartDisplay : false,
					pageList : [ "10", "25", "50", "100", "All" ],
					maintainSelected : true,
				columns: [{
			    	checkbox : true
			    },{
			    	field: 'vpunishType',
			    	title: $.i18n.prop('Vehicle.type.punish.type'),
			    	sortable:true
			    },{
			    	field: 'vpunishValue',
			    	title: $.i18n.prop('Vehicle.type.punish.value')	,
			    	sortable:true	
			    }],
 			});

			//添加Modal调用方法
			$("#addPunishBtn").click(function() {
				var url = "${root}/vehiclePunish/addModal.action";
				$('#addPunishModal').removeData('bs.modal');
				$('#addPunishModal').modal({
					remote : url,
					show : false,
					backdrop : 'static',
					keyboard : false
				});
			});

			$('#addPunishModal').on('loaded.bs.modal', function(e) {
				$('#addPunishModal').modal('show');
			});
			//模态框登录判断
			$('#addPunishModal').on('show.bs.modal', function(e) {
				var content = $(this).find(".modal-content").html();
				needLogin(content);
			});

			//编辑Modal调用方法
		 	$("#editPunishBtn").click(function() {
				var ids = $.map($table
						.bootstrapTable('getSelections'),
						function(row) {
							return row.vpunishId
						});
				if (ids.length == 0) {
					bootbox.alert($.i18n.prop("Vehicle.please.choose.sla.toModify"));
				} else if (ids.length > 1) {
					bootbox.alert($.i18n.prop("please.choose.oneSla.toModify"));
				} else {
					var url = "${root}/vehiclePunish/editModal.action?lsVehiclePunishBo.vpunishId="+ ids;
					$('#updatePunishModal').removeData('bs.modal');
					$('#updatePunishModal').modal({
						remote : url,
						show : false,
						backdrop : 'static',
						keyboard : false
					});
				}
			}); 
			$('#updatePunishModal').on('loaded.bs.modal', function(e) {
				$('#updatePunishModal').modal('show');
			});
			//模态框登录判断
			$('#updatePunishModal').on('show.bs.modal', function(e) {
				var content = $(this).find(".modal-content").html();
				needLogin(content);
			});
		});
	</script>
	
	<!-- 删除记录 -->
	<script type="text/javascript">
		function delObject() {
			var list = $('#punishTable').bootstrapTable('getSelections'); //获取表的行
			var ids = new Array();
			for ( var o in list) {
				ids.push(list[o].vpunishId);
			}
			var vpunishIds = ids.join(",");
			if(list.length<=0){
				bootbox.alert($.i18n.prop("Vehicle.please.choose.toDelete"));
				return;
			}else{
				var ajaxUrl = "${root}/vehiclePunish/delpunishById.action";  
				bootbox.confirm($.i18n.prop("are.you.sure"), function(result){
					if (result){
						$.ajax({
							url : ajaxUrl,
							type : "post",
							dataType : "json",
							data : {
								vpunishIds : vpunishIds
								},
							success : function(data) {
								if(!needLogin(data)) {
									if(data == true){
										bootbox.alert($.i18n.prop("Vehicle.choose.toDelete.success"));
										//window.location.reload();
										$('#punishTable').bootstrapTable('refresh', {});
									}
								}
							}
						});
					}
				})
			}
		}
	</script>
	<%--搜索表单重置 --%>
	<script type="text/javascript">
		function doRest(){
			$("#resetSearchBtn").click(function() {
				$("#VPunishForm")[0].reset();
				function resetQuery() {
					$table.bootstrapTable('refresh', {});
				}
			});
			window.location.reload();
		}
	</script>
</body>
</html>