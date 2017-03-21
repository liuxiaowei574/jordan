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
			  <div class="tab-cotent-title"><fmt:message key="punish.Management" /></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="PunishForm" action=""	onsubmit="return false;">
				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message key="punish.Name" /></label>
					<div class="col-sm-8">
						<input type="text" id="s_punishName" name="s_punishName"
							class="form-control">
					</div>
				</div>
				
			<div class="form-group col-sm-6">
				<label class="col-sm-4 control-label"><fmt:message key="sla.Type"/></label>
					<div class="col-sm-8">
						<s:select name="s_slaType" emptyOption="true"
							cssClass="form-control" theme="simple"
							list="@com.nuctech.ls.model.util.SLAType@values()"
							listKey="key" 
							listValue="key1"
							value="">
					    </s:select>
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
				<fmt:message key="punish.list"/>
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
				$.each($("#PunishForm").serializeArray(), function(i, field) {
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
				$.each($("#PunishForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params += "&" + field.name + "=" + field.value;
				});
				return params;
			}
			$table.bootstrapTable({
					clickToSelect : true,
					url:'${root}/punish/list.action',
					pagination : true,
					pageSize : 10,
					smartDisplay : false,
					sortable:true,
					pageList : [ 10, 20, 30 ],
					maintainSelected : true,
				columns: [{
			    	checkbox : true
			    },{
			    	field: 'punishName',
			    	title: $.i18n.prop('punish.Name'),
					sortable:true
			    },{
			    	field: 'slaType',
			    	title: $.i18n.prop('sla.Type'),			
			    	formatter : punishFormat ,
					sortable:true
			    },{
			    	field: 'slaContent',
			    	title: $.i18n.prop('sla.Content'),
					sortable:true
			    },{
			    	field: 'createTime',
			    	title: $.i18n.prop('registration.time'),
					sortable:true
			    },{
			    	field: 'createUser',
			    	title: $.i18n.prop('registration.name'),
					sortable:true
			    }, {
			    	field: 'solveTime',
			    	title: $.i18n.prop('sla.solveTime'),
					sortable:true
			    },{
			    	field: 'solveName',
			    	title: $.i18n.prop('sla.solveName'),
					sortable:true
			    },{
			    	field: 'punishValue',
			    	title: $.i18n.prop('sla.punishValue'),
					sortable:true
			    }],
 			});

			//添加Modal调用方法
			$("#addPunishBtn").click(function() {
				var url = "${root}/punish/addModal.action";
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
				var ids = $.map($table.bootstrapTable('getSelections'),
						function(row) {
							return row.punishId
						});
				if (ids.length == 0) {
					bootbox.alert($.i18n.prop("please.choose.sla.toModify"));
				} else if (ids.length > 1) {
					bootbox.alert($.i18n.prop("please.choose.oneSla.toModify"));
				} else {
					var url = "${root}/punish/editModal.action?lsSlaPunishBO.punishId="+ ids;
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
				ids.push(list[o].punishId);
			}
			var punishIds = ids.join(",");
			if(list.length<=0){
				bootbox.alert($.i18n.prop("please.choose.sla.toDelete"));
				return;
			}else{
				var ajaxUrl = "${root}/punish/delpunishById.action";  
				bootbox.confirm($.i18n.prop("are.you.sure"), function(result){
					if (result){
						$.ajax({
							url : ajaxUrl,
							type : "post",
							dataType : "json",
							data : {
								punishIds : punishIds
								},
							success : function(data) {
								if(!needLogin(data)) {
									if(data == true){
										bootbox.alert($.i18n.prop("choose.sla.Success"));
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
				$("#PunishForm")[0].reset();
				function resetQuery() {
					$table.bootstrapTable('refresh', {});
				}
			});
			window.location.reload();
		}
	</script>
	
	
	
	<!-- 将原先从数据库中查出来的值换为中文 -->
     <script type="text/javascript">
		function punishFormat(value, row, index){
			var show;
			if(value =='100'){
				show = $.i18n.prop('SLAType.NORMAL_SLA');
			}else if(value =='1000'){
				show =$.i18n.prop('SLAType.SERIOUS_SLA');
			}else{
				show = '--'
			}
			return [show].join('');
		}
    </script> 
</body>
</html>