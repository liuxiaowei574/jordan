/*****************全局参数******************/
var $table = $("#alarmListTable");
/*****************公用方法******************/
/**
 * 报警列表
 */
function searchTripList() {
	var url = root + "/monitoralarm/list.action";
	$table.bootstrapTable({
		singleSelect: true,
		showRefresh : false,
		search : false,
		showColumns : false,
		showExport : false,
		striped : true,
		height : "100%",
		url : url,
		method : "get",
		idfield: "alarmId",
		sortName:"alarmTime",
		sortOrder: 'desc',
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
			field : 'trackingDeviceNumber',
			title : $.i18n.prop('alarm.label.trackingDeviceNumber')
		}, {
			field : 'esealNumber',
			title :  $.i18n.prop('alarm.label.esealNumber')
		}, {
			field : 'sensorNumber',
			title :  $.i18n.prop('alarm.label.sensorNumber')
		}, {
			field : 'vehiclePlateNumber',
			title :  $.i18n.prop('alarm.label.vehiclePlateNumber')
		}, {
			field : 'driverName',
			title :  $.i18n.prop('alarm.label.driverName')
		}, {
			field : 'alarmTime',
			title : $.i18n.prop('alarm.label.alarmTime')
		}, {
			field : 'userName',
			title : $.i18n.prop('alarm.label.userName')
		}, {
			field : 'alarmStatus',
			title : $.i18n.prop('alarm.label.alarmStatus'),
			formatter : stateFormatter
		}, {
			field : 'alarmLevelName',
			title : $.i18n.prop('alarm.label.alarmLevelName')
		}, {
			field : 'alarmTypeName',
			title : $.i18n.prop('alarm.label.alarmTypeName')
		}, {
			field : 'alarmId',
			title : $.i18n.prop('alarm.list.operate'),
			formatter : linkFormatter
		}, {
			field : 'tripId',
			class: 'hidden'
		}]
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
		show = $.i18n.prop('alarm.label.alarmStatus.notProcessed');
	} else if(value == '1') {
		show = $.i18n.prop('alarm.label.alarmStatus.processing');
	} else if(value == '2') {
		show = $.i18n.prop('alarm.label.alarmStatus.processed');
	} else {
		show = '--';
	}
	return [show].join('');
}

function linkFormatter(value, row, index){
	var url = root + '/monitoralarm/toDetail.action?s_alarmId=' + value;
	return '<a href="' + url + '" style="color: #00abff;">' + $.i18n.prop('alarm.list.seeDetail') + '</a>';
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
	$("#form_alarmStartTime, #form_alarmEndTime").datetimepicker({
		format: "yyyy-mm-dd hh:ii",//ii:分钟
		autoclose: true,
		todayBtn: true,
		language: 'en'
	});
	
	$("#alarmListTable").on("click", "a", function(){
		//加上后面隐藏列的tripId
		this.href += '&s_tripId=' + $(this).closest("td").next("td").text();
	});
});