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
			$("#q_tripVehicleVO\\.trackingDeviceNumber").val(data);
		}, 'json');
		*/
		//以下为模拟数据
		var data = '1';
		$("#q_trackingDeviceNumber, #s_trackingDeviceNumber").val(data);
	});
	//获取报关单号按钮
	$("#btnGetDecNum").on("click", function(){
		/*
		$.get(root + "/...", {}, function(data){
			$("#q_tripVehicleVO\\.declarationNumber").val(data);
		}, 'json');
		*/
		//以下为模拟数据
		var data = '222520131250168837';
		$("#q_declarationNumber, #s_declarationNumber").val(data);
		/*
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
		*/
	});
	//获取车牌号按钮
	$("#btnGetVehicleNum").on("click", function(){
		/*
		$.get(root + "/...", {}, function(data){
			$("#q_tripVehicleVO\\.vehiclePlateNumber").val(data);
		}, 'json');
		*/
		//以下为模拟数据
		var data = "316A";
		$("#q_vehiclePlateNumber, #s_vehiclePlateNumber").val(data);
	});
	//查询所有行程信息
	$("#btnQuery").on("click", function(){
		trimText();
		if($("#s_trackingDeviceNumber").val() || $("#s_declarationNumber").val() || $("#s_vehiclePlateNumber").val()) {
			//以下为模拟数据
			getTripInfo({
				"s_trackingDeviceNumber": $("#s_trackingDeviceNumber").val(),
				"s_declarationNumber": $("#s_declarationNumber").val(),
				"s_vehiclePlateNumber": $("#s_vehiclePlateNumber").val()
			}, showTripInfo);
		}
	});
	//点击行程结束按钮
	$("#btnFinish").on("click", function(){
		//validateFiles();
	});

	//查询设备状态：定位、通讯、电量等
	getDeviceStatus();
	//初始化Jquery i18n
	initJqueryI18n();
	//添加表单验证
	bootstrapValidatorForm();
});
/**
 * 通过参数查询行程信息
 * @param {Object} params
 * @param {Object} callback
 */
function getTripInfo(params, callback) {
	$.get(root + "/monitortrip/findOneTripVehicle.action", params, function(data){
		if(data && data.rows.length > 0) {
			callback(data.rows[0]);
		}else{
			bootbox.alert($.i18n.prop("trip.info.trip.notFound", 
					params.s_trackingDeviceNumber || '', 
					params.s_declarationNumber || '', 
					params.s_vehiclePlateNumber || ''));
			return;
		}
	}, 'json');
}

/**
 * 展示行程信息
 * @param {Object} data
 */
function showTripInfo(data) {
	drillProps(data, '', function(obj, objName) {
		$('#tripVehicleVO\\.' + objName.replace(/\./g, '\\\.')).val(obj);
	});
	$("#q_trackingDeviceNumber, #s_trackingDeviceNumber").val($("#tripVehicleVO\\.trackingDeviceNumber").val());
	$("#q_declarationNumber, #s_declarationNumber").val($("#tripVehicleVO\\.declarationNumber").val());
	$("#q_vehiclePlateNumber, #s_vehiclePlateNumber").val($("#tripVehicleVO\\.vehiclePlateNumber").val());
	//获取子锁号
	getEsealNum($("#tripVehicleVO\\.esealNumber").val());
	//获取传感器编号
	getSensorNum($("#tripVehicleVO\\.sensorNumber").val());
	//列出检入图像
	showCheckinPicture($("#tripVehicleVO\\.checkinPicture").val());
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
//	    	alert($.i18n.prop("common.message.form.validator"))
	    }
	});
}
/**
 * 获取子锁号
 * @param data
 */
function getEsealNum(data){
	if(Object.prototype.toString.call(data) === '[object String]' && typeof(data) === 'string') {
		data = $.trim(data).split(/\s*,\s*/);
	}
	if($.isArray(data)) {
		$(".esealNumber-list").children("ul").empty().end().prev("div").hide();
		for(var i = 0, len = data.length; i < len; i++){
			appendEsealNums(data[i]);
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
		$(".sensorNumber-list").children("ul").empty().end().prev("div").hide();
		for(var i = 0, len = data.length; i < len; i++){
			appendSensorNums(data[i]);
		}
	}
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
			if(Object.keys(status).indexOf(deviceStatus.inArea) > -1) {
				$("#inArea").addClass(status[deviceStatus.inArea]);
			}
		}else{
			console.log("查询关锁状态为空");
		}
	}, 'json');
	*/
	//以下为模拟数据
	var data = {
		'inArea': '' + parseInt(Math.random() * 10) % 2
	};
	deviceStatus = data;
	if(Object.keys(status).indexOf(deviceStatus.inArea) > -1) {
		$("#inArea").addClass(status[deviceStatus.inArea]);
	}
}
/**
 * 列出检入图像
 * @param {Object} checkinPicture
 */
function showCheckinPicture(checkinPicture){
	checkinPicture = checkinPicture && $.trim(checkinPicture).split(/\s*,\s*/);
	if(checkinPicture && checkinPicture.length > 0) {
		$(".thumbnail").each(function(value, index){
			if($(this).find("a.delete").length < 1) {
				$(this).parent("div").remove();
			}
		});
		var html = '';
		for(var i = 0; i < checkinPicture.length; i++) {
			if(checkinPicture[i]) {
				var name = checkinPicture[i].slice(checkinPicture[i].lastIndexOf('/') + 1);
				html += '<div class="col-sm-6 col-md-3">';
				html += '	<div class="thumbnail">';
				html += '		<img src="' + tripPhotoPathHttp + checkinPicture[i] + '" name="localImage" />';
				html += '		<div class="caption">';
				html += '			<p title="' + name + '">';
				html += '				<span>' + name + '</span>';
				html += '			</p>';
				html += '		</div>';
				html += '	</div>';
				html += '</div>';
			}
		}
		$("#collapseTwo .row").append(html);
	}
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
 * 校验设备状态
 */
function validateDeviceStatus(){
	if(deviceStatus.inArea != '1') {
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
	if(uploadFiles.length < 1 && $("input[name=tripCameraBase64]").length < 1) {
		$("div.file-help-block").show();
		bootstrapValidator.disableSubmitButtons(false);
		$('body').animate({scrollTop: $(".wrapper-content").height()}, 400);
		return false;
	}
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
        if(validateDeviceStatus()) {
        	if(validateFiles()) {
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
        						bootbox.success($.i18n.prop("trip.finish.success"), function() {  
        							location.href = location.href.replace(/#$/, '');
        						});
        					} else if (result.message) {
        						bootbox.alert($.i18n.prop("trip.finish.failed") + ":" + result.message);
        					}
        				} catch (e) {}
        			},
        			error: function (XMLHttpRequest, textStatus, errorThrown) {
        				//this; // 调用本次AJAX请求时传递的options参数
        				console.error(textStatus || errorThrown);
        			}
        		});
        	}
		}
    });
	bootstrapValidator = $('#tripForm').data('bootstrapValidator');
}