<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<style>
#tripDetail .table-striped > tbody > tr:nth-of-type(even), #tripDetailCheckOut .table-striped > tbody > tr:nth-of-type(even) {
	background-color: #f9f9f9;
}
#tripDetail .table-striped td, #tripDetailCheckOut .table-striped td{
	border-right-color: rgb(221, 221, 221);
	border-right-style: solid;
	border-right-width: 1px;
}
#tripDetail table td, #tripDetailCheckOut table td {
	border: 1px solid #ddd;
}
</style>

<!-- 行程请求通知Modal -->
<div class="modal fade add_user_box" id="tripMsgModal" tabindex="-1"
	role="dialog" aria-labelledby="noticeModalMsgTitle">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="noticeModalTitle"><fmt:message key="system.notice.tip"/></h4>
			</div>
			<div class="modal-body">
				<form id="msgForm" class="form-horizontal row">
					<input type="hidden" id="log.noticeId" name="log.noticeId">
					<input type="hidden" id="log.receiveUser" name="log.receiveUser">
					<div class="col-md-10">
						<div class="form-group ">
							<label class="col-sm-4 control-label"><fmt:message key="notice.title"/></label>
							<div class="col-sm-8">
								<input type="text" id="msgTitle" class="form-control input-sm" readonly="readonly">
							</div>
						</div>
						<div class="form-group ">
							<label class="col-sm-4 control-label"><fmt:message key="notice.content"/></label>
							<div class="col-sm-8">
								<div id="msgContent" style="border: 1px solid #ccc; background-color: #eee; border-radius: 3px; padding: 5px 10px; height: auto; min-height: 1px;" class="col-sm-12">
									<div class="control-label" id="content" style="text-align: left;"></div>
									<div class="control-label" id="msgInfo" style="text-align: left;"></div>
									<%-- 
									<label class="col-sm-4 control-label"><fmt:message key="trip.info.requestType"/>:</label>
									<div class="col-sm-8 control-label" id="msgType"></div>
									<div class="clearfix"></div>
									<label class="col-sm-4 control-label"><fmt:message key="trip.info.tripInfo"/>:</label>
									<div class="col-sm-8 control-label" id="msgInfo"></div>
									<div class="clearfix"></div>
									<label class="col-sm-4 control-label"><fmt:message key="trip.info.requestPerson"/>:</label>
									<div class="col-sm-8 control-label" id="msgUser"></div>
									<div class="clearfix"></div>
									 --%>
								</div>
								<input type="hidden" name="requestTripId" id="requestTripId"/>
							</div>
						</div>
						<div class="form-group hidden" id="reasonDiv">
							<label class="col-sm-4 control-label"><fmt:message key="trip.info.reason"/></label>
							<div class="col-sm-8">
								<textarea rows="2" cols="15" id="reason" name="reason" class="form-control input-sm"></textarea>
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-danger" data-dismiss="modal" id="pass"><fmt:message key="trip.button.pass"/></button>
				<button type="button" class="btn btn-danger" id="nopass"><fmt:message key="trip.button.noPass"/></button>
				<button type="button" class="btn btn-danger hidden" data-dismiss="modal" id="nopassSubmit"><fmt:message key="trip.button.submit"/></button>
				<button type="button" class="btn btn-darch hidden" id="nopassCancel"><fmt:message key="trip.button.cancel"/></button>
			</div>
		</div>
	</div>
</div>
<!-- /Modal -->

<script>
var bootstrapValidator;
$(function(){
	$("#pass").on("click", function(){
		$.ajax({
			url : root + '/monitortrip/passTrip.action',
			type : "post",
			dataType : "json",
			data : {'tripId': $("#requestTripId").val(), "userId": '${sessionScope.sessionUser.userId}'},
			success : function(data) {
				var needLoginFlag = false;
				if(typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
					needLoginFlag = needLogin(data);
				}
				if(!needLoginFlag) {
					if(data.result) {
						$("#tripDetail, #tripDetailCheckOut").hide();
						bootbox.success($.i18n.prop('trip.info.success'), function() {
							initModal();
						});
					}else{
						data.message && bootbox.alert($.i18n.prop('trip.info.failed') + ':' + data.message);
					}
				}
			}
		});
	});
	$("#nopass, #nopassCancel").on("click", function(){
		$("#pass, #nopass, #nopassSubmit, #nopassCancel, #reasonDiv").toggleClass("hidden");
		$("#reason").val('');
	});
	$("#nopassSubmit").on("click", function(){
		$.ajax({
			url : root + '/monitortrip/noPassTrip.action',
			type : "post",
			dataType : "json",
			data : {'tripId': $("#requestTripId").val(), "userId": '${sessionScope.sessionUser.userId}', "reason": $("#reason").val() || ''},
			success : function(data) {
				var needLoginFlag = false;
				if(typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
					needLoginFlag = needLogin(data);
				}
				if(!needLoginFlag) {
					if(data.result) {
						$("#tripDetail, #tripDetailCheckOut").hide();
						bootbox.success($.i18n.prop('trip.info.success'), function() {
							initModal();
						});
					}else{
						data.message && bootbox.alert($.i18n.prop('trip.info.failed') + ':' + data.message);
					}
				}
			}
		});
	});
	$("#btnClose, #btnClose1").on("click", function(){
		$("#tripDetail, #tripDetailCheckOut").hide();
	});
	$("#checkinPictureDiv, #checkinPicture1Div, #checkoutPictureDiv").on("click", "img", function(){
		var clone = $(this).clone();
		clone.css("max-height", "").css("border-radius", "6px");
		$('#imageModal').removeData('bs.modal');
		$("#imageModal .modal-content").append(clone);
		$('#imageModal').modal({
			show : false,
			backdrop: true, 
			keyboard: true
		}).modal('show').css("z-index", 1052);
		$("#imageModal .modal-content").css("background-color", "rgba(255, 255, 255, 0)");
		
		$('#imageModal img').on("click", function(){
			$("#imageModal img").empty();
			$('#imageModal').modal("hide");
		});
	});
	$("#btnCloseDetail, #btnCloseDetail1").on("click", function(){
		$("#tripDetail, #tripDetailCheckOut").hide();
	});
});
function initModal(){
	$("#pass, #nopass").removeClass("hidden");
	$("#nopassSubmit, #nopassCancel, #reasonDiv").addClass("hidden");
	$("#reason").val('');
}
function bootstrapValidatorForm() {
	$('#msgForm').bootstrapValidator({
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
		var formData = new FormData($form[0]);
		$.ajax({
			url: $form[0].action,
			type: 'POST',
			contentType: false,
			data: formData,
			dataType: 'JSON',
			processData: false,
			success: function(data) {
				try {
					var needLoginFlag = false;
					if(typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
						needLoginFlag = needLogin(data);
					}
					if(!needLoginFlag) {
						if (data && data.result) {
							/*
							bootbox.success($.i18n.prop("trip.activate.success"), function() {  
								location.href = location.href.replace(/#$/, '');
							});
							*/
						} else if (data.message) {
							bootbox.alert($.i18n.prop("trip.activate.failed") + ":" + data.message);
						}
					}
				} catch (e) {}
			}
		});
    });
	bootstrapValidator = $('#msgForm').data('bootstrapValidator');
}

/**
 * 列出检入图像
 * @param {Object} checkinPicture
 */
function showCheckinPicture(checkinPicture, type){
	checkinPicture = checkinPicture && $.trim(checkinPicture).split(/\s*,\s*/);
	if(checkinPicture && checkinPicture.length > 0) {
		var liHtml = [], innerHtml = [], controlHtml = [], outterHtml = [];
		outterHtml.push('<div id="' + type + '" class="carousel slide" data-ride="carousel">');
		outterHtml.push('	<ol class="carousel-indicators">');
		
		for(var i = 0; i < checkinPicture.length; i++) {
			var imageName = checkinPicture[i].slice(checkinPicture[i].lastIndexOf('/') + 1);
			//轮播指标
			liHtml.push('<li data-target="#' + type + '" data-slide-to="' + i + '" ' + (i == 0 ? 'class="active"': '') + '></li>');
			
			//轮播项目
			innerHtml.push('<div class="item ' + (i == 0 ? 'active' : '' ) + '">');
			innerHtml.push('	<a href="javascript:void(0);">');
			innerHtml.push('		<img style="max-height: 120px;" src="' + tripPhotoPathHttp + checkinPicture[i] + '" alt="' + imageName + '">');
			innerHtml.push('	</a>');
			innerHtml.push('</div>');
		}
		outterHtml.push(liHtml.join(''));
		outterHtml.push('</ol>');
		outterHtml.push('<div class="carousel-inner" role="listbox">');
		outterHtml.push(innerHtml.join(''));
		outterHtml.push('</div>');
		
		//轮播导航
		outterHtml.push('<a class="left carousel-control" href="#' + type + '" role="button" data-slide="prev">');
		outterHtml.push('	<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>');
		outterHtml.push('	<span class="sr-only">Previous</span>');
		outterHtml.push('</a>');
		outterHtml.push('<a class="right carousel-control" href="#' + type + '" role="button" data-slide="next">');
		outterHtml.push('	<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>');
		outterHtml.push('	<span class="sr-only">Next</span>');
		outterHtml.push('</a>');
		
		outterHtml.push('</div>');
		
		//$("#checkinPicture>.carousel-indicators").html(liHtml.join(''));
		//$("#checkinPicture>.carousel-inner").html(innerHtml.join('')).after(controlHtml.join(''));
		$("#" + type + "Div").html(outterHtml.join(''));
	}
}
</script>