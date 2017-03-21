<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../../include/include.jsp" />
<title>Performance Analysis</title>
<style type="text/css">
#portForm .input-group-btn>button {
	height: 30px;
}
</style>
</head>
<body>
	<%@ include file="../../include/left.jsp"%>
	<div class="row site">
		<!-- Modal 组织机构添加模态框-->
		<div class="modal  add_user_box" id="addElockModal" tabindex="-1"
			role="dialog" aria-labelledby="addElockModalTitle">
			<div class="modal-dialog" role="document">
				<div class="modal-content"></div>
			</div>
		</div>
		<!-- /Modal -->

		<!-- Modify Modal组织机构修改模态框 -->
		<div class="modal  add_user_box" id="updateElockModal" tabindex="-1"
			role="dialog" aria-labelledby="updateElockModal">
			<div class="modal-dialog" role="document">
				<div class="modal-content"></div>
			</div>
		</div>
		<!-- Modify Modal -->

		<div class="wrapper-content margint95 margin60">
			<%--导航 --%>
			<c:set var="pageName"><fmt:message key="Organization.management"/></c:set>
			<jsp:include page="../../include/navigation.jsp" >
				<jsp:param value="${pageName }" name="pageName"/>
			</jsp:include>
		
			<div class="col-sm-3 kalb">
				<div class="panel panel-default" style="overflow: scroll">
					<div class="panel-heading">
						<%-- <div class="pull-right col-sm-8 text-right">
							<form id="portForm" onsubmit="return false;">
								<div class="input-group">
									<input type="text" class="form-control" id="organizationName"
										name="organizationName"
										placeholder="<fmt:message key="performance.label.search"/>"
										onkeyup="treeFilter('tree','organizationName',this.value)">
									<span class="input-group-btn">
										<button class="btn btn-default" type="submit"
											onclick="treeFilter('tree','organizationName',document.getElementById('userName').value)">
											<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
										</button>
									</span>
								</div>
								<!-- /input-group -->
							</form>
						</div> --%>
						<fmt:message key="Organization.institution" />
					</div>
					<ul id="tree" class="ztree" style="width: 260px; overflow: auto;"></ul>
				</div>
			</div>
			<div class="col-sm-9 right-content">
				<div class="tab-content m-b dept-search">
					<div class="tab-cotent-title">
						<fmt:message key="Organization.management" />
					</div>
					<div class="search_form">
						<form class="form-horizontal row" id="ElockForm" action=""
							onsubmit="return false;">

							<input type="hidden" class="form-control input-sm"
								id="organizationId" name="organizationId" value="">
                            <!-- 机构名称 -->
							<div class="form-group col-md-6">
								<label class="col-sm-4 control-label"><fmt:message
										key="Organization.name" /></label>
								<div class="col-sm-8">
									<input type="text" id="s_organizationName"
										name="s_organizationName" class="form-control">
								</div>
							</div>
                            <!-- 机构简称 -->
							<div class="form-group col-sm-6">
								<label class="col-sm-4 control-label"><fmt:message
										key="Organization.shortName" /></label>
								<div class="col-sm-8">
									<input type="text" id="s_organizationShort"
										name="s_organizationShort" class="form-control">
								</div>
							</div>
							<!-- 机构类型-->
							<div class="form-group col-sm-6">
								<label class="col-sm-4 control-label"><fmt:message key="Organization.type"/></label>
								<div class="col-sm-8">
									<s:select name="s_organizationType"
										emptyOption="true" 
										cssClass="form-control" 
										theme="simple"
										list="@com.nuctech.ls.model.util.OrganizationType@values()"
										listKey="text" 
										listValue="key">
									</s:select>
								</div>
				            </div>
							<div class="clearfix"></div>
							<div class="form-group">
								<div class="col-sm-offset-8 col-md-4">
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
				<div class="tab-content dept-search">
					<div class="tab-cotent-title">
						<div class="Features pull-right">
							<ul>
								<li><a id="addElockBtn" class="btn btn-info btn-weight"><fmt:message
											key="common.button.add" /></a></li>
								<li><a id="editElockBtn" class="btn btn-info btn-weight"><fmt:message
											key="common.button.modify" /></a></li>
								<li><a id="deletea" class="btn btn-info btn-weight"
									onclick="delObject();"><fmt:message
											key="common.button.delete" /></a></li>
							</ul>
						</div>
						<fmt:message key="Organization.institution" />
					</div>
					<div class="search_table">
						<div>
							<table id="table">
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript"
		src="${root}/static/zTree_v3/js/jquery.ztree.core.js"></script>
	<script type="text/javascript"
		src="${root}/static/zTree_v3/js/jquery.ztree.excheck.js"></script>
	<script type="text/javascript"
		src="${root}/static/zTree_v3/js/jquery.ztree.exhide.js"></script>
	<script type="text/javascript">
		var selections = [];
		var $table = $('#table');
		//刷新tale
		$(window).resize(function(){
			$table.bootstrapTable("resetView");
		});
		function doSearch() {
			//取消树中被选中的节点
			var treeObj = $.fn.zTree.getZTreeObj("tree");
			treeObj.cancelSelectedNode();
			
			var params = $table.bootstrapTable('getOptions');
			params.url = "${root}/deptMgmt/dlist.action";
			params.queryParams = function(params) {
				//遍历form 组装json  
				$.each($("#ElockForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params[field.name] = field.value;
				});
				console.log(params);
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
				url:'${root}/deptMgmt/dlist.action',
				clickToSelect : true,
				showRefresh : false,
				search : false,
				showColumns : false,
				showExport : false,
				striped : true,
				method : "get",
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
					field : 'organizationName',
					title : $.i18n.prop('Organization.name'),
					sortable:true
				}, {
					field : 'organizationShort',
					title : $.i18n.prop('Organization.shortName'),
					sortable:true
				}, {
					field : 'longitude',
					title : $.i18n.prop('Organization.longitude'),
					sortable:true
				}, {
					field : 'latitude',
					title : $.i18n.prop('Organization.latitude'),
					sortable:true
				}, {
					field : 'organizationType',
					title : $.i18n.prop('Organization.type'),
					formatter : organizationTypeFormat,
					sortable:true
				},{
					field : 'reservationRatio',
					title : $.i18n.prop('Organization.reservationRatio'),
					sortable:true
				}],
			});

			//添加Modal调用方法
			$("#addElockBtn").click(function() {
				var organizationId = $("#organizationId").val();
				
				var url = "${root}/deptMgmt/addModal.action?organizationId="+organizationId;
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
			//模态框登录判断
			$('#addElockModal').on('show.bs.modal', function(e) {
				var content = $(this).find(".modal-content").html();
				needLogin(content);
			});

			//编辑Modal调用方法
			$("#editElockBtn")
					.click(
							function() {
								var ids = $.map($table
										.bootstrapTable('getSelections'),
										function(row) {
											return row.organizationId
										});
								if (ids.length == 0) {
									bootbox
											.alert($.i18n
													.prop('Organization.choose.department.toUpdate'));
								} else if (ids.length > 1) {
									bootbox
											.alert($.i18n
													.prop('Organization.choose.one.department.toUpdate'));
								} else {
									var url = "${root}/deptMgmt/editDepartment.action?orgId="
											+ ids;
									$('#updateElockModal').removeData(
											'bs.modal');
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
	</script>



	<!-- 删除记录 -->
	<script type="text/javascript">
		function delObject() {
			var list = $('#table').bootstrapTable('getSelections'); //获取表的行
			var ids = new Array();
			for ( var o in list) {
				ids.push(list[o].organizationId);
			}
			//国家、Admin Center、Control Room不能删除
			if(ids.indexOf('0000') > -1 || ids.indexOf('9001') > -1 || ids.indexOf('9003') > -1) {
				bootbox.alert($.i18n.prop('common.message.departCanNotDelete'));
				return false;
			}
			var organizationIds = ids.join(",");
			if (list.length <= 0) {
				bootbox.alert($.i18n
						.prop('Organization.choose.department.toDelete'));
				return;
			} else {
				var ajaxUrl = "${root}/deptMgmt/delDepartmentById.action"; /*路径改过，原先是相对路径 */
				bootbox
						.confirm(
								$.i18n.prop("are.you.sure"),
								function(result) {
									if (result) {
										$.ajax({
													url : ajaxUrl,
													type : "post",
													dataType : "json",
													data : {
														organizationIds : organizationIds
													},
													success : function(data) {
														if (data==true) {
															bootbox.alert($.i18n.prop('Organization.delete.success'));
															$('#table').bootstrapTable('refresh',{});
															var setting = {
																	async : {
																		enable : true,
																		type : "get",
																		url : root	+ '/deptMgmt/findDepartmentTree.action'
																	},
																	check : {
																		enable : false,
																		chkboxType : {"Y" : "s","N" : "s"
																		}
																	},
																	view : {
																		dblClickExpand : false
																	},
																	data : {
																		keep : {
																			parent : true
																		},
																		key : {
																			name : "name",
																			title : "name"
																		},
																		simpleData : {
																			enable : true,
																			idKey : "id",
																			pidKey : "pId",
																			rootId : 0
																		}
																	},
																	callback : {
																		onClick : checkDepartment
																	}
																};
															
															$.ajax({url : root+ '/deptMgmt/findDepartmentTree.action',
																dataType : "json",
																cache : false,
																success : function(data) {
																	if(!needLogin(data)) {
																		$.fn.zTree.init($("#tree"),setting,data.departmentList);
																	}
																}
															});
														}
													}
												});
										//window.location.reload();
											}
										})
									}
								}
	</script>
	<%--搜索表单重置 --%>
	<script type="text/javascript">
		function doRest() {
			$("#ElockForm")[0].reset();
		}
	</script>



	<!-- 组织机构树 -->
	<script type="text/javascript">
		$(function() {
			var setting = {
				async : {
					enable : true,
					type : "get",
					url : root + '/deptMgmt/findDepartmentTree.action'
				},
				check : {
					enable : false,
					chkboxType : {
						"Y" : "s",
						"N" : "s"
					}
				},
				view : {
					dblClickExpand : false
				},
				data : {
					keep : {
						parent : true
					},
					key : {
						name : "name",
						title : "name"
					},
					simpleData : {
						enable : true,
						idKey : "id",
						pidKey : "pId",
						rootId : 0
					}
				},
				callback : {
					onClick : checkDepartment
				}
			};
			$.ajax({
				url : root + '/deptMgmt/findDepartmentTree.action',
				dataType : "json",
				cache : false,
				success : function(data) {
					$.fn.zTree.init($("#tree"), setting, data.departmentList);
				}
			});
		});
	</script>


	<!--选中复选框，在列表中显示相应的口岸数据  -->
	<script type="text/javascript">
		function checkDepartment(e, treeId, treeNode) {
			debugger;
			var zTree = $.fn.zTree.getZTreeObj("tree")
			/* nodes = zTree.getCheckedNodes(true) */
			nodes = zTree.getSelectedNodes()
			checkedId = "";
			for (var i = 0; i < nodes.length; i++) {
				checkedId += nodes[i].id + ",";
			}
			$("#organizationId").val(checkedId);
			var params = $table.bootstrapTable('getOptions');
			params.url = "${root}/deptMgmt/list.action?checkedId="
					+ $("#organizationId").val();
			$table.bootstrapTable('refresh', params);
		}
	</script>

	<script type="text/javascript">
		function organizationTypeFormat(value, row, index) {
			var hidHtml = '<input type="hidden" name="orgType" value="' + value + '">';
			var show;
			if (value == '1') {
				show = $.i18n.prop('OrganizationType.Country');
			} else if (value == '2') {
				show = $.i18n.prop('OrganizationType.Port');
			} else if (value == '3') {
				show = $.i18n.prop('OrganizationType.Monitor_Place');
			}else if (value == '4') {
				show = $.i18n.prop('OrganizationType.Manage_Center');
			}else if (value == '5') {
				show = $.i18n.prop('OrganizationType.Device_room');
			}  else {
				show = '--'
			}
			return [ show ].join('');
		}
	</script>
</body>
</html>