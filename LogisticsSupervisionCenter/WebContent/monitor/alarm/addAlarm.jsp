<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>

</head>

<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel"> <fmt:message key="alarm.label.addTitle" />
		</h4>
	</div>
	<form id="addAlarmForm" class="row" method="post">
		<div class="modal-body">
			<div class="col-md-12">
				<div class="form-group ">
					<label class="col-sm-4 control-label" for="lsMonitorAlarmBO.alarmTypeId"><fmt:message
							key="alarm.label.alarmTypeName" /></label>
					<s:select name="lsMonitorAlarmBO.alarmTypeId"
									theme="simple"
									emptyOption="true"
									cssClass="form-control"
									list="@com.nuctech.ls.model.util.AlarmType@values()"
									listKey="alarmType"
									listValue="key"
									value="%{#request.pageQuery.filters.alarmType}"
									>
					 </s:select>
				</div>
				<div class="clearfix margin15"> </div>
				<div class="form-group ">
					<label class="col-sm-4 control-label" for="lsMonitorAlarmBO.alarmContent"><fmt:message key="alarm.label.alarmContent"/></label>
	   	            <div class="col-sm-8">
	   	    	         <textarea rows="5" cols="15" class="form-control input-sm" id="lsMonitorAlarmBO.alarmContent" name="lsMonitorAlarmBO.alarmContent"></textarea>
	   	            </div>
				</div>
<!-- 				<div class="form-group  hidden"> -->
<!-- 					<label class="col-sm-4 control-label" for="isPunish"></label> -->
<!-- 	   	            <select id="isPunish" -->
<!-- 							name="isPunish" class="form-control"> -->
<!-- 							<option value="是否处罚">是否处罚</option> -->
<!-- 							<option value="0">处罚</option> -->
<!-- 							<option value="1">不处罚</option> -->
<!-- 					</select> -->
<!-- 				</div> -->
				<div class="form-group hidden">
					<label class="col-sm-4 control-label" for="lsMonitorAlarmBO.punishContent"><fmt:message key="alarm.label.punishContent"/></label>
	   	            <div class="col-sm-8">
	   	    	         <textarea rows="5" cols="15" class="form-control input-sm" id="lsMonitorAlarmBO.punishContent" name="lsMonitorAlarmBO.punishContent"></textarea>
	   	            </div>
				</div>
				<div class="form-group  hidden">
					<label class="col-sm-4 control-label" for="lsMonitorAlarmBO.alarmLatitude"></label>
	   	            <div class="col-sm-8">
	   	    	         <textarea rows="5" cols="15" class="form-control input-sm" id="lsMonitorAlarmBO.alarmLatitude" name="lsMonitorAlarmBO.alarmLatitude"></textarea>
	   	            </div>
				</div>
				<div class="form-group  hidden">
					<label class="col-sm-4 control-label" for="lsMonitorAlarmBO.alarmLongitude"></label>
	   	            <div class="col-sm-8">
	   	    	         <textarea rows="5" cols="15" class="form-control input-sm" id="lsMonitorAlarmBO.alarmLongitude" name="lsMonitorAlarmBO.alarmLongitude"></textarea>
	   	            </div>
				</div>
				<div class="form-group  hidden">
					<label class="col-sm-4 control-label" for="lsMonitorAlarmBO.tripId"></label>
	   	            <div class="col-sm-8">
	   	    	         <textarea rows="5" cols="15" class="form-control input-sm" id="lsMonitorAlarmBO.tripId" name="lsMonitorAlarmBO.tripId"></textarea>
	   	            </div>
				</div>
			</div>
		</div>
		<div class="clearfix margin15"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger" id="addAlarmButton">
				<fmt:message key="common.button.add" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
		
	</form>
	<script type="text/javascript" >
    var alarmLatitude='${param.alarmLatitude}';
    var alarmLongitude = '${param.alarmLongitude}';
    var tripId = '${param.tripId}';
    var vehicleId = "${vehicleId}"
	$(function() {
		//$('#addAlarmButton').on('click', function() {
			bindaddAlarmForm();
		//});
		
		//手动报警不需要误报警类型
		$("#lsMonitorAlarmBO_alarmTypeId").children('[value=FALSE_ALARM]').remove();
		//未开启区域模块时，不需要相关报警
		if(!systemModules.isAreaOn) {
			$("#lsMonitorAlarmBO_alarmTypeId").children('[value=TARGET_ZOON],[value=ENTER_DANGEROUS_AREA]').remove();
		}
		//未开启车载台模块时，不需要相关报警
		if(!systemModules.isPatrolOn) {
			$("#lsMonitorAlarmBO_alarmTypeId").children('[value=TRACK_UNIT_ALARM]').remove();
		}
	});
	/**
	 * 添加报警处理意见
	 */
	function bindaddAlarmForm(){
		 $('#addAlarmForm').bootstrapValidator({
			  message: $.i18n.prop("common.message.form.validator"),
		      fields: {
		    	'lsMonitorAlarmBO.alarmTypeId': {
		              validators: {
		            	  notEmpty: {}
		              }
		          },  
		      	'lsMonitorAlarmBO.alarmContent': {
		              validators: {
		            	  notEmpty: {}
		              }
		          }
		      }
		  }).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
			    $("#lsMonitorAlarmBO\\.alarmLatitude").val(alarmLatitude);
			    $("#lsMonitorAlarmBO\\.alarmLongitude").val(alarmLongitude);
			    $("#lsMonitorAlarmBO\\.tripId").val(tripId);
		      e.preventDefault();
		      var $form = $(e.target);
		      var bv = $form.data('bootstrapValidator');
		      var serialize = $form.serialize();
		      var url = '${root }/monitoralarm/addAlarmByManul.action?vehicleId='+vehicleId;
			  $.post(url, serialize, function(data) {
				    var needLoginFlag = false;
					if(typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
						needLoginFlag = needLogin(data);
					}
					if(!needLoginFlag) {
						$('#addAlarmModal').modal('hide');
						if(data.result) {
							var alarmBO = data.alarmReportVO;
							var lsalarmBo = data.lsMonitorAlarm;
							if(alarmBO==null){
								bootbox.alert($.i18n.prop("alarm.label.alarmId.cannotFind"));
							}else{
								if(alarmMakerHasForDeal){
									var arr = new Array();
									arr.push(alarmMakerHasForDeal);
									alarmMakerHasForDeal.setMap(null);
									GisClearOverlays(arr);
								}
								bootbox.alert($.i18n.prop("alarm.label.addAlarmSuccess"),function(){
									//手动添加报警成功后，刷新车辆报警列表
									findAllVehicleStatus(false);
   									
									//手动添加报警成功后，更新报警列表，不用弹出
									pop(alarmBO.vehicleId, false);
									
								});
									$(document).undelegate("#alarmHandlerBtn_"+alarmBO.alarmId,"click"); 
									$(document).undelegate("#patrolHandlerBtn_"+alarmBO.alarmId,"click"); 
									var alarmIcon = getAlarmIconByTypeAndLevel(alarmBO.alarmTypeId, alarmBO.alarmLevelId);
									var alarmMarker = GisCreateMarker({
										lat : alarmBO.alarmLatitude,
										lng : alarmBO.alarmLongitude
									}, alarmIcon, $.i18n.prop('AlarmType.' + alarmBO.alarmTypeId),JSON.stringify(alarmBO));
									//debugger;
									$("#bsound")[0].play();
									var alarmContent =createAlarmContent(alarmBO);
									//GisShowInfoWindow(alarmMarker,alarmContent);
									GisAddEventForVehicle(alarmMarker, "click", function() {
					                    var d = dialog({
					                           id: alarmBO.alarmId,
					                           title: vehiclePlateNUmber,//$.i18n.prop('trip.info.message'),
					                           content: alarmContent,
					                           resize:true
					                       });
					                       d.show();
					               });
									GisSetShowFront(alarmMarker);
									$(document).delegate("#alarmHandlerBtn_"+alarmBO.alarmId,"click", function(e){
		 								$(document).undelegate("#alarmHandlerBtn_"+alarmBO.alarmId,"click");
		 								//e.preventDefault();
										alarmDealClick(alarmBO);
										alarmMakerWaitForDeal = alarmMarker;
									});
		
									$(document).delegate("#patrolHandlerBtn_"+alarmBO.alarmId,"click", function(e){
		 								$(document).undelegate("#patrolHandlerBtn_"+alarmBO.alarmId,"click"); 
		 								//e.preventDefault();
										patrolHandlerClick(alarmBO);
										//$("#patrolHandlerBtn_"+data.alarmId).attr("disabled",true)
										alarmMakerWaitForDeal = alarmMarker;
									});
							}
						} else {
							bootbox.alert($.i18n.prop('alarm.label.addAlarmFailed') + ":" + data.message);
						}
						
					}
				  }, "json");
		  });
	
	}
	</script>
</body>

</html>	