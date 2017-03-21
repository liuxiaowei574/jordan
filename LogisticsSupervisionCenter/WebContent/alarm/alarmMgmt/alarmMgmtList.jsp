<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${root }/static/js/bootstrap/bootstrapEditTable/bootstrap-editable.css" />
<script type="text/javascript" src="${root}/static/bootstrap-table/bootstrap-table-editable.js"></script>
<script type="text/javascript" src="${root}/static/bootstrap-table/bootstrap-table-editable.min.js"></script>

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
			<c:set var="pageName"><fmt:message key="link.system.alarm.level"/></c:set>
			<jsp:include page="../../include/navigation.jsp" >
				<jsp:param value="${pageName }" name="pageName"/>
			</jsp:include>

			<!-- Modify Modal报警级别修改模态框 -->
			<div class="modal  add_user_box" id="updateAlarmLevelModal"
				tabindex="-1" role="dialog" aria-labelledby="updateAlarmLevelModal">
				<div class="modal-dialog" role="document">
					<div class="modal-content"></div>
				</div>
			</div>
			<!-- Modify Modal -->

			<div class="profile profile_box02">
				<!--/search form-->
				<!--my result-->
				<div class="tab-content m-b">
					<div class="tab-cotent-title">
						<div class="Features pull-right">
							<ul>
								<li><a id="editAlarmLevelBtn" class="btn btn-info"><fmt:message
											key="common.button.modify" /></a></li>
							</ul>
						</div>
						<fmt:message key="Alarm.List" />
					</div>
					<div class="search_table">
						<div>
							<table id="alarmTable"></table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var selections = [];
		var $table = $('#alarmTable');
		//刷新tale
		$(window).resize(function(){
			$table.bootstrapTable("resetView");
		});
		$(function() {
			$table.bootstrapTable({
				clickToSelect : true,
				url : '${root}/alarmModifyMgmt/list.action',
				pagination : true,
				sidePagination : 'server',
				pageNumber : 1,
				pageSize : 10,
				sortable:true,
				sortName:"alarmTypeCode",
				sortOrder: "asc",
				pageList : [ 10, 20, 30 ],
				columns : [ {
					checkbox : true
				}, {
					field : 'alarmTypeCode',
					title : $.i18n.prop('Alarm.type'),
					formatter: formview,
					sortable:false
				}, {
					field : 'alarmLevelCode',
					title : $.i18n.prop('Alarm.level'),
					formatter : levelFormatter,
					sortable:true
				}, {
					field : 'userName',
					title : $.i18n.prop('Alarm.updater'),
					sortable:true
				},{
					field : 'createTime',
					title : $.i18n.prop('Alarm.createTime'),
					sortable:true
				},{
					field : 'updateTime',
					title : $.i18n.prop('Alarm.updateTime'),
					sortable:true
				}],
			});

			//编辑Modal调用方法
			$("#editAlarmLevelBtn")
					.click(
							function() {
								var alarmLevelIds = $.map($table
										.bootstrapTable('getSelections'),
										function(row) {
											return row.alarmLevelId
										});
								var alarmList = $('#alarmTable')
										.bootstrapTable('getSelections');
								/* 获取参数id */
								var paramIds = new Array();
								for ( var o in alarmList) {
									paramIds.push(alarmList[o].paramId);
								}
								
								
								/* 获取报警级别 */
								var alarmLevel = new Array();
								for ( var o in alarmList) {
									alarmLevel
											.push(alarmList[o].alarmLevelName);
								}

								/* 获取报警名称 */
								var alarmName = new Array();
								for ( var o in alarmList) {
									alarmName.push(alarmList[o].alarmTypeName);
								}

								//获取报警类型的主键
								var alarmId = new Array();
								for ( var o in alarmList) {
									alarmId.push(alarmList[o].alarmTypeId);
								}
								//获取报警类型的alarmLevelId
								var LevelId = new Array();
								for ( var o in alarmList) {
									LevelId.push(alarmList[o].alarmLevelId);
								}
								//获取主键
								var alarmtypeIds = new Array();
								for ( var o in alarmList) {
									alarmtypeIds.push(alarmList[o].alarmTypeId);
								}


								if (alarmName.length == 0) {
									bootbox.alert($.i18n
											.prop('please.choose.alarmType'));
								} else if (alarmName.length > 1) {
									bootbox.alert($.i18n
											.prop('please.choose.onlyone'));
								} else {
									var url = "${root}/alarmModifyMgmt/editAlarmModal.action?alarmLevel="
											+ alarmLevel
											//+ "&alarmName="
											//+ alarmName
											+ "&alarmId="
											+ alarmId
											+ "&alarmLevelId="
											+ LevelId
											+ "&alarmTypeId=" + alarmtypeIds+"&paramIds="+paramIds;
									$('#updateAlarmLevelModal').removeData(
											'bs.modal');
									$('#updateAlarmLevelModal').modal({
										remote : url,
										show : false,
										backdrop : 'static',
										keyboard : false
									});
								}
							});
			$('#updateAlarmLevelModal').on('loaded.bs.modal', function(e) {
				$('#updateAlarmLevelModal').modal('show');
			});
			//模态框登录判断
			$('#updateAlarmLevelModal').on('show.bs.modal', function(e) {
				var content = $(this).find(".modal-content").html();
				needLogin(content);
			});
		});
	</script>
	 <script type="text/javascript">
		function formview(value, row, index){
			var show ;
			if(!!value){
				//show = $.i18n.prop('Alarm.Mgmt.' + value);
				show = $.i18n.prop('AlarmType.' + value);
			}else{
				show = '--'
			} 
			return [show].join('');
		}
		
		/**
		 * 报警等级显示
		 */
		function levelFormatter(value, row, index) {
			var show;
			 if (value == '0') {
				show = $.i18n.prop('AlarmLevel.Light');
			} else if(value == '1') {
				show = $.i18n.prop('AlarmLevel.Serious');
			} else {
				show = '--';
			}
			return [show].join('');
		}
    </script> 
</body>
</html>