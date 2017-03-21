/*****************全局参数******************/
var $table = $("#tripListTable");
var $ids=[];

$(window).resize(function(){
	$table.bootstrapTable("resetView");
});
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
		search : false,
		showColumns : false,
		showRefresh:false,
		showExport : false,
		striped : true,
		height : "100%",
		url : url,
		method : "get",
		idfield: "tripId",
		sortName:"checkoutTime",
		sortOrder: 'desc',
		sortable: true,
		cache : false,
		//queryParams : queryParams,
		//queryParamsType : "not-limit",
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 5,
		pageList : [ 10, 20, 30 ],
		columns : [{
			field : 'vehiclePlateNumber',
			title :  $.i18n.prop('trip.report.label.vehiclePlateNumber'),
			sortable:true
		}, {
			field : 'declarationNumber',
			title : $.i18n.prop('trip.report.label.declarationNumber'),
			sortable:true
		}, {
			field : 'trackingDeviceNumber',
			title : $.i18n.prop('trip.report.label.trackingDeviceNumber'),
			sortable:true
		}, {
			field : 'esealNumber',
			title :  $.i18n.prop('trip.report.label.esealNumber'),
			sortable:true
		}, {
			field : 'sensorNumber',
			title :  $.i18n.prop('trip.report.label.sensorNumber'),
			sortable:true
		}, {
			field : 'checkinUserName',
			title : $.i18n.prop('trip.report.label.checkinUser'),
			sortable:true
		}, {
			field : 'checkinTime',
			title : $.i18n.prop('trip.report.label.checkinTime'),
			sortable:true
		}, {
			field : 'checkoutUserName',
			title : $.i18n.prop('trip.report.label.checkoutUser'),
			sortable:true
		}, {
			field : 'checkoutTime',
			title : $.i18n.prop('trip.report.label.checkoutTime'),
			sortable:true
		}, {
			field : 'tripStatus',
			title :  $.i18n.prop('trip.report.label.tripStatus'),
			formatter : stateFormatter,
			sortable:true
		}, {
			field : 'tripId',
			title : $.i18n.prop('trip.report.list.operate'),
			formatter : editFormatter
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

function editFormatter(value, row, index){
	if(row.checkinUser == sessionUserId && row.tripStatus == '0') {
		var url = root + '/monitortrip/toEdit.action?tripVehicleVO.tripId=' + value;
		var link = '<a href="' + url + '" >' + $.i18n.prop('trip.activate.button.edit') + '</a>';
		return link;
	}
	return '';
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
		show = $.i18n.prop('trip.report.label.tripStatus.toStart');
	} else if(value == '1') {
		show = $.i18n.prop('trip.report.label.tripStatus.started');
	} else if(value == '2') {
		show = $.i18n.prop('trip.report.label.tripStatus.toFinish');
	} else if(value == '3') {
		show = $.i18n.prop('trip.report.label.tripStatus.finished');
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
function doRest(){
	$("#searchForm")[0].reset();
	function resetQuery() {
		$table.bootstrapTable('refresh', {});
	}
	window.location.reload();
}
/**
 * 查询所有检出口岸
 */
function getCheckoutPort(){
	$.get(root + "/deptMgmt/findAllPortByUserId.action", {}, function(data){
		var $port = $("#s_checkoutPort, #s_checkinPort");
		$port.empty().append('<option value=""></option>');
		if(data && data.total > 0) {
			var rows = data.rows;
			$.each(rows, function(index, item){
				$port.append("<option value='" + item.organizationId + "'>" + item.organizationName + "</option>");
			});
		}
	}, 'json');
}
function toEdit(tripId){
	var url = root + "/monitortrip/toEdit.action?tripVehicleVO.tripId="+tripId;
	window.location.href = url;
}
/****************init********************/
$(function() {
	// 设置表格
	searchTripList();
	
	//编辑数据
	$("#editBtn").click(function() {
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
		
	});
	
	getCheckoutPort();
});