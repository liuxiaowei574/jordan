/*****************全局参数******************/
var $table = $("#alarmListTable");
$(window).resize(function(){
	$table.bootstrapTable("resetView");
});
/*****************公用方法******************/
/**
 * 报警列表
 */
function searchTripList() {
	//调用search()的时候再进行查询，方便获取查询条件
//	var url = root + "/monitoralarm/list.action";
	$table.bootstrapTable({
		toolbar:'#toolbar',
		singleSelect: true,
		showRefresh : false,
		search : false,
		showColumns : false,
		showExport : true,
		striped : true,
		height : "100%",
//		url : url,
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
		exportDataType:"basic",
		columns : [ {
			field : 'declarationNumber',
			title : $.i18n.prop('alarm.label.declarationNumber'),
			sortable:true
		}, {
			field : 'trackingDeviceNumber',
			title : $.i18n.prop('alarm.label.trackingDeviceNumber'),
			sortable:true
		}, {
			field : 'esealNumber',
			title :  $.i18n.prop('alarm.label.esealNumber'),
			sortable:true
		}, {
			field : 'sensorNumber',
			title :  $.i18n.prop('alarm.label.sensorNumber'),
			sortable:true
		}, {
			field : 'vehiclePlateNumber',
			title :  $.i18n.prop('alarm.label.vehiclePlateNumber'),
			sortable:true
		}, {
			field : 'alarmTime',
			title : $.i18n.prop('alarm.label.alarmTime'),
			sortable:true
		}, {
			field : 'userName',
			title : $.i18n.prop('alarm.label.userName'),
			sortable:true
		}, {
			field : 'isManual',
			title : $.i18n.prop('alarm.label.isManual'),
			formatter : manualFormatter,
			sortable:true
		}, {
			field : 'createUserName',
			title : $.i18n.prop('dispatch.patrol.createUser'),
			sortable:true
		}, {
			field : 'alarmStatus',
			title : $.i18n.prop('alarm.label.alarmStatus'),
			formatter : stateFormatter,
			sortable:true
		}, {
			field : 'alarmLevelCode',
			title : $.i18n.prop('alarm.label.alarmLevelName'),
			formatter : levelFormatter,
			sortable:true
		}, {
			field : 'alarmTypeCode',
			title : $.i18n.prop('alarm.label.alarmTypeName'),
			formatter : typeFormatter,
			sortable:true
		}, {
			field : 'alarmId',
			title : $.i18n.prop('alarm.list.operate'),
			formatter : linkFormatter
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

/**
 * 报警等级显示
 * 
 * @param value
 * @param row
 * @param index
 */
function levelFormatter(value, row, index) {
	var show;
	 if (value == '0') {
		show = $.i18n.prop('AlarmLevel.Light');
	} else if(value == '1') {
		show = $.i18n.prop('AlarmLevel.Serious');
	} else {
		show = '--';
	}
	return [show].join('');
}

/**
 * 报警类型显示
 * 
 * @param value
 * @param row
 * @param index
 */
function typeFormatter(value, row, index) {
	var show;
	 if (!!value) {
		show = $.i18n.prop('AlarmType.' + value);
	} else {
		show = '--';
	}
	return [show].join('');
}
/**
 * 创建类型，手动/自动
 * 
 * @param value
 * @param row
 * @param index
 */
function manualFormatter(value, row, index) {
	var show;
	if (value == '0') {
		show = $.i18n.prop('alarm.label.auto');
	} else if (value == '1') {
		show = $.i18n.prop('alarm.label.manual');
	} else {
		show = '--';
	}
	return [show].join('');
}

function linkFormatter(value, row, index){
	var url = root + '/monitoralarm/toDetail.action?s_alarmId=' + value + '&s_tripId=' + row.tripId;
	return '<a href="' + url + '" style="color: #00abff;">' + $.i18n.prop('alarm.list.seeDetail') + '</a>';
}

/**
 * 条件查询方法
 */
function search(){
	var options = $table.bootstrapTable('getOptions');
	var url = root + "/monitoralarm/list.action";
	options.url = url;
	options.queryParams = function(params) {
        //遍历form 组装json  
        $.each($("#searchForm").serializeArray(), function(i, field) {  
            //console.info(field.name + ":" + field.value + " ");  
            //可以添加提交验证                   
            params[field.name] = field.value;  
            console.log( params);
        });  
       
        return params;  
    };
	$table.bootstrapTable('refresh', options);
}
function doRest(){
	$("#searchForm")[0].reset();
	var zTree = $.fn.zTree.getZTreeObj("userTree");
	zTree.checkAllNodes(false);//清空负责人树选中的复选框项
	$("#s_userId").val("");//清空负责人id
}
/****************init********************/
$(function() {
	// 设置表格
	searchTripList();
	search();
	
	//时间控件
	$("#form_alarmStartTime, #form_alarmEndTime").datetimepicker({
		format: "yyyy-mm-dd hh:ii",//ii:分钟
		autoclose: true,
		todayBtn: true,
		language: 'en'
	}).on('changeDate', function(ev){
	    var startTime = $('#s_alarmStartTime').val();//获得开始时间
	    $('#form_alarmEndTime').datetimepicker('setStartDate', startTime);//设置结束时间（大于开始时间）
	});
	//未开启区域模块时，不需要相关报警
	if(!systemModules.isAreaOn) {
		$("#s_alarmType").children('[value=TARGET_ZOON],[value=ENTER_DANGEROUS_AREA]').remove();
	}
	//未开启车载台模块时，不需要相关报警
	if(!systemModules.isPatrolOn) {
		$("#s_alarmType").children('[value=TRACK_UNIT_ALARM]').remove();
	}
});
