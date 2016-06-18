/*********************全局变量*********************/
var $table = $("#receiveUsersTable");
var _id = $("#receiveNoticeId").val();

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
function searchReceiveUsersList() {
	var url = root + "/notice/findReceiveUserList.action?notice.noticeId="+_id;
	$table.bootstrapTable({
		clickToSelect : true,
		showRefresh : false,
		search : true,
		showColumns : true,
		showExport : false,
		striped : true,
		height : "100%",
		url : url,
		method : "get",
		idfield: "noticeRevId",
		sortName:"noticeRevId",
		cache : false,
		queryParams : queryParams,
		queryParamsType : "not-limit",
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20, 30 ],
		columns : [ {
			checkbox : true
		}, {
			field : 'receiveUserName',
			title : $.i18n.prop('notice.log.receiveUser')
		}, {
			field : 'dealType',
			title : $.i18n.prop('notice.log.status'),
			formatter : typeFormatter
		}, {
			field : 'receiveTime',
			title : $.i18n.prop('notice.log.receiveTime')
		}]

	});
}

/**
 * 接收类型消息
 * 
 * @param value
 * @param row
 * @param index
 * @returns
 */
function typeFormatter(value, row, index) {
	var show;
	if(value == '0') {
		show = $.i18n.prop('notice.log.status.unread');
	} else if (value == '1') {
		show = $.i18n.prop('notice.log.status.read');
	} else {
		show = '--';
	}
	return [show].join('');
}

$(function() {
	searchReceiveUsersList();
});