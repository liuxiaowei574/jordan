var $table = $("#onlineUserListTable");
var $ids=[];
//刷新tale
$(window).resize(function(){
	$table.bootstrapTable("resetView");
});
/**
 * 在线人员列表
 */
function searchOnlineUserList() {
	var url = root + "/onlineUser/list.action";
	$table.bootstrapTable({
		clickToSelect : true,
		showRefresh : false,
		search : false,
		showColumns : false,
		showExport : false,
		striped : true,
		height : "100%",
		url : url,
		method : "get",
		idfield: "userId",
		sortName:"userId",
		cache : false,
		pagination : true,
		sidePagination : 'server',
		sortable:true,
		sortName:"logonTime",
		sortOrder: "desc",
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20, 30 ],
		columns : [ {
			field : 'userName',
			title : $.i18n.prop('system.online.logUserName'),
			sortable:true
		}, {
			field : 'ipAddress',
			title :  $.i18n.prop('system.online.ipAddress'),
			sortable:true
		}, {
			field : 'logonTime',
			title : $.i18n.prop('system.online.time'),
			sortable:true
		}, {
			field : '',
			title : $.i18n.prop('system.online.operateLog.title'),
			formatter : operateFormatter
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
				return row.userLogId;
			});
}

/**
 * 操作显示
 */
function operateFormatter(value, row, index) {
	// alert(row.id+""+index);
	return [
			'<a class="like" href="javascript:userOperateLog(\'' + row.userId
					+ '\')" title="Edit">',
			'<i class="glyphicon glyphicon-pencil"></i>',
			'</a>  ',
	].join('');
}

/**
 * 修改参数
 * @param id
 */
function userOperateLog(id) {
	var url = root + "/onlineUser/toOnlineUserDetailModal.action?userId="+ id;
	$('#onlineUserDetailListModal').removeData('bs.modal');
	$('#onlineUserDetailListModal').modal({
		remote : url,
		show : false,
		backdrop: 'static', 
		keyboard: false
	});
	
	$('#onlineUserDetailListModal').on('loaded.bs.modal', function(e) {
		$('#onlineUserDetailListModal').modal('show');
	});
	//模态框登录判断
	$('#onlineUserDetailListModal').on('show.bs.modal', function(e) {
		var content = $(this).find(".modal-content").html();
		needLogin(content);
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
            console.info(field.name + ":" + field.value + " ");  
            //可以添加提交验证                   
            params[field.name] = field.value;  
        });  
        return params;  
    }
	$table.bootstrapTable('refresh');
}

/**
 * 
 */
function refesh() {
	$table.bootstrapTable('refresh', params);
}

$(function() {
	searchOnlineUserList();
});