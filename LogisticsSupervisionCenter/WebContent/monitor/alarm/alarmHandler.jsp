<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title></title>

</head>

<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel"> <fmt:message key="alarm.label.alarmHandler" />
		</h4>
	</div>
	<form id="alarmHanlerForm">
		<div class="modal-body">
			<div class="col-md-6">
				<div class="form-group ">
					<label class="col-sm-4 control-label" for="alarmDeal.dealMethod"><fmt:message
							key="alarm.label.dealMethod" /></label>
					<select class="form-control input-sm" id="alarmDeal.dealMethod" name="alarmDeal.dealMethod">
						<option value="">-请选择处理方式-</option>
						<option value="0">误报警</option>
						<option value="1">正常处理</option>
					</select>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label" for="dealResultDesc"><fmt:message key="alarm.label.dealResult"/></label>
	   	            <div class="col-sm-8">
	   	    	         <textarea rows="10" cols="15" class="form-control input-sm" id="dealResultDesc" name="dealResultDesc"></textarea>
	   	            </div>
				</div>
				<div class="form-group  hidden">
					<label class="col-sm-4 control-label" for="dealResult"></label>
	   	            <div class="col-sm-8">
	   	              <input type="text" class="form-control input-sm" id="dealResult" name="dealResult">
   	               </div>
				</div>
				<div class="form-group  hidden">
					<label class="col-sm-4 control-label" for="alarmId"></label>
	   	            <div class="col-sm-8">
	   	              <input type="text" class="form-control input-sm" id="alarmId" name="alarmId">
   	               </div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="modal-footer ">
			<button type="submit" class="btn btn-danger" id="addAlarmHandlerButton">
				<fmt:message key="common.button.add" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
	</form>
	<script type="text/javascript" src="${root}/monitor/alarm/js/alarmHandler.js"></script>
	<script type="text/javascript" >
	$(function() {
		$('#addAlarmHandlerButton').on('click', function() {
			bindAlarmDealAddForm();
		});
		
	});
	/**
	 * 添加报警处理意见
	 */
	function bindAlarmDealAddForm(){
	//设置验证
	$('#alarmHanlerForm').bootstrapValidator({
		  message: $.i18n.prop("common.message.form.validator"),
	    fields: {
	  	'alarmDeal.dealMethod': {
	            validators: {
	          	  notEmpty: {}
	            }
	        },  
	    	'alarmDeal.dealResult': {
	            validators: {
	          	  notEmpty: {}
	            }
	        }
	    }
	}).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
		debugger;
	    e.preventDefault();
	    var $form = $(e.target);
	    var bv = $form.data('bootstrapValidator');
	    $("#dealResult").val("0");
	    $("#alarmId").val(alarmDealId);
	    var serialize = $form.serialize();
	    var url = '${root}/alarmdeal/addAlarmDeal.action'
		  $.post(url, serialize, function(data) {
			bootbox.alert($.i18n.prop("system.user.add.success"));
			$('#alarmHandlerModal').modal('hide');
		  }, "json");
	  });
	}
	</script>
</body>

</html>	