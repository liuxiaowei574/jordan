<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="Organization.update" /></title>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel">
			<fmt:message key="Organization.update" />
		</h4>
	</div>
	<form class="form-horizontal row" id="elockEditForm" method="post">
		<input type="hidden" class="form-control input-sm"
							id="systemDepartmentBO.levelCode"
							name="systemDepartmentBO.levelCode"
							value="${systemDepartmentBO.levelCode}">
		<input type="hidden" class="form-control input-sm"
			id="systemDepartmentBO.organizationId"
			name="systemDepartmentBO.organizationId"
			value="${systemDepartmentBO.organizationId}">

		<input type="hidden" class="form-control input-sm"
			id="systemDepartmentBO.parentId"
			name="systemDepartmentBO.parentId"
			value="${systemDepartmentBO.parentId}">
		<div class="modal-body">
			<div class="col-md-8">

				<div class="form-group ">
					<label class="col-sm-6 control-label"
						for="systemDepartmentBO.organizationName"><em>*</em><fmt:message
							key="Organization.name" /></label>
					<div class="col-sm-6">
						<input type="text" class="form-control input-sm"
							id="systemDepartmentBO.organizationName"
							name="systemDepartmentBO.organizationName"
							value="${systemDepartmentBO.organizationName}">
					</div>
				</div>


				<div class="form-group ">
					<label class="col-sm-6 control-label"
						for="systemDepartmentBO.organizationShort"><em>*</em><fmt:message
							key="Organization.shortName" /></label>
					<div class="col-sm-6">
						<input type="text" class="form-control input-sm"
							id="systemDepartmentBO.organizationShort"
							name="systemDepartmentBO.organizationShort"
							value="${systemDepartmentBO.organizationShort}">
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-6 control-label"
						for="systemDepartmentBO.longitude"><em>*</em><fmt:message
							key="Organization.longitude" /></label>
					<div class="col-sm-6">
						<input type="text" class="form-control input-sm"
							id="systemDepartmentBO.longitude"
							name="systemDepartmentBO.longitude"
							value="${systemDepartmentBO.longitude}">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-6 control-label"
						for="systemDepartmentBO.latitude"><em>*</em><fmt:message
							key="Organization.latitude" /></label>
					<div class="col-sm-6">
						<input type="text" class="form-control input-sm"
							id="systemDepartmentBO.latitude"
							name="systemDepartmentBO.latitude"
							value="${systemDepartmentBO.latitude}">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-6 control-label"><fmt:message key="Organization.if.warehouse"/></label>
					<div class="col-sm-6">
						<fmt:message key="Organization.yes"/><input cssClass="form-control" theme="simple"  name="systemDepartmentBO.isRoom" ${systemDepartmentBO.isRoom==1?'checked="checked"':'' } type="radio"  value="1">
						<fmt:message key="Organization.no"/><input cssClass="form-control" theme="simple"  name="systemDepartmentBO.isRoom" ${systemDepartmentBO.isRoom==0?'checked="checked"':'' } type="radio"  value="0">
					</div>
				</div>
				
				<c:if test="${systemModules.isPatrolOn()}">
				<div class="form-group ">
					<label class="col-sm-6 control-label"><em>*</em><fmt:message
							key="Organization.type" /></label>
					<div class="col-sm-6">
						<s:select name="systemDepartmentBO.organizationType"
							emptyOption="true" cssClass="form-control" theme="simple"
							list="@com.nuctech.ls.model.util.OrganizationType@values()"
							listKey="text" listValue="key">
						</s:select>
					</div>
				</div>
				</c:if>
				
				<c:if test="${systemModules.isPatrolOn()==false}">
				<div class="form-group ">
					<label class="col-sm-6 control-label"><em>*</em><fmt:message
							key="Organization.type" /></label>
					<div class="col-sm-6">
						<s:select name="systemDepartmentBO.organizationType"
							emptyOption="true" cssClass="form-control" theme="simple"
							list="@com.nuctech.ls.model.util.OrganizationTypeStandard@values()"
							listKey="text1" listValue="key">
						</s:select>
					</div>
				</div>
				</c:if>
				
				
				<div class="form-group ">
					<label class="col-sm-6 control-label"
						for="systemDepartmentBO.latitude"><fmt:message
							key="Organization.reservationRatio" /></label>
					<div class="col-sm-6">
						<input type="text" class="form-control input-sm"
							id="reservationRatio"
							name="systemDepartmentBO.reservationRatio"
							value="${systemDepartmentBO.reservationRatio}">
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger" id="modifyButton">
				<fmt:message key="common.button.save" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
	</form>
	<script type="text/javascript">
		$(function() {
			buildElockEditForm();
			
			//如果需要修改的机构不是口岸，则使得预留比例输入框为只读
			reservationRatio();
			
			if(['0000','9001','9003'].indexOf('${systemDepartmentBO.organizationId}') > -1) {
				$("#systemDepartmentBO_organizationType").attr("disabled", true);
				$("input[name=systemDepartmentBO\\.isRoom]").val('0');
			}
		});

		/**
		 * 
		 */
		function buildElockEditForm() {
			//设置验证
			$('#elockEditForm')
					.bootstrapValidator({
						message : $.i18n.prop("common.message.form.validator"),
						fields : {
							'systemDepartmentBO.organizationName' : {
								validators : {
									notEmpty : {}
								}
							},
							'systemDepartmentBO.organizationShort' : {
								validators : {
									notEmpty : {}
								}
							},
							'systemDepartmentBO.latitude' : {
								validators : {
									notEmpty : {},
									numeric: {}
								}
							},
							'systemDepartmentBO.longitude' : {
								validators : {
									notEmpty : {},
									numeric: {}
								}
							},
							'systemDepartmentBO.organizationType' : {
								validators : {
									notEmpty : {}
								}
							},
							'systemDepartmentBO.isRoom' : {
								validators : {
									notEmpty : {}
								}
							}
						}
					})
					.on(
							'success.form.bv',
							function(e) {//bootstrapvalidator 0.5.2用法
								e.preventDefault();
								var $form = $(e.target);
								var bv = $form.data('bootstrapValidator');
								if(['9001','9003'].indexOf('${systemDepartmentBO.organizationId}') > -1) {
									$("#systemDepartmentBO_organizationType").removeAttr("disabled").val('3');
								}
								if(['0000'].indexOf('${systemDepartmentBO.organizationId}') > -1) {
									$("#systemDepartmentBO_organizationType").removeAttr("disabled").val('1');
								}
								
								var serialize = $form.serialize();
								var url = '${root }/deptMgmt/modifyDepartment.action'
								$.post(
										url,
										serialize,
										function(data) {
											if (data) {
												bootbox
														.success($.i18n
																.prop('Organization.update.success'));
												$('#updateElockModal')
														.modal('hide');
												$table.bootstrapTable(
														'refresh', {});
												var setting = {
													async : {
														enable : true,
														type : "get",
														url : root	+ '/deptMgmt/findDepartmentTree.action'
													},
													check : {
														enable : false,
														chkboxType : {"Y" : "s","N" : "s"
														}
													},
													view : {
														dblClickExpand : false
													},
													data : {
														keep : {
															parent : true
														},
														key : {
															name : "name",
															title : "name"
														},
														simpleData : {
															enable : true,
															idKey : "id",
															pidKey : "pId",
															rootId : 0
														}
													},
													callback : {
														onClick : checkDepartment
													}
												};
												$.ajax({url : root+ '/deptMgmt/findDepartmentTree.action',
															dataType : "json",
															cache : false,
															success : function(data) {
																if(!needLogin(data)) {
																	$.fn.zTree.init($("#tree"),setting,data.departmentList);
																}
															}
														});
											} else {
												bootbox
														.error($.i18n.prop('Organization.update.fail'));
												$('#updateElockModal').modal('hide');
												$table.bootstrapTable('refresh', {});
											}
										}, "json");
							});
		}
		
		function reservationRatio(){
			/* 获取后台传过来的机构类型 */
			var orgType = ${orgType}
			if(orgType!="2"){
				$("#reservationRatio").attr("readonly",'readonly');
			}
		}
	</script>
</body>
</html>