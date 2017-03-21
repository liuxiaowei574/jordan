<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../include/include.jsp"%>
<title>${pageName }</title>
</head>
<body>
	<%@ include file="../include/left.jsp"%>
	<div class="row site">
		<div class="wrapper-content margint95 margin60">
			<%--导航 --%>
			<c:set var="parentName"><fmt:message key="menu.statistic.analysis"/></c:set>
			<c:set var="pageName"><fmt:message key="statistic.index.country"/></c:set>
			<jsp:include page="../include/navigation2.jsp" >
				<jsp:param value="${root }/statisitc/toList.action" name="parentUrl"/>
				<jsp:param value="${parentName }" name="parentName"/>
				<jsp:param value="${pageName }" name="pageName"/>
			</jsp:include>
			
			<!-- 过滤条件 -->
			<div class="clearfix"></div>
			<div class="profile profile_box02">
	        <div class="tab-content m-b">
			  <div class="tab-cotent-title"><fmt:message key="statics.jsp.condition"/></div>
			  	<div class="search_form">
					<form class="form-horizontal row" id="searchForm" onsubmit="return false;">
					    <!-- 过滤表单 -->
						<div class="form-group col-md-5">
							<label class="col-sm-4 control-label"><fmt:message key="statics.jsp.guojiagongsi"/></label>
							<div class="col-sm-8">
					   	      <input type="text" class="form-control input-sm" id="s_organizationName" name="s_organizationName">
					   	      <input type="hidden" id="s_logUserId" name="s_logUserId" />
					   	    </div>
						</div>
						<div class="form-group col-sm-3"></div>
						<div class="form-group col-sm-4">
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
								<fmt:message key="statistic.sub.country"/>
							</div>
							<div class="panel-body" id="countryDiv">
							</div>
						</div>
						
					</div>
				</div>
			</div>
			
			<!--my result-->
			<div class="tab-content">
			  <div class="tab-cotent-title">
				<fmt:message key="statics.jsp.result"/>
			  </div>
			  	<div class="search_table">
					<div>
						<table id="duijieguojiaReport"></table>
					</div>
				</div>
			</div>
		</div>
		</div>
	</div>
</body>
<script src="${root}/static/js/echarts/echarts.min.js"></script>
<script type="text/javascript">

//表格展示：
var selections = [];
var $table = $('#duijieguojiaReport');
//刷新tale
$(window).resize(function(){
	$table.bootstrapTable("resetView");
});

//统计图定义

function chart(x,y){
	var countryDiv = $("#countryDiv")[0];
	var countryCharts = echarts.init(countryDiv);
	countryOption = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		        data:['number']
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
		           // data : ['国家总数'],
		            data : x,
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : [
		        {
		            name:'number',
		            type:'bar',
		            data: y,
		        }
		    ]
		};
	countryCharts.setOption(countryOption);
};

 
//统计图表请求数据
 $(function() {
	//var param=$("#searchForm").serializeArray();
	$.ajax({
		type:"post",
		url:root+"/statisitc/countCountry.action",
		dataType:"json",
		async:false,
		cache:false,
		//data:param,
		success:function(v){
			var x=[];
			var y=[];
			for(var i=0;i<v.length;i++){
				//x.push(["国家总数"]);
				x.push(['<fmt:message key="statics.report.guojiatongji.countCountry"/>']);
				y.push(v[i].number);
			}
			chart(x,y);			
		},
		error:function(e,v){
			bootbox
			.alert('<fmt:message key="statics.report.cljgtj.fail"/>');
		}	
	});
	
	//暂时隐藏柱状图
	$("#countryDiv").hide();
}); 

//国家信息列表
$(function() {
	
	$table.bootstrapTable({
		clickToSelect : false,
		url : '${root}/statisitc/duijieguojiaReport.action',
		pagination : true,
		sidePagination : 'server',
		pageNumber : 1,
		pageSize : 10,
		sortable:true,
		pageList : [ 10, 20, 30 ],
		columns : [ {
			field : 'organizationName',/* 后面需要改成表的字段(sql关联表查询) */
			title : $.i18n.prop('gjgstj.jsp.country'),
			sortable:true
		}, {
			field : 'organizationShort',
			title : $.i18n.prop('Organization.shortName'),
			sortable:true
		}, {
			field : 'longitude',
			title : $.i18n.prop('Organization.longitude'),
			sortable:true
		}, {
			field : 'latitude',
			title : $.i18n.prop('Organization.latitude'),
			sortable:true
		}],		
	});
	
});

//条件查询
function search(){
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
}

//查询条件重置
function doRest(){
	$("#searchForm")[0].reset();
}
</script>
</html>