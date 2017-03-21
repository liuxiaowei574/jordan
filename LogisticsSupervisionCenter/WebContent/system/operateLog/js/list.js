var $table = $("#operateLogListTable");
var $ids=[];
//刷新tale
$(window).resize(function(){
	$table.bootstrapTable("resetView");
});
/**
 * 列表
 */
function searchOperateLogList() {
	var url = root + "/operateLog/list.action";
	$table.bootstrapTable({
		clickToSelect : false,
		showRefresh : false,
		search : false,
		showColumns : false,
		showExport : false,
		striped : true,
		height : "100%",
    	sortable:true,
		url : url,
		method : "get",
		idfield: "operateId",
		sortName:"operateTime",
		sortOrder: "desc",
		cache : false,
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20, 30 ],
		columns : [ {
			field : 'userName',
			title : $.i18n.prop('system.operate.log.logUserName'),
	    	sortable:true
		},{
			field : 'ipAddress',
			title :  $.i18n.prop('system.operate.log.ipAddress'),
	    	sortable:true
		}, {
			field : 'operateDesc',
			title : $.i18n.prop('system.operate.log.operateDesc'),
			formatter: descFormatter,
	    	sortable:true
		},{
			field : 'operateType',
			title : $.i18n.prop('system.operate.log.operateType'),
			formatter: typeFormatter,
	    	sortable:true
		},{
			field : 'operateTime',
			title : $.i18n.prop('system.operate.log.operateTime'),
	    	sortable:true
		}]

	});
	$table.on('check.bs.table uncheck.bs.table '
					+ 'check-all.bs.table uncheck-all.bs.table', function() {
				// push or splice the selections if you want to save all data
				// selections
				$ids = getIdSelections();
			});
}

//操作内容国际化
function descFormatter(value, row, index) {
	var show;
	 if (!!value) {
		show = $.i18n.prop('OperateContentType.' + value);
	} else {
		show = '--';
	}
	return [show].join('');
}

//操作实体对象国际化
function typeFormatter(value, row, index) {
	var show;
	 if (!!value) {
		show = $.i18n.prop('OperateEntityType.' + value);
	} else {
		show = '--';
	}
	return [show].join('');
}

/**
 * 获取选中的ID
 */
function getIdSelections() {
	return $.map($table.bootstrapTable('getSelections'),
			function(row) {
				return row.userLogId;
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
            //console.info(field.name + ":" + field.value + " ");  
            //可以添加提交验证                   
            params[field.name] = field.value;  
        });  
        return params;  
    }
	$table.bootstrapTable('refresh', {});
}

/**
 * 
 */
function refesh() {
	$table.bootstrapTable('refresh', params);
}

$(function() {
	searchOperateLogList();
	
	//时间控件
	$("#form_operateStartTime, #form_operateEndTime").datetimepicker({
		format: "yyyy-mm-dd hh:ii",//ii:分钟
		autoclose: true,
		todayBtn: true,
		language: 'en'
	}).on('changeDate', function(ev){
	    var startTime = $('#s_operateStartTime').val();//获得开始时间
	    $('#form_operateEndTime').datetimepicker('setStartDate', startTime);//设置结束时间（大于开始时间）
	});	
});