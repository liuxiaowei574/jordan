<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<%@ include file="../include/include.jsp"%>
<!DOCTYPE html>
<html>
<head>

<title></title>
</head>
<body>
	<%@ include file="../include/left.jsp"%>
	<div class="row site">
		<div class="wrapper-content margint95 margin60">
			<%--导航 --%>
			<c:set var="parentName"><fmt:message key="menu.statistic.analysis"/></c:set>
			<c:set var="pageName"><fmt:message key="statistic.index.tripMonitor"/></c:set>
			<jsp:include page="../include/navigation2.jsp" >
				<jsp:param value="${root }/statisitc/toList.action" name="parentUrl"/>
				<jsp:param value="${parentName }" name="parentName"/>
				<jsp:param value="${pageName }" name="pageName"/>
			</jsp:include>
			
			<div class="profile profile_box02">
			<div class="col-sm-12">
			<div class="panel panel-default">
			<div class="panel-heading">
				<div class="Features pull-right">
					<ul>
						<li><a href="${root}/statisitc/toList.action" class="btn btn-info"><fmt:message key="common.button.back"/></a></li>
				  	 </ul>
				</div>
				<fmt:message key="statics.jsp.condition"/>
			</div>
			<div class="search_form">
					<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
						<div class="form-group col-md-3">
							<label class="col-sm-4 control-label"><fmt:message key="OrganizationType.Port"/></label>
							<div class="col-sm-8">
						   	    <div id="menuContent">
									<ul id="userTree" class="ztree" style="margin-top:0; width:180px; height: 300px;"></ul>
								</div>
					   	      	<select style="" id="s_portname" name="s_portname"
									class="form-control">
									<option value=""></option>
									<c:forEach var="SystemDepartmentBO" items="${deptList}">
										<option value=${SystemDepartmentBO.organizationId}>${SystemDepartmentBO.organizationName}</option>
									</c:forEach>
								</select> 
					   	       <input type="hidden" class="form-control input-sm" id="s_timeFormat" name="s_timeFormat" value="yyyy-MM-dd HH:MM">
					   	    </div>
						</div>
						<%--操作时间，起/止 --%>
						<div class="form-group col-md-6">
							<label class="col-sm-2 control-label"><fmt:message key="statics.jsp.date"/></label>
							<div class="input-group date col-sm-4" id="form_operateStartTime">
								<input type="text" class="form-control" id="s_starttime" name="s_starttime" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
							<label class="col-sm-1 control-label" style="text-align: center;">-</label>
							<div class="input-group date col-sm-4" id="form_operateEndTime">
								<input type="text" class="form-control" id="s_endtime" name="s_endtime" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
							
						</div>
						<div class="form-group col-sm-3">
							<button type="submit" class="btn btn-danger" onclick="search();"><fmt:message key="common.button.query"/></button>
							<button type="reset" class="btn btn-darch" onclick=""><fmt:message key="common.button.reset"/></button>
						</div>
						<div class="clearfix"></div>
						
					</form>
				</div>
				</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="panel panel-default">
							<div class="panel-heading">
								<fmt:message key="statics.report.jgxctj.title"/>
							</div>
							<div class="panel-body" id="monitorTripDiv">
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<div class="panel panel-default">
							<div class="panel-heading">
								<fmt:message key="statics.report.cljgtj.detail"/>
							</div>
							<div class="search_table">
								<div>
									<table id="monitorTable"></table>
								</div>
								<div>
									<table id="operateLogListTable"></table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="clearfix"></div>
	   </div>
	</div>
</body>
<script src="${root}/static/js/echarts/echarts.min.js"></script>
<script type="text/javascript">
var $table = $('#monitorTable');	
$(function() {
	//初始化日期组件
	$("#form_operateStartTime, #form_operateEndTime").datetimepicker({
		format: "yyyy-mm-dd hh:ii",//ii:分钟
		autoclose: true,
		todayBtn: true,
		language: 'en'
	}).on('changeDate', function(ev){
	    var startTime = $('#s_starttime').val();//获得开始时间
	    $('#form_operateEndTime').datetimepicker('setStartDate', startTime);//设置结束时间（大于开始时间）
	});
	
	//表格自适应
	$(window).resize(function(){
		$table.bootstrapTable("resetView");
	});
	//加载数据
	search();
});
//图表
function chart(x,y1,y2){
	var vehicleDiv = $("#monitorTripDiv")[0];
	var vehicleCharts = echarts.init(vehicleDiv);
	vehicleOption = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		        data:['Arrived','Leaved']
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    xAxis : [
		        {
		            type : 'category',
		            data : x
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : [
		        {
		            name:'Arrived',
		            type:'bar',
		            data:y1
		        },{
		            name:'Leaved',
		            type:'bar',
		            data:y2
		        }
		    ]
		};
	vehicleCharts.setOption(vehicleOption);
	vehicleCharts.on('click',function(params){
		$("#s_portname").val(params.name);
		createtable();
		//
	});
	//加载数据
	createtable();
}
//创建表格
function createtable(){
	var options = $table.bootstrapTable('getOptions');
	options.queryParams = function(tparams) {
		//遍历form 组装json  
		$.each($("#searchForm").serializeArray(), function(i, field) {
			//console.info(field.name + ":" + field.value + " ");
			//可以添加提交验证                   
			tparams[field.name] = field.value;
		});
		return tparams;
	}
	$table.bootstrapTable("destroy").bootstrapTable({
		clickToSelect : false,
		url : '${root}/statisitc/jgxctjDetail.action',
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 10,
		sortable:true,
		queryParams:options.queryParams,
		pageList : [ 10, 20, 30 ],
		columns : [ {
			field : 'portname',
			title : '<fmt:message key="map.legend.port"/>',
			align:"center",
			sortable:true
		}, {
			field : 'flag',
			title : '<fmt:message key="statics.report.jgxctj.col.jrc"/>',
			formatter : flat,
			align:"center",
			sortable:false
		}, {
			field : 'status',
			title : '<fmt:message key="trip.label.tripStatus"/>',
			formatter : tripstatus,
			align:"center",
			sortable:true
		}, {
			field : 'ctime',
			align:"center",
			title : '<fmt:message key="statics.report.jgxctj.col.jrc"/><fmt:message key="report.inventory.mode.time"/>',
			formatter : dateformat,
			sortable:true
		}]
	}).bootstrapTable("resetView");
}
function dateformat(v){
	if(!$.isEmptyObject($.trim(v))){
	//	var date=new Date(v);
	//	return date.format("yyyy-MM-dd HH:mm:ss");
		return v;
	}
	return "-";
} 

function tripstatus(v){
	var s="-";
	switch (v) {
	case "0": s="<fmt:message key="trip.report.label.tripStatus.toStart"/>";
		break;
	case "1": s="<fmt:message key="trip.report.label.tripStatus.started"/>";
	break;
	case "2": s="<fmt:message key="trip.report.label.tripStatus.toFinish"/>";
	break;
	case "3": s="<fmt:message key="trip.report.label.tripStatus.finished"/>";
	break;
	default:
		break;
	}
	return s;
}
function flat(v){
	var s="-";
	switch (v) {
	case "0": s="<fmt:message key="statics.report.jgxctj.col.jr"/>";
		break;
	case "1": s="<fmt:message key="statics.report.jgxctj.col.jc"/>";
	break;
	
	default:
		break;
	}
	return s;
}

//查询
function search() {
	var param=$("#searchForm").serializeArray();
	$.ajax({
		type:"post",
		url:root+"/statisitc/jgxctj.action",
		dataType:"json",
		async:false,
		cache:false,
		data:param,
		success:function(v){
			var x=[];
			var y1=[];
			var y2=[];
			for(var i=0;i<v.length;i++){
				x.push(v[i].portname);
				y1.push(v[i].checkins);
				y2.push(v[i].checkouts);
			}
			chart(x,y1,y2);
		},
		error:function(e,v){
			bootbox
			.alert('<fmt:message key="statics.report.cljgtj.fail"/>');
		}
		
	});
}
</script>
</html>