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
	<!--巡逻队模态框显示(控制中心给巡逻队推送任务)-->
	<div class="modal  add_user_box" id="addPatrolModal" tabindex="-1"
		data-toggle="modal" role="dialog"
		aria-labelledby="addPatrolModalTitle">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>

	<!-- 控制中心普通人员模态框显示(交班任务) -->
	<div class="modal  add_user_box" id="controlRoomUser" tabindex="-1"
		data-toggle="modal" role="dialog"
		aria-labelledby="addPatrolModalTitle">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>


	<input type="hidden" class="form-control input-sm" id="userId"
		name="userId" value="${userId}">
	<input type="hidden" class="form-control input-sm" id="roleName"
		name="roleName" value="${roleName}">
	<div class="form-group">
		<div class="btn-group">
			<button id="alarmMission" type="button" class="btn btn-danger">
				<fmt:message key="mission.alarm.deal" />
			</button>
			<button id="escortMission" type="button" class="btn btn-info">
				<fmt:message key="mission.patrol.escort" />
			</button>
			<button id="dispatchMission" type="button" class="btn btn-default">
				<fmt:message key="mission.patrol.dispacth" />
			</button>
		</div>
	</div>
	<div class="sub-menu row" style="height: 450px; overflow: auto">
		<div class="col-xs-3">
			<!-- 报警处理任务 -->
			<div id="alarmTask" style="height:400px;overflow: auto">
				<ul class="" role="tablist" id="alarmMissionList">
				</ul>
			</div>
			<!-- 巡逻队护送任务 -->
			<div id="escortTask"style="height:400px;overflow: auto">
				<ul class="" role="tablist" id="patrolEscortList">
				</ul>
			</div>
			<!-- 设备调度任务 -->
			<div id="devicedipatchTask"style="height:400px;overflow: auto">
				<ul class="" role="tablist " id="deviceDispatchList">
				</ul>
			</div>
		</div>
		<div class="col-xs-9 sub-content" id="taskcontent">
			<div>
				<h4>
					<font color="green"><fmt:message key="mission.content" /></font>
				</h4>
				<textarea style="height: 200px; width: 540px" id="missionContent">
				</textarea>
			</div>
			<div>
				<h4>
					<font color="green"><fmt:message key="mission.priority" /></font>
				</h4>
				<input style="width: 540px" type="text" value="" id="taskPriority">
			</div>
			<div style="margin-top: 44px;">
				<!-- 用来处理任务的各种button -->
				<div align="center">
					<button type="submit" class="btn btn-danger" id="sendMissionButton"
						onclick="sendMissionToPatrol()">
						<fmt:message key="mission.choose.patrol" />
					</button>
					<button type="submit" class="btn btn-danger" id="modifyButton"
						onclick="handleMission()">
						<fmt:message key="AlarmDealType.Dealt" />
					</button>
					<button type="submit" class="btn btn-danger" id="shiftButton"
						onclick="forwardMission()">
						<fmt:message key="mission.forward" />
					</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 控制中心给巡逻队推送任务 -->
	<script type="text/javascript">
		function sendMissionToPatrol() {
			/* 获取任务的id */
			var taskIds = "";
			$("input[name='taskId']:checkbox").each(function() {
				if ($(this).prop("checked")) {
					taskIds += $(this).val() + ",";
				}
			});
			if (taskIds.length == 0) {
				bootbox.alert($.i18n
						.prop('mission.choose.one.more.mission.topush'));
			} else {
				/* 弹出巡逻队的模态框 */
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

	<!-- 交班任务 -->
	<script type="text/javascript">
		function forwardMission() {
			/* 获取任务的id */
			var taskIds = "";
			$("input[name='taskId']:checkbox").each(function() {
				if ($(this).prop("checked")) {
					taskIds += $(this).val() + ",";
				}
			});
			if (taskIds.length == 0) {
				bootbox.alert($.i18n
						.prop('mission.choose.one.more.mission.to.forward'));
			} else {
				/* 弹出控制中心除自己之外的普通员工的模态框 */
				var url = "${root}/dispatchSendMsg/chooseContromRoomUser.action?taskIds="
						+ taskIds;
				$('#controlRoomUser').removeData('bs.modal');
				$('#controlRoomUser').modal({
					remote : url,
					show : false,
					backdrop : 'static',
					keyboard : false
				});

				$('#controlRoomUser').on('loaded.bs.modal', function(e) {
					$('#controlRoomUser').modal('show');
				});
			}
		}
	</script>

	<!-- 根据用户的角色显示推送，处理或交班按钮 -->
	<script type="text/javascript">
		$(function() {
			var roleName = $("#roleName").val();
			/* 如果是控制中心的主管，则在任务列表页面显示"选择巡逻队按钮——给巡逻队推送任务"，
			如果是控制中心普通员工页面显示"已处理"和"交班任务按钮"，其他员工如巡逻队应该只有"已处理"按钮*/
			if (roleName == "contromRoomManager") {
				$("#modifyButton").hide();//隐藏处理任务按钮
				$("#shiftButton").hide();//隐藏交班任务按钮
			} else if (roleName == "contromRoomUser") {
				$("#sendMissionButton").hide();
			} else {
				$("#sendMissionButton").hide();
				$("#shiftButton").hide();//隐藏交班任务按钮
			}
		})
	</script>

	<!-- 如果待办任务数量为0，隐藏"任务内容，优先级" -->
	<script type="text/javascript">
		$(function() {
			var number = ${taskNumber};
			//如果任务数量为0，则隐藏"任务内容"和"任务优先级"
			if(number == 0){
				$("#taskcontent").hide();//隐藏处理任务按钮
				bootbox.alert($.i18n.prop('mission.no.unfinish'));
			}
		})
	</script>
	
	<script type="text/javascript">
		var root = "${root}";
	</script>
	<!-- 报警处理任务有关js -->
	<script type="text/javascript"
		src="${root}/artdialogconyent/js/alarmMission.js"></script>
	<!-- 巡逻队护送任务 -->
	<script type="text/javascript"
		src="${root}/artdialogconyent/js/escortMission.js"></script>
	<!-- 设备调度任务 -->
	<script type="text/javascript"
		src="${root}/artdialogconyent/js/dispatchMission.js"></script>
	<!-- 处理任务(处理，转发等) -->
	<script type="text/javascript"
		src="${root}/artdialogconyent/js/dealMission.js"></script>
</body>
</html>