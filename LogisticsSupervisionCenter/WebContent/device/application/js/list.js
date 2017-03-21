/*****************全局参数******************/
var $table = $("#warehouseDeviceApplicationTable");
var $ids=[];
var $status = [];
//刷新tale
$(window).resize(function(){
	$table.bootstrapTable("resetView");
});
/*****************公用方法******************/
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
function searchDeviceApplicationList() {
	var url = root + "/warehouseDeviceApplication/list.action";
	$table.bootstrapTable({
		clickToSelect : true,
		showRefresh : false,
		search : false,
		showColumns : false,
		showExport : false,
		striped : true,
		//height : "100%",
		url : url,
		method : "get",
		sortName:"applicationId",
		cache : false,
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 10,
		sortable:true,
		pageList : [ 10, 20, 30 ],
		columns : [ {
			checkbox : true
		}, {
			field : 'applcationPortName',
			title : $.i18n.prop('warehouse.device.application.portName'),
			sortable:true
		}, {
			field : 'deviceNumber',
			title :  $.i18n.prop('warehouse.device.application.trackDevice'),
			sortable:true
		}, {
			field : 'esealNumber',
			title : $.i18n.prop('warehouse.device.application.eseal'),
			sortable:true
			//formatter : stateFormatter
		}, {
			field : 'sensorNumber',
			title : $.i18n.prop('warehouse.device.application.sensor'),
			sortable:true
		},{
			field : 'applyStatus',
			title : $.i18n.prop('warehouse.device.application.status'),
			formatter : statusFormatter,
			sortable:true
		},{
			field : 'userName',
			title : $.i18n.prop('warehouse.dispatch.application.user'),
			sortable:true
		},{
			field : 'applyTime',
			title : $.i18n.prop('dispatch.applicate.time'),
			sortable:true
		},{
			field : '',
			title : $.i18n.prop('warehouse.device.application.operate'),
			formatter : operateFormatter
		}]		
	});
	function statusFormatter(value, row, index) {
		var show;
		if(value == '1') {
			show = $.i18n.prop('dispatch.applied');
		} else if (value == '2') {
			show = $.i18n.prop('dispatch.processed');
		}else if (value == '3') {
			show = $.i18n.prop('dispatch.completed');
		}  else {
			show = '--';
		}
		return [show].join('');
	}
	$table.on('check.bs.table uncheck.bs.table '
					+ 'check-all.bs.table uncheck-all.bs.table', function() {
				// push or splice the selections if you want to save all data
				// selections
				$ids = getIdSelections();
			});
}

/**
 * 操作显示
 */
function operateFormatter(value, row, index) {
	debugger;
	var roleName = $("#roleName").val();
	if(roleName=="portUser"){
		return "---";
	}
	//申请"已完成"的可以显示调度的详细信息
	if(row.applyStatus=="3"){
		return [
				'<a class="like" name="search" id="search"style="display" href="javascript:detailDispatch(\'' + row.applicationId
						+ '\')">',
				'<i class="glyphicon glyphicon-search"></i>',
				'</a>  ',
		].join('');
	}
	//申请状态为"已申请"
	if(row.applyStatus=="1"){
		return [
				'<a class="like" name="pencil" id="pencil"style="display" href="javascript:dispatchAnalysis(\'' + row.applicationId
						+ '\')">',
				'<i class="glyphicon glyphicon-pencil"></i>',
				'</a>  ',
		].join('');
	}
}


/**
 * 获取选中的ID
 */
function getIdSelections() {
	return $.map($table.bootstrapTable('getSelections'),
			function(row) {
				return row.applicationId;
			});
}

/**
 * 条件查询方法
 */
function search(){
	debugger;
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
	$table.bootstrapTable('refresh', params);
}

/**
 * 通知接收人列表
 */
function dispatchAnalysis(id) {
	var url = root + "/warehouseDispatchAnalysis/index.action?applicationId="+id;
	window.location.href=url;
}

/**
 * 查看申请状态为"已完成"的详细调度信息
 */
function detailDispatch(applicationId){
	var url = root +"/warehouseDeviceApplication/detailDispatch.action?applicationId="+applicationId;
	window.location.href=url;
}

/**
 * 
 */
function refesh() {
	$table.bootstrapTable('refresh', params);
}

/****************init********************/
$(function() {
	// 设置表格
	searchDeviceApplicationList();
	
	/**
	 * 添加对话框
	 */
	$("#addWarehouseDeviceApplicationButton").on("click", function() {
		var url = root + "/warehouseDeviceApplication/addModal.action";
		$('#warehouseDeviceApplicationAddModal').removeData('bs.modal');
		$('#warehouseDeviceApplicationAddModal').modal({
			remote : url,
			show : false,
			backdrop: 'static', 
			keyboard: false
		});
		
		$('#warehouseDeviceApplicationAddModal').on('loaded.bs.modal', function(e) {
			$('#warehouseDeviceApplicationAddModal').modal('show');
		});
		//模态框登录判断
		$('#warehouseDeviceApplicationAddModal').on('show.bs.modal', function(e) {
			var content = $(this).find(".modal-content").html();
			needLogin(content);
		});
	});
	
	//判断登陆角色,赋予不同的权限
	judgeRole();
});

function judgeRole(){
	var roleName = $("#roleName").val();
	//如果是qualityCenter的用户不能具有调度申请的功能
	if(roleName!="portUser"){
		$('#addWarehouseDeviceApplicationButton').attr('disabled',"true").off();
	}
}

//时间控件
$(function() {
	$("#applicationStartTime, #applicationEndTime").datetimepicker({
		format: "yyyy-mm-dd hh:ii",//ii:分钟
		autoclose: true,
		todayBtn: true,
		language: 'en'
	}).on('changeDate', function(ev){
	    var startTime = $('#s_applyStartTime').val();//获得开始时间
	    $('#applicationEndTime').datetimepicker('setStartDate', startTime);//设置结束时间（大于开始时间）
	});
});
//清除表单内容
function rest(){
	$("#searchForm")[0].reset();
}
