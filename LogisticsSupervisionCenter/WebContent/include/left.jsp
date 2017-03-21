<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp"%>
<style>
.messagenumber {
	display: block;
	padding: 0px;
	border-radius: 100px;
	background: #ff2800; /* Old browsers */
	background: -moz-linear-gradient(top, #ff2800 0%, #bf0000 100%);
	/* FF3.6+ */
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #ff2800),
		color-stop(100%, #bf0000)); /* Chrome,Safari4+ */
	background: -webkit-linear-gradient(top, #ff2800 0%, #bf0000 100%);
	/* Chrome10+,Safari5.1+ */
	background: -o-linear-gradient(top, #ff2800 0%, #bf0000 100%);
	/* Opera 11.10+ */
	background: -ms-linear-gradient(top, #ff2800 0%, #bf0000 100%);
	/* IE10+ */
	background: linear-gradient(to bottom, #ff2800 0%, #bf0000 100%);
	/* W3C */
	filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ff2800',
		endColorstr='#bf0000', GradientType=0); /* IE6-9 */
	color: #ffffff;
	text-shadow: none;
	display: block;
	height: 25px;
	font-size: 10px;
	line-height: 1.9;
	width: 25px;
	border: 2px solid #ffffff;
	position: absolute;
	margin-top: -40px;
	margin-left: 12px;
}
</style>
<!-- 调度完成往控制中心推送消息模态框 -->
	<div class="modal  add_user_box" id="controlRoomDispatchMsgModal"
		tabindex="-1" role="dialog"
		aria-labelledby="controlRoomDispatchMsgModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
<!-- 报警中心往巡逻队推送任务 的通知-->
<div class="modal  add_user_box" id="missionFromcontrolRoom"
	tabindex="-1" role="dialog" aria-labelledby="missionFromcontrolRoom">
	<div class="modal-dialog" role="document">
		<div class="modal-content"></div>
	</div>
</div>

<!-- 巡逻队模态框 -->
	<div class="modal  add_user_box" id="addPatrolModal" tabindex="-1"
		role="dialog" aria-labelledby="addPatrolModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>

<%--控制中心往巡逻队转发消息--%>
	<div class="modal  add_user_box" id="dispatchMsgModal" tabindex="-1"
		role="dialog" aria-labelledby="dispatchMsgModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- Modal -->

<!-- 控制中心接到交班任务审批通知-->
<div class="modal  add_user_box" id="shiftTaskAudit" tabindex="-1"
	role="dialog" aria-labelledby="shiftTaskAudit">
	<div class="modal-dialog" role="document">
		<div class="modal-content"></div>
	</div>
</div>
<!-- Modal -->

<!-- 控制中心普通员工接到主管同意交班的通知-->
<div class="modal  add_user_box" id="allowShift" tabindex="-1"
	role="dialog" aria-labelledby="allowShift">
	<div class="modal-dialog" role="document">
		<div class="modal-content"></div>
	</div>
</div>
<!-- Modal -->

<!-- 控制中心普通员工接到主管不同意交班的通知-->
<div class="modal  add_user_box" id="notAllowShift" tabindex="-1"
	role="dialog" aria-labelledby="notAllowShift">
	<div class="modal-dialog" role="document">
		<div class="modal-content"></div>
	</div>
</div>
<!-- Modal -->

<!-- 控制中心主管给交班任务接收人发送提醒消息-->
<div class="modal  add_user_box" id="msgToTaskReceiver" tabindex="-1"
	role="dialog" aria-labelledby="msgToTaskReceiver">
	<div class="modal-dialog" role="document">
		<div class="modal-content"></div>
	</div>
</div>
<!-- Modal -->

<!-- 控制中心给巡逻队发送交接调度任务的通知 -->
<div class="modal add_user_box" id="handOverDispatchTask" tabindex="-1"
	role="dialog" aria-labelledby="handOverDispatchTask">
	<div class="modal-dialog" role="document">
		<div class="modal-content"></div>
	</div>
</div>
<!-- Modal -->

<!-- 控制中心给巡逻队发送交接护送车队任务的通知(填写通知内容) -->
<div class="modal add_user_box" id="handOverEscortTask" tabindex="-1"
	role="dialog" aria-labelledby="handOverEscortTask">
	<div class="modal-dialog" role="document">
		<div class="modal-content"></div>
	</div>
</div>
<!-- Modal -->

<!-- 控制中心给巡逻队发送交接调度任务的通知(websocket) -->
<div class="modal add_user_box" id="handOverTaskMsgModal" tabindex="-1"
	role="dialog" aria-labelledby="handOverTaskMsgModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content"></div>
	</div>
</div>
<!-- Modal -->

<!-- followup user给巡逻队发送交接护送任务的通知(websocket) -->
<div class="modal add_user_box" id="escorthandOverTaskMsgModal"
	tabindex="-1" role="dialog"
	aria-labelledby="escorthandOverTaskMsgModal">
	<div class="modal-dialog" role="document">
		<div class="modal-content"></div>
	</div>
</div>
<!-- Modal -->

	<!-- 系统通知模态框 -->
	<div class="modal add_user_box" id="systemMsgModal"
		tabindex="-1" role="dialog"
		aria-labelledby="systemMsgModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- Modal -->
	<!-- 控制中心执行调度后给口岸用户发出调度通知 -->
	<div class="modal add_user_box" id="dispatchNoticeToPort"
		tabindex="-1" role="dialog"
		aria-labelledby=""dispatchNoticeToPort"">
		<div class="modal-dialog" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<!-- Modal -->
	
<div class="col-md-1 sub_menu">
	<div class="user_box dropdown">
		<a href="" class="avatar dropdown-toggle" id="drop1"
			data-toggle="dropdown" role="button" aria-haspopup="true"
			aria-expanded="false"><img
			src="${root }/static/images/user.png" alt=""> <%-- <span class="messagenumber" style="width: 30px; height: 25px;color:white;position:absolute;left:25px;top:35px;">${sessionScope.needDealNoticeCount}</span> --%></a>
	</div>

	<ul>
		<c:if
			test="${(sessionScope.sessionUser != null) && (sessionScope.sessionUser.systemFunctionList != null) }">
			<c:forEach var="function"
				items="${sessionScope.sessionUser.systemFunctionList}"
				varStatus="status">
				<c:choose>
					<c:when test="${function.functionType == '0' }">
						<c:if test="${!status.first }">
	</ul>
	</li>
	</c:if>
	<li class="dropdown"><a id="drop2" href="#"
		class="dropdown-toggle" data-toggle="dropdown" role="button"
		aria-haspopup="true" aria-expanded="false"> <span
			title="<fmt:message key='${function.functionName }'/>"
			class="glyphicon menu${function.functionId}"></span>
	</a> <%--系统管理二级菜单过长，调整top样式 --%>
		<ul class="dropdown-menu" aria-labelledby="drop2"
			<c:if test="${function.levelCode == '9' }">style="top:50%; transform:translateY(-50%);"</c:if>>
			</c:when>
			<c:when test="${function.functionType == '1' }">
				<c:choose>
					<c:when
						test="${function.functionName == 'link.main' && (sessionUser.roleName == 'enforcementPatrol' || sessionUser.roleName == 'escortPatrol') }">
						<li><a href="${root }/patrol_index.jsp"><fmt:message
									key="${function.functionName }" /></a></li>
						<c:if test="${function.afterIsSeperator == '1' }">
						<li class="divider"></li>
						</c:if>
					</c:when>
					<c:otherwise>
						<li><a href="${root }${function.functionPath }"><fmt:message
									key="${function.functionName }" /></a></li>
						<c:if test="${function.afterIsSeperator == '1' }">
						<li class="divider"></li>
						</c:if>
					</c:otherwise>
				</c:choose>
			</c:when>
			</c:choose>
			<c:if test="${status.last }"></li>
	</ul>
	</c:if>
	</c:forEach>
	</c:if>
	</ul>

	<!-- 非模态框 -->
	<div class="ico-list">
		<ul>
			<%--
			<li><a href="#" id="unDealMissionDialog" onclick="missiom()"
				title=<fmt:message key="mission.not.dealed"/>><span
					class="task-list"></span><span class="messagenumber">${sessionScope.needDealMissionCount}</span></a></li>
			 --%>
			<li><a href="#" id="noticeDialog"
				title=<fmt:message key="notice.not.readed.notice"/>><span
					class="alarm-list"></span><span id = "needDealNoticeCount" class="messagenumber">${sessionScope.needDealNoticeCount}</span></a></li>
			<!-- 
			<li><a href="#" title=<fmt:message key="chat.window"/>><span
					class="chat-list"></span><span class="messagenumber">56</span></a></li>
			 -->
		</ul>
	</div>


</div>
<div class="col-md-12 profile profile02 profile_closed" id="profile">
	<div class="">
		<span class="close_span" id="open_span"><a href="#"
			class="close-profile-link clooses" id="link_open">
			<span class="glyphicon glyphicon-menu-right"></span>
			</a></span>
		<!--User info-->
		<div class="row">
			<div class="col-md-12">
				<div class="user">
					<div class="user_box">
						<a href="" class="avatar dropdown-toggle" id="drop1"
							data-toggle="dropdown" role="button" aria-haspopup="true"
							aria-expanded="false"><img
							src="${root }/static/images/user.png" alt=""> <%-- <span class="messagenumber" style="width: 30px; height: 25px;color:white;position:absolute;left:25px;top:35px;">${sessionScope.needDealNoticeCount}</span> --%></a>
					</div>
					<div class="about_user">
						<h2>${sessionScope.sessionUser.userName }</h2>
						<ul>
							<li><fmt:message key="user.userName" />: <a href="#">${sessionScope.sessionUser.userAccount }</a></li>
							<li><fmt:message key="user.userPhone" />: <a href="#">${sessionScope.sessionUser.userPhone }</a></li>
							<li><fmt:message key="user.role" />: <a href="#">${sessionScope.sessionUser.roleName }</a></li>
							<li><fmt:message key="user.portID" />: <a href="#">${sessionScope.sessionUser.organizationName }</a></li>
							<li><fmt:message key="user.logonTime" />: <a href="#"><fmt:formatDate
										value="${sessionScope.sessionUser.logonTime }"
										pattern="yyyy-MM-dd hh:mm:dd" /></a></li>
						</ul>
					</div>
				</div>
				<div class="buttons">
					<a id="userPasswordEditButton" href="#"
						class="btn btn-darch btn-sm"><i></i></a>
					<%-- <a href="#" class="btn btn-darch btn-sm"><i class="message"></i><span style="width: 30px; height: 20px;">${sessionScope.needDealNoticeCount}</span></a> --%>
					<a id="exitSystemButton" href="#" class="btn btn-darch btn-sm"><i
						class="exit"></i></a>
				</div>
			</div>
		</div>
		<!--/User info-->
	</div>
</div>
<div class="header02 margin60">
	<div class="logo">
		<span> E-Tracking</span> System
	</div>
</div>
<!-- Modal -->
<div class="modal add_user_box" id="userPasswordEditModal" tabindex="-1"
	role="dialog" aria-labelledby="userPasswordEditModalTitle">
	<div class="modal-dialog" role="document">
		<div class="modal-content"></div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		if($(".sub_menu li.dropdown").length < 2) {
			$(".sub_menu li.dropdown:last>ul").css("transform", "translateY(-30%)");
		}
		$('[data-toggle="popover"]').popover();
		$('#link_open').on(
				'click',
				function() {
					if ($('#link_open').hasClass("clooses")) {
						$("#open_span").removeClass("close_span").addClass(
								"open_span");
						$("#profile").removeClass("profile_closed");
						$("#link_open").removeClass("clooses").children("span").toggleClass("glyphicon-menu-right  glyphicon-menu-left");
						$("#cont").addClass("none");
					} else {
						$("#open_span").addClass("close_span").removeClass(
								"open_span");
						$("#profile").addClass("profile_closed");
						$("#link_open").addClass("clooses").children("span").toggleClass("glyphicon-menu-right  glyphicon-menu-left");
						$("#cont").removeClass("none");
					}
				})

		$("#exitSystemButton")
				.click(
						function() {
							bootbox
									.confirm(
											'<fmt:message key="login.exit"/>',
											function(result) {
												if (result) {
													window.location.href = "${root }/security/exitSystem.action";
												}
											});
						});

		$("#userPasswordEditButton").click(function() {
			// alert();
			var url = "${root}/security/userPasswordEditModal.action";
			$('#userPasswordEditModal').removeData('bs.modal');
			$('#userPasswordEditModal').modal({
				remote : url,
				show : false,
				backdrop : 'static',
				keyboard : false
			});
		});

		$('#userPasswordEditModal').on('loaded.bs.modal', function(e) {
			$('#userPasswordEditModal').modal('show');
		});
		//模态框登录判断
		$('#userPasswordEditModal').on('show.bs.modal', function(e) {
			var content = $(this).find(".modal-content").html();
			needLogin(content);
		});
	})
</script>

<!-- artDialog 普通通知-->
<script type="text/javascript">
	$("#noticeDialog").click(function() {
		var a = dialog({
			id : 'noticeDialog',
			title : $.i18n.prop('notice.not.readed.notice'),
			url : '${root}/undealNotice/toList.action',
			height : '12cm',
			width : '20cm',
			left : '10%',
			fixed : 'false',
		});
		a.show();
	})

	function closeNotice() {
		var a = dialog({
			id : 'noticeDialog',
			title : $.i18n.prop('notice.not.readed.notice'),
			url : '${root}/undealNotice/toList.action',
			height : '12cm',
			width : '20cm',
			left : '10%',
			fixed : 'false',
		});
		a.close();
	}
</script>


<!-- artDialog 待办任务-->
<script type="text/javascript">
	$("#unDealMissionDialog").click(function() {
		var d = dialog({
			id : 'missionDialog',
			title : $.i18n.prop('mission.not.dealed'),
			url : '${root}/undealMission/toList.action',
			height : '12cm',
			width : '20cm',
		});
		d.show();
	})

	function closeDialog() {
		var d = dialog({
			id : 'missionDialog',
			title : $.i18n.prop('mission.not.dealed'),
			url : '${root}/undealMission/toList.action',
			height : '12cm',
			width : '20cm',
		});
		d.close();
	}
</script>