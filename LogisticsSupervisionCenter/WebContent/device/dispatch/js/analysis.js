//---------------------调度分析图表对象------

function ActualProgramChart( portName, data) {
	this.portName = portName; 
	this.data = data; 
}
var actualProgramChartObjArray = new Array(); //实际调度方案对象数组

/**
 * 通过整体数组变量构建图表
 * 
 * @param actualProgramChartObjArray
 */
function createActualProgramChartByObjArray(actualProgramChartObjArray) {
	actualProgramyAxisData = new Array();
	actualProgramSeriesData = new Array();
	$.each(actualProgramChartObjArray, function(i, value) {
		actualProgramyAxisData.push(value.portName);
		actualProgramSeriesData.push(value.data);
	});
	initActualProgramChart();
}

/**
 * 根据口岸名称和数据查询数组索引
 * 
 * @param portName
 * 			口岸名称
 * @param data
 * 			数据
 */
function findArrayIndexByPortNameAndData(portName) {
	var index = 0;
	$.each(actualProgramChartObjArray, function(i, value) {
		if(value.portName == portName) {
			index = i;
			return false;
		} 
	});
	return index;
}
//--------------------------------------
var actualProgramLegendData = [$.i18n.prop('warehouse.dispatch.distance')];	//调度分析实际方案Legend数组
var actualProgramSeriesData;	//调度分析实际方案Series数组
var actualProgramyAxisData ;	//调度分析实际方案yAxis数组


/**
 * 调度分析 JS方法
 */
$(function() {
	loadPortList();
	$('#portForm').bind('submit', function(e) {
		e.preventDefault(); //阻止页面跳转
		loadPortList();
	});
	
	initDeviceInventoryCharts(); //初始化设备库存报表
	
	initPlanProgramChart(planPortNameArr, planDistanceArr);
	//提交实际方案数据
	$("#dispatchExecuteButton").click(function() {
		submitActualProgramTableData();
	});
	
	$("#goBackButton").click(function() {
		window.location.href=root+'/warehouseDeviceApplication/index.action';
	});
});

//---------------页面数据相关加载方法--------
function loadPortList() {
	$('#portForm').ajaxSubmit({
		type: 'post',
		target: '#portTable',
		success : function(data) {
			if(!needLogin(data)) {
				//alert(data);
			}
		}
	});
}

//-------------------图表变量-----
var availableTrackDevice = $.i18n.prop('warehouse.dispatch.available.TrackDevice');
var destroyTrackDevice = $.i18n.prop('warehouse.dispatch.destroy.TrackDevice');
var reservationTrackDevice = $.i18n.prop('warehouse.dispatch.reservation.TrackDevice');
var availableEseal = $.i18n.prop('warehouse.dispatch.available.eseal');
var destroyEseal = $.i18n.prop('warehouse.dispatch.destroy.eseal');
var reservationEseal = $.i18n.prop('warehouse.dispatch.reservation.eseal');
var availableSensor = $.i18n.prop('warehouse.dispatch.available.sensor');
var destroySensor = $.i18n.prop('warehouse.dispatch.destroy.sensor');
var reservationSensor = $.i18n.prop('warehouse.dispatch.reservation.sensor');

var elock = $.i18n.prop('warehouse.device.elock');
var eseal = $.i18n.prop('warehouse.device.eseal');
var sensor = $.i18n.prop('warehouse.device.sensor');

/**
 * 初始化设备库存报表
 */
function initDeviceInventoryCharts() {
	var legendTitle = [
	   availableTrackDevice,
	   destroyTrackDevice,
	   reservationTrackDevice,
	   availableEseal,
	   destroyEseal,
	   reservationEseal,
	   availableSensor,
	   destroySensor,
	   destroySensor
	];
	var portNameArray= new Array();
	var portDeviceArray = new Array();
	var url = root+'/warehouseDispatchAnalysis/findDeviceInventoryList.action';
	$.ajax({
		url : url, // 请求url
		type : "post", // 提交方式
		dataType : "json", // 数据类型
		success : function(data) { // 提交成功的回调函数
			if(!needLogin(data)) {
				var deviceData = data.deviceInventoryList;
				$.each(deviceData,function(key, value) {
					portNameArray[key]=value.portName;
					portDeviceArray[key] = value.deviceArray;
				});
				var deviceInventoryChartsDiv = $("#deviceInventoryCharts")[0];
				var deviceInventoryCharts = echarts.init(deviceInventoryChartsDiv);
				option = {
						tooltip : {
							trigger: 'axis',
							axisPointer : {            // 坐标轴指示器，坐标轴触发有效
								type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
							}
						},
						legend: {
							data:legendTitle
						},
						grid: {
							left: '3%',
							right: '4%',
							bottom: 80,
							containLabel: true
						},
						xAxis : [
						         {
						        	 type : 'category',
						        	 axisLabel : {  
						        		 show:true,  
						        		 rotate:35,
						        		 interval: 0  
						        	 },
						        	 data : portNameArray
						         }
						         ],
						         yAxis : [
						                  {
						                	  type : 'value'
						                  }
						                  ],
						                  series : [
						                            {
						                            	name : availableTrackDevice,
						                            	type : 'bar',
						                            	stack: elock,
						                            	data:getDeviceData(portDeviceArray, 0)
						                            },
						                            {
						                            	name : destroyTrackDevice,
						                            	type : 'bar',
						                            	stack: elock,
						                            	data:getDeviceData(portDeviceArray, 1)
						                            },
						                            {
						                            	name : reservationTrackDevice,
						                            	type : 'bar',
						                            	stack: elock,
						                            	data:getDeviceData(portDeviceArray, 2)
						                            },
						                            {
						                            	name : availableEseal,
						                            	type : 'bar',
						                            	stack: eseal,
						                            	data:getDeviceData(portDeviceArray, 3)
						                            },
						                            {
						                            	name : destroyEseal,
						                            	type : 'bar',
						                            	stack: eseal,
						                            	data:getDeviceData(portDeviceArray, 4)
						                            },
						                            {
						                            	name : reservationEseal,
						                            	type : 'bar',
						                            	stack: eseal,
						                            	data:getDeviceData(portDeviceArray, 5)
						                            },
						                            {
						                            	name : availableSensor,
						                            	type : 'bar',
						                            	stack: sensor,
						                            	data:getDeviceData(portDeviceArray, 6)
						                            },
						                            {
						                            	name : destroySensor,
						                            	type : 'bar',
						                            	stack: sensor,
						                            	data:getDeviceData(portDeviceArray, 7)
						                            },
						                            {
						                            	name : reservationSensor,
						                            	type : 'bar',
						                            	stack: sensor,
						                            	data:getDeviceData(portDeviceArray, 8)
						                            } 
						                            ]
				};
				deviceInventoryCharts.setOption(option);
			}
		}
	});
}

function getDeviceData(portDeviceArray, i) {
	var array = new Array();
	$.each(portDeviceArray, function(key, value){
		array[key]=value[i];
	});
	return array;
}

/**
 * 添加实际方案
 * 
 * @param portId
 * 			口岸ID
 */
function addActualTable(portId) {
	var url = root + '/warehouseDispatchAnalysis/findDispatchActualProgramByPortId.action';
	$.ajax({
		url	 : url,
		type : "post", // 提交方式
		data : {"portId" : portId},
		dataType : "json", // 数据类型
		success : function(data) {
			if(!needLogin(data)) {
				var result = data.dispatchActualProgram;
				var html;
				html += '<tr>';
				html +='   <td style="display: none;"><input class="input_noborder" type="text" value="'+ portId +'" /></td>';
				html +='   <td>'+ result.portName +'</td>';
				html +='   <td><input class="input_noborder" type="text" value="'+ result.availableTrackDevice +'" /></td>';
				html +='   <td><input class="input_noborder" type="text" value="'+ result.availableEseal +'" /></td>';
				html +='   <td><input class="input_noborder" type="text" value="'+ result.availableSensor +'" /></td>';
				html +='</tr>';
				$("#actualTable tbody").append(html);
				//计算距离
				var applicationLongitude = $("#applicationLongitude").val();
				var applicationLatitude = $("#applicationLatitude").val();
				var endPoint = {
						lat : applicationLatitude,
						lng : applicationLongitude
				}
				var oriPoint = {
						lat : result.latitude,
						lng : result.longitude
				};
				measurePathLength(oriPoint,endPoint, function(disDuration){
					var dis = disDuration.distance;
					if(dis != undefined) {
						var obj = new ActualProgramChart(result.portName, dis.substring(0, dis.length - 2));
						actualProgramChartObjArray.push(obj);
						//报表
						createActualProgramChartByObjArray(actualProgramChartObjArray);
					}
				});
			}
		}
	});
}

/**
 * 删除实际方案数据
 * 
 * @param portId
 */
function deleteActualTable(portId) {
	$("#actualTable tr td:first-child").each(function(){ 
		if($(this).children("input").val()==portId){ 
			var portName = $(this).next().text();
			//alert(portName);
			$(this).parent().remove();
			var index = findArrayIndexByPortNameAndData(portName);
			//alert(index);
			actualProgramChartObjArray.splice(index, 1);
			createActualProgramChartByObjArray(actualProgramChartObjArray);
		} 
	}); 
}

var quantity = $.i18n.prop('warehouse.dispatch.quantity');
/**
 * 实际方案报表
 */
function initActualProgramChart() {
	var actualProgramChartDiv = $("#actualProgramChart")[0];
	var actualProgramChart = echarts.init(actualProgramChartDiv);
	option = {
		    tooltip: {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'shadow'
		        }
		    },
		    legend: {
		        data: actualProgramLegendData
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis: {
		        type: 'value',
		        boundaryGap: [0, 0.01]
		    },
		    yAxis: {
		        type: 'category',
		        data: actualProgramyAxisData
		    },
		    series: [
		        {
		            name: quantity,
		            type: 'bar',
		            data: actualProgramSeriesData
		        }
		    ]
		};
	actualProgramChart.setOption(option);
}

/**
 * 获取实际方案最终数据
 */
function getActualProgramTableData() {
	var milasUrl = new Array();//新建对象，用来存储所有数据
    var subMilasUrlArr = new Array();//存储每一行数据
    var tableData = new Array();
    $("#actualTable tbody tr").each(function(trindex,tritem){//遍历每一行
       tableData[trindex]=new Array();
	    $(tritem).find("input").each(function(tdindex,tditem){
	        tableData[trindex][tdindex]=$(tditem).val();//遍历每一个数据，并存入
	        subMilasUrlArr[trindex]=tableData[trindex];//将每一行的数据存入
	    });
    });
   for(var key in subMilasUrlArr) {
	   //alert(subMilasUrlArr[key]);
       milasUrl[key]=subMilasUrlArr[key];//将每一行存入对象
   }
   //alert(milasUrl[0][0]+" : " + milasUrl[1][0]);
   return milasUrl;
}

/**
 * 提交实际方案Table数据
 */
function submitActualProgramTableData() {
	//var serialize = $("#actualProgramForm").serialize();
	var deviceData = getActualProgramTableData();
	var trackingDeviceData=0;
	var esealData = 0;
	var sensorData = 0;
	for(var key in deviceData) {
		trackingDeviceData = Number(trackingDeviceData) + Number(deviceData[key][1]);
		esealData = Number(esealData) + Number(deviceData[key][2]);
		sensorData = Number(sensorData) + Number(deviceData[key][3]);
	}
	var applicationDeviceNumber = Number($("#applicationDeviceNumber").val());
	var applicationEsealNumber = Number($("#applicationEsealNumber").val());
	var applicationSensorNumber = Number($("#applicationSensorNumber").val());
	if(applicationDeviceNumber != trackingDeviceData) {
		bootbox.alert($.i18n.prop('warehouse.dispatch.elock.inconsistent'));
	} else if(applicationEsealNumber != esealData) {
		bootbox.alert($.i18n.prop('warehouse.dispatch.eseal.inconsistent'));
	} else if(applicationSensorNumber != sensorData) {
		bootbox.alert($.i18n.prop('warehouse.dispatch.sensor.inconsistent'));
	} else {
		var applicationId = $("#applicationId").val();
		var applicationPort = $("#applicationPort").val();
		//alert(JSON.stringify(deviceData));
		var ajaxUrl = root+'/warehouseDispatchAnalysis/submitDeviceDispatchInfo.action';
		$.ajax({
			url : ajaxUrl, // 请求url
			type : "post", // 提交方式
			data : {"deviceData": JSON.stringify(deviceData), "applicationId" : applicationId, "applicationPort" : applicationPort},
			dataType : "json", // 数据类型
			success : function(data) { // 提交成功的回调函数
				if(!needLogin(data)) {
					if (data.result) {
						//bootbox.alert($.i18n.prop('warehouse.dispatch.success'));
						bootbox.success($.i18n.prop('warehouse.dispatch.success'), function() {
							var url = root + "/warehouseDeviceApplication/index.action";
							window.location.href=url;
						})
					} else {
						bootbox.error($.i18n.prop('warehouse.dispatch.error'));
					}
				}
			}
		});
	}
}

/**
 * 推荐方案
 * 
 * @param planProgramyAxisData
 * @param planProgramSeriesData
 */
function initPlanProgramChart(planProgramyAxisData, planProgramSeriesData) {
	var planProgramChartDiv = $("#planProgramChart")[0];
	var planProgramChart = echarts.init(planProgramChartDiv);
	option = {
		    tooltip: {
		        trigger: 'axis',
		        axisPointer: {
		            type: 'shadow'
		        }
		    },
		    legend: {
		        data: actualProgramLegendData
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis: {
		        type: 'value',
		        boundaryGap: [0, 0.01]
		    },
		    yAxis: {
		        type: 'category',
		        data: planProgramyAxisData
		    },
		    series: [
		        {
		            name: quantity,
		            type: 'bar',
		            data: planProgramSeriesData
		        }
		    ]
		};
	planProgramChart.setOption(option);
}

