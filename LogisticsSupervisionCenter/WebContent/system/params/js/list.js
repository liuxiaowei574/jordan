var $table = $("#paramsListTable");
var $ids=[];
//刷新tale
$(window).resize(function(){
	$table.bootstrapTable("resetView");
});
/**
 * 通知列表
 */
function searchParamList() {
	var url = root + "/paramsMgmt/list.action";
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
		idfield: "paramId",
		sortName:"paramId",
		cache : false,
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 10,
		sortable:true,
		pageList : [ 10, 20, 30 ],
		columns : [ {
			field : 'paramName',
			title : $.i18n.prop('system.params.paramName'),
			sortable:true
		}, {
			field : 'paramCode',
			title :  $.i18n.prop('system.params.paramCode'),
			sortable:true
		}, {
			field : 'paramValue',
			title : $.i18n.prop('system.params.value'),
			sortable:true
		}, {
			field : '',
			title : $.i18n.prop('system.params.operate'),
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
				return row.paramId;
			});
}

/**
 * 操作显示
 */
function operateFormatter(value, row, index) {
	// alert(row.id+""+index);
	return [
			'<a class="like" href="javascript:modifyParam(\'' + row.paramId
					+ '\')" title="Edit">',
			'<i class="glyphicon glyphicon-pencil"></i>',
			'</a>  ',
	].join('');
}

/**
 * 修改参数
 * @param id
 */
function modifyParam(id) {
	var url = root + "/paramsMgmt/toParamEditModal.action?systemParams.paramId="+id;
	$('#paramEditModal').removeData('bs.modal');
	$('#paramEditModal').modal({
		remote : url,
		show : false,
		backdrop: 'static', 
		keyboard: false
	});
	
	$('#paramEditModal').on('loaded.bs.modal', function(e) {
		$('#paramEditModal').modal('show');
	});
	//模态框登录判断
	$('#paramEditModal').on('show.bs.modal', function(e) {
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
	searchParamList();
});