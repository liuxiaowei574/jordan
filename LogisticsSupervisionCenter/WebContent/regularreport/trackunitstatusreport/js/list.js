/*****************全局参数******************/
var $table = $("#statusReportTable");
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
 * 定时加载车载台状态列表
 */
function timedLoad() {
	$table.bootstrapTable({
		url:root + "/trackUnitStatusReport/list.action",
		height : $(window).height() - 300,//固定模态框 的 宽度 
		pagination : true,
		pageSize : 5,
		maintainSelected : true,
		columns : [ {
			checkbox : true
		}, {
			field : 'reportName',
			title : "报告名称",
			sortable:true
		}, {
			field : 'TRACKING_DEVICE_NUMBER',
			title :  "车载台编号",
			sortable:true
		}, {
			field : 'LOCATION_TIME',
			title : "生成报告时间",
			sortable:true
		},  {
			field : '',
			title : $.i18n.prop('notice.operate'),
			formatter : operateFormatter
		}]

	});
	$table.on('check.bs.table uncheck.bs.table '
					+ 'check-all.bs.table uncheck-all.bs.table', function() {
				// push or splice the selections if you want to save all data
				// selections
				$ids = getIdSelections();
				$status = getSelectedStatus();
			});
}

/**
 * 操作显示
 */
function operateFormatter(value, row, index) {
	// alert(row.id+""+index);
	return [
			'<a class="like" href="javascript:receiveUserList(\'' + row.GPS_ID
					+ '\')" title="Edit">',
			'<i class="glyphicon glyphicon-pencil"></i>',
			'</a>  ',
	].join('');
}

function receiveUserList(id) {
	var url = root + "/trackUnitStatusReport/statusDetail.action?gpsId="+id;
	$('#reportDetailModal').removeData('bs.modal');
	$('#reportDetailModal').modal({
		remote : url,
		show : false,
		backdrop: 'static', 
		keyboard: false
	});
	
	$('#reportDetailModal').on('loaded.bs.modal', function(e) {
		$('#reportDetailModal').modal('show');
	});
}


/**
 * 获取选中的ID
 */
function getIdSelections() {
	return $.map($table.bootstrapTable('getSelections'),
			function(row) {
				return row.noticeId;
			});
}

/**
 * 获取已选的通知状态
 * 
 * @returns
 */
function getSelectedStatus() {
	return $.map($table.bootstrapTable('getSelections'),
			function(row) {
				return row.noticeState;
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

/****************init********************/
$(function() {
	// 设置表格
	timedLoad();
	
	//定时向后台请求数据
	setInterval(function(){
		$table.bootstrapTable('refresh')
	}, 20000000)
	
	
	//添加Modal调用方法
	$("#addBtn").click(function() {
		var url = root + "/notice/addModal.action";
		$('#noticeAddModal').removeData('bs.modal');
		$('#noticeAddModal').modal({
			remote : url,
			show : false,
			backdrop: 'static', 
			keyboard: false
		});
		
		$('#noticeAddModal').on('loaded.bs.modal', function(e) {
			$('#noticeAddModal').modal('show');
		});
		//模态框登录判断
		$('#noticeAddModal').on('show.bs.modal', function(e) {
			var content = $(this).find(".modal-content").html();
			needLogin(content);
		});
	});
	
	$("#publishBtn").click(function() {
		if($ids.length == 0) {
			bootbox.alert($.i18n.prop("system.notice.publish.choose"));
		} else if($ids.length > 1){
			bootbox.alert($.i18n.prop("system.notice.publish.choose.only"));
		} else {
			if($status != '0') {
				bootbox.alert($.i18n.prop("system.notice.publish.status.error"));
			} else {
				bootbox.confirm($.i18n.prop("system.notice.publish.confirm"), function(result) {
					if(result) {
						var ajaxUrl = root+'/notice/publish.action?ids='+$ids;
						$.ajax({
							url : ajaxUrl, // 请求url
							type : "post", // 提交方式
							dataType : "json", // 数据类型
							success : function(data) { // 提交成功的回调函数
								if(!needLogin(data)) {
									if (data) {
										bootbox.success($.i18n.prop("system.notice.publish.success"));
										$table.bootstrapTable('refresh', {});
									} else {
										bootbox.error($.i18n.prop("system.notice.publish.error"));
										$table.bootstrapTable('refresh', {});
									}
								}
							}
						});
					}
				});
			}
		}
	});
	
	//编辑数据
	$("#editBtn").click(function() {
		if($ids.length == 0) {
			bootbox.alert($.i18n.prop("system.notice.edit.choose"));
		} else if($ids.length > 1){
			bootbox.alert($.i18n.prop("system.notice.edit.choose.only"));
		} else {
			if($status != '0') {
				bootbox.alert($.i18n.prop("system.notice.edit.status.error"));
			} else {
				var url = root + "/notice/editModal.action?notice.noticeId="+$ids;
				$('#noticeEditModal').removeData('bs.modal');
				$('#noticeEditModal').modal({
					remote : url,
					show : false,
					backdrop: 'static', 
					keyboard: false
				});
				$('#noticeEditModal').on('loaded.bs.modal', function(e) {
					$('#noticeEditModal').modal('show');
				});
				//模态框登录判断
				$('#noticeEditModal').on('show.bs.modal', function(e) {
					var content = $(this).find(".modal-content").html();
					needLogin(content);
				});
			}
		}
		
	});
	
	//删除数据
	$("#deleteBtn").click(function() {
		if($ids.length == 0) {
			bootbox.alert($.i18n.prop("system.notice.delete.choose"));
		} else {
			//alert($status);
			var flag = true;
			if($status.length > 0) {
				for(var i = 0; i < $status.length; i++) {
					//alert($status[i]);
					if($status[i] != '0') {
						flag = false;
						break;
					}
				}
			}
			if(!flag) {
				bootbox.alert($.i18n.prop("system.notice.delete.status.error"));
			} else {
				bootbox.confirm($.i18n.prop("system.notice.delete.confirm"), function(result) {
					if(result) {
						var ajaxUrl = root+'/notice/delete.action?ids='+$ids;
						$.ajax({
							url : ajaxUrl, // 请求url
							type : "post", // 提交方式
							dataType : "json", // 数据类型
							success : function(data) { // 提交成功的回调函数
								if(!needLogin(data)) {
									if (data) {
										bootbox.success($.i18n.prop("system.notice.delete.success"));
										$table.bootstrapTable('refresh', {});
									} else {
										bootbox.error($.i18n.prop("system.notice.delete.error"));
										$table.bootstrapTable('refresh', {});
									}
								}
							}
						});
					}
				});
			}
		}
	});
});