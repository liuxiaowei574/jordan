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
	<%--调度完成通知页面 --%>
	<%@ include file="../include/tripMsgModal.jsp"%>
	<div class="row">
		<div class="col-md-12 my_news">
			<div class="modal-header">
				<input type="hidden" class="form-control input-sm"id="taskIds" name="taskIds" value="${taskIds}">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="addPatrolModal">
					<fmt:message key="system.role.contromRoomUser"/>
				</h4>
			</div>

			<div class="modal-body">
				<div>
					<table id="patrolTable" class="table table-bordered table-striped">
					</table>
				</div>
			</div>
			<div class="clearfix"></div>
			<div class="modal-footer margin15">
				<button type="button" class="btn btn-danger" data-dismiss="modal"
					id="sendMsgBtn">
					<fmt:message key="AlarmDealMethod.Forward"/>
				</button>
				<button type="button" class="btn btn-darch" data-dismiss="modal">
					<fmt:message key="common.button.cancle" />
				</button>
			</div>

		</div>
	</div>
	<script type="text/javascript">
		var $table = $('#patrolTable');
		var taskIds = $("#taskIds").val();//input框不能接收字符串数组
		/**
		 * 控制中心普通员工列表 (除去自身)
		 */
		$(function() {
			$table.bootstrapTable({
				url : root + "/userMgmt/dlist.action",
				height : $(window).height() - 500,//固定模态框 的 宽度 
				pagination : true,
				pageSize : 5,
				maintainSelected : true,
				columns : [ {
					checkbox : true
				}, {
					field : 'userName',
					title : $.i18n.prop("mission.name")
				}, {
					field : 'userPhone',
					title :$.i18n.prop("mission.phone.number")
				}],
			})
		})

		/**
		 * 点击交班任务按钮推送消息(推送消息给控制中心管理人员审核)
		 */
		$("#sendMsgBtn").click(function() {
							//获取控制中心普通人员userid;
							var list = $table.bootstrapTable('getSelections'); //获取表的行
							var ids = $.map($table.bootstrapTable('getSelections'), function(row) {
								return row.userId
							});
							var contromuserId = ids.join(",");
							if(list.length<=0){
								bootbox.alert($.i18n.prop("dispatch.choose.patrol.to.push.message"));
								return;
							}else if(list.length>1){
								bootbox.alert($.i18n.prop("mission.forward.only.one.controlroomuser"));
								return;
							}else{
								//交班任务
								var ajaxUrl = "${root}/dispatchSendMsg/forwardMisionToControlroomUser.action?contromuserId="
									+ contromuserId
									+ "&taskIds="
									+ taskIds;
							$.ajax({
										url : ajaxUrl, // 请求url
										type : "post", // 提交方式
										dataType : "json", // 数据类型
										success : function(data) { // 提交成功的回调函数
											if(!needLogin(data)) {
												if (data) {
													bootbox.success($.i18n.prop('mission.shift.review.wait.audit'),function(){
													});
												} else {
													bootbox.error($.i18n.prop('dispatch.pushMsg.fail'));
												}
											}
										}
									});
								
							}
							/* window.location.reload();  */
						});
	</script>
</body>
</html>