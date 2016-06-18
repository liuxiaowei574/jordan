var $table = $("#inventoryListTable");
var $ids=[];

function searchInventoryListTable() {
	var url = root + "/inventoryReport/list.action";
	$table.bootstrapTable({
		showRefresh : false,
		search : false,
		showColumns : false,
		showExport : false,
		striped : true,
		height : "100%",
		url : url,
		method : "get",
		cache : false,
		pagination : true,
		sidePagination : 'client',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20, 30 ],
		columns : [{
			field : 'trackDeviceNumber',
			title : '关锁号'
		},{
			field : 'esealNumber',
			title : '子锁号'
		},{
			field : 'sensorNumber',
			title : '传感器锁号'
		}, {
			field : 'checkOutDate',
			title :  '流入时间'
		},  {
			field : 'checkInDate',
			title :  '流出时间'
		}, {
			field : 'formName',
			title : '来源口岸'
		}, {
			field : 'toName',
			title : '目的口岸'
		}]

	});
	$table.on('check.bs.table uncheck.bs.table '
					+ 'check-all.bs.table uncheck-all.bs.table', function() {
				// push or splice the selections if you want to save all data
				// selections
		$ids = getIdSelections();
	});
}

/**
 * 关锁流入
 */
function searchTrackDeviceFlowIn(){
	$("#deviceListTable").html("关锁流入信息列表");
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=trackDeviceFlowIn"});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
}

/**
 * 关锁流出
 */
function searchTrackDeviceFlowOut() {
	$("#deviceListTable").html("关锁流出信息列表");
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=trackDeviceFlowOut"});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
}

/**
 * 关锁转入
 */
function searchTrackDeviceTurnIn(){
	$("#deviceListTable").html("关锁转入信息列表");
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=trackDeviceTurnIn"});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
}

/**
 * 关锁转出
 */
function searchTrackDeviceTurnOut(){
	$("#deviceListTable").html("关锁转出信息列表");
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=trackDeviceTurnOut"});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
}


/**
 * 子锁流入
 */
function searchEsealFlowIn() {
	$("#deviceListTable").html("子锁流入信息列表");
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=esealFlowIn"});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
}


/**
 * 子锁流出
 */
function searchEsealFlowOut() {
	$("#deviceListTable").html("子锁流出信息列表");
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=esealFlowOut"});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
}

/**
 * 子锁转入
 */
function searchEsealTurnIn(){
	$("#deviceListTable").html("子锁转入信息列表");
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=esealTurnIn"});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
}

/**
 * 子锁转出
 */
function searchEsealTurnOut(){
	$("#deviceListTable").html("子锁转出信息列表");
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=esealTurnOut"});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'sensorNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
}

/**
 * 传感器流入
 */
function searchSensorFlowIn() {
	$("#deviceListTable").html("传感器流入信息列表");
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=sensorFlowIn"});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
}

/**
 * 传感器流出
 */
function searchSensorFlowOut() {
	$("#deviceListTable").html("传感器流出信息列表");
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=sensorFlowOut"});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
}

/**
 * 传感器转入
 */
function searchSensorTurnIn(){
	$("#deviceListTable").html("传感器转入信息列表");
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=sensorTurnIn"});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'checkOutDate');
}

/**
 * 传感器转出
 */
function searchSensorTurnOut(){
	$("#deviceListTable").html("传感器转出信息列表");
	$table.bootstrapTable('refresh', {url:  root + "/inventoryReport/list.action?type=sensorTurnOut"});
	showAllColums();
	$table.bootstrapTable('hideColumn', 'trackDeviceNumber');
	$table.bootstrapTable('hideColumn', 'esealNumber');
	$table.bootstrapTable('hideColumn', 'checkInDate');
}

function showAllColums() {
	$table.bootstrapTable('showColumn', 'trackDeviceNumber');
	$table.bootstrapTable('showColumn', 'esealNumber');
	$table.bootstrapTable('showColumn', 'sensorNumber');
	$table.bootstrapTable('showColumn', 'checkOutDate');
	$table.bootstrapTable('showColumn', 'checkInDate');
	$table.bootstrapTable('showColumn', 'formName');
	$table.bootstrapTable('showColumn', 'toName');
}


$(function() {
	searchInventoryListTable();
	//时间控件
	$("#form_checkinStartTime").datetimepicker({
		format: "yyyy-mm-dd hh:ii",//ii:分钟
		autoclose: true,
		todayBtn: true
	});
});