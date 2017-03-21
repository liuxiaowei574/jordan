<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../include/include.jsp" />
<title><fmt:message key="system.notice.title" /></title>
</head>
<body>
	<!--巡逻队模态框显示-->
<!-- 	<div class="modal  add_user_box" id="addPatrolModal" tabindex="-1"
		data-toggle="modal" role="dialog"
		aria-labelledby="addPatrolModalTitle">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div> -->
	<!-- /Modal -->
	
	<input type="hidden" class="form-control input-sm" id="userId"
		name="userId" value="${userId}">
	<input type="hidden" class="form-control input-sm" id="roleName"
		name="roleName" value="${roleName}">
	<div class="search_table" style="height: 300px; overflow: auto">
		<div>
			<!-- <table id="missionTable">
			</table> -->
			<table class="table table-bordered table-striped" id="missionTable">
				<thead></thead>
				<tbody>
					<c:forEach var="e" items="${missionList}">
						<tr>
							<td></td>
							<td>${e.TASK_CONTENT}</td>
							<td>${e.TASK_TYPE}</td>
							<td>${e.DEPLOY_TIME}</td>
							<td class="t">${e.TASK_PRIORITY}</td>
							<td class="t">${e.TASK_ID}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<div class="clearfix"></div>
	<div class="modal-footer margin15">
		<button type="submit" class="btn btn-danger" id="sendMissionButton"
			onclick="sendMissionToPatrol()"><fmt:message key="mission.choose.patrol"/></button>
		<button type="submit" class="btn btn-danger" id="modifyButton"
			onclick="handleMission()">
			<fmt:message key="AlarmDealType.Dealt" />
		</button>
		<button type="submit" class="btn btn-danger" id="modifyButton"
			onclick="forwardMission()">
			任务转发
		</button>
		<button type="button" class="btn btn-darch" onclick="closeWindow()"
			id="btnCancel">
			<fmt:message key="notice.close.window" />
		</button>
	</div>

	<script type="text/javascript">
		var $table = $('#missionTable');
		$(function() {
			$table.bootstrapTable({
				//url : '${root}/undealMission/list.action',
				clickToSelect : true,
				showRefresh : false,
				search : false,
				showColumns : false,
				showExport : false,
				striped : true,
				height : "100%",
				method : "get",
				idfield : "taskId",
				cache : false,
				sidePagination : 'server',
				pageNumber : 1,
				sortable : true,
				columns : [ {
					checkbox : true
				}, {
					field : 'taskContent',
					title : $.i18n.prop('mission.content'),
					sortable : true,
				}, {
					field : 'taskType',
					title : $.i18n.prop('mission.type'),
					formatter : missionFormatter,
					sortable : true,
				}, {
					field : 'deployTime',
					title : $.i18n.prop('notice.deployTime'),
					sortable : true,
				}, {
					field : 'priority',
				    //cellStyle :colorFormatter,
				}, {
					field : 'taskId',
				} ],
			});
		});
	</script>

	<!-- 处理任务 -->
	<script type="text/javascript">
		var userId = $("#userId").val();
		function handleMission() {
			var list = $('#missionTable').bootstrapTable('getSelections'); //获取表的行
			var taskIds = new Array();
			for ( var o in list) {
				taskIds.push(list[o].taskId);
			}
			var taskIds = taskIds.join(",");
			if (list.length <= 0) {
				bootbox.alert($.i18n.prop("mission.choose.one.more"));
				return;
			} else {
				var ajaxUrl = "${root}/undealMission/dealMission.action";
				$.ajax({
					url : ajaxUrl,
					type : "post",
					dataType : "json",
					data : {
						taskIds : taskIds,
						userId : userId
					},
					success : function(data) {
						if (data == true) {
							$('#missionTable').bootstrapTable('refresh', {});
						}
					}
				});
			}
		}
	</script>
	<script type="text/javascript">
		function closeWindow() {
			parent.window.closeDialog();
		}
	</script>


	<script type="text/javascript">
		function missionFormatter(value, row, index) {
			var show;
			if (value == '1') {
				show = $.i18n.prop('mission.escort.mission');
			} else if (value == '2') {
				show = $.i18n.prop('mission.deal.alarm.mission');
			} else if (value == '3') {
				show = $.i18n.prop('mission.dispatch.device.mission');
			} else {
				show = '--'
			}
			return [ show ].join('');
		}
	</script>

	<!-- 显示推送或处理按钮 -->
	<script type="text/javascript">
		$(function() {
			var roleName = $("#roleName").val();
			/* 如果是"contromRoomManager"角色则显示推送按钮，反之显示处理 任务按钮 */
			if (roleName == "contromRoomManager") {
				$("#modifyButton").hide();
			} else {
				$("#sendMissionButton").hide();
			}
		})
	</script>

	<!-- 把任务推送给巡逻队 -->
	<script type="text/javascript">
		function sendMissionToPatrol() {
			/* 获取任务的id */
			var list = $("#missionTable").bootstrapTable('getSelections');
			var taskIds = "";
			for ( var o in list) {
				taskIds = taskIds + list[o].taskId + ",";
			}
			if (taskIds.length == 0) {
				bootbox.alert($.i18n.prop('mission.choose.one.more.mission.topush'));
			} else {
				/* 弹出巡逻队的模态框 */
				//	var url = "${root}/dispatchSendMsg/missionPatrolModal.action?taskIds="+taskIds;
				var url = "${root}/dispatchSendMsg/missionPatrolModal.action?taskIds="
						+ taskIds;
				$('#addPatrolModal').removeData('bs.modal');
				$('#addPatrolModal').modal({
					remote : url,
					show : false,
					backdrop : 'static',
					keyboard : false
				});

				$('#addPatrolModal').on('loaded.bs.modal', function(e) {
					$('#addPatrolModal').modal('show');
				});
			}
		}
	</script>

	<script type="text/javascript">
		function colorFormatter(value, row, index, field) {
			var show;
			if (value == '1') {
				return {
					css : {
						"color" : "red"
					}
				};
			} else if (value == '2') {
				return {
					css : {
						"color" : "blue"
					}
				};
			} else if (value == '3') {
				return {
					css : {
						"color" : "red"
					}
				};
			} else {
				show = '--'
			}
			return [ show ].join('');
		}
	</script>

	<!-- 根据任务紧急程度改变行的颜色 -->
	<script type="text/javascript">
		$(function() {
			$(".t").hide();
			
			$("#missionTable td").each(function() {
				$td = $(this);
				if ($.trim($td.text()) == "1") {
					$td.parent().css("background", "red");
				}

				if ($.trim($td.text()) == "2") {
					$td.parent().css("background", "yellow");
				}

				if ($.trim($td.text()) == "3") {
					$td.parent().css("background", "green");
				}
			})
		})
	</script>
	<!-- 控制中心普通人员在转发任务前需向管理人员提出申请 -->
	<script type="text/javascript">
		function forwardMission() {
			/* 获取任务的id */
			var list = $("#missionTable").bootstrapTable('getSelections');
			var taskIds = "";
			for ( var o in list) {
				taskIds = taskIds + list[o].taskId + ",";
			}
			if (taskIds.length == 0) {
				bootbox.alert($.i18n.prop('mission.choose.one.more.mission.topush'));
			} else {
				/* 弹出巡逻队的模态框 */
				//	var url = "${root}/dispatchSendMsg/missionPatrolModal.action?taskIds="+taskIds;
				var url = "${root}/dispatchSendMsg/chooseManagers.action?taskIds="
						+ taskIds;
				$('#addPatrolModal').removeData('bs.modal');
				$('#addPatrolModal').modal({
					remote : url,
					show : false,
					backdrop : 'static',
					keyboard : false
				});

				$('#addPatrolModal').on('loaded.bs.modal', function(e) {
					$('#addPatrolModal').modal('show');
				});
			}
		}
	</script>
</body>
</html>