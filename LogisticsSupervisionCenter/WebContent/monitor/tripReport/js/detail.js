$(function() {
	$("#btnBack").on("click", function(){
		history.back(-1);
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
	
	$("#myTabContent").on("click", "img", function(){
		$("#imageModal .modal-content").html($(this).clone());
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
	
	$("#btnPass").on("click", function(){
		var tripid = $("#tripId").val();
		//$.get(root + '/monitortrip/doPass.action', {"s_tripId": tripid}, function(data){
		$.get(root + '/monitortrip/passTrip.action', {'tripId': tripid, "userId": sessionUserId}, function(data){
			if(!needLogin(data)) {
				if(data && data.result) {
					bootbox.success($.i18n.prop('trip.info.success'), function(){
						window.location.reload();
					});
				}else{
					bootbox.alert($.i18n.prop('trip.info.failed') + ':' + (data.message || ''), function(){
						window.location.reload();
					});
				}
			}
		}, 'json');
	});
	
	$("#btnNoPass").on("click", function(){
		$('#noPassModal').removeData('bs.modal');
		$('#noPassModal').modal({
			backdrop: 'static', 
			keyboard: false
		});
		
		$('#noPassModal').on('hidden.bs.modal', function () {
			$("#noPassReason").val('');
		});
	});
	$('#noPassModal').on('loaded.bs.modal', function(e) {
		$(this).modal('show');
	});
	//模态框登录判断
	$('#noPassModal').on('show.bs.modal', function(e) {
		var content = $(this).find(".modal-content").html();
		needLogin(content);
	});
	
	$("#noPassModal").on("click", "#nopassSubmit", function(){
		var tripid = $("#tripId").val();
		//$.get(root + '/monitortrip/doNoPass.action', {"s_tripId": tripid, "reason" : $("#reason").val()}, function(data){
		$.get(root + '/monitortrip/noPassTrip.action', {'tripId': tripid, "userId": sessionUserId, "reason" : $("#noPassReason").val() || ''}, function(data){
			if(!needLogin(data)) {
				if(data && data.result) {
					bootbox.success($.i18n.prop('trip.info.success'), function(){
						window.location.reload();
					});
				}else{
					bootbox.alert($.i18n.prop('trip.info.failed') + ':' + (data.message || ''), function(){
						window.location.reload();
					});
				}
			}
		}, 'json');
	});
	
	//导出Word
	$("#btnExportWord").on("click", function(){
		$(this).attr("disabled", true);
		setTimeout(function(){
			$("#btnExportWord").removeAttr("disabled");
		}, 4000);
		exportWord();
	});
	
	//撤销行程
	$("#btnRevoke").on("click", function(){
		bootbox.confirm($.i18n.prop("trip.info.revoke.sure"), function(result){
			if(result) {
				$.get(root + '/monitortrip/revokeTrip.action', {"tripId": $("#tripId").val()}, function(data){
					if(!needLogin(data)) {
						if(data && data.result) {
							bootbox.success($.i18n.prop('trip.info.success'), function(){
								window.location.reload();
							});
						}else{
							bootbox.alert($.i18n.prop('trip.info.failed') + ':' + (data.message || ''), function(){
								window.location.reload();
							});
						}
					}
				}, 'json');
			}
		});
	});
	
	//初始化Jquery i18n
	initJqueryI18n();
});
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
 * 导出到Word
 */
function exportWord(){
	//创建一个新表单
	$("form[id^=dlform]").remove();
	var formid = "dlform-" + new Date().getTime();
	var form = '<form id="' + formid + '" style="display: none;" target="" method="post" action="' + root + '/monitortripreport/exportWord.action">';
	form += '<input type="hidden" name="s_tripId" id="s_tripId" value="' + $("#tripId").val() + '"/>';
	form += '</form>';
	
	$("body").append(form);//将表单放置在web中
	$("#" + formid).submit();//表单提交
}