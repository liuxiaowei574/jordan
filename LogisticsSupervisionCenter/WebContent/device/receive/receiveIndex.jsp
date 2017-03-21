<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="system.notice.title" /></title>
</head>
<body>
	<%--行程请求推送通知页面 --%>
	<%@ include file="../../include/tripMsgModal.jsp" %>
	<%@ include file="../../include/left.jsp"%>
	<div class="row site">
		<div class="wrapper-content margint95 margin60">
			<%--导航 --%>
			<c:set var="pageName"><fmt:message key="link.device.dispatch.receive"/></c:set>
			<jsp:include page="../../include/navigation.jsp" >
				<jsp:param value="${pageName }" name="pageName"/>
			</jsp:include>

			<div class="profile profile_box02">
				<div class="tab-content m-b">
					<div class="tab-cotent-title">
						<fmt:message key="receive.management" />
					</div>
					<div class="search_form">
						<form class="form-horizontal row" id="ReceiveForm" action=""
							onsubmit="return false;">
							<div class="form-group col-sm-4">
								<label for="roleIds" class="col-sm-4 control-label"><fmt:message
										key="Out.of.the.port" /> </label>
								<div class="col-sm-8">
									<select style="/* font-size: 10px */" id="s_fromPort"
										name="s_fromPort" class="form-control">
										<option value=""><fmt:message key="please.choose" /></option>
										<c:forEach var="SystemDepartmentBO" items="${deptList}">
											<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
										</c:forEach>
									</select>
								</div>
							</div>

							<div class="form-group">
								<div class="col-sm-offset-9 col-md-3">
									<button type="button" class="btn btn-danger"
										onclick="doSearch();">
										<fmt:message key="common.button.query" />
									</button>
									<button id="resetSearchBtn" type="button" form="ElockForm"
										class="btn btn-darch" onclick="doRest();">
										<fmt:message key="common.button.reset" />
									</button>
								</div>
							</div>
						</form>
					</div>
				</div>
				<!--/search form-->
				<!--my result-->
				<div class="tab-content">
					<div class="tab-cotent-title">
						<div class="Features pull-right">
							<ul>
								<li><a id="receive" class="btn btn-info"
									onclick="deviceReceive();"><fmt:message key="reveive.ready" /></a></li>
							</ul>
						</div>
						<fmt:message key="reveive.list" />
					</div>
					<div class="search_table">
						<div>
							<table id="receiveTable"></table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var selections = [];
		var $table = $('#receiveTable');
		//刷新tale
		$(window).resize(function(){
			$table.bootstrapTable("resetView");
		});
		function doSearch() {
			var params = $table.bootstrapTable('getOptions');
			params.queryParams = function(params) {
				//遍历form 组装json  
				$.each($("#ReceiveForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params[field.name] = field.value;
				});
				return params;
			}
			$table.bootstrapTable('refresh', params);
		}

		$(function() {
			//设置传入参数
			function queryParams(params) {
				//遍历form 组装json  
				$.each($("#ReceiveForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params += "&" + field.name + "=" + field.value;
				});
				return params;
			}
			$table.bootstrapTable({
				url : '${root}/receive/list.action',
				clickToSelect : true,
				showRefresh : false,
				search : false,
				showColumns : false,
				showExport : false,
				striped : true,
				//height : "100%",
				method : "get",
				idfield : "dispatchId",
				sortName : "dispatchId",
				cache : false,
				sortable:true,
				pagination : true,
				sidePagination : 'server',
				pageNumber : 1,
				pageSize : 10,
				pageList : [ 10, 20, 30 ],
				columns : [ {
					checkbox : true
				}, {
					field : 'toPortName',
					title : $.i18n.prop('application.toPort'),
					sortable:true
				}, {
					field : 'fromPortName',
					title : $.i18n.prop('application.fromPort'),
					sortable:true

				}, {
					field : 'deviceNumber',
					title : $.i18n.prop('elock.number'),
					sortable:true
				}, {
					field : 'esealNumber',
					title : $.i18n.prop('eseal.number'),
					sortable:true
				}, {
					field : 'sensorNumber',
					title : $.i18n.prop('sensor.number'),
					sortable:true
				}, {
					field : 'dispatchStatus',
					title : $.i18n.prop('device.Status'),
					formatter : dispatchStatusFormat,
					sortable:true
				},{
					field : 'dispatchTime',
					title : $.i18n.prop('device.dispatchTime'),
					sortable:true
				},{
					field : 'dispatchUser',
					title : $.i18n.prop('device.dispatchUser'),
					sortable:true
				}],
			});

		});
	</script>

	<%--搜索表单重置 --%>
	<script type="text/javascript">
		function doRest() {
			$("#resetSearchBtn").click(function() {
				$("#ElockForm")[0].reset();
				function resetQuery() {
					$table.bootstrapTable('refresh', {});
				}
			});
			window.location.reload();
		}
	</script>


	<!-- 准备接收按钮 -->
	<script type="text/javascript">
		function deviceReceive() {
			var receiveList = $('#receiveTable')
					.bootstrapTable('getSelections');
			var l = receiveList.length;
			
			if(l==0){
				bootbox.alert($.i18n.prop("please.choose.toReceive"));
			}else if(l>1){
				bootbox.alert($.i18n.prop("please.choose.onlyOne.toReceive"));
			}else{
			
			/* 获取调配Id */
			var receiveDispatchIds = new Array();
			for ( var o in receiveList) {
				receiveDispatchIds.push(receiveList[o].dispatchId);
			}

			/* 获取申请主键 */
			var applicationIds = new Array();
			for ( var o in receiveList) {
				applicationIds.push(receiveList[o].applicationId);
			}
			var url = "${root}/receive/receiveReady.action?dispatchId="
					+ receiveDispatchIds + "&applicationId=" + applicationIds;
			window.location.href = url;
			}
		}		
	</script>

	<!-- 将原先从数据库中查出来的值换为中文 -->
    <script type="text/javascript">
		function dispatchStatusFormat(value, row, index){
			var show;
			if(value =='0'){
				show = $.i18n.prop('not.dispatch');
			}else if(value =='1'){
				show =$.i18n.prop('have.dispatch');
			}else if(value =='2'){
				show = $.i18n.prop('dispatch.reject.schedule');
			}else{
				show = '--'
			}
			return [show].join('');
			
		}
    
    </script>
</body>
</html>