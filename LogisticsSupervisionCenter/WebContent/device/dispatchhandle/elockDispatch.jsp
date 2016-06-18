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
		<div class="row">
			<div class="col-md-12 my_news">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h3>选择关锁</h3>
			</div>
			<div>
				<table id="select_device_table" >	</table>
			</div>
				<div class="Features pull-right">
         			<button type="button" class="btn btn-danger" data-dismiss="modal" id="dispatch">确认</button>
         			<button type="button" class="btn btn-darch" data-dismiss="modal">取消 </button>
				</div>
			</div>
		</div>
	<script type="text/javascript">
		var selections = [];
		var $table = $('#select_device_table');
		$(function() {
			//设置传入参数
			$table.bootstrapTable({
					url:'${root}/warehouseElock/dlist.action', 
					height: $(window).height() - 300,//固定模态框 的 宽度 
					pagination : true,
					pageSize : 5,
					maintainSelected:true,
					
					
					
				 columns: [{
			    	checkbox : true
			    },{
			    	field: 'elockNumber',
			    	title: $.i18n.prop('WarehouseElock.elockNumber')
			    },{
			    	field: 'organizationName',//组织机构表里面的"机构名称"
			    	title: $.i18n.prop('WarehouseElock.belongTo')
			    },{
			    	field: 'simCard',
			    	title: $.i18n.prop('WarehouseElock.simCard')
			    },{
			    	field: 'interval',
			    	title: $.i18n.prop('WarehouseElock.interval')
			    },{
			    	field: 'gatewayAddress',
			    	title: $.i18n.prop('WarehouseElock.gatewayAddress')
			    },{
			    	field: 'elockStatus',
			    	title: $.i18n.prop('WarehouseElock.elockStatus')
			    }], 
			    onLoadSuccess: function (data) {
			    	 var arrList = new Array();
			    	 var numbers = "${arrayList}"; //取出后台的arrayList(字符串)
			    	 if(numbers != null && "" != numbers){
			    		 numbers = numbers.substring(0,numbers.length - 1);//把字符串的最后一个 字符删除 (逗号)
			    		 arrList = numbers.split(",");
			    	 }
		             
					$('#select_device_table').bootstrapTable("checkBy", {field:"elockNumber", values:arrList});
			     }
 			});
			$("#dispatch").click(
					function() {
					   	var list = $('#select_device_table').bootstrapTable('getSelections'); //获取表的行
					   /* 模态框中的表头和调度页面表头中的field不同；新建一个conlist(属性名为ELOCK_NUMBER等)把原先list中的对象放到里面 */
					   	var conlist = new Array();
					   	for(var i=0;i<list.length;i++){
					   		var obj = new Object();
					   		obj.ELOCK_ID = list[i].elockId;
					   		obj.ELOCK_NUMBER=list[i].elockNumber;
					   		obj.ORGANIZATION_NAME = list[i].organizationName;
					   		obj.SIM_CARD = list[i].simCard;
					   		obj.INTERVAL = list[i].interval;
					   		obj.GATEWAY_ADDRESS = list[i].gatewayAddress;
					   		obj.ELOCK_STATUS = list[i].elockStatus;
					   		conlist.push(obj);
					   	}
					   	/*将数据传到DeviceDispatch.jsp  */
					   	addElockTable(conlist);
					}) 
			});
		
		
	</script>
	
	
</body>
</html>