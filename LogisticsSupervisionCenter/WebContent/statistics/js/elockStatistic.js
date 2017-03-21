
function elockStatusFormat(value, row, index){
		var show;
		if(value =='0'){
			show = $.i18n.prop('DeviceStatus.Scrap');
		}else if(value =='1'){
			show =$.i18n.prop('DeviceStatus.Normal');
		}else if(value =='2'){
			show = $.i18n.prop('DeviceStatus.Inway');
		}else if(value =='3'){
			show = $.i18n.prop('DeviceStatus.Destory');
		}else if(value =='4'){
			show = $.i18n.prop('DeviceStatus.Maintain');
		}else{
			show = '--'
		}
		return [show].join('');
	}

//显示关锁详细库存信息
function elockDetail(portName,seriesName){
	var params = $('#detailTable').bootstrapTable('getOptions');
	params.url =root+"/statisitc/elockdetails.action?portName="+portName+"&seriesName="+seriesName;
	$('#detailTable').bootstrapTable({
		//url:"${root}/statisitc/elockdetails.action?portName="+portName,
		clickToSelect : true,
		showRefresh : false,
		search : false,
		showColumns : false,
		showExport : false,
		striped : true,
		method : "get",
		idfield: "elockId",
		sortName:"elockNumber",
		cache : false,
		sortable:true,
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 5,
		pageList : [ 5, 10 ],
		columns : [ {    
				field: 'elockNumber',
		    	title: $.i18n.prop('WarehouseElock.elockNumber'),
		    },	{
		    	field: 'elockStatus',
		    	title: $.i18n.prop('WarehouseElock.elockStatus'),
		    	formatter : elockStatusFormat,
		    }],
	}); 
	$('#detailTable').bootstrapTable('refresh', params);
}

//显示子锁详细库存信息
function esealDetail(portName,seriesName){
	var esealparams = $('#esealDetailTable').bootstrapTable('getOptions');
	esealparams.url =root+"/statisitc/esealdetails.action?portName="+portName+"&seriesName="+seriesName;
	$('#esealDetailTable').bootstrapTable({
		//url : '${root}/statisitc/esealdetails.action',
		//url :"${root}/statisitc/esealdetails.action?portName="+portName,
		pagination : true,
		pageSize : 5,
		smartDisplay : false,
		pageList : [ "10", "25", "50", "100", "All" ],
		maintainSelected : true,
		columns : [ {
	    	field: 'ESEAL_NUMBER',
	    	title:  $.i18n.prop('warehouseEsealBO.esealNumber'),
	    },{
	    	field: 'ESEAL_STATUS',
	    	title:  $.i18n.prop('warehouseEsealBO.esealStatus'),
	    	formatter : elockStatusFormat ,
	    }],
	}); 
	$('#esealDetailTable').bootstrapTable('refresh', esealparams);
}

//显示传感器详细库存信息
function sensorDeatail(portName,seriesName){
	var sensorparams = $('#sensorDetailTable').bootstrapTable('getOptions');
	sensorparams.url = root+"/statisitc/sensordetails.action?portName="+portName+"&seriesName="+seriesName;
	$('#sensorDetailTable').bootstrapTable({
		//url :"${root}/statisitc/sensordetails.action?portName="+portName,
		pagination : true,
		pageSize : 5,
		smartDisplay : false,
		pageList : [ "10", "25", "50", "100", "All" ],
		maintainSelected : true,
		columns : [ {
	    	field: 'SENSOR_NUMBER',
	    	title:  $.i18n.prop('WarehouseSensor.sensorNumber'),
	    },{
	    	field: 'SENSOR_STATUS',
	    	title:  $.i18n.prop('WarehouseSensor.sensorStatus'),
	    	formatter : elockStatusFormat ,
	    }],
	}); 
	$('#sensorDetailTable').bootstrapTable('refresh', sensorparams);
}
