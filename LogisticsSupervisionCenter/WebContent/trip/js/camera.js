$(function(){
	//关闭摄像头按钮
	$("#btnCameraClose").on("click", function() {
		closeCamera();
	});
	
	start();
});
function start(){
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
/**
 * 读取拍摄的照片
 * @param {Object} b64 照片的Base64编码
 */
function readPhoto(b64) {
	$("#myCarousel li, #myCarousel div").removeClass("active");
	var index = $("#myCarousel>.carousel-indicators").children("li").length;
	$("#myCarousel>.carousel-indicators").append('<li data-target="#myCarousel" data-slide-to="' + index + '" class="active">')
		.append('<input type="hidden" name="tripCameraBase64" value="' + b64.substring(22) + '" />')
		.append('</li>');
	var image = {
			src: b64,
			name: uuid(),
			index: Object.keys(photoIndexVehicleNumMap).length + 'photo'
	};
	var html = ejs.render($("#imageItem").html(), image);
	$("#myCarousel>.carousel-inner").append(html);
	$("div.file-help-block").hide();
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
 	if(globalVehicle.editingId) {
 		photoIndexVehicleNumMap[Object.keys(photoIndexVehicleNumMap).length] = globalVehicle[globalVehicle.editingId].vehiclePlateNumber;
 	}else{
 		photoIndexVehicleNumMap[Object.keys(photoIndexVehicleNumMap).length] = null;
 	}
 	$('#cream_loading').toggle();
 	return image;
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
	$("#btnSnap").off();
	$('#cameraModal').modal('hide');
	gum.stop();
}
/**
 * 摄像头调用成功回调
 * @param {Object} video
 */
function showSuccess(video) {
	$("#btnSnap").off().on('click', function() {
		var canvas = document.getElementById("canvas"), context = canvas.getContext("2d");
		$('#cream_loading').toggle();
		context.drawImage(video, 0, 0, 640, 480);
		convertCanvasToImage(canvas);
		
		$("#face_scan_camera").prepend('<div id="alert" class="alert alert-success fade in">' + $.i18n.prop('trip.message.snap.success') + '</div>');
		$("#alert").fadeIn();
		setTimeout(function(){$("#alert").alert("close")}, 1500);
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