<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="system.user.edit.title" /></title>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel">
			<fmt:message key="system.user.edit.title" />
		</h4>
	</div>
	<form class="form-horizontal row" id="userEditForm" method="post">
		<input type="hidden" class="form-control input-sm"
			id="systemUser.userId" name="systemUser.userId"
			value="${systemUser.userId }">
		<input type="hidden"
			class="form-control input-sm" id="systemUser.userPassword"
			name="systemUser.userPassword" value="${systemUser.userPassword}">
		<input type="hidden" class="form-control input-sm"
			id="systemUser.isEnable" name="systemUser.isEnable"
			value="${systemUser.isEnable}">
		<div class="modal-body">
				<div class="form-group ">
					<label class="col-sm-2 control-label" for="systemUser.userAccount"><em>*</em><fmt:message
							key="user.userAccount" /></label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-sm"
							id="systemUser.userAccount" name="systemUser.userAccount"
							value="${systemUser.userAccount }">
					</div>
					
					<label class="col-sm-2 control-label" for="systemUser.userName"><em>*</em><fmt:message
							key="user.userName" /></label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-sm"
							id="systemUser.userName" name="systemUser.userName"
							value="${systemUser.userName }">
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-2 control-label" for="s_userRole"><em>*</em><fmt:message
							key="user.role" /></label>
					<div class="col-sm-3" id="role">
						<select style="font-size: 12px;" id="s_userRole" name="s_userRole"
							class="form-control" onchange="changeOrganization()">
							<option value=""><fmt:message key="please.choose" /></option>
							<c:forEach var="systemRole" items="${roleList}">
								<option value="${systemRole.roleId }"
									<c:if test="${systemRole.roleId eq pageQuery.filters.userRole}">selected</c:if>><fmt:message
										key="system.role.${systemRole.roleName}" /></option>
							</c:forEach>
						</select>
					</div>
					
					<label class="col-sm-2 control-label" for="s_userPort"><em>*</em><fmt:message
							key="user.portID" /></label>
					<div class="col-sm-3" id="port">
						<select style="font-size: 12px;" id="s_userPort" name="s_userPort"
							class="form-control" onchange="changeRole()">
							<option value=""><fmt:message key="please.choose" /></option>
							<c:forEach var="systemDepartmentBO" items="${portList}">
								<option value="${systemDepartmentBO.organizationId}"
									<c:if test="${systemDepartmentBO.organizationId eq pageQuery.filters.userPort}">selected</c:if>>${systemDepartmentBO.organizationName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				
				<div class="form-group ">
					<label class="col-sm-2 control-label" for="systemUser.userPhone"><fmt:message
							key="user.userPhone" /></label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-sm"
							id="systemUser.userPhone" name="systemUser.userPhone"
							value="${systemUser.userPhone }">
					</div>
					
					<label class="col-sm-2 control-label" for="systemUser.userAddress"><fmt:message
							key="user.userAddress" /></label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-sm"
							id="systemUser.userAddress" name="systemUser.userAddress"
							value="${systemUser.userAddress }">
					</div>
				</div>
				
				<div class="form-group ">
					<label class="col-sm-2 control-label" for="systemUser.userEmail"><fmt:message
							key="user.userEmail" /></label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-sm"
							id="systemUser.userEmail" name="systemUser.userEmail"
							value="${systemUser.userEmail }">
					</div>
					
					<label class="col-sm-2 control-label" for="systemUser.position"><fmt:message
							key="user.position" /></label>
					<div class="col-sm-3">
						<input type="text" class="form-control input-sm"
							id="systemUser.position" name="systemUser.position"
							value="${systemUser.position}">
					</div>
				</div>
				<%-- <div class="form-group ">
					<label class="col-sm-4 control-label" for="systemUser.level"><fmt:message key="user.level" /></label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm"
							id="systemUser.level" name="systemUser.level"
							value="${systemUser.level}">
					</div>
				</div> --%>
				
				<div class="form-group ">
					<label class="col-sm-2 control-label" for="systemUser.canDealAlarm"><fmt:message key="user.canDealAlarm" /></label>
					<div class="col-sm-3">
						<select style="font-size: 10px" id="systemUser.canDealAlarm" name="systemUser.canDealAlarm" class="form-control" <c:if test="${!'2,3,4,5,6,7'.contains(pageQuery.filters.userRole)}">disabled="disabled"</c:if> >
							<option value=""></option>
							<option value="1" <c:if test="${systemUser.canDealAlarm == '1'}">selected="selected"</c:if> ><fmt:message key="user.canDealAlarm.yes" /></option>
							<option value="0" <c:if test="${systemUser.canDealAlarm == '0'}">selected="selected"</c:if> ><fmt:message key="user.canDealAlarm.no" /></option>
						</select>
					</div>
				</div>
		</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger" id="editUserButton">
				<fmt:message key="common.button.save" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
	</form>
	<script type="text/javascript">
	var bootstrapValidator;
	
	var roleList = {}, portList = [];
	<c:forEach items="${roleList}" var="item">
	roleList[String('${item.roleId}')] = '${item.roleName}';
	</c:forEach>
	
	<c:forEach items="${portList}" var="item">
	portList.push({
		organizationId: '${item.organizationId}',
		organizationName: '${item.organizationName}',
		organizationType: '${item.organizationType}'
	});
	</c:forEach>
	
	var roleType = {
			controlRoom: [], //属于Control Room的角色Id
			port: ['8'], //口岸角色
			admin: ['1'] //管理员角色
	};
	<c:forEach items="${centerRoleList}" var="item">
	roleType.controlRoom.push('${item.roleId}');
	</c:forEach>
	
	
		$(function() {
			if("${sessionScope.sessionUser.roleName}" != 'admin') {
				//$("#s_userRole").children("option[value=1]").remove();
				//$("#s_userPort").children("option[value=9003]").remove();
			}
			buildUserEditForm();
			
			$("#s_userRole").on("change", function(){
				var role = $(this).val();
				if(['2','3','4','5','6','7'].indexOf(role) > -1) {
					$("#systemUser\\.canDealAlarm").removeAttr("disabled").val('');
				} else {
					$("#systemUser\\.canDealAlarm").attr("disabled", "disabled").val('');
				}
			});
			
			changeRole();
			changeOrganization();
		});

		/**
		 * 修改用户信息
		 */
		function buildUserEditForm() {
			//设置验证
			$('#userEditForm').bootstrapValidator({
				message : $.i18n.prop("common.message.form.validator"),
				fields : {
					'systemUser.userAccount' : {
						validators : {
							notEmpty : {}
						}
					},
					'systemUser.userName' : {
						validators : {
							notEmpty : {}
						}
					},
					'systemUser.userPhone' : {
						validators : {
							digits : {}
						}
					},
					'systemUser.userEmail' : {
						validators : {
							emailAddress : {}
						}
					},
					'systemUser.position' : {
						validators : {
						}
					},
					'systemUser.level' : {
						validators : {
						}
					},
					's_userPort' : {
						validators : {
							notEmpty : {}
						}
					},
					's_userRole' : {
						validators : {
							notEmpty : {}
						}
					}
				}
			}).on(
					'success.form.bv',
					function(e) {//bootstrapvalidator 0.5.2用法
						e.preventDefault();
						var $form = $(e.target);
						var bv = $form.data('bootstrapValidator');
						
						if(validateForm()) {
							var serialize = $form.serialize();
							var url = '${root }/userMgmt/editUser.action'
							$.post(url, serialize, function(data) {
								if (!needLogin(data)) {
									bootbox.success($.i18n
											.prop("system.user.edit.success"));
									$('#userEditModal').modal('hide');
									$table.bootstrapTable('refresh', {});
								}
							}, "json");
						}
					});
			bootstrapValidator = $('#userEditForm').data('bootstrapValidator');
		}
		function validateForm(){
			return validateCanDealAlarm();
		}
		function validateCanDealAlarm(){
			return true;
			var role = $("#s_userRole").val();
			if(['2','3','4','5','6','7'].indexOf(role) > -1) {
				var canDealAlarm = $("#systemUser\\.canDealAlarm").val();
				if(['0', '1'].indexOf(canDealAlarm) < 0) {
					bootbox.alert($.i18n.prop('user.info.select.canDealAlarm'), function(){
						bootstrapValidator.disableSubmitButtons(false);
					});
					return false;
				}
			}
			return true;
		}
	
	
		/**
		 * 添加用户时，选择所属机构，过滤角色下拉框中的内容
		 */
		function changeRole(){
			var selected = jQuery("#s_userPort").val();
			var rselected = jQuery("#s_userRole").val();
			var htmlTxt =  "<option value =''><fmt:message key="please.choose"/></option>";
			if(selected=="9003"){
				//Admin Center
				htmlTxt += "<option value ='1'><fmt:message key="system.role.admin"/></option>";
			}else if(selected=="9002"){
				//Quality Center
				//当开启报警推送功能时
				if(systemModules.isAlarmPushOn) {
					htmlTxt += "<option value ='2'><fmt:message key="system.role.qualityCenterUser"/></option>";
				}
			}else if(selected=="9001"){
				//Control Room
				for(var i in roleType.controlRoom) {
					htmlTxt += "<option value ='" + roleType.controlRoom[i] + "'>" + $.i18n.prop("system.role." + roleList[roleType.controlRoom[i]]) + "</option>";
				}
			}else if(!selected){
				for(var i in roleList) {
					htmlTxt += "<option value ='" + i + "'>" + $.i18n.prop("system.role." + roleList[i]) + "</option>";
				}
			}else{
				htmlTxt += "<option value ='8'><fmt:message key="system.role.portUser"/></option>";
			}
			jQuery("#s_userRole").html(htmlTxt).children("option[value=" + rselected + "]").attr("selected", "selected");
		}
	
		/**
		 * 添加用户时，选择角色，过滤所属机构下拉框中的内容
		 */
		function changeOrganization(){
			var selected = jQuery("#s_userPort").val();
			var rselected = jQuery("#s_userRole").val();
			var htmlTxt =  "<option value =''><fmt:message key="please.choose"/></option>";
			if(roleType.admin.indexOf(rselected) > -1){
				htmlTxt += "<option value ='9003'><fmt:message key="system.organization.Admin.Center"/></option>";
			}else if(rselected=="2"){
				//Quality Center
				//当开启报警推送功能时
				if(systemModules.isAlarmPushOn) {
					htmlTxt += "<option value ='9002'><fmt:message key="system.organization.Quality.Center"/></option>";
				}
			}else if(roleType.controlRoom.indexOf(rselected) > -1){
				htmlTxt += "<option value ='9001'><fmt:message key="system.organization.Control.Room"/></option>";
			}else if(roleType.port.indexOf(rselected) > -1){
				for(var i in portList) {
					if(portList[i].organizationType == '2') {
						htmlTxt += "<option value ='" + portList[i].organizationId + "'>" + portList[i].organizationName + "</option>";
					}
				}
			}else if(!rselected){
				for(var i in portList) {
					htmlTxt += "<option value ='" + portList[i].organizationId + "'>" + portList[i].organizationName + "</option>";
				}
			}
			jQuery("#s_userPort").html(htmlTxt).children("option[value=" + selected + "]").attr("selected", "selected");;
		}
	</script>
</body>
</html>
	
	
</body>
</html>