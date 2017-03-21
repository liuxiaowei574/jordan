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
