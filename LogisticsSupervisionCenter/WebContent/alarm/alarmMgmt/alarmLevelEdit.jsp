<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="WarehouseElock.list.title" /></title>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="myModalLabel">
			<fmt:message key="Alarm.Management" />
		</h4>
	</div>
	<form class="form-horizontal row" id="alarmEditForm" method="post">
		<input type="hidden" class="form-control input-sm" id="alarmTypeId"
			name="alarmTypeId" value="${alarmTypeId}">

		<div class="modal-body">
			<div class="col-md-12">
				<div class="form-group ">
					<label class="col-sm-2 control-label" for="alarmName"><fmt:message
							key="Alarm.type" /></label>
					<div class="col-sm-4">
						<input type="hidden" class="form-control input-sm"
							id="lsDmAlarmTypeBO.alarmTypeCode"
							name="lsDmAlarmTypeBO.alarmTypeCode"
							value="${lsDmAlarmTypeBO.alarmTypeCode}"> <input
							type="text" class="form-control input-sm" id="alarmTypeName"
							name="alarmTypeName"
							value="<fmt:message key="AlarmType.${lsDmAlarmTypeBO.alarmTypeCode}" />"
							readonly="true">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><em>*</em><fmt:message
							key="Alarm.level" /></label>
					<div class="col-sm-4">
						<s:select name="lsDmAlarmTypeBO.alarmLevelId" emptyOption="false"
							cssClass="form-control" theme="simple"
							list="@com.nuctech.ls.model.util.AlarmLevel@values()"
							listKey="text" listValue="key">
						</s:select>
					</div>
				</div>
				<div class="form-group" id="params">
					<label class="col-sm-2 control-label"><fmt:message key="system.params.alarm" /></label>
					<div class="panel-body col-sm-8">
						<div class="search_table">
							<div>
								<table id="table">
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="submit" class="btn btn-danger" id="modifyButton">
				<fmt:message key="common.button.modify" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
	</form>


	<script type="text/javascript">
		var alarmTypeId = $("#alarmTypeId").val();
		var $table = $('#table');
		$remove = $('#remove'), selections = [];

		$(function() {
			buildAlarmForm();
			$table.bootstrapTable({
				url : '${root}/alarmModifyMgmt/dlist.action?alarmTypeId='
						+ alarmTypeId,
				pageSize : 5,
				maintainSelected : true,
				columns : [ {
					//field : 'paramName',
					field : 'paramCode',
					title : $.i18n.prop("system.params.paramName"),
					formatter : nameFormatter,
					width:1000
				}, {

					field : 'paramValue',
					title : $.i18n.prop("system.params.value"),
					sortable : true,
					editable : true,
					formatter : valueFormatter
				} , {
					field : 'paramName',
					formatter : paramNameFormatter,
					visible:false
				}],
				onLoadSuccess : function(data) {
					var data = data;
					if(data.length==0){
						$("#params").hide();
					}
				}
			});
			
			function paramNameFormatter(value, row, index) {
				return '<input class="input_noborder" class="input" name="name" readonly="readonly" type="" value="' +value +'" />';
			}
			
			/**
			 * 把列表中的单元改为input框（值）
			 */
			function nameFormatter(value, row, index) {
				return '<input class="input_noborder" class="input" name="code" readonly="readonly" type="text" value="' +$.i18n.prop(value) +'" />';
			}

			/**
			 * 把列表中的单元改为input框（值）
			 */
			function valueFormatter(value, row, index) {
				return '<input  class="input_noborder" name="value" type="text" value="'+ value +'" />';
			}

		});

		function buildAlarmForm() {
			//设置验证
			$('#alarmEditForm').bootstrapValidator({
				message : $.i18n.prop("common.message.form.validator"),
				fields : {
					'alarmTypeName' : {
						validators : {
							notEmpty : {}
						}
					},
					'lsDmAlarmTypeBO.alarmLevelId' : {
						validators : {
							notEmpty : {}
						}
					},
				}
			}).on(
					'success.form.bv',
					function(e) {//bootstrapvalidator 0.5.2用法
						e.preventDefault();
						var $form = $(e.target);
						var bv = $form.data('bootstrapValidator');
						var serialize = $form.serialize();
						var url = '${root }/alarmModifyMgmt/editAlarm.action';
						$.post(url, serialize, function(data) {
							if (data) {
								bootbox.success($.i18n
										.prop("alarm.modifyAlarm.success"));
								$('#alarmTable').bootstrapTable('refresh', {});
								$('#updateAlarmLevelModal').modal('hide');
							} else {
								bootbox.error($.i18n
										.prop("alarm.modifyAlarm.fail"));
								$('#updateAlarmLevelModal').modal('hide');
								$('#alarmTable').bootstrapTable('refresh', {});
							}
						}, "json");
					});
		}
	</script>
</body>
</html>