/*****************全局参数******************/
var $table = $("#tripListTable");
var $ids=[];
var $status = [];
/*****************公用方法******************/
/**
 * 行程列表
 */
function searchTripList() {
	var url = root + "/monitortrip/list.action";
	$table.bootstrapTable({
		clickToSelect : true,
		singleSelect: true,
		showRefresh : false,
		search : true,
		showColumns : true,
		showExport : false,
		striped : true,
		height : "100%",
		url : url,
		method : "get",
		idfield: "tripId",
		sortName:"checkinTime",
		sortOrder: 'asc',
		sortable: true,
		cache : false,
		//queryParams : queryParams,
		//queryParamsType : "not-limit",
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20, 30 ],
		columns : [ {
			checkbox : true
		}, {
			field : 'trackingDeviceNumber',
			title : $.i18n.prop('trip.label.trackingDeviceNumber')
		}, {
			field : 'esealNumber',
			title :  $.i18n.prop('trip.label.esealNumber')
		}, {
			field : 'sensorNumber',
			title : $.i18n.prop('trip.label.sensorNumber')
		}, {
			field : 'vehiclePlateNumber',
			title : $.i18n.prop('trip.label.vehiclePlateNumber')
		}, {
			field : 'tripStatus',
			title : $.i18n.prop('trip.label.tripStatus'),
			formatter : stateFormatter
		}, {
			field : 'checkinTime',
			title : $.i18n.prop('trip.label.checkinTime')
		}, {
			field : 'checkinPort',
			title : $.i18n.prop('trip.label.checkinPort')
		}, {
			field : 'checkoutTime',
			title : $.i18n.prop('trip.label.checkoutTime')
		}, {
			field : 'checkoutPort',
			title : $.i18n.prop('trip.label.checkoutPort')
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
 * 获取选中的ID
 */
function getIdSelections() {
	return $.map($table.bootstrapTable('getSelections'),
			function(row) {
				return row.tripId;
			});
}

/**
 * 状态显示
 * 
 * @param value
 * @param row
 * @param index
 */
function stateFormatter(value, row, index) {
	var show;
	 if (value == '0') {
		show = $.i18n.prop('trip.label.tripStatus.started');
	} else if(value == '1') {
		show = $.i18n.prop('trip.label.tripStatus.finished');
	} else {
		show = '--';
	}
	return [show].join('');
}

/**
 * 条件查询方法
 */
function search(){
	var options = $table.bootstrapTable('getOptions');
	options.queryParams = function(params) {
        //遍历form 组装json  
        $.each($("#searchForm").serializeArray(), function(i, field) {  
            //console.info(field.name + ":" + field.value + " ");  
            //可以添加提交验证                   
            params[field.name] = field.value;  
        });  
        return params;  
    };
	$table.bootstrapTable('refresh', options);
}

/****************init********************/
$(function() {
	// 设置表格
	searchTripList();
	
	//编辑数据
	$("#editBtn").click(function() {
		if($ids.length == 0) {
			bootbox.alert($.i18n.prop("trip.edit.choose"));
		} else if($ids.length > 1){
			bootbox.alert($.i18n.prop("trip.edit.choose.only"));
		} else {
			var url = root + "/monitortrip/toEdit.action?tripVehicleVO.tripId="+$ids;
			window.location.href = url;
			/*
			$('#tripEditModal').removeData('bs.modal').modal({
				remote : url,
				show : false,
				backdrop: 'static', 
				keyboard: false
			});
			$('#tripEditModal').on('loaded.bs.modal', function(e) {
				$('#tripEditModal').modal('show');
			});
			*/
		}
		
	});
});