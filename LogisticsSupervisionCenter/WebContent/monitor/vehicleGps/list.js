var selections = [];
var $table = $('#gpslogTable');
//刷新tale
$(window).resize(function(){
	$table.bootstrapTable("resetView");
});
$(function() {
	// 设置表格
	searchTripList();
	
	//时间控件
	$("#form_locationStartTime, #form_locationEndTime").datetimepicker({
		format: "yyyy-mm-dd hh:ii",//ii:分钟
		autoclose: true,
		todayBtn: true,
		language: 'en'
	}).on('changeDate', function(ev){
	    var locationStartTime = $('#s_locationStartTime').val();//获得检入开始时间
	    $('#form_locationEndTime').datetimepicker('setStartDate', locationStartTime);//设置检入结束时间（大于登入开始时间）
	});
	//搜索匹配
	$("#s_trackingDeviceNumber").bsSuggest({
        allowNoKeyword: false,   //是否允许无关键字时请求数据。为 false 则无输入时不执行过滤请求
        getDataMethod: "url",    //获取数据的方式，总是从 URL 获取
        url: root + "/monitorvehicle/findTables.action?deviceNum=" + $.trim(this.value),
        effectiveFields: ["deviceNum"],
        searchFields: [ "deviceNum"],
        ignorecase: true,
        listStyle: {
            'max-height': '300px',
        },
        showBtn: false,     //不显示下拉按钮
        idField: "deviceNum",
        keyField: "deviceNum"
    }).on('onDataRequestSuccess', function (e, result) {
        //console.log('onDataRequestSuccess: ', result);
    }).on('onSetSelectValue', function (e, keyword, data) {
        //console.log('onSetSelectValue: ', keyword, data);
    }).on('onUnsetSelectValue', function () {
        //console.log("onUnsetSelectValue");
    });
});
function searchTripList() {
	$table.bootstrapTable({
		clickToSelect : true,
		//url : root + '/monitorvehicle/list.action',
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 10,
		sortable:true,
		sortName:"locationTime",
		sortOrder: "desc",
		pageList : [ 10, 20, 30 ],
		columns : [
		{
			field : 'trackingDeviceNumber',
			title : $.i18n.prop('gpslog.trackingDeviceNumber'),
			sortable:true
		},{
			field : 'locationTime',
			title : $.i18n.prop('gpslog.locationTime'),
			sortable:true
		},{
			field : 'locationType',
			title : $.i18n.prop('gpslog.locationType'),
			sortable:true,
			formatter: locationTypeFormatter
		},{
			field : 'locationStatus',
			title : $.i18n.prop('gpslog.locationStatus'),
			sortable:true
		},{
			field : 'elockStatus',
			title : $.i18n.prop('gpslog.elockStatus'),
			sortable:true
		},{
			field : 'poleStatus',
			title : $.i18n.prop('gpslog.poleStatus'),
			sortable:true
		},{
			field : 'brokenStatus',
			title : $.i18n.prop('gpslog.brokenStatus'),
			sortable:true
		},{
			field : 'eventUpload',
			title : $.i18n.prop('gpslog.eventUpload'),
			sortable:true
		},{
			field : 'longitude',
			title : $.i18n.prop('gpslog.longitude'),
			sortable:true
		},{
			field : 'latitude',
			title : $.i18n.prop('gpslog.latitude'),
			sortable:true
		},{
			field : 'altitude',
			title : $.i18n.prop('gpslog.altitude'),
			sortable:true
		},{
			field : 'elockSpeed',
			title : $.i18n.prop('gpslog.elockSpeed'),
			sortable:true
		},{
			field : 'direction',
			title : $.i18n.prop('gpslog.direction'),
			sortable:true
		},{
			field : 'electricityValue',
			title : $.i18n.prop('gpslog.electricityValue'),
			sortable:true
		},{
			field : 'relatedDevice',
			title : $.i18n.prop('gpslog.relatedDevice'),
			sortable:true
		}],
	});
}
/**
 * 条件查询方法
 */
function search(){
	var value = $.trim($("#s_trackingDeviceNumber").val());
	if(value != null) {
		$("#s_trackingDeviceNumber").val(value);
		var options = $table.bootstrapTable('getOptions');
		var url = root + '/monitorvehicle/list.action';
		options.url = url;
		options.queryParams = function(params) {
			//遍历form 组装json  
			$.each($("#searchForm").serializeArray(), function(i, field) {  
				//可以添加提交验证                   
				params[field.name] = field.value;  
			});  
			return params;  
		};
		$table.bootstrapTable('refresh', options);
	}
}
function doRest(){
	$("#searchForm")[0].reset();
}
function locationTypeFormatter(value, row, index){
	var show ;
	if(value == '0'){
		show = $.i18n.prop('gpslog.locationType.elock');
	} else if(value == '1'){
		show = $.i18n.prop('gpslog.locationType.trackUnit');
	} else{
		show = '--'
	} 
	return [show].join('');
}