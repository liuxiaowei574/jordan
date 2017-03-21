/**
 * gis业务操作与gis基本操作区分
 */
var realVehicleUpdatespeed = 10000;//10秒钟更新一次数据
var vehicleRealPath= undefined;//车辆真实路线
var realVehicleDetailMarker= undefined;//车辆当前最新marker
var nucVehicleStatusData = null;//记录点击车辆最新状态信息用于查询坐标点
var nucVehicleId;//用于记录点击车辆的车辆Id
var freazeAlarmVehicleId = "";
var ALL_POINT = "ALL_POINT";
var SELECT_POINT = "SELECT_POINT";
var selectNumber = 30;
/**
 * 初始化地图车辆状态
 * @param flag true为展示到地图上，false为不展示
 */
function findAllVehicleStatus(flag) {
	$("#header_title").html($.i18n.prop("link.system.vehicleList"));
	$("#panelList").html("");
	
	var qdCheckValues = [];
	$('input[name="qdPorts"]:checked').each(function(){
		qdCheckValues.push($(this).val());
	});
	
	var zdCheckValues = [];
	$('input[name="zdPorts"]:checked').each(function(){
		zdCheckValues.push($(this).val());
	});
	var param = "&qdPorts="+qdCheckValues+"&zdPorts="+zdCheckValues;
	var portUrl = getRootPath() + "vehiclestatus/findAllVehicleStatus.action?locationType=0&tripStatus="+tripStatus + param;
	$.ajax({
		type : "POST",
		url : portUrl,
		dataType : "json",
		cache : false,
		async : false,
		error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
		success : function(jsonObj) {
			var vehicleData = jsonObj;
			var html = '';
            html += '<div class="list_search"><input type="text" class="form-control" id="vehicleSearch" placeholder="Query..." name="vehicleSearch"><button onclick="vehiclefliter()" type="button" class="btn btn-default "><span class="glyphicon glyphicon-search"></span></button></div>';
            html += '       <div class="alert_table">';
            html += '           <table id="alert_table"  class="alert table table-condensed table-striped table-hover">';
			if (null!=vehicleData && vehicleData.rows && vehicleData.rows.length > 0) {
				$("#account_num").html("("+vehicleData.rows.length+")");
				for (var key in vehicleData.rows) {
					(function(i){
						var gpsVehicleInfo = vehicleData.rows[i];
						if (gpsVehicleInfo.trackingDeviceNumber != null
								&& gpsVehicleInfo.trackingDeviceNumber != "") {
							var iconpath = "";
							if ("0" == gpsVehicleInfo.riskStatus) {
								iconpath = "static/images/Safetruck.png";
							} else if ("1" == gpsVehicleInfo.riskStatus){
								iconpath = "static/images/warningtruck.png";
							}else if("2" == gpsVehicleInfo.riskStatus){
								iconpath = "static/images/alarmtruck.png";
							}else {
								iconpath = "static/images/Safetruck.png";
							}
							var configuration = "static/images/configure.png";
							html += '<tr><td style="width:207px">'+"<input type=\"checkbox\"><img style=\"width:40px\" alt=\""
							+ gpsVehicleInfo.vehiclePlateNumber
							+ "\"src=\""
							+ iconpath
							+ "\"/>"
							+ "<a   id=\"vehicle_"
							+ gpsVehicleInfo.vehicleStatusId
							+ "\" href='javascript:void(0);' onclick='' class=\"vehicle_label\">"+ gpsVehicleInfo.vehiclePlateNumber+" </a>"
							+ ""+'</td>';
                                html += '<td><img  id=\"img_'+gpsVehicleInfo.vehicleStatusId+'\" src='+configuration+' title=\"Setting\" style="cursor: pointer;" /></td></tr>';
                                 
                                $(document).undelegate("#vehicle_"+gpsVehicleInfo.vehicleStatusId,"dblclick"); 
      							$(document).delegate("#vehicle_"+gpsVehicleInfo.vehicleStatusId,"dblclick", function(e){
      								if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
      									showTimeTrack(gpsVehicleInfo.trackingDeviceNumber,gpsVehicleInfo.tripId);
      								}
      							});
                                $(document).undelegate("#vehicle_"+gpsVehicleInfo.vehicleStatusId,"click");
     							$(document).delegate("#vehicle_"+gpsVehicleInfo.vehicleStatusId,"click", function(e){
     								if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
     								   $("#mask").css("height", $(document).height());
     								    $("#mask").css("width", $(document).width());
     								    $("#mask").show();
     								       //showMask();
     								   
     								       setTimeout(function(){
                                               $('#handlerPoint').text("Show Points");
                                               showOrHiddenPoint = false;
                                               GisHiddenOverlays(showPointMarkers);
                                               var obj = {data:gpsVehicleInfo};
                                               nucVehicleStatusData = null;
                                               nucVehicleStatusData = obj;
                                               routeAreaIdByVehicle = gpsVehicleInfo.routeId;
                                               tripIdByVehicle = gpsVehicleInfo.tripId;
                                               VEHICLE_INFO = gpsVehicleInfo;
                                               getVehiclePaths(obj,ALL_POINT);
                                               var v_this = this;
                                               if(menuType == "9"){
                                                   $("#trackSeleCtr").removeClass("hidden");
                                               }
                                               //"标准版"点击车辆时更新车辆报警列表，但不需要自动弹出
                                               pop(gpsVehicleInfo.vehicleId, false);
                                            }, 100);
     								      clearAllTimeout();// 清除所有的定时任务
     									//$('#progressModal').on('shown.bs.modal', function (e) {
     										
   										//})
//     									$('#progressModal').modal({
//     										 backdrop: 'static', 
//     										  keyboard: false
//     										 });
//     									$('#progressModal').modal('show')
     								}
     							});

     						   $(document).undelegate("#img_"+gpsVehicleInfo.vehicleStatusId,"click"); 
    							$(document).delegate("#img_"+gpsVehicleInfo.vehicleStatusId,"click", function(e){
    								if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
    								    console.log("lqxImg");
    									freazeAlarmVehicleId = gpsVehicleInfo.vehicleId;
    									var obj = {data:gpsVehicleInfo};
    									setVehicleProperty(obj);
    								}
    							});
//							$("#img_"+gpsVehicleInfo.vehicleStatusId).on("click", function(){
//								if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
//									freazeAlarmVehicleId = gpsVehicleInfo.vehicleId;
//									var obj = {data:gpsVehicleInfo};
//									setVehicleProperty(obj);
//								}
//			            	});
//							$("#vehicle_"+gpsVehicleInfo.vehicleStatusId).on("dblclick", function(){
//								if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
//									//freazeAlarmVehicleId = gpsVehicleInfo.vehicleId;
//									//var obj = {data:gpsVehicleInfo};
//				                	//setVehicleProperty(obj);
//				                	showTimeTrack(gpsVehicleInfo.trackingDeviceNumber);
//								}
//			            	});
							
							
							
						}
					})(key);
				}
				
			}else{
				$("#account_num").html("(0)");
			}
			html += '                </table>';
            html += '            <div class="clearfix"></div>';
            $("#panelList").append(html);
			if(flag){
				if (null!=vehicleData && vehicleData.rows && vehicleData.rows.length > 0) {
					addVehicleToMap(vehicleData.rows,false);
				}
			}
			
		}
	});
}
function getVehiclePath(){
    
}
//兼容火狐、IE8 
//显示遮罩层  
function showMask(callback) {
    //$("#mask").css("height", $(document).height());
    //$("#mask").css("width", $(document).width());
    callback();
}
/**
 * 过滤查询车辆
 */
function vehiclefliter(flag){
	var vehicleplatename = $("#vehicleSearch").val();
	if(menuType == "8"){
		$("#header_title").html($.i18n.prop("link.system.historyList"));
	}else{
		$("#header_title").html($.i18n.prop("link.system.vehicleList"));
	}
	
	$("#panelList").html("");
	
	var qdCheckValues = [];
	$('input[name="qdPorts"]:checked').each(function(){
		qdCheckValues.push($(this).val());
	});
	
	var zdCheckValues = [];
	$('input[name="zdPorts"]:checked').each(function(){
		zdCheckValues.push($(this).val());
	});
	var param = "&qdPorts="+qdCheckValues+"&zdPorts="+zdCheckValues;
	var portUrl = getRootPath() + "vehiclestatus/findAllVehicleStatus.action?locationType=0&tripStatus="+tripStatus + param+"&vehicleplatename="+vehicleplatename;
	$.ajax({
		type : "POST",
		url : portUrl,
		dataType : "json",
		cache : false,
		async : false,
		error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
		success : function(jsonObj) {
			var vehicleData = jsonObj;
			if (null!=vehicleData && vehicleData.rows && vehicleData.rows.length > 0) {
				$("#account_num").html("("+vehicleData.rows.length+")");
				var html = '';
				html += '<div class="list_search"><input type="text" class="form-control" id="vehicleSearch"  placeholder="Query..." name="vehicleSearch"><button onclick="vehiclefliter()" type="button" class="btn btn-default "><span class="glyphicon glyphicon-search"></span></button></div>';
				html += '		<div class="alert_table">';
				html += '			<table class="alert table table-condensed table-striped table-hover">';
				for (var key in vehicleData.rows) {
					(function(i){
						var gpsVehicleInfo = vehicleData.rows[i];
						
						if (gpsVehicleInfo.trackingDeviceNumber != null
								&& gpsVehicleInfo.trackingDeviceNumber != "") {
							var iconpath = "";
							if ("0" == gpsVehicleInfo.riskStatus) {
								iconpath = "static/images/Safetruck.png";
							} else if ("1" == gpsVehicleInfo.riskStatus){
								iconpath = "static/images/warningtruck.png";
							}else if("2" == gpsVehicleInfo.riskStatus){
								iconpath = "static/images/alarmtruck.png";
							}
							else {
								iconpath = "static/images/Safetruck.png";
							}
							var configuration = "static/images/configure.png";
							html += '<tr><td style="width:207px">'+"<input type=\"checkbox\"><img alt=\""
							+ gpsVehicleInfo.vehiclePlateNumber
							+ "\"src=\""
							+ iconpath
							+ "\"/>"
							+ "<a   id=\"vehicle_"
							+ gpsVehicleInfo.vehicleStatusId
							+ "\" href='javascript:void(0);' onclick='' class=\"vehicle_label\">"+ gpsVehicleInfo.vehiclePlateNumber+" </a>"
							+ ""+'</td>';
                                 html += '<td><img id=\"img_'+gpsVehicleInfo.vehicleStatusId+'\" src='+configuration+' title=\"Setting\" style="cursor: pointer;" /></td></tr>';
                                $(document).undelegate("#vehicle_"+gpsVehicleInfo.vehicleStatusId,"dblclick"); 
      							$(document).delegate("#vehicle_"+gpsVehicleInfo.vehicleStatusId,"dblclick", function(e){
      								if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
      									showTimeTrack(gpsVehicleInfo.trackingDeviceNumber,gpsVehicleInfo.tripId);
      								}
      							});
                                 $(document).undelegate("#vehicle_"+gpsVehicleInfo.vehicleStatusId,"click"); 
     							$("#vehicle_"+gpsVehicleInfo.vehicleStatusId).on("click", function(e){
     								if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
     								   console.log("lqxFilter·1");
     									var obj = {data:gpsVehicleInfo};
    									nucVehicleStatusData = null;
    									nucVehicleStatusData = obj;
    									routeAreaIdByVehicle = gpsVehicleInfo.routeId;
    									tripIdByVehicle = gpsVehicleInfo.tripId;
    									VEHICLE_INFO = gpsVehicleInfo;
    				                	getVehiclePaths(obj,ALL_POINT);
    				                	var v_this = this;
    				                	if(menuType == "9"){
    				                		$("#trackSeleCtr").removeClass("hidden");
    				                	}
    				                	//"标准版"点击车辆时更新车辆报警列表，但不需要自动弹出
    				                	pop(gpsVehicleInfo.vehicleId, false);
     								}
     							});

     						   $(document).undelegate("#img_"+gpsVehicleInfo.vehicleStatusId,"click"); 
    							$(document).delegate("#img_"+gpsVehicleInfo.vehicleStatusId,"click", function(e){
    								if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
    									freazeAlarmVehicleId = gpsVehicleInfo.vehicleId;
    									var obj = {data:gpsVehicleInfo};
    									setVehicleProperty(obj);
    								}
    							});
							}
					})(key);
				}
				html += '		    	</table>';
				html += '	         <div class="clearfix"></div>';
				$("#panelList").append(html);
			}else{
				$("#account_num").html("(0)");
			}
			if(flag){
				if (null!=vehicleData && vehicleData.rows && vehicleData.rows.length > 0) {
					addVehicleToMap(vehicleData.rows,false);
				}
			}
			
		}
	});
}


function vehiclefliterHistory(flag){
	var vehicleplatename = $("#vehicleSearchHistory").val();
	if(menuType == "8"){
		$("#header_title").html($.i18n.prop("link.system.historyList"));
	}else{
		$("#header_title").html($.i18n.prop("link.system.vehicleList"));
	}
	$("#header_title").removeClass("hidden");
	$("#account_num").removeClass("hidden");
	$("#header_title_div").removeClass("hidden");
	$("#header_title").html($.i18n.prop("link.system.historyList"));
	$("#trackquery").html("");
	
	var qdCheckValues = [];
	$('input[name="qdPorts"]:checked').each(function(){
		qdCheckValues.push($(this).val());
	});
	
	var zdCheckValues = [];
	$('input[name="zdPorts"]:checked').each(function(){
		zdCheckValues.push($(this).val());
	});
	var param = "&qdPorts="+qdCheckValues+"&zdPorts="+zdCheckValues;
	var portUrl = getRootPath() + "vehiclestatus/findAllVehicleStatus.action?locationType=0&tripStatus="+tripStatus + param+"&vehicleplatename="+vehicleplatename;
	$.ajax({
		type : "POST",
		url : portUrl,
		dataType : "json",
		cache : false,
		async : false,
		error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
		success : function(jsonObj) {
			var vehicleData = jsonObj;
			if (null!=vehicleData && vehicleData.rows && vehicleData.rows.length > 0) {
				$("#account_num").html("("+vehicleData.rows.length+")");
				var areaText = '<div class="list_search"><input type="text" class="form-control" id="vehicleSearchHistory" placeholder="Query..."  name="vehicleSearchHistory"><button type="button" onclick="vehiclefliterHistory() " class="btn btn-default "><span class="glyphicon glyphicon-search"></span></button></div>';
				$("#trackquery").append(areaText);
				for (var key in vehicleData.rows) {
					(function(i){
						var gpsVehicleInfo = vehicleData.rows[i];
						if (gpsVehicleInfo.trackingDeviceNumber != null
								&& gpsVehicleInfo.trackingDeviceNumber != "") {
							var iconpath = "";
							if ("0" == gpsVehicleInfo.riskStatus) {
								iconpath = "static/images/Safetruck.png";
							} else if ("1" == gpsVehicleInfo.riskStatus)
								iconpath = "static/images/warningtruck.png";
							else {
								iconpath = "static/images/Safetruck.png";
							}
							var areaText = "<li ><label><input type=\"checkbox\"><img  style=\"width:40px\" alt=\""
									+ gpsVehicleInfo.vehiclePlateNumber
									+ "\"src=\""
									+ iconpath
									+ "\"/>"
									+ "<a id=\"vehicle_"
									+ gpsVehicleInfo.vehicleStatusId
									+ "\" href='javascript:void(0);' class=\"vehicle_label\">"+ gpsVehicleInfo.vehiclePlateNumber+" </a>"
									+ "</label></li>";
							$("#trackquery").append(areaText);
							$("#vehicle_"+gpsVehicleInfo.vehicleStatusId).on("click", function(){
								if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
									var obj = {data:gpsVehicleInfo};
									nucVehicleStatusData = null;
									nucVehicleStatusData = obj;
									routeAreaIdByVehicle = gpsVehicleInfo.routeId;
									tripIdByVehicle = gpsVehicleInfo.tripId;
				                	getVehiclePaths(obj,ALL_POINT);
				                	var v_this = this;
				                	if(menuType == "9"){
				                		$("#trackSeleCtr").removeClass("hidden");
				                	}
				                	
								}
			            	});
							$("#vehicle_"+gpsVehicleInfo.vehicleStatusId).on("dblclick", function(){
								if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
									var obj = {data:gpsVehicleInfo};
				                	setVehicleProperty(obj);
								}
			            	});
						}
					})(key);
				}
			}else{
				$("#account_num").html("(0)");
			}
			if(flag){
				if (null!=vehicleData && vehicleData.rows && vehicleData.rows.length > 0) {
					addVehicleToMap(vehicleData.rows,false);
			}
			}
			
		}
	});
}
/**
 * 弹出小窗体显示车辆轨迹
 */
function showTimeTrack(trackingDeviceNumber,tripId)
{
	var url = root + "/vehiclestatus/vehicleOverViewMap.action?trackingDeviceNumber="+trackingDeviceNumber+"&tripId="+tripId;
	var d = dialog({
		id:trackingDeviceNumber,
	    title: $.i18n.prop('common.title.vehicleTrack'),
	    url:url,
	    padding: 0
	   // content: '欢迎使用 artDialog 对话框组件！',
	    //ok: function () {},
	   // statusbar: '<label><input type="checkbox">不再提醒</label>'
	});
	d.show();
	//})

}

function findVehicleTrackStatus(flag) {
	$("#header_title").removeClass("hidden");
	$("#account_num").removeClass("hidden");
	$("#header_title_div").removeClass("hidden");
	$("#header_title").html($.i18n.prop("link.system.historyList"));
	$("#trackquery").html("");
	
	var qdCheckValues = [];
	$('input[name="qdPorts"]:checked').each(function(){
		qdCheckValues.push($(this).val());
	});
	
	var zdCheckValues = [];
	$('input[name="zdPorts"]:checked').each(function(){
		zdCheckValues.push($(this).val());
	});
	var param = "&qdPorts="+qdCheckValues+"&zdPorts="+zdCheckValues;
	var portUrl = getRootPath() + "vehiclestatus/findAllVehicleStatus.action?locationType=0&tripStatus="+tripStatus + param;
	$.ajax({
		type : "POST",
		url : portUrl,
		dataType : "json",
		cache : false,
		async : false,
		error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
		success : function(jsonObj) {
			var vehicleData = jsonObj;
			if (null!=vehicleData && vehicleData.rows && vehicleData.rows.length > 0) {
				$("#account_num").html("("+vehicleData.rows.length+")");
				var areaText = '<div class="list_search"><input type="text" class="form-control" id="vehicleSearchHistory" placeholder="Query..."  name="vehicleSearchHistory"><button type="button" onclick="vehiclefliterHistory() " class="btn btn-default "><span class="glyphicon glyphicon-search"></span></button></div>';
				$("#trackquery").append(areaText);
				for (var key in vehicleData.rows) {
					(function(i){
						var gpsVehicleInfo = vehicleData.rows[i];
						if (gpsVehicleInfo.trackingDeviceNumber != null
								&& gpsVehicleInfo.trackingDeviceNumber != "") {
							var iconpath = "";
							if ("0" == gpsVehicleInfo.riskStatus) {
								iconpath = "static/images/Safetruck.png";
							} else if ("1" == gpsVehicleInfo.riskStatus)
								iconpath = "static/images/warningtruck.png";
							else {
								iconpath = "static/images/Safetruck.png";
							}
							var areaText = "<li ><label><input type=\"checkbox\"><img  style=\"width:40px\" alt=\""
									+ gpsVehicleInfo.vehiclePlateNumber
									+ "\"src=\""
									+ iconpath
									+ "\"/>"
									+ "<a id=\"vehicle_"
									+ gpsVehicleInfo.vehicleStatusId
									+ "\" href='javascript:void(0);' class=\"vehicle_label\">"+ gpsVehicleInfo.vehiclePlateNumber+" </a>"
									+ "</label></li>";
							$("#trackquery").append(areaText);
							$("#vehicle_"+gpsVehicleInfo.vehicleStatusId).on("click", function(){
								if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
									var obj = {data:gpsVehicleInfo};
									nucVehicleStatusData = null;
									nucVehicleStatusData = obj;
									routeAreaIdByVehicle = gpsVehicleInfo.routeId;
									tripIdByVehicle = gpsVehicleInfo.tripId;
				                	getVehiclePaths(obj,ALL_POINT);
				                	var v_this = this;
				                	if(menuType == "9"){
				                		$("#trackSeleCtr").removeClass("hidden");
				                	}
				                	
								}
			            	});
							$("#vehicle_"+gpsVehicleInfo.vehicleStatusId).on("dblclick", function(){
								if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
									var obj = {data:gpsVehicleInfo};
				                	setVehicleProperty(obj);
								}
			            	});
						}
					})(key);
				}
			}else{
				$("#account_num").html("(0)");
			}
			if(flag){
				if (null!=vehicleData && vehicleData.rows && vehicleData.rows.length > 0) {
					addVehicleToMap(vehicleData.rows,false);
			}
			}
			
		}
	});
}


function findAllVehicleStatusInit(flag) {
	$("#header_title").html($.i18n.prop("link.system.vehicleList"));
	$("#panelList").html("");
	
	var qdCheckValues = [];
	$('input[name="qdPorts"]:checked').each(function(){
		qdCheckValues.push($(this).val());
	});
	
	var zdCheckValues = [];
	$('input[name="zdPorts"]:checked').each(function(){
		zdCheckValues.push($(this).val());
	});
	var param = "&qdPorts="+qdCheckValues+"&zdPorts="+zdCheckValues;
	var portUrl = getRootPath() + "vehiclestatus/findAllVehicleStatus.action?locationType=0&tripStatus="+tripStatus + param;
	$.ajax({
		type : "POST",
		url : portUrl,
		dataType : "json",
		cache : false,
		async : false,
		error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
		success : function(jsonObj) {
            var vehicleData = jsonObj;
            var html = '';
            html += '<div class="list_search"><input type="text" class="form-control" id="vehicleSearch" placeholder="Query..." name="vehicleSearch"><button onclick="vehiclefliter()" type="button" class="btn btn-default "><span class="glyphicon glyphicon-search"></span></button></div>';
            html += '       <div class="alert_table">';
            html += '           <table class="alert table table-condensed table-striped table-hover">';
            if (null!=vehicleData && vehicleData.rows && vehicleData.rows.length > 0) {
                $("#account_num").html("("+vehicleData.rows.length+")");
                for (var key in vehicleData.rows) {
                    (function(i){
                        var gpsVehicleInfo = vehicleData.rows[i];
                        
                        if (gpsVehicleInfo.trackingDeviceNumber != null
                                && gpsVehicleInfo.trackingDeviceNumber != "") {
                            var iconpath = "";
                            if ("0" == gpsVehicleInfo.riskStatus) {
                                iconpath = "static/images/Safetruck.png";
                            } else if ("1" == gpsVehicleInfo.riskStatus){
                                iconpath = "static/images/warningtruck.png";
                            }else if("2" == gpsVehicleInfo.riskStatus){
                                iconpath = "static/images/alarmtruck.png";
                            }
                            else {
                                iconpath = "static/images/Safetruck.png";
                            }
                            var configuration = "static/images/configure.png";
                            html += '<tr><td style="width:207px">'+"<input type=\"checkbox\"><img style=\"width:40px\" alt=\""
                            + gpsVehicleInfo.vehiclePlateNumber
                            + "\"src=\""
                            + iconpath
                            + "\"/>"
                            + "<a   id=\"vehicle_"
                            + gpsVehicleInfo.vehicleStatusId
                            + "\" href='javascript:void(0);' onclick='' class=\"vehicle_label\">"+ gpsVehicleInfo.vehiclePlateNumber+" </a>"
                            + ""+'</td>';
                           // if(travleStatus==0){
                                 html += '<td><img  id=\"img_'+gpsVehicleInfo.vehicleStatusId+'\" src='+configuration+' title=\"Setting\" style="cursor: pointer;" /></td></tr>';
                           // }else{
                            //  html += '<td><img src='+offline+' title=\"offline\"/></td></tr>';
                           // }   onclick=\"showTimeTrack('+gpsVehicleInfo.trackingDeviceNumber+')\"
                            //$("#panelList").append(areaText);
                                 
                                $(document).undelegate("#vehicle_"+gpsVehicleInfo.vehicleStatusId,"dblclick"); 
                                $(document).delegate("#vehicle_"+gpsVehicleInfo.vehicleStatusId,"dblclick", function(e){
                                    if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
                                        showTimeTrack(gpsVehicleInfo.trackingDeviceNumber,gpsVehicleInfo.tripId);
                                    }
                                });
                                $(document).undelegate("#vehicle_"+gpsVehicleInfo.vehicleStatusId,"click"); 
                                $(document).delegate("#vehicle_"+gpsVehicleInfo.vehicleStatusId,"click", function(e){
                                    if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
                                        var obj = {data:gpsVehicleInfo};
                                        nucVehicleStatusData = null;
                                        nucVehicleStatusData = obj;
                                        routeAreaIdByVehicle = gpsVehicleInfo.routeId;
                                        tripIdByVehicle = gpsVehicleInfo.tripId;
                                        VEHICLE_INFO = gpsVehicleInfo;
                                        getVehiclePaths(obj,ALL_POINT);
                                        var v_this = this;
                                        if(menuType == "9"){
                                            $("#trackSeleCtr").removeClass("hidden");
                                        }
                                        //"标准版"点击车辆时更新车辆报警列表，但不需要自动弹出
                                        pop(gpsVehicleInfo.vehicleId, false);
                                    }
                                });

                               $(document).undelegate("#img_"+gpsVehicleInfo.vehicleStatusId,"click"); 
                                $(document).delegate("#img_"+gpsVehicleInfo.vehicleStatusId,"click", function(e){
                                    if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
                                        freazeAlarmVehicleId = gpsVehicleInfo.vehicleId;
                                        var obj = {data:gpsVehicleInfo};
                                        setVehicleProperty(obj);
                                    }
                                });
//                          $("#img_"+gpsVehicleInfo.vehicleStatusId).on("click", function(){
//                              if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
//                                  freazeAlarmVehicleId = gpsVehicleInfo.vehicleId;
//                                  var obj = {data:gpsVehicleInfo};
//                                  setVehicleProperty(obj);
//                              }
//                          });
//                          $("#vehicle_"+gpsVehicleInfo.vehicleStatusId).on("dblclick", function(){
//                              if(typeof(gpsVehicleInfo.vehicleStatusId)!="undefined"){
//                                  //freazeAlarmVehicleId = gpsVehicleInfo.vehicleId;
//                                  //var obj = {data:gpsVehicleInfo};
//                                  //setVehicleProperty(obj);
//                                  showTimeTrack(gpsVehicleInfo.trackingDeviceNumber);
//                              }
//                          });
                            
                            
                            
                        }
                    })(key);
                }
                
            }else{
                $("#account_num").html("(0)");
            }
            html += '                </table>';
            html += '            <div class="clearfix"></div>';
            $("#panelList").append(html);
            if(flag){
                if (null!=vehicleData && vehicleData.rows && vehicleData.rows.length > 0) {
                    addVehicleToMap(vehicleData.rows,true);
                }
            }
            
        }
	});
}
/**
 * 设置车辆属性
 */
function setVehicleProperty(vehicleInfo){
	var url = root + "/vehiclesetting/vehicleSetModalShow.action?vehicleId="+vehicleInfo.data.vehicleId;
	//showDialog("",url);
//	var url = url;
//	var d = dialog({
//	    title: '欢迎',
//	    width:'auto',
//	    height:'auto',
//	    url:url,
//	   // content: '欢迎使用 artDialog 对话框组件！',
//	    ok: function () {},
//	    statusbar: '<label><input type="checkbox">不再提醒</label>'
//	});
//	d.show();
	$('#propertySetModal').removeData('bs.modal');
	$('#propertySetModal').modal({
		remote : url,
		show : false,
		backdrop: 'static', 
		keyboard: false
	});
	$('#propertySetModal').on('loaded.bs.modal', function(e) {
		$('#propertySetModal').modal('show');
	});
}
/**
 *添加车辆到地图
*/

 function addVehicleToMap(vehicleArr,isInit){
     GisClearOverlays(svgMarkers);
     GisClearOverlays(vehicleMarkers);
	 var setViewArr = [];
	 for ( var key in vehicleArr) {
		 (function(x){
			 var vehicle = vehicleArr[x];
			 if(vehicle.longitude!='0' && vehicle.latitude!=0 && vehicle.latitude!=null &&vehicle.longitude!=null){
				var localPoint = {lng:vehicle.longitude,lat:vehicle.latitude};
				setViewArr.push(localPoint);
				createVehicleMarker(localPoint,vehicle,"blue","white");
				var html = createCurrentContent(vehicle);
				//GisShowInfoWindow(vehicleMarker,html,false);
				
				//markerclusterer = GisMarkersClusterer(vehicleMarkers, 60, markerclusterer);
			}
		 })(key);
	
	}
	 initvehicelView = setViewArr;
	 if(isInit){
		 initvehicelView = setViewArr;
	 	 GisSetViewPortByArray(setViewArr);
	 }
 }
 /**
  *添加车辆到地图
 */
 function showDialog(content,url){
	 var d = dialog({
			title: $.i18n.prop('trip.info.message'),
			 url:url,
			 width:'auto',
			 height:'auto',
			// left:'90%',
			// top:'20%',
			 resize:true,
			content: content
		});
	    //d.position('90%', '20%');
		d.show();
 }
  
//  function addVehicleToMapInit(vehicleArr){
//      GisClearOverlays(svgMarkers);
//      var setViewArr = [];
//      for ( var key in vehicleArr) {
//          (function(x){
//              var vehicle = vehicleArr[x];
//              if(vehicle.longitude!='0' && vehicle.latitude!=0 && vehicle.latitude!=null &&vehicle.longitude!=null){
//                 var localPoint = {lng:vehicle.longitude,lat:vehicle.latitude};
//                 setViewArr.push(localPoint);
//                 createVehicleMarker(localPoint,vehicle,"blue","white");
//             }
//          })(key);
//     
//     }
// 	 initvehicelView = setViewArr;
// 	 GisSetViewPortByArray(setViewArr);
//  }
 var initvehicelView = [];
 function setVehicleView(vehicelArr){
	 GisSetViewPortByArray(vehicelArr);
 }
/**
 * 初始化巡逻队车辆状态
 * @param flag 是否展示巡逻队列表
 */
function findAllPatrolStatus(flag) {
 	var portUrl = getRootPath() + "vehiclestatus/findAllPatrolStatus.action?locationType=1";
	//var portUrl = getRootPath() + "patrolMgmt/findAllPatrols.action";
 	$("#patrolList").html("");
 	if(menuType=="7"){
		$("#patrolList").removeClass("hidden");
		$("#planRouteAreaList").addClass("hidden");
		$("#panelList").addClass("hidden");
		$("#classify").addClass("hidden");
		$("#addDelUpdate").addClass("hidden");
		$("#addRapoint").addClass("hidden");
		$("#addLandMarker").addClass("hidden");
		$("#mergePatrol").addClass("hidden");
		$("#handlePatrol").removeClass("hidden");
		$("#handleLanderMarker").addClass("hidden");
	}	
 	$.ajax({
 		type : "POST",
 		url : portUrl,
 		dataType : "json",
 		cache : false,
 		async : false,
 		error : function(e, message, response) {
 			console.log("Status: " + e.status + " message: " + message);
 		},
 		success : function(jsonObj) {
 			var patrolData = jsonObj;
 			if (null!=patrolData && patrolData.rows && patrolData.rows.length > 0) {
 				if(flag){
 					var commonPatrols = patrolData.rows;
 					$("#account_num").html("("+commonPatrols.length+")");
 					if(menuType=="7"){
 						for (var i = 0; i < commonPatrols.length; i++) {
 							var commonPatrol = commonPatrols[i];
 							
 							if (isNotNull(commonPatrol.patrolId)) {
 								var iconpath = "static/images/ic_08.png";
 								var areaText = "<li><label ><input type=\"checkbox\" "
 										+ "name=\"patrolIds\" value=\""
 										+ commonPatrol.patrolId+"\"><img  alt=\""
 										+ commonPatrol.trackUnitNumber
 										+ "\" src=\""
 										+ iconpath
 										+ "\"/>"
 										+ "<a  onclick=\"getPatrolLocation('"
 										+ commonPatrol.patrolId 
 										+ "','"+commonPatrol.longitude
 										+ "','"+commonPatrol.latitude
 										+ "');\" href='javascript:void(0);' class=\"vehicle_label\">"+ commonPatrol.patrolNumber+" </a>"
 										+ "</label></li>";
 								$("#patrolList").append(areaText);
 							}
 						}
 					}else if(menuType=="10"){
 						for (var i = 0; i < commonPatrols.length; i++) {
 							var commonPatrol = commonPatrols[i];
							if (isNotNull(commonPatrol.patrolId)) {
								var iconpath = "static/images/ic_08.png";
								var areaText = "<li><label ><input type=\"checkbox\" "
										+ "name=\"patrolIds\" value=\""
										+ commonPatrol.potralUser+"\"><img  alt=\""
										+ commonPatrol.trackUnitNumber
										+ "\" src=\""
										+ iconpath
										+ "\"/>"
										+ "<a  onclick=\"getPatrolTrack('"
										+ commonPatrol.tripId 
										+ "');\" href='javascript:void(0);' class=\"vehicle_label\">"+ commonPatrol.potralUser+" </a>"
										+ "</label></li>";
								$("#patrolList").append(areaText);

							}
 						}
 						
 					}
					
 	 			}
 				//所有巡逻队展示在地图上
 				addPatrolToMap(patrolData.rows);
 			}else{
 				if(flag){
 					$("#account_num").html("(0)");
 				}
 			}
 		}
 	});
 }
 function getPatrolTrack(){
	 var portUrl = getRootPath() + "vehiclestatus/findAllEndTripPatrol.action?locationType=1";
	 $("#trackquery").html("");
	 	if(menuType=="7"){
			$("#patrolList").removeClass("hidden");
			$("#planRouteAreaList").addClass("hidden");
			$("#panelList").addClass("hidden");
			$("#classify").addClass("hidden");
			$("#addDelUpdate").addClass("hidden");
			$("#addRapoint").addClass("hidden");
			$("#addLandMarker").addClass("hidden");
			$("#mergePatrol").addClass("hidden");
			$("#handlePatrol").removeClass("hidden");
			$("#handleLanderMarker").addClass("hidden");
		}	
	 	$.ajax({
	 		type : "POST",
	 		url : portUrl,
	 		dataType : "json",
	 		cache : false,
	 		async : false,
	 		error : function(e, message, response) {
	 			console.log("Status: " + e.status + " message: " + message);
	 		},
	 		success : function(jsonObj) {
	 			var patrolData = jsonObj;
	 			if (null!=patrolData && patrolData.rows && patrolData.rows.length > 0) {
	 				//if(flag){
	 					var commonPatrols = patrolData.rows;
	 					//$("#account_num").html("("+commonPatrols.length+")");
	 					if(menuType=="7"){
	 						for (var i = 0; i < commonPatrols.length; i++) {
	 							var commonPatrol = commonPatrols[i];
	 							if (isNotNull(commonPatrol.patrolId)) {
	 								var iconpath = "static/images/ic_08.png";
	 								var areaText = "<li><label ><input type=\"checkbox\" "
	 										+ "name=\"patrolIds\" value=\""
	 										+ commonPatrol.patrolId+"\"><img  alt=\""
	 										+ commonPatrol.trackUnitNumber
	 										+ "\" src=\""
	 										+ iconpath
	 										+ "\"/>"
	 										+ "<a  onclick=\"getPatrolLocation('"
	 										+ commonPatrol.patrolId 
	 										+ "','"+commonPatrol.longitude
	 										+ "','"+commonPatrol.latitude
	 										+ "');\" href='javascript:void(0);' class=\"vehicle_label\">"+ commonPatrol.vehiclePlateNumber+" </a>"
	 										+ "</label></li>";
	 								$("#trackquery").append(areaText);
	 							}
	 						}
	 					}else if(menuType=="8"){
	 						for (var i = 0; i < commonPatrols.length; i++) {
	 							var commonPatrol = commonPatrols[i];
								if (isNotNull(commonPatrol.patrolId)) {
									var starttime = $("#s_trackStartTime").val();
									var endtime = $("#s_trackEndTime").val();
									var iconpath = "static/images/ic_08.png";
									var areaText = "<li><label ><input type=\"checkbox\" "
											+ "name=\"patrolIds\" value=\""
											+ commonPatrol.potralUser+"\"><img  alt=\""
											+ commonPatrol.trackUnitNumber
											+ "\" src=\""
											+ iconpath
											+ "\"/>"
											+ "<a  onclick=\"getPatrolGps('"
											+ commonPatrol.tripId 
											+ "','"+commonPatrol.trackUnitNumber+"','"+starttime+"','"+endtime+"');\" href='javascript:void(0);' class=\"vehicle_label\">"+ commonPatrol.vehiclePlateNumber+" </a>"
											+ "</label></li>";
									$("#trackquery").append(areaText);

								}
	 						}
	 						
	 					}
						
	 	 		//	}
	 				//所有巡逻队展示在地图上
	 				addPatrolToMap(patrolData.rows);
	 			}else{
	 				if(flag){
	 					$("#account_num").html("(0)");
	 				}
	 			}
	 		}
	 	});
 }
 
 
 function getPatrolGps(tripId,trackUnitNumer1,startTime,endTime){
	 var patrolUrl = getRootPath() + "vehicletrack/findPatrolTrackByUnitNumber.action?tripId="+tripId+"&trackUnitNumber="+trackUnitNumer1+"&startTime="+startTime+"&endTime="+endTime;
	 $.ajax({
	 		type : "POST",
	 		url : patrolUrl,
	 		dataType : "json",
	 		cache : false,
	 		async : false,
	 		error : function(e, message, response) {
	 			console.log("Status: " + e.status + " message: " + message);
	 		},
	 		success : function(obj) {
	 			var jsonObj = obj.lsMonitorVehicleGpsBOs;//车辆实时轨迹
				if(jsonObj!=null){
					clearAllTimeout();//清除所有的定时任务
					mapCreatePatrolTracking(jsonObj,obj.lsMonitorTripBO,gpsVehicleInfo,type);//地图上展示车辆
				}
	 		}
	 });
 }
 
 
 /**
  * 巡逻队轨迹
  */
 function mapCreatePatrolTracking(jsonObj,lsMonitorTripBO,gpsVehicleInfo,type){
	 raPointsArr = jsonObj;
		GisClearOverlays(overlaysArrayRoute);
		GisClearOverlays(overlaysPlanRoute);
		tripBufferPoints = [];
		if(ALL_POINT==type){
			for (var i = 0; i < jsonObj.length; i++) {
				tripBufferPoints.push({
					lat : jsonObj[i].latitude,
					lng : jsonObj[i].longitude,
					direction: jsonObj[i].direction
				});
				//显示隐藏轨迹上的点
				/*var pointMarker = GisCreateMarker({lat:jsonObj[i].latitude,lng:jsonObj[i].longitude},"static/images/pointIcon.png",""+jsonObj[i].gpsId);
				GisShowInfoWindow(pointMarker,""+jsonObj[i].gpsId,false);
				pointMarkers.push(pointMarker);*/
			}
			var lineStyle = {
					color:"#1800ff", 
					weight:4, 
					opacity:1,
					zIndex:999
				};
		}else{
			for (var i = 0; i < selectNumber; i++) {
				tripBufferPoints.push({
					lat : jsonObj[jsonObj.length-selectNumber+i].latitude,
					lng : jsonObj[jsonObj.length-selectNumber+i].longitude,
					direction: jsonObj[jsonObj.length-selectNumber+i].direction
				});
				//显示隐藏轨迹上的点
				/*var pointMarker = GisCreateMarker({lat:jsonObj[i].latitude,lng:jsonObj[i].longitude},"static/images/pointIcon.png",""+jsonObj[i].gpsId);
				GisShowInfoWindow(pointMarker,""+jsonObj[i].gpsId,false);
				pointMarkers.push(pointMarker);*/
			}
			var lineStyle = {
					color:"#ff00ff", 
					weight:4, 
					opacity:1,
					zIndex:2
				};
		}
		
		
		
		/*if($("#handlePoint").attr("name")=="showPoint"){
			GisHiddenOverlays(pointMarkers);
		}else{
			GisShowOverlays(pointMarkers);
		}*/
		
		var localPoint = tripBufferPoints[tripBufferPoints.length-1];
		if("0"==gpsVehicleInfo.riskStatus){
			replayIcon = "images/gis/truck1.png";
		}else if("1"==gpsVehicleInfo.riskStatus){
			replayIcon = "images/gis/truck2.png";
		}else{
			replayIcon =  "images/gis/truck3.png";
		}
		
		if(menuType=="8"){
			GisCreateLineTrack(tripBufferPoints);
		}else{
			trackingLine = GisShowPolyLineInMap(tripBufferPoints, true, lineStyle);
			//GisSetShowFront(trackingLine,1);
			overlaysArrayRoute.push(trackingLine);
		}
		GisSetViewPortByArray(tripBufferPoints);
		
		if("0"==gpsVehicleInfo.riskStatus){
			
			trackingMaker =  GisCreateVehicelMarker(localPoint, createSafeIcon(gpsVehicleInfo.direction),""+lsMonitorTripBO.tripId,JSON.stringify(gpsVehicleInfo));
		}else if("1"==gpsVehicleInfo.riskStatus){
			trackingMaker =  GisCreateVehicelMarker(localPoint, createRiskIcon(gpsVehicleInfo.direction),""+lsMonitorTripBO.tripId,JSON.stringify(gpsVehicleInfo));
		}else{
			trackingMaker =  GisCreateVehicelMarker(localPoint, createDangerIcon(gpsVehicleInfo.direction),""+lsMonitorTripBO.tripId,JSON.stringify(gpsVehicleInfo));
		}
		var svgMaker =  GisCreateVehicleSVGMarker(localPoint, GisCreateSvgIcon(google.maps.SymbolPath.FORWARD_CLOSED_ARROW,2,gpsVehicleInfo.direction,"green","green"),""+gpsVehicleInfo.vehicleStatusId,"111");
			vehicleMarkers.push(svgMaker);
		var currentContent = creatRealVehcleContent(gpsVehicleInfo);
		
		GisAddEventForVehicle(trackingMaker, "click", function() {
			 var d = dialog({
				    id: gpsVehicleInfo.vehicleStatusId,
					title: gpsVehicleInfo.vehicleStatusId,//$.i18n.prop('trip.info.message'),
					content: currentContent,
					//okValue: '确 定',
					resize:true
				});
				d.show();
			//showDialog(html);
	    	//getVehiclePaths(obj,ALL_POINT);
	    });
		vehicleMarkers.push(trackingMaker);
 }
/**
 *添加巡逻队车辆到地图
 */
 function addPatrolToMap(patrolData){
	 GisClearOverlays(patrolMarkers);
	 for ( var key in patrolData) {
		 (function(x){
			 var patrol = patrolData[x];
			 var patrolIcon = "";
				var patrolName = patrol.roleName;
				if(patrolName=="enforcementPatrol"){
					patrolIcon = "images/gis/xunluo.png";
				}else if(patrolName=="escortPatrol"){
					patrolIcon = "images/gis/husongxunluo.png";
				}else{
					patrolIcon = "images/gis/husongxunluo.png";
				}
			 if(patrol.longitude!='0' && patrol.latitude!=0 && patrol.latitude!=null &&patrol.longitude!=null){
				var localPoint = {lng:patrol.longitude,lat:patrol.latitude};
				var patrolMarker =  GisCreateMarker(localPoint, patrolIcon,""+patrol.patrolId,JSON.stringify(patrol));
				GisSetShowFront(patrolMarker);//谷歌地图设置数字越大，越显示在最前
				patrolMarkers.push(patrolMarker);
				var html = createPatrolContent(patrol);
				GisAddEventForVehicle(patrolMarker, "click", function() {
					 var d = dialog({
						    id: patrol.patrolId,
							title: $.i18n.prop('common.title.patrolDetail'),
							content: html,
							//okValue: '确 定',
							resize:true,
							left:'90%',
							top:'20%'
						});
						d.show();
			    });
				partolclusterer = GisMarkersClusterer(patrolMarkers, 60, partolclusterer);
			}
		})(key);
	}
 }
 
 
 /**
  * 定时刷新车辆列表与地图车辆显示
  */
 function timeIntervalInit(){
		vehicelList("");
		function vehicelList(condition){
			findAllOnWayMonitorTrip(condition);
			allRealVehicleTimeoutValue = setTimeout(function(){
				vehicelList(condition);
			},realVehicleUpdatespeed);
		}
 }
 /**
  * 构建车辆信息弹出框
  */
 function createCurrentContent(obj){
	 var html = '';
	 var iconpath = "static/images/Safetruck.png";
	 if ("0" == obj.riskStatus) {
		 iconpath = "static/images/Safetruck.png";
	 } else if ("1" == obj.riskStatus){
		 iconpath = "static/images/warningtruck.png";
	 }else if ("2" == obj.riskStatus){
         iconpath = "static/images/alarmtruck.png";
     }
	 else{
		 iconpath = "static/images/Safetruck.png";
	 }
	 if(obj){
		 var locationTime = formatDateTime(new Date(obj.locationTime));
		 if(obj.locationTime.time) {
			 locationTime = formatDateTime(new Date(obj.locationTime.time));
		 }
			html += '<div class="alert_box">';
			html += '	<div class="alert_box_content">';
			html += '		<div class="alert-title">';
			html += '		    <div class="pull-left">';
			html += '			    <div class="Vehicle_bubble"><img alt="'+ obj.vehiclePlateNumber+ '" src="'+ iconpath+ '"/></div>';
			html += '				<b>'+obj.vehiclePlateNumber+'</b>';
			html += '           </div>';
			html += '			<div class="pull-right">'+$.i18n.prop("eclock.label.locationTime")+':'+locationTime+'</div>';
			html += '		</div>';
			html += '		<div class="alert_table"><tbody>';
			html += '			<table class="table table-condensed table-striped table-hover">';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.trackingDeviceNumber")+'</th>';
			html += '			         <td>'+obj.trackingDeviceNumber+'</td>';
			html += '			     <th>'+$.i18n.prop("monitorTrip.label.containerNumber")+'</th>';
			html += '			         <td>'+obj.containerNumber+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.declarationNumber")+'</th>';
			html += '			         <td>'+obj.declarationNumber+'</td>';
			html += '			     <th>'+$.i18n.prop("monitorTrip.label.trailerNumber")+'</th>';
			html += '			         <td>'+obj.trailerNumber+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.driverName")+'</th>';
			html += '			         <td>'+obj.driverName+'</td>';
			html += '			     <th>'+$.i18n.prop("monitorTrip.label.driverCountry")+'</th>';
			html += '			         <td>'+obj.driverCountry+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.vehicleCountry")+'</th>';
			html += '			         <td>'+obj.vehicleCountry+'</td>';
			html += '			     <th>'+$.i18n.prop("monitorTrip.label.esealNumber")+'</th>';
			html += '			         <td>'+obj.esealNumber+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.sensorNumber")+'</th>';
			html += '			         <td>'+obj.sensorNumber+'</td>';
			html += '			     <th>'+$.i18n.prop("monitorTrip.label.elockStatus")+'</th>';
			if(obj.altitude == "1"){
				html += '			         <td>'+$.i18n.prop("monitorTrip.label.elockStatus.lock")+'</td></tr>';
			}else{
				html += '			         <td>'+$.i18n.prop("monitorTrip.label.elockStatus.unlock")+'</td></tr>';
			}
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.FromTo")+'</th>';
			html += '			         <td>'+obj.checkinPortName+' - '+obj.checkoutPortName+'</td><th>'+$.i18n.prop("trip.label.routeId")+'</th>';
            html += '                    <td>'+obj.routeAreaName+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.locationAttr")+'</th>';
			html += '			         <td>'+(parseFloat(obj.longitude)).toFixed(6)+','+(parseFloat(obj.latitude)).toFixed(6)+'</td>';
			html += '                    <th>'+$.i18n.prop("monitorTrip.label.direction")+'('+'<img src="static/images/clockwise.png"  alt="clockwise(N-S)" />'+')'+'</th>';
			html += '			         <td>'+obj.direction+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.checkinTime")+'</th>';
			html += '			         <td>'+formatDateTime(new Date(obj.checkinTime))+'</td>';
			html += '			     <th>'+$.i18n.prop("monitorTrip.label.checkinUser")+'</th>';
			html += '			         <td>'+obj.checkinUserName+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.electricityValue")+'</th>';
			html += '			         <td>'+obj.electricityValue+'</td>';
			html += '			     <th>'+$.i18n.prop("monitorTrip.label.elockSpeed")+'</th>';
			html += '			         <td>'+obj.elockSpeed+'</td></tr>';
			html += '		    </tbody></table>';
			html += '	    </div>';
			html += '   </div>';
			html += '</div>';
	 }
	 return html;
 }
 
 /**
  * 构建车辆信息弹出框
  */
 function createUpdateContent(obj,newObj){
	 var html = '';
	 var iconpath = "static/images/Safetruck.png";
	 if ("0" == obj.riskStatus) {
		 iconpath = "static/images/Safetruck.png";
	 } else if ("1" == obj.riskStatus){
		 iconpath = "static/images/warningtruck.png";
	 }
	 else if ("2" == obj.riskStatus){
		 iconpath = "static/images/alarmtruck.png";
	 }
	 else{
		 iconpath = "static/images/Safetruck.png";
	 }
	 if(obj){
		 var locationTime = formatDateTime(new Date(newObj.locationTime));
		 if(newObj.locationTime.time) {
			 locationTime = formatDateTime(new Date(newObj.locationTime.time));
		 }
			html += '<div class="alert_box">';
			html += '	<div class="alert_box_content">';
			html += '		<div class="alert-title">';
			html += '		    <div class="pull-left">';
			html += '			    <div class="Vehicle_bubble"><img title="'+ obj.vehiclePlateNumber+ '" src="'+ iconpath+ '"/></div>';
			html += '				<b>'+obj.vehiclePlateNumber+'</b>';
			html += '           </div>';
			html += '			<div class="pull-right">'+$.i18n.prop("eclock.label.locationTime")+':'+locationTime+'</div>';
			html += '		</div>';
			html += '		<div class="alert_table"><tbody>';
			html += '			<table class="table table-condensed table-striped table-hover">';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.trackingDeviceNumber")+'</th>';
			html += '			         <td>'+obj.trackingDeviceNumber+'</td>';
			html += '			     <th>'+$.i18n.prop("monitorTrip.label.containerNumber")+'</th>';
			html += '			         <td>'+obj.containerNumber+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.declarationNumber")+'</th>';
			html += '			         <td>'+obj.declarationNumber+'</td>';
			html += '			     <th>'+$.i18n.prop("monitorTrip.label.trailerNumber")+'</th>';
			html += '			         <td>'+obj.trailerNumber+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.driverName")+'</th>';
			html += '			         <td>'+obj.driverName+'</td>';
			html += '			     <th>'+$.i18n.prop("monitorTrip.label.driverCountry")+'</th>';
			html += '			         <td>'+obj.driverCountry+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.vehicleCountry")+'</th>';
			html += '			         <td>'+obj.vehicleCountry+'</td>';
			html += '			     <th>'+$.i18n.prop("monitorTrip.label.esealNumber")+'</th>';
			html += '			         <td>'+obj.esealNumber+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.sensorNumber")+'</th>';
			html += '			         <td>'+obj.sensorNumber+'</td>';
			html += '			     <th>'+$.i18n.prop("monitorTrip.label.elockStatus")+'</th>';
			if(obj.elockStatus == "1"){
				html += '			         <td>'+$.i18n.prop("monitorTrip.label.elockStatus.lock")+'</td></tr>';
			}else{
				html += '			         <td>'+$.i18n.prop("monitorTrip.label.elockStatus.unlock")+'</td></tr>';
			}
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.FromTo")+'</th>';
			html += '			         <td>'+obj.checkinPortName+' - '+obj.checkoutPortName+'</td><th>'+$.i18n.prop("trip.label.routeId")+'</th> <td>'+obj.routeAreaName+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.locationAttr")+'</th>';
			html += '			         <td colspan="3">'+(parseFloat(newObj.longitude)).toFixed(6)+','+(parseFloat(newObj.latitude)).toFixed(6)+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.checkinTime")+'</th>';
			html += '			         <td>'+formatDateTime(new Date(obj.checkinTime))+'</td>';
			html += '			     <th>'+$.i18n.prop("monitorTrip.label.checkinUser")+'</th>';
			html += '			         <td>'+obj.checkinUserName+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.electricityValue")+'</th>';
			html += '			         <td>'+newObj.electricityValue+'</td>';
			html += '			     <th>'+$.i18n.prop("monitorTrip.label.elockSpeed")+'</th>';
			html += '			         <td>'+newObj.elockSpeed+'</td></tr>';
			
			if(systemModules.isPatrolOn || canAddAlarm()){
				html += '			     <tr><td colspan="4" style="text-align:center;">';
			}
			if(systemModules.isPatrolOn){
				html += "	    			<a onclick=\"vehicleHandlerBtnClick('"+obj.latitude+"','"+obj.longitude+"','"+obj.vehicleId+"')\"  id=\"vehicleHandlerBtn\" style=\"text-decoration:underline\"  href=\"javascript:void(0)\">";
				html +=                 		$.i18n.prop("vehicle.label.track")+"";
				html += '	    			</a>';
			}
			if(canAddAlarm()) {
				html += "	    			<a  id=\"addAlarmBtn\" onclick=\"addAlarmByManual('"+obj.latitude+"','"+obj.longitude+"','"+obj.tripId+"','" + obj.vehiclePlateNumber + "')\" href=\"javascript:void(0)\" style=\"text-decoration:underline;margin-left:15px\">";
				html +=                 		$.i18n.prop("alarm.label.addTitle")+"";
				html += '	     			</a>';
			}
			if(systemModules.isPatrolOn || canAddAlarm()){
				html += '			     </td></tr>';
			}
			html += '		    </tbody></table>';
			html += '	    </div>';
			html += '   </div>';
			html += '</div>';
	 }
	 return html;
 }
 function canAddAlarm() {
	 return ['qualityCenterUser', 'followupUser', 'contromRoomUser', 'riskAnalysisUser', 'patrolManager'].indexOf(roleName) > -1;
 }
 /**
  * 构建巡逻队车辆弹出框
  */
 function createPatrolContent(obj){
	 var html = '';
	 var iconpath = "static/images/ic_08.png";
	 if(obj){
		 var createTime = formatDateTime(new Date(obj.createTime));
			html += '<div class="alert_box">';
			html += '	<div class="alert_box_content">';
			html += '		<div class="alert-title">';
			html += '		    <div class="pull-left">';
			html += '			    <div class="Vehicle_bubble"><img alt="'+ obj.trackUnitNumber+ '" src="'+ iconpath+ '"/></div>';
			html += '				<b>'+obj.trackUnitNumber+'</b>';
			html += '           </div>';
			html += '			<div class="pull-right">'+$.i18n.prop("monitorTrip.label.createTime")+':'+createTime+'</div>';
			html += '		</div>';
			html += '		<div class="alert_table"><tbody>';
			html += '			<table class="table table-condensed table-striped table-hover">';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.potralUser")+'</th>';
			html += '			         <td>'+obj.potralUserName+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.routeAreaName")+'</th>';
			html += '			         <td>'+obj.routeAreaName+'</td></tr>';
			//html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.belongToPortName")+'</th>';
			//html += '			         <td>'+obj.belongToPortName+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.elockSpeed")+'</th>';
			html += '			         <td>'+obj.elockSpeed+'</td></tr>';
			html += '			     <tr><th>'+$.i18n.prop("monitorTrip.label.locationAttr")+'</th>';
			html += '			         <td>'+obj.longitude+','+obj.latitude+'</td></tr>';
			html += '			     <tr><td colspan="2" style="text-align:center;">';
			html += "	    			<a  id=\"centerSenMsgBtn_"+obj.patrolId+"\" href=\"javascript:void(0)\" style=\"text-decoration:underline\">";
			html +=                 	$.i18n.prop("vehicle.label.pushMsg")+"";
			html += '	     			</a>';
			html += '			     </td></tr>';
			html += '		    </tbody></table>';
			html += '	    </div>';
			html += '   </div>';
			html += '</div>';
	 }
	 return html;
 }
// var formatDateTime = function (date) {
//     var y = date.getFullYear();
//     var m = date.getMonth() + 1;
//     m = m < 10 ? ('0' + m) : m;
//     var d = date.getDate();
//     d = d < 10 ? ('0' + d) : d;
//     var h = date.getHours();
//     var minute = date.getMinutes();
//     minute = minute < 10 ? ('0' + minute) : minute;
//     var seconds = date.getSeconds();
//     return y + '-' + m + '-' + d+' '+h+':'+minute+':'+seconds;
// };
 
/**
 * 点击车辆，自动弹出车辆的报警弹出框(根据车辆id查询车辆的报警)
 * @param vehicleId 车辆Id
 * @param show 是否弹出显示，默认为true
 */
function pop(vehicleId, show){
	if(show !== false) {
		show = true;
	}
	$("#alarmListTbody").html("");
	$.ajax({
        type: "POST",
        url: getRootPath() + "vehiclestatus/vehicleAlarmlist.action?vehicleId="+vehicleId,
        cache : false,
		async : false,
        error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
        success: function(obj){
        	obj = JSON.parse(obj);
        	if(obj.success){
        		var alarmData = obj.alarmList;
				if (null!=alarmData && alarmData.length > 0) {
					$("#messagenumber").html(alarmData.length);
					for (var key in alarmData) {
						(function(i){
							var alarmReportVO = alarmData[i];
							var alarmIcon = getAlarmIconByTypeAndLevel(alarmReportVO.alarmTypeId, alarmReportVO.alarmLevelId);
							var alarmTime = alarmReportVO.alarmTime;
							if (typeof alarmReportVO.alarmTime !== 'string') {
					            alarmTime = formatDateTime(new Date(alarmReportVO.alarmTime.time));
					        }
							var areaText = "<tr><td style=\"cursor: pointer;\" id=\"vehicle_" + alarmReportVO.alarmId
									+ "\">"
									+ alarmReportVO.vehiclePlateNumber
									+ "</td><td style=\"text-align:left\"><img src="+alarmIcon+" alt='Alarm_Level_Icon'>"
									+ (alarmReportVO.alarmTypeCode && $.i18n.prop("AlarmType." + alarmReportVO.alarmTypeCode) || '')
									+ "</td><td>"
									+ alarmTime
									+ "</td></tr>"
							$("#alarmListTbody").append(areaText);
							$("#vehicle_"+alarmReportVO.alarmId).on("click", function(){
								alarmIdForRecord = alarmReportVO.alarmId;
								var portUrl = getRootPath() + "vehiclestatus/findVehicleInfoByAlarm.action?tripId="+alarmReportVO.tripId;
								$.ajax({
									type : "POST",
									url : portUrl,
									dataType : "json",
									cache : false,
									async : false,
									error : function(e, message, response) {
										console.log("Status: " + e.status + " message: " + message);
									},
									success : function(jsonObj) {
										if(jsonObj.success){
											if(typeof(jsonObj.vehicleInfoVO.vehicleStatusId)!="undefined"){
												var gpsVehicleInfo = {data:jsonObj.vehicleInfoVO};
							                	getVehiclePaths(gpsVehicleInfo,ALL_POINT);
							                	GisSetHomeCenter({lat:alarmReportVO.alarmLatitude,lng:alarmReportVO.alarmLongitude});
							                	var alarmContent = createAlarmContent(alarmReportVO);
							                     var d = dialog({
							                             id: alarmReportVO.alarmId,
							                             title:jsonObj.vehicleInfoVO.vehiclePlateNumber,//$.i18n.prop('trip.info.message'),
							                             content: alarmContent,
							                             resize:true
							                           });
							                        d.show();
											}
										}else{
											bootbox.alert($.i18n.prop("map.routeArea.select.noVehicleLine"));
										}
									}
								});
			            	});
						})(key);
					}
				}else{
					$("#messagenumber").html("0");
				}
        	}
        }
     });
	
	//弹出右上角的报警信息
	if(show) {
		document.getElementById("a").style.display = "block";
	}
} 
 
 
function findAlarmList(){
	$("#alarmListTbody").html("");
	$.ajax({
        type: "POST",
        url: getRootPath() + "vehiclestatus/alarmlist.action",
        cache : false,
		async : false,
        error : function(e, message, response) {
			console.log("Status: " + e.status + " message: " + message);
		},
        success: function(obj){
        	obj = JSON.parse(obj);
        	if(obj.success){
        		var alarmData = obj.alarmList;
				if (null!=alarmData && alarmData.length > 0) {
					$("#messagenumber").html(alarmData.length);
					for (var key in alarmData) {
						(function(i){
							var alarmReportVO = alarmData[i];
							var alarmIcon = getAlarmIconByTypeAndLevel(alarmReportVO.alarmTypeId, alarmReportVO.alarmLevelId);
							//debugger;formatDateTime(new Date(alarmReportVO.alarmTime.time));
							var alarmTime = alarmReportVO.alarmTime;
							var areaText = "<tr><td style=\"cursor: pointer;\" id=\"vehicle_" + alarmReportVO.alarmId
									+ "\">"
									+ alarmReportVO.vehiclePlateNumber
									+ "</td><td style=\"text-align:left;padding-left:20px;\"><img src="+alarmIcon+" alt='Alarm_Level_Icon'>"
									+ (alarmReportVO.alarmTypeCode && $.i18n.prop("AlarmType." + alarmReportVO.alarmTypeCode) || '')
									+ "</td><td>"
									+ alarmTime
									+ "</td></tr>"
							$("#alarmListTbody").append(areaText);
							$("#vehicle_"+alarmReportVO.alarmId).on("click", function(){
								alarmIdForRecord = alarmReportVO.alarmId;
								var portUrl = getRootPath() + "vehiclestatus/findVehicleInfoByAlarm.action?tripId="+alarmReportVO.tripId;
								$.ajax({
									type : "POST",
									url : portUrl,
									dataType : "json",
									cache : false,
									async : false,
									error : function(e, message, response) {
										console.log("Status: " + e.status + " message: " + message);
									},
									success : function(jsonObj) {
										if(jsonObj.success){
											if(typeof(jsonObj.vehicleInfoVO.vehicleStatusId)!="undefined"){
												var gpsVehicleInfo = {data:jsonObj.vehicleInfoVO};
							                	getVehiclePaths(gpsVehicleInfo,ALL_POINT);
							                	GisSetHomeCenter({lat:alarmReportVO.alarmLatitude,lng:alarmReportVO.alarmLongitude});
							                	var alarmContent = createAlarmContent(alarmReportVO);
							                     var d = dialog({
							                             id: alarmReportVO.alarmId,
							                             title:jsonObj.vehicleInfoVO.vehiclePlateNumber,//$.i18n.prop('trip.info.message'),
							                             content: alarmContent,
							                             resize:true
							                           });
							                        d.show();
											}
										}else{
											bootbox.alert($.i18n.prop("map.routeArea.select.noVehicleLine"));
										}
									}
								});
			            	});
						})(key);
					}
				}else{
					$("#messagenumber").html("0");
				}
        	}
        }
     });
}
 




/**
 * 过滤巡逻队车辆状态
 * @param flag 是否展示巡逻队列表
 */
function findFilterPatrolStatus(rolename) {
 	var portUrl = getRootPath() + "vehiclestatus/findAllPatrolStatus.action?locationType=1&roleName="+rolename;
	//var portUrl = getRootPath() + "patrolMgmt/findAllPatrols.action";
 	
 	$("#panelList").addClass("hidden");
	$("#vehicelTrackList").addClass("hidden");
 
 	$.ajax({
 		type : "POST",
 		url : portUrl,
 		dataType : "json",
 		cache : false,
 		async : false,
 		error : function(e, message, response) {
 			console.log("Status: " + e.status + " message: " + message);
 		},
 		success : function(jsonObj) {
 			$("#vehicelStatusList").html("");
 			$("#vehicelStatusList").removeClass("hidden");
 			var patrolData = jsonObj;
 			if (null!=patrolData && patrolData.rows && patrolData.rows.length > 0) {
 				if(flag){
 					var commonPatrols = patrolData.rows;
 					$("#account_num").html("("+commonPatrols.length+")");
 					//if(menuType=="7"){
 						for (var i = 0; i < commonPatrols.length; i++) {
 							var commonPatrol = commonPatrols[i];
 							
 							if (isNotNull(commonPatrol.patrolId)) {
 								var iconpath = "static/images/ic_08.png";
 								var areaText = "<li><label ><input type=\"checkbox\" "
 										+ "name=\"patrolIds\" value=\""
 										+ commonPatrol.patrolId+"\"><img  alt=\""
 										+ commonPatrol.trackUnitNumber
 										+ "\" src=\""
 										+ iconpath
 										+ "\"/>"
 										+ "<a  onclick=\"getPatrolLocation('"
 										+ commonPatrol.patrolId 
 										+ "','"+commonPatrol.longitude
 										+ "','"+commonPatrol.latitude
 										+ "');\" href='javascript:void(0);' class=\"vehicle_label\">"+ commonPatrol.potralUser+" </a>"
 										+ "</label></li>";
 								$("#vehicelStatusList").append(areaText);
 							}
 						}
 					//}
					
 	 			}
 				//所有巡逻队展示在地图上
 				addPatrolToMap(patrolData.rows);
 			}else{
 				if(flag){
 					$("#account_num").html("(0)");
 				}
 			}
 		}
 	});
 }
