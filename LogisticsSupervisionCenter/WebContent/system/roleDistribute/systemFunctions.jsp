<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="Authority.management" /></title>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel">
			<fmt:message key="Authority.management.distribute" />
		</h4>
	</div>
	<div class="modal-body">
		<div class="search_table" style="height:35em;overflow: auto">
			<table  id="roleFunction">
			</table>
		</div>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
		<button type="button" class="btn btn-danger" id="distribute">
			<fmt:message key="common.button.save" />
		</button>
		<button type="button" class="btn btn-darch" data-dismiss="modal">
			<fmt:message key="common.button.cancle" />
		</button>
	</div>
	<script type="text/javascript">
		var selections = [];
		var $atable = $('#roleFunction');
		$(function() {
			//设置传入参数
			$atable.bootstrapTable({
				url : '${root}/roleDistribute/functionList.action',
				pagination : true,
				pageSize : 10,
				maintainSelected : true,

				columns : [ {
					checkbox : true
				}, {
					field : 'functionId',
					title : $.i18n.prop('Authority.management.function.number')
				}, {
					field : 'functionName',
					title : $.i18n.prop('Authority.management.function.name'),
					formatter : nameFormatter
				}, {
					field : 'functionType',
					title : $.i18n.prop('Authority.management.function.type'),
					formatter : typeFormatter 
				} ],
				onLoadSuccess : function(data) {
					var arrList = new Array();
					var numbers = "${sFunctionsId}"; //取出后台的sFunctionsId(字符串)
					if (numbers != null && "" != numbers) {
						//numbers = numbers.substring(0, numbers.length - 1);//把字符串的最后一个 字符删除 (逗号)
						arrList = numbers.split(",");
					}

					$('#roleFunction').bootstrapTable("checkBy", {
						field : "functionId",
						values : arrList
					});
				}
			});
		});
		function typeFormatter(value, row, index) {
			var show;
			 if (value == '0') {
				show = $.i18n.prop('Authority.management.menu');
			} else if(value == '1') {
				show = $.i18n.prop('Authority.management.function');
			} else {
				show = '--';
			}
			return [show].join('');
		}
		
		/* 将功能树中的功能名称格式化 */
		function nameFormatter(value, row, index) {
			var show;
			 if (!!value) {
				show = $.i18n.prop(value);
			}else {
				show = '--';
			}
			return [show].join('');
		}
	</script>
	
	<script type="text/javascript">
		$("#distribute").click(
				function() {
					var roleId = "${roleId}";//获取从角色管理页面传递的roleId
					/* 获取勾选的functionId */
					var list = $('#roleFunction').bootstrapTable(
							'getSelections'); //获取表的行
					var ids = new Array();
					for ( var o in list) {
						ids.push(list[o].functionId);
					}
					var functionIds = ids.join(",");
					var ajaxUrl = "${root}/roleDistribute/modifyRoleFunction.action";
					$.ajax({
								url : ajaxUrl,
								type : "post",
								dataType : "json",
								data : {
									roleId : roleId,
									functionIds : functionIds
								},
								success : function(data) {
									var needLoginFlag = false;
									if(typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
										needLoginFlag = needLogin(data);
									}
									if(!needLoginFlag) {
										if (data == true) {
											$('#functionModal').modal('hide');
											bootbox.success($.i18n.prop('Authority.management.function.distribute.success'),
												function(result) {
													$table.bootstrapTable('refresh',{});
													window.location.reload();//后期更改
											});
										}
									}
								}
							});
				});
	</script>
	
</body>
</html>
