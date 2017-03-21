var $table = $("#userLogListTable");
var $ids=[];

/**
 * 列表
 */
function searchList() {
	var url = root + "/userLog/list.action";
	$table.bootstrapTable({
		clickToSelect : false,
		showRefresh : false,
		search : false,
		showColumns : false,
		showExport : false,
		striped : true,
		height : "100%",
		url : url,
		method : "get",
		idfield: "userLogId",
		sortable:true,
		sortName:"logonTime",
		sortOrder: "desc",
		cache : false,
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20, 30 ],
		columns : [ {
			field : 'userName',
			title : $.i18n.prop('system.user.log.logUser'),
	    	sortable:true
		}, {
			field : 'ipAddress',
			title :  $.i18n.prop('system.user.log.ipAddress'),
	    	sortable:true
		}, {
			field : 'logonTime',
			title : $.i18n.prop('system.user.log.logonTime'),
	    	sortable:true
		}, {
			field : 'logoutTime',
			title : $.i18n.prop('system.user.log.logoutTime'),
	    	sortable:true
		}, {
			field : 'logonSystem',
			title : $.i18n.prop('system.user.log.logonSystem'),
			formatter: systemFormatter,
			sortable:true
		}, {
			field : 'logoutType',
			title : $.i18n.prop('system.user.log.logoutType'),
			formatter: typeFormatter,
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
/**
 * 登录系统类型
 * 
 * @param value
 * @param row
 * @param index
 */
function systemFormatter(value, row, index) {
	var show;
	if (!!value) {
		show = $.i18n.prop('LoginSystem.' + value);
	} else {
		show = '--';
	}
	return [show].join('');
}
/**
 * 登出类型
 * @param value
 * @param row
 * @param index
 * @returns
 */
function typeFormatter(value, row, index) {
	var show;
	if (value == '0') {
		show = $.i18n.prop('LogoutType.NORMAL');
	} else if(value == '1') {
		show = $.i18n.prop('LogoutType.KICKOUT');
	} else if(value == '2') {
		show = $.i18n.prop('LogoutType.SESSION_TIMEOUT');
	} else {
		show = $.i18n.prop("system.user.log.notLogout");
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
        console.log(params);
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
	searchList();
	
	//时间控件
	$("#form_logonStartTime, #form_logonEndTime, #form_logoutStartTime, #form_logoutEndTime").datetimepicker({
		format: "yyyy-mm-dd hh:ii",//ii:分钟
		autoclose: true,
		todayBtn: true,
		language: 'en'
	}).on('changeDate', function(ev){
	    var logonStartTime = $('#s_logonStartTime').val();//获得登录开始时间
	    $('#form_logonEndTime').datetimepicker('setStartDate', logonStartTime);//设置登入结束时间（大于登入开始时间）
	    $('#form_logoutStartTime').datetimepicker('setStartDate', logonStartTime);//登出的开始时间大于登入的开始时间
	    var logoutStartTime = $('#s_logoutStartTime').val();//获得登出开始时间
	    $('#form_logoutEndTime').datetimepicker('setStartDate', logoutStartTime);//设置登出结束时间（大于登出开始时间）
	    
	});
});