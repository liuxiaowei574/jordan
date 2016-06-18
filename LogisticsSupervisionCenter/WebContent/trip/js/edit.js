var sel, fr, gum, bootstrapValidator,
	uploadFiles = [], uploadFileNames = [], esealNumberArray = [], sensorNumberArray = [],
	canvas = document.getElementById("canvas"),
	context = canvas.getContext("2d"),
	video = document.getElementById("video");
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
		//uploadFiles = uploadFiles.concat(files);
		//uploadFileNames = uploadFileNames.concat(getFileNames(files));
		if (files && files.length > 0) {
			var index = parseInt($(this).data("index"));
			for (var i in files) { //一次只允许上传一张
				uploadFiles.push(files[i]);
				uploadFileNames.push(files[i].name);
				var reader = new FileReader();
				reader.onload = (function(f) {
					return function(e) {
						var html = '';
						html += '<div class="col-sm-6 col-md-3">';
						html += '	<div class="thumbnail">';
						html += '		<img src="' + e.target.result + '" name="localImage" />';
						html += '		<div class="caption">';
						html += '		<p>' + f.name;
						html += '			<img class="upload-delele" title="' + $.i18n.prop("trip.activate.button.delete") + '" name="' + f.name + '" data-index="' + index + '" src="' + root + '/static/images/dele.png"/>';
						html += '		</p>';
						html += '		</div>';
						html += '	</div>';
						html += '</div>';
						$("#collapseTwo .row").append(html);
						$("div.file-help-block").hide();
					};
				})(files[i]);
				reader.readAsDataURL(files[i]);
			}
			var clone = this.cloneNode(true);
			clone.dataset.index = index + 1;
			$("#btnLocal").parent("li").append(clone);
			setSelectedStatus();
		} else {
			//$(this).val('');
			var clone = this.cloneNode(true);
			$("#btnLocal").parent("li").append(clone);
			$(this).remove();
		}
	});

	//删除上传图片的按钮
	$("#row").on("click", ".upload-delele", function() {
		var index = parseInt($(this).data("index"));
		if (!isNaN(index)) {
			$("input[name='tripPhotoLocal'][data-index='" + index + "']").val('').remove();
			var fileIndex = uploadFileNames.indexOf(this.name);
			if (fileIndex > -1) {
				uploadFiles.splice(fileIndex, 1);
				uploadFileNames.splice(fileIndex, 1);
			}
		}
		$(this).parents(".thumbnail").parent("div").remove();
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
	
	//查询当前用户所属的检入口岸
	getCheckinPort();
	//查询当前用户所在国家的检出口岸
	getCheckoutPort();
	//获取追踪终端号
	$("#btnGetDeviceNum").on("click", function(){
		/*
		$.get(root + "/...", {}, function(data){
		});
		*/
		var $trackingDeviceNumber = $("#tripVehicleVO\\.trackingDeviceNumber");
		$trackingDeviceNumber.val('86021FG01');
		bootstrapValidator
				.updateStatus('tripVehicleVO.trackingDeviceNumber', 'NOT_VALIDATED')
				.validateField('tripVehicleVO.trackingDeviceNumber')
				.disableSubmitButtons(false);
	});
	//手动添加子锁号
	$("#btnAddEsealNum").on("click", function(){
		var $esealNumberInput = $("#esealNumberInput");
		$esealNumberInput.val($.trim($esealNumberInput.val()));
		var num = $esealNumberInput.val();
		if(num){
			appendEsealNums(num);
			$esealNumberInput.val('');
			validateEsealNumber();
		}
	});
	//删除子锁号
	$(".esealNumber-list").on("click", ".glyphicon", function(){
		$(this).parents("li").remove();
		var index = esealNumberArray.indexOf($(this).parent().prev("span").data("ori-value"));
		if(index > -1) {
			esealNumberArray.splice(index, 1);
		}
		$("#tripVehicleVO\\.esealNumber").val(esealNumberArray.join());
		
		validateEsealNumber();
	});
	//获取子锁号
	$("#btnGetEsealNum").on("click", function(){
		/*
		$.get(root + "/...", {}, function(data){
			if(data){
				appendEsealNums(data);
			}
		});
		*/
		var d, data = ['LBA-000SD-1009AOAODDC'];
		for(d in data){
			appendEsealNums(data[d]);
		}
		bootstrapValidator.disableSubmitButtons(false);
		validateEsealNumber();
	});
	//手动添加传感器编号
	$("#btnAddSensorNum").on("click", function(){
		var $sensorNumberInput = $("#sensorNumberInput");
		$sensorNumberInput.val($.trim($sensorNumberInput.val()));
		var num = $sensorNumberInput.val();
		if(num){
			appendSensorNums(num);
			$sensorNumberInput.val('');
			validateSensorNumber();
		}
	});
	//删除传感器编号
	$(".sensorNumber-list").on("click", ".glyphicon", function(){
		$(this).parents("li").remove();
		var index = sensorNumberArray.indexOf($(this).parent().prev("span").data("ori-value"));
		if(index > -1) {
			sensorNumberArray.splice(index, 1);
		}
		$("#tripVehicleVO\\.sensorNumber").val(sensorNumberArray.join());
		
		validateSensorNumber();
	});
	//获取传感器编号
	$("#btnGetSensorNum").on("click", function(){
		/*
		$.get(root + "/...", {}, function(data){
		});
		*/
		var d, data = ['E-XA000C'];
		for(d in data){
			appendSensorNums(data[d]);
		}
		bootstrapValidator.disableSubmitButtons(false);
		validateSensorNumber();
		
	});
	//获取报关单号
	$("#btnGetDecNum").on("click", function(){
		/*
		$.get(root + "/...", {}, function(data){
			//json
			showElock(data);
		});
		*/
		showElock({
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
	});
	//点击行程激活
	$("#btnActivate").on("click", function(){
		validateFiles();
		validateEsealNumber();
		validateSensorNumber();
	});

	jQuery.i18n.properties({//加载资浏览器语言对应的资源文件
	    name : 'LocalizationResource_center', //资源文件名称
	    path : _getRootPath() + "/i18n/", //资源文件路径
	    mode : 'map', //用Map的方式使用资源文件中的值
	    language :language,
	    callback : function() {
	    	//alert($.i18n.prop("common.message.form.validator"))
	    }
	});
	//添加表单验证
	bindTripForm();
});
/**
 * 增加子锁号到显示列表
 * @param {Object} num
 */
function appendEsealNums(num){
	var li = '';
	li += '<li><span data-ori-value="' + num + '">';
	li += num;
	li += '</span><a href="javascript:void(0);" class="delete" title="' + $.i18n.prop("trip.activate.button.delete") +'">';
	li += '<span class="glyphicon glyphicon-trash"></span>';
	li += '</a>';
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
	li += '<li><span data-ori-value="' + num + '">';
	li += num;
	li += '</span><a href="javascript:void(0);" class="delete" title="' + $.i18n.prop("trip.activate.button.delete") +'">';
	li += '<span class="glyphicon glyphicon-trash"></span>';
	li += '</a>';
	li += '</li>';
	$(".sensorNumber-list").find("ul").append(li);
	sensorNumberArray.push(num);
	$("#tripVehicleVO\\.sensorNumber").val(sensorNumberArray.join());
}
/**
 * 查询当前用户所属的检入口岸
 */
function getCheckinPort(){
	$.get(root + "/deptMgmt/findPortByUserId.action", {}, function(data){
		if(data && data.port) {
			$("#tripVehicleVO\\.checkinPort").val(data.port);
		}else{
			console.log("获取用户所属口岸为空");
			$("#tripVehicleVO\\.checkinPort").val('Beijing');
		}
	});
}
/**
 * 查询所有检出口岸
 */
function getCheckoutPort(){
	$.get(root + "/deptMgmt/findAllPortByUserId.action", {}, function(data){
		data = data && $.parseJSON(data);
		if(data && data.total > 0) {
			var $checkoutPort = $("#tripVehicleVO\\.checkoutPort");
			var rows = data.rows;
			$.each(rows, function(index, item){
				$checkoutPort.append("<option value='" + item.organizationId + "'>" + item.organizationName + "</option>");
			});
		}else{
			console.log("获取所有检出口岸为空");
			$checkoutPort.append("<option value='1'>Jordan</option>");
		}
	});
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
 * 展示关锁信息 
 * @param {Object} data
 */
function showElock(data) {
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
			sel = document.getElementById('fileselect'); // get reference to file select input element
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
		$('html, body').animate({scrollTop: $("body").height()}, 800);
	}, false);
	*/
	$('html, body').animate({scrollTop: $("body").height()}, 800);
	$("#btnSnap").off().on('click', function() {
		$('#cream_loading').toggle();
		context.drawImage(video, 0, 0, 640, 480);
		convertCanvasToImage();
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
			context.drawImage(imgtag, 0, 0, 640, 480);
			convertCanvasToImage();
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
	image.src = document.getElementById("canvas").toDataURL("image/png");
	//字符串前有22位提示信息“data:image/png;base64”
	//var b64 = image.src.substring(22);
	var b64 = image.src;
	(function(code) {
		var html = '';
		html += '<div class="col-sm-6 col-md-3">';
		html += '	<div class="thumbnail">';
		html += '		<img src="' + code + '" />';
		html += '		<input type="hidden" name="tripCameraBase64" value="' + code.substring(22) + '"';
		html += '		<div class="caption">';
		html += '		<p>';
		html += '			<img class="upload-delele" title="' + $.i18n.prop("trip.activate.button.delete") + '" name="' + '" data-index="" src="' + root + '/static/images/dele.png"/>';
		html += '		</p>';
		html += '		</div>';
		html += '	</div>';
		html += '</div>';
		$("#collapseTwo .row").append(html);
		$("div.file-help-block").hide();
	})(b64);
	$('#cream_loading').toggle();
	return image;
}
/**
 * 校验文件
 */
function validateFiles(){
	if(uploadFiles.length < 1 && $("input[name=tripCameraBase64]").length < 1) {
		$("div.file-help-block").show();
		bootstrapValidator.disableSubmitButtons(false);
		return false;
	}
	return true;
}
/**
 * 校验子锁号
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
 * 校验传感器号
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
 * 添加表单验证
 */
function bindTripForm() {
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
						//max: 100
						max: 2
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
					notEmpty: {}
				}
			},
			'tripVehicleVO.checkoutPort': {
				validators: {
					notEmpty: {}
				}
			},
			'tripVehicleVO.routeId': {
				validators: {
					notEmpty: {}
				}
			}
            
		},
		submitHandler: function(validator, form, submitButton) {
			if(validateFiles() && validateSensorNumber()&& validateEsealNumber() ) {
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
	});
	bootstrapValidator = $('#tripForm').data('bootstrapValidator');
}