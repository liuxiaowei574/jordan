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
						<h3>选择传感器</h3>
			</div>
			<div>
				<table id="select_sensor_table" >	</table>
			</div>
				<div class="Features pull-right">
         			<button type="button" class="btn btn-danger" data-dismiss="modal" id="sensorDispatch">确认</button>
         			<button type="button" class="btn btn-darch" data-dismiss="modal">取消 </button>
				</div>
			</div>
		</div>
	<script type="text/javascript">
		var selections = [];
		var $table = $('#select_sensor_table');
		$(function() {
			$table.bootstrapTable({
					url:'${root}/sensorMgmt/dlist.action', 
					height: $(window).height() - 300,//固定模态框 的 宽度 
					pagination : true,
					pageSize : 5,
					maintainSelected:true,
					
				 columns: [{
			    	checkbox : true
			    },{
			    	field: 'sensorNumber',
			    	title: "传感器编号"
			    },{
			    	field: 'organizationName',//组织机构表里面的"机构名称"
			    	title: "所属节点"
			    },{
			    	field: 'sensorStatus',
			    	title: "传感器状态"
			    },{
			    	field: 'sensorType',
			    	title: "传感器类型"
			    }], 
			    onLoadSuccess: function (data) {
			    	
			    	 var arrList = new Array();
			    	 var numbers = "${sensorarrayList}"; //取出后台的sensorarrayList(字符串)
			    	 if(numbers != null && "" != numbers){
			    		 numbers = numbers.substring(0,numbers.length - 1);//把字符串的最后一个 字符删除 (逗号)
			    		 arrList = numbers.split(",");
			    	 }
		             
					$('#select_sensor_table').bootstrapTable("checkBy", {field:"sensorNumber", values:arrList});
			     }
 			});
			$("#sensorDispatch").click(
					function() {
					   	var list = $('#select_sensor_table').bootstrapTable('getSelections'); //获取表的行
					   /* 模态框中的表头和调度页面表头中的field不同；新建一个conlist(属性名为ELOCK_NUMBER等)把原先list中的对象放到里面 */
					   	var sensorList = new Array();
					   	for(var i=0;i<list.length;i++){
					   		var obj = new Object();
					   		obj.SENSOR_ID = list[i].sensorId;
					   		obj.SENSOR_NUMBER=list[i].sensorNumber;
					   		obj.ORGANIZATION_NAME = list[i].organizationName;
					   		obj.SENSOR_STATUS = list[i].sensorStatus;
					   		obj.SENSOR_TYPE = list[i].sensorType;
					   		sensorList.push(obj);
					   	}
					   	/*将数据传到DeviceDispatch.jsp  */
					   	addSensorTable(sensorList);
					}) 
					
			});
		
		
	</script>
	
	
</body>
</html>