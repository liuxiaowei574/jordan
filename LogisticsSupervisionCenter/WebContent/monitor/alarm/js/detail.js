$(function() {
	$("#btnBack").on("click", function(){
		history.back(-1);
	});
	
	$("#checkinPictures img, #checkoutPictures img").on("click", function(){
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
		
		$('#imageModal').on('hidden.bs.modal', function () {
			$("#imageModal .modal-content").empty();
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
