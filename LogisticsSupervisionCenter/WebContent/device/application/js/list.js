/*****************全局参数******************/
var $table = $("#warehouseDeviceApplicationTable");
var $ids=[];
var $status = [];
/*****************公用方法******************/
//设置传入参数
function queryParams(params) {
	//遍历form 组装json  
   $.each($("#searchForm").serializeArray(), function(i, field) {  
        console.info(field.name + ":" + field.value + " ");  
        //可以添加提交验证                   
        params += "&" + field.name +"="+ field.value;  
    }); 
    return params;
}

/**
 * 通知列表
 */
function searchDeviceApplicationList() {
	var url = root + "/warehouseDeviceApplication/list.action";
	$table.bootstrapTable({
		clickToSelect : true,
		showRefresh : false,
		search : false,
		showColumns : false,
		showExport : false,
		striped : true,
		height : "100%",
		url : url,
		method : "get",
		idfield: "applicationId",
		sortName:"applicationId",
		cache : false,
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20, 30 ],
		columns : [ {
			checkbox : true
		}, {
			field : 'applcationPortName',
			title : $.i18n.prop('warehouse.device.application.portName')
		}, {
			field : 'deviceNumber',
			title :  $.i18n.prop('warehouse.device.application.trackDevice')
		}, {
			field : 'esealNumber',
			title : $.i18n.prop('warehouse.device.application.eseal')
			//formatter : stateFormatter
		}, {
			field : 'sensorNumber',
			title : $.i18n.prop('warehouse.device.application.sensor')
		}, {
			field : '',
			title : $.i18n.prop('warehouse.device.application.operate'),
			formatter : operateFormatter
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
 * 操作显示
 */
function operateFormatter(value, row, index) {
	return [
			'<a class="like" href="javascript:dispatchAnalysis(\'' + row.applicationId
					+ '\')">',
			'<i class="glyphicon glyphicon-pencil"></i>',
			'</a>  ',
	].join('');
}


/**
 * 获取选中的ID
 */
function getIdSelections() {
	return $.map($table.bootstrapTable('getSelections'),
			function(row) {
				return row.applicationId;
			});
}

/**
 * 条件查询方法
 */
function search(){
	var params = $table.bootstrapTable('getOptions');
	params.queryParams = function(params) {
        //遍历form 组装json  
        $.each($("#searchForm").serializeArray(), function(i, field) {  
            console.info(field.name + ":" + field.value + " ");  
            //可以添加提交验证                   
            params[field.name] = field.value;  
        });  
        console.log(params);
        return params;  
    }
	$table.bootstrapTable('refresh', {});
}

/**
 * 通知接收人列表
 */
function dispatchAnalysis(id) {
	var url = root + "/warehouseDispatchAnalysis/index.action?applicationId="+id;
	window.location.href=url;
}

/**
 * 
 */
function refesh() {
	$table.bootstrapTable('refresh', params);
}

/****************init********************/
$(function() {
	// 设置表格
	searchDeviceApplicationList();
	
	/**
	 * 添加对话框
	 */
	$("#addWarehouseDeviceApplicationButton").click(function() {
		var url = root + "/warehouseDeviceApplication/addModal.action";
		$('#warehouseDeviceApplicationAddModal').removeData('bs.modal');
		$('#warehouseDeviceApplicationAddModal').modal({
			remote : url,
			show : false,
			backdrop: 'static', 
			keyboard: false
		});
		
		$('#warehouseDeviceApplicationAddModal').on('loaded.bs.modal', function(e) {
			$('#warehouseDeviceApplicationAddModal').modal('show');
		});
	});
});