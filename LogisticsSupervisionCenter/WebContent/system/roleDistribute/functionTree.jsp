<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key="Authority.management"/></title>
<style type="text/css">
#portForm .input-group-btn>button {
	height: 30px;
}
</style>
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
	<div class="modal-body"style="height:450px;overflow:auto">
		<ul id="tree" class="ztree" style="width: 440px; "></ul>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
		<button type="button" class="btn btn-danger" id="treeDistribute">
			<fmt:message key="common.button.save" />
		</button>
		<button type="button" class="btn btn-darch" data-dismiss="modal">
			<fmt:message key="common.button.cancle" />
		</button>
	</div>
	<script type="text/javascript"
		src="${root}/static/zTree_v3/js/jquery.ztree.core.js"></script>
	<script type="text/javascript"
		src="${root}/static/zTree_v3/js/jquery.ztree.excheck.js"></script>
	<script type="text/javascript"
		src="${root}/static/zTree_v3/js/jquery.ztree.exhide.js"></script>
	<script type="text/javascript">
		$(function() {
			var setting = {
				async : {
					enable : true,
					type : "get",
					url : root + '/roleDistribute/findFunctionTree.action'
				},
				check : {
					enable : true,
					chkboxType : {
						"Y" : "ps",
						"N" : "ps"
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
					/* onClick : checkDepartment */
				}
			};
			$.ajax({
				url : root + '/roleDistribute/findFunctionTree.action',
				dataType : "json",
				cache : false,
				success : function(data) {
					//后台“request.setAttribute("functionIds", systemRoleFunctionsBO.getFunctionsId());”
					var id ="${functionIds}"
					//将字符串转化为字符串数组
					var idList = id.split(",");
					$.fn.zTree.init($("#tree"), setting, data.functionList);
					var funTree = $.fn.zTree.getZTreeObj("tree");
					funTree.expandAll(true);
					var nodes  = funTree.transformToArray(funTree.getNodes());//获取所有节点
					for(var i=0;i<nodes.length;i++){
						//alert(nodes[i].id)
						for(var j=0; j<idList.length;j++){
							if(nodes[i].id==idList[j]){
								nodes[i].checked=true;
								funTree.updateNode(nodes[i]);
							}
						}
					}
					
				}
			});
		});
	</script>
	<!-- 修改角色功能表 -->
	<script type="text/javascript">
		$("#treeDistribute").click(function() {
			var roleId ="${roleId}"
			/* 获取选中复选框的functionId */
			var funTree = $.fn.zTree.getZTreeObj("tree");
			var nodesList = funTree.getCheckedNodes(true);
			var ids = new Array();			
			for(var o in nodesList){
				ids.push(nodesList[o].id);
			}
			var functionIds = ids.join(",");
			var ajaxUrl = "${root}/roleDistribute/updateRoleFunction.action";
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
							$('#treeFunctionModal').modal('hide');
							bootbox.success($.i18n.prop('Authority.management.function.distribute.success'),
								function(result) {$table.bootstrapTable('refresh',{});
								window.location.reload();//后期更改
							});
						}
					}
				}
			});
		})
	</script>
</body>
</html>