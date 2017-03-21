<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="system.user.add.title" /></title>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel">
			<fmt:message key="system.user.add.title" />
		</h4>
	</div>
	<form class="form-horizontal row" id="userAddForm" method="post">
		<div class="modal-body">
				<div class="form-group ">
					<label class="col-sm-2 control-label" for="systemUser.userAccount">
					<p class="form-control-static"><em>*</em>
					<fmt:message
							key="user.userAccount" />
						</p>
					</label>
					<div class="col-sm-3">
						<p class="form-control-static">
						<input type="text" class="form-control input-sm"
							id="systemUser.userAccount" name="systemUser.userAccount">
						</p>
					</div>
					
					<label class="col-sm-2 control-label" for="systemUser.userName">
					<p class="form-control-static"><em>*</em>
					<fmt:message
							key="user.userName" />
						</p>
					</label>
					<div class="col-sm-3">
						<p class="form-control-static">
						<input type="text" class="form-control input-sm"
							id="systemUser.userName" name="systemUser.userName">
						</p>
					</div>
				</div>
					
				<div class="form-group ">
					<label class="col-sm-2 control-label" for="s_userRole">
					<p class="form-control-static"><em>*</em>
					<fmt:message
							key="user.role" />
					</p>
					</label>
					<div class="col-sm-3" id="role">
						<p class="form-control-static">
						<select style="font-size: 12px" id="s_userRole" name="s_userRole"
							class="form-control" onchange="changeOrganization()">
							<option value=""><fmt:message key="please.choose" /></option>
							<c:forEach var="systemRole" items="${roleList}">
								<option value="${systemRole.roleId }"><fmt:message
										key="system.role.${systemRole.roleName}" /></option>
							</c:forEach>
						</select>
						</p>
					</div>
					
					<label class="col-sm-2 control-label" for="s_userPort" style="padding-right: 0;">
					<p class="form-control-static"><em>*</em>
					<fmt:message key="user.portID" />
					</p>		
					</label>
					<div class="col-sm-3" id="port">
						<p class="form-control-static">
						<select style="font-size: 12px" id="s_userPort" name="s_userPort"
							class="form-control" onchange="changeRole()">
							<option value=""><fmt:message key="please.choose" /></option>
							<c:forEach var="systemDepartmentBO" items="${portList}">
								<option value="${systemDepartmentBO.organizationId}">${systemDepartmentBO.organizationName}</option>
							</c:forEach>
						</select>
						</p>
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-2 control-label" for="systemUser.userPhone">
					<p class="form-control-static">
					<fmt:message
							key="user.userPhone" />
					</p>
					</label>
					<div class="col-sm-3">
						<p class="form-control-static">
						<input type="text" class="form-control input-sm"
							id="systemUser.userPhone" name="systemUser.userPhone">
						</p>
					</div>
					
					<label class="col-sm-2 control-label" for="systemUser.userAddress">
					<p class="form-control-static">
					<fmt:message
							key="user.userAddress" />
					</p>
					</label>
					<div class="col-sm-3">
						<p class="form-control-static">
						<input type="text" class="form-control input-sm"
							id="systemUser.userAddress" name="systemUser.userAddress">
						</p>
					</div>
				</div>
			
				<div class="form-group ">
					<label class="col-sm-2 control-label" for="systemUser.userEmail">
					<p class="form-control-static">
					<fmt:message
							key="user.userEmail" />
					</p>		
					</label>
					<div class="col-sm-3">
						<p class="form-control-static">
						<input type="text" class="form-control input-sm"
							id="systemUser.userEmail" name="systemUser.userEmail">
						</p>
					</div>
					
					<label class="col-sm-2 control-label" for="systemUser.position">
					<p class="form-control-static">
						<fmt:message key="user.position" />
					</p>
					</label>
					<div class="col-sm-3">
						<p class="form-control-static">
						<input type="text" class="form-control input-sm"
							id="systemUser.position" name="systemUser.position">
						</p>
					</div>
				</div>
				<%-- <div class="form-group ">
					<label class="col-sm-4 control-label" for="systemUser.level"><fmt:message key="user.level" /></label>
					<div class="col-sm-8">
						<input type="text" class="form-control input-sm"
							id="systemUser.level" name="systemUser.level">
					</div>
				</div> --%>
				<div class="form-group ">
					<label class="col-sm-2 control-label" for="systemUser.canDealAlarm">
					<p class="form-control-static">
					<fmt:message key="user.canDealAlarm" />
					</p>
					</label>
					<div class="col-sm-3">
						<p class="form-control-static">
						<select style="font-size: 12px" id="systemUser.canDealAlarm" name="systemUser.canDealAlarm" class="form-control" disabled="disabled">
							<option value=""></option>
							<option value="1"><fmt:message key="user.canDealAlarm.yes" /></option>
							<option value="0"><fmt:message key="user.canDealAlarm.no" /></option>
						</select>
						</p>
					</div>
				</div>
		</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger" id="addUserButton">
				<fmt:message key="common.button.add" />
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
				$("#s_userRole").children("option[value=1]").remove();
				$("#s_userPort").children("option[value=9003]").remove();
			}
			bindUserAddForm();
			
			$("#s_userRole").on("change", function(){
				var role = $(this).val();
				if(roleType.controlRoom.indexOf(role) > -1) {
					$("#systemUser\\.canDealAlarm").removeAttr("disabled").val('');
				} else {
					$("#systemUser\\.canDealAlarm").attr("disabled", "disabled").val('');
				}
			});
		});

		/**
		 * 添加用户表单验证
		 */
		function bindUserAddForm() {
			//设置验证
			$('#userAddForm').bootstrapValidator({
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
					's_userPort' : {
						validators : {
							notEmpty : {}
						}
					},
					's_userRole' : {
						validators : {
							notEmpty : {}
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
					'systemUser.center' : {
						validators : {
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
							var url = '${root }/userMgmt/addUser.action'
							$.post(url, serialize, function(data) {
								if (!needLogin(data)) {
									bootbox.success($.i18n
											.prop("system.user.add.success"));
									$('#userAddModal').modal('hide');
									$table.bootstrapTable('refresh', {});
								}
							}, "json");
						}
					});
			bootstrapValidator = $('#userAddForm').data('bootstrapValidator');
		}
		function validateForm(){
			return validateCanDealAlarm();
		}
		function validateCanDealAlarm(){
			return true;
			var role = $("#s_userRole").val();
			if(roleType.controlRoom.indexOf(role) > -1) {
				var canDealAlarm = $("#systemUser\\.canDealAlarm").val();
				if(!canDealAlarm) {
					bootbox.alert($.i18n.prop('user.info.select.canDealAlarm'), function(){
						bootstrapValidator.disableSubmitButtons(false);
					});
					return false;
				}
			}
			return true;
		}
	</script>
	
	<!-- 添加用户时，选择所属机构，过滤角色下拉框中的内容-->
	<script type="text/javascript">
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
	</script>
	
	<!-- 添加用户时，选择角色，过滤所属机构下拉框中的内容-->
	<script type="text/javascript">
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