<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/incbaxe.jsp"%>
<link href="${root}/business/checkinfo/css/sms.css" rel="stylesheet"
	type="text/css" media="screen">
<link href="${root}/business/checkinfo/css/style.css" rel="stylesheet"
	type="text/css" media="screen">
</head>
<body>
	<div id="con_bt" class="con_bt" style="display: none;">
		<!-- 扫描图像 -->
		<div class="showbox">
			<ul>
				<li style="width: 100%; height: 100%"><img alt=""
					src="${root}/business/checkinfo/css/images/Trailer.jpg"
					id="nii_imagePath" class="imgFull"
					onerror="javascript:this.src='${root}/business/checkinfo/css/images/valid-xhtml10.png'">
					<div class="showbox_bag"
						style="background-color: rgb(101, 101, 101)"></div></li>
			</ul>

		</div>
	</div>
	<div id="mask" class="mask" style="display: none;">
		<iframe id="align-center" name="align-center" class="align-center"
			frameborder="0" src="${root}/business/checkinfo/indexGDT.jsp"
			scrolling="no"></iframe>
	</div>
	<script type="text/javascript">
$(document).ready(function() {
	moveCoverDiv();
	$(".con_bt").show();
})

//隐藏遮罩层
		function hideConbtDiv() {

			$(".con_bt").hide();
		}
		/**
		 * 蒙层扫描移动
		 */
		function moveCoverDiv() {
			$(".con_bt").css("height", 327);
			$(".con_bt").css("width", 1300);
			var $w = $('.con_bt').width();//取得展示区外围宽度
			var $h = $('.con_bt').height();//取得展示区外围高度
			//根据车型不同显示不同车型的图片
			
			$.ajaxSettings.async = false; // (同步执行) 
			var url = location.search;
			var vehicleModel = url.substring(7, url.length);
			
			if(vehicleModel==null||vehicleModel==''){
				vehicleModel = '';
			}else{
			$
			.getJSON(
					"${root}/getDicTypeByModelId.action?vehicleModelId=" + vehicleModel,
					function(jsondata) {
						if (jsondata != null) {
							vehicleModel = jsondata.dicType;
						}else{
							vehicleModel = '';
						}
					});
			}
			$.ajaxSettings.async = true; // (异步执行) 
			if(vehicleModel==''){
				$("#nii_imagePath").attr("src",
						"${root}/business/checkinfo/css/images/vehicleMode/Others.jpg");
			}else{
			$("#nii_imagePath").attr("src",
						"${root}/business/checkinfo/css/images/vehicleMode/" + vehicleModel + ".jpg");
			}

			if ($("#nii_imagePath").attr("src") == "${root}/business/checkinfo/css/images/valid-xhtml10.png") {
				$(".showbox_bag").css("left", $w);
				return;
			}
			/**
			 * 如果要获取图片的真实的宽度和高度有三点必须注意 1、需要创建一个image对象：如这里的$("<img/>")2、指定图片的src路径  3、一定要在图片加载完成后执行如.load()函数里执行
			 */
			// alert($("#nii_imagePath").attr("src"));
			$("<img/>")
					.attr("src", $("#nii_imagePath").attr("src"))
					.load(
							function() {
								var realWidth = this.width; //扫描图像的真实宽度
								var $showbox = $('.showbox_bag');
								//若外围宽度小于图像宽度，将外围宽度赋予图像
								if ($w < realWidth) {
									$("#nii_imagePath").css("width", $w);
									$("#nii_imagePath").css("margin", '0');
									//$("#nii_imagePath").css("height", $h*0.4);
									if (!$showbox.is(':animated')) { //判断展示区是否动画
										$showbox.animate({
											left : '+=' + $w,
											width : '-=' + $w
										}, 10000, "swing", showMask);//改变left值,切换显示版面
									}
								} else {
									var moveTime = ((realWidth / $w).toFixed(2)) * 10000;
									;
									$("#nii_imagePath").css("width", realWidth);
									if (!$showbox.is(':animated')) { //判断展示区是否动画
										$showbox.animate({
											left : '+=' + realWidth
										}, moveTime);//改变left值,切换显示版面
									}
								}
							});
		}

		/**
		 * 蒙层复位
		 */
		function resetCoverDiv() {
			$(".showbox_bag").css("left", "0px");
		}
		/**
		 * 停止动画
		 */
		function stopMove() {
			var $showbox = $('.showbox_bag');
			var isMove = $showbox.is(':animated');
			if (isMove) {
				$showbox.stop(false, true).animate();
			}
		}
		function sleep(numberMillis) {
			var now = new Date();
			var exitTime = now.getTime() + numberMillis;
			while (true) {
				now = new Date();
				if (now.getTime() > exitTime)
					return;
			}
		}
		
		//兼容火狐、IE8 
		//显示遮罩层  
		function showMask() {
			//$("#mask").css("height", $(document).height());
			//$("#mask").css("width", $(document).width());
			$("#mask").css("height", 327);
			$("#mask").css("width", 1300);
			hideConbtDiv();
			$("#mask").show();
		}
		//隐藏遮罩层
		function hideMask() {

			$("#mask").hide();
		}
</script>
</body>
</html>