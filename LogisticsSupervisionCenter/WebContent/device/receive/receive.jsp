<%@page import="jcifs.util.transport.Request"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../include/include.jsp" />
<title><fmt:message key="WarehouseElock.list.title" /></title>
</head>
<body>
	<%--行程请求推送通知页面 --%>
	<%@ include file="../../include/tripMsgModal.jsp" %>
	<%@ include file="../../include/left.jsp"%>
	<div class="row site">
		<div class="wrapper-content margint95 margin60">
			<form role="form" id="RandomForm"
				action="${root}/dispatch/deviceDispatch.action" method="post">
				<div class="profile profile_box02">
					<div class="tab-content m-b">
						<div class="row">
							<div class="col-md-12 my_news">
								<div class="tab-cotent-title">
									<div class="Features pull-right">
										<ul>
											<li><a id="Manual" class="btn btn-info"
												onclick="ManualSelect()"><fmt:message
														key="Manual.Select" /></a></li>
											<li><a id="all" class="btn btn-info"
												onclick="allReceive()"><fmt:message key="all.Receive" /></a></li>
											<li><a class="btn btn-info" onclick="javascript: history.back(-1);"><fmt:message key="common.button.back"/></a></li>
										</ul>
									</div>
									<fmt:message key="device.Receive" />
								</div>
								<div class="search_table">
									<div>
										<table id="receiveTable">
											<%-- <tBody id=deviceId>
												 <c:forEach var="s" items="${deviceDetailList}">
													<tr>
														<td> </td>
														 <td>${s.deviceNumber}</td>
												         <td>${s.deviceType}</td>
												         <td>${s.recviceStatus}</td>
												          <td>${s.recviceUser}</td>
												         <td>${s.recviceTime}</td>
									  				</tr>
									  			</c:forEach> 
											</tBody>  --%>
										</table>

									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- 设备接收 -->
	<script type="text/javascript">
		var selections = [];
		var $table = $('#receiveTable');
		function doSearch() {
			var params = $table.bootstrapTable('getOptions');
			params.queryParams = function(params) {
				//遍历form 组装json  
				$.each($("#ElockForm").serializeArray(), function(i, field) {
					console.info(field.name + ":" + field.value + " ");
					//可以添加提交验证                   
					params[field.name] = field.value;
				});
				return params;
			}
			$table.bootstrapTable('refresh', params);
		}

		$(function() {
			var dispatchId = "${dispatchId}"
			$table.bootstrapTable({
				url : "${root}/receive/dlist.action?id=" + "${dispatchId}",
				pagination : true,
				pageSize : 10,
				smartDisplay : false,
				pageList : [ 10, 20, 30 ],
				maintainSelected : true,
				columns : [ {
					checkbox : true
				}, {
					field : 'deviceNumber',
					title : $.i18n.prop('device.Number')
				}, {
					field : 'deviceType',
					title : $.i18n.prop('device.Type'),
					sortable:true,
					formatter : stateFormatter
				}, {
					field : 'recviceStatus',
					title : $.i18n.prop('device.recviceStatus'),
					formatter : receiveFormat
				}, {
					field : 'recviceUser',
					title : $.i18n.prop('device.recviceUser')
				}, {
					field : 'recviceTime',
					title : $.i18n.prop('device.recviceTime')
				}, ]
			});
		});
	</script>

	<!-- 手动选择接收-->
	<script type="text/javascript">
		function ManualSelect() {
			/* 获取后台放到request中的参数 */
			var applicationId = "${applicationId}"
			/* 获取表格的总长度 用于后台判断*/
			var receiveTableList = $('#receiveTable').bootstrapTable('getData')
			var length = receiveTableList.length;

			/* 获得设备调配明细表的接收状态 */
			var recviceStatuslist = new Array();
			for ( var o in receiveTableList) {
				recviceStatuslist.push(receiveTableList[o].recviceStatus);
			}

			
			
			
			/* 获得设备调配明细表的主键 */
			var receivelist = $('#receiveTable')
					.bootstrapTable('getSelections');
			
			
			
			/* 获得勾选设备主键 */
			var DeviceIds = new Array();
			for ( var o in receivelist) {
				DeviceIds.push(receivelist[o].deviceId);
			}

			/*校验，选择并且只能选择“接收状态”为1的进行接收 ；获取接收状态 ,只对接收状态为0的进行调度*/
			var recviceStatusList = new Array();
			for ( var o in receivelist) {
				recviceStatusList.push(receivelist[o].recviceStatus);
			}
			/* 判断勾选的行中是否有已经接收的即接收状态为“1” */
			var boolean = false;
			var a = "1";
			for (var i = 0; i < recviceStatusList.length; i++) {
				if (recviceStatusList[i].indexOf(a) > -1) {
					boolean = true;
				}
			}
			if (recviceStatusList.length == 0) {
				bootbox.alert($.i18n.prop("please.choose.receive.device"));
			} else if (boolean) {
				bootbox.alert($.i18n.prop("donot.choose.which.is.received"));
			} else {

				var detailIds = new Array();
				for ( var o in receivelist) {
					detailIds.push(receivelist[o].detailId);
				}
				var ajaxUrl = "${root}/receive/manualSelect.action?detailIds="
						+ detailIds + "&tableLength=" + length
						+ "&recviceStatuS=" + recviceStatuslist
						+ "&applicationId=" + applicationId+"&deviceIds="+DeviceIds;
				
				/* $.ajax({
					url : ajaxUrl,
					type : "post",
					dataType : "json",
					data : {
					},
					success : function(data) {
						if(data == true){
							bootbox.alert("接收成功");
						}
					}
				});
				window.location.reload(); */
				
				  $.post(ajaxUrl, function(data) {
					  if(!needLogin(data)) {
						if(data) {
							bootbox.alert($.i18n.prop("receive.success"));
				  			$table.bootstrapTable('refresh', {});
						} else {
							bootbox.error($.i18n.prop("receive.fail"));
							$table.bootstrapTable('refresh', {});
						}
					  }
					 }, "json");
				
			}
		}
	</script>


	<!--全部接收  -->
	<script type="text/javascript">
		function allReceive() {
			/* 获取后台放到request中的参数 */
			var applicationId = "${applicationId}"
			
			var receiveTableList = $('#receiveTable').bootstrapTable('getData')
			
			/* 获得全部设备主键 */
			var allDeviceIds = new Array();
			for ( var o in receiveTableList) {
				allDeviceIds.push(receiveTableList[o].deviceId);
			}
			
			/* 获取列表中所有调配详细表的主键 */
			var allDetailIds = new Array();
			for ( var o in receiveTableList) {
				allDetailIds.push(receiveTableList[o].detailId);
			}
			var ajaxUrl = "${root}/receive/selectAll.action?allDetailIds="+ allDetailIds+"&applicationId="+applicationId+"&allDeviceIds="+allDeviceIds
			/*  $.ajax({
				url : ajaxUrl,
				type : "post",
				dataType : "json",
				data : {},
				success : function(data) {
					$('#receiveTable').bootstrapTable('refresh', {});
				}
			});
			window.location.reload();  */
			
			 $.post(ajaxUrl, function(data) {
				 if(!needLogin(data)) {
					if(data) {
						bootbox.alert($.i18n.prop("receive.success"));
			  			$table.bootstrapTable('refresh', {});
					} else {
						bootbox.error($.i18n.prop("receive.fail"));
						$table.bootstrapTable('refresh', {});
					}
				 }
			 }, "json"); 
		}
	</script>
	
<!-- 将列表的设备类型字段换成中文 -->	
<script type="text/javascript">
	function stateFormatter(value, row, index) {
	var show;
	if(value == 'TRACKING_DEVICE') {
		show = $.i18n.prop('elock.format.convert');
	} else if (value == 'ESEAL') {
		show = $.i18n.prop('eseal.format.convert');
	} else if(value == 'SENSOR') {
		show = $.i18n.prop('sensor.format.convert');
	} else {
		show = '--';
	}
	return [show].join('');
}
	
	function receiveFormat(value, row, index){
		var show;
		if(value =='0'){
			show = $.i18n.prop('not.receive');
		}else if(value =='1'){
			show =$.i18n.prop('have.receive');
		}else{
			show = '--'
		}
		return [show].join('');
	}
	
	
</script>
	
</body>
</html>