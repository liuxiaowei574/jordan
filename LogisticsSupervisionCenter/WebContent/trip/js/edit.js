var fr, gum, bootstrapValidator, deviceStatus,
	uploadFiles = [], uploadFileNames = [], esealNumberArray = [], sensorNumberArray = [], deletedFileNames = [];

$(function() {
	$('#canvas').hide();

	//本地上传按钮
	$("#btnLocal").on("click", function() {
		$("input[name=tripPhotoLocal]:last").click();
	});
	//本地上传按钮
	$("#photoMenu").on("change", "input[name='tripPhotoLocal']:last", function(e) {
		var files = e.target.files || e.dataTransfer.files;
		files = filterFiles(files);
		if (files && files.length > 0) {
			$('body').animate({scrollTop: $(".wrapper-content").height()}, 400);
			var index = parseInt($(this).data("index"));
			for (var i in files) { //一次只允许上传一张
				uploadFiles.push(files[i]);
				uploadFileNames.push(files[i].name);
				//读取并显示图片
				readImageFile(files[i], index);
			}
			var clone = this.cloneNode(true);
			clone.dataset.index = index + 1;
			$("#btnLocal").parent("li").append(clone);
			setSelectedStatus();
		} else {
			var clone = this.cloneNode(true);
			$("#btnLocal").parent("li").append(clone);
			$(this).remove();
		}
	});

	//删除上传图片的按钮
	$("#row").on("click", "a", function() {
		var index = parseInt($(this).data("index"));
		if (!isNaN(index)) {
			$("input[name='tripPhotoLocal'][data-index='" + index + "']").val('').remove();
			if(this.name) {
				var fileIndex = uploadFileNames.indexOf(this.name);
				if (fileIndex > -1) {
					uploadFiles.splice(fileIndex, 1);
					uploadFileNames.splice(fileIndex, 1);
				}
			}
		}
		$(this).closest(".thumbnail").parent("div").remove();
		setSelectedStatus();
		setPhotoStatus();
		validateFiles();
	});

	//打开摄像头按钮
	$("#btnCamera").on("click", function() {
		if ($("#main").css("display") === 'none') {
			$("#main").show();
			try {
				if (!gum) {
					gum = new GumWrapper({video: 'video'}, showSuccess, showError);
				}
				openCamera();
			} catch (err) {
				//判断是否Safari浏览器
				ifSafiri();
			}
		}
	});
	//关闭摄像头按钮
	$("#btnCameraClose").on("click", function() {
		closeCamera();
	});
	
	//自动读取追踪终端号按钮
	$("#btnGetDeviceNum").on("click", function(){
		readElockNum();
	});
	$("#tripVehicleVO\\.trackingDeviceNumber").on("blur", function(){
		this.value = $.trim(this.value);
		if(this.value) {
			checkElockNum(this.value);
		}
	});
	//自动读取子锁号按钮
	$("#btnGetEsealNum").on("click", function(){
		//最多添加6个
		if(esealNumberArray.length === 6) {
			bootbox.alert($.i18n.prop("trip.info.esealNumber.maximum"));
			return false;
		}
		readEsealNum();
	});
	//手动添加子锁号按钮
	$("#btnAddEsealNum").on("click", function(){
		//最多添加6个
		if(esealNumberArray.length === 6) {
			bootbox.alert($.i18n.prop("trip.info.esealNumber.maximum"));
			return;
		}
		if($.trim($("#esealNumberInput").val())) {
			checkEsealNumber($("#esealNumberInput").val());
		}
	});
	//删除子锁号按钮
	$(".esealNumber-list").on("click", ".glyphicon", function(){
		$(this).closest("li").remove();
		var index = esealNumberArray.indexOf(String($(this).parent().prev("span").data("ori-value")));
		if(index > -1) {
			esealNumberArray.splice(index, 1);
		}
		$("#tripVehicleVO\\.esealNumber").val(esealNumberArray.join());
		
		validateEsealNumber();
	});
	//自动读取传感器编号按钮
	$("#btnGetSensorNum").on("click", function(){
		//最多添加6个
		if(sensorNumberArray.length === 6) {
			bootbox.alert($.i18n.prop("trip.info.sensorNumber.maximum"));
			return false;
		}
		readSensorNum();
	});
	//手动添加传感器编号按钮
	$("#btnAddSensorNum").on("click", function(){
		//最多添加6个
		if(sensorNumberArray.length === 6) {
			bootbox.alert($.i18n.prop("trip.info.sensorNumber.maximum"));
			return;
		}
		if($.trim($("#sensorNumberInput").val())){
			checkSensorNumber($("#sensorNumberInput").val());
		}
	});
	//删除传感器编号按钮
	$(".sensorNumber-list").on("click", ".glyphicon", function(){
		$(this).parents("li").remove();
		var index = sensorNumberArray.indexOf(String($(this).parent().prev("span").data("ori-value")));
		if(index > -1) {
			sensorNumberArray.splice(index, 1);
		}
		$("#tripVehicleVO\\.sensorNumber").val(sensorNumberArray.join());
		
		validateSensorNumber();
	});
	//获取报关单号按钮
	$("#btnGetDecNum").on("click", function(){
		//以下为模拟数据
		showTripInfo({
			"tripVehicleVO": {
				"declarationNumber": "222520131250168837",
				"vehiclePlateNumber": "316A",
				"trailerNumber": "319A",
				"vehicleCountry": "Canada",
				"driverName": "James Bond",
				"driverCountry": "Singapore",
				"containerNumber": "MSKU0383250"
				//"routeId": "1"
			}
		});
		validateTripInfos();
	});
	//选择路线时，显示行程耗时
	$("#tripVehicleVO\\.routeId").on("change", function(){
		var timecost = $("#tripVehicleVO\\.routeId option:selected").data("timecost");
		$("div#timeCost").html(convertUnit(timecost));
	});
	//点击保存按钮
	$("#btnSave").on("click", function(){
		validateEsealNumber();
		validateSensorNumber();
	});
	 //添加关锁模态框事件
    $('#addElockModal').on('loaded.bs.modal', function(e) {
		$(this).modal('show');
	});
	//模态框登录判断
	$('#addElockModal').on('show.bs.modal', function(e) {
		var content = $(this).find(".modal-content").html();
		needLogin(content);
	});
	//添加子锁模态框事件
	$('#addEsealModal').on('loaded.bs.modal', function(e) {
		$(this).modal('show');
	});
	//模态框登录判断
	$('#addEsealModal').on('show.bs.modal', function(e) {
		var content = $(this).find(".modal-content").html();
		needLogin(content);
	});
	//添加传感器模态框事件
	$('#addSensorModal').on('loaded.bs.modal', function(e) {
		$(this).modal('show');
	});
	//模态框登录判断
	$('#addSensorModal').on('show.bs.modal', function(e) {
		var content = $(this).find(".modal-content").html();
		needLogin(content);
	});
	
	//查询设备状态：定位、通讯、电量等
	getDeviceStatus();
	//查询当前用户所属的检入口岸
	getCheckinPort();
	//查询当前用户所在国家的检出口岸
	getCheckoutPort();
	//查询所有路线
	getRoute();
	//获取子锁号
	getEsealNum($("#tripVehicleVO\\.esealNumber").val());
	//获取传感器编号
	getSensorNum($("#tripVehicleVO\\.sensorNumber").val());
	
	//添加表单验证
	bootstrapValidatorForm();
});
/**
 * 获取子锁号
 * @param data
 */
function getEsealNum(data){
	if(Object.prototype.toString.call(data) === '[object String]' && typeof(data) === 'string') {
		data = $.trim(data).split(/\s*,\s*/);
	}
//	console.log(data);
	if($.isArray(data)) {
		$(".esealNumber-list").children("ul").empty();//.end().prev("div").hide();
		for(var i = 0, len = data.length; i < len; i++){
			!!data[i] && appendEsealNums(data[i]);
		}
	}
}
/**
 * 获取传感器编号
 * @param data
 */
function getSensorNum(data){
	if(Object.prototype.toString.call(data) === '[object String]' && typeof(data) === 'string') {
		data = $.trim(data).split(/\s*,\s*/);
	}
	if($.isArray(data)) {
		$(".sensorNumber-list").children("ul").empty();//.end().prev("div").hide();
		for(var i = 0, len = data.length; i < len; i++){
			!!data[i] && appendSensorNums(data[i]);
		}
	}
}
/**
 * 刷新电量
 */
function refreshVoltage(voltage){
	var value = transferVVV(voltage);
	if(new RegExp(/^\d+%$/).test(value)){
		$(".percentage").html(value);
		$("#dianliang").animate({"width": value}, 'normal');
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
			html += $.i18n.prop('trip.info.center.noPass', userInfo.userName, ids[3]);
			bootbox.alert(html, function() {
				bootstrapValidator.disableSubmitButtons(false);
			});
		}
	});
}
/**
 * 时间格式转换
 */
function convertUnit(time) {
	if(!time) {
		return '';
	}
	//所有支持语言的相关单位，来源谷歌地图
	var originMinute = ['分钟,分', 'min,mins', 'قيقة'];//中文、英文、阿拉伯语
	var originHour = ['小时,时', 'hour,hours', 'اعة'];
	var originDay = ['天', 'day,days', 'يوم '];
	
	var unitMinute = unitHour = unitDay = [];
	originMinute.forEach(function(value,index){
	  unitMinute = unitMinute.concat(value.split(/\s*,\s*/));
	});
	originHour.forEach(function(value,index){
	  unitHour = unitHour.concat(value.split(/\s*,\s*/));
	});
	originDay.forEach(function(value,index){
	  unitDay = unitDay.concat(value.split(/\s*,\s*/));
	});
	var patternMinute = '(' + unitMinute.join('|') + ')';
	var patternHour = '(' + unitHour.join('|') + ')';
	var patternDay = '(' + unitDay.join('|') + ')';
	var result = '';
	if (new RegExp('(\\d*)\\s*' + patternDay).test(time)) {
		result += (result && ' ') + new RegExp('(\\d*)\\s*' + patternDay).exec(time)[1] + $.i18n.prop('unit.timecost.day');
	}
	if (new RegExp('(\\d*)\\s*' + patternHour).test(time)) {
		result += (result && ' ') + new RegExp('(\\d*)\\s*' + patternHour).exec(time)[1] + $.i18n.prop('unit.timecost.hour');
	}
	if (new RegExp('(\\d*)\\s*' + patternMinute).test(time)) {
		result += (result && ' ') + new RegExp('(\\d*)\\s*' + patternMinute).exec(time)[1] + $.i18n.prop('unit.timecost.minute');
	}
//	console.log('%s --> %s', time, result);
	return result;
}
/**
 * 读取图片并显示
 * @param {Object} file 上传的图片
 * @param {Object} index 索引号
 */
function readImageFile(file, index){
	var reader = new FileReader();
	reader.onload = (function(f) {
		return function(e) {
			var html = '';
			html += '<div class="col-sm-6 col-md-3">';
			html += '	<div class="thumbnail">';
			html += '		<img src="' + e.target.result + '" name="localImage" />';
			html += '		<div class="caption">';
			html += '			<p title="' + f.name + '">';
			html += '				<span>' + f.name + '</span>';
			html += '				<a href="javascript:void(0);" class="delete" title="' + $.i18n.prop("trip.activate.button.delete") +'" name="' + f.name + '" data-index="' + index + '">';
			html += '					<span class="glyphicon glyphicon-trash"></span>';
			html += '				</a>';
			html += '			</p>';
			html += '		</div>';
			html += '	</div>';
			html += '</div>';
			$("#collapseTwo .row").append(html);
			$("div.file-help-block").hide();
		};
	})(file);
	reader.readAsDataURL(file);
}
/**
 * 读取拍摄的照片
 * @param {Object} b64 照片的Base64编码
 */
function readPhoto(b64) {
	var html = '';
	html += '<div class="col-sm-6 col-md-3">';
	html += '	<div class="thumbnail">';
	html += '		<img src="' + b64 + '" />';
	html += '		<input type="hidden" name="tripCameraBase64" value="' + b64.substring(22) + '" />';
	html += '		<div class="caption">';
	html += '			<p>';
	html += '				<a href="javascript:void(0);" class="delete" title="' + $.i18n.prop("trip.activate.button.delete") +'" name="" data-index="">';
	html += '					<span class="glyphicon glyphicon-trash"></span>';
	html += '				</a>';
	html += '			</p>';
	html += '		</div>';
	html += '	</div>';
	html += '</div>';
	$("#collapseTwo .row").append(html);
	$("div.file-help-block").hide();
}
/**
 * 初始化Jquery i18n
 */
function initJqueryI18n(){
	jQuery.i18n.properties({//加载资浏览器语言对应的资源文件
	    name : 'LocalizationResource_center', //资源文件名称
	    path : _getRootPath() + "/i18n/", //资源文件路径
	    mode : 'map', //用Map的方式使用资源文件中的值
	    language :language,
	    callback : function() {
	    	//alert($.i18n.prop("common.message.form.validator"))
	    }
	});
}
/**
 * 增加设备号到显示列表
 * @param {Object} num
 */
function appendElockNums(num){
	$("#tripVehicleVO\\.trackingDeviceNumber").val(num);
}
/**
 * 增加子锁号到显示列表
 * @param {Object} num
 */
function appendEsealNums(num){
	var li = '';
	li += '<li>';
	li += '	<span data-ori-value="' + num + '" title="' + num + '">';
	li += num;
	li += '	</span>';
	li += '	<a href="javascript:void(0);" class="delete" title="' + $.i18n.prop("trip.activate.button.delete") +'">';
	li += '		<span class="glyphicon glyphicon-trash"></span>';
	li += '	</a>';
	li += '</li>';
	$(".esealNumber-list").find("ul").append(li);
	esealNumberArray.push(num);
	$("#tripVehicleVO\\.esealNumber").val(esealNumberArray.join());
}
/**
 * 增加传感器编号到显示列表
 * @param {Object} num
 */
function appendSensorNums(num){
	var li = '';
	li += '<li>';
	li += '	<span data-ori-value="' + num + '" title="' + num + '">';
	li += num;
	li += '	</span>';
	li += '	<a href="javascript:void(0);" class="delete" title="' + $.i18n.prop("trip.activate.button.delete") +'">';
	li += '		<span class="glyphicon glyphicon-trash"></span>';
	li += '	</a>';
	li += '</li>';
	$(".sensorNumber-list").find("ul").append(li);
	sensorNumberArray.push(num);
	$("#tripVehicleVO\\.sensorNumber").val(sensorNumberArray.join());
}
/**
 * 查询设备状态：定位、通讯、电量等
 */
function getDeviceStatus(){
	var status = {
		'0': 'glyphicon-remove status-error',
		'1': 'glyphicon-ok status-ok'
	};
	//以下为模拟数据
	var data = {
		'elockLocation': '1',
		'elockCommuicate': '1',
		'elockInArea': '1'
		//'elockInArea': '' + parseInt(Math.random() * 10) % 2
	};
	deviceStatus = data;
	if(Object.keys(status).indexOf(deviceStatus.elockLocation) > -1) {
		$("#elockLocation").addClass(status[deviceStatus.elockLocation]);
	}
	if(Object.keys(status).indexOf(deviceStatus.elockCommuicate) > -1) {
		$("#elockCommuicate").addClass(status[deviceStatus.elockCommuicate]);
	}
	if(Object.keys(status).indexOf(deviceStatus.elockInArea) > -1) {
		$("#elockInArea").addClass(status[deviceStatus.elockInArea]);
	}
}
/**
 * 查询当前用户所属的检入口岸
 */
function getCheckinPort(){
	$.get(root + "/deptMgmt/findPortByUserId.action", {}, function(data){
		if(data && data.portId) {
			$("#tripVehicleVO\\.checkinPort").val(data.portId);
			$("#tripVehicleVO\\.checkinPortName").val(data.portName);
		}
	}, 'json');
}
/**
 * 查询所有检出口岸
 */
function getCheckoutPort(){
	$.get(root + "/deptMgmt/findAllPortByUserId.action", {}, function(data){
		var $checkoutPort = $("#tripVehicleVO\\.checkoutPort");
		$checkoutPort.empty().append('<option value=""></option>');
		if(data && data.total > 0) {
			var rows = data.rows;
			$.each(rows, function(index, item){
				$checkoutPort.append("<option value='" + item.organizationId + "' " + (checkoutPort == item.organizationId ? "selected='selected'" : "") + ">" + item.organizationName + "</option>");
			});
		}
	}, 'json');
}
/**
 * 查询所有路线
 */
function getRoute(){
	$.get(root + "/monitorroutearea/findAllRouteAreas.action", {ids: '0'}, function(data){
		var $routeId = $("#tripVehicleVO\\.routeId");
		$routeId.empty().append('<option value=""></option>');
		if(data && data.length > 0) {
			$.each(data, function(index, item){
				$routeId.append("<option value='" + item.routeAreaId + "' " + (routeId == item.routeAreaId ? "selected='selected'" : "") + " data-timecost='" + item.routeCost + "'>" + item.routeAreaName + "</option>");
			});
			var timecost = $("#tripVehicleVO\\.routeId option:selected").data("timecost");
			$("div#timeCost").html(convertUnit(timecost));
		}
	}, 'json');
}
/**
 * 设置选择文件的信息
 */
function setSelectedStatus() {
	var size = 0;
	var num = uploadFiles.length;
	$.each(uploadFiles, function(index, element) {
		// 计算得到文件总大小
		size += element.size;
	});
	// 转换为KB或MB
	size = formatSize(size);
	// 设置内容
	$("#collapseTwo span[name='info-upload']").html($.i18n.prop("trip.activate.info.selectFiles", num, size));
}

/**
 * 设置拍摄照片的信息
 */
function setPhotoStatus() {
	var size = 0,
		$cameras = $("input[name=tripCameraBase64]");
	var num = $cameras.length;
	$cameras.each(function(index, element) {
		// 计算得到Base64总长度
		size += element.value.length;
	});
	// 转换为KB或MB
	size = formatSize(size);
	// 设置内容
	$("#collapseTwo span[name='info-camera']").html($.i18n.prop("trip.activate.info.cameraPhotos", num, size));
}
/**
 * 将文件容量转换为KB或MB
 * @param {Object} size
 */
function formatSize(size) {
	if (size > 1024 * 1024) {
		size = (Math.round(size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
	} else {
		size = (Math.round(size * 100 / 1024) / 100).toString() + 'KB';
	}
	return size;
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
 * 打开摄像头
 */
function openCamera() {
	gum.play();
}

/**
 * 关闭摄像头
 */
function closeCamera() {
	$("#main").hide();
	$("#btnSnap").off();
	gum.stop();
}

/**
 * 展示行程信息
 * @param {Object} data
 */
function showTripInfo(data) {
	drillProps(data, '', function(obj, objName) {
		$("#" + objName.replace(/\./g, '\\\.')).val(obj);
	});
	getRisk();
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
 * 根据车牌号、司机姓名查询对应的风险指数，并计算风险值。
 */
function getRisk(){
	$.get(root + "/analysis/getRisk.action", {"s_driverName": $("#tripVehicleVO\\.driverName").val(), "s_vehiclePlateNumber": $("#tripVehicleVO\\.vehiclePlateNumber").val()}, function(data){
		if(data) {
			var vehiclePlateNumRisk = driverNameRisk = 0;
			var riskParams = data.riskParams;
			//根据车牌计算结果
			if(data.lprArr && data.lprArr.length > 0) {
				vehiclePlateNumRisk = data.lprArr[0] && calcRisk(data.lprArr[0], riskParams) || 0;
			}
			//根据司机姓名计算结果
			if(data.driverArr && data.driverArr.length > 0) {
				driverNameRisk = data.driverArr[0] && calcRisk(data.driverArr[0], riskParams) || 0;
			}
//			console.log('vehiclePlateNumRisk: %s, driverNameRisk: %s', vehiclePlateNumRisk, driverNameRisk);
			var result = analyzeRisk(vehiclePlateNumRisk, driverNameRisk);
			$("input[name=tripVehicleVO\\.riskStatus]").removeAttr("checked");
			$("input[name=tripVehicleVO\\.riskStatus][value=" + result + "]")[0].checked = true;
		}
	}, 'json');
}
/**
 * 根据车牌号风险值和司机姓名风险值，分析最终风险值。
 * 规则：暂时按照车牌号风险值作为最终值
 * @param vehiclePlateNumRisk 车牌号风险值
 * @param driverNameRisk 司机姓名风险值
 */
function analyzeRisk(vehiclePlateNumRisk, driverNameRisk){
	return vehiclePlateNumRisk;
}
function calcRisk(rowObj, riskParams){
	var redNum = 0;
	var greenNum = 0;
	var yellowNum = 0;
	var tripGreenThreshold = parseFloat(riskParams["RISK_ALARM_TRIP_PERCENT_YELLOW"]);
	var tripYellowThreshold = parseFloat(riskParams["RISK_ALARM_TRIP_PERCENT_RED"]);
	var tripSeriousGreenThreshold = parseFloat(riskParams["RISK_SERIOUS_ALARM_TRIP_PERCENT_YELLOW"]);
	var tripSeriousYellowThreshold = parseFloat(riskParams["RISK_SERIOUS_ALARM_TRIP_PERCENT_RED"]);
	var tripNormalGreenThreshold = parseFloat(riskParams["RISK_NORMAL_ALARM_TRIP_PERCENT_YELLOW"]);
	var tripNormalYellowThreshold = parseFloat(riskParams["RISK_NORMAL_ALARM_TRIP_PERCENT_RED"]);
	var seriousNumberGreenThreshold = parseFloat(riskParams["RISK_SERIOUS_ALARM_NUMBER_PERCENT_YELLOW"]);
	var seriousNumberYellowThreshold = parseFloat(riskParams["RISK_SERIOUS_ALARM_NUMBER_PERCENT_RED"]);
	var normalNumberGreenThreshold = parseFloat(riskParams["RISK_NORMAL_ALARM_NUMBER_PERCENT_YELLOW"]);
	var normalNumberYellowThreshold = parseFloat(riskParams["RISK_NORMAL_ALARM_NUMBER_PERCENT_RED"]);
	var finalGreenThreshold = parseFloat(riskParams["RISK_FINAL_YELLOW"]);
	var finalYellowThreshold = parseFloat(riskParams["RISK_FINAL_RED"]);
	var greenPercent = finalGreenThreshold;
	var yellowPercent = finalYellowThreshold - finalGreenThreshold;	
	var redPercent = 100- finalYellowThreshold;
	
	var tripPercent = 100*rowObj['tripAlarmTotalNum']/rowObj['tripTotalNum'];
	if(tripPercent>tripYellowThreshold){
		redNum++;
	}else if(tripPercent>tripGreenThreshold){
		yellowNum++;
	}else{
		greenNum++;
	}
	var tripSeriousPercent = 100*rowObj['tripSeriousAlarmTotalNum']/rowObj['tripTotalNum'];
	if(tripSeriousPercent>tripSeriousYellowThreshold){
		redNum++;
	}else if(tripSeriousPercent>tripSeriousGreenThreshold){
		yellowNum++;
	}else{
		greenNum++;
	}
	var tripNormalPercent = 100*rowObj['tripMinorAlarmTotalNum']/rowObj['tripTotalNum'];
	if(tripNormalPercent>tripNormalYellowThreshold){
		redNum++;
	}else if(tripNormalPercent>tripNormalGreenThreshold){
		yellowNum++;
	}else{
		greenNum++;
	}

	var seriousNumberPercent = 100*rowObj['seriousAlarmTotalNum']/(rowObj['seriousAlarmTotalNum']+rowObj['minorAlarmTotalNum']);
	if(seriousNumberPercent>tripNormalYellowThreshold){
		redNum++;
	}else if(seriousNumberPercent>tripNormalGreenThreshold){
		yellowNum++;
	}else{
		greenNum++;
	}
	var normalNumberPercent = 100*rowObj['minorAlarmTotalNum']/(rowObj['seriousAlarmTotalNum']+rowObj['minorAlarmTotalNum']);
	if(normalNumberPercent>normalNumberYellowThreshold){
		redNum++;
	}else if(normalNumberPercent>normalNumberGreenThreshold){
		yellowNum++;
	}else{
		greenNum++;
	}
	
	var redNumPercent = 100* redNum/(redNum+greenNum+yellowNum);
	var yellowNumPercent = 100* yellowNum/(redNum+greenNum+yellowNum);
	var greenNumPercent = 100* greenNum/(redNum+greenNum+yellowNum);
	var value = 0;
	if(greenNumPercent>=greenPercent){
		value += 0;
	}else{
		value += greenPercent-greenNumPercent;
	}
	if(yellowNumPercent>=yellowPercent){
		value += yellowPercent;
	}else{
		value += yellowNumPercent;
	}
	if(redNumPercent>=redPercent){
		value += redPercent;
	}else{
		value += redNumPercent;
	}
	
	/*
	if(value > finalYellowThreshold){
		return '<div class="safebar redbg"></div>';
	}else if(value>finalGreenThreshold){
		return '<div class="safebar yellowbg"></div>';
	}else{
		return '<div class="safebar greenbg"></div>';
	}
	*/
	if(value > finalYellowThreshold){
		return 2; //高（red）
	}else if(value>finalGreenThreshold){
		return 1; //中（yellow）
	}else{
		return 0; //低（green）
	}
	
}
/**
 * Safiri
 */
function ifSafiri() {
	try {
		var userAgent = navigator.userAgent;
		if (userAgent.indexOf("Safari") > -1 && userAgent.indexOf("Oupeng") === -1 && userAgent.indexOf("360 Aphone") === -1) {
			var sel = document.getElementById('fileselect'); // get reference to file select input element
			window.addEventListener("DOMContentLoaded", function() {
				var errBack = function(error) {
					if (error.PERMISSION_DENIED) {
						bootbox.alert('用户拒绝了浏览器请求媒体的权限');
					} else if (error.NOT_SUPPORTED_ERROR) {
						bootbox.alert('对不起，您的浏览器不支持拍照功能，请使用其他浏览器');
					} else if (error.MANDATORY_UNSATISFIED_ERROR) {
						bootbox.alert('指定的媒体类型未接收到媒体流');
					} else {
						bootbox.alert('系统未能获取到摄像头，请确保摄像头已正确安装。或尝试刷新页面，重试');
					}
				};
				// Put video listeners into place
				sel.addEventListener('change', function(e) {
					var f = sel.files[0]; // get selected file (camera capture)
					fr = new FileReader();
					fr.onload = receivedData; // add onload event
					fr.readAsDataURL(f); // get captured image as data URI
				});
				$('#imgtag').show();
				$('.div_video').hide();
				$('#btnSnap').click(function() {
					sel.click();
				});
			}, false);
		}
	} catch (e) {

	}
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
 * 摄像头调用成功回调
 * @param {Object} video
 */
function showSuccess(video) {
	$('body').animate({scrollTop: $(".wrapper-content").height()}, 600);
	$("#btnSnap").off().on('click', function() {
		var canvas = document.getElementById("canvas"), context = canvas.getContext("2d");
		$('#cream_loading').toggle();
		context.drawImage(video, 0, 0, 640, 480);
		convertCanvasToImage(canvas);
		setPhotoStatus();
	});
}
/**
 * 摄像头调用失败回调
 * @param {Object} error
 */
function showError(error) {
	bootbox.alert('Error: ' + error.message, function() {  
        closeCamera();
    });
}
/**
 * for iOS 
 * create file reader
 */
function receivedData() {
	// readAsDataURL is finished - add URI to IMG tag src
	var imgtag = document.getElementById('imgtag'); // get reference to img tag
	imgtag.src = fr.result;
	$('#cream_loading').toggle();
	try {
		setTimeout(function() {
			var canvas = document.getElementById("canvas"), context = canvas.getContext("2d");
			context.drawImage(imgtag, 0, 0, 640, 480);
			convertCanvasToImage(canvas);
		}, 500);
	} catch (err) {
		bootbox.alert(err);
	}
}
/**
 * 帆布转换成图像并保存图片
 * @param {Object} canvas
 */
function convertCanvasToImage(canvas) {
	var image = new Image();
	image.src = canvas.toDataURL("image/png");
	//字符串前有22位提示信息“data:image/png;base64”
	//var b64 = image.src.substring(22);
	var b64 = image.src;
	//读取拍摄的照片
	readPhoto(b64);
	$('#cream_loading').toggle();
	return image;
}
/**
 * 校验追踪终端号
 */
function validateTrackingDeviceNum(){
	bootstrapValidator
				.updateStatus('tripVehicleVO.trackingDeviceNumber', 'NOT_VALIDATED')
				.validateField('tripVehicleVO.trackingDeviceNumber')
				.disableSubmitButtons(false);
}
/**
 * 校验各行程信息字段
 */
function validateTripInfos(){
	bootstrapValidator
			.updateStatus('tripVehicleVO.declarationNumber', 'NOT_VALIDATED')
			.updateStatus('tripVehicleVO.vehiclePlateNumber', 'NOT_VALIDATED')
			.updateStatus('tripVehicleVO.trailerNumber', 'NOT_VALIDATED')
			.updateStatus('tripVehicleVO.vehicleCountry', 'NOT_VALIDATED')
			.updateStatus('tripVehicleVO.driverName', 'NOT_VALIDATED')
			.updateStatus('tripVehicleVO.driverCountry', 'NOT_VALIDATED')
			.updateStatus('tripVehicleVO.containerNumber', 'NOT_VALIDATED')
//			.updateStatus('tripVehicleVO.routeId', 'NOT_VALIDATED')
//			.validate()
			.disableSubmitButtons(false);
}
/**
 * 校验设备状态
 */
function validateDeviceStatus(){
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
	if(deviceStatus.elockInArea != '1') {
		bootbox.alert($.i18n.prop("trip.info.inArea.not"), function(){
			bootstrapValidator.disableSubmitButtons(false);
		});
		return false;
	}
	return true;
}
/**
 * 校验设备号是否属于当前口岸，状态是否正常
 * @param num 用户输入或读取的号码
 * @param isRead 是否从设备读取的
 * @returns {Boolean}
 */
function checkElockNum(num, isRead){
	num = $.trim(num);
	$.get(root + "/warehouseElock/findByElockNumber.action", {"elockNumber": num}, function(data){
		var readMsg = $.i18n.prop("trip.info.elockNumber.read", num);
		//判断设备号是否存在
		if(!data || !data.warehouseElockBO) {
			var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.elockNumber.notExist");
			bootbox.confirm(msg, function(result){
				if(result) {
					var url = root + "/warehouseElock/addModal.action?fromTrip=1&elockNumber=" + num + "&checkinPort=" + $("#tripVehicleVO\\.checkinPort").val();
					$('#addElockModal').removeData('bs.modal');
					$('#addElockModal').modal({
						remote : url,
						show : false,
						backdrop : 'static',
						keyboard : false
					}).modal('show');
				}else{
					$("#tripVehicleVO\\.trackingDeviceNumber").val('');
					validateTrackingDeviceNum();
				}
			});
			return false;
		}
		/*
		//判断设备是否属于当前口岸
		if(data.warehouseElockBO.belongTo != data.organizationId) {
			var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.elockNumber.notBelong");
			bootbox.alert(msg);
			return false;
		}
		//判断设备状态是否为在途或正常
		if(data.warehouseElockBO.elockStatus == '2') {
			var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.elockNumber.statusInWay");
			bootbox.alert(msg, function(){
			});
			return false;
		}else if(data.warehouseElockBO.elockStatus != '1') {
			var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.elockNumber.statusInvalid");
			bootbox.alert(msg);
			return false;
		}
		*/
		appendElockNums(num);
		validateTrackingDeviceNum();
	}, 'json');
}
/**
 * 校验文件
 */
function validateFiles(){
	if(uploadFiles.length < 1 && $("input[name=tripCameraBase64]").length < 1) {
		$("div.file-help-block").show();
		bootstrapValidator.disableSubmitButtons(false);
		$('body').animate({scrollTop: $(".wrapper-content").height()}, 400);
		return false;
	}
	return true;
}
/**
 * 更新子锁号校验状态
 */
function validateEsealNumber(){
	if(esealNumberArray.length < 1) {
		bootstrapValidator.updateStatus('esealNumberInput', 'INVALID').disableSubmitButtons(false);
		return false;
	}else{
		bootstrapValidator.updateStatus('esealNumberInput', 'VALID');
		return true;
	}
}
/**
 * 校验子锁号是否属于当前口岸，状态是否正常等
 * @param num 用户输入或读取的号码
 * @param isRead 是否从设备读取的
 * @returns {Boolean}
 */
function checkEsealNumber(num, isRead){
	num = $.trim(num);
	if(esealNumberArray.indexOf(num) > -1) {
		bootbox.alert($.i18n.prop("trip.info.esealNumber.added"));
		return false;
	}
	$.get(root + "/esealMgmt/findByEsealNumber.action", {"esealNumber": num}, function(data){
		var readMsg = $.i18n.prop("trip.info.esealNumber.read", num);
		//判断子锁号是否存在
		if(!data || !data.warehouseEsealBO) {
			var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.esealNumber.notExist");
			bootbox.confirm(msg, function(result){
				if(result) {
					var url = root + "/esealMgmt/addModal.action?fromTrip=1&esealNumber=" + num + "&checkinPort=" + $("#tripVehicleVO\\.checkinPort").val();
					$('#addEsealModal').removeData('bs.modal');
					$('#addEsealModal').modal({
						remote : url,
						show : false,
						backdrop : 'static',
						keyboard : false
					});
				}
			});
			return false;
		}
		/*
		//判断子锁号是否属于当前口岸
		if(data.warehouseEsealBO.belongTo != data.organizationId) {
			var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.esealNumber.notBelong");
			bootbox.alert(msg);
			return false;
		}
		//判断子锁号是否在途或正常
		if(data.warehouseEsealBO.esealStatus == '2') {
			var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.esealNumber.statusInWay");
			bootbox.alert(msg, function(){
			});
			return false;
		}else if(data.warehouseEsealBO.esealStatus != '1') {
			var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.esealNumber.statusInvalid");
			bootbox.alert(msg);
			return false;
		}
		*/
		appendEsealNums(num);
		validateEsealNumber();
		!isRead && $("#esealNumberInput").val('');
	}, 'json');
}
/**
 * 更新传感器号校验状态
 */
function validateSensorNumber(){
	if(sensorNumberArray.length < 1) {
		bootstrapValidator.updateStatus('sensorNumberInput', 'INVALID').disableSubmitButtons(false);
		return false;
	}else{
		bootstrapValidator.updateStatus('sensorNumberInput', 'VALID');
		return true;
	}
}
/**
 * 校验传感器号是否存在，是否属于当前口岸，状态是否正常等
 * @param num 用户输入或读取的号码
 * @param isRead 是否从设备读取的
 * @returns {Boolean}
 */
function checkSensorNumber(num, isRead){
	num = $.trim(num);
	if(sensorNumberArray.indexOf(num) > -1) {
		bootbox.alert($.i18n.prop("trip.info.sensorNumber.added"));
		return false;
	}
	$.get(root + "/sensorMgmt/findBySensorNumber.action", {"sensorNumber": num}, function(data){
		var readMsg = $.i18n.prop("trip.info.sensorNumber.read", num);
		//判断传感器号是否存在
		if(!data || !data.warehouseSensorBO) {
			var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.sensorNumber.notExist");
			bootbox.confirm(msg, function(result){
				if(result) {
					var url = root + "/sensorMgmt/addModal.action?fromTrip=1&sensorNumber=" + num + "&checkinPort=" + $("#tripVehicleVO\\.checkinPort").val();
					$('#addSensorModal').removeData('bs.modal');
					$('#addSensorModal').modal({
						remote : url,
						show : false,
						backdrop : 'static',
						keyboard : false
					});
				}
			});
			return false;
		}
		/*
		//判断传感器号是否属于当前口岸
		if(data.warehouseSensorBO.belongTo != data.organizationId) {
			var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.sensorNumber.notBelong");
			bootbox.alert(msg);
			return false;
		}
		//判断传感器号状态是否在途或正常
		if(data.warehouseSensorBO.sensorStatus == '2') {
			var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.sensorNumber.statusInWay");
			bootbox.alert(msg, function(){
			});
			return false;
		}else if(data.warehouseSensorBO.sensorStatus != '1') {
			var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.sensorNumber.statusInvalid");
			bootbox.alert(msg, function(){
			});
			return false;
		}
		*/
		appendSensorNums(num);
		validateSensorNumber();
		!isRead && $("#sensorNumberInput").val('');
	}, 'json');
}
/**
 * 为esealOrder赋值
 */
function setEsealOrder(){
	var temp = new Array(6);
	$.each(temp, function(index, value){
		temp[index] = esealNumberArray[index] || '0';
	});
	$("#tripVehicleVO\\.esealOrder").val(temp.join());
}
/**
 * 为sensorOrder赋值
 */
function setSensorOrder(){
	var temp = new Array(6);
	$.each(temp, function(index, value){
		temp[index] = sensorNumberArray[index] || '0';
	});
	$("#tripVehicleVO\\.sensorOrder").val(temp.join());
}
/**
 * 添加表单验证
 */
function bootstrapValidatorForm() {
	$('#tripForm').bootstrapValidator({
		fields: {
			'tripVehicleVO.trackingDeviceNumber': {
				validators: {
					notEmpty: {},
					stringLength: {
						max: 50
					}
				}
			},
			'esealNumberInput': {
				validators: {
					callback: {
						message: $.fn.bootstrapValidator.i18n.notEmpty["default"],
	                    callback: function(fieldValue, validator, $field) {
	                    	$field.val($.trim(fieldValue));
	                        var length = esealNumberArray.length;
	                        if ($.trim(fieldValue) == '' && length < 1) {
	                            validator.updateStatus('esealNumberInput', 'INVALID');
	                            validateEsealNumber();
	                            return false;
	                        } else {
	                            return true;
	                        }
	                    }
					}
				}
			},
			'sensorNumberInput': {
				validators: {
					callback: {
						message: $.fn.bootstrapValidator.i18n.notEmpty["default"],
	                    callback: function(fieldValue, validator, $field) {
	                    	$field.val($.trim(fieldValue));
	                        var length = sensorNumberArray.length;
	                        if ($.trim(fieldValue) == '' && length < 1) {
	                            validator.updateStatus('sensorNumberInput', 'INVALID');
	                            validateSensorNumber();
	                            return false;
	                        } else {
	                            return true;
	                        }
	                    }
					}
				}
			},
			'tripVehicleVO.declarationNumber': {
				validators: {
					notEmpty: {},
					stringLength: {
						max: 50
					}
				}
			},
			'tripVehicleVO.vehiclePlateNumber': {
				validators: {
					notEmpty: {},
					stringLength: {
						max: 50
					}
				}
			},
			'tripVehicleVO.trailerNumber': {
				validators: {
					notEmpty: {},
					stringLength: {
						max: 100
					}
				}
			},
			'tripVehicleVO.vehicleCountry': {
				validators: {
					notEmpty: {},
					stringLength: {
						max: 100
					}
				}
			},
			'tripVehicleVO.driverName': {
				validators: {
					notEmpty: {},
					stringLength: {
						max: 100
					}
				}
			},
			'tripVehicleVO.driverCountry': {
				validators: {
					notEmpty: {},
					stringLength: {
						max: 100
					}
				}
			},
			'tripVehicleVO.containerNumber': {
				validators: {
					notEmpty: {},
					stringLength: {
						max: 50
					}
				}
			},
			'tripVehicleVO.checkinPort': {
				validators: {
					notEmpty: {},
					stringLength: {
						max: 50
					}
				}
			},
			'tripVehicleVO.checkoutPort': {
				validators: {
					notEmpty: {},
					stringLength: {
						max: 50
					}
				}
			},
			'tripVehicleVO.routeId': {
				validators: {
					notEmpty: {}
				}
			}
		}
	}).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
        // Prevent form submission
        e.preventDefault();

        // Get the form instance
        var $form = $(e.target);

        // Get the BootstrapValidator instance
        var bv = $form.data('bootstrapValidator');

        // Use Ajax to submit form data
        /*
        $.post($form.attr('action'), $form.serialize(), function(result) {
            console.log(result);
        }, 'json');
        */
        
		//为子锁号顺序、传感器号顺序赋值：
		setEsealOrder();
		setSensorOrder();
		if(validateEsealNumber() && validateSensorNumber() && validateDeviceStatus()) {
        	if($(".thumbnail").length > 0 || validateFiles()) {
				var formData = new FormData($form[0]);
				$.ajax({
					url: $form[0].action,
					type: 'POST',
					contentType: false,
					data: formData,
					dataType: 'JSON',
					processData: false,
					success: function(result) {
						try {
							if (result && result.result) {
								bootbox.success($.i18n.prop("trip.edit.success"), function(){
									window.location.href = root + "/monitortrip/toList.action";
								});
							} else if (result.message) {
								bootbox.alert($.i18n.prop("trip.edit.failed") + ":" + result.message);
							}
						} catch (e) {}
					}
				});
        	}
		}
    });
	bootstrapValidator = $('#tripForm').data('bootstrapValidator');
}