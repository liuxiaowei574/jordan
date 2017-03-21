/*****************全局参数******************/
var $table = $("#parameterTable");
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
function searchParameterList() {
	var url = root + "/regularReportParameter/list.action";
	$table.bootstrapTable({
		clickToSelect : true,
		showRefresh : false,
		search : false,
		showColumns : false,
		showExport : false,
		striped : true,
		url : url,
		method : "get",
		idfield: "reportId",
		sortName:"customTime",
		cache : false,
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		sortable:true,
		pageSize : 10,
		pageList : [ 10, 20, 30 ],
		columns : [ {
			checkbox : true
		}, {
			field : 'reportName',
			title : "报告名称",
			sortable:true
		}, {
			field : 'reportType',
			title :  "报告类型",
			formatter : reportTypeFormat,
			sortable:true
		}, /*{
			field : 'cycle',
			title : "周期",
			sortable:true
		},*/ {
			field : 'customTime',
			title : "定制时间",
			sortable:true
		}, {
			field : 'isEnable',
			title : "是否可用",
			sortable:true 
		}]

	});
	$table.on('check.bs.table uncheck.bs.table '
					+ 'check-all.bs.table uncheck-all.bs.table', function() {
				$ids = getIdSelections();
				$status = getSelectedStatus();
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


function getIdSelections() {
	return $.map($table.bootstrapTable('getSelections'),
			function(row) {
				return row.reportId;
			});
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
	searchParameterList();
	
	//添加Modal调用方法
	$("#addBtn").click(function() {
		var url = root + "/regularReportParameter/addModal.action";
		$('#parametersAddModal').removeData('bs.modal');
		$('#parametersAddModal').modal({
			remote : url,
			show : false,
			backdrop: 'static', 
			keyboard: false
		});
		
		$('#parametersAddModal').on('loaded.bs.modal', function(e) {
			$('#parametersAddModal').modal('show');
		});
		//模态框登录判断
		$('#parametersAddModal').on('show.bs.modal', function(e) {
			var content = $(this).find(".modal-content").html();
			needLogin(content);
		});
	});
	
	
	//编辑数据
	$("#editBtn").click(function() {
		if($ids.length == 0) {
			bootbox.alert("请选择要修改的报告参数");
		} else if($ids.length > 1){
			bootbox.alert("只能选择一条记录修改");
		} else {
				var url = root + "/regularReportParameter/editModal.action?regularReportParaSetBO.reportId="+$ids;
				$('#parametersEditModal').removeData('bs.modal');
				$('#parametersEditModal').modal({
					remote : url,
					show : false,
					backdrop: 'static', 
					keyboard: false
				});
				$('#parametersEditModal').on('loaded.bs.modal', function(e) {
					$('#parametersEditModal').modal('show');
				});
				//模态框登录判断
				$('#parametersEditModal').on('show.bs.modal', function(e) {
					var content = $(this).find(".modal-content").html();
					needLogin(content);
				});
		}
		
	});
	
	
	//删除数据
	$("#deleteBtn").click(function() {
		if($ids.length == 0) {
			bootbox.alert("请选择要删除的报告参数设置");
			}else{
				bootbox.confirm("确定要删除吗？", function(result) {
					if(result) {
						var ajaxUrl = root+'/regularReportParameter/delete.action?ids='+$ids;
						$.ajax({
							url : ajaxUrl, // 请求url
							type : "post", // 提交方式
							dataType : "json", // 数据类型
							success : function(data) { // 提交成功的回调函数
								if(!needLogin(data)) {
									if (data) {
										bootbox.success("删除成功");
										$table.bootstrapTable('refresh', {});
									} else {
										bootbox.error("删除失败");
										$table.bootstrapTable('refresh', {});
									}
								}
							}
						});
					}
				});
			}
	});
});


function reportTypeFormat(value, row, index){
	var show;
	if(value =='0'){
		show = $.i18n.prop('ReportType.TrackUnit');
	}else if(value =='1'){
		show =$.i18n.prop('ReportType.Elock');
	}else if(value =='2'){
		show = $.i18n.prop('ReportType.Eseal');
	}else if(value =='3'){
		show = $.i18n.prop('ReportType.Sensor');
	}else{
		show = '--'
	}
	return [show].join('');
}