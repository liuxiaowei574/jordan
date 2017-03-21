<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../include/include.jsp"%>
<title><fmt:message key="link.main"/></title>
</head>
<body>
	<%@ include file="../include/left.jsp"%>
	<div class="row site">
		<div class="wrapper-content margint95 margin60">
			<%--导航 --%>
			<c:set var="parentName"><fmt:message key="menu.statistic.analysis"/></c:set>
			<c:set var="pageName"><fmt:message key="statistic.index.user"/></c:set>
			<jsp:include page="../include/navigation2.jsp" >
				<jsp:param value="${root }/statisitc/toList.action" name="parentUrl"/>
				<jsp:param value="${parentName }" name="parentName"/>
				<jsp:param value="${pageName }" name="pageName"/>
			</jsp:include>
			
			<!--过滤条件 -->
			<div class="clearfix"></div>
			<div class="profile profile_box02">
			<div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message key="statics.jsp.condition"/></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
						<div class="form-group col-md-4">
							<label class="col-sm-3 control-label"><fmt:message key="statics.jsp.username"/></label>
							<div class="col-sm-8">
									<select style="/* font-size:10px */" id="userName" name="userName" class="form-control">
									<option  value=""></option>
										<c:forEach var="systemUser" items="${userList}">
											<option value=${systemUser.userName}>${systemUser.userName}</option>
										</c:forEach>
									</select>
					   	      <input type="hidden" id="s_logUserId" name="s_logUserId" />
					   	    </div>
						</div>
						<%-- <%--操作时间，起/止 
						<div class="form-group col-md-5">
							<label class="col-sm-3 control-label"><fmt:message key="statics.jsp.date"/></label>
							<div class="input-group date col-sm-3" id="form_operateStartTime">
								<input type="text" class="form-control" id="s_operateStartTime" name="s_operateStartTime" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
							<label class="col-sm-1 control-label" style="text-align: center;">-</label>
							<div class="input-group date col-sm-3" id="form_operateEndTime">
								<input type="text" class="form-control" id="s_operateEndTime" name="s_operateEndTime" readonly>
								<span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
							</div>
						</div> --%>
						<div class="form-group col-sm-3"></div>
						<div class="form-group col-sm-3">
							<button type="submit" class="btn btn-danger" onclick="search();"><fmt:message key="common.button.query"/></button>
							<button type="reset" class="btn btn-darch" onclick="doRest()"><fmt:message key="common.button.reset"/></button>
						</div>
						<div class="clearfix"></div>
					</form>
				</div>
			</div>
			
			<div class="profile profile_box02">
				<div class="row">
					<div class="col-sm-12">
						<div class="panel panel-default">
							<div class="panel-heading">
								<div class="Features pull-right">
									<ul>
										<li><a href="${root}/statisitc/toList.action" class="btn btn-info"><fmt:message key="common.button.back"/></a></li>
									</ul>
								</div>
								<fmt:message key="statistic.sub.user"/>
							</div>
							<div class="panel-body" id="userActiveDiv">
							</div>
						</div>
					</div>
				</div>
			</div>
	        
			<!--/search form-->
			<!--my result-->
			<div class="tab-content">
			  <div class="tab-cotent-title">
				<fmt:message key="statics.jsp.result"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="yonghuhuoyueduReport"></table>
					</div>
					
				</div>
			</div>
		</div>
		</div>
	</div>
</body>
<script src="${root}/static/js/echarts/echarts.min.js"></script>
<script type="text/javascript">

var selections = [];
var $table = $('#yonghuhuoyueduReport');

$(function() {
	//刷新tale
	$(window).resize(function(){
		$table.bootstrapTable("resetView");
	});
	
	//初始化表格数据
	createtable();
	
	/* //初始化日期组件
	$("#form_operateStartTime, #form_operateEndTime").datetimepicker({
		format: "yyyy-mm-dd hh:ii",//ii:分钟
		autoclose: true,
		todayBtn: true,
		language: 'en'
	}).on('changeDate', function(ev){
	    var startTime = $('#s_operateStartTime').val();//获得开始时间
	    $('#form_operateEndTime').datetimepicker('setStartDate', startTime);//设置结束时间（大于开始时间）
	}); */
	
	//初始化图表数据
	search();
});

//统计图表生成函数
function chart(x,y){
	var userActiveDiv = $("#userActiveDiv")[0];
	var userActiveCharts = echarts.init(userActiveDiv);
	userActiveOption = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		        data:['time(Min)']
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
		            name:'time(Min)',
		            type:'bar',
		            data:y
		        }
		    ]
		};
	userActiveCharts.setOption(userActiveOption);
	//统计图表点击回调函数
	userActiveCharts.on('click',function(params){
		$("#userName").val(params.name);//条件查询输入框
		createtable();//刷新表格列表
	});
};

//创建表格函数
function createtable(){
	
	//按条件查询表格数据
	var options = $table.bootstrapTable('getOptions');
	options.queryParams = function(tparams) {
        //遍历form 组装json  
        $.each($("#searchForm").serializeArray(), function(i, field) {  
            console.info(field.name + ":" + field.value + " ");  
            //可以添加提交验证                   
            tparams[field.name] = field.value;  
        });  
        return tparams;  
    } 
	$table.bootstrapTable('refresh');
	
	$table.bootstrapTable({
		clickToSelect : true,
		url : '${root}/statisitc/userOnline.action',
		pagination : true,
		//sidePagination : 'server',
		//pageNumber : 1,
		pageSize : 10,//前端进行分页
		sortable:true,
		maintainSelected : true,
		columns : [ {
			field : 'userName',/* 后面需要改成表的字段(sql关联表查询) */
			title : $.i18n.prop('yhhyd.jsp.username'),
			sortable:true
		}, {
			field : 'onlineTime',
			title : $.i18n.prop('yhhyd.jsp.loadtimes'),
			sortable:true
		}],
	});
	
};

//查询功能
function search(){ 
	
	//按条件查询表格数据
    var params = $table.bootstrapTable('getOptions');
	params.queryParams = function(params) {
        //遍历form 组装json  
        $.each($("#searchForm").serializeArray(), function(i, field) {  
            console.info(field.name + ":" + field.value + " ");  
            //可以添加提交验证                   
            params[field.name] = field.value;  
        });  
        return params;  
    } 
	$table.bootstrapTable('refresh');  
	
	//按条件查询统计表格数据
	var param=$("#searchForm").serializeArray();
	$.ajax({
		type:"post",
		//url:root+"/statisitc/countUserOnline.action",
		url:root+"/statisitc/userOnline.action",
		dataType:"json",
		async:false,
		cache:false,
		data:param,
		success:function(v){
			var x=[];
			var y=[];
			for(var i=0;i<v.length;i++){
				x.push(v[i].userName);
				y.push(v[i].onlineTime);
			}
			chart(x,y);
		},
		error:function(e,v){
			bootbox
			.alert('<fmt:message key="statics.report.cljgtj.fail"/>');
		}
		
	});
}

//查询条件重置
function doRest(){
	$("#searchForm")[0].reset();
}
</script>
</html>