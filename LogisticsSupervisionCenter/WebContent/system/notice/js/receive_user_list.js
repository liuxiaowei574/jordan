/*********************全局变量*********************/
var $receiveUsersTable = $("#receiveUsersTable");
var _id = $("#receiveNoticeId").val();

//设置传入参数
function queryParams(params) {
	//遍历form 组装json  
   $.each($("#searchForm1").serializeArray(), function(i, field) {  
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
	var url = root + "/notice/findReceiveUserList.action?s_noticeId="+_id;
	$receiveUsersTable.bootstrapTable({
		clickToSelect : true,
		showRefresh : false,
		search : false,
		showColumns : true,
		showExport : false,
		striped : true,
		height : "100%",
		url : url,
		method : "get",
		idfield: "noticeRevId",
		sortName:"receiveTime",
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
			checkbox : true
		}, {
			field : 'userName',
			title : $.i18n.prop('notice.log.receiveUser'),
			sortable:true
		}, {
			field : 'dealType',
			title : $.i18n.prop('notice.log.status'),
			formatter : typeFormatter,
			sortable:true
		}, {
			field : 'receiveTime',
			title : $.i18n.prop('notice.log.receiveTime'),
			sortable:true
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