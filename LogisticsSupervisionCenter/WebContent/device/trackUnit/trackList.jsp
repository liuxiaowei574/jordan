<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="track.Mgmt"/></title>
</head>
<body>
<%--行程请求推送通知页面 --%>
<%@ include file="../../include/tripMsgModal.jsp" %>
<%@ include file="../../include/left.jsp" %>
<div class="row site">
     <div class="wrapper-content margint95 margin60">
      <!-- Modal 车载台添加模态框-->
	<div class="modal  add_user_box" id="addTrackModal" tabindex="-1"
		role="dialog" aria-labelledby="addTrackModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- /Modal -->


	<!-- Modify Modal车载台修改模态框 -->
	<div class="modal  add_user_box" id="updateTrackModal" tabindex="-1"
		role="dialog" aria-labelledby="updateTrackModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- Modify Modal -->
	
	
		<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message key="track.Mgmt" /></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="TrackForm" action=""
				onsubmit="return false;">
				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message key="track.number" /></label>
					<div class="col-sm-8">
						<input type="text" id="s_trackUnitNumber" name="s_trackUnitNumber"
							class="form-control">
					</div>
				</div>
				
				<div class="form-group col-sm-6">
					<label for="roleIds" class="col-sm-4 control-label"><fmt:message key="track.belongto" /></label>
					<div class="col-sm-8">
						<select style="/* font-size:10px */" id="s_belongTo" name="s_belongTo" class="form-control">
						<option  value=""></option>
							<c:forEach var="SystemDepartmentBO" items="${deptList}">
								<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
							</c:forEach> 
						</select>
					</div>
				</div>

				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message key="track.simcard" /></label>
					<div class="col-sm-8">
						<input type="text" id="s_simCard" name="s_simCard"
							class="form-control">
					</div>
				</div>

				<div class="form-group col-md-6">
					<label class="col-sm-4 control-label"><fmt:message
							key="track.trackUnitStatus" /></label>
					<div class="col-sm-8">
						<s:select name="s_trackUnitStatus" 
						emptyOption="true"
						cssClass="form-control" theme="simple"
						list="@com.nuctech.ls.model.util.DeviceStatus@values()"
						listKey="text"
						listValue="key" 
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
						<li><a id="addTrackBtn"class="btn btn-info"><fmt:message key="common.button.add"/></a></li>
	         			<li><a id="editTrackBtn"class="btn btn-info"><fmt:message key="common.button.modify"/></a></li>
	         			<li><a id="deletea"class="btn btn-info" onclick="delObject();"><fmt:message key="common.button.delete"/></a></li>
					</ul>
				</div>
				<fmt:message key="track.list"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="trackTable"></table>
					</div>
				</div>
			</div>
		</div>
      </div>
	</div>
	<script type="text/javascript">
		var selections = [];
		var $table = $('#trackTable');
		//刷新tale
		$(window).resize(function(){
			$table.bootstrapTable("resetView");
		});
		function doSearch() {
			var params = $table.bootstrapTable('getOptions');
			params.queryParams = function(params) {
				//遍历form 组装json  
				$.each($("#TrackForm").serializeArray(), function(i, field) {
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
				$.each($("#TrackForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params += "&" + field.name + "=" + field.value;
				});
				return params;
			}
			$table.bootstrapTable({
					url:'${root}/trackMgmt/list.action',
					clickToSelect : true,
					showRefresh : false,
					search : false,
					showColumns : false,
					showExport : false,
					striped : true,
					//height : "100%",
					method : "get",
					pagination : true,
					sidePagination : 'server',
					pageNumber : 1,
					pageSize : 10,
					sortable:true,
					pageList : [ 10, 20, 30 ],
				columns: [{
			    	checkbox : true
			    },{
			    	field: 'trackUnitNumber',
			    	title: $.i18n.prop('track.number'),
					sortable:true
			    },{
			    	field: 'organizationName',//组织机构表里面的"机构名称"
			    	title: $.i18n.prop('track.belongto'),
					sortable:true
			    },{
			    	field: 'simCard',
			    	title: $.i18n.prop('track.simcard'),
					sortable:true
			    },{
			    	field: 'interval',
			    	title: $.i18n.prop('track.interval'),
					sortable:true
			    },{
			    	field: 'gatewayAddress',
			    	title: $.i18n.prop('track.gatewayAddress'),
					sortable:true
			    },{
			    	field: 'trackUnitStatus',
			    	title: $.i18n.prop('track.trackUnitStatus'),
			    	formatter : trackUnitStatusFormat ,
					sortable:true
			    }],
 			});

			//添加Modal调用方法
			$("#addTrackBtn").click(function() {
				var url = "${root}/trackMgmt/addModal.action";
				$('#addTrackModal').removeData('bs.modal');
				$('#addTrackModal').modal({
					remote : url,
					show : false,
					backdrop : 'static',
					keyboard : false
				});
			});

			$('#addTrackModal').on('loaded.bs.modal', function(e) {
				$('#addTrackModal').modal('show');
			});
			//模态框登录判断
			$('#addTrackModal').on('show.bs.modal', function(e) {
				var content = $(this).find(".modal-content").html();
				needLogin(content);
			});

			//编辑Modal调用方法
		 	$("#editTrackBtn")
					.click(
							function() {
								var ids = $.map($table
										.bootstrapTable('getSelections'),
										function(row) {
											return row.trackUnitId
										});
								if (ids.length == 0) {
									bootbox.alert($.i18n.prop('please.choose.track.toEdit'));
								} else if (ids.length > 1) {
									bootbox.alert($.i18n.prop('please.choose.track.one.toEdit'));
								} else {
									var url = "${root}/trackMgmt/editModal.action?lsWarehouseTrackUnitBO.trackUnitId="+ ids;
									$('#updateTrackModal').removeData('bs.modal');
									$('#updateTrackModal').modal({
										remote : url,
										show : false,
										backdrop : 'static',
										keyboard : false
									});
								}
							}); 
			$('#updateTrackModal').on('loaded.bs.modal', function(e) {
				$('#updateTrackModal').modal('show');
			});
			//模态框登录判断
			$('#updateTrackModal').on('show.bs.modal', function(e) {
				var content = $(this).find(".modal-content").html();
				needLogin(content);
			});
		});
	</script>
	
	
	
	<!-- 删除记录 -->
	<script type="text/javascript">
		function delObject() {
			var list = $('#trackTable').bootstrapTable('getSelections'); //获取表的行
			var ids = new Array();
			for ( var o in list) {
				ids.push(list[o].trackUnitId);
			}
			var trackUnitIds = ids.join(",");
			if(list.length<=0){
				bootbox.alert($.i18n.prop('please.choose.track.toDelete'));
				return;
			}else{
				var ajaxUrl = "${root}/trackMgmt/delTrackById.action";  /*路径改过，原先是相对路径 */
				bootbox.confirm($.i18n.prop("are.you.sure"), function(result){
					if (result){
						$.ajax({
							url : ajaxUrl,
							type : "post",
							dataType : "json",
							data : {
								trackUnitIds : trackUnitIds
								},
							success : function(data) {
								if(!needLogin(data)) {
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
				$("#TrackForm")[0].reset();
				function resetQuery() {
					$table.bootstrapTable('refresh', {});
				}
			});
			window.location.reload();
		}
	</script>
	
	<script type="text/javascript">
		function trackUnitStatusFormat(value, row, index){
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
</body>
</html>