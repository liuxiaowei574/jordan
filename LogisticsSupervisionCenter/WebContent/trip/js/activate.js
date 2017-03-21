var fr, gum, bootstrapValidator, deviceStatus,  
	uploadFiles = [], uploadFileNames = [], esealNumberArray = [], sensorNumberArray = [], globalVehicle = {}, fileIndexVehicleNumMap = {}, photoIndexVehicleNumMap = {}, refreshTimeout = {};
ejs.open = '{{';
ejs.close = '}}';
$.fn.extend({
	/**
	 * 检测数组中的某些元素是否满足指定函数的判断。
	 * func(index, element, array)：元素索引，元素值，被遍历的数组
	 */
	some: function(func) {
		var _self = this;
		if ($.isFunction(func)) {
			return $.grep(this, function(elem, i) {
				return !!func.call(elem, i, elem, _self) !== false;
			}).length > 0;
		}
		return true;
	}
});
$(function() {
	$('#canvas').hide();
	$("input[name=tripPhotoLocal]").attr("id", uuid());

	//本地上传按钮
	$("#myTabContent").delegate("#btnLocal", "click", function() {
		$("input[name=tripPhotoLocal]:last").click();
	});
	//本地上传按钮
	$("#photoMenu").on("change", "input[name='tripPhotoLocal']:last", function(e) {
		var files = e.target.files || e.dataTransfer.files;
		files = filterFiles(files);
		if (files && files.length > 0) {
			$('body').animate({scrollTop: $(".wrapper-content").height()}, 400);
			var index = parseInt($(this).data("index"));
			for (var i in files) { //一次实际只有一张
				var f = files[i];
				if(globalVehicle.editingId) {
					globalVehicle[globalVehicle.editingId].uploadFiles = globalVehicle[globalVehicle.editingId].uploadFiles || [];
					globalVehicle[globalVehicle.editingId].uploadFiles.push(f);
					globalVehicle[globalVehicle.editingId].uploadFileNames.push(f.name);
					fileIndexVehicleNumMap[index] = globalVehicle[globalVehicle.editingId].vehiclePlateNumber;
				}else{
					uploadFiles.push(f);
					uploadFileNames.push(f.name);
					fileIndexVehicleNumMap[index] = null;
				}
				//读取并显示图片
				readImageFile(f, index);
			}
			var clone = this.cloneNode(true);
			clone.dataset.index = index + 1;
			clone.id = uuid();
			$("#btnLocal").parent("li").append(clone);
		} else {
			//上传的文件不符合要求被过滤掉，重新复制一个新组件替换原来的
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
		
		var fromCamera = !!findUUID(name);
		
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
						if(globalVehicle.editingId) {
							globalVehicle[globalVehicle.editingId].uploadFiles.splice(fileIndex, 1);
							globalVehicle[globalVehicle.editingId].uploadFileNames.splice(fileIndex, 1);
						}else{
							uploadFiles.splice(fileIndex, 1);
							uploadFileNames.splice(fileIndex, 1);
						}
					}
				}
			}
		}
		validateFiles();
	});

	//打开摄像头按钮
	$("#myTabContent").delegate("#btnCamera", "click", function() {
		$('#cameraModal').removeData('bs.modal');
		$('#cameraModal').modal({
			remote : root + "/monitortrip/toCamera.action",
			show : false,
			backdrop : 'static',
			keyboard : false
		});
	});
	//自动读取追踪终端号按钮
	$("#myTabContent").delegate("#btnGetDeviceNum", "click", function(){
		readElockNum();
	});
	//手动输入追踪终端号按钮
	$("#myTabContent").delegate("#tripVehicleVO_trackingDeviceNumber", "blur", function(){
		this.value = $.trim(this.value);
		if(this.value) {
			checkElockNum(this.value);
		}
	});
	//自动读取子锁号按钮
	$("#myTabContent").delegate("#btnGetEsealNum", "click", function(){
		//最多添加6个
		if(esealNumberArray.length === 6) {
			bootbox.alert($.i18n.prop("trip.info.esealNumber.maximum"));
			return false;
		}
		readEsealNum();
	});
	//手动添加子锁号按钮
	$("#myTabContent").delegate("#btnAddEsealNum", "click", function(){
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
		$("#tripVehicleVO_esealNumber").val(esealNumberArray.join());
		//bindingRelation();
	});
	//自动读取传感器编号按钮
	$("#myTabContent").delegate("#btnGetSensorNum", "click", function(){
		//最多添加6个
		if(sensorNumberArray.length === 6) {
			bootbox.alert($.i18n.prop("trip.info.sensorNumber.maximum"));
			return false;
		}
		readSensorNum();
	});
	//手动添加传感器编号按钮
	$("#myTabContent").delegate("#btnAddSensorNum", "click", function(){
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
		$("#tripVehicleVO_sensorNumber").val(sensorNumberArray.join());
		//bindingRelation();
	});
	//读取报关单号按钮
	$("#btnGetDecNum").on("click", function(){
		//以下为模拟数据
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
		validateTripInfos();
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
	//选择路线时，显示行程耗时
	$("#tripVehicleVO\\.routeId").on("change", function(){
		var timecost = $("#tripVehicleVO\\.routeId option:selected").data("timecost");
		$("div#timeCost").html(convertTime(timecost));
	});
	//选择检出口岸时，自动匹配路线和耗时
	$("#tripVehicleVO\\.checkoutPort").on("change", function(){
		setCheckoutAndRoute($(this).children("option:selected").val());
	});
	//施封
	$("#myTabContent").delegate("#btnSetLocked", "click", function(){
		if($.trim($("#tripVehicleVO_trackingDeviceNumber").val())) {
			//为了不让websocket通信异常影响后续操作，此处应进行try、catch
			try{
				setLocked();
			}catch(e){}
			elockLog('setLocked');
		}
	});
	//解封
	$("#myTabContent").delegate("#btnSetUnlocked", "click", function(){
		if($.trim($("#tripVehicleVO_trackingDeviceNumber").val())) {
			//为了不让websocket通信异常影响后续操作，此处应进行try、catch
			try{
				setUnlocked();
			}catch(e){}
			elockLog('setUnlocked');
		}
	});
	//解除报警
	$("#myTabContent").delegate("#btnClearAlarm", "click", function(){
		if($.trim($("#tripVehicleVO_trackingDeviceNumber").val())) {
			//为了不让websocket通信异常影响后续操作，此处应进行try、catch
			try{
				clearAlarm();
			}catch(e){}
			elockLog('clearAlarm');
		}
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
	}).on('hidden.bs.modal', function (e) {
		$(this).find(".modal-content").empty();
	});
	//模态框登录判断
	$('#addEsealModal').on('show.bs.modal', function(e) {
		var content = $(this).find(".modal-content").html();
		needLogin(content);
	});
	//添加传感器模态框事件
	$('#addSensorModal').on('loaded.bs.modal', function(e) {
		$(this).modal('show');
	}).on('hidden.bs.modal', function (e) {
		$(this).find(".modal-content").empty();
	});
	//模态框登录判断
	$('#addSensorModal').on('show.bs.modal', function(e) {
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
	}).on('show.bs.modal', function(e) {
		var content = $(this).find(".modal-content").html();
		needLogin(content);
	});
	$("#myTabContent").delegate("#btnCancelVehicle", "click", function(){
		if($(this).data('edit') == true) {
			var $tabPan = $(this).closest(".tab-pane");
			$("#newVehicleTab").html($tabPan.html());
			var vehicleInfo = $("#vehicleInfo").html();
			var vehicle = globalVehicle[globalVehicle.editingId];
			var html = ejs.render(vehicleInfo, vehicle);
			$("#btnNewVehicle").show();
			$tabPan.html(html);
		}
		emptyEsealNumSensorNum();
		$("div[name=vehicle-panel] input[type=text]").val('').attr("value", '');
		$("div[name=vehicle-panel] select>option").removeAttr("selected");
		if(!globalVehicle.editingId) {
			$("#btnNewVehicle").parent("li").prev("li").children("a:last").tab("show");
		}
		globalVehicle.editingId = null;
		$("#btnSaveVehicle, #btnCancelVehicle").removeData("edit");
		$("#myTabContent").find("button[name=btnEditVehicle]").show();
	});
	$("#btnNewVehicle").on("click", function(){
		//emptyEsealNumSensorNum();
		//$("#btnSaveVehicle, #btnCancelVehicle").removeData("edit");
		//$("div[name=vehicle-panel] input[type=text]").val('').attr("value", '');
		//$("div[name=vehicle-panel] select>option").removeAttr("selected");
	});
	//保存新增的车辆信息
	$("#myTabContent").delegate("#btnSaveVehicle", "click", function(){
		if(!validateSingleVehicle()) {
			return false;
		}
		
		var vehicleInfo = $("#vehicleInfo").html();
		var carouselId = uuid();
        var vehicle = {
        		vehiclePlateNumber: $("#tripVehicleVO_vehiclePlateNumber").val(),
        		trackingDeviceNumber: $("#tripVehicleVO_trackingDeviceNumber").val(),
        		esealNumber: $("#tripVehicleVO_esealNumber").val(),
        		sensorNumber: $("#tripVehicleVO_sensorNumber").val(),
        		trailerNumber: $("#tripVehicleVO_trailerNumber").val()||'',
        		vehicleCountry: $("#tripVehicleVO_vehicleCountry").val()||'',
        		driverName: $("#tripVehicleVO_driverName").val()||'',
        		driverCountry: $("#tripVehicleVO_driverCountry").val()||'',
        		driverIdCard: $("#tripVehicleVO_driverIdCard").val()||uuid(),
        		containerNumber: $("#tripVehicleVO_containerNumber").val()||'',
        		goodsType: ($("#tripVehicleVO_goodsType").val()||[]).join(),
        		goodsTypeName: getGoodsTypeName($("#tripVehicleVO_goodsType").val())||'',
        		carousel: '#' + carouselId,
        		files: uploadFiles || globalVehicle[id].uploadFiles,
        		uploadFilesCount: uploadFiles.length
        };
        if(systemModules.isRiskOn) {
        	vehicle.riskStatus = Number($("input[name=tripVehicleVO_riskStatus]:checked").val());
        	vehicle.riskStatusName = getRiskStatusName($("input[name=tripVehicleVO_riskStatus]:checked").val());
        }
        var html = ejs.render(vehicleInfo, vehicle);
        
        var oldVehiclePlateNumber = $("#myTab>li.active a:eq(1)").text();
		var vehiclePlateNumber = $("#tripVehicleVO_vehiclePlateNumber").val();
		
		if($(this).data('edit') == true) {
			var nHtml = $(this).closest(".tab-pane").html();
			var index = $(this).closest(".tab-pane").index();
			for(var i in vehicle){
				globalVehicle[globalVehicle.editingId][i] = vehicle[i];
			}
			//如果修改了车牌号，需要更新
			for(var i in fileIndexVehicleNumMap) {
				if(fileIndexVehicleNumMap[i] == oldVehiclePlateNumber) {
					fileIndexVehicleNumMap[i] = vehiclePlateNumber;
				}
			}
			
			globalVehicle[globalVehicle.editingId].uploadFilesCount = globalVehicle[globalVehicle.editingId].uploadFiles && globalVehicle[globalVehicle.editingId].uploadFiles.length || 0;
			$("#newVehicleTab").html($(this).closest(".tab-pane").html());
			$(this).closest(".tab-pane").children().replaceWith(html);
			$("#myTab li").eq(index).children("a:last").html(vehiclePlateNumber);
		}else{
			var id = uuid();
			var tabLi = $("#tabLi").html();
			var liHtml = ejs.render(tabLi, {tabName: vehiclePlateNumber, tabHref: '#' + id});
			var tabHtml = '<div class="tab-pane fade" id="' + id + '">' + html + '</div>';
			globalVehicle[id] = vehicle;
			globalVehicle[id].uploadFiles = uploadFiles;
			globalVehicle[id].uploadFileNames = uploadFileNames;
			globalVehicle[id].uploadFilesCount = uploadFiles.length || 0;
			uploadFiles = [];
			uploadFileNames = [];
			for(var i in fileIndexVehicleNumMap) {
				if(!fileIndexVehicleNumMap[i]) {
					fileIndexVehicleNumMap[i] = vehicle.vehiclePlateNumber;
				}
			}
			for(var i in photoIndexVehicleNumMap) {
				if(!photoIndexVehicleNumMap[i]) {
					photoIndexVehicleNumMap[i] = vehicle.vehiclePlateNumber;
				}
			}
			$("#btnNewVehicle").parent("li").before(liHtml);
			$("#newVehicleTab").before(tabHtml);
			$("#btnNewVehicle").parent("li").prev("li").children("a:last").tab("show");
		}
        
		globalVehicle.editingId = null;
		$("#btnNewVehicle").show();
		$("#collapseVehicle input[type=text]").val('').attr("value", '');
		$("#collapseVehicle select>option").removeAttr("selected");
		emptyEsealNumSensorNum();
		$("#btnSaveVehicle, #btnCancelVehicle").removeData("edit");
		$("#myTabContent").find("button[name=btnEditVehicle]").show();
		//图片轮播
		$("#" + carouselId).children(".carousel-indicators").append($("#myCarousel").children(".carousel-indicators").children()).find("li").each(function(){
			this.dataset.target ='#' + carouselId;
		});
		$("#" + carouselId).children(".carousel-inner").append($("#myCarousel").children(".carousel-inner").children());
		
		$("#btnSetLocked, #btnSetUnlocked, #btnClearAlarm").attr("disabled", "disabled");
		//定时查询设备状态：定位、通讯、电量等
		clearAllTimeout();
		queryAllElockStatus();
	});
	//修改车辆信息
	$("#myTabContent").delegate("button[name=btnEditVehicle]", "click", function(){
		var index = $(this).closest(".tab-pane").index();
		var id = $(this).closest(".tab-pane").attr("id");
		//进入编辑时，其他Tab信息、图片不能修改
		$(this).closest(".tab-pane").siblings().find("button[name=btnEditVehicle],a.delete_image").hide();
		var $parent = $(this).parent(".panel-body");
		globalVehicle.editingId = id;
		var vehicle = globalVehicle[globalVehicle.editingId];
		vehicleToInput(vehicle, index);
		//图片轮播
		$("#myCarousel").children(".carousel-indicators").append($parent.find(".carousel-indicators").children()).find("li").each(function(){
			this.dataset.target ='#myCarousel';
		});
		$("#myCarousel").children(".carousel-inner").append($parent.find(".carousel-inner").children());
		clearAllTimeout();
	});
	//删除车辆信息
	$("#collapseVehicle").delegate("a.vehicle-remove", "click", function(){
		var id = $(this).next("a").attr("href");
		var vehiclePlateNumber = $(this).next("a").text();
		var $parent = $(this).parent("li");
		var _self = this;
		if($parent.hasClass("active")) {
			var $li;
			if($parent.prev("li").length > 0) {
				$li = $parent.prev("li");
			}else if($parent.next("li").length > 0) {
				$li = $parent.next("li");
			}
			$li.children("a:last").tab("show").one('shown.bs.tab', function(){
				$(id).remove();
				$parent.remove();
			});
		}else{
			$(id).remove();
			$parent.remove();
		}
		globalVehicle[id.slice(1)].uploadFileNames.forEach(function(element, index){
			$("#" + id.slice(1)).val('').remove();
			$("input[name='tripPhotoLocal'][data-index='" + index + "']").val('').remove();
			delete fileIndexVehicleNumMap[index];
		});
		for(var i in photoIndexVehicleNumMap) {
			if(photoIndexVehicleNumMap[i] == vehiclePlateNumber) {
				delete photoIndexVehicleNumMap[i];
			}
		}
		delete globalVehicle[id.slice(1)];
	});
	//检测车辆状态
	$("#myTabContent").delegate("#tripVehicleVO_vehiclePlateNumber", "blur", function(){
		this.value = $.trim(this.value);
		if(this.value != '') {
			checkVehiclePlateNumber(this.value);
		}
	});
	

	//查询设备状态：定位、通讯、电量等
	getDeviceStatus();
	setDefaultCheckout();
	//添加表单验证
	bootstrapValidatorForm();
});
/**
 * 刷新电量
 */
function refreshVoltage(voltage){
	//var value = transferVVV(voltage) || '0%';
	var value = createSealElectric(voltage) || '0%';
	if(new RegExp(/^\d+%$/).test(value)){
		$("#myTabContent .percentage").html(value);
		$("#myTabContent div[name=dianliang]").animate({"width": value}, 'normal');
	}
}
/**
 * 天朗关锁电量计算百分比
 * @param voltage
 * @returns {String}
 */
function createSealElectric(voltage) {
    if (voltage >= 450) {
        voltage = 100;
    } else if (voltage <= 320) {
        voltage = 0;
    } else if (parseInt((voltage - 320) / (415 - 320) * 100) >= 100) {
        voltage = 100;
    } else {
        voltage = parseInt((voltage - 320) / (415 - 320) * 100);
    }

    return voltage + "%";
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
 * 时间转换
 */
function convertTime(time) {
	time = time || 0;
	return time + $.i18n.prop('unit.timecost.minute');
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
			$("#myCarousel li, #myCarousel div").removeClass("active");
			var i = $("#myCarousel>.carousel-indicators").children("li").length;
			$("#myCarousel>.carousel-indicators").append('<li data-target="#myCarousel" data-slide-to="' + i + '" class="active"></li>');
			var image = {
					src: e.target.result,
					name: f.name,
					index: index
			};
			var html = ejs.render($("#imageItem").html(), image);
			$("#myCarousel>.carousel-inner").append(html);
			$("div.file-help-block").hide();
		};
	})(file);
	reader.readAsDataURL(file);
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
	$("#tripVehicleVO_trackingDeviceNumber").val(num);
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
	$("#tripVehicleVO_esealNumber").val(esealNumberArray.join());
	//bindingRelation();
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
	$("#tripVehicleVO_sensorNumber").val(sensorNumberArray.join());
	//bindingRelation();
}
//以下为获取关锁前模拟数据
var deviceData = {
	'elockLocation': '0',
	'elockCommuicate': '0',
	'elockInArea': '0'
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
	if(!systemModules.isAreaOn) {
		delete deviceData.elockInArea;
	}
	deviceStatus = deviceData;
	if(Object.keys(status).indexOf(deviceStatus.elockLocation) > -1) {
		$("span[name=elockLocation]").addClass(status[deviceStatus.elockLocation]);
	}
	if(Object.keys(status).indexOf(deviceStatus.elockCommuicate) > -1) {
		$("span[name=elockCommuicate]").addClass(status[deviceStatus.elockCommuicate]);
	}
	if(systemModules.isAreaOn) {
		if(Object.keys(status).indexOf(deviceStatus.elockInArea) > -1) {
			$("span[name=elockInArea]").addClass(status[deviceStatus.elockInArea]);
		}
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
 * 获得行程报关信息
 * @param code 报关单号
 */
function loadTripInfo(code){
	$.get(root + "/monitortrip/loadTripInfo.action", {declarationNumber: code}, function(data){
		if(!needLogin(data)) {
			if(data && data.tripVehicleVO) {
				showTripInfo(data.tripVehicleVO);
				validateTripInfos();
			}
		}
	}, 'json');
}

/**
 * 展示行程信息
 * @param {Object} data
 */
function showTripInfo(data) {
	drillProps(data, '', function(obj, objName) {
		$("#" + objName.replace(/\./g, '\\\.')).val(obj);
	});
	if(systemModules.isRiskOn) {
		getRisk();
	}
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
	$.get(root + "/analysis/getRisk.action", {"s_driverName": $("#tripVehicleVO_driverName").val(), "s_vehiclePlateNumber": $("#tripVehicleVO_vehiclePlateNumber").val()}, function(data){
		if(!needLogin(data)) {
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
	
	if(value > finalYellowThreshold){
		return 2; //高（red）
	}else if(value>finalGreenThreshold){
		return 1; //中（yellow）
	}else{
		return 0; //低（green）
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
 * 校验各行程信息字段
 */
function validateTripInfos(){
	bootstrapValidator
			.updateStatus('tripVehicleVO.declarationNumber', 'NOT_VALIDATED')
//			.updateStatus('tripVehicleVO.routeId', 'NOT_VALIDATED')
//			.validate()
			.disableSubmitButtons(false);
}
function validateRoute(){
	bootstrapValidator
			.updateStatus('tripVehicleVO.routeId', 'NOT_VALIDATED')
//			.validate()
			.disableSubmitButtons(false);
}
/**
 * 校验设备状态
 */
function validateDeviceStatus(){
	var flag = true;
	$("#myTabContent>.tab-pane[id!=newVehicleTab]").each(function(){
		if(!$(this).find("[name=elockLocation]").hasClass("glyphicon-ok")) {
			bootbox.alert($.i18n.prop("trip.info.location.invalid"), function(){
				bootstrapValidator.disableSubmitButtons(false);
			});
			flag = false;
			return false;
		}
		if(!$(this).find("[name=elockCommuicate]").hasClass("glyphicon-ok")) {
			bootbox.alert($.i18n.prop("trip.info.communication.invalid"), function(){
				bootstrapValidator.disableSubmitButtons(false);
			});
			flag = false;
			return false;
		}
		if(systemModules.isAreaOn) {
			if(!$(this).find("[name=elockInArea]").hasClass("glyphicon-ok")) {
				bootbox.alert($.i18n.prop("trip.info.inArea.invalid"), function(){
					bootstrapValidator.disableSubmitButtons(false);
				});
				flag = false;
				return false;
			}
		}
	});
	if(!flag) {
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
	// 关锁号只能是字母和数字，否则车辆状态表建表时，表名非法
	if(!/^[\da-zA-Z]+$/.test(num)) {
		bootbox.alert($.i18n.prop('trip.label.trackingDeviceNumber') + $.i18n.prop("trip.message.input.letterNumber"), function(){
			$("#tripVehicleVO_trackingDeviceNumber").val('');
		});
		return false;
	}
	$(".tab-pane input[name=trackingDeviceNumber]").each(function(){
		if($(this).val() == num) {
			bootbox.alert($.i18n.prop('trip.info.elockNumber.added', num), function(){
				$("#tripVehicleVO_trackingDeviceNumber").val('');
			});
			return false;
		}
	});
	$.get(root + "/warehouseElock/findByElockNumber.action", {"elockNumber": num}, function(data){
		if(!needLogin(data)) {
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
						});
					}else{
						!isRead && $("#tripVehicleVO_trackingDeviceNumber").val('');
						elockUtil.setElockNo('');
					}
				});
				return false;
			}
			//判断设备是否属于当前口岸
			if(data.warehouseElockBO.belongTo != data.organizationId) {
				var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.elockNumber.notBelong");
				bootbox.alert(msg, function(){
					!isRead && $("#tripVehicleVO_trackingDeviceNumber").val('');
					elockUtil.setElockNo('');
				});
				return false;
			}
			//判断设备状态是否为在途或正常
			if(data.warehouseElockBO.elockStatus == '2') {
				var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.elockNumber.statusInWay");
				bootbox.alert(msg, function(){
					!isRead && $("#tripVehicleVO_trackingDeviceNumber").val('');
					elockUtil.setElockNo('');
				});
				return false;
			}else if(data.warehouseElockBO.elockStatus != '1') {
				var msg = (!!isRead ? readMsg + ', ' : '') + $.i18n.prop("trip.info.elockNumber.statusInvalid");
				bootbox.alert(msg, function(){
					!isRead && $("#tripVehicleVO_trackingDeviceNumber").val('');
					elockUtil.setElockNo('');
				});
				return false;
			}
			appendElockNums(num);
		}
	}, 'json');
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
	for(var k in globalVehicle) {
		if(k != 'editingId') {
			if(globalVehicle[k].uploadFiles.length < 1) {
				bootbox.alert($.i18n.prop('trip.activate.info.image.required'));
				return false;
			}
		}
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
	var flag = true;
	$(".tab-pane input[name=esealNumber]").each(function(){
		if($(this).val().split(/\s*,\s*/).indexOf(num) > -1) {
			bootbox.alert($.i18n.prop('trip.info.esealNumber.added', num), function(){
				$("#tripVehicleVO_esealNumber").val('');
			});
			flag = false;
			return false;
		}
	});
	if(!flag) {
		return false;
	}
	$.get(root + "/esealMgmt/findByEsealNumber.action", {"esealNumber": num}, function(data){
		if(!needLogin(data)) {
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
			!isRead && $("#esealNumberInput").val('');
		}
	}, 'json');
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
	var flag = true;
	$(".tab-pane input[name=sensorNumber]").each(function(){
		if($(this).val().split(/\s*,\s*/).indexOf(num) > -1) {
			bootbox.alert($.i18n.prop('trip.info.sensorNumber.added', num), function(){
				$("#tripVehicleVO_sensorNumber").val('');
			});
			flag = false;
			return false;
		}
	});
	if(!flag) {
		return false;
	}
	$.get(root + "/sensorMgmt/findBySensorNumber.action", {"sensorNumber": num}, function(data){
		if(!needLogin(data)) {
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
			!isRead && $("#sensorNumberInput").val('');
		}
	}, 'json');
}
/**
 * 为esealOrder赋值
 */
function setEsealOrder(){
	$(".tab-pane").each(function(){
		if($(this).find("input[name=esealNumber]").length > 0) {
			var $esealNumber = $(this).find("input[name=esealNumber]").val().split(/\\s*,\\s*/);
			var temp = new Array(6);
			$.each(temp, function(index, value){
				//根据读取的锁号，解析子锁SSID存入esealOrder
				temp[index] = $esealNumber[index] && elockUtil.getElockSsidByElockNumber($esealNumber[index]) || '0';
			});
			$(this).find("input[name=esealOrder]").val(temp.join());
		}
	});
}
/**
 * 为sensorOrder赋值
 */
function setSensorOrder(){
	$(".tab-pane").each(function(){
		if($(this).find("input[name=sensorNumber]").length > 0) {
			var $sensorNumber = $(this).find("input[name=sensorNumber]").val().split(/\\s*,\\s*/);
			var temp = new Array(6);
			$.each(temp, function(index, value){
				temp[index] = $sensorNumber[index] || '0';
			});
			$(this).find("input[name=sensorOrder]").val(temp.join());
		}
	});
}
/**
 * 添加表单验证
 */
function bootstrapValidatorForm() {
	$('#tripForm').bootstrapValidator({
		fields: {
			'tripVehicleVO.declarationNumber': {
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
		setFileIndexVehicleNumMap();
		if(checkActivate()) {
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
					try {
						if(!needLogin(result)) {
							if (result && result.result) {
								if(result.tripId && $("#tripVehicleVO\\.tripId").length < 1) {
									$("#tripForm").append("<input type='hidden' id='tripVehicleVO.tripId' name='tripVehicleVO.tripId' value='" + result.tripId + "'/>");
								}
								if(!systemModules.isApprovalOn) {
									bootbox.success($.i18n.prop("trip.activate.success"), function() {  
										location.href = location.href.replace(/#$/, '');
									});
								}
								try{
									setBindingRelation();//绑定封条
								} catch(e) {}
							} else if (result.message) {
								bootbox.alert($.i18n.prop("trip.activate.failed") + ":" + result.message);
								bootstrapValidator.disableSubmitButtons(false);
							}
						}
					} catch (e) {}
				}
			});
		}
    });
	bootstrapValidator = $('#tripForm').data('bootstrapValidator');
}

/**
 * 查询关锁是否已经生成status表数据
 */
function queryElockStatus(trackingDeviceNumber){
 	refreshDeviceInfo();
    function refreshDeviceInfo(){
    	if(typeof(trackingDeviceNumber)!=undefined && trackingDeviceNumber!=null && trackingDeviceNumber!=""){
    		var portUrl = _getRootPath() + "/vehiclestatus/findDeviceNumber.action?trackingDeviceNumber="+trackingDeviceNumber;
    		loadingDeviceStatus(trackingDeviceNumber);
    		//先转圈，三秒之后才去查询
    		setTimeout(function(){
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
        				if(!needLogin(obj)) {
    	    				if(obj.success){
    	    					deviceData = {
    								'elockLocation': '1',
    								'elockCommuicate': '1',
    								'elockInArea': '1'
    								//'elockInArea': '' + parseInt(Math.random() * 10) % 2
    							};
    	    					if(!systemModules.isAreaOn) {
    	    						delete deviceData.elockInArea;
    	    					}
    	    					deviceStatus = deviceData;
    	    					validDeviceStatus(trackingDeviceNumber);
    	    					var $panel = $("input[name=trackingDeviceNumber][value=" + trackingDeviceNumber + "]").closest(".panel-body");
    	    					if($panel && $panel.length > 0) {
    	    						if(obj.lsMonitorVehicleStatusBO && !!parseInt(obj.lsMonitorVehicleStatusBO.electricityValue)){
    	    							refreshVoltage(parseInt(obj.lsMonitorVehicleStatusBO.electricityValue));
    	    						}
    	    					}
    	    				}else{
    	    					deviceData = {
    								'elockLocation': '0',
    								'elockCommuicate': '0',
    								'elockInArea': '0'
    							};
    	    					if(!systemModules.isAreaOn) {
    	    						delete deviceData.elockInArea;
    	    					}
    	    					deviceStatus = deviceData;
    	    					invalidDeviceStatus(trackingDeviceNumber);
    	    					var $panel = $("input[name=trackingDeviceNumber][value=" + trackingDeviceNumber + "]").closest(".panel-body");
    	    					if($panel && $panel.length > 0) {
    								refreshVoltage(0);
    	    					}
    	    				}
    	    			}
        			}
        		});
    		}, 3000);
    	}
    	refreshTimeout[trackingDeviceNumber] = setTimeout(function(){
			refreshDeviceInfo(trackingDeviceNumber);
		},5000);
	}
}
function bindingRelation(){
	if(esealNumberArray.length > 0 || sensorNumberArray.length > 0) {
		$("#btnActivate").html($.i18n.prop('trip.button.bindingRelation') + '...').attr("disabled", "disabled");
		setBindingRelation();//绑定封条
	} else {
		enableActivateButton();
	}
}
function enableActivateButton(){
	$("#btnActivate").html($.i18n.prop('trip.activate.button.activate')).removeAttr("disabled");
}
/**
 * 设定对应的检出地点和路线
 * @param checkoutPortId
 */
function setCheckoutAndRoute(checkoutPortId){
	if(checkoutPortId) {
		var checkinPort = $("#tripVehicleVO\\.checkinPort").val();
		$.get(root + "/monitorroutearea/findRouteByCheckinoutPort.action", {"s_startId": checkinPort, "s_endId": checkoutPortId, "s_routeAreaType": '0'}, function(data){
			if(!needLogin(data)) {
				if(data && data.total > 0) {
					var $routeId = $("#tripVehicleVO\\.routeId");
					var html = [];
					html.push('<option value=""></option>');
					$.each(data.rows, function(index, item){
						if(checkoutPortId == item.endId) {
							$("div#timeCost").html(convertTime(item.routeCost));
						}
						html.push("<option value='" + item.routeAreaId + "' data-timecost='" + item.routeCost + 
								"' data-startid='" + item.startId + "' data-startname='" + item.startName + 
								"' data-endid='" + item.endId + "' data-endname='" + item.endName + "' " + (checkoutPortId == item.endId ? 'selected': '') + ">" + item.routeAreaName + "</option>");
					});
					$routeId.html(html.join(''));
					validateRoute();
				}
			}
		}, 'json');
	}else{
		$("#tripVehicleVO\\.routeId").children('option:first').nextAll().remove();
		$("div#timeCost").html('');
	}
}
/**
 * 校验是否施封
 */
function validateSetLocked(){
	return true;
	if(!elockUtil.isLocked()){
		bootbox.alert($.i18n.prop("trip.info.device.not.locked"));
		return false;
	}
	return true;
}
/**
 * 行程激活前的校验
 */
function checkActivate(){
	/*
	if($("#vehicleTable tbody tr").length < 1) {
		bootbox.alert($.i18n.prop('trip.message.vehicleInfo.required'));
		return false;
	}
	*/
	return validateDeviceStatus() && validateSetLocked() && validateFiles() && validateVehicleInfos();
}
/**
 * 关锁操作日志记录
 * @param type 操作类型
 */
function elockLog(type) {
	$.get(root + "/monitortrip/elockLog.action", {"type": type, "trackingDeviceNumber": $("#tripVehicleVO_trackingDeviceNumber").val()}, function(data){
		if(!needLogin(data)) {
			if(data) {
				console.log(type, $("#tripVehicleVO_trackingDeviceNumber").val());
			}
		}
	}, 'json');
}
function setDefaultCheckout(){
	//默认选中第一个检出口岸及其路线
	var $checkoutPort = $("#tripVehicleVO\\.checkoutPort").children("option:eq(1)");
	if($checkoutPort.length > 0) {
		$checkoutPort.attr("selected", true);
		$checkoutPort[0].selected = true;
		setCheckoutAndRoute($checkoutPort.val());
	}
}
/**
 * 判断文本框是否空值
 * @param {Object} index
 * @param {Object} element
 * @param {Object} array
 */
function inputIsEmpty(index, element, array){
	return /^\s*$/.test(element.value);
}
/**
 * 校验车辆文本框、下拉框是否空值
 * @returns
 */
function checkVehicleInput(){
	//return $("#collapseVehicle .panel-body input[type=text][name!=esealNumberInput][name!=sensorNumberInput],#collapseVehicle .panel-body select").some(inputIsEmpty);
	return $("#collapseVehicle .panel-body input[type=text][name!=esealNumberInput][name!=sensorNumberInput][name!=tripVehicleVO_trailerNumber][name!=tripVehicleVO_driverIdCard][name!=tripVehicleVO_driverCountry]")
			.some(inputIsEmpty);
}
function validateVehicleInfos(){
	if($("#myTab>li").length < 2) {
		bootbox.alert($.i18n.prop("trip.message.vehicleInfo.save.required"));
		bootstrapValidator.disableSubmitButtons(false);
		return false;
	}
	return true;
}
/**
 * 校验单个车辆信息
 */
function validateSingleVehicle(){
	if(checkVehicleInput()) {
		bootbox.alert($.i18n.prop("trip.message.input.required"));
		return false;
	}
	/*
	if(esealNumberArray.length < 1) {
		bootbox.alert($.i18n.prop("trip.message.esealNum.required"));
		return false;
	}
	if(sensorNumberArray.length < 1) {
		bootbox.alert($.i18n.prop("trip.message.sensorNum.required"));
		return false;
	}
	*/
	// 关锁号只能是字母和数字，否则车辆状态表建表时，表名非法
	if(!/^[\da-zA-Z]+$/.test($("#tripVehicleVO_trackingDeviceNumber").val())) {
		bootbox.alert($.i18n.prop('trip.label.trackingDeviceNumber') + $.i18n.prop("trip.message.input.letterNumber"), function(){
			$("#tripVehicleVO_trackingDeviceNumber").val('');
		});
		return false;
	}
	return true;
}
/**
 * 车辆对象转为input输入框
 * @param {Object} vehicle 车辆对象
 * @param {Object} index 被编辑的Tab页索引
 */
function vehicleToInput(vehicle, index){
	var keys = Object.keys(vehicle);
	//$(".tab-pane").eq(index).html($("#newVehicleTab").html());
	$(".tab-pane").eq(index).children().replaceWith($("#newVehicleTab").children());
	
	$("#newVehicleTab").html('');
	for(var i in keys) {
		$("#tripVehicleVO_" + keys[i]).val(vehicle[keys[i]]);
	}
	$("#tripVehicleVO_goodsType").val(vehicle.goodsType.split(/\s*,\s*/));
	
	emptyEsealNumSensorNum();
	vehicle.esealNumber.split(/\s*,\s*/).forEach(appendEsealNums);
	vehicle.sensorNumber.split(/\s*,\s*/).forEach(appendSensorNums);
	$("#btnNewVehicle").hide();
	$("#btnSaveVehicle, #btnCancelVehicle").data('edit', true);
}
/**
 * 清空子锁号、传感器号数组
 */
function emptyEsealNumSensorNum(){
	esealNumberArray = [];
	sensorNumberArray = [];
	$(".esealNumber-list ul, .sensorNumber-list ul").empty();
}
/**
 * 校验车辆是否在途
 * @param num
 */
function checkVehiclePlateNumber(num){
	$(".tab-pane input[name=vehiclePlateNumber]").each(function(){
		if($(this).val() == num) {
			bootbox.alert($.i18n.prop('trip.message.vehicle.exists', num), function(){
				$("#tripVehicleVO_vehiclePlateNumber").val('');
			});
			return false;
		}
	});
	$.get(root + "/monitortrip/findLatestByVehiclePlateNumber.action", {"vehiclePlateNumber": num}, function(data){
		if(!needLogin(data)) {
			if(!data || data.isOnWay) {
				bootbox.alert($.i18n.prop("trip.message.vehicle.onway"), function(){
					$("#tripVehicleVO_vehiclePlateNumber").val('');
				});
				return false;
			}
		}
	}, 'json');
}
/**
 * 根据货物类型编码获取名称
 * @param goodsType
 */
function getGoodsTypeName(goodsType) {
	if(!goodsType) {
		return '';
	}
	if(!$.isArray(goodsType)) {
		goodsType = goodsType.split(/\s*,\s*/);
	}
	return goodsType.map(function(num){
		return $.i18n.prop('GoodsType.GOODS_TYPE' + num);
	}).join();
}
/**
 * 根据风险级别编码获取名称
 * @param status
 */
function getRiskStatusName(status) {
	if(!systemModules.isRiskOn) {
		return '';
	}
	if(['0','1','2'].indexOf(status) < 0) {
		return '';
	}
	return [$.i18n.prop('trip.label.riskStatus.low'),$.i18n.prop('trip.label.riskStatus.middle'),$.i18n.prop('trip.label.riskStatus.high')][Number(status)];
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
/**
 * 将设备状态置位有效
 * @param trackingDeviceNumber
 */
function validDeviceStatus(trackingDeviceNumber){
	var $panel = $("input[name=trackingDeviceNumber][value=" + trackingDeviceNumber + "]").closest(".panel-body");
	if($panel && $panel.length > 0) {
		$panel.find("span[name=elockLocation]").removeClass('glyphicon-remove status-error').addClass('glyphicon-ok status-ok').empty();
		$panel.find("span[name=elockCommuicate]").removeClass('glyphicon-remove status-error').addClass('glyphicon-ok status-ok').empty();
		if(systemModules.isAreaOn) {
			$panel.find("span[name=elockInArea]").removeClass('glyphicon-remove status-error').addClass('glyphicon-ok status-ok').empty();
		}
	}
}
/**
 * @param trackingDeviceNumber
 */
function loadingDeviceStatus(trackingDeviceNumber){
	var loading = '<img style="width:14px;height: 14px;" src="' + root + '/static/images/loading.gif"/>';
	var $panel = $("input[name=trackingDeviceNumber][value=" + trackingDeviceNumber + "]").closest(".panel-body");
	if($panel && $panel.length > 0) {
		$panel.find("span[name=elockLocation]").removeClass("glyphicon-remove status-error glyphicon-ok status-ok").html(loading);
		$panel.find("span[name=elockCommuicate]").removeClass("glyphicon-remove status-error glyphicon-ok status-ok").html(loading);
		if(systemModules.isAreaOn) {
			$panel.find("span[name=elockInArea]").removeClass("glyphicon-remove status-error glyphicon-ok status-ok").html(loading);
		}
	}
}
/**
 * 将设备状态置位无效
 * @param trackingDeviceNumber
 */
function invalidDeviceStatus(trackingDeviceNumber){
	var $panel = $("input[name=trackingDeviceNumber][value=" + trackingDeviceNumber + "]").closest(".panel-body");
	if($panel && $panel.length > 0) {
		$panel.find("span[name=elockLocation]").removeClass('glyphicon-ok status-ok').addClass('glyphicon-remove status-error').empty();
		$panel.find("span[name=elockCommuicate]").removeClass('glyphicon-ok status-ok').addClass('glyphicon-remove status-error').empty();
		if(systemModules.isAreaOn) {
			$panel.find("span[name=elockInArea]").removeClass('glyphicon-ok status-ok').addClass('glyphicon-remove status-error').empty();
		}
	}
}