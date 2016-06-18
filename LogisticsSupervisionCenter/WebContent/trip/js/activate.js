var fr, gum, bootstrapValidator, deviceStatus,
	uploadFiles = [], uploadFileNames = [], esealNumberArray = [], sensorNumberArray = [];
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
	
	//获取追踪终端号按钮
	$("#btnGetDeviceNum").on("click", function(){
		/*
		$.get(root + "/...", {}, function(data){
			$("#tripVehicleVO\\.trackingDeviceNumber").val(data);
			validateTrackingDeviceNum();
		}, 'json');
		*/
		//以下为模拟数据
		var num = '111';
		
		//后台校验设备是否属于当前口岸，状态是否正常
		$.get(root + "/warehouseElock/findByElockNumber.action", {"elockNumber": num}, function(data){
			if(checkDevice(data, num, true)){
				appendElockNums(num);
				validateTrackingDeviceNum();
			}
		}, 'json');
	});
	//手动添加子锁号按钮
	$("#btnAddEsealNum").on("click", function(){
		var $esealNumberInput = $("#esealNumberInput");
		var num = $.trim($esealNumberInput.val());
		$esealNumberInput.val(num);
		if(!num){
			return;
		}
		//最多添加6个
		if(esealNumberArray.length === 6) {
			bootbox.alert($.i18n.prop("trip.info.esealNumber.maximum"));
			return;
		}
		if(esealNumberArray.indexOf(num) > -1) {
			bootbox.alert($.i18n.prop("trip.info.esealNumber.added"));
			return;
		}
		
		//后台校验子锁是否属于当前口岸，状态是否正常
		$.get(root + "/esealMgmt/findByEsealNumber.action", {"esealNumber": num}, function(data){
			if(checkEsealNumber(data, num)){
				appendEsealNums(num);
				validateEsealNumber();
				$esealNumberInput.val('');
			}
		}, 'json');
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
	//获取子锁号按钮
	$("#btnGetEsealNum").on("click", function(){
		//最多添加6个
		if(esealNumberArray.length === 6) {
			bootbox.alert($.i18n.prop("trip.info.esealNumber.maximum"));
			return false;
		}
		/*
		$.get(root + "/...", {}, function(data){
			if(data){
				for(var i = 0, len = data.length; i < len; i++){
					appendEsealNums(data[i]);
				}
				validateEsealNumber();
			}
		}, 'json');
		*/
		//以下为模拟数据
		var num = 'LBA-000SD-1009AOAODDC';
		if(esealNumberArray.indexOf(num) > -1) {
			bootbox.alert($.i18n.prop("trip.info.esealNumber.added"));
			return;
		}
		//后台校验子锁是否属于当前口岸，状态是否正常
		$.get(root + "/esealMgmt/findByEsealNumber.action", {"esealNumber": num}, function(data){
			if(checkEsealNumber(data, num, true)){
				appendEsealNums(num);
				validateEsealNumber();
			}
		}, 'json');
	});
	//手动添加传感器编号按钮
	$("#btnAddSensorNum").on("click", function(){
		var $sensorNumberInput = $("#sensorNumberInput");
		var num = $.trim($sensorNumberInput.val());
		$sensorNumberInput.val(num);
		if(!num){
			return;
		}
		//最多添加6个
		if(sensorNumberArray.length === 6) {
			bootbox.alert($.i18n.prop("trip.info.sensorNumber.maximum"));
			return;
		}
		if(sensorNumberArray.indexOf(num) > -1) {
			bootbox.alert($.i18n.prop("trip.info.sensorNumber.added"));
			return;
		}
		//后台校验传感器是否存在，是否属于当前口岸，状态是否正常
		$.get(root + "/sensorMgmt/findBySensorNumber.action", {"sensorNumber": num}, function(data){
			if(checkSensorNumber(data, num)){
				appendSensorNums(num);
				validateSensorNumber();
				$sensorNumberInput.val('');
			}
		}, 'json');
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
	//获取传感器编号按钮
	$("#btnGetSensorNum").on("click", function(){
		//最多添加6个
		if(sensorNumberArray.length === 6) {
			bootbox.alert($.i18n.prop("trip.info.sensorNumber.maximum"));
			return false;
		}
		/*
		$.get(root + "/...", {}, function(data){
			if(data){
				for(var i = 0, len = data.length; i < len; i++){
					appendSensorNums(data[i]);
				}
				validateSensorNumber();
			}
		}, 'json');
		*/
		//以下为模拟数据
		var num = 'E-XA000C';
		if(sensorNumberArray.indexOf(num) > -1) {
			bootbox.alert($.i18n.prop("trip.info.sensorNumber.added"));
			return;
		}
		//后台校验传感器是否存在，属于当前口岸，状态是否正常
		$.get(root + "/sensorMgmt/findBySensorNumber.action", {"sensorNumber": num}, function(data){
			if(checkSensorNumber(data, num, true)){
				appendSensorNums(num);
				validateSensorNumber();
			}
		}, 'json');
	});
	//获取报关单号按钮
	$("#btnGetDecNum").on("click", function(){
		/*
		$.get(root + "/...", {}, function(data){
			//json
			showTripInfo(data);
			validateTripInfos();
		}, 'json');
		*/
		//以下为模拟数据
		showTripInfo({
			"tripVehicleVO": {
				"declarationNumber": "222520131250168837",
				"vehiclePlateNumber": "316A",
				"trailerNumber": "319A",
				"vehicleCountry": "Canada",
				"driverName": "James Bond",
				"driverCountry": "Singapore",
				"containerNumber": "MSKU0383250",
				"routeId": "1"
			}
		});
		validateTripInfos();
	});
	//点击行程激活按钮
	$("#btnActivate").on("click", function(){
		validateEsealNumber();
		validateSensorNumber();
		//validateFiles();
	});

	//查询设备状态：定位、通讯、电量等
	getDeviceStatus();
	//查询当前用户所属的检入口岸
	getCheckinPort();
	//查询当前用户所在国家的检出口岸
	getCheckoutPort();
	//查询所有路线
	getRoute();
	//初始化Jquery i18n
	initJqueryI18n();
	//添加表单验证
	bootstrapValidatorForm();
});
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
//			html += '				<img class="upload-delele" title="' + $.i18n.prop("trip.activate.button.delete") + '" name="' + f.name + '" data-index="' + index + '" src="' + root + '/static/images/dele.png"/>';
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
//	html += '				<img class="upload-delele" title="' + $.i18n.prop("trip.activate.button.delete") + '" name="' + '" data-index="" src="' + root + '/static/images/dele.png"/>';
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
	/*
	$.get(root + "/...", {}, function(data){
		if(data) {
			var status = {
				'0': 'glyphicon-remove status-error',
				'1': 'glyphicon-ok status-ok'
			};
			if(Object.keys(status).indexOf(deviceStatus.elockLocation) > -1) {
				$("#elockLocation").addClass(status[deviceStatus.elockLocation]);
			}
			if(Object.keys(status).indexOf(deviceStatus.elockCommuicate) > -1) {
				$("#elockCommuicate").addClass(status[deviceStatus.elockCommuicate]);
			}
			if(Object.keys(status).indexOf(deviceStatus.elockInArea) > -1) {
				$("#elockInArea").addClass(status[deviceStatus.elockInArea]);
			}
		}else{
			console.log("查询关锁状态为空");
		}
	}, 'json');
	*/
	//以下为模拟数据
	var data = {
		//'elockLocation': '' + parseInt(Math.random() * 10) % 2,
		'elockLocation': '1',
		//'elockCommuicate': '' + parseInt(Math.random() * 10) % 2,
		'elockCommuicate': '1',
		'elockInArea': '' + parseInt(Math.random() * 10) % 2
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
		}else{
			console.log("获取用户所属口岸为空");
			$("#tripVehicleVO\\.checkinPort").val('Beijing');
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
				$checkoutPort.append("<option value='" + item.organizationId + "'>" + item.organizationName + "</option>");
			});
		}else{
			console.log("获取所有检出口岸为空");
			$checkoutPort.append("<option value='1'>Jordan</option>");
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
				$routeId.append("<option value='" + item.routeAreaId + "'>" + item.routeAreaName + "</option>");
			});
		}else{
			console.log("获取所有路线为空");
			$routeId.append("<option value='1'>Neimenggu -> Kazakhstan -> Jordan</option>");
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
	/*
	video.addEventListener('loadeddata', function() {
		$('body').animate({scrollTop: $("body").height()}, 800);
	}, false);
	*/
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
			.updateStatus('tripVehicleVO.routeId', 'NOT_VALIDATED')
			.validate()
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
 * 校验设备状态
 * @param data 后台查到的数据
 * @param num 用户输入或读取的号码
 * @param isRead 是否从设备读取的
 * @returns {Boolean}
 */
function checkDevice(data, num, isRead){
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
				/*
				$('#addElockModal').on('hidden.bs.modal', function () {
					$("#addElockModal .modal-content").empty();
				});
				*/
			}
		});
		return false;
	}
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
	return true;
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
 * 校验子锁号
 * @param data 后台查到的数据
 * @param num 用户输入或读取的号码
 * @param isRead 是否从设备读取的
 * @returns {Boolean}
 */
function checkEsealNumber(data, num, isRead){
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
				}).modal('show');
				/*
				$('#addEsealModal').on('hidden.bs.modal', function () {
					$("#addEsealModal .modal-content").empty();
				});
				*/
			}
		});
		return false;
	}
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
	return true;
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
 * 校验传感器号
 * @param data 后台查到的数据
 * @param num 用户输入或读取的号码
 * @param isRead 是否从设备读取的
 * @returns {Boolean}
 */
function checkSensorNumber(data, num, isRead){
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
				}).modal('show');
			}
		});
		return false;
	}
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
	return true;
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
            
		}//,
	/*
		submitHandler: function(validator, form, submitButton) { //bootstrapvalidator 0.4.5用法
			if(validateEsealNumber() && validateSensorNumber()&& validateFiles()) {
				var formData = new FormData(form[0]);
				$.ajax({
					url: form[0].action,
					type: 'POST',
					contentType: false,
					data: formData,
					dataType: 'JSON',
					processData: false,
					success: function(result) {
						try {
							if (result && result.result) {
								bootbox.success($.i18n.prop("trip.activate.success"), function() {  
									location.href = location.href.replace(/#$/, '');
								});
							} else if (result.message) {
								bootbox.alert($.i18n.prop("trip.activate.failed") + ":" + result.message);
							}
						} catch (e) {}
					}
				});
			}
		}
	*/
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
        
        if(validateEsealNumber() && validateSensorNumber() && validateDeviceStatus()) {
        	if(validateFiles()) {
        		//为子锁号顺序、传感器号顺序赋值：
        		setEsealOrder();
        		setSensorOrder();
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
								bootbox.success($.i18n.prop("trip.activate.success"), function() {  
									location.href = location.href.replace(/#$/, '');
								});
							} else if (result.message) {
								bootbox.alert($.i18n.prop("trip.activate.failed") + ":" + result.message);
							}
						} catch (e) {}
					}
				});
        	}
		}
    });
	bootstrapValidator = $('#tripForm').data('bootstrapValidator');
}