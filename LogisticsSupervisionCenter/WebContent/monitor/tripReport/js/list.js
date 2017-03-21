/*****************全局参数******************/
var $table = $("#tripReportListTable");
$(window).resize(function(){
	$table.bootstrapTable("resetView");
});
/*****************公用方法******************/
/**
 * 行程监管信息列表
 */
function searchTripList() {
	//调用search()的时候再进行查询，方便获取查询条件
//	var url = root + "/monitortripreport/list.action";
	$table.bootstrapTable({
		singleSelect: true,
		showRefresh : false,
		showColumns : false,
		striped : true,
		height : "100%",
//		url : url,
		method : "get",
		idfield: "tripId",
		sortName:"checkinTime",
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
			sortable:false
		}, {
			field : 'checkinTime',
			title : $.i18n.prop('trip.report.label.checkinTime'),
			sortable:true
		}, {
			field : 'checkoutUserName',
			title : $.i18n.prop('trip.report.label.checkoutUser'),
			sortable:false
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
			formatter : linkFormatter
		}]
	});
}

function linkFormatter(value, row, index){
	var detailUrl = root + '/monitortripreport/toDetail.action?s_tripId=' + value;
	var link = '<a href="' + detailUrl + '" >' + $.i18n.prop('trip.report.list.seeDetail') + '</a>';
	return link;
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
		show = $.i18n.prop('trip.report.label.tripStatus.toStart');
	} else if (value == '1') {
		show = $.i18n.prop('trip.report.label.tripStatus.started');
	} else if (value == '2') {
		show = $.i18n.prop('trip.report.label.tripStatus.toFinish');
	} else if (value == '3') {
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
	var url = root + "/monitortripreport/list.action";
	options.url = url;
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
//	function resetQuery() {
//		$table.bootstrapTable('refresh', {});
//	}
}
/****************init********************/
$(function() {
	// 设置表格
	searchTripList();
	search();
	
	//时间控件
	$("#form_checkinStartTime, #form_checkinEndTime, #form_checkoutStartTime, #form_checkoutEndTime").datetimepicker({
		format: "yyyy-mm-dd hh:ii",//ii:分钟
		autoclose: true,
		todayBtn: true,
		language: 'en'
	}).on('changeDate', function(ev){
	    var checkinStartTime = $('#s_checkinStartTime').val();//获得检入开始时间
	    $('#form_checkinEndTime').datetimepicker('setStartDate', checkinStartTime);//设置检入结束时间（大于登入开始时间）
	    $('#form_checkoutStartTime').datetimepicker('setStartDate', checkinStartTime);//检出的开始时间大于检入的开始时间
	    var checkoutStartTime = $('#s_checkoutStartTime').val();//获得登出开始时间
	    $('#form_checkoutEndTime').datetimepicker('setStartDate', checkoutStartTime);//设置检出结束时间（大于检出开始时间）
	});
	
	$("#exportBtn").on("click", function(){
		//alert("export");
	});
});

//导出excel文件
function exportExcel(){
	var url = root + "/monitortripreport/exportExcel.action";
	window.location.href=url;
}