var fr, gum, bootstrapValidator, deviceStatus,
	uploadFiles = [], uploadFileNames = [];



$(function() {
	//打开摄像头按钮
	$("#btnCamera").on("click", function() {
		if ($("#main").css("display") == 'none') {
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
})





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
 * 打开摄像头
 */
function openCamera() {
	gum.play();
}
//关闭摄像头按钮
$("#btnCameraClose").on("click", function() {
	closeCamera();
});

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
						bootbox.alert($.i18n.prop('The user denied the browser request permission'));
					} else if (error.NOT_SUPPORTED_ERROR) {
						bootbox.alert($.i18n.prop('Sorry,your browser does not support the camera function,please use other browses'));
					} else if (error.MANDATORY_UNSATISFIED_ERROR) {
						bootbox.alert($.i18n.prop('The specified media type is not received by the media stream'));
					} else {
						bootbox.alert($.i18n.prop('System did not get to the camera,please ensure that the camera has been installed correctly.or try to refresh the page and try again'));
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
 * 设置拍摄照片的信息
 */
function setPhotoStatus() {
	var size = 0,
		$cameras = $("input[name=trackUnitCameraBase64]");
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
 * 读取拍摄的照片
 * @param {Object} b64 照片的Base64编码
 */
function readPhoto(b64) {
	var html = '';
	html += '<div class="col-sm-6 col-md-3">';
	html += '	<div class="thumbnail">';
	html += '		<img src="' + b64 + '" />';
	html += '		<input type="hidden" name="trackUnitCameraBase64" value="' + b64.substring(22) + '" />';
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

/**
 * 关闭摄像头
 */
function closeCamera() {
	$("#main").hide();
	$("#btnSnap").off();
	gum.stop();
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