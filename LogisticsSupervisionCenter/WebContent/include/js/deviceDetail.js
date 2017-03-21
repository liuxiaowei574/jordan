$(function() {
	$('#elockTable').bootstrapTable({
		pagination : true,
		pageSize : 2,
		smartDisplay : false,
		maintainSelected : true,
		columns : [
		/*
		 * { field: 'ELOCK_ID', title: "关锁Id" },
		 */{
			field : 'ELOCK_NUMBER',
			title : $.i18n.prop('WarehouseElock.elockNumber')
		}, {
			field : 'ORGANIZATION_NAME',// 组织机构表里面的"机构名称"
			title : $.i18n.prop('WarehouseElock.belongTo')
		}, {
			field : 'SIM_CARD',
			title : $.i18n.prop('WarehouseElock.simCard')
		}, {
			field : 'INTERVAL',
			title : $.i18n.prop('WarehouseElock.interval')
		} ],
	});
	$elockTableBootstraptable = $('#elockTable').bootstrapTable();

	$('#esealTable').bootstrapTable({
		dataType : "json",
		pagination : true,
		pageSize : 2,
		smartDisplay : false,
		maintainSelected : true,
		columns : [ {
			field : 'ESEAL_NUMBER',
			title : $.i18n.prop('warehouseEsealBO.esealNumber')
		}, {
			field : 'ORGANIZATION_NAME',
			title : $.i18n.prop('warehouseEsealBO.belongTo')
		} ]
	});
	$esealTableBootstraptable = $('#esealTable').bootstrapTable();

	$('#sensorTable').bootstrapTable({
		pagination : true,
		pageSize : 2,
		smartDisplay : false,
		maintainSelected : true,
		columns : [ {
			field : 'SENSOR_NUMBER',
			title : $.i18n.prop('WarehouseSensor.sensorNumber')
		}, {
			field : 'ORGANIZATION_NAME',
			title : $.i18n.prop('WarehouseSensor.belongTo')
		} ]
	});
	$sensorTableBootstraptable = $('#sensorTable').bootstrapTable();

});
