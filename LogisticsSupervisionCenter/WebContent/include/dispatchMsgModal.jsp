<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="elock.addElock" /></title>
</head>
<body>
	<!-- 调度完成向巡逻队推送消息Modal -->
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title" id="noticeModalTitle">
			<fmt:message key="system.notice.tip" />
		</h4>
	</div>
	<form id="dispatchMsgForm" class="form-horizontal row">
		<div class="modal-body">
			<input type="hidden" id="noticeId" name="noticeId">
			<!--对应websocket.js中的log\\.noticeId  -->
			<div class="col-md-10">
				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="notice.title" /></label>
					<div class="col-sm-8">
						<input type="text" id="dispatchMsgTitle"
							class="form-control input-sm" readonly="readonly">
					</div>
				</div>
				<div class="form-group ">
					<label class="col-sm-4 control-label"><fmt:message
							key="notice.content" /></label>
					<div class="col-sm-8">
						<textarea rows="3" cols="15" id="dispatchMsgContent"
							class="form-control input-sm" readonly="true">
								</textarea>
					</div>
				</div>
				<div>
					<div class="form-group col-sm-4"></div>
					<div class="form-group col-sm-8">
						<ul>
							<li><a href="javascript:void(0)" onclick="searchDetail()"><fmt:message
										key="dispatch.detail" /></a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</form>

	<div class="modal-footer">
		<button type="button" onclick="receiveDevice();"
			class="btn btn-danger" data-dismiss="modal">
			<fmt:message key="dispatch.aggree.receive" />
		</button>
		<button type="button" class="btn btn-danger" data-dismiss="modal">
			<fmt:message key="dispatch.refuse.receive" />
		</button>
	</div>

	<!-- 设备的详细信息DIV -->
	<div id="deviceDispatchDetail" class="fixed-table-container"
		style="display: none; position: fixed; left: 50%; top: 48%; z-index: 1051; background-color: #f9f9f9; text-align: center; transform: translate(-50%, -50%); width: 800px; height: 600px;">
		<button type="button" class="close" aria-label="Close"
			id="btnCloseDetail" style="margin-right: 5px;" onclick="hideDiv()">
			<span aria-hidden="true">&times;</span>
		</button>
		<!-- 显示设备的详细信息 -->
		<div class="search_table">
			<div>
				<table id="elockTable"
					class="table table-bordered table-striped table-hover">
					<thead>
						<!-- <tr>
									<th>关锁号</th>
									<th>迁出口岸</th>
									<th>sim卡号</th>
									<th>上传频率</th>
								</tr> -->
					</thead>
					<tbody>
						<c:forEach var="e" items="${elockDetailLit}">
							<tr>
								<td>${e.ELOCK_NUMBER}</td>
								<td>${e.ORGANIZATION_NAME}</td>
								<td>${e.SIM_CARD}</td>
								<td>${e.INTERVAL}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div class="search_table">
			<div>
				<table class="table table-bordered table-striped" id="esealTable">
					<thead>
						<!-- <tr>
									<th>子锁号</th>
									<th>迁出口岸</th>
								</tr> -->
					</thead>
					<tbody>
						<c:forEach var="e" items="${esealDetailLit}">
							<tr>
								<td>${e.ESEAL_NUMBER}</td>
								<td>${e.ORGANIZATION_NAME}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div class="search_table">
			<div>
				<table id="sensorTable" class="table table-bordered table-striped">
					<thead>
						<!-- <tr>
									<th>传感器号</th>
									<th>迁出口岸</th>
								</tr> -->
					</thead>
					<c:forEach var="s" items="${sensorDetailLit}">
						<tr>
							<td>${s.SENSOR_NUMBER}</td>
							<td>${s.ORGANIZATION_NAME}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<%--  <button type="button" class="btn btn-danger" id="btnClose" onclick="hideDiv1()">
			<fmt:message key="common.button.close" />
		</button> --%>
	</div>
	<!-- /Modal -->

	<script type="text/javascript">
		function receiveDevice() {
			//debugger;
			$
					.ajax({
						url : root + '/dispatchSendMsg/aggreeReceive.action',
						type : "post",
						dataType : "json",
						data : {
							'noticeId' : $("#noticeId").val()
						},
						success : function(data) {
							var needLoginFlag = false;
							if (typeof needLogin != 'undefined'
									&& $.isFunction(needLogin)) {
								needLoginFlag = needLogin(data);
							}
							if (!needLoginFlag) {
								if (data) {
									bootbox
											.success($.i18n
													.prop('dispatch.Consent.to.port.transport.equipment'));
								} else {
									bootbox
											.error($.i18n
													.prop('dispatch.notConsent.to.port.transport.equipment'));
								}
							}
						}
					});
		}
	</script>
	<!-- 显示调度详细信息 -->
	<script type="text/javascript">
		function searchDetail() {
			/* $("#deviceDispatchDetail").removeAttr("hidden"); */
			document.getElementById("deviceDispatchDetail").style.display = "block";
		}

		/* 关闭按钮,隐藏div */
		function hideDiv() {
			document.getElementById("deviceDispatchDetail").style.display = "none";
		}
		function hideDiv1() {
			document.getElementById("deviceDispatchDetail").style.display = "none";
		}
	</script>

	<script src="${root}/include/js/deviceDetail.js"></script>
</body>
</html>