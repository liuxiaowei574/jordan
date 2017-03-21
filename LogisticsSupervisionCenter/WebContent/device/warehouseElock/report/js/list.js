		var selections = [];
		var $table = $('#table');
		// 刷新tale
		$(window).resize(function(){
			$table.bootstrapTable("resetView");
		});
		//查询
		function doSearch() {
			var params = $table.bootstrapTable('getOptions');
			params.queryParams = function(params) {
				// 遍历form 组装json
				$.each($("#ElockForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					// 可以添加提交验证
					params[field.name] = field.value;
				});
				return params;
			}
			$table.bootstrapTable('refresh', params);
		}

		
		//初始表格
		$(function() {
			// 设置传入参数
			function queryParams(params) {
				// 遍历form 组装json
				$.each($("#ElockForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					// 可以添加提交验证
					params += "&" + field.name + "=" + field.value;
				});
				return params;
			}
			$table.bootstrapTable({
					url:root+'/warehouseElock/report.action',
					clickToSelect : true,
					showRefresh : false,
					search : false,
					showColumns : false,
					showExport : false,
					striped : true,
//					height : "100%",
					method : "get",
					idfield: "elockNumber",
					sortName:"elockNumber",
					cache : false,
					sortable:true,
					pagination : true,
					sidePagination : 'server',
					pageNumber : 1,
					pageSize : 10,
					
					pageList : [ 10, 20, 30 ],
				columns: [{
			    	field: 'elockNumber',
			    	title: $.i18n.prop('WarehouseElock.elockNumber'),
					sortable:true,
			    },{
			    	field: 'simCard',
			    	title: $.i18n.prop('WarehouseElock.simCard'),
					sortable:true,
			    },{
			    	field: 'applcationPortName',// 组织机构表里面的"机构名称"-申请口岸名称
			    	title: $.i18n.prop('warehouseelock.jsp.report.applcationPortName'),
					sortable:true,
			    },{
			    	field: 'applyTime',
			    	title: $.i18n.prop('warehouseelock.jsp.report.apptime'),
					sortable:true,
			    },{
			    	field: 'recviceTime',
			    	title: $.i18n.prop('warehouseelock.jsp.report.rectime'),
					sortable:true,
			    },{
			    	field: 'organizationShort',//调度口岸
			    	title: $.i18n.prop('warehouseelock.jsp.report.frmport'),
					sortable:true,
			    }]
		});
		});

			
	//搜索表单重置
		function doRest(){
			$("#resetSearchBtn").click(function() {
				$("#ElockForm")[0].reset();
				function resetQuery() {
					$table.bootstrapTable('refresh', {});
				}
			});
			window.location.reload();
		}
	//操作列格式化
		function linkFormatter(value, row, index){
			var url = root + '/warehouseElock/detail.action?id=' + value ;
			return '<a href="' + url + '" style="color: #00abff;">' + $.i18n.prop('alarm.list.seeDetail') + '</a>';
		}
	
	
	
	