<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<c:set var="root" value="${pageContext.request.contextPath}" />
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Trip</title>
<script type="text/javascript">
var tripData=${tripArr};
</script>
</head>
<body>
<div class="modal-header">
     <button type="button" class="close" data-dismiss="modal" 
        aria-hidden="true">×
     </button>
     <h4 class="modal-title" id="myModalLabel">
       <fmt:message key="trip.message" />
     </h4>
 </div>
 <div class="modal-body">
	<table id="tripTable" />
</div>


<script type="text/javascript">
$(function() {
	//设置传入参数
	function queryParams(params) {
		/* //遍历form 组装json  
        $.each($("#sform").serializeArray(), function(i, field) {  
            console.info(field.name + ":" + field.value + " ");  
            //可以添加提交验证                   
            params += "&" + field.name +"="+ field.value;  
        });   */
        return params;
	}
	$('#tripTable').bootstrapTable({
		//height: $(window).height() - 200,
		striped: true,
		dataType: "json",
		pagination: true, //分页
		idfield: "tripId",
		sortName:"tripId",
		"queryParamsType": "limit",
		singleSelect: false,
		contentType: "application/x-www-form-urlencoded",
		pageSize: 10,
		pageNumber:1,
		//totalRows:10,
		search: false, //不显示 搜索框
		showColumns: false, //不显示下拉框（选择显示的列）
		queryParams: queryParams,

		pageList : [ 10, 20, 30 ],
		search: true, //显示搜索框
		//sidePagination: "server", //服务端处理分页
	    columns: [{
	        field: 'tripId',
	        title: '<fmt:message key="trip.table.tripId"/>'
	    },{
	    	field:'vehicleId',
	    	title:"<fmt:message key="trip.table.vehicleId"/>"
	    },{
	    	field:'vehiclePlateNumber',
	    	title:"<fmt:message key="trip.table.vehiclePlateNumber"/>"
	    },{
	    	field:'driverName',
	    	title:"<fmt:message key="trip.table.driverName"/>"
	    },{
	    	field:'checkinTime',
	    	title:"<fmt:message key="trip.table.checkinTime"/>"
	    },{
	    	field:'checkinUser',
	    	title:"<fmt:message key="trip.table.checkinUser"/>"
	    },{
	    	field:'checkoutTime',
	    	title:"<fmt:message key="trip.table.checkoutTime"/>"
	    },{
	    	field:'checkoutUser',
	    	title:"<fmt:message key="trip.table.checkoutUser"/>"
	    },{
	    	field:'timeCost',
	    	title:"<fmt:message key="trip.table.timeCost"/>"
	    }],
	    data:tripData
	});
});

</script>

</body>
</html>