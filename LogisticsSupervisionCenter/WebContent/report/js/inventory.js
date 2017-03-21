var $table = $("#inventoryListTable");
var $ids=[];
var param = null;

$(window).resize(function(){
	$table.bootstrapTable("resetView");
});

function searchInventoryListTable() {
	//var url = root + "/inventoryReport/list.action";
	$table.bootstrapTable({
		showRefresh : false,
		search : false,
		showColumns : false,
		showExport : false,
		striped : true,
		height : "100%",
		//url : url,
		method : "get",
		cache : false,
		pagination : true,
		sidePagination : 'client',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20, 30 ],
		columns : [{
			field : 'trackDeviceNumber',
			title : $.i18n.prop('report.inventory.trackDeviceNumber'),
			sortable:true
		},{
			field : 'esealNumber',
			title : $.i18n.prop('report.inventory.esealNumber'),
			sortable:true
		},{
			field : 'sensorNumber',
			title : $.i18n.prop('report.inventory.sensorNumber'),
			sortable:true
		}, {
			field : 'checkOutDate',
			title :  $.i18n.prop('report.inventory.checkOutDate'),
			sortable:true
		},  {
			field : 'checkInDate',
			title :  $.i18n.prop('report.inventory.checkInDate'),
			sortable:true
		}, {
			field : 'formName',
			title : $.i18n.prop('report.inventory.formName'),
			sortable:true
		}, {
			field : 'toName',
			title : $.i18n.prop('report.inventory.toName'),
			sortable:true
		}, {
			field : 'trackDeviceStatus',
			title : $.i18n.prop('WarehouseElock.elockStatus'),
			formatter : deviceStatusFormat,
			sortable:true
		}, {
			field : 'esealStatus',
			title : $.i18n.prop('warehouseEsealBO.esealStatus'),
			formatter : deviceStatusFormat,
			sortable:true
		}, {
			field : 'sensorStatus',
			title : $.i18n.prop('WarehouseSensor.sensorStatus'),
			formatter : deviceStatusFormat,
			sortable:true
		},]

	});
	$table.on('all.bs.table load-success.bs.table load-error.bs.table', function() {
		$ids = getIdSelections();
	});
}

//设备状态转换
function deviceStatusFormat(value, row, index) {
	var show;
	if (value == '0') {
		show = $.i18n.prop('DeviceStatus.Scrap');
	} else if (value == '1') {
		show = $.i18n.prop('DeviceStatus.Normal');
	} else if (value == '2') {
		show = $.i18n.prop('DeviceStatus.Inway');
	} else if (value == '3') {
		show = $.i18n.prop('DeviceStatus.Destory');
	} else if (value == '4') {
		show = $.i18n.prop('DeviceStatus.Maintain');
	} else {
		show = '--'
	}
	return [ show ].join('');
}

function getIdSelections() {
	return $.map($table.bootstrapTable('getSelections'),
			function(row) {
				return row.userId
			});
}
function searchInventoryListTable1() {
//	var url = root + "/inventoryReport/list.action";
	$table.bootstrapTable({
		showRefresh : false,
		search : false,
		showColumns : false,
		showExport : false,
		striped : true,
		height : "100%",
//		url : url,
		method : "get",
		cache : false,
		pagination : true,
		sidePagination : 'client',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20, 30 ],
		columns : [{
			field : 'trackDeviceNumber',
			title : $.i18n.prop('report.inventory.trackDeviceNumber'),
			sortable:true
		},{
			field : 'esealNumber',
			title : $.i18n.prop('report.inventory.esealNumber'),
			sortable:true
		},{
			field : 'sensorNumber',
			title : $.i18n.prop('report.inventory.sensorNumber'),
			sortable:true
		},{
			field : 'deviceStatus',
			title : $.i18n.prop('report.inventory.toName'),
			formatter : deviceStatusFormat,
			sortable:true
		}]

	});
}

/**
 * 关锁流入
 */
function searchTrackDeviceFlowIn(){
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.trackDevice.flowIn.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=trackDeviceFlowIn&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'esealStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
}

/**
 * 关锁流出
 */
function searchTrackDeviceFlowOut() {
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.trackDevice.flowOut.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=trackDeviceFlowOut&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'esealStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
}

/**
 * 关锁转入
 */
function searchTrackDeviceTurnIn(){
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.trackDevice.turnIn.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=trackDeviceTurnIn&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'esealStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
	//alert($('#inventoryListTable tr:eq(0) th:eq(1)').children(":first").html());
	$('#inventoryListTable tr:eq(0) th:eq(1)').children(":first").html($.i18n.prop('report.inventory.turnInDate'));//转入时间
}

/**
 * 关锁转出
 */
function searchTrackDeviceTurnOut(){
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.trackDevice.turnOut.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=trackDeviceTurnOut&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'esealStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
	$('#inventoryListTable tr:eq(0) th:eq(1)').children(":first").html($.i18n.prop('report.inventory.turnOutDate'));//转出时间
}

/**
 * 关锁库存量
 */
function searchTrackDeviceInventory(){
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.trackDevice.inventory.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=trackDeviceInventory&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
	$table.bootstrapTable('hideColumn', 'formName');
	$table.bootstrapTable('hideColumn', 'toName');
	$table.bootstrapTable('hideColumn', 'esealStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
}

/**
 * 子锁流入
 */
function searchEsealFlowIn() {
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.eseal.flowIn.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=esealFlowIn&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');	
	$table.bootstrapTable('hideColumn', 'checkOutDate');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'esealStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
}


/**
 * 子锁流出
 */
function searchEsealFlowOut() {
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.eseal.flowOut.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=esealFlowOut&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'esealStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
}

/**
 * 子锁转入
 */
function searchEsealTurnIn(){
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.eseal.turnIn.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=esealTurnIn&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'esealStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
	$('#inventoryListTable tr:eq(0) th:eq(1)').children(":first").html($.i18n.prop('report.inventory.turnInDate'));//转入时间

}

/**
 * 子锁转出
 */
function searchEsealTurnOut(){
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.eseal.turnOut.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=esealTurnOut&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'esealStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
	$('#inventoryListTable tr:eq(0) th:eq(1)').children(":first").html($.i18n.prop('report.inventory.turnOutDate'));//转出时间
}

/**
 * 子锁库存量
 */
function searchEsealInventory(){
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.eseal.inventory.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=esealInventory&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
	$table.bootstrapTable('hideColumn', 'formName');
	$table.bootstrapTable('hideColumn', 'toName');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
}

/**
 * 传感器流入
 */
function searchSensorFlowIn() {
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.sensor.flowIn.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=sensorFlowIn&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'esealStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
}

/**
 * 传感器流出
 */
function searchSensorFlowOut() {
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.sensor.flowOut.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=sensorFlowOut&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'esealStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
}

/**
 * 传感器转入
 */
function searchSensorTurnIn(){
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.sensor.turnIn.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=sensorTurnIn&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'esealStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
	$('#inventoryListTable tr:eq(0) th:eq(1)').children(":first").html($.i18n.prop('report.inventory.turnInDate'));//转入时间
}

/**
 * 传感器转出
 */
function searchSensorTurnOut(){
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.sensor.turnOut.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=sensorTurnOut&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'esealStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
	$('#inventoryListTable tr:eq(0) th:eq(1)').children(":first").html($.i18n.prop('report.inventory.turnOutDate'));//转出时间
}

/**
 * 传感器库存量
 */
function searchSensorInventory(){
	searchInventoryListTable();//初始化库存详细信息列表
	var s_mode = $("#s_mode").val();
	var s_checkinStartTime = $("#s_checkinStartTime").val();
	var s_belongTo =  $("#s_belongTo").val();
	$("#deviceListTable").html($.i18n.prop('report.inventory.detail.sensor.inventory.list'));
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=sensorInventory&s_mode="+s_mode +"&s_checkinStartTime="+ s_checkinStartTime+"&s_belongTo="+ s_belongTo});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
	$table.bootstrapTable('hideColumn', 'formName');
	$table.bootstrapTable('hideColumn', 'toName');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'esealStatus');
}

//显示信息列表所有列
function showAllColums() {
	$table.bootstrapTable('showColumn', 'trackDeviceNumber');
	$table.bootstrapTable('showColumn', 'esealNumber');
	$table.bootstrapTable('showColumn', 'sensorNumber');
	$table.bootstrapTable('showColumn', 'checkOutDate');
	$table.bootstrapTable('showColumn', 'checkInDate');
	$table.bootstrapTable('showColumn', 'formName');
	$table.bootstrapTable('showColumn', 'toName');
	$table.bootstrapTable('showColumn', 'trackDeviceStatus');
	$table.bootstrapTable('showColumn', 'esealStatus');
	$table.bootstrapTable('showColumn', 'sensorStatus');
}

//隐藏信息列表所有列
function hideAllColums() {
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
	$table.bootstrapTable('hideColumn', 'checkInDate');
	$table.bootstrapTable('hideColumn', 'formName');
	$table.bootstrapTable('hideColumn', 'toName');
	$table.bootstrapTable('hideColumn', 'trackDeviceStatus');
	$table.bootstrapTable('hideColumn', 'esealStatus');
	$table.bootstrapTable('hideColumn', 'sensorStatus');
}


//统计条件联动效果
function changeStatisticsMode() {
	var value = $("#s_mode").val();
	if(value == 'year') {
		$('#form_checkinStartTime').datetimepicker('remove');
		//时间控件
		$("#form_checkinStartTime").datetimepicker({
			format: "yyyy",//ii:分钟
			autoclose: true,
			startView: 4,
			minView: 4,
			maxView : 4,
			todayBtn: true
		});
		$("#form_checkinStartTime").datetimepicker('update', (s_mode == 'year') && s_checkinStartTime || new Date());
	} else if(value == 'month') {
		$('#form_checkinStartTime').datetimepicker('remove');
		//时间控件
		$("#form_checkinStartTime").datetimepicker({
			format: "yyyy-mm",//ii:分钟
			autoclose: true,
			startView: 3,
			minView: 3,
			maxView : 3,
			todayBtn: true
		});
		$("#form_checkinStartTime").datetimepicker('update', (s_mode == 'month') && s_checkinStartTime || new Date());
	} else if(value == 'day') {
		$('#form_checkinStartTime').datetimepicker('remove');
		//时间控件
		$("#form_checkinStartTime").datetimepicker({
			format: "yyyy-mm-dd",//ii:分钟
			autoclose: true,
			startView: 2,
			minView: 2,
			maxView : 2,
			todayBtn: true
		});
		$("#form_checkinStartTime").datetimepicker('update', (s_mode == 'day')  && s_checkinStartTime || new Date());
	}
}

//库存相关量请求
function search(){
	$.ajax({
		type:"post",
		url:root+"/inventoryReport/toList.action",
		dataType:"json",
		async:false,
		cache:false,
		data:param,
		success:function(v){
			console.log(v);	
			$("#trackDeviceFlowIn").html(v.trackDeviceFlowIn);//关锁流入
			$("#trackDeviceFlowOut").html(v.trackDeviceFlowOut);//关锁流出
			$("#trackDeviceTurnIn").html(v.trackDeviceTurnIn);//关锁转入
			$("#trackDeviceTurnOut").html(v.trackDeviceTurnOut);//关锁转出
			$("#trackDeviceInventory").html(v.trackDeviceInventory);//关锁库存
			$("#esealFlowIn").html(v.esealFlowIn);//子锁流入
			$("#esealFlowOut").html(v.esealFlowOut);//子锁流出
			$("#esealTurnIn").html(v.esealTurnIn);//子锁转入
			$("#esealTurnOut").html(v.esealTurnOut);//子锁转出
			$("#esealInventory").html(v.esealInventory);//子锁库存
			$("#sensorFlowIn").html(v.sensorFlowIn);//传感器流入
			$("#sensorFlowOut").html(v.sensorFlowOut);//传感器流出
			$("#sensorTurnIn").html(v.sensorTurnIn);//传感器转入
			$("#sensorTurnOut").html(v.sensorTurnOut);//传感器转出
			$("#sensorInventory").html(v.sensorInventory);//传感器库存
		},
		error:function(e,v){
			
		}
		
	});
}

//（页面初始加载）非条件查询库存相关量
$(function(){
	//param=$("#searchForm").serializeArray();
	search();
})

//条件查询关锁、子锁及传感器的数量
function searchReportIndex(){
	param=$("#searchForm").serializeArray();
	search();
}	

$(function() {
	//searchInventoryListTable();
//	if(systemModules.isDispatchOn) {
//		searchInventoryListTable();
//	}else{
//		searchInventoryListTable1();
//	}
	changeStatisticsMode();
	hideAllColums();
});

//重置
function doRest(){
	$("#searchForm")[0].reset();
}