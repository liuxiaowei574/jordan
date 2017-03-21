<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
</head>
<body>
	<form id="patrolForm">
		<ul class="Custom_list hidden" id="mergePatrol">
			<li>
				<label class="col-sm-6 control-label" for="potralUser"><fmt:message key="main.list.commonPatrol.potralUser" /></label>
				<!-- <input type="text" class="form-control input-sm" id="potralUser" name="potralUser"> --> 
				<select class="form-control input-sm" id="potralUser" name="potralUser">
					<option value=""><fmt:message key="main.list.option.select" /></option>
				</select>
				
				<label class="col-sm-6 control-label" for="belongToArea"><fmt:message key="main.list.commonPatrol.belongTo.area" /></label>
				<select class="form-control input-sm" id="belongToArea" name="belongToArea">
					<option value=""><fmt:message key="main.list.option.select" /></option>
				</select>
				
				<input id="patrolId" name="patrolId" type="hidden" />
				
				<%-- 
				<label class="col-sm-6 control-label" for="belongToPort1"><fmt:message key="main.list.commonPatrol.belongToPort" /></label>
				<select class="form-control input-sm" id="belongToPort1" name="belongToPort1">
					<option value=""><fmt:message key="main.list.option.select" /></option>
				</select>
				 --%>

				<label class="col-sm-6 control-label" for="trackUnitNumber"><fmt:message key="main.list.commonPatrol.trackUnitNumber" /></label> 
				<select class="form-control input-sm" id="trackUnitNumber" name="trackUnitNumber">
					<option value=""><fmt:message key="main.list.option.select" /></option>
				</select>

				<button id="addPatrolBtn" type="button" class="btn btn-danger">
					<fmt:message key="common.button.add" />
				</button>
				<button id="editPatrolBtn" type="button" class="btn btn-danger">
					<fmt:message key="common.button.modify" />
				</button>
				<button id="patrolReset" type="button" class="btn btn-darch">
					<fmt:message key="common.button.reset" />
				</button></li>

		</ul>
	</form>

	<script>
     $('#addPatrolBtn').on('click', function () {
    	 addPatrol();
     });
     function addPatrol() {
    	var param = $("#patrolForm").serialize();
  		var portUrl = getRootPath() + "patrol/addPatrol.action";
  		$.ajax({
			type : "POST",
			url : portUrl,
			data: param,
			error : function(e, message, response) {
				console.log("Status: " + e.status + " message: " + message);
			},
			success : function(obj) {
				var needLoginFlag = false;
				if(typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
					needLoginFlag = needLogin(obj);
				}
				if(!needLoginFlag) {
					if(obj.success){
						clearAllOverlays(true);
						bootbox.success($.i18n.prop("map.routeArea.save.success"));
						//findAllPartrols();
		            	findAllPatrolStatus(true);
					}else{
						bootbox.error($.i18n.prop("map.routeArea.save.failure"));
					}
				}
				
			}
		});

  	}
     
     $('#editPatrolBtn').on('click', function () {
    	 updatePatrol();
     });
     
     function updatePatrol() {
    	var param = $("#patrolForm").serialize();
  		var portUrl = getRootPath() + "patrol/updatePatrol.action";
  		$.ajax({
			type : "POST",
			url : portUrl,
			data: param,
			error : function(e, message, response) {
				console.log("Status: " + e.status + " message: " + message);
			},
			success : function(obj) {
				var needLoginFlag = false;
				if(typeof needLogin != 'undefined' && $.isFunction(needLogin)) {
					needLoginFlag = needLogin(obj);
				}
				if(!needLoginFlag) {
					if(obj.success){
						clearAllOverlays(true);
						bootbox.success($.i18n.prop("map.routeArea.update.success"));
						//findAllPartrols();
		            	findAllPatrolStatus(true);
					}else{
						bootbox.error($.i18n.prop("map.routeArea.update.failure"));
					}
				}
			}
		}); 

  	}
   </script>

</body>