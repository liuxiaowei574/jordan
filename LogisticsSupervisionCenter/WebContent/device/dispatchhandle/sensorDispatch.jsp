<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%--模态框页面，不能重复引用include.jsp，否则左侧菜单不能正常显示 --%>
<%-- <jsp:include page="../../include/include.jsp" /> --%>
<title><fmt:message key="WarehouseElock.list.title" /></title>
</head>
<body>
	<div class="row">
		<div class="col-md-12 my_news">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="addSensorModal">
					<fmt:message key="choose.sensor" />
				</h4>
			</div>
			<div class="modal-body">
				<div class="search_table">
					<table id="select_sensor_table"
						class="table table-bordered table-striped">
					</table>
				</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="modal-footer margin15">
			<button type="button" class="btn btn-danger" data-dismiss="modal"
				id="sensorDispatch">
				<fmt:message key="dispatch.sure" />
			</button>
			<button type="button" class="btn btn-darch" data-dismiss="modal">
				<fmt:message key="common.button.cancle" />
			</button>
		</div>
	</div>

	<script type="text/javascript">
		var selections = [];
		var $table = $('#select_sensor_table');
		$(function() {
			$table.bootstrapTable({
				url : '${root}/sensorMgmt/dlist.action',
				height : $(window).height() - 300,//固定模态框 的 宽度 
				pagination : true,
				pageSize : 5,
				maintainSelected : true,
				pageList : [ 10, 20, 30 ],
				columns : [ {
					checkbox : true
				}, {
					field : 'sensorNumber',
					title : $.i18n.prop('WarehouseSensor.sensorNumber')
				}, {
					field : 'organizationName',//组织机构表里面的"机构名称"
					title : $.i18n.prop('WarehouseSensor.belongTo')
				}, {
					field : 'sensorStatus',
					title : $.i18n.prop('WarehouseSensor.sensorStatus'),
					formatter : sensorStatus
				} ],
				onLoadSuccess : function(data) {

					var arrList = new Array();
					var numbers = "${sensorarrayList}"; //取出后台的sensorarrayList(字符串)
					if (numbers != null && "" != numbers) {
						numbers = numbers.substring(0, numbers.length - 1);//把字符串的最后一个 字符删除 (逗号)
						arrList = numbers.split(",");
					}

					$('#select_sensor_table').bootstrapTable("checkBy", {
						field : "sensorNumber",
						values : arrList
					});
				}
			});
			$("#sensorDispatch").click(
					function() {
						var list = $('#select_sensor_table').bootstrapTable(
								'getSelections'); //获取表的行
						/* 模态框中的表头和调度页面表头中的field不同；新建一个conlist(属性名为ELOCK_NUMBER等)把原先list中的对象放到里面 */
						var sensorList = new Array();
						for (var i = 0; i < list.length; i++) {
							var obj = new Object();
							obj.SENSOR_ID = list[i].sensorId;
							obj.SENSOR_NUMBER = list[i].sensorNumber;
							obj.ORGANIZATION_NAME = list[i].organizationName;
							obj.SENSOR_STATUS = list[i].sensorStatus;
							obj.SENSOR_TYPE = list[i].sensorType;
							sensorList.push(obj);
						}
						/*将数据传到DeviceDispatch.jsp  */
						addSensorTable(sensorList);
					})

		});
	</script>

	<script type="text/javascript">
		function sensorStatus(value, row, index) {
			var show;
			if (value == '0') {
				show = $.i18n.prop('DeviceStatus.Scrap');
			} else if (value == '1') {
				show = $.i18n.prop('DeviceStatus.Normal');
			} else if (value == '2') {
				show = $.i18n.prop('DeviceStatus.Inway');
			} else if (value == '3') {
				show = $.i18n.prop('DeviceStatus.Destory');
			} else if (value == '4') {
				show = $.i18n.prop('DeviceStatus.Maintain');
			} else {
				show = '--'
			}
			return [ show ].join('');
		}
	</script>

</body>
</html>