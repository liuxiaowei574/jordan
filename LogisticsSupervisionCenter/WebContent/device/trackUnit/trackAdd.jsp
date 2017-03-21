<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="elock.addElock" /></title>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel">
			<fmt:message key="track.add"/>
		</h4>
	</div>
	<form class="form-horizontal row" id="trackAddForm" method="post">
		<div class="modal-body">	
		<div class="col-md-6">
			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message key="track.number"/></label>
				<div class="col-sm-8">
					<input type="text" id="lsWarehouseTrackUnitBO.trackUnitNumber"
						name="lsWarehouseTrackUnitBO.trackUnitNumber" value="${param.trackUnitNumber }"
						class="form-control input-sm">
				</div>
			</div>

			<div class="form-group">
				<label class="col-sm-4 control-label"><fmt:message
						key="WarehouseElock.belongTo" /></label>
				<div class="col-sm-8">
					<select style="font-size: 10px" id="lsWarehouseTrackUnitBO.belongTo" name="lsWarehouseTrackUnitBO.belongTo"
						class="form-control">
						<option value=""><fmt:message key="please.choose" /></option>
						<c:forEach var="SystemDepartmentBO" items="${deptList}">
							<option value='${SystemDepartmentBO.organizationId}'
								${param.checkinPort==SystemDepartmentBO.organizationId ? 'selected' : ''}>
								${SystemDepartmentBO.organizationName}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message key="track.simcard"/></label>
				<div class="col-sm-8">
					<input type="text" id="lsWarehouseTrackUnitBO.imCard"
						name="lsWarehouseTrackUnitBO.simCard" class="form-control input-sm">
				</div>
			</div>
			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message key="track.interval"/></label>
				<div class="col-sm-8">
					<input type="text" id="lsWarehouseTrackUnitBO.interval"
						name="lsWarehouseTrackUnitBO.interval" class="form-control input-sm">
				</div>
			</div>

			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message key="track.gatewayAddress"/></label>
				<div class="col-sm-8">
					<input type="text" id="lsWarehouseTrackUnitBO.gatewayAddress"name="lsWarehouseTrackUnitBO.gatewayAddress"class="form-control input-sm">
				</div>
			</div>
			<div class="form-group ">
				<label class="col-sm-4 control-label"><fmt:message key="track.trackUnitStatus"/></label>
				<div class="col-sm-8">
					<s:select name="lsWarehouseTrackUnitBO.trackUnitStatus" emptyOption="true"
						cssClass="form-control" theme="simple"
						list="@com.nuctech.ls.model.util.DeviceStatus@values()"
						listKey="text" listValue="key" 
						value="">
					</s:select>
				</div>
			</div>
		</div>
	</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger" id="addTrackButton">
				<fmt:message key="common.button.add" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">
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
			$('#trackAddForm').bootstrapValidator({
				message : $.i18n.prop("common.message.form.validator"),
				fields : {
					'lsWarehouseTrackUnitBO.trackUnitNumber' : {
						validators : {
							notEmpty : {},
							remote:{
								url:'${root }/trackMgmt/repeate.action',
								message:$.i18n.prop("track.number.repeat"),
								delay:2000,
								type:'post'
							},
							regexp: {
								regexp: /^[\da-zA-Z]+$/,
		                        message: $.i18n.prop('trip.message.input.letterNumber')
							}
						}
					},
					'lsWarehouseTrackUnitBO.belongTo' : {
						validators : {
							notEmpty : {}
						}
					},
					'lsWarehouseTrackUnitBO.simCard' : {
						validators : {
							notEmpty : {}
						}
					},
					'lsWarehouseTrackUnitBO.interval' : {
						validators : {
							notEmpty : {}
						}
					},
					'lsWarehouseTrackUnitBO.gatewayAddress' : {
						validators : {
							notEmpty : {}
						}
					},
					'lsWarehouseTrackUnitBO.trackUnitStatus' : {
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
				var url = '${root }/trackMgmt/addTrack.action';
				$.post(url, serialize, function(data) {
					if(!needLogin(data)) {
						if (data) {
							bootbox.alert($.i18n.prop('track.add.success'));
							$('#addTrackModal').modal('hide');
							$table.bootstrapTable('refresh', {});
						} else {
							bootbox.error($.i18n.prop('track.add.fail'));
							$('#addTrackModal').modal('hide');
							$table.bootstrapTable('refresh', {});
						}
					}
				}, "json");
			});
		}
	</script>
</body>
</html>