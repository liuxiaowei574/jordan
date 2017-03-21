<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include/include.jsp" />
<title><fmt:message key="system.notice.title" /></title>
<style>
/*
.add_user_box .modal-content{
	position: absolute;
	width: 50%;
	height:80%;
	top:50%;left:50%; 
    transform: translate(-50.1%, -50.1%);
    overflow:hidden;
    margin:0 auto;
} 
.add_user_box.modal.in .modal-dialog .modal-content{height:100%;}
.add_user_box.modal.in .modal-dialog .modal-content>.form-horizontal{  height: calc(100% - 50px);overflow-y: auto;margin: 0; }
*/
.add_user_box.modal.in .modal-dialog {
    position: relative;
    width: 50%;
    height: 100%;
    margin: 0 auto;
}
.add_user_box.modal.in .modal-dialog>.modal-content {
	margin: 0 auto;
    top: 5%;
}
</style>
</head>

<body>
	<%--行程请求推送通知页面 --%>
	<%@ include file="../include/tripMsgModal.jsp"%>
	<%@ include file="../include/left.jsp"%>
	<div class="row site">
		<div class="wrapper-content margint95 margin60">
			<!-- Modal 巡逻队添加模态框-->
			<div class="modal  add_user_box" id="addPatrolModal" tabindex="-1"
				role="dialog" aria-labelledby="addPatrolModalTitle">
				<div class="modal-dialog" role="document">
					<div class="modal-content"></div>
				</div>
			</div>
			<!-- /Modal -->


			<!-- Modify Modal巡逻队修改模态框 -->
			<div class="modal  add_user_box" id="updatePatrolModal" tabindex="-1"
				role="dialog" aria-labelledby="updatePatrolModal">
				<div class="modal-dialog" role="document">
					<div class="modal-content"></div>
				</div>
			</div>
			<!-- Modify Modal -->


			<div class="profile profile_box02">
				<div class="tab-content m-b">
					<div class="tab-cotent-title"><fmt:message key="patrol.management"/></div>
					<div class="search_form">
						<form class="form-horizontal row" id="PatrolForm" action=""
							onsubmit="return false;">

							<div class="form-group col-sm-6">
								<label for="roleIds" class="col-sm-4 control-label"><fmt:message key="patrol.label.trackUnitNumber"/></label>
								<div class="col-sm-8">
									<select style="" id="s_trackUnitNumber"
										name="s_trackUnitNumber" class="form-control">
										<option value=""></option>
										<c:forEach var="LsWarehouseTrackUnitBO" items="${trackList}">
											<option value=${LsWarehouseTrackUnitBO.trackUnitNumber}>${LsWarehouseTrackUnitBO.trackUnitNumber}</option>
										</c:forEach>
									</select>
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
								<label for="roleIds" class="col-sm-4 control-label"><fmt:message key="main.list.commonPatrol.belongToArea" /></label>
								<div class="col-sm-8">
									<select style="" id="s_belongToArea" name="s_belongToArea"
										class="form-control">
										<option value=""></option>
										<c:forEach var="LsMonitorRouteAreaBO" items="${areaList}">
											<option value=${LsMonitorRouteAreaBO.routeAreaId}>${LsMonitorRouteAreaBO.routeAreaName}</option>
										</c:forEach>
									</select>
								</div>
							</div>

							<div class="form-group col-sm-6">
								<label for="roleIds" class="col-sm-4 control-label"><fmt:message key="monitorTrip.label.potralUser"/></label>
								<div class="col-sm-8">
									<select style="" id="s_potralUser" name="s_potralUser"
										class="form-control">
										<option value=""></option>
										<c:forEach var="LsSystemUserBO" items="${userList}">
											<option value=${LsSystemUserBO.userId}>${LsSystemUserBO.userAccount}</option>
										</c:forEach>
									</select>
								</div>
							</div>


							<div class="clearfix"></div>
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

				<!--/search form-->
				<!--my result-->
				<div class="tab-content">
					<div class="tab-cotent-title">
						<div class="Features pull-right">
							<ul>
								<li><a id="addPatrolBtn" class="btn btn-info"><fmt:message
											key="common.button.add" /></a></li>
								<li><a id="editPatrolBtn" class="btn btn-info"><fmt:message
											key="common.button.modify" /></a></li>
								<li><a id="deletea" class="btn btn-info"
									onclick="delObject();"><fmt:message
											key="common.button.delete" /></a></li>
							</ul>
						</div>
						<fmt:message key="patrol.modify.list.table" />
					</div>
					<div class="search_table">
						<div>
							<table id="patrolTable">
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var selections = [];
		var $table = $('#patrolTable');
		//刷新tale
		$(window).resize(function() {
			$table.bootstrapTable("resetView");
		});

		function doSearch() {
			var params = $table.bootstrapTable('getOptions');
			params.queryParams = function(params) {
				//遍历form 组装json  
				$.each($("#PatrolForm").serializeArray(), function(i, field) {
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
				$.each($("#PatrolForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params += "&" + field.name + "=" + field.value;
				});
				return params;
			}
			$table.bootstrapTable({
				//url : root + "/patrol/dlist.action",
				url : root + "/patrolMgmt/list.action",
				clickToSelect : true,
				showRefresh : false,
				search : false,
				showColumns : false,
				showExport : false,
				striped : true,
				//height : "100%",
				method : "get",
				idfield: "patrolId",
				sortName:"patrolId",
				cache : false,
				sortable:true,
				pagination : true,
				sidePagination : 'server',
				pageNumber : 1,
				pageSize : 10,
				pageList : [ 10, 20, 30 ],
				columns : [ {
					checkbox : true
				},{
					field : 'patrolNumber',
					title : $.i18n.prop('patrol.number')
				}, {
					field : 'patrolType',
					title : $.i18n.prop('patrol.type'),
					formatter : patrolTypeFormat,
				},{
					field : 'vehiclePlateNumber',
					title : $.i18n.prop('patrol.vehicle.number')
				},{
					field : 'trackUnitNumber',
					title : $.i18n.prop('track.number')
				}, {
					field : 'routeAreaName',
					title : $.i18n.prop('dispatch.belongTo.areaName')
				},
				/*{
					field : 'organizationName',
					title : $.i18n.prop('dispatch.belongTo')
				}, 
				*/
				{
					field : 'userAccount',
					title : $.i18n.prop('dispacth.patrol.inCharge')
				}, {
					field : 'createUserName',
					title : $.i18n.prop('dispatch.patrol.createUser')
				} ],
			})
		})

		//添加Modal调用方法
		$("#addPatrolBtn").click(function() {
			var url = "${root}/patrolMgmt/addModal.action";
			$('#addPatrolModal').removeData('bs.modal');
			$('#addPatrolModal').modal({
				remote : url,
				show : false,
				backdrop : 'static',
				keyboard : false
			});
		});

		$('#addPatrolModal').on('loaded.bs.modal', function(e) {
			$('#addPatrolModal').modal('show');
		});
		//模态框登录判断
		$('#addPatrolModal').on('show.bs.modal', function(e) {
			var content = $(this).find(".modal-content").html();
			needLogin(content);
		});

		//编辑Modal调用方法
		$("#editPatrolBtn")
				.click(
						function() {
							var ids = $.map($table
									.bootstrapTable('getSelections'), function(
									row) {
								return row.patrolId
							});
							if (ids.length == 0) {
								bootbox.alert($.i18n.prop('patrol.choose.one.tomodify'));
							} else if (ids.length > 1) {
								bootbox.alert($.i18n.prop('patrol.choose.only.one.tomodify'));
							} else {
								var url = "${root}/patrolMgmt/editModal.action?patrolId="
										+ ids;
								$('#updatePatrolModal').removeData('bs.modal');
								$('#updatePatrolModal').modal({
									remote : url,
									show : false,
									backdrop : 'static',
									keyboard : false
								});
							}
						});

		$('#updatePatrolModal').on('loaded.bs.modal', function(e) {
			$('#updatePatrolModal').modal('show');
		});
	</script>



	<!-- 删除记录 -->
	<script type="text/javascript">
		function delObject() {
			var list = $('#patrolTable').bootstrapTable('getSelections'); //获取表的行
			var ids = new Array();
			for ( var o in list) {
				ids.push(list[o].patrolId);
			}
			var patrolIds = ids.join(",");
			if (list.length <= 0) {
				bootbox.alert($.i18n.prop('patrol.choose.one.more.todelete'));
				return;
			} else {
				var ajaxUrl = "${root}/patrolMgmt/delPatrolById.action"; /*路径改过，原先是相对路径 */
				bootbox.confirm($.i18n.prop("are.you.sure"), function(result) {
					if (result) {
						$.ajax({
							url : ajaxUrl,
							type : "post",
							dataType : "json",
							data : {
								patrolIds : patrolIds
							},
							success : function(data) {
								if (!needLogin(data)) {
									if (data == true) {
										bootbox.alert($.i18n.prop('patrol.delete.success'));
										$table.bootstrapTable('refresh', {});
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
		function doRest() {
			$("#resetSearchBtn").click(function() {
				$("#PatrolForm")[0].reset();
				function resetQuery() {
					$table.bootstrapTable('refresh', {});
				}
			});
			window.location.reload();
		}
	</script>
	<!-- 巡逻队类型国际化 -->
	<script type="text/javascript">
	function patrolTypeFormat(value, row, index){
			var show;
			if(value =='0'){
				show = $.i18n.prop('Patroltype.Enforce');
			}else if(value =='1'){
				show =$.i18n.prop('Patroltype.Escort');
			}else{
				show = '--'
			}
			return [show].join('');
		}
	</script>
</body>
</html>