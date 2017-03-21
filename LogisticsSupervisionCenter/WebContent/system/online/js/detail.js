/*********************全局变量*********************/
var $table = $("#userLogDetailTable");
var _id = $("#userId").val();

$(function() {
	searchOnlineUserOperateLogList();
});

//设置传入参数
function queryParams(params) {
	//遍历form 组装json  
   $.each($("#searchForm").serializeArray(), function(i, field) {  
        console.info(field.name + ":" + field.value + " ");  
        //可以添加提交验证                   
        params += "&" + field.name +"="+ field.value;  
    }); 
    return params;
};

/**
 * 操作记录列表
 */
function searchOnlineUserOperateLogList() {
	var url = root + "/onlineUser/findUserOperateList.action?userId="+_id;
	$table.bootstrapTable({
		clickToSelect : true,
		showRefresh : false,
		search : false,
		showColumns : false,
		showExport : false,
		striped : true,
		height : "100%",
		url : url,
		//idfield: "operateId",
		//sortName:"operateId",
		cache : false,
		//queryParamsType : "not-limit",
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20, 30 ],
		columns : [{
			field : 'userName',
			title : $.i18n.prop('system.operate.log.logUserName')
		}, {
			field : 'operateDesc',
			title : $.i18n.prop('system.operate.log.operateDesc'),
			formatter: descFormatter,
		},{
			field : 'operateType',
			title : $.i18n.prop('system.operate.log.operateType'),
			formatter: typeFormatter,
		},{
			field : 'operateTime',
			title : $.i18n.prop('system.operate.log.operateTime')
		}]
	});
};

//操作内容国际化
function descFormatter(value, row, index) {
	var show;
	 if (!!value) {
		show = $.i18n.prop('OperateContentType.' + value);
	} else {
		show = '--';
	}
	return [show].join('');
};

//操作实体对象国际化
function typeFormatter(value, row, index) {
	var show;
	 if (!!value) {
		show = $.i18n.prop('OperateEntityType.' + value);
	} else {
		show = '--';
	}
	return [show].join('');
};