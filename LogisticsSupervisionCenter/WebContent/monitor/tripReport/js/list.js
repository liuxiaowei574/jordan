/*****************全局参数******************/
var $table = $("#tripReportListTable");
/*****************公用方法******************/
/**
 * 行程监管信息列表
 */
function searchTripList() {
	var url = root + "/monitortripreport/list.action";
	$table.bootstrapTable({
		singleSelect: true,
		showRefresh : false,
		showColumns : false,
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
		pageList : [ 5, 10, 20, 30 ],
		columns : [{
			field : 'vehiclePlateNumber',
			title :  $.i18n.prop('trip.report.label.vehiclePlateNumber')
		}, {
			field : 'driverName',
			title : $.i18n.prop('trip.report.label.driverName')
		}, {
			field : 'trackingDeviceNumber',
			title : $.i18n.prop('trip.report.label.trackingDeviceNumber')
		}, {
			field : 'esealNumber',
			title :  $.i18n.prop('trip.report.label.esealNumber')
		}, {
			field : 'sensorNumber',
			title :  $.i18n.prop('trip.report.label.sensorNumber')
		}, {
			field : 'checkinUserName',
			title : $.i18n.prop('trip.report.label.checkinUser')
		}, {
			field : 'checkinTime',
			title : $.i18n.prop('trip.report.label.checkinTime')
		}, {
			field : 'checkoutUserName',
			title : $.i18n.prop('trip.report.label.checkoutUser')
		}, {
			field : 'checkoutTime',
			title : $.i18n.prop('trip.report.label.checkoutTime')
		}, {
			field : 'tripStatus',
			title :  $.i18n.prop('trip.report.label.tripStatus'),
			formatter : stateFormatter
		}, {
			field : 'tripId',
			title : $.i18n.prop('trip.report.list.operate'),
			formatter : linkFormatter
		}]
	});
}

function linkFormatter(value, row, index){
	var url = root + '/monitortripreport/toDetail.action?s_tripId=' + value;
	return '<a href="' + url + '" style="color: #00abff;">' + $.i18n.prop('trip.report.list.seeDetail') + '</a>';
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
	if(value == '0') {
		show = $.i18n.prop('trip.report.label.tripStatus.started');
	} else if (value == '1') {
		show = $.i18n.prop('trip.report.label.tripStatus.finished');
	} else {
		show = '-';
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
	
	//时间控件
	$("#form_checkinStartTime, #form_checkinEndTime, #form_checkoutStartTime, #form_checkoutEndTime").datetimepicker({
		format: "yyyy-mm-dd hh:ii",//ii:分钟
		autoclose: true,
		todayBtn: true,
		language: 'en'
	});
	
	$("#exportBtn").on("click", function(){
		alert("export");
	});
});