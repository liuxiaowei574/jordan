var fr, gum, bootstrapValidator, deviceStatus,
	uploadFiles = [], uploadFileNames = [], esealNumberArray = [], sensorNumberArray = [], globalVehicle = {}, vehicleIndex = -1, fileIndexVehicleNumMap = {}, photoIndexVehicleNumMap = {}, refreshTimeout = {};
ejs.open = '{{';
ejs.close = '}}';
$(function() {
	$('#canvas').hide();

	//本地上传按钮
	$("#myTabContent").delegate('#btnLocal', "click", function() {
		$("input[name=tripPhotoLocal]:last").click();
	});
	//本地上传按钮
	$("#myTabContent").delegate("input[name='tripPhotoLocal']:last", "change", function(e) {
		vehicleIndex = $(this).closest(".tab-pane").index();
		var files = e.target.files || e.dataTransfer.files;
		files = filterFiles(files);
		if (files && files.length > 0) {
			//$('body').animate({scrollTop: $(".wrapper-content").height()}, 400);
			var index = parseInt($(this).data("index"));
			for (var i in files) { //一次只允许上传一张
				//读取并显示图片
				fileIndexVehicleNumMap[index] =  $(this).closest(".tab-pane").attr("id"); // 记住当前这张图片和车牌号的对应关系
				readImageFile(files[i], index);
				uploadFiles.push(files[i]);
				uploadFileNames.push(files[i].name);
			}
			var clone = this.cloneNode(true);
			clone.dataset.index = index + 1;
			$("#btnLocal").parent("li").append(clone);
		} else {
			var clone = this.cloneNode(true);
			$("#btnLocal").parent("li").append(clone);
			$(this).remove();
		}
	});
	
	//删除上传图片
	$("#myTabContent").on("click", "a.delete_image", function() {
		var $carousel = $(this).closest(".carousel");
		var index = $(this).closest(".item").index();
		var name = $(this).attr("name");
		$carousel.carousel('pause');
		$carousel.find("li").eq(index).remove();
		$(this).closest(".item").remove();
		$carousel.find("li").each(function(i){
			$(this).data("slideTo", i).attr("data-slide-to", i);
		});
		$carousel.find("li:eq(0), .item:eq(0)").addClass("active");
		
		var dataIndex = $(this).data("index");
		if(/photo$/.test(dataIndex)) {
			delete photoIndexVehicleNumMap[dataIndex.slice(0, -5)];
		} else {
			var fileInputIndex = parseInt(dataIndex);
			if (!isNaN(fileInputIndex) && fileInputIndex > -1) {
				$("input[name='tripPhotoLocal'][data-index='" + fileInputIndex + "']").val('').remove();
				delete fileIndexVehicleNumMap[fileInputIndex];
				if(name) {
					var fileIndex = uploadFileNames.indexOf(name);
					if (fileIndex > -1) {
						uploadFiles.splice(fileIndex, 1);
						uploadFileNames.splice(fileIndex, 1);
					}
				}
			}
		}
		validateFiles();
	});

	//打开摄像头按钮
	$("#myTabContent").delegate('#btnCamera', "click", function() {
		$('#cameraModal').removeData('bs.modal');
		$('#cameraModal').modal({
			remote : root + "/monitortrip/toCamera.action",
			show : false,
			backdrop : 'static',
			keyboard : false
		});
	});
	
	//自动读取追踪终端号按钮
	$("#btnGetDeviceNum").on("click", function(){
		readElockNum();
	});
	//手动输入追踪终端号按钮
	$("#s_trackingDeviceNumber").on("blur", function(){
		this.value = $.trim(this.value);
	});
	$("#s_trackingDeviceNumber").on("change", function(){
		$.ajax({
			url : root + '/monitortrip/queryByDeviceNum.action',
			type : "get",
			dataType : "json",
			data : {
				's_trackingDeviceNumber' : $.trim(this.value)
			},
			success : function(data) {
				data.total && data.total > 0 && $.isFunction(callback) && callback(data.rows[0]);
			}
		});
	});
	//获取报关单号按钮
	$("#btnGetDecNum").on("click", function(){
		//以下为模拟数据
//		var data = '222520131250168837';
//		$("#s_declarationNumber").val(data);
		/*
		showTripInfo({
			"tripVehicleVO": {
				"declarationNumber": "222520131250168837",
				"vehiclePlateNumber": "316A",
				"trailerNumber": "319A",
				"vehicleCountry": "Canada",
				"driverName": "James Bond",
				"driverCountry": "Singapore",
				"containerNumber": "MSKU0383250"
			}
		});
		*/
		//扫描条码
		$('#scanModal').removeData('bs.modal');
		$('#scanModal').modal({
			remote : root + "/monitortrip/toScan.action",
			show : false,
			backdrop : 'static',
			keyboard : false
		});
	});
	$("#btnReset").on("click", function(){
		$("#s_declarationNumber, #s_trackingDeviceNumber").val('');
	});
	$("#btnFinishDo").on("click", function(){
		$("#btnFinish").click();
	});
	//点击行程结束按钮
	$("#btnFinish").on("click", function(){
		$("#tripVehicleVO\\.specialFlag").val('0');
		$("#tripVehicleVO\\.reason").val('');
		setFileIndexVehicleNumMap();
		$("#tripForm").attr("action", root +"/monitortrip/finish.action").submit();
	});
	
	$("#checkinPicturesDiv, #checkoutPicturesDiv").on("click", "img", function(){
		var html = '';
		if($(this).closest("#checkoutPicturesDiv").length > 0) {
			var $item = $(this).closest(".item");
			var index = $item.index();
			var fileName = $item.children("a").attr("name");
			var fileindex = $item.children("a").attr("fileindex");
			html = '<a href="javascript:void(0);" class="delete" title="' + $.i18n.prop('trip.activate.button.delete') + '" name="'+ fileName + '" data-index="' + index + '" data-fileindex="' + fileindex + '" style="position: absolute; left: 50%; bottom: 0%; z-index: 2; transform: translate(-50%, -50%);">';
			html += '	<span class="glyphicon glyphicon-remove btn-lg" style="font-size: 23px;"></span>';
			html += '</a>';
		}
		$("#imageModal .modal-content").html(html).append($(this).clone());
		$('#imageModal').modal({
			show : false,
			backdrop: true, 
			keyboard: true
		}).modal('show');
		
		$('#imageModal img').on("click", function(){
			$("#imageModal img").empty();
			$('#imageModal').modal("hide");
		});
	});
	
	$("#route").on("click", function(){
		var url = root + "/vehicletrack/getParamToVehicleTrack.action?routeAreaId=" + $(this).data("routeid") + "&tripId=" + $(this).data("tripid");
		$('#routeModal').removeData('bs.modal');
		$('#routeModal').modal({
			remote : url,
			show : false,
			backdrop: 'static', 
			keyboard: false
		});
	});
	$('#routeModal').on('loaded.bs.modal', function(e) {
		$(this).modal('show');
	});
	//模态框登录判断
	$('#routeModal').on('show.bs.modal', function(e) {
		var content = $(this).find(".modal-content").html();
		needLogin(content);
	});
	//扫描条码模态框事件
    $('#scanModal').on('loaded.bs.modal', function(e) {
		$(this).modal('show');
	}).on('show.bs.modal', function(e) {
		var content = $(this).find(".modal-content").html();
		needLogin(content);
	});
    //拍照模态框事件
    $('#cameraModal').on('loaded.bs.modal', function(e) {
		$(this).modal('show');
		readPhoto = readPhotoFinish;
	}).on('show.bs.modal', function(e) {
		var content = $(this).find(".modal-content").html();
		needLogin(content);
	});
	//施封
	$("button[name=btnSetLocked]").on("click", function(){
		var trackingDeviceNumber = $(this).parents(".form-group").find("p[name=trackingDeviceNumber]").text();
		if(trackingDeviceNumber) {
			//为了不让websocket通信异常影响后续操作，此处应进行try、catch
			try{
				elockUtil.setElockNo(trackingDeviceNumber);
				setLocked();
			}catch(e){}
			elockLog('setLocked', trackingDeviceNumber);
		}
	});
	//解封
	$("button[name=btnSetUnlocked]").on("click", function(){
		var trackingDeviceNumber = $(this).parents(".form-group").find("p[name=trackingDeviceNumber]").text();
		if(trackingDeviceNumber) {
			//为了不让websocket通信异常影响后续操作，此处应进行try、catch
			try{
				elockUtil.setElockNo(trackingDeviceNumber);
				setUnlocked();
			}catch(e){}
			elockLog('setUnlocked', trackingDeviceNumber);
		}
	});
	//解除报警
	$("button[name=btnClearAlarm]").on("click", function(){
		var trackingDeviceNumber = $(this).parents(".form-group").find("p[name=trackingDeviceNumber]").text();
		if(trackingDeviceNumber) {
			//为了不让websocket通信异常影响后续操作，此处应进行try、catch
			try{
				elockUtil.setElockNo(trackingDeviceNumber);
				clearAlarm();
			}catch(e){}
			elockLog('clearAlarm', trackingDeviceNumber);
		}
	});
	//关闭读取条形码
    $("#btnCloseBarcode").on("click", function(e) {
        e.preventDefault();
        Quagga.stop();
        $("#barcodeView").hide();
    });
    //特殊申请
    $("#btnFinishSpecial").on("click", function(){
    	if($("#tripVehicleVO\\.tripId").val() != '') {
    		bootbox.confirm($.i18n.prop('trip.info.sure.finish.special'), function(result){
    			if(result) {
    				$('#reasonModal').removeData('bs.modal');
    				$('#reasonModal').modal({
    					backdrop: 'static', 
    					keyboard: false
    				});
    			}
    		});
    	}
    });
	$('#reasonModal').on('loaded.bs.modal', function(e) {
		$(this).modal('show');
	}).on('hidden.bs.modal', function () {
		$("#specialReason").val('');
	});
	$("#reasonModal").on("click", "#nopassSubmit", function(){
		$("#tripVehicleVO\\.specialFlag").val('1');
		$("#tripVehicleVO\\.reason").val($("#specialReason").val() || '');
		$("#tripForm").attr("action", root +"/monitortrip/finish.action").submit();
	});
    $("#myTab").delegate("li", "click", function(){
		var index = $(this).index();
		$("#myTabContent .vehicle-img").eq(index).append($(".upload"));
	});
  //搜索匹配
	$("#s_trackingDeviceNumber").bsSuggest({
        allowNoKeyword: true,   //是否允许无关键字时请求数据。为 false 则无输入时不执行过滤请求
        getDataMethod: "url",    //获取数据的方式，总是从 URL 获取
        url: root + "/monitortrip/queryByDeviceNum.action?s_trackingDeviceNumber=" + $.trim(this.value),
        effectiveFields: ["number"],
        searchFields: [ "number"],
        ignorecase: true,
        listStyle: {
            'max-height': '300px',
        },
        showBtn: false,     //不显示下拉按钮
        idField: "number",
        keyField: "number"
    }).on('onDataRequestSuccess', function (e, result) {
        //console.log('onDataRequestSuccess: ', result);
    }).on('onSetSelectValue', function (e, keyword, data) {
        //console.log('onSetSelectValue: ', keyword, data);
    }).on('onUnsetSelectValue', function () {
        //console.log("onUnsetSelectValue");
    });

	//查询设备状态：定位、通讯、电量等
	getDeviceStatus();
	//定时查询设备状态：定位、通讯、电量等
	//queryElockStatus();
	//添加表单验证
	bootstrapValidatorForm();
	setGlobalVehicle();
});
/**
 * 刷新电量
 */
function refreshVoltage(voltage){
	var value = transferVVV(voltage) || '0%';
	if(new RegExp(/^\d+%$/).test(value)){
		$("#myTabContent .percentage").html(value);
		$("#myTabContent div[name=dianliang]").animate({"width": value}, 'normal');
	}
}
/**
 * 电量值设定
 */
function transferVVV(vvv){
	if (null != vvv && "" != vvv&&(vvv+"").indexOf(".")<1) {
		var v = parseInt(vvv);

		if (v >= 300 && v < 345)
			return "0%";
		else if (v >= 345 && v < 368)
			return "5%";
		else if (v >= 368 && v < 374)
			return "10%";
		else if (v >= 374 && v < 377)
			return "20%";
		else if (v >= 377 && v < 379)
			return "30%";
		else if (v >= 379 && v < 382)
			return "40%";
		else if (v >= 382 && v < 387)
			return "50%";
		else if (v >= 387 && v < 392)
			return "60%";
		else if (v >= 392 && v < 398)
			return "70%";
		else if (v >= 398 && v < 406)
			return "80%";
		else if (v >= 406 && v < 420)
			return "90%";
		else if (v >= 420 && v < 430 )
			return "100%";
		else
			return "ERROR";
	}else{
		return "";
	}
}
/**
 * 加载WebSocket的行程结果通知
 * @param userId
 */
function loadTripResult(data) {
	var receiveUsers = data.receiveUser;
	var receiveUserArray = receiveUsers.split(",");
	$.each(receiveUserArray, function(i,value){  
	  if(typeof sessionUserId != 'undefined' && sessionUserId == value) {//接收人有自己弹出框
		  $("#msgModal").modal("hide");
		  parseTripResult(data);
	  }
	}); 
}
/**
 * 解析行程结果通知
 * @param data
 */
function parseTripResult(data){
	var ids = data.content.split(",");
	getUserInfo(ids[2], function(userInfo){
		var html = '';
		if(ids[1] == '1') {
			html += $.i18n.prop('trip.info.center.pass', userInfo.userName);
			bootbox.success(html, function() {  
				location.href = location.href.replace(/#$/, '');
			});
		}else if(ids[1] == '2') {
			html += $.i18n.prop('trip.info.center.noPass', userInfo.userName, ids[3] || '');
			bootbox.alert(html, function() {
				bootstrapValidator.disableSubmitButtons(false);
			});
		}
	});
}
/**
 * 通过参数查询行程信息
 * @param {Object} params
 * @param {Object} callback
 */
function getTripInfo(params, callback) {
	$.get(root + "/monitortrip/findOneTripVehicleAlarm.action", params, function(data){
		if(!needLogin(data)) {
			if(data && !!data.tripVehicleVO) {
				callback(data);
			}else{
				bootbox.alert($.i18n.prop("trip.info.trip.notFound", 
						params.s_trackingDeviceNumber || '', 
						params.s_declarationNumber || ''));
				return;
			}
		}
	}, 'json');
}

/**
 * 展示行程信息
 * @param {Object} data
 */
function showTripInfo(data) {
	//展示基本信息
	drillProps(data, '', function(obj, objName) {
		if(objName == 'tripVehicleVO.goodsType') {
			if(!!obj && obj.length > 0) {
				var map = obj.split(/\s*,\s*/).map(function(v, i){
					  return $.i18n.prop('GoodsType.' + v) || '';
				});
				$('#tripVehicleVO\\.goodsType').text(map.join()).val(map.join());
			}
		} else {
			$('#' + objName.replace(/\./g, '\\\.')).text(obj).val(obj);
		}
	});
	
	var commonVehicleDriverList = data.tripVehicleVO.commonVehicleDriverList;
	if(commonVehicleDriverList && commonVehicleDriverList.length > 0) {
		var tabLi = $("#tabLi").html();
		var html = ejs.render(tabLi, data.tripVehicleVO);
		$("#myTab").html(html);
		
//		commonVehicleDriverList.forEach(function(value, index){
//			value.checkinPicture = value.checkinPicture && value.checkinPicture.split(/\s*,\s*/);
//		});
		var vehicleInfo = $("#vehicleInfo").html();
		html = ejs.render(vehicleInfo, data.tripVehicleVO);
		$("#myTabContent").append(html);
		
		$(".vehicle-img:eq(0)").append($("#upload"));
	}
	
	//展示报警信息
	showAlarmInfo(data.alarmList);
	//更新“查看轨迹路线”链接
	updateRoute(data.tripVehicleVO);
	
	$("#tripVehicleVO\\.checkoutTime, #tripVehicleVO\\.timeCost").text('');
	$("#tripVehicleVO\\.checkoutUserName").text(sessionUserName);
}
/**
 * 获取对象属性和对象值，执行指定操作
 * @param {Object} obj
 * @param {Object} objName
 * @param {Object} callback
 */
function drillProps(obj, objName, callback) {
	if (Object.prototype.toString.call(obj) === '[object String]') {
		callback(obj, objName);
	} else if (Object.prototype.toString.call(obj) === '[object Object]') {
		for (var k in obj) {
			if(obj.hasOwnProperty(k)) {
				drillProps(obj[k], (objName && (objName + ".")) + k, callback);
			}
		}
	}
}
/**
 * 展示报警信息
 * @param alarmList 报警信息列表
 */
function showAlarmInfo(alarmList){
	var statusName = {
			'0': $.i18n.prop("alarm.label.alarmStatus.notProcessed"),
			'1': $.i18n.prop("alarm.label.alarmStatus.processing"),
			'2': $.i18n.prop("alarm.label.alarmStatus.processed")
	};
	if(alarmList && alarmList.length > 0) {
		var html = [];
		for(var i = 0, len = alarmList.length; i < len;  i++){
			html.push('<tr>');
			html.push('	<td>' + (i + 1) + '</td>');
			html.push('	<td>' + alarmList[i].alarmTime + '</td>');
			html.push('	<td>' + alarmList[i].receiveTime + '</td>');
			html.push('	<td>' + alarmList[i].userName + '</td>');
			html.push('	<td>' + alarmList[i].alarmLongitude + '</td>');
			html.push('	<td>' + alarmList[i].alarmLatitude + '</td>');
			html.push('	<td>' + statusName[alarmList[i].alarmStatus] + '</td>');
			html.push('	<td>' + alarmList[i].alarmLevelName + '</td>');
			html.push('	<td>' + alarmList[i].alarmTypeName + '</td>');
			html.push('</tr>');
		}
		$("#alarmTable tbody").html(html.join(''));
	}
}
/**
 * 更新“查看轨迹路线”链接
 * @param tripVehicleVO
 */
function updateRoute(tripVehicleVO){
	$("#route").data("tripid", tripVehicleVO.tripId).data("routeid", tripVehicleVO.routeId).show();
}
/**
 * 读取图片并显示
 * @param {Object} file 上传的图片
 * @param {Object} index 索引号
 */
function readImageFile(file, index){
	var slideToIndex = $("#checkoutPictures>.carousel-indicators li").length;
	var reader = new FileReader();
	reader.onload = (function(f) {
		return function(e) {
			var $tabPane = $(".tab-pane.active");
			var $carousel = $tabPane.find(".carousel").eq(0);
			var id = $carousel.attr("id");
			$carousel.find("li, div").removeClass("active");
			var i = $carousel.children(".carousel-indicators").children("li").length;
			$carousel.children(".carousel-indicators").append('<li data-target="#' + id + '" data-slide-to="' + i + '" class="active"></li>');
			var image = {
					src: e.target.result,
					name: f.name,
					index: index
			};
			var html = ejs.render($("#imageItem").html(), image);
			$carousel.children(".carousel-inner").append(html);
			$("div.file-help-block").hide();
		};
	})(file);
	reader.readAsDataURL(file);
}
//以下为获取关锁前模拟数据
var deviceData = {
	'elockLocation': '1',
	'elockCommuicate': '1',
	'elockInArea': '1'
	//'elockInArea': '' + parseInt(Math.random() * 10) % 2
};
/**
 * 查询设备状态：定位、通讯、电量等
 */
function getDeviceStatus(){
	var status = {
		'0': 'glyphicon-remove status-error',
		'1': 'glyphicon-ok status-ok'
	};
	deviceStatus = deviceData;
	if(Object.keys(status).indexOf(deviceStatus.elockLocation) > -1) {
		$("span[name=elockLocation]").addClass(status[deviceStatus.elockLocation]);
	}
	if(Object.keys(status).indexOf(deviceStatus.elockCommuicate) > -1) {
		$("span[name=elockCommuicate]").addClass(status[deviceStatus.elockCommuicate]);
	}
	if(Object.keys(status).indexOf(deviceStatus.elockInArea) > -1) {
		$("span[name=elockInArea]").addClass(status[deviceStatus.elockInArea]);
	}
}
/**
 * 过滤选择的文件
 * @param {Object} files
 */
function filterFiles(files) {
	return $.grep(files, function(element, index) {
		return element && uploadFileNames.indexOf(element.name) < 0;
	});
}
/**
 * 获取文件名
 * @param {Object} files
 */
function getFileNames(files) {
	return $.map(files, function(element, index) {
		return element.name;
	});
}
/**
 * 过滤文本框首尾空白
 */
function trimText() {
	$("input[type=text]").each(function() {
		$(this).val($.trim($(this).val()));
	});
}
/**
 * 校验设备状态
 */
function validateDeviceStatus(){
	/*
	if(deviceStatus.elockLocation != '1') {
		bootbox.alert($.i18n.prop("trip.info.location.invalid"), function(){
			bootstrapValidator.disableSubmitButtons(false);
		});
		return false;
	}
	if(deviceStatus.elockCommuicate != '1') {
		bootbox.alert($.i18n.prop("trip.info.communication.invalid"), function(){
			bootstrapValidator.disableSubmitButtons(false);
		});
		return false;
	}
	*/
	if(deviceStatus.elockInArea != '1') {
		bootbox.alert($.i18n.prop("trip.info.inArea.not"), function(){
			bootstrapValidator.disableSubmitButtons(false);
		});
		return false;
	}
	return true;
}
/**
 * 校验文件
 */
function validateFiles(){
	/*
	if(uploadFiles.length < 1 && $("input[name=tripCameraBase64]").length < 1) {
		$("div.file-help-block").show();
		bootstrapValidator.disableSubmitButtons(false);
		$('body').animate({scrollTop: $(".wrapper-content").height()}, 400);
		return false;
	}
	*/
	return true;
}
/**
 * 添加表单验证
 */
function bootstrapValidatorForm() {
	$('#tripForm').bootstrapValidator({
		fields: {
		}
	}).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
        // Prevent form submission
        e.preventDefault();

        // Get the form instance
        var $form = $(e.target);

        // Get the BootstrapValidator instance
        var bv = $form.data('bootstrapValidator');

        // Use Ajax to submit form data
        var specialFlag = $("#tripVehicleVO\\.specialFlag").val();
        var validateFlag = false;
        if(specialFlag == '1') {
        	validateFlag = true;
        }else{
        	validateFlag = validateDeviceStatus() && validateFiles();
        }
        setFileIndexVehicleNumMap();
    	if(validateFlag && $("#tripVehicleVO\\.tripId").val() != '') {
    		var formData = new FormData($form[0]);
    		
    		if(systemModules.isApprovalOn) {
				//弹出等待审批的模态框
				$("#msgModal").modal({
					backdrop: 'static', 
					keyboard: false
				}).modal('show');
			}
    		
    		$.ajax({
    			url: $form[0].action,
    			type: 'POST',
    			contentType: false,
    			data: formData,
    			dataType: 'JSON',
    			processData: false,
    			success: function(result) {
    				if(!needLogin(result)) {
        				try {
        					if (result && result.result) {
        						if(!systemModules.isApprovalOn) {
        							bootbox.success($.i18n.prop("trip.finish.success"), function() {  
        								location.href = location.href.replace(/#$/, '');
        							});
        						}
        					} else if (result.message) {
        						bootbox.alert($.i18n.prop("trip.finish.failed") + ":" + result.message);
        						bootstrapValidator.disableSubmitButtons(false);
        					}
        				} catch (e) {}
    				}
    			},
    			error: function (XMLHttpRequest, textStatus, errorThrown) {
    				//this; // 调用本次AJAX请求时传递的options参数
    				console.error(textStatus || errorThrown);
    			}
    		});
    	}
    });
	bootstrapValidator = $('#tripForm').data('bootstrapValidator');
}

/**
 * 查询关锁是否已经生成status表数据
 */
function queryElockStatus(){
 	refreshDeviceInfo();
    function refreshDeviceInfo(){
    	var trackingDeviceNumber = $("#s_trackingDeviceNumber").val();
    	if(typeof(trackingDeviceNumber)!=undefined && trackingDeviceNumber!=null && trackingDeviceNumber!=""){
    		var portUrl = _getRootPath() + "/vehiclestatus/findDeviceNumber.action?trackingDeviceNumber="+trackingDeviceNumber;
    		$.ajax({
    			type : "POST",
    			url : portUrl,
    			dataType : "json",
    			cache : false,
    			async : true,
    			error : function(e, message, response) {
    				console.log("Status: " + e.status + " message: " + message);
    			},
    			success : function(obj) {
    				obj.success = true; //演示
    				if(obj.success){
    					deviceData = {
							'elockLocation': '1',
							'elockCommuicate': '1',
							'elockInArea': '1'
							//'elockInArea': '' + parseInt(Math.random() * 10) % 2
						};
    					deviceStatus = deviceData;
						$("#myTabContent span[name=elockInArea]").removeClass('glyphicon-remove status-error').addClass('glyphicon-ok status-ok');
						if(obj.lsMonitorVehicleStatusBO && !!parseInt(obj.lsMonitorVehicleStatusBO.electricityValue)){
							refreshVoltage(parseInt(obj.lsMonitorVehicleStatusBO.electricityValue));
						}
    				}else{
    					deviceData = {
							'elockLocation': '1',
							'elockCommuicate': '1',
							'elockInArea': '0'
						};
    					deviceStatus = deviceData;
    					$("#myTabContent span[name=elockInArea]").removeClass('glyphicon-ok status-ok').addClass('glyphicon-remove status-error');
						
    					refreshVoltage(0);
    				}
    			}
    		});
    	}
    	refreshTimeoutValue = setTimeout(function(){
			refreshDeviceInfo();
		},8000);
	}
}
/**
 * 关锁操作日志记录
 * @param type 操作类型
 * @param trackingDeviceNumber 关锁号
 */
function elockLog(type, trackingDeviceNumber) {
	$.get(root + "/monitortrip/elockLog.action", {"type": type, "trackingDeviceNumber": trackingDeviceNumber}, function(data){
		if(!needLogin(data)) {
			if(data) {
				console.log(type, trackingDeviceNumber);
			}
		}
	}, 'json');
}
function setFileIndexVehicleNumMap(){
	if(Object.keys(fileIndexVehicleNumMap).length > 0) {
		$("#fileIndexVehicleNumMap").val(JSON.stringify(fileIndexVehicleNumMap));
	}
	if(Object.keys(photoIndexVehicleNumMap).length > 0) {
		$("#photoIndexVehicleNumMap").val(JSON.stringify(photoIndexVehicleNumMap));
	}
}
/**
 * 取消所有定时
 */
function clearAllTimeout() {
	for (var i in refreshTimeout) {
		clearTimeout(refreshTimeout[i]);
		delete refreshTimeout[i];
	}
}
/**
 * 查询所有关锁号实时状态信息
 */
function queryAllElockStatus(){
	for(var i in globalVehicle) {
		if(i != 'editingId') {
			queryElockStatus(globalVehicle[i].trackingDeviceNumber);
		}
	}
}
function setGlobalVehicle(){
	$("#myTabContent>.tab-pane").each(function(){
		var id = $(this).attr("id");
		globalVehicle[String(id)] = {
				trackingDeviceNumber: $(this).find("[name=trackingDeviceNumber]").text(),
				vehiclePlateNumber: id
		};
	});
}
/**
 * 读取拍摄的照片
 * @param {Object} b64 照片的Base64编码
 */
function readPhotoFinish(b64) {
	var $tabPane = $(".tab-pane.active");
	var $carousel = $tabPane.find(".carousel").eq(0);
	var id = $carousel.attr("id");
	$carousel.find("li, div").removeClass("active");
	var i = $carousel.children(".carousel-indicators").children("li").length;
	var liHtml = '';
	liHtml += '<li data-target="#' + id + '" data-slide-to="' + i + '" class="active">';
	liHtml += '<input type="hidden" name="tripCameraBase64" value="' + b64.substring(22) + '" />';
	liHtml += '</li>';
	$carousel.children(".carousel-indicators").append(liHtml);
	var image = {
			src: b64,
			name: uuid(),
			index: Object.keys(photoIndexVehicleNumMap).length + 'photo'
	};
	var html = ejs.render($("#imageItem").html(), image);
	$carousel.children(".carousel-inner").append(html);
	$("div.file-help-block").hide();
}