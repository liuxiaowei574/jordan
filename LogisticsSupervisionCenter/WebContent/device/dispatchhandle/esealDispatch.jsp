<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title>子锁调度</title>
</head>
<body>
	<div class="row">
			<div class="col-md-12 my_news">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3>选择子锁</h3>
					</div>
					<div>
				<table id="select_eseal_table" >	</table>
					</div>
				<div class="Features pull-right">
         			<button type="button" class="btn btn-danger" data-dismiss="modal" id="esealDispatch">确认</button>
         			<button type="button" class="btn btn-darch" data-dismiss="modal">取消 </button>
				</div>
			</div>
		</div>
	<script type="text/javascript">
		var selections = [];
		var $table = $('#select_eseal_table');
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
				/* url : '${root}/esealMgmt/list.action', */
			    url : '${root}/esealMgmt/dlist.action', 
				height: $(window).height() - 300,//固定模态框 的 宽度 
				pagination : true,
				pageSize : 5,
				maintainSelected:true,
				
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
			    },],
			onLoadSuccess: function (data) {
		    	 var arrList = new Array();
		    	 //debugger;
		    	 var numbers = "${esealarrayList}"; //取出后台的arrayList(字符串)
		    	 if(numbers != null && "" != numbers){
		    		 numbers = numbers.substring(0,numbers.length - 1);//把字符串的最后一个 字符删除 (逗号)
		    		 arrList = numbers.split(",");
		    	 }
				$('#select_eseal_table').bootstrapTable("checkBy", {field:"esealNumber", values:arrList});
		     }
		});

		$("#esealDispatch").click(
				function() {
				   	var list = $('#select_eseal_table').bootstrapTable('getSelections'); //获取表的行
				   /* 模态框中的表头和调度页面表头中的field不同；新建一个conlist(属性名为esealNumber等)把原先list中的对象放到里面 */
				   	var esealList = new Array();
				   	for(var i=0;i<list.length;i++){
				   		var obj = new Object();
				   		obj.ESEAL_ID = list[i].esealId;
				   		obj.ESEAL_NUMBER=list[i].esealNumber;
				   		obj.ORGANIZATION_NAME = list[i].organizationName;
				   		obj.ESEAL_STATUS = list[i].esealStatus;
				   		esealList.push(obj);
				   	}
				   	/*将数据传到DeviceDispatch.jsp  */
				   	addEsealTable(esealList);
				}) 
	/* 勾选需调度的关锁，同时传多个Id
	$("#dispatch").click(
			function() {
				
				var list = $('#table').bootstrapTable('getSelections'); //获取表的行
				var ids = new Array();
				for ( var o in list) {
					ids.push(list[o].esealId);
				}
				var esealIds = ids.join(",");
				var url = "${root}/esealMgmt/esealDispatch.action?esealIds="+esealIds;
				window.location.href=url; 
			});  */
		});
</script>
</body>
</html>