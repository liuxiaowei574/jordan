<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="Organization.add" /></title>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel"><fmt:message key="Organization.add"/></h4>
	</div>
	<form class="form-horizontal row" id="elockAddForm" method="post">
		<div class="modal-body">
			<div class="col-md-8">
				<!-- <div class="form-group ">
				<label class="col-sm-4 control-label">上级机构</label>
				<div class="col-sm-8">
					<input type="text" id="systemDepartmentBO.parentId"
						name="systemDepartmentBO.parentId" value=""
						class="form-control input-sm">
				</div>
			</div> -->
			
				<div class="form-group ">
					<label class="col-sm-6 control-label"><em>*</em><fmt:message key="Organization.type"/></label>
					<div class="col-sm-6">
						<c:if test="${systemModules.isPatrolOn()}">
							<s:select name="systemDepartmentBO.organizationType"
								id="orgType"
								emptyOption="false" cssClass="form-control" theme="simple"
								list="@com.nuctech.ls.model.util.OrganizationType@values()"
								listKey="text" listValue="key" value="" headerKey=""
								headerValue=""
								onchange="ifcountry()">
							</s:select>
						</c:if>
						<c:if test="${systemModules.isPatrolOn()==false}">
						<s:select name="systemDepartmentBO.organizationType" 
							id="orgType"
							emptyOption="false" cssClass="form-control" theme="simple"
							list="@com.nuctech.ls.model.util.OrganizationTypeStandard@values()"
							listKey="text1" listValue="key" value="" headerKey=""
							headerValue=""
							onchange="ifcountry()">
						</s:select>
						</c:if>
					</div>
				</div>
			
				<input type="hidden" id="systemDepartmentBO.parentId" name="systemDepartmentBO.parentId"/>
				<!-- 上级机构用ztree -->
				<div class="form-group ">
					<label class="col-sm-6 control-label"><fmt:message key="Organization.higher.authority"/></label>
					<div class="col-sm-6">
						<div id="menuContent">
							<ul id="departmentTree" class="ztree"
								style="margin-top: 0; width: 180px; height: 300px;"></ul>
						</div>
						 
						 <input type="text" class="form-control input-sm" id="userChecked" name="userChecked" value="${organizationName}" readonly="readonly" onclick="showMenu()">
					   	 <input type="hidden" id="s_parentId" name="s_parentId" value="${organizationId}"/>
					</div>
				</div>

				<%-- <div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message key="Organization.primary.Id"/></label>
					<div class="col-sm-8">
						<input type="text" id="systemDepartmentBO.organizationId"
							name="systemDepartmentBO.organizationId" value=""
							class="form-control input-sm">
					</div>
				</div> --%>

				<div class="form-group">
					<label class="col-sm-6 control-label"><em>*</em><fmt:message key="Organization.name"/></label>
					<div class="col-sm-6">
						<input type="text" id="systemDepartmentBO.organizationName"
							name="systemDepartmentBO.organizationName" value=""
							class="form-control input-sm">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-6 control-label"><em>*</em><fmt:message key="Organization.shortName"/></label>
					<div class="col-sm-6">
						<input type="text" id="systemDepartmentBO.organizationShort"
							name="systemDepartmentBO.organizationShort"
							class="form-control input-sm">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-6 control-label"><em>*</em><fmt:message key="Organization.longitude"/></label>
					<div class="col-sm-6">
						<input type="text" id="systemDepartmentBO.longitude"
							name="systemDepartmentBO.longitude" class="form-control input-sm">
					</div>
				</div>

				<div class="form-group ">
					<label class="col-sm-6 control-label"><em>*</em><fmt:message key="Organization.latitude"/></label>
					<div class="col-sm-6">
						<input type="text" id="systemDepartmentBO.latitude"
							name="systemDepartmentBO.latitude" class="form-control input-sm">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-6 control-label"><fmt:message key="Organization.if.warehouse"/></label>
					<div class="col-sm-6">
						<fmt:message key="Organization.yes"/><input cssClass="form-control" theme="simple"  name="systemDepartmentBO.isRoom"  type="radio" value="1">
						<fmt:message key="Organization.no"/><input cssClass="form-control" theme="simple"  name="systemDepartmentBO.isRoom"  checked="checked" type="radio" value="0">
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger" id="addElockButton">
				<fmt:message key="common.button.add" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal"
				id="btnCancel">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
	</form>

	<script type="text/javascript">
		$(function() {

			bindElockAddForm();
		});

		/**
		 * 添加关锁表单验证
		 */
		function bindElockAddForm() {
			//设置验证
			$('#elockAddForm').bootstrapValidator({
				message : $.i18n.prop("common.message.form.validator"),
				fields : {
					'systemDepartmentBO.parentId' : {
						validators : {
							notEmpty : {}
						}
					},
					'systemDepartmentBO.organizationId' : {
						validators : {
							notEmpty : {}
						}
					},
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
					'systemDepartmentBO.longitude' : {
						validators : {
							notEmpty : {},
							numeric: {}
						}
					},
					'systemDepartmentBO.latitude' : {
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

			}).on('success.form.bv', function(e) {//bootstrapvalidator 0.5.2用法
				e.preventDefault();
				var $form = $(e.target);
				var bv = $form.data('bootstrapValidator');

				var serialize = $form.serialize();
				var url = '${root }/deptMgmt/addDepartment.action';
				$.post(url, serialize, function(data) {
					if(!needLogin(data)) {
						if (data) {
							bootbox.alert($.i18n.prop('Organization.add.success'));
							$('#addElockModal').modal('hide');
							$table.bootstrapTable('refresh', {});
							var  setting = {
				  					async: {
				  			            enable: true,
				  			            type: "get",
				  			            url: root+'/deptMgmt/findDepartmentTree.action'
				  			        },
				  					check: {
				  						enable: false,
				  						chkboxType: { "Y": "s", "N": "s" }
				  					},
				  					view: {
				  						dblClickExpand: false
				  					},
				  					data: {
				  			            keep: {
				  			                parent: true
				  			            },
				  			            key: {
				  			                name: "name",
				  			                title: "name"
				  			            },
				  			            simpleData: {
				  			                enable: true,
				  			                idKey: "id",
				  			                pidKey: "pId",
				  			                rootId: 0
				  			            }
				  			        },
				  					callback: {
				  						onClick : checkDepartment
				  					} 
				  				};
				  			$.ajax({
				  			    url: root+'/deptMgmt/findDepartmentTree.action',
				  			    dataType: "json",
				  			    cache: false,
				  			    success: function(data) {
				  			    	$.fn.zTree.init($("#tree"), setting, data.departmentList);
				  			    }
				  			});  
						} else {
							bootbox.error($.i18n.prop('Organization.add.fail'));
							$('#addElockModal').modal('hide');
							$table.bootstrapTable('refresh', {});
						}
					}

				}, "json");
			});
		}
	</script>
<script type="text/javascript">
	var root = "${root}";
	//如果添加机构类型是国家，则"上级机构"不可用
	function ifcountry(){
		//获取下拉列表选中的值
		var type = $("#orgType option:selected").val();
		//如果选择国家，则"上级机构"不可用
		if(type=="1"){
			document.getElementById("userChecked").onclick = function(){};
		}
		if(type!="1"){
			document.getElementById("userChecked").onclick = showMenu();
		}
	}
</script>
<script type="text/javascript" src="${root}/system/systemDepartment/js/pDepartmentTree.js"></script> 
	
</body>
</html>